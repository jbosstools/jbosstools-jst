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

import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.BaseTabControl;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.TabBoxesControl;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class BoxesSection extends AbstractCSSSection {

	@Override
	public BaseTabControl createSectionControl(Composite parent) {

		return new TabBoxesControl(parent, getStyleAttributes(),
				getBindingContext());

	}
}
