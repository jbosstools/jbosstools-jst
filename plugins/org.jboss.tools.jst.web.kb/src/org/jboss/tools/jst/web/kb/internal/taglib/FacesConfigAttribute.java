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

/**
 * @author Viacheslav Kabanovich
 */
public class FacesConfigAttribute extends AbstractAttribute {

	public String getXMLClass() {
		return KbXMLStoreConstants.CLS_FACESCONFIG_LIBRARY;
	}
	
	@Override
	protected void loadAttributesInfo(Element element, Properties context) {
		if(context.get(XMLStoreConstants.KEY_MODEL_OBJECT) == getId() && getId() != null) {
			XModelObject a = (XModelObject)getId();
			attributesInfo.put(XMLStoreConstants.ATTR_NAME, new XMLValueInfo(a, XMLScanner.ATTR_ATTRIBUTE_NAME));
			attributesInfo.put(AbstractComponent.DESCRIPTION, new XMLValueInfo(a, AbstractComponent.DESCRIPTION));
			//TODO how to define required?
//			attributesInfo.put(REQUIRED, new XMLValueInfo(a, REQUIRED));
		} else {
			super.loadAttributesInfo(element, context);
		}
	}

}
