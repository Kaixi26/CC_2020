package Handler;

import AnonGWUDP.AnonGWServerSocket;
import AnonGWUDP.AnonGWSocket;
import Handler.Pipe.AnonGWtoTCP;
import Handler.Pipe.TCPtoAnonGW;
import Settings.ImmutableSettings;
import Settings.UDPIdentifier;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class TCPConnectionHandler extends Thread {
    private final Socket sock;
    private final AnonGWServerSocket anonGWServerSocket;
    private final ImmutableSettings settings;

    public TCPConnectionHandler(ImmutableSettings settings, Socket sock, AnonGWServerSocket anonGWServerSocket) throws IOException {
        this.sock = sock;
        this.settings = settings;
        this.anonGWServerSocket = anonGWServerSocket;
    }

    public void run() {
        try {
            UDPIdentifier identifier = settings.randomGateway();
            System.out.println("[0] -> " + identifier.ip + " " + identifier.port);
            AnonGWSocket anonGWSocket = anonGWServerSocket.connect(InetAddress.getByName(identifier.ip), identifier.port, 1);
            Thread p0 = new TCPtoAnonGW(sock, anonGWSocket);
            Thread p1 = new AnonGWtoTCP(anonGWSocket, sock);
            p0.start();
            p1.start();

            Thread.sleep(10000);
            //closeSockets();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void closeSockets(){
        try {
            sock.shutdownOutput();
            sock.shutdownInput();
            sock.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
