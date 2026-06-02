package algorithms;

import common.Snapshot;
import common.SnapshotProducingStrategy;

import java.util.ArrayList;
import java.util.List;

public class NoSortStrategy implements SortStrategy<Integer>, SnapshotProducingStrategy {

    @Override
    public String toString() {
        return "None";
    }

    @Override
    public void sort(List<Integer> list) {
        // Do nothing.
    }

    @Override
    public List<Snapshot> sortAndReturnSnapshots(List<Integer> data) {
        // Return an empty list so that nothing is drawn.
        return new ArrayList<>();
    }
}
