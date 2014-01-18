/* Class: IOToolkit
 * Author: Troylan Tempra Jr. Ed. John Kwon, William Zhao
 * Last Modified:20/1/2013
 * Description: File writing and reading class
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import javax.swing.JOptionPane;


public class IOToolkit {
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static String[] file = new String[0];//empty string array for files being read.
	static int flength = 0;//length of the String[] file if it will be changed.
	public static char c = (char)(92);
	public static String dash = String.valueOf(c);
	public static boolean confirm_file(String name, File theFile)
	{//checks if a file exists, returns boolean
		boolean file_exists = true;//boolean remains true unless error occurs and file cannot be read.
		File file = new File(breakDash(theFile.getPath(), name));
		try{
			System.out.println(breakDash(theFile.getPath(), name));
			FileReader fr = new FileReader(file);
			//fr.chill(oolong_tea);
			fr.close();
		}catch(IOException fwee)
		{//error will occur if file does not exists and boolean value is changed to false
			file_exists = false;
			JOptionPane.showMessageDialog(null,
					"Error "+ fwee.toString() ,
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
		return file_exists;
	}
	public static boolean confirm_file(String file_name)
	{//checks if a file exists, returns boolean
		boolean file_exists = true;//boolean remains true unless error occurs and file cannot be read.
		try{
			FileReader fr = new FileReader(file_name);
			//fr.chill(oolong_tea);
			fr.close();
		}catch(IOException fwee)
		{//error will occur if file does not exists and boolean value is changed to false
			file_exists = false;
			JOptionPane.showMessageDialog(null,
					"Error "+ fwee.toString() ,
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
		return file_exists;
	}
	private static void checkfile_length(String name, File theFile)
	{//checks the length of a file and returns an integer representing the length, the file ends if null is read
		String line = "";//contains line in the file being tested for null
		flength = 0;//reset length
		File file = new File(breakDash(theFile.getPath(), name));
		System.out.println(file.getName());
		try
		{
			BufferedReader fbr = new BufferedReader(new FileReader(file));
			line = fbr.readLine();//reads the first line
			while(line!=null)
			{//if the line previously read contains something, increment and read next line
				flength++;
				line = fbr.readLine();
			}
			fbr.close();
		}catch(IOException fwee)
		{
			System.out.println("This should never print, please check code (Toolkit class).");
			fwee.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Error "+ fwee.toString() ,
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
		//System.out.println("File Length: " + flength);
	}

	public static void set_File(String templateName, File theFile)
	{//reads a full file and puts it into the String[] file
		if(confirm_file(templateName, theFile))
		{

			checkfile_length(templateName, theFile);//checks the file length
			File filo = new File(breakDash(theFile.getPath(), templateName));
			file = new String[flength];//accordingly resets the String[] file to the actual file length
			try
			{

				BufferedReader fbr = new BufferedReader(new FileReader(filo));
				for(int line_num = 0; line_num<flength; line_num++)
				{//puts all the lines in the file into the String[] file
					file[line_num] = fbr.readLine();
					System.out.println(file[line_num]);
				}	
				fbr.close();
			}catch(IOException fwee)
			{
				System.out.println("This should never print, please check code(Toolkit class)");
				fwee.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Error "+ fwee.toString() ,
						"ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void printFile()
	{//used to check the contents of String[] file, prints the whole array
		for(int line_num = 0; line_num<file.length; line_num++)
		{
			System.out.println(file[line_num]);
		}
	}

	public static String get_fline(int line_pos)
	{//returns a line from String[] file, making parts of it accessible to other classes
		try{
			return file[line_pos];
		}catch(Exception e){e.printStackTrace();
		return "0";}
	}
	public String[] get_file()
	{//returns a full copy of the file currently in String[] file
		return file;
	}
	public static int get_flength()
	{//returns the current known file length without checking
		return flength;
	}

	public static void loadTimeT(String templateName, File file)//load the time table from file
	{

		if (confirm_file(templateName + ".tt", file)) {//checks if the file exists
			File filo = new File(breakDash(file.getPath(), templateName + ".tt"));//recreate the file with different extension
			String line = "";//makes a line variable
			int sem = 0;//semester variable
			int dep = 0;//department varaible
			int i = 0;//column varaible
			String[] tokens;//string array used to break a string apart
			try {
				FileReader fr = new FileReader(filo);//file reader used to read filo
				br = new BufferedReader(fr);//bufferedreader to read file
				line = br.readLine();//reads line
				while (line != null) {//check if not line not empty n run while loop

					if(line.equals("$"))//checks for semesters
					{
						sem++;
						dep =0;
					}
					else if(line.equals("#"))//checks for departments
					{
						dep++;
						i =0;
					}
					else
					{
						tokens = line.split(" ");
						i++;
						for(int j = 0; j < tokens.length; j++)
						{
							if(tokens[j].equals("XXXX"))//if it empty
							{DataBase.timeT[dep-1][i-1][tokens.length-1][sem-1] = null;}//set as null
							else
							{

								DataBase.timeT[dep-1][i-1][j][sem-1] = DataBase.findCourse(tokens[j]);//set course
								System.out.println(DataBase.timeT[dep-1][i-1][j][sem-1].name);
							}
						}
					}
					line = br.readLine();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Error "+ ex.toString() ,
						"ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
			Course_Manager_Main.window1.export.setEnabled(true);//enable export option
		} else {
			Course_Manager_Main.window1.export.setEnabled(false);//disable export option
			System.out
			.println("Could not find .courses file for templateName: "
					+ file.getName());
			JOptionPane.showMessageDialog(null,
					"Error "+ "Could not find .courses file for templateName: "
							+ file.getName() ,
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
	}


	public static void saveTimeT(File file)//saves the timeT array into file
	{
		boolean first = true;//check if a course has been written
		try {
			String[] IDs;//String array made to hold course for each row
			FileWriter fw = new FileWriter(file+".tt"); //makes a file writer to write to specified file
			// use boolean true to amend text and not replace it
			// Creates a BufferdWriter and sends it the FileWriter
			BufferedWriter bw = new BufferedWriter(fw);//buffered writer to write file
			for(int d = 0; d < DataBase.timeT[0][0][0].length; d++)//loop for semester of timet array
			{
				bw.write("$");//flag used to indicate beginning of semester
				bw.newLine();
				for(int a= 0 ; a < DataBase.timeT.length; a++)//loop of timeT array :collum
				{
					for(int b = 0; b < DataBase.timeT[0].length; b++)//loop of timeT array :department
					{
						IDs = new String[DataBase.timeT[0].length];//initialize the array
						for(int x = 0; x < DataBase.timeT[0].length; x++)//fill array with blanks
						{
							IDs[x] = "";
						}
						if(b == 0)//beginning of department
						{
							bw.write("#");//write this symbol to indicate the beginning of department
							bw.newLine();
						}
						for(int c = 0; c < DataBase.timeT[0][0].length; c++)//loop of timeT array :row
						{	
							if(DataBase.timeT[a][b][c][d] != null & first)//check if the timeT array is not empty and if course has not been written yet
							{
								IDs[b] = IDs[b].concat(DataBase.timeT[a][b][c][d].name);//puts course into the string array
								first = false;
							}
							else if(DataBase.timeT[a][b][c][d] != null & !first)
							{
								IDs[b] = IDs[b].concat(" ").concat(DataBase.timeT[a][b][c][d].name);//check if the timeT array is not empty and if course has been written
							}
						}
						if(IDs[b] == "")//check if array is empty
						{
							bw.write("XXXX");//writes xxxx as indication that there is no course placed in row
						}
						else//if not empty
						{
							bw.write(IDs[b]);//writes the course
						}
						first =true;
						bw.newLine();
					}
				}
			}
			// close BW
			bw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Error "+ ex.toString() ,
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void readPnGCFile(String templateName, File file) {// reads file
		// containing data
		// for DataBase.schoolPeriodCount and DataBase.schoolGradeCount then assigns lunchPref
		System.out.println(templateName + ".PnGC");
		set_File(templateName + ".PnGC", file);
		DataBase.schoolPeriodCount = Integer.parseInt(get_fline(0).split(" ")[0]);
		// Extract first integer and give to periodCount
		DataBase.schoolGradeCount = Integer.parseInt(get_fline(0).split(" ")[1]);
		// Extract second integer and give to gradeCount
		DataBase.schoolYearDivisions = 2;
		// Console prompts for checking
		System.out.println("schoolPeriodCount set to: " + DataBase.schoolPeriodCount);
		System.out.println("schoolGradeCount set to: " + DataBase.schoolGradeCount);
		DataBase.grades = new Grade[DataBase.schoolGradeCount];// initiate new grades array
		// according to how many grades are indicated

		set_File(templateName + ".lunchpref", file);// switch file to /lunchpref
		DataBase.lunchPrefs = new boolean[DataBase.schoolGradeCount][DataBase.schoolPeriodCount];
		// spawn new lunchPrefs array according to gradecount and period count
		for (int index = 0; index < DataBase.schoolGradeCount; index++) {
			for (int index2 = 0; index2 < DataBase.schoolPeriodCount; index2++) {
				if (get_fline(index).split(" ")[index2].equals("1")) {
					DataBase.lunchPrefs[index][index2] = true;
				} else {
					DataBase.lunchPrefs[index][index2] = false;
				}
			}
		}

		// Console prompt for checking,
		for (int index = 0; index < DataBase.schoolGradeCount; index++) {
			System.out.print("Grade " + index + 9 + " lunchPrefs set to: ");
			for (int index2 = 0; index2 < DataBase.schoolPeriodCount; index2++) {
				System.out.print(DataBase.lunchPrefs[index][index2] + " ");
			}
			System.out.println();
		}// end console prompt

		for (int index = 0; index < DataBase.schoolGradeCount; index++) {// assign
			// lunchprefs to
			// grades
			DataBase.grades[index] = new Grade(index + 9, DataBase.lunchPrefs[index],
					DataBase.schoolPeriodCount);
		}
	}

	public static void readLevelsFile(String templateName, File file) {// reads a file and
		// uses its lines as the String for levels[]
		if (confirm_file(templateName + ".levels", file)) {
			set_File(templateName + ".levels", file);
			DataBase.levels = new String[get_flength()];
			// sets array length equal to file length
			for (int index = 0; index < DataBase.levels.length; index++) {
				DataBase.levels[index] = get_fline(index);
			}
			for (int index = 0; index < DataBase.levels.length; index++) {// for checking
				System.out.println(DataBase.levels[index] + ": Loaded as level");
			}
		} else {
			System.out.println("Could not find .levels file for templateName: "
					+ templateName);
			JOptionPane.showMessageDialog(null,
					"Error "+ "Could not find .levels file for templateName: "
							+ templateName,
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void readDeptsFile(String templateName, File file)
	{//Sets visibility options array in database
		set_File(templateName + ".depts", file);
		DataBase.departments = new Department[get_flength()];
		for (int index = 0; index<get_flength(); index++)
		{//Iterates through each line in the file
			String[] line = get_fline(index).split(", ");
			//Create Department
			DataBase.departments[index] = new Department(line[0]);
			//Set column and row dimentsions
			DataBase.departments[index].rowHeight = Integer.parseInt(line[1]);
			DataBase.departments[index].colWidth = Integer.parseInt(line[2]);
			for(int levelNum = 0; levelNum<DataBase.levels.length; levelNum++)
			{//Sets visibility of columns in department
				DataBase.departments[index].colVis[levelNum] = Integer.parseInt(line[3+levelNum]);
			}
			DataBase.departments[index].setBackGroundColor(//Sets Background color of dept
					Integer.parseInt(line[DataBase.levels.length+3]),
					Integer.parseInt(line[DataBase.levels.length+4]),
					Integer.parseInt(line[DataBase.levels.length+5]));
			DataBase.departments[index].setForeGroundColor(//Sets text color of dept
					Integer.parseInt(line[DataBase.levels.length+6]),
					Integer.parseInt(line[DataBase.levels.length+7]),
					Integer.parseInt(line[DataBase.levels.length+8]));
		}

		for (int index = 0; index<DataBase.departments.length; index++)
		{//Console Prompt
			try{
				System.out.println("Loaded department: " + DataBase.departments[index].getName() + ", "
						+ DataBase.departments[index].rowHeight + ", "
						+ DataBase.departments[index].colWidth + ", "
						+ DataBase.departments[index].colVis[0] + ", "
						+ DataBase.departments[index].colVis[1] + ", "
						+ DataBase.departments[index].colVis[2] + ", "
						+ DataBase.departments[index].colVis[3] + ", "
						+ DataBase.departments[index].colVis[4] + ", "
						+ DataBase.departments[index].backGroundColor.getRed() + ", "
						+ DataBase.departments[index].backGroundColor.getGreen() + ", "
						+ DataBase.departments[index].backGroundColor.getBlue() + ", "
						+ DataBase.departments[index].foreGroundColor.getRed() + ", "
						+ DataBase.departments[index].foreGroundColor.getGreen() + ", "
						+ DataBase.departments[index].foreGroundColor.getBlue()
						);//Prompt
			}catch(NullPointerException e){
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Error "+ e.toString() ,
						"ERROR",
						JOptionPane.ERROR_MESSAGE);}
		}

	}

	public static void readRoomsFile(String templateName, File file) {// Reads templateName.rooms file
		set_File(templateName + ".rooms", file);//sets the file
		DataBase.rooms = new Room[get_flength()];//initializes the size of array
		for (int index = 0; index < get_flength(); index++) {
			DataBase.rooms[index] = new Room(get_fline(index).split(", ")[0],
					Integer.parseInt(get_fline(index).split(", ")[1]),
					DataBase.schoolPeriodCount);
			String[] line = get_fline(index).split(", ");
			int[][] availability = new int[DataBase.schoolYearDivisions][line.length];
			for(int i = 0; i < DataBase.schoolYearDivisions;i++){
				for(int j = 2; j < line.length;j++){
					int max = (DataBase.schoolPeriodCount* i) +DataBase.schoolPeriodCount;
					if(j < max){
						availability[i][j] = Integer.parseInt(line[j]);
					}
				}
			}
			DataBase.rooms[index].setAvailability(availability);
			System.out.println(DataBase.rooms[index].getType() + ": Loaded as Room");
		}
	}

	public static void readCoursesFile(String templateName, File file) {
		System.out.println(templateName + ".courses");
		if (confirm_file(templateName + ".courses", file)) {
			set_File(templateName + ".courses", file);
			DataBase.courses = new Course[get_flength()];
			// sets array length equal to file length
			for (int index = 0; index < DataBase.courses.length; index++) {// Extracts
				// data written in flat file to construct a course.
				String line = get_fline(index);
				DataBase.courses[index] = new Course(line.split(", ")[0],// Extract Name
						DataBase.getDepartment(line.split(", ")[1]),// Extract Dept
						line.split(", ")[2], // Extract level
						Integer.parseInt(line.split(", ")[3]) - 9,// Extract Grade
						(line.split(", ")[4]),// Extract RoomType
						Integer.parseInt(line.split(", ")[5]));//
				for(int period = 0; period < DataBase.schoolPeriodCount; period++){
					if(line.split(", ")[6+period].equalsIgnoreCase("1"))
						DataBase.courses[index].setPerPref(period, true);
					else if(line.split(", ")[6+period].equalsIgnoreCase("0"))
						DataBase.courses[index].setPerPref(period, false);
				}
			}
			for (int index = 0; index < DataBase.courses.length; index++) {// for
				// checking
				System.out.print(DataBase.courses[index]
						+ ": Loaded as course with Data: "
						+ DataBase.courses[index].name + " "
						+ DataBase.courses[index].department.getName() + " "
						+ DataBase.courses[index].level + " "
						+ DataBase.courses[index].grade + " "
						+ DataBase.courses[index].existingSectionCount + " "
						+ DataBase.courses[index].roomType);
				for(int period = 0; period < DataBase.schoolPeriodCount; period++){
					System.out.print(", " + DataBase.courses[index].getPerPref(period));
				}
				System.out.println();
			}
		} else {
			System.out
			.println("Could not find .courses file for templateName: "
					+ templateName);
			JOptionPane.showMessageDialog(null,
					"Error "+ "Could not find .courses file for templateName: "
							+ templateName ,
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void writeCoursesFile (File file){//will save courses data to a flat file
		int z = 0;
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(file+ ".courses"));//set file name and extension, then build file writer and link to buffered writer 
			for(int index = 0; index < DataBase.courses.length; index++)//for loop runs the number of courses 
			{
				int[] perpref = new int[DataBase.courses[index].perPref.length];
				for(int i =0; i<DataBase.courses[index].perPref.length; i++)
				{
					if(DataBase.courses[index].perPref[i] == true)
					{
						perpref[i] = 1;
					}else{
						perpref[i] = 0;
					}
				}
				bw.write(DataBase.courses[index].name +", "+//write course name
						DataBase.courses[index].department.getName() + ", " + //write course department
						DataBase.courses[index].level + ", " +//write course level
						(DataBase.courses[index].grade + 9) + ", " +//write course grade
						DataBase.courses[index].roomType + ", " +//write room requirements
						DataBase.courses[index].existingSectionCount + ", "
						);//write number of section in the course
				for(int i =0; i<DataBase.courses[index].perPref.length; i++)
				{
					if(i==DataBase.courses[index].perPref.length-1){
						bw.write(perpref[i] + "");
					}else{
						bw.write(perpref[i] + ", ");
					}
				}	
				bw.newLine();//next line
			}//end for
			bw.close();//close stream
		}catch(IOException ex){//crash prevention
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Error "+ ex.toString() ,
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
		}//end catch
		System.out.println("Wrote courses file to: " + file.getName() + ".courses");//inforn user that file has been written
	}//end writeCoursesFile

	public static void writeDeptsFile (File file){//will save list of departments to a flat file
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(file + ".depts"));//build a buffered reader - set file name
			for(int index = 0; index < DataBase.departments.length; index++)//runs however number of departments present
			{
				bw.write(DataBase.departments[index].getName()//write department name
						+ ", " + DataBase.departments[index].rowHeight//write dept rowheight
						+ ", " + DataBase.departments[index].colWidth);//write dept colwidth
				for(int levelsIndex = 0; levelsIndex<DataBase.levels.length; levelsIndex++)
				{//write the visibility of each level
					bw.write(", " + DataBase.departments[index].colVis[levelsIndex]);
				}
				bw.write(", " + DataBase.departments[index].backGroundColor.getRed()
						+ ", " + DataBase.departments[index].backGroundColor.getGreen()
						+ ", " + DataBase.departments[index].backGroundColor.getBlue()
						+ ", " + DataBase.departments[index].foreGroundColor.getRed()
						+ ", " + DataBase.departments[index].foreGroundColor.getGreen()
						+ ", " + DataBase.departments[index].foreGroundColor.getBlue());
				bw.newLine();//next line
			}
			bw.close();//close stream
		}catch(IOException ex){//crash prevent
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Error "+ ex.toString() ,
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
		}//end catch
		System.out.println("Wrote departments file to: " + file.getName() + ".depts");//inform user file has been writeen
	}//end writeDeptsFile
	public static boolean isDirectory(String path){//checks if tthe path is valid
		File file = new File(path);//makes a file with path
		if(file.exists())//check if path exists
		{
			return true;
		}else{
			return false;
		}
	}
	public static String breakDash(String path, String end)//this methods takes the path and removes the very end of path n replaces it with a new ending
	{
		char [] array = new char[path.length()];//intializes the array
		for (int i = 0;i < path.length(); i++){//fills array
			array[i] = path.charAt(i);
		}

		int index = linearSearchB(array, c);//finds the last directory
		array = new char[index];//reinitliazes it so that it wont include the last directory
		for (int i = 0;i < index; i++){
			array[i] = path.charAt(i);
		}
		String pathh = new String(array);//converts char array into String
		pathh = pathh.concat(dash).concat(end);//ands the ending desired
		return pathh;
	}
	public static File removeFileExtension(File file){//removes the file's extension
		String fileName = removeExtension(file.getName());
		String path = breakDash(file.getPath(), fileName);
		file = new File(path);
		return file;
	}
	public static String removeExtension(String filename)//removes extension from the filename String
	{
		String fileName = filename;
		char [] array = new char[filename.length()];
		for (int i = 0;i < filename.length(); i++){
			array[i] = filename.charAt(i);
		}
		char period = '.';
		int index = linearSearchB(array, period);
		if(index != -1){
			array = new char[index];
			for (int i = 0;i < index; i++){
				array[i] = filename.charAt(i);
			}
			fileName = new String(array);}
		return fileName;
	}

	public static int linearSearchB(char[] data, char lookfor)//linear search b searches and returns last location
	{
		int position = -1; //flag
		for(int i = 0; i < data.length; i++){
			if(data[i]==lookfor)
			{
				position = i;
			}
		}
		return position;
	}
	public static void writeLevelsFile (File file){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(file + ".levels"));//build buffered writer - set file name
			for(int index = 0; index < DataBase.levels.length; index++)//runs the number of levels
			{
				bw.write(DataBase.levels[index]);//write course level
				bw.newLine();//next line
			}
			bw.close();//close stream
		}catch(IOException ex){//crash prevent
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Error "+ ex.toString() ,
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
		}//end catch

		System.out.println("Wrote levels file to: " + file.getName() + ".levels");//inform user file has been written
	}//end writeLevelsFile

	public static void writePGCFile (File file){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(file + ".PnGC"));//build buffered writer - set file name
			bw.write(DataBase.schoolPeriodCount+" " +DataBase.schoolGradeCount+" "+DataBase.schoolYearDivisions);//write to file the number of periods, number of grades and the starting grade
			bw.newLine();//end line
			bw.close();//close stream
		}catch(IOException ex){//crash prevent
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Error "+ ex.toString() ,
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
		}//end catch
		System.out.println("Wrote Period/Grades file to: " + file.getName() + ".PnGC");//inform user file has been written
	}	//end writePGCFile

	public static void writeLunchFile (File file){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(file+ ".lunchpref"));//build buffered writer - set file name
			for(int indexY = 0; indexY < DataBase.schoolGradeCount; indexY++)// runs the number of grades
			{
				for (int indexX = 0; indexX < DataBase.schoolPeriodCount; indexX++)//runs number of periods
				{
					if (DataBase.lunchPrefs[indexY][indexX] == true)//if lunch period
						bw.write("1");					//write 1
					else
						bw.write("0");					//otherwise write 0 to indicate non lunch period
					bw.write(" ");//space
				}//end inner for
				bw.newLine();//next line
			}//end outer for
			bw.close();//close stream
		}catch(IOException ex){//crash prevent
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Error "+ ex.toString() ,
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
		}//end catch
		System.out.println("Wrote lunch preference file to: " + file.getName() + ".lunchpref");//inform user file has been written
	}
	public static void saveLastTemplate(File file)
	{
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(file + ".mtm"));//build buffered writer - set file name
			bw.write(file.getName());
			bw.close();
		}catch(IOException ex){//crash prevent
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Error "+ ex.toString() ,
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
		}//end catch
	}
	public static void writeRoomsFile (File file){//writes the room file

		String token ="";//String variable used to hold number of rooms
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(file+ ".rooms"));//build buffered writer - set file name
			for(int index = 0; index < DataBase.rooms.length; index++)//runs number of rooms
			{

				for(int y = 0; y < DataBase.rooms[index].numOfRooms().length; y++){
					for(int x = 0; x < DataBase.rooms[index].numOfRooms()[0].length; x++){
						if(y == 0 && x == 0)//first time looping
						{
							String s = new String(""+DataBase.rooms[index].getOriginalNumOfRooms());//gets total number of rooms
							token = token.concat(s);//held in token string
						}
						else//if not first
						{
							String s = new String(""+DataBase.rooms[index].numOfRooms()[y][x]);
							token = token.concat(", ").concat(s);
						}
					}
				}
				bw.write(DataBase.rooms[index].getType() +", " + token);//write room type and number of each room
				bw.newLine();//next line
				token ="";
			}
			bw.close();//close stream
		}catch(IOException ex){//crash prevent
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Error "+ ex.toString() ,
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
		}//end catch
		System.out.println("Wrote rooms file to: " + file.getName() + ".rooms");//inform user file has been written
	}
	public static void readTemplate(String templateName, File file) {// Reads files of a
		// template
		DataBase.projectFile = file;
		set_File(templateName + ".mtm", file);//reads the full file and puts it into the String[] file
		if (confirm_file(templateName + ".PnGC", file)//check if all files exist
				& confirm_file(templateName + ".lunchpref", file)
				& confirm_file(templateName + ".depts", file)
				& confirm_file(templateName + ".levels", file)
				& confirm_file(templateName + ".rooms", file)
				& confirm_file(templateName + ".courses", file)
				)
		{
			readPnGCFile(templateName, file);// Loads period grade and sets lunchprefs
			readLevelsFile(templateName, file);// Loads levels
			readDeptsFile(templateName, file);// Loads Departments
			readRoomsFile(templateName, file);// Loads Rooms
			readCoursesFile(templateName, file); // Loads courses


			DataBase.initializeTimeT();
			//			DataBase.isNew = false;// Moved to inside initializeTimeT
			//loadTimeTInitializers(templateName, file);
			loadTimeT(templateName, file);//loads the timetable
			Course_Manager_Main.window1.generateMasterTimeTable();
			DataBase.setTimeTCopy();
		} else {
			System.out
			.println("Unable to find all files required to load Template: "
					+ templateName);
		}
	}
	public static boolean doesFileExist(String path, String name){
		String newPath = new String(path + dash + name + ".tt");
		File file = new File(newPath);
		System.out
		.println(file.getPath());
		if(file.exists())
		{
			return true;
		}else{
			System.out
			.println("wtfffffffffffff");
			return false;
		}
	}
	public static void saveTemplate(File file)//this saves all the files required to load project
	{
		if(!DataBase.isSaveAsFile){//this checks if user wishes to replace an old project thus not requiring a new directory
			try {
				file.mkdir();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Error "+ e.toString() ,
						"ERROR",
						JOptionPane.ERROR_MESSAGE);
			}//makes a new directory
			//change flie path to the directory, which puts all files in directory
			String path = file.getPath();
			path = path.concat(dash).concat(file.getName());
			file = new File(path);
			System.out.println("wowowo");
		}
		DataBase.isSaveAsFile = false;//reset boolean
		//saving all the files
		writeCoursesFile(file);
		writeDeptsFile(file);
		writeLevelsFile(file);
		writePGCFile(file);
		writeLunchFile(file);
		writeRoomsFile(file);
		//save the template file
		saveLastTemplate(file);
		Course_Manager_Main.window1.save.setEnabled(true);//enable the save option in the file menu
		saveTimeT(file);//saves the timetable

	}
	// string array of data (must be in comma delimited format)
	// title of file
	public static void basicWrite(String[] writeable_array, String doc_title) {
		try {
			// creates a new file, filewriter, and bufferedwriter in order to
			// make a new file
			File ff = new File(doc_title);
			FileWriter fr = new FileWriter(ff);
			BufferedWriter output = new BufferedWriter(fr);

			// writes all of the array to a file
			for (int i = 0; i < writeable_array.length; i++) {
				output.write(writeable_array[i]);
				output.newLine();
			}
			// closes the writer to save the file
			output.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Error "+ e.toString() ,
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	public static String[] basicRead(String file_name) {
		// temporary string used for reading the file
		String tempStr = "";
		String lessTempStr = "";
		// string array that will be returned to the user
		String[] returnStrArray = {};

		try {

			FileReader fr = new FileReader(file_name);
			BufferedReader br = new BufferedReader(fr);

			lessTempStr = br.readLine();
			while (!(lessTempStr == null || lessTempStr.equals(""))) {
				tempStr = tempStr + lessTempStr + "%";
				lessTempStr = br.readLine();
			}

			returnStrArray = tempStr.split("%");
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Error "+ e.toString() ,
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
		}

		return returnStrArray;

	}



}
