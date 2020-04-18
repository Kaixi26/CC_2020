package Handler;

import Settings.ImmutableSettings;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionHandler extends Thread {
    private final Socket csock;
    private final Socket ssock;
    private final ImmutableSettings settings;

    public ConnectionHandler(ImmutableSettings settings, Socket sock) throws IOException {
        this.csock = sock;
        this.settings = settings;
        ssock = new Socket(settings.getServerIP(), settings.getServerTcpPort());
    }

    public void run() {
        try {
            InputStream cin = csock.getInputStream();
            InputStream sin = ssock.getInputStream();
            OutputStream cout = csock.getOutputStream();
            OutputStream sout = ssock.getOutputStream();
            Thread cthread = new ClientHandler(cin, sout);
            Thread sthread = new ServerHandler(sin, cout);
            cthread.start();
            sthread.start();
            cthread.join();
            sthread.join();
            closeSockets();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    private void closeSockets(){
        try {
            csock.shutdownOutput();
            csock.shutdownInput();
            csock.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            ssock.shutdownOutput();
            ssock.shutdownInput();
            ssock.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
