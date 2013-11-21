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
package org.jboss.tools.jst.web.ui.internal.properties.html;

import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.jst.web.html.HTMLConstants;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.AbstractAdvancedPropertySetViewer;
import org.w3c.dom.Element;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class HTMLPropertySetContext implements HTMLConstants {
	protected AbstractAdvancedPropertySetViewer viewer;

	protected String elementName = "";
	protected String typeName = "";

	public HTMLPropertySetContext(AbstractAdvancedPropertySetViewer viewer) {
		this.viewer = viewer;
	}

	/**
	 * Helper method for the case if property is shown for Element object.
	 * @return
	 */
	public Element getElement() {
		Object value = viewer.getModel().getValue();
		return (value instanceof Element) ? (Element)value : null;
	}

	public boolean update() {
		boolean isModified = false;
		Element element = getElement();
		if(element != null) {
			if(!element.getNodeName().equals(elementName)) {
				elementName = element.getNodeName();
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

	public String getElementName() {
		return elementName;
	}

	public String getTypeName() {
		return typeName;
	}

	public boolean isInput() {
		return TAG_INPUT.equalsIgnoreCase(elementName);
	}

	public boolean isNumberType() {
		return isInput() && INPUT_TYPE_NUMBER.equals(typeName);
	}

	public boolean isRangeType() {
		return isInput() && INPUT_TYPE_RANGE.equals(typeName);
	}

	public boolean isButtonType() {
		return isInput() && 
			(BUTTON_TYPE_BUTTON.equals(typeName)
				|| BUTTON_TYPE_RESET.equals(typeName)
				|| BUTTON_TYPE_SUBMIT.equals(typeName));
	}

	public boolean isCheckbox() {
		return isInput() && INPUT_TYPE_CHECKBOX.equals(typeName);
	}

	public boolean isRadio() {
		return isInput() && INPUT_TYPE_RADIO.equals(typeName);
	}

	static String[] PATTERN_TEXT_TYPES = {INPUT_TYPE_TEXT, INPUT_TYPE_SEARCH, INPUT_TYPE_URL, INPUT_TYPE_TEL, INPUT_TYPE_EMAIL, INPUT_TYPE_PASSWORD};

	public boolean isPatternTextType() {
		if(isInput() && typeName != null) {
			for (String s: PATTERN_TEXT_TYPES) {
				if(typeName.equals(s)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isTextType() {
		if(isInput() && typeName != null) {
			for (String s: TEXT_TYPES) {
				if(typeName.equals(s)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isFileType() {
		return INPUT_TYPE_FILE.equals(typeName);
	}

	public boolean isTag(String tagName) {
		return tagName.equalsIgnoreCase(elementName);
	}

}
