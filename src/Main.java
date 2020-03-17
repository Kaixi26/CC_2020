import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
	    String target_server="";
	    String port="";
	    int size = args.length;
        List<String> peers = new ArrayList<>();

        if (size<6){
            System.out.println("Erro");
            return;
        }

        if (args[0].equals("target-server"))
            target_server = args[1];
        else{
            System.out.println("Erro");
            return;
        }

        if (args[2].equals("port"))
            port=args[3];
        else{
            System.out.println("Erro");
            return;
        }

        if(args[4].equals("overlay-peers")){

            for (int i = 5; i < size ; i++){
                peers.add(args[i]);
            }

        }

        else{
            System.out.println("Erro");
            return;
        }



        System.out.println(target_server + " // " + port + " // " + peers.toString());
    }

}
