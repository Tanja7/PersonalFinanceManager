import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

@DisplayName("Тестирование: CategorizingPurchases")
public class CategorizingPurchasesTest {

    private final CategorizingPurchases categorizingPurchases = new CategorizingPurchases();

    @Test
    @DisplayName("Тестирование метода сложения суммы покупок по категории")
    void purchaseSumByCategoryTest() {
        Map<String, Integer> purchases = new HashMap<>();
        categorizingPurchases.purchaseSumByCategory(purchases,"еда", 567);
        categorizingPurchases.purchaseSumByCategory(purchases, "одежда", 605);
        categorizingPurchases.purchaseSumByCategory(purchases, "еда", 45);
        int result = purchases.get("еда");
        Assertions.assertEquals(612, result);
    }

    @Test
    @DisplayName("Тестирование метода определения максимальной суммы по категориям")
    void findSumMaxCategoryTest() {
        Map<String, Integer> purchases1 = new HashMap<>();
        categorizingPurchases.purchaseSumByCategory(purchases1,"еда", 567);
        categorizingPurchases.purchaseSumByCategory(purchases1,"одежда", 605);
        categorizingPurchases.purchaseSumByCategory(purchases1,"еда", 45);
        int result = categorizingPurchases.findSumMaxCategory(purchases1);
        Assertions.assertEquals(612, result);
    }

    @Test
    @DisplayName("Тестирование метода поиска категории с максимальной суммой")
    void findNameMaxCategoryTest() {
        Map<String, Integer> purchases2 = new HashMap<>();
        categorizingPurchases.purchaseSumByCategory(purchases2, "еда", 567);
        categorizingPurchases.purchaseSumByCategory(purchases2, "одежда", 605);
        categorizingPurchases.purchaseSumByCategory(purchases2, "еда", 45);
        String result = categorizingPurchases.findNameMaxCategory(purchases2, categorizingPurchases.findSumMaxCategory(purchases2));
        Assertions.assertEquals("еда", result);
    }

}
