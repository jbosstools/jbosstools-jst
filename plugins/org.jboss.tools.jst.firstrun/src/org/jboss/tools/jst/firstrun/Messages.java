package org.jboss.tools.jst.firstrun;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.jst.firstrun.messages"; //$NON-NLS-1$
	public static String JBossASAdapterInitializer_AppServer32;
	public static String JBossASAdapterInitializer_AppServer40;
	public static String JBossASAdapterInitializer_AppServer42;
	public static String JBossASAdapterInitializer_AppServer50;
	public static String JBossASAdapterInitializer_CannotCreateDriver;
	public static String JBossASAdapterInitializer_CannotCreateProfile;
	public static String JBossASAdapterInitializer_CannotCreateServer;
	public static String JBossASAdapterInitializer_JBossASHypersonicEmbeddedDB;
	public static String JBossASAdapterInitializer_Runtime;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
