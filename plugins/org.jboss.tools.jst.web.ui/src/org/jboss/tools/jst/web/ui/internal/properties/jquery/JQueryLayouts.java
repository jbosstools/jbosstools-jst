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
package org.jboss.tools.jst.web.ui.internal.properties.jquery;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jboss.tools.jst.web.html.JQueryHTMLConstants;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.AbstractAdvancedPropertySetViewer.Entry;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil.TwoColumns;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class JQueryLayouts implements JQueryHTMLConstants {
	JQueryPropertySetViewer v;

	public JQueryLayouts(JQueryPropertySetViewer v) {
		this.v = v;
	}

	public boolean isButtonLayout() {
		JQueryPropertySetContext context = v.getContext();
		return (context.isButtonRole() || context.isTag(TAG_A) || context.isTag(TAG_BUTTON)
				|| (context.isInput() && context.isButtonType()));
	}

	public void layoutButton(Composite fields, List<Entry> entries) {
		v.layoutEditor(ATTR_DATA_ROLE, fields);
		if(v.hasEditor(ATTR_DATA_ROLE)) {
			LayoutUtil.createSeparator(fields);
		}
		v.layoutEditor(ATTR_DATA_REL, fields);
		v.layoutEditor(ATTR_DATA_TRANSITION, fields);
		v.layoutEditor(ATTR_DATA_POSITION_TO, fields);
		boolean reverse = v.hasEditor(ATTR_DATA_DIRECTION);
		boolean prefetch = v.hasEditor(ATTR_DATA_PREFETCH);
		if(reverse && prefetch) {
			TwoColumns cs = LayoutUtil.createTwoColumns(fields);
			v.layoutEditor(ATTR_DATA_PREFETCH, cs.left());
			v.layoutEditor(ATTR_DATA_DIRECTION, cs.right());
		} else {
			v.layoutEditor(ATTR_DATA_PREFETCH, fields);
			v.layoutEditor(ATTR_DATA_DIRECTION, fields);
		}
		if(v.hasEditor(ATTR_DATA_REL) || reverse || prefetch) {
			LayoutUtil.createSeparator(fields);
		}
		if(v.hasEditor(ATTR_DATA_MINI)) {
			TwoColumns cs = LayoutUtil.createTwoColumns(fields);
			v.layoutEditor(ATTR_DATA_MINI, cs.left());
			v.layoutEditor(ATTR_DATA_CORNERS, cs.right());
			v.layoutEditor(ATTR_DATA_INLINE, cs.left());
			v.layoutEditor(ATTR_DATA_SHADOW, cs.right());
			LayoutUtil.createSeparator(fields);
		}

		if(v.hasEditor(ATTR_DATA_ICON)) {
			v.layoutEditor(ATTR_DATA_ICON, fields);
			v.layoutEditor(ATTR_DATA_ICONPOS, fields);
			v.layoutEditor(ATTR_DATA_ICON_SHADOW, fields);
			LayoutUtil.createSeparator(fields);
		}

		if(v.hasEditor(ATTR_DATA_THEME)) {
			v.layoutEditor(ATTR_DATA_THEME, fields);
		}

		layoutAjaxEnhanceDom(fields, true);
	}

	public void layoutCollapsible(Composite fields, List<Entry> entries) {
		v.layoutEditor(ATTR_DATA_ROLE, fields);
		LayoutUtil.createSeparator(fields);
		TwoColumns cs = LayoutUtil.createTwoColumns(fields);
		v.layoutEditor(ATTR_DATA_MINI, cs.left());
		v.layoutEditor(ATTR_DATA_INSET, cs.right());
		if(v.hasEditor(ATTR_DATA_COLLAPSED)) {
			v.layoutEditor(ATTR_DATA_COLLAPSED, cs.left());
			JQueryFieldEditorFactory.createSpan("xx", 3).doFillIntoGrid(cs.right());
		}
		LayoutUtil.createSeparator(fields);
		v.layoutEditor(ATTR_DATA_COLLAPSED_ICON, fields);
		v.layoutEditor(ATTR_DATA_EXPANDED_ICON, fields);
		v.layoutEditor(ATTR_DATA_ICONPOS, fields);
		LayoutUtil.createSeparator(fields);
		v.layoutEditor(ATTR_DATA_THEME, fields);
		v.layoutEditor(ATTR_DATA_CONTENT_THEME, fields);
		layoutAjaxEnhanceDom(fields, true);
	}

	public void layoutControlgroup(Composite fields, List<Entry> entries) {
		v.layoutEditor(ATTR_DATA_ROLE, fields);
		LayoutUtil.createSeparator(fields);
		v.layoutEditor(ATTR_DATA_TYPE, fields);
		v.layoutEditor(ATTR_DATA_MINI, fields);
		layoutAjaxEnhanceDom(fields, true);
	}

	public void layoutDialog(Composite fields, List<Entry> entries) {
		v.layoutEditor(ATTR_DATA_ROLE, fields);
		v.layoutEditor(ATTR_DATA_TITLE, fields);
		LayoutUtil.createSeparator(fields);
		v.layoutEditor(ATTR_DATA_CORNERS, fields);
		v.layoutEditor(ATTR_DATA_CLOSE_BTN, fields);
		v.layoutEditor(ATTR_DATA_CLOSE_BTN_TEXT, fields);
		LayoutUtil.createSeparator(fields);
		v.layoutEditor(ATTR_DATA_THEME, fields);
		v.layoutEditor(ATTR_DATA_OVERLAY_THEME, fields);
		LayoutUtil.createSeparator(fields);
		layoutAjaxEnhanceDom(fields, true);
	}

	public void layoutPage(Composite fields, List<Entry> entries) {
		v.layoutEditor(ATTR_DATA_ROLE, fields);
		v.layoutEditor(ATTR_DATA_TITLE, fields);
		LayoutUtil.createSeparator(fields);

		v.layoutEditor(ATTR_DATA_CLOSE_BTN_TEXT, fields);
		v.layoutEditor(ATTR_DATA_ADD_BACK_BUTTON, fields);
		Composite backParent = LayoutUtil.createPanel(fields);
		v.layoutEditor(ATTR_DATA_BACK_BUTTON_TEXT, backParent);
		v.layoutEditor(ATTR_DATA_BACK_BUTTON_THEME, backParent);

		LayoutUtil.createSeparator(fields);

		v.layoutEditor(ATTR_DATA_THEME, fields);
		v.layoutEditor(ATTR_DATA_OVERLAY_THEME, fields);

		layoutAjaxEnhanceDom(fields, true);
	}

	public void layoutPanel(Composite fields, List<Entry> entries) {
		v.layoutEditor(ATTR_DATA_ROLE, fields);
		LayoutUtil.createSeparator(fields);
		v.layoutEditor(ATTR_DATA_DISPLAY, fields);
		v.layoutEditor(ATTR_DATA_POSITION, fields);
		TwoColumns cs = LayoutUtil.createTwoColumns(fields);
		v.layoutEditor(ATTR_DATA_POSITION_FIXED, cs.left());
		JQueryFieldEditorFactory.createSpan("panel-span", 3).doFillIntoGrid(cs.right());
		v.layoutEditor(ATTR_DATA_DISMISSABLE, cs.left());
		v.layoutEditor(ATTR_DATA_SWIPE_CLOSE, cs.right());
		LayoutUtil.createSeparator(fields);
		v.layoutEditor(ATTR_DATA_THEME, fields);
		layoutAjaxEnhanceDom(fields, true);
	}

	public void layoutPopup(Composite fields, List<Entry> entries) {
		v.layoutEditor(ATTR_DATA_ROLE, fields);
		LayoutUtil.createSeparator(fields);

		TwoColumns cs = LayoutUtil.createTwoColumns(fields);
		v.layoutEditor(ATTR_DATA_CORNERS, cs.left());
		v.layoutEditor(ATTR_DATA_SHADOW, cs.right());
		v.layoutEditor(ATTR_DATA_DISMISSABLE, cs.left());
		JQueryFieldEditorFactory.createSpan("popup-span", 3).doFillIntoGrid(cs.right());

		LayoutUtil.createSeparator(fields);

		v.layoutEditor(ATTR_DATA_THEME, fields);
		v.layoutEditor(ATTR_DATA_OVERLAY_THEME, fields);

		LayoutUtil.createSeparator(fields);
		v.layoutEditor(ATTR_DATA_TOLERANCE, fields);
		layoutAjaxEnhanceDom(fields, false);
	}

	public void layoutTable(Composite fields, List<Entry> entries) {
		v.layoutEditor(ATTR_DATA_ROLE, fields);
		LayoutUtil.createSeparator(fields);
		
		Group g = LayoutUtil.createGroup(fields, WizardMessages.columnLabel);
		v.layoutEditor(ATTR_DATA_COLUMN_BUTTON_TEXT, g);
		v.layoutEditor(ATTR_DATA_COLUMN_BUTTON_THEME, g);
		v.layoutEditor(ATTR_DATA_COLUMN_POPUP_THEME, g);
	}

	public void layoutListview(Composite fields, List<Entry> entries) {
		v.layoutEditor(ATTR_DATA_ROLE, fields);
		LayoutUtil.createSeparator(fields);
		v.layoutEditor(ATTR_DATA_INSET, fields);
		v.layoutEditor(ATTR_DATA_ICON, fields);

		v.layoutEditor(ATTR_DATA_FILTER, fields);
		Composite g = LayoutUtil.createPanel(fields);
		v.layoutEditor(ATTR_DATA_FILTER_PLACEHOLDER, g);
		v.layoutEditor(ATTR_DATA_FILTER_THEME, g);

		g = LayoutUtil.createGroup(fields, "Dividers");
		v.layoutEditor(ATTR_DATA_AUTODIVIDERS, g);
		v.layoutEditor(ATTR_DATA_DIVIDER_THEME, g);

		v.layoutEditor(ATTR_DATA_THEME, fields);

		g = LayoutUtil.createGroup(fields, "Split");
		v.layoutEditor(ATTR_DATA_SPLIT_ICON, g);
		v.layoutEditor(ATTR_DATA_SPLIT_THEME, g);
		
		g = LayoutUtil.createGroup(fields, "Header");
		v.layoutEditor(ATTR_DATA_HEADER_THEME, g);

		g = LayoutUtil.createGroup(fields, "Count");
		v.layoutEditor(ATTR_DATA_COUNT_THEME, g);
	}

	public void layoutRange(Composite fields, List<Entry> entries) {
		v.layoutEditor(ATTR_DATA_ROLE, fields);
		LayoutUtil.createSeparator(fields);
		TwoColumns cs = LayoutUtil.createTwoColumns(fields);
		v.layoutEditor(ATTR_DATA_MINI, cs.left());
		v.layoutEditor(ATTR_DATA_HIGHLIGHT, cs.right());
		LayoutUtil.createSeparator(fields);
		v.layoutEditor(ATTR_DATA_THEME, fields);
		v.layoutEditor(ATTR_DATA_TRACK_THEME, fields);
	}

	public void layoutSelect(Composite fields, List<Entry> entries) {
		v.layoutEditor(ATTR_DATA_ROLE, fields);
		LayoutUtil.createSeparator(fields);
		TwoColumns cs = LayoutUtil.createTwoColumns(fields);
		v.layoutEditor(ATTR_DATA_MINI, cs.left());
		v.layoutEditor(ATTR_DATA_INLINE, cs.right());
		LayoutUtil.createSeparator(fields);
		v.layoutEditor(ATTR_DATA_ICON, fields);
		v.layoutEditor(ATTR_DATA_ICONPOS, fields);
		LayoutUtil.createSeparator(fields);
		cs = LayoutUtil.createTwoColumns(fields);
		v.layoutEditor(ATTR_DATA_PLACEHOLDER, cs.left());
		v.layoutEditor(ATTR_DATA_NATIVE_MENU, cs.right());
		LayoutUtil.createSeparator(fields);
		v.layoutEditor(ATTR_DATA_THEME, fields);
		v.layoutEditor(ATTR_DATA_DIVIDER_THEME, fields);
		v.layoutEditor(ATTR_DATA_OVERLAY_THEME, fields);
		v.layoutEditor(ATTR_DATA_TRACK_THEME, fields);
	}

	public void layoutToolbar(Composite fields, List<Entry> entries) {
		v.layoutEditor(ATTR_DATA_ROLE, fields);
		LayoutUtil.createSeparator(fields);
		TwoColumns cs = LayoutUtil.createTwoColumns(fields);
		v.layoutEditor(ATTR_DATA_POSITION, cs.left());
		v.layoutEditor(ATTR_DATA_FULL_SCREEN, cs.right());
		v.layoutEditor(ATTR_DATA_THEME, fields);
		LayoutUtil.createSeparator(fields);
		v.layoutEditor(ATTR_DATA_ID, fields);
		layoutAjaxEnhanceDom(fields, false);
	}

	static String[] AJAX_ATTRS = {ATTR_DATA_AJAX, ATTR_DATA_ENHANCE, ATTR_DATA_DOM_CACHE};

	public void layoutAjaxEnhanceDom(Composite fields, boolean addSeparator) {
		List<String> available = new ArrayList<String>();
		for (String id: AJAX_ATTRS) {
			if(v.hasEditor(id)) {
				available.add(id);
			}
		}
		if(available.isEmpty()) {
			return;
		}
		if(addSeparator) {
			LayoutUtil.createSeparator(fields);
		}
		if(available.size() == 1) {
			v.layoutEditor(available.get(0), fields);
		} else {
			TwoColumns cs = LayoutUtil.createTwoColumns(fields);
			v.layoutEditor(available.get(0), cs.left());
			v.layoutEditor(available.get(1), cs.right());
			if(available.size() > 2) {
				v.layoutEditor(available.get(2), cs.left());
				JQueryFieldEditorFactory.createSpan("ajax-span", 3).doFillIntoGrid(cs.right());
			}
		}
		
	}

}
