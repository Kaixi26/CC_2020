package AnonGWUDP;

import java.nio.ByteBuffer;

public class AnonGWConnectData {

    final long id;
    final int hops;

    AnonGWConnectData(long id, int hops){
        this.id = id;
        this.hops = hops;
    }
    static int length(){
        return Integer.BYTES + Long.BYTES;
    }

    public byte[] serialize(){
        ByteBuffer buffer = ByteBuffer.allocate(length());
        buffer.putLong(id);
        buffer.putInt(hops);
        return buffer.array();
    }

    public static AnonGWConnectData parse(byte[] bytes){
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        long id = buffer.getLong();
        int hops = buffer.getInt();
        return new AnonGWConnectData(id, hops);
    }

}
