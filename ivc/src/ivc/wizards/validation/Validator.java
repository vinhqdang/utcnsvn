package ivc.wizards.validation;

import java.util.Vector;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public abstract class Validator {
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
				setError("");
			}
		};
		control.addModifyListener(validation);
		controls.add(validation);
	}

	public boolean isValid() {
		for (IValidation validator : controls) {
			if (!validator.isValid()) {
				setError(error);
				return false;
			}
		}
		setError("");
		return true;
	}

	public ControlDecoration createControlDecoration(Control destControl, String hoverText) {
		ControlDecoration controlDecoration = new ControlDecoration(destControl, SWT.LEFT | SWT.TOP);
		controlDecoration.setDescriptionText(hoverText);

		FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
				FieldDecorationRegistry.DEC_ERROR);

		controlDecoration.setImage(fieldDecoration.getImage());
		return controlDecoration;
	}

	public abstract void setError(String error);
}
