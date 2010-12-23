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

import java.util.List;
import java.util.Properties;

import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.ModelFeatureFactory;
import org.jboss.tools.jst.web.project.list.IWebPromptingProvider;
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
		IWebPromptingProvider provider = (IWebPromptingProvider)ModelFeatureFactory.getInstance().createFeatureInstance("org.jboss.tools.jsf.model.pv.JSFPromptingProvider"); //$NON-NLS-1$
		if(provider == null) {
			return null;
		}
		List<Object> result = provider.getList(model, IWebPromptingProvider.JSF_FACES_CONFIG, "", new Properties()); //$NON-NLS-1$
		if(result != null && !result.isEmpty()) {
			return (XModelObject)result.get(0);
		}
		return null;
	}

}
