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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.StyleAttributes;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.BaseTabControl;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public abstract class AbstractCssSection extends AbstractPropertySection {
	private DataBindingContext bindingContext;
	private StyleAttributes styleAttributes;
	protected BaseTabControl control;

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
		control = createTabControl(parent);
	}

	abstract public BaseTabControl createTabControl(Composite parent);

	public DataBindingContext getBindingContext() {
		return bindingContext;
	}

	public StyleAttributes getStyleAttributes() {
		return styleAttributes;
	}

	public BaseTabControl getTabControl() {
		return control;
	}
}
