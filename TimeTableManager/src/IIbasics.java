/* Class: IIbasics
 * Author: Luka Pavlovic Ed. Troylan Tempra Jr.
 * Last Modified:20/1/2013
 * Description: First window for new project option
 */
import javax.swing.*;

import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class IIbasics extends JFrame {

	// declare Labels
	private JLabel glb; // general Label - used for everything
	// declare text Fields
	// private JTextField a_tf = new JTextField("OLMC", 15); // Name of school
	private JTextField b_tf = new JTextField("5", 15); // # periods in a day
	private JTextField c_tf = new JTextField(15); // Name of new project

	// declare buttons
	private JButton ca_Btn; // "Clear All" button resets all text fields
	private JButton next_Btn; // "Next" advances to next frame
	private JButton cancel_Btn;
	// declare radio buttons
	private JRadioButton rb_4 = new JRadioButton("4", true);
	private JRadioButton rb_5 = new JRadioButton("5");
	ButtonGroup b_group = new ButtonGroup();
	// insets
	private Insets defaultInsets = new Insets(5, 5, 5, 5);
	private int previousSession = 0;

	// declare strings

	// -----------------------------------------------------------------Constructor

	public IIbasics(int previousSession) {
		this.previousSession = previousSession;// always set at the start
		// set the title
		if (previousSession == 0) {
			setTitle("New Project");
		} else {
			setTitle("Project");
		}
		// set up the layout manager
		JPanel infoPanel = new JPanel(new GridBagLayout());
		// give it a black border
		infoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		// make a new GridBagConstraints
		GridBagConstraints gbc = new GridBagConstraints();
		// set this as content pane
		setContentPane(infoPanel);
		// set size
		// infoPanel.setPreferredSize(new Dimension(350, 270));
		// set exit
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// Set insets
		gbc.insets = defaultInsets;

		int i = 0;

		// set the weights
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		// set the fill
		gbc.fill = GridBagConstraints.BOTH;

		// ---------------Adding

		// add label: Name of the new project
		glb = new JLabel("Name of new project:");
		gbc.gridx = 0;
		gbc.gridy = i;
		infoPanel.add(glb, gbc);

		gbc.weightx = 1.0;
		// add Text Field: for name of school - c_tf
		gbc.gridx = 1;
		gbc.gridy = i;
		infoPanel.add(c_tf, gbc);

		i++;// 1
		gbc.weightx = 0.0;
		// add label: Name of school
		glb = new JLabel("Name of school:");
		gbc.gridx = 0;
		gbc.gridy = i;
		// infoPanel.add(glb, gbc);

		// add Text Field: for name of school - a_tf
		// a_tf.setText("OLMC");
		gbc.gridx = 1;
		gbc.gridy = i;
		// infoPanel.add(a_tf, gbc);

		i++;// 2

		// add label: Number of Periods
		glb = new JLabel("Number of periods:");
		gbc.gridx = 0;
		gbc.gridy = i;
		infoPanel.add(glb, gbc);

		// add Text Field: for number of periods - b_tf

		gbc.gridx = 1;
		gbc.gridy = i;
		infoPanel.add(b_tf, gbc);
		if (previousSession == -1) {
			b_tf.setEnabled(false);
			// a_tf.setText(secondaryDataBase.nameOfSchool);
			b_tf.setText(Integer.toString(secondaryDataBase.amountOfPeriods));
			c_tf.setText(secondaryDataBase.projectName);
		}
		i++;// 3

		// add label: Number of grades in the school
		glb = new JLabel("Number of grades:");
		gbc.gridx = 0;
		gbc.gridy = i;
		// infoPanel.add(glb, gbc);

		// add RadioButtons
		gbc.gridx = 1;
		gbc.gridy = i;
		// infoPanel.add(gradeNumPanel(), gbc);

		i++;// 4

		// add label: Number of grades in the school
		glb = new JLabel("School starts at which grade:");
		gbc.gridx = 0;
		gbc.gridy = i;
		// infoPanel.add(glb, gbc);

		// add Text Field: for number of grades - e_tf
		gbc.gridx = 1;
		gbc.gridy = i;
		// infoPanel.add(e_tf, gbc);

		i++;// 5

		// add label: Number of grades in the school
		glb = new JLabel("Number of semesters:");
		gbc.gridx = 0;
		gbc.gridy = i;
		// infoPanel.add(glb, gbc);

		// add Text Field: for number of semesters - d_tf
		gbc.gridx = 1;
		gbc.gridy = i;
		// infoPanel.add(d_tf, gbc);

		i++;// 6

		// ------------------------------------Buttons

		gbc.gridwidth = 1;
		ca_Btn = new JButton("Clear All");
		gbc.gridx = 0;
		gbc.gridy = i;
		gbc.gridwidth = 2;
		infoPanel.add(ca_Btn, gbc);
		if (previousSession == -1) {// if editing fileName (constructed from
									// editMenu)
			ca_Btn.setEnabled(false);
		}
		i++;

		cancel_Btn = new JButton("Cancel");
		gbc.gridx = 0;
		gbc.gridy = i;
		gbc.gridwidth = 1;
		infoPanel.add(cancel_Btn, gbc);

		next_Btn = new JButton("Next");
		gbc.gridx = 1;
		gbc.gridy = i;
		infoPanel.add(next_Btn, gbc);

		// ----------------------------------Add Action Listeners
		ca_Btn.addActionListener(new clearAllBtnClick());
		next_Btn.addActionListener(new setLunchPrefernces());
		if (previousSession == -1) {
			next_Btn.setText("Update");
		}
		cancel_Btn.addActionListener(new cancelClick());

		// ---------------------------Loading

		// TODO: Fix loading

		// previousSession is 1: previous template (session) will be loaded
		// Use: when user want to edit everything from start to finish
		// **The Time Table will be refreshed**
		if (previousSession == 1) {

			loadPreviousSession();
			IIcourses.checkLoad(true);
			IIroomsAndDepts.checkLoad(true);
			IIlunchPrefs.checkLoad(true);

		}

		// previousSession is 2: only the previous session for this frame will
		// be loaded
		// Use: when back is clicked from the lunch preferences frame
		else if (previousSession == 2) {
			loadPreviousSession();
		}

		// previousSession is 0: user gets a blank template
		else if (previousSession == 0) {
			IIcourses.checkLoad(false);
			IIroomsAndDepts.checkLoad(false);
			IIlunchPrefs.checkLoad(false);
		}

		// anything else input will result in a blank template (Nothing will be
		// loaded)

		// ---------------Pack
		pack();

	}

	// -----------------------------------------------------------------ACTION_LISTENERS

	public class clearAllBtnClick implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {

			// set all of the text fields to blank
			// a_tf.setText("");
			b_tf.setText("");
			c_tf.setText("");

			// set both radio buttons to unselected
			rb_4.setSelected(false);
			rb_5.setSelected(false);
		}

	}

	public class setLunchPrefernces implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {

			// declare mainCheck which is used to check if the user filled out
			// each text field correctly
			boolean mainCheck = true;

			// -----------------------Validate

			if (// validateText(a_tf.getText()) &&
			validateInt(b_tf.getText()) && validateText(c_tf.getText())) {
				mainCheck = true;
			} else {
				// if the user input invalid values, the main check is false and
				// the are not able to continue
				mainCheck = false;
			}

			// gets boolean from Radio Buttons to see how many grades there are
			if (rb_4.isSelected()) {
				secondaryDataBase.grades = 4;

			} else if (rb_5.isSelected()) {
				secondaryDataBase.grades = 5;

			} else {
				mainCheck = false;
			}

			// if values from text boxes are valid: moves on to the next frame
			if (mainCheck) {
				try {
					// assigns each value from the text box to the temporary
					// database and to the basicDataArray which is used for
					// saving to a file

					// transfer School Name to database
					// secondaryDataBase.nameOfSchool = a_tf.getText();

					// transfer Project Name to database
					secondaryDataBase.projectName = c_tf.getText();

					// transfer grade start to database
					secondaryDataBase.gradesStart = 9;
					// transfer periods in a day
					secondaryDataBase.amountOfPeriods = Integer.parseInt(b_tf
							.getText());

					secondaryDataBase.semesters = 2;

				} catch (Exception ex) {
					ex.printStackTrace();
				}
				if (previousSession != -1) {
					IIlunchPrefs lp = new IIlunchPrefs("All");
					lp.setVisible(true);
				} else if (previousSession == -1) {
					DataBase.projectName = secondaryDataBase.projectName;
				}
				// Move onto the next frame

				// Set frame to be no longer visible
				setVisible(false);
				dispose();

			} else {

				JOptionPane.showMessageDialog(new JFrame(),
						"Do not leave the text fields blank", "Input Error",
						JOptionPane.ERROR_MESSAGE);

				// tells user that the text fields were not filled out correctly
				System.out.println("Error");
				System.out.println("Try Again");
			}

		}
	}

	// sets the frame visibility to false and disposes of the frame
	class cancelClick implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
			dispose();
		}
	}

	// -----------------------------------------------------------------METHODS

	// checks to see if the string is not empty or null
	public boolean validateText(String s) {
		if (s.equals("") || s.equals(null)) {
			return false;
		} else {
			return true;
		}
	}

	// checks if the string can be successfully be parsed to an integer
	public boolean validateInt(String s) {

		char asd = ' ';
		int ascii;
		if (s.length() != 0) {

			for (int i = 0; i < s.length(); i++) {
				// assigns a letter from String s to char asd
				asd = s.charAt(i);
				// if the integer value of the char is between 48 and 57, the
				// ascii
				// values for 0 through 9, true is returned
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

	// sets up the text fields to contain information from the database
	public void loadPreviousSession() {

		// assigns all values from the file to the text fields

		// if the name of school is blank it will automatically be set to OLMC
		// if (secondaryDataBase.nameOfSchool.equals("")) {
		// a_tf.setText("OLMC");
		// } else {
		// a_tf.setText(secondaryDataBase.nameOfSchool);
		// }
		b_tf.setText(Integer.toString(secondaryDataBase.amountOfPeriods));
		c_tf.setText(secondaryDataBase.projectName);
		// selects appropriate radio button based on the file info
		if (secondaryDataBase.grades == 5) {
			rb_5.setSelected(true);
		} else {
			rb_4.setSelected(true);
		}

	}

	// JPanel containing the grade numbers
	public JPanel gradeNumPanel() {
		JPanel gnPanel = new JPanel(new GridBagLayout());
		// give it a black border
		gnPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		// make a new GridBagConstraints
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;

		// adds the radio buttons to the group
		b_group.add(rb_4);
		b_group.add(rb_5);

		// places the radio buttons
		gbc.gridx = 0;
		gbc.gridy = 0;
		gnPanel.add(rb_4, gbc);

		gbc.gridx = 1;
		gnPanel.add(rb_5, gbc);

		// rb_4.setSelected(true);

		return gnPanel;
	}
}
