import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

public class MaxCategories {
    private final MaxCategory maxCategory;
    private final MaxCategory maxCategoryYear;
    private final MaxCategory maxCategoryMonth;
    private final MaxCategory maxCategoryDay;


    public MaxCategories(
            @JsonProperty("MaxCategory") MaxCategory maxCategory,
            @JsonProperty("MaxCategoryYear") MaxCategory maxCategoryYear,
            @JsonProperty("MaxCategoryMonth") MaxCategory maxCategoryMonth,
            @JsonProperty("MaxCategoryDay") MaxCategory maxCategoryDay) {
        this.maxCategory = maxCategory;
        this.maxCategoryYear = maxCategoryYear;
        this.maxCategoryMonth = maxCategoryMonth;
        this.maxCategoryDay = maxCategoryDay;
    }

    public String toJson(MaxCategories maxCategories) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(maxCategories);
    }

    public MaxCategory getMaxCategory() {
        return maxCategory;
    }

    public MaxCategory getMaxCategoryYear() {
        return maxCategoryYear;
    }

    public MaxCategory getMaxCategoryMonth() {
        return maxCategoryMonth;
    }

    public MaxCategory getMaxCategoryDay() {
        return maxCategoryDay;
    }

}
