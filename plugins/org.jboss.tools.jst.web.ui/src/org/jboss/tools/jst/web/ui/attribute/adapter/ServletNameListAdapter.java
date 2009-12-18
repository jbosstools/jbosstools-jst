/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.attribute.adapter;

import org.jboss.tools.common.model.ui.attribute.*;
import org.jboss.tools.common.model.ui.attribute.adapter.*;
import org.jboss.tools.common.meta.XAttribute;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class ServletNameListAdapter extends DefaultComboBoxValueAdapter {

	protected IListContentProvider createListContentProvider(XAttribute attribute) {
		ServletNameListContentProvider p = new ServletNameListContentProvider();
		p.setModel(model, modelObject);
		p.setAttribute(attribute);
		return p;	
	}

}
