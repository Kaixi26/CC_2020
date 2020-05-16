package AnonGWUDP;

import Encryption.SharedKey;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

public class AnonGWSocket {
    final AnonGWServerSocket parent;
    final DatagramSocket datagramSocket;
    final AnonGWConnection connection;
    AnonGWOutInfo outInfo;
    final long id;
    boolean closed = false;
    SharedKey key;

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
        byte[] encData = key.encryptWith(data);
        ByteBuffer bbuf = ByteBuffer.allocate(AnonGWHeader.headerLength() + encData.length);
        bbuf.put(new AnonGWHeader(AnonGWHeaderType.DATA, outInfo.id, encData.length).serialize());
        bbuf.put(encData);
        byte[] buf = bbuf.array();
        DatagramPacket datagramPacket
                = new DatagramPacket(buf, buf.length, outInfo.address, outInfo.port);
        datagramSocket.send(datagramPacket);
        connection.awaitType(AnonGWHeaderType.DATA_ACK);
    }

    /* TODO send ack (?) */
    public byte[] read() throws IOException, AnonGWClosedException {
        byte[] encData = connection.awaitType(AnonGWHeaderType.DATA).getData();
        byte[] ret = key.decryptWith(encData);
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

    public void setKey(SharedKey key) {
        this.key = key;
    }
}
