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
package org.jboss.tools.jst.web.ui.internal.properties.html;

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.jst.web.html.HTMLConstants;
import org.jboss.tools.jst.web.html.JQueryHTMLConstants;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.AbstractAdvancedPropertySetViewer.Entry;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil.TwoColumns;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class HTMLLayouts implements HTMLConstants {
	HTMLPropertySetViewer v;

	public HTMLLayouts(HTMLPropertySetViewer v) {
		this.v = v;
	}

	public void layoutImage(Composite fields, List<Entry> entries) {
		v.layoutEditor(ATTR_SRC, fields);
		v.layoutEditor(ATTR_ID, fields);
		v.layoutEditor(ATTR_ALT, fields);
		TwoColumns cs = LayoutUtil.createTwoColumns(fields);
		v.layoutEditor(ATTR_WIDTH, cs.left());
		v.layoutEditor(ATTR_HEIGHT, cs.right());
		v.layoutEditor(ATTR_ISMAP, fields);
		v.layoutEditor(ATTR_USEMAP, fields);
		v.layoutEditor(ATTR_CROSSORIGIN, fields);
	}

	public void layoutTextArea(Composite fields, List<Entry> entries) {
		v.layoutEditor(ATTR_ID, fields);
		v.layoutEditor(ATTR_NAME, fields);
		LayoutUtil.createSeparator(fields);
		v.layoutEditor(JQueryHTMLConstants.ATTR_PLACEHOLDER, fields);
		v.layoutEditor(JQueryHTMLConstants.ATTR_MAXLENGTH, fields);
		TwoColumns cs = LayoutUtil.createTwoColumns(fields);
		v.layoutEditor(JQueryHTMLConstants.ATTR_REQUIRED, cs.left());
		v.layoutEditor(ATTR_READONLY, cs.right());
		v.layoutEditor(ATTR_DISABLED, cs.left());
		v.layoutEditor(JQueryHTMLConstants.ATTR_AUTOFOCUS, cs.right());
		v.layoutEditor(ATTR_COLS, cs.left());
		v.layoutEditor(ATTR_ROWS, cs.right());
		LayoutUtil.createSeparator(fields);
		v.layoutEditor(ATTR_FORM, fields);
		v.layoutEditor(ATTR_WRAP, fields);
	}

	public void layoutInputText(Composite fields, List<Entry> entries) {
		v.layoutEditor(ATTR_ID, fields);
		v.layoutEditor(ATTR_TYPE, fields);
		LayoutUtil.createSeparator(fields);
		v.layoutEditor(ATTR_NAME, fields);
		v.layoutEditor(ATTR_VALUE, fields);
		LayoutUtil.createSeparator(fields);
		if(!v.context.isRangeType()) {
			v.layoutEditor(JQueryHTMLConstants.ATTR_PLACEHOLDER, fields);
			v.layoutEditor(JQueryHTMLConstants.ATTR_MAXLENGTH, fields);
			v.layoutEditor(JQueryHTMLConstants.ATTR_PATTERN, fields);
			LayoutUtil.createSeparator(fields);
		}
		TwoColumns cs = LayoutUtil.createTwoColumns(fields);
		v.layoutEditor(JQueryHTMLConstants.ATTR_REQUIRED, cs.left());
		v.layoutEditor(ATTR_READONLY, cs.right());
		v.layoutEditor(ATTR_DISABLED, cs.left());
		v.layoutEditor(JQueryHTMLConstants.ATTR_AUTOFOCUS, cs.right());
		if(v.context.isFileType()) {
			v.layoutEditor(ATTR_MULTIPLE, cs.left());
			JQueryFieldEditorFactory.createSpan("file-span", 3).doFillIntoGrid(cs.right());
		}
		if(v.context.isNumberType() || v.context.isRangeType()) {
			Composite[] cs3 = LayoutUtil.createColumns(fields, 3);
			v.layoutEditor(JQueryHTMLConstants.ATTR_DATA_MIN, cs3[0]);
			v.layoutEditor(JQueryHTMLConstants.ATTR_DATA_MAX, cs3[1]);
			v.layoutEditor(JQueryHTMLConstants.ATTR_DATA_STEP, cs3[2]);
		}
		LayoutUtil.createSeparator(fields);
		v.layoutEditor(ATTR_FORM, fields);
	}

	public void layoutInputCheckbox(Composite fields, List<Entry> entries) {
		v.layoutEditor(ATTR_ID, fields);
		v.layoutEditor(ATTR_TYPE, fields);
		LayoutUtil.createSeparator(fields);
		v.layoutEditor(ATTR_NAME, fields);
		v.layoutEditor(ATTR_VALUE, fields);
		LayoutUtil.createSeparator(fields);
		TwoColumns cs = LayoutUtil.createTwoColumns(fields);
		v.layoutEditor(CHECKED, cs.left());
		v.layoutEditor(ATTR_DISABLED, cs.right());
		v.layoutEditor(JQueryHTMLConstants.ATTR_AUTOFOCUS, cs.left());
		JQueryFieldEditorFactory.createSpan("checkbox-span", 3).doFillIntoGrid(cs.right());
		LayoutUtil.createSeparator(fields);
		v.layoutEditor(ATTR_FORM, fields);
	}

	public void layoutSelect(Composite fields, List<Entry> entries) {
		v.layoutEditor(ATTR_ID, fields);
		v.layoutEditor(ATTR_NAME, fields);
		LayoutUtil.createSeparator(fields);
		TwoColumns cs = LayoutUtil.createTwoColumns(fields);
		v.layoutEditor(ATTR_DISABLED, cs.left());
		v.layoutEditor(JQueryHTMLConstants.ATTR_AUTOFOCUS, cs.right());
		if(v.hasEditor(JQueryHTMLConstants.ATTR_REQUIRED)) {
			v.layoutEditor(JQueryHTMLConstants.ATTR_REQUIRED, cs.left());
		} else {
			JQueryFieldEditorFactory.createSpan("select-span", 3).doFillIntoGrid(cs.left());
		}
		v.layoutEditor(ATTR_MULTIPLE, cs.right());
		LayoutUtil.createSeparator(fields);
		v.layoutEditor(ATTR_SIZE, fields);
		v.layoutEditor(ATTR_FORM, fields);
	}

}
