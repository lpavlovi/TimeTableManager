/* Class: IIlunchPrefs
 * Author: Luka Pavlovic Ed. Troylan Tempra Jr.
 * Last Modified:20/1/2013
 * Description: Second window for new project option
 */
import javax.swing.*;

import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class IIlunchPrefs extends JFrame {

	// declare Labels
	private JLabel glb; // general Label - used for everything
	// amount of periods in a day
	private int perPerDay = secondaryDataBase.amountOfPeriods; // periods per
																// day
	// declare public variables
	private String[] lunchdata; // holds data the user input
	// declare integer holding the value at which the grades start
	private int gs = 9;
	private int totalGrades = secondaryDataBase.grades;
	// declare check boxes
	JCheckBox[] cb_09;
	JCheckBox[] cb_10;
	JCheckBox[] cb_11;
	JCheckBox[] cb_12;
	// insets
	private Insets defaultInsets = new Insets(0, 5, 5, 5);
	// declare buttons
	private JButton a_Btn; // "Uncheck All" button resets all check boxes
	private JButton b_Btn; // "Check All" button sets all check boxes to true
	private JButton back_Btn; // "Back" button goes back to previous frame
	private JButton next_Btn; // "Next" advances to next frame
	private JButton cancel_Btn; // "Cancel" hides and disposes the frame
	// Separators
	private JSeparator sep; // used to separate the title and the check box
							// panel
	static boolean loadMe = false; // used for loading the previous data
	// private String file_name = "lunchPref.ses";
	private static int periodsFS = 0;

	private String mode = "";

	// ------------------------------------------------Constructor
	public IIlunchPrefs(String mode) {
		// set the title
		super("Lunch Preferences");
		this.mode = mode;
		// set up the layout manager
		JPanel panel = new JPanel(new GridBagLayout());
		// give it a black border
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		// make a new GridBagConstraints
		GridBagConstraints gbc = new GridBagConstraints();
		// set this as content pane
		setContentPane(panel);
		// set size
		// panel.setPreferredSize(new Dimension(325, 350));
		// set exit
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// Set insets
		gbc.insets = defaultInsets;
		// Set the weights
		gbc.weightx = 1.00;
		gbc.weighty = 0.00;

		// i is used for organizing the rows when it comes to placement of the
		// components
		int i = 0;

		gbc.anchor = GridBagConstraints.CENTER;
		// set the fill

		// ---------------Adding Components

		// add label: Instructions
		glb = new JLabel(
				"<html><div style=\"text-align: center;\">"
						+ "The program will avoid placing duplicate<p>sections in selected periods. "
						+ "(Will only affect automation)" + "</html>");
		gbc.gridx = 0;
		gbc.gridy = i;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.NONE;
		panel.add(glb, gbc);

		i++;// 1
		gbc.fill = GridBagConstraints.BOTH;
		sep = new JSeparator(JSeparator.HORIZONTAL);
		gbc.gridx = 0;
		gbc.gridy = i;
		panel.add(sep, gbc);

		i++;// 2

		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;

		// add label: "Grades"
		glb = new JLabel("Grades");
		gbc.gridx = 0;
		gbc.gridy = i;
		panel.add(glb, gbc);

		// add label: "Periods"
		glb = new JLabel("Periods");
		gbc.gridx = 1;
		gbc.gridy = i;
		panel.add(glb, gbc);

		gbc.fill = GridBagConstraints.BOTH;
		i++;// 3

		// ----Check Boxes
		gbc.weightx = 1.00;
		gbc.weighty = 1.00;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = i;
		panel.add(checkLunch(), gbc);

		// add a separator
		sep = new JSeparator(JSeparator.HORIZONTAL);
		gbc.gridx = 0;
		gbc.gridy = i;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		panel.add(sep, gbc);

		i++;

		gbc.weighty = 0.00;

		// ------------------------------------::Buttons
		a_Btn = new JButton("Uncheck All");
		b_Btn = new JButton("Check All");

		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;
		gbc.gridy = i;
		panel.add(a_Btn, gbc);

		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 1;
		gbc.gridy = i;
		panel.add(b_Btn, gbc);

		i++;

		// add separator
		sep = new JSeparator(JSeparator.HORIZONTAL);
		gbc.gridx = 0;
		gbc.gridy = i;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		panel.add(sep, gbc);

		i++;

		// bottom row of buttons: next, back, cancel
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = i;
		gbc.gridwidth = 2;
		panel.add(addButtons(), gbc);

		// ----------------------------------Add Action Listeners
		a_Btn.addActionListener(new unCheckAll());
		b_Btn.addActionListener(new checkAll());
		next_Btn.addActionListener(new next());
		back_Btn.addActionListener(new back());
		cancel_Btn.addActionListener(new cancel());
		if (mode.equals("LunchPrefsOnly")) {
			next_Btn.setText("Update");
			back_Btn.setEnabled(false);
		}
		// ----------Load

		// if continuing from a previous session
		// previous buttons will be selected
		if (loadMe) {
			loadLunchPrefs();
		}
		// ---------------Pack
		pack();

	}

	// -----------------------------------------Check Boxes
	public JPanel checkLunch() {

		JPanel lunchPanel = new JPanel(new GridBagLayout());
		// give it a black border
		lunchPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		// make a new GridBagConstraints
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = defaultInsets;
		int i = 0;

		// add label: Grade 9
		glb = new JLabel("Grade " + gs + " :");
		gs++;
		gbc.gridx = 0;
		gbc.gridy = i;
		lunchPanel.add(glb, gbc);

		// add check boxes for Grade 9
		cb_09 = new JCheckBox[perPerDay];

		for (int b = 0; b < cb_09.length; b++) {
			gbc.gridx = b + 1;
			gbc.gridy = i;
			cb_09[b] = new JCheckBox("" + gbc.gridx);
			lunchPanel.add(cb_09[b], gbc);
		}
		i++;// 1

		// add label: Grade 10
		glb = new JLabel("Grade " + gs + " :");
		gs++;
		gbc.gridx = 0;
		gbc.gridy = i;
		lunchPanel.add(glb, gbc);

		// add check boxes for Grade 10
		cb_10 = new JCheckBox[perPerDay];

		for (int b = 0; b < perPerDay; b++) {
			gbc.gridx = b + 1;
			gbc.gridy = i;
			cb_10[b] = new JCheckBox("" + gbc.gridx);
			lunchPanel.add(cb_10[b], gbc);
		}
		i++;// 2

		// add label: Grade 11
		glb = new JLabel("Grade " + gs + " :");
		gs++;
		gbc.gridx = 0;
		gbc.gridy = i;
		lunchPanel.add(glb, gbc);

		// add check boxes for Grade 11
		cb_11 = new JCheckBox[perPerDay];

		for (int b = 0; b < perPerDay; b++) {
			gbc.gridx = b + 1;
			gbc.gridy = i;
			cb_11[b] = new JCheckBox("" + gbc.gridx);
			lunchPanel.add(cb_11[b], gbc);
		}
		i++;// 3

		// add label: Grade 12
		glb = new JLabel("Grade " + gs + " :");
		gs++;
		gbc.gridx = 0;
		gbc.gridy = i;
		lunchPanel.add(glb, gbc);

		// add check boxes for Grade 12
		cb_12 = new JCheckBox[perPerDay];

		for (int b = 0; b < perPerDay; b++) {
			gbc.gridx = b + 1;
			gbc.gridy = i;

			cb_12[b] = new JCheckBox("" + gbc.gridx);
			lunchPanel.add(cb_12[b], gbc);
		}

		return lunchPanel;
	}

	// ------------------------------------------------Action Listeners

	// this button action listener unchecks all of the checkboxes
	class unCheckAll implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			// for loop covers all of the boxes and sets the selected to false
			for (int i = 0; i < perPerDay; i++) {

				for (int b = 0; b < cb_09.length; b++) {
					cb_09[b].setSelected(false);
				}
				for (int b = 0; b < cb_10.length; b++) {
					cb_10[b].setSelected(false);
				}
				for (int b = 0; b < cb_11.length; b++) {
					cb_11[b].setSelected(false);
				}
				for (int b = 0; b < cb_12.length; b++) {
					cb_12[b].setSelected(false);
				}

			}

		}
	}

	class next implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setVisible(false);

			secondaryDataBase.lunchPrefs = analyseData();

			boolean[][] lunchPrefBoolean = analyseData();

			lunchdata = new String[lunchPrefBoolean.length];
			for (int i = 0; i < lunchdata.length; i++) {
				lunchdata[i] = "";
			}

			// creates the lunchdata array based of the boolean array containing
			// the lunch preferences
			for (int y = 0; y < lunchPrefBoolean.length; y++) {
				for (int x = 0; x < lunchPrefBoolean[0].length; x++) {
					if (lunchPrefBoolean[y][x]) {

						lunchdata[y] += "1 ";
					} else {

						lunchdata[y] += "0 ";
					}

				}

			}
			// displays the lunch data array
			System.out.println("Lunch Data Array");
			for (int i = 0; i < lunchdata.length; i++) {
				System.out.println(lunchdata[i]);
			}

			loadMe = true;
			// moves on to rooms and departments frame
			if (mode.equals("All")) {
				IIroomsAndDepts rnd = new IIroomsAndDepts("All");
				rnd.setVisible(true);
			} else if (mode.equals("LunchPrefsOnly")) {
				System.out.println("Transferring LunchPrefs");
				DataBase.lunchPrefs = new boolean[secondaryDataBase.lunchPrefs.length][secondaryDataBase.lunchPrefs[0].length];
				for (int i = 0; i < DataBase.lunchPrefs.length; i++) {
					for (int b = 0; b < DataBase.lunchPrefs[0].length; b++) {
						DataBase.lunchPrefs[i][b] = secondaryDataBase.lunchPrefs[i][b];
					}
				}
				DataBase.grades = new Grade[secondaryDataBase.grades];
				for (int i = 0; i < DataBase.grades.length; i++) {
					DataBase.grades[i] = new Grade(i,
							secondaryDataBase.lunchPrefs[i],
							DataBase.schoolPeriodCount);
				}
			} else {
				System.out.println("INVALID IILUNCHPREFS MODE: " + mode);
			}

		}
	}

	// this action listener checks all of the buttons, although it is not
	// recommended, it still exists
	class checkAll implements ActionListener {
		public void actionPerformed(ActionEvent args0) {

			// cycles through the periods and sets all of the check boxes to
			// true (selected)
			for (int i = 0; i < perPerDay; i++) {
				for (int b = 0; b < cb_09.length; b++) {
					cb_09[b].setSelected(true);
				}
				for (int b = 0; b < cb_10.length; b++) {
					cb_10[b].setSelected(true);
				}
				for (int b = 0; b < cb_11.length; b++) {
					cb_11[b].setSelected(true);
				}
				for (int b = 0; b < cb_12.length; b++) {
					cb_12[b].setSelected(true);
				}

			}

		}

	}

	// takes the user back to the first input frame
	class back implements ActionListener {
		public void actionPerformed(ActionEvent args0) {
			setVisible(false);
			dispose();

			// calls up the basics frame, passes a 2: will load the previous
			// data
			IIbasics basicsObj = new IIbasics(2);
			basicsObj.setVisible(true);

		}

	}

	// a generic cancel button
	class cancel implements ActionListener {
		public void actionPerformed(ActionEvent args0) {
			// hide frame
			setVisible(false);
			// return resources
			dispose();

		}

	}

	// -----------------------------------------------Methods

	// loads info from temporary database and selects check boxes representing
	// the data
	public void loadLunchPrefs() {
		periodsFS = secondaryDataBase.amountOfPeriods;
		// getLunchPref
		boolean[][] lda = new boolean[secondaryDataBase.lunchPrefs.length][secondaryDataBase.lunchPrefs[0].length];

		for (int i = 0; i < lda.length; i++) {
			for (int b = 0; b < lda[0].length; b++) {
				lda[i][b] = secondaryDataBase.lunchPrefs[i][b];
			}
		}

		if (periodsFS == lda[0].length) {

			for (int x = 0; x < lda[0].length; x++) {

				if (lda[0][x] == true) {
					cb_09[x].setSelected(true);
				}
				if (lda[1][x] == true) {
					cb_10[x].setSelected(true);
				}
				if (lda[2][x] == true) {
					cb_11[x].setSelected(true);
				}
				if (lda[3][x] == true) {
					cb_12[x].setSelected(true);
				}

			}

		} else {
			// warning message shows user that the amount of periods changed, to
			// aviod errors, the file was not loaded
			JOptionPane.showMessageDialog(new JFrame("Periods do not match"),
					"The period amount have been changed",
					"Periods do not match", JOptionPane.ERROR_MESSAGE);

		}

	}

	public boolean[][] analyseData() {

		// creates a 2D boolean array for each grade and period
		boolean[][] checkboxBooleanArray = new boolean[totalGrades][perPerDay];

		// for loop cycles through each one and checks to which checkboxes are
		// selected and assigns that value to the sepcific position
		for (int y = 0; y < checkboxBooleanArray.length; y++) {
			for (int x = 0; x < checkboxBooleanArray[0].length; x++) {
				if (y == 0) {
					checkboxBooleanArray[y][x] = cb_09[x].isSelected();
				} else if (y == 1) {
					checkboxBooleanArray[y][x] = cb_10[x].isSelected();
				} else if (y == 2) {
					checkboxBooleanArray[y][x] = cb_11[x].isSelected();
				} else if (y == 3) {
					checkboxBooleanArray[y][x] = cb_12[x].isSelected();
				}
			}
		}

		// returns the 2D boolean array
		return checkboxBooleanArray;

	}

	public JPanel addButtons() {
		// make the option Panel
		JPanel btnPanel = new JPanel(new GridBagLayout());
		// create the GridBagConstraints
		GridBagConstraints gbc = new GridBagConstraints();
		// set insets
		gbc.insets = defaultInsets;

		// initialize buttons
		back_Btn = new JButton("Back");
		next_Btn = new JButton("Next");
		cancel_Btn = new JButton("Cancel");

		// set coordinates
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		btnPanel.add(back_Btn, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		btnPanel.add(cancel_Btn, gbc);

		// set the weights
		// do you even lift?
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		btnPanel.add(next_Btn, gbc);

		return btnPanel;
	}

	// passes a boolean which will adjust the loadMe boolean used for loading
	// files and starting with clean template
	public static void checkLoad(boolean loadFile) {
		loadMe = loadFile;
	}

}
