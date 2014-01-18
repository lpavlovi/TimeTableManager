////////////////////////////////////////////////////////////////////////////////
/* Class: Grade
 * 
 * Class: Class for Grade Objects. Each object holds data and method specific
 * to its instance
 * 
 * Authors: 
 * 	 William Zhao
 * 
 * Date Created:December 18, 2012
 * Date Modified: January 9, 2013
 */
////////////////////////////////////////////////////////////////////////////////
public class Grade {
	private int gradeNum;
	private boolean[] lunchPref;
	public Grade(int gradeNum, boolean[] lunchPref, int periods)
	{
		this.gradeNum = gradeNum;
		this.lunchPref = new boolean[periods];
		this.lunchPref = lunchPref;
	}
	public boolean[] getLunchPref()
	{
		return lunchPref;
	}
	public int getGradeNum()
	{
		return gradeNum;
	}
}