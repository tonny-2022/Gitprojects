package librarysystem;

import business.*;
import business.SystemController;
import business.ControllerInterface;
import librarysystem.rulesets.RuleSet;
import librarysystem.rulesets.RuleSetFactory;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;


public class MemberCheckoutRecordWindow extends JFrame implements LibWindow {

    private final ControllerInterface memberInterface = new SystemController();

    public static String title = "Member Checkout Record";

    public static final MemberCheckoutRecordWindow INSTANCE = new MemberCheckoutRecordWindow();
    private JTextArea textArea;
    private JPanel bottomPanel = new JPanel(new BorderLayout());

    private JSplitPane splitPaneOuter;

    private JPanel memberIDPanel = new JPanel(new BorderLayout());
    private JLabel memberIDLabel = new JLabel("Member ID");
    private JTextField memberIDTextField = new JTextField(10);


    // middle
    private JPanel middleWrapperPanel = new JPanel(new BorderLayout());
    private JPanel middlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    private JButton button = new JButton("Print");
    private JPanel buttonPanel = new JPanel();

    private JPanel mainPanel = new JPanel(new BorderLayout());

    private JLabel headerLabel = new JLabel(title);



    private boolean isInitialized = false;

    private MemberCheckoutRecordWindow() {
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
        setupMemberID();
        setupButton();
        mainPanel.add(middleWrapperPanel,BorderLayout.CENTER);
        middleWrapperPanel.add(middlePanel,BorderLayout.NORTH);
        setupBackButton();
        setupTextArea();
        setTitle(title);
        setUpVerticalPane();
        getContentPane().add(splitPaneOuter);
        isInitialized(true);

    }


    private void setUpVerticalPane() {
        splitPaneOuter = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                mainPanel,
                bottomPanel);
        splitPaneOuter.setDividerLocation(getHeight()/4);
        add(splitPaneOuter);
    }

    private void setupTextArea() {
        textArea = new JTextArea("Welcome to the Checkout printer!");
        textArea.setMaximumSize(new Dimension(500,10));
        Util.adjustLabelFont(textArea,Util.DARK_BLUE,true);
        bottomPanel.add(textArea,BorderLayout.NORTH);
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
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
        buttonPanel.add(button);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        button.setSize(100,30);
        button.addActionListener((evt) -> {
            try {
                RuleSet rules = RuleSetFactory.getRuleSet(MemberCheckoutRecordWindow.this);
                rules.applyRules(MemberCheckoutRecordWindow.this);
                print(getMemberID());
                clearTextFields();
                JOptionPane.showMessageDialog(this,"Please check the console log");
            }catch (Group2Exception e) {
            	memberInterface. showError(e.getMessage());
            }
        });
        middlePanel.add(buttonPanel);
    }

    private void setupBackButton() {
        JButton backButton = new JButton("<= Back to Main");
        backButton.addActionListener(evt -> {
            LibrarySystem.hideAllWindows();

            LibrarySystem.INSTANCE.setVisible(true);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
        buttonPanel.add(backButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        middleWrapperPanel.add(buttonPanel,BorderLayout.SOUTH);

    }
    private void setupMemberID() {
        memberIDPanel.add(memberIDTextField,BorderLayout.NORTH);
        memberIDPanel.add(memberIDLabel,BorderLayout.SOUTH);
        middlePanel.add(memberIDPanel);
        memberIDLabel.setFont(Util.makeSmallFont(memberIDLabel.getFont()));
    }

    private void clearTextFields() {
        memberIDTextField.setText("");
    }

   

    private void print(String memberID) throws Group2Exception {
        CheckoutRecord record = memberInterface.getRecord(memberID);
        List<CheckoutRecordEntry> list = record.getRecordEntries();

        System.out.println("-------- Checkout Record of " + memberID + "---------");

        StringBuilder builder = new StringBuilder();
        System.out.printf("%s %-20s %-20s %-20s %-20s %n",
                "No.",
                "Book",
                "Copy No. ",
                "Checkout Date",
                "Due Date"
        );

        System.out.println("\n");

        builder.append(String.format("%s %-20s %-20s %-20s %-20s %n","No.",
                "Book",
                "Copy No. ",
                "Checkout Date",
                "Due Date"));
        builder.append("\n");
        for(int i = 0; i< list.size();i++){
            CheckoutRecordEntry recordEntry = list.get(i);
            System.out.printf("%2d. %-20s %-20s %-20s %-20s %n",  i + 1,
                    recordEntry.getBookCopy().getBook().getTitle(),
                    recordEntry.getBookCopy().getCopyNum(),
                    recordEntry.getCheckoutDate().toString(),
                    recordEntry.getDueDate().toString()
            );
            builder.append(String.format("%2d. %-20s %-20s %-20s %-20s %n",  i + 1,
                    recordEntry.getBookCopy().getBook().getTitle(),
                    recordEntry.getBookCopy().getCopyNum(),
                    recordEntry.getCheckoutDate().toString(),
                    recordEntry.getDueDate().toString()));
        }
        textArea.setText(builder.toString());
        textArea.repaint();
    }

    public String getMemberID () {
        return memberIDTextField.getText().trim();
    }

}
