import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class OfficersUI extends JFrame {
    JTable table = new JTable();

    public OfficersUI() {
        setTitle("Officers Module");
        setSize(800, 1000);
        setLayout(new BorderLayout());
        JLabel header = new JLabel("Officers Module", JLabel.CENTER);
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(30, 30, 40));
        header.setOpaque(true);
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton addBtn = new JButton("Add");
        btnPanel.add(addBtn);
        addBtn.addActionListener(e -> {
            JDialog dialog = new JDialog(OfficersUI.this, "Add Officers", true);
            setTitle("Add Officers");
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(4, 2));
            dialog.add(new JLabel("Name"));
            JTextField nameField = new JTextField();
            dialog.add(nameField);
            dialog.add(new JLabel("officer_rank"));
            JTextField officer_rankField = new JTextField();
            dialog.add(officer_rankField);
            dialog.add(new JLabel("Station Id"));
            JTextField station_idField = new JTextField();
            dialog.add(station_idField);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String name = nameField.getText();
                String officer_rank = officer_rankField.getText();
                String station_id = station_idField.getText();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Insert into Officers(name,officer_rank,station_id) values (?,?,?)");
                    ps.setString(1, name);
                    ps.setString(2, officer_rank);
                    ps.setString(3, station_id);
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
                PreparedStatement ps = connection.prepareStatement("Delete from Officers where officer_id=?");
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
            String currentofficer_rank = table.getValueAt(row, 2).toString();
            String currentstation_id = table.getValueAt(row, 3).toString();
            String currentId = table.getValueAt(row, 0).toString();
            JDialog dialog = new JDialog();
            dialog.setSize(400, 300);
            dialog.setTitle("Update Officers");
            dialog.setLayout(new GridLayout(4, 2));
            dialog.add(new JLabel("Name"));
            JTextField nameField = new JTextField();
            dialog.add(nameField);
            nameField.setText(currentname);
            dialog.add(new JLabel("officer_rank"));
            JTextField officer_rankField = new JTextField();
            dialog.add(officer_rankField);
            officer_rankField.setText(currentofficer_rank);
            dialog.add(new JLabel("Station Id"));
            JTextField station_idField = new JTextField();
            dialog.add(station_idField);
            station_idField.setText(currentstation_id);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String name = nameField.getText();
                String officer_rank = officer_rankField.getText();
                String station_id = station_idField.getText();
                String id = table.getValueAt(row, 0).toString();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Update Officers set name=?,officer_rank=?,station_id=? where officer_id=?");
                    ps.setString(1, name);
                    ps.setString(2, officer_rank);
                    ps.setString(3, station_id);
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
            dialog.setTitle("Search Officers");
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(4, 2));
            dialog.add(new JLabel("Name"));
            JTextField nameField = new JTextField();
            dialog.add(nameField);
            dialog.add(new JLabel("officer_rank"));
            JTextField officer_rankField = new JTextField();
            dialog.add(officer_rankField);
            dialog.add(new JLabel("Station Id"));
            JTextField station_idField = new JTextField();
            dialog.add(station_idField);
            JButton btnSearch = new JButton("Search");
            dialog.add(btnSearch);
            btnSearch.addActionListener(e1 -> {
                try {
                    String query = "Select * from Officers where 1=1";
                    if (!nameField.getText().isEmpty()) {
                        query += " AND name LIKE '%" + nameField.getText() + "%'";
                    }
                    if (!officer_rankField.getText().isEmpty()) {
                        query += " AND officer_rank like '%" + officer_rankField.getText() + "%'";
                    }
                    if (!station_idField.getText().isEmpty()) {
                        query += " and station_id =" + station_idField.getText();
                    }
                    Connection connection = DBConnection.getConnection();
                    Statement stmt = connection.createStatement();
                    ResultSet resultSet = stmt.executeQuery(query);
                    String[] columns = {"officer_id", "name", "officer_rank", "station_id"};
                    DefaultTableModel model = new DefaultTableModel(columns, 0);
                    while (resultSet.next()) {
                        Object[] row = {
                                resultSet.getString("officer_id"),
                                resultSet.getString("name"),
                                resultSet.getString("officer_rank"),
                                resultSet.getString("station_id")
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
        ResultSet resultSet = stmt.executeQuery("Select * from Officers");
        String[] columns = {"officer_id", "Name", "officer_rank", "Station Id"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        while (resultSet.next()) {
            Object[] row = {
                    resultSet.getString("officer_id"),
                    resultSet.getString("name"),
                    resultSet.getString("officer_rank"),
                    resultSet.getString("station_id")
            };
            model.addRow(row);
        }
        table.setModel(model);
        connection.close();
    }

    public static void main(String[] args) {
        new OfficersUI();
    }
}