package Handler.Pipe;

import AnonGWUDP.AnonGWSocket;

import java.net.Socket;

public class AnonGWtoTCP extends Thread {
    private final AnonGWSocket in;
    private final Socket out;

    public AnonGWtoTCP(AnonGWSocket in, Socket out){
        this.in = in;
        this.out = out;
    }

    public void run() {
        try {
            while (true)
                out.getOutputStream().write(in.read());
        } catch (Exception e) {}
    }
}
