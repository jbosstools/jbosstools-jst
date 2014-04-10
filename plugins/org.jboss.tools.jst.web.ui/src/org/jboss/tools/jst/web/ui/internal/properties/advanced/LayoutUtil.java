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
package org.jboss.tools.jst.web.ui.internal.properties.advanced;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class LayoutUtil {
	
	public static Combo findCombo(IFieldEditor editor) {
		for (Object o: editor.getEditorControls()) {
			if(o instanceof Combo) {
				return (Combo)o; 
			}
		}
		return null;
	}

	public static Text findText(IFieldEditor editor) {
		for (Object o: editor.getEditorControls()) {
			if(o instanceof Text) {
				return (Text)o; 
			}
		}
		return null;
	}

	public static void expandCombo(IFieldEditor editor) {
		Combo c = findCombo(editor);
		if(c != null) {
			GridData d = (GridData)c.getLayoutData();
			d.horizontalAlignment = SWT.FILL;
			d.grabExcessHorizontalSpace = true;
			c.setLayoutData(d);
		}
	}

	public static void createSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData sd = new GridData(GridData.FILL_HORIZONTAL);
		sd.horizontalSpan = 3;
		separator.setLayoutData(sd);
	}

	/**
	 * Creates group for the parent that has grid layout with 3 columns
	 * and layouts it with grid layout with 3 columns.
	 * 
	 * @param parent
	 * @param label
	 * @return
	 */
	public static Group createGroup(Composite parent, String label) {
		Group g = new Group(parent,SWT.BORDER);
		g.setText(label);
		GridData d = new GridData(GridData.FILL_HORIZONTAL);
		d.horizontalSpan = 3;
		g.setLayoutData(d);
		g.setLayout(new GridLayout(3, false));		
		return g;
	}

	/**
	 * Creates panel for the parent that has grid layout with 3 columns
	 * and layouts it with grid layout with 3 columns.
	 * 
	 * @param parent
	 * @param label
	 * @return
	 */
	public static Composite createPanel(Composite parent) {
		Composite g = new Composite(parent,SWT.BORDER);
		GridData d = new GridData(GridData.FILL_HORIZONTAL);
		d.horizontalSpan = 3;
		g.setLayoutData(d);
		g.setLayout(new GridLayout(3, false));		
		return g;
	}

	public static Composite[] createColumns(Composite parent, int k) {
		Composite all = null;
		if(parent != null) {
		all = new Composite(parent, SWT.NONE);
		GridData d = new GridData(GridData.FILL_HORIZONTAL);
		d.horizontalSpan = 3;
		all.setLayoutData(d);
		GridLayout layout = new GridLayout(k, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 20;
		all.setLayout(layout);
		}

		Composite[] result = new Composite[k];

		for (int i = 0; i < k; i++) {
			Composite c = new Composite(all, SWT.NONE);
			c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			GridLayout layout = new GridLayout(3, false);
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
