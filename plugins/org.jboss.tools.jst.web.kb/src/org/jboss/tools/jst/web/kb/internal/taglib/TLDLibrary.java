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

import org.jboss.tools.common.model.project.ext.IValueInfo;
import org.jboss.tools.common.model.project.ext.event.Change;
import org.jboss.tools.jst.web.kb.internal.KbObject;
import org.jboss.tools.jst.web.kb.internal.KbXMLStoreConstants;
import org.jboss.tools.jst.web.kb.taglib.ITLDLibrary;
import org.w3c.dom.Element;

/**
 * @author Viacheslav Kabanovich
 */
public class TLDLibrary extends AbstractTagLib implements ITLDLibrary {
	public static final String DISPLAY_NAME = "display-name";
	public static final String SHORT_NAME = "short-name";
	public static final String VERSION = "version";

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
		attributesInfo.put(DISPLAY_NAME, s);
	}

	public void setShortName(IValueInfo s) {
		shortName = s == null ? null : s.getValue();
		attributesInfo.put(SHORT_NAME, s);
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setVersion(IValueInfo s) {
		version = s == null ? null : s.getValue();
		attributesInfo.put(VERSION, s);
	}

	public void createDefaultNameSpace() {
		
	}

	public TLDLibrary clone() throws CloneNotSupportedException {
		return (TLDLibrary)super.clone();
	}

	public List<Change> merge(KbObject s) {
		List<Change> changes = super.merge(s);
		//TODO
		return changes;
	}

	public String getXMLClass() {
		return KbXMLStoreConstants.CLS_TLD_LIBRARY;
	}

	protected void saveAttributeValues(Element element) {
		super.saveAttributeValues(element);
		if(attributesInfo.get(VERSION) == null && version != null) {
			element.setAttribute(VERSION, version);
		}
	}

	protected void loadAttributeValues(Element element) {
		super.loadAttributeValues(element);
		setShortName(attributesInfo.get(SHORT_NAME));
		setDisplayName(attributesInfo.get(DISPLAY_NAME));
		setVersion(attributesInfo.get(VERSION));
		if(version == null && element.hasAttribute(VERSION)) {
			version = element.getAttribute(VERSION);
		}
		//TODO
	}

}
