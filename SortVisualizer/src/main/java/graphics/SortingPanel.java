package graphics;

import common.Snapshot;
import common.SnapshotProducingStrategy;
import algorithms.SortStrategy;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A panel to visualize sorting algorithms.
 * It manages snapshots returned by a SnapshotProducingStrategy.
 */
public class SortingPanel extends JPanel {

    private boolean reverseOrder = false;
    private boolean showIndex = false;
    private int timeDelay = 100;
    private Color barColor;
    private JComboBox<SortStrategy<Integer>> algoCombo;
    private List<Integer> data;

    // Now we store Snapshot objects instead of raw List<Integer>
    private List<Snapshot> snapshots = new ArrayList<>();
    private int currentIndex = 0;
    private Timer timer; // for continuous animation

    private static final int PREF_WIDTH = 300;
    private static final int PREF_HEIGHT = 350;

    public SortingPanel(Color color) {
        this.barColor = color;
        setPreferredSize(new Dimension(PREF_WIDTH, PREF_HEIGHT));
        setLayout(new BorderLayout());

        // Combo box to choose algorithm
        algoCombo = new JComboBox<>();
        add(algoCombo, BorderLayout.NORTH);
    }

    public void setReverseOrder(boolean reverseOrder) {
        this.reverseOrder = reverseOrder;
        repaint();
    }

    public void setShowIndex(boolean showIndex) {
        this.showIndex = showIndex;
        repaint();
    }

    /** Called by MainFrame to add a possible algorithm to the combo box. */
    public void addAlgorithm(SortStrategy<Integer> algo) {
        algoCombo.addItem(algo);
    }

    /** Called by MainFrame to load new data. */
    public void setData(List<Integer> newData) {
        this.data = newData;
        snapshots.clear();
        currentIndex = 0;
        repaint();
    }

    /** Called to trigger a new sort, generating snapshots. */
    public void sortData() {
        snapshots.clear();
        currentIndex = 0;

        if (data == null || data.size() < 2) {
            repaint();
            return;
        }

        // Which algorithm is chosen?
        SortStrategy<Integer> selectedAlgo = (SortStrategy<Integer>) algoCombo.getSelectedItem();
        if (selectedAlgo == null) {
            repaint();
            return;
        }

        // Make a copy to avoid mutating the original
        List<Integer> copy = new ArrayList<>(data);

        if (selectedAlgo instanceof SnapshotProducingStrategy) {
            // This means it can produce snapshots
            SnapshotProducingStrategy snapAlgo = (SnapshotProducingStrategy) selectedAlgo;
            // We store all the snapshots it returns
            snapshots = snapAlgo.sortAndReturnSnapshots(copy);
        } else {
            // A non-snapshot algorithm: just sort once
            selectedAlgo.sort(copy);
            // Create a single "dummy" snapshot (if you like)
            snapshots.add(new Snapshot() {
                @Override
                public List<Integer> getValues() {
                    return new ArrayList<>(copy);
                }
                @Override
                public int getHighlightIndex() {
                    return -1;
                }
            });
        }

        // Start with the first snapshot
        currentIndex = 0;
        repaint();
    }

    /** Step forward by one snapshot. */
    public void nextStep() {
        if (snapshots.isEmpty()) return;
        currentIndex = Math.min(currentIndex + 1, snapshots.size() - 1);
        repaint();
    }

    /** Step backward by one snapshot. */
    public void prevStep() {
        if (snapshots.isEmpty()) return;
        currentIndex = Math.max(currentIndex - 1, 0);
        repaint();
    }

    /** Start continuous animation (the "Continue" button). */
    public void startAnimation() {
        if (timer != null && timer.isRunning()) {
            return;
        }
        timer = new Timer(timeDelay, e -> {
            if (currentIndex < snapshots.size() - 1) {
                currentIndex++;
                repaint();
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    /** Pause the animation. */
    public void pauseAnimation() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Make room for combo box
        int comboHeight = algoCombo.getHeight();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(0, comboHeight);

        // Fill background with white
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight() - comboHeight);

        // Retrieve the current snapshot
        Snapshot snapshot = null;
        if (!snapshots.isEmpty()) {
            snapshot = snapshots.get(currentIndex);
        } else {
            g2.dispose();
            return;
        }

        if (snapshot == null) {
            // If no snapshots but we have data, show the raw data as fallback
            if (data != null) {
                drawBars(g2, data, -1, null);
            }
        } else {
            // We have a valid Snapshot
            List<Integer> values = snapshot.getValues();
            int highlightIndex = snapshot.getHighlightIndex();
            List<Integer> originalIndices = snapshot.getOriginalIndices(); // might be null
            drawBars(g2, values, highlightIndex, originalIndices);
        }

        g2.dispose();
    }

    /**
     * Draw bars for the given array of integers, plus a highlight index,
     * and optionally some originalIndices for labeling.
     */
    private void drawBars(Graphics2D g, List<Integer> arr, int highlightIndex, List<Integer> originalIndices) {
        int width = getWidth();
        int height = getHeight() - algoCombo.getHeight();
        int size = arr.size();
        if (size == 0) return;

        int barWidth = Math.max(1, width / size);
        int minVal = Collections.min(arr);
        int maxVal = Collections.max(arr);
        int range = Math.max(1, maxVal - minVal);

        float[] hsb = Color.RGBtoHSB(barColor.getRed(), barColor.getGreen(), barColor.getBlue(), null);

        for (int i = 0; i < size; i++) {
            int value = arr.get(i);
            double normalized = (double)(value - minVal) / range;
            int barHeight = (int)(normalized * height);
            int x = i * barWidth;
            int y = height - barHeight;

            // Adjust brightness
            float newBrightness = 0.90f - 0.45f * (float) normalized;
            newBrightness = Math.max(0f, Math.min(1f, newBrightness));
            Color gradientColor = Color.getHSBColor(hsb[0], hsb[1], newBrightness);
            g.setColor(gradientColor);
            g.fillRect(x, y, barWidth - 2, barHeight);

            // Draw border if this bar is the highlight
            if (i == highlightIndex) {
                g.setColor(Color.BLACK);
                g.drawRect(x, y, barWidth - 2, barHeight);
            }

            // If few enough elements, label the bar
            if (size <= 15) {
                String label = String.valueOf(value);
                if (showIndex && originalIndices != null && i < originalIndices.size()) {
                    label = "[" + originalIndices.get(i) + "] " + label;
                }
                g.setFont(new Font("SansSerif", Font.BOLD, 12));
                FontMetrics fm = g.getFontMetrics();
                int textWidth = fm.stringWidth(label);
                int textHeight = fm.getHeight();
                int textX = x + (barWidth - 2 - textWidth) / 2;
                // textY is set to be a bit above the bottom of the bar
                int textY = height - 5;

                // Draw a semi-transparent background box for better readability
                // We will use a white color with 200/255 alpha (roughly 78% opacity)
                // We can use 40% 100/255 alpha to make it less 'white-ish'
                Color bgColor = new Color(255, 255, 255, 100);
                g.setColor(bgColor);
                // Fill a rectangle slightly larger than the text
                g.fillRect(textX - 2, textY - fm.getAscent(), textWidth + 4, textHeight);

                // Draw the text over the background box
                g.setColor(Color.BLACK);
                g.drawString(label, textX, textY);
            }
        }
    }
}
