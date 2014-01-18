/* Class: IIroomsAndDepts
 * Author: Luka Pavlovic Ed. Troylan Tempra Jr.
 * Last Modified:20/1/2013
 * Description: Combined 3rd window for new project option
 * Uses IIdept and IIrooms as top and bottom half panels
 */
import javax.swing.*;

import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class IIroomsAndDepts extends JFrame {

	// declare the IIrooms and IIdepts objects
	IIrooms cir = new IIrooms();
	IIdept cid = new IIdept();

	// declares the JButtons
	private JButton next = new JButton("Next");
	private JButton back = new JButton("Back");
	private JButton cancel = new JButton("Cancel");
	// declares insest for positioning the components
	private Insets standardInset = new Insets(0, 6, 6, 6);
	// declare loadMe for loading data
	public static boolean loadMe = false;
	private String mode = "";

	// ----------------------------------------------------- :: Constructor
	public IIroomsAndDepts(String mode) {
		// set the title
		super("New Template");
		this.mode = mode;
		// set up the layout manager
		JPanel mainPanel = new JPanel(new GridBagLayout());
		// give it a black border
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		// make a new GridBagConstraints
		GridBagConstraints gbc = new GridBagConstraints();
		// set this as content pane
		setContentPane(mainPanel);
		// set exit
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// set the weights
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		// set the fill
		gbc.fill = GridBagConstraints.BOTH;

		// ---------------Adding

		// add label: Name of the new template
		gbc.gridx = 0;
		gbc.gridy = 0;
		mainPanel.add(cir.getPanel(), gbc);

		// add label: Name of the new template
		gbc.gridx = 0;
		gbc.gridy = 1;
		mainPanel.add(cid.getPanel(), gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		mainPanel.add(addButtons(), gbc);

		// --------load
		// loads the rooms and departments from the main database
		if (loadMe) {
			loadRoomsAndDepts();
		}
		// pack it together
		pack();

		// ----- Add Action Listener
		next.addActionListener(new nextClick());
		back.addActionListener(new backClick());
		cancel.addActionListener(new cancelClick());
	}

	// ----------------------------------------------------- :: Action Listener
	public class nextClick implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			// send key data from the previous panels to the database
			cir.toDB();
			cid.toDB();

			// create boolean used to check if user inputed correct data
			boolean pass = true;

			// create string arrays from the Room and Department Data in the
			// database
			String[] tempDeptData = secondaryDataBase.deptList.split("<p>");
			String[] tempRoomData = secondaryDataBase.roomList.split("<p>");
			int[] roomAmount = new int[tempRoomData.length];

			// if the tempRoomData has some values (is not blank) it is split
			// and assigned to 2 separate arrays which contain the room type
			// names and the room type amounts.
			int arrayLength = tempRoomData.length;
			if (validateText(tempRoomData[0])) {
				for (int i = 0; i < arrayLength; i++) {
					String[] ta = tempRoomData[i].split("%");
					roomAmount[i] = Integer.parseInt(ta[0]);
					tempRoomData[i] = ta[1];
				}
			} else {
				pass = false;
			}

			// the room arrays in the temporary database are initialized
			secondaryDataBase.roomArray = new String[tempRoomData.length];
			secondaryDataBase.roomTypeAmount = new int[roomAmount.length];

			// the room arrays are assigned values from the tempRoomData and
			// roomAmount arrays
			for (int i = 0; i < roomAmount.length; i++) {
				secondaryDataBase.roomArray[i] = tempRoomData[i];
				secondaryDataBase.roomTypeAmount[i] = roomAmount[i];

			}
			// sets the loadMe boolean to true so that the courses will be
			// loaded when the user comes back to this frame
			loadMe = true;
			// initializes the deptArray in the temporary database
			secondaryDataBase.deptArray = new String[tempDeptData.length];
			if (validateText(tempDeptData[0])) {
				for (int i = 0; i < tempDeptData.length; i++) {
					secondaryDataBase.deptArray[i] = tempDeptData[i];
				}
			} else {
				pass = false;
			}

			// if everything was input correctly, the user is allowed to go to
			// the next frame (course input)
			if (pass) {
				// hide the frame
				setVisible(false);
				// clear resources for the frame
				dispose();

				if (mode.equals("All")) {
					IIcourses tbTest = new IIcourses("All");
					tbTest.setVisible(true);
				} else if (mode.equals("DeptsRoomsCourses")) {
					IIcourses tbTest = new IIcourses("DeptsRoomsCourses");
					tbTest.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(new JFrame("Error"),
							"Invalid IIroomsAndDepts mode: " + mode
									+ " Check construction");
				}
			}
			// otherwise, the user shall not pass
			else {
				// System.out.println("You Shall Not Pass");
				JOptionPane.showMessageDialog(new JFrame("DENIED"),
				// "You Shall Not Pass"
						"Invalid Information");
			}

		}
	}

	// takes the user back to the previous frame: Lunch Preferences
	class backClick implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// hide the frame
			setVisible(false);
			// clear resources for the frame
			dispose();
			// takes user back to the lunch preferences frame
			IIlunchPrefs lp = new IIlunchPrefs("All");
			lp.setVisible(true);

		}
	}

	// cancels the input interface
	class cancelClick implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// hide the frame
			setVisible(false);
			// clear resources for the frame
			dispose();
		}
	}

	// ----------------------------------------------------- :: Methods

	// checks if the text is not blank or null
	public boolean validateText(String s) {
		if (s.equals("") || s.equals(null)) {
			return false;
		} else {
			return true;
		}
	}

	// checks if the string passed can be sucessfully parsed to an int
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

	// loads the previous session in the rooms and departments panels
	public void loadRoomsAndDepts() {
		// get the names of the departments from the database
		String[] newDepts = new String[DataBase.departments.length];
		// assign newDepts each of the department names
		for (int i = 0; i < newDepts.length; i++) {
			newDepts[i] = DataBase.departments[i].getName();
		}

		String[] newRooms = new String[DataBase.rooms.length];

		// assign newDepts each of the department names
		for (int i = 0; i < newRooms.length; i++) {
			newRooms[i] = DataBase.rooms[i].getType()
					+ ", "
					+ Integer.toString(DataBase.rooms[i]
							.getOriginalNumOfRooms());
		}

		// as long as the arrays are not empty, the departments and room types
		// lists will update
		if (!newDepts[0].equals("")) {
			cid.refreshList(newDepts);
		}
		if (!newRooms[0].equals(", 0")) {
			cir.refreshList(newRooms);
		}

	}

	// JPanel used for adding the bottom row of buttons: Back, Cancel, and Next
	public JPanel addButtons() {
		// make the option Panel
		JPanel btnPanel = new JPanel(new GridBagLayout());
		// create the GridBagConstraints
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = standardInset;
		// sets coordinates
		gbc.gridx = 0;
		gbc.gridy = 0;
		// sets orientation
		gbc.anchor = GridBagConstraints.WEST;
		btnPanel.add(back, gbc);

		// set coordinates
		gbc.gridx = 1;
		gbc.gridy = 0;
		btnPanel.add(cancel, gbc);

		// set coordinates
		gbc.gridx = 2;
		gbc.gridy = 0;

		// set the weights of the components
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		// set orientation
		gbc.anchor = GridBagConstraints.EAST;
		btnPanel.add(next, gbc);

		// return the panel
		return btnPanel;
	}

	// passes a boolean which will adjust the loadMe boolean used for loading
	// files and starting with clean template
	public static void checkLoad(boolean loadFromDB) {
		loadMe = loadFromDB;
	}

}
