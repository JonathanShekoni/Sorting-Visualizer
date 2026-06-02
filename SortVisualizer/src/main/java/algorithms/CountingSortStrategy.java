package algorithms;

import common.ElementWithIndex;
import common.Snapshot;
import common.SnapshotProducingStrategy;
import common.SnapshotWithIndex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static common.SnapshotUtils.*;

public class CountingSortStrategy<T extends Integer>
        implements SortStrategy<T>, SnapshotProducingStrategy {

    @Override
    public String toString() {
        return "CountingSort (Stable)";
    }

    @Override
    public void sort(List<T> list) {
        List<Snapshot> snapshots = sortAndReturnSnapshots((List<Integer>) list);
        if (!snapshots.isEmpty()) {
            Snapshot finalSnap = snapshots.get(snapshots.size() - 1);
            if (finalSnap instanceof SnapshotWithIndex) {
                SnapshotWithIndex sw = (SnapshotWithIndex) finalSnap;
                List<Integer> sortedValues = sw.getValues();
                for (int i = 0; i < list.size(); i++) {
                    list.set(i, (T) sortedValues.get(i));
                }
            }
        }
    }

    @Override
    public List<Snapshot> sortAndReturnSnapshots(List<Integer> data) {
        List<Snapshot> snapshots = new ArrayList<>();
        if (data == null || data.isEmpty()) {
            snapshots.add(makeSnapshotOf(data, -1));
            return snapshots;
        }

        List<ElementWithIndex> elements = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            elements.add(new ElementWithIndex(data.get(i), i));
        }
        snapshots.add(new SnapshotWithIndex(cloneElements(elements), -1));

        int max = Collections.max(data);
        int min = Collections.min(data);
        int range = max - min + 1;
        int[] count = new int[range];

        for (ElementWithIndex e : elements) {
            count[e.getValue() - min]++;
        }

        snapshots.add(new SnapshotWithIndex(cloneElements(elements), -1));

        for (int i = 1; i < range; i++) {
            count[i] += count[i - 1];
        }

        snapshots.add(new SnapshotWithIndex(cloneElements(elements), -1));

        ElementWithIndex[] output = new ElementWithIndex[elements.size()];
        for (int i = elements.size() - 1; i >= 0; i--) {
            ElementWithIndex e = elements.get(i);
            int pos = count[e.getValue() - min] - 1;
            output[pos] = e;
            count[e.getValue() - min]--;
        }

        for (int i = 0; i < output.length; i++) {
            elements.set(i, output[i]);
            snapshots.add(new SnapshotWithIndex(cloneElements(elements), i));
        }

        snapshots.add(new SnapshotWithIndex(cloneElements(elements), -1));
        return snapshots;
    }
}
