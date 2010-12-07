package org.jboss.tools.jst.web.ui.wizards.messages;

import org.eclipse.osgi.util.NLS;

public class WebUIMessages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.jst.web.ui.wizards.messages.messages"; //$NON-NLS-1$
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, WebUIMessages.class);
	}

	private WebUIMessages() {
	}
}
