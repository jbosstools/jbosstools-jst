package org.jboss.tools.jst.jsp.drop.treeviewer.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.jst.jsp.outline.ValueHelper;

public abstract class SeamElement extends ModelElement implements IAttributeValueContainer, IAttributeValue {
	protected ModelElement[] elements = null;

	public SeamElement(String name, ModelElement parent) {
		super(name, parent);
	}
	
	protected SeamVariablesResourceElement getResource() {
		ModelElement p = getParent();
		while(p != null && !(p instanceof SeamVariablesResourceElement)) p = p.getParent();
		return (SeamVariablesResourceElement)p;
	}

	public ModelElement[] getChildren() {
		if(elements != null) return elements;
		SeamVariablesResourceElement resource = getResource();
		if(resource == null) return elements = EMPTY_LIST;
		IFile f = resource.getValueHelper().getFile();
		String prefix = getComparedValue() + "."; //$NON-NLS-1$
		Properties p = new Properties();
		p.put("file", f); //$NON-NLS-1$
		List list = ValueHelper.seamPromptingProvider.getList(null, "seam.members", prefix, p); //$NON-NLS-1$
		Set<String> methods = new HashSet<String>();
		for (int i = 0; i < list.size(); i++) {
			String s = list.get(i).toString();
			if(s.indexOf('(') < 0) continue;
			methods.add(s.substring(0, s.indexOf('(')));
		}
		ArrayList<ModelElement> cs = new ArrayList<ModelElement>();
		for (int i = 0; i < list.size(); i++) {
			String s = list.get(i).toString();
			int k = s.indexOf('(');
			if(k < 0 && methods.contains(s)) continue;
			if(k >= 0) {
				s = s.substring(0, k);
				SeamMethodElement m = new SeamMethodElement(s, this);
				cs.add(m);
			} else {
				SeamPropertyElement c = new SeamPropertyElement(s, this);
				cs.add(c);
			}
		}
		return elements = cs.toArray(new ModelElement[0]);
	}

}
