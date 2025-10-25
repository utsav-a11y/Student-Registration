import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentRegistrationForm extends JFrame {

    private final JTextField nameField;
    private final JTextField regNoField;
    private final JComboBox<String> branchComboBox;
    private final JComboBox<Integer> yearComboBox;
    private final JTextField phoneField;
    private final JTextField emailField;
    private final JRadioButton maleRadioButton;
    private final JRadioButton femaleRadioButton;
    private final ButtonGroup genderGroup;
    private final JButton registerButton;
    private final JButton clearButton;

    // --- MySQL Database Configuration ---
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "studentdb";
    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
    
    // Static variables to hold the credentials entered by the user
    private static String dbUser;
    private static String dbPass;

    public StudentRegistrationForm() {
        setTitle("Student Registration Form");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        nameField = new JTextField(20);
        add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Registration No:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        regNoField = new JTextField(20);
        add(regNoField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Branch:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        String[] branches = {"CSE (AI & ML)", "CSE (Core)", "ECE", "Mechanical", "Civil"};
        branchComboBox = new JComboBox<>(branches);
        add(branchComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Year:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        Integer[] years = {1, 2, 3, 4};
        yearComboBox = new JComboBox<>(years);
        add(yearComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        maleRadioButton = new JRadioButton("Male");
        femaleRadioButton = new JRadioButton("Female");
        maleRadioButton.setSelected(true);
        genderGroup = new ButtonGroup();
        genderGroup.add(maleRadioButton);
        genderGroup.add(femaleRadioButton);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        genderPanel.add(maleRadioButton);
        genderPanel.add(femaleRadioButton);
        add(genderPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Phone No:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        phoneField = new JTextField(20);
        add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        add(new JLabel("Email ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6;
        emailField = new JTextField(20);
        add(emailField, gbc);

        registerButton = new JButton("Register");
        clearButton = new JButton("Clear");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(registerButton);
        buttonPanel.add(clearButton);
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        registerButton.addActionListener(e -> registerStudent());
        clearButton.addActionListener(e -> clearForm());
    }

    private void clearForm() {
        nameField.setText("");
        regNoField.setText("");
        branchComboBox.setSelectedIndex(0);
        yearComboBox.setSelectedIndex(0);
        maleRadioButton.setSelected(true);
        phoneField.setText("");
        emailField.setText("");
    }

    private void registerStudent() {
        String name = nameField.getText();
        String regNo = regNoField.getText();
        String branch = (String) branchComboBox.getSelectedItem();
        Integer year = (Integer) yearComboBox.getSelectedItem();
        String gender = maleRadioButton.isSelected() ? "Male" : "Female";
        String phone = phoneField.getText();
        String email = emailField.getText();

        if (name.isEmpty() || regNo.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid 10-digit phone number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!email.matches("^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO students(name, reg_no, branch, year, gender, phone, email) VALUES(?,?,?,?,?,?,?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, dbUser, dbPass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, regNo);
            pstmt.setString(3, branch);
            pstmt.setInt(4, year);
            pstmt.setString(5, gender);
            pstmt.setString(6, phone);
            pstmt.setString(7, email);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Student registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();

        } catch (SQLException ex) {
            if (ex.getSQLState().equals("23000")) { 
                 JOptionPane.showMessageDialog(this, "Registration number already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                 JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static boolean setupDatabase() {
        String serverUrl = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT;
        try (Connection conn = DriverManager.getConnection(serverUrl, dbUser, dbPass);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        } catch (SQLException e) {
            // Handle authentication failure
            if (e.getSQLState().equals("28000")) {
                JOptionPane.showMessageDialog(null, "Authentication Failed. Please check your username and password.", "Database Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Could not connect to MySQL server: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }

        String createTableSql = "CREATE TABLE IF NOT EXISTS students (\n"
                + " id INT AUTO_INCREMENT PRIMARY KEY,\n"
                + " name VARCHAR(255) NOT NULL,\n"
                + " reg_no VARCHAR(255) NOT NULL UNIQUE,\n"
                + " branch VARCHAR(255) NOT NULL,\n"
                + " year INT NOT NULL,\n"
                + " gender VARCHAR(10) NOT NULL,\n"
                + " phone VARCHAR(15) NOT NULL,\n"
                + " email VARCHAR(255) NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(DB_URL, dbUser, dbPass);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSql);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Could not create table: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        // --- Create a panel for the credential input dialog ---
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        panel.add(new JLabel("MySQL Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("MySQL Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Database Credentials", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            dbUser = usernameField.getText();
            dbPass = new String(passwordField.getPassword());
        } else {
            System.exit(0); // Exit if user cancels
        }

        // --- Initialize database and table, then launch the GUI ---
        if (setupDatabase()) {
            SwingUtilities.invokeLater(() -> {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new StudentRegistrationForm().setVisible(true);
            });
        }
    }
}

