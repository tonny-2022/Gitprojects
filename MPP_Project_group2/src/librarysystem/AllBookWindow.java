package librarysystem;

import business.Book;
import business.Group2Exception;
import business.SystemController;
import business.ControllerInterface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;


public class AllBookWindow extends JFrame implements LibWindow {
	public static final AllBookWindow INSTANCE = new AllBookWindow();
    ControllerInterface bi = new SystemController();
	private boolean isInitialized = false;
	public JPanel getMainPanel() {
		return mainPanel;
	}
	private JPanel mainPanel;
	private JPanel topPanel;
	private JPanel middlePanel;
	private JPanel lowerPanel;
	private JTable table;

	private JButton addBookButton;

	private DefaultTableModel model;

	private AllBookWindow() {}
	
	public void init() {
		if(isInitialized){
			return;
		}
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		defineTopPanel();
		defineMiddlePanel();
		defineLowerPanel();
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(middlePanel, BorderLayout.CENTER);	
		mainPanel.add(lowerPanel, BorderLayout.SOUTH);
		getContentPane().add(mainPanel);
		isInitialized = true;
	}
	
	public void defineTopPanel() {
		topPanel = new JPanel();
		JLabel AllIDsLabel = new JLabel("All Book");
		Util.adjustLabelFont(AllIDsLabel, Util.DARK_BLUE, true);
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		topPanel.add(AllIDsLabel);

	}

	public void defineMiddlePanel() {
		middlePanel = new JPanel(new BorderLayout());

		JPanel featurePanel = new JPanel(new BorderLayout());

		addBookButton = new JButton("Add Book");
		featurePanel.add(addBookButton, BorderLayout.EAST);
		addBookButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LibrarySystem.hideAllWindows();
				AddBookWindow.INSTANCE.init();
				AddBookWindow.INSTANCE.pack();
				AddBookWindow.INSTANCE.setVisible(true);
				Util.centerFrameOnDesktop(AddBookWindow.INSTANCE);
			}
		});

		JButton backButton = new JButton("<== Back to Main");
		addBackButtonListener(backButton);
		featurePanel.add(backButton, BorderLayout.WEST);

		middlePanel.add(featurePanel, BorderLayout.NORTH);

		List<Book> data = getAllBook();
		String[] columnNames = new String[]{"ISBN","Title","Max Checkout Length", "Available Count"};

		String[][] dataTable = new String[data.size()][5];

		for(int i = 0; i< data.size();i++){
			Book lm = data.get(i);

			dataTable[i] = new String[]{lm.getIsbn(), lm.getTitle(), String.valueOf(lm.getMaxCheckoutLength()), String.valueOf(lm.availableCount())};

		}
		model = new DefaultTableModel(dataTable, columnNames){
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2) {     // to detect doble click events
					JTable target = (JTable)me.getSource();
					int row = target.getSelectedRow(); // select a row
					TableModel tm = table.getModel();
					String isbn = tm.getValueAt(row,0).toString();

					EventQueue.invokeLater(() ->
					{
						BookDetailWindow bookDetailWindow = new BookDetailWindow();
						bookDetailWindow.setTitle("Prius Library System 1.0.0");
						try {
							bookDetailWindow.setBook(isbn);
						} catch (Group2Exception e) {
							throw new RuntimeException(e);
						}
						bookDetailWindow.init();
						bookDetailWindow.setVisible(true);
					});
				}
			}
		});

		middlePanel.add(new JScrollPane(table), BorderLayout.CENTER);
		
	}

	public void refreshTable(Book newBook){
		model.addRow(new String[]{newBook.getIsbn(), newBook.getTitle(), String.valueOf(newBook.getMaxCheckoutLength()), String.valueOf(newBook.availableCount())});
		reloadUI();
	}

	public void defineLowerPanel() {
		lowerPanel = new JPanel();
	}

	private List<Book> getAllBook()
	{
		return bi.getAllBook();
	}

	private void addBackButtonListener(JButton butn) {
		butn.addActionListener(evt -> {
		   LibrarySystem.hideAllWindows();
		   LibrarySystem.INSTANCE.setVisible(true);
	    });
	}

	@Override
	public boolean isInitialized() {
		
		return isInitialized;
	}

	@Override
	public void isInitialized(boolean val) {
		isInitialized = val;
		
	}

	private void reloadUI() {
		model.fireTableDataChanged();
		table.repaint();
		table.updateUI();
		repaint();
	}

	public void updateAvailableCountRecord(Book book){
		for (int i = 0; i < model.getRowCount(); i++){
			String isbn = model.getValueAt(i,0).toString();
			if(book.getIsbn().equals(isbn)){
				model.setValueAt(book.availableCount(),i,3);
				reloadUI();
				return;
			}
		}
	}
}


