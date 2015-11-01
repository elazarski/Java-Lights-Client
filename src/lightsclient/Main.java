package lightsclient;

import java.io.IOException;
import java.util.concurrent.SynchronousQueue;

import javax.sound.midi.InvalidMidiDataException;

import lightsclient.*;

public class Main {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		
		// create and start UI thread
		SynchronousQueue<String[]> windowQueue = new SynchronousQueue<>();
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
				String[] command = windowQueue.take();
				
				// check command
				if (command[0].equals("song")) {
					Song s = reader.readSong(command[1]);
					setlist.addSong(s);
					// update UI
					String[] w = new String[1];
					w[0] = s.toString();
					windowQueue.put(w);
					
				} else if (command[0].equals("setlist")) {
					setlist = reader.readSetlist(command[1]);
					
					// update UI
					windowQueue.put(setlist.getSongTitles());
					
				} else if (command[0].equals("exit")) {
					System.exit(0);
				} else if (command[0].equals("reorder")) {
					String[] newOrder = new String[command.length - 1];
					
					for (int i = 0; i < newOrder.length; i++) {
						newOrder[i] = command[i + 1];
					}
					
					setlist.reorder(newOrder);
				} else if (command[0].equals("midi")) {
					if (command[1].equals("names")) {
						String[] names = m.getInputNames();
						windowQueue.put(names);
					}
				}
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

}
