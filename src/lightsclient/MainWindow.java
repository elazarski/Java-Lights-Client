package lightsclient;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.SynchronousQueue;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

public class MainWindow {

	protected Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Label songLabel;
	private List setlistList;
	private Button btnStart, btnStop, btnUpButton, btnDownButton;
	private int selectedSong;
	private boolean midiReady = false;
	private boolean setlistReady = false;

	/**
	 * Launch the application.
	 * @param args
	 */
	static SynchronousQueue<byte[]> queue;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public static void main(SynchronousQueue<byte[]> q)  {
		queue = q;
		
		try {
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		// notify main to exit
		byte[] exit = new byte[1];
		exit[0] = 0x0;
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
			
			// check queue
			byte[] data = queue.poll();
			System.out.println(data);
			if (data != null) {
				switch (data[0]) {
				case 0x1:
					// new song title
					String title = new String(Arrays.copyOfRange(data, 1, data.length));
					System.out.println(title);
					songLabel.setText(title);
				}
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(SWT.SHELL_TRIM & (~SWT.RESIZE));
		shell.setSize(327, 184);
		
		//shell.setMinimumSize(327,  184);
		shell.setText("Lights Client");
		shell.setLayout(new GridLayout(4, false));
		
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
				
				
				String[] ext = {"*.tar.gz"};
				fd.setFilterExtensions(ext);
				String selected = fd.open();
				
				// construct String[] to send to main thread
				if (selected == null) {
					return;
				}
				byte[] filePath = selected.getBytes();
				byte[] send = new byte[filePath.length + 1];
				send[0] = 0x1;
				for (int i = 0; i < filePath.length; i++) {
					send[i + 1] = filePath[i];
				}
				
				// send data to main thread
				if (selected != null) {
					sendData(send);
					
					// update UI
					String song = new String(getData());
					setlistList.add(song);
				}
				
				// enable start button
				setlistReady = true;
				if (setlistReady && midiReady) {
					btnStart.setEnabled(true);
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
				if (selected == null) {
					return;
				}
				byte[] filePath = selected.getBytes();
				byte[] send = new byte[selected.length() + 1];
				send[0] = 0x2;
				for (int i = 0; i < filePath.length; i++) {
					send[i + 1] = filePath[i];
				}
				
				// send data to main thread
				sendData(send);
				
				// get data to populate setlistList
				String[] names = new String(getData()).split(Pattern.quote("|"));
				setlistList.setItems(names);
				
				// activate start button
				setlistReady = true;
				if (setlistReady && midiReady) {
					btnStart.setEnabled(true);
				}
			}
		});
		mntmOpenSetlist.setText("Open Setlist");
		
		MenuItem mntmMidi = new MenuItem(menu, SWT.NONE);
		mntmMidi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				// tell main to get the names of available input devices
				byte[] data = new byte[1];
				data[0] = 0x3;
				sendData(data);
				
				// wait for names to come back
				String[] inputNames = new String(getData()).split(Pattern.quote("|"));
				String[] outputNames = new String(getData()).split(Pattern.quote("|"));
				SelectDevices s = new SelectDevices(new Shell(), SWT.APPLICATION_MODAL, inputNames, outputNames);
				MidiSelection selected = s.open();
				
				// send data to main
				try {
					queue.put(MidiSelection.serialize(selected));
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				midiReady = true;
				
				if (setlistReady && midiReady) {
					btnStart.setEnabled(true);
				}
			}
		});
		mntmMidi.setText("MIDI");
		
		Label lblCurrentSong = new Label(shell, SWT.NONE);
		formToolkit.adapt(lblCurrentSong, true, true);
		lblCurrentSong.setText("Current Song:");
		Label label = new Label(shell, SWT.NONE);
		formToolkit.adapt(label, true, true);
		
		Label lblSetlist = new Label(shell, SWT.NONE);
		formToolkit.adapt(lblSetlist, true, true);
		lblSetlist.setText("Setlist:");
		
		setlistList = new List(shell, SWT.BORDER | SWT.V_SCROLL);
		setlistList.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 3));
		setlistList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedSong = setlistList.getSelectionIndex();
				
				// activate reorder buttons
				if (setlistList.getItemCount() > 1 && btnStart.isEnabled()) {
					btnUpButton.setEnabled(true);
					btnDownButton.setEnabled(true);
				}
			}
		});
		setlistList.setEnabled(true);
		formToolkit.adapt(setlistList, true, true);
		
		songLabel = new Label(shell, SWT.BORDER);
		songLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		songLabel.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		formToolkit.adapt(songLabel, true, true);
		
		btnUpButton = new Button(shell, SWT.CANCEL);
		btnUpButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
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
				byte[] allSongs = strToB(songs);
				
				// create byte[] to send to main
				byte[] data = new byte[allSongs.length + 1];
				data[0] = 0x4;
				for (int i = 0; i < allSongs.length; i++) {
					data[i + 1] = allSongs[i];
				}
				
				sendData(data);
			}
		});
		btnUpButton.setEnabled(false);
		btnUpButton.setText("\u25B2");
		formToolkit.adapt(btnUpButton, true, true);
		
		btnStart = formToolkit.createButton(shell, "Start", SWT.NONE);
		btnStart.setEnabled(false);
		btnStart.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				byte[] data = new byte[] {0x5};
				sendData(data);
				
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
				setlistList.setEnabled(false);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
			}
		});
		
		btnStop = new Button(shell, SWT.NONE);
		btnStop.setEnabled(false);
		formToolkit.adapt(btnStop, true, true);
		btnStop.setText("Stop");
		btnStop.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				byte[] data = new byte[] {0x6};
				sendData(data);
				
				btnStart.setEnabled(true);
				btnStop.setEnabled(false);
				setlistList.setEnabled(true);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		btnDownButton = new Button(shell, SWT.NONE);
		btnDownButton.setText("\u25BC");
		btnDownButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
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
				byte[] allSongs = strToB(songs);
				
				byte[] data = new byte[allSongs.length + 1];
				data[0] = 0x4;
				
				for (int i = 0; i < allSongs.length; i++) {
					data[i + 1] = allSongs[i];
				}
				
				sendData(data);
			}
		});
		btnDownButton.setEnabled(false);
		formToolkit.adapt(btnDownButton, true, true);
		
		shell.setSize(shell.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}
	
	// sends data to main thread
	private void sendData(byte[] data) {
		try {
			queue.put(data);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	
	}
	
	private byte[] getData() {
		try {
			return queue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	// convert String[] to byte[]
	private byte[] strToB(String[] strings) {
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
