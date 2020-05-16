import Handler.ClientHandler;
import Handler.ConnectionHandler;
import Handler.ServerHandler;
import Settings.ImmutableSettings;
import Settings.Settings;
import Settings.UDPIdentifier;

import java.net.ServerSocket;
import java.util.Scanner;

public class Main {

    private static ImmutableSettings parseArgs(String[] args){
        Settings settings = new Settings();
        final String regex_ip_field = "((25[0-5])|(2[0-4][0-9])|(1[0-9][0-9])|([1-9][0-9])|([0-9]))";
        final String regex_ip = "((" + regex_ip_field + "\\.){3}" + regex_ip_field + ")";
        final String regex_ipport = "(" + regex_ip + ":[0-9]*" + ")";
        final String regex_ipports = "(" + regex_ipport + "(;" + regex_ipport + ")*" + ")";
        for(int i=0; i<args.length; i++) {
            if (args[i].matches("--port=\\d+")) {
                settings.tcpPort = Integer.parseInt(args[i].substring(7));
                System.out.println("TCP port set to " + Integer.parseInt(args[i].substring(7)) + ".");
            } else if (args[i].matches("--udp-port=\\d+")) {
                settings.udpPort = Integer.parseInt(args[i].substring(11));
                System.out.println("UDP port set to " + Integer.parseInt(args[i].substring(11)) + ".");
            } else if (args[i].matches("--server-port=\\d+")) {
                settings.serverTcpPort = Integer.parseInt(args[i].substring(14));
                System.out.println("Server port set to " + Integer.parseInt(args[i].substring(14)) + ".");
            } else if (args[i].matches("--hops=\\d+")) {
                settings.hops = Integer.parseInt(args[i].substring(7));
                System.out.println("Hops set to " + Integer.parseInt(args[i].substring(7)) + ".");
            } else if (args[i].matches("--server-ip=" + regex_ip)) {
                settings.serverIP = args[i].substring(12);
                System.out.println("Server ip set to " + args[i].substring(12) + ".");
            } else if (args[i].matches("--gateways=" + regex_ipports )){
                String[] ipports = args[i].substring(11).split(";");
                for(String ipport : ipports) {
                    String[] tmp = ipport.split(":");
                    settings.gateways.add(new UDPIdentifier(tmp[0], Integer.parseInt(tmp[1])));
                    System.out.println("Added gateway " + tmp[0] + ":" + tmp[1] + ".");
                }
            } else {
                System.out.println("Invalid argument '" + args[i] + "'.");
            }
        }
        return settings.getImmutableSettings();
    }

    public static void main(String[] args) {
        ImmutableSettings settings = parseArgs(args);
        Thread tcpThread = tcpThread(settings);
        tcpThread.setDaemon(true);
        tcpThread.start();
        System.out.print("Write 'exit' to close the gateway.\n>> ");
        Scanner scanner = new Scanner(System.in);
        String cmd = scanner.nextLine();
        while (!cmd.equals("quit") && !cmd.equals("exit") && !cmd.equals(":q")){
            System.out.println(cmd);
            System.out.print(">> ");
            cmd = scanner.nextLine();
        }
    }

    private static Thread tcpThread(ImmutableSettings settings){
        return new Thread(new Runnable() {
            public void run() {
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
        });
    }

}
