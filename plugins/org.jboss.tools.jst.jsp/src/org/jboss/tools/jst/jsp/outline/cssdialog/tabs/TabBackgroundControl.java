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
 * Class for creating control in Background tab
 * 
 * @author Igor Zhukov (izhukov@exadel.com)
 */
public class TabBackgroundControl extends BaseTabControl {



	/**
	 * Constructor for creating controls
	 * 
	 * @param composite
	 *            Composite element
	 * @param comboMap
	 * @param styleAttributes
	 *            the StyleAttributes object
	 */
	public TabBackgroundControl(final Composite composite,
			final StyleAttributes styleAttributes,
			DataBindingContext bindingContext) {
		super(bindingContext, styleAttributes, composite, SWT.NONE);

		// Add BACKGROUND_COLOR element
		addLabel(this, JstUIMessages.BACKGROUND_COLOR);
		addColorComposite(this, CSSConstants.BACKGROUND_COLOR);

		// Add BACKGROUND_IMAGE element
		addLabel(this, JstUIMessages.BACKGROUND_IMAGE);
		addImageFileComposite(this, CSSConstants.CSS_STYLE_VALUES_MAP
				.get(CSSConstants.BACKGROUND_IMAGE),
				CSSConstants.BACKGROUND_IMAGE);

		// Add BACKGROUND_REPEAT element
		addLabel(this, JstUIMessages.BACKGROUND_REPEAT);
		addCombo(this, CSSConstants.CSS_STYLE_VALUES_MAP
				.get(CSSConstants.BACKGROUND_REPEAT),
				CSSConstants.BACKGROUND_REPEAT);

	}

}