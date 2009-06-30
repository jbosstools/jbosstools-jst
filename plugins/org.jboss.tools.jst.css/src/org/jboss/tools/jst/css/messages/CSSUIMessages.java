package org.jboss.tools.jst.css.messages;

import org.eclipse.osgi.util.NLS;

public class CSSUIMessages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.jst.css.messages.messages"; //$NON-NLS-1$
	public static String CSSViewDefaultPreviewText;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, CSSUIMessages.class);
	}

	private CSSUIMessages() {
	}
}
