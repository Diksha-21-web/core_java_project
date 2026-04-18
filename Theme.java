import java.awt.*;
import javax.swing.*;

public class Theme {
    public static final Color BG_DARK      = new Color(18, 18, 30);
    public static final Color BG_CARD      = new Color(28, 28, 45);
    public static final Color BG_INPUT     = new Color(38, 38, 58);
    public static final Color ACCENT_GREEN = new Color(29, 158, 117);
    public static final Color ACCENT_BLUE  = new Color(24, 95, 165);
    public static final Color ACCENT_RED   = new Color(163, 45, 45);
    public static final Color TEXT_WHITE   = new Color(235, 235, 245);
    public static final Color TEXT_MUTED   = new Color(140, 140, 160);
    public static final Color BORDER       = new Color(55, 55, 80);

    public static JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 36));
        return btn;
    }

    public static JTextField makeField() {
        JTextField f = new JTextField();
        f.setBackground(BG_INPUT);
        f.setForeground(TEXT_WHITE);
        f.setCaretColor(TEXT_WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return f;
    }

    public static JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT_MUTED);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return l;
    }

    public static JLabel makeTitle(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT_WHITE);
        l.setFont(new Font("Segoe UI", Font.BOLD, 20));
        return l;
    }
}