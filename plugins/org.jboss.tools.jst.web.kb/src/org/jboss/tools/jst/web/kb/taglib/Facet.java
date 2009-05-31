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
package org.jboss.tools.jst.web.kb.taglib;

import java.util.List;
import java.util.Properties;

import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.project.ext.IValueInfo;
import org.jboss.tools.common.model.project.ext.event.Change;
import org.jboss.tools.common.model.project.ext.store.XMLStoreConstants;
import org.jboss.tools.jst.web.kb.internal.KbObject;
import org.jboss.tools.jst.web.kb.internal.KbXMLStoreConstants;
import org.jboss.tools.jst.web.kb.internal.scanner.XMLScanner;
import org.jboss.tools.jst.web.kb.internal.taglib.AbstractComponent;
import org.jboss.tools.jst.web.model.project.ext.store.XMLValueInfo;
import org.w3c.dom.Element;

/**
 * JSF Facet Component
 * @author Alexey Kazakov
 */
public class Facet extends KbObject {

	private String description;
	private String name;

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setName(IValueInfo s) {
		name = s == null ? null : s.getValue();
		attributesInfo.put(XMLStoreConstants.ATTR_NAME, s);
	}

	public void setDescription(IValueInfo s) {
		description = s == null ? null : s.getValue();
		attributesInfo.put(AbstractComponent.DESCRIPTION, s);
	}

	public Facet clone() throws CloneNotSupportedException {
		return (Facet)super.clone();
	}

	public List<Change> merge(KbObject s) {
		List<Change> changes = super.merge(s);
		Facet f = (Facet)s;
		if(!stringsEqual(name, f.name)) {
			changes = Change.addChange(changes, new Change(this, XMLStoreConstants.ATTR_NAME, name, f.name));
			name = f.name;
		}
		if(!stringsEqual(description, f.description)) {
			changes = Change.addChange(changes, new Change(this, AbstractComponent.DESCRIPTION, description, f.description));
			description = f.description;
		}
		return changes;
	}

	public String getXMLName() {
		return KbXMLStoreConstants.TAG_FACET;
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
		setDescription(attributesInfo.get(AbstractComponent.DESCRIPTION));

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
			attributesInfo.put(XMLStoreConstants.ATTR_NAME, new XMLValueInfo(a, XMLScanner.ATTR_FACET_NAME));
			attributesInfo.put(AbstractComponent.DESCRIPTION, new XMLValueInfo(a, AbstractComponent.DESCRIPTION));
		} else {
			super.loadAttributesInfo(element, context);
		}
	}

}