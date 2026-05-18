import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentManagementSystem {

    JFrame frame;
    JTextField idField, nameField, courseField, yearField, contactField;
    JTable table;
    DefaultTableModel model;

    public StudentManagementSystem() {

        frame = new JFrame("Student Record Management System");

    
        frame.getContentPane().setBackground(new Color(220, 240, 255));

    
        JLabel title = new JLabel("Student Record Management");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(250, 10, 400, 30);

        JLabel idLabel = new JLabel("ID:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel courseLabel = new JLabel("Course:");
        JLabel yearLabel = new JLabel("Year:");
        JLabel contactLabel = new JLabel("Contact:");

        idLabel.setBounds(30, 60, 100, 25);
        nameLabel.setBounds(30, 100, 100, 25);
        courseLabel.setBounds(30, 140, 100, 25);
        yearLabel.setBounds(30, 180, 100, 25);
        contactLabel.setBounds(30, 220, 100, 25);

    
        idField = new JTextField();
        nameField = new JTextField();
        courseField = new JTextField();
        yearField = new JTextField();
        contactField = new JTextField();

        idField.setBounds(120, 60, 150, 25);
        nameField.setBounds(120, 100, 150, 25);
        courseField.setBounds(120, 140, 150, 25);
        yearField.setBounds(120, 180, 150, 25);
        contactField.setBounds(120, 220, 150, 25);

        
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");

        addBtn.setBounds(30, 270, 80, 30);
        updateBtn.setBounds(120, 270, 90, 30);
        deleteBtn.setBounds(220, 270, 90, 30);
        clearBtn.setBounds(320, 270, 90, 30);

        
        addBtn.setBackground(new Color(100, 200, 100));
        updateBtn.setBackground(new Color(255, 200, 100));
        deleteBtn.setBackground(new Color(255, 120, 120));
        clearBtn.setBackground(new Color(180, 180, 180));


        model = new DefaultTableModel();
        table = new JTable(model);

        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Course");
        model.addColumn("Year");
        model.addColumn("Contact");

        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(320, 60, 450, 180);

        
        frame.add(title);
        frame.add(idLabel); frame.add(nameLabel);
        frame.add(courseLabel); frame.add(yearLabel); frame.add(contactLabel);

        frame.add(idField); frame.add(nameField);
        frame.add(courseField); frame.add(yearField); frame.add(contactField);

        frame.add(addBtn); frame.add(updateBtn);
        frame.add(deleteBtn); frame.add(clearBtn);

        frame.add(pane);

        
        frame.setSize(800, 400);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        loadTable();

        
        addBtn.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO students(name, course, year, contact) VALUES (?, ?, ?, ?)"
                );

                ps.setString(1, nameField.getText());
                ps.setString(2, courseField.getText());
                ps.setString(3, yearField.getText());
                ps.setString(4, contactField.getText());

                ps.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Record Added");

                loadTable();
                clearFields();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        
        updateBtn.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE students SET name=?, course=?, year=?, contact=? WHERE id=?"
                );

                ps.setString(1, nameField.getText());
                ps.setString(2, courseField.getText());
                ps.setString(3, yearField.getText());
                ps.setString(4, contactField.getText());
                ps.setInt(5, Integer.parseInt(idField.getText()));

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(frame, "Record Updated");
                } else {
                    JOptionPane.showMessageDialog(frame, "ID not found!");
                }

                loadTable();
                clearFields();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        deleteBtn.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM students WHERE id=?"
                );

                ps.setInt(1, Integer.parseInt(idField.getText()));
                ps.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Record Deleted");

                loadTable();
                clearFields();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        
        clearBtn.addActionListener(e -> clearFields());

        
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = table.getSelectedRow();

                idField.setText(model.getValueAt(i, 0).toString());
                nameField.setText(model.getValueAt(i, 1).toString());
                courseField.setText(model.getValueAt(i, 2).toString());
                yearField.setText(model.getValueAt(i, 3).toString());
                contactField.setText(model.getValueAt(i, 4).toString());
            }
        });
    }

    
    void loadTable() {
        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM students");

            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("course"),
                        rs.getString("year"),
                        rs.getString("contact")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void clearFields() {
        idField.setText("");
        nameField.setText("");
        courseField.setText("");
        yearField.setText("");
        contactField.setText("");
    }

    public static void main(String[] args) {
        new StudentManagementSystem();
    }
}
































































































