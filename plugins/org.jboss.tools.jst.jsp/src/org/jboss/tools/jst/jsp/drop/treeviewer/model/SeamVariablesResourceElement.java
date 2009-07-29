/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.jsp.drop.treeviewer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.ui.IEditorInput;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.outline.ValueHelper;

/**
 * 
 * @author Viacheslav Kabanovich	
 */
public class SeamVariablesResourceElement extends AttributeValueResource {
	IEditorInput editorInput;
	
	SeamVariableElement[] elements = null;

	public SeamVariablesResourceElement(IEditorInput editorInput, String name, ModelElement parent) {
		super(name, parent);
		this.editorInput = editorInput;
	}

	public ModelElement[] getChildren() {
		if(elements != null) {
			return elements;
		}
		Properties p = new Properties();
		p.put("file", valueHelper.getFile()); //$NON-NLS-1$
		List list = ValueHelper.seamPromptingProvider.getList(null, "seam.variables", "", p); //$NON-NLS-1$ //$NON-NLS-2$
		if(list == null) return EMPTY_LIST;
		List<SeamVariableElement> es = new ArrayList<SeamVariableElement>();
		for (int i = 0; i < list.size(); i++) {
			String s = list.get(i).toString();
			if(s.length() == 0) {
				continue;
			}
			es.add(new SeamVariableElement(s, this));
		}
		return elements = es.toArray(new SeamVariableElement[es.size()]);
	}

	public String getName() {
		return "Seam Variables"; //$NON-NLS-1$
	}

}
