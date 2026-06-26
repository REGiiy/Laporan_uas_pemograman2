package tokoberkah;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class FormTransaksi extends JFrame {

    JComboBox<String> cmbCustomer;
    JComboBox<String> cmbBarang;

    JTextField txtHarga, txtStok, txtJumlah, txtTotal;
    JButton btnHitung, btnSimpan, btnReset;

    JTable tblDetail;
    DefaultTableModel modelDetail;

    // CACHE DATA
    ArrayList<String> listCustomer = new ArrayList<>();
    ArrayList<String> listBarang = new ArrayList<>();

    HashMap<String, String> mapCustomer = new HashMap<>();
    HashMap<String, String> mapBarang = new HashMap<>();

    public FormTransaksi() {

        setTitle("Transaksi)");
        setSize(820, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.PURPLE_BG);

        // ================= TOPBAR =================
        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setBackground(Theme.WHITE);
        topbar.setPreferredSize(new Dimension(0, 60));

        JLabel title = new JLabel("Regi mart");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Theme.PURPLE_DARK);
        topbar.add(title, BorderLayout.WEST);

        // ================= FORM =================
        JPanel panelForm = new JPanel(null);
        panelForm.setBackground(Theme.WHITE);
        panelForm.setBorder(Theme.titledBorder("Kasir Transaksi"));
        panelForm.setPreferredSize(new Dimension(800, 280));

        int lx = 20, fx = 140, fw = 240, fh = 30, gy = 35, gap = 44;

        addLabel(panelForm, "Customer", lx, gy);
        cmbCustomer = Theme.styledCombo();
        cmbCustomer.setEditable(true);
        cmbCustomer.setBounds(fx, gy, fw, fh);
        panelForm.add(cmbCustomer);

        addLabel(panelForm, "Barang", lx, gy + gap);
        cmbBarang = Theme.styledCombo();
        cmbBarang.setEditable(true);
        cmbBarang.setBounds(fx, gy + gap, fw, fh);
        panelForm.add(cmbBarang);

        addLabel(panelForm, "Harga", lx, gy + gap * 2);
        txtHarga = Theme.disabledField();
        txtHarga.setBounds(fx, gy + gap * 2, fw, fh);
        panelForm.add(txtHarga);

        addLabel(panelForm, "Stok", lx, gy + gap * 3);
        txtStok = Theme.disabledField();
        txtStok.setBounds(fx, gy + gap * 3, fw, fh);
        panelForm.add(txtStok);

        addLabel(panelForm, "Jumlah", lx, gy + gap * 4);
        txtJumlah = Theme.styledField();
        txtJumlah.setBounds(fx, gy + gap * 4, fw, fh);
        panelForm.add(txtJumlah);

        addLabel(panelForm, "Total", lx, gy + gap * 5);
        txtTotal = Theme.disabledField();
        txtTotal.setBounds(fx, gy + gap * 5, fw, fh);
        panelForm.add(txtTotal);

        btnHitung = Theme.primaryButton("Hitung");
        btnSimpan = Theme.successButton("Simpan");
        btnReset = Theme.mutedButton("Reset");

        int bx = 430, bw = 130, bh = 36;

        btnHitung.setBounds(bx, gy + gap, bw, bh);
        btnSimpan.setBounds(bx, gy + gap * 2, bw, bh);
        btnReset.setBounds(bx, gy + gap * 3, bw, bh);

        panelForm.add(btnHitung);
        panelForm.add(btnSimpan);
        panelForm.add(btnReset);

        // ================= TABLE =================
        JPanel panelDetail = new JPanel(new BorderLayout());
        panelDetail.setBackground(Theme.WHITE);
        panelDetail.setBorder(Theme.titledBorder("Detail Penjualan"));

        String[] kolom = {"No", "Tanggal", "Customer", "Barang", "Jumlah", "Harga", "Total"};
        modelDetail = new DefaultTableModel(kolom, 0);

        tblDetail = new JTable(modelDetail);
        Theme.styleTable(tblDetail);

        panelDetail.add(new JScrollPane(tblDetail), BorderLayout.CENTER);

        JPanel body = new JPanel(new BorderLayout(10, 10));
        body.setBackground(Theme.PURPLE_BG);
        body.setBorder(new EmptyBorder(10, 10, 10, 10));
        body.add(panelForm, BorderLayout.NORTH);
        body.add(panelDetail, BorderLayout.CENTER);

        root.add(topbar, BorderLayout.NORTH);
        root.add(body, BorderLayout.CENTER);

        setContentPane(root);

        // ================= LOAD DATA =================
        loadCustomer();
        loadBarang();
        loadDetail();

        // ================= AUTO SEARCH (FIX STABLE) =================
        setupAutoSearch();

        // ================= EVENT =================
        cmbBarang.addActionListener(e -> ambilBarang());
        btnHitung.addActionListener(e -> hitungTotal());
        btnSimpan.addActionListener(e -> simpan());
        btnReset.addActionListener(e -> reset());
    }

    // ================= LABEL =================
    private void addLabel(JPanel p, String t, int x, int y) {
        JLabel l = new JLabel(t);
        l.setBounds(x, y, 120, 25);
        p.add(l);
    }

    // ================= LOAD CUSTOMER =================
    private void loadCustomer() {
        try {
            Connection c = Koneksi.getKoneksi();
            ResultSet rs = c.createStatement()
                    .executeQuery("SELECT id_customer, nama_customer FROM tb_customer");

            listCustomer.clear();
            mapCustomer.clear();

            while (rs.next()) {
                String id = rs.getString("id_customer");
                String nama = rs.getString("nama_customer");

                listCustomer.add(nama);
                mapCustomer.put(nama, id);
            }

            refreshCustomer("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= LOAD BARANG =================
    private void loadBarang() {
        try {
            Connection c = Koneksi.getKoneksi();
            ResultSet rs = c.createStatement()
                    .executeQuery("SELECT id_barang, nama_barang FROM tb_barang");

            listBarang.clear();
            mapBarang.clear();

            while (rs.next()) {
                String id = rs.getString("id_barang");
                String nama = rs.getString("nama_barang");

                listBarang.add(nama);
                mapBarang.put(nama, id);
            }

            refreshBarang("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= AUTO SEARCH FIX (NO POPUP ERROR) =================
    private void setupAutoSearch() {

        JTextField tc = (JTextField) cmbCustomer.getEditor().getEditorComponent();
        tc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                refreshCustomer(tc.getText());
            }
        });

        JTextField tb = (JTextField) cmbBarang.getEditor().getEditorComponent();
        tb.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                refreshBarang(tb.getText());
            }
        });
    }

    // ================= FILTER CUSTOMER =================
    private void refreshCustomer(String key) {
        cmbCustomer.removeAllItems();

        for (String nama : listCustomer) {
            if (nama.toLowerCase().contains(key.toLowerCase())) {
                cmbCustomer.addItem(nama);
            }
        }
        // ❌ NO setPopupVisible (FIX ERROR)
    }

    // ================= FILTER BARANG =================
    private void refreshBarang(String key) {
        cmbBarang.removeAllItems();

        for (String nama : listBarang) {
            if (nama.toLowerCase().contains(key.toLowerCase())) {
                cmbBarang.addItem(nama);
            }
        }
        // ❌ NO setPopupVisible (FIX ERROR)
    }

    // ================= AMBIL BARANG =================
    private void ambilBarang() {
        try {
            if (cmbBarang.getSelectedItem() == null) return;

            String nama = cmbBarang.getSelectedItem().toString();
            String id = mapBarang.get(nama);

            if (id == null) return;

            Connection c = Koneksi.getKoneksi();
            PreparedStatement ps = c.prepareStatement(
                    "SELECT harga_jual, stok FROM tb_barang WHERE id_barang=?"
            );

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtHarga.setText(rs.getString("harga_jual"));
                txtStok.setText(rs.getString("stok"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= HITUNG =================
    private void hitungTotal() {
        try {
            int jumlah = Integer.parseInt(txtJumlah.getText());
            double harga = Double.parseDouble(txtHarga.getText());

            txtTotal.setText(String.valueOf(jumlah * harga));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Input tidak valid");
        }
    }

    // ================= SIMPAN =================
    private void simpan() {
        try {
            int jumlah = Integer.parseInt(txtJumlah.getText());
            int stok = Integer.parseInt(txtStok.getText());

            if (jumlah > stok) {
                JOptionPane.showMessageDialog(this, "Stok tidak cukup");
                return;
            }

            String namaCust = cmbCustomer.getSelectedItem().toString();
            String namaBarang = cmbBarang.getSelectedItem().toString();

            String idCust = mapCustomer.get(namaCust);
            String idBarang = mapBarang.get(namaBarang);

            Connection c = Koneksi.getKoneksi();

            PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO tb_penjualan (tgl_transaksi,id_customer,id_barang,jumlah_beli,total_bayar,id_user) " +
                    "VALUES (CURDATE(),?,?,?,?,1)"
            );

            ps.setString(1, idCust);
            ps.setString(2, idBarang);
            ps.setInt(3, jumlah);
            ps.setDouble(4, Double.parseDouble(txtTotal.getText()));

            ps.executeUpdate();

            PreparedStatement ps2 = c.prepareStatement(
                    "UPDATE tb_barang SET stok = stok - ? WHERE id_barang=?"
            );
            ps2.setInt(1, jumlah);
            ps2.setString(2, idBarang);
            ps2.executeUpdate();

            JOptionPane.showMessageDialog(this, "Transaksi berhasil");

            reset();
            loadDetail();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= DETAIL =================
    private void loadDetail() {
        try {
            Connection c = Koneksi.getKoneksi();

            String sql =
                    "SELECT p.tgl_transaksi, c.nama_customer, b.nama_barang, " +
                    "p.jumlah_beli, b.harga_jual, p.total_bayar " +
                    "FROM tb_penjualan p " +
                    "JOIN tb_customer c ON p.id_customer=c.id_customer " +
                    "JOIN tb_barang b ON p.id_barang=b.id_barang " +
                    "ORDER BY p.id_jual DESC";

            ResultSet rs = c.createStatement().executeQuery(sql);

            modelDetail.setRowCount(0);

            int no = 1;
            while (rs.next()) {
                modelDetail.addRow(new Object[]{
                        no++,
                        rs.getString("tgl_transaksi"),
                        rs.getString("nama_customer"),
                        rs.getString("nama_barang"),
                        rs.getInt("jumlah_beli"),
                        rs.getDouble("harga_jual"),
                        rs.getDouble("total_bayar")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= RESET =================
    private void reset() {
        txtHarga.setText("");
        txtStok.setText("");
        txtJumlah.setText("");
        txtTotal.setText("");
    }
}