package Encryption;

import java.math.BigInteger;
import java.util.Random;

public class KeyPair {
    public final int privateKey;
    public final long publicKey;

    public KeyPair(){
        privateKey = Math.abs(new Random().nextInt());
        publicKey = PublicValues.prime
                .modPow(BigInteger.valueOf(privateKey), PublicValues.n)
                .longValue();
    }

}
