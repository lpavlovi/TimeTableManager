/* Class: IIcourses
 * Author: Luka Pavlovic Ed. Troylan Tempra Jr.
 * Last Modified:20/1/2013
 * Description: Final window for new project option
 */
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class IIcourses extends JFrame {
	// declarations

	// declare panel
	JPanel panel = new JPanel(new GridBagLayout());

	// declare Labels
	private JLabel glb; // general Label - used for everything
	// declare text Fields
	private JTextField courseCode_tf = new JTextField(15); // input course code
	private JTextField sections_tf = new JTextField(15); // Number of sections

	// declare buttons ----// Purpose:
	private JButton addCourspreviewCode_Btn; // Add Course
	private JButton undo_Btn; // Undo
	private JButton clearAll_Btn; // Clear All
	private JButton updateSelection_Btn; // Update Selection
	private JButton previewCode_Btn; // Preview Code
	// Back Button
	private JButton back_Btn = new JButton("Back");
	// Finish Button
	private JButton finish_Btn = new JButton("Finish");
	// Cancel Button
	private JButton cancel_Btn = new JButton("Cancel");

	// declare radio buttons : Used for selecting the level of a course
	private JRadioButton rb_ac; // academic
	private JRadioButton rb_ap; // applied
	private JRadioButton rb_op; // open
	private JRadioButton rb_ld; // locally developed
	private JRadioButton rb_ep; // ESP

	private ButtonGroup gr_lvl = new ButtonGroup(); // groups buttons together

	// declare radio buttons : Used for selecting the grade level of a course
	private JRadioButton rb_09; // grade 9
	private JRadioButton rb_10; // grade 10
	private JRadioButton rb_11; // grade 11
	private JRadioButton rb_12; // grade 12
	private ButtonGroup gr_grade = new ButtonGroup(); // grade button group

	// declare check boxes - used for period restrictions
	JCheckBox cb_1 = new JCheckBox("1"); // Period 1
	JCheckBox cb_2 = new JCheckBox("2"); // Period 2
	JCheckBox cb_3 = new JCheckBox("3"); // Period 3
	JCheckBox cb_4 = new JCheckBox("4"); // Period 4
	JCheckBox cb_5 = new JCheckBox("5"); // Period 5 (Conditional)

	// declare combo boxes
	// ---- : departments
	private JComboBox<String> a_cb = new JComboBox<String>(
			secondaryDataBase.deptArray);
	// ---- : room types
	private JComboBox<String> b_cb = new JComboBox<String>(
			secondaryDataBase.roomArray);

	// declare insets
	private Insets defaultInsets = new Insets(5, 5, 5, 5);

	private JSeparator sep2 = new JSeparator(JSeparator.HORIZONTAL);

	// declare variables and strings
	private int rowClicked = 0;
	private String[] restrictionsArray;
	public static String[][] dataStrArray;
	private String[] dataSimplifiedArray;
	public static int deptInt;
	public boolean[] perPrefBool;
	private String mode1 = "";

	// declare the object array for the table

	public static Object[][] data = null;

	// declare tables
	private JTable courseTable;
	private DefaultTableModel model;

	// declare labels
	TitledBorder pr_title = new TitledBorder("Period Restrictions");
	TitledBorder level_title = new TitledBorder("Select Level");
	TitledBorder grade_title = new TitledBorder("Select Grade");

	// declare List Selection Model
	private ListSelectionModel rowSM;
	private ListSelectionModel lsm;

	// the finish button sets up the data array, writes it to a file, and sets
	// up the timetable for editing
	private static String coursesWithInvalidity = "";

	public static boolean loadMe = false;

	// -------------------------------------------------------------------------------------------Constructor

	public IIcourses(String mode) {

		// set the title
		super("Add Courses");

		// give it a black border
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		// make a new GridBagConstraints
		GridBagConstraints gbc = new GridBagConstraints();
		// set this as content pane
		setContentPane(panel);
		// set size
		// panel.setPreferredSize(new Dimension(550, 400));
		// set exit
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// Set insets
		gbc.insets = defaultInsets;
		mode1 = mode;
		// set the weights
		gbc.weightx = 0.0;
		gbc.weighty = 1.0;
		// set anchor
		gbc.anchor = GridBagConstraints.CENTER;
		// set the fill
		gbc.fill = GridBagConstraints.BOTH;
		// set the grid width to 1
		gbc.gridwidth = 1;
		// initialize integer i
		int i = 0;

		// ---------------Adding

		// add label: Instructions
		glb = new JLabel("<html>Enter a course and its details</html>");
		gbc.gridx = 0;
		gbc.gridy = i;
		gbc.gridwidth = 2;
		panel.add(glb, gbc);

		// add JPANEL: Course List
		gbc.gridx = 2;
		gbc.gridy = i;
		gbc.gridheight = 10;
		gbc.weightx = 1.0;
		panel.add(addInfoPanel(), gbc);

		i++;// 1
		gbc.gridheight = 1;
		gbc.gridwidth = 1;

		gbc.weightx = 0.0;
		// add label: Course Code
		glb = new JLabel("<html>Enter Course Code:</html>");
		gbc.gridx = 0;
		gbc.gridy = i;
		panel.add(glb, gbc);

		// add text field: courseCode_tf - Input Course Code
		gbc.gridx = 1;
		gbc.gridy = i;
		panel.add(courseCode_tf, gbc);

		i++;// 2

		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		// add label: Select Department
		glb = new JLabel("<html>Select Department:</html>");
		gbc.gridx = 0;
		gbc.gridy = i;
		panel.add(glb, gbc);

		// add combo box: a_cb - Departments
		gbc.gridx = 1;
		gbc.gridy = i;
		panel.add(a_cb, gbc);

		i++;// 3

		// add label: Select room type
		glb = new JLabel("<html>Select Room Type:</html>");
		gbc.gridx = 0;
		gbc.gridy = i;
		panel.add(glb, gbc);

		// add combo box: b_cb - Room Types
		gbc.gridx = 1;
		gbc.gridy = i;
		panel.add(b_cb, gbc);

		i++;// 4

		gbc.gridx = 0;
		gbc.gridy = i;
		gbc.gridwidth = 2;
		panel.add(levelPanel(), gbc);

		i++;// 6

		gbc.gridx = 0;
		gbc.gridy = i;
		gbc.gridwidth = 2;
		panel.add(gradesPanel(), gbc);

		i++;// 5

		gbc.gridx = 0;
		gbc.gridy = i;
		gbc.gridwidth = 2;
		panel.add(periodRestrictions(), gbc);

		i++; // 6

		// add label: Number of sections
		glb = new JLabel("<html>Enter Number of<p>Sections:</html>");
		gbc.gridx = 0;
		gbc.gridy = i;
		panel.add(glb, gbc);

		gbc.gridx = 1;
		gbc.gridy = i;
		panel.add(sections_tf, gbc);

		i++;// 7

		// --------- :: Buttons

		// set the fill to horizontal
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = i;
		gbc.gridheight = 2;
		gbc.gridwidth = 2;
		panel.add(addOptBtns(), gbc);

		i += 2;// 9

		// Add separator
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridx = 0;
		gbc.gridy = i;
		gbc.gridwidth = 4;
		gbc.gridheight = 1;
		panel.add(sep2, gbc);

		i++;// 9

		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;
		gbc.gridy = i;
		panel.add(addButtons(), gbc);

		// ---------::Action Listeners
		addCourspreviewCode_Btn.addActionListener(new addCourse());
		undo_Btn.addActionListener(new removeRow());
		clearAll_Btn.addActionListener(new clearAll());
		updateSelection_Btn.addActionListener(new editCourse());
		previewCode_Btn.addActionListener(new previewCourse());
		back_Btn.addActionListener(new toRoomsAndDept());
		if (mode.equals("All")) {
			finish_Btn.addActionListener(new finish());
			back_Btn.setEnabled(true);
			back_Btn.setVisible(true);
		} else if (mode.equals("CoursesOnly")) {
			finish_Btn.addActionListener(new courseTransferListener());
			back_Btn.setEnabled(false);
			back_Btn.setVisible(true);
			setTitle("Modify Courses");
		} else if (mode.equals("DeptsRoomsCourses")) {
			finish_Btn.addActionListener(new deptRoomsCourseTransfer());
			back_Btn.setEnabled(false);// No backing because backing glitches
			back_Btn.setVisible(true);
			setTitle("Modify Courses");
		}
		cancel_Btn.addActionListener(new cancel());

		rowSM = courseTable.getSelectionModel();// [p]
		rowSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				// Ignore extra messages.
				if (e.getValueIsAdjusting())
					return;
				lsm = (ListSelectionModel) e.getSource();
				if (!(lsm.isSelectionEmpty())) {
					rowClicked = lsm.getMinSelectionIndex();

					// method resets options on the left side to match selection
					sectionMatch(rowClicked);
				} else {
					rowClicked = 0;
				}
			}
		});

		// pack everything together
		pack();
	}

	// ------------------------------------------------------------Methods

	// ------------JPanels

	public JPanel levelPanel() {
		JPanel gnPanel = new JPanel(new GridBagLayout());
		// give it a black border
		gnPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		// make a new GridBagConstraints
		GridBagConstraints gbc = new GridBagConstraints();

		// set the fill to both
		gbc.fill = GridBagConstraints.BOTH;
		// set the weights
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;

		// integer i is used for managing the rows in the coordinates
		int i = 0;

		// declare radio button rb_ac: Acad/M/U
		gbc.gridwidth = 1;
		rb_ac = new JRadioButton("Acad/M/U");
		gbc.gridx = 0;
		gbc.gridy = i;
		gnPanel.add(rb_ac, gbc);

		// declare radio button rb_ap: Appl/C
		rb_ap = new JRadioButton("Appl/C");
		gbc.gridx = 1;
		gbc.gridy = i;
		gnPanel.add(rb_ap, gbc);

		i++;

		// declare radio button rb_ep: ESP
		rb_op = new JRadioButton("Open");
		gbc.gridx = 0;
		gbc.gridy = i;
		gnPanel.add(rb_op, gbc);
		// declare radio button rb_ep: ESP
		rb_ld = new JRadioButton("Loc Dev");
		gbc.gridx = 1;
		gbc.gridy = i;
		gnPanel.add(rb_ld, gbc);

		// declare radio button rb_ep: ESP
		rb_ep = new JRadioButton("ESP");
		gbc.gridx = 2;
		gbc.gridy = i;
		gnPanel.add(rb_ep, gbc);

		// add the radio buttons to a group
		gr_lvl.add(rb_ac);
		gr_lvl.add(rb_ap);
		gr_lvl.add(rb_op);
		gr_lvl.add(rb_ld);
		gr_lvl.add(rb_ep);

		// set the border
		gnPanel.setBorder(level_title);

		// return the panel
		return gnPanel;
	}

	public JPanel gradesPanel() {
		JPanel grPanel = new JPanel(new GridBagLayout());
		// give it a black border
		grPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		// make a new GridBagConstraints
		GridBagConstraints gbc = new GridBagConstraints();

		// set the fill to both
		gbc.fill = GridBagConstraints.BOTH;

		// set the weights
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;

		int i = 0;

		rb_09 = new JRadioButton("Gr. 9");
		gbc.gridx = 0;
		gbc.gridy = i;
		grPanel.add(rb_09, gbc);

		rb_10 = new JRadioButton("Gr. 10");
		gbc.gridx = 1;
		gbc.gridy = i;
		grPanel.add(rb_10, gbc);
		i++;

		rb_11 = new JRadioButton("Gr. 11");
		gbc.gridx = 0;
		gbc.gridy = i;
		grPanel.add(rb_11, gbc);

		rb_12 = new JRadioButton("Gr. 12");
		gbc.gridx = 1;
		gbc.gridy = i;
		grPanel.add(rb_12, gbc);

		gr_grade.add(rb_09);
		gr_grade.add(rb_10);
		gr_grade.add(rb_11);
		gr_grade.add(rb_12);

		grPanel.setBorder(grade_title);

		return grPanel;
	}

	public JPanel addInfoPanel() {
		// make the option Panel
		JPanel infoPanel = new JPanel(new GridBagLayout());
		// give it a black border
		infoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		// create the GridBagConstraints
		GridBagConstraints gbc = new GridBagConstraints();
		infoPanel.setPreferredSize(new Dimension(900, 350));

		// set insets
		gbc.insets = defaultInsets;

		// add the title of the panel label..........
		glb = new JLabel("Courses:");
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		infoPanel.add(glb, gbc);

		// -------------------------------[pth]------------ :: put table here

		// :: table info
		// declare the column names
		String[] columnNames = { "Course Code", "Department", "Level", "Grade",
				"Room", "# of Sections", "Period Restrictions" };

		// set the data for the table: if a file is to be loaded and is located
		// in the folder, it will be read and the data from it will be assigned
		if (loadMe) {
			data = readCoursesDB();
		}
		// else the default values for the data will be assigned
		else if (!(mode1.equals("CoursesOnly") | mode1
				.equals("DeptsRoomsCourses"))) {
			data = readCoursesFromFile();
		}
		// JOptionPane.showMessageDialog(new JFrame("Prompt"),
		// "IIcourses mode: " + mode1);
		System.out.println("IIcourses mode: " + mode1);

		courseTable = new JTable(new UneditableTableModel(data, columnNames));
		model = (DefaultTableModel) courseTable.getModel();

		// :: table layout info
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		infoPanel.add(new JScrollPane(courseTable), gbc);
		// last part of the method
		return infoPanel;

	}

	public JPanel addButtons() {
		// make the option Panel
		JPanel btnPanel = new JPanel(new GridBagLayout());
		// create the GridBagConstraints
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = defaultInsets;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		btnPanel.add(back_Btn, gbc);
		// sets the coordinates for the buttons
		gbc.gridx = 1;
		gbc.gridy = 0;
		btnPanel.add(cancel_Btn, gbc);
		// sets the coordinates for the buttons
		gbc.gridx = 2;
		gbc.gridy = 0;
		// /set the weightings so that the option pane could be resized
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.EAST;
		btnPanel.add(finish_Btn, gbc);

		// return the btnPanel
		return btnPanel;
	}

	public JPanel addOptBtns() {

		// make the option Panel
		JPanel restrictionsPanel = new JPanel(new GridBagLayout());
		// create the GridBagConstraints
		GridBagConstraints gbc = new GridBagConstraints();
		// set insetsS
		gbc.insets = defaultInsets;

		gbc.fill = GridBagConstraints.BOTH;
		// set weights
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;

		// declare integer i as 0
		int i = 0;

		// set the fill to horizontal
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// initialize the buttons
		addCourspreviewCode_Btn = new JButton("Add Course");
		undo_Btn = new JButton("Remove Row");
		clearAll_Btn = new JButton("Clear All");
		updateSelection_Btn = new JButton("Update Changes");
		previewCode_Btn = new JButton("Preview Course");

		// set the coordinates
		gbc.gridx = 0;
		gbc.gridy = i;
		gbc.gridwidth = 2;
		restrictionsPanel.add(clearAll_Btn, gbc);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 3;
		gbc.gridy = i;
		gbc.gridwidth = 1;
		gbc.gridheight = 2;
		restrictionsPanel.add(addCourspreviewCode_Btn, gbc);

		gbc.gridx = 2;
		gbc.gridy = i;
		gbc.gridheight = 1;
		restrictionsPanel.add(updateSelection_Btn, gbc);

		i++;// 8

		gbc.gridheight = 1;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = i;
		restrictionsPanel.add(undo_Btn, gbc);

		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.gridx = 2;
		gbc.gridy = i;
		restrictionsPanel.add(previewCode_Btn, gbc);

		return restrictionsPanel;

	}

	// this JPanel contains the period Restrictions for the
	public JPanel periodRestrictions() {

		// make the option Panel
		JPanel restrictionsPanel = new JPanel(new GridBagLayout());
		// create the GridBagConstraints
		GridBagConstraints gbc = new GridBagConstraints();
		// set insetsS
		gbc.insets = defaultInsets;

		gbc.fill = GridBagConstraints.BOTH;
		// set weights
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;

		// declare integer b as 0: used for organizing the rows
		int b = 0;

		// set the fill to horizontal
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// set the coordinates
		gbc.gridx = b;
		gbc.gridy = 0;
		restrictionsPanel.add(cb_1, gbc);

		b++;// 1

		// set the coordinates
		gbc.gridx = b;
		gbc.gridy = 0;
		restrictionsPanel.add(cb_2, gbc);

		b++;// 2

		// set the coordinates
		gbc.gridx = b;
		gbc.gridy = 0;
		restrictionsPanel.add(cb_3, gbc);

		b++;// 3

		// set the coordinates
		gbc.gridx = b;
		gbc.gridy = 0;
		restrictionsPanel.add(cb_4, gbc);

		b++;// 4

		if (secondaryDataBase.amountOfPeriods == 5) {

			// set the coordinates
			gbc.gridx = b;
			gbc.gridy = 0;
			restrictionsPanel.add(cb_5, gbc);

			// b++;//5
		}

		// give it a border
		restrictionsPanel.setBorder(pr_title);
		return restrictionsPanel;

	}

	public Object[][] readCoursesFromFile() {

		// assign all contents of the file to the array
		// Informs user of progress
		System.out.println("Reading File");
		dataSimplifiedArray = IOToolkit.basicRead("blank_courses.ses");

		// Declare the objects array
		Object[][] objArray = new Object[dataSimplifiedArray.length][7];

		// Declare Temporary variables and strings for the objects array
		String ime; // - Name
		String dpt; // - Department
		String lvl; // - Level
		int grd; // ---- Grade
		String rms; // - Rooms
		int sec; // ---- Section
		String ppr;// -- Period Preferences

		for (int i = 0; i < dataSimplifiedArray.length; i++) {
			// assigns all 7 variables values from the file data array
			ime = dataSimplifiedArray[i].split(", ")[0];
			dpt = dataSimplifiedArray[i].split(", ")[1];
			lvl = dataSimplifiedArray[i].split(", ")[2];
			grd = Integer.parseInt(dataSimplifiedArray[i].split(", ")[3]);
			rms = dataSimplifiedArray[i].split(", ")[4];
			sec = Integer.parseInt(dataSimplifiedArray[i].split(", ")[5]);
			ppr = dataSimplifiedArray[i].split(", ")[6];

			objArray[i][0] = ime;
			objArray[i][1] = dpt;
			objArray[i][2] = lvl;
			objArray[i][3] = grd;
			objArray[i][4] = rms;
			objArray[i][5] = sec;
			objArray[i][6] = ppr;
		}
		// Informs user of progress
		System.out.println("File Read");

		// returns the object 2D array
		return objArray;
	}

	public Object[][] readCoursesDB() {

		// Declare the objects array
		Object[][] objArray = new Object[DataBase.courses.length][7];

		// Declare Temporary variables and strings for the objects array
		String ime; // - Name
		String dpt; // - Department
		String lvl; // - Level
		int grd; // ---- Grade
		String rms; // - Rooms
		int sec; // ---- Section
		String ppr;// -- Period Preferences

		for (int i = 0; i < objArray.length; i++) {
			// assigns all 7 variables values from the file data array
			ime = DataBase.courses[i].getName();
			dpt = DataBase.courses[i].department.getName();
			lvl = DataBase.courses[i].getLevel();
			grd = DataBase.courses[i].getGrade() + 9;
			rms = DataBase.courses[i].getRoomType();
			sec = DataBase.courses[i].getExistingSectionCount();
			ppr = secondaryDataBase
					.restrictionsBoolToString(DataBase.courses[i]
							.getPerPrefArray());

			objArray[i][0] = ime;
			objArray[i][1] = dpt;
			objArray[i][2] = lvl;
			objArray[i][3] = grd;
			objArray[i][4] = rms;
			objArray[i][5] = sec;
			objArray[i][6] = ppr;
		}

		return objArray;
	}

	public boolean[] analyseRestrictionsBool(String periodPrefStr) {
		periodPrefStr = periodPrefStr.trim();

		boolean[] prefReturnBool = new boolean[DataBase.schoolPeriodCount];

		// if there is no restrictions
		if (periodPrefStr.equals("None")) {
			// whole array is true
			for (int i = 0; i < prefReturnBool.length; i++) {
				prefReturnBool[i] = true;
			}
		} else if (periodPrefStr.equals("All")) {
			// if all periods are restricted: every element is false
			for (int i = 0; i < prefReturnBool.length; i++) {
				prefReturnBool[i] = false;
			}
		} else {
			restrictionsArray = periodPrefStr.split(" ");
			for (int i = 0; i < prefReturnBool.length; i++)
				prefReturnBool[i] = true;
			for (int i = 0; i < restrictionsArray.length; i++) {
				int indexofrestriction = Integer.parseInt(restrictionsArray[i]);
				indexofrestriction -= 1;
				prefReturnBool[indexofrestriction] = false;

			}

		}

		return prefReturnBool;
	}

	// ---------------Other

	public boolean validating(String s) {

		char[] cc = s.toCharArray();
		int ci = 0;
		if (s.equals("") || s == null) {
			// if the user does not input anything: return false
			return false;
		}

		// else if: the user inputs anything other than letters or numbers
		// (filtering out special characters and spaces)
		else {

			// loops through each of the positions of the char array
			for (int i = 0; i < cc.length; i++) {
				// assigns the integer the value of the current char(integer
				// value)
				ci = (int) cc[i];
				// checks if the char is not a number and not a letter
				if (!((ci >= 47 && ci <= 57) || (ci >= 65 && ci <= 90))) {
					// if it is not a letter or a number , false is returned
					JOptionPane
							.showMessageDialog(
									new JFrame("Error"),
									"<html>Courses / Section number was not input correctly<p>Courses must be uppercase</html>");
					return false;
				}
			}
			// if the user's input is valid: return true
			return true;
		}

	}

	public boolean validateInt(String s) {

		char asd = ' ';
		int ascii;
		if (s.length() != 0) {

			for (int i = 0; i < s.length(); i++) {
				// assigns a letter from String s to char asd
				asd = s.charAt(i);
				// if the integer value of the char is between 48 and 57, the
				// ascii values for 0 through 9, true is returned
				ascii = (int) (asd);
				if (!(ascii >= 48 && ascii <= 57)) {
					return false;
				}
			}
			return true;
		}
		// if the length of the string is 0 - nothing was input, false is
		// returned
		else {
			return false;
		}
	}

	public int getGrade() {
		int gr;

		if (rb_09.isSelected()) {
			gr = 9;
		} else if (rb_10.isSelected()) {
			gr = 10;
		} else if (rb_11.isSelected()) {
			gr = 11;
		} else if (rb_12.isSelected()) {
			gr = 12;
		} else {
			gr = 0;
			System.out.println("Grades not selected");
		}

		return gr;

	}

	public String getLevel() {
		String lvl = "";

		if (rb_ac.isSelected()) {
			lvl = "Acad/M/U";
		} else if (rb_ap.isSelected()) {
			lvl = "Appl/C";
		} else if (rb_op.isSelected()) {
			lvl = "Open";
		} else if (rb_ld.isSelected()) {
			lvl = "Loc Dev";
		} else if (rb_ep.isSelected()) {
			lvl = "ESP";
		} else {
			System.out.println("Level not selected");
		}

		return lvl;
	}

	public void sectionMatch(int selectedRow) {

		int b = (int) model.getValueAt(selectedRow, 3);

		// Swap the initial text box with the data from first column in the
		// selected row
		courseCode_tf.setText((String) model.getValueAt(selectedRow, 0));

		// Set both combo boxes to selected data
		a_cb.setSelectedItem(model.getValueAt(selectedRow, 1));
		b_cb.setSelectedItem(model.getValueAt(selectedRow, 4));

		// Set Radio Buttons (grade) change to match selected data
		if (b == 9) {
			rb_09.setSelected(true);
		} else if (b == 10) {
			rb_10.setSelected(true);
		} else if (b == 11) {
			rb_11.setSelected(true);
		} else if (b == 12) {
			rb_12.setSelected(true);
		} else {
			System.out.println("No data set for the grade");
		}

		// b is now used for the number of sections
		b = (int) model.getValueAt(selectedRow, 5);
		sections_tf.setText(Integer.toString(b));

		String t = (String) model.getValueAt(selectedRow, 2);
		if (t.equals("Acad/M/U")) {
			rb_ac.setSelected(true);

		} else if (t.equals("Appl/C")) {
			rb_ap.setSelected(true);

		} else if (t.equals("Open")) {
			rb_op.setSelected(true);

		} else if (t.equals("Loc Dev")) {
			rb_ld.setSelected(true);

		} else if (t.equals("ESP")) {
			rb_ep.setSelected(true);

		} else {
			System.out.println("No data set for the level");
		}

		setrestricttionCheckBoxes((String) model.getValueAt(selectedRow, 6));

		// debugDisplay(selectedRow);

	}

	public void setrestricttionCheckBoxes(String restrict) {

		// if there are no restricted all boxes are unchecked
		if (restrict.equals("None")) {
			cb_1.setSelected(false);
			cb_2.setSelected(false);
			cb_3.setSelected(false);
			cb_4.setSelected(false);
			cb_5.setSelected(false);

		}
		// if all periods are restricted, Checks all boxes off
		else if (restrict.equals("All")) {
			cb_1.setSelected(true);
			cb_2.setSelected(true);
			cb_3.setSelected(true);
			cb_4.setSelected(true);
			cb_5.setSelected(true);
		}
		// if only specific boxes are selected
		else {
			// creats an array based on the string passed to method
			// containing the numbers of the restricted periods
			restrictionsArray = restrict.split(" ");

			// resets all of the checkboxes to false
			cb_1.setSelected(false);
			cb_2.setSelected(false);
			cb_3.setSelected(false);
			cb_4.setSelected(false);
			cb_5.setSelected(false);

			// for loop cycles through the array and checks off the check boxes
			// that are specified in the array
			for (int i = 0; i < restrictionsArray.length; i++) {
				if (restrictionsArray[i].equals("1")) {
					cb_1.setSelected(true);
				} else if (restrictionsArray[i].equals("2")) {
					cb_2.setSelected(true);
				} else if (restrictionsArray[i].equals("3")) {
					cb_3.setSelected(true);
				} else if (restrictionsArray[i].equals("4")) {
					cb_4.setSelected(true);
				} else if (restrictionsArray[i].equals("5")) {
					cb_5.setSelected(true);
				}
			}

		}

	}

	public void debugDisplay(int row) {

		// Displays Selection from the table:

		// Column 1: Course Code
		System.out.println((String) model.getValueAt(row, 0));
		// Column 2: Department
		System.out.println((String) model.getValueAt(row, 1));
		// Column 3: Level
		System.out.println((String) model.getValueAt(row, 2));
		// Column 4: Grade
		System.out.println(Integer.toString((int) model.getValueAt(row, 3)));
		// Column 5: Room Type
		System.out.println((String) model.getValueAt(row, 4));
		// Column 6: Sections
		System.out.println(Integer.toString((int) model.getValueAt(row, 5)));
		// Column 7: period restrictions
		System.out.println((String) model.getValueAt(row, 6));

	}

	// checks to see if course code is already in use
	public boolean codeCheck(String idCode, boolean edit) {

		for (int i = 0; i < model.getRowCount(); i++) {

			if (idCode.equals(model.getValueAt(i, 0))) {

				if (!(edit == true && idCode.equals(model.getValueAt(
						rowClicked, 0)))) {
					JOptionPane.showMessageDialog(new JFrame("Error"),
							"This Course Code is already in use");
					courseTable.changeSelection(i, 0, true, false);
					sectionMatch(i);

					return false;
				}

			}

		}
		return true;

	}

	public String getRestrictions() {

		String gpr = ""; // stands for get period restriction

		// checks if all check boxes are selected
		if (cb_1.isSelected() && cb_2.isSelected() && cb_3.isSelected()
				&& cb_4.isSelected() && cb_5.isSelected()) {
			gpr = "All"; // if all selected then gpr becomes "All"
		}
		// if none are selected
		else if (!(cb_1.isSelected() || cb_2.isSelected() || cb_3.isSelected()
				|| cb_4.isSelected() || cb_5.isSelected())) {
			gpr = "None";// if none selected then gpr becomes "None"
		}
		// if only specific periods are selected
		else {

			if (cb_1.isSelected()) {
				gpr = gpr + "1 ";
			}
			if (cb_2.isSelected()) {
				gpr = gpr + "2 ";
			}
			if (cb_3.isSelected()) {
				gpr = gpr + "3 ";
			}
			if (cb_4.isSelected()) {
				gpr = gpr + "4 ";
			}
			if (cb_5.isSelected()) {
				gpr = gpr + "5 ";
			}
		}

		return gpr;
	}

	// passes a boolean which will adjust the loadMe boolean used for loading
	// files and starting with clean template
	public static void checkLoad(boolean loadFile) {
		loadMe = loadFile;
	}

	// transfers data from temporary database to the database
	public void dbTransfer() {
		// From: secondaryDataBase
		// To: DataBase

		// Assign values to the database
		DataBase.projectName = secondaryDataBase.projectName;
		DataBase.schoolPeriodCount = secondaryDataBase.amountOfPeriods;
		DataBase.schoolGradeCount = secondaryDataBase.grades;
		DataBase.schoolYearDivisions = secondaryDataBase.semesters;
		// assigns the levels array in the database
		DataBase.levels = new String[5];
		DataBase.levels[0] = "Acad/M/U";
		DataBase.levels[1] = "Appl/C";
		DataBase.levels[2] = "Open";
		DataBase.levels[3] = "Loc Dev";
		DataBase.levels[4] = "ESP";

		// Creates the Rooms objects
		DataBase.rooms = new Room[secondaryDataBase.roomArray.length];
		for (int i = 0; i < DataBase.rooms.length; i++) {
			DataBase.rooms[i] = new Room(secondaryDataBase.roomArray[i],
					secondaryDataBase.roomTypeAmount[i],
					DataBase.schoolPeriodCount);
		}

		// Assigns Lunch Preferences in database values from the temporary
		// database
		DataBase.lunchPrefs = new boolean[secondaryDataBase.lunchPrefs.length][secondaryDataBase.lunchPrefs[0].length];
		for (int i = 0; i < DataBase.lunchPrefs.length; i++) {
			for (int b = 0; b < DataBase.lunchPrefs[0].length; b++) {
				DataBase.lunchPrefs[i][b] = secondaryDataBase.lunchPrefs[i][b];
			}
		}

		// Creates the Grades objects
		DataBase.grades = new Grade[secondaryDataBase.grades];
		for (int i = 0; i < DataBase.grades.length; i++) {
			DataBase.grades[i] = new Grade(i, secondaryDataBase.lunchPrefs[i],
					DataBase.schoolPeriodCount);
		}

		// Creates the Departments objects
		DataBase.departments = new Department[secondaryDataBase.deptArray.length];
		for (int i = 0; i < secondaryDataBase.deptArray.length; i++) {
			// Departments
			DataBase.departments[i] = new Department(
					secondaryDataBase.deptArray[i]);
			DataBase.departments[i]
					.setBackGroundColor(new Color(250, 240, 180));
			DataBase.departments[i].setForeGroundColor(new Color(0, 0, 0));

		}

		// (String name, Department department, String level, int grade,
		// String roomType, int sectionCount)

		// Creates the Courses object
		DataBase.courses = new Course[dataStrArray.length];
		for (int i = 0; i < dataStrArray.length; i++) {
			int gradeInt = Integer.parseInt(dataStrArray[i][3]) - 9;

			int secInt = Integer.parseInt(dataStrArray[i][5]);

			// look for department name
			deptInt = DataBase.findDeptPos(dataStrArray[i][1]);
			perPrefBool = analyseRestrictionsBool(dataStrArray[i][6]);
			DataBase.courses[i] = new Course(dataStrArray[i][0],
					DataBase.departments[deptInt], dataStrArray[i][2],
					gradeInt, dataStrArray[i][4], secInt);

			for (int b = 0; b < perPrefBool.length; b++) {
				DataBase.courses[i].setPerPref(b, perPrefBool[b]); // edit]
			}

		}

	}

	// ------------------------------------------------------------Action_Listeners

	// action listener for the add course button
	class addCourse implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			// temporary string to get the name of course
			String course_code_temp = courseCode_tf.getText().toUpperCase();
			// temporary string to department name
			String department_temp = (String) a_cb.getSelectedItem();
			// temporary string to room type
			String room_temp = (String) b_cb.getSelectedItem();
			// get grade as integer value
			int grade = getGrade();
			// get String for the level of course
			String levels = getLevel();
			// declare integer section
			int section = 0;
			// declare String for the period restrictions
			String per_restrict_temp = getRestrictions();

			// if the text box containing the section number is not empty:
			// assigns the value input to the integer section
			if (validating(sections_tf.getText())
					&& validateInt(sections_tf.getText())) {

				section = Integer.parseInt(sections_tf.getText());

			}

			// validates all text
			if (validating(course_code_temp) && !(levels.equals(""))
					&& grade != 0 && section != 0
					&& codeCheck(course_code_temp, false)) {

				// gets the table model
				model = (DefaultTableModel) courseTable.getModel();
				// adds an additional row
				model.addRow(new Object[] { course_code_temp, department_temp,
						levels, grade, room_temp, section, per_restrict_temp });

			}
		}
	}

	// removes the selected row
	class removeRow implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// if a row is selected, it is removed
			if (courseTable.getRowCount() > 0) {
				model.removeRow(rowClicked);
			}

		}

	}

	// clears the whole table
	class clearAll implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			while (courseTable.getRowCount() > 0) {
				model.removeRow(0);
			}
		}

	}

	// goes back to the previous frame
	class toRoomsAndDept implements ActionListener {
		public void actionPerformed(ActionEvent boop) {
			// sets visibility to false
			setVisible(false);
			// disposes of the frame
			dispose();
			if (mode1.equals("All")) {
				IIroomsAndDepts rnd = new IIroomsAndDepts("All");
				rnd.setVisible(true);
			} else if (mode1.equals("DeptsRoomsCourses")) {
				IIroomsAndDepts rnd = new IIroomsAndDepts("DeptsRoomsCourses");
				rnd.setVisible(true);
			}
		}
	}

	// (a.k.a. the Update Row button) updates the selected row with new options
	class editCourse implements ActionListener {
		public void actionPerformed(ActionEvent args0) {
			System.out.println("EditCourse is Running!");
			// temporary string to get the name of course
			String course_code_temp = courseCode_tf.getText();
			// temporary string to department name
			String department_temp = (String) a_cb.getSelectedItem();
			// temporary string to room type
			String room_temp = (String) b_cb.getSelectedItem();
			// get grade as integer value
			int grade = getGrade();
			// get String for the level of course
			String levels = getLevel();
			// declare integer section
			int section = 0;
			// declare String for the period restrictions
			String per_restrict_temp = getRestrictions(); // gr]

			// if the text box containing the section number is not empty:
			// assigns the value input to the integer section
			if (validating(sections_tf.getText())
					&& validateInt(sections_tf.getText())) {
				section = Integer.parseInt(sections_tf.getText());
			}

			System.out.println("rowClicked: " + rowClicked);
			System.out.println("validating(course_code_temp): "
					+ validating(course_code_temp));
			System.out.println("!(levels.equals()): " + !(levels.equals("")));
			System.out.println("grade != 0: " + (grade != 0));
			System.out.println("section != 0: " + (section != 0));
			System.out.println("codeCheck(course_code_temp, true): "
					+ codeCheck(course_code_temp, true));
			// if everything is input correctly
			if (validating(course_code_temp) && !(levels.equals(""))
					&& grade != 0 && section != 0
					&& codeCheck(course_code_temp, true)) {

				// Edits the row
				model = (DefaultTableModel) courseTable.getModel();

				if (courseTable.getRowCount() > 0) {
					model.setValueAt(course_code_temp, rowClicked, 0);
					model.setValueAt(department_temp, rowClicked, 1);
					model.setValueAt(levels, rowClicked, 2);
					model.setValueAt(grade, rowClicked, 3);
					model.setValueAt(room_temp, rowClicked, 4);
					model.setValueAt(section, rowClicked, 5);
					model.setValueAt(per_restrict_temp, rowClicked, 6);
				}

			}

		}
	}

	// checks the last 2 characters at the end of a course code and assigns
	// appropriate grade and levels
	class previewCourse implements ActionListener {
		public void actionPerformed(ActionEvent args0) {

			String asdfffsdad = courseCode_tf.getText().toUpperCase();

			for (int i = 0; i < asdfffsdad.length(); i++) {
				System.out.print(i + "\t");
			}
			System.out.println();
			for (int i = 0; i < asdfffsdad.length(); i++) {
				System.out.print(asdfffsdad.charAt(i) + "\t");
			}

			System.out.println();
			System.out.println();

			if (asdfffsdad.length() >= 5) {

				// Check for levels and assign and select matching radio button
				if (asdfffsdad.charAt(4) == 'U' || asdfffsdad.charAt(4) == 'M'
						|| asdfffsdad.charAt(4) == 'D') {
					rb_ac.setSelected(true);
				} else if (asdfffsdad.charAt(4) == 'O') { // Open
					rb_op.setSelected(true);
				} else if (asdfffsdad.charAt(4) == 'C'
						|| asdfffsdad.charAt(4) == 'P') { // College
					rb_ap.setSelected(true);
				} else if (asdfffsdad.charAt(4) == 'L') { // ESP
					rb_ep.setSelected(true);
				} else if (asdfffsdad.charAt(4) == 'E') { // Loc Dev
					rb_ld.setSelected(true);
				}

				// Check for grade and assign and select matching radio button
				if (asdfffsdad.charAt(3) == '1') {
					rb_09.setSelected(true);
				} else if (asdfffsdad.charAt(3) == '2') {
					rb_10.setSelected(true);
				} else if (asdfffsdad.charAt(3) == '3') {
					rb_11.setSelected(true);
				} else if (asdfffsdad.charAt(3) == '4') {
					rb_12.setSelected(true);
				}
			}
		}
	}

	class cancel implements ActionListener {
		public void actionPerformed(ActionEvent args0) {

			// sets visibility to false
			setVisible(false);
			// disposes of the frame
			dispose();

		}

	}

	// ///////////////////////////////////////////Anti-editing TableModel Class:
	private class UneditableTableModel extends DefaultTableModel {

		// Used to create an overridden table model where cells cannot be edited
		public UneditableTableModel(Object[][] tableData, Object[] colNames) {
			super(tableData, colNames);
		}

		public boolean isCellEditable(int row, int column) {
			return false;
		}// Override the isCellEditable method
	}

	private boolean checkIfDeptExists(String deptName) {
		for (int index = 0; index < secondaryDataBase.deptArray.length; index++) {
			if (secondaryDataBase.deptArray[index].equals(deptName)) {
				// System.out.println("For deptName: " + deptName +
				// "- Returning TRUE (EXISTS)");
				return true;
			}
		}
		// System.out.println("For deptName: " + deptName +
		// "Returning FALSE (DNE)");
		return false;
	}

	private boolean checkIfRoomExists(String roomName) {
		for (int index = 0; index < secondaryDataBase.roomArray.length; index++) {
			if (secondaryDataBase.roomArray[index].equals(roomName)) {
				// System.out.println("For roomName: " + roomName +
				// "- Returning TRUE (EXISTS)");
				return true;
			}
		}
		// System.out.println("For roomName: " + roomName +
		// "Returning FALSE (DNE)");
		return false;
	}

	private boolean checkDataValidity() {// checks the data in data[][] to see
											// if all data is valid
		boolean dataIsValid = true;
		boolean rowIsValid = true;
		for (int index = 0; index < dataStrArray.length; index++) {
			String errorType = "";
			rowIsValid = true;
			if (!checkIfDeptExists((String) dataStrArray[index][1])) {
				// check dept does not exist
				dataIsValid = false;
				rowIsValid = false;
				errorType = " Invalid department;";
			}
			if (DataBase.findLevelPos((String) dataStrArray[index][2]) == -1) {
				// check level
				System.out.println("Invalid levelName : "
						+ (String) dataStrArray[index][2]);
				dataIsValid = false;
				rowIsValid = false;
				errorType = errorType + " Level not defined;";
			}
			if (!(Integer.parseInt(dataStrArray[index][3]) == 9
					| Integer.parseInt(dataStrArray[index][3]) == 10
					| Integer.parseInt(dataStrArray[index][3]) == 11 | Integer
						.parseInt(dataStrArray[index][3]) == 12)) {
				// check grade
				dataIsValid = false;
				rowIsValid = false;
				errorType = errorType + " Invalid Grade;";
			}
			if (!checkIfRoomExists((String) dataStrArray[index][4])) {
				// check room does not exist
				dataIsValid = false;
				rowIsValid = false;
				errorType = errorType + " Invalid room;";
			}
			if (Integer.parseInt(dataStrArray[index][5]) == 0) {
				// check sectionCount
				dataIsValid = false;
				rowIsValid = false;
				errorType = errorType + " Invalid section count;";
			}
			if (!rowIsValid) {// Add to array of courses with invalid data
				coursesWithInvalidity = coursesWithInvalidity + "</separator>"
						+ (String) data[index][0] + errorType;
			}
		}
		return dataIsValid;
	}

	class finish implements ActionListener {
		public void actionPerformed(ActionEvent args0) {

			dataStrArray = new String[model.getRowCount()][7];

			// assigns table values to the data array
			for (int i = 0; i < dataStrArray.length; i++) {
				for (int b = 0; b < dataStrArray[0].length; b++) {

					if (b == 3 || b == 5) {
						dataStrArray[i][b] = Integer.toString((int) model
								.getValueAt(i, b));
					} else {
						dataStrArray[i][b] = (String) model.getValueAt(i, b);
					}

				}
			}
			coursesWithInvalidity = "";
			if (checkDataValidity()) {
				// to database
				// assign values from the temporary database (secondaryDataBase)
				// to the main
				// database
				dbTransfer();

				// sets visibility to false
				setVisible(false);
				// disposes of the frame
				dispose();
				repatchTimeT();
				DataBase.selected_Department = DataBase.departments[0]
						.getName();
				if (Course_Manager_Main.window1.frame != null) {
					Course_Manager_Main.window1.frame.dispose();
				}
				if (Course_Manager_Main.window1.window2 != null) {
					Course_Manager_Main.window1.window2.dispose();
				}
				Course_Manager_Main.window1.define();
				// Set timeT size
				int period = DataBase.schoolPeriodCount;
				int grade = DataBase.schoolGradeCount;
				int numberOfDepartments = DataBase.departments.length;
				int numberOfRowsPerDept = grade * period;
				int numberOfColsPerDept = DataBase.colCountPerLevel
						* DataBase.levels.length;
				int numberOfSemesters = 2;
				DataBase.timeT = new Course[numberOfDepartments][numberOfRowsPerDept][numberOfColsPerDept][numberOfSemesters];
				DataBase.timeTcopy = new Course[numberOfDepartments][numberOfRowsPerDept][numberOfColsPerDept][numberOfSemesters];
				DataBase.selected_Department = DataBase.departments[0]
						.getName();
				DataBase.isItFirstLoad = false;
				DataBase.isDataBaseEmpty = false;
				Course_Manager_Main.window1.refreshDisplay();

				System.out.println("Close the courses frame");
				System.out.println("SchoolPeriodCount: "
						+ DataBase.schoolPeriodCount);
			} else {
				// Spawn Notice Frame
				JFrame invalids_Frame = new JFrame();
				JList<String> invalids_List = new JList<String>(
						coursesWithInvalidity.split("</separator>"));
				JScrollPane invalids_SPane = new JScrollPane(invalids_List);
				invalids_Frame.setTitle("Error: Invalid Information");
				invalids_Frame.setContentPane(invalids_SPane);
				invalids_SPane
						.setBorder(BorderFactory
								.createTitledBorder("Invalid Information Found in Courses: "));
				invalids_Frame.setVisible(true);
				invalids_Frame.pack();
				invalids_Frame.setBounds(0, 0, 500, 600);
				invalids_Frame.setLocationRelativeTo(null);
			}
		}
	}

	class courseTransferListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			dataStrArray = new String[model.getRowCount()][7];

			// assigns table values to the data array
			for (int i = 0; i < dataStrArray.length; i++) {
				for (int b = 0; b < dataStrArray[0].length; b++) {

					if (b == 3 || b == 5) {
						dataStrArray[i][b] = Integer.toString((int) model
								.getValueAt(i, b));
					} else {
						dataStrArray[i][b] = (String) model.getValueAt(i, b);
					}

				}
			}
			coursesWithInvalidity = "";
			if (checkDataValidity()) {
				coursesTransfer();// Transfer Courses
				// sets visibility to false
				setVisible(false);
				// disposes of the frame
				dispose();
				// Set timeT size
				DataBase.isItFirstLoad = false;
				DataBase.isDataBaseEmpty = false;
				DataBase.isDataBaseEmpty = false;
				for (int i = 0; i < DataBase.timeT.length; i++) {
					for (int j = 0; j < DataBase.timeT[0].length; j++) {
						for (int k = 0; k < DataBase.timeT[0][0].length; k++) {
							for (int l = 0; l < DataBase.timeT[0][0][0].length; l++) {
								if (DataBase.timeT[i][j][k][l] != null) {
									DataBase.timeT[i][j][k][l] = DataBase
											.findCourse(DataBase.timeT[i][j][k][l]
													.getName());
								}
							}
						}
					}
				}
				DataBase.rescanForValidity();
				Course_Manager_Main.window1.refreshDisplay();

				System.out.println("Close the courses frame");
				System.out.println("SchoolPeriodCount: "
						+ DataBase.schoolPeriodCount);
			} else {
				JFrame invalids_Frame = new JFrame();
				JList<String> invalids_List = new JList<String>(
						coursesWithInvalidity.split(" "));
				JScrollPane invalids_SPane = new JScrollPane(invalids_List);
				invalids_Frame.setTitle("Error: Invalid Information");
				invalids_Frame.setContentPane(invalids_SPane);
				invalids_SPane
						.setBorder(BorderFactory
								.createTitledBorder("Invalid Information Found in Courses: "));
				invalids_Frame.setVisible(true);
				invalids_Frame.pack();
				invalids_Frame.setBounds(0, 0, 350, 500);
				invalids_Frame.setLocationRelativeTo(null);
			}
		}
	}

	private void roomsTransfer() {
		// Creates the Rooms objects
		DataBase.rooms = new Room[secondaryDataBase.roomArray.length];
		for (int i = 0; i < DataBase.rooms.length; i++) {
			DataBase.rooms[i] = new Room(secondaryDataBase.roomArray[i],
					secondaryDataBase.roomTypeAmount[i],
					DataBase.schoolPeriodCount);
		}
	}

	Department[] departments_copy = new Department[DataBase.departments.length];

	private void departmentsTransfer() {
		// Creates the Departments objects
		departments_copy = new Department[DataBase.departments.length];
		int earlierDeptPos = -1;
		for (int index = 0; index < departments_copy.length; index++) {
			departments_copy[index] = DataBase.departments[index];
		}
		DataBase.departments = new Department[secondaryDataBase.deptArray.length];
		for (int i = 0; i < secondaryDataBase.deptArray.length; i++) {
			// Departments
			System.out
					.println("Transfering: " + secondaryDataBase.deptArray[i]);
			for (int searchIndex = 0; searchIndex < departments_copy.length; searchIndex++) {// check
																								// if
																								// existed
																								// earlier
				if (secondaryDataBase.deptArray[i]
						.equals(departments_copy[searchIndex].getName())) {
					earlierDeptPos = searchIndex;
				}
			}
			DataBase.departments[i] = new Department(
					secondaryDataBase.deptArray[i]);
			if (earlierDeptPos != -1)// if it existed earlier
			{
				System.out.println(DataBase.departments[i].getName()
						+ "("
						+ earlierDeptPos
						+ ") Setting BackGroundColor to : "
						+ departments_copy[earlierDeptPos].backGroundColor
								.getRed()
						+ ", "
						+ departments_copy[earlierDeptPos].backGroundColor
								.getGreen()
						+ ", "
						+ departments_copy[earlierDeptPos].backGroundColor
								.getBlue() + ", ");
				DataBase.departments[i]
						.setBackGroundColor(departments_copy[earlierDeptPos].backGroundColor);
				DataBase.departments[i]
						.setForeGroundColor(departments_copy[earlierDeptPos].foreGroundColor);
			} else {
				System.out
						.println(DataBase.departments[i].getName()
								+ "("
								+ earlierDeptPos
								+ ") Setting BackGroundColor to : Default(250, 240, 180)");
				DataBase.departments[i].setBackGroundColor(new Color(250, 240,
						180));
				DataBase.departments[i].setForeGroundColor(new Color(0, 0, 0));
			}
		}
	}

	private void coursesTransfer() {
		// Transfers courses
		DataBase.courses = new Course[dataStrArray.length];
		for (int i = 0; i < dataStrArray.length; i++) {
			int gradeInt = Integer.parseInt(dataStrArray[i][3]) - 9;

			int secInt = Integer.parseInt(dataStrArray[i][5]);
			System.out.println("Transferring: *" + dataStrArray[i][0]
					+ "* with SectionCount :" + secInt);
			// look for department name
			deptInt = DataBase.findDeptPos(dataStrArray[i][1]);
			perPrefBool = analyseRestrictionsBool(dataStrArray[i][6]);
			DataBase.courses[i] = new Course(dataStrArray[i][0],
					DataBase.departments[deptInt], dataStrArray[i][2],
					gradeInt, dataStrArray[i][4], secInt);
			for (int b = 0; b < perPrefBool.length; b++) {
				DataBase.courses[i].setPerPref(b, perPrefBool[b]); // edit]
			}
		}
	}

	private void repatchTimeT() {
		// resize timeT to new dimensions
		int period = DataBase.schoolPeriodCount;
		int grade = DataBase.schoolGradeCount;
		int numberOfDepartments = DataBase.departments.length;
		int numberOfRowsPerDept = grade * period;
		int numberOfColsPerDept = DataBase.colCountPerLevel
				* DataBase.levels.length;
		int numberOfSemesters = 2;
		DataBase.timeT = new Course[numberOfDepartments][numberOfRowsPerDept][numberOfColsPerDept][numberOfSemesters];
		DataBase.timeTcopy = new Course[numberOfDepartments][numberOfRowsPerDept][numberOfColsPerDept][numberOfSemesters];
		DataBase.selected_Department = DataBase.departments[0].getName();
	}

	class deptRoomsCourseTransfer implements ActionListener {
		public void actionPerformed(ActionEvent args0) {

			dataStrArray = new String[model.getRowCount()][7];

			// assigns table values to the data array
			for (int i = 0; i < dataStrArray.length; i++) {
				for (int b = 0; b < dataStrArray[0].length; b++) {

					if (b == 3 || b == 5) {
						dataStrArray[i][b] = Integer.toString((int) model
								.getValueAt(i, b));
					} else {
						dataStrArray[i][b] = (String) model.getValueAt(i, b);
					}

				}
			}
			coursesWithInvalidity = "";
			if (checkDataValidity()) {
				roomsTransfer();
				departmentsTransfer();
				coursesTransfer();
				repatchTimeT();
				// sets visibility to false
				setVisible(false);
				// disposes of the frame
				dispose();
				// Set timeT size
				DataBase.isItFirstLoad = false;
				DataBase.isDataBaseEmpty = false;
				DataBase.isDataBaseEmpty = false;
				for (int i = 0; i < DataBase.timeT.length; i++) {
					for (int j = 0; j < DataBase.timeT[0].length; j++) {
						for (int k = 0; k < DataBase.timeT[0][0].length; k++) {
							for (int l = 0; l < DataBase.timeT[0][0][0].length; l++) {
								if (DataBase.timeT[i][j][k][l] != null) {
									DataBase.timeT[i][j][k][l] = DataBase
											.findCourse(DataBase.timeT[i][j][k][l]
													.getName());
								}
							}
						}
					}
				}
				DataBase.rescanForValidity();
				Course_Manager_Main.window1.refreshDisplay();
				Course_Manager_Main.window1.generate_DeptButtons();
				Course_Manager_Main.window1.window2.dispose();
				Course_Manager_Main.window1.window2 = new Course_Manager_Window2();
				System.out.println("Close the courses frame");
				System.out.println("SchoolPeriodCount: "
						+ DataBase.schoolPeriodCount);
			} else {
				// Spawn Notice Frame
				JFrame invalids_Frame = new JFrame();
				JList<String> invalids_List = new JList<String>(
						coursesWithInvalidity.split("</separator>"));
				JScrollPane invalids_SPane = new JScrollPane(invalids_List);
				invalids_Frame.setTitle("Error: Invalid Information");
				invalids_Frame.setContentPane(invalids_SPane);
				invalids_SPane
						.setBorder(BorderFactory
								.createTitledBorder("Invalid Information Found in Courses: "));
				invalids_Frame.setVisible(true);
				invalids_Frame.pack();
				invalids_Frame.setBounds(0, 0, 500, 600);
				invalids_Frame.setLocationRelativeTo(null);
			}
		}
	}
}
