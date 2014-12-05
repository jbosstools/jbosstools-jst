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

import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.AbstractPaletteGroup;

public class TestPaletteGroup extends AbstractPaletteGroup {
	private static boolean enabled = false;
	public TestPaletteGroup(){
		add(new TestVersionGroup());
	}
	public static void setEnabled(boolean enabled){
		TestPaletteGroup.enabled = enabled;
	}
	@Override
	public String getName() {
		return "A Test";
	}

	@Override
	public ITagLibRecognizer getRecognizer() {
		return new TestTaglibRecognizer();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}
	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
