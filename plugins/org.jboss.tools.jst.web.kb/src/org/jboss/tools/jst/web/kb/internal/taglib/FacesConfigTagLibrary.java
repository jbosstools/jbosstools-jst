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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.tools.common.model.project.ext.event.Change;
import org.jboss.tools.jst.web.kb.internal.KbObject;
import org.jboss.tools.jst.web.kb.internal.KbXMLStoreConstants;
import org.jboss.tools.jst.web.kb.taglib.Facet;
import org.jboss.tools.jst.web.kb.taglib.IComponent;
import org.jboss.tools.jst.web.kb.taglib.IFacesConfigTagLibrary;

/**
 * @author Viacheslav Kabanovich
 */
public class FacesConfigTagLibrary extends AbstractTagLib implements
		IFacesConfigTagLibrary {

	Map<String, IComponent> componentsByType = new HashMap<String, IComponent>();

	public void addComponent(IComponent component) {
		super.addComponent(component);
		componentsByType.put(component.getComponentType(), component);
	}

	public IComponent getComponentByType(String type) {
		return componentsByType.get(type);
	}

	public FacesConfigTagLibrary clone() throws CloneNotSupportedException {
		FacesConfigTagLibrary copy = (FacesConfigTagLibrary)super.clone();
		
		return copy;
	}

	public List<Change> merge(KbObject s) {
		List<Change> changes = super.merge(s);
		FacesConfigTagLibrary t = (FacesConfigTagLibrary)s;

		Change children = new Change(this, null, null, null);
		mergeFunctions(t, children);
		changes = Change.addChange(changes, children);

		return changes;
	}

	public void mergeFunctions(FacesConfigTagLibrary c, Change children) {
		//TODO
	}

	public String getXMLClass() {
		return KbXMLStoreConstants.CLS_FACESCONFIG_LIBRARY;
	}

}
