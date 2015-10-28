package lightsclient;

import java.io.IOException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

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
		
		// create file reader
		MyReader reader = new MyReader();
		
		// main loop
		while (mainWindow.isAlive()) {
			try {
				String[] command = windowQueue.take();
				
				// check command
				if (command[0].equals("song")) {
					Song s = null;
					s = reader.readSong(command[1]);
					
					// update UI
					
				} else if (command[0].equals("setlist")) {
					Song[] s = reader.readSetlist(command[1]);
					
					// update UI
					
				} else if (command[0].equals("exit")) {
					break;
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
