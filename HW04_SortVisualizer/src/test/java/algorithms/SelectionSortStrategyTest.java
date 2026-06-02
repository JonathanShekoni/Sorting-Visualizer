package algorithms;

import common.Snapshot;
import common.SnapshotWithIndex;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SelectionSortStrategyTest {

    @Test
    public void testStubDetection() {
        SelectionSortStrategy<Integer> sorter = new SelectionSortStrategy<>();
        List<Integer> input = new ArrayList<>(Arrays.asList(10, 5, 8, 2, 7));
        List<Integer> original = new ArrayList<>(input);
        sorter.sort(input);

        // This assertion ensures the list has been modified.
        assertNotEquals(original, input, "The sorting method appears to be a stub, as the input list was not modified.");
        // This assertion checks the list is actually sorted.
        SortingTestUtils.assertListSorted(input);
    }

    @Test
    public void testUnstable() {
        SelectionSortStrategy<Integer> selectionSort = new SelectionSortStrategy<>();
        List<Integer> input = Arrays.asList(3, 3, 1, 3, 1, 1, 2);
        List<Snapshot> snapshots = selectionSort.sortAndReturnSnapshots(new ArrayList<>(input));
        Snapshot finalSnapshot = snapshots.get(snapshots.size() - 1);
        SnapshotWithIndex snapIdx = (SnapshotWithIndex) finalSnapshot;

        // Check for reversed duplicate pairs to indicate instability.
        boolean foundReversedDuplicates = false;
        List<Integer> sortedValues = snapIdx.getValues();
        List<Integer> originalIndices = snapIdx.getOriginalIndices();
        for (int i = 0; i < sortedValues.size() - 1; i++) {
            if (sortedValues.get(i).equals(sortedValues.get(i + 1)) &&
                    originalIndices.get(i) > originalIndices.get(i + 1)) {
                foundReversedDuplicates = true;
                break;
            }
        }
        assertTrue(foundReversedDuplicates,
                "Expected selection sort to be unstable on this input, but found no reversed duplicates.");
    }

    @Test
    public void testEmptyList() {
        SelectionSortStrategy<Integer> selectionSort = new SelectionSortStrategy<>();
        List<Integer> input = new ArrayList<>();
        selectionSort.sort(input);
        assertTrue(input.isEmpty());
    }

    @Test
    public void testSingleElement() {
        SelectionSortStrategy<Integer> selectionSort = new SelectionSortStrategy<>();
        List<Integer> input = new ArrayList<>(List.of(42));
        selectionSort.sort(input);
        assertEquals(List.of(42), input);
    }

    @Test
    public void testAlreadySorted() {
        SelectionSortStrategy<Integer> selectionSort = new SelectionSortStrategy<>();
        List<Integer> input = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        selectionSort.sort(input);
        // Use the utility to ensure ascending order.
        SortingTestUtils.assertListSorted(input);
    }

    @Test
    public void testReverseSorted() {
        SelectionSortStrategy<Integer> selectionSort = new SelectionSortStrategy<>();
        List<Integer> input = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
        selectionSort.sort(input);
        SortingTestUtils.assertListSorted(input);
    }

    @Test
    public void testRandomList() {
        SelectionSortStrategy<Integer> selectionSort = new SelectionSortStrategy<>();
        List<Integer> input = new ArrayList<>(Arrays.asList(4, 1, 5, 2, 7));
        selectionSort.sort(input);
        assertEquals(List.of(1, 2, 4, 5, 7), input);
    }

    @Test
    public void testMediumRandomList() {
        SelectionSortStrategy<Integer> selectionSort = new SelectionSortStrategy<>();
        List<Integer> input = new ArrayList<>(Arrays.asList(10, 3, 2, 2, 9, 8, 7, 5, 5, 1, 6, 4));
        selectionSort.sort(input);
        SortingTestUtils.assertListSorted(input);
    }

    @Test
    public void testLargerRandomList() {
        SelectionSortStrategy<Integer> sorter = new SelectionSortStrategy<>();
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
