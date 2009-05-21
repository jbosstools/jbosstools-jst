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
package org.jboss.tools.jst.web.kb.internal.taglib;

import java.util.Properties;

import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.project.ext.store.XMLStoreConstants;
import org.jboss.tools.jst.web.kb.internal.KbXMLStoreConstants;
import org.jboss.tools.jst.web.kb.internal.scanner.XMLScanner;
import org.jboss.tools.jst.web.model.project.ext.store.XMLValueInfo;
import org.w3c.dom.Element;

public class TLDTag extends AbstractComponent {

	public String getXMLClass() {
		return KbXMLStoreConstants.CLS_TLD_LIBRARY;
	}
	
	@Override
	protected void saveAttributesInfo(Element element, Properties context) {
		if(context.get(XMLStoreConstants.KEY_MODEL_OBJECT) == getId()) {
			
		} else {
			super.saveAttributesInfo(element, context);
		}
	}

	@Override
	protected void loadAttributesInfo(Element element, Properties context) {
		if(context.get(XMLStoreConstants.KEY_MODEL_OBJECT) == getId()) {
			XModelObject a = (XModelObject)getId();
			attributesInfo.put(XMLStoreConstants.ATTR_NAME, new XMLValueInfo(a, XMLStoreConstants.ATTR_NAME));
			attributesInfo.put(AbstractComponent.DESCRIPTION, new XMLValueInfo(a, AbstractComponent.DESCRIPTION));
			attributesInfo.put(COMPONENT_CLASS, new XMLValueInfo(a, XMLScanner.ATTR_TAGCLASS));
			attributesInfo.put(BODY_CONTENT, new XMLValueInfo(a, XMLScanner.ATTR_BODY_CONTENT));
			//TODO other attributes as in XMLScanner
		} else {
			super.loadAttributesInfo(element, context);
		}
	}

}
