/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.html.jquery.wizard;

import java.beans.PropertyChangeEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.common.model.ui.editors.dnd.ValidationException;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewJQueryWidgetWizardPage extends AbstractNewHTMLWidgetWizardPage implements JQueryConstants {
	IFieldEditor addID = null;
	IFieldEditor id = null;

	public NewJQueryWidgetWizardPage(String pageName, String title) {
		super(pageName, title);
	}

	protected void createIDEditor(Composite parent, boolean enabling) {
		if(!enabling) {
			id = JQueryFieldEditorFactory.createIDEditor();
			addEditor(id, parent);
		} else {
			addID = JQueryFieldEditorFactory.createAddIDEditor();
			addEditor(addID, parent);
			id = JQueryFieldEditorFactory.createIDEditor2();
			addEditor(id, parent);
			updateIDEnablement();
		}
	}

	protected boolean isTrue(String editorID) {
		return TRUE.equals(getEditorValue(editorID));
	}

	public boolean isIDEnabled() {
		return id != null && (addID == null || TRUE.equals(addID.getValueAsString()));
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if(EDITOR_ID_ADD_ID.equals(evt.getPropertyName())) {
			updateIDEnablement();
		}
		super.propertyChange(evt);
	}

	public void validate() throws ValidationException {
		if(isIDEnabled()) {
			String id = getEditorValue(EDITOR_ID_ID);
			if(id != null && !getWizard().isIDAvailable(id)) {
				throw new ValidationException(WizardMessages.errorIDisUsed);
			}
		}
	}

	protected void updateIDEnablement() {
		if(addID != null && id != null) {
			id.setEnabled(isIDEnabled());
		}
	}

	protected void setEnabled(String editorName, boolean value) {
		IFieldEditor editor = getEditor(editorName);
		if(editor != null) {
			editor.setEnabled(value);
		}
	}

	public static Composite[] createColumns(Composite parent, int k) {
		Composite all = new Composite(parent, SWT.NONE);
		GridData d = new GridData(GridData.FILL_HORIZONTAL);
		d.horizontalSpan = 3;
		all.setLayoutData(d);
		GridLayout layout = new GridLayout(k, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 20;
		all.setLayout(layout);

		Composite[] result = new Composite[k];

		for (int i = 0; i < k; i++) {
			Composite c = new Composite(all, SWT.NONE);
			c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			layout = new GridLayout(3, false);
			layout.marginWidth = 0;
			layout.marginHeight = 0;
			c.setLayout(layout);
			result[i] = c;
		}

		return result;
	}

	public static TwoColumns createTwoColumns(Composite parent) {
		Composite[] columns = createColumns(parent, 2);
		return new TwoColumns(columns[0], columns[1]);
	}

	public static class TwoColumns {
		private Composite left;
		private Composite right;
		
		public TwoColumns(Composite left, Composite right) {
			this.left = left;
			this.right = right;
		}

		public Composite left() {
			return left;
		}
	
		public Composite right() {
			return right;
		}
	}

}
