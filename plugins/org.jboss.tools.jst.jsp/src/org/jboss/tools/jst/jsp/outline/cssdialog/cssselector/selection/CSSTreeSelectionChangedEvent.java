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

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TreeSelection;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSContainer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSRuleContainer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSSelectorTreeModel;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSStyleSheetContainer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSTreeNode;

/**
 * 
 * @author yzhishko
 * 
 */

@SuppressWarnings("serial")
public class CSSTreeSelectionChangedEvent extends CSSClassSelectionChangedEvent {

	public CSSTreeSelectionChangedEvent(ISelectionProvider source,
			ISelection selection, CSSSelectorTreeModel model) {
		super(source, selection);
		this.model = model;
	}

	@Override
	public String[] getSelectedClassNames() {
		Set<String> selectedNames = new LinkedHashSet<String>(0);
		TreeSelection treeSelection = (TreeSelection) selection;
		Object[] selectedItems = treeSelection.toArray();
		for (int i = 0; i < selectedItems.length; i++) {
			CSSTreeNode treeNode = (CSSTreeNode) selectedItems[i];
			CSSContainer container = treeNode.getCSSContainer();
			if (container instanceof CSSStyleSheetContainer) {
				List<CSSTreeNode> childNodes = treeNode.getChildren();
				for (Iterator<CSSTreeNode> iterator = childNodes.iterator(); iterator
						.hasNext();) {
					CSSTreeNode cssTreeNode = (CSSTreeNode) iterator.next();
					selectedNames.add(cssTreeNode.toString());
				}
			} else {
				selectedNames.add(treeNode.toString());
			}
		}
		return selectedNames.toArray(new String[0]);
	}

	@Override
	public CSSRuleContainer[] getSelectedRuleContainers() {
		Set<CSSRuleContainer> ruleContainers = new LinkedHashSet<CSSRuleContainer>(
				0);
		TreeSelection treeSelection = (TreeSelection) selection;
		Object[] selectedItems = treeSelection.toArray();
		for (int i = 0; i < selectedItems.length; i++) {
			CSSTreeNode treeNode = (CSSTreeNode) selectedItems[i];
			CSSContainer container = treeNode.getCSSContainer();
			if (container instanceof CSSStyleSheetContainer) {
				List<CSSTreeNode> childNodes = treeNode.getChildren();
				for (Iterator<CSSTreeNode> iterator = childNodes.iterator(); iterator
						.hasNext();) {
					CSSTreeNode cssTreeNode = (CSSTreeNode) iterator.next();
					ruleContainers.add((CSSRuleContainer) cssTreeNode
							.getCSSContainer());
				}
			} else {
				ruleContainers.add((CSSRuleContainer) treeNode
						.getCSSContainer());
			}
		}
		return ruleContainers.toArray(new CSSRuleContainer[0]);
	}

}
