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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.jboss.tools.common.ui.CommonUIMessages;
import org.jboss.tools.common.ui.widget.editor.ButtonFieldEditor;
import org.jboss.tools.common.ui.widget.editor.CheckBoxFieldEditor;
import org.jboss.tools.common.ui.widget.editor.CompositeEditor;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.common.ui.widget.editor.LabelFieldEditor;
import org.jboss.tools.common.ui.widget.editor.SwtFieldEditorFactory;
import org.jboss.tools.common.ui.widget.editor.TextFieldEditor;
import org.jboss.tools.jst.web.kb.internal.taglib.jq.JQueryMobileAttrProvider;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class JQueryFieldEditorFactory implements JQueryConstants {

	/**
	 * Used in New Form wizard.
	 * @return
	 */
	public static IFieldEditor createNameEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_NAME, WizardMessages.nameLabel, "",
				WizardDescriptions.formName);
	}

	/**
	 * Used in many jQuery Mobile wizards.
	 * @return
	 */
	public static IFieldEditor createLabelEditor() {
		return createLabelEditor(EDITOR_ID_LABEL);
	}

	public static IFieldEditor createLabelEditor(String editorID) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(editorID, WizardMessages.labelLabel, "",
				WizardDescriptions.widgetLabel);
	}

	/**
	 * Used in New Grouped Checkboxes Slider wizard and New Radio wizard.
	 * @return
	 */
	public static IFieldEditor createLegendEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_LEGEND, WizardMessages.legendLabel, "",
				description);
	}

	static String[] THEMES = {"", "a", "b", "c", "d", "e"};

	/**
	 * Used in all jQuery Mobile wizards.
	 * @return
	 */
	public static IFieldEditor createDataThemeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_THEME, WizardMessages.themeLabel, toList(THEMES), "", true,
				WizardDescriptions.widgetTheme);
	}

	/**
	 * Used in New Range Slider wizard and New Toggle wizard.
	 * @return
	 */
	public static IFieldEditor createDataTrackThemeEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_TRACK_THEME, WizardMessages.trackThemeLabel, toList(THEMES), "", true,
				description);
	}

	/**
	 * Used in New Listview wizard.
	 * @return
	 */
	public static IFieldEditor createDividerThemeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_DIVIDER_THEME, WizardMessages.dividerThemeLabel, toList(THEMES), "", true,
				WizardDescriptions.listviewDividerTheme);
	}


	/**
	 * Used in New Collapsible wizard.
	 * @return
	 */
	public static IFieldEditor createDataContentThemeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_CONTENT_THEME, WizardMessages.contentThemeLabel, toList(THEMES), "", true,
				WizardDescriptions.collapsibleContentTheme);
	}

	/**
	 * Used in New in many wizards.
	 * @return
	 */
	public static IFieldEditor createMiniEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_MINI, WizardMessages.miniLabel, false,
				WizardDescriptions.widgetMini);
	}

	/**
	 * Used in New Checkboxes wizard and New RadioEditor.
	 * @return
	 */
	public static IFieldEditor createCheckedEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_CHECKED, WizardMessages.checkedLabel, false,
				description);
	}

	/**
	 * Used in New Select Menu wizard.
	 * @return
	 */
	public static IFieldEditor createSelectedEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_SELECTED, WizardMessages.selectedLabel, false,
				WizardDescriptions.selectMenuSelected);
	}

	/**
	 * Used in New Button wizard, New Popup wizard and New Select Menu wizard.
	 * @return
	 */
	public static IFieldEditor createCornersEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_CORNERS, WizardMessages.cornersLabel, true,
				WizardDescriptions.widgetCorners);
	}

	/**
	 * Used in New Listview wizard.
	 * @return
	 */
	public static IFieldEditor createNumberedEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_NUMBERED, WizardMessages.numberedLabel, false,
				WizardDescriptions.listviewNumbered);
	}

	/**
	 * Used in New Listview wizard.
	 * @return
	 */
	public static IFieldEditor createReadonlyEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_READ_ONLY, WizardMessages.readonlyLabel, false,
				WizardDescriptions.listviewReadonly);
	}

	/**
	 * Used in New Listview wizard.
	 * @return
	 */
	public static IFieldEditor createDividerEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_DIVIDER, WizardMessages.dividerLabel, false,
				WizardDescriptions.listviewDivider);
	}

	/**
	 * Used in New Collapsible wizard and New Listview wizard.
	 * @return
	 */
	public static IFieldEditor createAutodividersEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_AUTODIVIDERS, WizardMessages.autodividersLabel, false,
				WizardDescriptions.listviewAutodividers);
	}

	/**
	 * Used in New Collapsible wizard and New listview wizard.
	 * @return
	 */
	public static IFieldEditor createSearchFilterEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_SEARCH_FILTER, WizardMessages.searchFilterLabel, false,
				WizardDescriptions.listviewSearchFilter);
	}

	/**
	 * Used in New Collapsible wizard and New listview wizard.
	 * @return
	 */
	public static IFieldEditor createInsetEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_INSET, WizardMessages.insetLabel, false,
				description);
	}

	/**
	 * Used in New Toggle wizard.
	 * @return
	 */
	public static IFieldEditor createOffLabelEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_OFF, WizardMessages.offLabelLabel, "Off",
				WizardDescriptions.toggleOffLabel);
	}

	/**
	 * Used in New Toggle wizard.
	 * @return
	 */
	public static IFieldEditor createOnLabelEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_ON, WizardMessages.onLabelLabel, "On",
				WizardDescriptions.toggleOnLabel);
	}

	/**
	 * Used in wizards creating elements that require id.
	 * @return
	 */
	public static IFieldEditor createIDEditor() {
		CompositeEditor editor = (CompositeEditor)SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_ID, WizardMessages.idLabel, "",
				WizardDescriptions.elementId);
		TextFieldEditor text = (TextFieldEditor)editor.getEditors().get(1);
		text.setMessage("Generate");
		return editor; 
	}

	/**
	 * Used in wizards creating elements that do not require id.
	 * @return
	 */
	public static IFieldEditor createAddIDEditor() {
		return createCheckboxEditor(EDITOR_ID_ADD_ID, WizardMessages.idLabel, true, 1,
				WizardDescriptions.elementId);
	}

	public static IFieldEditor createIDEditor2() {
		TextFieldEditor text = (TextFieldEditor)createTextEditor(EDITOR_ID_ID, "", 2);
		text.setMessage("Generate");
		return text;
	}

	/**
	 * Used in New Dialog wizard, New Header wizard, New Footer wizard.
	 * @return
	 */
	public static IFieldEditor createTitleEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_TITLE, WizardMessages.titleLabel, "",
				description);
	}

	/**
	 * Used in New Collapsible wizard.
	 * @return
	 */
	public static IFieldEditor createCollapsibleHeaderEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_HEADER, WizardMessages.headerLabel, "",
				WizardDescriptions.collapsibleHeader);
	}

	static String[] LAYOUT_LIST = {LAYOUT_HORIZONTAL, LAYOUT_VERTICAL};

	/**
	 * Used in New Grouped Buttons wizard, New Grouped Checkboxes wizard,
	 * New Radio wizard, New Select Menu wizard, New Text Input wizard,
	 * New Toggle wizard.
	 * @return
	 */
	public static IFieldEditor createLayoutEditor() {
		return SwtFieldEditorFactory.INSTANCE.createRadioEditor(
				EDITOR_ID_LAYOUT, 
				WizardMessages.layoutLabel, 
				toList(LAYOUT_LIST), 
				toList(LAYOUT_LIST), 
				LAYOUT_HORIZONTAL,
				WizardDescriptions.widgetLayout);
	}

	
	/**
	 * Used in New Button wizard, New Link wizard, ButtonsEditor, ListEditor.
	 * @return
	 */
	public static IFieldEditor createURLEditor() {
		return createURLEditor(EDITOR_ID_URL);
	}

	/**
	 * Used in New Header wizard and createURLEditor().
	 * @return
	 */
	public static IFieldEditor createURLEditor(String editorID) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(editorID, WizardMessages.urlLabel, "",
			WizardDescriptions.href);
	}

	/**
	 * Used in New Image wizard.
	 * @return
	 */
	public static IFieldEditor createSrcEditor(IFile context) {
		return createBrowseWorkspaceImageEditor(EDITOR_ID_SRC, WizardMessages.srcLabel, context,
				WizardDescriptions.imageSrc);
	}

	/**
	 * Used in New Video wizard.
	 * @return
	 */
	public static IFieldEditor createPosterEditor(IFile context) {
		return createBrowseWorkspaceImageEditor(EDITOR_ID_POSTER, WizardMessages.posterLabel, context,
				WizardDescriptions.videoPoster);
	}

	/**
	 * Used in New Video wizard.
	 * @return
	 */
	public static IFieldEditor createVideoSrcEditor(IFile context) {
		return createBrowseWorkspaceVideoEditor(EDITOR_ID_SRC, WizardMessages.srcLabel, context);
	}

	/**
	 * Used in New Audio wizard.
	 * @return
	 */
	public static IFieldEditor createAudioSrcEditor(IFile context) {
		return createBrowseWorkspaceAudioEditor(EDITOR_ID_SRC, WizardMessages.srcLabel, context);
	}

	private static IFieldEditor createBrowseWorkspaceImageEditor(String name, String label, IFile context, String description) {
		ButtonFieldEditor.ButtonPressedAction action = createSelectWorkspaceImageAction(CommonUIMessages.SWT_FIELD_EDITOR_FACTORY_BROWS, context);
		CompositeEditor editor = new CompositeEditor(name, label, "");
		editor.addFieldEditors(new IFieldEditor[]{new LabelFieldEditor(name,label, description),
				new TextFieldEditor(name,label, ""),
				new ButtonFieldEditor(name, action, "")});
		action.setFieldEditor(editor);
		return editor;
	}

	public static ButtonFieldEditor.ButtonPressedAction createSelectWorkspaceImageAction(String buttonName, final IFile context) {
		ViewerFilter filter = new ViewerFilter() {
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				return (element instanceof IFolder 
						|| (element instanceof IProject
							&& element == context.getProject())
						|| (element instanceof IFile 
							&& SRCUtil.isImageFile(((IFile)element).getName())));
			}
		};
		return createSelectWorkspaceFileAction(buttonName, context, WizardMessages.selectImageDialogTitle, WizardMessages.selectImageDialogMessage, filter);
	}

	private static IFieldEditor createBrowseWorkspaceVideoEditor(String name, String label, IFile context) {
		ButtonFieldEditor.ButtonPressedAction action = createSelectWorkspaceVideoAction(CommonUIMessages.SWT_FIELD_EDITOR_FACTORY_BROWS, context);
		CompositeEditor editor = new CompositeEditor(name, label, "");
		editor.addFieldEditors(new IFieldEditor[]{new LabelFieldEditor(name,label, WizardDescriptions.videoSrc),
				new TextFieldEditor(name,label, ""),
				new ButtonFieldEditor(name, action, "")});
		action.setFieldEditor(editor);
		return editor;
	}

	public static ButtonFieldEditor.ButtonPressedAction createSelectWorkspaceVideoAction(String buttonName, final IFile context) {
		ViewerFilter filter = new ViewerFilter() {
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				return (element instanceof IFolder 
						|| (element instanceof IProject
							&& element == context.getProject())
						|| (element instanceof IFile 
							&& SRCUtil.isVideoFile(((IFile)element).getName())));
			}
		};
		return createSelectWorkspaceFileAction(buttonName, context, WizardMessages.selectVideoDialogTitle, WizardMessages.selectVideoDialogMessage, filter);
	}

	private static IFieldEditor createBrowseWorkspaceAudioEditor(String name, String label, IFile context) {
		ButtonFieldEditor.ButtonPressedAction action = createSelectWorkspaceAudioAction(CommonUIMessages.SWT_FIELD_EDITOR_FACTORY_BROWS, context);
		CompositeEditor editor = new CompositeEditor(name, label, "");
		editor.addFieldEditors(new IFieldEditor[]{new LabelFieldEditor(name,label, WizardDescriptions.audioSrc),
				new TextFieldEditor(name,label, ""),
				new ButtonFieldEditor(name, action, "")});
		action.setFieldEditor(editor);
		return editor;
	}

	public static ButtonFieldEditor.ButtonPressedAction createSelectWorkspaceAudioAction(String buttonName, final IFile context) {
		ViewerFilter filter = new ViewerFilter() {
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				return (element instanceof IFolder 
						|| (element instanceof IProject
							&& element == context.getProject())
						|| (element instanceof IFile 
							&& SRCUtil.isAudioFile(((IFile)element).getName())));
			}
		};
		return createSelectWorkspaceFileAction(buttonName, context, WizardMessages.selectAudioDialogTitle, WizardMessages.selectAudioDialogMessage, filter);
	}

	public static ButtonFieldEditor.ButtonPressedAction createSelectWorkspaceFileAction(String buttonName, final IFile context, 
			final String title, final String message, final ViewerFilter filter) {
		ButtonFieldEditor.ButtonPressedAction action = new ButtonFieldEditor.ButtonPressedAction(buttonName) {
			private String inerInitPath;

			@Override
			public void run() {
				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
						Display.getCurrent().getActiveShell(),
						new WorkbenchLabelProvider(), new WorkbenchContentProvider());
				dialog.setInput(ResourcesPlugin.getWorkspace());
				String path = inerInitPath != null ? inerInitPath : 
					context != null ? context.getParent().getFullPath().toString() : "";
				IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
				if (resource != null && resource.exists()) {
					dialog.setInitialSelection(resource);
				}
				dialog.addFilter(filter);
				dialog.setValidator(new ISelectionStatusValidator() {
					public IStatus validate(Object[] selection) {
						return (selection.length == 1 && (selection[0] instanceof IFile))
								? new Status(IStatus.OK, WebUiPlugin.PLUGIN_ID, "") :  new Status(IStatus.ERROR, WebUiPlugin.PLUGIN_ID, "");  //$NON-NLS-1$
					}
				});
				dialog.setAllowMultiple(false);
				dialog.setTitle(title); 
				dialog.setMessage(message); 
				if (dialog.open() == Window.OK) {
					IResource res = (IResource) dialog.getFirstResult();
					String value = SRCUtil.getRelativePath(res, context.getParent());
					inerInitPath = res.getFullPath().toString();
					getFieldEditor().setValue(value);
				}
			}
		};
		return action;
	}

	/**
	 * Used in New Form wizard.
	 * @return
	 */
	public static IFieldEditor createFormActionEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_FORM_ACTION, WizardMessages.actionLabel, "",
				WizardDescriptions.formAction);
	}

	static String[] METHOD_LIST = {METHOD_GET, METHOD_POST};

	/**
	 * Used in New Form wizard.
	 * @return
	 */
	public static IFieldEditor createFormMethodEditor() {
		return SwtFieldEditorFactory.INSTANCE.createRadioEditor(
				EDITOR_ID_FORM_METHOD, 
				WizardMessages.methodLabel, 
				toList(METHOD_LIST), 
				toList(METHOD_LIST), 
				METHOD_GET,
				WizardDescriptions.formMethod);
	}

	/**
	 * Used in New Button wizard, and New Range Slider wizard,
	 * New Text Input wizard and New Toggle wizard.
	 * @return
	 */
	public static IFieldEditor createDisabledEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_DISABLED, WizardMessages.disabledLabel, false,
				WizardDescriptions.widgetDisabled);
	}

	/**
	 * Used in New Button wizard and New Select Menu wizard.
	 * @return
	 */
	public static IFieldEditor createInlineEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_INLINE, WizardMessages.inlineLabel, false,
				WizardDescriptions.widgetInline);
	}

	/**
	 * Used in New Collapsible wizard.
	 * @return
	 */
	public static IFieldEditor createCollapsedEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_COLLAPSED, WizardMessages.collapsedLabel, true,
				WizardDescriptions.collapsibleCollapsed);
	}

	/**
	 * Used in New Collapsible wizard.
	 * @return
	 */
	public static IFieldEditor createFieldSetEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_FIELD_SET, WizardMessages.fieldSetLabel, true,
				WizardDescriptions.collapsibleFieldSet);
	}

	/**
	 * Used in New Button wizard and in wizards that specify a button.
	 * @return
	 */
	public static IFieldEditor createIconOnlyEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_ICON_ONLY, WizardMessages.iconOnlyLabel, false,
				WizardDescriptions.buttonIconOnly);
	}

	/**
	 * Used in New Form wizard.
	 * @return
	 */
	public static IFieldEditor createAutocompleteEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_AUTOCOMPLETE, WizardMessages.autocompleteLabel, true,
				WizardDescriptions.formAutocomplete);
	}

	/**
	 * Used in New Form wizard.
	 * @return
	 */
	public static IFieldEditor createValidateEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_VALIDATE, WizardMessages.validateLabel, true,
				WizardDescriptions.formValidate);
	}

	static String[] ICON_VALUES = new String[JQueryMobileAttrProvider.ENUM_ICON_VALUES.length + 1];
	static {
		ICON_VALUES[0] = "";
		System.arraycopy(JQueryMobileAttrProvider.ENUM_ICON_VALUES, 0, ICON_VALUES, 1, ICON_VALUES.length - 1);
	}

	/**
	 * Used in New Button wizard and in ButtonsEditor.
	 * @return
	 */
	public static IFieldEditor createIconEditor() {
		return createIconEditor(EDITOR_ID_ICON);
	}

	public static IFieldEditor createIconEditor(String editorID) {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(editorID, WizardMessages.iconLabel, toList(ICON_VALUES), "", true,
				WizardDescriptions.buttonIcon);
	}

	/**
	 * Used in New Collapsible wizard.
	 * @return
	 */
	public static IFieldEditor createCollapsedIconEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_COLLAPSED_ICON, WizardMessages.collapsedIconLabel, toList(ICON_VALUES), "", true,
				WizardDescriptions.collapsibleCollapsedIcon);
	}

	/**
	 * Used in New Collapsible wizard.
	 * @return
	 */
	public static IFieldEditor createExpandedIconEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_EXPANDED_ICON, WizardMessages.expandedIconLabel, toList(ICON_VALUES), "", true,
				WizardDescriptions.collapsibleExpandedIcon);
	}

	/**
	 * Used in many wizards where a button may be decorated with icon.
	 * @return
	 */
	public static IFieldEditor createIconPositionEditor() {
		String[] values = new String[]{"", "left", "right", "top", "bottom"};
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_ICON_POS, WizardMessages.iconposLabel, toList(values), "", true,
				WizardDescriptions.iconPosition);
	}

	/**
	 * Used in New Button wizard and New Link wizard.
	 * @return
	 */
	public static IFieldEditor createActionEditor() {
		String[] values = new String[]{"", 
				WizardMessages.actionDialogLabel, 
				WizardMessages.actionPopupLabel,
				WizardMessages.actionBackLabel,
				WizardMessages.actionCloseLabel,
				WizardMessages.actionExternalLabel};
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_ACTION, WizardMessages.actionLabel, toList(values), "", false,
				WizardDescriptions.linkAction);
	}

	/**
	 * Used in New Range Slider wizard.
	 * @return
	 */
	public static IFieldEditor createRangeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_RANGE, WizardMessages.rangeLabel, false,
				WizardDescriptions.sliderIsRange);
	}

	static String[] TRANSITION_LIST = {"", 
		TRANSITION_FADE, TRANSITION_POP, TRANSITION_FLIP, TRANSITION_TURN, 
		TRANSITION_FLOW, TRANSITION_SLIDEFADE, TRANSITION_SLIDEDOWN,
		TRANSITION_SLIDE, TRANSITION_SLIDEUP, TRANSITION_NONE};

	/**
	 * Used in New Link wizard and New Popup wizard for opening button.
	 * @return
	 */
	public static IFieldEditor createTransitionEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				EDITOR_ID_TRANSITION, 
				WizardMessages.transitionLabel, 
				toList(TRANSITION_LIST), "", false,
				description);
	}

	static String[] CLOSE_BUTTON_LIST = {CLOSE_LEFT, CLOSE_RIGHT, CLOSE_NONE};

	/**
	 * Used in New Dialog wizard.
	 * @return
	 */
	public static IFieldEditor createCloseButtonEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				EDITOR_ID_CLOSE_BUTTON, 
				WizardMessages.closeButtonPositionLabel, 
				toList(CLOSE_BUTTON_LIST), CLOSE_LEFT, false,
				WizardDescriptions.dialogCloseButton);
	}

	static String[] CLOSE_POPUP_BUTTON_LIST = {CLOSE_NONE, CLOSE_LEFT, CLOSE_RIGHT};

	/**
	 * Used in New Popup wizard.
	 * @return
	 */
	public static IFieldEditor createClosePopupButtonEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				EDITOR_ID_CLOSE_BUTTON, 
				WizardMessages.closeButtonPositionLabel, 
				toList(CLOSE_POPUP_BUTTON_LIST), CLOSE_NONE, false,
				WizardDescriptions.popupCloseButton);
	}

	/**
	 * Used in New Range Slider wizard and New Text Input wizard.
	 * @return
	 */
	public static IFieldEditor createMinEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_MIN, WizardMessages.minLabel, "0",
				description);
	}

	/**
	 * Used in New Range Slider wizard and New Text Input wizard.
	 * @return
	 */
	public static IFieldEditor createMaxEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_MAX, WizardMessages.maxLabel, "100",
				description);
	}

	/**
	 * Used in New Range Slider wizard and New Text Input wizard.
	 * @return
	 */
	public static IFieldEditor createStepEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_STEP, WizardMessages.stepLabel, "",
				description);
	}

	/**
	 * Used in New Range Slider wizard.
	 * @return
	 */
	public static IFieldEditor createValueEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_VALUE, WizardMessages.valueLabel, "",
				WizardDescriptions.rangeSliderValue);
	}

	/**
	 * Used in New Range Slider wizard.
	 * @return
	 */
	public static IFieldEditor createRightValueEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_RVALUE, WizardMessages.rightValueLabel, "60",
				WizardDescriptions.rangeSliderRightValue);
	}

	/**
	 * Used in New Range Slider wizard.
	 * @return
	 */
	public static IFieldEditor createHighlightEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_HIGHLIGHT, WizardMessages.highlightLabel, true,
				WizardDescriptions.rangeSliderHighlight);
	}

	/**
	 * Used in New Range Slider wizard, New Select Menu wizard, 
	 * New Text Input wizard, New Toggle wizard.
	 * @return
	 */
	public static IFieldEditor createHideLabelEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_HIDE_LABEL, WizardMessages.hideLabelLabel, false,
				WizardDescriptions.hideLabel);
	}

	static List<String> toList(String[] values) {
		List<String> list = new ArrayList<String>();
		for (String s: values) list.add(s);
		return list;
	}

	/**
	 * Used in New Page wizard.
	 * @return
	 */
	public static IFieldEditor createAddHeaderEditor() {
		return createCheckboxEditor(EDITOR_ID_ADD_HEADER, WizardMessages.headerLabel, true, 1,
				WizardDescriptions.pageHeader);
	}

	/**
	 * Used in New Page wizard.
	 * @return
	 */
	public static IFieldEditor createAddFooterEditor() {
		return createCheckboxEditor(EDITOR_ID_ADD_FOOTER, WizardMessages.footerLabel, true, 1,
				WizardDescriptions.pageFooter);
	}

	/**
	 * Used in New Page wizard.
	 * Goes together with enabling label that provides description.
	 * @return
	 */
	public static IFieldEditor createHeaderTitleEditor() {
		return createTextEditor(EDITOR_ID_HEADER_TITLE, "Page Title", 2);
	}

	/**
	 * Used in New Page wizard.
	 * Goes together with enabling label that provides description.
	 * @return
	 */
	public static IFieldEditor createFooterTitleEditor() {
		return createTextEditor(EDITOR_ID_FOOTER_TITLE, "Page Footer", 2);
	}

	/**
	 * Creates checkbox editor with label as text at checkbox.
	 * This can serve as enabling label for a text editor.
	 * 
	 * @param name
	 * @param label
	 * @param defaultValue
	 * @param span
	 * @return
	 */
	public static IFieldEditor createCheckboxEditor(String name, final String label, boolean defaultValue, final int span, final String description) {
		return new CheckBoxFieldEditor(name,label,Boolean.valueOf(defaultValue)){
			public void doFillIntoGrid(Object parent) {
				Composite c = (Composite) parent;
				final Control[] controls = (Control[]) getEditorControls(c);
				Button button = (Button)controls[0];
				button.setText(label);
				button.setToolTipText(description);
				GridData d = new GridData();
				d.horizontalSpan = span;
				button.setLayoutData(d);
			}
		};
	}

	/**
	 * Creates text editor without label.
	 * 
	 * @param name
	 * @param defaultValue
	 * @param span
	 * @return
	 */
	public static IFieldEditor createTextEditor(String name, String defaultValue, final int span) {
		return new TextFieldEditor(name,"",defaultValue) {
			public void doFillIntoGrid(Object parent) {
				Composite c = (Composite) parent;
				final Control[] controls = (Control[]) getEditorControls(c);
				Text text = (Text)controls[0];
				GridData d = new GridData(GridData.FILL_HORIZONTAL);
				d.horizontalSpan = span;
				text.setLayoutData(d);
			}
		};
	}

	/**
	 * Creates an invisible placeholder.
	 * 
	 * @param name
	 * @param span
	 * @return
	 */
	public static IFieldEditor createSpan(String name, final int span) {
		return new LabelFieldEditor(name, "X") {
			public void doFillIntoGrid(Object parent) {
				Composite c = (Composite) parent;
				Control[] controls = (Control[]) getEditorControls(c);
				Control text = controls[0];
				GridData d = new GridData();
				controls[0].setVisible(false);
				d.horizontalSpan = span;
				text.setLayoutData(d);
			}
		};
	}

	/**
	 * Used in New Text Input wizard.
	 * @return
	 */
	public static IFieldEditor createClearInputEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_CLEAR_INPUT, WizardMessages.clearInputLabel, true,
				WizardDescriptions.textInputClear);
	}

	static String[] TEXT_TYPES = {
		TYPE_TEXT, TYPE_TEXTAREA, TYPE_SEARCH, TYPE_PASSWORD, TYPE_NUMBER,
		TYPE_FILE, TYPE_URL, TYPE_EMAIL, TYPE_TEL,
		TYPE_DATE, TYPE_TIME, TYPE_DATETIME, TYPE_MONTH, TYPE_WEEK,
		TYPE_COLOR
	};

	/**
	 * Used in New Text Input wizard.
	 * @return
	 */
	public static IFieldEditor createTextTypeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				EDITOR_ID_TEXT_TYPE, 
				WizardMessages.textTypeLabel, 
				toList(TEXT_TYPES), TYPE_TEXT, false,
				WizardDescriptions.textInputType);
	}

	/**
	 * Used in New Text Input wizard.
	 * @return
	 */
	public static IFieldEditor createPlaceholderEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_PLACEHOLDER, WizardMessages.placeholderLabel, "",
				WizardDescriptions.textInputPlaceholder);
	}

	/**
	 * Used in New Text Input wizard.
	 * @return
	 */
	public static IFieldEditor createPatternEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_PATTERN, WizardMessages.patternLabel, "",
				WizardDescriptions.textInputPattern);
	}

	/**
	 * Used in New Text Input wizard.
	 * @return
	 */
	public static IFieldEditor createMaxlengthEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_MAXLENGTH, WizardMessages.maxlengthLabel, "",
				WizardDescriptions.textInputMaxlength);
	}

	/**
	 * Used in New Text Input wizard.
	 * @return
	 */
	public static IFieldEditor createAutofocusEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_AUTOFOCUS, WizardMessages.autofocusLabel, false,
				WizardDescriptions.textInputAutofocus);
	}

	/**
	 * Used in New Text Input wizard.
	 * @return
	 */
	public static IFieldEditor createRequiredEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_REQUIRED, WizardMessages.requiredLabel, false,
				WizardDescriptions.textInputRequired);
	}

	/**
	 * Used in New Header wizard, New Footer wizard and New Panel wizard.
	 * @return
	 */
	public static IFieldEditor createFixedPositionEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_FIXED_POSITION, WizardMessages.fixedPositionLabel, false,
				description);
	}

	/**
	 * Used in New Header wizard and New Footer wizard.
	 * @return
	 */
	public static IFieldEditor createFullScreenEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_FULL_SCREEN, WizardMessages.fullScreenLabel, false,
				description);
	}

	/**
	 * Used in New Header wizard.
	 * @return
	 */
	public static IFieldEditor createLeftButtonEditor() {
		return createCheckboxEditor(EDITOR_ID_LEFT_BUTTON, WizardMessages.leftButtonLabel, true, 3,
				WizardDescriptions.headerLeftButton);
	}

	/**
	 * Used in New Header wizard.
	 * @return
	 */
	public static IFieldEditor createRightButtonEditor() {
		return createCheckboxEditor(EDITOR_ID_RIGHT_BUTTON, WizardMessages.rightButtonLabel, true, 3,
				WizardDescriptions.headerRightButton);
	}

	/**
	 * Used in New Page wizard.
	 * @return
	 */
	public static IFieldEditor createBackButtonEditor() {
		return createCheckboxEditor(EDITOR_ID_BACK_BUTTON, WizardMessages.backButtonLabel, false, 3,
			WizardDescriptions.pageBackButton);
	}

	public static IFieldEditor createItemsNumberEditor(String label, int min, int max, int value) {
		String[] numbers = new String[max - min + 1];
		for (int i = min; i <= max; i++) {
			numbers[i - min] = "" + i;
		}
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_NUMBER_OF_ITEMS, WizardMessages.numberOfItemsLabel, toList(numbers), "" + value, false);
	}

	/**
	 * Used in New Footer wizard.
	 * @return
	 */
	public static IFieldEditor createArragementEditor() {
		String[] values = new String[]{ARRAGEMENT_DEFAULT, ARRAGEMENT_GROUPED, ARRAGEMENT_NAVBAR};
		return SwtFieldEditorFactory.INSTANCE.createRadioEditor(EDITOR_ID_ARRAGEMENT, WizardMessages.arragementLabel, 
				toList(values), toList(values), values[0], WizardDescriptions.footerArrangement);
	}

	/**
	 * Used in New Grid wizard.
	 * @return
	 */
	public static IFieldEditor createGridColumnsEditor() {
		String[] values = new String[]{"1", "2", "3", "4", "5"};
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_GRID_COLUMNS, WizardMessages.gridColumnsLabel, 
				toList(values), "3", false, WizardDescriptions.gridColumns);
	}

	/**
	 * Used in New Grid wizard.
	 * @return
	 */
	public static IFieldEditor createGridRowsEditor() {
		String[] values = new String[]{"1", "2", "3", "4", "5"};
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_GRID_ROWS, WizardMessages.gridRowsLabel, 
				toList(values), "3", false, WizardDescriptions.gridRows);
	}

	/**
	 * Used in New Panel wizard and New Popup wizard.
	 * @return
	 */
	public static IFieldEditor createDismissableEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_DISMISSABLE, WizardMessages.dismissableLabel, true,
				description);
	}

	/**
	 * Used in New Popup wizard.
	 * @return
	 */
	public static IFieldEditor createPopupButtonEditor() {
		return createCheckboxEditor(EDITOR_ID_POPUP_BUTTON, WizardMessages.popupButtonLabel, true, 3,
			WizardDescriptions.popupOpenButton);
	}

	/**
	 * Used in New Popup wizard.
	 * @return
	 */
	public static IFieldEditor createShadowEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_SHADOW, WizardMessages.shadowLabel, true,
				WizardDescriptions.popupShadow);
	}

	/**
	 * Used in New Popup wizard.
	 * @return
	 */
	public static IFieldEditor createPaddingEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_PADDING, WizardMessages.paddingLabel, true,
				WizardDescriptions.popupPadding);
	}

	/**
	 * Used in New Popup wizard.
	 * @return
	 */
	public static IFieldEditor createOverlayEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_OVERLAY, WizardMessages.overlayLabel, toList(THEMES), "", true,
				WizardDescriptions.popupOverlay);
	}

	static String[] POPUP_THEMES = {"", "none", "a", "b", "c", "d", "e"};

	/**
	 * Used in New Popup wizard.
	 * @return
	 */
	public static IFieldEditor createPopupThemeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_THEME, WizardMessages.themeLabel, toList(POPUP_THEMES), "", true,
				WizardDescriptions.widgetTheme);
	}

	/**
	 * Used in New Popup wizard.
	 * @return
	 */
	public static IFieldEditor createInfoStyledEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_INFO_STYLED, WizardMessages.infoStyledLabel, false,
				WizardDescriptions.popupInfoStyled);
	}

	/**
	 * Used in New Popup wizard.
	 * @return
	 */
	public static IFieldEditor createPositionToEditor() {
		String[] values = new String[]{"", POSITION_TO_WINDOW, POSITION_TO_ORIGIN};
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_POSITION_TO, WizardMessages.positionToLabel, 
				toList(values), "", true, WizardDescriptions.popupPositionTo);
	}

	static String[] PANEL_POSITION_LIST = {POSITION_LEFT, POSITION_RIGHT};
	
	static String[] PANEL_POSITION_DESCRIPTIONS = {
		WizardDescriptions.panelPositionLeft,
		WizardDescriptions.panelPositionRight
	};

	/**
	 * Used in New Panel wizard.
	 * @return
	 */
	public static IFieldEditor createPanelPositionEditor() {
		return SwtFieldEditorFactory.INSTANCE.createRadioEditor(
				EDITOR_ID_PANEL_POSITION, 
				WizardMessages.panelPositionLabel, 
				toList(PANEL_POSITION_LIST), 
				toList(PANEL_POSITION_LIST), 
				POSITION_LEFT,
				WizardDescriptions.panelPosition,
				toList(PANEL_POSITION_DESCRIPTIONS));
	}

	static String[] PANEL_DISPLAY_LIST = {DISPLAY_OVERLAY, DISPLAY_REVEAL, DISPLAY_PUSH};

	static String[] PANEL_DISPLAY_DESCRIPTIONS = {
		WizardDescriptions.panelDisplayOverlay,
		WizardDescriptions.panelDisplayReveal,
		WizardDescriptions.panelDisplayPush
	};
	/**
	 * Used in New Panel wizard.
	 * @return
	 */
	public static IFieldEditor createPanelDisplayEditor() {
		return SwtFieldEditorFactory.INSTANCE.createRadioEditor(
				EDITOR_ID_DISPLAY, 
				WizardMessages.displayLabel, 
				toList(PANEL_DISPLAY_LIST), 
				toList(PANEL_DISPLAY_LIST), 
				DISPLAY_REVEAL,
				WizardDescriptions.panelDisplay,
				toList(PANEL_DISPLAY_DESCRIPTIONS));
	}

	/**
	 * Used in New Panel wizard.
	 * @return
	 */
	public static IFieldEditor createSwipeCloseEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_SWIPE_CLOSE, WizardMessages.swipeCloseLabel, true,
				WizardDescriptions.panelSwipeClose);
	}

	static String[] TABLE_MODE_LIST = {MODE_COLUMNTOGGLE, MODE_REFLOW};
	static String[] TABLE_MODE_LABEL_LIST = {WizardMessages.modeColumntoggleLabel, WizardMessages.modeReflowLabel};

	/**
	 * Used in New Table wizard.
	 * @return
	 */
	public static IFieldEditor createTableModeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createRadioEditor(
				EDITOR_ID_MODE, 
				WizardMessages.modeLabel, 
				toList(TABLE_MODE_LABEL_LIST), 
				toList(TABLE_MODE_LIST), 
				MODE_REFLOW,
				WizardDescriptions.tableMode);
	}

	/**
	 * Used in New Table wizard.
	 * @return
	 */
	public static IFieldEditor createColumnNameEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_COLUMN_NAME, WizardMessages.headLabel, "",
				WizardDescriptions.tableColumnName);
	}

	/**
	 * Used in New Table wizard.
	 * @return
	 */
	public static IFieldEditor createPriorityEditor() {
		String[] values = new String[]{"", "1", "2", "3", "4", "5", "6"};
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_PRIORITY, WizardMessages.priorityLabel, 
				toList(values), "", true, WizardDescriptions.tableColumnPriority);
	}

	/**
	 * Used in New Table wizard.
	 * @return
	 */
	public static IFieldEditor createColumnContentEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_CONTENT, WizardMessages.contentLabel, "",
				WizardDescriptions.tableColumnContent);
	}

	/**
	 * Used in New Table wizard.
	 * @return
	 */
	public static IFieldEditor createResponsiveEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_RESPONSIVE, WizardMessages.responsiveLabel, true,
				WizardDescriptions.tableResponsive);
	}

	/**
	 * Used in New Table wizard.
	 * @return
	 */
	public static IFieldEditor createStripesEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_STRIPES, WizardMessages.stripesLabel, false,
				WizardDescriptions.tableStripes);
	}

	/**
	 * Used in New Image wizard.
	 * @return
	 */
	public static IFieldEditor createAltEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_ALT, WizardMessages.altLabel, "",
				WizardDescriptions.imageAlt);
	}

	/**
	 * Used in New Image wizard and New Video wizard.
	 * @return
	 */
	public static IFieldEditor createWidthEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_WIDTH, WizardMessages.widthLabel, "",
				description);
	}

	/**
	 * Used in New Image wizard and New Video wizard.
	 * @return
	 */
	public static IFieldEditor createHeightEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_HEIGHT, WizardMessages.heightLabel, "",
				description);
	}

	/**
	 * Used in New Image wizard.
	 * @return
	 */
	public static IFieldEditor createIsmapEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_ISMAP, WizardMessages.ismapLabel, false,
				WizardDescriptions.imageIsmap);
	}

	/**
	 * Used in New Image wizard.
	 * @return
	 */
	public static IFieldEditor createUsemapEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_USEMAP, WizardMessages.usemapLabel, "",
				WizardDescriptions.imageUsemap);
	}

	/**
	 * Used in New Image wizard.
	 * @return
	 */
	public static IFieldEditor createCrossoriginEditor() {
		String[] values = new String[]{"", CROSSORIGIN_ANONIMOUS, CROSSORIGIN_USE_CREDENTIALS};
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_CROSSORIGIN, WizardMessages.crossoriginLabel, 
				toList(values), "", true, WizardDescriptions.imageCrossorigin);
	}

	/**
	 * Used in New Video wizard and New Audio wizard.
	 * @return
	 */
	public static IFieldEditor createAutoplayEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_AUTOPLAY, WizardMessages.autoplayLabel, false,
				description);
	}

	/**
	 * Used in New Video wizard and New Audio wizard.
	 * @return
	 */
	public static IFieldEditor createControlsEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_CONTROLS, WizardMessages.controlsLabel, true,
				description);
	}

	/**
	 * Used in New Video wizard and New Audio wizard.
	 * @return
	 */
	public static IFieldEditor createLoopEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_LOOP, WizardMessages.loopLabel, false,
				description);
	}

	/**
	 * Used in New Video wizard and New Audio wizard.
	 * @return
	 */
	public static IFieldEditor createMutedEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_MUTED, WizardMessages.mutedLabel, false,
				description);
	}

	static String[] PRELOAD_LIST = {AUTO, METADATA, NONE};
	static String[] PRELOAD_LABEL_LIST = {WizardMessages.preloadAutoLabel, WizardMessages.preloadMetadataLabel, WizardMessages.preloadNoneLabel};

	static String[] VIDEO_PRELOAD_VALUE_DESCRIPTIONS = {
		WizardDescriptions.videoPreloadAuto,
		WizardDescriptions.videoPreloadMetadata,
		WizardDescriptions.videoPreloadNone
	};

	/**
	 * Used in New Video wizard.
	 * @return
	 */
	public static IFieldEditor createPreloadVideoEditor() {
		return SwtFieldEditorFactory.INSTANCE.createRadioEditor(
				EDITOR_ID_PRELOAD, 
				WizardMessages.preloadLabel, 
				toList(PRELOAD_LABEL_LIST), 
				toList(PRELOAD_LIST), 
				AUTO, WizardDescriptions.videoPreload, toList(VIDEO_PRELOAD_VALUE_DESCRIPTIONS));
	}

	static String[] PRELOAD_VALUE_DESCRIPTIONS = {
		WizardDescriptions.audioPreloadAuto,
		WizardDescriptions.audioPreloadMetadata,
		WizardDescriptions.audioPreloadNone
	};

	/**
	 * Used in New Audio wizard.
	 * @return
	 */
	public static IFieldEditor createPreloadAudioEditor() {
		return SwtFieldEditorFactory.INSTANCE.createRadioEditor(
				EDITOR_ID_PRELOAD, 
				WizardMessages.preloadLabel, 
				toList(PRELOAD_LABEL_LIST), 
				toList(PRELOAD_LIST), 
				AUTO, WizardDescriptions.audioPreload, toList(PRELOAD_VALUE_DESCRIPTIONS));
	}

	/**
	 * Used in New Video wizard.
	 * @return
	 */
	public static IFieldEditor createVideoTypeEditor() {
		String[] values = new String[]{"", VIDEO_TYPE_MP4, VIDEO_TYPE_OGG, VIDEO_TYPE_WEBM};
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_VIDEO_TYPE, WizardMessages.typeLabel, 
				toList(values), "", true, WizardDescriptions.videoType);
	}

	/**
	 * Used in New Audio wizard.
	 * @return
	 */
	public static IFieldEditor createAudioTypeEditor() {
		String[] values = new String[]{"", AUDIO_TYPE_MPEG, AUDIO_TYPE_OGG};
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_AUDIO_TYPE, WizardMessages.typeLabel, 
				toList(values), "", true, WizardDescriptions.audioType);
	}

	/**
	 * Used in New Label wizard.
	 * @return
	 */
	public static IFieldEditor createLabelTextEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_LABEL, WizardMessages.textLabel, "",
				WizardDescriptions.labelText);
	}

	/**
	 * Used in New Label wizard.
	 * @return
	 */
	public static IFieldEditor createForEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_FOR, WizardMessages.forLabel, "",
				WizardDescriptions.labelFor);
	}

	/**
	 * Used in New Label wizard.
	 * @return
	 */
	public static IFieldEditor createFormReferenceEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_FORM, WizardMessages.formLabel, "",
				WizardDescriptions.labelForm);
	}

}

