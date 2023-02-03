import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
public class Purchase {
    // класс параметров одной покупки
    private final String date; //дата покупки
    private final String title; // наименование покупки
    private final int sum; // сумма покупки
// //  LocalDate date = LocalDate.parse(parts[1]);

    public Purchase(
            @JsonProperty("title") String title,
            @JsonProperty("date") String date,
            @JsonProperty("sum") int sum) {
        this.title = title;
        this.date = date;
        this.sum = sum;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public int getSum() {
        return sum;
    }
}
