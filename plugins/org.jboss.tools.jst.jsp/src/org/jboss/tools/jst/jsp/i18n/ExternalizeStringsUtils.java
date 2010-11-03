/*******************************************************************************
 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.i18n;

import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.jsf.model.pv.JSFProjectTreeConstants;
import org.jboss.tools.jsf.model.pv.JSFProjectsRoot;
import org.jboss.tools.jsf.model.pv.JSFProjectsTree;
import org.jboss.tools.jst.web.model.pv.WebProjectNode;
import org.w3c.dom.Element;

/**
 * The Utils class for externalize strings routine.
 */
public class ExternalizeStringsUtils {

	/**
	 * Checks that the user has selected a correct string.
	 *  
	 * @param selection the current selection
	 * @return <code>true</code> if correct
	 */
	public static boolean isSelectionCorrect(ISelection selection) {
		boolean isSelectionCorrect = false;
		if ((selection instanceof TextSelection)
				&& (selection instanceof IStructuredSelection)
				&& (((IStructuredSelection) selection).size() == 1)) {
			/*
			 * In general selection is correct now except one case:
			 */
			isSelectionCorrect = true;
			/*
			 * If the whole tag is selected -- check if it has children.
			 * The wizard could not find a proper string to externalize
			 * among several tags.
			 */
			Object selectedElement = ((IStructuredSelection) selection).getFirstElement();
			if (selectedElement instanceof Element) {
				Element element = (Element) selectedElement;
				if (element.hasChildNodes()) {
					isSelectionCorrect = false;
				} 
			}
		} 
		return isSelectionCorrect;
	}

	public static XModelObject findFacesConfig(XModel model) {
		XModelObject facesConfig = null;
		JSFProjectsRoot root = JSFProjectsTree.getProjectsRoot(model);
		if (root != null) {
			WebProjectNode n = (WebProjectNode) root
					.getChildByPath(JSFProjectTreeConstants.CONFIGURATION);
			if (n != null) {
				/*
				 * The array contains the all configuration files in the project
				 * including files from jar archives.
				 * Only editable object is be the necessary faces-config file.
				 */
				XModelObject[] os = n.getTreeChildren();
				for (XModelObject o : os) {
					if (o.isObjectEditable()) {
						facesConfig = o;
						break;
					}
				}
			}
		}
		/*
		 * When nothing has been found try the last straight-forward way.
		 */
		if (facesConfig == null) {
			facesConfig = model.getByPath("/faces-config.xml"); //$NON-NLS-1$
		}
		return facesConfig;
	}
}
