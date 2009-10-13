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
package org.jboss.tools.jst.jsp.jspeditor.dnd;

import org.jboss.tools.common.model.ui.editors.dnd.AbsoluteFilePathAttributeValueLoader;
import org.jboss.tools.common.model.ui.editors.dnd.DefaultDropWizardModel;
import org.jboss.tools.common.model.ui.editors.dnd.IDropWizardModel;

public class CssLinkAttributeValueLoader extends AbsoluteFilePathAttributeValueLoader {
	static String ATTR_REL = "rel"; //$NON-NLS-1$
	static String ATTR_TYPE = "type"; //$NON-NLS-1$

	public CssLinkAttributeValueLoader(String pathAttributeName) {
		super(pathAttributeName, null, null);
	}

	public void fillTagAttributes(IDropWizardModel model) {
		super.fillTagAttributes(model);
		model.setAttributeValue(ATTR_REL, "stylesheet"); //$NON-NLS-1$
		model.setAttributeValue(ATTR_TYPE, "text/css"); //$NON-NLS-1$
		if(model instanceof DefaultDropWizardModel) {
			((DefaultDropWizardModel)model).setPreferable(ATTR_REL);
			((DefaultDropWizardModel)model).setPreferable(ATTR_TYPE);
		}
	}

}
