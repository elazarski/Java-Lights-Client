package lightsclient;


// import statements
import lightsclient.Song;
import javax.sound.midi.Sequence;
import javax.sound.midi.MidiSystem;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;


// this class only contains methods for reading files
// it will return necessary data after file(s) have been read
public class MyReader {
	
	private String os;
	private String home;
	private char fileSeparator;
	
	// initialize variables
	public MyReader() {
		os = System.getProperty("sun.desktop");
		home = System.getProperty("user.home");
		fileSeparator = File.separatorChar;
	}
	
	// reads .zip file and returns Song object
	Song readSong(String relativePath) throws IOException {
		// create whole path to song
		String path = home + relativePath;
		
		// initialize Song with title
		int lastIndexOf = path.lastIndexOf(fileSeparator);
		String title = path.substring(lastIndexOf, path.length() - 5);		
		Song ret = new Song(title);
		
		// open .zip file
		// code found at: http://stackoverflow.com/questions/15667125/read-content-from-files-which-are-inside-zip-file
		ZipFile zipFile = new ZipFile(path);
		
		
		return ret;
	}
}
