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
package org.jboss.tools.jst.web.refactoring;

import java.util.Properties;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.jboss.tools.common.model.refactoring.RefactoringHelper;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;

public class WebRenameTLDWebAppChange extends CompositeChange {
	XModelObject object;
	String newName;
	Properties replacements = new Properties();
	
	public WebRenameTLDWebAppChange(XModelObject object, String newName) {
		super(WebUIMessages.WEBXML_CHANGES);
		this.object = object;
		this.newName = newName;
		replacements.clear();
		String oldText = XModelObjectLoaderUtil.getResourcePath(object);
		if(object.getModel().getByPath("FileSystems/WEB-INF" + oldText) == object) { //$NON-NLS-1$
			oldText = "/WEB-INF" + oldText; //$NON-NLS-1$
		}
		int i = oldText.lastIndexOf("/"); //$NON-NLS-1$
		String newText = oldText.substring(0, i + 1) + newName;
		replacements.setProperty(oldText, newText);
		try {
			addChanges();
		} catch (Exception e) {
			WebModelPlugin.getPluginLog().logError(e);
		}
	}

	private void addChanges() throws Exception {
		if(object == null) return;
		XModelObject webxml = object.getModel().getByPath("/web.xml"); //$NON-NLS-1$
		XModelObject[] fs = (webxml == null) ? new XModelObject[0] : new XModelObject[]{webxml};
		addChanges(fs);
	}
	
	private void addChanges(XModelObject[] objects) {
		for (int i = 0; i < objects.length; i++) {
			RefactoringHelper.addChanges(objects[i], replacements, this);
		}
	}

}
