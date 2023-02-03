import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CategorizingPurchases {
    public static final ObjectMapper mapper = new ObjectMapper();
    private int sumMaxCategory = 0;
    private String nameMaxCategory;

    // справочник соотношения названия продуктов и категорий
    public Map<String, String> directory = TSVReader.readTSV();
    // мар для подсчета сумм затрат по категориям
    public Map<String, Integer> sumCategories = new HashMap<>();
    public CategorizingPurchases() throws Exception {
    }

    public String distribution(String message) throws Exception {
        // код для преобразования JSON в Java
        //получили покупку
        Purchase purchases = mapper.readValue(message, Purchase.class);
        //получить название покупки и определить категорию
        String titlePurchase = purchases.getTitle();
        String category = directory.getOrDefault(titlePurchase, "другое");
        // получить сумму покупки и сложить её в сумму покупок по категории
        if (sumCategories.containsKey(category)) {
            int sumCategory = sumCategories.remove(category);
            sumCategory = sumCategory + purchases.getSum();
            sumCategories.put(category, sumCategory);
        } else {
            sumCategories.put(category, purchases.getSum());
        }
        // определить максимальную сумму
        Object[] sum = sumCategories.values().toArray();
        for (Object o : sum) {
            int sum1 = (int) o;
            if (sum1 > sumMaxCategory) sumMaxCategory = sum1;
        }
      // найти название категории с максимальной суммой
        for (Map.Entry<String, Integer> entry: sumCategories.entrySet())
        {
            if (entry.getValue() == sumMaxCategory) {
                nameMaxCategory = entry.getKey();
            }
        }

        // вернуть JSON по MaxCategory

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("category", nameMaxCategory);
        jsonObject.put("sum", sumMaxCategory);

        String answer = "\"maxCategory\": " + jsonObject;
        return answer;
    }
}