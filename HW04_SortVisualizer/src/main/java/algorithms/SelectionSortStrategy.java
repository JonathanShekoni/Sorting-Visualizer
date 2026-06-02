package algorithms;

import common.ElementWithIndex;
import common.Snapshot;
import common.SnapshotProducingStrategy;
import common.SnapshotWithIndex;

import java.util.ArrayList;
import java.util.List;

import static common.SnapshotUtils.*;

public class SelectionSortStrategy<T extends Comparable<T>>
        implements SortStrategy<T>, SnapshotProducingStrategy {

    @Override
    public String toString() {
        return "SelectionSort (Unstable)";
    }

    @Override
    public void sort(List<T> list) {
        if (list == null || list.size() < 2) return;
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (list.get(j).compareTo(list.get(minIdx)) < 0) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                swap(list, i, minIdx);
            }
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

        int n = elements.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                snapshots.add(new SnapshotWithIndex(cloneElements(elements), minIdx));
                if (elements.get(j).getValue() < elements.get(minIdx).getValue()) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                ElementWithIndex temp = elements.get(i);
                elements.set(i, elements.get(minIdx));
                elements.set(minIdx, temp);
                snapshots.add(new SnapshotWithIndex(cloneElements(elements), minIdx));
            }
        }

        snapshots.add(new SnapshotWithIndex(cloneElements(elements), -1));
        return snapshots;
    }
}
