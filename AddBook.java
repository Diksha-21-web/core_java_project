import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddBook extends JFrame {

    public AddBook() {
        setTitle("Add Book");
        setSize(420, 320);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Theme.BG_DARK);
        main.setBorder(BorderFactory.createEmptyBorder(24, 30, 24, 30));

        // Title
        JPanel titleP = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titleP.setBackground(Theme.BG_DARK);
        titleP.add(Theme.makeTitle("Add New Book"));
        main.add(titleP, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridLayout(4, 2, 12, 14));
        form.setBackground(Theme.BG_DARK);
        form.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JTextField tTitle  = Theme.makeField();
        JTextField tAuthor = Theme.makeField();
        JTextField tPrice  = Theme.makeField();

        form.add(Theme.makeLabel("Book Title"));   form.add(tTitle);
        form.add(Theme.makeLabel("Author Name"));  form.add(tAuthor);
        form.add(Theme.makeLabel("Price (₹)"));    form.add(tPrice);
        form.add(new JLabel(""));

        main.add(form, BorderLayout.CENTER);

        // Button
        JPanel btnP = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnP.setBackground(Theme.BG_DARK);
        JButton addBtn = Theme.makeButton("Add Book", Theme.ACCENT_GREEN);
        btnP.add(addBtn);
        main.add(btnP, BorderLayout.SOUTH);

        add(main);

        addBtn.addActionListener(e -> {
            if (tTitle.getText().trim().isEmpty() ||
                tAuthor.getText().trim().isEmpty() ||
                tPrice.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields!");
                return;
            }
            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO books(title, author, price) VALUES (?, ?, ?)");
                ps.setString(1, tTitle.getText().trim());
                ps.setString(2, tAuthor.getText().trim());
                ps.setDouble(3, Double.parseDouble(tPrice.getText().trim()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Book added successfully!");
                tTitle.setText(""); tAuthor.setText(""); tPrice.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }
}