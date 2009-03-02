package org.jboss.tools.jst.web.ui.wizards.messages;

import org.eclipse.osgi.util.NLS;

public class WebUIMessages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.jst.web.ui.wizards.messages.messages"; //$NON-NLS-1$
	public static String FILE_SELECT_DIALOG_EMPTY_MESSAGE;
	public static String FILE_SELECT_DIALOG_MESSAGE;
	public static String FILE_SELECT_DIALOG_TITLE;
	public static String FILE_SELECT_LABEL;
	public static String FILE_SELECT_BUTTON;
	public static String CSS_CLASS_NAME_LABEL;
	public static String WIZARD_WINDOW_TITLE;
	public static String WIZARD_TITLE;
	public static String WIZARD_DESCRIPTION;
	public static String WIZARD_ERROR_FILE_SELECTION;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, WebUIMessages.class);
	}

	private WebUIMessages() {
	}
}
