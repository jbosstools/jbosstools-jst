/******************************************************************************* 
 * Copyright (c) 2013 - 2015 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.angularjs.internal.taglib.html;

import java.util.Map;

import org.jboss.tools.jst.web.kb.KbQuery;

/**
 * @author Alexey Kazakov
 */
public class ClassDirective implements IDirective {

	private String description;
	private String startText;
	private String endText;
	private String[] attributes;

	public ClassDirective(String description, String startText, String endText,	String directiveName) {
		super();
		this.description = description;
		this.startText = startText;
		this.endText = endText;
		this.attributes = AngularAttributeProvider.getNgAttributes(directiveName);
	}

	@Override
	public boolean checkAttribute(KbQuery query) {
		Map<String, String> map = query.getAttributes();
		if(map!=null) {
			for (String attr : attributes) {
				if(map.containsKey(attr)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String getStartText() {
		return startText;
	}

	@Override
	public String getEndText() {
		return endText;
	}

	@Override
	public String getDescription() {
		return description;
	}
}