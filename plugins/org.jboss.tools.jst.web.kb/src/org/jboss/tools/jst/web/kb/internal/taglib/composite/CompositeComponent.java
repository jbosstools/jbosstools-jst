/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.taglib.composite;

import java.util.Properties;

import org.jboss.tools.jst.web.kb.internal.KbXMLStoreConstants;
import org.jboss.tools.jst.web.kb.internal.taglib.AbstractComponent;
import org.w3c.dom.Element;

public class CompositeComponent extends AbstractComponent {

	public String getXMLClass() {
		return KbXMLStoreConstants.CLS_COMPOSITE_LIBRARY;
	}
	
	@Override
	protected void saveAttributesInfo(Element element, Properties context) {
		super.saveAttributesInfo(element, context);
	}

	@Override
	protected void loadAttributesInfo(Element element, Properties context) {
		super.loadAttributesInfo(element, context);
	}

}
