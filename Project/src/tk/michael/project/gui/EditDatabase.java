package tk.michael.project.gui;

import net.miginfocom.swing.MigLayout;
import tk.michael.project.connecter.ConnectedDb;
import tk.michael.project.util.Database;
import tk.michael.project.util.DatabaseHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * Created By: Michael Risher
 * Date: 4/25/15
 * Time: 2:40 AM
 */
public class EditDatabase extends BasicFrameObject implements ActionListener {

	private HashMap< String, RegexTextField > textFields = new HashMap<>();
	private HashMap< String, MLabel > labels = new HashMap<>();
	private Database db;

	public EditDatabase( Database db ) {
		super( "Editing "  + db.getName() );
		this.db = db;
		init();
	}

	@Override
	void init() {
		JPanel panel = new JPanel( new MigLayout(  ) );

		labels.put( "name", new MLabel( "Name:" ) );
		textFields.put( "name", new RegexTextField( db.getName(), 20, "notempty" ) );

		labels.put( "host", new MLabel( "Host:" ) );
		textFields.put( "host", new RegexTextField( db.getHost(), 11, "notempty" ) );

		labels.put( "port", new MLabel( "Port:" ) );
		textFields.put( "port", new RegexTextField( db.getPort(), 5, "^0*(110,?000|((10[0-9]|[1-9][0-9]|[1-9]),?[0-9]{3})|([1-9][0-9]{2}|[1-9][0-9]|[1-9]))$" ) );

		labels.put( "user", new MLabel( "Username:" ) );
		textFields.put( "user", new RegexTextField( db.getUsername(), 20, "notempty" ) );

		labels.put( "pass", new MLabel( "Password:" ) );
		textFields.put( "pass", new RegexTextField( db.getPassword(), 20, null ) );

		labels.put( "database", new MLabel( "Database:" ) );
		textFields.put( "database", new RegexTextField( db.getDatabaseName(), 20, null ) );
		textFields.get( "database" ).setToolTipText( "Default database to use" );

		JButton confirm = new JButton( "OK" );
		confirm.setActionCommand( "create" );
		confirm.addActionListener( this );

		JButton cancel = new JButton( "Cancel" );
		cancel.setActionCommand( "cancel" );
		cancel.addActionListener( this );

		JButton testConnection = new JButton( "Test Connection" );
		testConnection.setActionCommand( "test" );
		testConnection.addActionListener( this );

		panel.add( labels.get( "name" ) );
		panel.add( textFields.get( "name" ), "wrap" );
		panel.add( labels.get( "host" ) );
		panel.add( textFields.get( "host" ), "split 3" );
		panel.add( labels.get( "port" ) );
		panel.add( textFields.get( "port" ), "wrap" );
		panel.add( labels.get( "user" ) );
		panel.add( textFields.get( "user" ), "wrap" );
		panel.add( labels.get( "pass" ) );
		panel.add( textFields.get( "pass" ), "wrap" );
		panel.add( labels.get( "database" ), "gaptop 20px" );
		panel.add( textFields.get( "database" ), "wrap" );

		panel.add( confirm, "skip, split 3" );
		panel.add( cancel, "" );
		panel.add( testConnection, "wrap" );

		frame.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
		frame.add( panel );
		frame.pack();
		frame.setLocationRelativeTo( null );
	}

	@Override
	void initMenu() {
		//no menus
	}

	@Override
	public void actionPerformed( ActionEvent e ) {
		String action = e.getActionCommand();

		if ( action.equals( "cancel" ) ) {
			frame.dispose();
		}

		if ( action.equals( "create" ) ) {
			String msg = "<html><p>Fix the following errors</p><ul>";
			boolean ok = true;
			String[] fields = { "name", "host", "user" };

			for ( int i = 0; i < fields.length; i++ ){
				if ( !textFields.get( fields[i] ).matches() ) {
					ok = false;
					msg += "<li>Fill in the " + fields[i] + " field</li>";
				}
			}

			if ( !textFields.get( "port" ).matches() ) {
				ok = false;
				msg += "<li>The port entered was invalid</li>";
			}

			if ( ok ) {
				db.setName( textFields.get( "name" ).getText() );
				db.setHost( textFields.get( "host" ).getText() );
				db.setPort( textFields.get( "port" ).getText() );
				db.setUsername( textFields.get( "user" ).getText() );
				db.setPassword( textFields.get( "pass" ).getText() );
				db.setDatabaseName( textFields.get( "database" ).getText() );

				DatabaseHandler.editDatabase( db );
				frame.dispose();
			}
			else{
				JOptionPane.showMessageDialog( frame, "The port you entered is invalid" );
			}
		}

		if ( action.equals( "test" ) ) {
			//todo figure out the code for this
			Database db = new Database(
				textFields.get( "name" ).getText(),
				textFields.get( "host" ).getText(),
				textFields.get( "port" ).getText(),
				textFields.get( "user" ).getText(),
				textFields.get( "pass" ).getText(),
				textFields.get(  "database" ).getText()
			);

			boolean state = ConnectedDb.testConnection( db.getUrl(), db.getUsername(), db.getPassword() );
			String message = "Connected successfully!";
			if ( !state ) {
				message = "Unable to connect.";
			}

			JOptionPane.showMessageDialog( frame, message );
		}
	}
}