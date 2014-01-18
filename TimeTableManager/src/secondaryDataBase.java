/* Class: secondaryDataBase
 * Author: Luka Pavlovic Ed. Troylan Tempra Jr.
 * Last Modified:20/1/2013
 * Description: Mini-Database. Holds data temporarily. Used when
 * 	creating a new project or editing data from current project
 */
import java.awt.Color;

// this is a mini-database
public class secondaryDataBase {
	// --------------------------------------------------------------::Basic
	// holds the number of grades
	public static int grades = 0;
	// holds the name of the school
	public static String nameOfSchool = "";
	// holds number of periods in each school day
	public static int amountOfPeriods = 0;
	// holds the grade at which the school starts (typically grade 9)
	public static int gradesStart = 0;
	// holds the name of the project (template)
	public static String projectName = "";
	// holds the lunch preferences
	public static boolean[][] lunchPrefs;
	// Semesters in the school
	public static int semesters;

	// --------------------------------------------------------------::Rooms
	// holds the rooms in a single String
	public static String roomList = "";
	// holds the name of the rooms in a String array
	public static String[] roomArray = { "Testing Room Type" };
	// number of total different rooms
	public static int roomCount = 0;
	// number of rooms per room type held in an array
	public static int[] roomTypeAmount = { 1 };
	// --------------------------------------------------------------::Departments
	// amount of departments
	public static int deptCount = 0;
	// holds each department in a single string
	public static String deptList = "";
	// holds each departments
	public static String[] deptArray = { "Testing Department" };

	// --------------------------------------------------------------::Courses
	// Array holds the course data
	public static String[][] courseData;

	public static void dbTransferReverse() {
		// TODO: create reverse dbTransfer
		// To: secondaryDataBase
		// From: DataBase

		// Assign values to the database
		secondaryDataBase.projectName = DataBase.projectName;
		secondaryDataBase.amountOfPeriods = DataBase.schoolPeriodCount;
		secondaryDataBase.grades = DataBase.schoolGradeCount;
		secondaryDataBase.semesters = DataBase.schoolYearDivisions;

		secondaryDataBase.lunchPrefs = getLunchPrefs();

		// Copy Room Objects
		secondaryDataBase.roomArray = new String[DataBase.rooms.length];
		secondaryDataBase.roomTypeAmount = new int[DataBase.rooms.length];
		for (int i = 0; i < DataBase.rooms.length; i++) {
			secondaryDataBase.roomArray[i] = DataBase.rooms[i].getType();
			secondaryDataBase.roomTypeAmount[i] = DataBase.rooms[i].originalNumOfRooms;
		}

		// Copy to temporary DataBase
		secondaryDataBase.lunchPrefs = new boolean[DataBase.lunchPrefs.length][DataBase.lunchPrefs[0].length];
		for (int i = 0; i < DataBase.lunchPrefs.length; i++) {
			for (int b = 0; b < DataBase.lunchPrefs[0].length; b++) {
//				System.out.println("DataBase.lunchPrefs[i][b]: "+DataBase.lunchPrefs[i][b]);
//				System.out.println("DataBase.grades[i].getLunchPref()[b]: " + DataBase.grades[i].getLunchPref()[b]);
				secondaryDataBase.lunchPrefs[i][b] = DataBase.grades[i].getLunchPref()[b];
			}
		}

		// Copy to temprorary DataBase
		secondaryDataBase.deptArray = new String[DataBase.departments.length];
		for (int i = 0; i < DataBase.departments.length; i++) {
			secondaryDataBase.deptArray[i] = DataBase.departments[i].getName();
		}

		// (String name, Department department, String level, int grade,
		// String roomType, int sectionCount)

		// Copy courses to temporary DataBase
		IIcourses.data = new Object[DataBase.courses.length][7];
		for (int i = 0; i < IIcourses.data.length; i++) {
			IIcourses.data[i][0] = DataBase.courses[i].getName();// Code
			IIcourses.data[i][1] = DataBase.courses[i].getDept().getName();// Dept
			IIcourses.data[i][2] = DataBase.courses[i].getLevel();// Level
			IIcourses.data[i][3] = DataBase.courses[i].getGrade() + 9;// Grade
			IIcourses.data[i][4] = DataBase.courses[i].getRoomType();// Room
			IIcourses.data[i][5] = DataBase.courses[i]
					.getExistingSectionCount();// SectionCount
			IIcourses.data[i][6] = restrictionsBoolToString(DataBase.courses[i].perPref);// PeriodRes
		}
	}

	public static String restrictionsBoolToString(boolean[] perPref) {
		String str = "";
		boolean allIsRestricted = true;
		boolean noneAreRestricted = true;
		for (int i = 0; i < perPref.length; i++) {
			if (perPref[i] == true) {
				allIsRestricted = false;
			} else {
				noneAreRestricted = false;
			}
		}
		if (allIsRestricted) {
			return "All";
		} else if (noneAreRestricted) {
			return "None";
		}

		for (int i = 0; i < perPref.length; i++) {
			if (perPref[i] == false) {
				str += Integer.toString(i + 1) + " ";
			}
		}
		return str;
	}

	public static boolean[][] getLunchPrefs() {

		boolean[][] booAr = new boolean[DataBase.grades.length][secondaryDataBase.amountOfPeriods];

		for (int i = 0; i < DataBase.grades.length; i++) {
			booAr[i] = DataBase.grades[i].getLunchPref();
		}
		
		return booAr;

	}
}
