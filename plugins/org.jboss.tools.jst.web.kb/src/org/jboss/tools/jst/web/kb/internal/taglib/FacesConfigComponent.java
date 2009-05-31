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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jboss.tools.common.model.project.ext.event.Change;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.jst.web.kb.internal.KbObject;
import org.jboss.tools.jst.web.kb.internal.KbXMLStoreConstants;
import org.jboss.tools.jst.web.kb.taglib.Facet;
import org.w3c.dom.Element;

/**
 * @author Viacheslav Kabanovich
 */
public class FacesConfigComponent extends AbstractComponent {
	Map<String,Facet> facets = new HashMap<String, Facet>();
	Facet[] facetArray = EMPTY_FACET_SET;

	public Facet getFacet(String name) {
		return facets.get(name);
	}
	
	public static final Facet[] EMPTY_FACET_SET = new Facet[0];

	public Facet[] getFacets() {
		if(facetArray == null) {
			synchronized (facets) {
				if (facets.isEmpty()) {
					facetArray = EMPTY_FACET_SET;
				} else {
					facetArray = facets.values().toArray(new Facet[0]);
				}
			}
		}
		return facetArray;
	}

	public Facet[] getFacets(String nameTemplate) {
		Facet[] fs = getFacets();
		if(fs.length == 0) return EMPTY_FACET_SET;
		List<Facet> result = new ArrayList<Facet>();
		for (Facet f: fs) {
			String name = f.getName();
			//TODO implement better matching name and nameTemplate
			boolean match = name.startsWith(nameTemplate);
			if(match) result.add(f);
		}
		return result.isEmpty() ? EMPTY_FACET_SET : result.toArray(new Facet[0]);
	}

	public void addFacet(Facet f) {
		adopt((KbObject)f);
		synchronized(facets) {
			facets.put(f.getName(), f);
			facetArray = null;
		}
	}

	public FacesConfigComponent clone() throws CloneNotSupportedException {
		FacesConfigComponent copy = new FacesConfigComponent();
		copy.facets = new HashMap<String, Facet>();
		for (Facet f: getFacets()) {
			copy.addFacet(f.clone());
		}		
		return copy;
	}

	public String getXMLClass() {
		return KbXMLStoreConstants.CLS_FACESCONFIG_LIBRARY;
	}

	public List<Change> merge(KbObject s) {
		List<Change> changes = super.merge(s);
		FacesConfigComponent c = (FacesConfigComponent)s;

		Change children = new Change(this, null, null, null);
		mergeFacets(c, children);
		changes = Change.addChange(changes, children);

		return changes;
	}

	public void mergeFacets(FacesConfigComponent c, Change children) {
		Map<Object,Facet> facetMap = new HashMap<Object, Facet>();
		for (Facet f: getFacets()) facetMap.put(f.getId(), f);
		for (Facet f: c.getFacets()) {
			Facet loaded = f;
			Facet current = facetMap.get(loaded.getId());
			if(current == null) {
				addFacet(loaded);
				Change change = new Change(this, null, null, loaded);
				children.addChildren(Change.addChange(null, change));
			} else {
				List<Change> rc = current.merge(loaded);
				if(rc != null) children.addChildren(rc);
			}
		}
		for (Facet f: facetMap.values()) {
			Facet removed = f;
			synchronized (facets) {
				if(facets.get(removed.getName()) != removed) {
					continue;
				}
				facets.remove(removed.getName());
				facetArray = null;
			}
			Change change = new Change(this, null, removed, null);
			children.addChildren(Change.addChange(null, change));
		}
	}

	public Element toXML(Element parent, Properties context) {
		Element element = super.toXML(parent, context);

		for (Facet f: getFacets()) {
			f.toXML(element, context);
		}

		return element;
	}

	public void loadXML(Element element, Properties context) {
		super.loadXML(element, context);
		
		Element[] cs = XMLUtilities.getChildren(element, KbXMLStoreConstants.TAG_FACET);
		for (Element e: cs) {
			Facet f = new Facet();
			f.loadXML(e, context);
			addFacet(f);
		}
	}

}
