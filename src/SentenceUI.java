import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SentenceUI extends JFrame {
    JTable table = new JTable();

    public SentenceUI() {
        setTitle("Sentence Module");
        setSize(800, 1000);
        setLayout(new BorderLayout());
        JLabel header = new JLabel("Sentence Module", JLabel.CENTER);
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(30, 30, 60));
        header.setOpaque(true);
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton addBtn = new JButton("Add");
        btnPanel.add(addBtn);
        addBtn.addActionListener(e -> {
            JDialog dialog = new JDialog(SentenceUI.this, "Add Sentence", true);
            setTitle("Add Sentence");
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(4, 2));
            dialog.add(new JLabel("judgement_id"));
            JTextField judgement_idField = new JTextField();
            dialog.add(judgement_idField);
            dialog.add(new JLabel("punishment"));
            JTextField punishmentField = new JTextField();
            dialog.add(punishmentField);
            dialog.add(new JLabel("duration"));
            JTextField durationField = new JTextField();
            dialog.add(durationField);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String judgement_id = judgement_idField.getText();
                String punishment = punishmentField.getText();
                String duration = durationField.getText();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Insert into Sentence(judgement_id,punishment,duration) values (?,?,?)");
                    ps.setString(1, judgement_id);
                    ps.setString(2, punishment);
                    ps.setString(3, duration);
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
                PreparedStatement ps = connection.prepareStatement("Delete from Sentence where sentence_id=?");
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
            String currentjudgement_id = table.getValueAt(row, 1).toString();
            String currentpunishment = table.getValueAt(row, 2).toString();
            String currentduration = table.getValueAt(row, 3).toString();
            String currentId = table.getValueAt(row, 0).toString();
            JDialog dialog = new JDialog();
            dialog.setSize(400, 300);
            dialog.setTitle("Update Sentence");
            dialog.setLayout(new GridLayout(4, 2));
            dialog.add(new JLabel("judgement_id"));
            JTextField judgement_idField = new JTextField();
            dialog.add(judgement_idField);
            judgement_idField.setText(currentjudgement_id);
            dialog.add(new JLabel("punishment"));
            JTextField punishmentField = new JTextField();
            dialog.add(punishmentField);
            punishmentField.setText(currentpunishment);
            dialog.add(new JLabel("duration"));
            JTextField durationField = new JTextField();
            dialog.add(durationField);
            durationField.setText(currentduration);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String judgement_id = judgement_idField.getText();
                String punishment = punishmentField.getText();
                String duration = durationField.getText();
                String id = table.getValueAt(row, 0).toString();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Update Sentence set judgement_id=?,punishment=?,duration=? where sentence_id=?");
                    ps.setString(1, judgement_id);
                    ps.setString(2, punishment);
                    ps.setString(3, duration);
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
            dialog.setTitle("Search Sentence");
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(4, 2));
            dialog.add(new JLabel("judgement_id"));
            JTextField judgement_idField = new JTextField();
            dialog.add(judgement_idField);
            dialog.add(new JLabel("punishment"));
            JTextField punishmentField = new JTextField();
            dialog.add(punishmentField);
            dialog.add(new JLabel("duration"));
            JTextField durationField = new JTextField();
            dialog.add(durationField);
            JButton btnSearch = new JButton("Search");
            dialog.add(btnSearch);
            btnSearch.addActionListener(e1 -> {
                try {
                    String query = "Select * from Sentence where 1=1";
                    if (!judgement_idField.getText().isEmpty()) {
                        query += " AND judgement_id =" + judgement_idField.getText();
                    }
                    if (!punishmentField.getText().isEmpty()) {
                        query += " AND punishment like '%" + punishmentField.getText() + "%'";
                    }
                    if (!durationField.getText().isEmpty()) {
                        query += " and duration like '%" + durationField.getText() + "%'";
                    }
                    Connection connection = DBConnection.getConnection();
                    Statement stmt = connection.createStatement();
                    ResultSet resultSet = stmt.executeQuery(query);
                    String[] columns = {"sentence_id", "judgement_id", "punishment", "duration"};
                    DefaultTableModel model = new DefaultTableModel(columns, 0);
                    while (resultSet.next()) {
                        Object[] row = {
                                resultSet.getString("sentence_id"),
                                resultSet.getString("judgement_id"),
                                resultSet.getString("punishment"),
                                resultSet.getString("duration")
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
        ResultSet resultSet = stmt.executeQuery("Select * from Sentence");
        String[] columns = {"sentence_id", "judgement_id", "punishment", "duration"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        while (resultSet.next()) {
            Object[] row = {
                    resultSet.getString("sentence_id"),
                    resultSet.getString("judgement_id"),
                    resultSet.getString("punishment"),
                    resultSet.getString("duration")
            };
            model.addRow(row);
        }
        table.setModel(model);
        connection.close();
    }

    public static void main(String[] args) {
        new SentenceUI();
    }
}

