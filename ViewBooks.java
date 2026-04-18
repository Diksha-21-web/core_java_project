import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class ViewBooks extends JFrame {

    public ViewBooks() {
        setTitle("View Books");
        setSize(700, 450);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout(0, 12));
        main.setBackground(Theme.BG_DARK);
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel hdr = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        hdr.setBackground(Theme.BG_DARK);
        hdr.add(Theme.makeTitle("All Books"));
        main.add(hdr, BorderLayout.NORTH);

        // Table
        DefaultTableModel model = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        model.setColumnIdentifiers(new String[]{"ID", "Title", "Author", "Price (₹)"});

        JTable table = new JTable(model);
        styleTable(table);

        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(Theme.BG_CARD);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        main.add(sp, BorderLayout.CENTER);

        // Load
        try {
            Connection con = DBConnection.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM books ORDER BY id DESC");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    "₹" + rs.getDouble("price")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

        add(main);
        setVisible(true);
    }

    private void styleTable(JTable t) {
        t.setBackground(Theme.BG_CARD);
        t.setForeground(Theme.TEXT_WHITE);
        t.setGridColor(Theme.BORDER);
        t.setRowHeight(28);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setSelectionBackground(Theme.ACCENT_BLUE);
        t.setSelectionForeground(Color.WHITE);

        JTableHeader h = t.getTableHeader();
        h.setBackground(new Color(38, 38, 60));
        h.setForeground(Theme.TEXT_MUTED);
        h.setFont(new Font("Segoe UI", Font.BOLD, 12));
        h.setReorderingAllowed(false);

        int[] w = {50, 250, 200, 100};
        for (int i = 0; i < w.length; i++)
            t.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
    }
}