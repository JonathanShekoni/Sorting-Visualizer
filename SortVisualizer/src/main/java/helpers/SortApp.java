package helpers;

import algorithms.*;

import java.util.*;

/**
 * Demonstration class for various sorting strategies.
 *
 * @version 1.0, 2024-03-11
 *
 *  Author:    Maria Hybinette
 *  Copyright: Copyright (c) 2023-2025, Maria Hybinette.
 *             All rights reserved.
 */
public class SortApp {

    /**
     * A general method to run a sort, measure its time, and (optionally) print results.
     *
     * @param <T>       The comparable type to sort
     * @param title     A label or short description of the sort (e.g. "Insertion Sort")
     * @param original  The original list (will be copied before sorting)
     * @param strategy  The sorting strategy to use
     * @param largeList If true, suppress printing large lists for clarity
     */
    private static <T extends Comparable<T>> void runSortDemo(
            String title,
            List<T> original,
            SortStrategy<T> strategy,
            boolean largeList
    ) {
        // Make a copy so we don't mutate the original list
        List<T> copy = new ArrayList<>(original);

        System.out.println(title + ":");

        // Measure time
        long startTime = System.nanoTime();
        strategy.sort(copy);
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;  // convert ns -> ms

        // Print results if not large
        if (!largeList) {
            System.out.println("\t" + copy);
        }
        System.out.println("\tTime taken: " + durationMs + " ms\n");
    }

    /**
     * Generates a list of random integers with the given size and max range.
     */
    private static List<Integer> generateRandomList(int size, int maxValue) {
        List<Integer> list = new ArrayList<>(size);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            list.add(random.nextInt(maxValue));  // values in [0..maxValue-1]
        }
        return list;
    }

    private static List<Integer> generateAscendingList(int size, int maxValue) {
        List<Integer> list = generateRandomList(size, maxValue);
        Collections.sort(list);
        return list;
    }

    private static List<Integer> generateDescendingList(int size, int maxValue) {
        List<Integer> list = generateRandomList(size, maxValue);
        Collections.sort(list, Collections.reverseOrder());
        return list;
    }

    public static void main(String[] args) {
        // 1. Generate a random list of integers
        int numberOfElements = 7;
        List<Integer> numbers = generateRandomList(numberOfElements, 100);
        boolean largeList = (numberOfElements > 20);

        // 2. Print the original list (if not large)
        if (!largeList) {
            System.out.println("Original list:");
            System.out.println("\t" + numbers);
        }
        System.out.println();

        // 4. "Verbose" quick sort example (in your updated code, if there's no verbose mode,
        //    just treat it as normal QuickSort again)
        runSortDemo("Quick Sort (Verbose)", numbers, new QuickSortStrategy<>(), largeList);

        // 5. Counting Sort with Repetitions example (Unstable)
        List<Integer> CSRnumbers = new ArrayList<>(Arrays.asList(
                7, 0, 4, 0, 9, 1, 9, 1, 9, 5, 3, 7, 3, 1, 6, 7, 4, 2, 0
        ));
        runSortDemo("Counting Sort Repetitions Example", CSRnumbers, new CountingSortNotStableStrategy(), largeList);

        // 5. Bubble Sort example:
        // Note: runSortDemo calls strategy.sort(copy). For snapshot-producing strategies,
        // the final sorted order might be stored only in the snapshots and not reflected in 'copy'.
        // Since BubbleSort sorts the list in place, we can call sort() directly and print the result.
        BubbleSortStrategy<Double> bubbleSort = new BubbleSortStrategy<>();
        List<Double> BSnumbers = new ArrayList<>(Arrays.asList(5.0, 3.5, 4.4, 33.55, 0.01));
        bubbleSort.sort(BSnumbers);
        System.out.print("Bubble Sort Example:\n\t");
        System.out.println(BSnumbers + "\n");

        // 6. TODO: Uncomment each demo as you implement the corresponding strategy

        // Counting Sort (Stable)
        List<Integer> CSnumbers = new ArrayList<>(Arrays.asList(7, 0, 4, 0, 9, 1, 9, 1, 9, 5, 3, 7, 3, 1, 6, 7, 4, 2, 0));
        runSortDemo("Counting Sort (Stable)", CSnumbers, new CountingSortStrategy<>(), largeList);

        // Selection Sort
        runSortDemo("Selection Sort", numbers, new SelectionSortStrategy<>(), largeList);

        // Insertion Sort
        runSortDemo("Insertion Sort", numbers, new InsertionSortStrategy<>(), largeList);

        // Merge Sort
        runSortDemo("Merge Sort", numbers, new MergeSortStrategy<>(), largeList);

        // 7. Compare different QuickSort Strategies (show in Demo Video your new Pivot Strategies)

        // QuickSort with left pivot on a descending-ordered list (worst-case behavior)
        List<Integer> descendingList = generateDescendingList(100000, 1000); // smaller, maxValue == 100 what happens and why?
        runSortDemo("Quick Sort (Left Pivot, Descending)", descendingList, new QuickSortLeftStrategy<>(), descendingList.size() > 20);

        // QuickSort with midpoint pivot on an ascending-ordered list
        List<Integer> ascendingList = generateAscendingList(100000, 1000);
        runSortDemo("Quick Sort (Midpoint Pivot, Ascending)", ascendingList, new QuickSortStrategy<>(), ascendingList.size() > 20);

        // QuickSort with random pivot
        runSortDemo("Quick Sort (Random Pivot)", numbers, new QuickSortRandomPivotStrategy<>(), largeList);

        // QuickSort with median-of-three pivot
        runSortDemo("Quick Sort (Pivot Three Locations)", numbers, new QuickSortPivotThreeLocationsStrategy<>(), largeList);

        // 8. Bonus strategies (uncomment if implemented)
        runSortDemo("Quick Sort (Three-Way)", numbers, new QuickSortThreeWayStrategy<>(), largeList);
        runSortDemo("Counting Sort (Unstable LIFO)", CSnumbers, new CountingSortNotStableLookUpTableStrategy<>(), largeList);
        List<Integer> BckSnumbers = new ArrayList<>(Arrays.asList(78, 17, 39, 26, 72, 94, 21, 12, 23, 68));
        runSortDemo("Bucket Sort", BckSnumbers, new BucketSortStrategy<>(), largeList);
    }


}