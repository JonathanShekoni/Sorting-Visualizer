package algorithms;

import common.ElementWithIndex;
import common.Snapshot;
import common.SnapshotProducingStrategy;
import common.SnapshotWithIndex;

import java.util.ArrayList;
import java.util.List;

import static common.SnapshotUtils.*;

public class QuickSortPivotThreeLocationsStrategy<T extends Comparable<T>>
        implements SortStrategy<T>, SnapshotProducingStrategy {

    @Override
    public String toString() {
        return "QuickSort (Median-of-Three Pivot)";
    }

    @Override
    public void sort(List<T> list) {
        if (list == null || list.size() < 2) return;
        quickSort(list, 0, list.size() - 1);
    }

    private int medianOfThreeIndex(List<T> list, int left, int right) {
        int mid = left + (right - left) / 2;
        T a = list.get(left), b = list.get(mid), c = list.get(right);
        if (a.compareTo(b) <= 0 && b.compareTo(c) <= 0) return mid;
        if (a.compareTo(c) <= 0 && c.compareTo(b) <= 0) return right;
        return left;
    }

    private void quickSort(List<T> list, int left, int right) {
        if (left < right) {
            int pivotIndex = medianOfThreeIndex(list, left, right);
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

    private int medianOfThreeIndexElems(List<ElementWithIndex> elems, int left, int right) {
        int mid = left + (right - left) / 2;
        int a = elems.get(left).getValue();
        int b = elems.get(mid).getValue();
        int c = elems.get(right).getValue();
        if (a <= b && b <= c) return mid;
        if (a <= c && c <= b) return right;
        return left;
    }

    private void quickSortWithSnapshots(List<ElementWithIndex> elems, int left, int right, List<Snapshot> snapshots) {
        if (left < right) {
            int pivotIndex = medianOfThreeIndexElems(elems, left, right);
            ElementWithIndex tmp = elems.get(pivotIndex);
            elems.set(pivotIndex, elems.get(left));
            elems.set(left, tmp);
            snapshots.add(new SnapshotWithIndex(cloneElements(elems), left));
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
