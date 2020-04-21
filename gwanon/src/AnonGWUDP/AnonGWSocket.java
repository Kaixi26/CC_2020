package AnonGWUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;

public class AnonGWSocket {
    final AnonGWServerSocket parent;
    final DatagramSocket datagramSocket;
    final AnonGWConnection connection;
    AnonGWOutInfo outInfo;
    final long id;
    boolean closed = false;

    AnonGWSocket(AnonGWServerSocket parent, AnonGWConnection anonGWConnection, long id){
        this.parent = parent;
        this.datagramSocket = parent.datagramSocket;
        this.connection = anonGWConnection;
        this.id = id;
    }

    AnonGWSocket(AnonGWServerSocket parent, AnonGWConnection anonGWConnection, AnonGWOutInfo outInfo, long id){
        this.parent = parent;
        this.datagramSocket = parent.datagramSocket;
        this.connection = anonGWConnection;
        this.outInfo = outInfo;
        this.id = id;
    }

    public void send(String data) throws IOException, AnonGWClosedException {
        send(data.getBytes());
    }

    /* TODO: SPLIT PACKETS IN SMALL SYZES ? */
    public void send(byte[] data) throws IOException, AnonGWClosedException {
        ByteBuffer bbuf = ByteBuffer.allocate(AnonGWHeader.headerLength() + data.length);
        bbuf.put(new AnonGWHeader(AnonGWHeaderType.DATA, outInfo.id, data.length).serialize());
        bbuf.put(data);
        byte[] buf = bbuf.array();
        DatagramPacket datagramPacket
                = new DatagramPacket(buf, buf.length, outInfo.address, outInfo.port);
        datagramSocket.send(datagramPacket);
        connection.awaitType(AnonGWHeaderType.DATA_ACK);
    }

    /* TODO send ack (?) */
    public byte[] read() throws IOException, AnonGWClosedException {
        byte[] ret = connection.awaitType(AnonGWHeaderType.DATA).getData();
        byte[] buf = new AnonGWHeader(AnonGWHeaderType.DATA_ACK, outInfo.id, 0).serialize();
        datagramSocket.send(new DatagramPacket(buf, buf.length, outInfo.address, outInfo.port));
        return ret;
    }

    public void close() throws IOException {
        byte[] buf = new AnonGWHeader(AnonGWHeaderType.CLOSE, outInfo.id, 0).serialize();
        datagramSocket.send(new DatagramPacket(buf, buf.length, outInfo.address, outInfo.port));
        try {
            connection.l.lock();
            connection.closed = true;
            connection.cpacket.signalAll();
        } finally {
            connection.l.unlock();
        }
        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setOutInfo(AnonGWOutInfo outInfo) {
        this.outInfo = outInfo;
    }
}
