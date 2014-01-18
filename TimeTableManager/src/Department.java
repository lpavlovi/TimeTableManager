////////////////////////////////////////////////////////////////////////////////
/* Class: Department
 * 
 * Description: Class for Department Objects. Each object holds data and method 
 * specific to its instance. Especially used to contain visibility Options
 * 
 * Authors: 
 * 	Troylan Tempra Jr Ed. William Zhao
 * 
 * Date Created:December 10, 2012
 * Date Modified: January 9, 2013
 */
////////////////////////////////////////////////////////////////////////////////
import java.awt.Color;

public class Department {
	public int rowHeight = 0;
	public int[] colVis;
	public int colWidth = 0;
	public Color foreGroundColor;
	public Color backGroundColor;
	private String name;

	public Department(String name)
	{
		this.name = name;
		colVis = new int[DataBase.levels.length];
	}
	

	public void setBackGroundColor(int red, int green, int blue)
	{//Sets cell Fill color using an RGB parameters
		backGroundColor = new Color(red, green, blue);
	}
	public void setForeGroundColor(int red, int green, int blue)
	{//Sets Text color using RGB parameters
		foreGroundColor = new Color(red, green, blue);
	}
	

	public void setBackGroundColor(Color new_Color)
	{//sets cell fill color using Color parameter
		backGroundColor = new_Color;
	}
	public void setForeGroundColor(Color new_Color)
	{//sets text color using color parameter
		foreGroundColor = new_Color;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
}
