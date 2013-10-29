package org.jboss.tools.jst.web.ui.internal.properties.html;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.common.ui.CommonUIMessages;
import org.jboss.tools.common.ui.widget.editor.ButtonFieldEditor;
import org.jboss.tools.common.ui.widget.editor.CompositeEditor;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.common.ui.widget.editor.LabelFieldEditor;
import org.jboss.tools.common.ui.widget.editor.SwtFieldEditorFactory;
import org.jboss.tools.common.ui.widget.editor.TextFieldEditor;
import org.jboss.tools.jst.web.html.HTMLConstants;
import org.jboss.tools.jst.web.html.JQueryHTMLConstants;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.WizardDescriptions;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class HTMLFieldEditorFactory implements HTMLConstants {

	/**
	 * Used in Image.
	 */
	public static IFieldEditor createAltEditor() {
		return JQueryFieldEditorFactory.createAltEditor();
	}

	/**
	 * Used in Script.
	 * @return
	 */
	public static IFieldEditor createAsyncEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_ASYNC, 
				WizardMessages.asyncLabel, false,
				WizardDescriptions.scriptAsync);
	}

	static String[] BORDER_VALUES = {"", "1"
	};
	/**
	 * Used in Area.
	 * @return
	 */
	public static IFieldEditor createBorderEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				ATTR_BORDER, 
				WizardMessages.borderLabel, 
				toList(BORDER_VALUES), "", true,
				WizardDescriptions.tableBorder);
	}

	/**
	 * Used in blockquote
	 * @return
	 */
	public static IFieldEditor createCiteEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_CITE, 
				WizardMessages.citeLabel, "", description);
	}

	/**
	 * Used in keygen.
	 * @return
	 */
	public static IFieldEditor createChallengeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_CHALLENGE, 
				WizardMessages.challengeLabel, false, WizardDescriptions.keygenChallenge);
	}

	static String[] CHARSET_LIST = {"", "UTF-8", "ISO-8859-1"};
	/**
	 * Used in meta.
	 * @return
	 */
	public static IFieldEditor createCharsetEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				ATTR_CHARSET, 
				WizardMessages.charsetLabel, 
				toList(CHARSET_LIST), 
				"",
				true,
				WizardDescriptions.metaCharset);
	}

	/**
	 * Used in TextArea
	 * @return
	 */
	public static IFieldEditor createColsEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_COLS, WizardMessages.colsLabel, "");
	}

	/**
	 * Used in td
	 * @return
	 */
	public static IFieldEditor createColspanEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_COLSPAN, WizardMessages.colspanLabel, "", WizardDescriptions.tdColspan);
	}

	/**
	 * Used in meta
	 * @return
	 */
	public static IFieldEditor createContentEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_CONTENT, 
				WizardMessages.metaContentLabel, "", WizardDescriptions.metaContent);
	}

	/**
	 * Used in Area
	 * @return
	 */
	public static IFieldEditor createCoordsEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_COORDS, 
				WizardMessages.coordsLabel, "", WizardDescriptions.areaCoords);
	}

	/**
	 * Used in Image.
	 * @return
	 */
	public static IFieldEditor createCrossoriginEditor() {
		return JQueryFieldEditorFactory.createCrossoriginEditor();
	}

	/**
	 * Used in del
	 * @return
	 */
	public static IFieldEditor createDatetimeEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_DATETIME, 
				WizardMessages.datetimeLabel, "", description);
	}

	/**
	 * Used in track.
	 * @return
	 */
	public static IFieldEditor createDefaultEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_DEFAULT, 
				WizardMessages.defaultLabel, false,
				WizardDescriptions.trackDefault);
	}

	/**
	 * Used in Script.
	 * @return
	 */
	public static IFieldEditor createDeferEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_DEFER, 
				WizardMessages.deferLabel, false,
				WizardDescriptions.scriptDefer);
	}

	static String[] DIR_LIST = {"ltr", "rtl"};
	/**
	 * Used in bdo.
	 * @return
	 */
	public static IFieldEditor createDirEditor() {
		return SwtFieldEditorFactory.INSTANCE.createRadioEditor(
				ATTR_DIR, 
				WizardMessages.dirLabel, 
				toList(DIR_LIST), 
				toList(DIR_LIST), 
				"ltr",
				WizardDescriptions.bdoDir);
	}

	/**
	 * Used in Area
	 * @return
	 */
	public static IFieldEditor createDownloadEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_DOWNLOAD, 
				WizardMessages.downloadLabel, "", WizardDescriptions.areaDownload);
	}

	/**
	 * Used in TextArea
	 * @return
	 */
	public static IFieldEditor createFormEditor() {
		return JQueryFieldEditorFactory.createFormReferenceEditor();
	}

	/**
	 * Used in td
	 * @return
	 */
	public static IFieldEditor createHeadersEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_HEADERS, WizardMessages.headersLabel, "", WizardDescriptions.tdHeaders);
	}

	/**
	 * Used in Image.
	 */
	public static IFieldEditor createHeightEditor(String description) {
		return JQueryFieldEditorFactory.createHeightEditor(description);
	}

	/**
	 * Used in meter.
	 * @return
	 */
	public static IFieldEditor createHighEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_HIGH, 
				WizardMessages.highLabel, "", WizardDescriptions.meterHigh);
	}

	static String[] HTTP_EQUIV_VALUES = {"", 
		"content-type", "default-style", "refresh"};
	/**
	 * Used in meta.
	 * @return
	 */
	public static IFieldEditor createHttpEquiveEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				ATTR_HTTP_EQUIV, 
				WizardMessages.http_equiveLabel, 
				toList(HTTP_EQUIV_VALUES), "", true,
				WizardDescriptions.metaHttpEquive);
	}

	/**
	 * Used in command.
	 */
	public static IFieldEditor createIconEditor(IFile context) {
		ButtonFieldEditor.ButtonPressedAction action = JQueryFieldEditorFactory.createSelectWorkspaceImageAction(CommonUIMessages.SWT_FIELD_EDITOR_FACTORY_BROWS, context);
		return createBrowseWorkspaceEditor(ATTR_ICON, WizardMessages.iconLabel, WizardDescriptions.commandIcon, action);
	}

	/**
	 * Used in all elements
	 * @return
	 */
	public static IFieldEditor createIDEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(
				ATTR_ID, WizardMessages.idLabel, "", WizardDescriptions.elementId);
	}

	/**
	 * Used in Image.
	 * @return
	 */
	public static IFieldEditor createIsmapEditor() {
		return JQueryFieldEditorFactory.createIsmapEditor();
	}

	static String[] KEYTYPE_VALUES = {"",
		"rsa", "dsa", "ec"
	};

	/**
	 * Used in keygen.
	 * @return
	 */
	public static IFieldEditor createKeytypeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				ATTR_KEYTYPE, 
				WizardMessages.keytypeLabel, 
				toList(KEYTYPE_VALUES), "", false,
				WizardDescriptions.keygenKeytype);
	}

	static String[] KIND_VALUES = {"",
		"captions", "chapters", "descriptions", "metadata", "subtitles"
	};

	/**
	 * Used in track.
	 * @return
	 */
	public static IFieldEditor createKindEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				ATTR_KIND, 
				WizardMessages.kindLabel, 
				toList(KIND_VALUES), "", false,
				WizardDescriptions.trackKind);
	}

	/**
	 * Used in meter.
	 * @return
	 */
	public static IFieldEditor createLowEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_LOW, 
				WizardMessages.lowLabel, "", WizardDescriptions.meterLow);
	}

	/**
	 * Used in Input type=file.
	 * @return
	 */
	public static IFieldEditor createManifestEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_MANIFEST, 
				WizardMessages.manifestLabel, "", WizardDescriptions.htmlManifest);
	}

	/**
	 * Used in input and meter.
	 * @return
	 */
	public static IFieldEditor createMaxEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_MAX, WizardMessages.maxLabel, "",
				description);
	}

	/**
	 * Used in input and meter.
	 * @return
	 */
	public static IFieldEditor createMinEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_MIN, WizardMessages.minLabel, "",
				description);
	}

	/**
	 * Used in Input type=file.
	 * @return
	 */
	public static IFieldEditor createMultipleEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_MULTIPLE, 
				WizardMessages.multipleLabel, false, WizardDescriptions.inputMultiple);
	}

	static String[] META_NAME_VALUES = {"", 
		"application-name", "author", "description", "generator", "keywords"};
	/**
	 * Used in meta.
	 * @return
	 */
	public static IFieldEditor createMetaNameEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				ATTR_NAME, 
				WizardMessages.nameLabel, 
				toList(META_NAME_VALUES), "", true,
				WizardDescriptions.metaName);
	}

	/**
	 * Used in Form.
	 * @return
	 */
	public static IFieldEditor createNovalidateEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(JQueryHTMLConstants.ATTR_NOVALIDATE, WizardMessages.novalidateLabel, false,
				WizardDescriptions.formValidate);
	}

	/**
	 * Used in open.
	 * @return
	 */
	public static IFieldEditor createOpenEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_OPEN, 
				WizardMessages.openLabel, false,
				description);
	}

	/**
	 * Used in meter.
	 * @return
	 */
	public static IFieldEditor createOptimumEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_OPTIMUM, 
				WizardMessages.optimumLabel, "", WizardDescriptions.meterOptimum);
	}

	/**
	 * Used in command
	 * @return
	 */
	public static IFieldEditor createRadiogroupEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_RADIOGROUP, 
				WizardMessages.radiogroupLabel, "", WizardDescriptions.commandRadiogroup);
	}

	/**
	 * Used in ol.
	 * @return
	 */
	public static IFieldEditor createReversedEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_REVERSED, 
				WizardMessages.reversedLabel, false, WizardDescriptions.olReversed);
	}

	/**
	 * Used in TextArea
	 * @return
	 */
	public static IFieldEditor createRowsEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_ROWS, "Rows:", "");
	}

	/**
	 * Used in td
	 * @return
	 */
	public static IFieldEditor createRowspanEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_ROWSPAN, WizardMessages.rowspanLabel, "", WizardDescriptions.tdRowspan);
	}

	static String[] SANDBOX_VALUES = {"", "allow-forms", "allow-same-origin", "allow-scripts", "allow-top-navigation"};
	/**
	 * Used in iframe.
	 */
	public static IFieldEditor createSandboxEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(ATTR_SANDBOX, 
				WizardMessages.sandboxLabel, 
				toList(SANDBOX_VALUES),
				"",
				true,
				WizardDescriptions.iframeSandbox);
	}

	static String[] SCOPE_VALUES = {"", "col", "colgroup", "row", "rowgroup"};
	/**
	 * Used in th.
	 */
	public static IFieldEditor createScopeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(ATTR_SCOPE, 
				WizardMessages.scopeLabel, 
				toList(SCOPE_VALUES),
				"",
				true,
				WizardDescriptions.thScope);
	}

	/**
	 * Used in style.
	 * @return
	 */
	public static IFieldEditor createScopedEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_SCOPED, 
				WizardMessages.scopedLabel, false, WizardDescriptions.styleScoped);
	}

	/**
	 * Used in iframe.
	 */
	public static IFieldEditor createSeamlessEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_SEAMLESS, 
				WizardMessages.seamlessLabel, false,
				WizardDescriptions.iframeSeamless);
	}

	static String[] AREA_SHAPE_VALUES = {"",
		"default", "rect", "circle", "poly"
	};
	/**
	 * Used in Area.
	 * @return
	 */
	public static IFieldEditor createShapeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				ATTR_SHAPE, 
				WizardMessages.shapeLabel, 
				toList(AREA_SHAPE_VALUES), "", false,
				WizardDescriptions.areaShape);
	}

	/**
	 * Used in Select
	 * @return
	 */
	public static IFieldEditor createSizeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_SIZE, 
				WizardMessages.sizeLabel, "", WizardDescriptions.selectSize);
	}

	/**
	 * Used in Image.
	 */
	public static IFieldEditor createSrcEditor(IFile context) {
		ButtonFieldEditor.ButtonPressedAction action = JQueryFieldEditorFactory.createSelectWorkspaceImageAction(CommonUIMessages.SWT_FIELD_EDITOR_FACTORY_BROWS, context);
		return createBrowseWorkspaceEditor(ATTR_SRC, WizardMessages.srcLabel, WizardDescriptions.imageSrc, action);
	}

	private static IFieldEditor createBrowseWorkspaceEditor(String name, String label, String description, ButtonFieldEditor.ButtonPressedAction action) {
		CompositeEditor editor = new CompositeEditor(name, label, "");
		editor.addFieldEditors(new IFieldEditor[]{new LabelFieldEditor(name,label, description),
				new TextFieldEditor(name,label, ""),
				new ButtonFieldEditor(name, action, "")});
		action.setFieldEditor(editor);
		return editor;
	}

	public static IFieldEditor createAudioSrcEditor(IFile context) {
		ButtonFieldEditor.ButtonPressedAction action = JQueryFieldEditorFactory.createSelectWorkspaceAudioAction(CommonUIMessages.SWT_FIELD_EDITOR_FACTORY_BROWS, context);
		return createBrowseWorkspaceEditor(ATTR_SRC, WizardMessages.srcLabel, WizardDescriptions.audioSrc, action);
	}

	/**
	 * Used in source and embed.
	 * @return
	 */
	public static IFieldEditor createSourceSrcEditor(IFile context, String description) {
		ButtonFieldEditor.ButtonPressedAction action = JQueryFieldEditorFactory.createSelectWorkspaceSourceAction(CommonUIMessages.SWT_FIELD_EDITOR_FACTORY_BROWS, context);
		return createBrowseWorkspaceEditor(ATTR_SRC, WizardMessages.srcLabel, description, action);
	}

	/**
	 * Used in ol.
	 * @return
	 */
	public static IFieldEditor createStartEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(
				ATTR_START, WizardMessages.startLabel, "", WizardDescriptions.olStart);
	}

	/**
	 * Used in some elements
	 * @return
	 */
	public static IFieldEditor createTextSrcEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(
				ATTR_SRC, WizardMessages.srcLabel, "", description);
	}

	/**
	 * Used in some elements
	 * @return
	 */
	public static IFieldEditor createTextTypeEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(
				ATTR_TYPE, WizardMessages.typeLabel, "", description);
	}

	static String[] TYPES = {
		BUTTON_TYPE_BUTTON, INPUT_TYPE_CHECKBOX, INPUT_TYPE_COLOR, 
		INPUT_TYPE_DATE, INPUT_TYPE_DATETIME, 
		INPUT_TYPE_EMAIL, INPUT_TYPE_FILE,
		INPUT_TYPE_HIDDEN, INPUT_TYPE_IMAGE,
		INPUT_TYPE_MONTH, INPUT_TYPE_NUMBER, 
		INPUT_TYPE_PASSWORD, INPUT_TYPE_RADIO, INPUT_TYPE_RANGE, BUTTON_TYPE_RESET, 
		INPUT_TYPE_SEARCH, BUTTON_TYPE_SUBMIT,
		INPUT_TYPE_TEXT, INPUT_TYPE_TEL, INPUT_TYPE_TIME, 
		INPUT_TYPE_URL, INPUT_TYPE_WEEK		
	};

	/**
	 * Used in Input.
	 * @return
	 */
	public static IFieldEditor createInputTypeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				ATTR_TYPE, 
				WizardMessages.textTypeLabel, 
				toList(TYPES), "", false,
				WizardDescriptions.textInputType);
	}

	static String[] OL_TYPES = {
		"", "1", "a", "A", "i", "I"
	};

	/**
	 * Used in ol.
	 * @return
	 */
	public static IFieldEditor createOlTypeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				ATTR_TYPE, 
				WizardMessages.textTypeLabel, 
				toList(OL_TYPES), "", false,
				WizardDescriptions.olType);
	}

	static String[] COMMAND_TYPES = {
		"", "checkbox", "command", "radio"
	};

	/**
	 * Used in command.
	 * @return
	 */
	public static IFieldEditor createCommandTypeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				ATTR_TYPE, 
				WizardMessages.textTypeLabel, 
				toList(COMMAND_TYPES), "", false,
				WizardDescriptions.commandType);
	}

	static String[] STYLE_TYPES = {
		"", "text/css"
	};

	/**
	 * Used in style.
	 * @return
	 */
	public static IFieldEditor createStyleTypeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				ATTR_TYPE, 
				WizardMessages.textTypeLabel, 
				toList(STYLE_TYPES), "", true,
				WizardDescriptions.styleType);
	}

	static String[] REL_VALUES = {"",
		"alternate", "archives", "author", "bookmark", "external", "first", "help", 
		"icon", "last", "license", "next", "nofollow", "noreferrer", "pingback",
		"prefetch", "prev", "search", "sidebar", "stylesheet", "tag", "up"
	};

	/**
	 * Used in Link.
	 * @return
	 */
	public static IFieldEditor createLinkRelEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				ATTR_REL, 
				WizardMessages.relLabel, 
				toList(REL_VALUES), "", false,
				WizardDescriptions.linkRel);
	}

	static String[] AREA_REL_VALUES = {"",
		"alternate", "author", "bookmark", "help", 
		"license", "next", "nofollow", "noreferrer", 
		"prefetch", "prev", "search", "tag"
	};

	/**
	 * Used in Area.
	 * @return
	 */
	public static IFieldEditor createAreaRelEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				ATTR_REL, 
				WizardMessages.relLabel, 
				toList(AREA_REL_VALUES), "", false,
				WizardDescriptions.linkRel);
	}

	static String[] LINK_MIME_TYPES = {"", "text/css"};

	/**
	 * Used in Link.
	 * @return
	 */
	public static IFieldEditor createLinkTypeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				ATTR_TYPE, 
				WizardMessages.mimeType, 
				toList(LINK_MIME_TYPES), "", true,
				WizardDescriptions.linkType);
	}

	static String[] SCRIPT_MIME_TYPES = {"", "text/javascript", "application/javascript"};

	/**
	 * Used in Script.
	 * @return
	 */
	public static IFieldEditor createScriptTypeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				ATTR_TYPE, 
				WizardMessages.mimeType, 
				toList(SCRIPT_MIME_TYPES), "", true,
				WizardDescriptions.scriptType);
	}

	static String[] MENU_TYPES = {"", "list", "context", "toolbar"};

	/**
	 * Used in Script.
	 * @return
	 */
	public static IFieldEditor createMenuTypeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				ATTR_TYPE, 
				WizardMessages.typeLabel, 
				toList(MENU_TYPES), "", true,
				WizardDescriptions.menuType);
	}

	static String[] SOURCE_TYPES = {"", "audio/ogg", "audio/mpeg", "video/ogg", "video/mp4", "video/webm"};
	/**
	 * Used in Script.
	 * @return
	 */
	public static IFieldEditor createSourceTypeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				ATTR_TYPE, 
				WizardMessages.typeLabel, 
				toList(SOURCE_TYPES), "", true,
				WizardDescriptions.audioType);
	}

	/**
	 * Used in link A, base.
	 */
	public static IFieldEditor createURLEditor() {
		return JQueryFieldEditorFactory.createURLEditor(ATTR_HREF);
	}

	/**
	 * Used in Image.
	 */
	public static IFieldEditor createUsemapEditor() {
		return JQueryFieldEditorFactory.createUsemapEditor();
	}

	/**
	 * Used in Image.
	 */
	public static IFieldEditor createWidthEditor(String description) {
		return JQueryFieldEditorFactory.createWidthEditor(description);
	}

	static String[] WRAP_VALUES = {SOFT_WRAP, HARD_WRAP};
	static String[] WRAP_DESCRIPTIONS = {WizardDescriptions.textareaWrapSoft, WizardDescriptions.textareaWrapHard};

	/**
	 * Used in Textarea.
	 * @return
	 */
	public static IFieldEditor createWrapEditor() {
		return SwtFieldEditorFactory.INSTANCE.createRadioEditor(
				ATTR_WRAP, 
				WizardMessages.wrapLabel,
				toList(WRAP_VALUES),
				toList(WRAP_VALUES), SOFT_WRAP,
				WizardDescriptions.textareaWrap, 
				toList(WRAP_DESCRIPTIONS));
	}

	static List<String> toList(String[] values) {
		List<String> list = new ArrayList<String>();
		for (String s: values) list.add(s);
		return list;
	}

}
