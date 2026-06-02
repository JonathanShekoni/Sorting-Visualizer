package algorithms;

import common.ElementWithIndex;
import common.Snapshot;
import common.SnapshotProducingStrategy;
import common.SnapshotWithIndex;

import java.util.ArrayList;
import java.util.List;

import static common.SnapshotUtils.*;

public class InsertionSortStrategy<T extends Comparable<T>>
        implements SortStrategy<T>, SnapshotProducingStrategy {

    @Override
    public String toString() {
        return "InsertionSort";
    }

    @Override
    public void sort(List<T> list) {
        if (list == null || list.size() < 2) return;
        for (int i = 1; i < list.size(); i++) {
            T key = list.get(i);
            int j = i - 1;
            while (j >= 0 && list.get(j).compareTo(key) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
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

        for (int i = 1; i < elements.size(); i++) {
            ElementWithIndex key = elements.get(i);
            int j = i - 1;
            while (j >= 0 && elements.get(j).getValue() > key.getValue()) {
                elements.set(j + 1, elements.get(j));
                snapshots.add(new SnapshotWithIndex(cloneElements(elements), j + 1));
                j--;
            }
            elements.set(j + 1, key);
            snapshots.add(new SnapshotWithIndex(cloneElements(elements), j + 1));
        }

        snapshots.add(new SnapshotWithIndex(cloneElements(elements), -1));
        return snapshots;
    }
}
