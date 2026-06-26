package tokoberkah;

import java.awt.BorderLayout;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class tokoberkah extends JFrame {

    JTextField txtId;
    JTextField txtNama;
    JTextField txtSatuan;
    JTextField txtHarga;
    JTextField txtStok;

    JComboBox<String> cmbKategori;

    JButton btnTambah;
    JButton btnUbah;
    JButton btnHapus;
    JButton btnReset;

    JTable table;
    DefaultTableModel model;

    public tokoberkah() {

        setTitle("Data Barang");
        setSize(900, 500);
        setLocationRelativeTo(null);

        initKomponen();
        loadKategori();
        tampilData();
    }

    private void initKomponen() {

        JPanel form = new JPanel();
        form.setLayout(null);

        JLabel l1 = new JLabel("ID Barang");
        l1.setBounds(10,10,100,25);
        form.add(l1);

        txtId = new JTextField();
        txtId.setBounds(120,10,150,25);
        form.add(txtId);

        JLabel l2 = new JLabel("Kategori");
        l2.setBounds(10,45,100,25);
        form.add(l2);

        cmbKategori = new JComboBox<>();
        cmbKategori.setBounds(120,45,150,25);
        form.add(cmbKategori);

        JLabel l3 = new JLabel("Nama Barang");
        l3.setBounds(10,80,100,25);
        form.add(l3);

        txtNama = new JTextField();
        txtNama.setBounds(120,80,200,25);
        form.add(txtNama);

        JLabel l4 = new JLabel("Satuan");
        l4.setBounds(10,115,100,25);
        form.add(l4);

        txtSatuan = new JTextField();
        txtSatuan.setBounds(120,115,150,25);
        form.add(txtSatuan);

        JLabel l5 = new JLabel("Harga");
        l5.setBounds(10,150,100,25);
        form.add(l5);

        txtHarga = new JTextField();
        txtHarga.setBounds(120,150,150,25);
        form.add(txtHarga);

        JLabel l6 = new JLabel("Stok");
        l6.setBounds(10,185,100,25);
        form.add(l6);

        txtStok = new JTextField();
        txtStok.setBounds(120,185,150,25);
        form.add(txtStok);

        btnTambah = new JButton("Tambah");
        btnTambah.setBounds(350,20,100,30);
        form.add(btnTambah);

        btnUbah = new JButton("Ubah");
        btnUbah.setBounds(350,60,100,30);
        form.add(btnUbah);

        btnHapus = new JButton("Hapus");
        btnHapus.setBounds(350,100,100,30);
        form.add(btnHapus);

        btnReset = new JButton("Reset");
        btnReset.setBounds(350,140,100,30);
        form.add(btnReset);

        form.setPreferredSize(new java.awt.Dimension(850,230));

        add(form, BorderLayout.NORTH);

        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Kategori");
        model.addColumn("Nama");
        model.addColumn("Satuan");
        model.addColumn("Harga");
        model.addColumn("Stok");

        table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);

        btnTambah.addActionListener(e -> tambahData());
        btnUbah.addActionListener(e -> ubahData());
        btnHapus.addActionListener(e -> hapusData());
        btnReset.addActionListener(e -> resetForm());

        table.getSelectionModel().addListSelectionListener(e -> {
            if(table.getSelectedRow() != -1){
                ambilDataTabel();
            }
        });
    }

    private void loadKategori() {

        try {

            Connection c = Koneksi.getKoneksi();

            String sql = "SELECT * FROM tb_kategori";

            Statement st = c.createStatement();

            ResultSet rs = st.executeQuery(sql);

            cmbKategori.removeAllItems();

            while(rs.next()) {

                cmbKategori.addItem(
                        rs.getString("id_kategori")
                );
            }

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    private void tampilData() {

        model.setRowCount(0);

        try {

            Connection c = Koneksi.getKoneksi();

            String sql = "SELECT * FROM tb_barang";

            Statement st = c.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while(rs.next()) {

                model.addRow(new Object[]{
                    rs.getString("id_barang"),
                    rs.getString("id_kategori"),
                    rs.getString("nama_barang"),
                    rs.getString("satuan"),
                    rs.getDouble("harga_jual"),
                    rs.getInt("stok")
                });

            }

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    private void tambahData() {

        try {

            Connection c = Koneksi.getKoneksi();

            String sql =
            "INSERT INTO tb_barang VALUES(?,?,?,?,?,?)";

            PreparedStatement ps =
            c.prepareStatement(sql);

            ps.setString(1, txtId.getText());
            ps.setString(2, cmbKategori.getSelectedItem().toString());
            ps.setString(3, txtNama.getText());
            ps.setString(4, txtSatuan.getText());
            ps.setDouble(5, Double.parseDouble(txtHarga.getText()));
            ps.setInt(6, Integer.parseInt(txtStok.getText()));

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
            "Data berhasil disimpan");

            tampilData();
            resetForm();

        } catch(Exception e) {

            JOptionPane.showMessageDialog(this,e.getMessage());

        }
    }

    private void ubahData() {

        try {

            Connection c = Koneksi.getKoneksi();

            String sql =
            "UPDATE tb_barang SET " +
            "id_kategori=?, " +
            "nama_barang=?, " +
            "satuan=?, " +
            "harga_jual=?, " +
            "stok=? " +
            "WHERE id_barang=?";

            PreparedStatement ps =
            c.prepareStatement(sql);

            ps.setString(1, cmbKategori.getSelectedItem().toString());
            ps.setString(2, txtNama.getText());
            ps.setString(3, txtSatuan.getText());
            ps.setDouble(4, Double.parseDouble(txtHarga.getText()));
            ps.setInt(5, Integer.parseInt(txtStok.getText()));
            ps.setString(6, txtId.getText());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
            "Data berhasil diubah");

            tampilData();

        } catch(Exception e) {

            JOptionPane.showMessageDialog(this,e.getMessage());

        }
    }

    private void hapusData() {

        try {

            Connection c = Koneksi.getKoneksi();

            String sql =
            "DELETE FROM tb_barang WHERE id_barang=?";

            PreparedStatement ps =
            c.prepareStatement(sql);

            ps.setString(1, txtId.getText());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
            "Data berhasil dihapus");

            tampilData();
            resetForm();

        } catch(Exception e) {

            JOptionPane.showMessageDialog(this,e.getMessage());

        }
    }

    private void ambilDataTabel() {

        int baris = table.getSelectedRow();

        txtId.setText(
        model.getValueAt(baris,0).toString());

        cmbKategori.setSelectedItem(
        model.getValueAt(baris,1).toString());

        txtNama.setText(
        model.getValueAt(baris,2).toString());

        txtSatuan.setText(
        model.getValueAt(baris,3).toString());

        txtHarga.setText(
        model.getValueAt(baris,4).toString());

        txtStok.setText(
        model.getValueAt(baris,5).toString());
    }

    private void resetForm() {

        txtId.setText("");
        txtNama.setText("");
        txtSatuan.setText("");
        txtHarga.setText("");
        txtStok.setText("");

        if(cmbKategori.getItemCount() > 0){
            cmbKategori.setSelectedIndex(0);
        }
    }
}