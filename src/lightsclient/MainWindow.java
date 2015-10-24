package lightsclient;

import java.io.File;
import java.io.IOException;

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
	public static void main(String[] args) {
		
		// check if appdata folder exists
		String appDataPath = System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Local" +
				File.separator + "Lights Client" + File.separator + "Folders" + File.separator;
		File appDataDir = new File(appDataPath);
		if (!(appDataDir.exists() && appDataDir.isDirectory())) {
			// create directory
			boolean success = appDataDir.mkdirs();
			if (!success) {
				System.out.println("ERROR CREATING DIRECTORY!!!");
			}
		}
		
		// create empty dirs file
		File dirs = new File(appDataPath + "dirs.txt");
		try {
			boolean success = dirs.createNewFile();
			if (!success) {
				System.out.println("ERROR CREATING FILE!!!");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
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
		shell.setSize(226, 104);
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
				fd.setFilterPath(System.getProperty("user.home"));
				String[] filterExt = {"*.zip"};
				fd.setFilterExtensions(filterExt);
				String selected = fd.open();
				System.out.println(selected);
				
			}
		});
		mntmOpenSong.setText("Open Song");
		
		MenuItem mntmOpenSetlist = new MenuItem(menu_1, SWT.NONE);
		mntmOpenSetlist.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				FileDialog fd = new FileDialog(shell);
				fd.setText("Open");
				fd.setFilterPath(System.getProperty("user.home"));
				String[] filterExt = {"*.xml"};
				fd.setFilterExtensions(filterExt);
				String selected = fd.open();
				System.out.println(selected);
				
			}
		});
		mntmOpenSetlist.setText("Open Setlist");
		
		Button btnStart = formToolkit.createButton(shell, "Start", SWT.NONE);
		btnStart.setBounds(10, 10, 75, 25);
		
		Button btnStop = new Button(shell, SWT.NONE);
		btnStop.setBounds(91, 10, 75, 25);
		formToolkit.adapt(btnStop, true, true);
		btnStop.setText("Stop");

	}
	
}
