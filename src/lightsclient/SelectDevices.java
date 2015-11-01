package lightsclient;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.SWT;

public class SelectDevices extends Dialog {

	protected Object result;
	protected Shell shell;
	private String[] names;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public SelectDevices(Shell parent, int style, String[] names) {
		super(parent, style);
		setText("Select Devices");
		this.names = names;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(238, 300);
		shell.setText(getText());

	}
}
