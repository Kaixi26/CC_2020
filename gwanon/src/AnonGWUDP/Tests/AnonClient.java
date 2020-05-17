package AnonGWUDP.Tests;

import AnonGWUDP.AnonGWHeader;
import AnonGWUDP.AnonGWHeaderType;
import AnonGWUDP.AnonGWServerSocket;
import AnonGWUDP.AnonGWSocket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class AnonClient {
    public static void main(String[] args){
        try {
            AnonGWServerSocket ssock = new AnonGWServerSocket(8889);
            Thread packetReciever = new Thread(new Runnable() {
                public void run() {
                    while(!Thread.currentThread().isInterrupted())
                        try {
                            ssock.recievePacket();
                            System.out.println("Packet recieved.");
                        } catch (Exception e) {e.printStackTrace();}
                }
            });
            packetReciever.setDaemon(true);
            packetReciever.start();
            AnonGWSocket sock =  ssock.connect(InetAddress.getByName("127.1.1.1"), 8888, 1);
            System.out.println("Connection established.");
            sock.send("Hello.");
            System.out.println("Sent.");
            System.out.println("Recieved : " + new String(sock.read()));

            sock.close();
            System.out.println("Bye");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
