import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Crime Record Management System");
        setSize(800, 1000);
        JLabel header = new JLabel("Crime Record Management System", JLabel.CENTER);
        header.setOpaque(true);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setBackground(new Color(30, 30, 60));
        header.setForeground(Color.WHITE);
        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new GridLayout(4, 5, 10, 10));
        JButton Criminals = new JButton("Criminals");
        panel.add(Criminals);
        Criminals.addActionListener(e -> {
            new CriminalUI();
        });
        JButton Crime = new JButton("Crime");
        panel.add(Crime);
        Crime.addActionListener(e -> {
            new CrimeUI();
        });
        JButton PoliceStation = new JButton("Police Station");
        panel.add(PoliceStation);
        PoliceStation.addActionListener((e -> {
            new PoliceStationUI();
        }));
        JButton Officers = new JButton("Officers");
        panel.add(Officers);
        Officers.addActionListener(e -> {
            new OfficersUI();
        });
        JButton Victim = new JButton("Victim");
        panel.add(Victim);
        Victim.addActionListener(e -> {
            new VictimUI();
        });
        JButton crimeCriminals = new JButton("Crime Criminals");
        panel.add(crimeCriminals);
        crimeCriminals.addActionListener(e -> {
            new CrimeCriminalUI();
        });
        JButton crimeVictim = new JButton("CrimeVictim");
        panel.add(crimeVictim);
        crimeVictim.addActionListener(e -> {
            new CrimeVictimUI();
        });
        JButton crimeOfficers = new JButton("CrimeOfficers");
        panel.add(crimeOfficers);
        crimeOfficers.addActionListener(e -> {
            new CrimeOfficersUI();
        });
        JButton Witness = new JButton("Witness");
        panel.add(Witness);
        Witness.addActionListener(e -> {
            new WitnessUI();
        });
        JButton Evidence = new JButton("Evidence");
        panel.add(Evidence);
        Evidence.addActionListener(e -> {
            new EvidenceUI();
        });
        JButton forensicReport = new JButton("Forensic Report");
        panel.add(forensicReport);
        forensicReport.addActionListener(e -> {
            new ForensicReportUI();
        });
        JButton weaponRegistry = new JButton("Weapon Registry");
        panel.add(weaponRegistry);
        weaponRegistry.addActionListener(e -> {
            new WeaponRegistryUI();
        });
        JButton Court = new JButton("Court");
        panel.add(Court);
        Court.addActionListener(e -> {
            new CourtUI();
        });
        JButton Judgment = new JButton("Judgment");
        panel.add(Judgment);
        Judgment.addActionListener(e -> {
            new JudgementUI();
        });
        JButton Sentence = new JButton("Sentence");
        panel.add(Sentence);
        Sentence.addActionListener(e -> {
            new SentenceUI();
        });
        JButton Prison = new JButton("Prison");
        panel.add(Prison);
        Prison.addActionListener(e -> {
            new PrisonUI();
        });
        JButton Inmates = new JButton("Inmates");
        panel.add(Inmates);
        Inmates.addActionListener(e -> {
            new InmatesUI();
        });
        add(header, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}