////////////////////////////////////////////////////////////////////////////////
/* Class: Transfer Handler
 * 
 * Description: Drag and Drop Handler
 * 
 * Authors: 
 * 	Troylan Tempra Jr.
 * 
 * Date Created:December 13, 2012
 * Date Modified: January 6, 2013
 */
////////////////////////////////////////////////////////////////////////////////
import java.awt.datatransfer.*;

import javax.swing.*;
import javax.swing.text.*;

class TableTransferHandler extends TransferHandler {
	private static final long serialVersionUID = -8306880930476375158L;//TODO:
	Position p0 = null, p1 = null;
	int deptPos = -1;//indicates the number of the department
	int semFROM = -1;//0 or 1 value that indicates which semester is the drag from
	int semTO = -1; //0 or 1 value the indicates which semester the drag is to
	int timeTxCoordinateFROM = -1;//3rd dimension coordinate for timeT[], original location of data
	int timeTxCoordinateTO = -1;//3rd dimension coordinate for timeT[], destination of data
	//^The integers above are used as coordinates to access the timeT array
	private JTable tableFROM = new JTable();//table where drag is from
	private JTable tableTO = new JTable();//table where drag is to
	private int rowFROM = 0;//original row index of data
	private int colFROM = 0;//original col index of data
	private int rowTO = 0;//destination row index of  data
	private int colTO = 0;//destination col index of data

    public boolean importData(TransferHandler.TransferSupport support) {
    	//check if import type is valid
        if (!canImport(support)) {
            return false;
        }
        //find data
        String data;
        try {
            data = (String)support.getTransferable().getTransferData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException e) {
            return false;
        } catch (java.io.IOException e) {
            return false;
        }
        tableTO = (JTable)support.getComponent();//allows table access
        //rowFROM has already been assigned
        rowTO = tableTO.getSelectedRow();
        colTO = tableTO.getSelectedColumn();
        
        //The following 2 integers are to check for DnD validity
        int levelNum = DataBase.findLevelPos(DataBase.findCourse(data).level);
        //int gradeNum = DataBase.findCourse(data).getGrade();
        
        //The following integers are to access correct parts of the timeT array
        //semFROM has already been assigned
        semTO = Course_Manager_Main.window1.last_selected_table;//set semTO
        System.out.println("semFROM: " + semFROM + "semTO: " + semTO);
        System.out.println("deptPos: " + deptPos + "rowFrom: " + tableFROM.getSelectedRow() + "");
//        System.out.println(levelNum + ": " + DataBase.colCountPerLevel + ": " + colTO + "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
        System.out.println(rowTO + ": " + DataBase.schoolGradeCount + ": "  //+ gradeNum
        					);
        if((colTO == 0 | colTO == 1))
        {//Trash data dragged to grade/period column
        	//data is trashed
        	tableFROM.setValueAt(null, rowFROM, colFROM);
        	DataBase.timeT[deptPos][rowFROM][timeTxCoordinateFROM][semFROM] = null;
        	System.out.println("Data DELETION occured!");
        	DataBase.findCourse(data);//TODO: update room/placement status
        }
        else if(((levelNum*DataBase.colCountPerLevel)+1<colTO & colTO<levelNum*DataBase.colCountPerLevel+2+DataBase.colCountPerLevel)//Disallows DnD to invalid level
        		// &(rowTO%DataBase.schoolGradeCount == gradeNum)//!(Disallows DnD to invalid grade) Now rescans for validity instead  
        		)
        {
        	if(tableTO.getValueAt(rowTO, colTO) == null)
        	{//Replace null if nothing is at the destination
        		tableFROM.setValueAt(null, rowFROM, colFROM);
        		DataBase.timeT[deptPos][rowFROM][timeTxCoordinateFROM][semFROM] = null;
        		for(int index = 0; index<DataBase.timeT[deptPos][rowTO].length; index++)
                {//one-dimensionally iterates through a part of timeT array and
                	if(DataBase.timeT[deptPos][rowTO][index][semTO]==null)
                	{//locates first null value
                		timeTxCoordinateTO = index;
                		index = DataBase.timeT[deptPos].length;
                	}
                }
        		tableTO.setValueAt(data, rowTO, colTO);
        		System.out.println("Data TRANSFER occured!");
        		DataBase.timeT[deptPos][rowTO][timeTxCoordinateTO][semTO] = DataBase.findCourse(data);
        		System.out.println(data + "** " + DataBase.findCourse(data));
        	}
        	else
        	{//Swap data if not null
        		for(int index = 0; index<DataBase.timeT[deptPos][rowTO].length; index++)
                {//one-dimensionally iterates through a part of timeT array and
                	if(DataBase.timeT[deptPos][rowTO][index][semTO].getName().equals(
                			DataBase.findCourse((String)tableTO.getValueAt(rowTO, colTO)).getName()))
                	{//locates first section with appropriate courseName value
                		timeTxCoordinateTO = index;
                		index = DataBase.timeT[deptPos].length;
                	}
                }
        		DataBase.timeT[deptPos][rowFROM][timeTxCoordinateFROM][semFROM] = DataBase.findCourse((String)tableTO.getValueAt(rowTO, colTO));
        		tableFROM.setValueAt(tableTO.getValueAt(rowTO, colTO), rowFROM, colFROM);
        		DataBase.timeT[deptPos][rowTO][timeTxCoordinateTO][semTO] = DataBase.findCourse(data);
        		tableTO.setValueAt(data, rowTO, colTO);
        		System.out.println("Data SWAP occured!");
        	}
        }
        else
        {//if data transfer is not successful due to conditions from directly above, data is returned.
        	if(data!=null)//Data is not returned if null (this occurs when dragging from col 0 or col 1)
        	{
        		DataBase.timeT[deptPos][rowFROM][timeTxCoordinateFROM][semFROM] = DataBase.findCourse(data);
        		tableFROM.setValueAt(data, rowFROM, colFROM);
        		System.out.println("DnD failure, data RETURNED to original position!");
        	}
        }
        Course_Manager_Main.window1.refreshDisplay();
        return true;
    }
    
    protected Transferable createTransferable(JComponent c) {
        
        semFROM = -1;
        //Set some FROM that are required to find proper transferable data
        deptPos = DataBase.findDeptPos(DataBase.selected_Department);
        tableFROM = (JTable)c;
        rowFROM = tableFROM.getSelectedRow();
        colFROM = tableFROM.getSelectedColumn();
        semFROM = Course_Manager_Main.window1.last_selected_table;
        
        String data = "";
        
        if(tableFROM.getSelectedColumn() == 0 | tableFROM.getSelectedColumn() == 1){
        	data = null;
        }//Prevents changing/taking of data from column 0 and 1
        else{
        	data = (String)(tableFROM.getValueAt(rowFROM, colFROM));
        }
        
        //set other timeTxCoordinateFROM (this variable requires data to be properly defined)
        for(int index = 0; index<DataBase.timeT[deptPos][rowFROM].length; index++)
        {
        	//System.out.println("deptPos: " + deptPos + " rowFROM: " + rowFROM +" index: " + index + " semFROM: " + semFROM);
        	if(DataBase.timeT[deptPos][rowFROM][index][semFROM]!=null)
        		System.out.println(DataBase.timeT[deptPos][rowFROM][index][semFROM].getName());
        	if(DataBase.timeT[deptPos][rowFROM][index][semFROM]!=null &&
        			DataBase.timeT[deptPos][rowFROM][index][semFROM].getName().equals(data))
        	{//locates last matching value
        		timeTxCoordinateFROM = index;
        	}
        }
       // System.out.println("deptPos: " + deptPos + " rowFROM: " + rowFROM +" timeTxCoordinateFROM: " + timeTxCoordinateFROM + " semFROM: " + semFROM);
        System.out.println(data);
        return new StringSelection(data);
    }

    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }


    protected void exportDone(JComponent c, Transferable data, int action) {
        if (action != MOVE) {
            return;
        }
        if ((p0 != null) && (p1 != null) &&
            (p0.getOffset() != p1.getOffset())) {
            try {
                JTextComponent tc = (JTextComponent)c;
                tc.getDocument().remove(p0.getOffset(), 
                        p1.getOffset() - p0.getOffset());
            } catch (BadLocationException e) {
                System.out.println("Can't remove text from source.");
            }
        }
    }
    public boolean canImport(TransferHandler.TransferSupport support) {
        //import Strings only
        if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return false;
        }
        return true;
    }
}
