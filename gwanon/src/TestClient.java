import java.net.Socket;

public class TestClient {
    public static void main(String[] args) {
        try {
            Socket sock = new Socket("127.0.0.1", 12345);
            sock.shutdownOutput();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
