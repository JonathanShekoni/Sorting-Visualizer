package common;

import java.util.List;

/** A generic interface for any sorting snapshot. */
public interface Snapshot {
    /** Returns the list of values (for bar heights). */
    List<Integer> getValues();
    /** Returns which element is highlighted, or -1. */
    int getHighlightIndex();
    /** Optionally returns original indices for stable sorts, else null. */
    default List<Integer> getOriginalIndices() {
        return null;
    }
}
