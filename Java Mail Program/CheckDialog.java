package mail.project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;

/**
 * 
 * @author 		Paul Stevens
 * 
 * 				The CheckDialog class opens a new full-sized frame with different IMAP
 * 				setting fields for user-defined settings that are used to access an inbox
 * 				using IMAP. 
 *
 */

@SuppressWarnings("serial")
public class CheckDialog extends JFrame {

	private JPanel contentPane;
	private JTextField txtImapServer;
	private JTextField txtPortnum;
	private JTextField txtUsername;
	private JTextField txtPassword;
	private Folder inbox;
	private Message[] messages;


	/**
	 * Create the frame. 
	 */
	public CheckDialog(JFrame parent) throws NoSuchProviderException {
		setTitle("Check Emails");
		//Creates the session and message store objects with specified properties and protocol
		Session checkSession = Session.getDefaultInstance(new Properties());
		Store checkStore = checkSession.getStore("imaps");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 535);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbl_Imap = new JLabel("IMAP server :");
		lbl_Imap.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_Imap.setBounds(10, 11, 75, 14);
		contentPane.add(lbl_Imap);
		
		JLabel lblPort = new JLabel("Port number :");
		lblPort.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPort.setBounds(0, 42, 85, 14);
		contentPane.add(lblPort);
		
		JLabel lblUser = new JLabel("Username :");
		lblUser.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUser.setBounds(10, 73, 75, 14);
		contentPane.add(lblUser);
		
		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPassword.setBounds(10, 104, 75, 14);
		contentPane.add(lblPassword);
		
		txtImapServer = new JTextField();
		txtImapServer.setBounds(95, 8, 254, 20);
		contentPane.add(txtImapServer);
		txtImapServer.setColumns(10);
		
		txtPortnum = new JTextField();
		txtPortnum.setBounds(95, 39, 254, 20);
		contentPane.add(txtPortnum);
		txtPortnum.setColumns(10);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(95, 70, 254, 20);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		txtPassword = new JTextField();
		txtPassword.setBounds(95, 101, 254, 20);
		contentPane.add(txtPassword);
		txtPassword.setColumns(10);
		
		JTextArea txtEmails = new JTextArea();
		txtEmails.setLineWrap(true);
		txtEmails.setBounds(10, 129, 604, 356);
		contentPane.add(txtEmails);
		
		JButton btnCheck = new JButton("Check");
		btnCheck.addActionListener(new ActionListener() {
			//Action even on the 'Check' button that calls actionPerformed method
			public void actionPerformed(ActionEvent e) {
				//Will try to connected to the IMAP message store using user-defined settings
				try {
					checkStore.connect(txtImapServer.getText(), Integer.parseInt(txtPortnum.getText()), 
							txtUsername.getText(), txtPassword.getText());
				} catch (NumberFormatException | MessagingException ex) {
					ex.printStackTrace();
				}
				//Retrieves the inbox folder from the user's mail account
				try {
					inbox = checkStore.getFolder("INBOX");
				} catch (MessagingException ex) {
					ex.printStackTrace();
				}
				//Opens it as 'read only', only reading e-mails can happen
				try {
					inbox.open(Folder.READ_ONLY);
				} catch (MessagingException ex) {
					ex.printStackTrace();
				};
				//Searches for e-mails that have not been read yet
				try {
					messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
				} catch (MessagingException ex) {
					ex.printStackTrace();
				}
				
				//Sort the message array into most recent first
				Arrays.sort(messages, (m1, m2) -> {
					try {
						return m2.getSentDate().compareTo(m1.getSentDate());
					} catch (MessagingException ex) {
						ex.printStackTrace();
					}
					return 0;
				});
				
				//Appends the JTextArea with the most recent unread messages
				for (Message message : messages) {
					try {
						txtEmails.append("Sent Date: " + message.getSentDate() + "\n" + 
								"Subject: " + message.getSubject() + "\n");
					} catch (MessagingException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		btnCheck.setBounds(441, 38, 104, 23);
		contentPane.add(btnCheck);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(e -> this.dispose());
		btnCancel.setBounds(441, 69, 104, 23);
		contentPane.add(btnCancel);

	}
}
