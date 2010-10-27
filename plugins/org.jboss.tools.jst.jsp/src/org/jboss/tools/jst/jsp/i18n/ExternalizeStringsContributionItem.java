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
package org.jboss.tools.jst.jsp.i18n;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.swt.widgets.Menu;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;

public class ExternalizeStringsContributionItem extends ActionContributionItem {

	/**
	 * Instantiates a new externalize strings contribution item.
	 * Default constructor is used to create 
	 * source editor context menu from plugin.xml
	 */
	public ExternalizeStringsContributionItem() {
		super(new ExternalizeStringsAction());
	}

	@Override
	public void fill(Menu parent, int index) {
		/*
		 * Simply sets the title
		 */
		getAction().setText(JstUIMessages.EXTERNALIZE_STRINGS_POPUP_MENU_TITLE);
		super.fill(parent, index);
	}
	
}
