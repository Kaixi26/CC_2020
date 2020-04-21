package AnonGWUDP;

import java.nio.ByteBuffer;

public class AnonGWHeader {

    final byte type;
    final long id;
    final long datalength;

    public AnonGWHeader(byte type, long id, long datalength){
        this.type = type;
        this.id = id;
        this.datalength = datalength;
    }

    static int headerLength(){
        return 1 + Long.BYTES + Long.BYTES;
    }

    public byte[] serialize(){
        ByteBuffer buffer = ByteBuffer.allocate(headerLength());
        buffer.put(type);
        buffer.putLong(id);
        buffer.putLong(datalength);
        return buffer.array();
    }

    public static AnonGWHeader parse(byte[] bytes){
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        byte type = buffer.get();
        long id = buffer.getLong();
        long datalength = buffer.getLong();
        return new AnonGWHeader(type, id, datalength);
    }

    public boolean equals(AnonGWHeader h){
        return this.type == h.type
            && this.id == h.id
            && this.datalength == h.datalength;
    }

}
