package ivc.wizards;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author alexm
 * 
 *         The wizard page base class. This class contains methods used to design the
 *         classes in a cleaner way
 */
public abstract class BaseWizardPage extends WizardPage {

	protected BaseWizardPage(String pageName) {
		super(pageName);

	}

	public BaseWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);

	}

	/**
	 * Creates a text field in the parent container
	 * 
	 * @param parent
	 *            the parent composite
	 * @return the new text box
	 */
	public Text createTextField(Composite parent) {
		Text text = new Text(parent, SWT.SINGLE | SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.verticalAlignment = GridData.CENTER;
		data.horizontalIndent = 4;
		data.grabExcessVerticalSpace = false;
		data.widthHint = IDialogConstants.ENTRY_FIELD_WIDTH;
		text.setLayoutData(data);
		return text;
	}

	/**
	 * Creates a new control decoration for the control
	 * 
	 * @param destControl
	 *            the destination control
	 * @param hoverText
	 *            the tooltip text
	 * @return the new control
	 */
	public ControlDecoration createControlDecoration(Control destControl, String hoverText) {
		ControlDecoration controlDecoration = new ControlDecoration(destControl, SWT.LEFT
				| SWT.TOP);
		controlDecoration.setDescriptionText(hoverText);

		FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);

		controlDecoration.setImage(fieldDecoration.getImage());
		return controlDecoration;
	}

	protected Button createCheckBox(Composite group, String label) {
		Button button = new Button(group, SWT.CHECK | SWT.LEFT);
		button.setText(label);
		GridData data = new GridData();
		data.horizontalSpan = 2;
		button.setLayoutData(data);
		return button;
	}

	/**
	 * Creates a new combo
	 * 
	 * @param parent
	 *            the parent composite
	 * @return a new combo
	 */
	protected Combo createCombo(Composite parent) {
		Combo combo = new Combo(parent, SWT.READ_ONLY);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = IDialogConstants.ENTRY_FIELD_WIDTH;
		combo.setLayoutData(data);
		return combo;
	}

	/**
	 * Creates a new indented label
	 * 
	 * @param parent
	 *            the parent composite
	 * @param text
	 *            the text of the label
	 * @return the newly created label
	 */
	public static Label createLabel(Composite parent, String text) {
		return createIndentedLabel(parent, text, 0);
	}

	public static Label createIndentedLabel(Composite parent, String text, int indent) {
		Label label = new Label(parent, SWT.LEFT);
		label.setText(text);
		GridData data = new GridData();
		data.horizontalSpan = 0;
		data.horizontalAlignment = GridData.FILL;
		data.horizontalIndent = indent;
		label.setLayoutData(data);
		return label;
	}

	/**
	 * Creates a new composite
	 * 
	 * @param parent
	 *            the parent control
	 * @param numColumns
	 *            the number of columns
	 * @return the newly created composite
	 */
	protected Composite createComposite(Composite parent, int numColumns) {
		Composite composite = new Composite(parent, SWT.NULL);

		// GridLayout
		GridLayout layout = new GridLayout();
		layout.numColumns = numColumns;
		composite.setLayout(layout);

		// GridData
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		composite.setLayoutData(data);
		return composite;
	}
}
