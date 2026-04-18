import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Library Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // ← FULL SCREEN
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Theme.BG_DARK);

        // ── HEADER ──
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG_CARD);
        header.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel icon = new JLabel("📚");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));

        JPanel headerText = new JPanel();
        headerText.setLayout(new BoxLayout(headerText, BoxLayout.Y_AXIS));
        headerText.setBackground(Theme.BG_CARD);

        JLabel title = new JLabel("Library Management System");
        title.setForeground(Theme.TEXT_WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel sub = Theme.makeLabel("Mini Project  —  Dashboard");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        headerText.add(title);
        headerText.add(Box.createVerticalStrut(6));
        headerText.add(sub);

        header.add(icon, BorderLayout.WEST);
        header.add(Box.createHorizontalStrut(20), BorderLayout.CENTER);
        header.add(headerText, BorderLayout.EAST);

        main.add(header, BorderLayout.NORTH);

        // ── CENTER WRAPPER ──
        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setBackground(Theme.BG_DARK);

        JPanel menu = new JPanel(new GridLayout(4, 1, 0, 20));
        menu.setBackground(Theme.BG_DARK);
        menu.setPreferredSize(new Dimension(500, 400));

        JButton btnAdd   = makeMenuBtn("➕   Add Book",       Theme.ACCENT_GREEN);
        JButton btnView  = makeMenuBtn("📖   View Books",     new Color(80, 60, 160));
        JButton btnIssue = makeMenuBtn("📤   Issue / Return", Theme.ACCENT_BLUE);
        JButton btnExit  = makeMenuBtn("✖    Exit",           Theme.ACCENT_RED);

        menu.add(btnAdd);
        menu.add(btnView);
        menu.add(btnIssue);
        menu.add(btnExit);

        centerWrap.add(menu);
        main.add(centerWrap, BorderLayout.CENTER);

        // ── FOOTER ──
        JLabel footer = new JLabel(
            "Developed by Diksha  |  Java + MySQL", JLabel.CENTER);
        footer.setForeground(Theme.TEXT_MUTED);
        footer.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        footer.setOpaque(true);
        footer.setBackground(Theme.BG_DARK);
        main.add(footer, BorderLayout.SOUTH);

        add(main);

        btnAdd.addActionListener(e   -> new AddBook());
        btnView.addActionListener(e  -> new ViewBooks());
        btnIssue.addActionListener(e -> new IssueBook());
        btnExit.addActionListener(e  -> {
            int c = JOptionPane.showConfirmDialog(this,
                "Exit the application?", "Confirm",
                JOptionPane.YES_NO_OPTION);
            if (c == 0) System.exit(0);
        });

        setVisible(true);
    }

    private JButton makeMenuBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(0, 70));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                b.setBackground(bg.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                b.setBackground(bg);
            }
        });
        return b;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        new MainMenu();
    }
}