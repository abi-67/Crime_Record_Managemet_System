import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class VictimUI extends JFrame {
    JTable table = new JTable();

    public VictimUI() {
        setTitle("Victim Module");
        setSize(800, 1000);
        setLayout(new BorderLayout());
        JLabel header = new JLabel("Victim Module", JLabel.CENTER);
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(30, 30, 40));
        header.setOpaque(true);
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton addBtn = new JButton("Add");
        btnPanel.add(addBtn);
        addBtn.addActionListener(e -> {
            JDialog dialog = new JDialog(VictimUI.this, "Add Victim", true);
            setTitle("Add Victim");
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(4, 2));
            dialog.add(new JLabel("Name"));
            JTextField nameField = new JTextField();
            dialog.add(nameField);
            dialog.add(new JLabel("contact"));
            JTextField contactField = new JTextField();
            dialog.add(contactField);
            dialog.add(new JLabel("Address"));
            JTextField addressField = new JTextField();
            dialog.add(addressField);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String name = nameField.getText();
                String contact = contactField.getText();
                String address = addressField.getText();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Insert into Victim(name,contact,address) values (?,?,?)");
                    ps.setString(1, name);
                    ps.setString(2, contact);
                    ps.setString(3, address);
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
                PreparedStatement ps = connection.prepareStatement("Delete from Victim where victim_id=?");
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
            String currentname = table.getValueAt(row, 1).toString();
            String currentcontact = table.getValueAt(row, 2).toString();
            String currentaddress = table.getValueAt(row, 3).toString();
            String currentId = table.getValueAt(row, 0).toString();
            JDialog dialog = new JDialog();
            dialog.setSize(400, 300);
            dialog.setTitle("Update Victim");
            dialog.setLayout(new GridLayout(4, 2));
            dialog.add(new JLabel("Name"));
            JTextField nameField = new JTextField();
            dialog.add(nameField);
            nameField.setText(currentname);
            dialog.add(new JLabel("contact"));
            JTextField contactField = new JTextField();
            dialog.add(contactField);
            contactField.setText(currentcontact);
            dialog.add(new JLabel("Address"));
            JTextField addressField = new JTextField();
            dialog.add(addressField);
            addressField.setText(currentaddress);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String name = nameField.getText();
                String contact = contactField.getText();
                String address = addressField.getText();
                String id = table.getValueAt(row, 0).toString();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Update Victim set name=?,contact=?,address=? where victim_id=?");
                    ps.setString(1, name);
                    ps.setString(2, contact);
                    ps.setString(3, address);
                    ps.setString(4, id);
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
            dialog.setTitle("Search Victim");
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(4, 2));
            dialog.add(new JLabel("Name"));
            JTextField nameField = new JTextField();
            dialog.add(nameField);
            dialog.add(new JLabel("contact"));
            JTextField contactField = new JTextField();
            dialog.add(contactField);
            dialog.add(new JLabel("Address"));
            JTextField addressField = new JTextField();
            dialog.add(addressField);
            JButton btnSearch = new JButton("Search");
            dialog.add(btnSearch);
            btnSearch.addActionListener(e1 -> {
                try {
                    String query = "Select * from Victim where 1=1";
                    if (!nameField.getText().isEmpty()) {
                        query += " AND name LIKE '%" + nameField.getText() + "%'";
                    }
                    if (!contactField.getText().isEmpty()) {
                        query += " AND contact =" + contactField.getText();
                    }
                    if (!addressField.getText().isEmpty()) {
                        query += " and address like '%" + addressField.getText() + "%'";
                    }
                    Connection connection = DBConnection.getConnection();
                    Statement stmt = connection.createStatement();
                    ResultSet resultSet = stmt.executeQuery(query);
                    String[] columns = {"victim_id", "name", "contact", "address"};
                    DefaultTableModel model = new DefaultTableModel(columns, 0);
                    while (resultSet.next()) {
                        Object[] row = {
                                resultSet.getString("victim_id"),
                                resultSet.getString("name"),
                                resultSet.getString("contact"),
                                resultSet.getString("address")
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
        ResultSet resultSet = stmt.executeQuery("Select * from Victim");
        String[] columns = {"victim_id", "Name", "contact", "Address"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        while (resultSet.next()) {
            Object[] row = {
                    resultSet.getString("victim_id"),
                    resultSet.getString("name"),
                    resultSet.getString("contact"),
                    resultSet.getString("address")
            };
            model.addRow(row);
        }
        table.setModel(model);
        connection.close();
    }

    public static void main(String[] args) {
        new VictimUI();
    }
}