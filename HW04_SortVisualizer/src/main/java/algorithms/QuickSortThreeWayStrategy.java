package algorithms;

import common.ElementWithIndex;
import common.Snapshot;
import common.SnapshotProducingStrategy;
import common.SnapshotWithIndex;

import java.util.ArrayList;
import java.util.List;

import static common.SnapshotUtils.*;

public class QuickSortThreeWayStrategy<T extends Comparable<T>>
        implements SortStrategy<T>, SnapshotProducingStrategy {

    @Override
    public String toString() {
        return "QuickSort (3-Way, Left Pivot)";
    }

    @Override
    public void sort(List<T> list) {
        if (list == null || list.size() < 2) return;
        quickSort(list, 0, list.size() - 1);
    }

    private void quickSort(List<T> list, int left, int right) {
        if (left >= right) return;
        T pivot = list.get(left);
        int lt = left;
        int gt = right;
        int i = left + 1;
        while (i <= gt) {
            int cmp = list.get(i).compareTo(pivot);
            if (cmp < 0) {
                swap(list, lt, i);
                lt++;
                i++;
            } else if (cmp > 0) {
                swap(list, i, gt);
                gt--;
            } else {
                i++;
            }
        }
        quickSort(list, left, lt - 1);
        quickSort(list, gt + 1, right);
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
        if (left >= right) return;
        int pivotValue = elems.get(left).getValue();
        int lt = left;
        int gt = right;
        int i = left + 1;
        while (i <= gt) {
            int val = elems.get(i).getValue();
            if (val < pivotValue) {
                ElementWithIndex tmp = elems.get(lt);
                elems.set(lt, elems.get(i));
                elems.set(i, tmp);
                lt++;
                i++;
                snapshots.add(new SnapshotWithIndex(cloneElements(elems), i - 1));
            } else if (val > pivotValue) {
                ElementWithIndex tmp = elems.get(i);
                elems.set(i, elems.get(gt));
                elems.set(gt, tmp);
                gt--;
                snapshots.add(new SnapshotWithIndex(cloneElements(elems), gt + 1));
            } else {
                i++;
            }
        }
        snapshots.add(new SnapshotWithIndex(cloneElements(elems), lt));
        quickSortWithSnapshots(elems, left, lt - 1, snapshots);
        quickSortWithSnapshots(elems, gt + 1, right, snapshots);
    }
}
