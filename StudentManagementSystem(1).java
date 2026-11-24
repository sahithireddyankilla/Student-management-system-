import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentManagementSystem extends JFrame {
    private JTextField nameField, rollField, emailField, phoneField, searchField;
    private JComboBox<String> courseBox, deptBox;
    private JTable table;
    private DefaultTableModel model;

    public StudentManagementSystem() {
        setTitle("Student Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout(12, 12));
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        main.setBackground(new Color(240, 245, 250));

        JLabel heading = new JLabel("STUDENT MANAGEMENT SYSTEM", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setForeground(new Color(25, 55, 110));
        main.add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Student Details"));
        form.setBackground(Color.WHITE);

        nameField = new JTextField();
        rollField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();
        courseBox = new JComboBox<>(new String[]{"B.Tech", "B.Sc", "B.Com", "BA", "MCA", "MBA", "Other"});
        deptBox = new JComboBox<>(new String[]{"Computer Science", "Electronics", "Mechanical", "Civil", "Commerce", "Arts", "Other"});

        form.add(new JLabel("Student Name:")); form.add(nameField);
        form.add(new JLabel("Roll Number:")); form.add(rollField);
        form.add(new JLabel("Email:")); form.add(emailField);
        form.add(new JLabel("Phone:")); form.add(phoneField);
        form.add(new JLabel("Course:")); form.add(courseBox);
        form.add(new JLabel("Department:")); form.add(deptBox);

        JButton addButton = new JButton("Add Student");
        JButton updateButton = new JButton("Update Selected");
        JButton deleteButton = new JButton("Delete Selected");
        JButton clearButton = new JButton("Clear Fields");

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(addButton); buttons.add(updateButton);
        buttons.add(deleteButton); buttons.add(clearButton);

        JPanel left = new JPanel(new BorderLayout(8, 8));
        left.setPreferredSize(new Dimension(330, 0));
        left.add(form, BorderLayout.CENTER);
        left.add(buttons, BorderLayout.SOUTH);

        model = new DefaultTableModel(
            new String[]{"Name", "Roll No.", "Email", "Phone", "Course", "Department"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < 6; i++) table.getColumnModel().getColumn(i).setPreferredWidth(130);

        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton showAllButton = new JButton("Show All");
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search name / roll:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);

        JPanel right = new JPanel(new BorderLayout(8, 8));
        right.add(searchPanel, BorderLayout.NORTH);
        right.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel content = new JPanel(new BorderLayout(12, 12));
        content.add(left, BorderLayout.WEST);
        content.add(right, BorderLayout.CENTER);
        main.add(content, BorderLayout.CENTER);

        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        clearButton.addActionListener(e -> clearFields());
        searchButton.addActionListener(e -> searchStudents());
        showAllButton.addActionListener(e -> showAllStudents());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int r = table.getSelectedRow();
                nameField.setText(model.getValueAt(r, 0).toString());
                rollField.setText(model.getValueAt(r, 1).toString());
                emailField.setText(model.getValueAt(r, 2).toString());
                phoneField.setText(model.getValueAt(r, 3).toString());
                courseBox.setSelectedItem(model.getValueAt(r, 4).toString());
                deptBox.setSelectedItem(model.getValueAt(r, 5).toString());
            }
        });

        setContentPane(main);
    }

    private boolean validateFields() {
        if (nameField.getText().trim().isEmpty() || rollField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter student name and roll number.");
            return false;
        }
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 1).toString().equalsIgnoreCase(rollField.getText().trim())
                    && i != table.getSelectedRow()) {
                JOptionPane.showMessageDialog(this, "Roll number already exists.");
                return false;
            }
        }
        return true;
    }

    private Object[] getStudentData() {
        return new Object[]{nameField.getText().trim(), rollField.getText().trim(),
            emailField.getText().trim(), phoneField.getText().trim(),
            courseBox.getSelectedItem(), deptBox.getSelectedItem()};
    }

    private void addStudent() {
        if (!validateFields()) return;
        model.addRow(getStudentData());
        clearFields();
        JOptionPane.showMessageDialog(this, "Student added successfully!");
    }

    private void updateStudent() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a student from the table to update.");
            return;
        }
        if (!validateFields()) return;
        for (int col = 0; col < 6; col++) model.setValueAt(getStudentData()[col], row, col);
        clearFields();
        JOptionPane.showMessageDialog(this, "Student updated successfully!");
    }

    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a student to delete.");
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this, "Delete selected student?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            model.removeRow(row);
            clearFields();
        }
    }

    private void searchStudents() {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) { showAllStudents(); return; }
        for (int i = 0; i < model.getRowCount(); i++) {
            String name = model.getValueAt(i, 0).toString().toLowerCase();
            String roll = model.getValueAt(i, 1).toString().toLowerCase();
            if (name.contains(query) || roll.contains(query)) {
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "No matching student found.");
    }

    private void showAllStudents() {
        searchField.setText("");
        table.clearSelection();
    }

    private void clearFields() {
        nameField.setText("");
        rollField.setText("");
        emailField.setText("");
        phoneField.setText("");
        courseBox.setSelectedIndex(0);
        deptBox.setSelectedIndex(0);
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentManagementSystem().setVisible(true));
    }
}
