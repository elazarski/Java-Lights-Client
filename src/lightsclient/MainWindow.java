package lightsclient;

import java.util.concurrent.SynchronousQueue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.events.SelectionAdapter;

public class MainWindow {

	protected Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	/**
	 * Launch the application.
	 * @param args
	 */
	static SynchronousQueue<String[]> queue;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public static void main(SynchronousQueue<String[]> q)  {
		queue = q;
		
		try {
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		// notify main to exit
		String[] exit = new String[1];
		exit[0] = "exit";
		try {
			queue.put(exit);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(379, 208);
		shell.setText("Lights Client");
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");
		
		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);
		
		MenuItem mntmOpenSong = new MenuItem(menu_1, SWT.NONE);
		mntmOpenSong.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				FileDialog fd = new FileDialog(shell);
				fd.setText("Open");
				
				String[] filterExt = {"*.zip"};
				fd.setFilterExtensions(filterExt);
				String selected = fd.open();
				
				// construct String[] to send to main thread
				String[] send = new String[2];
				send[0] = "song";
				send[1] = selected;
				
				// send data to main thread
				if (selected != null) {
					sendData(send);
				}
				
			}
		});
		mntmOpenSong.setText("Open Song");
		
		MenuItem mntmOpenSetlist = new MenuItem(menu_1, SWT.NONE);
		mntmOpenSetlist.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				FileDialog fd = new FileDialog(shell);
				fd.setText("Open");
				
				String[] filterExt = {"*.xml"};
				fd.setFilterExtensions(filterExt);
				String selected = fd.open();
				
				// construct String[] to send to main thread
				String[] send = new String[2];
				send[0] = "setlist";
				send[1] = selected;
				
				// send data to main thread
				if (selected != null) {
					sendData(send);
				}
			}
		});
		mntmOpenSetlist.setText("Open Setlist");
		
		MenuItem mntmMidi = new MenuItem(menu, SWT.NONE);
		mntmMidi.setText("MIDI");
		
		Button btnStart = formToolkit.createButton(shell, "Start", SWT.NONE);
		btnStart.setBounds(10, 114, 75, 25);
		
		Button btnStop = new Button(shell, SWT.NONE);
		btnStop.setBounds(91, 114, 75, 25);
		formToolkit.adapt(btnStop, true, true);
		btnStop.setText("Stop");

	}
	
	// sends data to main thread
	private void sendData(String[] data) {
		try {
			queue.put(data);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	
	}
}
