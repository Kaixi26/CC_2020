package Handler.Old;

import Settings.ImmutableSettings;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class ClientHandler extends Thread {

    InputStream cin;
    OutputStream sout;

    public ClientHandler(InputStream cin, OutputStream sout) throws IOException {
        this.cin = cin;
        this.sout = sout;
    }

    @Override
    public void run() {
        try{
            byte[] buf = new byte[1024];
            int rd;
            while((rd = cin.read(buf)) > 0){
                System.out.print("Read from client: ");
                for(int i=0; i<rd; i++) System.out.print(buf[i]+"");
                System.out.print("\n");
                sout.write(buf, 0, rd);
            }
            System.out.println("Closing client handler.");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
