package tokoberkah;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class FormBarang extends JFrame {

    JTextField txtId, txtNama, txtHarga, txtStok, txtKategori;
    JButton btnSimpan, btnUbah, btnHapus, btnReset;
    JTable tabel;
    DefaultTableModel model;

    public FormBarang() {
        setTitle("Data Barang");
        setSize(860, 580);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(Theme.PURPLE_BG);

        // ===== TOPBAR =====
        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setBackground(Theme.WHITE);
        topbar.setPreferredSize(new Dimension(0, 60));
        topbar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(233, 213, 255)),
            new EmptyBorder(0, 20, 0, 20)
        ));
        JLabel title = new JLabel("📦  Data Barang");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Theme.PURPLE_DARK);
        topbar.add(title, BorderLayout.WEST);

        // ===== FORM PANEL =====
        JPanel panelForm = new JPanel(null);
        panelForm.setBackground(Theme.WHITE);
        panelForm.setBorder(Theme.titledBorder("Input Data Barang"));
        panelForm.setPreferredSize(new Dimension(860, 240));

        int lx = 20, fx = 140, fw = 200, fh = 30, gy = 35;

        addFormRow(panelForm, "ID Barang",   lx, gy,      fx, gy,      fw, fh, false);
        txtId       = addField(panelForm, fx, gy,       fw, fh, false);
        addLabel(panelForm, "Nama Barang", lx, gy+45);
        txtNama     = addField(panelForm, fx, gy+45,   fw+60, fh, false);
        addLabel(panelForm, "Harga",       lx, gy+90);
        txtHarga    = addField(panelForm, fx, gy+90,   fw, fh, false);
        addLabel(panelForm, "Stok",        lx, gy+135);
        txtStok     = addField(panelForm, fx, gy+135,  fw, fh, false);
        addLabel(panelForm, "ID Kategori", lx, gy+180);
        txtKategori = addField(panelForm, fx, gy+180,  fw, fh, false);

        btnSimpan = Theme.successButton("Simpan");
        btnUbah   = Theme.primaryButton("Ubah");
        btnHapus  = Theme.dangerButton("Hapus");
        btnReset  = Theme.mutedButton("Reset");

        int bx = 430, by = gy, bw = 120, bh = 34, bg = 46;
        setBtnBounds(btnSimpan, bx, by,      bw, bh, panelForm);
        setBtnBounds(btnUbah,   bx, by+bg,   bw, bh, panelForm);
        setBtnBounds(btnHapus,  bx, by+bg*2, bw, bh, panelForm);
        setBtnBounds(btnReset,  bx, by+bg*3, bw, bh, panelForm);

        // ===== TABEL =====
        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.setBackground(Theme.WHITE);
        panelTable.setBorder(Theme.titledBorder("Daftar Barang"));

        model = new DefaultTableModel(new String[]{"ID","Nama","Harga","Stok","Kategori"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabel = new JTable(model);
        Theme.styleTable(tabel);

        tabel.getColumnModel().getColumn(0).setPreferredWidth(80);
        tabel.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabel.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabel.getColumnModel().getColumn(3).setPreferredWidth(60);
        tabel.getColumnModel().getColumn(4).setPreferredWidth(100);

        panelTable.add(new JScrollPane(tabel), BorderLayout.CENTER);

        JPanel body = new JPanel(new BorderLayout(0, 12));
        body.setBackground(Theme.PURPLE_BG);
        body.setBorder(new EmptyBorder(12, 12, 12, 12));
        body.add(panelForm, BorderLayout.NORTH);
        body.add(panelTable, BorderLayout.CENTER);

        root.add(topbar, BorderLayout.NORTH);
        root.add(body, BorderLayout.CENTER);
        setContentPane(root);

        tampilData();

        btnSimpan.addActionListener(e -> simpan());
        btnUbah.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());
        btnReset.addActionListener(e -> reset());

        tabel.getSelectionModel().addListSelectionListener(e -> {
            int row = tabel.getSelectedRow();
            if (row >= 0) {
                txtId.setText(model.getValueAt(row, 0).toString());
                txtNama.setText(model.getValueAt(row, 1).toString());
                txtHarga.setText(model.getValueAt(row, 2).toString());
                txtStok.setText(model.getValueAt(row, 3).toString());
                txtKategori.setText(model.getValueAt(row, 4).toString());
            }
        });
    }

    private void addFormRow(JPanel p, String label, int lx, int ly, int fx, int fy, int fw, int fh, boolean dis) {}
    private void addLabel(JPanel p, String text, int x, int y) {
        JLabel l = Theme.label(text); l.setBounds(x, y, 120, 25); p.add(l);
    }
    private JTextField addField(JPanel p, int x, int y, int w, int h, boolean disabled) {
        JTextField tf = disabled ? Theme.disabledField() : Theme.styledField();
        tf.setBounds(x, y, w, h); p.add(tf); return tf;
    }
    private void setBtnBounds(JButton btn, int x, int y, int w, int h, JPanel p) {
        btn.setBounds(x, y, w, h); p.add(btn);
    }

    private void tampilData() {
        model.setRowCount(0);
        try {
            Connection conn = Koneksi.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM tb_barang");
            while (rs.next()) model.addRow(new Object[]{
                rs.getString("id_barang"), rs.getString("nama_barang"),
                rs.getString("harga_jual"), rs.getString("stok"), rs.getString("id_kategori")
            });
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }

    private void simpan() {
        try {
            Connection conn = Koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO tb_barang(id_barang,nama_barang,harga_jual,stok,id_kategori) VALUES(?,?,?,?,?)");
            ps.setString(1, txtId.getText()); ps.setString(2, txtNama.getText());
            ps.setDouble(3, Double.parseDouble(txtHarga.getText()));
            ps.setInt(4, Integer.parseInt(txtStok.getText())); ps.setString(5, txtKategori.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan");
            tampilData(); reset();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }

    private void ubah() {
        try {
            Connection conn = Koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE tb_barang SET nama_barang=?,harga_jual=?,stok=?,id_kategori=? WHERE id_barang=?");
            ps.setString(1, txtNama.getText()); ps.setDouble(2, Double.parseDouble(txtHarga.getText()));
            ps.setInt(3, Integer.parseInt(txtStok.getText())); ps.setString(4, txtKategori.getText());
            ps.setString(5, txtId.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil diubah");
            tampilData(); reset();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }

    private void hapus() {
        try {
            Connection conn = Koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM tb_barang WHERE id_barang=?");
            ps.setString(1, txtId.getText()); ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
            tampilData(); reset();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }

    private void reset() {
        txtId.setText(""); txtNama.setText(""); txtHarga.setText(""); txtStok.setText(""); txtKategori.setText("");
    }
}