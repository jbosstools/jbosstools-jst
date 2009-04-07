package org.jboss.tools.jst.web.context;

import org.eclipse.osgi.util.NLS;

public final class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.jboss.tools.jst.web.context.RegisterServerContext";//$NON-NLS-1$

	private Messages() {
		// Do not instantiate
	}

	public static String ERR_APP_NAME_IS_NOT_SPECIFIED;
	public static String ERR_SERVLET_VERSION_IS_NOT_SET;
	public static String ERR_SERVLET_VERSION_IS_NOT_VALID;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
