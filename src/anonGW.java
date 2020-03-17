import java.net.ServerSocket;
import java.net.Socket;

public class anonGW {

    private String protected_server;
    private String protected_server_port;

    public anonGW(String server, String port){
        this.protected_server = server;
        this.protected_server_port = port;
    }

    public void start(){
        try {
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(protected_server_port));
            Socket clientSocket = serverSocket.accept();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
