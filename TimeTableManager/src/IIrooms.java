/* Class: IIrooms
 * Author: Luka Pavlovic Ed. Troylan Tempra Jr.
 * Last Modified: 20/1/2013
 * Description: Used to define/construct the edit rooms info panel. That is, the bottom half
 * of the 3rd Frame when a new project is being made
 */
import javax.swing.*;

import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class IIrooms extends JPanel {

	// declare strings
	// used for getting the string from user
	private String outputText = "";
	private String roomTypeAndAmountStr = ""; // copy of string above
	private String roomListOutput = "No rooms set"; // room list output
	// declare insets
	private Insets standardInset = new Insets(6, 6, 6, 6);
	private Insets optInset = new Insets(2, 2, 2, 2);
	// declare labels
	private JLabel glb;
	private JLabel roomList = new JLabel(roomListOutput);
	// declare Text Fields
	private JTextField roomInput = new JTextField(10);
	private JTextField roomAmount = new JTextField(10);
	// declare buttons
	private JButton addRoomBtn = new JButton("Add Manually");
	private JButton addRoomBtnCB = new JButton("Add To List");
	private JButton undo = new JButton("Undo");
	private JButton clearAll = new JButton("Clear All");
	// declare the combo box
	private JComboBox<String> cb;

	private int counter = 0;
	private JPanel panel = new JPanel(new GridBagLayout());

	public IIrooms() {
		// set the title

		getTheList();
		// create the panel & give it the gird bag layout

		// panel.setPreferredSize(new Dimension(800, 400));
		GridBagConstraints gbc = new GridBagConstraints();

		// set the weights
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		// set the fill
		gbc.fill = GridBagConstraints.BOTH;
		// define the inset size

		gbc.insets = standardInset;

		// add the panels to the main panel...

		// add the first panel: action
		// set the coordinates
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(addOptionPanel(), gbc);

		// add the first panel: action
		// set the coordinates
		gbc.gridx = 1;
		gbc.gridy = 0;
		panel.add(addInfoPanel(), gbc);
	}

	public JPanel getPanel() {
		return panel;
	}

	public JPanel addOptionPanel() {
		// make the option Panel
		JPanel optPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		optPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		optPanel.setPreferredSize(new Dimension(500, 250));
		gbc.insets = optInset;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.BOTH;

		int i = 0;

		// add the first label: User Information
		glb = new JLabel(
				"<html>Add each type of room available for use in the school for the user interface to be more organized later</html>");
		gbc.gridx = 0;
		// gbc.gridy = 0;
		gbc.gridy = i;
		gbc.gridwidth = 2;
		optPanel.add(glb, gbc);

		i++;

		gbc.gridwidth = 1;
		// add Label: "Add Room Types"
		glb = new JLabel("Add Room Types from the list below");
		gbc.gridx = 0;
		// gbc.gridy = 1;
		gbc.gridy = i;
		gbc.gridwidth = 2;
		optPanel.add(glb, gbc);

		i++;

		gbc.gridwidth = 1;
		// add the combo box
		cb = new JComboBox<String>(getTheList());
		gbc.gridx = 0;
		// gbc.gridy = 2;
		gbc.gridy = i;
		optPanel.add(cb, gbc);

		// add Button: addRoomBtnCB - add room types from combo box
		gbc.gridx = 1;
		// gbc.gridy = 2;
		gbc.gridy = i;
		optPanel.add(addRoomBtnCB, gbc);

		i++;

		// add Label: "Add a room type manually"
		glb = new JLabel("Add a room type manually:");
		gbc.gridx = 0;
		// gbc.gridy = 3;
		gbc.gridy = i;
		gbc.gridwidth = 2;
		optPanel.add(glb, gbc);

		i++;

		gbc.gridwidth = 1;
		// add Text Field: roomInput - Manually adding room types
		gbc.gridx = 0;
		// gbc.gridy = 4;
		gbc.gridy = i;
		optPanel.add(roomInput, gbc);

		// add Button: addRoomBtn - Manually adding room types
		gbc.gridx = 1;
		// gbc.gridy = 4;
		gbc.gridy = i;
		gbc.gridwidth = 1;
		optPanel.add(addRoomBtn, gbc);

		i++;

		// add Label: "Add Departments"
		glb = new JLabel("Number of Rooms Available:");
		gbc.gridx = 0;
		gbc.gridy = i;
		gbc.gridwidth = 2;
		optPanel.add(glb, gbc);

		i++;

		gbc.gridwidth = 1;
		// add Text Field: roomAmount - number of rooms available
		gbc.gridx = 0;
		gbc.gridy = i;
		optPanel.add(roomAmount, gbc);

		i++;

		// add Button: Clear All
		gbc.gridx = 0;
		gbc.gridy = i;
		optPanel.add(clearAll, gbc);

		// add Button: Undo Mistake
		gbc.gridx = 1;
		gbc.gridy = i;
		optPanel.add(undo, gbc);

		// -------------------------------- add ActionListeners
		clearAll.addActionListener(new clearAllClick());
		undo.addActionListener(new undoClick());
		addRoomBtn.addActionListener(new addClick());
		// cb.addActionListener(new cbAction());
		addRoomBtnCB.addActionListener(new addCB());

		// return the option Panel
		return optPanel;
	}

	public JPanel addInfoPanel() {
		// make the option Panel
		JPanel infoPanel = new JPanel(new GridBagLayout());
		// give it a black border
		infoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		// create the GridBagConstraints
		GridBagConstraints gbc = new GridBagConstraints();
		infoPanel.setPreferredSize(new Dimension(250, 250));

		// set insets
		gbc.insets = optInset;

		// add the title of the panel label..........
		glb = new JLabel("Rooms:");
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		infoPanel.add(glb, gbc);

		roomList = new JLabel(roomListOutput);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		infoPanel.add(new JScrollPane(roomList), gbc);
		// last part of the method
		return infoPanel;
	}

	// this method returns the room list
	public String[] getTheList() {

		String[] roomsArray = { "General", "Art Room", "Drama Room",
				"Music Room", "Academic Resource", "Science Lab", "Auto Shop",
				"Wood Shop", "Graphics Lab", "Kitchen", "Gym", "Weight Room",
				"Video Room", "Computer Lab", "HealthCare Room", "Transitions",
				"credit", "General", "Art Room", "Drama Room", "Music Room",
				"Academic Resource", "Science Lab", "Auto Shop", "Wood Shop",
				"Graphics Lab", "Kitchen", "Gym", "Weight Room", "Video Room",
				"Computer Lab", "HealthCare Room", "Transitions", "credit" };

		return roomsArray;
	}

	// ----------------------------------------ACTION_LISTENERS

	public class addClick implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			String textTest = roomInput.getText();

			// checks if the room type was entered correctly
			if (validateStr(textTest)) {
				// checks if the room amount was entered correctly
				if (validateInt(roomAmount.getText())) {
					// checks if the room type is already listed
					if (validateItem(textTest)) {
						// --- roomTypeAmountStr
						roomTypeAndAmountStr = roomTypeAndAmountStr
								+ roomAmount.getText() + "%" + textTest + "<p>";

						outputText = outputText + "[" + roomAmount.getText()
								+ "] " + textTest + "<p>";
						// outputs the list of rooms to user
						roomListOutput = "<html> " + outputText + "</html>";
						roomList.setText(roomListOutput);
						roomInput.setText("");
						roomAmount.setText("");
						counter++;
					}
					// Room type was previously listed
					else {
						// replace the amount of the room that is already listed
						int changeAmount = JOptionPane
								.showConfirmDialog(
										null,
										"<html>This room type has already been listed<p>Would you like to replace the listed amount room type amount with the one you entered?</html>",
										"Input Error",
										JOptionPane.YES_NO_OPTION);
						// if the user wants to change the room amount
						if (changeAmount == JOptionPane.YES_OPTION) {

							roomTypeAndAmountStr = changeAmount(
									roomTypeAndAmountStr, textTest,
									roomAmount.getText());

							outputText = setOutput(roomTypeAndAmountStr);

							roomListOutput = "<html> " + outputText + "</html>";
							roomList.setText(roomListOutput);
							roomInput.setText("");
						}
					}

				}
				// Amount was not input properly
				else {
					JOptionPane.showMessageDialog(new JFrame(),
							"Please enter a room amount as a number",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			// Text was not input properly
			else {
				JOptionPane.showMessageDialog(new JFrame(),
						"Please enter the room type", "Input Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// adds a room type from the combo box
	public class addCB implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// holds the value from the roomAmount text box
			// String testAmount = roomAmount.getText();
			// if the inputed text is a number
			if (validateInt(roomAmount.getText())) {
				// the string textTest is create and assigned the item currently
				// selected from the combo box
				String textTest = (String) cb.getSelectedItem();
				// that item is then checked out to see if it has been
				// previously listed
				if (validateItem(textTest)) {
					roomTypeAndAmountStr = roomTypeAndAmountStr
							+ roomAmount.getText() + "%" + textTest + "<p>";
					outputText = setOutput(roomTypeAndAmountStr);
					roomListOutput = "<html> " + outputText + "</html>";
					roomList.setText(roomListOutput);
					roomInput.setText("");
					roomAmount.setText("");
					counter++;
				} else {

					// assigns the value the returned by the JOptionPane after
					// it gets the user input
					int changeAmount = JOptionPane
							.showConfirmDialog(
									null,
									"<html>This room type has already been listed<p>Would you like to replace the listed amount room type amount with the one you entered?</html>",
									"Input Error", JOptionPane.YES_NO_OPTION);
					// if the user wants to change the room amount
					if (changeAmount == JOptionPane.YES_OPTION) {

						roomTypeAndAmountStr = changeAmount(
								roomTypeAndAmountStr, textTest,
								roomAmount.getText());

						outputText = setOutput(roomTypeAndAmountStr);

						roomListOutput = "<html> " + outputText + "</html>";
						roomList.setText(roomListOutput);
						roomInput.setText("");

					}
				}
				// informs user of an error that occurred with input
			} else {
				JOptionPane.showMessageDialog(new JFrame(),
						"Please enter a room amount as a number",
						"Input Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// replaces the room amount to the amount that the user enters
	public String changeAmount(String roomInfo, String lookFor,
			String replacementInt) {

		// declare string array containing the room info
		String[] ar = new String[roomInfo.split("<p>").length];
		// keeps track of where the room is to be changed
		int counter = 0;
		// string that will be returned
		String ret = "";
		// String that will be compared to lookFor to find the selected room
		// type
		String search;

		// searches for the position on room type selected
		for (int i = 0; i < ar.length; i++) {
			search = roomInfo.split("<p>")[i].split("%")[1];
			if (search.equals(lookFor)) {
				counter = i;
			}
		}

		// assigns the room type info to ar Array
		ar = roomInfo.split("<p>");

		// replaces the room amount number with the one the user enters by
		// swapping the numbers
		for (int i = 0; i < ar.length; i++) {
			if (i == counter) {
				ret += replacementInt + "%" + lookFor + "<p>";
			} else {
				ret += ar[i] + "<p>";
			}
		}

		// returns the ret string containing the new information
		return ret;

	}

	public String setOutput(String roomData) {

		// declares ar array by splitting the roomData string at <p>
		String[] ar = roomData.split("<p>");
		// the string holding the output, will be returned
		String outputStr = "";

		// assigns the output string the each string from ar and adds brackets
		// to them to make it more user friendly
		for (int i = 0; i < ar.length; i++) {
			outputStr += "[" + ar[i].split("%")[0] + "] " + ar[i].split("%")[1]
					+ "<p>";
		}
		// returns the outputStr
		return outputStr;

	}

	public class undoClick implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			// start

			// makes a temporary string array for the roomListOutput and
			// descriptions, used for removing one
			String[] ttt = outputText.split("<p>");

			outputText = "";
			if (!(ttt.length <= 1)) {
				for (int i = 0; i < ttt.length - 1; i++) {
					outputText = outputText + ttt[i] + "<p>";

				}
				counter--;
				// reset the label
				roomListOutput = "<html> " + outputText + "</html>";
				roomList.setText(roomListOutput);
			} else {
				counter = 0;
				roomList.setText("Nothing here yet");
				System.out.println("Cannot preform action");
			}

		}

	}

	// Sets the room list and output strings to blank: clears the whole list
	public class clearAllClick implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			// resests everything back to default
			roomList.setText("Nothing set");
			outputText = "";
			roomListOutput = "";
			counter = 0;
		}
	}

	// -------------------------------------------------METHODS

	public boolean validateStr(String s) {

		char[] tempCharArray = s.toCharArray();
		boolean check = false;
		if (s.equals("")) {
			// if the user does not input anything: return false
			return false;
		} else {
			// for loop cycles through each position of the char array to check
			// if there are any characters
			// returns false if any special character appears
			for (int i = 0; i < tempCharArray.length; i++) {
				if (tempCharArray[i] == ',' || tempCharArray[i] == '%'
						|| tempCharArray[i] == '<' || tempCharArray[i] == '>') {

					// shows the user an error message informing them not to
					// input special characters (especially not the ones listed
					// in the message)
					JOptionPane
							.showMessageDialog(
									new JFrame(),
									"<html>Special characters are not allowed<p>This includes: < ,> ,% , (and no commas)</html>",
									"Special Character Error",
									JOptionPane.ERROR_MESSAGE);

					return false;
				}
				// if checks if there user just held down the space bar
				else if (tempCharArray[i] == ' ' && check == false) {
					check = false;
				}
				// if the character is valid: check becomes true
				else {
					check = true;
				}
			}// end of for loop

			// if check is true: true is returned; string is valid
			if (check) {
				return true;
			}
			// if check is false: false is returned; string is invalid
			else {
				return false;
			}
		}

	}

	// checks if the string can be parsed to an integer successfully
	public boolean validateInt(String s) {

		char ascii_char = ' ';
		int ascii_int;
		if (s.length() != 0) {

			// assigns a letter from String s to char ascii_char
			ascii_char = s.charAt(0);
			// if the integer value of the char is between 48 and 57, the
			// ascii_int
			// values for 0 through 9, true is returned
			ascii_int = (int) (ascii_char);
			if (!(ascii_int >= 49 && ascii_int <= 57)) {
				return false;

			}
			return true;
		}
		// if the length of the string is 0 - nothing was input, false is
		// returned
		else {
			return false;
		}
	}

	public void toDB() {
		secondaryDataBase.roomList = roomTypeAndAmountStr;
		secondaryDataBase.roomCount = counter;
	}

	public void refreshList(String[] rooms_array) {

		String[][] rmData = new String[rooms_array.length][2];

		for (int i = 0; i < rmData.length; i++) {
			rmData[i] = rooms_array[i].split(", ");

			// -- roomTypeAmountStr
			// String holds both the room type and the room type amount
			// separated by a %
			roomTypeAndAmountStr = roomTypeAndAmountStr + rmData[i][1] + "%"
					+ rmData[i][0] + "<p>";

			// assigns output text a user friendly layout for the string: square
			// brackets around the room type amounts with the room types right
			// next to them
			outputText = outputText + "[" + rmData[i][1] + "] " + rmData[i][0]
					+ "<p>";
			counter++;

		}
		// outputs the list of rooms to user
		roomListOutput = "<html> " + outputText + "</html>";
		roomList.setText(roomListOutput);
		roomInput.setText("");
		roomAmount.setText("");

	}

	// method checks if department was previously listed
	public boolean validateItem(String s) {
		// create the temporary array holding each department
		String[] tempAr = new String[roomTypeAndAmountStr.split("<p>").length];

		// assigns the room type values to tempAr
		// it is broken down from the roomTypeAmountStr and then once more to
		// separate the room type (name) from the amount
		for (int i = 0; i < tempAr.length; i++) {
			if (!roomTypeAndAmountStr.equals(""))
				tempAr[i] = roomTypeAndAmountStr.split("<p>")[i].split("%")[1];
		}

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
