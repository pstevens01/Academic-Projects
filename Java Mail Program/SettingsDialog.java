package mail.project;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

/**
 * 
 * @author 		Paul Stevens
 * 
 * 				Will display a separate settings frame that allows a user to
 * 				define their own SMTP settings. The settings are saved to the
 * 				'smtp.properties' file which is used to create the email object.
 *				
 */

@SuppressWarnings("serial")
public class SettingsDialog extends JFrame {

	private JPanel contentPane;
	private JTextField txtHostname;
	private JTextField txtPortnum;
	private JTextField txtUsername;
	private JTextField txtPassword;
	
	private EmailSettings configUtil;
	
	/**
	 * Create the frame.
	 */
	public SettingsDialog(JFrame parent, EmailSettings configUtil) {
		setTitle("SMTP Settings");
		this.configUtil = configUtil;
		
		loadSettings();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 386, 248);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblHost = new JLabel("Host name :");
		lblHost.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHost.setBounds(10, 11, 75, 14);
		contentPane.add(lblHost);
		
		JLabel lblPort = new JLabel("Port number :");
		lblPort.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPort.setBounds(0, 54, 85, 14);
		contentPane.add(lblPort);
		
		JLabel lblUser = new JLabel("Username :");
		lblUser.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUser.setBounds(10, 97, 75, 14);
		contentPane.add(lblUser);
		
		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPassword.setBounds(10, 140, 75, 14);
		contentPane.add(lblPassword);
		
		txtHostname = new JTextField();
		txtHostname.setBounds(106, 8, 254, 20);
		contentPane.add(txtHostname);
		txtHostname.setColumns(10);
		
		txtPortnum = new JTextField();
		txtPortnum.setBounds(106, 51, 254, 20);
		contentPane.add(txtPortnum);
		txtPortnum.setColumns(10);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(106, 94, 254, 20);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		txtPassword = new JTextField();
		txtPassword.setBounds(106, 137, 254, 20);
		contentPane.add(txtPassword);
		txtPassword.setColumns(10);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(64, 175, 89, 23);
		contentPane.add(btnSave);
		btnSave.addActionListener(new ActionListener() {
			//Action event on the 'Save' button which will call custom actionPerformed method
			@Override
			public void actionPerformed(ActionEvent event) {
				btnSaveActionPerformed(event);
			}
		});
		
		//Close frame action on the 'Cancel' button
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(e -> this.dispose());

		btnCancel.setBounds(217, 175, 89, 23);
		contentPane.add(btnCancel);
	}
	
	//Gets text from each JTextField and saves them to the EMailSettings class object when
	//when the 'Save' button is pressed
	private void btnSaveActionPerformed(ActionEvent event) {
		try {
			configUtil.saveProperties(txtHostname.getText(), txtPortnum.getText(), txtUsername.getText(), 
					txtPassword.getText());
			JOptionPane.showMessageDialog(SettingsDialog.this, "Properties were saved successfully!");
			dispose();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "Error saving properties file: " + ex.getMessage(), "Error", 
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	//Loads settings from 'smtp.properties' file to a 'configUtil' EmaiSettings class object
	private void loadSettings() {
		try {
			configUtil.loadProperties();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "Error reading settings: " + ex.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
