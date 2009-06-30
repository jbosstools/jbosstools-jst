/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.css.properties;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.BaseTabControl;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.TabQuickEditControl;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class QuickEditSection extends AbstractCssSection {

	@Override
	public BaseTabControl createTabControl(Composite parent) {
		return new TabQuickEditControl(parent, getStyleAttributes(),
				getBindingContext());
	}

	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		getTabControl().update();
		// TODO find better way
		getTabControl().getParent().getParent().layout(true);
		super.setInput(part, selection);
	}

	@Override
	public void aboutToBeShown() {
		super.aboutToBeShown();
	}
}
