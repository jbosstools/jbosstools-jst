package org.jboss.tools.jst.web.ui.operation;

import org.eclipse.osgi.util.NLS;

public final class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.jboss.tools.jst.web.ui.operation.WebProjectCreationOperation";//$NON-NLS-1$

	private Messages() {
		// Do not instantiate
	}

	public static String COD_MESSAGE;
	public static String COD_TITLE;
	public static String BTN_CANCEL;
	public static String BTN_OK;
	public static String ADOPT_WILL_OVERWRITE_DOT_FILES_MESSAGE;
	public static String ADOPT_WILL_CLEAR_WORKSPACE_MESSAGE;

	public static String WebProjectAdoptOperation_Cancel;
	public static String WebProjectAdoptOperation_Continue;
	public static String WebProjectAdoptOperation_Warning;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
