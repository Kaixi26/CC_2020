package Settings;

import java.util.ArrayList;
import java.util.List;

public class Settings {
    public String serverIP = "127.1.1.1";
    public int tcpPort = 80;
    public int udpPort = 6666;
    public int serverTcpPort = 80;
    public int hops = 1;
    public List<UDPIdentifier> gateways = new ArrayList<UDPIdentifier>();

    public final ImmutableSettings getImmutableSettings(){
        ImmutableSettings settings = new ImmutableSettings();
        settings.serverIP = serverIP;
        settings.tcpPort = tcpPort;
        settings.udpPort = udpPort;
        settings.serverTcpPort = serverTcpPort;
        settings.hops = hops;
        settings.gateways = new ArrayList<UDPIdentifier>(gateways);
        return settings;
    }
}
