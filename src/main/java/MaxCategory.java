import org.json.simple.JSONObject;

public class MaxCategory {

    private String category;
    private int sum;

    public MaxCategory (String category, int sum) {
        this.category = category;
        this.sum = sum;
    }

    public String getCategory() {
        return category;
    }

    public int getSum() {
        return sum;
    }

//        public String toJson(String nameCategory, int sumCategory) {
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("category", nameCategory);
//        jsonObject.put("sum", sumCategory);
//
//        return "\"maxCategory\": " + jsonObject;
//    }
}
