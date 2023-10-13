package tables;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class TableExample extends JFrame {
	private JPanel topPanel; //panel containing table
	private JPanel middlePanel;
	private JTable table;
	private JScrollPane scrollPane;
	private CustomTableModel model;
	private JButton button;
	
	//table data and config
	private final String[] DEFAULT_COLUMN_HEADERS 
	   = {"First Name", "Last Name", "Salary"};
	private static final int FRAME_WIDTH = 640;
	private static final int FRAME_HEIGHT = 480;
	private static final int TABLE_WIDTH = (int) (0.75 * FRAME_WIDTH);
    private static final int DEFAULT_TABLE_HEIGHT = (int) (0.75 * FRAME_HEIGHT);
    
    //these numbers specify relative widths of the columns -- 
    //they  must add up to 1
    private final float [] COL_WIDTH_PROPORTIONS =
    	{0.35f, 0.35f, 0.3f};//{.75f, .25f}
    
	public TableExample() {
		initializeWindow();
		JPanel mainPanel = new JPanel();
		defineTopPanel();
		defineMiddlePanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(middlePanel, BorderLayout.CENTER);
		getContentPane().add(mainPanel);		
	}
	private void initializeWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Table Example");
		centerFrameOnDesktop(this);
		setSize(FRAME_WIDTH,FRAME_HEIGHT); 
		setResizable(false);
	}
	
	public static void centerFrameOnDesktop(Component f) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int height = toolkit.getScreenSize().height;
        int width  = toolkit.getScreenSize().width;
        int frameHeight = FRAME_HEIGHT;
        int frameWidth  = FRAME_WIDTH;
        f.setLocation(((width-frameWidth)/2),(height-frameHeight)/2);    
    }

	private void defineTopPanel() {
		topPanel = new JPanel();
		createTableAndTablePane();
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		topPanel.add(scrollPane);		
	}
	private void defineMiddlePanel(){
		middlePanel=new JPanel();
		middlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		button = new JButton("Insert Data");
		middlePanel.add(button);
		button.addActionListener(new ButtonListener());
	}
	
	private void createTableAndTablePane() {
		updateModel(); 
		table = new JTable(model);
		createCustomColumns(table, TABLE_WIDTH,
	            COL_WIDTH_PROPORTIONS, DEFAULT_COLUMN_HEADERS);
		scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(
				new Dimension(TABLE_WIDTH, DEFAULT_TABLE_HEIGHT));
		scrollPane.getViewport().add(table);
	}

	private void updateModel() {
		List<String[]> list = new ArrayList<String[]>();
		if(model == null) {
			model = new CustomTableModel();
		}
		model.setTableValues(list);
	}
	
	private void createCustomColumns(JTable table, int width, float[] proportions,
		  String[] headers) {
		table.setAutoCreateColumnsFromModel(false);
        int num = headers.length;
        for(int i = 0; i < num; ++i) {
            TableColumn column = new TableColumn(i);
            column.setHeaderValue(headers[i]);
            column.setMinWidth(Math.round(proportions[i]*width));
            table.addColumn(column);
        }
	}
	
	class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			setValues(model);
			table.updateUI();			
		}
	}
	private void setValues(CustomTableModel model) {
		String[] row0 = {"Jim", "Jones", "100,000"};
		String[] row1 = {"Anne", "Rand", "128,000"};
		String[] row2 = {"Osama", "Bacchus", "110,000"};
		List<String[]> data = new ArrayList<>();
		data.add(row0);
		data.add(row1);
		data.add(row2);
		model.setTableValues(data);	
	}
	
	public static void main(String[] args) {		
		EventQueue.invokeLater(new Runnable()
		{
			public void run() {
				TableExample mf = new TableExample();
				mf.setVisible(true);
			}
		});
	}
	
	private static final long serialVersionUID = 3618976789175941431L;
}
