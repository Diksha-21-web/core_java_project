import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class IssueBook extends JFrame {

    JComboBox<String> studentDropdown, bookDropdown;
    JTextField issueId;
    DefaultTableModel model;

    public IssueBook() {
        setTitle("Issue / Return Book");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout(0, 12));
        main.setBackground(Theme.BG_DARK);
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ── TITLE ──
        JPanel titleP = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titleP.setBackground(Theme.BG_DARK);
        JLabel titleL = new JLabel("Issue / Return Book");
        titleL.setForeground(Theme.TEXT_WHITE);
        titleL.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleP.add(titleL);
        main.add(titleP, BorderLayout.NORTH);

        // ── FORM CARD ──
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Theme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1),
            BorderFactory.createEmptyBorder(20, 24, 20, 24)
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 10, 8, 10);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 0.33;

        // Labels row
        g.gridy = 0;
        g.gridx = 0; card.add(makeLabel("Student Name"), g);
        g.gridx = 1; card.add(makeLabel("Book Name"), g);
        g.gridx = 2; card.add(makeLabel("Issue ID (for return)"), g);

        // Inputs row
        studentDropdown = makeDropdown();
        bookDropdown    = makeDropdown();
        issueId         = Theme.makeField();

        g.gridy = 1;
        g.gridx = 0; card.add(studentDropdown, g);
        g.gridx = 1; card.add(bookDropdown, g);
        g.gridx = 2; card.add(issueId, g);

        // Buttons row
        JButton issueBtn  = Theme.makeButton("Issue Book",  Theme.ACCENT_GREEN);
        JButton returnBtn = Theme.makeButton("Return Book", Theme.ACCENT_BLUE);
        JLabel  rule      = makeLabel("Max 3 books  |  Fine ₹10/day after 7 days");

        issueBtn.setPreferredSize(new Dimension(180, 40));
        returnBtn.setPreferredSize(new Dimension(180, 40));

        g.gridy = 2;
        g.gridx = 0; card.add(issueBtn, g);
        g.gridx = 1; card.add(returnBtn, g);
        g.gridx = 2; card.add(rule, g);

        main.add(card, BorderLayout.CENTER);

        // ── TABLE ──
        model = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        model.setColumnIdentifiers(new String[]{
            "ID", "Student Name", "Book Name",
            "Issue Date", "Return Date", "Fine (₹)", "Status", "Books Issued"
        });

        JTable table = new JTable(model);
        styleTable(table);

        // ── SORTING ──
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(Theme.BG_CARD);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER));

        main.add(sp, BorderLayout.SOUTH);

        add(main);

        loadStudents();
        loadBooks();
        loadData();

        issueBtn.addActionListener(e  -> issueBook());
        returnBtn.addActionListener(e -> returnBook());

        setVisible(true);
    }

    // ── DROPDOWN STYLE (BLACK TEXT) ──
    private JComboBox<String> makeDropdown() {
        JComboBox<String> cb = new JComboBox<>();
        cb.setBackground(Color.WHITE);
        cb.setForeground(Color.BLACK);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setPreferredSize(new Dimension(0, 34));
        cb.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
                setForeground(isSelected ? Color.WHITE : Color.BLACK);
                setBackground(isSelected ? Theme.ACCENT_BLUE : Color.WHITE);
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
                setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                return this;
            }
        });
        return cb;
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Theme.TEXT_MUTED);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return l;
    }

    // ── LOAD STUDENTS ──
    void loadStudents() {
        studentDropdown.removeAllItems();
        try {
            Connection con = DBConnection.getConnection();
            ResultSet rs = con.createStatement()
                .executeQuery("SELECT name FROM students ORDER BY name");
            while (rs.next()) {
                studentDropdown.addItem(rs.getString("name"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading students: " + e.getMessage());
        }
    }

    // ── LOAD BOOKS ──
    void loadBooks() {
        bookDropdown.removeAllItems();
        try {
            Connection con = DBConnection.getConnection();
            ResultSet rs = con.createStatement()
                .executeQuery("SELECT title FROM books ORDER BY title");
            while (rs.next()) {
                bookDropdown.addItem(rs.getString("title"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading books: " + e.getMessage());
        }
    }

    // ── LOAD TABLE DATA ──
    void loadData() {
        model.setRowCount(0);
        try {
            Connection con = DBConnection.getConnection();
            ResultSet rs = con.createStatement().executeQuery(
                "SELECT i.id, s.name, b.title, i.issue_date, i.return_date, " +
                "(SELECT COUNT(*) FROM issue_books x " +
                " WHERE x.student_id=s.id AND x.return_date IS NULL) AS active_count " +
                "FROM issue_books i " +
                "JOIN students s ON i.student_id=s.id " +
                "JOIN books b ON i.book_id=b.id " +
                "ORDER BY i.id DESC"
            );
            while (rs.next()) {
                Date issueDate  = rs.getDate("issue_date");
                Date returnDate = rs.getDate("return_date");
                int  active     = rs.getInt("active_count");
                long fine = 0;
                String status;

                if (returnDate == null) {
                    long days = ChronoUnit.DAYS.between(
                        issueDate.toLocalDate(), LocalDate.now());
                    if (days > 7) fine = (days - 7) * 10;
                    status = "Active";
                } else {
                    long days = ChronoUnit.DAYS.between(
                        issueDate.toLocalDate(), returnDate.toLocalDate());
                    if (days > 7) fine = (days - 7) * 10;
                    status = "Returned";
                }

                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("title"),
                    issueDate,
                    returnDate != null ? returnDate : "—",
                    fine == 0 ? "—" : "₹" + fine,
                    status,
                    active + " / 3"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Load Error: " + e.getMessage());
        }
    }

    // ── ISSUE BOOK ──
    void issueBook() {
        String sName = (String) studentDropdown.getSelectedItem();
        String bName = (String) bookDropdown.getSelectedItem();

        if (sName == null || bName == null) {
            JOptionPane.showMessageDialog(this,
                "Select student and book!"); return;
        }
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps1 = con.prepareStatement(
                "SELECT id FROM students WHERE name=?");
            ps1.setString(1, sName);
            ResultSet rs1 = ps1.executeQuery();
            if (!rs1.next()) {
                JOptionPane.showMessageDialog(this,
                    "Student not found!"); return;
            }
            int sid = rs1.getInt(1);

            PreparedStatement ps2 = con.prepareStatement(
                "SELECT id FROM books WHERE title=?");
            ps2.setString(1, bName);
            ResultSet rs2 = ps2.executeQuery();
            if (!rs2.next()) {
                JOptionPane.showMessageDialog(this,
                    "Book not found!"); return;
            }
            int bid = rs2.getInt(1);

            // Limit check
            PreparedStatement chk = con.prepareStatement(
                "SELECT COUNT(*) FROM issue_books " +
                "WHERE student_id=? AND return_date IS NULL");
            chk.setInt(1, sid);
            ResultSet rc = chk.executeQuery();
            rc.next();
            if (rc.getInt(1) >= 3) {
                JOptionPane.showMessageDialog(this,
                    sName + " already has 3 books!\n" +
                    "Please return a book first."); return;
            }

            PreparedStatement ins = con.prepareStatement(
                "INSERT INTO issue_books(student_id,book_id,issue_date) " +
                "VALUES(?,?,CURDATE())");
            ins.setInt(1, sid);
            ins.setInt(2, bid);
            ins.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "Book issued to " + sName + "!");
            loadData();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage());
        }
    }

    // ── RETURN BOOK ──
    void returnBook() {
        String idText = issueId.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Enter Issue ID from table!"); return;
        }
        try {
            Connection con = DBConnection.getConnection();
            int iid = Integer.parseInt(idText);

            PreparedStatement chk = con.prepareStatement(
                "SELECT issue_date, return_date " +
                "FROM issue_books WHERE id=?");
            chk.setInt(1, iid);
            ResultSet rs = chk.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this,
                    "Issue ID not found!"); return;
            }
            if (rs.getDate("return_date") != null) {
                JOptionPane.showMessageDialog(this,
                    "Already returned!"); return;
            }

            LocalDate issueDate = rs.getDate("issue_date").toLocalDate();
            long days = ChronoUnit.DAYS.between(issueDate, LocalDate.now());
            long fine = (days > 7) ? (days - 7) * 10 : 0;

            PreparedStatement up = con.prepareStatement(
                "UPDATE issue_books SET return_date=CURDATE() WHERE id=?");
            up.setInt(1, iid);
            up.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "Book Returned!\n" +
                "Days kept: " + days + "\n" +
                "Fine: " + (fine == 0 ? "No fine" : "₹" + fine));
            issueId.setText("");
            loadData();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Issue ID must be a number!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage());
        }
    }

    // ── TABLE STYLE ──
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
        int[] w = {50, 150, 170, 100, 100, 80, 80, 100};
        for (int i = 0; i < w.length; i++)
            t.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
    }
}