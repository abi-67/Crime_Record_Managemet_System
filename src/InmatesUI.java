import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class InmatesUI extends JFrame {
    JTable table = new JTable();

    public InmatesUI() {
        setTitle("Inmates Module");
        setSize(800, 1000);
        setLayout(new BorderLayout());
        JLabel header = new JLabel("Inmates Module", JLabel.CENTER);
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(30, 30, 60));
        header.setOpaque(true);
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton addBtn = new JButton("Add");
        btnPanel.add(addBtn);
        addBtn.addActionListener(e -> {
            JDialog dialog = new JDialog(InmatesUI.this, "Add Inmates", true);
            setTitle("Add Inmates");
            dialog.setSize(400, 600);
            dialog.setLayout(new GridLayout(5, 2));
            dialog.add(new JLabel("criminal_id"));
            JTextField criminal_idField = new JTextField();
            dialog.add(criminal_idField);
            dialog.add(new JLabel("prison_id"));
            JTextField prison_idField = new JTextField();
            dialog.add(prison_idField);
            dialog.add(new JLabel("entry_date"));
            JTextField entry_dateField = new JTextField();
            dialog.add(entry_dateField);
            dialog.add(new JLabel("release_date"));
            JTextField release_dateField = new JTextField();
            dialog.add(release_dateField);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String criminal_id = criminal_idField.getText();
                String prison_id = prison_idField.getText();
                String entry_date = entry_dateField.getText();
                String release_date = release_dateField.getText();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Insert into Inmates(criminal_id,prison_id,entry_date,release_date) values (?,?,?,?)");
                    ps.setString(1, criminal_id);
                    ps.setString(2, prison_id);
                    ps.setString(3, entry_date);
                    ps.setString(4, release_date);
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
                PreparedStatement ps = connection.prepareStatement("Delete from Inmates where inmate_id=?");
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
            String currentcriminal_id = table.getValueAt(row, 1).toString();
            String currentprison_id = table.getValueAt(row, 2).toString();
            String currententry_date = table.getValueAt(row, 3).toString();
            String currentrelease_date = table.getValueAt(row, 4).toString();
            String currentId = table.getValueAt(row, 0).toString();
            JDialog dialog = new JDialog();
            dialog.setSize(400, 300);
            dialog.setTitle("Update Inmates");
            dialog.setLayout(new GridLayout(5, 2));
            dialog.add(new JLabel("criminal_id"));
            JTextField criminal_idField = new JTextField();
            dialog.add(criminal_idField);
            criminal_idField.setText(currentcriminal_id);
            dialog.add(new JLabel("prison_id"));
            JTextField prison_idField = new JTextField();
            dialog.add(prison_idField);
            prison_idField.setText(currentprison_id);
            dialog.add(new JLabel("entry_date"));
            JTextField entry_dateField = new JTextField();
            dialog.add(entry_dateField);
            entry_dateField.setText(currententry_date);
            dialog.add(new JLabel("release_date"));
            JTextField release_dateField = new JTextField();
            dialog.add(release_dateField);
            release_dateField.setText(currentrelease_date);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String criminal_id = criminal_idField.getText();
                String prison_id = prison_idField.getText();
                String entry_date = entry_dateField.getText();
                String release_date = release_dateField.getText();
                String id = table.getValueAt(row, 0).toString();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Update Inmates set criminal_id=?,prison_id=?,entry_date=?,release_date=?,release_date=? where inmate_id=?");
                    ps.setString(1, criminal_id);
                    ps.setString(2, prison_id);
                    ps.setString(3, entry_date);
                    ps.setString(4, release_date);
                    ps.setString(5, release_date);
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
            dialog.setTitle("Search Inmates");
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(6, 2));
            dialog.add(new JLabel("criminal_id"));
            JTextField criminal_idField = new JTextField();
            dialog.add(criminal_idField);
            dialog.add(new JLabel("prison_id"));
            JTextField prison_idField = new JTextField();
            dialog.add(prison_idField);
            dialog.add(new JLabel("entry_date"));
            JTextField entry_dateField = new JTextField();
            dialog.add(entry_dateField);
            dialog.add(new JLabel("release_date"));
            JTextField release_dateField = new JTextField();
            dialog.add(release_dateField);
            JButton btnSearch = new JButton("Search");
            dialog.add(btnSearch);
            btnSearch.addActionListener(e1 -> {
                try {
                    String query = "Select * from Inmates where 1=1";
                    if (!criminal_idField.getText().isEmpty()) {
                        query += " AND criminal_id =" + criminal_idField.getText();
                    }
                    if (!prison_idField.getText().isEmpty()) {
                        query += " AND prison_id=" + prison_idField.getText();
                    }
                    if (!entry_dateField.getText().isEmpty()) {
                        query += " and entry_date ='" + entry_dateField.getText() + "'";
                    }
                    if (!release_dateField.getText().isEmpty()) {
                        query += " and release_date ='" + release_dateField.getText() + "'";
                    }
                    if (!entry_dateField.getText().isEmpty() && !release_dateField.getText().isEmpty()) {
                        query += " and entry_date between '" + entry_dateField.getText() + "' and '" + release_dateField.getText() + "'";
                    }
                    Connection connection = DBConnection.getConnection();
                    Statement stmt = connection.createStatement();
                    ResultSet resultSet = stmt.executeQuery(query);
                    String[] columns = {"inmate_id", "criminal_id", "prison_id", "entry_date", "release_date"};
                    DefaultTableModel model = new DefaultTableModel(columns, 0);
                    while (resultSet.next()) {
                        Object[] row = {
                                resultSet.getString("inmate_id"),
                                resultSet.getString("criminal_id"),
                                resultSet.getString("prison_id"),
                                resultSet.getString("entry_date"),
                                resultSet.getString("release_date"),
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
        ResultSet resultSet = stmt.executeQuery("Select * from Inmates");
        String[] columns = {"inmate_id", "criminal_id", "prison_id", "entry_date", "release_date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        while (resultSet.next()) {
            Object[] row = {
                    resultSet.getString("inmate_id"),
                    resultSet.getString("criminal_id"),
                    resultSet.getString("prison_id"),
                    resultSet.getString("entry_date"),
                    resultSet.getString("release_date")
            };
            model.addRow(row);
        }
        table.setModel(model);
        connection.close();
    }

    public static void main(String[] args) {
        new InmatesUI();
    }
}