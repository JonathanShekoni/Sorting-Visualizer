package common;

import java.util.List;

/**
 * An interface for algorithms that produce snapshots when sorting.
 */
public interface SnapshotProducingStrategy {
    /**
     * Sort the given data while producing multiple snapshots.
     * Each snapshot is a `Snapshot` instance describing
     * the current state of the array at some step of the algorithm.
     *
     * @param data original data to sort
     * @return a list of snapshot objects (for visualization)
     */


    List<Snapshot> sortAndReturnSnapshots(List<Integer> data);
}
