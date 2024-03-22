import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * This class represents a user registration system with GUI.
 */
public class User extends JFrame {
    // Components for user registration
    private JLabel usernameLabel, emailLabel, passwordLabel, eloRatingLabel;
    private JTextField usernameField, emailField, eloRatingField;
    private JPasswordField passwordField;
    private JButton registerButton, loginButton;

    /**
     * Constructor to initialize the user registration GUI.
     */
    public User() {
        setTitle("User Registration");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        // Initialize components
        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        add(usernameLabel);
        add(usernameField);

        emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        add(emailLabel);
        add(emailField);

        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        add(passwordLabel);
        add(passwordField);

        eloRatingLabel = new JLabel("Elo Rating:");
        eloRatingField = new JTextField();
        add(eloRatingLabel);
        add(eloRatingField);

        // Register button action listener
        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            if (validateRegistration()) {
                registerUser();
                openLoginPage();
            } else {
                JOptionPane.showMessageDialog(User.this, "Please fill in all fields.");
            }
        });
        add(registerButton);

        // Login button action listener
        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> openLoginPage());
        add(loginButton);

        setVisible(true);
    }

    /**
     * Validates the user registration fields.
     * @return true if all fields are filled, false otherwise
     */
    private boolean validateRegistration() {
        // Check if all fields are filled
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String eloRating = eloRatingField.getText();
        return !username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !eloRating.isEmpty();
    }

    /**
     * Registers the user by saving their details.
     */
    private void registerUser() {
        // Get user details
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        int eloRating = Integer.parseInt(eloRatingField.getText());

        // Save user details to file
        try (FileWriter writer = new FileWriter("users.txt", true);
             BufferedWriter bw = new BufferedWriter(writer)) {
            bw.write(username + "," + email + "," + password + "," + eloRating);
            bw.newLine();
            JOptionPane.showMessageDialog(this, "User registered successfully!");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while registering user.");
        }
    }

    /**
     * Opens the login page and disposes the current frame.
     */
    private void openLoginPage() {
        dispose();
        new LoginPage(this);
    }

    /**
     * This class represents the login page for registered users.
     */
    private class LoginPage extends JFrame {
        // Components for login page
        private JLabel usernameLabel, passwordLabel;
        private JTextField usernameField;
        private JPasswordField passwordField;
        private JButton loginButton, backButton;
        private User parent;

        /**
         * Constructor to initialize the login page GUI.
         * @param parent The parent frame (User) reference
         */
        public LoginPage(User parent) {
            this.parent = parent;
            setTitle("User Login");
            setSize(300, 150);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new GridLayout(4, 2));

            // Initialize components
            usernameLabel = new JLabel("Username:");
            usernameField = new JTextField();
            add(usernameLabel);
            add(usernameField);

            passwordLabel = new JLabel("Password:");
            passwordField = new JPasswordField();
            add(passwordLabel);
            add(passwordField);

            // Login button action listener
            loginButton = new JButton("Login");
            loginButton.addActionListener(e -> loginUser());
            add(loginButton);

            // Back button action listener
            backButton = new JButton("Back to Register");
            backButton.addActionListener(e -> backToRegister());
            add(backButton);

            setVisible(true);
        }

        /**
         * Logs in the user by validating the credentials.
         */
        private void loginUser() {
            // Get entered username and password
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Validate credentials
            if (checkCredentials(username, password)) {
                dispose();
                displayDashboard(username);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        }

        /**
         * Checks the entered username and password against stored credentials.
         * @param username The entered username
         * @param password The entered password
         * @return true if the credentials are valid, false otherwise
         */
        private boolean checkCredentials(String username, String password) {
            try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data[0].equals(username) && data[2].equals(password)) {
                        return true;
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return false;
        }

        /**
         * Navigates back to the user registration page.
         */
        private void backToRegister() {
            dispose();
            parent.setVisible(true);
        }
    }

 /**
     * Displays the dashboard after successful user login.
     * @param username The username of the logged-in user
     */
    private void displayDashboard(String username) {
        // Create dashboard frame
        JFrame dashboardFrame = new JFrame("Dashboard");
        dashboardFrame.setSize(400, 200);
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Welcome label
        JLabel welcomeLabel = new JLabel("Hey, Welcome " + username + "!");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        dashboardFrame.add(welcomeLabel, BorderLayout.NORTH);

        // Button panel for various options
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        JButton lessonsButton = new JButton("Apply for Lesson Training");
        JButton puzzlesButton = new JButton("Try Puzzle");
        JButton gamesButton = new JButton("Play Game");
        JButton coachButton = new JButton("Coaching");

        // Add buttons to the panel
        buttonPanel.add(lessonsButton);
        buttonPanel.add(puzzlesButton);
        buttonPanel.add(gamesButton);
        buttonPanel.add(coachButton);

        // Add button panel to the dashboard frame
        dashboardFrame.add(buttonPanel, BorderLayout.CENTER);

        // Add action listeners to buttons
        lessonsButton.addActionListener(e -> showLessonTrainingForm());
        puzzlesButton.addActionListener(e -> showPuzzleForm());
        gamesButton.addActionListener(e -> showGameForm());
        coachButton.addActionListener(e -> showCoachingForm());

        // Make the dashboard frame visible
        dashboardFrame.setVisible(true);
    }

    /**
     * Displays the lesson training form for the user to apply for lessons.
     */
    private void showLessonTrainingForm() {
        // Create lesson training form frame
        JFrame lessonFrame = new JFrame("Apply for Lesson Training");
        lessonFrame.setSize(400, 250);
        lessonFrame.setLayout(new GridLayout(6, 2));

        // Initialize form components
        JLabel idLabel = new JLabel("ID:");
        JTextField idField = new JTextField();
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel fideIdLabel = new JLabel("FIDE ID:");
        JTextField fideIdField = new JTextField();
        JLabel coachLabel = new JLabel("Coach Trainer:");
        JTextField coachField = new JTextField();
        JLabel descriptionLabel = new JLabel("Application Description:");
        JTextField descriptionField = new JTextField();
        JButton saveButton = new JButton("Save");
        JButton backButton = new JButton("Back");

        // Add components to the form frame
        lessonFrame.add(idLabel);
        lessonFrame.add(idField);
        lessonFrame.add(usernameLabel);
        lessonFrame.add(usernameField);
        lessonFrame.add(fideIdLabel);
        lessonFrame.add(fideIdField);
        lessonFrame.add(coachLabel);
        lessonFrame.add(coachField);
        lessonFrame.add(descriptionLabel);
        lessonFrame.add(descriptionField);
        lessonFrame.add(saveButton);
        lessonFrame.add(backButton);

        // Add action listeners to buttons
        saveButton.addActionListener(e -> {
            if (validateLessonForm(idField.getText(), usernameField.getText(), fideIdField.getText(), coachField.getText(), descriptionField.getText())) {
                saveCredentials(idField.getText(), usernameField.getText(), fideIdField.getText(), coachField.getText(), descriptionField.getText());
            } else {
                JOptionPane.showMessageDialog(lessonFrame, "Please fill in all fields.");
            }
        });
        backButton.addActionListener(e -> lessonFrame.dispose());

        // Display saved credentials
        JTextArea credentialsArea = new JTextArea();
        lessonFrame.add(new JScrollPane(credentialsArea));

        // Make the lesson training form frame visible
        lessonFrame.setVisible(true);
    }
    private boolean validateLessonForm(String id, String username, String fideId, String coach, String description) {
        return !id.isEmpty() && !username.isEmpty() && !fideId.isEmpty() && !coach.isEmpty() && !description
        .isEmpty();
    }

    private void saveCredentials(String id, String username, String fideId, String coach, String description) {
        // Save credentials to local storage (e.g., file)
        try (FileWriter writer = new FileWriter("lesson_credentials.txt", true);
             BufferedWriter bw = new BufferedWriter(writer)) {
            bw.write(id + "," + username + "," + fideId + "," + coach + "," + description);
            bw.newLine();
            JOptionPane.showMessageDialog(null, "Credentials saved successfully!");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occurred while saving credentials.");
        }
    }
    /**
     * Displays the puzzle form for the user to try a puzzle.
     */
    private void showPuzzleForm() {
        JFrame puzzleFrame = new JFrame("Try Puzzle");
        puzzleFrame.setSize(400, 250);
        puzzleFrame.setLayout(new GridLayout(4, 2));

        // Labels and text fields for ID, username, and puzzle difficulty
        JLabel idLabel = new JLabel("ID:");
        JTextField idField = new JTextField();
        puzzleFrame.add(idLabel);
        puzzleFrame.add(idField);

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        puzzleFrame.add(usernameLabel);
        puzzleFrame.add(usernameField);

        JLabel difficultyLabel = new JLabel("Puzzle Difficulty:");
        String[] difficulties = {"Easy", "Hard", "Extremely Hard"};
        JComboBox<String> difficultyComboBox = new JComboBox<>(difficulties);
        puzzleFrame.add(difficultyLabel);
        puzzleFrame.add(difficultyComboBox);

        // Submit and back buttons
        JButton submitButton = new JButton("Submit");
        JButton backButton = new JButton("Back");

        // ActionListener for the submit button
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (validatePuzzleForm(idField.getText(), usernameField.getText())) {
                    savePuzzleCredentials(idField.getText(), usernameField.getText(), (String) difficultyComboBox.getSelectedItem());
                } else {
                    JOptionPane.showMessageDialog(puzzleFrame, "Please fill in all fields.");
                }
            }
        });

        // ActionListener for the back button
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                puzzleFrame.dispose();
            }
        });

        // Add buttons to the puzzle frame
        puzzleFrame.add(submitButton);
        puzzleFrame.add(backButton);

        // Display area for saved credentials
        JTextArea credentialsArea = new JTextArea();
        puzzleFrame.add(new JScrollPane(credentialsArea));

        // Make the puzzle frame visible
        puzzleFrame.setVisible(true);
    }

    /**
     * Validates the puzzle form fields.
     * @param id The ID entered in the puzzle form
     * @param username The username entered in the puzzle form
     * @return True if both ID and username are not empty, otherwise false
     */
    private boolean validatePuzzleForm(String id, String username) {
        return !id.isEmpty() && !username.isEmpty();
    }

    /**
     * Saves puzzle credentials to local storage.
     * @param id The ID entered in the puzzle form
     * @param username The username entered in the puzzle form
     * @param difficulty The difficulty level selected in the puzzle form
     */
    private void savePuzzleCredentials(String id, String username, String difficulty) {
        // Save credentials to local storage (e.g., file)
        try (FileWriter writer = new FileWriter("puzzle_credentials.txt", true);
             BufferedWriter bw = new BufferedWriter(writer)) {
            bw.write("ID: " + id + ", Username: " + username + ", Difficulty: " + difficulty);
            bw.newLine();
            JOptionPane.showMessageDialog(null, "Puzzle credentials saved successfully!");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occurred while saving puzzle credentials.");
        }
    }

  /**
     * Displays the form for playing a game.
     */
    private void showGameForm() {
        // Create a new JFrame for the game form
        JFrame gameFrame = new JFrame("Play Game");
        gameFrame.setSize(400, 250);
        gameFrame.setLayout(new GridLayout(5, 2));

        // Labels and text fields for your ID, white player, black player, and result
        JLabel yourIdLabel = new JLabel("Your ID:");
        JTextField yourIdField = new JTextField();
        gameFrame.add(yourIdLabel);
        gameFrame.add(yourIdField);

        JLabel whitePlayerLabel = new JLabel("White Player Username:");
        JTextField whitePlayerField = new JTextField();
        gameFrame.add(whitePlayerLabel);
        gameFrame.add(whitePlayerField);

        JLabel blackPlayerLabel = new JLabel("Black Player Username:");
        JTextField blackPlayerField = new JTextField();
        gameFrame.add(blackPlayerLabel);
        gameFrame.add(blackPlayerField);

        JLabel resultLabel = new JLabel("Result:");
        String[] results = {"White", "Black", "Draw"};
        JComboBox<String> resultComboBox = new JComboBox<>(results);
        gameFrame.add(resultLabel);
        gameFrame.add(resultComboBox);

        // Submit and back buttons
        JButton submitButton = new JButton("Submit");
        JButton backButton = new JButton("Back");

        // ActionListener for the submit button
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Validate form fields and save credentials if valid
                if (validateGameForm(yourIdField.getText(), whitePlayerField.getText(), blackPlayerField.getText())) {
                    saveGameCredentials(yourIdField.getText(), whitePlayerField.getText(), blackPlayerField.getText(), (String) resultComboBox.getSelectedItem());
                } else {
                    JOptionPane.showMessageDialog(gameFrame, "Please fill in all fields.");
                }
            }
        });

        // ActionListener for the back button
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Close the game form
                gameFrame.dispose();
            }
        });

        // Add buttons to the game frame
        gameFrame.add(submitButton);
        gameFrame.add(backButton);

        // Display area for saved credentials
        JTextArea credentialsArea = new JTextArea();
        gameFrame.add(new JScrollPane(credentialsArea));

        // Make the game frame visible
        gameFrame.setVisible(true);
    }

    /**
     * Validates the game form fields.
     * @param yourId The ID entered in the game form
     * @param whitePlayer The username of the white player entered in the game form
     * @param blackPlayer The username of the black player entered in the game form
     * @return True if all fields are not empty, otherwise false
     */
    private boolean validateGameForm(String yourId, String whitePlayer, String blackPlayer) {
        // Check if all fields are not empty
        return !yourId.isEmpty() && !whitePlayer.isEmpty() && !blackPlayer.isEmpty();
    }

    /**
     * Saves game credentials to local storage.
     * @param yourId The ID entered in the game form
     * @param whitePlayer The username of the white player entered in the game form
     * @param blackPlayer The username of the black player entered in the game form
     * @param result The result of the game (either "White" or "Black")
     */
    private void saveGameCredentials(String yourId, String whitePlayer, String blackPlayer, String result) {
        // Save credentials to local storage (e.g., file)
        try (FileWriter writer = new FileWriter("game_credentials.txt", true);
             BufferedWriter bw = new BufferedWriter(writer)) {
            bw.write("Your ID: " + yourId + ", White Player: " + whitePlayer + ", Black Player: " + blackPlayer + ", Result: " + result);
            bw.newLine();
            JOptionPane.showMessageDialog(null, "Game credentials saved successfully!");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occurred while saving game credentials.");
        }
    }
    /**
     * Displays the form for coaching.
     */
    private void showCoachingForm() {
        // Create a new JFrame for the coaching form
        JFrame coachingFrame = new JFrame("Coaching");
        coachingFrame.setSize(400, 250);
        coachingFrame.setLayout(new GridLayout(4, 2));

        // Labels and text fields for ID, name, coach bio, and student to coach
        JLabel idLabel = new JLabel("ID:");
        JTextField idField = new JTextField();
        coachingFrame.add(idLabel);
        coachingFrame.add(idField);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        coachingFrame.add(nameLabel);
        coachingFrame.add(nameField);

        JLabel bioLabel = new JLabel("Coach Bio:");
        JTextField bioField = new JTextField();
        coachingFrame.add(bioLabel);
        coachingFrame.add(bioField);

        JLabel studentLabel = new JLabel("Student to Coach:");
        JTextField studentField = new JTextField();
        coachingFrame.add(studentLabel);
        coachingFrame.add(studentField);

        // Submit and back buttons
        JButton submitButton = new JButton("Submit");
        JButton backButton = new JButton("Back");

        // ActionListener for the submit button
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Validate form fields and save credentials if valid
                if (validateCoachingForm(idField.getText(), nameField.getText(), bioField.getText(), studentField.getText())) {
                    saveCoachingCredentials(idField.getText(), nameField.getText(), bioField.getText(), studentField.getText());
                } else {
                    JOptionPane.showMessageDialog(coachingFrame, "Please fill in all fields.");
                }
            }
        });

        // ActionListener for the back button
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Close the coaching form
                coachingFrame.dispose();
            }
        });

        // Add buttons to the coaching frame
        coachingFrame.add(submitButton);
        coachingFrame.add(backButton);

        // Display area for saved credentials
        JTextArea credentialsArea = new JTextArea();
        coachingFrame.add(new JScrollPane(credentialsArea));

        // Make the coaching frame visible
        coachingFrame.setVisible(true);
    }

    /**
     * Validates the coaching form fields.
     * @param id The ID entered in the coaching form
     * @param name The name entered in the coaching form
     * @param bio The coach bio entered in the coaching form
     * @param student The student to coach entered in the coaching form
     * @return True if all fields are not empty, otherwise false
     */
    private boolean validateCoachingForm(String id, String name, String bio, String student) {
        // Check if all fields are not empty
        return !id.isEmpty() && !name.isEmpty() && !bio.isEmpty() && !student.isEmpty();
    }

    /**
     * Saves coaching credentials to local storage.
     * @param id The ID entered in the coaching form
     * @param name The name entered in the coaching form
     * @param bio The coach bio entered in the coaching form
     * @param student The student to coach entered in the coaching form
     */
    private void saveCoachingCredentials(String id, String name, String bio, String student) {
        // Save credentials to local storage (e.g., file)
        try (FileWriter writer = new FileWriter("coaching_credentials.txt", true);
             BufferedWriter bw = new BufferedWriter(writer)) {
            bw.write("ID: " + id + ", Name: " + name + ", Coach Bio: " + bio + ", Student: " + student);
            bw.newLine();
            JOptionPane.showMessageDialog(null, "Coaching credentials saved successfully!");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occurred while saving coaching credentials.");
        }
    }

    // Main method to start the application
    public static void main(String[] args) {
        // Run the User interface
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new User();
            }
        });
    }
}
