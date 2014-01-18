////////////////////////////////////////////////////////////////////////////////
/* Class: Course
 * 
 * Descriptions: Class for Course Objects. Each object holds data and method 
 * specific to its instance. Especially used to contain course statuses
 * 
 * Authors: 
 * 	Troylan Tempra Jr Ed. William Zhao
 * 
 * Date Created:December 10, 2012
 * Date Modified: January 9, 2013
 */
////////////////////////////////////////////////////////////////////////////////

public class Course {
	public String name;
	public Department department;
	public String level;
	public boolean[] perPref = { true, true, true, true, true };
	public int grade;
	public int remainingSectionCount;
	public int existingSectionCount;
	public String roomType;

	public Course(String name, Department department, String level, int grade,
			String roomType, int sectionCount) {// Constructor, requires a
												// specified name, sectionCount,
												// department number and level
												// number to be denoted
												// and converts them to its own
												// private variables.
		this.name = name;
		this.department = department;
		this.level = level;
		this.grade = grade;
		this.remainingSectionCount = sectionCount;
		this.existingSectionCount = sectionCount;
		this.roomType = roomType;
	}

	public boolean equals(Course instance_of_course) {// Compares the name of
														// this course and a
														// different, specified
														// course, if they are
														// the same, return true
		if (this.name.equals(instance_of_course.name))
			return true;
		else
			return false;
	}

	public void setPerPref(int period, boolean setting){
		perPref[period] = setting;
	}
	public boolean getPerPref(int period){
		return perPref[period];
	}
	public boolean[] getPerPrefArray(){
		return perPref;
	}
	public int getGrade() {
		return grade;
	}
	public String getName()
	{
		return name;
	}

	public void setRemainingSectionCount(int value) {
		remainingSectionCount = value;
	}
	public void subtractFromRemainingSectionCount(int value)
	{
		remainingSectionCount = remainingSectionCount-value;
	}

	public int getRemainingSectionCount() {
//		System.out.println(name + " returned: " + remainingSectionCount + " as remainingSectionCount");
		return remainingSectionCount;
	}
	public int getExistingSectionCount() {
		return existingSectionCount;
	}

	public String getLevel() {
		return level;
	}
	public Department getDept()
	{
		return department;
	}
	
	public String getRoomType()
	{
		return roomType;
	}

}
