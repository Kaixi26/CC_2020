package AnonGWUDP;

public class AnonGWHeaderDataPair {

    AnonGWHeader anonGWHeader;
    byte[] data;

    AnonGWHeaderDataPair(AnonGWHeader anonGWHeader, byte[] data){
        this.anonGWHeader = anonGWHeader;
        this.data = data;
    }

    public AnonGWHeader getAnonGWHeader() {
        return anonGWHeader;
    }

    public byte[] getData() {
        return data;
    }
}
