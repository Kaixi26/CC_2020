package Handler.Pipe;

import AnonGWUDP.AnonGWSocket;

import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Base64;

public class TCPtoAnonGW extends Thread {
    private final Socket in;
    private final AnonGWSocket out;

    public TCPtoAnonGW(Socket in, AnonGWSocket out){
        this.in = in;
        this.out = out;
    }

    public void run() {
        try {
            InputStream ins = in.getInputStream();
            byte[] buf = new byte[512];
            int rd;
            while ((rd = ins.read(buf)) > 0) {
                byte[] tmp = Arrays.copyOfRange(buf, 0, rd);
                out.send(tmp);
            }
        } catch (Exception e) {}
    }

}
