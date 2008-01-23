package org.jboss.tools.jst.jsp.test;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistantExtension;
import org.eclipse.swt.widgets.Display;

public class JstJspTestUtil {


	/**
     * Returns the CA Processor from content assistant for the given offset in the document.
     * 
     * 
     * @param viewer
     * @param offset
     * @param ca
     */

	public static IContentAssistProcessor getProcessor(ITextViewer viewer, int offset, IContentAssistant ca) {
		try {
			IDocument document= viewer.getDocument();
			String type= TextUtilities.getContentType(document, ((IContentAssistantExtension)ca).getDocumentPartitioning(), offset, true);
			return ca.getContentAssistProcessor(type);
		} catch (BadLocationException x) {
		}

		return null;
	}

	/**
     * Process UI input but do not return for the specified time interval.
     * 
     * @param waitTimeMillis
     *                the number of milliseconds
     */
    public static void delay(long waitTimeMillis) {
		Display display = Display.getCurrent();
	
		// If this is the UI thread,
		// then process input.
		if (display != null) {
		    long endTimeMillis = System.currentTimeMillis() + waitTimeMillis;
		    while (System.currentTimeMillis() < endTimeMillis) {
			if (!display.readAndDispatch())
			    display.sleep();
		    }
		    display.update();
		}
		// Otherwise, perform a simple sleep.
		else {
		    try {
			Thread.sleep(waitTimeMillis);
		    } catch (InterruptedException e) {
			// Ignored.
		    }
		}
    }

}
