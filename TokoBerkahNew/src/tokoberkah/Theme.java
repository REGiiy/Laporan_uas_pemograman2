package tokoberkah;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.JTableHeader;

/**
 * Kelas utilitas tema Ungu Elegan untuk seluruh aplikasi Toko Berkah.
 */
public class Theme {

    // === PALET WARNA ===
    public static final Color PURPLE_DARK   = new Color(49, 10, 80);    // sidebar / header
    public static final Color PURPLE_MID    = new Color(88, 28, 135);   // aksen
    public static final Color PURPLE_LIGHT  = new Color(147, 51, 234);  // tombol utama
    public static final Color PURPLE_SOFT   = new Color(233, 213, 255); // highlight / hover
    public static final Color PURPLE_BG     = new Color(250, 245, 255); // background konten
    public static final Color WHITE         = Color.WHITE;
    public static final Color TEXT_DARK     = new Color(30, 10, 50);
    public static final Color TEXT_MUTED    = new Color(120, 90, 150);
    public static final Color SUCCESS       = new Color(22, 163, 74);
    public static final Color DANGER        = new Color(220, 38, 38);
    public static final Color MUTED_BTN     = new Color(156, 163, 175);
    public static final Color INPUT_BG      = new Color(245, 240, 255);
    public static final Color INPUT_DISABLED= new Color(235, 228, 248);

    // === FONT ===
    public static final Font FONT_TITLE  = new Font("Arial", Font.BOLD, 22);
    public static final Font FONT_LABEL  = new Font("Arial", Font.PLAIN, 13);
    public static final Font FONT_BOLD   = new Font("Arial", Font.BOLD, 13);
    public static final Font FONT_BTN    = new Font("Arial", Font.BOLD, 13);
    public static final Font FONT_TABLE  = new Font("Arial", Font.PLAIN, 12);
    public static final Font FONT_HEADER = new Font("Arial", Font.BOLD, 12);

    /** Tombol warna utama (ungu) */
    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(PURPLE_LIGHT);
        btn.setForeground(WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(FONT_BTN);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /** Tombol sukses (hijau) */
    public static JButton successButton(String text) {
        JButton btn = primaryButton(text);
        btn.setBackground(SUCCESS);
        return btn;
    }

    /** Tombol bahaya (merah) */
    public static JButton dangerButton(String text) {
        JButton btn = primaryButton(text);
        btn.setBackground(DANGER);
        return btn;
    }

    /** Tombol netral (abu) */
    public static JButton mutedButton(String text) {
        JButton btn = primaryButton(text);
        btn.setBackground(MUTED_BTN);
        return btn;
    }

    /** TextField standar */
    public static JTextField styledField() {
        JTextField tf = new JTextField();
        tf.setBackground(INPUT_BG);
        tf.setForeground(TEXT_DARK);
        tf.setFont(FONT_LABEL);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(196, 181, 253), 1),
            BorderFactory.createEmptyBorder(3, 6, 3, 6)
        ));
        return tf;
    }

    /** TextField disabled */
    public static JTextField disabledField() {
        JTextField tf = styledField();
        tf.setEditable(false);
        tf.setBackground(INPUT_DISABLED);
        return tf;
    }

    /** JPasswordField standar */
    public static JPasswordField styledPasswordField() {
        JPasswordField pf = new JPasswordField();
        pf.setBackground(INPUT_BG);
        pf.setForeground(TEXT_DARK);
        pf.setFont(FONT_LABEL);
        pf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(196, 181, 253), 1),
            BorderFactory.createEmptyBorder(3, 6, 3, 6)
        ));
        return pf;
    }

    /** JComboBox standar */
    public static <T> JComboBox<T> styledCombo() {
        JComboBox<T> cb = new JComboBox<>();
        cb.setBackground(INPUT_BG);
        cb.setFont(FONT_LABEL);
        return cb;
    }

    /** Label biasa */
    public static JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_LABEL);
        l.setForeground(TEXT_DARK);
        return l;
    }

    /** Panel konten putih dengan padding */
    public static JPanel contentPanel() {
        JPanel p = new JPanel();
        p.setBackground(WHITE);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));
        return p;
    }

    /** Styled border dengan judul */
    public static Border titledBorder(String title) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(196, 181, 253), 1),
            title, TitledBorder.LEFT, TitledBorder.TOP,
            FONT_BOLD, PURPLE_MID
        );
    }

    /** Terapkan style tabel */
    public static void styleTable(JTable table) {
        table.setRowHeight(28);
        table.setFont(FONT_TABLE);
        table.setForeground(TEXT_DARK);
        table.setBackground(WHITE);
        table.setSelectionBackground(PURPLE_SOFT);
        table.setSelectionForeground(TEXT_DARK);
        table.setGridColor(new Color(233, 213, 255));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_HEADER);
        header.setBackground(PURPLE_MID);
        header.setForeground(WHITE);
        header.setReorderingAllowed(false);
    }
}
