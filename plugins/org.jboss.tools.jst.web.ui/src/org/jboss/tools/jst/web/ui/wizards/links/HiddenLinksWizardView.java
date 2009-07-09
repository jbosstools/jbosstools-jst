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
package org.jboss.tools.jst.web.ui.wizards.links;

import org.jboss.tools.common.model.ui.wizards.query.list.AbstractListWizardView;
import org.jboss.tools.jst.web.ui.Messages;

public class HiddenLinksWizardView extends AbstractListWizardView {

	public HiddenLinksWizardView() {}

	protected String[] getActions() {
		return new String[]{Messages.HiddenLinksWizardView_HideAll, Messages.HiddenLinksWizardView_ShowAll};
	}

	protected void internalAction(String command) {
		if(Messages.HiddenLinksWizardView_HideAll.equals(command)) {
			for (int i = 0; i < boxes.length; i++) {
				boxes[i].setSelection(false);
				apply(i);
			}
		} else if(Messages.HiddenLinksWizardView_ShowAll.equals(command)) {
			for (int i = 0; i < boxes.length; i++) {
				boxes[i].setSelection(true);
				apply(i);
			}
		}
	}

}
