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

import java.beans.PropertyChangeEvent;

import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil.TwoColumns;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewContentWizardPage extends NewIonicWidgetWizardPage {

	public NewContentWizardPage() {
		super("newContent", IonicWizardMessages.newContentWizardTitle);
		setDescription(IonicWizardMessages.newContentWizardDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		IFieldEditor delegate = IonicFieldEditorFactory.createDelegateHandleEditor();
		addEditor(delegate, parent);

		createIDEditor(parent, true);
		addID.setValue(Boolean.FALSE);

		createSeparator(parent);

		IFieldEditor padding = IonicFieldEditorFactory.createPaddingEditor();
		addEditor(padding, parent);

		createSeparator(parent);

		TwoColumns columns = createTwoColumns(parent);

		IFieldEditor scroll = IonicFieldEditorFactory.createScrollEditor();
		addEditor(scroll, columns.left());

		IFieldEditor overflowScroll = IonicFieldEditorFactory.createOverflowScrollEditor();
		addEditor(overflowScroll, columns.right());

		IFieldEditor direction = IonicFieldEditorFactory.createDirectionEditor();
		addEditor(direction, columns.left());

		IFieldEditor starty = IonicFieldEditorFactory.createStartYEditor();
		addEditor(starty, columns.right());

		IFieldEditor scrollbarx = IonicFieldEditorFactory.createScrollbarXEditor();
		addEditor(scrollbarx, columns.left());

		IFieldEditor scrollbary = IonicFieldEditorFactory.createScrollbarYEditor();
		addEditor(scrollbary, columns.right());

		createSeparator(parent);

		IFieldEditor onscroll = IonicFieldEditorFactory.createOnScrollEditor();
		addEditor(onscroll, parent);

		IFieldEditor onscrollcomplete = IonicFieldEditorFactory.createOnScrollCompleteEditor();
		addEditor(onscrollcomplete, parent);

		updateScrollEnablement();
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String name = evt.getPropertyName();
		if(ATTR_SCROLL.equals(name) 
				|| ATTR_DIRECTION.equals(name)
				|| ATTR_OVERFLOW_SCROLL.equals(name)) {
			updateScrollEnablement();
		}
		super.propertyChange(evt);
	}

	void updateScrollEnablement() {
		boolean scrollEnabled = isTrue(ATTR_SCROLL);
		boolean ionicScrollEnabled = scrollEnabled && !isTrue(ATTR_OVERFLOW_SCROLL);
		String direction = getEditorValue(ATTR_DIRECTION);
		boolean xEnabled = direction.indexOf("x") >= 0;
		boolean yEnabled = direction.indexOf("y") >= 0 || direction.length() == 0;
		setEnabled(ATTR_DIRECTION, ionicScrollEnabled);
		setEnabled(ATTR_OVERFLOW_SCROLL, scrollEnabled);
		setEnabled(ATTR_SCROLLBAR_X, ionicScrollEnabled && xEnabled);
		setEnabled(ATTR_SCROLLBAR_Y, ionicScrollEnabled && yEnabled);
		setEnabled(ATTR_START_Y, ionicScrollEnabled);
		setEnabled(ATTR_ON_SCROLL, scrollEnabled);
		setEnabled(ATTR_ON_SCROLL_COMPLETE, scrollEnabled);
	}

}