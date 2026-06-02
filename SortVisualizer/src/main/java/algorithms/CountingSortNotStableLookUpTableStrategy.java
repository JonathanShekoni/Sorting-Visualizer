package algorithms;

import common.ElementWithIndex;
import common.Snapshot;
import common.SnapshotProducingStrategy;
import common.SnapshotWithIndex;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import static common.SnapshotUtils.*;

public class CountingSortNotStableLookUpTableStrategy<T extends Integer>
        implements SortStrategy<T>, SnapshotProducingStrategy {

    @Override
    public String toString() {
        return "CountingSort Look-UpTable (Unstable)";
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

        @SuppressWarnings("unchecked")
        Deque<ElementWithIndex>[] stacks = new Deque[range];
        for (int i = 0; i < range; i++) {
            stacks[i] = new ArrayDeque<>();
        }

        for (ElementWithIndex e : elements) {
            stacks[e.getValue() - min].push(e);
        }

        snapshots.add(new SnapshotWithIndex(cloneElements(elements), -1));

        int idx = 0;
        for (int v = 0; v < range; v++) {
            while (!stacks[v].isEmpty()) {
                elements.set(idx, stacks[v].pop());
                snapshots.add(new SnapshotWithIndex(cloneElements(elements), idx));
                idx++;
            }
        }

        snapshots.add(new SnapshotWithIndex(cloneElements(elements), -1));
        return snapshots;
    }
}
