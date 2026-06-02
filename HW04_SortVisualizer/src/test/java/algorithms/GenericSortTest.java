package algorithms;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * GenericSortTest verifies that sorting algorithms correctly handle
 * custom types. Here, the Animal class (defined as a static inner class)
 * is sorted alphabetically by name.
 */
public class GenericSortTest {

    // Define a custom class for testing purposes.
    private static class Animal implements Comparable<Animal> {
        private final String name;
        private final int weight;  // weight not used for sorting here

        public Animal(String name, int weight) {
            this.name = name;
            this.weight = weight;
        }

        @Override
        public int compareTo(Animal other) {
            return this.name.compareToIgnoreCase(other.name);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    // Helper method to verify alphabetical order.
    private boolean isSortedAlphabetically(List<Animal> animals) {
        for (int i = 0; i < animals.size() - 1; i++) {
            if (animals.get(i).compareTo(animals.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }

    // Helper method to generate a list of Animal objects.
    private List<Animal> generateAnimalList() {
        return new ArrayList<>(Arrays.asList(
                new Animal("Zebra", 400),
                new Animal("Elephant", 6000),
                new Animal("Dog", 200),
                new Animal("Cat", 150),
                new Animal("Bear", 500),
                new Animal("Bird", 300),
                new Animal("Chicken", 200),
                new Animal("Pig", 100),
                new Animal("Sheep", 100),
                new Animal("Koala", 100),
                new Animal("Lion", 420),
                new Animal("Lizard", 200),
                new Animal("Antelope", 300),
                new Animal("Giraffe", 800),
                new Animal("Buffalo", 900),
                new Animal("Hyena", 200)
        ));
    }


    /**
     * Utility method to print the list of Animal objects in sorted order.
     * This method prints each Animal on its own line using the Animal's toString() method.
     */
    private void printAnimals(List<Animal> animals) {
        System.out.println("Sorted animals:");
        for (Animal a : animals) {
            System.out.println(a);
        }
    }

    @Test
    public void testGenericInsertionSort() {
        InsertionSortStrategy<Animal> sorter = new InsertionSortStrategy<>();
        List<Animal> animals = generateAnimalList();
        sorter.sort(animals);
        assertTrue(isSortedAlphabetically(animals), "InsertionSort should sort animals alphabetically.");
        // printAnimals(animals);
    }

    @Test
    public void testGenericSelectionSort() {
        SelectionSortStrategy<Animal> sorter = new SelectionSortStrategy<>();
        List<Animal> animals = generateAnimalList();
        sorter.sort(animals);
        assertTrue(isSortedAlphabetically(animals), "SelectionSort should sort animals alphabetically.");
        // printAnimals(animals);
    }

    @Test
    public void testGenericQuickSortRandomPivotStrategy() {
        QuickSortRandomPivotStrategy<Animal> sorter = new QuickSortRandomPivotStrategy<>();
        List<Animal> animals = generateAnimalList();
        sorter.sort(animals);
        assertTrue(isSortedAlphabetically(animals), "QuickSortRandom should sort animals alphabetically.");
        // printAnimals(animals);
    }

    @Test
    public void testGenericQuickSortPivotThreeLocationsStrategy() {
        QuickSortPivotThreeLocationsStrategy<Animal> sorter = new QuickSortPivotThreeLocationsStrategy<>();
        List<Animal> animals = generateAnimalList();
        sorter.sort(animals);
        assertTrue(isSortedAlphabetically(animals), "QuickSortPivot should sort animals alphabetically.");
        // printAnimals(animals);
    }

    @Test
    public void testGenericQuickSortThreeWayStrategy() {
        QuickSortThreeWayStrategy<Animal> sorter = new QuickSortThreeWayStrategy<>();
        List<Animal> animals = generateAnimalList();
        sorter.sort(animals);
        assertTrue(isSortedAlphabetically(animals), "QuickSortThreeWay should sort animals alphabetically.");
        // printAnimals(animals);
    }


    @Test
    public void testGenericMergeSort() {
        MergeSortStrategy<Animal> sorter = new MergeSortStrategy<>();
        List<Animal> animals = generateAnimalList();
        sorter.sort(animals);
        assertTrue(isSortedAlphabetically(animals), "MergeSort should sort animals alphabetically.");
        // printAnimals(animals);
    }
}