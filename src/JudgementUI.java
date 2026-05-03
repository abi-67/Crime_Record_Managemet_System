import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class JudgementUI extends JFrame {
    JTable table = new JTable();

    public JudgementUI() {
        setTitle("Judgement Module");
        setSize(800, 1000);
        setLayout(new BorderLayout());
        JLabel header = new JLabel("Judgement Module", JLabel.CENTER);
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(30, 30, 60));
        header.setOpaque(true);
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton addBtn = new JButton("Add");
        btnPanel.add(addBtn);
        addBtn.addActionListener(e -> {
            JDialog dialog = new JDialog(JudgementUI.this, "Add Judgement", true);
            setTitle("Add Judgement");
            dialog.setSize(400, 600);
            dialog.setLayout(new GridLayout(5, 2));
            dialog.add(new JLabel("crime_id"));
            JTextField crime_idField = new JTextField();
            dialog.add(crime_idField);
            dialog.add(new JLabel("court_id"));
            JTextField court_idField = new JTextField();
            dialog.add(court_idField);
            dialog.add(new JLabel("verdict"));
            JTextField verdictField = new JTextField();
            dialog.add(verdictField);
            dialog.add(new JLabel("judgement_date"));
            JTextField judgement_dateField = new JTextField();
            dialog.add(judgement_dateField);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String crime_id = crime_idField.getText();
                String court_id = court_idField.getText();
                String verdict = verdictField.getText();
                String judgement_date = judgement_dateField.getText();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Insert into Judgement(crime_id,court_id,verdict,judgement_date) values (?,?,?,?)");
                    ps.setString(1, crime_id);
                    ps.setString(2, court_id);
                    ps.setString(3, verdict);
                    ps.setString(4, judgement_date);
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
                PreparedStatement ps = connection.prepareStatement("Delete from Judgement where judgement_id=?");
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
            String currentcourt_id = table.getValueAt(row, 2).toString();
            String currentverdict = table.getValueAt(row, 3).toString();
            String currentjudgement_date = table.getValueAt(row, 4).toString();
            String currentId = table.getValueAt(row, 0).toString();
            JDialog dialog = new JDialog();
            dialog.setSize(400, 300);
            dialog.setTitle("Update Judgement");
            dialog.setLayout(new GridLayout(5, 2));
            dialog.add(new JLabel("crime_id"));
            JTextField crime_idField = new JTextField();
            dialog.add(crime_idField);
            crime_idField.setText(currentcrime_id);
            dialog.add(new JLabel("court_id"));
            JTextField court_idField = new JTextField();
            dialog.add(court_idField);
            court_idField.setText(currentcourt_id);
            dialog.add(new JLabel("verdict"));
            JTextField verdictField = new JTextField();
            dialog.add(verdictField);
            verdictField.setText(currentverdict);
            dialog.add(new JLabel("judgement_date"));
            JTextField judgement_dateField = new JTextField();
            dialog.add(judgement_dateField);
            judgement_dateField.setText(currentjudgement_date);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String crime_id = crime_idField.getText();
                String court_id = court_idField.getText();
                String verdict = verdictField.getText();
                String judgement_date = judgement_dateField.getText();
                String id = table.getValueAt(row, 0).toString();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Update Judgement set crime_id=?,court_id=?,verdict=?,judgement_date=?,judgement_date=? where judgement_id=?");
                    ps.setString(1, crime_id);
                    ps.setString(2, court_id);
                    ps.setString(3, verdict);
                    ps.setString(4, judgement_date);
                    ps.setString(5, judgement_date);
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
            dialog.setTitle("Search Judgement");
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(6, 2));
            dialog.add(new JLabel("crime_id"));
            JTextField crime_idField = new JTextField();
            dialog.add(crime_idField);
            dialog.add(new JLabel("court_id"));
            JTextField court_idField = new JTextField();
            dialog.add(court_idField);
            dialog.add(new JLabel("verdict"));
            JTextField verdictField = new JTextField();
            dialog.add(verdictField);
            dialog.add(new JLabel("judgement_date"));
            JTextField judgement_dateField = new JTextField();
            dialog.add(judgement_dateField);
            JButton btnSearch = new JButton("Search");
            dialog.add(btnSearch);
            btnSearch.addActionListener(e1 -> {
                try {
                    String query = "Select * from Judgement where 1=1";
                    if (!crime_idField.getText().isEmpty()) {
                        query += " AND crime_id =" + crime_idField.getText();
                    }
                    if (!court_idField.getText().isEmpty()) {
                        query += " AND court_id=" + court_idField.getText();
                    }
                    if (!verdictField.getText().isEmpty()) {
                        query += " and verdict like '%" + verdictField.getText() + "%'";
                    }
                    if (!judgement_dateField.getText().isEmpty()) {
                        query += " and judgement_date ='" + judgement_dateField.getText() + "'";
                    }
                    Connection connection = DBConnection.getConnection();
                    Statement stmt = connection.createStatement();
                    ResultSet resultSet = stmt.executeQuery(query);
                    String[] columns = {"judgement_id", "crime_id", "court_id", "verdict", "judgement_date"};
                    DefaultTableModel model = new DefaultTableModel(columns, 0);
                    while (resultSet.next()) {
                        Object[] row = {
                                resultSet.getString("judgement_id"),
                                resultSet.getString("crime_id"),
                                resultSet.getString("court_id"),
                                resultSet.getString("verdict"),
                                resultSet.getString("judgement_date"),
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
        ResultSet resultSet = stmt.executeQuery("Select * from Judgement");
        String[] columns = {"judgement_id", "crime_id", "court_id", "verdict", "judgement_date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        while (resultSet.next()) {
            Object[] row = {
                    resultSet.getString("judgement_id"),
                    resultSet.getString("crime_id"),
                    resultSet.getString("court_id"),
                    resultSet.getString("verdict"),
                    resultSet.getString("judgement_date")
            };
            model.addRow(row);
        }
        table.setModel(model);
        connection.close();
    }

    public static void main(String[] args) {
        new JudgementUI();
    }
}