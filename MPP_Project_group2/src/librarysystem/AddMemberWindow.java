package librarysystem;

import business.*;
import librarysystem.rulesets.RuleSet;
import librarysystem.rulesets.RuleSetFactory;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;


public class AddMemberWindow extends JFrame implements LibWindow {
    public static final AddMemberWindow INSTANCE = new AddMemberWindow();

    private ControllerInterface memberInterface = new SystemController();

    private JTextArea textArea;
    private JPanel bottomPPanel = new JPanel(new BorderLayout());

    private JSplitPane splitPaneOuter;


    public static String title = "Add New Library Member";

    private JLabel headerLabel = new JLabel(title);

    private JPanel leftAlignPanel = new JPanel();
    private JPanel rightAlignPanel = new JPanel();

    // memberID
    private JLabel memberIdLabel = new JLabel("Member ID");
    private JTextField memberIDField = new JTextField(10);

    // First Name
    private JLabel firstNameLabel = new JLabel("First name");
    private JTextField firstNameTextField = new JTextField(10);

    // last name
    private JLabel lastNameLabel = new JLabel("Last name");
    private JTextField lastNameTextField = new JTextField(10);


    // tel
    private JLabel phoneLabel = new JLabel("Tel.");
    private JTextField phoneTextField = new JTextField(10);

    // city
    private JLabel cityLabel = new JLabel("City");
    private JTextField cityTextField = new JTextField(10);

    // street
    private JLabel streetLabel = new JLabel("Street");
    private JTextField streetTextField = new JTextField(20);

    // zip
    private JLabel zipLabel = new JLabel("Zip Code");
    private JTextField zipCodeField = new JTextField(10);

    // state
    private JLabel stateLabel = new JLabel("State");
    private JTextField stateField = new JTextField(10);


    // middle
    private JPanel middlePanel = new JPanel(new FlowLayout());
    private JPanel middleWrapperPanel = new JPanel(new BorderLayout());

    private JButton button = new JButton("Add Member");
    private JPanel bottomPanel = new JPanel(new FlowLayout());
    private JPanel mainPanel = new JPanel(new BorderLayout());
    private boolean isInitialized = false;

    private AddMemberWindow() {
    }

    @Override
    public void init() {
        if (isInitialized) {
            return;
        }
        setSize(600,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        updateHeaderLabel();
        setupLeftAlignPanel();
        setupRightAlignPanel();
        setupTextArea();
        setupButton();
        mainPanel.add(middleWrapperPanel,BorderLayout.CENTER);
        middleWrapperPanel.add(middlePanel,BorderLayout.NORTH);
        setTitle(title);
        isInitialized(true);
        setUpVerticalPane();
        getContentPane().add(splitPaneOuter);

    }

    private void setUpVerticalPane() {
        splitPaneOuter = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                mainPanel,
                textArea);
        splitPaneOuter.setDividerLocation(getHeight()*3/4);
        add(splitPaneOuter);
    }

    private void setupTextArea() {
        textArea = new JTextArea("Welcome to the Library System!");
        textArea.setMaximumSize(new Dimension(500,10));
        Util.adjustLabelFont(textArea,Util.DARK_BLUE,true);
        bottomPPanel.add(textArea,BorderLayout.NORTH);
    }

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public void isInitialized(boolean val) {
        isInitialized = val;
    }

    private void updateHeaderLabel() {
        Util.adjustLabelFont(headerLabel,Util.DARK_BLUE,true);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(headerLabel);
        mainPanel.add(panel,BorderLayout.NORTH);

    }
    private void setupButton() {
        bottomPanel.add(button,BorderLayout.NORTH);
        middleWrapperPanel.add(bottomPanel,BorderLayout.CENTER);

        JButton backButton = new JButton("<= Back to List");
        backButton.addActionListener(evt -> {
            LibrarySystem.hideAllWindows();
            AllMemberWindow.INSTANCE.init();
            AllMemberWindow.INSTANCE.pack();
            AllMemberWindow.INSTANCE.setVisible(true);
            Util.centerFrameOnDesktop(AllMemberWindow.INSTANCE);

        });
        bottomPanel.add(backButton);

        button.setSize(100,30);
        button.addActionListener((evt) -> {
            try {

                RuleSet rules = RuleSetFactory.getRuleSet(AddMemberWindow.this);
                rules.applyRules(AddMemberWindow.this);
            	

                LibraryMember lm = memberInterface.createLibraryMember(
                        getMemberID(),
                        getFirstName(),
                        getLastName(),
                        getStreet(),
                        getCity(),
                        getZipCode(),
                        getStateString(),
                        getPhone()
                );
                AllMemberWindow.INSTANCE.refreshTable(lm);
            } catch (Group2Exception e) {
            	memberInterface.showError(e.getMessage());
                return;
            }
            memberInterface.showInfo("");
            clearTextFields();
            JOptionPane.showMessageDialog(this,"Successful added " + getMemberID());
        });

    }

    private void setupLeftAlignPanel() {
        leftAlignPanel.setLayout(new BoxLayout(leftAlignPanel,BoxLayout.Y_AXIS));

        Component[] items = new Component[]{
                memberIdLabel,
                firstNameLabel,
                lastNameLabel,
                phoneLabel,
                streetLabel,
                cityLabel,
                zipLabel,
                stateLabel
        };

        for (Component item: items) {
            leftAlignPanel.add(item);
            leftAlignPanel.add(Box.createRigidArea(new Dimension(0, 17)));
        }

        middlePanel.add(leftAlignPanel);
    }

    private void setupRightAlignPanel() {
        rightAlignPanel.setLayout(new BoxLayout(rightAlignPanel,BoxLayout.Y_AXIS));
        Component[] items = new Component[]{
                memberIDField,
                firstNameTextField,
                lastNameTextField,
                phoneTextField,
                streetTextField,
                cityTextField,
                zipCodeField,
                stateField
        };
        for (Component item: items) {
            rightAlignPanel.add(item);
            rightAlignPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        middlePanel.add(rightAlignPanel);
    }


    private void clearTextFields() {
        memberIDField.setText("");
        firstNameTextField.setText("");
        lastNameTextField.setText("");
        phoneTextField.setText("");
        streetTextField.setText("");
        cityTextField.setText("");
        zipCodeField.setText("");
        stateField.setText("");
    }


    public String getMemberID() {
        return memberIDField.getText();
    }
    public String getFirstName() {
        return firstNameTextField.getText();
    }
    public String getLastName() {
        return lastNameTextField.getText();
    }

    public String getPhone() {
        return phoneTextField.getText();
    }
    public String getStreet() {
        return streetTextField.getText();
    }
    public String getCity() {
        return cityTextField.getText();
    }
    public String getZipCode() {
        return zipCodeField.getText();
    }

    public String getStateString() {
        return zipCodeField.getText();
    }


}
