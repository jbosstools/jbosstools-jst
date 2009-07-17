/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.tiles.ui.editor;

import org.eclipse.osgi.util.NLS;

/**
 * @author Igels
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TilesEditorMessages extends NLS {

	private static final String BUNDLE_NAME= "org.jboss.tools.jst.web.tiles.ui.editor.TilesEditorMessages"; //$NON-NLS-1$
	
	public static String TilesDiagram_select;
	
	public static String TilesDiagram_marquee;
	
	public static String TilesDiagram_create_new_connection;
	
	public static String TilesDiagram_defenition_template;


	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, TilesEditorMessages.class);		
	}
	
}