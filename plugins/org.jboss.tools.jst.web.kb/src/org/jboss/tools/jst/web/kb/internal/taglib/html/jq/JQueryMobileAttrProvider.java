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
package org.jboss.tools.jst.web.kb.internal.taglib.html.jq;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileAttrConstants.*;

import org.jboss.tools.jst.web.kb.internal.taglib.AbstractAttributeProvider;
import org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttribute;

/**
 * @author Alexey Kazakov
 */
public abstract class JQueryMobileAttrProvider extends AbstractAttributeProvider {

	private static Set<String> ALL_ATTRIBUTES;

	private static void addAttributes(Set<String> result, HtmlAttribute... attributes) {
		for (HtmlAttribute attribute : attributes) {
			result.add(attribute.getName());
		}
	}

	public static Set<String> getAllAttributes() {
		if(ALL_ATTRIBUTES==null) {
			Set<String> attributes = new HashSet<String>();
			addAttributes(attributes, AButtonAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, ButtonAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, CheckboxAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, CollapsibleAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, CollapsibleAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, CollapsibleSetAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, CollapsibleSetAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, ContentAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, ControlgroupAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, ControlgroupAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, DialogAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, DialogAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, EnhancementAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, FieldcontainAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, FixedToolbarAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, FlipToggleSwitchAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, FlipToggleSwitchAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, FooterHeaderAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, FooterHeaderAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, LinkAttributeProvider.DATA_ATTRIBUTES);
			addAttributes(attributes, ListViewAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, ListViewAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, ListviewItemAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, NavbarAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, NavbarAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, PageAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, PageAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, PanelAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, PanelAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, PopupAnchorAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, PopupAnchorAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, PopupAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, PopupAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, RadioButtonAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, SelectAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, SliderAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, TableAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, TableAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, TextInputAndTextareaAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, ThAttributeProvider.ATTRIBUTES);
			ALL_ATTRIBUTES = Collections.unmodifiableSet(attributes);
		}
		return ALL_ATTRIBUTES;
	}

	protected boolean checkDataRole(String role) {
		Map<String, String> attributes = query.getAttributes();
		if(attributes==null) {
			return false;
		}
		String dataRole = attributes.get(DATA_ROLE);
		return role.equalsIgnoreCase(dataRole);
	}
}