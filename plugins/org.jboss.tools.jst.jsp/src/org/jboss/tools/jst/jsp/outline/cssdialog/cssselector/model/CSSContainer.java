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

/**
 * 
 * @author yzhishko
 *
 */

public abstract class CSSContainer {

	private String styleSheetPath;
	
	public CSSContainer(String styleSheetPath) {
		this.setStyleSheetPath(styleSheetPath);
	}

	public void setStyleSheetPath(String styleSheetPath) {
		this.styleSheetPath = styleSheetPath;
	}

	public String getStyleSheetPath() {
		return styleSheetPath;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CSSContainer)) {
			return false;
		}
		return styleSheetPath.equals(((CSSContainer)obj).getStyleSheetPath());
	}
	
}
