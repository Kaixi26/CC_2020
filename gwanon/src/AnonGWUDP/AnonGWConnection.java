package AnonGWUDP;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AnonGWConnection {
    final Lock l = new ReentrantLock();
    final Condition cpacket = l.newCondition();
    List<AnonGWHeaderDataPair> packs = new ArrayList<>();
    boolean closed = false;
    //final InetAddress address;
    //final int port;

    AnonGWConnection(){ }

    //AnonGWConnection(InetAddress address, int port){
    //    this.address = address;
    //    this.port = port;
    //}

    /* TODO: signal */
    void add(AnonGWHeaderDataPair hdp){
        l.lock();
        try {
            packs.add(hdp);
            cpacket.signalAll();
        } finally {
            l.unlock();
        }
    }

    AnonGWHeaderDataPair awaitType(byte headertype) throws AnonGWClosedException {
        l.lock();
        try {
            while((packs.size() == 0 || packs.stream().noneMatch(hdp -> hdp.anonGWHeader.type == headertype)) && !closed)
                try {cpacket.await();} catch (Exception e) {}
            if(closed) throw new AnonGWClosedException();
            int i;
            for(i=0; i<packs.size(); i++)
                if(packs.get(i).anonGWHeader.type == headertype)
                    break;
            AnonGWHeaderDataPair ret =  packs.get(i);
            packs.remove(i);
            return ret;
        } finally {
            l.unlock();
        }
    }

}
