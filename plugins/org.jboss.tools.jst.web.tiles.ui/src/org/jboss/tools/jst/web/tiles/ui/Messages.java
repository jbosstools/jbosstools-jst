package org.jboss.tools.jst.web.tiles.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.jst.web.tiles.ui.messages"; //$NON-NLS-1$
	public static String ConnectionCommand_Label;
	public static String LinkEditPart_Link;
	public static String TilesDiagramEditPart_TilesDiagram;
	public static String TilesPreferencesRetargetAction_Text;
	public static String TilesPreferencesRetargetAction_Tooltip;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
