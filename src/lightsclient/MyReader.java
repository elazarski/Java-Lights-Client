package lightsclient;


// import statements
import lightsclient.Song;
import lightsclient.Setlist;
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
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jfugue.midi.MidiFileManager;
import org.jfugue.parser.Parser;
import org.jfugue.parser.ParserListenerAdapter;
import org.jfugue.pattern.*;
import org.jfugue.theory.Chord;
import org.jfugue.theory.Note;
import org.jfugue.midi.MidiTools;


// this class only contains methods for reading files
// it will return necessary data after file(s) have been read
public class MyReader {
	
	private String home;
	private char fileSeparator;
	
	// initialize variables
	public MyReader() {
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
	//	ZipFile zipFile = new ZipFile(path);
		//Enumeration<? extends ZipEntry> entries = zipFile.entries();
		File folder = new File(path);
		File[] files = folder.listFiles();
		
		for (File f : files) {
			String name = f.getName();
			
			if (name.contains("xml")) {
				// input
				System.out.println(name);
				Sequence seq = MidiSystem.getSequence(f);
				Map m = MidiTools.sortMessagesByTick(seq);
				
				for (Object obj : m.keySet()) {
					Long tick = (Long)obj;
					
					ArrayList<MidiMessage> messages = (ArrayList<MidiMessage>)m.get(tick);
					
					for (MidiMessage msg : messages) {
						if (msg instanceof ShortMessage) {
							ShortMessage sm = (ShortMessage)msg;
							
							if (sm.getCommand() == 0x90) {
								System.out.println("NOTE_ON: " + sm.getData1() + ", " + tick);
							}
						}
					}
					System.out.println();
				}			}
		}
		
		/*while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			String name = entry.getName();
			
			// check name of current file
			if (name.startsWith("i")) {
				// input
				System.out.println(name);
				Sequence seq = MidiSystem.getSequence(zipFile.getInputStream(entry));
				Map m = MidiTools.sortMessagesByTick(seq);
				
				for (Object obj : m.keySet()) {
					Long tick = (Long)obj;
					
					ArrayList<MidiMessage> messages = (ArrayList<MidiMessage>)m.get(tick);
					
					for (int i = 0; i < messages.size(); i++) {
						MidiMessage msg = messages.get(i);
						if (msg instanceof ShortMessage) {
							ShortMessage sm = (ShortMessage)msg;
							
							if (sm.getCommand() == ShortMessage.NOTE_ON ) {
								System.out.println("NOTE_ON: " + sm.getData1() + ", " + tick);
							}
						}
					}
					System.out.println();
				}
				
			} else if (name.startsWith("o")) {
				// output
				
			} else {
				// m or p
				// get times whether it be m or p
			//
				/*long tickLength = sequence.getTickLength();
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
				if (name.startsWith("p")) {
					ret.addPartTimes(ticks);
				} else {
					ret.addMeasureTimes(ticks);
				}
			}
		}*/
		
		// close file
		//zipFile.close();
		
		// return song that we have built
		return ret;
	}

	// read setlist
	// returns Song[] to be passed directly to player
	Setlist readSetlist(String path) throws IOException, InvalidMidiDataException {
		Setlist ret = new Setlist();
		
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
			String p = home + fileSeparator + lines.get(0).replace(':', fileSeparator);
			lines.add(p);
	
			// remove original line
			lines.remove(0);
		}
		
		// read songs and return
		for (int i = 0; i < lines.size(); i++) {
			ret.addSong(readSong(lines.get(i)));
		}
		
		
		return ret;
	}
}
