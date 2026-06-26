package tokoberkah;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class FormCustomer extends JFrame {

    JTextField txtSearch, txtId, txtNama, txtAlamat, txtTelepon;
    JTable table;
    DefaultTableModel model;

    JPopupMenu suggestionPopup;
    JList<String> suggestionList;

    ArrayList<String[]> cache = new ArrayList<>();

    public FormCustomer() {

        setTitle("Regi Mart - Customer");
        setSize(1150, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(245, 243, 255));

        // TOPBAR
        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setPreferredSize(new Dimension(0, 65));
        topbar.setBackground(Color.WHITE);
        topbar.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("🛒 Regi Mart - Customer");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(88, 28, 135));
        topbar.add(title, BorderLayout.WEST);

        // LEFT PANEL
        JPanel left = new JPanel(null);
        left.setPreferredSize(new Dimension(360, 0));
        left.setBackground(Color.WHITE);
        left.setBorder(new EmptyBorder(15, 15, 15, 15));

        txtSearch = field();
        txtId = field();
        txtNama = field();
        txtAlamat = field();
        txtTelepon = field();

        JLabel lSearch = label("Quick Search");
        JLabel lId = label("ID Customer");
        JLabel lNama = label("Nama");
        JLabel lAlamat = label("Alamat");
        JLabel lTelp = label("No Telp");

        int y = 20;

        lSearch.setBounds(10, y, 300, 20);
        txtSearch.setBounds(10, y + 25, 320, 38);

        lId.setBounds(10, y + 80, 200, 20);
        txtId.setBounds(10, y + 100, 320, 38);

        lNama.setBounds(10, y + 150, 200, 20);
        txtNama.setBounds(10, y + 170, 320, 38);

        lAlamat.setBounds(10, y + 220, 200, 20);
        txtAlamat.setBounds(10, y + 240, 320, 38);

        lTelp.setBounds(10, y + 290, 200, 20);
        txtTelepon.setBounds(10, y + 310, 320, 38);

        left.add(lSearch); left.add(txtSearch);
        left.add(lId); left.add(txtId);
        left.add(lNama); left.add(txtNama);
        left.add(lAlamat); left.add(txtAlamat);
        left.add(lTelp); left.add(txtTelepon);

        // BUTTON
        JButton btnAdd = btn("TAMBAH", new Color(16,185,129));
        JButton btnEdit = btn("EDIT", new Color(59,130,246));
        JButton btnDel = btn("HAPUS", new Color(239,68,68));
        JButton btnReset = btn("RESET", new Color(107,114,128));

        btnAdd.setBounds(10, 380, 150, 40);
        btnEdit.setBounds(180, 380, 150, 40);
        btnDel.setBounds(10, 430, 150, 40);
        btnReset.setBounds(180, 430, 150, 40);

        left.add(btnAdd);
        left.add(btnEdit);
        left.add(btnDel);
        left.add(btnReset);

        // TABLE
        model = new DefaultTableModel(new String[]{"ID", "Nama", "Alamat", "Telp"}, 0);
        table = new JTable(model);
        table.setRowHeight(30);

        JScrollPane sp = new JScrollPane(table);

        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(new EmptyBorder(15, 15, 15, 15));
        center.setBackground(new Color(245, 243, 255));

        center.add(left, BorderLayout.WEST);
        center.add(sp, BorderLayout.CENTER);

        root.add(topbar, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);

        setContentPane(root);

        // POPUP
        suggestionPopup = new JPopupMenu();
        suggestionList = new JList<>();
        suggestionPopup.add(new JScrollPane(suggestionList));

        suggestionList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String val = suggestionList.getSelectedValue();
                if (val != null) {
                    fill(val);
                    suggestionPopup.setVisible(false);
                }
            }
        });

        // EVENTS
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { search(); }
            public void removeUpdate(DocumentEvent e) { search(); }
            public void changedUpdate(DocumentEvent e) { search(); }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                txtId.setText(model.getValueAt(r,0).toString());
                txtNama.setText(model.getValueAt(r,1).toString());
                txtAlamat.setText(model.getValueAt(r,2).toString());
                txtTelepon.setText(model.getValueAt(r,3).toString());
            }
        });

        btnAdd.addActionListener(e -> insert());
        btnEdit.addActionListener(e -> update());
        btnDel.addActionListener(e -> delete());
        btnReset.addActionListener(e -> clear());

        loadData();
    }

    // ================= CRUD =================

    private void insert() {
        try {
            Connection c = Koneksi.getConnection();
            String sql = "INSERT INTO tb_customer VALUES (?,?,?,?)";
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setString(1, txtId.getText());
            ps.setString(2, txtNama.getText());
            ps.setString(3, txtAlamat.getText());
            ps.setString(4, txtTelepon.getText());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Berhasil tambah data");
            loadData();
            clear();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal tambah data");
            e.printStackTrace();
        }
    }

    private void update() {
        try {
            Connection c = Koneksi.getConnection();
            String sql = "UPDATE tb_customer SET nama_customer=?, alamat=?, no_telp=? WHERE id_customer=?";
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setString(1, txtNama.getText());
            ps.setString(2, txtAlamat.getText());
            ps.setString(3, txtTelepon.getText());
            ps.setString(4, txtId.getText());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Berhasil update");
            loadData();
            clear();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal update");
            e.printStackTrace();
        }
    }

    private void delete() {
        try {
            Connection c = Koneksi.getConnection();
            String sql = "DELETE FROM tb_customer WHERE id_customer=?";
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setString(1, txtId.getText());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Berhasil hapus");
            loadData();
            clear();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal hapus");
            e.printStackTrace();
        }
    }

    private void clear() {
        txtId.setText("");
        txtNama.setText("");
        txtAlamat.setText("");
        txtTelepon.setText("");
    }

    // ================= SEARCH =================
    private void search() {
        try {
            cache.clear();
            model.setRowCount(0);

            Connection c = Koneksi.getConnection();
            String sql = "SELECT * FROM tb_customer WHERE nama_customer LIKE ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, "%" + txtSearch.getText() + "%");

            ResultSet rs = ps.executeQuery();

            DefaultListModel<String> lm = new DefaultListModel<>();

            while (rs.next()) {
                String id = rs.getString(1);
                String nama = rs.getString(2);
                String alamat = rs.getString(3);
                String telp = rs.getString(4);

                model.addRow(new Object[]{id,nama,alamat,telp});
                cache.add(new String[]{id,nama,alamat,telp});

                lm.addElement(id + " - " + nama);
            }

            suggestionList.setModel(lm);
            suggestionPopup.show(txtSearch, 0, txtSearch.getHeight());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fill(String val) {
        for (String[] r : cache) {
            if ((r[0] + " - " + r[1]).equals(val)) {
                txtId.setText(r[0]);
                txtNama.setText(r[1]);
                txtAlamat.setText(r[2]);
                txtTelepon.setText(r[3]);
            }
        }
    }

    private void loadData() {
        txtSearch.setText("");
        search();
    }

    // ================= UI =================
    private JTextField field() {
        JTextField f = new JTextField();
        f.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return f;
    }

    private JLabel label(String t) {
        return new JLabel(t);
    }

    private JButton btn(String t, Color c) {
        JButton b = new JButton(t);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        return b;
    }
}