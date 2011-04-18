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

import java.util.List;
import java.util.Properties;

import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.XModelObjectConstants;
import org.jboss.tools.common.model.project.ext.IValueInfo;
import org.jboss.tools.common.model.project.ext.event.Change;
import org.jboss.tools.common.model.project.ext.store.XMLStoreConstants;
import org.jboss.tools.jst.web.kb.internal.KbObject;
import org.jboss.tools.jst.web.kb.internal.KbXMLStoreConstants;
import org.jboss.tools.jst.web.kb.internal.scanner.XMLScanner;
import org.jboss.tools.jst.web.kb.taglib.IELFunction;
import org.jboss.tools.jst.web.model.project.ext.store.XMLValueInfo;
import org.w3c.dom.Element;

public class ELFunction extends KbObject implements IELFunction {
	public static final String SIGNATURE = "signature"; //$NON-NLS-1$
	public static final String FUNCTION_CLASS = "function-class"; //$NON-NLS-1$
	private String name;
	private String signature;
	private String functionClass;

	public ELFunction() {}

	public String getName() {
		return name;
	}

	public String getFunctionSignature() {
		return signature;
	}

	public String getFunctionClass() {
		return functionClass;
	}

	public void setName(IValueInfo s) {
		name = s == null ? null : s.getValue();
		attributesInfo.put(XMLStoreConstants.ATTR_NAME, s);
	}

	public void setSignature(IValueInfo s) {
		signature = s == null ? null : s.getValue();
		attributesInfo.put(SIGNATURE, s);
	}

	public void setFunctionClass(IValueInfo s) {
		functionClass = s == null ? null : s.getValue();
		attributesInfo.put(FUNCTION_CLASS, s);
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
		setFunctionClass(attributesInfo.get(FUNCTION_CLASS));

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
			String attrname = a.getModelEntity().getAttribute(XMLScanner.ATTR_FUNC_NAME) != null ? XMLScanner.ATTR_FUNC_NAME : XModelObjectConstants.ATTR_NAME;
			attributesInfo.put(XMLStoreConstants.ATTR_NAME, new XMLValueInfo(a, attrname));
			attributesInfo.put(SIGNATURE, new XMLValueInfo(a, XMLScanner.ATTR_FUNC_SIGN));
			attributesInfo.put(FUNCTION_CLASS, new XMLValueInfo(a, FUNCTION_CLASS));
		} else {
			super.loadAttributesInfo(element, context);
		}
	}

	@Override
	public List<Change> merge(KbObject s) {
		List<Change> changes = super.merge(s);
		
		ELFunction a = (ELFunction)s;
		if(!stringsEqual(name, a.name)) {
			changes = Change.addChange(changes, new Change(this, XMLStoreConstants.ATTR_NAME, name, a.name));
			name = a.name;
		}
		if(!stringsEqual(signature, a.signature)) {
			changes = Change.addChange(changes, new Change(this, SIGNATURE, signature, a.signature));
			signature = a.signature;
		}
		if(!stringsEqual(functionClass, a.functionClass)) {
			changes = Change.addChange(changes, new Change(this, FUNCTION_CLASS, functionClass, a.functionClass));
			functionClass = a.functionClass;
		}
		return changes;
	}

}
