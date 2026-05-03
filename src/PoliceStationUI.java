import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PoliceStationUI extends JFrame {
    JTable table = new JTable();

    public PoliceStationUI() {
        setTitle("PoliceStation Module");
        setSize(800, 1000);
        setLayout(new BorderLayout());
        JLabel header = new JLabel("PoliceStation Module", JLabel.CENTER);
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(30, 30, 40));
        header.setOpaque(true);
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton addBtn = new JButton("Add");
        btnPanel.add(addBtn);
        addBtn.addActionListener(e -> {
            JDialog dialog = new JDialog(PoliceStationUI.this, "Add PoliceStation", true);
            setTitle("Add PoliceStation");
            dialog.setSize(400, 400);
            dialog.setLayout(new GridLayout(4, 2));
            dialog.add(new JLabel("Name"));
            JTextField nameField = new JTextField();
            dialog.add(nameField);
            dialog.add(new JLabel("Location"));
            JTextField locationField = new JTextField();
            dialog.add(locationField);
            dialog.add(new JLabel("Contact Number"));
            JTextField contact_numberField = new JTextField();
            dialog.add(contact_numberField);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String name = nameField.getText();
                String location = locationField.getText();
                String contact_number = contact_numberField.getText();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Insert into Police_Station(name,location,contact_number) values (?,?,?)");
                    ps.setString(1, name);
                    ps.setString(2, location);
                    ps.setString(3, contact_number);
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
                PreparedStatement ps = connection.prepareStatement("Delete from Police_Station where station_id=?");
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
            String currentlocation = table.getValueAt(row, 2).toString();
            String currentcontact_number = table.getValueAt(row, 3).toString();
            String currentId = table.getValueAt(row, 0).toString();
            JDialog dialog = new JDialog();
            dialog.setSize(400, 300);
            dialog.setTitle("Update PoliceStation");
            dialog.setLayout(new GridLayout(4, 2));
            dialog.add(new JLabel("Name"));
            JTextField nameField = new JTextField();
            dialog.add(nameField);
            nameField.setText(currentname);
            dialog.add(new JLabel("Location"));
            JTextField locationField = new JTextField();
            dialog.add(locationField);
            locationField.setText(currentlocation);
            dialog.add(new JLabel("Contact Number"));
            JTextField contact_numberField = new JTextField();
            dialog.add(contact_numberField);
            contact_numberField.setText(currentcontact_number);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String name = nameField.getText();
                String location = locationField.getText();
                String contact_number = contact_numberField.getText();
                String id = table.getValueAt(row, 0).toString();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Update Police_Station set name=?,location=?,contact_number=? where station_id=?");
                    ps.setString(1, name);
                    ps.setString(2, location);
                    ps.setString(3, contact_number);
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
            dialog.setTitle("Search PoliceStation");
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(4, 2));
            dialog.add(new JLabel("Name"));
            JTextField nameField = new JTextField();
            dialog.add(nameField);
            dialog.add(new JLabel("Location"));
            JTextField locationField = new JTextField();
            dialog.add(locationField);
            dialog.add(new JLabel("Contact Number"));
            JTextField contact_numberField = new JTextField();
            dialog.add(contact_numberField);
            JButton btnSearch = new JButton("Search");
            dialog.add(btnSearch);
            btnSearch.addActionListener(e1 -> {
                try {
                    String query = "Select * from Police_Station where 1=1";
                    if (!nameField.getText().isEmpty()) {
                        query += " AND name LIKE '%" + nameField.getText() + "%'";
                    }
                    if (!locationField.getText().isEmpty()) {
                        query += " AND location like '%" + locationField.getText() + "%'";
                    }
                    if (!contact_numberField.getText().isEmpty()) {
                        query += " and contact_number =" + contact_numberField.getText();
                    }
                    Connection connection = DBConnection.getConnection();
                    Statement stmt = connection.createStatement();
                    ResultSet resultSet = stmt.executeQuery(query);
                    String[] columns = {"station_id", "name", "location", "contact_number"};
                    DefaultTableModel model = new DefaultTableModel(columns, 0);
                    while (resultSet.next()) {
                        Object[] row = {
                                resultSet.getString("station_id"),
                                resultSet.getString("name"),
                                resultSet.getString("location"),
                                resultSet.getString("contact_number")
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
        ResultSet resultSet = stmt.executeQuery("Select * from Police_Station");
        String[] columns = {"Station_id", "Name", "Location", "Contact Number"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        while (resultSet.next()) {
            Object[] row = {
                    resultSet.getString("station_id"),
                    resultSet.getString("name"),
                    resultSet.getString("location"),
                    resultSet.getString("contact_number")
            };
            model.addRow(row);
        }
        table.setModel(model);
        connection.close();
    }

    public static void main(String[] args) {
        new PoliceStationUI();
    }
}