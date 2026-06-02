package algorithms;

import common.ElementWithIndex;
import common.Snapshot;
import common.SnapshotProducingStrategy;
import common.SnapshotWithIndex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static common.SnapshotUtils.*;

public class BucketSortStrategy<T extends Integer>
        implements SortStrategy<T>, SnapshotProducingStrategy {

    @Override
    public String toString() {
        return "BucketSort (Bonus)";
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
        if (data.size() == 1) {
            snapshots.add(makeSnapshotOf(data, -1));
            return snapshots;
        }

        List<ElementWithIndex> elements = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            elements.add(new ElementWithIndex(data.get(i), i));
        }
        snapshots.add(new SnapshotWithIndex(cloneElements(elements), -1));

        int min = Collections.min(data);
        int max = Collections.max(data);
        int numBuckets = data.size();

        @SuppressWarnings("unchecked")
        List<ElementWithIndex>[] buckets = new List[numBuckets];
        for (int i = 0; i < numBuckets; i++) {
            buckets[i] = new ArrayList<>();
        }

        for (ElementWithIndex e : elements) {
            int bucketIndex;
            if (max == min) {
                bucketIndex = 0;
            } else {
                bucketIndex = (e.getValue() - min) * numBuckets / (max - min + 1);
            }
            buckets[bucketIndex].add(e);
        }

        List<ElementWithIndex> flattened = flattenBuckets(buckets, numBuckets);
        for (int i = 0; i < flattened.size(); i++) {
            elements.set(i, flattened.get(i));
        }
        snapshots.add(new SnapshotWithIndex(cloneElements(elements), -1));

        for (int b = 0; b < numBuckets; b++) {
            List<ElementWithIndex> bucket = buckets[b];
            for (int i = 1; i < bucket.size(); i++) {
                ElementWithIndex key = bucket.get(i);
                int j = i - 1;
                while (j >= 0 && bucket.get(j).getValue() > key.getValue()) {
                    bucket.set(j + 1, bucket.get(j));
                    j--;
                }
                bucket.set(j + 1, key);
                List<ElementWithIndex> snap = flattenBuckets(buckets, numBuckets);
                for (int k = 0; k < elements.size(); k++) {
                    elements.set(k, snap.get(k));
                }
                snapshots.add(new SnapshotWithIndex(cloneElements(elements), j + 1));
            }
        }

        int idx = 0;
        for (int b = 0; b < numBuckets; b++) {
            for (ElementWithIndex e : buckets[b]) {
                elements.set(idx, e);
                snapshots.add(new SnapshotWithIndex(cloneElements(elements), idx));
                idx++;
            }
        }

        snapshots.add(new SnapshotWithIndex(cloneElements(elements), -1));
        return snapshots;
    }

    private List<ElementWithIndex> flattenBuckets(List<ElementWithIndex>[] buckets, int numBuckets) {
        List<ElementWithIndex> result = new ArrayList<>();
        for (int b = 0; b < numBuckets; b++) {
            result.addAll(buckets[b]);
        }
        return result;
    }
}
