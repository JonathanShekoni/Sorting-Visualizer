package helpers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.Function;

/**
 * A standalone CSV generator program.
 *
 * Usage:
 *   java helpers.CSVGenerator <filename> <data_size> <highest_value> <lowest_value> <data_type> <order>
 *   if @ maven root directory:
 *   java -cp  target/classes helpers.CSVGenerator  src/main/resources/input/nearly_sorted.csv  50 100 0 integer nearly
 *
 * Examples:
 *   java helpers.CSVGenerator numbers.csv 50 100 0 integer random
 *   java helpers.CSVGenerator doubles.csv 100 99.9 0.1 double nearly
 *   java helpers.CSVGenerator chars.csv 30 z a char sorted
 *   java helpers.CSVGenerator strings.csv 20 z a string reverse
 */
public class CSVGenerator {

    public static void main(String[] args) {
        if(args.length < 6) {
            System.err.println("Usage: CSVGenerator <filename> <data_size> <highest_value> <lowest_value> <data_type> <order>");
            System.exit(1);
        }
        String filename = args[0];
        int dataSize = Integer.parseInt(args[1]);
        String highestValueStr = args[2];
        String lowestValueStr = args[3];
        String dataType = args[4].toLowerCase();
        String order = args[5].toLowerCase();

        List<?> data;
        try {
            switch(dataType) {
                case "integer":
                    int highInt = Integer.parseInt(highestValueStr);
                    int lowInt = Integer.parseInt(lowestValueStr);
                    data = generateIntegers(dataSize, lowInt, highInt, order);
                    break;
                case "double":
                    double highDouble = Double.parseDouble(highestValueStr);
                    double lowDouble = Double.parseDouble(lowestValueStr);
                    data = generateDoubles(dataSize, lowDouble, highDouble, order);
                    break;
                case "char":
                    char highChar = highestValueStr.charAt(0);
                    char lowChar = lowestValueStr.charAt(0);
                    data = generateChars(dataSize, lowChar, highChar, order);
                    break;
                case "string":
                    // Generate random strings of fixed length (e.g., 5)
                    char highForStr = highestValueStr.charAt(0);
                    char lowForStr = lowestValueStr.charAt(0);
                    data = generateStrings(dataSize, lowForStr, highForStr, 5, order);
                    break;
                default:
                    System.err.println("Unknown data type: " + dataType);
                    return;
            }
        } catch (NumberFormatException nfe) {
            System.err.println("Error parsing numerical values: " + nfe.getMessage());
            return;
        }

        // Write the data to CSV file: one element per line.
        try (PrintWriter out = new PrintWriter(new FileWriter(new File(filename)))) {
            for (Object element : data) {
                out.println(element);
            }
        } catch (IOException e) {
            System.err.println("Error writing CSV: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("CSV file generated: " + filename);
    }

    private static List<Integer> generateIntegers(int size, int low, int high, String order) {
        List<Integer> list = new ArrayList<>(size);
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            list.add(rand.nextInt(high - low + 1) + low);
        }
        applyOrder(list, order);
        return list;
    }

    private static List<Double> generateDoubles(int size, double low, double high, String order) {
        List<Double> list = new ArrayList<>(size);
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            list.add(low + rand.nextDouble() * (high - low));
        }
        applyOrder(list, order);
        return list;
    }

    private static List<Character> generateChars(int size, char low, char high, String order) {
        List<Character> list = new ArrayList<>(size);
        Random rand = new Random();
        int lowInt = (int) low;
        int highInt = (int) high;
        for (int i = 0; i < size; i++) {
            list.add((char)(rand.nextInt(highInt - lowInt + 1) + lowInt));
        }
        applyOrder(list, order);
        return list;
    }

    private static List<String> generateStrings(int size, char low, char high, int strLength, String order) {
        List<String> list = new ArrayList<>(size);
        Random rand = new Random();
        int lowInt = (int) low;
        int highInt = (int) high;
        for (int i = 0; i < size; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < strLength; j++) {
                sb.append((char)(rand.nextInt(highInt - lowInt + 1) + lowInt));
            }
            list.add(sb.toString());
        }
        applyOrder(list, order);
        return list;
    }

    /**
     * Adjusts the order of the list based on the order parameter.
     * "random" - no change,
     * "nearly" - sorted then a few swaps,
     * "sorted" - ascending sort,
     * "reverse" - descending sort.
     */
    private static <T extends Comparable<T>> void applyOrder(List<T> list, String order) {
        if (order.equals("random")) {
            // Already random.
        } else if (order.equals("nearly")) {
            Collections.sort(list);
            Random rand = new Random();
            int swaps = Math.max(1, list.size() / 10);
            for (int i = 0; i < swaps; i++) {
                int idx1 = rand.nextInt(list.size());
                int idx2 = rand.nextInt(list.size());
                Collections.swap(list, idx1, idx2);
            }
        } else if (order.equals("sorted")) {
            Collections.sort(list);
        } else if (order.equals("reverse")) {
            Collections.sort(list, Collections.reverseOrder());
        }
    }
}