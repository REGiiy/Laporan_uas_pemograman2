package tokoberkah;

import java.awt.*;
import java.sql.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Dashboard extends JFrame {

    private JLabel lblTotalBarang;
    private JLabel lblTotalCustomer;
    private JLabel lblTotalTransaksi;
    private JLabel lblTotalPendapatan;

    private ChartPanel chartPanel;

    public Dashboard() {

        setTitle("Toko Berkah - Dashboard PRO");
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());

        // ================= SIDEBAR =================
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(240, 720));
        sidebar.setBackground(new Color(88, 28, 135));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("🛍 TOKO BERKAH");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Arial", Font.BOLD, 18));
        logo.setBorder(new EmptyBorder(20, 20, 20, 20));

        sidebar.add(logo);

        JButton btnDashboard = createMenu("📊 Dashboard");
        JButton btnBarang = createMenu("📦 Barang");
        JButton btnCustomer = createMenu("👥 Customer");
        JButton btnTransaksi = createMenu("🧾 Transaksi");
        JButton btnLaporan = createMenu("📋 Laporan");

        sidebar.add(btnDashboard);
        sidebar.add(btnBarang);
        sidebar.add(btnCustomer);
        sidebar.add(btnTransaksi);
        sidebar.add(btnLaporan);

        // ================= CONTENT =================
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(245, 243, 255));

        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setPreferredSize(new Dimension(0, 60));
        topbar.setBackground(Color.WHITE);
        topbar.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(88, 28, 135));
        topbar.add(title, BorderLayout.WEST);

        // ================= CARDS =================
        JPanel cards = new JPanel(new GridLayout(2, 2, 15, 15));
        cards.setBorder(new EmptyBorder(20, 20, 20, 20));
        cards.setBackground(new Color(245, 243, 255));

        lblTotalBarang = createValue();
        lblTotalCustomer = createValue();
        lblTotalTransaksi = createValue();
        lblTotalPendapatan = createValue();

        cards.add(card("📦 Barang", lblTotalBarang, new Color(99, 102, 241)));
        cards.add(card("👥 Customer", lblTotalCustomer, new Color(168, 85, 247)));
        cards.add(card("🧾 Transaksi", lblTotalTransaksi, new Color(59, 130, 246)));
        cards.add(card("💰 Pendapatan", lblTotalPendapatan, new Color(16, 185, 129)));

        // ================= CHART =================
        chartPanel = new ChartPanel();
        JPanel chartWrapper = new JPanel(new BorderLayout());
        chartWrapper.setBorder(BorderFactory.createTitledBorder("📈 Grafik Omzet 7 Hari"));
        chartWrapper.setBackground(Color.WHITE);
        chartWrapper.add(chartPanel, BorderLayout.CENTER);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBorder(new EmptyBorder(10, 20, 20, 20));
        center.setBackground(new Color(245, 243, 255));

        center.add(cards, BorderLayout.NORTH);
        center.add(chartWrapper, BorderLayout.CENTER);

        content.add(topbar, BorderLayout.NORTH);
        content.add(center, BorderLayout.CENTER);

        root.add(sidebar, BorderLayout.WEST);
        root.add(content, BorderLayout.CENTER);

        setContentPane(root);

        // ================= EVENTS =================
        btnDashboard.addActionListener(e -> refresh());
        btnBarang.addActionListener(e -> new FormBarang().setVisible(true));
        btnCustomer.addActionListener(e -> new FormCustomer().setVisible(true));
        btnTransaksi.addActionListener(e -> new FormTransaksi().setVisible(true));
        btnLaporan.addActionListener(e -> new FormLaporan().setVisible(true));

        // ================= INIT =================
        refresh();

        new javax.swing.Timer(5000, e -> refresh()).start();
    }

    // ================= MENU =================
    private JButton createMenu(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(240, 45));
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(109, 40, 217));
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 20, 0, 0));
        return btn;
    }

    private JLabel createValue() {
        JLabel l = new JLabel("0");
        l.setFont(new Font("Arial", Font.BOLD, 26));
        l.setForeground(Color.WHITE);
        return l;
    }

    private JPanel card(String title, JLabel value, Color bg) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(bg);
        p.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel t = new JLabel(title);
        t.setForeground(Color.WHITE);

        p.add(t, BorderLayout.NORTH);
        p.add(value, BorderLayout.CENTER);

        return p;
    }

    // ================= REFRESH DATA =================
    private void refresh() {
        muatData();
        loadChart();
    }

    private void muatData() {
        try {
            Connection c = Koneksi.getKoneksi();
            Statement st = c.createStatement();

            lblTotalBarang.setText(getCount(st, "tb_barang") + "");
            lblTotalCustomer.setText(getCount(st, "tb_customer") + "");
            lblTotalTransaksi.setText(getCount(st, "tb_penjualan") + "");
            lblTotalPendapatan.setText(formatRupiah(getSum(st)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getCount(Statement st, String table) throws Exception {
        ResultSet rs = st.executeQuery("SELECT COUNT(*) c FROM " + table);
        return rs.next() ? rs.getInt("c") : 0;
    }

    private double getSum(Statement st) throws Exception {
        ResultSet rs = st.executeQuery("SELECT COALESCE(SUM(total_bayar),0) t FROM tb_penjualan");
        return rs.next() ? rs.getDouble("t") : 0;
    }

    private String formatRupiah(double v) {
        return "Rp " + NumberFormat.getNumberInstance(new Locale("id", "ID")).format(v);
    }

    // ================= CHART =================
    private void loadChart() {
        try {
            Connection c = Koneksi.getKoneksi();

            String sql =
                    "SELECT tgl_transaksi, SUM(total_bayar) total " +
                    "FROM tb_penjualan " +
                    "GROUP BY tgl_transaksi ORDER BY tgl_transaksi DESC LIMIT 7";

            ResultSet rs = c.createStatement().executeQuery(sql);

            ArrayList<Integer> data = new ArrayList<>();

            while (rs.next()) {
                data.add(rs.getInt("total"));
            }

            chartPanel.setData(data);
            chartPanel.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= CHART PANEL =================
    class ChartPanel extends JPanel {

        ArrayList<Integer> data = new ArrayList<>();

        public void setData(ArrayList<Integer> d) {
            this.data = d;
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

            int max = data.stream().max(Integer::compare).orElse(1);
            int step = w / (data.size() + 1);

            for (int i = 0; i < data.size() - 1; i++) {

                int x1 = step * (i + 1);
                int y1 = h - (data.get(i) * h / max);

                int x2 = step * (i + 2);
                int y2 = h - (data.get(i + 1) * h / max);

                g2.setColor(new Color(99, 102, 241));
                g2.drawLine(x1, y1, x2, y2);

                g2.fillOval(x1 - 4, y1 - 4, 8, 8);
            }
        }
    }
}