import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Date;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//store in a file
import java.io.*;
import java.util.*;




public class ToDoListApp {
    private JFrame frame;
    private DefaultListModel<Task> taskListModel;
    private JList<Task> taskList;
    private JTextField taskInputField;
    private HashMap<String, DefaultListModel<Task>> userTasks;
    private HashMap<String, User> users; //Calls the User Class
    private String currentUser;
    private Theme currentTheme;
    private DefaultListModel<Task> completedTaskListModel;



    public ToDoListApp() {

        userTasks = new HashMap<>();
        users = new HashMap<>();



    }

    //PERSONALISATION AND THEME SPECIFICATIONS -----------------------------------------------------------------------
    //THEME APPLICATION ________
    public void applyTheme(Theme theme) {
        currentTheme = theme;
        frame.getContentPane().setBackground(theme.getBackgroundColor());
        taskInputField.setBackground(theme.getBackgroundColor());
        taskInputField.setForeground(theme.getTextColor());
        taskInputField.setFont(theme.getFont());
        taskList.setBackground(theme.getBackgroundColor());
        taskList.setForeground(theme.getTextColor());
        taskList.setFont(theme.getFont());
        // Apply the theme to other UI components as needed
        SwingUtilities.updateComponentTreeUI(frame);
    }
    //CUSTOM USER PERSONALISATION __________
    private void createThemeMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu themeMenu = new JMenu("Themes");

        JMenuItem lightThemeItem = new JMenuItem("Light Theme");
        lightThemeItem.addActionListener(e -> {
            applyTheme(ThemeManager.lightTheme);
            saveCurrentTheme("light");
        });

        JMenuItem darkThemeItem = new JMenuItem("Dark Theme");
        darkThemeItem.addActionListener(e -> {
            applyTheme(ThemeManager.darkTheme);
            saveCurrentTheme("dark");
        });

        JMenuItem blueThemeItem = new JMenuItem("Blue Theme");
        blueThemeItem.addActionListener(e -> {
            applyTheme(ThemeManager.blueTheme);
            saveCurrentTheme("blue");
        });

        themeMenu.add(lightThemeItem);
        themeMenu.add(darkThemeItem);
        themeMenu.add(blueThemeItem);

        menuBar.add(themeMenu);
        frame.setJMenuBar(menuBar);
    }

    private void saveCurrentTheme(String themeName) {
        UserPreferences userPreferences = new UserPreferences();
        userPreferences.saveTheme(themeName);
    }

    private void loadAndApplyTheme() {
        UserPreferences userPreferences = new UserPreferences();
        String themeName = userPreferences.loadTheme();
        switch (themeName) {
            case "dark":
                applyTheme(ThemeManager.darkTheme);
                break;
            case "blue":
                applyTheme(ThemeManager.blueTheme);
                break;
            case "light":
            default:
                applyTheme(ThemeManager.lightTheme);
                break;
        }
    }


    public void createAndShowLogin() {
        // GUI STARTS HERE -------------------------------------------------------------------------------------------
        // Default Display Frame ________
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500); //Default size of login page

        //loadData();
        //loadUserTasks();
        loadUserList();

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //Title Label ________
        JLabel titleLabel = new JLabel("Daily To-Dos For You");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        loginPanel.add(titleLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Display logo__________
        JLabel logoLabel = new JLabel(new ImageIcon("logo.png")); // replace with your logo image
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setPreferredSize(new Dimension((int)(frame.getWidth() * 0.4), (int)(frame.getHeight() * 0.4))); // Set the size of the logo
        loginPanel.add(logoLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add some space between logo and dropdown menu

        // INTERACTIVE GUI STARTS HERE --------------------------------------------------------------------------------

        // Dropdown menu for existing users__________
        JComboBox<String> userComboBox = new JComboBox<>(users.keySet().toArray(new String[0]));

        userComboBox.setPreferredSize(new Dimension((int)(frame.getWidth() * 0.8), (int)(frame.getHeight() * 0.2)));
        userComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        userComboBox.addActionListener(e -> showPasscodePrompt((String) userComboBox.getSelectedItem()));
        loginPanel.add(userComboBox);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20))); //space between the buttons


        // Option to add a new account___________
        JButton newAccountButton = new JButton("Add New Account");
        newAccountButton.setPreferredSize(new Dimension(200, 50));
        newAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newAccountButton.setFocusPainted(false);
        newAccountButton.setFont(new Font("Arial", Font.PLAIN, 14));
        newAccountButton.addActionListener(e -> showNewAccountPrompt());
        loginPanel.add(newAccountButton);

        frame.add(loginPanel);
        frame.setIconImage(new ImageIcon("logo.png").getImage()); // replace with your logo image
        frame.setVisible(true);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            //saveData();
            saveUserList();
            //saveUserTasks();// ACCOUNT DATA SAVED HERE -- __ -- __ -- __ -- __ --
            frame.dispose();
            createAndShowLogin(); // back login page from to-do list
        });

        //Option to remove an existing account

        JButton removeAccountButton = new JButton("Remove Account");
        removeAccountButton.setPreferredSize(new Dimension(200, 50));
        removeAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeAccountButton.setFocusPainted(false);
        removeAccountButton.setFont(new Font("Arial", Font.PLAIN, 14));
        removeAccountButton.addActionListener(e -> {
            String selectedUser = (String) userComboBox.getSelectedItem();
            if (selectedUser!= null) {
                users.remove(selectedUser);
                userTasks.remove(selectedUser);
                //saveData();
                saveUserList();
                JOptionPane.showMessageDialog(frame, "User removed successfully");
                userComboBox.removeItem(selectedUser);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a user to remove");
            }
        });
        loginPanel.add(removeAccountButton);



    }

    //subclasses used in LOGIN STARTS HERE --------------------------------------------------------------------------

    //PASSCODE AUTHORISED ACCESS PROMPT __________
    private void showPasscodePrompt(String username) {
        // GUI
        JPanel passcodePanel = new JPanel();
        passcodePanel.setLayout(new BoxLayout(passcodePanel, BoxLayout.Y_AXIS));
        JLabel passcodeLabel = new JLabel("Enter passcode for " + username + ":");
        passcodeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passcodePanel.add(passcodeLabel);
        JPasswordField passcodeField = new JPasswordField(20);
        passcodeField.setMaximumSize(new Dimension(200, 30));
        passcodeField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passcodePanel.add(passcodeField);

        // Confirm Dialog for the Passcode Enter Corresponding, works by pressing enter
        int result = JOptionPane.showConfirmDialog(frame, passcodePanel,
                "Passcode", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String passcode = new String(passcodeField.getPassword()).trim(); //Setting the Passcode through String
            if (passcode.equals(users.get(username).getPasscode())) {
                currentUser = username;
                //loadData();
                //loadUserTasks();
                frame.dispose();
                createAndShowGUI();
            } else {
                JOptionPane.showMessageDialog(frame, "Incorrect passcode. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }


    }
    //NEW ACCOUNT PROMPT_________
    private void showNewAccountPrompt() {
        //GUI
        JPanel newAccountPanel = new JPanel();
        newAccountPanel.setLayout(new BoxLayout(newAccountPanel, BoxLayout.Y_AXIS));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        newAccountPanel.add(usernameLabel);
        JTextField usernameField = new JTextField(20);
        usernameField.setMaximumSize(new Dimension(200, 30));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        newAccountPanel.add(usernameField);

        JLabel passcodeLabel = new JLabel("Passcode:");
        passcodeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        newAccountPanel.add(passcodeLabel);
        JPasswordField passcodeField = new JPasswordField(20);
        passcodeField.setMaximumSize(new Dimension(200, 30));
        passcodeField.setAlignmentX(Component.CENTER_ALIGNMENT);
        newAccountPanel.add(passcodeField);

        int result = JOptionPane.showConfirmDialog(frame, newAccountPanel, "Create New Account", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String passcode = new String(passcodeField.getPassword()).trim();
            if (!username.isEmpty() && !passcode.isEmpty()) {
                if (users.containsKey(username)) {
                    JOptionPane.showMessageDialog(frame, "Username already exists. Choose another.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    currentUser = username;
                    users.put(username, new User(username, passcode)); //Calls User Class
                    saveUserList();
                    //loadUserTasks();
                    frame.dispose();
                    createAndShowGUI();
                }
                //VALIDATION
            } else {
                JOptionPane.showMessageDialog(frame, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    //PRIVATE TO-DO LIST STARTS HERE ---------------------------------------------------------------------------------
public void createAndShowGUI() {
    //GUI
    frame = new JFrame(" Your To-Do List");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400, 300);

    //loadData();

    // Render Uncompleted tasks list
    taskListModel = userTasks.getOrDefault(currentUser, new DefaultListModel<>());
    JList<Task> uncompletedTaskList = new JList<>(taskListModel);
    uncompletedTaskList.setCellRenderer(new TaskCellRenderer());
    JPanel uncompletedTaskPanel = new JPanel();
    uncompletedTaskPanel.add(new JScrollPane(uncompletedTaskList));
    frame.add(uncompletedTaskPanel, BorderLayout.CENTER);

    // Render Completed tasks list
    completedTaskListModel = new DefaultListModel<>();
    JList<Task> completedTaskList = new JList<>(completedTaskListModel);
    completedTaskList.setCellRenderer(new TaskCellRenderer());
    JPanel completedTaskPanel = new JPanel();
    completedTaskPanel.add(new JScrollPane(completedTaskList));
    frame.add(completedTaskPanel, BorderLayout.SOUTH);

    //tasklist
    taskList = new JList<>(taskListModel);
    taskList.setCellRenderer(new TaskCellRenderer());
    taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    taskInputField = new JTextField(20);
    taskInputField.addActionListener(e -> addTask()); // --------------------------------------

    JButton addButton = new JButton("Add");
    addButton.addActionListener(e -> {
        addTask();}


        //saveUserTasks();
        //saveData();} //-_-_-_
    ); //----------------------------------------

    JButton removeButton = new JButton("Remove");
    removeButton.addActionListener(e -> removeTask());

    JButton completeButton = new JButton("Complete");
    completeButton.addActionListener(e -> {
        completeTask();
        //saveUserTasks();
        //saveData();
    }); //-------------------------------

    JButton logoutButton = new JButton("Logout");
    logoutButton.addActionListener(e -> {
        //saveUserTasks();
        //saveData();
        frame.dispose();
        createAndShowLogin(); // back login page
    });

    JPanel controlPanel = new JPanel();
    controlPanel.add(addButton);
    controlPanel.add(removeButton);
    controlPanel.add(completeButton);
    controlPanel.add(logoutButton);

    JPanel inputPanel = new JPanel();
    inputPanel.add(taskInputField);
    inputPanel.add(addButton);

    frame.setLayout(new BorderLayout());
    frame.add(new JScrollPane(taskList), BorderLayout.CENTER);
    frame.add(inputPanel, BorderLayout.NORTH);
    frame.add(controlPanel, BorderLayout.SOUTH);

    frame.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            //saveUserTasks(); // Save user tasks before exiting the application
            //saveData();
            System.exit(0);
        }
    });

    loadAndApplyTheme();  // Apply the saved theme
    //loadData();
    createThemeMenu();  // Add the theme selection menu

    frame.setVisible(true);
}
    private void addTask() {
        String taskText = taskInputField.getText().trim();
        if (!taskText.isEmpty()) {
            taskListModel.addElement(new Task(taskText));
            //saveUserTasks();
            taskInputField.setText("");

            //saveData();
        }
    }

    private void removeTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            taskListModel.remove(selectedIndex);
            //saveUserTasks();
            //saveData();
        }
    }

    private void completeTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            Task task = taskListModel.getElementAt(selectedIndex);
            task.setCompleted(true);
            task.setCompletionTime(System.currentTimeMillis());
            taskListModel.remove(selectedIndex);
            completedTaskListModel.addElement(task);
            taskListModel.add(0, task); // Move the completed task to the top
            //saveUserTasks();
            //saveData();

        }
    }
    private void loadUserList() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.ser"))) {
            users = (HashMap<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading user list: " + e.getMessage());
        }
    }

    private void saveUserList() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.ser"))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.err.println("Error saving user list: " + e.getMessage());
        }
    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ToDoListApp().createAndShowLogin());
    }




    class TaskCellRenderer extends DefaultListCellRenderer { //INHERITANCE
        @Override //POLYMORPHISM
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Task) {
                Task task = (Task) value;
                if (task.isCompleted()) {
                    String description = "<html><strike>" + task.getDescription() + "</strike> (Completed: " + new Date(task.getCompletionTime()) + ")</html>";
                    setText(description);
                    component.setForeground(Color.GRAY);
                } else {
                    setText(task.getDescription());
                    component.setForeground(currentTheme.getTextColor());
                }
            }
            return component;
        }
    }
    }





