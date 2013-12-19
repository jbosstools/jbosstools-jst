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
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.html.JQueryHTMLConstants;
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileVersion;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.PagePaletteContents;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.AbstractAdvancedPropertySetViewer;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.IFieldEditorProvider;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.WizardDescriptions;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class JQueryPropertySetViewer extends AbstractAdvancedPropertySetViewer implements JQueryHTMLConstants {
	JQueryPropertySetContext context = new JQueryPropertySetContext(this);
	JQueryLayouts layouts = new JQueryLayouts(this);

	public JQueryPropertySetViewer() {
	}

	@Override
	public String getCategoryDisplayName() {
		return "jQuery";
	}

	protected JQueryMobileVersion getVersion() {
		PagePaletteContents contents = (PagePaletteContents)model.getWorkbenchPart().getAdapter(PagePaletteContents.class);
		if(contents != null) {
			String version = contents.getVersion(JQueryConstants.JQM_CATEGORY);
			for (JQueryMobileVersion v: JQueryMobileVersion.ALL_VERSIONS) {
				if(v.toString().equals(version)) {
					return v;
				}
			}
		}
		return JQueryMobileVersion.getLatestDefaultVersion();
	}


	@Override
	protected boolean isStructureChanged(List<IPropertyDescriptor> descriptors) {
		return context.update();
	}

	public JQueryPropertySetContext getContext() {
		return context;
	}

	@Override
	protected List<IPropertyDescriptor> getFilteredDescriptors(List<IPropertyDescriptor> descriptors) {
		List<IPropertyDescriptor> result = new ArrayList<IPropertyDescriptor>();
		for (IPropertyDescriptor d: descriptors) {
			if(category != null && category.equals(d.getCategory())) {
				result.add(d);
			}
		}
		return result;
	}

	@Override
	protected void layoutEditors(Composite fields, List<Entry> entries) {
		if(layouts.isButtonLayout()) {
			layouts.layoutButton(fields, entries);
		} else if(context.isInput() && "range".equals(context.getTypeName())) {
			layouts.layoutRange(fields, entries);
		} else if(context.isTag(TAG_SELECT)) {
			layouts.layoutSelect(fields, entries);
		} else if(context.isPage()) {
			layouts.layoutPage(fields, entries);
		} else if(context.isCollapsible() || context.isCollapsibleSet()) {
			layouts.layoutCollapsible(fields, entries);
		} else if(context.isControlGroup()) {
			layouts.layoutControlgroup(fields, entries);
		} else if(context.isDialog()) {
			layouts.layoutDialog(fields, entries);
		} else if(context.isListview()) {
			layouts.layoutListview(fields, entries);
		} else if(context.isPanel()) {
			layouts.layoutPanel(fields, entries);
		} else if(context.isPopup()) {
			layouts.layoutPopup(fields, entries);
		} else if(context.isTag(TAG_TABLE) && context.isTableRole()) {
			layouts.layoutTable(fields, entries);
		} else if(context.isHeader() || context.isFooter()) {
			layouts.layoutToolbar(fields, entries);
		} else {
			if(hasEditor(ATTR_DATA_ROLE)) {
				layoutEditor(getEditor(ATTR_DATA_ROLE), fields);
				if(entries.size() > 1) {
					LayoutUtil.createSeparator(fields);
				}
			}
		}
		for (Entry e: entries) {
			if(!e.isLayout()) {
				layoutEditor(e, fields, true);
			}
		}
	}

	protected String toVisualValue(String modelValue, IPropertyDescriptor d) {
		if(isPositionFixedAttr(d.getId())) {
			return (FIXED.equals(modelValue)) ? TRUE : FALSE;
		} else if(ATTR_DATA_DIRECTION.equals(d.getId())) {
			return (REVERSE.equals(modelValue)) ? TRUE : FALSE;
		} 
		return modelValue;
	}
	
	protected Object toModelValue(Object visualValue, IPropertyDescriptor d) {
		if(isPositionFixedAttr(d.getId())) {
			if(visualValue != null && TRUE.equals(visualValue.toString())) {
				return FIXED;
			} else {
				return null;
			}
		} else if(ATTR_DATA_DIRECTION.equals(d.getId())) {
			if(visualValue != null && TRUE.equals(visualValue.toString())) {
				return REVERSE;
			} else {
				return null;
			}
		}
		return visualValue;
	}

	private boolean isPositionFixedAttr(Object id) {
		return (ATTR_DATA_POSITION.equals(id) && !context.isPanel()) || ATTR_DATA_POSITION_FIXED.equals(id);
	}

	@Override
	protected void initEditorProviders() {
		editorProviders.put(ATTR_DATA_ADD_BACK_BUTTON, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createAddBackButton();
			}
		});

		editorProviders.put(ATTR_DATA_AJAX, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createAjaxEditor();
			}
		});

		editorProviders.put(ATTR_DATA_AUTODIVIDERS, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createAutodividersEditor();
			}
		});

		editorProviders.put(ATTR_DATA_BACK_BUTTON_TEXT, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createBackButtonTextEditor();
			}
		});

		editorProviders.put(ATTR_DATA_BACK_BUTTON_THEME, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createBackButtonThemeEditor(getVersion());
			}
		});

		editorProviders.put(ATTR_DATA_CLEAR_BTN, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createClearInputEditor();
			}
		});

		editorProviders.put(ATTR_DATA_CLEAR_BTN_TEXT, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createClearButtonTextEditor();
			}
		});

		editorProviders.put(ATTR_DATA_CLOSE_BTN, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createCloseButtonEditor();
			}
		});

		editorProviders.put(ATTR_DATA_CLOSE_BTN_TEXT, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createCloseButtonTextEditor();
			}
		});

		editorProviders.put(ATTR_DATA_COLLAPSED, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createCollapsedEditor();
			}
		});

		editorProviders.put(ATTR_DATA_COLLAPSED_ICON, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createCollapsedIconEditor();
			}
		});

		editorProviders.put(ATTR_DATA_COLUMN_BUTTON_TEXT, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createColumnButtonTextEditor();
			}
		});

		editorProviders.put(ATTR_DATA_COLUMN_BUTTON_THEME, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createColumnButtonThemeEditor(getVersion());
			}
		});

		editorProviders.put(ATTR_DATA_COLUMN_POPUP_THEME, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createColumnPopupThemeEditor(getVersion());
			}
		});

		editorProviders.put(ATTR_DATA_CONTENT_THEME, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createDataContentThemeEditor();
			}
		});

		editorProviders.put(ATTR_DATA_CORNERS, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createCornersEditor();
			}
		});

		editorProviders.put(ATTR_DATA_COUNT_THEME, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createThemeEditorForID(ATTR_DATA_COUNT_THEME);
			}
		});

		editorProviders.put(ATTR_DATA_DIRECTION, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createReverseEditor();
			}
		});

		editorProviders.put(ATTR_DATA_DISMISSABLE, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createDismissableEditor("");
			}
		});

		editorProviders.put(ATTR_DATA_DISPLAY, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createPanelDisplayEditor();
			}
		});

		editorProviders.put(ATTR_DATA_DIVIDER_THEME, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createDividerThemeEditor(getVersion());
			}
		});

		editorProviders.put(ATTR_DATA_DOM_CACHE, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createDomCacheEditor();
			}
		});

		editorProviders.put(ATTR_DATA_ENHANCE, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createEnhance();
			}
		});

		editorProviders.put(ATTR_DATA_EXPANDED_ICON, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createExpandedIconEditor();
			}
		});

		editorProviders.put(ATTR_DATA_FILTER, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createSearchFilterEditor();
			}
		});

		editorProviders.put(ATTR_DATA_FILTER_PLACEHOLDER, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createFilterPlaceholderEditor();
			}
		});

		editorProviders.put(ATTR_DATA_FILTER_THEME, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createThemeEditorForID(ATTR_DATA_FILTER_THEME);
			}
		});

		editorProviders.put(ATTR_DATA_FULL_SCREEN, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				String description = context.isHeader()
						? WizardDescriptions.headerFullScreen : WizardDescriptions.footerFullScreen;
				return JQueryFieldEditorFactory.createFullScreenEditor(description);
			}
		});

		editorProviders.put(ATTR_DATA_HEADER_THEME, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createThemeEditorForID(ATTR_DATA_HEADER_THEME);
			}
		});

		editorProviders.put(ATTR_DATA_HIGHLIGHT, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createHighlightEditor();
			}
		});

		editorProviders.put(ATTR_DATA_ICON, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createIconEditor();
			}
		});

		editorProviders.put(ATTR_DATA_ICONPOS, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				//TODO add text only
				return JQueryFieldEditorFactory.createIconPositionEditor();
			}
		});

		editorProviders.put(ATTR_DATA_ICON_SHADOW, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createIconShadowEditor();
			}
		});

		editorProviders.put(ATTR_DATA_ID, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createDataIDEditor();
			}
		});

		editorProviders.put(ATTR_DATA_INLINE, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createInlineEditor();
			}
		});

		editorProviders.put(ATTR_DATA_INSET, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				String description = context.isListview()
						? WizardDescriptions.listviewInset : WizardDescriptions.collapsibleInset;
				return JQueryFieldEditorFactory.createInsetEditor(description);
			}
		});

		editorProviders.put(ATTR_DATA_MINI, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createMiniEditor();
			}
		});

		editorProviders.put(ATTR_DATA_MODE, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createTableModeEditor();
			}
		});

		editorProviders.put(ATTR_DATA_NATIVE_MENU, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createNativeMenuEditor();
			}
		});

		editorProviders.put(ATTR_DATA_OVERLAY_THEME, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createOverlayThemeEditor(getVersion());
			}
		});

		editorProviders.put(ATTR_DATA_PLACEHOLDER, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createDataPlaceholderEditor();
			}
		});

		editorProviders.put(ATTR_DATA_POSITION, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				if(context.isPanel()) {
					return JQueryFieldEditorFactory.createPanelPositionEditor();
				} else {
					String description = context.isHeader()
							? WizardDescriptions.headerFixedPosition : WizardDescriptions.footerFixedPosition;
					return JQueryPropertiesFieldEditorFactory.createFixedPositionEditor(description);
				}
			}
		});

		editorProviders.put(ATTR_DATA_POSITION_FIXED, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createFixedPositionEditor();
			}
		});

		editorProviders.put(ATTR_DATA_POSITION_TO, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createPositionToEditor();
			}
		});

		editorProviders.put(ATTR_DATA_PREFETCH, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createPrefetchEditor();
			}
		});

		editorProviders.put(ATTR_DATA_PRIORITY, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createPriorityEditor();
			}
		});

		editorProviders.put(ATTR_DATA_REL, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createActionEditor();
			}
		});

		editorProviders.put(ATTR_DATA_ROLE,	new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createDataRoleEditor(context.getElementName());
			}
		});

		editorProviders.put(ATTR_DATA_SHADOW, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createShadowEditor();
			}
		});


		editorProviders.put(ATTR_DATA_SPLIT_ICON, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createIconEditor(ATTR_DATA_SPLIT_ICON);
			}
		});

		editorProviders.put(ATTR_DATA_SPLIT_THEME, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createThemeEditorForID(ATTR_DATA_SPLIT_THEME);
			}
		});

		editorProviders.put(ATTR_DATA_SWIPE_CLOSE, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createSwipeCloseEditor();
			}
		});

		editorProviders.put(ATTR_DATA_THEME, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createDataThemeEditor(getVersion(), context.getRoleName());
			}
		});

		editorProviders.put(ATTR_DATA_TITLE, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createDataTitleEditor();
			}
		});

		editorProviders.put(ATTR_DATA_TOLERANCE, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createToleranceEditor();
			}
		});

		editorProviders.put(ATTR_DATA_TRACK_THEME, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createDataTrackThemeEditor(getVersion(), WizardDescriptions.rangeSliderTrackTheme);
			}
		});

		editorProviders.put(ATTR_DATA_TRANSITION, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				String description = context.isPopup() ? WizardDescriptions.popupTransition : WizardDescriptions.transition;
				return JQueryPropertiesFieldEditorFactory.createTransitionEditor(description);
			}
		});

		editorProviders.put(ATTR_DATA_TYPE, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createDataTypeEditor();
			}
		});

		editorProviders.put(ATTR_DATA_URL, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryPropertiesFieldEditorFactory.createDataURLEditor();
			}
		});

	}

}
