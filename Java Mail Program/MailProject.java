package mail.project;

import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import javax.mail.NoSuchProviderException;


/**
 * 
 * @author 		Paul Stevens
 * 
 * 				Java email program using Swing GUI components that can send an email with an
 * 				attachment using SMTP protocol and Javax Mail API libraries. The program will
 * 				also let a user check their most recent unread messages using the same libraries
 * 				and IMAP protocol.
 *
 */

@SuppressWarnings("serial")
public class MailProject extends JFrame {

	private JPanel contentPane;
	private JTextField txtTo;
	private JTextField txtSubject;
	private JTextField txtFile;
	private JTextArea textBody;
	private File selectedFile;
	
	private EmailSettings configUtil = new EmailSettings();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MailProject frame = new MailProject();
					frame.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MailProject() {
		setTitle("Java Mail Project");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 535);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTo = new JLabel("To :");
		lblTo.setBounds(69, 11, 25, 14);
		contentPane.add(lblTo);
		
		JLabel lblSubject = new JLabel("Subject :");
		lblSubject.setBounds(42, 36, 58, 14);
		contentPane.add(lblSubject);
		
		JLabel lblFile = new JLabel("Attachment :");
		lblFile.setBounds(18, 61, 82, 14);
		contentPane.add(lblFile);
		
		txtTo = new JTextField();
		txtTo.setBounds(102, 8, 343, 20);
		contentPane.add(txtTo);
		txtTo.setColumns(10);
		
		txtSubject = new JTextField();
		txtSubject.setBounds(102, 33, 343, 20);
		contentPane.add(txtSubject);
		txtSubject.setColumns(10);
		
		txtFile = new JTextField();
		txtFile.setBounds(102, 58, 343, 20);
		contentPane.add(txtFile);
		txtFile.setColumns(10);
		
		textBody = new JTextArea();
		textBody.setBounds(10, 86, 604, 365);
		contentPane.add(textBody);
		
		//Allows the user to attach a file to the email
		JButton btnFile = new JButton("Attach File ...");
		btnFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				//Defaults to a image filter but the user can choose any type of file to send
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif");
				fc.setFileFilter(filter);
				fc.showDialog(btnFile, "Choose File");
				String strPath = txtFile.getText() + "\n" + fc.getSelectedFile().toString();
				//Outputs the file selected to a JTextField
				txtFile.setText(strPath);
				selectedFile = fc.getSelectedFile();
			}
		});
		btnFile.setBounds(455, 57, 112, 23);
		contentPane.add(btnFile);
		
		JButton btnSend = new JButton("Send");
		btnSend.setBounds(69, 462, 112, 23);
		contentPane.add(btnSend);
		btnSend.addActionListener(new ActionListener() {
			//Action event on the 'Send' button which will call custom actionPerformed method
            @Override
            public void actionPerformed(ActionEvent event) {
                btnSendActionPerformed(event);
            }
        });
		
		JButton btnSettings = new JButton("Mail Settings");
		btnSettings.addActionListener(new ActionListener() {
			//Opens the 'SettingsDialog' window to allow for user-defined SMTP settings
			public void actionPerformed(ActionEvent e) {
				SettingsDialog settings = new SettingsDialog(MailProject.this, configUtil);
				settings.setVisible(true);
			}
		});
		btnSettings.setBounds(455, 7, 112, 23);
		contentPane.add(btnSettings);
		
		JButton btnCheckMail = new JButton("Check Email");
		btnCheckMail.addActionListener(new ActionListener() {
			//Opens the 'CheckDialog' so the user can then enter their IMAP settings to check emails
			public void actionPerformed(ActionEvent e) {
				try {
					CheckDialog check;
					check = new CheckDialog(MailProject.this);
					check.setVisible(true);
				} catch (NoSuchProviderException ex) {
					ex.printStackTrace();
				}
			}
		});
		btnCheckMail.setBounds(253, 462, 112, 23);
		contentPane.add(btnCheckMail);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			//'Exit' button closes the program
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(437, 462, 112, 23);
		contentPane.add(btnExit);
	}
	
	//Retrieves address, subject, and email body from JTextFields passing them to the sendMail method
	//defined in the 'CreateEmail' class which ultimately sends the email.
	private void btnSendActionPerformed(ActionEvent event) {
		if (!validateFields()) {
			return;
		}
		String toAddress = txtTo.getText();
		String subject = txtSubject.getText();
		String message = textBody.getText();
				
		try {
			Properties smtpProps = configUtil.loadProperties();
			CreateEmail.sendEmail(smtpProps, toAddress, subject, message, selectedFile);
			JOptionPane.showMessageDialog(this, "The e-mail has been sent successfully!");
		} catch (Exception ex) {
			JOptionPane.showConfirmDialog(this, "Error while sending the e-mail: " + ex.getMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	//Validation for the address, subject, and email body fields so that there are no null fields
	private boolean validateFields() {
        if (txtTo.getText().equals("")) {
            JOptionPane.showMessageDialog(this,
                    "Please enter To address!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            txtTo.requestFocus();
            return false;
        }
         
        if (txtSubject.getText().equals("")) {
            JOptionPane.showMessageDialog(this,
                    "Please enter subject!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            txtSubject.requestFocus();
            return false;
        }
         
        if (textBody.getText().equals("")) {
            JOptionPane.showMessageDialog(this,
                    "Please enter message!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            textBody.requestFocus();
            return false;
        }   
        return true;
    }
}