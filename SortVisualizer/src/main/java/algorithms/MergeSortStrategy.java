package algorithms;

import common.ElementWithIndex;
import common.Snapshot;
import common.SnapshotProducingStrategy;
import common.SnapshotWithIndex;

import java.util.ArrayList;
import java.util.List;

import static common.SnapshotUtils.*;

public class MergeSortStrategy<T extends Comparable<T>>
        implements SortStrategy<T>, SnapshotProducingStrategy {

    @Override
    public String toString() {
        return "MergeSort";
    }

    @Override
    public void sort(List<T> list) {
        if (list == null || list.size() < 2) return;
        List<T> sorted = mergeSort(list);
        for (int i = 0; i < list.size(); i++) {
            list.set(i, sorted.get(i));
        }
    }

    private List<T> mergeSort(List<T> list) {
        if (list.size() <= 1) return new ArrayList<>(list);
        int mid = list.size() / 2;
        List<T> left = mergeSort(list.subList(0, mid));
        List<T> right = mergeSort(list.subList(mid, list.size()));
        return merge(left, right);
    }

    private List<T> merge(List<T> left, List<T> right) {
        List<T> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < left.size() && j < right.size()) {
            if (left.get(i).compareTo(right.get(j)) <= 0) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }
        while (i < left.size()) result.add(left.get(i++));
        while (j < right.size()) result.add(right.get(j++));
        return result;
    }

    @Override
    public List<Snapshot> sortAndReturnSnapshots(List<Integer> data) {
        List<Snapshot> snapshots = new ArrayList<>();
        if (data == null || data.size() < 2) {
            snapshots.add(makeSnapshotOf(data, -1));
            return snapshots;
        }

        List<ElementWithIndex> elements = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            elements.add(new ElementWithIndex(data.get(i), i));
        }
        snapshots.add(new SnapshotWithIndex(cloneElements(elements), -1));

        mergeSortWithSnapshots(elements, 0, elements.size() - 1, snapshots);

        snapshots.add(new SnapshotWithIndex(cloneElements(elements), -1));
        return snapshots;
    }

    private void mergeSortWithSnapshots(List<ElementWithIndex> elements, int left, int right, List<Snapshot> snapshots) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        mergeSortWithSnapshots(elements, left, mid, snapshots);
        mergeSortWithSnapshots(elements, mid + 1, right, snapshots);
        mergeWithSnapshots(elements, left, mid, right, snapshots);
    }

    private void mergeWithSnapshots(List<ElementWithIndex> elements, int left, int mid, int right, List<Snapshot> snapshots) {
        List<ElementWithIndex> leftPart = new ArrayList<>(elements.subList(left, mid + 1));
        List<ElementWithIndex> rightPart = new ArrayList<>(elements.subList(mid + 1, right + 1));

        int i = 0, j = 0, k = left;
        while (i < leftPart.size() && j < rightPart.size()) {
            if (leftPart.get(i).getValue() <= rightPart.get(j).getValue()) {
                elements.set(k, leftPart.get(i++));
            } else {
                elements.set(k, rightPart.get(j++));
            }
            snapshots.add(new SnapshotWithIndex(cloneElements(elements), k));
            k++;
        }
        while (i < leftPart.size()) {
            elements.set(k, leftPart.get(i++));
            snapshots.add(new SnapshotWithIndex(cloneElements(elements), k));
            k++;
        }
        while (j < rightPart.size()) {
            elements.set(k, rightPart.get(j++));
            snapshots.add(new SnapshotWithIndex(cloneElements(elements), k));
            k++;
        }
    }
}
