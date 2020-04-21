package AnonGWUDP;

import java.net.InetAddress;

public class AnonGWOutInfo {
    final InetAddress address;
    final int port;
    final long id;

    AnonGWOutInfo(InetAddress address, int port, long id){
        this.address = address;
        this.port = port;
        this.id = id;
    }
}
