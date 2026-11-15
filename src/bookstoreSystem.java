package project;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class bookstoreSystem extends JFrame {

    JComboBox<String> tables;
    JPanel formPanel = new JPanel();

    JTextField f1 = new JTextField();
    JTextField f2 = new JTextField();
    JTextField f3 = new JTextField();
    JTextField f4 = new JTextField();
    JTextField f5 = new JTextField();

    String currentTable = "Book";

    Font bigFont = new Font("Segoe UI", Font.BOLD, 34);
    Font labelFont = new Font("Segoe UI", Font.BOLD, 28);
    Font inputFont = new Font("Segoe UI", Font.PLAIN, 26);

    public bookstoreSystem() {

        setTitle("📚 E-Book System");
        setSize(1500, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 20));

        JPanel top = new JPanel();

        tables = new JComboBox<>(new String[]{"Book", "Author", "Publisher"});
        tables.setFont(bigFont);

        JButton load = new JButton("LOAD");
        load.setFont(bigFont);

        top.add(tables);
        top.add(load);

        add(top, BorderLayout.NORTH);

        formPanel.setLayout(new GridLayout(6, 2, 20, 20));
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 120, 40, 120));
        add(formPanel, BorderLayout.CENTER);

        JTextField[] fields = {f1, f2, f3, f4, f5};
        for (JTextField f : fields) f.setFont(inputFont);

        JPanel btns = new JPanel();

        JButton insert = new JButton("INSERT");
        JButton search = new JButton("SEARCH");
        JButton update = new JButton("UPDATE");
        JButton delete = new JButton("DELETE");

        JButton[] buttons = {insert, search, update, delete};

        for (JButton b : buttons) {
            b.setFont(bigFont);
            b.setPreferredSize(new Dimension(220, 80));
            btns.add(b);
        }

        add(btns, BorderLayout.SOUTH);

        load.addActionListener(e -> {
            currentTable = tables.getSelectedItem().toString();
            buildForm();
        });

        insert.addActionListener(e -> insertData());
        search.addActionListener(e -> searchData());
        update.addActionListener(e -> updateData());
        delete.addActionListener(e -> deleteData());

        setVisible(true);
    }

    void buildForm() {
        formPanel.removeAll();

        if (currentTable.equals("Book")) {
            addRow("ISBN", f1);
            addRow("Title", f2);
            addRow("Type", f3);
            addRow("Pages", f4);
            addRow("Price", f5);

        } else if (currentTable.equals("Author")) {
            addRow("Auth ID", f1);
            addRow("First Name", f2);
            addRow("Last Name", f3);

        } else {
            addRow("Pub Code", f1);
            addRow("Name", f2);
            addRow("City", f3);
            addRow("Phone", f4);
        }

        formPanel.revalidate();
        formPanel.repaint();
    }

    void addRow(String text, JTextField field) {
        JLabel l = new JLabel(text);
        l.setFont(labelFont);
        formPanel.add(l);
        formPanel.add(field);
    }

    Connection getCon() throws Exception {
        return DriverManager.getConnection(
                "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=E_book;encrypt=true;trustServerCertificate=true",
                "project",
                "1234"
        );
    }

    // ================= INSERT =================
    void insertData() {
        try (Connection con = getCon()) {

            PreparedStatement ps;

            if (currentTable.equals("Book")) {

                ps = con.prepareStatement(
                        "INSERT INTO Book VALUES (?,?,?,?,?,?)"
                );

                ps.setString(1, f1.getText());
                ps.setString(2, f2.getText());
                ps.setString(3, f3.getText());
                ps.setInt(4, Integer.parseInt(f4.getText().isEmpty() ? "0" : f4.getText()));
                ps.setDouble(5, Double.parseDouble(f5.getText().isEmpty() ? "0" : f5.getText()));
                ps.setString(6, null); // pubcode (optional)

            } else if (currentTable.equals("Author")) {

                ps = con.prepareStatement("INSERT INTO Author VALUES (?,?,?)");
                ps.setString(1, f1.getText());
                ps.setString(2, f2.getText());
                ps.setString(3, f3.getText());

            } else {

                ps = con.prepareStatement("INSERT INTO Publisher VALUES (?,?,?,?)");
                ps.setString(1, f1.getText());
                ps.setString(2, f2.getText());
                ps.setString(3, f3.getText());
                ps.setString(4, f4.getText());
            }

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "✔ Inserted Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= SEARCH =================
    void searchData() {
        try (Connection con = getCon()) {

            PreparedStatement ps;
            ResultSet rs;

            if (currentTable.equals("Book")) {

                ps = con.prepareStatement("SELECT * FROM Book WHERE ISBN=?");
                ps.setString(1, f1.getText());
                rs = ps.executeQuery();

                if (rs.next()) {
                    f2.setText(rs.getString("title"));
                    f3.setText(rs.getString("type"));
                    f4.setText(String.valueOf(rs.getInt("pages")));
                    f5.setText(String.valueOf(rs.getDouble("price")));
                }

            } else if (currentTable.equals("Author")) {

                ps = con.prepareStatement("SELECT * FROM Author WHERE authid=?");
                ps.setString(1, f1.getText());
                rs = ps.executeQuery();

                if (rs.next()) {
                    f2.setText(rs.getString("f_name"));
                    f3.setText(rs.getString("l_name"));
                }

            } else {

                ps = con.prepareStatement("SELECT * FROM Publisher WHERE pubcode=?");
                ps.setString(1, f1.getText());
                rs = ps.executeQuery();

                if (rs.next()) {
                    f2.setText(rs.getString("name"));
                    f3.setText(rs.getString("city"));
                    f4.setText(rs.getString("phone"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= UPDATE =================
    void updateData() {
        try (Connection con = getCon()) {

            PreparedStatement ps;

            if (currentTable.equals("Book")) {

                ps = con.prepareStatement(
                        "UPDATE Book SET title=?,type=?,pages=?,price=? WHERE ISBN=?"
                );

                ps.setString(1, f2.getText());
                ps.setString(2, f3.getText());
                ps.setInt(3, Integer.parseInt(f4.getText().isEmpty() ? "0" : f4.getText()));
                ps.setDouble(4, Double.parseDouble(f5.getText().isEmpty() ? "0" : f5.getText()));
                ps.setString(5, f1.getText());

            } else if (currentTable.equals("Author")) {

                ps = con.prepareStatement(
                        "UPDATE Author SET f_name=?,l_name=? WHERE authid=?"
                );

                ps.setString(1, f2.getText());
                ps.setString(2, f3.getText());
                ps.setString(3, f1.getText());

            } else {

                ps = con.prepareStatement(
                        "UPDATE Publisher SET name=?,city=?,phone=? WHERE pubcode=?"
                );

                ps.setString(1, f2.getText());
                ps.setString(2, f3.getText());
                ps.setString(3, f4.getText());
                ps.setString(4, f1.getText());
            }

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "✔ Updated");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= DELETE =================
    void deleteData() {
        try (Connection con = getCon()) {

            PreparedStatement ps;

            if (currentTable.equals("Book")) {
                ps = con.prepareStatement("DELETE FROM Book WHERE ISBN=?");
                ps.setString(1, f1.getText());

            } else if (currentTable.equals("Author")) {
                ps = con.prepareStatement("DELETE FROM Author WHERE authid=?");
                ps.setString(1, f1.getText());

            } else {
                ps = con.prepareStatement("DELETE FROM Publisher WHERE pubcode=?");
                ps.setString(1, f1.getText());
            }

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "✔ Deleted");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new bookstoreSystem();
    }
}