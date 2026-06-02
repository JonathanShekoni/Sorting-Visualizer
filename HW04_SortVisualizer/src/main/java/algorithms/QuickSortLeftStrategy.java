package algorithms;

import common.ElementWithIndex;
import common.Snapshot;
import common.SnapshotProducingStrategy;
import common.SnapshotWithIndex;
import static common.SnapshotUtils.*;

import java.util.ArrayList;
import java.util.List;

public class QuickSortLeftStrategy<T extends Comparable<T>>
        implements SortStrategy<T>, SnapshotProducingStrategy {

    @Override
    public String toString() {
        return "QuickSort (Left Pivot)";
    }

    @Override
    public void sort(List<T> list) {
        if (list == null || list.size() < 2) return;
        quickSort(list, 0, list.size() - 1);
    }

    @Override
    public List<Snapshot> sortAndReturnSnapshots(List<Integer> data) {
        List<Snapshot> snapshots = new ArrayList<>();
        if (data == null || data.size() < 2) {
            snapshots.add(makeSnapshotOf(data, -1));
            return snapshots;
        }

        // 1) Convert raw data to a list of ElementWithIndex.
        List<ElementWithIndex> elements = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            elements.add(new ElementWithIndex(data.get(i), i));
        }
        // 2) Capture the initial unsorted state (deep copy).
        snapshots.add(new SnapshotWithIndex(cloneElements(elements), -1));

        // 3) Perform QuickSort with snapshots using leftmost pivot.
        quickSortWithSnapshots(elements, 0, elements.size() - 1, snapshots);

        // 4) Final snapshot (sorted, no highlight).
        snapshots.add(new SnapshotWithIndex(cloneElements(elements), -1));
        return snapshots;
    }

    // --- Standard QuickSort using left pivot ---
    private void quickSort(List<T> list, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(list, left, right);
            quickSort(list, left, pivotIndex - 1);
            quickSort(list, pivotIndex + 1, right);
        }
    }

    // Partition for the non-snapshot version: pivot is leftmost.
    private int partition(List<T> list, int left, int right, T pivot) {
        int i = left + 1;
        int j = right;
        while (true) {
            while (i <= j && list.get(i).compareTo(pivot) <= 0) {
                i++;
            }
            while (i <= j && list.get(j).compareTo(pivot) > 0) {
                j--;
            }
            if (i > j) break;
            swap(list, i, j);
        }
        swap(list, left, j);
        return j;
    }

    private int partition(List<T> list, int left, int right) {
        T pivot = list.get(left); // pivot is leftmost element.
        return partition(list, left, right, pivot);
    }

    // --- QuickSort with snapshots (using left pivot) ---
    private void quickSortWithSnapshots(List<ElementWithIndex> elems, int left, int right, List<Snapshot> snapshots) {
        if (left < right) {
            int pivotIndex = partitionWithSnapshots(elems, left, right, snapshots);
            quickSortWithSnapshots(elems, left, pivotIndex - 1, snapshots);
            quickSortWithSnapshots(elems, pivotIndex + 1, right, snapshots);
        }
    }

    /**
     * Partition method for snapshot-based quick sort.
     * Chooses the leftmost element as the pivot and partitions the subarray.
     * Captures a snapshot after each swap and after the pivot is placed.
     */
    private int partitionWithSnapshots(List<ElementWithIndex> elems, int left, int right, List<Snapshot> snapshots) {
        int pivotValue = elems.get(left).getValue(); // pivot is leftmost
        ElementWithIndex pivot = elems.get(left);
        int i = left + 1;
        int j = right;
        while (true) {
            while (i <= j && elems.get(i).getValue() <= pivotValue) {
                i++;
            }
            while (i <= j && elems.get(j).getValue() > pivotValue) {
                j--;
            }
            if (i > j) break;
            // Swap elems[i] and elems[j]
            ElementWithIndex temp = elems.get(i);
            elems.set(i, elems.get(j));
            elems.set(j, temp);
            // Capture snapshot after swap, highlighting index i.
            snapshots.add(new SnapshotWithIndex(cloneElements(elems), i));
        }
        // Place pivot in its correct position by swapping with elems[j]
        elems.set(left, elems.get(j));
        elems.set(j, pivot);
        // Capture snapshot after placing pivot, highlighting index j.
        snapshots.add(new SnapshotWithIndex(cloneElements(elems), j));
        return j;
    }
}