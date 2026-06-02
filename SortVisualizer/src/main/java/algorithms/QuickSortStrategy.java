package algorithms;

import common.ElementWithIndex;
import common.Snapshot;
import common.SnapshotProducingStrategy;
import common.SnapshotWithIndex;

import java.util.ArrayList;
import java.util.List;
import static common.SnapshotUtils.*;

public class QuickSortStrategy<T extends Comparable<T>>
        implements SortStrategy<T>, SnapshotProducingStrategy {

    @Override
    public String toString() {
        return "QuickSort";
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

        // 1) Convert raw data to a list of ElementWithIndex
        List<ElementWithIndex> elements = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            elements.add(new ElementWithIndex(data.get(i), i));
        }

        // 2) Capture the initial unsorted state (deep copy)
        snapshots.add(new SnapshotWithIndex(cloneElements(elements), -1));

        // 3) Perform QuickSort with snapshots
        quickSortWithSnapshots(elements, 0, elements.size() - 1, snapshots);

        // 4) Final snapshot (sorted, no highlight)
        snapshots.add(new SnapshotWithIndex(cloneElements(elements), -1));
        return snapshots;
    }

    // Standard QuickSort (without snapshots)
    private void quickSort(List<T> list, int left, int right) {
        if (left >= right) return;
        T pivot = list.get((left + right) / 2);
        int index = partition(list, left, right, pivot);
        quickSort(list, left, index - 1);
        quickSort(list, index, right);
    }

    private int partition(List<T> list, int left, int right, T pivot) {
        while (left <= right) {
            while (list.get(left).compareTo(pivot) < 0) left++;
            while (list.get(right).compareTo(pivot) > 0) right--;
            if (left <= right) {
                swap(list, left, right);
                left++;
                right--;
            }
        }
        return left;
    }

    // QuickSort with snapshots (operating on ElementWithIndex)
    private void quickSortWithSnapshots(List<ElementWithIndex> elems, int left, int right, List<Snapshot> snapshots) {
        if (left >= right) return;
        int mid = (left + right) / 2;
        int pivot = elems.get(mid).getValue();
        int index = partitionWithSnapshots(elems, left, right, pivot, snapshots);
        quickSortWithSnapshots(elems, left, index - 1, snapshots);
        quickSortWithSnapshots(elems, index, right, snapshots);
    }

    private int partitionWithSnapshots(List<ElementWithIndex> elems, int left, int right, int pivotVal, List<Snapshot> snapshots) {
        while (left <= right) {
            while (elems.get(left).getValue() < pivotVal) left++;
            while (elems.get(right).getValue() > pivotVal) right--;
            if (left <= right) {
                // Swap
                ElementWithIndex temp = elems.get(left);
                elems.set(left, elems.get(right));
                elems.set(right, temp);
                // Capture a snapshot after each swap (deep copy) highlighting the "left" index.
                snapshots.add(new SnapshotWithIndex(cloneElements(elems), left));
                left++;
                right--;
            }
        }
        return left;
    }
}