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
package org.jboss.tools.jst.jsp.outline;

import java.util.Map;

import org.eclipse.jface.text.IDocument;
import org.jboss.tools.jst.web.html.JQueryHTMLConstants;
import org.jboss.tools.jst.web.kb.KbQuery;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
@Deprecated
public class JQueryCategoryProvider implements ICategoryProvider, JQueryHTMLConstants {
	public static final String CATEGORY_OFTEN_USED = "Often used";
	public static final String CATEGORY_JQM = "jQuery";
	public static final String CATEGORY_OTHER = "Advanced HTML";
	public static final String CATEGORY_ANGULAR = "AngularJS";
	
	public boolean init(IDocument document, KbQuery query) {
		return true;
	}

	public String getCategory(String attributeName) {
		return CATEGORY_OTHER;
	}
	
	public boolean isExpert(String category) {
		return !CATEGORY_OFTEN_USED.equals(category);
	}

	public void fillAttributeWeights(Map<String, Integer> weights) {
	}

}
