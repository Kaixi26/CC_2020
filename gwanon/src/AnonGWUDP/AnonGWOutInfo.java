package AnonGWUDP;

import java.net.InetAddress;

public class AnonGWOutInfo {
    final InetAddress address;
    final int port;
    final long id;
    final long externPublicKey;
    final int hops;

    AnonGWOutInfo(InetAddress address, int port, long id, long externPublicKey, int hops){
        this.address = address;
        this.port = port;
        this.id = id;
        this.externPublicKey = externPublicKey;
        this.hops = hops;
    }
}
