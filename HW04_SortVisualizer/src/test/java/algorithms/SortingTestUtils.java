package algorithms;

import common.SnapshotWithIndex;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class SortingTestUtils {

    /**
     * Asserts that the given list is sorted in ascending order.
     */
    public static <T extends Comparable<T>> void assertListSorted(List<T> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            assertTrue(list.get(i).compareTo(list.get(i + 1)) <= 0,
                    "List is not sorted in ascending order: element at index " + i +
                            " (" + list.get(i) + ") should be <= element at index " + (i + 1) +
                            " (" + list.get(i + 1) + "). Full list: " + list);
        }
    }

    /**
     * Asserts that the given snapshot (with original indices) indicates a stable sort.
     * For each pair of equal values, the original index should be in ascending order.
     */
    public static <T> void assertStableSort(SnapshotWithIndex snapshot) {
        List<T> values = (List<T>) snapshot.getValues();
        List<Integer> origIndices = snapshot.getOriginalIndices();
        for (int i = 0; i < values.size() - 1; i++) {
            if (values.get(i).equals(values.get(i + 1))) {
                assertTrue(origIndices.get(i) < origIndices.get(i + 1),
                        "Stable sort violated: duplicate values " + values.get(i) +
                                " have original indices " + origIndices.get(i) + " and " + origIndices.get(i + 1));
            }
        }
    }
}
