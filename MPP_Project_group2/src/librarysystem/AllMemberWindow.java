package librarysystem;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import business.LibraryMember;
import business.ControllerInterface;
import business.SystemController;

public class AllMemberWindow extends JFrame implements LibWindow {
	public static final AllMemberWindow INSTANCE = new AllMemberWindow();
    ControllerInterface ci = new SystemController();

	ControllerInterface memberInterface = new SystemController();
	private boolean isInitialized = false;
	public JPanel getMainPanel() {
		return mainPanel;
	}
	private JPanel mainPanel;
	private JPanel topPanel;
	private JPanel middlePanel;
	private JPanel lowerPanel;
	private JTable table;

	private JButton addMemberButton;

	private DefaultTableModel model;
	
	private AllMemberWindow() {}
	
	public void init() {
		if(isInitialized){
			return;
		}
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		defineTopPanel();
		defineMiddlePanel();
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(middlePanel, BorderLayout.CENTER);	
		//mainPanel.add(lowerPanel, BorderLayout.SOUTH);
		getContentPane().add(mainPanel);
		isInitialized = true;
	}
	
	public void defineTopPanel() {
		topPanel = new JPanel();
		JLabel AllIDsLabel = new JLabel("All Member");
		Util.adjustLabelFont(AllIDsLabel, Util.DARK_BLUE, true);
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		topPanel.add(AllIDsLabel);

	}
	
	public void defineMiddlePanel() {
		middlePanel = new JPanel(new BorderLayout());

		JPanel featurePanel = new JPanel(new BorderLayout());

		addMemberButton = new JButton("Add Member");
		featurePanel.add(addMemberButton, BorderLayout.EAST);
		addMemberButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LibrarySystem.hideAllWindows();
				AddMemberWindow.INSTANCE.init();
				Util.centerFrameOnDesktop(AddMemberWindow.INSTANCE);
				AddMemberWindow.INSTANCE.setVisible(true);
			}
		});

		JButton backButton = new JButton("<== Back to Main");
		addBackButtonListener(backButton);
		featurePanel.add(backButton, BorderLayout.WEST);

		middlePanel.add(featurePanel, BorderLayout.NORTH);

		List<LibraryMember> data = getAllLibraryMember();
		String[] columnNames = new String[]{"Member ID","First Name","Last Name","Tel","Street","City","Zipcode","State", "Records"};

		String[][] dataTable = new String[data.size()][8];

		for(int i = 0; i< data.size();i++){
			LibraryMember lm = data.get(i);
			dataTable[i] = new String[]{
					lm.getMemberId(),
					lm.getFirstName(),
					lm.getLastName(),
					lm.getTelephone(),
					lm.getAddress().getStreet(),
					lm.getAddress().getCity(),
					lm.getAddress().getZip(),
					lm.getAddress().getState(),
					String.valueOf(lm.getCheckoutRecord().getRecordEntries().size())
			};

			System.out.println(lm.getCheckoutRecord());
		}

		model = new DefaultTableModel(dataTable, columnNames){
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
		middlePanel.add(new JScrollPane(table), BorderLayout.CENTER);
		
	}

	private List<LibraryMember> getAllLibraryMember()
	{
		return memberInterface.getAllLibraryMember();
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

	public void refreshTable(LibraryMember lm){
		model.addRow(new String[]{
					lm.getMemberId(),
					lm.getFirstName(),
					lm.getLastName(),
					lm.getTelephone(),
					lm.getAddress().getStreet(),
					lm.getAddress().getCity(),
					lm.getAddress().getZip(),
					lm.getAddress().getState(),
					String.valueOf(lm.getCheckoutRecord().getRecordEntries().size())
		});
		reloadUI();
	}
	
}


