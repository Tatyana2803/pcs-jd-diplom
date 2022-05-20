import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {

        String word = "Бизнес";

        try (Socket clientSocket = new Socket("localhost", 8989);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            out.println(word);
            in.lines().forEach(System.out::println);
//            String resp = in.readLine();
//            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



