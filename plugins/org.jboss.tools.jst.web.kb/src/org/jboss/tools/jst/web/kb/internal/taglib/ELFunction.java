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
import org.jboss.tools.common.model.project.ext.IValueInfo;
import org.jboss.tools.common.model.project.ext.store.XMLStoreConstants;
import org.jboss.tools.jst.web.kb.internal.KbObject;
import org.jboss.tools.jst.web.kb.internal.KbXMLStoreConstants;
import org.jboss.tools.jst.web.kb.internal.scanner.XMLScanner;
import org.jboss.tools.jst.web.kb.taglib.IELFunction;
import org.jboss.tools.jst.web.model.project.ext.store.XMLValueInfo;
import org.w3c.dom.Element;

public class ELFunction extends KbObject implements IELFunction {
	public static final String SIGNATURE = "signature"; //$NON-NLS-1$
	private String name;
	private String signature;

	public ELFunction() {}

	public String getName() {
		return name;
	}

	public String getFunctionSignature() {
		return signature;
	}

	public void setName(IValueInfo s) {
		name = s == null ? null : s.getValue();
		attributesInfo.put(XMLStoreConstants.ATTR_NAME, s);
	}

	public void setSignature(IValueInfo s) {
		signature = s == null ? null : s.getValue();
		attributesInfo.put(SIGNATURE, s);
	}

	public ELFunction clone() throws CloneNotSupportedException {
		return (ELFunction)super.clone();
	}

	public String getXMLName() {
		return KbXMLStoreConstants.TAG_FUNCTION;
	}
	
	public Element toXML(Element parent, Properties context) {
		Element element = super.toXML(parent, context);
		
		if(attributesInfo.get(XMLStoreConstants.ATTR_NAME) == null && name != null) {
			element.setAttribute(XMLStoreConstants.ATTR_NAME, name);
		}

		return element;
	}

	public void loadXML(Element element, Properties context) {
		super.loadXML(element, context);

		setName(attributesInfo.get(XMLStoreConstants.ATTR_NAME));
		setSignature(attributesInfo.get(SIGNATURE));

		if(name == null && element.hasAttribute(XMLStoreConstants.ATTR_NAME)) {
			name = element.getAttribute(XMLStoreConstants.ATTR_NAME);
		}
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
		if(context.get(XMLStoreConstants.KEY_MODEL_OBJECT) == getId() && getId() != null) {
			XModelObject a = (XModelObject)getId();
			attributesInfo.put(XMLStoreConstants.ATTR_NAME, new XMLValueInfo(a, XMLScanner.ATTR_FUNC_NAME));
			attributesInfo.put(SIGNATURE, new XMLValueInfo(a, XMLScanner.ATTR_FUNC_SIGN));
		} else {
			super.loadAttributesInfo(element, context);
		}
	}

}
