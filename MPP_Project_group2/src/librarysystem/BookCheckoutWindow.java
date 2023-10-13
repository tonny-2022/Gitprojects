package librarysystem;

import business.*;
import business.SystemController;
import business.ControllerInterface;
import librarysystem.rulesets.RuleSet;
import librarysystem.rulesets.RuleSetFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class BookCheckoutWindow extends JFrame implements LibWindow {

    private final ControllerInterface bookInterface = new SystemController();
    private final ControllerInterface memberInterface = new SystemController();

    public static String title = "Book Checkout";

    public static final BookCheckoutWindow INSTANCE = new BookCheckoutWindow();
    private JTextArea textArea;
    private JPanel bottomPanel = new JPanel(new BorderLayout());

    private JSplitPane splitPaneOuter;

    private JPanel memberIDPanel = new JPanel(new BorderLayout());
    private JLabel memberIDLabel = new JLabel("memberID");
    private JTextField memberIDTextField = new JTextField(10);

    private JPanel isbnPanel = new JPanel(new BorderLayout());
    private JLabel isbnLabel = new JLabel("ISBN");
    private JTextField isbnTextField = new JTextField(10);

    // middle
    private JPanel middleWrapperPanel = new JPanel(new BorderLayout());
    private JPanel middlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JPanel lowerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    private JButton button = new JButton("Checkout");
    private JPanel buttonPanel = new JPanel();

    private JPanel mainPanel = new JPanel(new BorderLayout());

    private JLabel headerLabel = new JLabel(title);


    private JTable table;

    private boolean isInitialized = false;

    private BookCheckoutWindow() {
    }

    private String prevMemberID;
    private DefaultTableModel model;

    @Override
    public void init() {
        if (isInitialized) {
            return;
        }
        setSize(450,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        updateHeaderLabel();
        setupMemberID();
        setupISBN();
        setupButton();
        mainPanel.add(middleWrapperPanel,BorderLayout.CENTER);
        middleWrapperPanel.add(middlePanel,BorderLayout.NORTH);
        middleWrapperPanel.add(lowerPanel,BorderLayout.CENTER);
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
        splitPaneOuter.setDividerLocation(getHeight()*3/4);
        add(splitPaneOuter);
    }

    private void setupTextArea() {
        textArea = new JTextArea("Welcome to Book Checkout!");
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
            String memberID = getMemberID();
            try {
                RuleSet rules = RuleSetFactory.getRuleSet(BookCheckoutWindow.this);
                rules.applyRules(BookCheckoutWindow.this);
                Book book = bookInterface.checkout(memberID,getISBN());
                AllBookWindow.INSTANCE.updateAvailableCountRecord(book);
            } catch (Group2Exception e) {
            	bookInterface.showError(e.getMessage());
                return;
            }
            bookInterface.showInfo("");
            clearTextFields();
            JOptionPane.showMessageDialog(this,"Successful checkout " + getISBN());
            reloadTable(memberID);
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

    private void setupISBN() {
        isbnPanel.add(isbnTextField,BorderLayout.NORTH);
        isbnPanel.add(isbnLabel,BorderLayout.SOUTH);
        middlePanel.add(isbnPanel);
        isbnLabel.setFont(Util.makeSmallFont(isbnLabel.getFont()));
    }

    private void clearTextFields() {
        memberIDTextField.setText("");
        isbnTextField.setText("");
    }

    

    private void reloadTable(String memberID) {
        try {
            CheckoutRecord record = memberInterface.getRecord(memberID);
            List<CheckoutRecordEntry> list = record.getRecordEntries();

            if (table == null) {
                String[] columnNames = new String[]{"Book","Copy No.","Checkout Date","Due Date"};
                String[][] dataTable = new String[list.size()][4];
                for(int i = 0; i< list.size();i++){
                    CheckoutRecordEntry recordEntry = list.get(i);
                    dataTable[i] = new String[]{
                            recordEntry.getBookCopy().getBook().getTitle(),
                            String.valueOf(recordEntry.getBookCopy().getCopyNum()),
                            recordEntry.getCheckoutDate().toString(),
                            recordEntry.getDueDate().toString()
                    };
                }
                this.model = new DefaultTableModel(dataTable, columnNames){
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                table = new JTable(model);
                table.setFillsViewportHeight(true);
                lowerPanel.add(new JScrollPane(table));
                this.prevMemberID = memberID;
            } else {
                if (!Objects.equals(memberID, prevMemberID)) {
                    while (model.getRowCount() > 0) {
                        model.removeRow(model.getRowCount()-1);
                    }

                    for(int i = 0; i< list.size();i++){
                        CheckoutRecordEntry recordEntry = list.get(i);
                        model.addRow( new String[]{
                                recordEntry.getBookCopy().getBook().getTitle(),
                                String.valueOf(recordEntry.getBookCopy().getCopyNum()),
                                recordEntry.getCheckoutDate().toString(),
                                recordEntry.getDueDate().toString()
                        });
                    }
                } else {
                    CheckoutRecordEntry last = list.get(list.size()-1);
                    String[] row = new String[]{
                            last.getBookCopy().getBook().getTitle(),
                            String.valueOf(last.getBookCopy().getCopyNum()),
                            last.getCheckoutDate().toString(),
                            last.getDueDate().toString()
                    };
                    model.addRow(row);
                }
            }
        }
        catch (Group2Exception e) {
        	bookInterface.showError(e.getMessage());
        }

        reloadUI();
    }

    private void reloadUI() {
        LibrarySystem.hideAllWindows();
        Util.centerFrameOnDesktop(BookCheckoutWindow.INSTANCE);
        BookCheckoutWindow.INSTANCE.setVisible(true);
        model.fireTableDataChanged();
        table.repaint();
        table.updateUI();
        repaint();
    }

    public String getMemberID() {
        return memberIDTextField.getText();
    }

    public String getISBN() {
        return isbnTextField.getText();
    }
}
