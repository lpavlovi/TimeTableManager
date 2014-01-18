////////////////////////////////////////////////////////////////////////////////
/*
 * 
 * Class: Window 1 Class
 * Description: Constructs the first window and enables its functionalities
 * 
 * Authors: 
 * 	Troylan Tempra Jr Ed. William Zhao, Holden Ciufo
 * 
 * Date Created: December 6, 2012
 * Date Modified: January 20, 2013
 */
////////////////////////////////////////////////////////////////////////////////

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;


public class Course_Manager_Window1{
	public JFrame frame = new JFrame();//Main Frame
	private JFrame frame2 = new JFrame("Launcher");//Launcher Frame
	public int last_selected_table = -1;//Indicates if sem 1 or sem 2 table is last selected
	private Color backGroundColor;//Current displayed BG color
	private Color foreGroundColor;//Current displayed FG color
	private boolean displayPeriodTotals = false;//determines whether period total table are displayed
	private boolean displayVisOpsPanel = true;//default hide visOps
	private int myRowHeight = 30;//Cell height
	private static int myColumnWidth = 80;//cell width
	public Course_Manager_Window2 window2;//second (right-side) window
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();//uses AWT toolkit
	//////////////////////////////////////////////////////////////////GUI Objects
	//////////////JMenu 
	private JMenuBar menuBar = new JMenuBar();
	/////File Menu
	private JMenu fileMenu = new JMenu("File");

	private JMenuItem nu = new JMenuItem("New");
	private JMenuItem load = new JMenuItem("Load");
	private JMenuItem saveAs = new JMenuItem("Save As");
	public JMenuItem save = new JMenuItem("Save");
	public JMenuItem export = new JMenuItem("Create HTML");
	private JFileChooser jFileChooser1;
	/////View Menu
	private JMenu viewMenu = new JMenu("View");
	public JMenuItem departmentDisplayOptions = new JMenuItem("Show/Hide Column Visibility Options Bar");
	public JMenuItem togglePeriodicTotalTables = new JMenuItem("Show/Hide Period Totals");
	private JMenuItem selectDeptBackColor = new JMenuItem("Select Department Fill Color");
	private JMenuItem selectDeptForeColor = new JMenuItem("Select Department Text Color");
	private JMenuItem generateMasterTimeT = new JMenuItem("Generate Master Time Table Window...");
	private JMenuItem deptSummaries = new JMenuItem("Department Summaries"); 
	private JMenuItem roomSummaries = new JMenuItem("Room Summaries");
	public JMenuItem courseListWindow = new JMenuItem("Open CourseLister");
	private JMenuItem toggleRepatchPreference = new JMenuItem("Toggle Automatic Column Visibility Adjustment [Currently: "+ DataBase.autoRepatchVisibilityIsPreferred +"]");
	/////EditMenu
	private JMenu editMenu = new JMenu("Edit");
	public JMenuItem courseData = new JMenuItem("Course Information");
	//public JMenuItem projectData = new JMenuItem("Project Information");
	public JMenuItem dept_roomsData = new JMenuItem("Department and Room Information");
	public JMenuItem lunchPrefData = new JMenuItem("Lunch Preferences");
	public JMenuItem editFileName = new JMenuItem("Project Name");
	public JMenuItem clearAll = new JMenuItem("Clear All");
	/////Help Menu
	private JMenu helpMenu = new JMenu("Help");
	public JMenuItem userGuidePDF = new JMenuItem("User Guide .pdf");
	public JMenuItem userGuideTXT = new JMenuItem("User Guide .txt");
	public JMenuItem userGuideHTML = new JMenuItem("User Guide .html");
	//////////////TOP - Labels
	private JPanel departmentLabelPanel = new JPanel(new FlowLayout());
	private JLabel departmentLabel = new JLabel("");
	private JLabel sem1_totals = new JLabel
			("Semester 1 Total Placed Courses: " + DataBase.sem1_Total);
	//Shows school-wide total number of courses placed in semester 1
	private JLabel sem2_totals = new JLabel
			("Semester 2 Total Placed Courses: " + DataBase.sem2_Total);
	//Shows school-wide total number of courses placed in semester 2

	//////////////TOP - Spinners
	private JPanel visOpsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5,0));

	//////////////MasterT
	private static JFrame sem_Frames[];
	private static JScrollPane sem_SWTables_SPanes[];
	private static JScrollPane sem_SWPerBalTables_SPanes[];

	private static MasterSemTable[] sem_SWTables;

	private static String[][] sem_SWPerBal_headers;

	private static JTable[] sem_SWPerBalTables;

	private static int highestRowCount = 0;

	private static String[][] sem_SWTable_headers;

	//////////////MIDDLE 1 - SplitPane and Tables
	private UneditableTableModel sem1_table_model = 
			new UneditableTableModel(
					DataBase.sem1_main_table_data, DataBase.sem1_main_table_header);
	public JTable sem1_table = new JTable(sem1_table_model){
		public Component prepareRenderer(
				TableCellRenderer renderer, int row, int col)
		{
			Component c = super.prepareRenderer(renderer, row, col);
			if(getValueAt(row, col) != null & !(getSelectedRow() == row & getSelectedColumn() == col) & col>1)
			{
				c.setBackground(backGroundColor);
				c.setForeground(foreGroundColor);
			}
			else if(getSelectedRow() == row & getSelectedColumn() == col)
			{
				c.setBackground(new Color(
						backGroundColor.getBlue(),
						backGroundColor.getGreen(),
						backGroundColor.getRed()));
				c.setForeground(foreGroundColor);
			}
			else{
				if(row%2 == 0)
					c.setBackground(Color.WHITE);
				else
					c.setBackground(new Color(240, 240, 240));
				c.setForeground(Color.BLACK);
			}
			return c;
		}
		public String getToolTipText(MouseEvent e) {
			String tip = null;
			java.awt.Point p = e.getPoint();
			int rowIndex = rowAtPoint(p);
			int colIndex = columnAtPoint(p);
			int realColumnIndex = convertColumnIndexToModel(colIndex);
			int periodNum = rowIndex/DataBase.schoolGradeCount;
			if (realColumnIndex<2) {//column 0 or 1
				tip = "<html>";
				tip = tip + "<u>Period " + ((int)periodNum+1) +"</u><br>";
				for(int index = 0; index < DataBase.rooms.length; index++)
				{
					tip = tip + DataBase.rooms[index].getType() + ": " + 
							DataBase.rooms[index].numOfRooms()[0][periodNum] + "/"+ 
							DataBase.rooms[index].getOriginalNumOfRooms() + "<br>";
				}
				tip = tip + "</html>";
			}
			return tip;
		}
	};
	private JScrollPane sem1_ScrollPane = new JScrollPane(sem1_table);
	//Creates the greater left side table (semester 1)

	private UneditableTableModel sem2_table_model = new UneditableTableModel(
			DataBase.sem2_main_table_data, DataBase.sem2_main_table_header);
	public JTable sem2_table = new JTable(sem2_table_model){
		public Component prepareRenderer(
				TableCellRenderer renderer, int row, int col)
		{
			Component c = super.prepareRenderer(renderer, row, col);
			if(getValueAt(row, col) != null & !(getSelectedRow() == row & getSelectedColumn() == col) & col>1)
			{
				c.setBackground(backGroundColor);
				c.setForeground(foreGroundColor);
			}
			else if(getSelectedRow() == row & getSelectedColumn() == col)
			{
				c.setBackground(new Color(
						backGroundColor.getBlue(),
						backGroundColor.getGreen(),
						backGroundColor.getRed()));
				c.setForeground(foreGroundColor);
			}
			else{
				if(row%2 == 0)
					c.setBackground(Color.WHITE);
				else
					c.setBackground(new Color(240, 240, 240));
				c.setForeground(Color.BLACK);
			}
			return c;
		}
		public String getToolTipText(MouseEvent e) {
			String tip = null;
			java.awt.Point p = e.getPoint();
			int rowIndex = rowAtPoint(p);
			int colIndex = columnAtPoint(p);
			int realColumnIndex = convertColumnIndexToModel(colIndex);
			int periodNum = rowIndex/DataBase.schoolGradeCount;
			if (realColumnIndex<2) {//column 0 or 1
				tip = "<html>";
				tip = tip + "Period " + ((int)periodNum+1) +"<br>";
				for(int index = 0; index < DataBase.rooms.length; index++)
				{
					tip = tip + DataBase.rooms[index].getType() + ": " + 
							DataBase.rooms[index].numOfRooms()[1][periodNum] + "/"+ 
							DataBase.rooms[index].getOriginalNumOfRooms() + "<br>";
				}
				tip = tip + "</html>";
			}
			return tip;
		}
	};
	private JScrollPane sem2_ScrollPane = new JScrollPane(sem2_table);

	private JSplitPane sem1sem2_SplitPane = new JSplitPane(
			JSplitPane.HORIZONTAL_SPLIT, sem1_ScrollPane, sem2_ScrollPane);
	//Creates the greater right side table (semester 2)

	//////////////MIDDLE 2 - Department Buttons
	private JPanel department_buttons_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,0));
	//Panel to contain buttons denoting different departments

	/////////////BOTTOM - Tables
	private JLabel sem1_Depttotal = new JLabel(
			"Semester 1 Department's Placed Courses: " + DataBase.sem1_DeptTotal);
	//Shows department-wide total number of courses placed in semester 1
	private JLabel sem2_Depttotal = new JLabel(
			"Semester 2 Department's Placed Courses: " + DataBase.sem2_DeptTotal);
	//Shows department-wide total number of courses placed in semester 2

	private UneditableTableModel sem1_period_model = new UneditableTableModel(
			DataBase.sem1_period_table_data, DataBase.sem1_period_totals_header);
	private JTable sem1_period = new JTable(sem1_period_model);
	private JScrollPane sem1_period_ScrollPane = new JScrollPane(sem1_period);
	//Creates lesser left side table

	private UneditableTableModel sem2_period_model = new UneditableTableModel(
			DataBase.sem2_period_table_data, DataBase.sem2_period_totals_header);
	private JTable sem2_period = new JTable(sem2_period_model);
	private JScrollPane sem2_period_ScrollPane = new JScrollPane(sem2_period);
	//creates lesser right side table

	/////////////////////////////////////////////////HTML Printing interface Objects
	private File department;
	private String selectedDepartment;
	private JTextField tField = new JTextField();
	private boolean isDepart = false;
	private boolean isBrowse = false;
	private boolean isFirst = true;

	/////////////////////////////////////////////Anti-editing TableModel Class:
	private class UneditableTableModel extends DefaultTableModel {
		//Used to create an overridden table model where cells cannot be edited
		public UneditableTableModel(Object[][] tableData, Object[] colNames) {
			super(tableData, colNames);
		}
		public boolean isCellEditable(int row, int column) {
			return false;
		}//Override the isCellEditable method
	}

	//Other Variables
	GridBagConstraints gbc = new GridBagConstraints();//GridBagConstraints
	////////////////////////////////////////GBC helper methods
	//Methods are used to change multiple GBC values in one line
	////////////////////////////////////////
	private void setGBCPos(int gridx, int gridy, int gridwidth, int gridheight)
	{//Changes x/y coordinate and size on grid
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.gridwidth = gridwidth;
		gbc.gridheight = gridheight;
	};

	private void setGBCWgt(double weightx, double weighty)
	{//Changes weight prioritization
		gbc.weightx = weightx;
		gbc.weighty = weighty;
	};

	/////////////////////////////////////////////////////////////////Constructor
	public Course_Manager_Window1(){
		initFirstComponent();
	}
	public Course_Manager_Window1(String args){
		initMainComponents();
	}
	public void define(){//This method is use to create the frames
		if(DataBase.isItFirstLoad == true)//check if it is the launcher frame being created
		{
			frame2.dispose();//disposes the welcome frame
			initMainComponents();//opens the main frame with the JTable
		}else//if this is not the first time a frame is being created
		{
			new Course_Manager_Window1("2");//the second contructor is called 
		}
	}
	//Obsolete?
//	private JPanel panel2 = new JPanel();//a second panel is initalized
//	private JLabel label = new JLabel("Select A Project");// a label that will be used in the welcome frame
//	private JButton loadBut = new JButton("Load");
//	private Toolkit toolkit =  Toolkit.getDefaultToolkit ();
//	private Dimension dim = toolkit.getScreenSize();
	protected void initFirstComponent()//this creates the components in the launcher frame
	{
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//close operation is set
		frame2.setBounds(380, 180,640, 395);//set x y coodinates
		JComponent newContentPane = new LayeredPane();
		newContentPane.setOpaque(true); //content panes must be opaque
		frame2.setContentPane(newContentPane);
		//lPane.add(, JLayeredPane.DEFAULT_LAYER);
		frame2.setResizable(false);
		frame2.setVisible(true);
	}
	public class LayeredPane extends JPanel{

		final Image backGroundIcon = new ImageIcon("res/Launcher.png").getImage();
		private JButton load = new JButton("   Load   ");
		private JButton nu = new JButton("   New   ");
		public void paintComponent(Graphics g)
		{
//			String title = "Welcome To";
//			String title2 = "Master Menology Maker";
			g.drawImage(backGroundIcon, 0, 0, null);
			// g.setColor(new Color(255,255,255));
			//g.setFont(new Font("Courier New", Font.BOLD,20));
			// g.drawString(title, 50, 50);
			// g.drawString(title2, 50, 80);
		}
		public LayeredPane(){
			setLayout(new GridBagLayout());
			load.addActionListener(new loadFileChooser());
			nu.addActionListener(new newFileOpener());
			//load.light(new Color());
			setPreferredSize(new Dimension(656, 438));
			// set the weights
			// do you even lift?
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.insets = new Insets(0,0,29,24);
			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.SOUTHEAST;

			add(load, gbc);
			// set the weights
			// do you even lift?
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.insets = new Insets(0,0,29,102);
			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.SOUTHEAST;
			add(nu, gbc);
		}

	}
	protected void initMainComponents()
	{
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0,0,0,0);
		setGBCWgt(0,0);
		frame.setTitle("Master Timetable Maker - " + DataBase.projectName);
		frame.setLayout(new GridBagLayout());
		//setting close operation
		WindowListener exitListener = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
					frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
					int confirm = JOptionPane.showOptionDialog(null, "Do You Want to Exit Program?", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
					if (confirm == 0) {
						System.exit(0);
				}
			}
		};

		frame.addWindowListener(exitListener);
		setGBCPos(0,0,1,1);//Default

		///////////JMenu
		frame.setJMenuBar(menuBar);
		////FileMenu
		menuBar.add(fileMenu);
		menuBar.setBorderPainted(false);
		//		loadTemplate.addActionListener(new loadTemplateListener());//Obsolete?//yeah
		nu.addActionListener(new newFileOpener());
		save.addActionListener(new saveFileChooser());
		save.setEnabled(false);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
		load.addActionListener(new loadFileChooser());
		saveAs.addActionListener(new saveAsFileChooser());
		export.addActionListener(new exportWindow());
		fileMenu.add(nu);
		fileMenu.add(load);
		fileMenu.add(save);
		fileMenu.add(saveAs);
		fileMenu.add(export);
		////ViewMenu
		menuBar.add(viewMenu);
		departmentDisplayOptions.addActionListener(new visOpsToggleListener());
		departmentDisplayOptions.setEnabled(true);
		viewMenu.add(departmentDisplayOptions);
		togglePeriodicTotalTables.addActionListener(new togglePeriodicTotalsListener());
		toggleRepatchPreference.addActionListener(new toggleRepatchPreferenceListener());
		viewMenu.add(toggleRepatchPreference);
		toggleRepatchPreference.setVisible(false);
		toggleRepatchPreference.setEnabled(false);//Feature Under construction
		togglePeriodicTotalTables.setEnabled(true);
		viewMenu.add(togglePeriodicTotalTables);
		selectDeptBackColor.addActionListener(new selectDeptBackColorListener());
		viewMenu.add(selectDeptBackColor);
		selectDeptForeColor.addActionListener(new selectDeptForeColorListener());
		viewMenu.add(selectDeptForeColor);
		viewMenu.add(new JSeparator());
		generateMasterTimeT.addActionListener(new generateMasterTimeTListener());
		viewMenu.add(generateMasterTimeT);
		deptSummaries.addActionListener(new deptSummariesListener());
		viewMenu.add(deptSummaries);
		roomSummaries.addActionListener(new roomSummariesListener());
		viewMenu.add(roomSummaries);
		viewMenu.add(courseListWindow);
		courseListWindow.setEnabled(false);
		courseListWindow.addActionListener(new courseListWindowListener());
		// //editMenu Bar
		menuBar.add(editMenu);
		editMenu.add(courseData);
		editMenu.add(dept_roomsData);
		editMenu.add(lunchPrefData);
		editMenu.add(editFileName);
		editMenu.add(new JSeparator());
		editMenu.add(clearAll);
		//editMenu.add(projectData);
		courseData.addActionListener(new editCourseDataListener());
		dept_roomsData.addActionListener(new editDept_RoomsDataListener());
		lunchPrefData.addActionListener(new editlunchPrefDataListener());
		editFileName.addActionListener(new editFileNameListener());
		clearAll.addActionListener(new clearAllListener());
		//projectData.addActionListener(new editProjectDataListener());
		////helpmenu
		menuBar.add(helpMenu);
		userGuidePDF.addActionListener(new userGuidePdfListener());
		userGuideTXT.addActionListener(new userGuideTxtListener());
		userGuideHTML.addActionListener(new userGuideHTMLListener());
		helpMenu.add(userGuidePDF);
		helpMenu.add(userGuideTXT);
		helpMenu.add(userGuideHTML);
		///////////Sets Top Labels GUI Behaviors
		gbc.fill = GridBagConstraints.HORIZONTAL;//Label fill set to horizontal
		setGBCPos(0,0,2,1);
		generate_Spinners();
		frame.add(visOpsPanel, gbc);//Add Spinners
		setGBCPos(0,1,2,1);
		frame.add(new JSeparator(), gbc);
		visOpsPanel.setVisible(this.displayVisOpsPanel);
		setGBCPos(0,2,2,1);
		this.setGBCWgt(1, 0);
		gbc.anchor = GridBagConstraints.PAGE_START;
		departmentLabelPanel.add(departmentLabel);
		frame.add(departmentLabelPanel, gbc);//Adition of this has caused the y coordinates to shift 2 units downwards
		gbc.anchor = GridBagConstraints.CENTER;
		setGBCPos(0,3,1,1);//Change GBC y Coordinate to 1, x to 0, width 2
		this.setGBCWgt(0, 0);
		frame.add(sem1_totals, gbc);//Placed in row 0, column 0 with width 1, height 1
		setGBCPos(1,3,1,1);
		gbc.gridx = 1;//change GBC x-coordinate to column 1
		frame.add(sem2_totals, gbc);//Placed in row 0, column 2 with width 1, height 1


		///////////Sets Middle SplitPane GUI Behaviors
		////Format Tables
		TableTransferHandler tth = new TableTransferHandler();
		sem1_table.setDragEnabled(true);
		sem1_table.setTransferHandler(tth);//link to transferHandler
		sem1_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sem1_table.getSelectionModel().addListSelectionListener(new rowListener(0));
		sem1_table.getColumnModel().getSelectionModel().addListSelectionListener(new columnListener(0));
		sem1_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		sem1_table.getTableHeader().setReorderingAllowed(false);
		sem2_table.setDragEnabled(true);
		sem2_table.setTransferHandler(tth);//link to transferHandler
		sem2_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sem2_table.getSelectionModel().addListSelectionListener(new rowListener(1));
		sem2_table.getColumnModel().getSelectionModel().addListSelectionListener(new columnListener(1));
		sem2_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		sem1_table.getTableHeader().setReorderingAllowed(false);
		//Allow Horizontal Scrolling
		setMainTableContents("clear");

		sem1sem2_SplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				sem1_ScrollPane, sem2_ScrollPane);
		sem1sem2_SplitPane.setResizeWeight(0.5);
		gbc.fill = GridBagConstraints.BOTH;
		//change fill to both for sem1sem2_SplitPane
		setGBCWgt(1,1);//change weight to prioritize filling empty space w/ this
		setGBCPos(0,4,2,1);//change GBC y-coordinate to row 2, set width to 2
		frame.add(sem1sem2_SplitPane, gbc);
		//Placed in row 2, column 0 with width 2, height 1

		///////////Sets Department Buttons panel GUI Behaviors
		setGBCPos(0,5,2,1);//change GBC y-coordinate to row 3
		setGBCWgt(1,0);//disallow taking extra vertical space
		generate_DeptButtons();//Adds department buttons based on DataBase
		JScrollPane departmentsScroller = new JScrollPane(
				department_buttons_panel);
		departmentsScroller.setMinimumSize(new Dimension(0,45));
		frame.add(departmentsScroller, gbc);
		//Placed in row 3, column 0 with width 2, height 1

		///////////Sets Department-Wide Semester Total Labels GUI Behaviors
		setGBCPos(0,6,1,1);//change GBC y-coordinate to row 4, width to 1
		frame.add(sem1_Depttotal, gbc);
		//Placed in row 4, column 0 with width 1, height 1
		setGBCPos(1,6,1,1);//change GBC x-coordinate to column 2
		frame.add(sem2_Depttotal, gbc);
		//Placed in row 4, column 0 with width 1, height 1

		///////////Sets Bottom Period Tables GUI Behaviors
		gbc.fill = GridBagConstraints.HORIZONTAL;//Disables Vertical Fill
		setGBCPos(0,7,1,1);//change GBC y-coordinate to row 5
		sem1_period_ScrollPane.setMinimumSize(new Dimension(50,108));
		frame.add(sem1_period_ScrollPane, gbc);
		//Placed in row 5, column 0 with width 1, height 1
		setGBCPos(1,7,1,1);//change GBC x-coordinate to column 2
		sem2_period_ScrollPane.setMinimumSize(new Dimension(50,108));
		frame.add(sem2_period_ScrollPane, gbc);
		//Placed in row 5, column 0 with width 1, height 1
		sem1_period_ScrollPane.setVisible(false);
		sem2_period_ScrollPane.setVisible(false);

		frame.pack();//Packs contents
		frame.setBounds(0,0,screenSize.width-300, screenSize.height-40);
		//^Sets location to far left and leaves space for window 2
		frame.setVisible(true);
		//Open Window 2
		window2 = new Course_Manager_Window2();
		window2.setVisible(true);
		//window2.setAlwaysOnTop(true);

		setMainTableContents("");
	}
	class LevelSpinner extends JSpinner
	{//Custom class of JSpinner that has the method isLevelSpinner to identify itself
		private static final long serialVersionUID = -1930338295963046279L;
		public LevelSpinner(SpinnerModel model)
		{
			super(model);
		}
		public boolean isLevelSpinner()
		{
			return true;
		}
	}
	private JSpinner createLevelSpinner(String level, SpinnerModel model)
	{//Creates a Labeled Level Spinner
		LevelSpinner levelSpinner = new LevelSpinner(model);
		JLabel spinnerLabel = new JLabel(level);
		visOpsPanel.add(spinnerLabel);
		spinnerLabel.setLabelFor(levelSpinner);
		levelSpinner.setEditor(new JSpinner.DefaultEditor(levelSpinner));
		levelSpinner.addChangeListener(new colVisibilityChangeListener(
				level.trim().substring(0,level.trim().length()-1)));
		//Extracts^ level name and passes it to listener constructor
		levelSpinner.setValue("2");//set default display to 2
		//Increase width
		((JSpinner.DefaultEditor)levelSpinner.getEditor()).setPreferredSize( new Dimension (50, ((JSpinner.DefaultEditor)levelSpinner.getEditor()).getPreferredSize().height));
		return levelSpinner;
	}
	public void generate_Spinners()
	{//Generates spinners according to Database.levels[]
		 visOpsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5,0));
		for (int index = 0; index<DataBase.levels.length; index++)
		{
			String[] spinnerRange = new String[DataBase.colCountPerLevel];
			for (int j = 0; j < spinnerRange.length; j++) {
				spinnerRange[j] = Integer.toString(j);
			}
			visOpsPanel.add(
					createLevelSpinner(DataBase.levels[index] + ": 	", 
							new SpinnerListModel(spinnerRange)));
		}
	}


	public void setMainTableContents(String mode)
	{//Sets and formats table according to DataBase data
		if(mode.equals("clear"))
		{//if clears data
			DataBase.resetMain_table_headers();
			DataBase.resetMain_table_data();
			DataBase.resetPeriod_totals_headers();
			DataBase.resetPeriod_totals_data();
		}
		else
		{
			//Update tables data to DataBase data
			sem1_table.setModel(new UneditableTableModel(
					DataBase.sem1_main_table_data, DataBase.sem1_main_table_header));
			sem2_table.setModel(new UneditableTableModel(
					DataBase.sem2_main_table_data, DataBase.sem2_main_table_header));

			//Format Rows
			for(int index = 0; index<DataBase.sem1_main_table_data.length; index ++)
			{//Set all row heights
				sem1_table.setRowHeight(index, myRowHeight);
				sem2_table.setRowHeight(index, myRowHeight);
			}//TODO: Create adjustable visibility

			//Format Columns
			for(int index = 0; index<DataBase.sem1_main_table_header.length; index ++)
			{
				if(index<2)
				{//columns 0 and 1 set to 40 width and unresizable
					sem1_table.getColumnModel().getColumn(index).setPreferredWidth(25);
					sem2_table.getColumnModel().getColumn(index).setPreferredWidth(25);
					sem1_table.getColumnModel().getColumn(index).setResizable(false);
					sem1_table.getColumnModel().getColumn(index).setResizable(false);
				}
				else
				{//columns 2+ set to 80, resizable
					sem1_table.getColumnModel().getColumn(index).setPreferredWidth(myColumnWidth);
					sem2_table.getColumnModel().getColumn(index).setPreferredWidth(myColumnWidth);
				}	
			}
			System.out.println("setMainTableContents() was called without asking for clear...");
		}

	}

	public JButton createDeptButton(String department)
	{//Creates a department button with attached listener
		System.out.println("Creating button for: " + department);
		JButton deptButton = new JButton(department);
		deptButton.setBackground(DataBase.getDepartment(department).backGroundColor);
		deptButton.setForeground(DataBase.getDepartment(department).foreGroundColor);
		deptButton.addActionListener(new deptButtonListener());
		return deptButton;
	}

	int generate_counter = 0;
	public void generate_DeptButtons()
	{//Generates the department buttons according to Database.departments[]
		generate_counter++;
		System.out.println("**************GENERATE IS RUNNING: " + generate_counter);
		department_buttons_panel.removeAll();
		System.out.println("Generating deptButtons");
		for (int index = 0; index<DataBase.departments.length; index++)
		{
			department_buttons_panel.add(
					createDeptButton(DataBase.departments[index].getName()));
		}	
	}
	public void packAgain()
	{//method to rerun pack
		frame.pack();
		frame.setBounds(0,0,screenSize.width-300, screenSize.height-40);
	}
	//////////////////////////////////////////////////////////////////Listeners

	///////////////////////////////////////////////////JTable Listeners//====//
	class rowListener implements ListSelectionListener {
		private int semesterNum = -1;
		public rowListener(int semesterNum)
		{
			this.semesterNum = semesterNum;
		}
		public void valueChanged(ListSelectionEvent e) {
			// Ignore extra messages.
			if (e.getValueIsAdjusting())
				return;

			ListSelectionModel lsm = (ListSelectionModel) e.getSource();
			if (lsm.isSelectionEmpty()) {
				System.out.println("No rows are selected.");
			} else {
				DataBase.selectedRow = lsm.getMinSelectionIndex();
				System.out.println("Row " + DataBase.selectedRow + " is now selected for Sem " + semesterNum);
			}
			last_selected_table = semesterNum;//sets last_selected_table 
			//to indicate selected semester table
		}
	}
	class columnListener implements ListSelectionListener {
		private int semesterNum = -1;
		public columnListener(int semesterNum)
		{
			this.semesterNum = semesterNum;
		}
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting())
				return;
			ListSelectionModel lsm = (ListSelectionModel) e.getSource();
			if (lsm.isSelectionEmpty()) {
				System.out.println("No columns are selected.");
			} else {
				DataBase.selectedCol = lsm.getMinSelectionIndex();
				System.out.println("Column " + DataBase.selectedCol + " is now selected for Sem " + semesterNum);
			}
			last_selected_table = semesterNum;
		}
	}

	///////////////////////////////////////////////////JMenu Listeners//=====//
	/////////////////////////////////////////FileMenu Listeners//=====//=====//
	class TextFilter extends FileFilter {//text filter is a method that sets the filter for files in the file chooser

		public boolean accept(File f) {//returns boolean for whether file is accepted or not
			if (f.isDirectory())
				return true;
			String s = f.getName();
			int i = s.lastIndexOf('.');

			if (i > 0 && i < s.length() - 1)
				if (s.substring(i + 1).toLowerCase().equals("mtm"))//checks extension
					return true;

			return false;
		}

		public String getDescription() {//only allows files with .mtm extension
			return ".mtm";
		}
	}

	@SuppressWarnings("serial")
	class loadFileChooser extends JFrame implements ActionListener {//load actionlistener class
		public void actionPerformed(ActionEvent ee) {
			jFileChooser1 = new JFileChooser();//new file chooser
			jFileChooser1.setAcceptAllFileFilterUsed(false); 
			jFileChooser1.setFileFilter(new TextFilter());//sets filter
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);//sets default close operation so that only this window closes when x is pressed
			if (jFileChooser1.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {//opens jfilechooser open dialog checks for aprrove option to be selected
				File file = jFileChooser1.getSelectedFile();//make file
				DataBase.projectName = IOToolkit.removeExtension(file.getName());//remove extension
				// Load template name located in LastTemplate text file
				IOToolkit.set_File(file.getName(), file);//sets the mtm file
				IOToolkit.readTemplate(IOToolkit.get_fline(0), file);//load all other files
				// IOToolkit.load(file.getName());
				DataBase.isDataBaseEmpty = false;
				if(DataBase.isLauncher){//if this is the launcher's load button
					define();//generate gui 
					DataBase.isLauncher = false;
				}
				DataBase.projectFile = file;//set project file
				DataBase.projectFile = IOToolkit.removeFileExtension(DataBase.projectFile);//remove extension
				DataBase.isItFirstLoad = false;
				DataBase.selected_Department = DataBase.departments[0].getName();//set department
				refreshDisplay();//refresh display
			}
		}
	}

	class newFileOpener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//New template is loaded
			IIbasics base = new IIbasics(0);
			base.setVisible(true);
		}
	}

	class saveFileChooser implements ActionListener {//save actionlisnter
		public void actionPerformed(ActionEvent e) {

			IOToolkit.saveTemplate(DataBase.projectFile);//saves all the files

		}	
	}

	@SuppressWarnings("serial")
	class saveAsFileChooser extends JFrame implements ActionListener {//save as actionlistner
		public void actionPerformed(ActionEvent ee) {

			jFileChooser1 = new JFileChooser();//new file chooser
			jFileChooser1.setSelectedFile(new File(DataBase.projectName));//set selected file as a file name after project name
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			if (jFileChooser1.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {//opens jfilechooser saVE dialog checks for aprrove option to be selected
				String filename = IOToolkit.removeExtension(jFileChooser1.getSelectedFile().getName());
				if(jFileChooser1.getSelectedFile().exists()|IOToolkit.doesFileExist(jFileChooser1.getCurrentDirectory().getPath(),filename))//check if files selected exists

				{
					DataBase.isSaveAsFile = true;
					int actionDialog = JOptionPane.showConfirmDialog(null, "Replace existing file?");//send confirm message to user
					if (actionDialog == JFileChooser.APPROVE_OPTION){//checks if user approves
						DataBase.projectFile = new File(jFileChooser1.getCurrentDirectory()
								.getPath(), filename);
						IOToolkit.saveTemplate(DataBase.projectFile);//saves files
					}else{

					}
				}else{
					DataBase.projectFile = new File(jFileChooser1.getCurrentDirectory()
							.getPath(), filename);
					IOToolkit.saveTemplate( DataBase.projectFile);
				}
			}
		}


	}
	class BrowseListener extends JFrame implements ActionListener{//browse action lisnetner
		private JFileChooser jFileChooser1;
		public void actionPerformed(ActionEvent e) {
			jFileChooser1 = new JFileChooser();//makes file chooser
			jFileChooser1.setSelectedFile(new File("myHTML"));//set selection file to file name myhtml
			jFileChooser1.setDialogType(JFileChooser.SAVE_DIALOG);
			jFileChooser1.setDialogTitle("Browse");
			jFileChooser1.setApproveButtonText("Select");//change approve button text to select
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			if (jFileChooser1.showDialog(this, null) == JFileChooser.APPROVE_OPTION) {//open jfilechooser dialog and checks for approve option
				if(jFileChooser1.getCurrentDirectory().isDirectory()){//checks if directory exists
					isBrowse = true;
				}else{
					isBrowse = false;
				}
				department = new File(jFileChooser1.getCurrentDirectory()//sets file to path
						.getPath(), jFileChooser1.getSelectedFile().getName());
				tField.setText(jFileChooser1.getCurrentDirectory()//sets tfield to path
						.getPath());
			}
		}
	}
	class ComboListener implements ActionListener{//combobox actionalisnter

		public void actionPerformed(ActionEvent e) {//sets the selected thing in combox into the String department
			JComboBox cb = (JComboBox)e.getSource();
			String department = (String)cb.getSelectedItem();
			selectedDepartment = department;
			if(selectedDepartment == "----------")
			{
				isDepart = true;
			}else{
				isDepart = true;
			}
		}
	}

	class CreateListener implements ActionListener{//this is the create listener class implements actionlisteners
		private exportWindow frame = new exportWindow();//make instance of export window and holds it in the frame variable
		private Runtime rt = Runtime.getRuntime();//get runtime and hold it in rt variable

		public void actionPerformed(ActionEvent e) {//creates html 
			String path = tField.getText();//gets path from tfield
			if(path != "" & IOToolkit.isDirectory(path)){//check if path exists
				path = path.concat(DataBase.dash).concat(department.getName());
				department = new File(path);//makes file to path
				isBrowse = true;
			}else{
				isBrowse = false;
			}
			if(isBrowse && isDepart){
				if(selectedDepartment == "----------")
				{
					JOptionPane.showMessageDialog(frame,
							"No Department Selected",
							"ERROR",JOptionPane.ERROR_MESSAGE);
				}else if(selectedDepartment == "MASTER")
				{
					boolean isSuccess = DataBase.printMasterHTML(department);//creates html for master
					if(isSuccess)
					{
						JOptionPane.showMessageDialog(frame,
								"File was successfully completed!",
								"Export Complete",
								JOptionPane.INFORMATION_MESSAGE);
						try {

							String name = new String(""+department.getPath()+ ".html");
							Process p = rt.exec("rundll32 url.dll , FileProtocolHandler " + name);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}else
					{
						JOptionPane.showMessageDialog(frame,
								"Error occurred in file creation",
								"ERROR",
								JOptionPane.ERROR_MESSAGE);
					}
				}else if(selectedDepartment == "ALL")
				{
					boolean isSuccess = DataBase.printAllDepartmentHTML(department);//creates html for all departments
					if(isSuccess)
					{
						JOptionPane.showMessageDialog(frame,
								"File was successfully completed!",
								"Export Complete",
								JOptionPane.INFORMATION_MESSAGE);	
						try {

							String name = new String(""+department.getPath()+ ".html");
							Process p = rt.exec("rundll32 url.dll , FileProtocolHandler " + name);
						} catch (IOException e1) {
							e1.printStackTrace();
						}

					}else
					{
						JOptionPane.showMessageDialog(frame,
								"Error occurred in file creation",
								"ERROR",
								JOptionPane.ERROR_MESSAGE);
					}
				}else{
					boolean isSuccess = DataBase.printSingleDepartmentHTML(department, selectedDepartment);//creastes html for single department
					if(isSuccess)
					{
						JOptionPane.showMessageDialog(frame,
								"File was successfully completed!",
								"Export Complete",
								JOptionPane.INFORMATION_MESSAGE);	
						try {

							String name = new String(""+department.getPath()+ ".html");
							Process p = rt.exec("rundll32 url.dll , FileProtocolHandler " + name);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}else
					{
						JOptionPane.showMessageDialog(frame,
								"Error occurred in file creation",
								"ERROR",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}else if(isBrowse){
				JOptionPane.showMessageDialog(frame,
						"No Department Selected",
						"ERROR",
						JOptionPane.ERROR_MESSAGE);
			}else if(isDepart){
				JOptionPane.showMessageDialog(frame,
						"No Directory Selected",
						"ERROR",JOptionPane.ERROR_MESSAGE);
			}else{
				JOptionPane.showMessageDialog(frame,
						"No Directory and Department Selected",
						"ERROR",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	public class userGuideDocListener implements ActionListener {
		private Runtime rt = Runtime.getRuntime();//get runtime and hold it in rt variable
		public void actionPerformed(ActionEvent e) {
			try {

				//Process p = rt.exec("cmd /c start MTM_Manual_2_1.docx");//+ helpPath);
				Desktop.getDesktop().open(new File("MTM_Manual_2_1.docx"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Error "+ e1.toString() ,
						"ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	public class userGuidePdfListener implements ActionListener {
		private Runtime rt = Runtime.getRuntime();//get runtime and hold it in rt variable
		public void actionPerformed(ActionEvent e) {
			try {

				//Process p = rt.exec("cmd /c start MTM_Manual_2_1.pdf");//+ helpPath);
				
				Desktop.getDesktop().open(new File("MTM_Manual_2_1.pdf"));//launches the help file
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Error "+ e1.toString() ,
						"ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	public class userGuideTxtListener implements ActionListener {
		private Runtime rt = Runtime.getRuntime();//get runtime and hold it in rt variable
		public void actionPerformed(ActionEvent e) {
			try {

				//Process p = rt.exec("cmd /c start MTM_Manual_2_1.txt");//+ helpPath);
				Desktop.getDesktop().open(new File("MTM_Manual_2_1.txt"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Error "+ e1.toString() ,
						"ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	public class userGuideHTMLListener implements ActionListener {
		private Runtime rt = Runtime.getRuntime();//get runtime and hold it in rt variable
		public void actionPerformed(ActionEvent e) {
			try {

				//Process p = rt.exec("cmd /c start MTM_Manual_2_1.htm");//+ helpPath);
				Desktop.getDesktop().open(new File("MTM_Manual_2_1.htm"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Error "+ e1.toString() ,
						"ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	class exportWindow extends JFrame implements ActionListener {//this class is the export window class used for exporting html
		//extends actionlistener
		public String[] convertToStringArray(Department[] depart) {//this method makes the array for the combo box list 
			String[] array = new String[depart.length + 3];//takes the Departments array and initializes the String array with it's length
			for (int i = 0; i < array.length; i++) {//loops through String array's length
				if(i == 0){//add default blank for first option
					array[i] = "----------";
				}else if (i == (array.length - 1)) {//adds an all option
					array[i] = "ALL";
				} else if (i == (array.length - 2)) {//adds a master option
					array[i] = "MASTER";
				} else {//put the rest of the departments in the array
					array[i] = depart[i-1].getName();
				}
			}
			return array;//returns the array

		}
		private JButton cancel = new JButton("Cancel");//makes a jbutton with cancel on it
		private JButton create = new JButton("Create");//makes a jbutton with create on it
		private JLabel title = new JLabel("Select Export Destination:");// make jlabel 
		private JLabel description = new JLabel("Select Department:");//makes a jlabel
		private JButton Browse = new JButton("Browse");//makes a jbutton with browse on it
		public void actionPerformed(ActionEvent ee) {//if action performed
			if(isFirst){//checks if this is the first time the export window is made
				setTitle("To Html");//set title
				JPanel panel = new JPanel();//makes jpanel
				add(panel);//add jPanel into frame
				setBounds(500, 300, 400, 150);//sets the y and x coordinates and the dimensions of frame
				panel.setBorder(
						BorderFactory.createTitledBorder("Export to HTML File"));//add a border to panel
				WindowListener exitListener = new WindowAdapter() {//sets up the exit listener

					public void windowClosing(WindowEvent e) {
						//int confirm = JOptionPane.showOptionDialog(null, "Are You Sure to Close Application?", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
						//if (confirm == 0) {
						isFirst = false;
						setVisible(false);//makes the frame invisible when it is closed
						//}
					}
				};
				addWindowListener(exitListener);//add exit listener to the frame
				JComboBox<String> departList = new JComboBox<String>(//makes a combo box list using an array
						convertToStringArray(DataBase.departments));
				departList.addActionListener(new ComboListener());//adds the combolistner to the departlist combo box
				Browse.addActionListener(new BrowseListener());//add browselistener to browse button
				create.addActionListener(new CreateListener());//adds vreate listener to create button
				cancel.addActionListener( new ActionListener() {// adds action lisnter to cancel button
					public void actionPerformed(ActionEvent e) {//which sets the frame invisible when clicked
						isFirst = false;
						setVisible(false);
					}
				});
				title.setFont(new Font("Arial", 0, 12));//change font for title
				description.setFont(new Font("Arial", 0, 12));//change font for description
				panel.setLayout(new GridBagLayout());//sets gridbag layout
				GridBagConstraints gbc = new GridBagConstraints();//add constraints

				gbc.fill = GridBagConstraints.BOTH;
				gbc.gridx = 0;
				gbc.gridy = 0;
				panel.add(title, gbc);//add title to the panel 
				gbc.gridx = 0;
				gbc.gridy = 1;
				panel.add(tField, gbc);//adds text field under the title in panel
				gbc.gridx = 1;
				gbc.gridy = 1;
				panel.add(Browse, gbc);//adds the browse button beside text field
				gbc.gridx = 0;
				gbc.gridy = 2;
				panel.add(description, gbc);//adds the label under the textfield and browse button

				gbc.gridx = 1;
				gbc.gridy = 2;
				panel.add(departList, gbc);//adds the combo box beside the jlabel
				gbc.gridx = 0;
				gbc.gridy = 3;
				panel.add(create, gbc);//adds the create button under the jlaBEL
				gbc.gridx = 1;
				gbc.gridy = 3;
				panel.add(cancel, gbc);//adds cancel button beside the create button
				pack();//pack the frame into panel
				setResizable(false);//make the frame not resisable
				setVisible(true);//set visible
			}else//if this is not first time the window has been opened
			{
				setVisible(true);//its is set visible again
			}
		}
	}
	/////////////////////////////////////////ViewMenu Listeners//=====//=====//
	class visOpsToggleListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			displayVisOpsPanel = (displayVisOpsPanel) ? false : true;
			if(displayVisOpsPanel)
			{
				visOpsPanel.setVisible(true);
			}
			else
			{
				visOpsPanel.setVisible(false);
			}
			packAgain();//repack to screensize
		}
	}
	class colVisibilityChangeListener implements ChangeListener
	{
		//Listens for change in visibility options values
		//Code this has been originally attached to has been temporarily removed!!
		String level = "";
		public colVisibilityChangeListener(String level)
		{
			this.level = level;
		}
		public void stateChanged(ChangeEvent e) {
			int visibility = 0;
			JSpinner source = (JSpinner)e.getSource();
			visibility = Integer.parseInt((String)source.getValue());
			System.out.println(
					level + " column visibility adjusted to: " + visibility);
			int matchCounter = 0;
			for(int colNum = 0; colNum<DataBase.sem1_main_table_header.length; colNum++)
			{
				if(DataBase.sem1_main_table_header[colNum].equals(level))
					matchCounter++;
				if(DataBase.sem1_main_table_header[colNum].equals(level) & matchCounter>visibility)
				{
					sem1_table.getColumnModel().getColumn(colNum).setMaxWidth(0);
					sem1_table.getColumnModel().getColumn(colNum).setMinWidth(0);
					sem1_table.getColumnModel().getColumn(colNum).setPreferredWidth(0);
					sem2_table.getColumnModel().getColumn(colNum).setMaxWidth(0);
					sem2_table.getColumnModel().getColumn(colNum).setMinWidth(0);
					sem2_table.getColumnModel().getColumn(colNum).setPreferredWidth(0);
				}
				else if (DataBase.sem1_main_table_header[colNum].equals(level) & !(colNum<2))//If not the P or Gr column
				{
					sem1_table.getColumnModel().getColumn(colNum).setMaxWidth(120);
					sem1_table.getColumnModel().getColumn(colNum).setMinWidth(20);
					sem1_table.getColumnModel().getColumn(colNum).setPreferredWidth(myColumnWidth);
					sem2_table.getColumnModel().getColumn(colNum).setMaxWidth(120);
					sem2_table.getColumnModel().getColumn(colNum).setMinWidth(20);
					sem2_table.getColumnModel().getColumn(colNum).setPreferredWidth(myColumnWidth);
				}
			}
			//			if(DataBase.selected_Department!=null)
			//				DataBase.departments[DataBase.findDeptPos(DataBase.selected_Department)].colVis[DataBase.findLevelPos(level)] = visibility;
		}
	}
	class togglePeriodicTotalsListener implements ActionListener
	{//toggles visibility of bottom 2 tables
		public void actionPerformed(ActionEvent e) {
			displayPeriodTotals = (displayPeriodTotals) ? false : true;
			if(displayPeriodTotals)
			{
				sem1_period_ScrollPane.setVisible(true);
				sem2_period_ScrollPane.setVisible(true);
			}
			else
			{
				sem1_period_ScrollPane.setVisible(false);
				sem2_period_ScrollPane.setVisible(false);
			}
			packAgain();//repack to screensize
		}
	}
	class courseListWindowListener implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			window2.setVisible(true);
			courseListWindow.setEnabled(false);
		}

	}
	/////////////////////////////////////////EditMenu Listeners//=====//=====//
	class editCourseDataListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0) {
			secondaryDataBase.dbTransferReverse();//Copies data from Database to secondaryDataBase
			IIcourses.loadMe = false;//Prevents DataBase overwrite
			IIcourses IIcourses = new IIcourses("CoursesOnly");
			IIcourses.setVisible(true);
			IIcourses.setLocationRelativeTo(null);
		}
	}
	class editDept_RoomsDataListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0) {
			int option = JOptionPane.showConfirmDialog(null, 
					"Editing the Department and Room information will remove all placed sections. " +
					"Do you wish to continue?", 
					"Continue to Department and Room Options", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				secondaryDataBase.dbTransferReverse();//Copies data from Database to secondaryDataBase
				IIroomsAndDepts.loadMe = true;//Prevents DataBase overwrite
				IIroomsAndDepts IIroomsAndDepts = new IIroomsAndDepts("DeptsRoomsCourses");
				IIroomsAndDepts.setVisible(true);
				IIroomsAndDepts.setLocationRelativeTo(null);
			}
		}
	}
	class editlunchPrefDataListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0) {
			secondaryDataBase.dbTransferReverse();//Copies data from Database to secondaryDataBase
			IIlunchPrefs.loadMe = true;//Prevents DataBase overwrite
			IIlunchPrefs IIlunchPrefs = new IIlunchPrefs("LunchPrefsOnly");
			IIlunchPrefs.setVisible(true);
			IIlunchPrefs.setLocationRelativeTo(null);
		}
	}
	class editFileNameListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0) {
			secondaryDataBase.dbTransferReverse();//Copies data from Database to secondaryDataBase
			IIbasics IIbasics = new IIbasics(-1);
			IIbasics.setVisible(true);
			IIbasics.setLocationRelativeTo(null);
		}
	}
		private void clearTimeT()
		{//sets all values of timeT to null
			for (int i = 0; i < DataBase.timeT.length; i++) {
				for (int j = 0; j < DataBase.timeT[0].length; j++) {
					for (int k = 0; k < DataBase.timeT[0][0].length; k++) {
						for (int l = 0; l < DataBase.timeT[0][0][0].length; l++) {
							if (DataBase.timeT[i][j][k][l] != null) {
								DataBase.timeT[i][j][k][l] = null;
							}
						}
					}
				}
			}
		}
	class clearAllListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0) 
		{
			int option = JOptionPane.showConfirmDialog(null, "Clear the time table? (Removes all placed sections)", "Clear All", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				clearTimeT();//clear
				refreshDisplay();
				JOptionPane.showMessageDialog(null, "TimeTable has been cleared");//notify
			}
		}
	}
//	class editProjectDataListener implements ActionListener
//	{
//		public void actionPerformed(ActionEvent arg0) {
//			secondaryDataBase.dbTransferReverse();
//			IIbasics IIbasics = new IIbasics(1);
//			IIbasics.setVisible(true);
//		}
//	}
	////////////////////////////////////////////////////////Listeners in Window 1
	public void colorTables(Color backGroundColor, Color foreGroundColor)
	{
		System.out.println("**Colouring tables***");
		this.backGroundColor = backGroundColor;
		this.foreGroundColor = foreGroundColor;
	}
	public void loadVisOp(String visOpName)
	{
		Department visOp = DataBase.departments[DataBase.findDeptPos(visOpName)];
		if (visOp != null)
		{
			for (int index = 0; index<visOpsPanel.getComponentCount(); index++)
			{
				try
				{
					((JSpinner) visOpsPanel.getComponent(index)).setValue("1");
					((JSpinner) visOpsPanel.getComponent(index)).setValue("0");
				}catch(ClassCastException cCE){}
			}
			for (int index = 0, levelNum = 0; index<visOpsPanel.getComponentCount(); index++)
			{
				try
				{
					if(((LevelSpinner) visOpsPanel.getComponent(index)).isLevelSpinner())
					{
						System.out.println(((JSpinner) visOpsPanel.getComponent(index)) + " Spinner set to: " + visOp.colVis[levelNum]);
						((JSpinner) visOpsPanel.getComponent(index)).setValue(Integer.toString(visOp.colVis[levelNum]));
						levelNum++;
					}
				}catch(ClassCastException cCE){}
				catch(Exception e){e.printStackTrace();}		
			}
			colorTables(visOp.backGroundColor, visOp.foreGroundColor);
		}
	}
	class deptButtonListener implements ActionListener
	{//Listens for selected department among department buttons
		public void actionPerformed(ActionEvent e) 
		{//does nothing besides print selected department yet
			JButton source = (JButton)e.getSource();
			DataBase.selected_Department = source.getText();
			System.out.println("Selected Department: " + DataBase.selected_Department);
			departmentLabel.setText("Project Name: " + DataBase.projectName + "      -      Selected Department: " + DataBase.selected_Department);
			DataBase.refreshTables(DataBase.findDeptPos(DataBase.selected_Department));
			setMainTableContents("");
			loadVisOp(DataBase.selected_Department);
			DataBase.updatePeriodicTotals();
			sem1_period.setModel(new UneditableTableModel(
					DataBase.sem1_period_table_data, DataBase.sem1_period_totals_header));
			sem2_period.setModel(new UneditableTableModel(
					DataBase.sem2_period_table_data, DataBase.sem2_period_totals_header));
			DataBase.updateDeptTotal();
			sem1_Depttotal.setText("Semester 1 Department's Placed Courses: " + DataBase.sem1_DeptTotal);
			sem2_Depttotal.setText("Semester 2 Department's Placed Courses: " + DataBase.sem2_DeptTotal);
		}
	}
	class toggleRepatchPreferenceListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{//Switch autoRepatchVisibilityIsPreferred value
			DataBase.autoRepatchVisibilityIsPreferred = DataBase.autoRepatchVisibilityIsPreferred ? false : true ;
			toggleRepatchPreference.setText("Toggle Automatic Column Visibility Adjustment [Currently: "+ DataBase.autoRepatchVisibilityIsPreferred +"] ");
		}
	}
	public void resetSchoolTotals() {
		sem1_totals.setText("Semester 1 Total Placed Courses: " + DataBase.sem1_Total);
		sem2_totals.setText("Semester 2 Total Placed Courses: " + DataBase.sem2_Total);
	}
		public boolean timeTIsClear()
		{//Checks if all of timeT is null
			for (int i = 0; i < DataBase.timeT.length; i++) {
				for (int j = 0; j < DataBase.timeT[0].length; j++) {
					for (int k = 0; k < DataBase.timeT[0][0].length; k++) {
						for (int l = 0; l < DataBase.timeT[0][0][0].length; l++) {
							if (DataBase.timeT[i][j][k][l] != null) {
								return false;
							}
						}
					}
				}
			}
			return true;
		}
	public void refreshDisplay()//TAG: RD
	{//Resets or Labels and Tables and lists to display properly according to timeT and other data
		DataBase.isChange = true;
		DataBase.rescanForValidity();
		frame.setTitle("Master Timetable Maker - " + DataBase.projectName);
		if(DataBase.autoRepatchVisibilityIsPreferred)
			DataBase.autoRepatchVisibility();
		DataBase.updateRoomStatuses();
		DataBase.updateCourseStatuses();
		DataBase.refilter_CourseSelections(
				DataBase.selected_DeptFilter, DataBase.selected_LvlFilter);
		Course_Manager_Window2.courseSelectionList.setListData(DataBase.courseSelections);
		DataBase.updateSchoolTotal();
		resetSchoolTotals();
		if(DataBase.isNew){
			DataBase.initializeTimeT();
		}
		DataBase.refreshTables(DataBase.findDeptPos(DataBase.selected_Department));
		setMainTableContents("");
		departmentLabel.setText("Project Name: " + DataBase.projectName + "      -      Selected Department: " + DataBase.selected_Department);
		loadVisOp(DataBase.selected_Department);
		DataBase.updatePeriodicTotals();
		sem1_period.setModel(new UneditableTableModel(
				DataBase.sem1_period_table_data, DataBase.sem1_period_totals_header));
		sem2_period.setModel(new UneditableTableModel(
				DataBase.sem2_period_table_data, DataBase.sem2_period_totals_header));
		DataBase.updateDeptTotal();
		sem1_Depttotal.setText("Semester 1 Department's Placed Courses: " + DataBase.sem1_DeptTotal);
		sem2_Depttotal.setText("Semester 2 Department's Placed Courses: " + DataBase.sem2_DeptTotal);
		DataBase.setTimeTCopy();
		this.defineDeptSum();
		System.out.println("Refreshed Display");
		deptSumsFrame.setVisible(false);
		roomSumsFrame.setVisible(false);
		try {
			for(int sem = 0; sem < DataBase.schoolYearDivisions; sem++){
				sem_Frames[sem].setVisible(false);
			}
		} catch (Exception e) {
		}
		Course_Manager_Window2.automateButton.setEnabled(timeTIsClear());
	}
	class selectDeptBackColorListener implements ActionListener
	{//Spawns a color chooser
		JColorChooser colorChooser = new JColorChooser();
		class changeListener implements ChangeListener
		{//Associates color chooser to Department array in DataBase
			public void stateChanged(ChangeEvent e) {
				DataBase.departments[DataBase.findDeptPos(DataBase.selected_Department)].setBackGroundColor(colorChooser.getColor());
				//^Sets the color appropriately
				loadVisOp(DataBase.selected_Department);//Applies changes immediately
				department_buttons_panel.getComponent(DataBase.findDeptPos(DataBase.selected_Department)).setBackground(DataBase.departments[DataBase.findDeptPos(DataBase.selected_Department)].backGroundColor);
				refreshDisplay();
			}
		}
		public void actionPerformed(ActionEvent e) {
			colorChooser.getSelectionModel().addChangeListener(new changeListener());
			//^Attach Listener above
			JDialog dialogFrame = new JDialog();//Create own frame
			dialogFrame.setTitle("Set Cell Fill Colour");
			dialogFrame.setContentPane(colorChooser);//^And add the new chooser
			//Sets location to slightly to the top left, but not the corner
			dialogFrame.setLocation(screenSize.width/7, screenSize.height/7);
			dialogFrame.pack();//Repacks to fit content
			dialogFrame.setVisible(true);
		}	
	}

	class selectDeptForeColorListener implements ActionListener
	{//Spawns a color chooser
		JColorChooser colorChooser = new JColorChooser();
		class changeListener implements ChangeListener
		{//Associates color chooser to Department array in DataBase
			public void stateChanged(ChangeEvent e) {
				DataBase.departments[DataBase.findDeptPos(DataBase.selected_Department)].setForeGroundColor(colorChooser.getColor());
				//^Sets the color appropriately
				loadVisOp(DataBase.selected_Department);//Applies changes immediately
				department_buttons_panel.getComponent(DataBase.findDeptPos(DataBase.selected_Department)).setForeground(DataBase.departments[DataBase.findDeptPos(DataBase.selected_Department)].foreGroundColor);
				refreshDisplay();
			}
		}
		public void actionPerformed(ActionEvent e) {
			colorChooser.getSelectionModel().addChangeListener(new changeListener());
			//^Attach Listener above
			JDialog dialogFrame = new JDialog();//Create own frame
			dialogFrame.setTitle("Set Text Colour");
			dialogFrame.setContentPane(colorChooser);//^And add the new chooser
			//Sets location to slightly to the top left, but not the corner
			dialogFrame.setLocation(screenSize.width/7, screenSize.height/7);
			dialogFrame.pack();//Repacks to fit content
			dialogFrame.setVisible(true);
		}	
	}

	@SuppressWarnings("serial")
	class deptSumsTable extends JTable
	{//Colored table class for a full semester
		public Component prepareRenderer(
				TableCellRenderer renderer, int row, int col){
			Component c = super.prepareRenderer(renderer, row, col);
			if(getValueAt(row, col) != null
					&& col==0 && DataBase.findDeptPos((String)getValueAt(row, col))!=-1){
				c.setBackground(DataBase.departments[DataBase.findDeptPos((String)getValueAt(row, col))].backGroundColor);
				c.setForeground(DataBase.departments[DataBase.findDeptPos((String)getValueAt(row, col))].foreGroundColor);
			}
			else{
				if(row%2 == 0)
					c.setBackground(new Color(250, 250, 250));
				else
					c.setBackground(new Color(240, 240, 240));
				c.setForeground(Color.BLACK);
			}
			return c;
		}
	}
	private static JFrame deptSumsFrame = new JFrame();
	private static JTable deptSumsTable = new JTable();
	private static String[] deptSumsTable_header;
	private static String[][] deptSumsTable_data;
	public int countDWSections(int deptPos, int sem)
	{
		int sectionCount = 0;
		for(int rowNum = 0; rowNum<DataBase.timeT[0].length; rowNum++)
		{//iterates through each row
			for(int xCoordinate = 0; xCoordinate<DataBase.timeT[0][0].length; xCoordinate++)
			{//iterates through each column
				if(DataBase.timeT[deptPos][rowNum][xCoordinate][sem] != null)
					sectionCount++;
			}
		}
		return sectionCount;
	}
	public void defineDeptSum()
	{
		deptSumsFrame.setTitle("Department Summaries");
		deptSumsTable_header = new String[DataBase.schoolYearDivisions+2];
		deptSumsTable_data = new String[DataBase.departments.length+1][DataBase.schoolYearDivisions+2];
		deptSumsTable = new deptSumsTable();

		for(int colNum = 0; colNum<DataBase.schoolYearDivisions+2; colNum++)
		{
			if(colNum == 0)
				deptSumsTable_header[colNum] = "Department Name";
			else if(colNum == DataBase.schoolYearDivisions+1)
				deptSumsTable_header[colNum] = "Total";
			else
				deptSumsTable_header[colNum] = "Sem " + colNum;
			deptSumsTable_data[DataBase.departments.length][colNum] = Integer.toString(0);

		}
		for(int rowNum = 0; rowNum<DataBase.departments.length+1; rowNum++)
		{
			deptSumsTable_data[rowNum][DataBase.schoolYearDivisions+1] = Integer.toString(0);
			for(int colNum = 0; colNum<DataBase.schoolYearDivisions+1; colNum++)
			{
				if(colNum == 0)
				{
					if(rowNum!=DataBase.departments.length)
						deptSumsTable_data[rowNum][colNum] = DataBase.departments[rowNum].getName();
					else
						deptSumsTable_data[rowNum][colNum] = "Total";
				}
				else if (rowNum<DataBase.departments.length)
				{
					deptSumsTable_data[rowNum][colNum] = Integer.toString(countDWSections(rowNum, colNum-1));
					deptSumsTable_data[rowNum][DataBase.schoolYearDivisions+1] = Integer.toString(
							Integer.parseInt(deptSumsTable_data[rowNum][colNum])		+
							Integer.parseInt(deptSumsTable_data[rowNum][DataBase.schoolYearDivisions+1])
							);
					deptSumsTable_data[DataBase.departments.length][colNum] = Integer.toString(
							Integer.parseInt(deptSumsTable_data[rowNum][colNum])		+
							Integer.parseInt(deptSumsTable_data[DataBase.departments.length][colNum])
							);
					deptSumsTable_data[DataBase.departments.length][DataBase.schoolYearDivisions+1] = 
							Integer.toString(
									Integer.parseInt(deptSumsTable_data[rowNum][colNum])		+
									Integer.parseInt(deptSumsTable_data[DataBase.departments.length][DataBase.schoolYearDivisions+1])
									);
				}
			}
		}
		for (int semNum = 1; semNum<=DataBase.schoolYearDivisions; semNum++)
			deptSumsTable_data[DataBase.departments.length][DataBase.schoolYearDivisions+1] = Integer.toString(
					Integer.parseInt(deptSumsTable_data[DataBase.departments.length][DataBase.schoolYearDivisions+1])
					+ Integer.parseInt(deptSumsTable_data[DataBase.departments.length][semNum])
					);
		deptSumsTable.setModel(new UneditableTableModel(deptSumsTable_data, deptSumsTable_header));
		//deptSumsTable.setFillsViewportHeight(true);
		deptSumsTable.getColumnModel().getColumn(0).setPreferredWidth(myColumnWidth*3);
		for(int colNum = 1; colNum<DataBase.schoolYearDivisions+1; colNum++)
		{
			deptSumsTable.getColumnModel().getColumn(colNum).setPreferredWidth(myColumnWidth);
		}
		//deptSumsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		deptSumsFrame.setContentPane(new JScrollPane(deptSumsTable));
		deptSumsFrame.pack();
		//deptSumsFrame.setBounds(screenSize.width/3, screenSize.height/3,(DataBase.schoolYearDivisions+4)*myColumnWidth+25, (int)(DataBase.departments.length*21.6+21.6));
	}
	class deptSummariesListener implements ActionListener
	{//refreshes and toggles visibility of deptSumsFrame
		public void actionPerformed(ActionEvent e) {
			//			if(showDeptSums)
			//			{
			//				showDeptSums = false;
			//			}
			//			else
			//			{
			//				showDeptSums = true;
			//			}
			//			refreshDisplay();
			Course_Manager_Main.window1.defineDeptSum();
			deptSumsFrame.setVisible(true);
		}	
	}
		private static JFrame roomSumsFrame = new JFrame();
		private static JTable roomSumsTable = new JTable();
		private static String[] roomSumsTable_header;
		private static String[][] roomSumsTable_data;
		private void defineRoomSums()
		{//Fills in table
			roomSumsFrame = new JFrame();
			roomSumsTable = new JTable();
			roomSumsTable_header = new String[DataBase.schoolPeriodCount*2+1];
			roomSumsTable_data = new String[DataBase.rooms.length][DataBase.schoolPeriodCount*2+1];
			roomSumsTable_header[0] = "Room";//First column header
			for(int col = 0; col <=DataBase.schoolPeriodCount*2; col++)
			{//Sets up headers by period
				if(col!= 0)
					roomSumsTable_header[col] = "Sem " + (int)((col-1)/DataBase.schoolPeriodCount + 1) + " Prd " +
					Integer.toString(((col-1)%DataBase.schoolPeriodCount)+1);
			}

			for (int row = 0; row < roomSumsTable_data.length; row++) {
				for (int col = 0; col < roomSumsTable_data[0].length; col++) {
					if(col==0)
					{
						roomSumsTable_data[row][col] = DataBase.rooms[row].getType();
					}
					else
					{//Fill with status of the room (FreeRoomCount/ExistingRoomCount)
						roomSumsTable_data[row][col] = "("+
								DataBase.rooms[row].numOfRooms()[(int)((col-1)/DataBase.schoolPeriodCount)][((col-1)%DataBase.schoolPeriodCount)]+
								"/"+DataBase.rooms[row].getOriginalNumOfRooms() + ")";
					}
				}
			}
			roomSumsTable.setModel(new UneditableTableModel(roomSumsTable_data, roomSumsTable_header));
			roomSumsTable.getColumnModel().getColumn(0).setPreferredWidth(150);
			roomSumsFrame.setContentPane(new JScrollPane(roomSumsTable));
			roomSumsFrame.setTitle("Number of Unused Rooms");
			roomSumsFrame.pack();
			roomSumsFrame.setBounds(0, 0, 900, 500);
			roomSumsFrame.setLocationRelativeTo(null);
		}
	class roomSummariesListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			defineRoomSums();
			roomSumsFrame.setVisible(true);
		}
	}
	public static void disposeAlike() {
		for (int sem = 0; sem < DataBase.schoolYearDivisions; sem++)
			sem_Frames[sem].dispose();
	}
	public static void display(String courseName, int sem, int rowNum) {
		int level = DataBase.findLevelPos(DataBase.findCourse(courseName)
				.getLevel());
		int col = 0;// column in which the courseName will be placed
		int row = 0;// row in which the courseName will be placed
		int colStart;// initial col coordinate of the column range where the
		// courseName will be placed
		int rowStart;// initial row coordinate of the row range where the
		// courseName will be placed
		boolean done = false;// indicates if placement has succeeded
		// calculate start of column range
		colStart = 2;// start at 2 since 0 and 1 are P and Gr columns
		for (int levelsIndex = 1; levelsIndex <= level; levelsIndex++) {
			colStart = colStart
					+ DataBase.levelColCount[levelsIndex - 1][sem];
		}
		// calculate start of row range
		// System.out.println(courseName + " period: " + period + " grade: "
		// + grade + " level: " + level);
		rowStart = rowNum * highestRowCount;
		// calculate the actual accurate col and row to place it in
		for (col = colStart; !done
				&& col < colStart + DataBase.levelColCount[level][sem]; col++) {
			for (row = rowStart; !done && row < rowStart + highestRowCount; row++) {
				if (DataBase.sem_SWTable_data[sem][row][col] == null) {
					DataBase.sem_SWTable_data[sem][row][col] = courseName;
					done = true;// ends search loops
				}
			}// iterates through each possible row in the grade's range
		}// iterates through each possible column in the level's range
		// System.out.println(courseName + " placed at: " + row + ", " +
		// col);
	}
	public static void calculateDimensions(int semester) {
		DataBase.colCountSum = 0;// reset
		DataBase.rowCountSum = 0;// reset
		// DataBase.levelColCount = new
		// int[DataBase.levels.length][semester];// reset
		int[] levelRowCount = new int[DataBase.levels.length];
		int[] highestSectionCount = new int[DataBase.levels.length];// highest
		// number of cells that will be filled by sections
		int[] rowSectionCount = new int[DataBase.levels.length];// number of
		// cells that will be filled by sections at a giver row
		highestRowCount = 0;// number minimum number of rows required to
		// accommodate the highestSectionCount
		// when used (multiplied) along with DataBase.levelColCount for the level
		// with the highestSectionCount
		for (int levelsIndex = 0; levelsIndex < DataBase.levels.length; levelsIndex++) {
			rowSectionCount = new int[DataBase.levels.length];// resets on iteration//Obsolete?
			for (int rowNum = 0; rowNum < DataBase.timeT[0].length; rowNum++) {// iterates through each row
				rowSectionCount[levelsIndex] = 0;// resets on iteration
				for (int deptPos = 0; deptPos < DataBase.departments.length; deptPos++) {// iterates through each Department
					for (int xCoordinate = 0; xCoordinate < DataBase.timeT[0][0].length; xCoordinate++) {// iterates
						// through each column
						if (DataBase.timeT[deptPos][rowNum][xCoordinate][semester] != null
								&& DataBase.timeT[deptPos][rowNum][xCoordinate][semester]
										.getLevel()
										.equals(DataBase.levels[levelsIndex])) {
							rowSectionCount[levelsIndex]++;
						}// increase count if level match
					}// check each row
				}// ^and each department
				if (rowSectionCount[levelsIndex] > highestSectionCount[levelsIndex]) {// If
					// a row has a higher sectionCount than the current highest recorded, replace
					highestSectionCount[levelsIndex] = rowSectionCount[levelsIndex];
				}
			}// Check every row
		}// Do this for every level
		if (semester == 0) {
			DataBase.levelColCount = new int[DataBase.levels.length][DataBase.schoolYearDivisions];// reset
		}

		// Find the number of columns preferred to accommodate a number of
		// cells to be filled(highestSectionCount) of every level
		// The the number of columns preferred is found by starting with 1
		// row and 1 column,
		// If more are needed, a row is added first, then a column,
		// such that a formation with 3 rows 2 columns is tested before one
		// with 3 columns is tested
		// 1 cell -> 1 column (1 row), 2 cells-> 1 column (2 rows), 3
		// cells-> 2 columns (2 rows),
		// 6 cells -> 2 columns (3 rows), 7 cells ->3 columns(3 rows), 11
		// cells -> 3 columns (4 rows) and so on
		for (int levelsIndex = 0; levelsIndex < DataBase.levels.length; levelsIndex++) {
			DataBase.levelColCount[levelsIndex][semester] = 0;// start at simulating 0 columns
			while (DataBase.levelColCount[levelsIndex][semester]
					* DataBase.levelColCount[levelsIndex][semester] <= highestSectionCount[levelsIndex]
							- DataBase.levelColCount[levelsIndex][semester]) {// Increase
				// the number of columns until it can accommodate the given number of cells of the level
				DataBase.levelColCount[levelsIndex][semester]++;
			}
		}// Do this for every level
		for (int levelsIndex = 0; levelsIndex < DataBase.levels.length; levelsIndex++) {// calculate
			// local rowCounts based on number of columns and highest section count
			if (DataBase.levelColCount[levelsIndex][semester]
					* DataBase.levelColCount[levelsIndex][semester] > highestSectionCount[levelsIndex]) {
				levelRowCount[levelsIndex] = DataBase.levelColCount[levelsIndex][semester];
			} else {
				levelRowCount[levelsIndex] = DataBase.levelColCount[levelsIndex][semester] + 1;
			}
			if (highestRowCount < levelRowCount[levelsIndex]) {// then find
				// the highest rowCount between the levels as this is being done
				highestRowCount = levelRowCount[levelsIndex];
			}
		}
		for (int levelsIndex = 0; levelsIndex < DataBase.levels.length; levelsIndex++) {// revise
			// colCounts of the levels according to the highest rowCount that was found
			DataBase.levelColCount[levelsIndex][semester] = (int) highestSectionCount[levelsIndex]
					/ highestRowCount;
			if (DataBase.levelColCount[levelsIndex][semester] == 0
					|| highestSectionCount[levelsIndex]
							/ DataBase.levelColCount[levelsIndex][semester] > highestRowCount) {// if
				// there is a remainder for the division operations 
				//(if highestSectionCount does not fit in the a DataBase.levelColCount[levelsIndex] by highestRowCount Grid)
				DataBase.levelColCount[levelsIndex][semester]++;// Add an extra column
			}
			//			System.out.println("DataBase.levelColCount " + levelsIndex
			//					+ ": " + DataBase.levelColCount[levelsIndex]);
			DataBase.colCountSum = DataBase.colCountSum
					+ DataBase.levelColCount[levelsIndex][semester];// Sum all the final colcounts together
		}
		// multiply the highestRowCount by the period and grade count to
		// find totalrowcount
		DataBase.rowCountSum = highestRowCount * DataBase.schoolGradeCount
				* DataBase.schoolPeriodCount;
		DataBase.colCountSum = DataBase.colCountSum + 2 + 1;// Add 2 for
		// period and grade column, add 1 for section count column
		// These will be the final dimensions of the table
		System.out.println("DataBase.rowCountSum: " + DataBase.rowCountSum);
		System.out.println("highestRowCount: " + highestRowCount);
	}
	public void defineContents(int sem) {
		//System.out.println("***sem: " + sem);
		calculateDimensions(sem);
		// set sem#Header to appropriate length
		sem_SWTable_headers[sem] = new String[DataBase.colCountSum];
		for (int headerIndex = 2, levelsIndex = 0, writeCounter = 0; headerIndex < sem_SWTable_headers[sem].length
				&& levelsIndex < DataBase.levels.length; headerIndex++) {// write onto the header string[]
			sem_SWTable_headers[sem][headerIndex] = DataBase.levels[levelsIndex];
			writeCounter++;
			// ^write name of the level
			if (writeCounter == DataBase.levelColCount[levelsIndex][sem]) {// Once
				// the certain number of columns have recieved the name of the level, move on to writing the name of the next level
				levelsIndex++;
				writeCounter = 0;
			}
		}
		sem_SWTable_headers[sem][0] = "P";
		sem_SWTable_headers[sem][1] = "Gr";
		sem_SWTable_headers[sem][sem_SWTable_headers[0].length - 1] = "Total Number";
		DataBase.sem_SWTable_data[sem] = new String[DataBase.rowCountSum][DataBase.colCountSum];
		for (int rowNum = 0; rowNum < DataBase.timeT[0].length; rowNum++) {// iterates through each row
			for (int xCoordinate = 0; xCoordinate < DataBase.timeT[0][0].length; xCoordinate++) {// iterates through each column
				for (int deptPos = 0; deptPos < DataBase.departments.length; deptPos++) {// iterates through each department
					if (DataBase.timeT[deptPos][rowNum][xCoordinate][sem] != null)
						display(DataBase.timeT[deptPos][rowNum][xCoordinate][sem].name,
								sem, rowNum);
				}
			}
		}
		for (int rowNum = 0; rowNum < DataBase.rowCountSum; rowNum++) {
			if (rowNum % (DataBase.schoolGradeCount * highestRowCount) == 0)// Write periods
				DataBase.sem_SWTable_data[sem][rowNum][0] = Integer
				.toString(rowNum
						/ (DataBase.schoolGradeCount * highestRowCount)
						+ 1);
			if (rowNum % (highestRowCount) == 0)// Write grades
				DataBase.sem_SWTable_data[sem][rowNum][1] = Integer
				.toString(((rowNum / highestRowCount) % highestRowCount)
						+ 9);
		}
		// calculate section totals per period
		for (int rowStart = 0, sectionCount = 0; rowStart < DataBase.rowCountSum; rowStart += highestRowCount) {
			sectionCount = 0;// reset
			for (int colNum = 2; colNum < DataBase.colCountSum - 1; colNum++) {
				for (int rowNum = rowStart; rowNum < rowStart
						+ highestRowCount; rowNum++) {// caculate by adding
					// the number of non-null values within the range of the section
					if (DataBase.sem_SWTable_data[sem][rowNum][colNum] != null) {
						sectionCount = sectionCount + 1;
					}
				}
			}
			// add to table
			DataBase.sem_SWTable_data[sem][rowStart + highestRowCount - 1][DataBase.colCountSum - 1] = Integer
					.toString(sectionCount);
		}

		//		for (int rowNum = 0; rowNum < DataBase.sem_SWTable_data[sem].length; rowNum++) {
		//			for (int colNum = 2; colNum < DataBase.colCountSum - 1; colNum++) {
		//				System.out
		//				.print(DataBase.sem_SWTable_data[sem][rowNum][colNum]
		//						+ ", ");
		//			}
		//			System.out.println();
		//		}

		definePerBal(sem);
	}
	public void definePerBal(int sem) {
		String grandTotal = Integer.toString(0);
		DataBase.sem_SWPerBal_data[sem][DataBase.schoolGradeCount][DataBase.sem_SWPerBal_data[sem][0].length - 1] = Integer
				.toString(0);
		for (int colNum = 0; colNum < sem_SWPerBal_headers[sem].length; colNum++) {// Iterates
			// through every column
			if (colNum == 0) {// First column header is "P"
				sem_SWPerBal_headers[sem][colNum] = "Grade";
			} else if (colNum == sem_SWPerBal_headers[sem].length - 1) {// Final column header is "Total"
				sem_SWPerBal_headers[sem][colNum] = "Total";
			} else {// Else Period number is set as column header
				sem_SWPerBal_headers[sem][colNum] = "Prd "
						+ Integer.toString(colNum);
			}
			DataBase.sem_SWPerBal_data[sem][DataBase.schoolGradeCount][colNum] = Integer
					.toString(0);
		}
		for (int rowNum = 0; rowNum <= DataBase.schoolGradeCount; rowNum++) {// Iterates through each row
			DataBase.sem_SWPerBal_data[sem][rowNum][DataBase.sem_SWPerBal_data[sem][rowNum].length - 1] = Integer
					.toString(0);
			for (int colNum = 0; colNum <= DataBase.schoolPeriodCount; colNum++) {// Iterates through each row
				if (colNum == 0) {// Place Grade Number to first column
					if (rowNum < DataBase.schoolGradeCount)// Not bottom row
						DataBase.sem_SWPerBal_data[sem][rowNum][colNum] = "Gr "
								+ Integer
								.toString(9 + rowNum);
					else
						// if bottom row
						DataBase.sem_SWPerBal_data[sem][rowNum][colNum] = "Total";
				} else if (rowNum < DataBase.schoolGradeCount) {// Count total for the grade and period
					DataBase.sem_SWPerBal_data[sem][rowNum][colNum] = Integer
							.toString(countSections(sem, rowNum, colNum - 1));
					DataBase.sem_SWPerBal_data[sem][rowNum][DataBase.sem_SWPerBal_data[sem][rowNum].length - 1] = // And to gradeWide total
							Integer.toString(Integer
									.parseInt(DataBase.sem_SWPerBal_data[sem][rowNum][DataBase.sem_SWPerBal_data[sem][rowNum].length - 1])
									+ Integer
									.parseInt(DataBase.sem_SWPerBal_data[sem][rowNum][colNum]));
					DataBase.sem_SWPerBal_data[sem][DataBase.schoolGradeCount][colNum] = // Add to periodWide total
							Integer.toString(Integer
									.parseInt(DataBase.sem_SWPerBal_data[sem][DataBase.schoolGradeCount][colNum])
									+ Integer
									.parseInt(DataBase.sem_SWPerBal_data[sem][rowNum][colNum]));
					grandTotal = Integer
							.toString(Integer.parseInt(grandTotal)
									+ Integer
									.parseInt(DataBase.sem_SWPerBal_data[sem][rowNum][colNum]));
					//System.out.println("TOTAL is now: " + grandTotal);
				}
			}
		}
		DataBase.sem_SWPerBal_data[sem][DataBase.schoolGradeCount][DataBase.sem_SWPerBal_data[sem][0].length - 1] = grandTotal;
		sem_SWPerBalTables[sem]
				.setModel(new UneditableTableModel(
						DataBase.sem_SWPerBal_data[sem],
						sem_SWPerBal_headers[sem]));
	}
	public static int countSections(int sem, int grade, int period) {
		int sectionCount = 0;
		int gradeRowHeight = DataBase.sem_SWTable_data[sem].length
				/ DataBase.schoolPeriodCount / DataBase.schoolGradeCount;
		// ^The number of rows in a single grade has in any given period
		//System.out.println("Given from: " + grade + ", " + period + ", "
		//		+ gradeRowHeight);
		int rowStart = period * DataBase.schoolGradeCount * gradeRowHeight
				+ grade * gradeRowHeight;// The starting row coordinate for
		// counting
		int colStart = 2;// The starting column coordinate for counting
		//System.out.println("Counting from: " + rowStart + ", " + colStart);
		for (int rowNum = rowStart; rowNum < rowStart + gradeRowHeight; rowNum++) {// Iterate
			// through
			// the
			// row
			// range
			for (int colNum = colStart; colNum < DataBase.sem_SWTable_data[sem][rowNum].length - 1; colNum++) {// iterate
				// through
				// the
				// column
				// range
				if (DataBase.sem_SWTable_data[sem][rowNum][colNum] != null
						&& // If not null
						DataBase.findCourse(DataBase.sem_SWTable_data[sem][rowNum][colNum]) != null)// Double
					// check
					// for
					// good
					// measure
				{// Record to sectionCount
					sectionCount++;
				}
			}
		}
		//System.out.println("To: " + (rowStart + gradeRowHeight) + ", "
		//		+ (DataBase.sem_SWTable_data[sem][0].length - 1));
		//System.out.println("Returning: " + sectionCount);
		return sectionCount;
	}
	public void generateMasterTimeTable(){
		WindowListener SWexitListener = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				disposeAlike();
			}
		};
		sem_Frames = new JFrame[DataBase.schoolYearDivisions];
		sem_SWTables_SPanes = new JScrollPane[DataBase.schoolYearDivisions];
		sem_SWPerBalTables_SPanes = new JScrollPane[DataBase.schoolYearDivisions];
		sem_SWTables = new MasterSemTable[DataBase.schoolYearDivisions];
		sem_SWTable_headers = new String[DataBase.schoolYearDivisions][1];
		DataBase.sem_SWTable_data = new String[DataBase.schoolYearDivisions][1][1];
		sem_SWPerBalTables = new JTable[DataBase.schoolYearDivisions];
		sem_SWPerBal_headers = new String[DataBase.schoolYearDivisions][DataBase.schoolPeriodCount + 2];
		DataBase.sem_SWPerBal_data = new String[DataBase.schoolYearDivisions][DataBase.schoolGradeCount + 1][DataBase.schoolPeriodCount + 2];
		for (int sem = 0; sem < DataBase.schoolYearDivisions; sem++) {
			sem_SWTables[sem] = new MasterSemTable(sem);
			sem_Frames[sem] = new JFrame();
			sem_Frames[sem].addWindowListener(SWexitListener);
			sem_SWPerBalTables[sem] = new JTable();
			defineContents(sem);
			sem_Frames[sem].setTitle("Semester "
					+ Integer.toString(sem + 1) + " Master TimeTable");
			sem_SWTables[sem].setModel(new UneditableTableModel(
					DataBase.sem_SWTable_data[sem],
					sem_SWTable_headers[sem]));
			sem_SWTables[sem].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			for (int index = 0; index < sem_SWTable_headers[sem].length; index++) {
				if (index < 2) {// columns 0 and 1 set to 40 width and
					// unresizable
					sem_SWTables[sem].getColumnModel().getColumn(index)
					.setPreferredWidth(25);
					sem_SWTables[sem].getColumnModel().getColumn(index)
					.setResizable(false);
				} else {// columns 2+ set to 80, resizable
					sem_SWTables[sem].getColumnModel().getColumn(index)
					.setPreferredWidth(myColumnWidth);
				}
			}
			sem_SWTables_SPanes[sem] = new JScrollPane(sem_SWTables[sem]);
			sem_SWPerBalTables_SPanes[sem] = new JScrollPane(
					sem_SWPerBalTables[sem]);
			sem_SWPerBalTables_SPanes[sem].setPreferredSize(new Dimension(
					50, (int) (21.6 * DataBase.schoolGradeCount + 21.6)));
			sem_Frames[sem].setContentPane(new JSplitPane(
					JSplitPane.VERTICAL_SPLIT, sem_SWTables_SPanes[sem],
					sem_SWPerBalTables_SPanes[sem]));
			((JSplitPane) sem_Frames[sem].getContentPane())
			.setResizeWeight(1);// First split is given all extra
			// space
			sem_Frames[sem].pack();
			sem_Frames[sem].setBounds(sem * screenSize.width
					/ DataBase.schoolYearDivisions, 0, screenSize.width
					/ DataBase.schoolYearDivisions, screenSize.height - 40);

		}
		System.out.println("generateMasterTimeTListener has been fired");
	}
	class MasterSemTable extends JTable {// Colored table class for a full
		// semester
		public int sem;

		MasterSemTable(int sem) {// Set semester number on construction
			super();
			this.sem = sem;
		}

		public Component prepareRenderer(TableCellRenderer renderer,
				int row, int col) {
			Component c = super.prepareRenderer(renderer, row, col);
			if (getValueAt(row, col) != null
					&& !(getSelectedRow() == row && getSelectedColumn() == col)
					&& col > 1
					&& DataBase.findCourse((String) getValueAt(row, col)) != null) {
				c.setBackground(DataBase.findCourse(
						(String) getValueAt(row, col)).getDept().backGroundColor);
				c.setForeground(DataBase.findCourse(
						(String) getValueAt(row, col)).getDept().foreGroundColor);
			} else if (getSelectedRow() == row
					&& getSelectedColumn() == col
					&& DataBase.findCourse((String) getValueAt(row, col)) != null) {
				c.setBackground(new Color(
						DataBase.findCourse((String) getValueAt(row, col))
						.getDept().backGroundColor.getBlue(),
						DataBase.findCourse((String) getValueAt(row, col))
						.getDept().backGroundColor.getGreen(),
						DataBase.findCourse((String) getValueAt(row, col))
						.getDept().backGroundColor.getRed()));
				c.setForeground(foreGroundColor);
			} else {
				if ((row / (DataBase.sem_SWTable_data[sem].length / DataBase.schoolPeriodCount)) % 2 == 0) {
					if ((row / (DataBase.sem_SWTable_data[sem].length / (DataBase.schoolPeriodCount * DataBase.schoolGradeCount))) % 2 == 0)
						c.setBackground(new Color(240, 240, 240));
					else
						c.setBackground(Color.WHITE);
					c.setForeground(Color.BLACK);
				} else {
					if ((row / (DataBase.sem_SWTable_data[sem].length / (DataBase.schoolPeriodCount * DataBase.schoolGradeCount))) % 2 == 0)
						c.setBackground(new Color(250, 250, 170));
					else
						c.setBackground(new Color(250, 250, 190));
					c.setForeground(Color.BLACK);
				}
			}
			return c;
		}
	}
	class generateMasterTimeTListener implements ActionListener {// spawns a
		// window containing  all the courses  as scheduled
		public void actionPerformed(ActionEvent e) {
			Course_Manager_Main.window1.generateMasterTimeTable();
			for (int sem = 0; sem < DataBase.schoolYearDivisions; sem++) {
				sem_Frames[sem].setVisible(true);
			}
		}
	}

	public class generateMasterTimeTListener2 {// spawns a window containing all
		// the courses as
		// scheduled//TODO: TAG
		// generateMasterTimeTListener
		// Start
		// private JFrame sem_Frames[];
		// private JScrollPane sem_SWTables_SPanes[];
		// private JScrollPane sem_SWPerBalTables_SPanes[];

		private int highestRowCount = 0;

		private String[][] sem_SWTable_headers;

		private int countSections(int sem, int grade, int period) {
			int sectionCount = 0;
			int gradeRowHeight = DataBase.sem_SWTable_data[sem].length
					/ DataBase.schoolPeriodCount / DataBase.schoolGradeCount;
			//System.out.println("Given from: " + grade + ", " + period + ", "
			//		+ gradeRowHeight);
			// ^The number of rows in a single grade has in any given period
			int rowStart = period * DataBase.schoolGradeCount * gradeRowHeight
					+ grade * gradeRowHeight;// The starting row coordinate for
			// counting
			int colStart = 2;// The starting column coordinate for counting
			//System.out.println("Counting from: " + rowStart + ", " + colStart);
			for (int rowNum = rowStart; rowNum < rowStart + gradeRowHeight; rowNum++) {// Iterate
				// through
				// the
				// row
				// range
				for (int colNum = colStart; colNum < DataBase.sem_SWTable_data[sem][rowNum].length - 1; colNum++) {// iterate
					// through
					// the
					// column
					// range
					if (DataBase.sem_SWTable_data[sem][rowNum][colNum] != null
							&& // If not null
							DataBase.findCourse(DataBase.sem_SWTable_data[sem][rowNum][colNum]) != null)// Double
						// check
						// for
						// good
						// measure
					{// Record to sectionCount
						sectionCount++;
					}
				}
			}
			//System.out.println("To: " + (rowStart + gradeRowHeight) + ", "
			//		+ (DataBase.sem_SWTable_data[sem][0].length - 1));
			//System.out.println("Returning: " + sectionCount);
			return sectionCount;
		}

		private void calculateDimensions(int semester) {
			DataBase.colCountSum = 0;// reset
			DataBase.rowCountSum = 0;// reset
			// DataBase.levelColCount = new
			// int[DataBase.levels.length][semester];//reset
			int[] levelRowCount = new int[DataBase.levels.length];
			int[] highestSectionCount = new int[DataBase.levels.length];// highest
			// number
			// of
			// cells
			// that
			// will
			// be
			// filled
			// by
			// sections
			int[] rowSectionCount = new int[DataBase.levels.length];// number of
			// cells
			// that will
			// be filled
			// by
			// sections
			// at a
			// giver row
			highestRowCount = 0;// number minimum number of rows required to
			// accommodate the highestSectionCount
			// when used (multiplied) along with DataBase.levelColCount for the
			// level with the highestSectionCount
			for (int levelsIndex = 0; levelsIndex < DataBase.levels.length; levelsIndex++) {
				rowSectionCount = new int[DataBase.levels.length];// resets on
				// iteration//Obsolete?
				for (int rowNum = 0; rowNum < DataBase.timeT[0].length; rowNum++) {// iterates
					// through
					// each
					// row
					rowSectionCount[levelsIndex] = 0;// resets on iteration
					for (int deptPos = 0; deptPos < DataBase.departments.length; deptPos++) {// iterates
						// through
						// each
						// Department
						for (int xCoordinate = 0; xCoordinate < DataBase.timeT[0][rowNum].length; xCoordinate++) {// iterates
							// through
							// each
							// column
							if (DataBase.timeT[deptPos][rowNum][xCoordinate][semester] != null
									&& DataBase.timeT[deptPos][rowNum][xCoordinate][semester]
											.getLevel()
											.equals(DataBase.levels[levelsIndex])) {
								rowSectionCount[levelsIndex]++;
							}// increase count if level match
						}// check each row
					}// ^and each department
					if (rowSectionCount[levelsIndex] > highestSectionCount[levelsIndex]) {// If
						// a
						// row
						// has
						// a
						// higher
						// sectionCount
						// than
						// the
						// current
						// highest
						// recorded,
						// replace
						highestSectionCount[levelsIndex] = rowSectionCount[levelsIndex];
					}
				}// Check every row
			}// Do this for every level

			// DataBase.levelColCount = new
			// int[DataBase.levels.length][semester];//reset
			// Find the number of columns preferred to accommodate a number of
			// cells to be filled(highestSectionCount) of every level
			// The the number of columns preferred is found by starting with 1
			// row and 1 column,
			// If more are needed, a row is added first, then a column,
			// such that a formation with 3 rows 2 columns is tested before one
			// with 3 columns is tested
			// 1 cell -> 1 column (1 row), 2 cells-> 1 column (2 rows), 3
			// cells-> 2 columns (2 rows),
			// 6 cells -> 2 columns (3 rows), 7 cells ->3 columns(3 rows), 11
			// cells -> 3 columns (4 rows) and so on
			for (int levelsIndex = 0; levelsIndex < DataBase.levels.length; levelsIndex++) {
				DataBase.levelColCount[levelsIndex][semester] = 0;// start at
				// simulating
				// 0 columns
				while (DataBase.levelColCount[levelsIndex][semester]
						* DataBase.levelColCount[levelsIndex][semester] <= highestSectionCount[levelsIndex]
								- DataBase.levelColCount[levelsIndex][semester]) {// Increase
					// the
					// number
					// of
					// columns
					// until
					// it
					// can
					// accommodate
					// the
					// given
					// number
					// of
					// cells
					// of
					// the
					// level
					DataBase.levelColCount[levelsIndex][semester]++;
				}
			}// Do this for every level
			for (int levelsIndex = 0; levelsIndex < DataBase.levels.length; levelsIndex++) {// calculate
				// local
				// rowCounts
				// based
				// on
				// number
				// of
				// columns
				// and
				// highest
				// section
				// count
				if (DataBase.levelColCount[levelsIndex][semester]
						* DataBase.levelColCount[levelsIndex][semester] > highestSectionCount[levelsIndex]) {
					levelRowCount[levelsIndex] = DataBase.levelColCount[levelsIndex][semester];
				} else {
					levelRowCount[levelsIndex] = DataBase.levelColCount[levelsIndex][semester] + 1;
				}
				if (highestRowCount < levelRowCount[levelsIndex]) {// then find
					// the
					// highest
					// rowCount
					// between
					// the
					// levels as
					// this is
					// being
					// done
					highestRowCount = levelRowCount[levelsIndex];
				}
			}
			for (int levelsIndex = 0; levelsIndex < DataBase.levels.length; levelsIndex++) {// revise
				// colCounts
				// of
				// the
				// levels
				// to
				// according
				// to
				// the
				// highest
				// rowCount
				// that
				// was
				// found.
				DataBase.levelColCount[levelsIndex][semester] = (int) highestSectionCount[levelsIndex]
						/ highestRowCount;
				if (DataBase.levelColCount[levelsIndex][semester] == 0
						|| highestSectionCount[levelsIndex]
								/ DataBase.levelColCount[levelsIndex][semester] > highestRowCount) {// if
					// there
					// is
					// a
					// remainder
					// for
					// the
					// division
					// operation
					// (if
					// highestSectionCount
					// does
					// not
					// fit
					// in
					// a
					// DataBase.levelColCount[levelsIndex]
					// by
					// highestRowCount
					// grid)
					DataBase.levelColCount[levelsIndex][semester]++;// Add an
					// extra
					// column
				}
				//				System.out.println("DataBase.levelColCount " + levelsIndex
				//						+ ": " + DataBase.levelColCount[levelsIndex]);
				DataBase.colCountSum = DataBase.colCountSum
						+ DataBase.levelColCount[levelsIndex][semester];// Sum
				// all
				// the
				// final
				// ColCounts
				// together
			}
			// multiply the highestRowCount by the period and grade count to
			// find totalrowcount
			DataBase.rowCountSum = highestRowCount * DataBase.schoolGradeCount
					* DataBase.schoolPeriodCount;
			DataBase.colCountSum = DataBase.colCountSum + 2 + 1;// Add 2 for
			// period and
			// grade column,
			// add 1 for
			// section count
			// column
			// These will be the final dimensions of the table
			System.out.println("rowCountSum: " + DataBase.rowCountSum);
			System.out.println("highestRowCount: " + highestRowCount);
		}

		private void display(String courseName, int sem, int rowNum) {
			int level = DataBase.findLevelPos(DataBase.findCourse(courseName)
					.getLevel());
			int col = 0;// column in which the courseName will be placed
			int row = 0;// row in which the courseName will be placed
			int colStart;// initial col coordinate of the column range where the
			// courseName will be placed
			int rowStart;// initial row coordinate of the row range where the
			// courseName will be placed
			boolean done = false;// indicates if placement has succeeded
			// calculate start of column range
			colStart = 2;// start at 2 since 0 and 1 are P and Gr columns
			for (int levelsIndex = 1; levelsIndex <= level; levelsIndex++) {
				colStart = colStart
						+ DataBase.levelColCount[levelsIndex - 1][sem];
			}
			// calculate start of row range
			// System.out.println(courseName + " period: " + period + " grade: "
			// + grade + " level: " + level);
			rowStart = rowNum * highestRowCount;
			// calculate the actual accurate col and row to place it in
			for (col = colStart; !done
					&& col < colStart + DataBase.levelColCount[level][sem]; col++) {
				for (row = rowStart; !done && row < rowStart + highestRowCount; row++) {
					if (DataBase.sem_SWTable_data[sem][row][col] == null) {
						DataBase.sem_SWTable_data[sem][row][col] = courseName;
						done = true;// ends search loops
					}
				}// iterates through each possible row in the grade's range
			}// iterates through each possible column in the level's range
			// System.out.println(courseName + " placed at: " + row + ", " +
			// col);
		}

		//TODO: Check Local Use
		private void defineContents(int sem) {
			//System.out.println("***sem: " + sem);
			calculateDimensions(sem);
			// set sem#Header to appropriate length
			sem_SWTable_headers[sem] = new String[DataBase.colCountSum];
			for (int headerIndex = 2, levelsIndex = 0, writeCounter = 0; headerIndex < sem_SWTable_headers[sem].length
					&& levelsIndex < DataBase.levels.length; headerIndex++) {// write
				// onto
				// the
				// header
				// string[]
				sem_SWTable_headers[sem][headerIndex] = DataBase.levels[levelsIndex];
				writeCounter++;
				// ^write name of the level
				if (writeCounter == DataBase.levelColCount[levelsIndex][sem]) {// Once
					// the certain number of columns have received the name of the 
					// level, move on to writing the name of the next level
					levelsIndex++;
					writeCounter = 0;
				}
			}
			sem_SWTable_headers[sem][0] = "P";
			sem_SWTable_headers[sem][1] = "Gr";
			sem_SWTable_headers[sem][sem_SWTable_headers[sem].length - 1] = "Total Number";
			DataBase.sem_SWTable_data[sem] = new String[DataBase.rowCountSum][DataBase.colCountSum];
			for (int rowNum = 0; rowNum < DataBase.timeT[sem].length; rowNum++) {// iterates
				// through
				// each
				// row
				for (int xCoordinate = 0; xCoordinate < DataBase.timeT[sem][rowNum].length; xCoordinate++) {// iterates
					// through
					// each
					// column
					for (int deptPos = 0; deptPos < DataBase.departments.length; deptPos++) {// iterates
						// through
						// each
						// department
						if (DataBase.timeT[deptPos][rowNum][xCoordinate][sem] != null)
							display(DataBase.timeT[deptPos][rowNum][xCoordinate][sem].name,
									sem, rowNum);
					}
				}
			}
			for (int rowNum = 0; rowNum < DataBase.rowCountSum; rowNum++) {
				if (rowNum % (DataBase.schoolGradeCount * highestRowCount) == 0)// Write
					// periods
					DataBase.sem_SWTable_data[sem][rowNum][0] = Integer
					.toString(rowNum
							/ (DataBase.schoolGradeCount * highestRowCount)
							+ 1);
				if (rowNum % (highestRowCount) == 0)// Write grades
					DataBase.sem_SWTable_data[sem][rowNum][1] = Integer
					.toString(((rowNum / highestRowCount) % highestRowCount) + 9);
			}
			// calculate section totals per period
			for (int rowStart = 0, sectionCount = 0; rowStart < DataBase.rowCountSum; rowStart += highestRowCount) {
				sectionCount = 0;// reset
				for (int colNum = 2; colNum < DataBase.colCountSum - 1; colNum++) {
					for (int rowNum = rowStart; rowNum < rowStart
							+ highestRowCount; rowNum++) {// caculate by adding
						// the number of
						// non-null values
						// within the range
						// of the section
						if (DataBase.sem_SWTable_data[sem][rowNum][colNum] != null) {
							sectionCount = sectionCount + 1;
						}
					}
				}
				// add to table
				DataBase.sem_SWTable_data[sem][rowStart + highestRowCount - 1][DataBase.colCountSum - 1] = Integer
						.toString(sectionCount);
			}

			//			for (int rowNum = 0; rowNum < DataBase.sem_SWTable_data[sem].length; rowNum++) {
			//				for (int colNum = 2; colNum < DataBase.colCountSum - 1; colNum++) {
			//					System.out
			//					.print(DataBase.sem_SWTable_data[sem][rowNum][colNum]
			//							+ ", ");
			//				}
			//				System.out.println();
			//			}
		}

		public void actionPerformed(ActionEvent e) {
			//			sem_SWTable_headers = new String[DataBase.schoolYearDivisions][1];
			//			DataBase.sem_SWTable_data = new String[DataBase.schoolYearDivisions][1][1];
			//			for (int sem = 0; sem < DataBase.schoolYearDivisions; sem++) {
			//				defineContents(sem);
			//			}
			//			System.out.println("generateMasterTimeTListener has been fired");
			Course_Manager_Main.window1.generateMasterTimeTable();
		}
	}

}	
