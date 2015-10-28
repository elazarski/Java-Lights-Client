package lightsclient;


// import statements
import lightsclient.Song;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
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
	Song readSong(String path) throws IOException, InvalidMidiDataException {		
		// initialize Song with title
		int lastIndexOf = path.lastIndexOf(fileSeparator) + 1;
		String title = path.substring(lastIndexOf, path.length() - 4);		
		Song ret = new Song(title);
		
		// open .zip file
		// code found at: http://stackoverflow.com/questions/15667125/read-content-from-files-which-are-inside-zip-file
		ZipFile zipFile = new ZipFile(path);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			String name = entry.getName();
			Sequence sequence = MidiSystem.getSequence(zipFile.getInputStream(entry));
			
			// check name of current file
			if (name.contains("i")) {
				ret.addInput(sequence);
			} else if (name.contains("o")) {
				ret.addOutput(sequence);
			} else {
				// m or p
				// get times whether it be m or p
				long tickLength = sequence.getTickLength();
				ret.setTickLength(tickLength);
				
				// parse sequence, extracting the tick of each event
				ArrayList<Long> ticks = new ArrayList<Long>();
				for (Track track : sequence.getTracks()) {
					for (int i = 0; i < track.size(); i++) {
						MidiEvent event = track.get(i);
						
						// check if event is a note
						MidiMessage message = event.getMessage();
						if (message instanceof ShortMessage) {
							ShortMessage sMessage = (ShortMessage)message;
							
							// check if note on here
							if (sMessage.getCommand() == 0x90) {
								ticks.add(event.getTick());								
							}
						}
					}
				}
				
				// check if m or p now
				if (name.contains("p")) {
					ret.addPartTimes(ticks);
				} else {
					ret.addMeasureTimes(ticks);
				}
			}
		}
		
		// close file
		zipFile.close();
		
		// return song that we have built
		return ret;
	}

	// read setlist
	// returns Song[] to be passed directly to player
	Song[] readSetlist(String path) throws IOException, InvalidMidiDataException {
		ArrayList<Song> retList = new ArrayList<Song>();
		
		// first read .txt of setlist
		// code modified from: http://stackoverflow.com/questions/16027229/reading-from-a-text-file-and-storing-in-a-string
		ArrayList<String> lines = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = br.readLine();
		
		while (line != null) {
			lines.add(line);	
			line = br.readLine();
		}
		br.close();
		
		// get paths from lines
		for (int i = 0; i < lines.size(); i++) {
			String p = home + fileSeparator + lines.get(i).replace(':', fileSeparator);
			lines.remove(i);
			lines.add(p);
		}
		
		// fill retList by reading songs from lines
		for (int i = 0; i < lines.size(); i++) {
			retList.add(readSong(lines.get(i)));
		}
		
		Song[] ret = new Song[retList.size()];
		ret = retList.toArray(ret);
		return ret;
	}
}
