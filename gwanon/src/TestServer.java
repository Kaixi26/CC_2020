import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TestServer {

    private static List<Socket> sockets = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12346);
            new Thread(new Runnable() {
                public void run() {
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                        while (true) {
                            String str = in.readLine();
                            PrintWriter out = new PrintWriter(new OutputStreamWriter(
                                    sockets.get(Integer.parseInt(str.substring(0, 1))).getOutputStream()));
                            out.print(str);
                            out.flush();
                        }
                    } catch (Exception e){

                    }
                }
            }).start();

            while(true){
                Socket sock = serverSocket.accept();
                int id = sockets.size();
                sockets.add(sock);
                new Thread(new Runnable() {
                    public void run() {
                        System.out.println("Opened socket " + id);
                        try {
                            BufferedReader is = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                            while (true){
                                System.out.println("[" + id + "]" + is.readLine());
                                System.out.flush();
                            }
                        } catch (Exception e){
                        }
                    }
                }).start();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
