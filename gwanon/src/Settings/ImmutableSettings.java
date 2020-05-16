package Settings;

import java.util.ArrayList;
import java.util.List;

public class ImmutableSettings {
    protected String serverIP;
    protected int tcpPort;
    protected int udpPort;
    protected int serverTcpPort;
    protected int hops;
    protected List<UDPIdentifier> gateways;

    public int getServerTcpPort() {
        return serverTcpPort;
    }

    public String getServerIP() {
        return serverIP;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public int getHops() {
        return hops;
    }

    public List<UDPIdentifier> getGateways() {
        return new ArrayList<UDPIdentifier>(gateways);
    }

}
