/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.jsp.jspeditor.dnd;

import org.jboss.tools.common.model.ui.editors.dnd.AbsoluteFilePathAttributeValueLoader;
import org.jboss.tools.common.model.ui.editors.dnd.DefaultDropWizardModel;
import org.jboss.tools.common.model.ui.editors.dnd.IDropWizardModel;

public class JsLinkAttributeValueLoader extends AbsoluteFilePathAttributeValueLoader {
	static String ATTR_TYPE = "type"; //$NON-NLS-1$

	public JsLinkAttributeValueLoader(String pathAttributeName) {
		super(pathAttributeName, null, null);
	}

	public void fillTagAttributes(IDropWizardModel model) {
		super.fillTagAttributes(model);
		model.setAttributeValue(ATTR_TYPE, "text/javascript"); //$NON-NLS-1$
		if(model instanceof DefaultDropWizardModel) {
			((DefaultDropWizardModel)model).setPreferable(ATTR_TYPE);
		}
	}

}
