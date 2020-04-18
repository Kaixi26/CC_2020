package Settings;

public class Settings {
    public String serverIP = "127.1.1.1";
    public int tcpPort = 80;
    public int serverTcpPort = 80;

    public final ImmutableSettings getImmutableSettings(){
        ImmutableSettings settings = new ImmutableSettings();
        settings.setServerIP(serverIP);
        settings.setTcpPort(tcpPort);
        settings.setServerTcpPort(serverTcpPort);
        return settings;
    }
}
