import Handler.ClientHandler;
import Handler.ConnectionHandler;
import Handler.ServerHandler;
import Settings.ImmutableSettings;
import Settings.Settings;

import java.net.ServerSocket;

public class Main {

    private static ImmutableSettings parseArgs(String[] args){
        Settings settings = new Settings();
        final String regex_ip_field = "((25[0-5])|(2[0-4][0-9])|(1[0-9][0-9])|([1-9][0-9])|([0-9]))";
        final String regex_ip = "((" + regex_ip_field + "\\.){3}" + regex_ip_field + ")";
        for(int i=0; i<args.length; i++) {
            if (args[i].matches("--port=\\d+")) {
                settings.tcpPort = Integer.parseInt(args[i].substring(7));
                System.out.println("Port set to " + Integer.parseInt(args[i].substring(7)) + ".");
            } else if (args[i].matches("--server-port=\\d+")) {
                settings.serverTcpPort = Integer.parseInt(args[i].substring(14));
                System.out.println("Server port set to " + Integer.parseInt(args[i].substring(14)) + ".");
            } else if (args[i].matches("--server-ip=" + regex_ip)) {
                settings.serverIP = args[i].substring(12);
                System.out.println("Server ip set to " + args[i].substring(12) + ".");
            } else {
                System.out.println("Invalid argument '" + args[i] + "'.");
            }
        }
        return settings.getImmutableSettings();
    }

    public static void main(String[] args) {
        ImmutableSettings settings = parseArgs(args);
        try {
            ServerSocket sock = new ServerSocket(settings.getTcpPort());
            while (true) {
                new ConnectionHandler(settings, sock.accept()).start();
                System.out.println("Accepted new connection.");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
