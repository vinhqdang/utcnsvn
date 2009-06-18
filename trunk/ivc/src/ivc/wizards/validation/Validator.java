package ivc.wizards.validation;

import java.util.Vector;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author alexm
 * 
 *         Class used to validate an entire form consisting of many controls
 */
public abstract class Validator {
	/**
	 * Vector of controls to validate
	 */
	private Vector<IValidation> controls;
	private String error = "Some of the required fields are not filled in";

	public void setErrorMessage(String er) {
		error = er;
	}

	public String getValidatorErrorMessage() {
		return error;
	}

	public Validator() {
		controls = new Vector<IValidation>();
	}

	public void addControl(Text control, String controlError) {
		ControlDecoration txt = createControlDecoration(control, controlError);
		TextRequiredValidator validation = new TextRequiredValidator(txt) {
			public void resetError() {
				isValidated();
			}
		};
		control.addModifyListener(validation);
		controls.add(validation);
	}

	/**
	 * Resturns true if all controls have valid inputs else returns false. At the same
	 * time the error is set
	 * 
	 * @return true if all controls have valid inputs else returns false
	 */
	public boolean isValidated() {
		for (IValidation control : controls) {
			if (!control.isValid()) {
				setError(error);
				return false;
			}
		}
		setError(null);
		return true;
	}

	/**
	 * Creates a new Control decoration for the control with the error
	 * 
	 * @param destControl
	 *            the control where to apply the decoration
	 * @param hoverText
	 *            the tooltip
	 * @return the created ControlDecoration
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

	public abstract void setError(String error);
}
