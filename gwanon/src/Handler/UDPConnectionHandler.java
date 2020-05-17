package Handler;

import AnonGWUDP.AnonGWServerSocket;
import AnonGWUDP.AnonGWSocket;
import Handler.Pipe.AnonGWtoAnonGW;
import Handler.Pipe.AnonGWtoTCP;
import Handler.Pipe.TCPtoAnonGW;
import Settings.ImmutableSettings;
import Settings.UDPIdentifier;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class UDPConnectionHandler extends Thread {
    private final AnonGWSocket sock;
    private final ImmutableSettings settings;
    private final AnonGWServerSocket anonGWServerSocket;

    public UDPConnectionHandler(ImmutableSettings settings, AnonGWSocket sock, AnonGWServerSocket anonGWServerSocket) throws IOException {
        this.sock = sock;
        this.settings = settings;
        this.anonGWServerSocket = anonGWServerSocket;
    }

    public void run() {
        try {
            if(sock.getHops() < settings.getHops())
                connectNextGateway();
            else
                connectServer();
            Thread.sleep(10000);
            //closeSockets();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void connectNextGateway() throws Exception {
        UDPIdentifier identifier = settings.randomGateway();
        System.out.println("[1] -> (H " + sock.getHops() + ") " + identifier.ip + " " + identifier.port);

        AnonGWSocket nextGw =
                anonGWServerSocket.connect(InetAddress.getByName(identifier.ip), identifier.port, sock.getHops()+1);
        Thread p0 = new AnonGWtoAnonGW(sock, nextGw);
        Thread p1 = new AnonGWtoAnonGW(nextGw, sock);
        p0.start();
        p1.start();
    }

    private void connectServer() throws Exception {
        System.out.println("[2] -> (H " + sock.getHops() + ") " + settings.getServerIP() + " " + settings.getServerTcpPort());
        Socket ssock = new Socket(settings.getServerIP(), settings.getServerTcpPort());
        Thread p0 = new TCPtoAnonGW(ssock, sock);
        Thread p1 = new AnonGWtoTCP(sock, ssock);
        p0.start();
        p1.start();
    }

    private void closeSockets(){
        try {
            sock.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
