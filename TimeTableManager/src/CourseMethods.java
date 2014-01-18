/*
 * Program: Course Manager
 * Authors: William Zhao, Holden Ciufo
 * Date: 29/11/2012
 * Description:
 */
public class CourseMethods {
	private int y, sem =0;//varaible used for the y axis of timetable and for the semester index
	private int counter;//counter used to find min
	private int period;//variabled used as period index
	private boolean flip = true;
	//two arrays used as counters for the time talbes's period and rows
	private int[][][][] timeTCounter = new int[DataBase.timeT.length][DataBase.timeT[0].length][1][DataBase.timeT[0][0][0].length];
	private int[][][][][] timeTperiodCounter = new int[DataBase.schoolGradeCount][DataBase.timeT.length][DataBase.schoolPeriodCount][1][DataBase.timeT[0][0][0].length];
	private int[][] timeTsemCounter = new int[DataBase.timeT.length][DataBase.timeT[0][0][0].length];//counter for the semester
	private void initializetimeTCounter(){//resets the two counter arrays to 0 with nested loops
		for(int a = 0; a < timeTCounter.length; a++){
			for(int b = 0; b < timeTCounter[0].length; b++){
				for(int c = 0; c < timeTCounter[0][0].length; c++){
					for(int d = 0; d < timeTCounter[0][0][0].length; d++){
						timeTCounter[a][b][c][d] = 0;
					}
				}
			}
		}
		
	}
	private void initializeCounter(){
		for(int a = 0; a < timeTperiodCounter.length; a++){
			for(int b = 0; b < timeTperiodCounter[0].length; b++){
				for(int c = 0; c < timeTperiodCounter[0][0].length; c++){
					for(int d = 0; d < timeTperiodCounter[0][0][0].length; d++){
						for(int e = 0; e < timeTperiodCounter[0][0][0][0].length; e++){
							timeTperiodCounter[a][b][c][d][e] = 0;
						}

					}
				}
			}
		}
		for(int a = 0; a < timeTsemCounter.length; a++){
			for(int b = 0; b < timeTsemCounter[0].length; b++){
				timeTsemCounter[a][b] = 0;
			}
		}
	}
	public CourseMethods(int numberOfDepartments, int numberOfRowsPerDept,
			int numberOfColsPerDept, int numberOfSemesters) {
		// reset timeT array
		DataBase.timeT = new Course[numberOfDepartments][numberOfRowsPerDept][numberOfColsPerDept][numberOfSemesters];
		DataBase.timeTcopy = new Course[numberOfDepartments][numberOfRowsPerDept][numberOfColsPerDept][numberOfSemesters];
		DataBase.coursenum = new int[2][DataBase.schoolPeriodCount];
		for (int index = 0; index < DataBase.rooms.length; index++) {// Reset
			// Room
			// availability
			DataBase.rooms[index].resetAvailability();
		}
	}

	public boolean isAllPeriodRestric(Course course) {// check if the the course
		// has period
		// restriction for every
		// period

		boolean isAllPeriodRestric = false;
		int counter = 0;
		for (int i = 0; i < course.perPref.length; i++) {
			if (course.getPerPref(i) == false) {
				counter++;
			}
		}
		if (counter == 5) {
			isAllPeriodRestric = true;
		}
		return isAllPeriodRestric;
	}

	public void manageCourse() {//this method is the helper method for the recursive method setCourses it feed the course into that method
		initializeCounter();
		for (int a = 0; a < DataBase.departments.length; a++) {
			initializetimeTCounter();
			for (int b = 0; b < DataBase.courses.length; b++) {
				if (!isAllPeriodRestric(DataBase.courses[b])) {
					if (DataBase.courses[b].department
							.equals(DataBase.departments[a])
							&& DataBase.courses[b].getRemainingSectionCount() > 1) {

						setCourses(a, DataBase.courses[b],//calling recursive method
								DataBase.courses[b].remainingSectionCount,
								DataBase.findRoom(DataBase.courses[b].roomType)
								.numOfRooms(),
								DataBase.grades[DataBase.courses[b].getGrade()]
										.getLunchPref(),
										DataBase.courses[b].grade);
					}
				}
			}
		}
	}
	private void setYSemA(int w, int daperiod, int grade, int z, boolean[] lunchPref, Course course, int[][] numOfRoom){//this method finds the best y axis for the timetable to put course in
//looks from first to last
		int x = (4 * daperiod) + grade;//a tester for possible y axis
		if(daperiod == (DataBase.schoolPeriodCount-1)){//base case
			flip =false;//flag
			if(timeTperiodCounter[grade][w][daperiod][0][z] < counter && lunchPref[daperiod] == false && course.getPerPref(daperiod) == true && numOfRoom[sem][daperiod] > 0){
				counter = timeTperiodCounter[grade][w][daperiod][0][z];
				y = x;
				sem = z;
				period = daperiod;
			}else if(lunchPref[daperiod] == true && course.getPerPref(daperiod) == true && numOfRoom[sem][daperiod] > 0){
				if(timeTCounter[w][x][0][z] == 0){
					counter = timeTCounter[w][x][0][z];
					y = x;
					sem = z;
					period = daperiod;
				}
			}
		}else{//checks for the period with least amount of course placed check for period restriction and room availability
			System.out.println("lunchPref.length:"+lunchPref.length + "daperiod: " + daperiod + "numOfRoom: " + numOfRoom.length + "sem: " + sem + "numOfRoom[0]: " + numOfRoom[0].length);
			if(timeTperiodCounter[grade][w][daperiod][0][z] < counter && lunchPref[daperiod] == false && course.getPerPref(daperiod) == true && numOfRoom[sem][daperiod] > 0){
				counter = timeTperiodCounter[grade][w][daperiod][0][z];
				y = x;//sets y
				sem = z;
				period = daperiod;
			}else if(lunchPref[daperiod] == true && course.getPerPref(daperiod) == true && numOfRoom[sem][daperiod] > 0){//if there is lunchpreference
				if(timeTCounter[w][x][0][z] == 0){//allows only one course to be placed per row
					counter = timeTCounter[w][x][0][z];
					y = x;
					sem = z;
					period = daperiod;
				}
			}
			daperiod++;
			setYSemA(w, daperiod, grade, z,lunchPref, course, numOfRoom);//recurive call
		}
	}
	private void setYSemB(int w, int daperiod, int grade, int z, boolean[] lunchPref, Course course, int[][] numOfRoom){//this method finds the best y axis for the timetable to put course in
//looks from last to first
		int x = (4 * daperiod) + grade;//a tester for possible y axis
		if(daperiod == 0){//base case
			flip =true;//flag
			if(timeTperiodCounter[grade][w][daperiod][0][z] < counter && lunchPref[daperiod] == false && course.getPerPref(daperiod) == true && numOfRoom[sem][daperiod] > 0){
				counter = timeTperiodCounter[grade][w][daperiod][0][z];
				y = x;
				sem = z;
				period = daperiod;
			}else if(lunchPref[daperiod] == true && course.getPerPref(daperiod) == true && numOfRoom[sem][daperiod] > 0){
				if(timeTCounter[w][x][0][z] == 0){
					counter = timeTCounter[w][x][0][z];
					y = x;
					sem = z;
					period = daperiod;
				}
			}
		}else{//checks for the period with least amount of course placed check for period restriction and room availability
			if(timeTperiodCounter[grade][w][daperiod][0][z] < counter && lunchPref[daperiod] == false && course.getPerPref(daperiod) == true && numOfRoom[sem][daperiod] > 0){
				counter = timeTperiodCounter[grade][w][daperiod][0][z];
				y = x;//sets y
				sem = z;
				period = daperiod;
			}else if(lunchPref[daperiod] == true && course.getPerPref(daperiod) == true && numOfRoom[sem][daperiod] > 0){//if there is lunchpreference
				if(timeTCounter[w][x][0][z] == 0){//allows only one course to be placed per row
					counter = timeTCounter[w][x][0][z];
					y = x;
					sem = z;
					period = daperiod;
				}
			}
			daperiod--;
			setYSemB(w, daperiod, grade, z,lunchPref, course, numOfRoom);//recurive call
		}
	}
	private void setCourses(int department, Course course,//this method creates the time table
			int sections, int[][] numOfRoom, boolean[] lunchPref,
			int grade) {
		counter = 1000;//counter used to find min
		//code below is used to find the the sem with lowest amount of course placed
		int min = timeTsemCounter[department][0];
		int m = 0;
		System.out.println("**************************************************"+timeTsemCounter[0].length);
		System.out.println(":" + DataBase.schoolYearDivisions);
		for(int i = 0; i< timeTsemCounter[0].length; i++){
			if(timeTsemCounter[department][i] < min){
				min = timeTsemCounter[department][i];
				m = i;
			}
		}
		if(flip){//flip used to switch from looking from first period to last to last period to first
			setYSemA(department,0,grade, m,lunchPref, course, numOfRoom);//finds best y axis for a course to be placed in
		}else{
			setYSemB(department,(DataBase.schoolPeriodCount-1),grade, m,lunchPref, course, numOfRoom);//finds best y axis for a course to be placed in
		}
		if (sections == 0 || numOfRoom[sem][period] == 0)// base case ends
			// recursion
		{// Done placing sections

		} else {

			DataBase.timeT[department][y][timeTCounter[department][y][0][sem]][sem] = course;//setting course into array
			timeTCounter[department][y][0][sem]++;
			timeTperiodCounter[grade][department][period][0][sem]++;
			timeTsemCounter[department][sem]++;
			numOfRoom[sem][period] -= 1;// decrease room #s
			sections--;// decrease # of sections
			DataBase.coursenum[sem][period]++;
			setCourses(department, course, sections, numOfRoom, lunchPref,//recursive call
					grade);
		}

	}
}