
package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Base64;
import java.util.Scanner;

public class Client {

    public static void main(String argv[]) throws Exception {

        Socket clientSocket = new Socket("localhost", 2006);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        RSAEncryption encryption = new RSAEncryption("clepubliqueserv");
        RSAUtil.initialiserCles("clientclepublique", "clientcleprivee");
        byte[] clePublique = RSAUtil.lireFichier("clientclepublique");
        String dank = Base64.getEncoder().encodeToString(clePublique);
        outToServer.writeBytes(dank + '\n');
        String phrase = inFromServer.readLine();
        
        System.out.println(phrase);
        System.out.println(encryption.authentifierSignature(phrase, dank));
        
        
        RSADecryption decryption = new RSADecryption("clientcleprivee");
        Scanner clavier = new Scanner(System.in);
        while(true)
        {
            String ligne = clavier.nextLine();
            if(ligne.isEmpty()) break;
            outToServer.writeBytes(encryption.encrypter(ligne) + "\n");
            double t1 = System.currentTimeMillis();
             String ree2 = decryption.decrypter(inFromServer.readLine());
             double t2 = System.currentTimeMillis();
             System.out.println(ree2);
             System.out.println(t2 - t1);
            
        }
        clientSocket.close();
    }
}
