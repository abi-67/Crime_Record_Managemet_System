import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class WeaponRegistryUI extends JFrame {
    JTable table = new JTable();

    public WeaponRegistryUI() {
        setTitle("Weapon_Registry Module");
        setSize(800, 1000);
        setLayout(new BorderLayout());
        JLabel header = new JLabel("Weapon_Registry Module", JLabel.CENTER);
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(30, 30, 60));
        header.setOpaque(true);
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton addBtn = new JButton("Add");
        btnPanel.add(addBtn);
        addBtn.addActionListener(e -> {
            JDialog dialog = new JDialog(WeaponRegistryUI.this, "Add Weapon_Registry", true);
            setTitle("Add Weapon_Registry");
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(4, 2));
            dialog.add(new JLabel("type"));
            JTextField typeField = new JTextField();
            dialog.add(typeField);
            dialog.add(new JLabel("owner"));
            JTextField ownerField = new JTextField();
            dialog.add(ownerField);
            dialog.add(new JLabel("license_number"));
            JTextField license_numberField = new JTextField();
            dialog.add(license_numberField);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String type = typeField.getText();
                String owner = ownerField.getText();
                String license_number = license_numberField.getText();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Insert into Weapon_Registry(type,owner,license_number) values (?,?,?)");
                    ps.setString(1, type);
                    ps.setString(2, owner);
                    ps.setString(3, license_number);
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
                PreparedStatement ps = connection.prepareStatement("Delete from Weapon_Registry where weapon_id=?");
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
            String currenttype = table.getValueAt(row, 1).toString();
            String currentowner = table.getValueAt(row, 2).toString();
            String currentlicense_number = table.getValueAt(row, 3).toString();
            String currentId = table.getValueAt(row, 0).toString();
            JDialog dialog = new JDialog();
            dialog.setSize(400, 300);
            dialog.setTitle("Update Weapon_Registry");
            dialog.setLayout(new GridLayout(4, 2));
            dialog.add(new JLabel("type"));
            JTextField typeField = new JTextField();
            dialog.add(typeField);
            typeField.setText(currenttype);
            dialog.add(new JLabel("owner"));
            JTextField ownerField = new JTextField();
            dialog.add(ownerField);
            ownerField.setText(currentowner);
            dialog.add(new JLabel("license_number"));
            JTextField license_numberField = new JTextField();
            dialog.add(license_numberField);
            license_numberField.setText(currentlicense_number);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String type = typeField.getText();
                String owner = ownerField.getText();
                String license_number = license_numberField.getText();
                String id = table.getValueAt(row, 0).toString();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Update Weapon_Registry set type=?,owner=?,license_number=? where weapon_id=?");
                    ps.setString(1, type);
                    ps.setString(2, owner);
                    ps.setString(3, license_number);
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
            dialog.setTitle("Search Weapon_Registry");
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(4, 2));
            dialog.add(new JLabel("type"));
            JTextField typeField = new JTextField();
            dialog.add(typeField);
            dialog.add(new JLabel("owner"));
            JTextField ownerField = new JTextField();
            dialog.add(ownerField);
            dialog.add(new JLabel("license_number"));
            JTextField license_numberField = new JTextField();
            dialog.add(license_numberField);
            JButton btnSearch = new JButton("Search");
            dialog.add(btnSearch);
            btnSearch.addActionListener(e1 -> {
                try {
                    String query = "Select * from Weapon_Registry where 1=1";
                    if (!typeField.getText().isEmpty()) {
                        query += " AND type LIKE '%" + typeField.getText() + "%'";
                    }
                    if (!ownerField.getText().isEmpty()) {
                        query += " AND owner like '%" + ownerField.getText() + "%'";
                    }
                    if (!license_numberField.getText().isEmpty()) {
                        query += " and license_number =" + license_numberField.getText();
                    }
                    Connection connection = DBConnection.getConnection();
                    Statement stmt = connection.createStatement();
                    ResultSet resultSet = stmt.executeQuery(query);
                    String[] columns = {"weapon_id", "type", "owner", "license_number"};
                    DefaultTableModel model = new DefaultTableModel(columns, 0);
                    while (resultSet.next()) {
                        Object[] row = {
                                resultSet.getString("weapon_id"),
                                resultSet.getString("type"),
                                resultSet.getString("owner"),
                                resultSet.getString("license_number")
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
        ResultSet resultSet = stmt.executeQuery("Select * from Weapon_Registry");
        String[] columns = {"weapon_id", "type", "owner", "license_number"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        while (resultSet.next()) {
            Object[] row = {
                    resultSet.getString("weapon_id"),
                    resultSet.getString("type"),
                    resultSet.getString("owner"),
                    resultSet.getString("license_number")
            };
            model.addRow(row);
        }
        table.setModel(model);
        connection.close();
    }

    public static void main(String[] args) {
        new WeaponRegistryUI();
    }
}