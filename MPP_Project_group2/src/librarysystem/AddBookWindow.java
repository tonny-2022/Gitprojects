package librarysystem;

import business.*;
import business.SystemController;
import business.ControllerInterface;
import librarysystem.rulesets.RuleSet;
import librarysystem.rulesets.RuleSetFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AddBookWindow extends JFrame implements LibWindow {
    public static final AddBookWindow INSTANCE = new AddBookWindow();

    public static String title = "Add Book";

    public ControllerInterface bookInterface = new SystemController();

    public ControllerInterface authorInterface = new SystemController();
    private JLabel headerLabel = new JLabel(title);

    private JPanel leftAlignPanel = new JPanel();
    private JPanel rightAlignPanel = new JPanel();
    private JPanel dataTablePanel = new JPanel();

    // ISBN
    private JLabel ISBNLabel = new JLabel("ISBN");
    private JTextField ISBNField = new JTextField(10);

    // Title
    private JLabel titleLabel = new JLabel("Title");
    private JTextField titleTextField = new JTextField(10);

    // Max check out length
    private JLabel maxCheckOutLabel = new JLabel("Max Check Out Length");
    private JTextField maxCheckOutTextField = new JTextField(10);

    // middle
    private JPanel middlePanel = new JPanel(new FlowLayout());
    private JPanel middleWrapperPanel = new JPanel(new BorderLayout());

    private JTextArea textArea;
    private JPanel bottomPanel = new JPanel(new FlowLayout());
    private JPanel mainPanel = new JPanel(new BorderLayout());
    private boolean isInitialized = false;

    private JButton addBookButton;

    private JTable authorTable;
    private Book book;

    private JLabel numberOfCopiesLabel = new JLabel("Number of copies");;

    private JTextField numberOfCopiesTextField = new JTextField(10);

    private JSplitPane splitPaneOuter;

    private JPanel bottomPPanel = new JPanel(new BorderLayout());

    private List<Author> authors;

    DefaultTableModel modelAuthor;
    protected AddBookWindow() {
    }

    @Override
    public void init() {
        if(isInitialized){
            return;
        }

        book = new Book("","",0,new ArrayList<Author>());
        setSize(600,800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        updateHeaderLabel();
        setupLeftAlignPanel();
        setupRightAlignPanel();
        defineMiddleTableDataPanel();
        setupTextArea();
        mainPanel.add(middleWrapperPanel, BorderLayout.CENTER);
        middleWrapperPanel.add(middlePanel, BorderLayout.NORTH);
        middleWrapperPanel.add(dataTablePanel, BorderLayout.CENTER);
        middleWrapperPanel.add(bottomPanel, BorderLayout.SOUTH);
        setTitle(title);
        isInitialized(true);
        add(mainPanel);
        setUpVerticalPane();
        getContentPane().add(splitPaneOuter);
    }

    private void setUpVerticalPane() {
        splitPaneOuter = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                mainPanel,
                bottomPPanel);
        splitPaneOuter.setDividerLocation(getHeight()*3/4);
        add(splitPaneOuter);
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

    private void setupLeftAlignPanel() {
        leftAlignPanel.setLayout(new BoxLayout(leftAlignPanel,BoxLayout.Y_AXIS));

        Component[] items = new Component[]{
                ISBNLabel,
                titleLabel,
                maxCheckOutLabel,
                numberOfCopiesLabel
        };

        for (Component item: items) {
            leftAlignPanel.add(item);
            leftAlignPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        }

        middlePanel.add(leftAlignPanel);
    }

    private void setupRightAlignPanel() {
        rightAlignPanel.setLayout(new BoxLayout(rightAlignPanel,BoxLayout.Y_AXIS));

        numberOfCopiesTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                String value = numberOfCopiesTextField.getText();
                int l = value.length();
                if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') {
                    numberOfCopiesTextField.setEditable(true);
                } else {
                    numberOfCopiesTextField.setEditable(false);
                }
            }
        });

        maxCheckOutTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                String value = numberOfCopiesTextField.getText();
                int l = value.length();
                if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') {
                    maxCheckOutTextField.setEditable(true);
                } else {
                    maxCheckOutTextField.setEditable(false);
                }
            }
        });

        Component[] items = new Component[]{
                ISBNField,
                titleTextField,
                maxCheckOutTextField,
                numberOfCopiesTextField
        };
        for (Component item: items) {
            rightAlignPanel.add(item);
            rightAlignPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        middlePanel.add(rightAlignPanel);
    }

    public void defineMiddleTableDataPanel() {
        dataTablePanel = new JPanel(new BorderLayout());

        JPanel featurePanel = new JPanel();
        featurePanel.setLayout(new FlowLayout());

        JButton backButton = new JButton("<= Back to List");
        backButton.addActionListener(evt -> {
            LibrarySystem.hideAllWindows();
            AllBookWindow.INSTANCE.init();
            AllBookWindow.INSTANCE.pack();
            AllBookWindow.INSTANCE.setVisible(true);
            Util.centerFrameOnDesktop(AllBookWindow.INSTANCE);
            this.dispose();
        });
        featurePanel.add(backButton);

        addBookButton = new JButton("Save");
        featurePanel.add(addBookButton);
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    RuleSet rules = RuleSetFactory.getRuleSet(AddBookWindow.this);
                    rules.applyRules(AddBookWindow.this);
                    bookInterface.showInfo("");
                } catch (Group2Exception pe) {
                	bookInterface.showError(pe.getMessage());
                }
                List<Author> newAuthors = new ArrayList<>();
                TableModel tm = authorTable.getModel();
                for(int i = 0; i < authors.size();i++)
                {
                    boolean isCheck = Boolean.parseBoolean(tm.getValueAt(i,0).toString());
                    if(isCheck)
                        newAuthors.add(authors.get(i));
                }

                int numberOfCopies = Integer.parseInt(numberOfCopiesTextField.getText());
                if(numberOfCopies > 1)
                    for(int i = 1; i < numberOfCopies; i++)
                        book.addCopy();
                try {
                    Book newBook = bookInterface.addBook(ISBNField.getText(), titleTextField.getText(), Integer.parseInt(maxCheckOutTextField.getText()),
                            newAuthors, List.of(book.getCopies()));
                    AllBookWindow.INSTANCE.refreshTable(newBook);
                    clearTextFields();
                } catch (Group2Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        dataTablePanel.add(featurePanel, BorderLayout.NORTH);

        authors = authorInterface.getAllAuthor();
        Object[] columnNameAuthors = new Object[]{"Is Author?","First Name","Last Name","Tel","Bio","Street","City","Zipcode","State"};

        Object[][] dataTableAuthor = new Object[authors.size()][2];

        for(int i = 0; i< authors.size();i++){
            Author lm = authors.get(i);

            dataTableAuthor[i] = new Object[]{false, lm.getFirstName(), lm.getLastName(), lm.getTelephone(), lm.getBio(),
                    lm.getAddress().getStreet(),lm.getAddress().getCity(),lm.getAddress().getZip(),lm.getAddress().getState()};

        }
        modelAuthor = new DefaultTableModel(dataTableAuthor, columnNameAuthors){
            @Override
            public boolean isCellEditable(int row, int column) {
                if(column == 0)
                    return true;
                return false;
            }
        };
        authorTable = new JTable(modelAuthor){
            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return Boolean.class;
                    default:
                        return String.class;
                }
            }
        };
        dataTablePanel.add(new JScrollPane(authorTable), BorderLayout.SOUTH);
    }

    private void setupTextArea() {
        textArea = new JTextArea("Welcome to the Library System!");
        textArea.setMaximumSize(new Dimension(500,10));
        Util.adjustLabelFont(textArea,Util.DARK_BLUE,true);
        bottomPPanel.add(textArea,BorderLayout.NORTH);
    }

    

    private void clearTextFields() {
        maxCheckOutTextField.setText("");
        ISBNField.setText("");
        titleTextField.setText("");
        numberOfCopiesTextField.setText("");
        book = new Book("","",0,new ArrayList<>());

        for (int i = 0; i < modelAuthor.getRowCount(); i++){
            modelAuthor.setValueAt(false,i,0);
        }
        modelAuthor.fireTableDataChanged();
        authorTable.repaint();
        authorTable.updateUI();
        repaint();
    }

    public String getBookTitle() {
        return titleTextField.getText();
    }

    public String getISBN(){
        return ISBNField.getText();
    }

    public String getMaxCheckOut(){
        return maxCheckOutTextField.getText();
    }

    public String getNumberOfCopies(){
        return numberOfCopiesTextField.getText();
    }
}
