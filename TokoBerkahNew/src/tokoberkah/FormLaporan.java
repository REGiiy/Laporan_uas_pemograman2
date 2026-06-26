package tokoberkah;

import java.awt.*;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class FormLaporan extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTotalPendapatan;

    public FormLaporan() {
        setTitle("Laporan Penjualan");
        setSize(1020, 560);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.PURPLE_BG);

        // ===== TOPBAR =====
        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setBackground(Theme.WHITE);
        topbar.setPreferredSize(new Dimension(0, 60));
        topbar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(233, 213, 255)),
            new EmptyBorder(0, 20, 0, 20)
        ));
        JLabel title = new JLabel("📋  Laporan Penjualan");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Theme.PURPLE_DARK);
        topbar.add(title, BorderLayout.WEST);

        JButton btnRefresh = Theme.primaryButton("🔄 Refresh");
        topbar.add(btnRefresh, BorderLayout.EAST);

        // ===== SUMMARY BAR =====
        JPanel summaryBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        summaryBar.setBackground(Theme.PURPLE_MID);
        summaryBar.setBorder(new EmptyBorder(2, 16, 2, 16));

        JLabel lblSumTitle = new JLabel("Total Pendapatan:");
        lblSumTitle.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSumTitle.setForeground(new Color(233, 213, 255));

        lblTotalPendapatan = new JLabel("Rp 0");
        lblTotalPendapatan.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotalPendapatan.setForeground(Color.WHITE);

        summaryBar.add(lblSumTitle);
        summaryBar.add(lblTotalPendapatan);

        // ===== TABEL =====
        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.setBackground(Theme.WHITE);
        panelTable.setBorder(Theme.titledBorder("Riwayat Penjualan"));

        model = new DefaultTableModel(
            new String[]{"No","ID Jual","Tanggal","Customer","Barang","Jumlah","Total Bayar"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        Theme.styleTable(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(35);
        table.getColumnModel().getColumn(1).setPreferredWidth(60);
        table.getColumnModel().getColumn(2).setPreferredWidth(90);
        table.getColumnModel().getColumn(3).setPreferredWidth(160);
        table.getColumnModel().getColumn(4).setPreferredWidth(160);
        table.getColumnModel().getColumn(5).setPreferredWidth(60);
        table.getColumnModel().getColumn(6).setPreferredWidth(120);

        panelTable.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel body = new JPanel(new BorderLayout(0, 12));
        body.setBackground(Theme.PURPLE_BG);
        body.setBorder(new EmptyBorder(12, 12, 12, 12));
        body.add(panelTable, BorderLayout.CENTER);

        JPanel northBlock = new JPanel(new BorderLayout());
        northBlock.add(topbar, BorderLayout.NORTH);
        northBlock.add(summaryBar, BorderLayout.SOUTH);

        root.add(northBlock, BorderLayout.NORTH);
        root.add(body, BorderLayout.CENTER);
        setContentPane(root);

        tampilData();
        btnRefresh.addActionListener(e -> tampilData());
    }

    private void tampilData() {
        model.setRowCount(0);
        try {
            Connection conn = Koneksi.getKoneksi();
            String sql = "SELECT p.id_jual, p.tgl_transaksi, c.nama_customer, b.nama_barang, " +
                "p.jumlah_beli, p.total_bayar " +
                "FROM tb_penjualan p " +
                "JOIN tb_customer c ON p.id_customer=c.id_customer " +
                "JOIN tb_barang b ON p.id_barang=b.id_barang " +
                "ORDER BY p.id_jual DESC";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            int no = 1;
            double total = 0;
            while (rs.next()) {
                double t = rs.getDouble("total_bayar");
                total += t;
                model.addRow(new Object[]{
                    no++, rs.getString("id_jual"), rs.getString("tgl_transaksi"),
                    rs.getString("nama_customer"), rs.getString("nama_barang"),
                    rs.getInt("jumlah_beli"), formatRupiah(t)
                });
            }
            lblTotalPendapatan.setText(formatRupiah(total));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private String formatRupiah(double v) {
        return "Rp " + NumberFormat.getNumberInstance(new Locale("id","ID")).format(v);
    }
}
