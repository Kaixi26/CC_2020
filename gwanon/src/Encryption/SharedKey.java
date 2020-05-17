package Encryption;

import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Base64;

public class SharedKey {

    final long lkey;
    SecretKeySpec pkey;

    public SharedKey(long privateKey, long externPrivateKey){
        lkey = BigInteger.valueOf(externPrivateKey)
                .modPow(BigInteger.valueOf(privateKey), PublicValues.n)
                .longValue();

        try {
            byte[] key = String.valueOf(lkey).getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            pkey = new SecretKeySpec(Arrays.copyOf(sha.digest(key), 16), "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] encryptWith(byte[] buf){
        return Encryption.encrypt(buf, pkey);
    }

    public byte[] decryptWith(byte[] buf){
        return Encryption.decrypt(buf, pkey);
    }

}
