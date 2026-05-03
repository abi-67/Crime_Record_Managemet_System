import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ForensicReportUI extends JFrame {
    JTable table = new JTable();

    public ForensicReportUI() {
        setTitle("Forensic_Report Module");
        setSize(800, 1000);
        setLayout(new BorderLayout());
        JLabel header = new JLabel("Forensic_Report Module", JLabel.CENTER);
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(30, 30, 60));
        header.setOpaque(true);
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton addBtn = new JButton("Add");
        btnPanel.add(addBtn);
        addBtn.addActionListener(e -> {
            JDialog dialog = new JDialog(ForensicReportUI.this, "Add Forensic_Report", true);
            setTitle("Add Forensic_Report");
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(4, 2));
            dialog.add(new JLabel("crime_id"));
            JTextField crime_idField = new JTextField();
            dialog.add(crime_idField);
            dialog.add(new JLabel("details"));
            JTextField detailsField = new JTextField();
            dialog.add(detailsField);
            dialog.add(new JLabel("result"));
            JTextField resultField = new JTextField();
            dialog.add(resultField);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String crime_id = crime_idField.getText();
                String details = detailsField.getText();
                String result = resultField.getText();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Insert into Forensic_Report(crime_id,details,result) values (?,?,?)");
                    ps.setString(1, crime_id);
                    ps.setString(2, details);
                    ps.setString(3, result);
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
                PreparedStatement ps = connection.prepareStatement("Delete from Forensic_Report where report_id=?");
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
            String currentdetails = table.getValueAt(row, 2).toString();
            String currentresult = table.getValueAt(row, 3).toString();
            String currentId = table.getValueAt(row, 0).toString();
            JDialog dialog = new JDialog();
            dialog.setSize(400, 300);
            dialog.setTitle("Update Forensic_Report");
            dialog.setLayout(new GridLayout(4, 2));
            dialog.add(new JLabel("Crime_id"));
            JTextField crime_idField = new JTextField();
            dialog.add(crime_idField);
            crime_idField.setText(currentcrime_id);
            dialog.add(new JLabel("details"));
            JTextField detailsField = new JTextField();
            dialog.add(detailsField);
            detailsField.setText(currentdetails);
            dialog.add(new JLabel("result"));
            JTextField resultField = new JTextField();
            dialog.add(resultField);
            resultField.setText(currentresult);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String crime_id = crime_idField.getText();
                String details = detailsField.getText();
                String result = resultField.getText();
                String id = table.getValueAt(row, 0).toString();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Update Forensic_Report set crime_id=?,details=?,result=? where report_id=?");
                    ps.setString(1, crime_id);
                    ps.setString(2, details);
                    ps.setString(3, result);
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
            dialog.setTitle("Search Forensic_Report");
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(4, 2));
            dialog.add(new JLabel("crime_id"));
            JTextField crime_idField = new JTextField();
            dialog.add(crime_idField);
            dialog.add(new JLabel("details"));
            JTextField detailsField = new JTextField();
            dialog.add(detailsField);
            dialog.add(new JLabel("result"));
            JTextField resultField = new JTextField();
            dialog.add(resultField);
            JButton btnSearch = new JButton("Search");
            dialog.add(btnSearch);
            btnSearch.addActionListener(e1 -> {
                try {
                    String query = "Select * from Forensic_Report where 1=1";
                    if (!crime_idField.getText().isEmpty()) {
                        query += " AND crime_id =" + crime_idField.getText();
                    }
                    if (!detailsField.getText().isEmpty()) {
                        query += " AND details like '%" + detailsField.getText() + "%'";
                    }
                    if (!resultField.getText().isEmpty()) {
                        query += " and result like '%" + resultField.getText() + "%'";
                    }
                    Connection connection = DBConnection.getConnection();
                    Statement stmt = connection.createStatement();
                    ResultSet resultSet = stmt.executeQuery(query);
                    String[] columns = {"report_id", "crime_id", "details", "result"};
                    DefaultTableModel model = new DefaultTableModel(columns, 0);
                    while (resultSet.next()) {
                        Object[] row = {
                                resultSet.getString("report_id"),
                                resultSet.getString("crime_id"),
                                resultSet.getString("details"),
                                resultSet.getString("result")
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
        ResultSet resultSet = stmt.executeQuery("Select * from Forensic_Report");
        String[] columns = {"report_id", "crime_id", "details", "result"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        while (resultSet.next()) {
            Object[] row = {
                    resultSet.getString("report_id"),
                    resultSet.getString("crime_id"),
                    resultSet.getString("details"),
                    resultSet.getString("result")
            };
            model.addRow(row);
        }
        table.setModel(model);
        connection.close();
    }

    public static void main(String[] args) {
        new ForensicReportUI();
    }
}
