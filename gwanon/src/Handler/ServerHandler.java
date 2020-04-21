package Handler;


import java.io.InputStream;
import java.io.OutputStream;

public class ServerHandler extends Thread {

    InputStream sin;
    OutputStream cout;

    public ServerHandler(InputStream sin, OutputStream cout){
        this.sin = sin;
        this.cout = cout;
    }

    public void run() {
        try{
            byte[] buf = new byte[1024];
            int rd;
            while((rd = sin.read(buf)) > 0){
                System.out.print("Read from server: ");
                for(int i=0; i<rd; i++) System.out.print(buf[i]+"");
                System.out.print("\n");
                cout.write(buf, 0, rd);
            }
            System.out.println("Closing client handler.");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
