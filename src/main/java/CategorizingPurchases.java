import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CategorizingPurchases {
    public static final ObjectMapper mapper = new ObjectMapper();

    // справочник соотношения названия продуктов и категорий
    public Map<String, String> directory = new HashMap<>();

    public void readTSV() {
        // чтение файла и запись строк в мар, где ключ - название покупки, а значение - её категория

        try (BufferedReader TSVFile = new BufferedReader(new FileReader("categories.tsv"))) {
            String line = TSVFile.readLine();
            while (line != null) {
                String[] text = line.split("\t");
                String title = text[0];
                String category = text[1];
                directory.put(title, category);
                line = TSVFile.readLine(); // Read next line
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // мар для подсчета сумм затрат по категориям
    private Map<String, Integer> sumCategories = new HashMap<>();
    private Map<String, Integer> sumCategoriesYear = new HashMap<>();
    private Map<String, Integer> sumCategoriesMonth = new HashMap<>();
    private Map<String, Integer> sumCategoriesDay = new HashMap<>();

    // лист, для сохранения всех покупок
    private final List<Purchase> purchasesAll = new ArrayList<>();
    public String distributionAll(String message) throws Exception {
        readTSV();
        //получили покупку
        Purchase purchases = mapper.readValue(message, Purchase.class);
        purchasesAll.add(purchases);
        MaxCategories maxCategories = distributionOfDate(purchasesAll, purchases);
        // вернуть JSON по MaxCategories
        return maxCategories.toJson(maxCategories);
    }

    public MaxCategories distributionOfDate(List<Purchase> purchaseList, Purchase purchase) {
        LocalDate datePurchase = LocalDate.parse(purchase.getDate(), DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        int yearPurchase = datePurchase.getYear();
        int monthPurchase = datePurchase.getMonthValue();
        int dayPurchase = datePurchase.getDayOfMonth();
        sumCategories.clear();
        sumCategoriesYear.clear();
        sumCategoriesMonth.clear();
        sumCategoriesDay.clear();
        purchaseList
                .forEach(value -> {
                    //получить название покупки и определить категорию
                    String category = categoryDefinition(value.getTitle());
                    // получить сумму покупки и сложить её в сумму покупок по категории
                    sumCategories = purchaseSumByCategory(sumCategories, category, value.getSum());
                });
        purchaseList.stream()
                .filter(value -> LocalDate.parse(value.getDate(), DateTimeFormatter.ofPattern("yyyy.MM.dd")).getYear() == yearPurchase)
                .forEach(value -> {
                    String categoryYear = categoryDefinition(value.getTitle());
                    sumCategoriesYear = purchaseSumByCategory(sumCategoriesYear, categoryYear, value.getSum());
                });
        purchaseList.stream()
                .filter(value -> LocalDate.parse(value.getDate(), DateTimeFormatter.ofPattern("yyyy.MM.dd")).getMonthValue() == monthPurchase)
                .forEach(value -> {
                    String categoryMonth = categoryDefinition(value.getTitle());
                    sumCategoriesMonth = purchaseSumByCategory(sumCategoriesMonth, categoryMonth, value.getSum());
                });
        purchaseList.stream()
                .filter(value -> LocalDate.parse(value.getDate(), DateTimeFormatter.ofPattern("yyyy.MM.dd")).getDayOfMonth() == dayPurchase)
                .forEach(value -> {
                    String categoryDay = categoryDefinition(value.getTitle());
                    sumCategoriesDay = purchaseSumByCategory(sumCategoriesDay, categoryDay, value.getSum());
                });
        // определить максимальную сумму
        int sumMaxCategory = findSumMaxCategory(sumCategories);
        int sumMaxCategoryYear = findSumMaxCategory(sumCategoriesYear);
        int sumMaxCategoryMonth = findSumMaxCategory(sumCategoriesMonth);
        int sumMaxCategoryDay = findSumMaxCategory(sumCategoriesDay);
        // найти название категории с максимальной суммой
        String nameMaxCategory = findNameMaxCategory(sumCategories, sumMaxCategory);
        String nameMaxCategoryYear = findNameMaxCategory(sumCategoriesYear, sumMaxCategoryYear);
        String nameMaxCategoryMonth = findNameMaxCategory(sumCategoriesMonth, sumMaxCategoryMonth);
        String nameMaxCategoryDay = findNameMaxCategory(sumCategoriesDay, sumMaxCategoryDay);
        // создать объект максимальной категории по каждому виду
        MaxCategory maxCategory = new MaxCategory(nameMaxCategory, sumMaxCategory);
        MaxCategory maxCategoryYear = new MaxCategory(nameMaxCategoryYear, sumMaxCategoryYear);
        MaxCategory maxCategoryMonth = new MaxCategory(nameMaxCategoryMonth, sumMaxCategoryMonth);
        MaxCategory maxCategoryDay = new MaxCategory(nameMaxCategoryDay, sumMaxCategoryDay);
        return new MaxCategories(maxCategory, maxCategoryYear, maxCategoryMonth, maxCategoryDay);
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

    public static String scannerPurchase() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String[] parts = input.split(", ");
        Purchase purchase = new Purchase(parts[0], parts[1], Integer.parseInt(parts[2]));
        return purchase.toJson(purchase);
    }

    public static void main(String[] args) throws Exception {
        CategorizingPurchases categorizingPurchases = new CategorizingPurchases();
        System.out.println(categorizingPurchases.distributionAll("{\"date\":\"2022.12.12\",\"title\":\"тапки\",\"sum\":10}"));
        System.out.println(categorizingPurchases.distributionAll("{\"date\":\"2023.01.12\",\"title\":\"мыло\",\"sum\":300}"));
        System.out.println(categorizingPurchases.distributionAll("{\"date\":\"2022.11.23\",\"title\":\"шапка\",\"sum\":1000}"));
        System.out.println(categorizingPurchases.distributionAll("{\"date\":\"2023.01.12\",\"title\":\"шапка\",\"sum\":100}"));
    }
}
