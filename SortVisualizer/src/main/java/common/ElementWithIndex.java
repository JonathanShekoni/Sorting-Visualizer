package common;

/**
 * A small pair-like class for storing a value and its original index.
 */
public class ElementWithIndex {
    private int value;
    private int originalIndex;

    public ElementWithIndex(int value, int originalIndex) {
        this.value = value;
        this.originalIndex = originalIndex;
    }

    public int getValue() {
        return value;
    }

    public int getOriginalIndex() {
        return originalIndex;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setOriginalIndex(int originalIndex) {
        this.originalIndex = originalIndex;
    }

    @Override
    public String toString() {
        return "[" + value + ", origIdx=" + originalIndex + "]";
    }
}
