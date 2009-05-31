package ivc.listeners;

import org.eclipse.jface.text.*;


public class SharedDocumentListener implements IDocumentListener {

	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {
		IDocument doc;
		// we retrieve the document where the update occurred
		doc = event.fDocument;

		// handle character deletion
//		if (event.fLength > 0) {
//			addOperationToHistory(doc, event.fOffset, event.fLength, OperationEffect.DELETE, "");
//			addAnnotationToHistory(doc, event.fOffset);
//		}
//		// handle character insertion
//		if (event.fText != "") {
//			addOperationToHistory(doc, event.fOffset, event.fLength, OperationEffect.INSERT, event.fText);
//			addAnnotationToHistory(doc, event.fOffset);
//		}

	}

	@Override
	public void documentChanged(DocumentEvent arg0) {
	}

}
