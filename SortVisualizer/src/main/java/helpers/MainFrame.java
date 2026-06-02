package helpers;

import algorithms.*;
import graphics.SortingPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * MainFrame with three SortingPanels side by side.
 */
public class MainFrame extends JFrame {

    private SortingPanel panelLeft;
    private SortingPanel panelMiddle;
    private SortingPanel panelRight;

    private static final int MAX_VALUE = 100;

    private void addAlgorithmOptions(SortingPanel panel) {
        panel.addAlgorithm(new NoSortStrategy());
        panel.addAlgorithm(new BubbleSortStrategy<>());
        panel.addAlgorithm(new QuickSortStrategy<>());
        panel.addAlgorithm(new QuickSortLeftStrategy<>());
        panel.addAlgorithm(new CountingSortStrategy<>());
        panel.addAlgorithm(new SelectionSortStrategy<>());
        panel.addAlgorithm(new InsertionSortStrategy<>());
        panel.addAlgorithm(new MergeSortStrategy<>());
        panel.addAlgorithm(new QuickSortRandomPivotStrategy<>());
        panel.addAlgorithm(new QuickSortPivotThreeLocationsStrategy<>());
        panel.addAlgorithm(new CountingSortNotStableStrategy());
        panel.addAlgorithm(new QuickSortThreeWayStrategy<>());
        panel.addAlgorithm(new CountingSortNotStableLookUpTableStrategy());
        panel.addAlgorithm(new BucketSortStrategy<>());
    }

    public MainFrame() {
        super("3-Panel Sorting Visualizer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create 3 SortingPanels with different colors
        panelLeft = new SortingPanel(Color.BLUE);
        panelMiddle = new SortingPanel(Color.GREEN);
        panelRight = new SortingPanel(Color.RED);

        // Add common algorithm options to each panel using the helper method
        addAlgorithmOptions(panelLeft);
        addAlgorithmOptions(panelMiddle);
        addAlgorithmOptions(panelRight);

        // Layout: side by side
        JPanel panelContainer = new JPanel(new GridLayout(1, 3));
        panelContainer.add(panelLeft);
        panelContainer.add(panelMiddle);
        panelContainer.add(panelRight);
        add(panelContainer, BorderLayout.CENTER);

        // Create a slider panel to select number of elements
        JPanel sliderPanel = new JPanel();
        JLabel sliderLabel = new JLabel("Number of Elements: ");
        // Slider from 5 to 200, initial value 14
        JSlider elementSlider = new JSlider(JSlider.HORIZONTAL, 5, 200, 14);
        elementSlider.setMajorTickSpacing(50);
        elementSlider.setMinorTickSpacing(5);
        elementSlider.setPaintTicks(true);
        elementSlider.setPaintLabels(true);
        JLabel currentValueLabel = new JLabel(String.valueOf(elementSlider.getValue()));
        elementSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentValueLabel.setText(String.valueOf(elementSlider.getValue()));
            }
        });
        sliderPanel.add(sliderLabel);
        sliderPanel.add(elementSlider);
        sliderPanel.add(currentValueLabel);

        // Create a combo box for data generation type.
        String[] dataTypes = { "Random", "Almost Sorted", "Ascending", "Descending" };
        JComboBox<String> dataTypeCombo = new JComboBox<>(dataTypes);
        sliderPanel.add(new JLabel("      Data Set: "));
        sliderPanel.add(dataTypeCombo);

        // Create a checkbox for reversing the data before sorting.
        // (This checkbox applies globally; in our case, we will use it only for the left panel.)
        JCheckBox chkReverseData = new JCheckBox("Reverse Data (Left Panel Only)");
        sliderPanel.add(chkReverseData);

        // Create a panel for control buttons
        JPanel buttonPanel = new JPanel();
        JButton btnGenerate = new JButton("Generate Data");
        JButton btnSort = new JButton("Sort");
        JButton btnPause = new JButton("Pause");
        JButton btnNext = new JButton("Next");
        JButton btnPrev = new JButton("Previous");
        JButton btnContinue = new JButton("Continue");
        JButton btnLoadCSV = new JButton("Load CSV");

        buttonPanel.add(btnGenerate);
        buttonPanel.add(btnSort);
        buttonPanel.add(btnPause);
        buttonPanel.add(btnNext);
        buttonPanel.add(btnPrev);
        buttonPanel.add(btnContinue);
        buttonPanel.add(btnLoadCSV);

        JCheckBox chkShowIndex = new JCheckBox("Show Index");
        chkShowIndex.addActionListener(e -> {
            boolean showIndex = chkShowIndex.isSelected();
            // For each panel, call a setter to toggle index display
            panelLeft.setShowIndex(showIndex);
            panelMiddle.setShowIndex(showIndex);
            panelRight.setShowIndex(showIndex);
        });
        buttonPanel.add(chkShowIndex);

        // Combine slider and button panels into a single control panel.
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(sliderPanel, BorderLayout.NORTH);
        controlPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(controlPanel, BorderLayout.SOUTH);

        // Button actions
        btnGenerate.addActionListener(e -> {
            panelLeft.pauseAnimation();
            panelMiddle.pauseAnimation();
            panelRight.pauseAnimation();

            // Use the slider value for number of elements
            int numElements = elementSlider.getValue();

            // Determine the selected data type.
            String type = (String) dataTypeCombo.getSelectedItem();
            List<Integer> generatedData;
            switch (type) {
                case "Almost Sorted":
                    generatedData = generateAlmostSortedList(numElements, MAX_VALUE);
                    break;
                case "Ascending":
                    generatedData = generateAscendingList(numElements, MAX_VALUE);
                    break;
                case "Descending":
                    generatedData = generateDescendingList(numElements, MAX_VALUE);
                    break;
                case "Random":
                default:
                    generatedData = generateRandomList(numElements, MAX_VALUE);
                    break;
            }

            // Create a reversed copy for the left panel if the checkbox is selected.
            List<Integer> leftData = new ArrayList<>(generatedData);
            if (chkReverseData.isSelected()) {
                Collections.reverse(leftData);
            }

            // Provide each panel with a fresh copy.
            panelLeft.setData(leftData);
            panelMiddle.setData(new ArrayList<>(generatedData));
            panelRight.setData(new ArrayList<>(generatedData));
        });

        btnSort.addActionListener(e -> {
            panelLeft.sortData();
            panelMiddle.sortData();
            panelRight.sortData();
        });

        btnPause.addActionListener(e -> {
            panelLeft.pauseAnimation();
            panelMiddle.pauseAnimation();
            panelRight.pauseAnimation();
        });

        btnNext.addActionListener(e -> {
            panelLeft.nextStep();
            panelMiddle.nextStep();
            panelRight.nextStep();
        });

        btnPrev.addActionListener(e -> {
            panelLeft.prevStep();
            panelMiddle.prevStep();
            panelRight.prevStep();
        });

        btnContinue.addActionListener(e -> {
            panelLeft.startAnimation();
            panelMiddle.startAnimation();
            panelRight.startAnimation();
        });

        btnLoadCSV.addActionListener(e -> {
            try {
                // Get the resources directory "input" from the classpath
                java.net.URL url = getClass().getResource("/input");
                if (url == null) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Resource folder '/input' not found!");
                    return;
                }
                // Convert URL to a File
                File resourcesDir = new File(url.toURI());
                JFileChooser chooser = new JFileChooser(resourcesDir);
                int returnVal = chooser.showOpenDialog(MainFrame.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    String filePath = chooser.getSelectedFile().getAbsolutePath();
                    // Read CSV file as integers using the CSVReader helper.
                    List<Integer> csvData = helpers.CSVReader.readCSV(filePath, Integer::valueOf);
                    // Provide each panel with a fresh copy of CSV data.
                    panelLeft.setData(new ArrayList<>(csvData));
                    panelMiddle.setData(new ArrayList<>(csvData));
                    panelRight.setData(new ArrayList<>(csvData));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(MainFrame.this, "Error loading CSV: " + ex.getMessage());
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Utility methods for generating data

    private List<Integer> generateRandomList(int size, int maxValue) {
        Random rand = new Random();
        List<Integer> arr = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            arr.add(rand.nextInt(maxValue));
        }
        return arr;
    }

    private List<Integer> generateAscendingList(int size, int maxValue) {
        List<Integer> arr = generateRandomList(size, maxValue);
        Collections.sort(arr);
        return arr;
    }

    private List<Integer> generateDescendingList(int size, int maxValue) {
        List<Integer> arr = generateRandomList(size, maxValue);
        Collections.sort(arr, Collections.reverseOrder());
        return arr;
    }

    private List<Integer> generateAlmostSortedList(int size, int maxValue) {
        List<Integer> arr = generateAscendingList(size, maxValue);
        Random rand = new Random();
        int swaps = Math.max(1, size / 10); // swap about 10% of the elements
        for (int i = 0; i < swaps; i++) {
            int idx1 = rand.nextInt(size);
            int idx2 = rand.nextInt(size);
            Collections.swap(arr, idx1, idx2);
        }
        return arr;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
