package Handler.Pipe;

import AnonGWUDP.AnonGWSocket;

import java.net.Socket;

public class AnonGWtoAnonGW extends Thread {
    private final AnonGWSocket in;
    private final AnonGWSocket out;

    public AnonGWtoAnonGW(AnonGWSocket in, AnonGWSocket out){
        this.in = in;
        this.out = out;
    }

    public void run() {
        try {
            while (true)
                out.send(in.read());
        } catch (Exception e) {}
    }
}
