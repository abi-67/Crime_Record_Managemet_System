import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CrimeVictimUI extends JFrame {
    JTable table = new JTable();

    public CrimeVictimUI() {
        setTitle("Crime Victim Module");
        setSize(800, 1000);
        setLayout(new BorderLayout());
        JLabel header = new JLabel("Crime Victim Module", JLabel.CENTER);
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(30, 30, 60));
        header.setOpaque(true);
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton addBtn = new JButton("Add");
        btnPanel.add(addBtn);
        addBtn.addActionListener(e -> {
            JDialog dialog = new JDialog(CrimeVictimUI.this, "Add Crime Victim", true);
            setTitle("Add Crime Victim");
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(3, 2));
            dialog.add(new JLabel("crime_id"));
            JTextField crime_idField = new JTextField();
            dialog.add(crime_idField);
            dialog.add(new JLabel("victim_id"));
            JTextField victim_idField = new JTextField();
            dialog.add(victim_idField);
            JButton submitBtn = new JButton("Submit");
            dialog.add(submitBtn);
            submitBtn.addActionListener(e1 -> {
                String crime_id = crime_idField.getText();
                String victim_id = victim_idField.getText();
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("Insert into Crime_Victim(crime_id,victim_id) values (?,?)");
                    ps.setString(1, crime_id);
                    ps.setString(2, victim_id);
                    ps.executeUpdate();
                    dialog.dispose();
                    loadData();
                    connection.close();
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
            String crimeId = table.getValueAt(row, 0).toString();
            String VictimId = table.getValueAt(row, 1).toString();
            try {
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement("Delete from Crime_Victim where crime_id=? and victim_id=?");
                ps.setString(1, crimeId);
                ps.setString(2, VictimId);
                ps.executeUpdate();
                loadData();
                connection.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        JButton searchByVictimBtn = new JButton("Search By Victim");
        btnPanel.add(searchByVictimBtn);
        searchByVictimBtn.addActionListener(e -> {
            JDialog dialog = new JDialog();
            dialog.setTitle("Search By Victim");
            dialog.setSize(300, 200);
            dialog.setLayout(new GridLayout(2, 2));
            dialog.add(new JLabel("Victim ID:"));
            JTextField victim_idField = new JTextField();
            dialog.add(victim_idField);
            JButton btnSearch = new JButton("Search");
            dialog.add(btnSearch);
            btnSearch.addActionListener(e1 -> {
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("select Victim.name,Crime.crime_type,Crime.crime_date" + " From Crime_Victim" + " Join Victim on Crime_Victim.victim_id=Victim.victim_id" + " join Crime on Crime_Victim.crime_id=Crime.crime_id" + " where Crime_Victim.victim_id=?");
                    ps.setString(1, victim_idField.getText());
                    ResultSet resultSet = ps.executeQuery();
                    String[] columns = {"Victim Name", "Crime Type", "Date"};
                    DefaultTableModel model = new DefaultTableModel(columns, 0);
                    while (resultSet.next()) {
                        Object[] row = {
                                resultSet.getString("name"),
                                resultSet.getString("crime_type"),
                                resultSet.getString("crime_date"),
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
        JButton searchByCrimeBtn = new JButton("Search By Crime");
        btnPanel.add(searchByCrimeBtn);
        searchByCrimeBtn.addActionListener(e -> {
            JDialog dialog = new JDialog();
            dialog.setTitle("Search By Crime");
            dialog.setSize(300, 200);
            dialog.setLayout(new GridLayout(2, 2));
            dialog.add(new JLabel("Crime ID:"));
            JTextField crime_idField = new JTextField();
            dialog.add(crime_idField);
            JButton btnSearch = new JButton("Search");
            dialog.add(btnSearch);
            btnSearch.addActionListener(e1 -> {
                try {
                    Connection connection = DBConnection.getConnection();
                    PreparedStatement ps = connection.prepareStatement("select Victim.name,Crime.crime_type,Crime.crime_date" + " From Crime_Victim" + " Join Victim on Crime_Victim.victim_id=Victim.victim_id " + " join Crime on Crime_Victim.crime_id=Crime.crime_id " + "where Crime_Victim.crime_id=?");
                    ps.setString(1, crime_idField.getText());
                    ResultSet resultSet = ps.executeQuery();
                    String[] columns = {"Victim Name", "Crime Type", "Date"};
                    DefaultTableModel model = new DefaultTableModel(columns, 0);
                    while (resultSet.next()) {
                        Object[] row = {
                                resultSet.getString("name"),
                                resultSet.getString("crime_type"),
                                resultSet.getString("crime_date"),
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
        ResultSet resultSet = stmt.executeQuery("Select * from Crime_Victim");
        String[] columns = {"crime_id", "victim_id"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        while (resultSet.next()) {
            Object[] row = {
                    resultSet.getString("crime_id"),
                    resultSet.getString("victim_id")
            };
            model.addRow(row);
        }
        table.setModel(model);
        connection.close();
    }

    public static void main(String[] args) {
        new CrimeVictimUI();
    }
}