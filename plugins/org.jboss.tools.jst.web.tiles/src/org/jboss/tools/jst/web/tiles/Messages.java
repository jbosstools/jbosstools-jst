package org.jboss.tools.jst.web.tiles;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.jst.web.tiles.messages"; //$NON-NLS-1$
	public static String CreateTilesSupport_CreateTiles;
	public static String RenameTilesRegistrationChange_UpdateRegistration;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
