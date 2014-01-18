////////////////////////////////////////////////////////////////////////////////
/*
 * Class: Window 2 Class
 * Description: Constructs the second window and enables its functionalities
 * 
 * Authors: 
 * 	Troylan Tempra Jr
 * 
 * Date Created:December 6, 2012
 * Date Modified: January 20, 2012
 */
////////////////////////////////////////////////////////////////////////////////
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;
import javax.swing.event.*;



public class Course_Manager_Window2 extends JFrame {
	private static final long serialVersionUID =
			-4264469601995112396L;//TODO: Serialize
	///////////////////////////////////////////////////////////////////Variables
	private String selected_CourseName;
	//Contains the name of the currently selected course


	/////////////////////////////////////////////////////////////////GUI Objects
	////TOP - Filters
	JList<String> deptFilterList = new JList<String>(DataBase.dept_filters);
	//Left Side FilterList
	JScrollPane deptFiltersList_Scroller = new JScrollPane(deptFilterList);
	//Left Side ScrollPane container for left hand side FilterList
	JList<String> lvlFilterList = new JList<String>(DataBase.lvl_filters);
	//Right Side FilterList
	JScrollPane lvlFiltersList_Scroller = new JScrollPane(lvlFilterList);
	//Right Side ScrollPane, container for right side FilterList
	JPanel filtersPanel = new JPanel(new GridBagLayout());
	//Panel to hold the 2 filters

	////MIDDLE 1 - Course SelectionList
	public static JList<String> courseSelectionList = 
			new JList<String>(DataBase.courseSelections);
	//List for courseNames
	JScrollPane courseSelectionList_Scroller = 
			new JScrollPane(courseSelectionList);
	//ScrollPane for above list
	JLabel selectedCourseLabel = new JLabel(selected_CourseName);

	////MIDDLE 2 - Data Editing Buttons
	JButton addSectionButton = new JButton ("Add/Replace Section");
	//Button to Add Courses
	JButton removeSectionButton = new JButton ("Remove Section");
	//Button to Remove Courses


	////Bottom
	static JButton automateButton = new JButton("Generate Time Table");
	//Button to start automatically placing courses

	private GridBagConstraints gbc = new GridBagConstraints();

	///////////////////////////////////////////////////////////Listener Objects
	ListSelectionModel deptFilterSelectionModel = 
			deptFilterList.getSelectionModel();
	//enables Interface for deptFitlerList
	ListSelectionModel lvlFilterSelectionModel = 
			lvlFilterList.getSelectionModel();
	//enables Interface for lvlFitlerList
	ListSelectionModel courseSelectionModel = 
			courseSelectionList.getSelectionModel();
	//enables Interface for  courseSelectionList

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
	//Formatting and adding same as in Course_Manager_Window1
	/////////////////////////////////////////////////////////////////Constructor
	public Course_Manager_Window2(){
		setTitle("Course Lister and Controls");
		setLayout( new GridBagLayout());
		WindowListener exitListener = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				Course_Manager_Main.window1.courseListWindow.setEnabled(true);
			}
		};

		addWindowListener(exitListener);
		gbc.insets = new Insets(0,0,0,0);//set no insets
		DataBase.resetArray("ALL");
		/////////Top:
		//Apply Listening
		deptFilterSelectionModel.addListSelectionListener(
				new FiltersListener("Dept"));//Add Dept type FiltersListener
		deptFilterSelectionModel.setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);//Set to SINGLE_SELECTION
		lvlFilterSelectionModel.addListSelectionListener(
				new FiltersListener("Lvl"));//Add Lvl type FiltersListener
		lvlFilterSelectionModel.setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);//Set to SINGLE_SELECTION

		////Formatting and adding
		courseSelectionModel.addListSelectionListener(
				new CourseSelectionListener());//Add CourseSelectionListener
		lvlFilterSelectionModel.setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);//Set to SINGLE_SELECTION

		deptFilterList.setListData(DataBase.dept_filters);
		lvlFilterList.setListData(DataBase.lvl_filters);
		setGBCPos(0,0,1,1);
		setGBCWgt(1,1);
		filtersPanel.add(deptFiltersList_Scroller, gbc);
		setGBCPos(1,0,1,1);
		filtersPanel.add(lvlFiltersList_Scroller, gbc);
		filtersPanel.setBorder(
				BorderFactory.createTitledBorder("Filter Courses:"));
		gbc.fill = GridBagConstraints.HORIZONTAL;
		setGBCPos(0,0,2,1);
		setGBCWgt(0,0);
		add(filtersPanel, gbc);

		/////////Middle 1:
		//Apply Listening

		////Formatting and adding
		courseSelectionList.setListData(
				DataBase.courseSelections);
		setGBCPos(0,1,2,1);
		setGBCWgt(1,1);
		gbc.fill = GridBagConstraints.BOTH;
		courseSelectionList_Scroller.setBorder(
				BorderFactory.createTitledBorder("Select a Course:"));
		add(courseSelectionList_Scroller, gbc);
		setGBCWgt(0,0.2);
		setGBCPos(0,2,2,1);
		selectedCourseLabel.setBorder(BorderFactory.createTitledBorder("Currently Selected Course:"));
		add(selectedCourseLabel, gbc);

		////////Middle 2:
		////Formatting and adding
		setGBCWgt(0,0);
		setGBCPos(0,3,1,1);
		addSectionButton.setEnabled(false);//Disable on startup
		addSectionButton.addActionListener(new addSectionListener());
		add(addSectionButton, gbc);
		setGBCPos(1,3,1,1);
		removeSectionButton.addActionListener(new removeSectionListener());
		add(removeSectionButton, gbc);

		gbc.insets = new Insets(0,0,0,0);//reset to default
		////////Bottom:
		////Formatting and adding
		setGBCWgt(0,0);
		setGBCPos(0,4,2,1);
		add(new JSeparator(), gbc);
		setGBCPos(0,5,2,1);
		gbc.insets = new Insets(10, 10, 10, 10);
		automateButton.setPreferredSize(new Dimension(260,50));
		automateButton.addActionListener(new automateListener());
		add(automateButton, gbc);

		/////////Final Constructor Calls
		pack();//Packs the contents
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(screenSize.width-300,0,300, screenSize.height-100);
		setVisible(true);////^Sets the location to far right
	}

	/////////////////////////////////////////////////////////////ListenerClasses

	//////FiltersListener
	class FiltersListener implements ListSelectionListener
	{//Listens for change in filters
		private String type;
		//Denotes if this filter listener is for Dept or Level filter
		public FiltersListener(String type)
		{
			this.type = type;
		}
		public void valueChanged(ListSelectionEvent e) {
			ListSelectionModel lsm = (ListSelectionModel)e.getSource();//
			for (int index = lsm.getMinSelectionIndex(); 
					index <= lsm.getMaxSelectionIndex(); index++) {
				if (lsm.isSelectedIndex(index)) {//Scan for selected indexes
					if(type.equals("Dept"))
						//If department filter listener, 
						//update department filter to selected department
					{
						DataBase.selected_DeptFilter = DataBase.dept_filters[index];
						//System.out.println("Selected Department Filter: " + selected_DeptFilter);//Console Prompt
					}
					else if(type.equals("Lvl"))//If level filter listener, 
						//update level filter to selected level
					{
						DataBase.selected_LvlFilter = DataBase.lvl_filters[index];
						//System.out.println("Selected Level Filter: " + selected_LvlFilter);//Console Prompt
					}
				}
			}
			DataBase.refilter_CourseSelections(
					DataBase.selected_DeptFilter, DataBase.selected_LvlFilter);
			courseSelectionList.setListData(DataBase.courseSelections);
		}
	}
	////////////////////////////////////////////////////////////////////////////////
	//////CourseSelectionListener
	class CourseSelectionListener implements ListSelectionListener
	{//Listener updates selected course when clicked
		public void valueChanged(ListSelectionEvent e) 
		{
			//UNCOMMENT TO IMPLEMENT SAFETY SEARCH
			String courseName = "";//holds the name of selected course
			int courseNameStartIndex = 0;
			ListSelectionModel lsm = (ListSelectionModel)e.getSource();
			for (int index = lsm.getMinSelectionIndex();
					index <= lsm.getMaxSelectionIndex(); index++) 
			{
				if (lsm.isSelectedIndex(index)) 
				{
					for(int index2 = 0;
							index2<DataBase.courseSelections[index].length();
							index2++)
					{//search for ' ' and record first occurrence
						if(DataBase.courseSelections[index].charAt(index2) == ' ')
						{
							courseNameStartIndex = index2 + 1;//record occurrence 
							//(index2 is location of ' ' 
							//our concern is start of name so 1 is added)
							index2 = DataBase.courseSelections[index].length();
							//end ' ' search loop
						}
					}
					//TODO: If courseSelections[] naming convention changes, 
					//change this courseName isolation algorithm
					courseName = DataBase.courseSelections[index].substring(
							courseNameStartIndex);
				}
			}
			selected_CourseName = courseName;
			//DataBase.findCourse(courseName).getString("name");
			//Use the above line to replace the line above it if you wish
			//an additional safety search to isolate for the existence of
			//A course with this name
			System.out.println("Selected Course: " + selected_CourseName);
			if(selected_CourseName.equals(""))
			{//disable button if there is not course selected
				System.out.println("GhostString returned True");
				addSectionButton.setEnabled(false);
			}
			else
			{//enable button only if a course is selected
				addSectionButton.setEnabled(true);
			}
			selectedCourseLabel.setText(selected_CourseName);
			selectedCourseLabel.setForeground(new Color(60, 0, 0));
			//Console Prompt
		}
	}
	class addSectionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			boolean table_selected = false;//indicates if a table is selected
			boolean departmentIsMatched = false;//indicates if department is appropriate for course
			boolean levelIsMatched = false;//indicates it the level of the selected column is matched to the course level
			//boolean gradeIsMatched = false;//indicates if the grade of the selected row is matched to the course
			int levelNum = DataBase.findLevelPos(DataBase.findCourse(selected_CourseName).level);
			int gradeNum = DataBase.findCourse(selected_CourseName).getGrade();
			int deptPos = -1;
			int sem = -1;
			int timeTxCoordinateTO = -1;

			if(selected_CourseName!=null)
			{
				if( DataBase.selected_Department != null &//first check if a dept is selected
						DataBase.selected_Department.equals(DataBase.findCourse(selected_CourseName).getDept().getName()))
				{//then check if department selected is matched with the course
					departmentIsMatched = true;
					deptPos = DataBase.findDeptPos(DataBase.selected_Department);//record deptPos
				}
				else{//Invalidity prompt
					System.out.println("Invalid location, department mismatch");
				}
				//Then identify the selected table
				JTable table = new JTable();//used to access a table
				if(Course_Manager_Main.window1.sem1_table.getSelectedColumn()>1 &//if appropriate column is selected 
						Course_Manager_Main.window1.last_selected_table == 0	){//and sem1 table is selected
					System.out.println("Selected table is semester 1");
					sem = 0;
					table = Course_Manager_Main.window1.sem1_table;//access table 1
					table_selected = true;//indicate table is selected
				}
				else if(Course_Manager_Main.window1.sem2_table.getSelectedColumn()>1 &
						Course_Manager_Main.window1.last_selected_table == 1){
					System.out.println("Selected table is semester 2");
					sem = 1;
					table = Course_Manager_Main.window1.sem2_table;//access table 2
					table_selected = true;//indicate table is selected
				}else{
					//No table is selected
				}

					//Check if level, grade and department is appropriate
					if(levelNum*DataBase.colCountPerLevel+1<table.getSelectedColumn() & table.getSelectedColumn()<levelNum*DataBase.colCountPerLevel+2+DataBase.colCountPerLevel)
					{//checks if adding to valid level
						levelIsMatched = true;
					}
					else{//Invalidity prompt
						System.out.println("Invalid location, level mismatch");
					}
//					if(table.getSelectedRow()%DataBase.schoolGradeCount == gradeNum)
//					{//checks if adding to valid grade
//						gradeIsMatched = true;
//					}
//					else{//Invalidity prompt
//						System.out.println("Invalid location, grade mismatch; " + gradeNum);
//					}

				if(table_selected & departmentIsMatched & levelIsMatched 
						//& gradeIsMatched//Now rescans for validity and auto transports instead
						)
				{//^If course is valid for selected position
					if(table.getValueAt(DataBase.selectedRow, DataBase.selectedCol) == null)
					{//Replace null if nothing is at the destination
						for(int index = 0; index<DataBase.timeT[deptPos][DataBase.selectedRow].length; index++)
						{//one-dimensionally iterates through a part of timeT array and
							if(DataBase.timeT[deptPos][DataBase.selectedRow][index][sem]==null)
							{//locates first null value
								timeTxCoordinateTO = index;
								index = DataBase.timeT[deptPos].length;
							}
						}
						table.setValueAt(selected_CourseName, DataBase.selectedRow, DataBase.selectedCol);
						System.out.println("Data ADDITION occured!");
						DataBase.timeT[deptPos][DataBase.selectedRow][timeTxCoordinateTO][sem] = DataBase.findCourse(selected_CourseName);
						System.out.println(selected_CourseName + "** " + DataBase.findCourse(selected_CourseName));
					}
					else
					{//Replace data if not null
						for(int index = 0; index<DataBase.timeT[deptPos][DataBase.selectedRow].length; index++)
						{//one-dimensionally iterates through a part of timeT array and
							if(DataBase.timeT[deptPos][DataBase.selectedRow][index][sem].getName().equals(
									DataBase.findCourse((String)table.getValueAt(DataBase.selectedRow, DataBase.selectedCol)).getName()))
							{//locates first section with appropriate courseName value
								timeTxCoordinateTO = index;
								index = DataBase.timeT[deptPos].length;
							}
						}
						DataBase.timeT[deptPos][DataBase.selectedRow][timeTxCoordinateTO][sem] = DataBase.findCourse(selected_CourseName);
						table.setValueAt(selected_CourseName, DataBase.selectedRow, DataBase.selectedCol);
						System.out.println("Data REPLACEMENT occured!");
					}
				}
			}
			Course_Manager_Main.window1.refreshDisplay();
		}
	}
	class removeSectionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			boolean table_selected = false;//indicates if a table is selected
			int deptPos = -1;
			int sem = -1;
			int timeTxCoordinateTO = -1;
			
			JTable table = new JTable();//used to access a table
			if(Course_Manager_Main.window1.sem1_table.getSelectedColumn()>1 &//if appropriate column is selected 
					Course_Manager_Main.window1.last_selected_table == 0	){//and sem1 table is selected
				System.out.println("Selected table is semester 1");
				sem = 0;
				table = Course_Manager_Main.window1.sem1_table;//access table 1
				table_selected = true;//indicate table is selected
			}
			else if(Course_Manager_Main.window1.sem2_table.getSelectedColumn()>1 &
					Course_Manager_Main.window1.last_selected_table == 1){
				System.out.println("Selected table is semester 2");
				sem = 1;
				table = Course_Manager_Main.window1.sem2_table;//access table 2
				table_selected = true;//indicate table is selected
			}else{
				//No table is selected
			}
			if(table_selected)
			{//table sets value appropriately
				deptPos = DataBase.findDeptPos(DataBase.selected_Department);//record deptPos
				if(table.getValueAt(DataBase.selectedRow, DataBase.selectedCol) != null)
				{
					for(int index = 0; index<DataBase.timeT[deptPos][DataBase.selectedRow].length; index++)
					{//one-dimensionally iterates through a part of timeT array and
						if(DataBase.timeT[deptPos][DataBase.selectedRow][index][sem]!=null &&
								DataBase.timeT[deptPos][DataBase.selectedRow][index][sem].getName().equals(
								DataBase.findCourse((String)table.getValueAt(DataBase.selectedRow, DataBase.selectedCol)).getName()))
						{//locates first section with appropriate courseName value
							timeTxCoordinateTO = index;
							index = DataBase.timeT[deptPos].length;
						}
					}
					DataBase.timeT[deptPos][DataBase.selectedRow][timeTxCoordinateTO][sem] = null;
					table.setValueAt(selected_CourseName, DataBase.selectedRow, DataBase.selectedCol);
					System.out.println("Data REMOVAL occured!");
				}
			}
			Course_Manager_Main.window1.refreshDisplay();
		}
	}
	public void setAutoListener() {
		// TODO Auto-generated method stub
		automateButton.addActionListener(new automateListener());
	}
	class automateListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			if(DataBase.isDataBaseEmpty)
			{
				JOptionPane.showMessageDialog(null,
						"No Registed Data, Automation cannot be initiated!",
						"No Data",
						JOptionPane.ERROR_MESSAGE);
			}else{
				int period = DataBase.schoolPeriodCount;
				int grade = DataBase.schoolGradeCount;
				int numberOfDepartments = DataBase.departments.length;
				int numberOfRowsPerDept = grade*period;
				int numberOfColsPerDept = 3*DataBase.levels.length;//3 slots per level per period
				int numberOfSemesters = 2;
				CourseMethods obj = new CourseMethods(numberOfDepartments, numberOfRowsPerDept, numberOfColsPerDept, numberOfSemesters);
			obj.manageCourse();
			
			DataBase.autoRepatchVisibility();
			DataBase.updateSchoolTotal();
			DataBase.updateCourseStatuses();
			Course_Manager_Main.window1.resetSchoolTotals();
			DataBase.refilter_CourseSelections("ALL", "ALL");
			courseSelectionList.setListData(DataBase.courseSelections);
			if(DataBase.selected_Department == null)
			{
				DataBase.selected_Department = DataBase.departments[0].getName();
			}
			Course_Manager_Main.window1.refreshDisplay();
			Course_Manager_Main.window1.departmentDisplayOptions.setEnabled(true);
			Course_Manager_Main.window1.togglePeriodicTotalTables.setEnabled(true);
			Course_Manager_Main.window1.export.setEnabled(true);
			Course_Manager_Main.window1.generateMasterTimeTable();
			JOptionPane.showMessageDialog(null,
					"Automation was successful!",
					"Automation Complete",
					JOptionPane.INFORMATION_MESSAGE);
			
			}
			
		}
	}

}
