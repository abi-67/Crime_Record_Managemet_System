import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CrimeUI extends JFrame {
    JTable table = new JTable();

    public CrimeUI() {
        setTitle("Crime Module");
        setSize(800, 1000);
        setLayout(new BorderLayout());
        JLabel header = new JLabel("Crime Module", JLabel.CENTER);
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(30, 30, 60));
        header.setOpaque(true);
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton addBtn = new JButton("Add");
        btnPanel.add(addBtn);
        addBtn.addActionListener(e -> {
            JDialog dialog = new JDialog(CrimeUI.this, "Add Crime", true);
            setTitle("Add Crime");
            dialog.setSize(400, 600);
            dialog.setLayout(new GridLayout(5, 2));
            dialog.add(new JLabel("crime_type"));
            JTextField crime_typeField = new JTextField();
            dialog.add(crime_typeField);
            dialog.add(new JLabel("crime_date"));
            JTextField crime_dateField = new JTextField();
            dialog.add(crime_dateField);
            dialog.add(new JLabel("description"));
            JTextField descriptionField = new JTextField();
            dialog.add(descriptionField);
            dialog.add(new JLabel("status"));
            JTextField statusField = new JTextField();
            dialog.add(statusField);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String crime_type = crime_typeField.getText();
                String crime_date = crime_dateField.getText();
                String description = descriptionField.getText();
                String status = statusField.getText();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Insert into Crime(crime_type,crime_date,description,status) values (?,?,?,?)");
                    ps.setString(1, crime_type);
                    ps.setString(2, crime_date);
                    ps.setString(3, description);
                    ps.setString(4, status);
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
                PreparedStatement ps = connection.prepareStatement("Delete from Crime where crime_id=?");
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
            String currentcrime_type = table.getValueAt(row, 1).toString();
            String currentcrime_date = table.getValueAt(row, 2).toString();
            String currentdescription = table.getValueAt(row, 3).toString();
            String currentstatus = table.getValueAt(row, 4).toString();
            String currentId = table.getValueAt(row, 0).toString();
            JDialog dialog = new JDialog();
            dialog.setSize(400, 300);
            dialog.setTitle("Update Crime");
            dialog.setLayout(new GridLayout(5, 2));
            dialog.add(new JLabel("crime_type"));
            JTextField crime_typeField = new JTextField();
            dialog.add(crime_typeField);
            crime_typeField.setText(currentcrime_type);
            dialog.add(new JLabel("crime_date"));
            JTextField crime_dateField = new JTextField();
            dialog.add(crime_dateField);
            crime_dateField.setText(currentcrime_date);
            dialog.add(new JLabel("description"));
            JTextField descriptionField = new JTextField();
            dialog.add(descriptionField);
            descriptionField.setText(currentdescription);
            dialog.add(new JLabel("status"));
            JTextField statusField = new JTextField();
            dialog.add(statusField);
            statusField.setText(currentstatus);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String crime_type = crime_typeField.getText();
                String crime_date = crime_dateField.getText();
                String description = descriptionField.getText();
                String status = statusField.getText();
                String id = table.getValueAt(row, 0).toString();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Update Crime set crime_type=?,crime_date=?,description=?,status=?,status=? where crime_id=?");
                    ps.setString(1, crime_type);
                    ps.setString(2, crime_date);
                    ps.setString(3, description);
                    ps.setString(4, status);
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
            dialog.setTitle("Search Crime");
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(6, 2));
            dialog.add(new JLabel("crime_type"));
            JTextField crime_typeField = new JTextField();
            dialog.add(crime_typeField);
            dialog.add(new JLabel("crime_date"));
            JTextField crime_dateField = new JTextField();
            dialog.add(crime_dateField);
            dialog.add(new JLabel("description"));
            JTextField descriptionField = new JTextField();
            dialog.add(descriptionField);
            dialog.add(new JLabel("status"));
            JTextField statusField = new JTextField();
            dialog.add(statusField);
            JButton btnSearch = new JButton("Search");
            dialog.add(btnSearch);
            btnSearch.addActionListener(e1 -> {
                try {
                    String query = "Select * from Crime where 1=1";
                    if (!crime_typeField.getText().isEmpty()) {
                        query += " AND crime_type LIKE '%" + crime_typeField.getText() + "%'";
                    }
                    if (!crime_dateField.getText().isEmpty()) {
                        query += " AND crime_date=" + crime_dateField.getText();
                    }
                    if (!statusField.getText().isEmpty()) {
                        query += " and status like '%" + statusField.getText() + "%'";
                    }
                    Connection connection = DBConnection.getConnection();
                    Statement stmt = connection.createStatement();
                    ResultSet resultSet = stmt.executeQuery(query);
                    String[] columns = {"crime_id", "crime_type", "crime_date", "description", "status"};
                    DefaultTableModel model = new DefaultTableModel(columns, 0);
                    while (resultSet.next()) {
                        Object[] row = {
                                resultSet.getString("crime_id"),
                                resultSet.getString("crime_type"),
                                resultSet.getString("crime_date"),
                                resultSet.getString("description"),
                                resultSet.getString("status"),
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
        ResultSet resultSet = stmt.executeQuery("Select * from Crime");
        String[] columns = {"crime_id", "crime_type", "crime_date", "description", "status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        while (resultSet.next()) {
            Object[] row = {
                    resultSet.getString("crime_id"),
                    resultSet.getString("crime_type"),
                    resultSet.getString("crime_date"),
                    resultSet.getString("description"),
                    resultSet.getString("status")
            };
            model.addRow(row);
        }
        table.setModel(model);
        connection.close();
    }

    public static void main(String[] args) {
        new CrimeUI();
    }
}