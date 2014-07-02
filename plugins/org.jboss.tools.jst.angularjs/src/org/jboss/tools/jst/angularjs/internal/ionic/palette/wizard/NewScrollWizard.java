/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard;

import org.jboss.tools.common.model.ui.editors.dnd.DropWizardMessages;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.ElementNode;
import org.jboss.tools.jst.angularjs.internal.ui.AngularJsUIImages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewScrollWizard extends NewIonicWidgetWizard<NewScrollWizardPage> implements IonicConstants {

	public NewScrollWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(AngularJsUIImages.getInstance()
				.getOrCreateImageDescriptor(AngularJsUIImages.SCROLL_IMAGE));
	}

	@Override
	protected NewScrollWizardPage createPage() {
		return new NewScrollWizardPage();
	}

	protected void addContent(ElementNode parent, boolean browser) {
		ElementNode pg = parent.addChild(TAG_ION_SCROLL, "\n");
		if(browser) {
			pg.addAttribute("style", "border-style: groove; height: 1.7in; background-color: #EEEEEE; margin: 3mm");
			ElementNode table = pg.addChild(TAG_TABLE);
			table.addAttribute(ATTR_WIDTH, "300%");
			for (int i = 0; i < 20; i++) {
				ElementNode tr = table.addChild(TAG_TR);
				for (int j = 0; j < 17; j++) {
					ElementNode td = tr.addChild(TAG_TD, "" + ((char)(65 + j)) + (i + 1));
					td.addAttribute(ATTR_HEIGHT, "30mm");
				}
			}
		}
		addAttributeIfNotEmpty(pg, ATTR_DELEGATE_HANDLE, ATTR_DELEGATE_HANDLE);

		addID("scroll-", pg);

		addAttributeIfNotEmpty(pg, ATTR_DIRECTION, ATTR_DIRECTION);

		String direction = page.getEditorValue(ATTR_DIRECTION);
		boolean xEnabled = direction.indexOf("x") >= 0;
		boolean yEnabled = direction.indexOf("y") >= 0 || direction.length() == 0;
		if(!isTrue(ATTR_SCROLLBAR_X) && xEnabled) {
			pg.addAttribute(ATTR_SCROLLBAR_X, FALSE);
		}
		if(!isTrue(ATTR_SCROLLBAR_Y) && yEnabled) {
			pg.addAttribute(ATTR_SCROLLBAR_Y, FALSE);
		}

		addAttributeIfNotEmpty(pg, ATTR_HAS_BOUNCING, ATTR_HAS_BOUNCING);

		if(isTrue(ATTR_PAGING)) {
			pg.addAttribute(ATTR_PAGING, TRUE);
		}

		if(isTrue(ATTR_ZOOMING)) {
			pg.addAttribute(ATTR_ZOOMING, TRUE);
		}

		addAttributeIfNotEmpty(pg, ATTR_MIN_ZOOM, ATTR_MIN_ZOOM);
		addAttributeIfNotEmpty(pg, ATTR_MAX_ZOOM, ATTR_MAX_ZOOM);
		addAttributeIfNotEmpty(pg, ATTR_ON_SCROLL, ATTR_ON_SCROLL);
		addAttributeIfNotEmpty(pg, ATTR_ON_REFRESH, ATTR_ON_REFRESH);

	}
}