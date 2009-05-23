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
package org.jboss.tools.jst.jsp.outline.cssdialog.tabs;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.CSSConstants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.StyleAttributes;

/**
 * Class for creating Text tab controls
 * 
 * @author Igor Zhukov (izhukov@exadel.com)
 */
public class TabTextControl extends BaseTabControl {

	/**
	 * Constructor for creating controls
	 * 
	 * @param composite
	 *            Composite element
	 * @param comboMap
	 * @param styleAttributes
	 *            the StyleAttributes object
	 */
	public TabTextControl(final Composite composite,
			final StyleAttributes styleAttributes,
			DataBindingContext bindingContext) {

		super(bindingContext, styleAttributes, composite, SWT.NONE);

		// Add FONT_FAMILY element
		addLabel(this, JstUIMessages.FONT_FAMILY);
		addFontComposite(this, CSSConstants.FONT_FAMILY);

		// Add COLOR element
		addLabel(this, JstUIMessages.COLOR);
		addColorComposite(this, CSSConstants.COLOR);

		// Add FONT_SIZE element
		addLabel(this, JstUIMessages.FONT_SIZE);
		addSizeCombo(this, CSSConstants.CSS_STYLE_VALUES_MAP
				.get(CSSConstants.FONT_SIZE), CSSConstants.FONT_SIZE);

		// Add FONT_STYLE element
		addLabel(this, JstUIMessages.FONT_STYLE);
		addCombo(this, CSSConstants.CSS_STYLE_VALUES_MAP
				.get(CSSConstants.FONT_STYLE), CSSConstants.FONT_STYLE);

		// Add FONT_WEIGHT element
		addLabel(this, JstUIMessages.FONT_WEIGHT);
		addCombo(this, CSSConstants.CSS_STYLE_VALUES_MAP
				.get(CSSConstants.FONT_WEIGHT), CSSConstants.FONT_WEIGHT);

		// Add TEXT_DECORATION element
		addLabel(this, JstUIMessages.TEXT_DECORATION);
		addCombo(this, CSSConstants.CSS_STYLE_VALUES_MAP
				.get(CSSConstants.TEXT_DECORATION),
				CSSConstants.TEXT_DECORATION);

		// Add TEXT_ALIGN element
		addLabel(this, JstUIMessages.TEXT_ALIGN);
		addCombo(this, CSSConstants.CSS_STYLE_VALUES_MAP
				.get(CSSConstants.TEXT_ALIGN), CSSConstants.TEXT_ALIGN);

	}

}