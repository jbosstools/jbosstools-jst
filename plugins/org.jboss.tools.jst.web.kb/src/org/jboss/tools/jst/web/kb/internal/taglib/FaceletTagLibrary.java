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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jboss.tools.common.model.project.ext.IValueInfo;
import org.jboss.tools.common.model.project.ext.event.Change;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.internal.KbObject;
import org.jboss.tools.jst.web.kb.internal.KbXMLStoreConstants;
import org.jboss.tools.jst.web.kb.internal.scanner.JSF2ResourcesScanner;
import org.jboss.tools.jst.web.kb.taglib.IComponent;
import org.jboss.tools.jst.web.kb.taglib.IFaceletTagLibrary;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;
import org.w3c.dom.Element;

/**
 * @author Viacheslav Kabanovich
 */
public class FaceletTagLibrary extends FunctionTagLib implements
		IFaceletTagLibrary {
	public static final String COMPOSITE_LIBRARY_NAME = "composite-library-name";

	protected String compositeLibraryName = null;
	protected Map<Object, Cloned> copies = new Hashtable<Object, Cloned>();
	
	class Cloned {
		IComponent origin;
		IComponent copy;
		
		public Cloned(IComponent origin) {
			setObject(origin);
		}

		public void setObject(IComponent origin) {
			this.origin = origin;
			try {
				copy = (IComponent)((KbObject)origin).clone();
				((KbObject)copy).setParent(FaceletTagLibrary.this);
			} catch (CloneNotSupportedException e) {
				WebKbPlugin.getDefault().logError(e);
				copy = origin;
			}
		}
	}

	public FaceletTagLibrary() {		
	}

	@Override
	public FaceletTagLibrary clone() throws CloneNotSupportedException {
		FaceletTagLibrary copy = (FaceletTagLibrary)super.clone();
		copy.compositeLibraryName = compositeLibraryName;
		return copy;
	}

	@Override
	public String getXMLClass() {
		return KbXMLStoreConstants.CLS_FACELET_LIBRARY;
	}

	public String getCompositeLibraryName() {
		return compositeLibraryName;
	}

	public void setCompositeLibraryName(String s) {
		compositeLibraryName = s;
	}

	public ITagLibrary[] getCompositeLibraries() {
		List<ITagLibrary> result = new ArrayList<ITagLibrary>();
		if(compositeLibraryName != null && compositeLibraryName.length() > 0) {
			String uri = JSF2ResourcesScanner.URI_PREFIX + "/" + compositeLibraryName;
			ITagLibrary[] list = getKbProject() == null ? new ITagLibrary[0] : getKbProject().getTagLibraries(uri);
			if(list.length > 0) {
				for (ITagLibrary l: list) result.add(l);
			}
		}
		if(compositeLibraryName != null && compositeLibraryName.length() > 0) {
			String uri = JSF2ResourcesScanner.URI_PREFIX_22 + "/" + compositeLibraryName;
			ITagLibrary[] list = getKbProject() == null ? new ITagLibrary[0] : getKbProject().getTagLibraries(uri);
			if(list.length > 0) {
				for (ITagLibrary l: list) result.add(l);
			}
		}
		return result.toArray(new ITagLibrary[0]);
	}
	
	public void setCompositeLibraryName(IValueInfo s) {
		compositeLibraryName = s == null ? null : s.getValue();
		attributesInfo.put(COMPOSITE_LIBRARY_NAME, s);
	}

	public void onCompositeLibraryChanged() {
		copies.clear();
	}

	@Override
	public IComponent getComponent(String name) {
		IComponent result = super.getComponent(name);
		if(result == null) {
			ITagLibrary[] ls = getCompositeLibraries();
			for (int i = 0; result == null && i < ls.length; i++) {
				result = ls[i].getComponent(name);
				if(result != null) {
					result = copy(result);
				}
			}
		}
		return result;
	}

	@Override
	public IComponent[] getComponents() {
		IComponent[] result = super.getComponents();
		ITagLibrary[] ls = getCompositeLibraries();
		if(ls.length > 0) {
			result = union(result, collectComponents(ls));
		}
		return result;
	}

	private IComponent[] collectComponents(ITagLibrary[] ls) {
		if(ls.length == 0) {
			return new IComponent[0];
		} else {
			List<IComponent> cs = new ArrayList<IComponent>();
			for (ITagLibrary l: ls) {
				IComponent[] cs2 = l.getComponents();
				for (IComponent c: cs2) {
					cs.add(c);
				}
			}
			return cs.toArray(new IComponent[0]);
		}
	}

	private void copy(IComponent[] cs2) {
		for (int i = 0; i < cs2.length; i++) {
			cs2[i] = copy(cs2[i]);
		}
	}

	private IComponent copy(IComponent c) {
		Cloned cl = copies.get(((KbObject)c).getId());
		if(cl == null) {
			cl = new Cloned(c);
			copies.put(((KbObject)c).getId(), cl);
		}
		if(cl.origin != c) {
			cl.setObject(c);
		}
		return cl.copy;
	}

	private IComponent[] union(IComponent[] cs1, IComponent[] cs2) {
		copy(cs2);
		if(cs1.length == 0) {
			return cs2;
		}
		if(cs2.length == 0) {
			return cs1;
		}
		IComponent[] res = new IComponent[cs1.length + cs2.length];
		System.arraycopy(cs1, 0, res, 0, cs1.length);
		System.arraycopy(cs2, 0, res, cs1.length, cs2.length);
		return res;
	}

	@Override
	public List<Change> merge(KbObject s) {
		List<Change> changes = super.merge(s);
		FaceletTagLibrary library = (FaceletTagLibrary)s;
		if(!stringsEqual(compositeLibraryName, library.compositeLibraryName)) {
			changes = Change.addChange(changes, new Change(this, COMPOSITE_LIBRARY_NAME, compositeLibraryName, library.compositeLibraryName));
			compositeLibraryName = library.compositeLibraryName;
			copies.clear();
		}
		return changes;
	}

	@Override
	public void loadXML(Element element, Properties context) {
		super.loadXML(element, context);
		setCompositeLibraryName(attributesInfo.get(COMPOSITE_LIBRARY_NAME));
	}
}