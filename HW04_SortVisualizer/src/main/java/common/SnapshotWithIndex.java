package common;

import java.util.ArrayList;
import java.util.List;

/**
 * A single snapshot class for *all* sorts, storing (value, originalIndex).
 */
public class SnapshotWithIndex implements Snapshot {

    private final List<ElementWithIndex> elements;
    private final int highlightIndex;  // which bar to visually highlight

    public SnapshotWithIndex(List<ElementWithIndex> elements, int highlightIndex) {
        // Make a copy so the snapshot is “frozen”
        this.elements = new ArrayList<>(elements);
        this.highlightIndex = highlightIndex;
    }

    @Override
    public List<Integer> getValues() {
        // Return just the values (used to draw bar heights)
        List<Integer> vals = new ArrayList<>();
        for (ElementWithIndex e : elements) {
            vals.add(e.getValue());
        }
        return vals;
    }

    @Override
    public int getHighlightIndex() {
        return highlightIndex;
    }

    @Override
    public List<Integer> getOriginalIndices() {
        // Return the original indices in parallel with getValues()
        List<Integer> inds = new ArrayList<>();
        for (ElementWithIndex e : elements) {
            inds.add(e.getOriginalIndex());
        }
        return inds;
    }

    /** For debugging/logging. */
    @Override
    public String toString() {
        return "SnapshotWithIndex: " + elements + " [highlight=" + highlightIndex + "]";
    }
}
