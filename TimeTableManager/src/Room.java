////////////////////////////////////////////////////////////////////////////////
/* Class: Room
 * 
 * Description: Class for Room Objects. Each object holds data and method 
 * specific to its instance. Especially used to conain room statuses
 * 
 * Authors: 
 * 	Troylan Tempra Jr Ed. William Zhao, Luka Pavlovic
 * 
 * Date Created:December 11, 2012
 * Date Modified: January 14, 2013
 */
////////////////////////////////////////////////////////////////////////////////

public class Room {
	public int[][] numberOfRooms;
	public int originalNumOfRooms;
	private String type;
	public Room(String type, int numberOfRooms, int numOfPeriods)
	{
		this.numberOfRooms = new int[2][numOfPeriods];//hardcoded stuff!!!!!!!!!!!!
		for(int i = 0; i<this.numberOfRooms[0].length; i++){
			this.numberOfRooms[0][i] = numberOfRooms;
			this.numberOfRooms[1][i] = numberOfRooms;
		}
		originalNumOfRooms = numberOfRooms;
		this.type = type;
	}
	public int[][] numOfRooms()
	{
		return numberOfRooms;
	}
//	public int numOfRoomsSpec(int i, int j)
//	{//Obsolete due to above method being usable as numOfRooms()[i][j], similarly
//		return numberOfRooms[i][j];
//	}
	public int getOriginalNumOfRooms()
	{
		return originalNumOfRooms;
	}
	public void holdSectionAt(int semester,int period)
	{
		numberOfRooms[semester][period]-=1;
	}
	public String getType()
	{
		return type;
	}
	public void setAvailability(int[][] availability)
	{//used when reading from a saved flat file, sets the number of rooms[]
		for (int semester = 0; semester<2; semester++)//HardCoded stuff
		{
			for(int period = 0; period<DataBase.schoolPeriodCount; period++)
			{
				numberOfRooms[semester][period] = availability[semester][period];
			}
		}
	}
	public void resetAvailability()
	{
		for (int semester = 0; semester<2; semester++)//HardCoded stuff
		{
			for(int period = 0; period<DataBase.schoolPeriodCount; period++)
			{
				numberOfRooms[semester][period] = originalNumOfRooms;
			}
		}
	}
}
