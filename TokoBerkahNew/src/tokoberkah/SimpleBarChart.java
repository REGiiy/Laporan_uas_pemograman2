package tokoberkah;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SimpleBarChart extends JPanel {

    private List<String> labels;
    private List<Integer> values;

    public SimpleBarChart(List<String> labels, List<Integer> values) {
        this.labels = labels;
        this.values = values;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (labels == null || values == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLUE);

        int width = getWidth();
        int height = getHeight();

        int barWidth = width / (values.size() * 2);

        int max = values.stream().max(Integer::compare).orElse(1);

        for (int i = 0; i < values.size(); i++) {

            int barHeight = (int) ((double) values.get(i) / max * (height - 50));

            int x = i * (barWidth * 2) + 30;
            int y = height - barHeight - 30;

            g2.fillRect(x, y, barWidth, barHeight);

            g2.setColor(Color.BLACK);
            g2.drawString(labels.get(i), x, height - 10);

            g2.setColor(Color.BLUE);
        }
    }
}