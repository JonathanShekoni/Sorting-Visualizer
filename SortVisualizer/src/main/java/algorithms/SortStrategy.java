package algorithms;

import java.util.List;

/**
 * @param <T> the type of elements to be sorted.
 * @version 1.0, 2023-03-11
 * @author Maria Hybinette
 * @copyright Copyright (c) 2023-2024, Maria Hybinette. All rights reserved.
 *
 */
public interface SortStrategy<T> {
    /**
     * Sorts the given list of elements in place.
     *
     * @param list the list of elements to be sorted
     */
    void sort(List<T> list);


    /**
     * A default utility method to swap two elements in a list,
     * useful for in-place sorting algorithms (e.g., QuickSort,
     * SelectionSort)
     *
     * Algorithms that don't do direct swapping (e.g., counting sort,
     * merge sort) can simply ignore this.
     */
    default void swap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}
