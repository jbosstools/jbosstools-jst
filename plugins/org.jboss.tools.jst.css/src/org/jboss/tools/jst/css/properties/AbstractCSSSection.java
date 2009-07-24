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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.StyleAttributes;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.BaseTabControl;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public abstract class AbstractCSSSection extends AbstractPropertySection {
	private DataBindingContext bindingContext;
	private StyleAttributes styleAttributes;
	private BaseTabControl sectionControl;
	private Composite tabComposite;

	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		if (aTabbedPropertySheetPage instanceof CSSPropertyPage) {
			bindingContext = ((CSSPropertyPage) aTabbedPropertySheetPage)
					.getBindingContext();
			styleAttributes = ((CSSPropertyPage) aTabbedPropertySheetPage)
					.getStyleAttributes();
		}
		tabComposite = (Composite) aTabbedPropertySheetPage.getControl();
		sectionControl = createSectionControl(parent);

	}

	abstract public BaseTabControl createSectionControl(Composite parent);

	public DataBindingContext getBindingContext() {
		return bindingContext;
	}

	public StyleAttributes getStyleAttributes() {
		return styleAttributes;
	}

	public BaseTabControl getSectionControl() {
		return sectionControl;
	}
	
	public Composite getTabComposite() {
		return tabComposite;
	}
}
