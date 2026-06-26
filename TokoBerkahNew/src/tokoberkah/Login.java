package tokoberkah;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame {

    private final JTextField txtUsername;
    private final JPasswordField txtPassword;

    public Login() {
        setTitle("Login - Toko Berkah");
        setSize(440, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.PURPLE_BG);

        // HEADER
        JPanel header = new JPanel();
        header.setBackground(Theme.PURPLE_DARK);
        header.setPreferredSize(new Dimension(440, 100));
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(20, 0, 20, 0));

        JLabel icon = new JLabel("🛍", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appName = new JLabel("TOKO BERKAH", SwingConstants.CENTER);
        appName.setFont(new Font("Arial", Font.BOLD, 20));
        appName.setForeground(Color.WHITE);
        appName.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(icon);
        header.add(Box.createVerticalStrut(4));
        header.add(appName);

        // FORM LOGIN
        JPanel card = new JPanel();
        card.setBackground(Theme.WHITE);
        card.setLayout(null);
        card.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel lblTitle = new JLabel("Masuk ke Akun Anda");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(Theme.PURPLE_DARK);
        lblTitle.setBounds(40, 20, 360, 28);

        JLabel lblSub = new JLabel("Silakan masukkan username dan password");
        lblSub.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSub.setForeground(Theme.TEXT_MUTED);
        lblSub.setBounds(40, 50, 360, 20);

        JLabel l1 = Theme.label("Username");
        l1.setBounds(40, 90, 100, 25);

        txtUsername = Theme.styledField();
        txtUsername.setBounds(40, 115, 340, 32);

        JLabel l2 = Theme.label("Password");
        l2.setBounds(40, 160, 100, 25);

        txtPassword = Theme.styledPasswordField();
        txtPassword.setBounds(40, 185, 340, 32);

        JButton btnLogin = Theme.primaryButton("Masuk");
        btnLogin.setBounds(40, 240, 160, 38);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnExit = Theme.mutedButton("Keluar");
        btnExit.setBounds(220, 240, 160, 38);
        btnExit.setFont(new Font("Arial", Font.BOLD, 14));

        card.add(lblTitle);
        card.add(lblSub);
        card.add(l1);
        card.add(txtUsername);
        card.add(l2);
        card.add(txtPassword);
        card.add(btnLogin);
        card.add(btnExit);

        root.add(header, BorderLayout.NORTH);
        root.add(card, BorderLayout.CENTER);

        setContentPane(root);

        btnLogin.addActionListener(e -> handleLogin());
        btnExit.addActionListener(e -> System.exit(0));
        txtPassword.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {

        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Username dan Password wajib diisi!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {

            Connection conn = Koneksi.getConnection();

            if (conn == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Koneksi database gagal!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "SELECT * FROM tb_user WHERE username=? AND password=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String level = rs.getString("level");

                JOptionPane.showMessageDialog(
                        this,
                        "Login berhasil sebagai " + level,
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);

                dispose();

                SwingUtilities.invokeLater(() -> {
                    new Dashboard().setVisible(true);
                });

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Username atau Password salah!",
                        "Login Gagal",
                        JOptionPane.ERROR_MESSAGE);

                txtPassword.setText("");
                txtPassword.requestFocus();
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Gagal Login : " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void resetForm() {
        txtUsername.setText("");
        txtPassword.setText("");
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}