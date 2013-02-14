package org.jboss.tools.jst.web.ui.palette.html.wizard;

import org.eclipse.osgi.util.NLS;

public class WizardMessages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.jst.web.ui.palette.html.wizard.messages"; //$NON-NLS-1$

	public static String showPreviewButtonText;
	public static String hidePreviewButtonText;

	public static String labelLabel;
	public static String miniLabel;
	public static String themeLabel;
	public static String numberedLabel;
	public static String readonlyLabel;
	public static String autodividersLabel;
	public static String searchFilterLabel;
	public static String insetLabel;
	public static String offLabelLabel;
	public static String onLabelLabel;
	public static String idLabel;
	public static String layoutLabel;

	public static String newCheckboxWizardTitle;
	public static String newCheckboxWizardDescription;
	
	public static String newListviewWizardTitle;
	public static String newListviewWizardDescription;
	
	public static String newToggleWizardTitle;
	public static String newToggleWizardDescription;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, WizardMessages.class);
	}

	private WizardMessages() {
	}
}
