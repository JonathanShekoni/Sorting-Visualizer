package common;

import java.util.ArrayList;
import java.util.List;

public class SnapshotUtils {

    /**
     * Converts a plain List of Integer into a SnapshotWithIndex.
     */
    public static Snapshot makeSnapshotOf(List<Integer> data, int highlight) {
        List<ElementWithIndex> list = new ArrayList<>();
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                list.add(new ElementWithIndex(data.get(i), i));
            }
        }
        return new SnapshotWithIndex(list, highlight);
    }

    /**
     * Handles the edge case for null or single-element data.
     */
    public static Snapshot emptyOrSingleSnapshot(List<Integer> data) {
        List<ElementWithIndex> elements = new ArrayList<>();
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                elements.add(new ElementWithIndex(data.get(i), i));
            }
        }
        return new SnapshotWithIndex(elements, -1);
    }

    /**
     * Creates a deep copy of a list of ElementWithIndex objects.
     */
    public static List<ElementWithIndex> cloneElements(List<ElementWithIndex> elements) {
        List<ElementWithIndex> copy = new ArrayList<>();
        for (ElementWithIndex e : elements) {
            copy.add(new ElementWithIndex(e.getValue(), e.getOriginalIndex()));
        }
        return copy;
    }
}