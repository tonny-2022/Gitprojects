package librarysystem;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import business.SystemController;

public class LibrarySystem extends JFrame implements LibWindow {

	public final static LibrarySystem INSTANCE =new LibrarySystem();
	JPanel mainPanel;
	JMenuBar menuBar;
    JMenu memberMenu, bookMenu;

	JMenuItem login, logout;
    JMenuItem allBookIds, allMemberIds, addBookCopy, bookCheckout;
    String pathToImage;
    private boolean isInitialized = false;
    
    private static LibWindow[] allWindows = {
			BookCheckoutWindow.INSTANCE,
			LibrarySystem.INSTANCE,
			LoginWindow.INSTANCE,
			AddMemberWindow.INSTANCE,
			AllMemberWindow.INSTANCE,
			AllBookWindow.INSTANCE,
			AddBookCopyWindow.INSTANCE,
			AddBookWindow.INSTANCE,
			MemberCheckoutRecordWindow.INSTANCE,
			BookDueDateWindow.INSTANCE
	};
    	
	public static void hideAllWindows() {		
		for(LibWindow frame: allWindows) {
			frame.setVisible(false);			
		}
	}
     
    private LibrarySystem() {}
    
    public void init() {
    	formatContentPane();
    	setPathToImage();
    	insertSplashImage();
		
		createMenus();
		//pack();
		setSize(660,630);
		isInitialized = true;
    }
    
    private void formatContentPane() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1,2));
		getContentPane().add(mainPanel);	
	}
    
    private void setPathToImage() {
    	pathToImage = "/resources/library.png";
    	
    }
    
    private void insertSplashImage() {
        ImageIcon image = new ImageIcon(getClass().getResource(pathToImage));
		mainPanel.add(new JLabel(image));
    }
    private void createMenus() {
    	menuBar = new JMenuBar();
		menuBar.setBorder(BorderFactory.createRaisedBevelBorder());
		reload();
		setJMenuBar(menuBar);		
    }
    
    private void addMenuItemsForAdmin() {
		memberMenu = new JMenu("Member");
		menuBar.add(memberMenu);

		allMemberIds = new JMenuItem("All Member");
		allMemberIds.addActionListener(new AllMemberIdsListener());
		memberMenu.add(allMemberIds);

		logout = new JMenuItem("Logout");
		logout.addActionListener(new LogoutActionListener());
		memberMenu.add(logout);

		bookMenu = new JMenu("Book");
		menuBar.add(bookMenu);

		allBookIds = new JMenuItem("All Book");
		allBookIds.addActionListener(new AllBookIdsListener());
		bookMenu.add(allBookIds);

		addBookCopy = new JMenuItem("Add Book Copy");
		addBookCopy.addActionListener(new AddBookCopyActionListener());
		bookMenu.add(addBookCopy);
	}

	private void addMenuItemsForLibrarian() {
		memberMenu = new JMenu("Member");
		menuBar.add(memberMenu);

		JMenuItem memberRecord = new JMenuItem("Member Record");
		memberRecord.addActionListener(new MemberRecordActionListener());
		memberMenu.add(memberRecord);

		logout = new JMenuItem("Logout");
		logout.addActionListener(new LogoutActionListener());
		memberMenu.add(logout);

		bookMenu = new JMenu("Book");
		menuBar.add(bookMenu);

		bookCheckout = new JMenuItem("Book Checkout");
		bookCheckout.addActionListener(new AddBookCheckoutActionListener());
		bookMenu.add(bookCheckout);

		JMenuItem dueDate = new JMenuItem("Book Due Date");
		dueDate.addActionListener(new AddBookDueDateActionListener());
		bookMenu.add(dueDate);

		menuBar.repaint();
	}

	private void addMenuItemsForBoth() {
		memberMenu = new JMenu("Member");
		menuBar.add(memberMenu);

		allMemberIds = new JMenuItem("All Member");
		allMemberIds.addActionListener(new AllMemberIdsListener());
		memberMenu.add(allMemberIds);


		JMenuItem memberRecord = new JMenuItem("Member Record");
		memberRecord.addActionListener(new MemberRecordActionListener());
		memberMenu.add(memberRecord);

		logout = new JMenuItem("Logout");
		logout.addActionListener(new LogoutActionListener());
		memberMenu.add(logout);

		bookMenu = new JMenu("Book");
		menuBar.add(bookMenu);

		allBookIds = new JMenuItem("All Book");
		allBookIds.addActionListener(new AllBookIdsListener());
		bookMenu.add(allBookIds);

		addBookCopy = new JMenuItem("Add Book Copy");
		addBookCopy.addActionListener(new AddBookCopyActionListener());
		bookMenu.add(addBookCopy);

		bookCheckout = new JMenuItem("Book Checkout");
		bookCheckout.addActionListener(new AddBookCheckoutActionListener());
		bookMenu.add(bookCheckout);

		JMenuItem dueDate = new JMenuItem("Book Due Date");
		dueDate.addActionListener(new AddBookDueDateActionListener());
		bookMenu.add(dueDate);
	}

	private void addLogInForUnAuthorizedUser() {
		login = new JMenuItem("Login");
		login.addActionListener(new LoginListener());
		menuBar.add(login);
	}
    
    class LoginListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			LibrarySystem.hideAllWindows();
			LoginWindow.INSTANCE.init();
			Util.centerFrameOnDesktop(LoginWindow.INSTANCE);
			LoginWindow.INSTANCE.setVisible(true);
			
		}
    	
    }

	class MemberRecordActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			LibrarySystem.hideAllWindows();
			MemberCheckoutRecordWindow.INSTANCE.init();
			Util.centerFrameOnDesktop(MemberCheckoutRecordWindow.INSTANCE);
			MemberCheckoutRecordWindow.INSTANCE.setVisible(true);
		}
	}

	class AddMemberActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			LibrarySystem.hideAllWindows();
			AddMemberWindow.INSTANCE.init();
			Util.centerFrameOnDesktop(AddMemberWindow.INSTANCE);
			AddMemberWindow.INSTANCE.setVisible(true);
		}
	}

	class AddBookCheckoutActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			LibrarySystem.hideAllWindows();
			BookCheckoutWindow.INSTANCE.init();
			Util.centerFrameOnDesktop(BookCheckoutWindow.INSTANCE);
			BookCheckoutWindow.INSTANCE.setVisible(true);
		}
	}

	class AddBookDueDateActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			LibrarySystem.hideAllWindows();
			BookDueDateWindow.INSTANCE.init();
			Util.centerFrameOnDesktop(BookDueDateWindow.INSTANCE);
			BookDueDateWindow.INSTANCE.setVisible(true);
		}
	}

	class LogoutActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			LibrarySystem.hideAllWindows();
			Util.centerFrameOnDesktop(LibrarySystem.INSTANCE);
			LibrarySystem.INSTANCE.setVisible(true);
			SystemController.currentAuth = null;
			reload();
		}
	}

	class AddBookCopyActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			LibrarySystem.hideAllWindows();
			AddBookCopyWindow.INSTANCE.init();
			Util.centerFrameOnDesktop(AddBookCopyWindow.INSTANCE);
			AddBookCopyWindow.INSTANCE.setVisible(true);
		}

	}
    class AllBookIdsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			LibrarySystem.hideAllWindows();
			AllBookWindow.INSTANCE.init();
			AllBookWindow.INSTANCE.pack();
			Util.centerFrameOnDesktop(AllBookWindow.INSTANCE);
			AllBookWindow.INSTANCE.setVisible(true);
			
		}
    	
    }
    
    class AllMemberIdsListener implements ActionListener {

    	@Override
		public void actionPerformed(ActionEvent e) {
			LibrarySystem.hideAllWindows();
			AllMemberWindow.INSTANCE.init();
			AllMemberWindow.INSTANCE.pack();
			AllMemberWindow.INSTANCE.setVisible(true);
			Util.centerFrameOnDesktop(AllMemberWindow.INSTANCE);

		}
    	
    }

	public void reload() {
		menuBar.removeAll();
		if (SystemController.currentAuth == null) {
			addLogInForUnAuthorizedUser();
			setJMenuBar(menuBar);
			return;
		}
		switch (SystemController.currentAuth) {
			case BOTH -> {addMenuItemsForBoth();}
			case ADMIN -> {addMenuItemsForAdmin();}
			case LIBRARIAN -> {addMenuItemsForLibrarian();}
		}
		setJMenuBar(menuBar);
	}

	@Override
	public boolean isInitialized() {
		return isInitialized;
	}


	@Override
	public void isInitialized(boolean val) {
		isInitialized =val;
		
	}
    
}
