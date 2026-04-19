import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentDemo extends Frame implements ActionListener {

    TextField t1, t2;
    Button insert, view, update, delete;
    Connection con;

    StudentDemo() {

        // Layout
        setLayout(new FlowLayout());

        // Components
        add(new Label("ID:"));
        t1 = new TextField(10);
        add(t1);

        add(new Label("Name:"));
        t2 = new TextField(10);
        add(t2);

        insert = new Button("Insert");
        view = new Button("View");
        update = new Button("Update");
        delete = new Button("Delete");

        add(insert);
        add(view);
        add(update);
        add(delete);

        // Button actions
        insert.addActionListener(this);
        view.addActionListener(this);
        update.addActionListener(this);
        delete.addActionListener(this);

        // Frame settings
        setSize(300, 200);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // Database connection
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:student.db");

            Statement stmt = con.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS students (id INTEGER PRIMARY KEY, name TEXT)");

            System.out.println("Connected to Database!");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void actionPerformed(ActionEvent e) {

        try {
            int id = Integer.parseInt(t1.getText());
            String name = t2.getText();

            // INSERT
            if (e.getSource() == insert) {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO students VALUES (?, ?)");
                ps.setInt(1, id);
                ps.setString(2, name);
                ps.executeUpdate();
                System.out.println("Inserted!");
            }

            // VIEW
            if (e.getSource() == view) {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM students");

                while (rs.next()) {
                    System.out.println(rs.getInt("id") + " " + rs.getString("name"));
                }
            }

            // UPDATE
            if (e.getSource() == update) {
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE students SET name=? WHERE id=?");
                ps.setString(1, name);
                ps.setInt(2, id);
                ps.executeUpdate();
                System.out.println("Updated!");
            }

            // DELETE
            if (e.getSource() == delete) {
                PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM students WHERE id=?");
                ps.setInt(1, id);
                ps.executeUpdate();
                System.out.println("Deleted!");
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public static void main(String[] args) {
        new StudentDemo();
    }
}