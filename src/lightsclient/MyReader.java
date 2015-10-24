package lightsclient;

import java.io.BufferedWriter;
// import statements
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


// this class only contains methods for reading files
// it will return necessary data after file(s) have been read
public class MyReader {
	
	
	String os;
	
	public MyReader() {
		this.os = System.getProperty("sun.desktop");
		
	}
	
	// check for last used folders directory and files
	public String[] checkPrefs() {
		
		// return variable
		// ret[0] is last songs directory
		// ret[1] is last setlist directory
		String[] ret = new String[2];
		ret[0] = null;
		ret[1] = null;
		
		// check for settings directory first
		String prefsDir = null;
		if (os.equals("windows")) {
			prefsDir = System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Local" +
					File.separator + "Lights Client" + File.separator + "dirs.xml";
		} else { // Linux
			prefsDir = System.getProperty("user.home") + File.separator + ".Lights Client" + File.separator + "dirs.xml";
		}
		
		// check if prefsDir exists, if it does not, return with null values after creating
		File f = new File(prefsDir);
		if (!f.exists()) {
			// file does not exist
			// create and return
			
			try {
				f.getParentFile().mkdirs();
				f.createNewFile();
				
				// populate file with necessary nodes
				// can be written as string for ease
				String doc = "<prefs>\n<setlistDir></setlistDir>\n<songDir></songDir>\n</prefs>";
				BufferedWriter writer = new BufferedWriter(new FileWriter(f));
				writer.write(doc);
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return ret;
		}
		
		// since we got this far, the file exists. Now we must read it and extract the information
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(f);
			
			doc.getDocumentElement().normalize();
			
			Element setlistDirElement = (Element)doc.getElementsByTagName("setlistDir").item(0);
			Element songDirElement = (Element)doc.getElementsByTagName("songDir").item(0);
			
			String setlistDir = setlistDirElement.getChildNodes().item(0).getNodeValue().trim();
			String songDir = songDirElement.getChildNodes().item(0).getNodeValue().trim();
			
			// append values to ret
			ret[0] = songDir;
			ret[1] = setlistDir;
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return ret;
	}

}
