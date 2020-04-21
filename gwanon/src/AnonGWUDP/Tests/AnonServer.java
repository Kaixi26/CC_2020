package AnonGWUDP.Tests;

import AnonGWUDP.AnonGWConnection;
import AnonGWUDP.AnonGWServerSocket;
import AnonGWUDP.AnonGWSocket;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class AnonServer {

    static Map<Long, AnonGWSocket> socks = new HashMap<>();

    public static void main(String[] args){
        try {
            AnonGWServerSocket ssock = new AnonGWServerSocket(8888);
            Thread packetRecieved = new Thread(new Runnable() {
                public void run() {
                    while(true) try {ssock.recievePacket();} catch (Exception e) {e.printStackTrace();}
                }
            });
            packetRecieved.setDaemon(true);
            packetRecieved.start();
            System.out.println("Packet reciever started.");

            Thread socketHandler = new Thread(new Runnable() {
                public void run() {
                    long next = 0;
                    while(true) {
                        try {
                            long thisID = next;
                            AnonGWSocket tmp = ssock.accept();
                            Thread thread = new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        while (!tmp.isClosed()) {
                                            System.out.println("Recieved : " + new String(tmp.read()));
                                        }
                                    } catch (Exception e){}
                                    System.out.println("Socket " + thisID + " closed.");
                                }
                            });
                            thread.setDaemon(true);
                            thread.start();
                            System.out.println("Socket " + thisID + " started.");
                            socks.put(next++, tmp);
                        } catch (Exception e){}
                    }
                }
            });
            socketHandler.setDaemon(true);
            socketHandler.start();
            System.out.println("Socket handler started.");

            Scanner scanner = new Scanner(System.in);

            String cmd = scanner.nextLine();
            while (!cmd.equals("q")){
                long id = Long.parseLong(cmd.substring(0, cmd.indexOf(' ')));
                socks.get(id).send(cmd.substring(cmd.indexOf(' ') + 1));
                cmd = scanner.nextLine();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
