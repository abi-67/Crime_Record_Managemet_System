import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PrisonUI extends JFrame {
    JTable table = new JTable();

    public PrisonUI() {
        setTitle("Prison Module");
        setSize(800, 1000);
        setLayout(new BorderLayout());
        JLabel header = new JLabel("Prison Module", JLabel.CENTER);
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(30, 30, 60));
        header.setOpaque(true);
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton addBtn = new JButton("Add");
        btnPanel.add(addBtn);
        addBtn.addActionListener(e -> {
            JDialog dialog = new JDialog(PrisonUI.this, "Add Prison", true);
            setTitle("Add Prison");
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(3, 2));
            dialog.add(new JLabel("name"));
            JTextField nameField = new JTextField();
            dialog.add(nameField);
            dialog.add(new JLabel("location"));
            JTextField locationField = new JTextField();
            dialog.add(locationField);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String name = nameField.getText();
                String location = locationField.getText();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Insert into Prison(name,location) values (?,?)");
                    ps.setString(1, name);
                    ps.setString(2, location);

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
                PreparedStatement ps = connection.prepareStatement("Delete from Prison where prison_id=?");
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
            String currentId = table.getValueAt(row, 0).toString();
            JDialog dialog = new JDialog();
            dialog.setSize(300, 300);
            dialog.setTitle("Update Prison");
            dialog.setLayout(new GridLayout(3, 2));
            dialog.add(new JLabel("name"));
            JTextField nameField = new JTextField();
            dialog.add(nameField);
            nameField.setText(currentname);
            dialog.add(new JLabel("location"));
            JTextField locationField = new JTextField();
            dialog.add(locationField);
            locationField.setText(currentlocation);

            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String name = nameField.getText();
                String location = locationField.getText();
                String id = table.getValueAt(row, 0).toString();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Update Prison set name=?,location=? where prison_id=?");
                    ps.setString(1, name);
                    ps.setString(2, location);
                    ps.setString(3, id);
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
            dialog.setTitle("Search Prison");
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(4, 2));
            dialog.add(new JLabel("name"));
            JTextField nameField = new JTextField();
            dialog.add(nameField);
            dialog.add(new JLabel("location"));
            JTextField locationField = new JTextField();
            dialog.add(locationField);
            JButton btnSearch = new JButton("Search");
            dialog.add(btnSearch);
            btnSearch.addActionListener(e1 -> {
                try {
                    String query = "Select * from Prison where 1=1";
                    if (!nameField.getText().isEmpty()) {
                        query += " AND name LIKE '%" + nameField.getText() + "%'";
                    }
                    if (!locationField.getText().isEmpty()) {
                        query += " AND location like '%" + locationField.getText() + "%'";
                    }

                    Connection connection = DBConnection.getConnection();
                    Statement stmt = connection.createStatement();
                    ResultSet resultSet = stmt.executeQuery(query);
                    String[] columns = {"prison_id", "name", "location"};
                    DefaultTableModel model = new DefaultTableModel(columns, 0);
                    while (resultSet.next()) {
                        Object[] row = {
                                resultSet.getString("prison_id"),
                                resultSet.getString("name"),
                                resultSet.getString("location"),
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
        ResultSet resultSet = stmt.executeQuery("Select * from Prison");
        String[] columns = {"prison_id", "name", "location"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        while (resultSet.next()) {
            Object[] row = {
                    resultSet.getString("prison_id"),
                    resultSet.getString("name"),
                    resultSet.getString("location")
            };
            model.addRow(row);
        }
        table.setModel(model);
        connection.close();
    }

    public static void main(String[] args) {
        new PrisonUI();
    }
}