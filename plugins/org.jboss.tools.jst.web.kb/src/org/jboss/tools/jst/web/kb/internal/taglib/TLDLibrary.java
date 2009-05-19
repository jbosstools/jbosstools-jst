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

import org.jboss.tools.common.model.project.ext.IValueInfo;
import org.jboss.tools.jst.web.kb.internal.KbXMLStoreConstants;
import org.jboss.tools.jst.web.kb.taglib.ITLDLibrary;
import org.w3c.dom.Element;

/**
 * @author Viacheslav Kabanovich
 */
public class TLDLibrary extends AbstractTagLib implements ITLDLibrary {
	String displayName = null;
	String shortName = null;
	String version = null;

	public String displayName() {
		return displayName;
	}

	public String getShortName() {
		return shortName;
	}

	public String getVersion() {
		return version;
	}

	public void setDisplayName(IValueInfo s) {
		displayName = s == null ? null : s.getValue();
		attributes.put("display-name", s);
	}

	public void setShortName(IValueInfo s) {
		shortName = s == null ? null : s.getValue();
		attributes.put("short-name", s);
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void createDefaultNameSpace() {
		
	}

	public String getXMLClass() {
		return KbXMLStoreConstants.CLS_TLD_LIBRARY;
	}

	protected void saveAttributeValues(Element element) {
		super.saveAttributeValues(element);
		if(shortName != null) element.setAttribute(KbXMLStoreConstants.ATTR_SHORT_NAME, shortName);
		//TODO
	}

	protected void loadAttributeValues(Element element) {
		super.loadAttributeValues(element);
		if(element.hasAttribute(KbXMLStoreConstants.ATTR_SHORT_NAME)) {
			shortName = element.getAttribute(KbXMLStoreConstants.ATTR_SHORT_NAME);
		}
		//TODO
	}

}
