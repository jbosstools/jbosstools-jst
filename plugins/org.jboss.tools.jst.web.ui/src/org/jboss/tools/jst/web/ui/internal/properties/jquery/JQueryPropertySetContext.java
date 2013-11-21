/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.internal.properties.jquery;

import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.jst.web.html.JQueryHTMLConstants;
import org.jboss.tools.jst.web.ui.internal.properties.html.HTMLPropertySetContext;
import org.w3c.dom.Element;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class JQueryPropertySetContext extends HTMLPropertySetContext implements JQueryHTMLConstants {
	String roleName = "";

	public JQueryPropertySetContext(JQueryPropertySetViewer viewer) {
		super(viewer);
	}

	public boolean update() {
		boolean isModified = false;
		Element element = getElement();
		if(element != null) {
			if(!element.getNodeName().equals(elementName)) {
				elementName = element.getNodeName();
				isModified = true;
			}
			String newRoleName = XMLUtilities.hasAttribute(element, ATTR_DATA_ROLE)
					? element.getAttribute(ATTR_DATA_ROLE) : "";
			if(!roleName.equals(newRoleName)) {
				roleName = newRoleName;
				isModified = true;
			}
			String newTypeName = "";
			if(TAG_INPUT.equals(elementName) && XMLUtilities.hasAttribute(element, ATTR_TYPE)) {
				newTypeName = element.getAttribute(ATTR_TYPE);
			}
			if(!newTypeName.equals(typeName)) {
				typeName = newTypeName;
				isModified = true;
			}
		}
		return isModified;
	}

	public String getRoleName() {
		return roleName;
	}

	public boolean isControlGroup() {
		return ROLE_CONTROLGROUP.equals(roleName);
	}

	public boolean isPopup() {
		return ROLE_POPUP.equals(roleName);
	}

	public boolean isListview() {
		return ROLE_LISTVIEW.equals(roleName);
	}

	public boolean isCollapsible() {
		return ROLE_COLLAPSIBLE.equals(roleName);
	}

	public boolean isCollapsibleSet() {
		return ROLE_COLLAPSIBLE_SET.equals(roleName);
	}

	public boolean isPanel() {
		return ROLE_PANEL.equals(roleName);
	}

	public boolean isButtonRole() {
		return ROLE_BUTTON.equals(roleName);
	}

	public boolean isDialog() {
		return ROLE_DIALOG.equals(roleName);
	}

	public boolean isPage() {
		return ROLE_PAGE.equals(roleName);
	}

	public boolean isHeader() {
		return ROLE_HEADER.equals(roleName);
	}

	public boolean isFooter() {
		return ROLE_FOOTER.equals(roleName);
	}

	public boolean isSlider() {
		return ROLE_SLIDER.equals(roleName);
	}

	public boolean isTableRole() {
		return ROLE_TABLE.equals(roleName);
	}

}
