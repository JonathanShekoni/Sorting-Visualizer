package algorithms;

import common.ElementWithIndex;
import common.Snapshot;
import common.SnapshotProducingStrategy;
import common.SnapshotWithIndex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static common.SnapshotUtils.*;

public class CountingSortNotStableStrategy implements SortStrategy<Integer>, SnapshotProducingStrategy {

    @Override
    public String toString() {
        return "CountingSort (Unstable)";
    }

    @Override
    public void sort(List<Integer> list) {
        // 1) Produce all snapshots
        List<Snapshot> snapshots = sortAndReturnSnapshots(list);

        // 2) Grab the final snapshot (should be fully sorted)
        if (!snapshots.isEmpty()) {
            Snapshot finalSnap = snapshots.get(snapshots.size() - 1);
            if (finalSnap instanceof SnapshotWithIndex) {
                SnapshotWithIndex sw = (SnapshotWithIndex) finalSnap;
                List<Integer> sortedValues = sw.getValues();
                // 3) Overwrite the original list with these sorted values
                for (int i = 0; i < list.size(); i++) {
                    list.set(i, sortedValues.get(i));
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

        // 1) Convert raw data to a list of ElementWithIndex
        List<ElementWithIndex> elements = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            elements.add(new ElementWithIndex(data.get(i), i));
        }
        // Capture the initial unsorted state (make a clone so that later changes don't affect this snapshot)
        snapshots.add(new SnapshotWithIndex(cloneElements(elements), -1));


        // 2) Find the maximum value
        int max = Collections.max(data);
        int[] count = new int[max + 1];

        // 3) Count occurrences (using the value from each element)
        for (ElementWithIndex e : elements) {
            count[e.getValue()]++;
        }

        // 4) Reconstruct the sorted list in an UNSTABLE manner.
        // In an unstable sort, we update the value while leaving the original index unchanged.
        int index = 0;
        for (int val = 0; val < count.length; val++) {
            while (count[val] > 0) {
                elements.get(index).setValue(val);
                index++;
                count[val]--;
                // Capture a snapshot after each placement, highlighting the element just placed.
                snapshots.add(new SnapshotWithIndex(cloneElements(elements), index - 1));
            }
        }
        // Optionally add a final snapshot with no highlight
        snapshots.add(new SnapshotWithIndex(cloneElements(elements), -1));
        return snapshots;
    }
}
