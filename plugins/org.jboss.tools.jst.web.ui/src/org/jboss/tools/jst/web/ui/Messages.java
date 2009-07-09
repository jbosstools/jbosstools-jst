package org.jboss.tools.jst.web.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.jst.web.ui.messages"; //$NON-NLS-1$
	public static String HiddenLinksWizardView_HideAll;
	public static String HiddenLinksWizardView_ShowAll;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
