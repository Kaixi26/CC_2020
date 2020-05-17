package Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ImmutableSettings {
    protected String serverIP;
    protected int tcpPort;
    protected int udpPort;
    protected int serverTcpPort;
    protected int hops;
    protected List<UDPIdentifier> gateways;
    private Random random = new Random();

    public int getServerTcpPort() {
        return serverTcpPort;
    }

    public String getServerIP() {
        return serverIP;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public int getHops() {
        return hops;
    }

    public List<UDPIdentifier> getGateways() {
        return new ArrayList<UDPIdentifier>(gateways);
    }

    public UDPIdentifier randomGateway(){
        return gateways.get(Math.abs(random.nextInt()) % gateways.size());
    }

}
