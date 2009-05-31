package ivc.wizards.validation;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Text;

public abstract class TextRequiredValidator implements ModifyListener,IValidation {


	private ControlDecoration controlDecoration;
	private boolean valid = false;

	public TextRequiredValidator(ControlDecoration controlDec) {

		controlDecoration = controlDec;
	}

	public void modifyText(ModifyEvent event) {
		if (((Text) event.widget).getText().isEmpty()) {
			controlDecoration.show();
			valid = false;
		} else {
			valid = true;
			controlDecoration.hide();
		}
		resetError();
	}


	public boolean isValid() {
		return valid;
	}
	public abstract void resetError();
}