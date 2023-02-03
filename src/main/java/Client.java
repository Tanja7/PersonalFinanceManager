import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final int PORT = 8989;
    private static final String HOST = "127.0.0.1";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ) {
            System.out.println(in.readLine());
            String input = scanner.nextLine();
            String[] parts = input.split(", ");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", parts[0]);
            jsonObject.put("date", parts[1]);
            jsonObject.put("sum", parts[2]);
            out.println(jsonObject);
            System.out.println(in.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
