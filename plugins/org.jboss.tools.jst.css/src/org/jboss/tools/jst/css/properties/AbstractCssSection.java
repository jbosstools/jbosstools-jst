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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSNode;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSStyleRule;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.StyleAttributes;
import org.w3c.dom.css.CSSStyleDeclaration;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public abstract class AbstractCssSection extends AbstractPropertySection {
	private DataBindingContext bindingContext;
	private StyleAttributes styleAttributes;

	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		if (aTabbedPropertySheetPage instanceof CSSPropertyPage) {
			bindingContext = ((CSSPropertyPage) aTabbedPropertySheetPage)
					.getBindingContext();
			styleAttributes = ((CSSPropertyPage) aTabbedPropertySheetPage)
					.getStyleAttributes();
		}
		createTabControl(parent);
	}

	abstract public void createTabControl(Composite parent);

	public DataBindingContext getBindingContext() {
		return bindingContext;
	}

	public StyleAttributes getStyleAttributes() {
		return styleAttributes;
	}

	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);

	}
}
