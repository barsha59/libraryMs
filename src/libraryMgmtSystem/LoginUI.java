package libraryMgmtSystem;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import LibraryManagementSystem.DashBoard;

public class LoginUI {
JTextField userNameField;
JTextField passwordField;


public Connection getConnection() {
    Connection conn = null;
    try {
        String url = "jdbc:mysql://localhost:3306/LibraryManagementSystem"; // Update with your database URL
        String user = "root"; 
        String password = "root"; 
        conn = DriverManager.getConnection(url, user, password);
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Failed to connect to the database", "Error", JOptionPane.ERROR_MESSAGE);
    }
    return conn;
}

public LoginUI() {
    JFrame jf = new JFrame("Login Module");
    JPanel jp = new JPanel();
    GridLayout gl = new GridLayout(4, 2, 10, 10);
    FlowLayout fl = new FlowLayout();
    jf.setLayout(fl);
    jp.setLayout(gl);

    // Creating components
    JLabel jl = new JLabel("User Name: ");
    JLabel jl3 = new JLabel("Password: ");
    userNameField = new JTextField(10);
    passwordField = new JPasswordField(10);

    JComboBox<String> combo = new JComboBox<>();
    combo.addItem("BBA");
    combo.addItem("BBABI");
    combo.addItem("BBATT");
    combo.addItem("BCIS");

    JButton signBtn = new JButton("Sign In");
    JButton registerBtn = new JButton("Register");

    jl.setSize(190, 30); // User name
    jl.setFont(new Font("Times New Roman", Font.PLAIN, 16));
    userNameField.setSize(150, 30);
    jl3.setSize(190, 30); // Password
    passwordField.setSize(150, 30);
    signBtn.setSize(170, 30);
    registerBtn.setSize(170, 30);

    // Adding components to the container
    jp.add(jl); 
    jp.add(userNameField); 
    jp.add(jl3);
    jp.add(passwordField); 
    jp.add(signBtn); 
    jp.add(registerBtn);

    signBtn.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userName = userNameField.getText();
            String password = passwordField.getText();

            Connection conn = getConnection();
            if (conn != null) {
                try {
                    String query = "SELECT * FROM users WHERE username = ? AND password = ?";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, userName);
                    stmt.setString(2, password);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        DashBoard db = new DashBoard();
                        jf.dispose();
                        JOptionPane.showMessageDialog(null, "Username and password matched", "Status", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Username and password didn't match", "Status", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    });

    registerBtn.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userName = userNameField.getText();
            String password = passwordField.getText();

            Connection conn = getConnection();
            if (conn != null) {
                try {
                    String query = "INSERT INTO users (username, password) VALUES (?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, userName);
                    stmt.setString(2, password);
                    int rowsInserted = stmt.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(null, "User registered successfully", "Status", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    });

    jf.setContentPane(jp);
    jf.setSize(400, 160); 
    jf.setVisible(true); 
}
public static void main(String[] args) {
    new LoginUI();
}
}