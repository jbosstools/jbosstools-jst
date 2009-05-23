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
 * Class for creating control in Boxes tab
 * 
 * @author Igor Zhukov (izhukov@exadel.com)
 */
public class TabBoxesControl extends BaseTabControl {

	/**
	 * Constructor for creating controls
	 * 
	 * @param composite
	 *            Composite element
	 * @param comboMap
	 * @param styleAttributes
	 *            the StyleAttributes object
	 */
	public TabBoxesControl(final Composite composite,
			final StyleAttributes styleAttributes,
			DataBindingContext bindingContext) {
		super(bindingContext, styleAttributes, composite, SWT.NONE);

		// Dimension section
		addSectionLabel(this, JstUIMessages.DIMENSION_TITLE);

		// Add WIDTH element
		addLabel(this, JstUIMessages.WIDTH);
		addSizeText(this, CSSConstants.WIDTH);

		// Add HEIGHT element
		addLabel(this, JstUIMessages.HEIGHT);
		addSizeText(this, CSSConstants.HEIGHT);

		// border section
		addSectionLabel(this, JstUIMessages.BORDER_TITLE);

		// Add BORDER_STYLE element
		addLabel(this, JstUIMessages.BORDER_STYLE);
		addCombo(this, CSSConstants.CSS_STYLE_VALUES_MAP
				.get(CSSConstants.BORDER_STYLE), CSSConstants.BORDER_STYLE);

		// Add BORDER_COLOR element
		addLabel(this, JstUIMessages.BORDER_COLOR);
		addColorComposite(this, CSSConstants.BORDER_COLOR);

		// Add BORDER_WIDTH element
		addLabel(this, JstUIMessages.BORDER_WIDTH);
		addSizeCombo(this, CSSConstants.CSS_STYLE_VALUES_MAP
				.get(CSSConstants.BORDER_WIDTH), CSSConstants.BORDER_WIDTH);

		// margin/padding section
		addSectionLabel(this, JstUIMessages.MARGIN_PADDING_TITLE);

		// Add MARGIN element
		addLabel(this, JstUIMessages.MARGIN);
		addSizeText(this, CSSConstants.MARGIN);

		// Add PADDING element
		addLabel(this, JstUIMessages.PADDING);
		addSizeText(this, CSSConstants.PADDING);

	}

}