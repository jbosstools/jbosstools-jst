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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.common.ui.widget.editor.SwtFieldEditorFactory;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil.TwoColumns;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewScrollWizardPage extends NewIonicWidgetWizardPage {

	public NewScrollWizardPage() {
		super("newScroll", IonicWizardMessages.newScrollWizardTitle);
		setDescription(IonicWizardMessages.newScrollWizardDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		IFieldEditor delegate = IonicFieldEditorFactory.createDelegateHandleEditor();
		addEditor(delegate, parent);

		createIDEditor(parent, true);
		addID.setValue(Boolean.FALSE);

		createSeparator(parent);

		TwoColumns columns = createTwoColumns(parent);

		IFieldEditor direction = IonicFieldEditorFactory.createDirectionEditor();
		addEditor(direction, columns.left());

		IFieldEditor hasBouncing = IonicFieldEditorFactory.createHasBouncingEditor();
		addEditor(hasBouncing, columns.right());

		IFieldEditor scrollbarx = IonicFieldEditorFactory.createScrollbarXEditor();
		addEditor(scrollbarx, columns.left());

		IFieldEditor paging = IonicFieldEditorFactory.createPagingEditor();
		addEditor(paging, columns.right());

		IFieldEditor scrollbary = IonicFieldEditorFactory.createScrollbarYEditor();
		addEditor(scrollbary, columns.left());

		if(parent != null) {
		IFieldEditor span = SwtFieldEditorFactory.INSTANCE.createCheckboxEditor("span1", "span1", false, "");
		addEditor(span, columns.right());
			for (Object o: span.getEditorControls()) {
				if(o instanceof Control) {
					((Control)o).setVisible(false);
				}
			}
		}

		createSeparator(parent);

		IFieldEditor zooming = IonicFieldEditorFactory.createZoomingEditor();
		addEditor(zooming, parent);

		columns = createTwoColumns(parent);

		IFieldEditor minzoom = IonicFieldEditorFactory.createMinZoomEditor();
		addEditor(minzoom, columns.left());

		IFieldEditor maxzoom = IonicFieldEditorFactory.createMaxZoomEditor();
		addEditor(maxzoom, columns.right());

		createSeparator(parent);

		IFieldEditor onscroll = IonicFieldEditorFactory.createOnScrollEditor();
		addEditor(onscroll, parent);

		IFieldEditor onrefresh = IonicFieldEditorFactory.createOnRefreshEditor();
		addEditor(onrefresh, parent);

		updateScrollEnablement();
		updateZoomingEnablement();
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String name = evt.getPropertyName();
		if(ATTR_DIRECTION.equals(name)) {
			updateScrollEnablement();
		} else if(ATTR_ZOOMING.equals(name)) {
			updateZoomingEnablement();
		}
		super.propertyChange(evt);
	}

	void updateScrollEnablement() {
		String direction = getEditorValue(ATTR_DIRECTION);
		boolean xEnabled = direction.indexOf("x") >= 0;
		boolean yEnabled = direction.indexOf("y") >= 0 || direction.length() == 0;
		setEnabled(ATTR_SCROLLBAR_X, xEnabled);
		setEnabled(ATTR_SCROLLBAR_Y, yEnabled);
	}

	void updateZoomingEnablement() {
		boolean enabled = isTrue(ATTR_ZOOMING);
		setEnabled(ATTR_MIN_ZOOM, enabled);
		setEnabled(ATTR_MAX_ZOOM, enabled);
	}
}