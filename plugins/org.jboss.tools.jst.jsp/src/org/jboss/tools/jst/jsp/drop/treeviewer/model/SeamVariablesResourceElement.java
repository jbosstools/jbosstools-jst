package org.jboss.tools.jst.jsp.drop.treeviewer.model;


import java.util.List;
import java.util.Properties;

import org.eclipse.ui.IEditorInput;
import org.jboss.tools.jst.jsp.outline.ValueHelper;

public class SeamVariablesResourceElement extends AttributeValueResource {
	IEditorInput editorInput;
	
	SeamVariableElement[] elements = null;

	public SeamVariablesResourceElement(IEditorInput editorInput, String name, ModelElement parent) {
		super(name, parent);
		this.editorInput = editorInput;
	}

	public ModelElement[] getChildren() {
		if(elements != null) {
			return elements;
		}
		Properties p = new Properties();
		p.put("file", valueHelper.getFile());
		List list = ValueHelper.seamPromptingProvider.getList(null, "seam.variables", "", p);
		if(list == null) return EMPTY_LIST;
		SeamVariableElement[] es = new SeamVariableElement[list.size()];
		for (int i = 0; i < es.length; i++) {
			es[i] = new SeamVariableElement(list.get(i).toString(), this);
		}
		return elements = es;
	}

	public String getName() {
		return "Seam Variables";
	}

}
