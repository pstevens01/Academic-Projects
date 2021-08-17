package mail.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * 
 * @author 		Paul Stevens
 * 
 * 				A class that will read a 'smtp.properties' property files into
 * 				a file object that the SMTP server settings uses to send an email
 * 				with Javax Mail libraries. The class will set default properties
 * 				and then load them from the properties file. A 'saveProperties' 
 * 				method is used by the 'SettingsDialog' class to save new SMTP
 * 				user-defined settings.
 *
 */

public class EmailSettings {
	private File settingFile = new File("smtp.properties");
	private Properties settingProps;
	
	public Properties loadProperties() throws IOException {
		Properties defaultProps = new Properties();
		//Sets the default SMTP properties and can be changed by the user
		defaultProps.setProperty("mail.smtp.host", "smtp.gmail.com");
		defaultProps.setProperty("mail.smtp.port", "587");
		defaultProps.setProperty("mail.user", "p.stevens.0785@gmail.com");
		defaultProps.setProperty("mail.password", "Internet92595");
		defaultProps.setProperty("mail.smtp.starttls.enable", "true");
		defaultProps.setProperty("mail.smtp.auth", "true");
		
		settingProps = new Properties(defaultProps);
		//Load SMTP properties from 'smtp.properties' file
		if (settingFile.exists()) {
			InputStream input = new FileInputStream(settingFile);
			settingProps.load(input);
			input.close();
		}
		return settingProps;
	}
	
	public void saveProperties(String host, String port, String user, String pass) throws IOException {
		//User-defined SMTP settings that allows the user to check different SMTP mail servers
		settingProps.setProperty("mail.smtp.host", host);
		settingProps.setProperty("mail.smtp.port", port);
		settingProps.setProperty("mail.user", user);
		settingProps.setProperty("mail.password", pass);
		settingProps.setProperty("mail.smtp.starttls.enable", "true");
		settingProps.setProperty("mail.smtp.auth", "true");
		
		//Saves settings to 'smtp.properties' file
		OutputStream output = new FileOutputStream(settingFile);
		settingProps.store(output, "host settings");
		output.close();
	}
}
