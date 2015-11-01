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
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.List;

public class MainWindow {

	protected Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Label songLabel;
	private List setlistList;
	private Button btnStart, btnStop, btnUpButton, btnDownButton;
	private int selectedSong;

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
		shell.setSize(379, 156);
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
					
					// update UI
					String song = getData()[0];
					addSong(song);
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
				
				String[] filterExt = {"*.txt"};
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
				
				// get data to populate setlistList
				String[] names = getData();
				setlistList.setItems(names);
			}
		});
		mntmOpenSetlist.setText("Open Setlist");
		
		MenuItem mntmMidi = new MenuItem(menu, SWT.NONE);
		mntmMidi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				// tell main to get the names of available input devices
				String[] data = new String[2];
				data[0] = "midi";
				data[1] = "names";
				sendData(data);
				
				// wait for names to come back
				String[] names = getData();
				
				
			}
		});
		mntmMidi.setText("MIDI");
		
		btnStart = formToolkit.createButton(shell, "Start", SWT.NONE);
		btnStart.setEnabled(false);
		btnStart.setBounds(10, 62, 75, 25);
		
		btnStop = new Button(shell, SWT.NONE);
		btnStop.setEnabled(false);
		btnStop.setBounds(91, 62, 75, 25);
		formToolkit.adapt(btnStop, true, true);
		btnStop.setText("Stop");
		
		Label lblCurrentSong = new Label(shell, SWT.NONE);
		lblCurrentSong.setBounds(10, 10, 75, 15);
		formToolkit.adapt(lblCurrentSong, true, true);
		lblCurrentSong.setText("Current Song:");
		
		songLabel = new Label(shell, SWT.BORDER);
		songLabel.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		songLabel.setBounds(10, 31, 156, 25);
		formToolkit.adapt(songLabel, true, true);
		
		Label lblSetlist = new Label(shell, SWT.NONE);
		lblSetlist.setBounds(167, 10, 34, 15);
		formToolkit.adapt(lblSetlist, true, true);
		lblSetlist.setText("Setlist:");
		
		setlistList = new List(shell, SWT.BORDER | SWT.V_SCROLL);
		setlistList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedSong = setlistList.getSelectionIndex();
			}
		});
		setlistList.setEnabled(true);
		setlistList.setBounds(213, 10, 140, 77);
		formToolkit.adapt(setlistList, true, true);
		
		btnUpButton = new Button(shell, SWT.NONE);
		btnUpButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// have to reorder what is in the list
				
				// check first if the top is selected
				if (selectedSong == 0) {
					return;
				}
				
				String[] songs = setlistList.getItems();
				String selected = songs[selectedSong];
				String moved = songs[selectedSong - 1];
				
				songs[selectedSong - 1] = selected;
				songs[selectedSong] = moved;
				
				// update UI
				setlistList.setItems(songs);
				
				// update main thread
				String[] data = new String[songs.length + 1];
				data[0] = "reorder";
				
				for (int i = 0; i < songs.length; i++) {
					data[i + 1] = songs[i];
				}
				
				sendData(data);
			}
		});
		btnUpButton.setEnabled(false);
		btnUpButton.setText("\u25B2");
		btnUpButton.setBounds(195, 37, 12, 15);
		formToolkit.adapt(btnUpButton, true, true);
		
		btnDownButton = new Button(shell, SWT.NONE);
		btnDownButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// have to reorder what is in the list
				String[] songs = setlistList.getItems();
				
				// check if the last item is selected first
				if (selectedSong == songs.length - 1) {
					return;
				}
				String selected = songs[selectedSong];
				String moved = songs[selectedSong + 1];
				
				songs[selectedSong + 1] = selected;
				songs[selectedSong] = moved;
				
				// update UI
				setlistList.setItems(songs);
				
				// update main thread
				String[] data = new String[songs.length + 1];
				data[0] = "reorder";
				
				for (int i = 0; i < songs.length; i++) {
					data[i + 1] = songs[i];
				}
				
				sendData(data);
			}
		});
		btnDownButton.setEnabled(false);
		btnDownButton.setText("\u25BC");
		btnDownButton.setBounds(195, 62, 12, 15);
		formToolkit.adapt(btnDownButton, true, true);

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
	
	private String[] getData() {
		try {
			return queue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private void addSong(String title) {
		setlistList.add(title);
		
		// check if start button enabled, if it is not, do so
		if (!btnStart.getEnabled()) {
			btnStart.setEnabled(true);
		}
		
		// check if the up and down buttons should be enabled
		if (setlistList.getItemCount() > 1) {
			btnUpButton.setEnabled(true);
			btnDownButton.setEnabled(true);
		}
	}
}
