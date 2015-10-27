package lightsclient;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import lightsclient.*;

public class Main {

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
		
		// main loop
		while (mainWindow.isAlive()) {
			try {
				String[] command = windowQueue.take();
				
				if (command[0].equals("exit")) {
					break;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}
		
	}

}
