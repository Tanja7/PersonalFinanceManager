import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    //серверное мавен-приложение, играющее роль менеджера личных финансов
    private static final int PORT = 8989;

    public static void main(String[] args) throws Exception {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) { // стартуем сервер
            System.out.println("Server started");
            CategorizingPurchases categorizingPurchases = new CategorizingPurchases();
            while (true) { //  принимаем подключения
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                ) {
                    // обработка одного подключения

                    out.println("Введите информацию о покупке в формате:" +
                            " \"название, год.месяц.число, сумма\"");
                    String message = in.readLine();
                    out.println(categorizingPurchases.distributionAll(message));
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}

//Дополнительное задание 1 (НЕобязательное для выполнения):
//Добавьте автосохранение данных: пусть после каждой операции сервер записывает в файл data.bin,
// размещённый в корне проекта, всю нужную статистику для работы сервера.
// В случае старта сервера и наличия этого файла, пусть он загружает все предыдущие данные из него.

