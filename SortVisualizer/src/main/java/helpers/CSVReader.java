package helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Utility class for reading CSV files.
 *
 * Example usage:
 *   List<Integer> data = CSVReader.readCSV("numbers.csv", Integer::valueOf);
 */
public class CSVReader {
    /**
     * Reads a CSV file and parses each token using the provided parser function.
     *
     * @param <T> the type of elements to parse (e.g. Integer, Double, etc.)
     * @param filePath path to the CSV file
     * @param parser   function to convert from String to T (e.g. Integer::valueOf)
     * @return a list of T parsed from the CSV
     */
    public static <T> List<T> readCSV(String filePath, Function<String, T> parser) {
        List<T> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Remove BOM if present
                if (line.startsWith("\uFEFF")) {
                    line = line.substring(1);
                }
                // Split line on commas
                String[] tokens = line.split(",");
                for (String token : tokens) {
                    token = token.trim();
                    if (!token.isEmpty()) {
                        data.add(parser.apply(token));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}