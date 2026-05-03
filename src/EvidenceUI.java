import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class EvidenceUI extends JFrame {
    JTable table = new JTable();

    public EvidenceUI() {
        setTitle("Evidence Module");
        setSize(800, 1000);
        setLayout(new BorderLayout());
        JLabel header = new JLabel("Evidence Module", JLabel.CENTER);
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(30, 30, 60));
        header.setOpaque(true);
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton addBtn = new JButton("Add");
        btnPanel.add(addBtn);
        addBtn.addActionListener(e -> {
            JDialog dialog = new JDialog(EvidenceUI.this, "Add Evidence", true);
            setTitle("Add Evidence");
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(4, 2));
            dialog.add(new JLabel("crime_id"));
            JTextField crime_idField = new JTextField();
            dialog.add(crime_idField);
            dialog.add(new JLabel("type"));
            JTextField typeField = new JTextField();
            dialog.add(typeField);
            dialog.add(new JLabel("Description"));
            JTextField descriptionField = new JTextField();
            dialog.add(descriptionField);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String crime_id = crime_idField.getText();
                String type = typeField.getText();
                String description = descriptionField.getText();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Insert into Evidence(crime_id,type,description) values (?,?,?)");
                    ps.setString(1, crime_id);
                    ps.setString(2, type);
                    ps.setString(3, description);
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
                PreparedStatement ps = connection.prepareStatement("Delete from Evidence where evidence_id=?");
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
            String currentcrime_id = table.getValueAt(row, 1).toString();
            String currenttype = table.getValueAt(row, 2).toString();
            String currentdescription = table.getValueAt(row, 3).toString();
            String currentId = table.getValueAt(row, 0).toString();
            JDialog dialog = new JDialog();
            dialog.setSize(400, 300);
            dialog.setTitle("Update Evidence");
            dialog.setLayout(new GridLayout(4, 2));
            dialog.add(new JLabel("Crime_id"));
            JTextField crime_idField = new JTextField();
            dialog.add(crime_idField);
            crime_idField.setText(currentcrime_id);
            dialog.add(new JLabel("type"));
            JTextField typeField = new JTextField();
            dialog.add(typeField);
            typeField.setText(currenttype);
            dialog.add(new JLabel("Description"));
            JTextField descriptionField = new JTextField();
            dialog.add(descriptionField);
            descriptionField.setText(currentdescription);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String crime_id = crime_idField.getText();
                String type = typeField.getText();
                String description = descriptionField.getText();
                String id = table.getValueAt(row, 0).toString();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Update Evidence set crime_id=?,type=?,description=? where evidence_id=?");
                    ps.setString(1, crime_id);
                    ps.setString(2, type);
                    ps.setString(3, description);
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
            dialog.setTitle("Search Evidence");
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(4, 2));
            dialog.add(new JLabel("crime_id"));
            JTextField crime_idField = new JTextField();
            dialog.add(crime_idField);
            dialog.add(new JLabel("type"));
            JTextField typeField = new JTextField();
            dialog.add(typeField);
            dialog.add(new JLabel("Description"));
            JTextField descriptionField = new JTextField();
            dialog.add(descriptionField);
            JButton btnSearch = new JButton("Search");
            dialog.add(btnSearch);
            btnSearch.addActionListener(e1 -> {
                try {
                    String query = "Select * from Evidence where 1=1";
                    if (!crime_idField.getText().isEmpty()) {
                        query += " AND crime_id LIKE '%" + crime_idField.getText() + "%'";
                    }
                    if (!typeField.getText().isEmpty()) {
                        query += " AND type like '%" + typeField.getText() + "%'";
                    }
                    if (!descriptionField.getText().isEmpty()) {
                        query += " and description =" + descriptionField.getText();
                    }
                    Connection connection = DBConnection.getConnection();
                    Statement stmt = connection.createStatement();
                    ResultSet resultSet = stmt.executeQuery(query);
                    String[] columns = {"evidence_id", "crime_id", "type", "description"};
                    DefaultTableModel model = new DefaultTableModel(columns, 0);
                    while (resultSet.next()) {
                        Object[] row = {
                                resultSet.getString("evidence_id"),
                                resultSet.getString("crime_id"),
                                resultSet.getString("type"),
                                resultSet.getString("description")
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
        ResultSet resultSet = stmt.executeQuery("Select * from Evidence");
        String[] columns = {"evidence_id", "crime_id", "type", "Description"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        while (resultSet.next()) {
            Object[] row = {
                    resultSet.getString("evidence_id"),
                    resultSet.getString("crime_id"),
                    resultSet.getString("type"),
                    resultSet.getString("description")
            };
            model.addRow(row);
        }
        table.setModel(model);
        connection.close();
    }

    public static void main(String[] args) {
        new EvidenceUI();
    }
}