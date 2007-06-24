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
import org.jboss.tools.common.model.ui.forms.InfoLayoutDataFactory;

/**
 * @author Igels
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TLDFormLayoutData implements IFormLayoutData {

	private static final FormActionData REMOVE_ACTION = new FormActionData(TableStructuredEditor.REMOVE_ACTION, "DeleteActions.Delete");
	private static final FormActionData EDIT_ACTION = new FormActionData(TableStructuredEditor.EDIT_ACTION, "Properties.Properties");
	private static final FormActionData UP_ACTION = new FormActionData(TableStructuredEditor.UP_ACTION, "%internal%");
	private static final FormActionData DOWN_ACTION = new FormActionData(TableStructuredEditor.DOWN_ACTION, "%internal%");
	private static final FormActionData SELECT_ACTION = new FormActionData(TableStructuredEditor.EDIT_ACTION, "%SelectIt%");

/*	private final static IFormActionData[] DEFAULT_TABLE_ACTION_TYPE = new IFormActionData[] {
		new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateTag"),
		new FormActionData(TableStructuredEditor.REMOVE_ACTION, "DeleteActions.Delete"),
		new FormActionData(TableStructuredEditor.EDIT_ACTION, "Properties.Properties"),
		new FormActionData(TableStructuredEditor.UP_ACTION, "%internal%"),
		new FormActionData(TableStructuredEditor.DOWN_ACTION, "%internal%")
	};*/
	
	private final static String STBFE_CLASS_NAME = "org.jboss.tools.common.model.ui.attribute.editor.JavaHyperlinkLineFieldEditor";
	
	static IFormData[] createFileTagDefinitions(String tagEntity) {
		return new IFormData[] {
		// Tiles Config Description Form
		new FormData(
			"Tag Library Description",
			"", // "Description, description, description",
			new FormAttributeData[]{new FormAttributeData("tlibversion"), new FormAttributeData("jspversion"), new FormAttributeData("shortname"), new FormAttributeData("uri"), new FormAttributeData("display-name"), new FormAttributeData("small-icon"), new FormAttributeData("large-icon"), new FormAttributeData("description", InfoLayoutDataFactory.getInstance())}
		),
		// Tiles PutLists Form
		new FormData(
			"Defined Tags",
			"",
			new FormAttributeData[]{new FormAttributeData("name", 50), new FormAttributeData("tagclass", 50)},
			new String[]{tagEntity},
			new IFormActionData[] {new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateTag"), REMOVE_ACTION, SELECT_ACTION, UP_ACTION, DOWN_ACTION }
		)
		};
	}
	
	static IFormData TAG_VARIABLE_LIST = new FormData(
		"Tag Variables",
		"",
		new FormAttributeData[]{new FormAttributeData("name-given", 25), new FormAttributeData("name-from-attribute", 25), new FormAttributeData("variable-class", 25),  new FormAttributeData("scope", 25)},
		new String[]{"TLDVariable"},
		new IFormActionData[] {new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateVariable"), REMOVE_ACTION, EDIT_ACTION, UP_ACTION, DOWN_ACTION }
	);
	
	static IFormData TAG_GENERAL = new FormData(
		"Tag Description",
		"", // "Description, description, description",
		new FormAttributeData[]{new FormAttributeData("name"), new FormAttributeData("tagclass", null, STBFE_CLASS_NAME), new FormAttributeData("teiclass", null, STBFE_CLASS_NAME), new FormAttributeData("bodycontent"), new FormAttributeData("display-name"), new FormAttributeData("small-icon"), new FormAttributeData("large-icon"), new FormAttributeData("description", InfoLayoutDataFactory.getInstance()), new FormAttributeData("example", InfoLayoutDataFactory.getInstance())}
	);
	
	static IFormData[] TAG_DEFINITIONS = new IFormData[] {
		TAG_GENERAL,
		new FormData(
			"Tag Attributes",
			"",
			new FormAttributeData[]{new FormAttributeData("name", 25), new FormAttributeData("required", 25), new FormAttributeData("rtexprvalue", 25),  new FormAttributeData("type", 25)},
			new String[]{"TLDAttribute12"},
			new IFormActionData[] {new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateAttribute"), REMOVE_ACTION, EDIT_ACTION, UP_ACTION, DOWN_ACTION }
		),
		TAG_VARIABLE_LIST
	};

	static IFormData[] TAG_DEFINITIONS_20 = new IFormData[] {
		TAG_GENERAL,
		new FormData(
			"Tag Attributes",
			"",
			new FormAttributeData[]{new FormAttributeData("name", 70), new FormAttributeData("required", 30)},
			new String[]{"TLDAttribute20", "TLDAttribute2F"},
			new IFormActionData[] {new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateAttribute"), REMOVE_ACTION, SELECT_ACTION, UP_ACTION, DOWN_ACTION }
		),
		TAG_VARIABLE_LIST
	};

	static IFormData[] TAG_DEFINITIONS_21 = new IFormData[] {
		TAG_GENERAL,
		new FormData(
			"Tag Attributes",
			"",
			new FormAttributeData[]{new FormAttributeData("name", 70), new FormAttributeData("required", 30)},
			new String[]{"TLDAttribute21", "TLDAttribute2F"},
			new IFormActionData[] {new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateAttribute"), REMOVE_ACTION, SELECT_ACTION, UP_ACTION, DOWN_ACTION }
		),
		TAG_VARIABLE_LIST
	};

	private final static IFormData[] FORM_LAYOUT_DEFINITIONS =
		new IFormData[] {
			new FormData(
				"FileTLD_PRO",
				new IFormData[] {
					// Tiles Config Description Form
					new FormData(
						"Tag Library Description",
						"", // "Description, description, description",
						new FormAttributeData[]{new FormAttributeData("tlibversion"), new FormAttributeData("jspversion"), new FormAttributeData("shortname"), new FormAttributeData("uri"), new FormAttributeData("info", InfoLayoutDataFactory.getInstance())}
					),
					// Tiles PutLists Form
					new FormData(
						"Defined Tags",
						"",
						new FormAttributeData[]{new FormAttributeData("name", 50), new FormAttributeData("tagclass", 50)},
						new String[]{"TLDTag"},
						new IFormActionData[] {new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateTag"), REMOVE_ACTION, SELECT_ACTION, UP_ACTION, DOWN_ACTION }
					)
				}
			),
			new FormData(
				"TLDTag",
				new IFormData[] {
					// Tiles Basic Definition Form
					new FormData(
						"Tag Description",
						"", // "Description, description, description",
						new FormAttributeData[]{new FormAttributeData("name"), new FormAttributeData("tagclass", null, STBFE_CLASS_NAME), new FormAttributeData("teiclass", null, STBFE_CLASS_NAME), new FormAttributeData("bodycontent"), new FormAttributeData("info", InfoLayoutDataFactory.getInstance())}
					),
					// Tiles Advanced Definition Form
					new FormData(
						"Tag Attributes",
						"",
						new FormAttributeData[]{new FormAttributeData("name", 33), new FormAttributeData("required", 33), new FormAttributeData("rtexprvalue", 33)},
						new String[]{"TLDAttribute"},
						new IFormActionData[] {new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateAttribute"), REMOVE_ACTION, EDIT_ACTION, UP_ACTION, DOWN_ACTION }
					)
				}
			),
			// Tiles Basic Put Form
			new FormData(
				"Listeners",
				"", // "Description, description, description",
				"TLDListeners",
				new FormAttributeData[]{new FormAttributeData("listener-class", 100)},
				new String[]{"TLDListener"},
				new IFormActionData[] {new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateListener"), REMOVE_ACTION, SELECT_ACTION, UP_ACTION, DOWN_ACTION }
			),
			new FormData(
				"Functions",
				"", // "Description, description, description",
				"TLDFunctions",
				new FormAttributeData[]{new FormAttributeData("name", 40), new FormAttributeData("function-class", 60)},
				new String[]{"TLDFunction"},
				new IFormActionData[] {new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateFunction"), REMOVE_ACTION, EDIT_ACTION, UP_ACTION, DOWN_ACTION }
			),
			new FormData(
				"TLDValidator",
				new IFormData[] {
					// Tiles Put List Attributes Form
					new FormData(
						"Validator",
						"", // "Description, description, description",
						new FormAttributeData[]{new FormAttributeData("validator-class", null, STBFE_CLASS_NAME), new FormAttributeData("description", InfoLayoutDataFactory.getInstance())}
					),
					// Tiles Put List Adds Form
					new FormData(
						"Init Params",
						"",
						new FormAttributeData[]{new FormAttributeData("param-name", 50), new FormAttributeData("param-value", 50)},
						new String[]{"WebAppInitParam"},
						new IFormActionData[] {new FormActionData(TableStructuredEditor.ADD_ACTION, "CreateActions.CreateInitParam"), REMOVE_ACTION, EDIT_ACTION, UP_ACTION, DOWN_ACTION }
					)
				}
			),
			new FormData(
				"FileTLD_1_2", createFileTagDefinitions("TLDTag12")
			),
			new FormData(
				"FileTLD_2_0", createFileTagDefinitions("TLDTag20")
			),
			new FormData(
				"FileTLD_2_1", createFileTagDefinitions("TLDTag21")
			),
			new FormData(
				"TLDTag12", TAG_DEFINITIONS
			),
			new FormData(
				"TLDTag20", TAG_DEFINITIONS_20
			),
			new FormData(
				"TLDTag21", TAG_DEFINITIONS_21
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