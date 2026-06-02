package algorithms;

import common.ElementWithIndex;
import common.Snapshot;
import common.SnapshotProducingStrategy;
import common.SnapshotWithIndex;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static common.SnapshotUtils.*;

public class QuickSortRandomPivotStrategy<T extends Comparable<T>>
        implements SortStrategy<T>, SnapshotProducingStrategy {

    private final Random random = new Random();

    @Override
    public String toString() {
        return "QuickSort (Random Pivot)";
    }

    @Override
    public void sort(List<T> list) {
        if (list == null || list.size() < 2) return;
        quickSort(list, 0, list.size() - 1);
    }

    private void quickSort(List<T> list, int left, int right) {
        if (left < right) {
            int pivotIndex = left + random.nextInt(right - left + 1);
            swap(list, pivotIndex, left);
            T pivot = list.get(left);
            int i = left + 1;
            int j = right;
            while (true) {
                while (i <= j && list.get(i).compareTo(pivot) <= 0) i++;
                while (i <= j && list.get(j).compareTo(pivot) > 0) j--;
                if (i > j) break;
                swap(list, i, j);
            }
            swap(list, left, j);
            quickSort(list, left, j - 1);
            quickSort(list, j + 1, right);
        }
    }

    @Override
    public List<Snapshot> sortAndReturnSnapshots(List<Integer> data) {
        List<Snapshot> snapshots = new ArrayList<>();
        if (data == null || data.size() < 2) {
            snapshots.add(makeSnapshotOf(data, -1));
            return snapshots;
        }

        List<ElementWithIndex> elements = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            elements.add(new ElementWithIndex(data.get(i), i));
        }
        snapshots.add(new SnapshotWithIndex(cloneElements(elements), -1));

        quickSortWithSnapshots(elements, 0, elements.size() - 1, snapshots);

        snapshots.add(new SnapshotWithIndex(cloneElements(elements), -1));
        return snapshots;
    }

    private void quickSortWithSnapshots(List<ElementWithIndex> elems, int left, int right, List<Snapshot> snapshots) {
        if (left < right) {
            int pivotIndex = left + random.nextInt(right - left + 1);
            ElementWithIndex tmp = elems.get(pivotIndex);
            elems.set(pivotIndex, elems.get(left));
            elems.set(left, tmp);
            int p = partitionWithSnapshots(elems, left, right, snapshots);
            quickSortWithSnapshots(elems, left, p - 1, snapshots);
            quickSortWithSnapshots(elems, p + 1, right, snapshots);
        }
    }

    private int partitionWithSnapshots(List<ElementWithIndex> elems, int left, int right, List<Snapshot> snapshots) {
        int pivotValue = elems.get(left).getValue();
        ElementWithIndex pivot = elems.get(left);
        int i = left + 1;
        int j = right;
        while (true) {
            while (i <= j && elems.get(i).getValue() <= pivotValue) i++;
            while (i <= j && elems.get(j).getValue() > pivotValue) j--;
            if (i > j) break;
            ElementWithIndex temp = elems.get(i);
            elems.set(i, elems.get(j));
            elems.set(j, temp);
            snapshots.add(new SnapshotWithIndex(cloneElements(elems), i));
        }
        elems.set(left, elems.get(j));
        elems.set(j, pivot);
        snapshots.add(new SnapshotWithIndex(cloneElements(elems), j));
        return j;
    }
}
