package algorithms;

import common.ElementWithIndex;
import common.Snapshot;
import common.SnapshotProducingStrategy;
import common.SnapshotWithIndex;

import java.util.ArrayList;
import java.util.List;
import static common.SnapshotUtils.*;

public class BubbleSortStrategy<T extends Comparable<T>>
        implements SortStrategy<T>, SnapshotProducingStrategy {

    @Override
    public String toString() {
        return "BubbleSort";
    }

    @Override
    public void sort(List<T> list) {
        // Plain bubble sort with no snapshots
        if (list == null || list.size() < 2) return;
        boolean swapped;
        int n = list.size();
        do {
            swapped = false;
            for (int i = 0; i < n - 1; i++) {
                if (list.get(i).compareTo(list.get(i + 1)) > 0) {
                    swap(list, i, i + 1);
                    swapped = true;
                }
            }
            n--;
        } while (swapped);
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

        // 2) Capture the initial unsorted state (using a deep copy).
        snapshots.add(new SnapshotWithIndex(cloneElements(elements), -1));

        // 3) Bubble sort—with snapshots.
        int n = elements.size();
        boolean swapped = false;
        do {
            for (int i = 0; i < n - 1; i++) {
                // Always capture a snapshot for this comparison.
                snapshots.add(new SnapshotWithIndex(cloneElements(elements), i));
                if (elements.get(i).getValue() > elements.get(i + 1).getValue()) {
                    // Swap the two elements.
                    ElementWithIndex temp = elements.get(i);
                    elements.set(i, elements.get(i + 1));
                    elements.set(i + 1, temp);
                    swapped = true;
                    // Capture a snapshot after the swap, highlighting index i+1.
                    snapshots.add(new SnapshotWithIndex(cloneElements(elements), i + 1));
                }
            }
            // If no swaps occurred during this pass, capture a snapshot with a default highlight (e.g., 0).
            if (!swapped) {
                snapshots.add(new SnapshotWithIndex(cloneElements(elements), 0));
            }
            n--;
        } while (n > 1 && swapped);

        // 4) Final snapshot (sorted, no highlight).
        snapshots.add(new SnapshotWithIndex(cloneElements(elements), -1));
        return snapshots;
    }
}
