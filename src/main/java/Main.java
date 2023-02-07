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
                    System.out.println(message);

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
//
//Дополнительное задание 2 (НЕобязательное для выполнения):
//Пусть объект статистики содержит дополнительные поля, которые показывают
// максимальную категорию за год, месяц и день сохранённой покупки:

//{
//  "maxCategory": {
//    "category": "еда",
//    "sum": 350000
//  },
//  "maxYearCategory": {
//    "category": "еда",
//    "sum": 300000
//  },
//  "maxMonthCategory": {
//    "category": "еда",
//    "sum": 24000
//  },
//  "maxDayCategory": {
//    "category": "одежда",
//    "sum": 3000
//  },
//}
//Этот объект состоит из четырёх полей, каждое из которых отображает максимальную по абсолютным тратам категорию
// за соответствующий период - maxCategory за всё время, maxYearCategory - за год добавленной покупки,
// maxMonthCategory за месяц добавленной покупки, maxDayCategory за день добавленной покупки.
