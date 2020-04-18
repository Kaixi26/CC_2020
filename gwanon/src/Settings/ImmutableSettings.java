package Settings;

public class ImmutableSettings {
    protected String serverIP;
    protected int tcpPort;
    protected int serverTcpPort;

    public int getServerTcpPort() {
        return serverTcpPort;
    }

    public String getServerIP() {
        return serverIP;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    protected void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    protected void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    protected void setServerTcpPort(int serverTcpPort) {
        this.serverTcpPort = serverTcpPort;
    }
}
