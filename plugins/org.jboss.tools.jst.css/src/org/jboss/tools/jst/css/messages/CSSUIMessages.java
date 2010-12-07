package org.jboss.tools.jst.css.messages;

import org.eclipse.osgi.util.NLS;

public class CSSUIMessages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.jst.css.messages.messages"; //$NON-NLS-1$
	public static String CSSPreview_DefaultBrowserText;
	public static String CSS_CLASS_NAME_LABEL;
	public static String WIZARD_WINDOW_TITLE;
	public static String WIZARD_TITLE;
	public static String WIZARD_DESCRIPTION;
	public static String WIZARD_ERROR_FILE_SELECTION;
	public static String WIZARD_ERROR_EMPTY_CLASSNAME;
	public static String WIZARD_ERROR_INVALID_CLASSNAME;
	
	public static String FILE_SELECT_DIALOG_EMPTY_MESSAGE;
	public static String FILE_SELECT_DIALOG_MESSAGE;
	public static String FILE_SELECT_DIALOG_TITLE;
	public static String FILE_SELECT_LABEL;
	public static String FILE_SELECT_BUTTON;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, CSSUIMessages.class);
	}

	private CSSUIMessages() {
	}
}
