package AnonGWUDP;

import Encryption.KeyPair;
import Encryption.SharedKey;

import javax.imageio.IIOException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.security.Key;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AnonGWServerSocket {

    Lock l = new ReentrantLock();
    Condition acceptCond = l.newCondition();
    DatagramSocket datagramSocket;
    long next = 0;
    Map<Long, AnonGWSocket> sockets = new HashMap<>();
    List<AnonGWOutInfo> unassigned = new ArrayList<>();
    KeyPair keys = new KeyPair();

    public AnonGWServerSocket(int port) throws SocketException {
        datagramSocket = new DatagramSocket(port);
    }

    /* TODO */
    public void recievePacket() throws IOException {
        byte[] buf = new byte[4096];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        datagramSocket.receive(packet);
        l.lock();
        try {
            AnonGWHeader header = AnonGWHeader.parse(packet.getData());
            if (header.type == AnonGWHeaderType.CONNECT) {
                AnonGWConnectData connectData = AnonGWConnectData.parse(
                    Arrays.copyOfRange(packet.getData(), AnonGWHeader.headerLength(), AnonGWHeader.headerLength() + (int)header.datalength));
                unassigned.add(new AnonGWOutInfo(packet.getAddress(), packet.getPort(), connectData.id, connectData.publicKey, connectData.hops));
                acceptCond.signalAll();
            } else if(header.type == AnonGWHeaderType.CLOSE){
                AnonGWSocket socket = sockets.get(header.id);
                sockets.remove(header.id);
                if(socket != null) socket.close();
            } else {
                sockets.get(header.id).connection.add(new AnonGWHeaderDataPair(header,
                        Arrays.copyOfRange(packet.getData(), AnonGWHeader.headerLength(), AnonGWHeader.headerLength() + (int)header.datalength)));
            }
        } finally {
            l.unlock();
        }
    }

    public AnonGWSocket accept() throws IOException {
        l.lock();
        try {
            while(unassigned.isEmpty())
                try {acceptCond.await();} catch (Exception e){}
            AnonGWOutInfo outInfo = unassigned.get(0);
            unassigned.remove(0);
            AnonGWConnection connection = new AnonGWConnection();
            ByteBuffer bbuf = ByteBuffer.allocate(AnonGWHeader.headerLength() + AnonGWConnectData.length());
            bbuf.put(new AnonGWHeader(AnonGWHeaderType.CONNECT_ACK, outInfo.id, AnonGWConnectData.length()).serialize());
            bbuf.put(new AnonGWConnectData(next, 0, keys.publicKey).serialize());
            DatagramPacket datagramPacket
                    = new DatagramPacket(bbuf.array(), bbuf.array().length, outInfo.address, outInfo.port);
            datagramSocket.send(datagramPacket);
            AnonGWSocket ret = new AnonGWSocket(this, connection, outInfo, next);
            ret.setKey(new SharedKey(keys.privateKey, outInfo.externPublicKey));
            sockets.put(next, ret);
            return ret;
        } finally {
            next++;
            l.unlock();
        }
    }

    public AnonGWSocket connect(InetAddress address, int port, int hops) throws IOException, AnonGWClosedException {
        l.lock();
        try {
            long connectionId = next++;
            ByteBuffer bbuf = ByteBuffer.allocate(AnonGWHeader.headerLength() + AnonGWConnectData.length());
            bbuf.put(new AnonGWHeader(AnonGWHeaderType.CONNECT, connectionId, AnonGWConnectData.length()).serialize());
            bbuf.put(new AnonGWConnectData(connectionId, hops, keys.publicKey).serialize());
            DatagramPacket req = new DatagramPacket(bbuf.array(), bbuf.array().length, address, port);
            datagramSocket.send(req);
            AnonGWConnection connection = new AnonGWConnection();
            AnonGWSocket ret = new AnonGWSocket(this, connection, connectionId);
            sockets.put(connectionId, ret);
            l.unlock();
            AnonGWHeaderDataPair hdp = connection.awaitType(AnonGWHeaderType.CONNECT_ACK);
            l.lock();
            AnonGWConnectData connectData = AnonGWConnectData.parse(hdp.getData());
            ret.setOutInfo(new AnonGWOutInfo(address, port, connectData.id, connectData.publicKey, hops-1));
            ret.setKey(new SharedKey(keys.privateKey, connectData.publicKey));
            return ret;
        } finally {
            l.unlock();
        }
    }

}
