package ivc.wizards.sharing.pages;

import org.eclipse.jface.viewers.ICellEditorValidator;

public class TextValidator implements ICellEditorValidator {
	public String isValid(Object value) {
		if (!(value instanceof String))
			return null;

		String text = ((String) value).trim();
		if (text.equals(""))
			return "Project Name Cannot be null";
		
		return null;
	}

}
