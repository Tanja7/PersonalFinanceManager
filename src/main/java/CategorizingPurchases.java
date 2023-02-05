import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class CategorizingPurchases {
    public static final ObjectMapper mapper = new ObjectMapper();

    // справочник соотношения названия продуктов и категорий
    public Map<String, String> directory = new HashMap<>();

    public void readTSV() throws Exception {
        // чтение файла и запись строк в мар, где ключ - название покупки, а значение - её категория

        BufferedReader TSVFile = new BufferedReader(new FileReader("categories.tsv"));
        String line = TSVFile.readLine();
        while (line != null) {
            String[] text = line.split("\t");
            String title = text[0];
            String category = text[1];
            directory.put(title, category);
            line = TSVFile.readLine(); // Read next line
        }
        TSVFile.close();
    }

    // мар для подсчета сумм затрат по категориям
    public Map<String, Integer> sumCategories = new HashMap<>();

    public String distribution(String message) throws Exception {
        readTSV();
        //получили покупку
        Purchase purchases = mapper.readValue(message, Purchase.class);
        //получить название покупки и определить категорию
        String category = categoryDefinition(purchases.getTitle());
        // получить сумму покупки и сложить её в сумму покупок по категории
        sumCategories = purchaseSumByCategory(sumCategories, category, purchases.getSum());
        // определить максимальную сумму
        int sumMaxCategory = findSumMaxCategory(sumCategories);
        // найти название категории с максимальной суммой
        String nameMaxCategory = findNameMaxCategory(sumCategories, sumMaxCategory);
        // вернуть JSON по MaxCategory
        return toJson(nameMaxCategory, sumMaxCategory);
    }

    public String categoryDefinition(String titlePurchase) {
        //определение категории
        return directory.getOrDefault(titlePurchase, "другое");

    }

    public Map<String, Integer> purchaseSumByCategory(Map<String, Integer> sums, String categoryName, int sum) {
        // сложение суммы покупок в сумму по категории
        if (sums.containsKey(categoryName)) {
            int sumCategory = sums.remove(categoryName);
            sumCategory = sumCategory + sum;
            sums.put(categoryName, sumCategory);
        } else {
            sums.put(categoryName, sum);
        }
        return sums;
    }

    public int findSumMaxCategory(Map<String, Integer> categoryAndSum) {
        // определить максимальную сумму
        Object[] sum = categoryAndSum.values().toArray();
        int sumMax = 0;
        for (Object o : sum) {
            int sum1 = (int) o;
            if (sum1 > sumMax) sumMax = sum1;
        }
        return sumMax;
    }

    public String findNameMaxCategory(Map<String, Integer> categoriesAndSums, int sumMax) {
        // найти название категории с максимальной суммой
        String nameCategory = null;
        for (
                Map.Entry<String, Integer> entry : categoriesAndSums.entrySet()) {
            if (entry.getValue() == sumMax) {
                nameCategory = entry.getKey();
            }
        }
        return nameCategory;
    }

    public String toJson(String nameCategory, int sumCategory) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("category", nameCategory);
        jsonObject.put("sum", sumCategory);

        return "\"maxCategory\": " + jsonObject;
    }
}