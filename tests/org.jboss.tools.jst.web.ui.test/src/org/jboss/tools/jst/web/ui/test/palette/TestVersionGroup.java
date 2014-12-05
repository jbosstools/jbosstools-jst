/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.test.palette;

import org.jboss.tools.jst.web.kb.taglib.IHTMLLibraryVersion;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.AbstractPaletteVersionGroup;

public class TestVersionGroup extends AbstractPaletteVersionGroup {

	@Override
	public IHTMLLibraryVersion getVersion() {
		return TestLibraryVersion.TEST_1_0;
	}

}
