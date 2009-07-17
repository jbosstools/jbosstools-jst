/*
 * Created on 24.02.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.jboss.tools.jst.web.tiles.ui.editor.form;

import java.util.Collections;
import java.util.Map;

import org.jboss.tools.common.model.ui.attribute.editor.TableStructuredEditor;

import org.jboss.tools.common.model.ui.forms.ArrayToMap;
import org.jboss.tools.common.model.ui.forms.FormActionData;
import org.jboss.tools.common.model.ui.forms.FormAttributeData;
import org.jboss.tools.common.model.ui.forms.FormData;
import org.jboss.tools.common.model.ui.forms.IFormActionData;
import org.jboss.tools.common.model.ui.forms.IFormData;
import org.jboss.tools.common.model.ui.forms.IFormLayoutData;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;

/**
 * @author Eskimo
 *
 */
public class TilesFormLayoutData implements IFormLayoutData {
	private static String EMPTY_DESCRIPTION = ""; //$NON-NLS-1$

	private final static String SELECT_IT_ACTION = "%SelectIt%"; //$NON-NLS-1$

	private final static String STRING_BUTTON_WRAPPER = "org.jboss.tools.common.model.ui.attribute.editor.StringButtonFieldEditorEx"; //$NON-NLS-1$
	private final static String JAVA_LINK_WRAPPER = "org.jboss.tools.common.model.ui.attribute.editor.JavaHyperlinkLineFieldEditor"; //$NON-NLS-1$

	private final static IFormData[] FORM_LAYOUT_DEFINITIONS =
		new IFormData[] {
			new FormData(
				"FileTiles", //$NON-NLS-1$
				new IFormData[] {
					// Tiles Config Description Form
					new FormData(
						WebUIMessages.TILES_CONFIG_DESCRIPTION,
						"", //$NON-NLS-1$
						new FormAttributeData[]{new FormAttributeData("name"), new FormAttributeData("encoding")} //$NON-NLS-1$ //$NON-NLS-2$
					),
					// Tiles Definitions Form
					new FormData(
						WebUIMessages.DEFINITIONS,
						EMPTY_DESCRIPTION, 
						new FormAttributeData[]{new FormAttributeData("name", 33), new FormAttributeData("extends", 33), new FormAttributeData("path", 33)}, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						new String[]{"TilesDefinition"}, //$NON-NLS-1$
					new IFormActionData[] {
							new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateDefinition"), //$NON-NLS-1$
							new FormActionData(TableStructuredEditor.REMOVE_ACTION, "DeleteActions.Delete"), //$NON-NLS-1$
							new FormActionData(TableStructuredEditor.EDIT_ACTION, SELECT_IT_ACTION),
							new FormActionData(TableStructuredEditor.UP_ACTION, "%internal%"), //$NON-NLS-1$
							new FormActionData(TableStructuredEditor.DOWN_ACTION, "%internal%") //$NON-NLS-1$
						}
					)
				}
			),
			new FormData(
				"TilesDefinition", //$NON-NLS-1$
				new IFormData[] {
					// Tiles Basic Definition Form
					new FormData(
						WebUIMessages.BASIC,
						"", //$NON-NLS-1$
						new FormAttributeData[]{
							new FormAttributeData("name"),  //$NON-NLS-1$
							new FormAttributeData("extends"),  //$NON-NLS-1$
							new FormAttributeData("path"),  //$NON-NLS-1$
							new FormAttributeData("controllerClass", null, JAVA_LINK_WRAPPER),  //$NON-NLS-1$
							new FormAttributeData("controllerUrl")} //$NON-NLS-1$
					),
					// Tiles Advanced Definition Form
					new FormData(
						WebUIMessages.ADVANCED,
						"", //$NON-NLS-1$
						new FormAttributeData[]{
							new FormAttributeData("id"),  //$NON-NLS-1$
							new FormAttributeData("display-name"),  //$NON-NLS-1$
							new FormAttributeData("description", null, STRING_BUTTON_WRAPPER),  //$NON-NLS-1$
							new FormAttributeData("role"),  //$NON-NLS-1$
							new FormAttributeData("small-icon"),  //$NON-NLS-1$
							new FormAttributeData("large-icon")} //$NON-NLS-1$
					),
					// Tiles Deprecated Definition Form
					new FormData(
						WebUIMessages.DEPRECATED,
						"", //$NON-NLS-1$
						new FormAttributeData[]{
							new FormAttributeData("page"),  //$NON-NLS-1$
							new FormAttributeData("template")} //$NON-NLS-1$
					),
					// Tiles Puts Form
					new FormData(
						WebUIMessages.PUT,
						EMPTY_DESCRIPTION, 
						new FormAttributeData[]{
							new FormAttributeData("name", 33),  //$NON-NLS-1$
							new FormAttributeData("type", 33),  //$NON-NLS-1$
							new FormAttributeData("value", 33)}, //$NON-NLS-1$
						new String[]{"TilesPut"}, //$NON-NLS-1$
//						DEFAULT_TABLE_ACTION_TYPE
					new IFormActionData[] {
							new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreatePut"), //$NON-NLS-1$
							new FormActionData(TableStructuredEditor.REMOVE_ACTION, "DeleteActions.Delete"), //$NON-NLS-1$
							new FormActionData(TableStructuredEditor.EDIT_ACTION, SELECT_IT_ACTION),
							new FormActionData(TableStructuredEditor.UP_ACTION, "%internal%"), //$NON-NLS-1$
							new FormActionData(TableStructuredEditor.DOWN_ACTION, "%internal%") //$NON-NLS-1$
					}

					),
					// Tiles PutLists Form
					new FormData(
						WebUIMessages.PUTLIST,
						EMPTY_DESCRIPTION, 
						new FormAttributeData[]{new FormAttributeData("name", 100)}, //$NON-NLS-1$
						new String[]{"TilesList"}, //$NON-NLS-1$
//						DEFAULT_TABLE_ACTION_TYPE
					new IFormActionData[] {
							new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreatePutList"), //$NON-NLS-1$
							new FormActionData(TableStructuredEditor.REMOVE_ACTION, "DeleteActions.Delete"), //$NON-NLS-1$
							new FormActionData(TableStructuredEditor.EDIT_ACTION, SELECT_IT_ACTION),
							new FormActionData(TableStructuredEditor.UP_ACTION, "%internal%"), //$NON-NLS-1$
							new FormActionData(TableStructuredEditor.DOWN_ACTION, "%internal%") //$NON-NLS-1$
					}
					)
				}
			),
			new FormData(
				"TilesPut", //$NON-NLS-1$
				new IFormData[] {
					// Tiles Basic Put Form
					new FormData(
						WebUIMessages.BASIC,
						"", //$NON-NLS-1$
						new FormAttributeData[]{
							new FormAttributeData("name"),  //$NON-NLS-1$
							new FormAttributeData("type"),  //$NON-NLS-1$
							new FormAttributeData("value", null, STRING_BUTTON_WRAPPER),  //$NON-NLS-1$
							new FormAttributeData("content", null, STRING_BUTTON_WRAPPER)}  //$NON-NLS-1$
					),
					// Tiles Advanced Put Form
					new FormData(
						WebUIMessages.ADVANCED,
						"", //$NON-NLS-1$
						new FormAttributeData[]{new FormAttributeData("id")} //$NON-NLS-1$
					),
					// Tiles Deprecated Put Form
					new FormData(
						WebUIMessages.DEPRECATED,
						"", //$NON-NLS-1$
						new FormAttributeData[]{new FormAttributeData("direct")} //$NON-NLS-1$
					)
				}
			),
			new FormData(
				"TilesList", //$NON-NLS-1$
				new IFormData[] {
					// Tiles Put List Attributes Form
					new FormData(
						WebUIMessages.ATTRIBUTES,
						"", //$NON-NLS-1$
						new FormAttributeData[]{new FormAttributeData("name"), new FormAttributeData("id")} //$NON-NLS-1$ //$NON-NLS-2$
					),
					// Tiles Put List Adds Form
					new FormData(
						WebUIMessages.ADD,
						EMPTY_DESCRIPTION, 
						new FormAttributeData[]{new FormAttributeData("type", 50), new FormAttributeData("value", 50)}, //$NON-NLS-1$ //$NON-NLS-2$
						new String[]{"TilesAdd"}, //$NON-NLS-1$
//						DEFAULT_TABLE_ACTION_TYPE
					new IFormActionData[] {
							new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateAdd"), //$NON-NLS-1$
							new FormActionData(TableStructuredEditor.REMOVE_ACTION, "DeleteActions.Delete"), //$NON-NLS-1$
							new FormActionData(TableStructuredEditor.EDIT_ACTION, SELECT_IT_ACTION),
							new FormActionData(TableStructuredEditor.UP_ACTION, "%internal%"), //$NON-NLS-1$
							new FormActionData(TableStructuredEditor.DOWN_ACTION, "%internal%") //$NON-NLS-1$
					}
					),
					// Tiles Put List Items Form
					new FormData(
						WebUIMessages.ITEM,
						EMPTY_DESCRIPTION, 
						new FormAttributeData[]{new FormAttributeData("value", 50), new FormAttributeData("link", 50)}, //$NON-NLS-1$ //$NON-NLS-2$
						new String[]{"TilesItem"}, //$NON-NLS-1$
//						DEFAULT_TABLE_ACTION_TYPE
					new IFormActionData[] {
							new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateItem"), //$NON-NLS-1$
							new FormActionData(TableStructuredEditor.REMOVE_ACTION, "DeleteActions.Delete"), //$NON-NLS-1$
							new FormActionData(TableStructuredEditor.EDIT_ACTION, SELECT_IT_ACTION),
							new FormActionData(TableStructuredEditor.UP_ACTION, "%internal%"), //$NON-NLS-1$
							new FormActionData(TableStructuredEditor.DOWN_ACTION, "%internal%") //$NON-NLS-1$
					}
					),
					// Tiles Put List Adds Form
					new FormData(
						WebUIMessages.BEAN,
						EMPTY_DESCRIPTION, 
						new FormAttributeData[]{new FormAttributeData("classtype", 100)}, //$NON-NLS-1$
						new String[]{"TilesBean"}, //$NON-NLS-1$
//						DEFAULT_TABLE_ACTION_TYPE
					new IFormActionData[] {
							new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateBean"), //$NON-NLS-1$
							new FormActionData(TableStructuredEditor.REMOVE_ACTION, "DeleteActions.Delete"), //$NON-NLS-1$
							new FormActionData(TableStructuredEditor.EDIT_ACTION, SELECT_IT_ACTION),
							new FormActionData(TableStructuredEditor.UP_ACTION, "%internal%"), //$NON-NLS-1$
							new FormActionData(TableStructuredEditor.DOWN_ACTION, "%internal%") //$NON-NLS-1$
					}
					),
					// Tiles PutLists Form
					new FormData(
						WebUIMessages.PUTLIST,
						EMPTY_DESCRIPTION, 
						new FormAttributeData[]{new FormAttributeData("name", 100)}, //$NON-NLS-1$
						new String[]{"TilesList"}, //$NON-NLS-1$
//						DEFAULT_TABLE_ACTION_TYPE
					new IFormActionData[] {
							new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreatePutList"), //$NON-NLS-1$
							new FormActionData(TableStructuredEditor.REMOVE_ACTION, "DeleteActions.Delete"), //$NON-NLS-1$
							new FormActionData(TableStructuredEditor.EDIT_ACTION, SELECT_IT_ACTION),
							new FormActionData(TableStructuredEditor.UP_ACTION, "%internal%"), //$NON-NLS-1$
							new FormActionData(TableStructuredEditor.DOWN_ACTION, "%internal%") //$NON-NLS-1$
					}
					)
				}
			),
			new FormData(
				"TilesItem", //$NON-NLS-1$
				new IFormData[] {
					// Tiles Basic Item Form
					new FormData(
						WebUIMessages.BASIC,
						"", //$NON-NLS-1$
						new FormAttributeData[]{new FormAttributeData("link"), new FormAttributeData("value")} //$NON-NLS-1$ //$NON-NLS-2$
					),
					// Tiles Advanced Item Form
					new FormData(
						WebUIMessages.ADVANCED,
						"", //$NON-NLS-1$
						new FormAttributeData[]{
							new FormAttributeData("classtype", null, JAVA_LINK_WRAPPER),  //$NON-NLS-1$
							new FormAttributeData("id"),  //$NON-NLS-1$
							new FormAttributeData("icon"),  //$NON-NLS-1$
							new FormAttributeData("tooltip")} //$NON-NLS-1$
					)
				}
			),
			new FormData(
				"TilesBean", //$NON-NLS-1$
				new IFormData[] {
					// Tiles Basic Bean Form
					new FormData(
						WebUIMessages.BASIC,
						"", //$NON-NLS-1$
						new FormAttributeData[]{
							new FormAttributeData("classtype", null, JAVA_LINK_WRAPPER),  //$NON-NLS-1$
							new FormAttributeData("id")} //$NON-NLS-1$
					),
					// Tiles Set-properties Bean Form
					new FormData(
						WebUIMessages.SET_PROPERTIES,
						EMPTY_DESCRIPTION, 
						new FormAttributeData[]{new FormAttributeData("id", 33), new FormAttributeData("property", 33), new FormAttributeData("value", 33)}, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						new String[]{"StrutsSetProperty"}, //$NON-NLS-1$
//						DEFAULT_TABLE_ACTION_TYPE
					new IFormActionData[] {
							new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateSetProperty"), //$NON-NLS-1$
							new FormActionData(TableStructuredEditor.REMOVE_ACTION, "DeleteActions.Delete"), //$NON-NLS-1$
							new FormActionData(TableStructuredEditor.EDIT_ACTION, SELECT_IT_ACTION)
					}
					)
				}
			),
			new FormData(
				"TilesAdd", //$NON-NLS-1$
				new IFormData[] {
					// Tiles Basic Add Form
					new FormData(
						WebUIMessages.BASIC,
						"", //$NON-NLS-1$
						new FormAttributeData[]{
							new FormAttributeData("type"),  //$NON-NLS-1$
							new FormAttributeData("value", null, STRING_BUTTON_WRAPPER),  //$NON-NLS-1$
							new FormAttributeData("content", null, STRING_BUTTON_WRAPPER)}  //$NON-NLS-1$
							//new FormAttributeData("save value as 'content' attr")}
					),
					// Tiles Advanced Add Form
					new FormData(
						WebUIMessages.ADVANCED,
						"", //$NON-NLS-1$
						new FormAttributeData[]{new FormAttributeData("id")} //$NON-NLS-1$
					),
					// Tiles Deprecated Add Form
					new FormData(
						WebUIMessages.DEPRECATED,
						"", //$NON-NLS-1$
						new FormAttributeData[]{new FormAttributeData("direct")} //$NON-NLS-1$
					)
				}
			),
			new FormData(
				"StrutsSetProperty", //$NON-NLS-1$
				new IFormData[] {
					// Tiles StrutsSetProperty Form
					new FormData(
						WebUIMessages.SET_PROPERTY,
						"", //$NON-NLS-1$
						new FormAttributeData[]{new FormAttributeData("id"), new FormAttributeData("property"), new FormAttributeData("value")} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					)
				}
			)
		};

	private static Map FORM_LAYOUT_DEFINITION_MAP = Collections.unmodifiableMap(new ArrayToMap(FORM_LAYOUT_DEFINITIONS));

	private static final TilesFormLayoutData INSTANCE = new TilesFormLayoutData();

	public static TilesFormLayoutData getInstance() {
		return INSTANCE;
	}

	private TilesFormLayoutData() {
	} 

	public IFormData getFormData(String entityName) {
		return (IFormData)FORM_LAYOUT_DEFINITION_MAP.get(entityName);
	}
}