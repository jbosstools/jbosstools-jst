/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.taglib;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jboss.tools.common.model.project.ext.event.Change;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.jst.web.kb.internal.KbObject;
import org.jboss.tools.jst.web.kb.internal.KbXMLStoreConstants;
import org.jboss.tools.jst.web.kb.taglib.Facet;
import org.jboss.tools.jst.web.kb.taglib.IELFunction;
import org.jboss.tools.jst.web.kb.taglib.IFaceletTagLibrary;
import org.w3c.dom.Element;

/**
 * @author Viacheslav Kabanovich
 */
public class FaceletTagLibrary extends AbstractTagLib implements
		IFaceletTagLibrary {
	List<ELFunction> functions = new ArrayList<ELFunction>();
	IELFunction[] functionArray = null;

	public FaceletTagLibrary() {
		
	}

	public IELFunction[] getFunctions() {
		if(functionArray == null) {
			functionArray = functions.toArray(new ELFunction[0]);
		}
		return functionArray;
	}

	public void addFunction(ELFunction f) {
		functions.add(f);
		functionArray = null;
	}

	public FaceletTagLibrary clone() throws CloneNotSupportedException {
		FaceletTagLibrary copy = (FaceletTagLibrary)super.clone();
		copy.functions = new ArrayList<ELFunction>();
		for (IELFunction f: getFunctions()) {
			copy.addFunction(((ELFunction)f).clone());
		}
		return copy;
	}

	public String getXMLClass() {
		return KbXMLStoreConstants.CLS_FACELET_LIBRARY;
	}

	public List<Change> merge(KbObject s) {
		List<Change> changes = super.merge(s);
		FacesConfigTagLibrary t = (FacesConfigTagLibrary)s;
		//TODO
		return changes;
	}

	public Element toXML(Element parent, Properties context) {
		Element element = super.toXML(parent, context);

		for (IELFunction f: getFunctions()) {
			((KbObject)f).toXML(element, context);
		}

		return element;
	}

	public void loadXML(Element element, Properties context) {
		super.loadXML(element, context);
		
		Element[] cs = XMLUtilities.getChildren(element, KbXMLStoreConstants.TAG_FUNCTION);
		for (Element e: cs) {
			ELFunction f = new ELFunction();
			f.loadXML(e, context);
			addFunction(f);
		}
	}

}
