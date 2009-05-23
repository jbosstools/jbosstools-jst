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

package org.jboss.tools.jst.jsp.outline.cssdialog.widgets;

import org.eclipse.jface.databinding.swt.WidgetValueProperty;
import org.eclipse.swt.SWT;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class CSSWidgetValueProperty extends WidgetValueProperty {

	public CSSWidgetValueProperty() {
		super(SWT.Modify);
	}

	protected Object doGetValue(Object source) {
		return ((CSSWidget) source).getText();
	}

	@Override
	protected void doSetValue(Object source, Object value) {
		((CSSWidget) source).setText((String) (value != null ? value : "")); //$NON-NLS-1$
	}

	public Object getValueType() {
		return String.class;
	}

}
