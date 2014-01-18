/* Class: IIdept
 * Author: Luka Pavlovic Ed. Troylan Tempra Jr.
 * Last Modified: 20/1/2013
 * Description: Used to define/construct the edit department info panel. That is, the top half
 * of the 3rd Frame when a new project is being made
 */
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class IIdept extends JPanel {

	private JLabel glb;
	private JLabel deptList = new JLabel();
	private JTextField jtfDept = new JTextField(15);
	private Insets standardInset = new Insets(6, 6, 6, 6);
	private Insets actionPanelInset = new Insets(2, 2, 2, 2);
	// declare the JButtons : ---------------------------------- Purpose:
	// Add the department manually
	private JButton addDept = new JButton("Add Manually");
	// Add the department from the combo box
	private JButton addDeptCB = new JButton("Add To List");
	// Clear all departments
	private JButton clearAll = new JButton("Clear All");
	// undo - delete last department
	private JButton undoMistake = new JButton("Undo");
	// declare strings used for storing the departments and outputting them
	private String tempStr = "";
	private String departments = "No departments Set";
	// lists all of the departments
	private String[] cb_list = { "Visual-Performing Arts", "Business",
			"Social Science", "English", "Moderns", "Academic Resource",
			"Guidance", "Religion", "Math", "Physical Education", "Science",
			"Technologies" };
	private JComboBox<String> cb;
	private int counter = 0;
	// create the panel & give it the gird bag layout
	private JPanel panel = new JPanel(new GridBagLayout());

	// the constructor create the panel by adding the action panel (where all
	// the buttons are) and the info panel (where the list of departments is
	// displayed)
	public IIdept() {

		// create the GridBagConstraints for the JPanel
		GridBagConstraints gbc = new GridBagConstraints();

		// set the weights
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		// set the fill
		gbc.fill = GridBagConstraints.BOTH;
		// define the inset size
		gbc.insets = standardInset;

		// add the panels to the main panel...

		// add the first panel: action
		// set the coordinates
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(addActionPanel(), gbc);

		// add the first panel: action
		// set the coordinates
		gbc.gridx = 1;
		gbc.gridy = 0;
		panel.add(addInfoPanel(), gbc);

		// pack it
	}

	// =================================================================______JPANELS

	// method makes a JPanel and returns it; The INFO PANEL is made here
	public JPanel addInfoPanel() {

		JPanel infoPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		infoPanel.setPreferredSize(new Dimension(250, 250));
		infoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		// addingTheComponents_________________________________________________
		// set the insets
		gbc.insets = actionPanelInset;
		// add the departments label..........
		glb = new JLabel("Departments:");
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		infoPanel.add(glb, gbc);

		gbc.weightx = 1.0;
		gbc.weighty = 1.0;

		// add the list of departments.............
		deptList.setText(departments);
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.BOTH;
		infoPanel.add(new JScrollPane(deptList), gbc);

		return infoPanel;
	}

	// method makes a JPanel and returns it; The ACTION PANEL is made here
	public JPanel addActionPanel() {

		JPanel actionPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		actionPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		actionPanel.setPreferredSize(new Dimension(500, 250));
		gbc.insets = actionPanelInset;

		// -----------------------------Adding Components

		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.BOTH;

		// add the first label: User Information
		glb = new JLabel(
				"<html>Add each department in the school for the user interface to be more organized later <p><p> </html>");
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		actionPanel.add(glb, gbc);

		gbc.gridwidth = 1;

		// add Label: "Add Departments"
		glb = new JLabel("Add Departments from a list below");
		gbc.gridx = 0;
		gbc.gridy = 1;
		actionPanel.add(glb, gbc);

		// add a combo box
		cb_list = sortTheList(cb_list);
		cb = new JComboBox<String>(cb_list);
		gbc.gridx = 0;
		gbc.gridy = 2;
		actionPanel.add(cb, gbc);

		// add a combo box add button
		gbc.gridx = 1;
		gbc.gridy = 2;
		actionPanel.add(addDeptCB, gbc);

		// add Label: "Add Departments"
		glb = new JLabel("Add a department manually:");
		gbc.gridx = 0;
		gbc.gridy = 3;
		actionPanel.add(glb, gbc);

		// add Text Field: jtfDept - Manually adding departments
		gbc.gridx = 0;
		gbc.gridy = 4;
		actionPanel.add(jtfDept, gbc);

		// add Button: addDept - Manually adding departments
		gbc.gridx = 1;
		gbc.gridy = 4;
		actionPanel.add(addDept, gbc);

		// add Button: Clear All
		gbc.gridx = 0;
		gbc.gridy = 5;
		actionPanel.add(clearAll, gbc);

		// add Button: Undo Mistake
		gbc.gridx = 1;
		gbc.gridy = 5;
		actionPanel.add(undoMistake, gbc);

		// add ActionListeners
		clearAll.addActionListener(new resetDept());
		undoMistake.addActionListener(new ctrl_Z());
		addDept.addActionListener(new newDept());
		addDeptCB.addActionListener(new addCB());
		cb.addActionListener(new newDeptCB());

		// packs everything together
		return actionPanel;
	}

	// =================================================================______ACTION_LISTENERS

	public class addCB implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// get the string from the combo box
			String textTest = (String) cb.getSelectedItem();
			if (validateItem(textTest)) {
				tempStr = tempStr + textTest + "<p>";
				departments = "<html> " + tempStr + "</html>";

				deptList.setText(departments);
				jtfDept.setText("");
				counter++;
			} else {
				JOptionPane.showMessageDialog(new JFrame(),
						"Department Already Listed", "Adding Error",
						JOptionPane.ERROR_MESSAGE);

			}

		}
	}

	public class resetDept implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			deptList.setText("Nothing Listed");

			tempStr = "";
			counter = 0;
		}
	}

	// the undo button
	public class ctrl_Z implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// makes a temporary string array for the departments and
			// descriptions, used for removing one
			String[] ttt = tempStr.split("<p>");

			tempStr = "";
			if (!(ttt.length <= 1)) {
				for (int i = 0; i < ttt.length - 1; i++) {
					tempStr = tempStr + ttt[i] + "<p>";

				}
				counter--;
				// reset the label
				departments = "<html> " + tempStr + "</html>";
				deptList.setText(departments);
			} else {
				counter = 0;
				deptList.setText("No Departments Listed");
			}

		}
	}

	public class newDept implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			String textTest = jtfDept.getText();

			if (validateStr(textTest)) {
				if (validateItem(textTest)) {
					// string holds the list of departments
					tempStr = tempStr + textTest + "<p>";
					// departments string is just for show
					departments = "<html> " + tempStr + "</html>";
					// set the deptList to show the list of departments
					deptList.setText(departments);
					// reset the text box to blank after a new room is added
					jtfDept.setText("");
					// add one to the counter for keeping track of the total
					// number
					// of departments
					counter++;
				}
				// if the department is listed already: error message pops up
				// informing the user
				else {
					JOptionPane.showMessageDialog(new JFrame(),
							"Department Already Listed", "Adding Error",
							JOptionPane.ERROR_MESSAGE);
				}

			}
			// if the text field is blank: error message pops up informing the
			// user
			else {
				JOptionPane.showMessageDialog(new JFrame(),
						"Do not leave the text field blank", "Text Error",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	// whenever a new department is selected, the text field gets reset to blank
	public class newDeptCB implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			jtfDept.setText("");
		}
	}

	// =================================================================______OTHER_METHODS

	// checks if the user inputs a valid string
	public boolean validateStr(String s) {
		if (s.equals("")) {
			// if the user does not input anything: return false
			return false;
		} else {
			// if the user's input is valid: return true
			return true;
		}

	}

	// make it look cool.. set look and feel to nimbus
	public void setLookAndFeel() {
		try {

			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");

		} catch (Exception exc) {
			// ignore error
			exc.printStackTrace();
		}
	}

	// sorts the list of departments in alphabetical order
	public String[] sortTheList(String data[]) {

		for (int end = data.length - 1; end > 0; end--) {
			int biggie = 0; // Location of largest number
			for (int i = 1; i <= end; i++) {
				if (data[i].compareTo(data[biggie]) > 0) {
					biggie = i;
				}

			}
			String temp = data[end];
			data[end] = data[biggie];
			data[biggie] = temp;
		}

		return data;
	}

	public JPanel getPanel() {
		return panel;
	}

	// saves the department list and amount of departments to the
	// secondaryDataBase
	public void toDB() {
		secondaryDataBase.deptList = tempStr;
		secondaryDataBase.deptCount = counter;
	}

	public void refreshList(String[] tempListAr) {

		// the array passed holds the
		for (int i = 0; i < tempListAr.length; i++) {
			tempStr = tempStr + tempListAr[i] + "<p>";
			// add one to the counter for keeping track of the total number
			// of departments
			counter++;
		}

		// departments string is just for show
		departments = "<html> " + tempStr + "</html>";
		// set the deptList to show the list of departments
		deptList.setText(departments);
		// reset the text box to blank after a new room is added
		jtfDept.setText("");

	}

	// method checks if department was previously listed
	public boolean validateItem(String s) {
		// create the temporary array holding each department
		String[] tempAr = tempStr.split("<p>");
		for (int i = 0; i < tempAr.length; i++) {
			// if the string that is tested for matches with any of the items
			// listed, false is returned, if not true is returned
			if (s.equals(tempAr[i])) {
				return false;
			}
		}
		return true;
	}
}
