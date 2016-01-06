package lightsclient;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.SynchronousQueue;
import java.util.regex.Pattern;

import javax.sound.midi.InvalidMidiDataException;

import lightsclient.*;

public class Main {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		
		// create and start UI thread
		SynchronousQueue<byte[]> windowQueue = new SynchronousQueue<>();
		Thread mainWindow = new Thread(new Runnable() {
			
			@Override
			public void run() {
				MainWindow.main(windowQueue);
			}
		});
		mainWindow.setName("UI Thread");
		mainWindow.start();
		
		// create variables
		MyReader reader = new MyReader();
		Setlist setlist = new Setlist();
		MidiInterface m = new MidiInterface();
		
		// main loop
		while (mainWindow.isAlive()) {
			try {
				byte[] command = windowQueue.take();
				String path;
				
				// check command
				switch (command[0]) {
				case 0x0:
					// exit
					System.exit(0);
					
					
				case 0x1:
					// open song
					// get byte[] after command[1]
					path = new String(Arrays.copyOfRange(command, 1, command.length));
					Song s = reader.readSong(path);
					setlist.addSong(s);
					
					// update UI
					windowQueue.put(s.toString().getBytes());
					break;
					
					
				case 0x2:
					// open setlist
					// get byte[] after command[1]
					path = new String(Arrays.copyOfRange(command,  1,  command.length));
					setlist = reader.readSetlist(path);
					
					// update UI
					byte[] data = strToB(setlist.getSongTitles());
					windowQueue.put(data);
					break;
					
					
				case 0x3:
					// select MIDI devices
					// send device names to window
					byte[] inputNames = strToB(m.getInputNames());
					windowQueue.put(inputNames);
					byte[] outputNames = strToB(m.getOutputNames());
					windowQueue.put(outputNames);
					
					// wait for device selection
					
					break;
					
					
				case 0x4:
					// reorder setlist
					// get byte[] after command[1]
					String[] newOrder = new String(Arrays.copyOfRange(command,  1,  command.length)).split(Pattern.quote("|"));
					
					setlist.reorder(newOrder);	
					break;
					
					
				default:
					// not implemented yet
					System.err.println(command[0] + " not implemented yet!");
					break;
				}
//				if (command[0].equals("song")) {
//					Song s = reader.readSong(command[1]);
//					setlist.addSong(s);
//					// update UI
//					String[] w = new String[1];
//					w[0] = s.toString();
//					windowQueue.put(w);
//					
//				} else if (command[0].equals("setlist")) {
//					setlist = reader.readSetlist(command[1]);
//					
//					// update UI
//					windowQueue.put(setlist.getSongTitles());
//					
//				} else if (command[0].equals("exit")) {
//					System.exit(0);
//				} else if (command[0].equals("reorder")) {
//					String[] newOrder = new String[command.length - 1];
//					
//					for (int i = 0; i < newOrder.length; i++) {
//						newOrder[i] = command[i + 1];
//					}
//					
//					setlist.reorder(newOrder);
//				} else if (command[0].equals("midi")) {
//					if (command[1].equals("names")) {
//						String[] inputNames = m.getInputNames();
//						windowQueue.put(inputNames);
//						String[] outputNames = m.getOutputNames();
//						windowQueue.put(outputNames);
//					}
//				}
			} catch (InterruptedException | IOException | InvalidMidiDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if (mainWindow.isAlive()) {
					mainWindow.stop();
				}
				System.exit(1);
			}
		}
		
	}
	
	// convert String[] to byte[]
	private static byte[] strToB(String[] strings) {
		String allStrings = new String();
		for (int i = 0; i < strings.length; i++) {
			allStrings = allStrings.concat(strings[i]);
			allStrings = allStrings.concat("|");
		}
		
		// remove last '|'
		allStrings = allStrings.substring(0, allStrings.length() - 1);
		
		// convert allStrings to byte[]
		byte[] ret = allStrings.getBytes();
		
		return ret;
	}

}
