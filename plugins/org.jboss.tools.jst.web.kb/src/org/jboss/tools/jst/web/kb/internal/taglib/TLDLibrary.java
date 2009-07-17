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
import org.jboss.tools.jst.web.kb.taglib.INameSpace;
import org.jboss.tools.jst.web.kb.taglib.ITLDLibrary;
import org.w3c.dom.Element;

/**
 * @author Viacheslav Kabanovich
 */
public class TLDLibrary extends AbstractTagLib implements ITLDLibrary {
	public static final String DISPLAY_NAME = "display-name"; //$NON-NLS-1$
	public static final String SHORT_NAME = "short-name"; //$NON-NLS-1$
	public static final String VERSION = "version"; //$NON-NLS-1$

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
		setDefaultNameSpace(new INameSpace() {
		
			public String getURI() {
				return uri;
			}
		
			public String getPrefix() {
				return shortName;
			}
		});
	}

	public TLDLibrary clone() throws CloneNotSupportedException {
		return (TLDLibrary)super.clone();
	}

	public List<Change> merge(KbObject s) {
		List<Change> changes = super.merge(s);
		TLDLibrary l = (TLDLibrary)s;
		if(!stringsEqual(displayName, l.displayName)) {
			changes = Change.addChange(changes, new Change(this, DISPLAY_NAME, displayName, l.displayName));
			displayName = l.displayName;
		}
		if(!stringsEqual(shortName, l.shortName)) {
			changes = Change.addChange(changes, new Change(this, SHORT_NAME, shortName, l.shortName));
			shortName = l.shortName;
		}
		if(!stringsEqual(version, l.version)) {
			changes = Change.addChange(changes, new Change(this, VERSION, version, l.version));
			version = l.version;
		}
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
	}

}
