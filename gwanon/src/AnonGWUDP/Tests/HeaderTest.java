package AnonGWUDP.Tests;

import AnonGWUDP.AnonGWHeader;
import AnonGWUDP.AnonGWHeaderType;

import java.util.Random;

public class HeaderTest {
    static public void main(String[] args){
        AnonGWHeader[] headers = new AnonGWHeader[5];
        headers[0] = new AnonGWHeader(AnonGWHeaderType.CONNECT  , 0, 0);
        headers[1] = new AnonGWHeader(AnonGWHeaderType.DATA     , 1, 100);
        headers[2] = new AnonGWHeader(AnonGWHeaderType.DATA     , 2, 200);
        headers[3] = new AnonGWHeader(AnonGWHeaderType.CONNECT  , 3, 300);
        headers[4] = new AnonGWHeader(AnonGWHeaderType.CONNECT  , 4, 400);

        int failed = 0;
        for(int i=0; i<headers.length; i++)
            if(!headers[i].equals(AnonGWHeader.parse(headers[i].serialize()))) {
                System.out.println("Failed for " + i + ".");
                failed++;
            }
        if(failed != 0)
            System.out.println("Failed " + failed + "/" + headers.length + ".");
        else System.out.println("All tests passed.");
    }
}
