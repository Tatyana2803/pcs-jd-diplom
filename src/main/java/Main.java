import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws Exception {

        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        //System.out.println(engine.search("бизнес"));

        try (ServerSocket serverSocket = new ServerSocket(8989);
             Socket clientSocket = serverSocket.accept();
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String word = in.readLine();

            List<PageEntry> list = engine.search(word.toLowerCase(Locale.ROOT));
            String jsonStr = new GsonBuilder().setPrettyPrinting().create().toJson(list);
            out.println(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}