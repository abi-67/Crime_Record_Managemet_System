import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CriminalUI extends JFrame {
    JTable table = new JTable();

    public CriminalUI() {
        setTitle("Criminal Module");
        setSize(800, 1000);
        setLayout(new BorderLayout());
        JLabel header = new JLabel("Criminal Module", JLabel.CENTER);
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(30, 30, 60));
        header.setOpaque(true);
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton addBtn = new JButton("Add");
        btnPanel.add(addBtn);
        addBtn.addActionListener(e -> {
            JDialog dialog = new JDialog(CriminalUI.this, "Add Criminals", true);
            setTitle("Add Criminals");
            dialog.setSize(400, 600);
            dialog.setLayout(new GridLayout(6, 2));
            dialog.add(new JLabel("Name"));
            JTextField nameField = new JTextField();
            dialog.add(nameField);
            dialog.add(new JLabel("Age"));
            JTextField ageField = new JTextField();
            dialog.add(ageField);
            dialog.add(new JLabel("Gender"));
            JTextField genderField = new JTextField();
            dialog.add(genderField);
            dialog.add(new JLabel("Address"));
            JTextField addressField = new JTextField();
            dialog.add(addressField);
            dialog.add(new JLabel("Status"));
            JTextField statusField = new JTextField();
            dialog.add(statusField);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String name = nameField.getText();
                String age = ageField.getText();
                String gender = genderField.getText();
                String address = addressField.getText();
                String status = statusField.getText();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Insert into Criminals(name,age,gender,address,status) values (?,?,?,?,?)");
                    ps.setString(1, name);
                    ps.setString(2, age);
                    ps.setString(3, gender);
                    ps.setString(4, address);
                    ps.setString(5, status);
                    ps.executeUpdate();
                    dialog.dispose();
                    loadData();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            });
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        });
        JButton delBtn = new JButton("Delete");
        btnPanel.add(delBtn);
        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select the row");
                return;
            }
            String id = table.getValueAt(row, 0).toString();
            try {
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement("Delete from Criminals where criminal_id=?");
                ps.setString(1, id);
                ps.executeUpdate();
                loadData();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        JButton updateBtn = new JButton("Update");
        btnPanel.add(updateBtn);
        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select the row");
                return;
            }
            String currentName = table.getValueAt(row, 1).toString();
            String currentAge = table.getValueAt(row, 2).toString();
            String currentGender = table.getValueAt(row, 3).toString();
            String currentAddress = table.getValueAt(row, 4).toString();
            String currentStatus = table.getValueAt(row, 5).toString();
            String currentId = table.getValueAt(row, 0).toString();
            JDialog dialog = new JDialog();
            dialog.setSize(400, 300);
            dialog.setTitle("Update Criminals");
            dialog.setLayout(new GridLayout(6, 2));
            dialog.add(new JLabel("Name"));
            JTextField nameField = new JTextField();
            dialog.add(nameField);
            nameField.setText(currentName);
            dialog.add(new JLabel("Age"));
            JTextField ageField = new JTextField();
            dialog.add(ageField);
            ageField.setText(currentAge);
            dialog.add(new JLabel("Gender"));
            JTextField genderField = new JTextField();
            dialog.add(genderField);
            genderField.setText(currentGender);
            dialog.add(new JLabel("Address"));
            JTextField addressField = new JTextField();
            dialog.add(addressField);
            addressField.setText(currentAddress);
            dialog.add(new JLabel("Status"));
            JTextField statusField = new JTextField();
            dialog.add(statusField);
            statusField.setText(currentStatus);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String name = nameField.getText();
                String age = ageField.getText();
                String gender = genderField.getText();
                String address = addressField.getText();
                String status = statusField.getText();
                String id = table.getValueAt(row, 0).toString();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Update Criminals set name=?,age=?,gender=?,address=?,status=? where criminal_id=?");
                    ps.setString(1, name);
                    ps.setString(2, age);
                    ps.setString(3, gender);
                    ps.setString(4, address);
                    ps.setString(5, status);
                    ps.setString(6, id);
                    ps.executeUpdate();
                    dialog.dispose();
                    loadData();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        });
        JButton searchBtn = new JButton("Search");
        btnPanel.add(searchBtn);
        searchBtn.addActionListener(e -> {
            JDialog dialog = new JDialog();
            dialog.setTitle("Search Criminals");
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(6, 2));
            dialog.add(new JLabel("Name"));
            JTextField nameField = new JTextField();
            dialog.add(nameField);
            dialog.add(new JLabel("Age"));
            JTextField ageField = new JTextField();
            dialog.add(ageField);
            dialog.add(new JLabel("Gender"));
            JTextField genderField = new JTextField();
            dialog.add(genderField);
            dialog.add(new JLabel("Address"));
            JTextField addressField = new JTextField();
            dialog.add(addressField);
            dialog.add(new JLabel("Status"));
            JTextField statusField = new JTextField();
            dialog.add(statusField);
            JButton btnSearch = new JButton("Search");
            dialog.add(btnSearch);
            btnSearch.addActionListener(e1 -> {
                try {
                    String query = "Select * from Criminals where 1=1";
                    if (!nameField.getText().isEmpty()) {
                        query += " AND name LIKE '%" + nameField.getText() + "%'";
                    }
                    if (!ageField.getText().isEmpty()) {
                        query += " AND age=" + ageField.getText();
                    }
                    if (!addressField.getText().isEmpty()) {
                        query += " and address like '%" + addressField.getText() + "%'";
                    }
                    if (!statusField.getText().isEmpty()) {
                        query += " and status like '%" + statusField.getText() + "%'";
                    }
                    Connection connection = DBConnection.getConnection();
                    Statement stmt = connection.createStatement();
                    ResultSet resultSet = stmt.executeQuery(query);
                    String[] columns = {"Criminal_id", "Name", "Age", "Gender", "Address", "Status"};
                    DefaultTableModel model = new DefaultTableModel(columns, 0);
                    while (resultSet.next()) {
                        Object[] row = {
                                resultSet.getString("criminal_id"),
                                resultSet.getString("name"),
                                resultSet.getString("age"),
                                resultSet.getString("gender"),
                                resultSet.getString("address"),
                                resultSet.getString("status")
                        };
                        model.addRow(row);
                    }
                    table.setModel(model);
                    dialog.dispose();
                    connection.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            dialog.setVisible(true);
        });
        try {
            loadData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        add(header, BorderLayout.NORTH);
        add(btnPanel, BorderLayout.SOUTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    void loadData() throws SQLException {
        Connection connection = DBConnection.getConnection();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("Select * from Criminals");
        String[] columns = {"Criminal_id", "Name", "Age", "Gender", "Address", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        while (resultSet.next()) {
            Object[] row = {
                    resultSet.getString("criminal_id"),
                    resultSet.getString("name"),
                    resultSet.getString("age"),
                    resultSet.getString("gender"),
                    resultSet.getString("address"),
                    resultSet.getString("status")
            };
            model.addRow(row);
        }
        table.setModel(model);
        connection.close();

    }

    public static void main(String[] args) {
        new CriminalUI();
    }
}