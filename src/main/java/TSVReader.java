import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class TSVReader {
    public static Map<String, String> readTSV() throws Exception {
    // чтение файла и запись строк в мар, где ключ - название покупки, а значение - её категория
        Map<String, String> directories = new HashMap<>();
        BufferedReader TSVFile = new BufferedReader(new FileReader("categories.tsv"));
        String line = TSVFile.readLine();
        while (line != null) {
            String[] text = line.split("\t");
            String title = text[0];
            String category = text[1];
            directories.put(title, category);
            line = TSVFile.readLine(); // Read next line
        }
        TSVFile.close();
        return directories;
    }

}