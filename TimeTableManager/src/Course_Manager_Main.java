////////////////////////////////////////////////////////////////////////////////
/* Program: Course Manager
 * Description: This Program is designed to aid in the course scheduling work of
 * high schools.
 * 
 * Class: Main/Runner Class
 * Description: Initiates the program, specifies GUI Look and Feel if possible
 * and opens the first window
 * 
 * Authors: 
 * 	Holden Cuifo
 * 	John Kwon
 * 	Luka Pavlovic
 * 	Troylan Tempra Jr
 * 	William Zhao
 * 
 * Date Created: December 6, 2012
 * Date Modified: January 20, 2013
 */
////////////////////////////////////////////////////////////////////////////////
import java.awt.EventQueue;

public class Course_Manager_Main {
	
	public static Course_Manager_Window1 window1;
	
	public static void main(String[] args) {
		try {// Set GUI to Nimbus
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
					.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
				}
			}
		} catch (Exception ex) {
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window1 = new Course_Manager_Window1();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}