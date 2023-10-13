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
import java.util.Optional;


public class BookDueDateWindow extends JFrame implements LibWindow {

    private final ControllerInterface bookInterface = new SystemController();
    private final ControllerInterface memberInterface = new SystemController();

    public static String title = "Book Overdue Checking";

    public static final BookDueDateWindow INSTANCE = new BookDueDateWindow();
    private JTextArea textArea;
    private JPanel bottomPanel = new JPanel(new BorderLayout());

    private JSplitPane splitPaneOuter;

    private JPanel isbnPanel = new JPanel(new BorderLayout());
    private JLabel isbnLabel = new JLabel("ISBN");
    private JTextField isbnTextField = new JTextField(10);

    // middle
    private JPanel middleWrapperPanel = new JPanel(new BorderLayout());
    private JPanel middlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JPanel lowerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    private JButton button = new JButton("Find");
    private JPanel buttonPanel = new JPanel();

    private JPanel mainPanel = new JPanel(new BorderLayout());

    private JLabel headerLabel = new JLabel(title);


    private JTable table;

    private boolean isInitialized = false;

    private BookDueDateWindow() {
    }

    private String prevISBN;
    private DefaultTableModel model;

    @Override
    public void init() {
        if (isInitialized) {
            return;
        }
        setSize(550,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        updateHeaderLabel();
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
            String isbn = this.isbnTextField.getText();
            try {
                RuleSet rules = RuleSetFactory.getRuleSet(BookDueDateWindow.this);
                rules.applyRules(BookDueDateWindow.this);

               HashMap<BookCopy, LibraryMember> map = bookInterface.find(isbn);
               bookInterface.showInfo("");
                clearTextFields();
                JOptionPane.showMessageDialog(this,"Found " + isbn);
                reloadTable(map,isbn);
            } catch (Group2Exception e) {
            	bookInterface. showError(e.getMessage());
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

    private void setupISBN() {
        isbnPanel.add(isbnTextField,BorderLayout.NORTH);
        isbnPanel.add(isbnLabel,BorderLayout.SOUTH);
        middlePanel.add(isbnPanel);
        isbnLabel.setFont(Util.makeSmallFont(isbnLabel.getFont()));
    }

    private void clearTextFields() {
        isbnTextField.setText("");
    }

    
    private void reloadTable(HashMap<BookCopy, LibraryMember> map, String isbn) {
        System.out.println("map: " + map.size());
        Book book = null;
        for (BookCopy copy: map.keySet()) {
            book = copy.getBook();
            System.out.println("key: " + copy + ", value: " + map.get(copy));
            break;
        }


        if (table == null) {
            String[] columnNames = new String[]{"ISBN", "Title","Copy No.","Member","Due Date"};
            String[][] dataTable = new String[book.getCopies().length][5];
            for(int i = 0; i< book.getCopies().length;i++){
                BookCopy copy =  book.getCopies()[i];
                LibraryMember member = map.get(copy);
                String memberID = member != null ? map.get(copy).getMemberId() : "-";
                String dueDate = "-";
                if (member != null) {
                    Optional<CheckoutRecordEntry> optional = member.getCheckoutRecord().getRecordEntries().stream().filter(x -> x.getBookCopy().equals(copy)).findFirst();
                    CheckoutRecordEntry entry = optional.orElse(null);
                    dueDate = entry != null ? entry.getDueDate().toString() : "-";
                }

                dataTable[i] = new String[]{
                        book.getIsbn(),
                        book.getTitle(),
                        String.valueOf(copy.getCopyNum()),
                        memberID,
                        dueDate
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
        } else {
            while (model.getRowCount() > 0) {
                model.removeRow(model.getRowCount()-1);
            }

            for(int i = 0; i< book.getCopies().length;i++){
                BookCopy copy =  book.getCopies()[i];
                LibraryMember member = map.get(copy);
                String memberID = member != null ? map.get(copy).getMemberId() : "-";
                String dueDate = "-";
                if (member != null) {
                    Optional<CheckoutRecordEntry> optional = member.getCheckoutRecord().getRecordEntries().stream().filter(x -> x.getBookCopy().equals(copy)).findFirst();
                    CheckoutRecordEntry entry = optional.orElse(null);
                    dueDate = entry != null ? entry.getDueDate().toString() : "-";
                }

                model.addRow( new String[]{
                        book.getIsbn(),
                        book.getTitle(),
                        String.valueOf(copy.getCopyNum()),
                        memberID,
                        dueDate
                });
            }
        }
        this.prevISBN = isbn;

        reloadUI();
    }

    private void reloadUI() {
        LibrarySystem.hideAllWindows();
        Util.centerFrameOnDesktop(BookDueDateWindow.INSTANCE);
        BookDueDateWindow.INSTANCE.setVisible(true);
        model.fireTableDataChanged();
        table.repaint();
        table.updateUI();
        repaint();
    }

    public String getISBNString() {
        return isbnTextField.getText().trim();
    }
}
