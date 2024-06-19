package LibraryManagementSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DashBoard implements ActionListener {

    private JFrame frame;
    private JTabbedPane tabbedPane;

    // Add Student tab components
    private JTextField txtAddFName;
    private JTextField txtAddLName;
    private JTextField txtAddBook;
    private JRadioButton radioAddMale;
    private JRadioButton radioAddFemale;
    private JComboBox<String> comboAddProgram;
    private JComboBox<String> comboAddSection;

    // Add Book tab components
    private JTextField txtAddBookTitle;
    private JTextField txtAddAuthor;
    private JTextField txtAddPublication;
    private JTextField txtAddSubject;

    // Issue Book tab components
    private JTextField txtIssueStudentName;
    private JTextField txtIssueBookIssued;
    private JTextField txtIssueIssueDate;
    private JTextField txtIssueDueDate;

    // Display Records tab components
    private JTable table;

    public DashBoard() {
        frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        tabbedPane = new JTabbedPane();

        JPanel addStudentPanel = createAddStudentPanel();
        JPanel addBookPanel = createAddBookPanel();
        JPanel issueBookPanel = createIssueBookPanel();
        JPanel displayRecordsPanel = createDisplayRecordsPanel();

        tabbedPane.addTab("Add Student", addStudentPanel);
        tabbedPane.addTab("Add Book", addBookPanel);
        tabbedPane.addTab("Issue Book", issueBookPanel);
        tabbedPane.addTab("Display Records", displayRecordsPanel);

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    private JPanel createAddStudentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 15));

        JLabel lblAddFName = new JLabel("First Name:");
        JLabel lblAddLName = new JLabel("Last Name:");
        JLabel lblAddProgram = new JLabel("Program:");
        JLabel lblAddGender = new JLabel("Gender:");
        JLabel lblAddBook = new JLabel("Book Taken:");
        JLabel lblAddSection = new JLabel("Section:");

        txtAddFName = new JTextField(10);
        txtAddLName = new JTextField(10);
        txtAddBook = new JTextField(10);

        radioAddMale = new JRadioButton("Male");
        radioAddFemale = new JRadioButton("Female");
        ButtonGroup rgAdd = new ButtonGroup();
        rgAdd.add(radioAddMale);
        rgAdd.add(radioAddFemale);

        String[] section = {"Maxthon", "Mozilla", "Deism", "Divine", "Enum", "Efika", "Fusion", "Fourier", "Grit", "Garnet"};
        comboAddProgram = new JComboBox<>();
        comboAddProgram.addItem("BBA");
        comboAddProgram.addItem("BBA-TT");
        comboAddProgram.addItem("BCIS");
        comboAddProgram.addItem("BBA-BI");

        comboAddSection = new JComboBox<>(section);
        comboAddSection.addItem("Jasper");
        comboAddSection.addItem("Jasmin");
        comboAddSection.addItem("Icon");
        comboAddSection.addItem("Image");
        comboAddSection.addItem("Ideal");
        comboAddSection.addItem("helm");

        panel.add(lblAddFName);
        panel.add(txtAddFName);
        panel.add(lblAddLName);
        panel.add(txtAddLName);
        panel.add(lblAddGender);
        panel.add(createGenderPanel());
        panel.add(lblAddProgram);
        panel.add(comboAddProgram);
        panel.add(lblAddSection);
        panel.add(comboAddSection);
        panel.add(lblAddBook);
        panel.add(txtAddBook);

        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(this);
        panel.add(btnAdd);

        return panel;
    }

    private JPanel createAddBookPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 15));

        JLabel lblAddBookTitle = new JLabel("Book Title:");
        JLabel lblAddAuthor = new JLabel("Author:");
        JLabel lblAddPublication = new JLabel("Publication:");
        JLabel lblAddSubject = new JLabel("Subject:");

        txtAddBookTitle = new JTextField(10);
        txtAddAuthor = new JTextField(10);
        txtAddPublication = new JTextField(10);
        txtAddSubject = new JTextField(10);

        panel.add(lblAddBookTitle);
        panel.add(txtAddBookTitle);
        panel.add(lblAddAuthor);
        panel.add(txtAddAuthor);
        panel.add(lblAddPublication);
        panel.add(txtAddPublication);
        panel.add(lblAddSubject);
        panel.add(txtAddSubject);

        JButton btnAddBook = new JButton("Add Book");
        btnAddBook.addActionListener(this);
        panel.add(btnAddBook);

        return panel;
    }

    private JPanel createIssueBookPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 15));

        JLabel lblIssueStudentName = new JLabel("Student Name:");
        JLabel lblIssueBookIssued = new JLabel("Book Issued:");
        JLabel lblIssueIssueDate = new JLabel("Issue Date:");
        JLabel lblIssueDueDate = new JLabel("Due Date:");

        txtIssueStudentName = new JTextField(10);
        txtIssueBookIssued = new JTextField(10);
        txtIssueIssueDate = new JTextField(10);
        txtIssueDueDate = new JTextField(10);

        panel.add(lblIssueStudentName);
        panel.add(txtIssueStudentName);
        panel.add(lblIssueBookIssued);
        panel.add(txtIssueBookIssued);
        panel.add(lblIssueIssueDate);
        panel.add(txtIssueIssueDate);
        panel.add(lblIssueDueDate);
        panel.add(txtIssueDueDate);

        JButton btnIssue = new JButton("Issue");
        btnIssue.addActionListener(this);
        panel.add(btnIssue);

        return panel;
    }

    private JPanel createDisplayRecordsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 15));

        // Fetch and display records in a JTable
        fetchAndDisplayRecords(panel);

        return panel;
    }

    private void fetchAndDisplayRecords(JPanel panel) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/LibraryManagementSystem";
            String username = "root";
            String password = "root";

            Connection con = DriverManager.getConnection(url, username, password);

            String query = "SELECT s.FirstName, s.Program, s.Section, ib.BookIssued, ib.DueDate " +
                    "FROM students s " +
                    "INNER JOIN issued_books ib ON s.FirstName = ib.StudentName";
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"Student Name", "Program", "Section", "Book Taken", "Due Date"});

            while (rs.next()) {
                String studentName = rs.getString("FirstName");
                String program = rs.getString("Program");
                String section = rs.getString("Section");
                String bookTaken = rs.getString("BookIssued");
                String dueDate = rs.getString("DueDate");

                model.addRow(new Object[]{studentName, program, section, bookTaken, dueDate});
            }

            table = new JTable(model);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);

            con.close();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database connection error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createGenderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        radioAddMale = new JRadioButton("Male");
        radioAddFemale = new JRadioButton("Female");
        ButtonGroup rgAdd = new ButtonGroup();
        rgAdd.add(radioAddMale);
        rgAdd.add(radioAddFemale);

        panel.add(radioAddMale);
        panel.add(radioAddFemale);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Add")) {
            
        } else if (e.getActionCommand().equals("Add Book")) {
            String bookTitle = txtAddBookTitle.getText();
            String author = txtAddAuthor.getText();
            String publication = txtAddPublication.getText();
            String subject = txtAddSubject.getText();

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = "jdbc:mysql://localhost:3306/LibraryManagementSystem";
                String username = "root";
                String password = "root";

                Connection con = DriverManager.getConnection(url, username, password);

                String query = "INSERT INTO books(BookTitle, Author, Publication, Subject) VALUES(?,?,?,?)";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1, bookTitle);
                statement.setString(2, author);
                statement.setString(3, publication);
                statement.setString(4, subject);
                
                int n = statement.executeUpdate();

                if (n > 0) {
                    JOptionPane.showMessageDialog(null, "Book added successfully!", "Status", JOptionPane.INFORMATION_MESSAGE);
                    // After successful insertion, update the display records tab
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.setRowCount(0); // Clear existing rows
                    fetchAndDisplayRecords((JPanel) tabbedPane.getComponentAt(3)); // Refresh the table
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add book.", "Status", JOptionPane.ERROR_MESSAGE);
                }

                con.close();
            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Database connection error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getActionCommand().equals("Issue")) {
        	String studentName = txtIssueStudentName.getText();
            String bookIssued = txtIssueBookIssued.getText();
            String issueDate = txtIssueIssueDate.getText();
            String dueDate = txtIssueDueDate.getText();

            // Basic validation
            if (studentName.isEmpty() || bookIssued.isEmpty() || issueDate.isEmpty() || dueDate.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }}
        }
        
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DashBoard();
            }
        });
    }
}

