package tokoberkah;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class SmoothTradingChart extends JPanel {

    private List<Double> data = new ArrayList<>();
    private int animationStep = 0;

    public SmoothTradingChart() {
        setBackground(Color.WHITE);

        // dummy data (nanti bisa dari DB)
        data.add(1200.0);
        data.add(1800.0);
        data.add(1500.0);
        data.add(2100.0);
        data.add(1700.0);
        data.add(2600.0);
        data.add(2000.0);

        // ANIMATION LOOP (SMOOTH)
        Timer timer = new Timer(40, e -> {
            animationStep++;
            if (animationStep > data.size() * 15) {
                animationStep = 0;
            }
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        if (data.size() < 2) return;

        double max = data.stream().max(Double::compare).orElse(1.0);
        double min = data.stream().min(Double::compare).orElse(0.0);

        int padding = 40;

        int stepX = (w - 2 * padding) / (data.size() - 1);

        // ================= DRAW GRID =================
        g2.setColor(new Color(240, 240, 240));
        for (int i = 0; i < 5; i++) {
            int y = padding + i * (h - 2 * padding) / 4;
            g2.drawLine(padding, y, w - padding, y);
        }

        // ================= BUILD LINE PATH =================
        Path2D path = new Path2D.Double();

        for (int i = 0; i < data.size(); i++) {

            int x = padding + i * stepX;

            double norm = (data.get(i) - min) / (max - min);
            int y = (int) (h - padding - norm * (h - 2 * padding));

            if (i == 0) path.moveTo(x, y);
            else path.lineTo(x, y);
        }

        // ================= GRADIENT COLOR (GREEN ↔ RED) =================
        for (int i = 0; i < data.size() - 1; i++) {

            int x1 = padding + i * stepX;
            int x2 = padding + (i + 1) * stepX;

            double v1 = data.get(i);
            double v2 = data.get(i + 1);

            double n1 = (v1 - min) / (max - min);
            double n2 = (v2 - min) / (max - min);

            int y1 = (int) (h - padding - n1 * (h - 2 * padding));
            int y2 = (int) (h - padding - n2 * (h - 2 * padding));

            // GREEN if naik, RED if turun
            if (v2 >= v1) {
                g2.setColor(new Color(0, 200, 120)); // green
            } else {
                g2.setColor(new Color(255, 80, 80)); // red
            }

            // glow effect
            g2.setStroke(new BasicStroke(4f));
            g2.drawLine(x1, y1, x2, y2);
        }

        // ================= POINTS (GLOW DOTS) =================
        for (int i = 0; i < data.size(); i++) {

            int x = padding + i * stepX;

            double norm = (data.get(i) - min) / (max - min);
            int y = (int) (h - padding - norm * (h - 2 * padding));

            // outer glow
            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillOval(x - 7, y - 7, 14, 14);

            // main dot
            g2.setColor(Color.WHITE);
            g2.fillOval(x - 5, y - 5, 10, 10);

            g2.setColor(Color.BLACK);
            g2.drawOval(x - 5, y - 5, 10, 10);
        }
    }
}