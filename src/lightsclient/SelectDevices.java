package lightsclient;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;

public class SelectDevices extends Dialog {

	protected Object result;
	protected Shell shell;
	private String[] inputNames;
	private String[] outputNames;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public SelectDevices(Shell parent, int style, String[] inputNames, String[] outputNames) {
		super(parent, style);
		setText("Select Devices");
		this.inputNames = inputNames;
		this.outputNames = outputNames;
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
		shell.setSize(353, 352);
		shell.setText(getText());
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		btnCancel.setBounds(262, 288, 75, 25);
		btnCancel.setText("Cancel");
		
		Button btnOkay = new Button(shell, SWT.NONE);
		btnOkay.setBounds(181, 288, 75, 25);
		btnOkay.setText("Okay");
		
		CTabFolder tabFolder = new CTabFolder(shell, SWT.BORDER);
		tabFolder.setBounds(10, 10, 327, 272);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tbtmInput = new CTabItem(tabFolder, SWT.NONE);
		tbtmInput.setText("Input");
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		tbtmInput.setControl(scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite compositeInput = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(compositeInput);
		scrolledComposite.setMinSize(compositeInput.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		CTabItem tbtmOutput = new CTabItem(tabFolder, SWT.NONE);
		tbtmOutput.setText("Output");
		
		ScrolledComposite scrolledComposite_1 = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		tbtmOutput.setControl(scrolledComposite_1);
		scrolledComposite_1.setExpandHorizontal(true);
		scrolledComposite_1.setExpandVertical(true);
		
		Composite compositeOutput = new Composite(scrolledComposite_1, SWT.NONE);
		scrolledComposite_1.setContent(compositeOutput);
		scrolledComposite_1.setMinSize(compositeOutput.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	
	}
}
