package algorithms;

import common.Snapshot;
import common.SnapshotWithIndex;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CountingSortStrategyTest {

    @Test
    public void testStubDetection() {
        CountingSortStrategy<Integer> sorter = new CountingSortStrategy<>();
        List<Integer> input = new ArrayList<>(Arrays.asList(10, 5, 8, 2, 7));
        List<Integer> original = new ArrayList<>(input);
        sorter.sort(input);

        // This assertion ensures the list has been modified.
        assertNotEquals(original, input, "The sorting method appears to be a stub, as the input list was not modified.");
        // This assertion checks the list is actually sorted.
        SortingTestUtils.assertListSorted(input);
    }

    @Test
    public void testStability() {
        CountingSortStrategy<Integer> stableCounting = new CountingSortStrategy<>();
        List<Integer> input = Arrays.asList(5, 5, 3, 5, 3);

        List<Snapshot> snapshots = stableCounting.sortAndReturnSnapshots(new ArrayList<>(input));
        SnapshotWithIndex finalSnapshot = (SnapshotWithIndex) snapshots.get(snapshots.size() - 1);

        // Verify ascending order and stable duplicate ordering.
        SortingTestUtils.assertListSorted(finalSnapshot.getValues());
        SortingTestUtils.assertStableSort(finalSnapshot);
    }

    @Test
    public void testEmptyList() {
        CountingSortStrategy<Integer> stableCounting = new CountingSortStrategy<>();
        List<Integer> input = new ArrayList<>();
        stableCounting.sort(input);
        assertTrue(input.isEmpty());
    }

    @Test
    public void testSingleElement() {
        CountingSortStrategy<Integer> stableCounting = new CountingSortStrategy<>();
        List<Integer> input = new ArrayList<>(List.of(42));
        stableCounting.sort(input);
        assertEquals(List.of(42), input);
    }

    @Test
    public void testAlreadySorted() {
        CountingSortStrategy<Integer> stableCounting = new CountingSortStrategy<>();
        List<Integer> input = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        stableCounting.sort(input);
        SortingTestUtils.assertListSorted(input);
    }

    @Test
    public void testReverseSorted() {
        CountingSortStrategy<Integer> stableCounting = new CountingSortStrategy<>();
        List<Integer> input = new ArrayList<>(List.of(5, 4, 3, 2, 1));
        stableCounting.sort(input);
        SortingTestUtils.assertListSorted(input);
    }

    @Test
    public void testAllDuplicates() {
        CountingSortStrategy<Integer> stableCounting = new CountingSortStrategy<>();
        List<Integer> input = new ArrayList<>(Arrays.asList(7, 7, 7, 7, 7));
        stableCounting.sort(input);
        assertEquals(List.of(7, 7, 7, 7, 7), input,
                "All duplicates should remain unchanged after sort.");
    }

    @Test
    public void testMostlyDuplicates() {
        CountingSortStrategy<Integer> stableCounting = new CountingSortStrategy<>();
        List<Integer> input = Arrays.asList(3, 3, 3, 2, 2, 3, 2, 3);
        List<Snapshot> snapshots = stableCounting.sortAndReturnSnapshots(new ArrayList<>(input));
        SnapshotWithIndex finalSnapshot = (SnapshotWithIndex) snapshots.get(snapshots.size() - 1);

        SortingTestUtils.assertListSorted(finalSnapshot.getValues());
        SortingTestUtils.assertStableSort(finalSnapshot);
    }

    @Test
    public void testRandomList() {
        CountingSortStrategy<Integer> sorter = new CountingSortStrategy<>();
        List<Integer> input = new ArrayList<>(Arrays.asList(10, 1, 7, 4, 9, 2));
        sorter.sort(input);
        assertEquals(List.of(1, 2, 4, 7, 9, 10), input, "Random list should be sorted ascending.");
    }

    @Test
    public void testMediumRandomList() {
        CountingSortStrategy<Integer> sorter = new CountingSortStrategy<>();
        List<Integer> input = new ArrayList<>(Arrays.asList(10, 3, 2, 2, 9, 8, 7, 5, 5, 1, 6, 4));
        sorter.sort(input);
        SortingTestUtils.assertListSorted(input);
    }

    @Test
    public void testLargerRandomList() {
        CountingSortStrategy<Integer> sorter = new CountingSortStrategy<>();
        List<Integer> input = new ArrayList<>();
        Random rand = new Random();
        int size = 100; // This can be adjusted to your liking
        for (int i = 0; i < size; i++) {
            input.add(rand.nextInt(1000)); // ensure a wide range of random values
        }
        // Use either sort() or sortAndReturnSnapshots(), depending on what you want to verify.
        sorter.sort(input);
        SortingTestUtils.assertListSorted(input);
    }

}
