import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

////////////////////////////////////////////////////////////////////////////////
/*
 * Class: DataBase Class
 * Description: Holds static project wide data and most methods that affect them
 * 
 * Authors: 
 * 	Troylan Tempra Jr
 * 
 * Date Created:December 6, 2012
 * Date Modified: January 20, 2012
 */
////////////////////////////////////////////////////////////////////////////////

public class DataBase {
	// //////////////////////////////////Project-Wide Data and Methods
	public static int[][] coursenum;
	public static String selected_TemplateName = "";
	public static String last_TemplateName = "";
	public static String projectName = "ProjectName";//Name of the project
	public static int schoolPeriodCount;//Number of periods of the school
	public static int schoolGradeCount;//Number of Grades of the school
	public static int schoolYearDivisions = 2;// The number of divisions the school year is divided into (aka 2 semesters)
	public static int colCountPerLevel = 50;//Number of columns per level
	public static boolean autoRepatchVisibilityIsPreferred = true;
	//^Currently Obsolete, would have been used to control auto column visibility adjustment
	public static final int slotsPerColumn = 1;//Number of slots per column per grade per period
	public static Course[][][][] timeT;//Holds the placement of all the courses
	public static Course[][][][] timeTcopy;
	public static String[][][] sem_SWTable_data;//Used to display timeT in the table
	public static String[][][] sem_SWPerBal_data;//Used to display the period totals in a table
	public static int rowCountSum;//Number of rows of the tables
	public static int colCountSum;//Number of columns of the tables
	public static int[][] levelColCount;//Number of columns per level (MasterTimeT)
	public static int[] totalColCount;//Number of columns per level (HTML)
	public static boolean[][] lunchPrefs = {};//LunchPref Array (True = Preferred)
	public static Room[] rooms = {};//Room object array
	public static Grade[] grades = {};//Grade object Array
	public static Department[] departments = {};//Department Object Array
	public static String[] levels = {"Acad/M/U", "Appl/C", "Open", "Loc Dev", "ESP"};//Levels Array
	public static Course[] courses = {};//Course object Array
	public static String selected_Department;//Currently selected department or department on display
	public static int selectedRow;//Currently selected row
	public static int selectedCol;//Currently selected Column
	public static File projectFile;
	public static boolean isNew = true;
	public static boolean isSaveAsFile = false;
	public static boolean isItFirstLoad = true;
	public static boolean isLauncher = true;
	public static boolean isChange = false;
	public static boolean isDataBaseEmpty = true;
	public static boolean isAutomationComplete = false;
	public static String selected_DeptFilter = "";// Contains the name of the
													// department currently
													// selected in
													// deptFilterList
	public static String selected_LvlFilter = "";// Contains the name of the
													// level currently selected
													// in lvlFilterList
	public static char c = (char) (92);
	public static String dash = String.valueOf(c);

	// File Reading Methods//TODO: Move to course Loader

	public static Department getDepartment(String name) {// Receives a STRING
															// department name
															// and returns a
															// DEPARTMENT
															// matched with that
															// name
		for (int i = 0; i < DataBase.departments.length; i++) {
			if (departments[i].getName().equalsIgnoreCase(name)) {
				return departments[i];
			}
		}
		return null;
	}

	public static boolean printSingleDepartmentHTML(File file, String name)
	{
		// regenerate the master timetable window
		Course_Manager_Main.window1.generateMasterTimeTable();
		setTimeTCopy();
		try {
			int width = 0;//holds the column width
			for (int x = 0; x < DataBase.levels.length; x++) {
				width = width + DataBase.totalColCount[x];
			}

			// create File object called sampleFile and pass it the name of the
			// file to save to
			// pass the sampleFile to the FileWriter, and pass that to the
			// BufferedWriter assign that value to output, the Writer file
			FileWriter filewriter = new FileWriter(file + ".html");
			BufferedWriter output = new BufferedWriter(filewriter);
			// use the write() method to write the text to the output file
			output.write("<html>");
			output.write("<head><title>All Departments</title></head>");
			output.write("<body>");
			for (int a = 0; a < DataBase.departments.length; a++) { // department
				if (departments[a].getName().equalsIgnoreCase(name)) {
					output.write("<font size=\"5\"><b>"
							+ departments[a].getName() + "</b></font>");
					output.write("<table border=\"1\" cellpadding=\"4\">");
					output.write("<th>Semester</th><th>Period</th><th>Grade</th>");
					for (int lvl = 0; lvl < DataBase.levels.length; lvl++) {
						output.write("<th colspan=\"" + totalColCount[lvl]
								+ "\">" + DataBase.levels[lvl] + "</th>");
					}
					for (int d = 0; d < DataBase.timeTcopy[0][0][0].length; d++) { // semester
						for (int b = 0; b < DataBase.timeTcopy[0].length; b++) { // period/grade
							output.write("<tr>");
							if (b == 0) {
								output.write("<td rowspan=\""
										+ (DataBase.schoolGradeCount * DataBase.schoolPeriodCount)
										+ "\"><center>" + (d + 1)
										+ "</center></td>");
							}
							if (b == 0 || b == 4 || b == 8 || b == 12
									|| b == 16) {
								output.write("<td rowspan=\"4\"><center>"
										+ (b / 4 + 1) + "</center></td>");
							}
							int tempperiod = 0; // reset tempperiod
							// sets the period based on row #
							if (b >= 0 && b <= 3) {
								tempperiod = 1;
							} else if (b >= 4 && b <= 7) {
								tempperiod = 2;
							} else if (b >= 8 && b <= 11) {
								tempperiod = 3;
							} else if (b >= 12 && b <= 15) {
								tempperiod = 4;
							} else if (b >= 16 && b <= 19) {
								tempperiod = 5;
							}
							// algorithm for determining grade based on
							// period/row
							int tempgrade = (b - (tempperiod * 4)) + 13;
							output.write("<td");
							// shade alternating rows light grey
							if (tempgrade == 9 || tempgrade == 11) {
								output.write(" bgcolor=\"#F0F0F0\"");
							}
							output.write("><center>" + tempgrade
									+ "</center></td>");
							for (int c = 0; c < width; c++) { // X-axis
								output.write("<td");
								// shade alternating rows light grey
								if (tempgrade == 9 || tempgrade == 11) {
									output.write(" bgcolor=\"#F0F0F0\"");
								}
								// enters the course name
								if (DataBase.timeTcopy[a][b][c][d] == null) {
									output.write(">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</td>");
								} else {
									output.write("><center>"
											+ DataBase.timeTcopy[a][b][c][d].name
											+ "</center></td>");
								}
							}
							output.write("</tr>");
							output.newLine();
						}
					}
					// end table
					output.write("");
					output.write("</table>");
				}
			}
			// end HTML file
			output.write("</body>");
			output.write("</html>");
			// close the output file
			output.close();
		} catch (IOException error) {
			error.printStackTrace();
			return false;
		}
		// inform user that the operation is complete
		System.out.println("Your file has been written");
		return true;
	}

	public static boolean printMasterHTML(File file) {
		// regenerate the master timetable window
		Course_Manager_Main.window1.generateMasterTimeTable();
		DataBase.colCountSum = DataBase.colCountSum - 1;
		try {
			// create File object called sampleFile and pass it the name of the
			// file to save to
			// pass the sampleFile to the FileWriter, and pass that to the
			// BufferedWriter assign that value to output, the Writer file
			FileWriter filewriter = new FileWriter(file + ".html");
			BufferedWriter output = new BufferedWriter(filewriter);
			// use the write() method to write the text to the output file
			output.write("<html>");
			output.write("<head><title>Master Timetable</title></head>");// title
			output.write("<body>");
			for (int d = 0; d < DataBase.sem_SWTable_data.length; d++) { // semester
				output.write("<font size=\"5\"><b>Master Timetable Semester "
						+ (d + 1) + "</b></font>"); // header
				output.write("<table border=\"1\" cellpadding=\"4\">");// table
																		// properties
				output.write("<th>Semester</th><th>Period</th><th>Grade</th>");
				for (int lvl = 0; lvl < DataBase.levels.length; lvl++) {// prints
																		// the
																		// table
																		// headings
					output.write("<th colspan=\""
							+ DataBase.levelColCount[lvl][d] + "\">"
							+ DataBase.levels[lvl] + "</th>");
				}
				for (int b = 0; b < DataBase.sem_SWTable_data[0].length; b++) { // period/grade
					output.write("<tr>");
					if (b == 0) {
						output.write("<td rowspan=\"" + (DataBase.rowCountSum)
								+ "\"><center>" + (d + 1) + "</center></td>");
					}
					if (b == 0 || b == 16 || b == 32 || b == 48 || b == 64) { // print
																				// period
						if (DataBase.schoolPeriodCount == 5) {
							output.write("<td rowspan=\""
									+ (DataBase.rowCountSum / 5)
									+ "\"><center>" + (b / 16 + 1)
									+ "</center></td>");
						}else{
							output.write("<td rowspan=\""
									+ (DataBase.rowCountSum / 5)
									+ "\"><center>" + ((b / 16 + 1)*2)
									+ "</center></td>");
						}
					}
					int tempperiod = 0; // reset period
					// sets the period based on row #
					if (b >= 0 && b <= 15) {
						tempperiod = 1;
					} else if (b >= 16 && b <= 31) {
						tempperiod = 2;
					} else if (b >= 32 && b <= 47) {
						tempperiod = 3;
					} else if (b >= 48 && b <= 63) {
						tempperiod = 4;
					} else if (b >= 64 && b <= 79) {
						tempperiod = 5;
					}
					int tempgrade = ((b - ((tempperiod - 1) * 16)) / 4) + 9; // algorithm
																				// to
																				// determine
																				// grade
					output.write("<td");
					if (tempgrade == 9 || tempgrade == 11) {
						output.write(" bgcolor=\"#F0F0F0\""); // shade
																// alternating
																// rows
																// light grey
					}
					output.write("><center>" + tempgrade + "</center></td>");
					int xaxis = 0;
					for (int var = 0; var < DataBase.levels.length; var++) {
						xaxis = xaxis + DataBase.levelColCount[var][d];
					}
					for (int c = 2; c < (xaxis + 2); c++) { // X-axis
						output.write("<td");
						// shade alternating rows light gray
						if (tempgrade == 9 || tempgrade == 11) {
							output.write(" bgcolor=\"#F0F0F0\"");
						}
						// enters the course name
						if (DataBase.sem_SWTable_data[d][b][c] == null) { // if
																			// cell
																			// is
																			// empty
							output.write(">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</td>");
						} else { // if there is a course code in the cell
							output.write("><center>"
									+ DataBase.sem_SWTable_data[d][b][c]
									+ "</center></td>");
						}
					}
					output.write("</tr>");
					output.newLine();
				}
				// end table
				output.write("");
				output.write("</table>");
			}
			// end HTML file
			output.write("</body>");
			output.write("</html>");
			// close the output file
			output.close();
		} catch (IOException error) {
			error.printStackTrace();
			return false;
		}
		// inform user that the operation is complete
		System.out.println("Your file has been written");
		return true;
	}

	public static boolean printAllDepartmentHTML(File file) {
		// regenerate the master timetable window
		Course_Manager_Main.window1.generateMasterTimeTable();
		setTimeTCopy();
		try {
			int width = 0;
			for (int x = 0; x < DataBase.levels.length; x++) {
				width = width + DataBase.totalColCount[x];
			}
			// create File object called sampleFile and pass it the name of the
			// file to save to
			// pass the sampleFile to the FileWriter, and pass that to the
			// BufferedWriter assign that value to output, the Writer file
			FileWriter filewriter = new FileWriter(file + ".html");
			BufferedWriter output = new BufferedWriter(filewriter);
			// use the write() method to write the text to the output file
			output.write("<html>");
			output.write("<head><title>All Departments</title></head>");
			output.write("<body>");
			for (int a = 0; a < DataBase.departments.length; a++) { // department
				output.write("<font size=\"5\"><b>" + departments[a].getName()
						+ "</b></font>");
				output.write("<table border=\"1\" cellpadding=\"4\">");
				output.write("<th>Semester</th><th>Period</th><th>Grade</th>");
				for (int lvl = 0; lvl < DataBase.levels.length; lvl++) {
					output.write("<th colspan=\"" + totalColCount[lvl] + "\">"
							+ DataBase.levels[lvl] + "</th>");
				}
				for (int d = 0; d < DataBase.timeTcopy[0][0][0].length; d++) { // semester
					for (int b = 0; b < DataBase.timeTcopy[0].length; b++) { // period/grade
						output.write("<tr>");
						if (b == 0) {
							output.write("<td rowspan=\""
									+ (DataBase.schoolGradeCount * DataBase.schoolPeriodCount)
									+ "\"><center>" + (d + 1)
									+ "</center></td>");
						}
						if (b == 0 || b == 4 || b == 8 || b == 12 || b == 16) {
							output.write("<td rowspan=\"4\"><center>"
									+ (b / 4 + 1) + "</center></td>");
						}
						int tempperiod = 0;
						if (b >= 0 && b <= 3) {
							tempperiod = 1;
						} else if (b >= 4 && b <= 7) {
							tempperiod = 2;
						} else if (b >= 8 && b <= 11) {
							tempperiod = 3;
						} else if (b >= 12 && b <= 15) {
							tempperiod = 4;
						} else if (b >= 16 && b <= 19) {
							tempperiod = 5;
						}
						int tempgrade = (b - (tempperiod * 4)) + 13;
						output.write("<td");
						if (tempgrade == 9 || tempgrade == 11) {
							output.write(" bgcolor=\"#F0F0F0\"");
						}
						output.write("><center>" + tempgrade + "</center></td>");
						for (int c = 0; c < width; c++) { // X-axis -
							// timeT[0][0].length
							output.write("<td");
							if (tempgrade == 9 || tempgrade == 11) {
								output.write(" bgcolor=\"#F0F0F0\"");
							}
							// enters the course name
							if (DataBase.timeTcopy[a][b][c][d] == null) {
								output.write(">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</td>");
							} else {
								output.write("><center>"
										+ DataBase.timeTcopy[a][b][c][d].name
										+ "</center></td>");
							}
						}
						output.write("</tr>");
						output.newLine();
					}
				}
				output.write("");
				output.write("</table>");
			}
			output.write("</body>");
			output.write("</html>");
			// close the output file
			output.close();
		} catch (IOException error) {
			error.printStackTrace();
			return false;
		}
		// inform user that the operation is complete
		System.out.println("Your file has been written");
		return true;
	}

	// Add/Remove Methods
	public static void addCourse(Course newCourse) {// Method to add a course to
		// the array in database named courses
		// TempVal:
		Course[] courses_copy = new Course[courses.length];
		// Creates an array of courses 1 index larger than the array currently
		// in database.
		for (int index = 0; index < courses.length; index++) {// copies all
			// values in
			// courses[] to
			// courses_copy[]
			courses_copy[index] = courses[index];
		}
		courses = new Course[courses.length + 1];// adds one element index
		for (int index = 0; index < courses_copy.length; index++) {// transfers
			// all old
			// elements
			courses[index] = courses_copy[index];
		}
		courses[courses.length - 1] = newCourse;// adds the new element to the
		// end
	}

	public static void addDept(Department newDept) {// Method to add a
													// department to
		// the departments[]
		// TempVal:
		Department[] departments_copy = new Department[departments.length];
		for (int index = 0; index < departments.length; index++) {// copies all
			// values in courses[] to courses_copy[]
			departments_copy[index] = departments[index];
		}
		departments = new Department[departments.length + 1];// adds one element
		// index
		for (int index = 0; index < departments_copy.length; index++) {// transfers
			// all old elements
			departments[index] = departments_copy[index];
		}
		departments[courses.length - 1] = newDept;// adds the new element to the
		// end
	}

	public static void addLevel(String newLevel) {// Method to add a new Level
		// to the levels[]
		// TempVal:
		String[] levels_copy = new String[levels.length];
		for (int index = 0; index < levels.length; index++) {// copies all
			// values in
			// levels[] to
			// levels_copy[]
			levels_copy[index] = levels[index];
		}
		levels = new String[levels.length + 1];// adds one element index
		for (int index = 0; index < levels_copy.length; index++) {// transfers
			// all old elements
			levels[index] = levels_copy[index];
		}
		levels[courses.length - 1] = newLevel;// adds the new element to the end
	}

	public static void addRoom(Room newRoom) {// Method to add a new Level to
		// the levels[]
		// TempVal:
		Room[] rooms_copy = new Room[rooms.length];
		for (int index = 0; index < rooms.length; index++) {// copies all values
			// in courses[] to
			// courses_copy[]
			rooms_copy[index] = rooms[index];
		}
		rooms = new Room[rooms.length + 1];// adds one element index
		for (int index = 0; index < rooms_copy.length; index++) {// transfers
			// all old
			// elements
			rooms[index] = rooms_copy[index];
		}
		rooms[courses.length - 1] = newRoom;// adds the new element to the end
	}

	public static Course findCourse(String courseName) {// Finds a course and
		// returns it
		for (int i = 0; i < DataBase.courses.length; i++) {
			if (DataBase.courses[i].name.equalsIgnoreCase(courseName)) {
				return courses[i];
			}
		}
		// if Course is not found
		System.out.println("***Failed to find Course: " + courseName);
		return null;
	}

	public static Room findRoom(String roomName) {
		for (int index = 0; index < rooms.length; index++) {
			if (rooms[index].getType().equals(roomName)) {
				// System.out.println("Matched: " + roomName + " with: "
				// + rooms[index].getType() + " containing: "
				// + rooms[index].numOfRooms()[0][0] + ", "
				// + rooms[index].numOfRooms()[0][1] + ", "
				// + rooms[index].numOfRooms()[0][2] + ", "
				// + rooms[index].numOfRooms()[0][3] + ", "
				// + rooms[index].numOfRooms()[0][4] + ", "
				// + rooms[index].numOfRooms()[1][0] + ", "
				// + rooms[index].numOfRooms()[1][1] + ", "
				// + rooms[index].numOfRooms()[1][2] + ", "
				// + rooms[index].numOfRooms()[1][3] + ", "
				// + rooms[index].numOfRooms()[1][4]);
				return rooms[index];
			}
		}
		// If room is not found
		System.out.println("***Failed to find roomType: " + roomName);
		return null;
	}

	// //////////////////////////////////////////////////////////////////////CourseManager
	// // TODO TAG: Window 1-centric Data and Methods
	// Data Displayed in top labels
	public static int sem1_Total = 0, sem2_Total = 0;

	// Data displayed in the main table
	// Left hand side table
	public static String[] sem1_main_table_header = // Column headers
	{};
	public static String[][] sem1_main_table_data = // Table Values
	{ {} };
	// Right hand side table
	public static String[] sem2_main_table_header = // Column headers
	{};
	public static String[][] sem2_main_table_data = // Table Values
	{ {} };

	// Data displayed in labels below main table
	public static int sem1_DeptTotal = 0, sem2_DeptTotal = 0;

	// Data displayed in tables at the bottom
	// Left hand side table
	public static String[] sem1_period_totals_header;// //Column Headers
	public static String[][] sem1_period_table_data; // Table Values

	// Right hand side table
	public static String[] sem2_period_totals_header; // //Column Headers
	public static String[][] sem2_period_table_data; // Table Values

	// Window 1 Related
	// Methods//////////////////////////////////////////////////////////////
	public static int findDeptPos(String deptName) {// finds a department using
													// a name and returns its
													// position in the
													// departments[]
		int deptPos = -1;
		for (int index = 0; index < departments.length; index++) {
			// System.out.println(deptName +
			// " comparing with: "+departments[index].getName());
			if (deptName != null
					&& deptName.equals(departments[index].getName())) {
				deptPos = index;
				// System.out.println("Returning deptPos: " + deptPos);
				return deptPos;// end searching
			} else {
				// Check Next Position
			}
		}
		System.out.println("Failed to find dept with deptName: " + deptName);
		return -1;// returns -1 if not found
	}

	public static int findLevelPos(String levelName) {// finds a level using a
														// name and returns its
														// position in the
														// departments[]
		int levelPos = -1;
		for (int index = 0; index < levels.length; index++) {
			if (levelName.equals(levels[index])) {
				levelPos = index;
				return levelPos;// end searching
			}
		}
		return -1;
	}

	// updateSchoolTotal is found at the end of Window 1 Related Methods

	public static void resetMain_table_headers() {// Sets sem1_main_table_header
													// and
													// sem2_main_table_header
													// according to levels[]
		sem1_main_table_header = new String[levels.length * colCountPerLevel
				+ 2];
		sem2_main_table_header = new String[levels.length * colCountPerLevel
				+ 2];
		// length is set to 3 times the number of levels + 2 for period and
		// grade column
		sem1_main_table_header[0] = "P";
		sem1_main_table_header[1] = "Gr";
		sem2_main_table_header[0] = "P";
		sem2_main_table_header[1] = "Gr";
		for (int indexH = 2, indexL = 0; indexL < levels.length; indexH += colCountPerLevel, indexL++) {
			for (int colNum = 0; colNum < colCountPerLevel; colNum++) {
				sem1_main_table_header[indexH + colNum] = levels[indexL];
				sem2_main_table_header[indexH + colNum] = levels[indexL];
			}
		}
	}

	public static void resetMain_table_data() {
		// set rowCount to 5*4 for 1 slots per 5 periods per 4 grades
		sem1_main_table_data = new String[schoolPeriodCount * schoolGradeCount][levels.length
				* colCountPerLevel + 2];
		sem2_main_table_data = new String[schoolPeriodCount * schoolGradeCount][levels.length
				* colCountPerLevel + 2];
		// Set Constants and clear Variables
		// integer y for depth, where 0 is top of the table
		// integer x for across, where 0 is the left of the table
		for (int y = 0; y < sem1_main_table_data.length; y++) {
			for (int x = 0; x < sem1_main_table_data[y].length; x++) {
				if (x == 0 & y % 4 == 0) {// sets row 0 and every 8 steps
											// consecutive of
											// column 0 using formula depth/8 +
											// 1, truncated, to string
											// sets period numbers 1-5
					sem1_main_table_data[y][x] = Integer
							.toString((int) (y / 4 + 1));
					sem2_main_table_data[y][x] = Integer
							.toString((int) (y / 4 + 1));
				} else if (x == 1 & y % 1 == 0) {// sets row 0 and every 2 steps
													// consecutive of
													// column 1 using formula
													// depth%8/2 + 9, truncated,
													// to string
													// sets grade levels
					sem1_main_table_data[y][x] = Integer
							.toString((int) (y % 4 + 9));
					sem2_main_table_data[y][x] = Integer
							.toString((int) (y % 4 + 9));
				} else {
					sem1_main_table_data[y][x] = null;
					sem2_main_table_data[y][x] = null;
				}
			}
		}
	}

	public static void display(String courseName, int sem, int rowNum) {
		Course thisCourse = findCourse(courseName);
		int levelNum = findLevelPos(thisCourse.level);
		if (sem == 0) {
			for (int colNum = 0; colNum < colCountPerLevel; colNum++) {// iterates
																		// through
																		// each
																		// column
				if (sem1_main_table_data[rowNum][2 + levelNum
						* colCountPerLevel + colNum] == null) {
					sem1_main_table_data[rowNum][2 + levelNum
							* colCountPerLevel + colNum] = thisCourse.name;
					colNum = colCountPerLevel;// end and exit loop
				}
			}
		} else if (sem == 1) {
			for (int colNum = 0; colNum < colCountPerLevel; colNum++) {
				if (sem2_main_table_data[rowNum][2 + levelNum
						* colCountPerLevel + colNum] == null) {
					sem2_main_table_data[rowNum][2 + levelNum
							* colCountPerLevel + colNum] = thisCourse.name;
					colNum = colCountPerLevel;
				}
			}
		}
	}

	public static void refreshTables(int deptPos) {// Sets the main_table_data
													// to a specific dept
		resetMain_table_headers();
		resetMain_table_data();
		System.out.println("Refreshing with deptPos: " + deptPos);
		for (int sem = 0; sem < DataBase.schoolYearDivisions; sem++) {// iterates
																		// through
																		// both
																		// semesters
			for (int rowNum = 0; rowNum < timeT[0].length; rowNum++) {// iterates
																		// through
																		// each
																		// row
				for (int xCoordinate = 0; xCoordinate < timeT[0][0].length; xCoordinate++) {// iterates
																							// through
																							// each
																							// column
					if (timeT[deptPos][rowNum][xCoordinate][sem] != null)
						display(timeT[deptPos][rowNum][xCoordinate][sem].name,
								sem, rowNum);
				}
			}
		}
	}

	public static void autoRepatchVisibility() {// Autoadjusts visibility of
												// tables to timeT contents,
												// done after automation
		int[] highestSectionCount = new int[levels.length];// records the
															// highest number of
															// sections of a
															// each level
		int[] rowSectionCount = new int[levels.length];// records the number of
														// sections in each row,
														// reset after each row
														// is checked
		boolean noVisibility;// records if all visibilities are set to 0 (i.e.
								// no sections have been placed/empty table)
		for (int deptsIndex = 0; deptsIndex < departments.length; deptsIndex++) {// iterates
																					// through
																					// each
																					// visOp
			highestSectionCount = new int[levels.length];
			for (int sem = 0; sem < 2; sem++) {// iterates through both
												// semesters
				for (int yCoordinate = 0; yCoordinate < timeT[deptsIndex].length; yCoordinate++) {// iterates
																									// through
																									// each
																									// row
					rowSectionCount = new int[levels.length];// rests on
																// iteration
					for (int xCoordinate = 0; xCoordinate < timeT[deptsIndex][yCoordinate].length; xCoordinate++) {// iterates
																													// through
																													// each
																													// column
						if (timeT[deptsIndex][yCoordinate][xCoordinate][sem] != null) {
							for (int levelIndex = 0; levelIndex < levels.length; levelIndex++) {
								if ((timeT[deptsIndex][yCoordinate][xCoordinate][sem])
										.getLevel().equals(levels[levelIndex])) {
									// System.out.println("At row: " +
									// yCoordinate + ", " +
									// timeT[deptsIndex][yCoordinate][xCoordinate][sem].getName()
									// + " adding to: " + levels[levelIndex]);
									rowSectionCount[levelIndex]++;
								}
							}
						}
					}
					for (int levelIndex = 0; levelIndex < levels.length; levelIndex++) {// Checks
																						// if
																						// a
																						// new
																						// max
																						// is
																						// found
						if (rowSectionCount[levelIndex] > highestSectionCount[levelIndex]) {
							highestSectionCount[levelIndex] = rowSectionCount[levelIndex];
						}
					}
				}
			}
			for (int levelIndex = 0; levelIndex < levels.length; levelIndex++) {
				// System.out.println(departments[deptsIndex].getName()+
				// levelIndex + ": " + highestSectionCount[levelIndex]);
				if (highestSectionCount[levelIndex] > 0)
					departments[deptsIndex].colVis[levelIndex] = highestSectionCount[levelIndex];
				else
					departments[deptsIndex].colVis[levelIndex] = 0;
			}
			noVisibility = true;
			for (int levelsIndex = 0; levelsIndex < levels.length; levelsIndex++) {
				if (departments[deptsIndex].colVis[levelsIndex] > 0) {
					noVisibility = false;
				}
			}
			if (noVisibility) {
				for (int levelsIndex = 0; levelsIndex < levels.length; levelsIndex++) {
					departments[deptsIndex].colVis[levelsIndex] = 1;
				}
			}
		}
	}

	public static void resetPeriod_totals_headers() {// Sets
														// sem1_period_totals_header
														// and
														// sem2_period_totals_header
														// according to # of
														// periods
		sem1_period_totals_header = new String[schoolPeriodCount + 1];
		sem2_period_totals_header = new String[schoolPeriodCount + 1];
		// Sets first column headers
		sem1_period_totals_header[0] = "Sem1";
		sem2_period_totals_header[0] = "Sem2";
		for (int colNum = 1; colNum < sem1_period_totals_header.length; colNum++) {// set
																					// headers
																					// to
																					// P
																					// +
																					// (the
																					// number
																					// of
																					// the
																					// period)
			sem1_period_totals_header[colNum] = "P" + Integer.toString(colNum);
			sem2_period_totals_header[colNum] = "P" + Integer.toString(colNum);
		}
	}

	public static void resetPeriod_totals_data() {// Sets sem1_main_table_data
													// and sem2_main_table_data
													// according to # of grades
													// & periods
		sem1_period_table_data = new String[schoolGradeCount + 1][schoolPeriodCount + 1];
		sem2_period_table_data = new String[schoolGradeCount + 1][schoolPeriodCount + 1];
		for (int rowNum = 0; rowNum < schoolGradeCount + 1; rowNum++) {
			for (int colNum = 0; colNum < schoolPeriodCount; colNum++) {
				if (colNum > 0) {// fill the totals with 0
					sem1_period_table_data[rowNum][colNum] = "0";
					sem2_period_table_data[rowNum][colNum] = "0";
				} else if (rowNum < schoolGradeCount) {// fill the first column
														// with the grade number
					sem1_period_table_data[rowNum][colNum] = rowNum + 9 + "";
					sem2_period_table_data[rowNum][colNum] = rowNum + 9 + "";
				} else {// fill the last row of the first column with the word
						// "Total"
					sem1_period_table_data[rowNum][colNum] = "Total";
					sem2_period_table_data[rowNum][colNum] = "Total";
				}
			}
		}
	}

	public static int countPeriodicTotal(int sem, int grade, int period) {
		int total = 0;
		if (sem == 1) {// conduct sem1 sections in table count
			for (int index = 2; index < levels.length * colCountPerLevel + 2; index++) {// starts
																						// count
																						// from
																						// column
																						// 2
																						// going
																						// across
																						// the
																						// length
																						// of
																						// the
																						// sem_table_data
																						// tables
				if (sem1_main_table_data[period * schoolGradeCount + grade][index] != null) {
					total++;
				}
			}
		} else if (sem == 2) {// conduct sem1 sections in table count
			for (int index = 2; index < levels.length * colCountPerLevel + 2; index++) {// starts
																						// count
																						// from
																						// column
																						// 2
																						// going
																						// across
																						// the
																						// length
																						// of
																						// the
																						// sem_table_data
																						// tables
				if (sem2_main_table_data[period * schoolGradeCount + grade][index] != null) {
					total++;
				}
			}
		}
		return total;
	}

	public static int countPeriodicPeriodWideTotal(int sem, int period) {
		int gradeWideTotal = 0;
		if (sem == 1) {
			for (int rowNum = 0; rowNum < schoolGradeCount; rowNum++) {
				gradeWideTotal += Integer
						.parseInt((String) (sem1_period_table_data[rowNum][period]));
			}
		} else if (sem == 2) {// conduct sem1 sections in table count
			for (int rowNum = 0; rowNum < schoolGradeCount; rowNum++) {
				gradeWideTotal += Integer
						.parseInt((String) (sem2_period_table_data[rowNum][period]));
			}
		}
		return gradeWideTotal;
	}

	public static void updatePeriodicTotals() {
		for (int rowNum = 0; rowNum <= schoolGradeCount; rowNum++) {
			for (int colNum = 0; colNum <= schoolPeriodCount; colNum++) {
				if (colNum > 0 & rowNum < schoolGradeCount) {// updates
																// displayed #of
																// courses
					sem1_period_table_data[rowNum][colNum] = Integer
							.toString(countPeriodicTotal(1, rowNum, colNum - 1));
					sem2_period_table_data[rowNum][colNum] = Integer
							.toString(countPeriodicTotal(2, rowNum, colNum - 1));
				} else if (colNum > 0 & rowNum == schoolGradeCount) {
					sem1_period_table_data[rowNum][colNum] = Integer
							.toString(countPeriodicPeriodWideTotal(1, colNum));
					sem2_period_table_data[rowNum][colNum] = Integer
							.toString(countPeriodicPeriodWideTotal(2, colNum));
				}
			}
		}
	}

	public static void updateDeptTotal() {
		sem1_DeptTotal = 0;
		sem2_DeptTotal = 0;// reset to recount
		for (int colNum = 1; colNum <= schoolPeriodCount; colNum++) {
			sem1_DeptTotal += Integer
					.parseInt(sem1_period_table_data[schoolGradeCount][colNum]);
			sem2_DeptTotal += Integer
					.parseInt(sem2_period_table_data[schoolGradeCount][colNum]);
		}
	}

	public static void updateSchoolTotal() {// Counts the number of placed
											// courses within the school
		sem1_Total = 0;
		sem2_Total = 0;
		for (int deptsIndex = 0; deptsIndex < departments.length; deptsIndex++) {// iterates
																					// through
																					// each
																					// department
			for (int yCoordinate = 0; yCoordinate < timeT[0].length; yCoordinate++) {// Iterates
																						// through
																						// each
																						// grade
																						// and
																						// period
				for (int xCoordinate = 0; xCoordinate < timeT[0][yCoordinate].length; xCoordinate++) {// iterates
																										// through
																										// all
																										// locations
																										// in
																										// the
																										// period
					if (timeT[deptsIndex][yCoordinate][xCoordinate][0] != null) {
						sem1_Total++;
					}
				}
			}
		}
		for (int deptsIndex = 0; deptsIndex < departments.length; deptsIndex++) {
			for (int yCoordinate = 0; yCoordinate < timeT[0].length; yCoordinate++) {
				for (int xCoordinate = 0; xCoordinate < timeT[0][yCoordinate].length; xCoordinate++) {
					if (timeT[deptsIndex][yCoordinate][xCoordinate][1] != null) {
						sem2_Total++;
					}
				}
			}
		}
	}

	private static void resetRoomAvailability() {// resets the availability
													// count of the rooms to
													// default
		int[][] defaultAvailability = new int[2][schoolPeriodCount];

		for (int index = 0; index < rooms.length; index++) {
			for (int availabilityIndex = 0; availabilityIndex < schoolPeriodCount; availabilityIndex++) {
				defaultAvailability[0][availabilityIndex] = rooms[index].originalNumOfRooms;
				defaultAvailability[1][availabilityIndex] = rooms[index].originalNumOfRooms;
			}
			rooms[index].setAvailability(defaultAvailability);
		}
	}

	public static void updateRoomStatuses() {// Recounts the status of the rooms
		resetRoomAvailability();
		for (int deptsIndex = 0; deptsIndex < departments.length; deptsIndex++) {// iterates
																					// through
																					// each
																					// department
			for (int yCoordinate = 0; yCoordinate < timeT[0].length; yCoordinate++) {// Iterates
																						// through
																						// each
																						// grade
																						// and
																						// period
				for (int xCoordinate = 0; xCoordinate < timeT[0][yCoordinate].length; xCoordinate++) {// iterates
																										// through
																										// all
																										// locations
																										// in
																										// the
																										// period
					if (timeT[deptsIndex][yCoordinate][xCoordinate][0] != null)
						findRoom(
								timeT[deptsIndex][yCoordinate][xCoordinate][0]
										.getRoomType()).holdSectionAt(0,
								(int) yCoordinate / schoolGradeCount);
					if (timeT[deptsIndex][yCoordinate][xCoordinate][1] != null)
						findRoom(
								timeT[deptsIndex][yCoordinate][xCoordinate][1]
										.getRoomType()).holdSectionAt(1,
								(int) yCoordinate / schoolGradeCount);
				}
			}
		}
	}

	// /////////////////////////////////////////////////////////////////////CourseManager
	// Window 2-centric data and methods
	// String[] for deptFilter_list
	public static String[] dept_filters = { "" };
	// String[] for lvlFilter_list
	public static String[] lvl_filters = { "" };

	// String[] for courseSelections
	public static String[] courseSelections;
	public static boolean isTimeTEmpty = true;

	// TODO TAG: Window 2 Related
	// Methods//////////////////////////////////////////////////////////////
	private static void reset_dept_filters() {
		// Updates dept_filters array to contents of departments array and add
		// "ALL" as last, additional element
		dept_filters = new String[departments.length + 1];
		for (int index = 0; index < dept_filters.length; index++) {
			if (index != departments.length)
				dept_filters[index] = departments[index].getName();// Set
																	// department
																	// names
			// as filter options
			else
				dept_filters[index] = "ALL";// Final department filter option
			// displayed is "ALL"
		}
	}

	private static void reset_lvl_filters() {
		// Updates lvl_filters array to contents of levels array and add "ALL"
		// as last, additional element
		lvl_filters = new String[levels.length + 1];
		for (int index = 0; index < lvl_filters.length; index++) {
			if (index != levels.length)
				lvl_filters[index] = levels[index];// Set department names as
			// filter options
			else
				lvl_filters[index] = "ALL";// Final department filter option
			// displayed is "ALL"
		}
	}

	private static void reset_courseSelections() {// Makes names
		// courseSelections[] equal
		// to names contained by
		// courses in courses[]
		courseSelections = new String[courses.length];
		for (int index = 0; index < courses.length; index++) {
			courseSelections[index] = "("
					+ courses[index].getRemainingSectionCount() + "/"
					+ courses[index].getExistingSectionCount() + ") "
					+ courses[index].name;
		}
	}

	public static void refilter_CourseSelections(String selected_DeptFilter,
			String selected_LvlFilter) {// Filters the displayed courses in
										// course
		// TODO: create filter Style
		int validCourseCount = 0;
		for (int index = 0; index < courses.length; index++) {// Counts the
			// number of valid courses
			if ((selected_DeptFilter.equals("ALL") | courses[index].department
					.getName().equals(selected_DeptFilter))
					& (selected_LvlFilter.equals("ALL") | courses[index].level
							.equals(selected_LvlFilter))) {
				validCourseCount++;
			}
		}
		courseSelections = new String[validCourseCount];
		// Resets the array to the number of the valid courses
		int cS_Index = -1;// helper index to move through CourseSelections array
		for (int index = 0; index < courses.length; index++) {// Places each
			// courseName into the array
			if ((selected_DeptFilter.equals("ALL") | courses[index].department
					.getName().equals(selected_DeptFilter))
					& (selected_LvlFilter.equals("ALL") | courses[index].level
							.equals(selected_LvlFilter))) {
				cS_Index++;
				courseSelections[cS_Index] = "("
						+ courses[index].getRemainingSectionCount() + "/"
						+ courses[index].getExistingSectionCount() + ") "
						+ courses[index].name;
			}
		}
	}

	public static void resetArray(String arrayname) {// Used to update values in
		// certain arrays, window 2 arrays
		// called when a change in another array that it is related to occurs
		/*
		 * Usable arraynames: ALL* dept_filters lvl_filters
		 */

		if (arrayname.equals("ALL")) {// TODO: Update as more arrays are added
			reset_dept_filters();
			reset_lvl_filters();
			reset_courseSelections();
		} else if (arrayname.equals("dept_filters"))// updates dept_filters
		{
			reset_dept_filters();
		} else if (arrayname.equals("lvl_filters"))// updates lvl_filters
		{
			reset_lvl_filters();
		} else if (arrayname.equals("courseSelections")) {
			reset_courseSelections();
		}
	}

	public static void updateCourseStatuses() {
		int deptPos = -1;
		for (int courseIndex = 0; courseIndex < courses.length; courseIndex++) {
			// Resets section count to max
			courses[courseIndex]
					.setRemainingSectionCount(courses[courseIndex].existingSectionCount);
			// narrows down search area to a single department
			deptPos = DataBase.findDeptPos(courses[courseIndex].department
					.getName());
			for (int sem = 0; sem < 2; sem++) {// iterates through both
												// semesters
				for (int rowNum = 0; rowNum < timeT[0].length; rowNum++) {// iterates
																			// through
																			// each
																			// row
					for (int xCoordinate = 0; xCoordinate < timeT[0][0].length; xCoordinate++) {// iterates
																								// through
																								// each
																								// column
						if (timeT[deptPos][rowNum][xCoordinate][sem] != null
								&& timeT[deptPos][rowNum][xCoordinate][sem]
										.getName().equals(
												courses[courseIndex].getName()))
							courses[courseIndex]
									.subtractFromRemainingSectionCount(1);// Subtracts
																			// 1
																			// from
																			// remainingSectionCount
					}
				}
			}
		}
	}

	public static void initializeTimeT() {
		// Set timeT size
		int period = DataBase.schoolPeriodCount;
		int grade = DataBase.schoolGradeCount;
		int numberOfDepartments = DataBase.departments.length;
		int numberOfRowsPerDept = grade * period;
		int numberOfColsPerDept = 4 * DataBase.levels.length;
		System.out.println(numberOfDepartments);
		int numberOfSemesters = DataBase.schoolYearDivisions;
		DataBase.timeT = new Course[numberOfDepartments][numberOfRowsPerDept][numberOfColsPerDept][numberOfSemesters];
		DataBase.timeTcopy = new Course[numberOfDepartments][numberOfRowsPerDept][numberOfColsPerDept][numberOfSemesters];
		isNew = false;
	}

	public static void initializeTimeTCopy() {
		int period = DataBase.schoolPeriodCount;
		int grade = DataBase.schoolGradeCount;
		int numberOfDepartments = DataBase.departments.length;
		int numberOfRowsPerDept = grade * period;
		int numberOfColsPerDept = 4 * DataBase.levels.length;// 2
																			// slots
																			// per
																			// level
																			// per
																			// period
		System.out.println(numberOfDepartments);
		int numberOfSemesters = DataBase.schoolYearDivisions;
		DataBase.timeTcopy = new Course[numberOfDepartments][numberOfRowsPerDept][numberOfColsPerDept][numberOfSemesters];
		isNew = false;
	}

	public static void setTimeTCopy() {// initializes timeTcopy using timeT
		initializeTimeTCopy();
		int levelnum = 0;// used to hold the course's level
		int xaxis = 0;// used for row index
		int totCol = 0; // holds the # of cols
		DataBase.totalColCount = new int[DataBase.levels.length];
		for (int x = 0; x < DataBase.levels.length; x++) {
			DataBase.totalColCount[x] = 0;
		}
		for (int semcount = 0; semcount < DataBase.schoolYearDivisions; semcount++) {
			for (int x = 0; x < DataBase.levels.length; x++) {
				if (DataBase.totalColCount[x] < DataBase.levelColCount[x][semcount]) {
					DataBase.totalColCount[x] = DataBase.levelColCount[x][semcount];
				}
			}
		}
		for (int d = 0; d < DataBase.timeT[0][0][0].length; d++) {// semester
			for (int a = 0; a < DataBase.timeT.length; a++) {// department
				for (int b = 0; b < DataBase.timeT[0].length; b++) {
					for (int c = 0; c < DataBase.timeT[0][0].length; c++) {
						if (DataBase.timeT[a][b][c][d] != null) {
							for (int lvl = 0; lvl < DataBase.levels.length; lvl++) {
								if (DataBase.timeT[a][b][c][d].level
										.equalsIgnoreCase(DataBase.levels[lvl])) {
									levelnum = lvl;
									lvl = DataBase.levels.length;
								}
							}
							xaxis = 0;
							// sets the x axis for the row
							for (int x = 0; x < levelnum; x++) {
								xaxis = xaxis + DataBase.totalColCount[x];
							}
							totCol = xaxis + DataBase.totalColCount[levelnum];

						}
						boolean flag = false;
						do {
							// System.out.println( a + ": " + b + ": "+ xaxis + ": "+ d + ": ");
							// System.out.println(DataBase.timeTcopy[0][0].length);
							if (DataBase.timeTcopy[a][b][xaxis][d] == null) {// if
																				// timeTcopy
																				// is
																				// empty
								DataBase.timeTcopy[a][b][xaxis][d] = DataBase.timeT[a][b][c][d];// copy
																								// the
																								// course
																								// in
																								// from
																								// timeT
								flag = true;
							} else {
								xaxis++;// increase x axis
								if (xaxis > totCol) {
									DataBase.totalColCount[levelnum]++;
								}
							}
						} while (flag == false);
						DataBase.timeTcopy[a][b][xaxis][d] = DataBase.timeT[a][b][c][d];
					}
				}
			}
		}
		initializeTimeTCopy();
		for (int d = 0; d < DataBase.timeT[0][0][0].length; d++) {// semester
			for (int a = 0; a < DataBase.timeT.length; a++) {// department
				for (int b = 0; b < DataBase.timeT[0].length; b++) {
					for (int c = 0; c < DataBase.timeT[0][0].length; c++) {
						if (DataBase.timeT[a][b][c][d] != null) {
							for (int lvl = 0; lvl < DataBase.levels.length; lvl++) {
								if (DataBase.timeT[a][b][c][d].level
										.equalsIgnoreCase(DataBase.levels[lvl])) {
									levelnum = lvl;
									lvl = DataBase.levels.length;
								}
							}
							xaxis = 0;
							// sets the x axis for the row
							for (int x = 0; x < levelnum; x++) {
								xaxis = xaxis + DataBase.totalColCount[x];
							}

						}
						boolean flag = false;
						do {
							// System.out.println( a + ": " + b + ": "+ xaxis +
							// ": "+ d + ": ");
							// System.out.println(DataBase.timeTcopy[0][0].length);
							if (DataBase.timeTcopy[a][b][xaxis][d] == null) {// if
																				// timeTcopy
																				// is
																				// empty
								DataBase.timeTcopy[a][b][xaxis][d] = DataBase.timeT[a][b][c][d];// copy
																								// the
																								// course
																								// in
																								// from
																								// timeT
								flag = true;
							} else {
								xaxis++;// increase x axis
							}
						} while (flag == false);
						DataBase.timeTcopy[a][b][xaxis][d] = DataBase.timeT[a][b][c][d];
					}
				}
			}
		}

	}
	
		private static void lvlTransfer(Course course, int deptsIndex, int yCoordinate, int sem)
		{
			yCoordinate = ((int)(yCoordinate/DataBase.schoolGradeCount))*DataBase.schoolGradeCount + course.getGrade();
			for(int xCoordinate = 0; xCoordinate<timeT[0][yCoordinate].length; xCoordinate++)
			{
				if(timeT[deptsIndex][yCoordinate][xCoordinate][sem]==null)
				{
					timeT[deptsIndex][yCoordinate][xCoordinate][sem] = course;
					xCoordinate = timeT[0][yCoordinate].length;
				}
			}
		}
		private static void deptTransfer(Course course, int yCoordinate, int sem)
		{
			for(int deptsIndex = 0; deptsIndex<departments.length; deptsIndex++)
			{
				if(DataBase.findDeptPos(course.getDept().getName()) == deptsIndex)
				{
					for(int xCoordinate = 0; xCoordinate<timeT[0][yCoordinate].length; xCoordinate++)
					{
						if(timeT[deptsIndex][yCoordinate][xCoordinate][sem]==null)
						{
							timeT[deptsIndex][yCoordinate][xCoordinate][sem] = course;
							xCoordinate = timeT[0][yCoordinate].length;
						}
					}
				}
			}
		}
	public static void rescanForValidity()
	{//TODO: Create a check method for new invalidities
		System.out.println("SchoolGradeCount: " + DataBase.schoolGradeCount);
		for(int deptsIndex = 0; deptsIndex<departments.length; deptsIndex++)
		{//iterates through each department
			for(int yCoordinate = 0; yCoordinate<timeT[0].length; yCoordinate++)
			{//Iterates through each grade and period
				for(int xCoordinate = 0; xCoordinate<timeT[0][yCoordinate].length; xCoordinate++)
				{
					for(int sem = 0; sem<timeT[0][yCoordinate][xCoordinate].length; sem++)
					{
						if(timeT[deptsIndex][yCoordinate][xCoordinate][sem]!=null)
						{
							if(DataBase.findDeptPos(timeT[deptsIndex][yCoordinate][xCoordinate][sem].getDept().getName()) != deptsIndex)
							{
								deptTransfer(timeT[deptsIndex][yCoordinate][xCoordinate][sem], yCoordinate, sem);
								timeT[deptsIndex][yCoordinate][xCoordinate][sem] = null;
							}
						}
					}
				}
			}
		}
		for(int deptsIndex = 0; deptsIndex<departments.length; deptsIndex++)
		{//iterates through each department
			for(int yCoordinate = 0; yCoordinate<timeT[0].length; yCoordinate++)
			{//Iterates through each grade and period
				for(int xCoordinate = 0; xCoordinate<timeT[0][yCoordinate].length; xCoordinate++)
				{
					for(int sem = 0; sem<timeT[0][yCoordinate][xCoordinate].length; sem++)
					{
						if(timeT[deptsIndex][yCoordinate][xCoordinate][sem]!=null)
						{
							if(timeT[deptsIndex][yCoordinate][xCoordinate][sem].getGrade() != yCoordinate%DataBase.schoolGradeCount)
							{
								System.out.println("Transfering: " + yCoordinate%DataBase.schoolGradeCount);
								lvlTransfer(timeT[deptsIndex][yCoordinate][xCoordinate][sem], deptsIndex, yCoordinate, sem);
								timeT[deptsIndex][yCoordinate][xCoordinate][sem] = null;
							}
						}
					}
				}
			}
		}
	}
}