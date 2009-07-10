/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.editors.forms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.jboss.tools.common.meta.XModelEntity;
import org.jboss.tools.common.meta.impl.XModelMetaDataImpl;
import org.jboss.tools.common.model.ui.attribute.editor.TableStructuredEditor;

import org.jboss.tools.common.model.ui.forms.ArrayToMap;
import org.jboss.tools.common.model.ui.forms.FormActionData;
import org.jboss.tools.common.model.ui.forms.FormAttributeData;
import org.jboss.tools.common.model.ui.forms.FormData;
import org.jboss.tools.common.model.ui.forms.FormLayoutDataUtil;
import org.jboss.tools.common.model.ui.forms.IFormActionData;
import org.jboss.tools.common.model.ui.forms.IFormData;
import org.jboss.tools.common.model.ui.forms.IFormLayoutData;
import org.jboss.tools.common.model.ui.forms.InfoLayoutDataFactory;
import org.jboss.tools.jst.web.ui.Messages;

/**
 * @author Igels
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TLDFormLayoutData implements IFormLayoutData {

	private static final FormActionData REMOVE_ACTION = new FormActionData(TableStructuredEditor.REMOVE_ACTION, "DeleteActions.Delete"); //$NON-NLS-1$
	private static final FormActionData EDIT_ACTION = new FormActionData(TableStructuredEditor.EDIT_ACTION, "Properties.Properties"); //$NON-NLS-1$
	private static final FormActionData UP_ACTION = new FormActionData(TableStructuredEditor.UP_ACTION, "%internal%"); //$NON-NLS-1$
	private static final FormActionData DOWN_ACTION = new FormActionData(TableStructuredEditor.DOWN_ACTION, "%internal%"); //$NON-NLS-1$
	private static final FormActionData SELECT_ACTION = new FormActionData(TableStructuredEditor.EDIT_ACTION, "%SelectIt%"); //$NON-NLS-1$

/*	private final static IFormActionData[] DEFAULT_TABLE_ACTION_TYPE = new IFormActionData[] {
		new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateTag"),
		new FormActionData(TableStructuredEditor.REMOVE_ACTION, "DeleteActions.Delete"),
		new FormActionData(TableStructuredEditor.EDIT_ACTION, "Properties.Properties"),
		new FormActionData(TableStructuredEditor.UP_ACTION, "%internal%"),
		new FormActionData(TableStructuredEditor.DOWN_ACTION, "%internal%")
	};*/
	
	private final static String STBFE_CLASS_NAME = "org.jboss.tools.common.model.ui.attribute.editor.JavaHyperlinkLineFieldEditor"; //$NON-NLS-1$
	
	@SuppressWarnings("nls")
	private static FormAttributeData[] getFileTagAttributes(String entityName) {
		ArrayList<FormAttributeData> list = new ArrayList<FormAttributeData>();
		XModelEntity entity = XModelMetaDataImpl.getInstance().getEntity(entityName);
		if(entity != null) {
			String[] attributes = {"tlibversion", "jspversion", "shortname", "uri", "display-name", "small-icon", "large-icon"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			for (int i = 0; i < attributes.length; i++) {
				if(entity.getAttribute(attributes[i]) != null) list.add(new FormAttributeData(attributes[i]));
			}
			list.add(new FormAttributeData("description", InfoLayoutDataFactory.getHighInstance())); //$NON-NLS-1$
		}
		
		return list.toArray(new FormAttributeData[0]);
	}
	static IFormData[] createFileTagDefinitions(String entity, String tagEntity) {
		return new IFormData[] {
		// Tiles Config Description Form
		new FormData(
			Messages.TLDFormLayoutData_TagLibraryDescription,
			"", // "Description, description, description", //$NON-NLS-1$
			getFileTagAttributes(entity)
		),
		// Tiles PutLists Form
		new FormData(
			Messages.TLDFormLayoutData_DefinedTags,
			"", //$NON-NLS-1$
			new FormAttributeData[]{new FormAttributeData("name", 50), new FormAttributeData("tagclass", 50)}, //$NON-NLS-1$ //$NON-NLS-2$
			new String[]{tagEntity},
			new IFormActionData[] {new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateTag"), REMOVE_ACTION, SELECT_ACTION, UP_ACTION, DOWN_ACTION } //$NON-NLS-1$
		)
		};
	}
	
	static IFormData TAG_VARIABLE_LIST = new FormData(
		Messages.TLDFormLayoutData_TagVariables,
		"", //$NON-NLS-1$
		new FormAttributeData[]{new FormAttributeData("name-given", 25), new FormAttributeData("name-from-attribute", 25), new FormAttributeData("variable-class", 25),  new FormAttributeData("scope", 25)}, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		new String[]{"TLDVariable"}, //$NON-NLS-1$
		new IFormActionData[] {new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateVariable"), REMOVE_ACTION, EDIT_ACTION, UP_ACTION, DOWN_ACTION } //$NON-NLS-1$
	);
	
	static IFormData TAG_GENERAL = new FormData(
		Messages.TLDFormLayoutData_TagDescription,
		"", // "Description, description, description", //$NON-NLS-1$
		new FormAttributeData[]{new FormAttributeData("name"), new FormAttributeData("tagclass", null, STBFE_CLASS_NAME), new FormAttributeData("teiclass", null, STBFE_CLASS_NAME), new FormAttributeData("bodycontent"), new FormAttributeData("display-name"), new FormAttributeData("small-icon"), new FormAttributeData("large-icon"), new FormAttributeData("description", InfoLayoutDataFactory.getHighInstance()), new FormAttributeData("example", InfoLayoutDataFactory.getInstance())} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
	);

	
	static IFormData[] TAG_DEFINITIONS = new IFormData[] {
		TAG_GENERAL,
		new FormData(
			Messages.TLDFormLayoutData_TagAttributes,
			"", //$NON-NLS-1$
			new FormAttributeData[]{new FormAttributeData("name", 25), new FormAttributeData("required", 25), new FormAttributeData("rtexprvalue", 25),  new FormAttributeData("type", 25)}, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			new String[]{"TLDAttribute12"}, //$NON-NLS-1$
			new IFormActionData[] {new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateAttribute"), REMOVE_ACTION, EDIT_ACTION, UP_ACTION, DOWN_ACTION } //$NON-NLS-1$
		),
		TAG_VARIABLE_LIST
	};

	static IFormData[] TAG_DEFINITIONS_20 = new IFormData[] {
		TAG_GENERAL,
		new FormData(
			Messages.TLDFormLayoutData_TagAttributes,
			"", //$NON-NLS-1$
			new FormAttributeData[]{new FormAttributeData("name", 70), new FormAttributeData("required", 30)}, //$NON-NLS-1$ //$NON-NLS-2$
			new String[]{"TLDAttribute20", "TLDAttribute2F"}, //$NON-NLS-1$ //$NON-NLS-2$
			new IFormActionData[] {new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateAttribute"), REMOVE_ACTION, SELECT_ACTION, UP_ACTION, DOWN_ACTION } //$NON-NLS-1$
		),
		TAG_VARIABLE_LIST
	};

	static IFormData[] TAG_DEFINITIONS_21 = new IFormData[] {
		TAG_GENERAL,
		new FormData(
			Messages.TLDFormLayoutData_TagAttributes,
			"", //$NON-NLS-1$
			new FormAttributeData[]{new FormAttributeData("name", 70), new FormAttributeData("required", 30)}, //$NON-NLS-1$ //$NON-NLS-2$
			new String[]{"TLDAttribute21", "TLDAttribute2F"}, //$NON-NLS-1$ //$NON-NLS-2$
			new IFormActionData[] {new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateAttribute"), REMOVE_ACTION, SELECT_ACTION, UP_ACTION, DOWN_ACTION } //$NON-NLS-1$
		),
		TAG_VARIABLE_LIST
	};

	private final static IFormData[] FORM_LAYOUT_DEFINITIONS =
		new IFormData[] {
			new FormData(
				"FileTLD_PRO", //$NON-NLS-1$
				new IFormData[] {
					// Tiles Config Description Form
					new FormData(
						Messages.TLDFormLayoutData_TagLibraryDescription,
						"", // "Description, description, description", //$NON-NLS-1$
						new FormAttributeData[]{new FormAttributeData("tlibversion"), new FormAttributeData("jspversion"), new FormAttributeData("shortname"), new FormAttributeData("uri"), new FormAttributeData("info", InfoLayoutDataFactory.getInstance())} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
					),
					// Tiles PutLists Form
					new FormData(
						Messages.TLDFormLayoutData_DefinedTags,
						"", //$NON-NLS-1$
						new FormAttributeData[]{new FormAttributeData("name", 50), new FormAttributeData("tagclass", 50)}, //$NON-NLS-1$ //$NON-NLS-2$
						new String[]{"TLDTag"}, //$NON-NLS-1$
						new IFormActionData[] {new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateTag"), REMOVE_ACTION, SELECT_ACTION, UP_ACTION, DOWN_ACTION } //$NON-NLS-1$
					)
				}
			),
			new FormData(
				"TLDTag", //$NON-NLS-1$
				new IFormData[] {
					// Tiles Basic Definition Form
					new FormData(
						Messages.TLDFormLayoutData_TagDescription,
						"", // "Description, description, description", //$NON-NLS-1$
						new FormAttributeData[]{new FormAttributeData("name"), new FormAttributeData("tagclass", null, STBFE_CLASS_NAME), new FormAttributeData("teiclass", null, STBFE_CLASS_NAME), new FormAttributeData("bodycontent"), new FormAttributeData("info", InfoLayoutDataFactory.getInstance())} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
					),
					// Tiles Advanced Definition Form
					new FormData(
						Messages.TLDFormLayoutData_TagAttributes,
						"", //$NON-NLS-1$
						new FormAttributeData[]{new FormAttributeData("name", 33), new FormAttributeData("required", 33), new FormAttributeData("rtexprvalue", 33)}, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						new String[]{"TLDAttribute"}, //$NON-NLS-1$
						new IFormActionData[] {new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateAttribute"), REMOVE_ACTION, EDIT_ACTION, UP_ACTION, DOWN_ACTION } //$NON-NLS-1$
					)
				}
			),
			// Tiles Basic Put Form
			new FormData(
				Messages.TLDFormLayoutData_Listeners,
				"", // "Description, description, description", //$NON-NLS-1$
				"TLDListeners", //$NON-NLS-1$
				new FormAttributeData[]{new FormAttributeData("listener-class", 100)}, //$NON-NLS-1$
				new String[]{"TLDListener"}, //$NON-NLS-1$
				new IFormActionData[] {new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateListener"), REMOVE_ACTION, SELECT_ACTION, UP_ACTION, DOWN_ACTION } //$NON-NLS-1$
			),
			new FormData(
				Messages.TLDFormLayoutData_Functions,
				"", // "Description, description, description", //$NON-NLS-1$
				"TLDFunctions", //$NON-NLS-1$
				new FormAttributeData[]{new FormAttributeData("name", 40), new FormAttributeData("function-class", 60)}, //$NON-NLS-1$ //$NON-NLS-2$
				new String[]{"TLDFunction"}, //$NON-NLS-1$
				new IFormActionData[] {new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateFunction"), REMOVE_ACTION, EDIT_ACTION, UP_ACTION, DOWN_ACTION } //$NON-NLS-1$
			),
			new FormData(
				"TLDValidator", //$NON-NLS-1$
				new IFormData[] {
					// Tiles Put List Attributes Form
					new FormData(
						Messages.TLDFormLayoutData_Validator,
						"", // "Description, description, description", //$NON-NLS-1$
						new FormAttributeData[]{new FormAttributeData("validator-class", null, STBFE_CLASS_NAME), new FormAttributeData("description", InfoLayoutDataFactory.getHighInstance())} //$NON-NLS-1$ //$NON-NLS-2$
					),
					// Tiles Put List Adds Form
					new FormData(
						Messages.TLDFormLayoutData_InitParams,
						"", //$NON-NLS-1$
						new FormAttributeData[]{new FormAttributeData("param-name", 50), new FormAttributeData("param-value", 50)}, //$NON-NLS-1$ //$NON-NLS-2$
						new String[]{"WebAppInitParam"}, //$NON-NLS-1$
						new IFormActionData[] {new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateInitParam"), REMOVE_ACTION, EDIT_ACTION, UP_ACTION, DOWN_ACTION } //$NON-NLS-1$
					)
				}
			),
			new FormData(
				"FileTLD_1_2", createFileTagDefinitions("FileTLD_1_2", "TLDTag12") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			),
			new FormData(
				"FileTLD_2_0", createFileTagDefinitions("FileTLD_2_0", "TLDTag20") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			),
			new FormData(
				"FileTLD_2_1", createFileTagDefinitions("FileTLD_2_1", "TLDTag21") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			),
			new FormData(
				"TLDTag12", TAG_DEFINITIONS //$NON-NLS-1$
			),
			new FormData(
				"TLDTag20", TAG_DEFINITIONS_20 //$NON-NLS-1$
			),
			new FormData(
				"TLDTag21", TAG_DEFINITIONS_21 //$NON-NLS-1$
			)
		};

	private static Map FORM_LAYOUT_DEFINITION_MAP = Collections.unmodifiableMap(new ArrayToMap(FORM_LAYOUT_DEFINITIONS));

	private static final TLDFormLayoutData INSTANCE = new TLDFormLayoutData();

	public static TLDFormLayoutData getInstance() {
		return INSTANCE;
	}

	private TLDFormLayoutData() {
	} 

	public IFormData getFormData(String entityName) {
		return (IFormData)FORM_LAYOUT_DEFINITION_MAP.get(entityName);
	}
}