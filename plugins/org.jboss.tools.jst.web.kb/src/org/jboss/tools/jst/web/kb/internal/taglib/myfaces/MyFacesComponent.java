/******************************************************************************* 
 * Copyright (c) 2010 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/

package org.jboss.tools.jst.web.kb.internal.taglib.myfaces;

import java.util.Properties;

import org.jboss.tools.common.model.project.ext.IValueInfo;
import org.jboss.tools.jst.web.kb.internal.KbXMLStoreConstants;
import org.jboss.tools.jst.web.kb.internal.taglib.AbstractComponent;
import org.w3c.dom.Element;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class MyFacesComponent extends AbstractComponent {
	static String PARENT_CLASS = "parent-class"; //$NON-NLS-1$
	protected String parentClass;

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.KbObject#getXMLClass()
	 */
	@Override
	public String getXMLClass() {
		return KbXMLStoreConstants.CLS_MYFACES_LIBRARY;
	}

	public String getParentClass() {
		return parentClass;
	}

	public void setParentClass(String parentClass) {
		this.parentClass = parentClass;
	}

	public void setParentClass(IValueInfo s) {
		parentClass = s == null ? null : s.getValue();
		attributesInfo.put(PARENT_CLASS, s);
	}

	@Override
	public void loadXML(Element element, Properties context) {
		super.loadXML(element, context);
		setParentClass(attributesInfo.get(PARENT_CLASS));
	}

}
