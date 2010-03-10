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

package org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.selection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSRuleContainer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSSelectorTreeModel;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSTreeNode;

/**
 * yzhishko
 */

@SuppressWarnings("serial")
public abstract class CSSClassSelectionChangedEvent extends
		SelectionChangedEvent {

	protected CSSSelectorTreeModel model;

	public CSSClassSelectionChangedEvent(ISelectionProvider source,
			ISelection selection) {
		super(source, selection);
	}

	public CSSRuleContainer[] getRuleContainersForName(String cssClassName) {
		if (model == null) {
			return null;
		}
		return findContainers(model.getInvisibleRoot(), cssClassName);
	}

	public abstract String[] getSelectedClassNames();

	public CSSRuleContainer[] getSelectedRuleContainers() {
		List<CSSRuleContainer> containers = new ArrayList<CSSRuleContainer>();
		String[] classNames = getSelectedClassNames();
		if (classNames != null) {
			for (int i = 0; i < classNames.length; i++) {
				CSSRuleContainer[] cssContainers = getRuleContainersForName(classNames[i]);
				if (cssContainers != null) {
					for (int j = 0; j < cssContainers.length; j++) {
						containers.add(cssContainers[j]);
					}
				}
			}
		}
		return containers.toArray(new CSSRuleContainer[0]);
	}

	private CSSRuleContainer[] findContainers(CSSTreeNode parentRoot,
			String className) {
		List<CSSRuleContainer> ruleContainers = new ArrayList<CSSRuleContainer>(
				0);
		List<CSSTreeNode> styleSheetList = parentRoot.getChildren();
		for (Iterator<CSSTreeNode> iterator = styleSheetList.iterator(); iterator
				.hasNext();) {
			CSSTreeNode styleSheetNode = (CSSTreeNode) iterator.next();
			List<CSSTreeNode> styleClassList = styleSheetNode.getChildren();
			for (Iterator<CSSTreeNode> iterator2 = styleClassList.iterator(); iterator2
					.hasNext();) {
				CSSTreeNode styleClassNode = (CSSTreeNode) iterator2.next();
				if (className.equals(styleClassNode.toString())) {
					ruleContainers.add((CSSRuleContainer) styleClassNode
							.getCSSContainer());
				}
			}
		}
		return ruleContainers.toArray(new CSSRuleContainer[0]);
	}

	public CSSSelectorTreeModel getModel() {
		return model;
	}

}
