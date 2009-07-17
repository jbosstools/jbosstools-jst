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

import org.jboss.tools.jst.web.project.list.IWebPromptingProvider;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;

/**
 * @author Igels
 */
public class ViewActionsResorceElement extends XModelAttributeValueResource {

	public static String SUPPORTED_ID = WebPromptingProvider.JSF_VIEW_ACTIONS;

	private ViewActionElement[] viewActionElements;

	public ViewActionsResorceElement(IEditorInput editorInput, ModelElement parent) {
		super(editorInput, parent);
	}

	public ViewActionsResorceElement(IEditorInput editorInput, String name,	ModelElement parent) {
		super(editorInput, name, parent);
	}

	/**
	 * @see IAttributeValueContainer#getChildren()
	 */
	public ModelElement[] getChildren() {
		if(viewActionElements!=null) {
			return viewActionElements;
		}
		if(!isReadyToUse()) {
			return EMPTY_LIST;
		}

		Properties view = new Properties();
		view.put(IWebPromptingProvider.VIEW_PATH, path); 

		List actions = provider.getList(xModelObject.getModel(), SUPPORTED_ID, "", view); //$NON-NLS-1$
		List result = new ArrayList();
		for(int i=0; i<actions.size(); i++) {
			Object actionName = actions.get(i);
			if(actionName!=null && ((String)actionName).trim().length()>0) {
				result.add(new ViewActionElement((String)actionName, this));
			}
		}
		viewActionElements = (ViewActionElement[])result.toArray(new ViewActionElement[result.size()]);

		return viewActionElements;
	}

	/**
	 * @see ModelElement#getName()
	 */
	public String getName() {
		return "View Actions"; //$NON-NLS-1$
	}
}