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

package org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model;

import org.w3c.dom.css.CSSStyleSheet;

/**
 * 
 * @author yzhishko
 *
 */

public class CSSStyleSheetContainer extends CSSContainer {

	private CSSStyleSheet sheet;

	public CSSStyleSheetContainer(CSSStyleSheet sheet, String styleSheetPath) {
		super(styleSheetPath);
		this.setSheet(sheet);
	}

	public void setSheet(CSSStyleSheet sheet) {
		this.sheet = sheet;
	}

	public CSSStyleSheet getSheet() {
		return sheet;
	}

	@Override
	public boolean equals(Object obj) {
		boolean eq = super.equals(obj);
		if (eq == false) {
			return false;
		}
		if (!(obj instanceof CSSStyleSheetContainer)) {
			return false;
		}
		return eq && sheet.equals(((CSSStyleSheetContainer) obj).getSheet());
	}

}
