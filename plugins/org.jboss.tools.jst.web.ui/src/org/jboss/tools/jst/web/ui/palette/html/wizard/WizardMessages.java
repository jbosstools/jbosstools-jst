package org.jboss.tools.jst.web.ui.palette.html.wizard;

import org.eclipse.osgi.util.NLS;

public class WizardMessages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.jst.web.ui.palette.html.wizard.messages"; //$NON-NLS-1$

	public static String showPreviewButtonText;
	public static String hidePreviewButtonText;

	public static String labelLabel;
	public static String miniLabel;
	public static String themeLabel;

	public static String newCheckboxWizardTitle;
	public static String newCheckboxWizardDescription;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, WizardMessages.class);
	}

	private WizardMessages() {
	}
}
