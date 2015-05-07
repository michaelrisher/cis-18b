package tk.michael.project;

import com.michael.api.IO.IO;
import com.michael.api.security.AES;
import tk.michael.project.gui.AddDatabase;
import tk.michael.project.gui.ConnectedWindow;
import tk.michael.project.gui.DirectoryChooser;
import tk.michael.project.gui.MainWindow;
import tk.michael.project.util.DatabaseHandler;

import javax.swing.*;
import java.util.Arrays;

/**
 * Created By: Michael Risher
 * Date: 4/22/15
 * Time: 4:02 PM
 */
public class Main {

	private static boolean debugView  = false;

	public static void main( String[] args ) throws Exception{
		if ( Arrays.asList( args ).contains( "debugview".toLowerCase() ) ) {
			debugView = true;
		}

//		new Test();

//		new TreeExample();

		try {
			for ( UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels() ){
				if ( "Nimbus".equals( info.getName() ) ) {
					UIManager.setLookAndFeel( info.getClassName() );
					break;
				}
			}
		}
		catch ( Exception e) {
			// handle exception
		}

		DatabaseHandler.load();
//		ConnectedWindow cw = new ConnectedWindow( DatabaseHandler.getDatabases().get( 0 ).getId() );
//		cw.display();
		MainWindow.GetInstance().display();
	}

	public static boolean isDebugView() {
		return debugView;
	}
}