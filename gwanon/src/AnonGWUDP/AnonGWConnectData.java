package AnonGWUDP;

import java.nio.ByteBuffer;

public class AnonGWConnectData {

    final long id;
    final int hops;
    final long publicKey;

    AnonGWConnectData(long id, int hops, long publicKey){
        this.id = id;
        this.hops = hops;
        this.publicKey = publicKey;
    }
    static int length(){
        return Integer.BYTES + Long.BYTES*2;
    }

    public byte[] serialize(){
        ByteBuffer buffer = ByteBuffer.allocate(length());
        buffer.putLong(id);
        buffer.putInt(hops);
        buffer.putLong(publicKey);
        return buffer.array();
    }

    public static AnonGWConnectData parse(byte[] bytes){
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        long id = buffer.getLong();
        int hops = buffer.getInt();
        long publicKey = buffer.getLong();
        return new AnonGWConnectData(id, hops, publicKey);
    }

}
