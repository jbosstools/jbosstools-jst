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
package org.jboss.tools.jst.web.tld.model;

import org.w3c.dom.*;

import org.jboss.tools.common.meta.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.engines.impl.EnginesLoader;
import org.jboss.tools.common.model.util.*;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.jst.web.WebModelPlugin;

public class FileTLD12Loader extends FileTLDLoader {

    protected XModelObjectLoaderUtil createUtil() {
        return new TLDLoader12Util(getTagEntity());
    }

    protected String getTagEntity() {
    	return "TLDTag12"; //$NON-NLS-1$
    }

}

class TLDLoader12Util extends XModelObjectLoaderUtil implements TLDConstants {
	String tagEntity;
	
	public TLDLoader12Util(String tagEntity) {
		this.tagEntity = tagEntity;
	}

	public void loadChildren(Element element, XModelObject o) {
		if(o.getFileType() == XModelObject.FILE) {
			addRequiredChildren(o);
			XModelObject[] vs = o.getChildren("TLDValidator"); //$NON-NLS-1$
			if(vs.length > 0) {
				Element ce = XMLUtil.getUniqueChild(element, applyNamespaceToTag("validator")); //$NON-NLS-1$
				if(ce != null) super.load(ce, vs[0]);				
			}
			XModelObject ls = o.getChildByPath("Listeners"); //$NON-NLS-1$
			if(ls != null) super.loadChildren(element, ls);
			XModelObject fs = o.getChildByPath("Functions"); //$NON-NLS-1$
			if(fs != null) super.loadChildren(element, fs);
			loadTags(element, o);
		} else {
			super.loadChildren(element, o);
		}
	}
	
	private void loadTags(Element element, XModelObject o) {
		loadElements(element, o, tagEntity);
		loadElements(element, o, "TLDTagFile"); //$NON-NLS-1$
	}
	
	void loadElements(Element element, XModelObject o, String childEntityName) {
		XModel model = o.getModel();
		XModelEntity childEntity = model.getMetaData().getEntity(childEntityName);
		if(childEntity == null) return;
		XModelEntity entity = o.getModelEntity();
		XChild cs = entity.getChild(childEntityName);
		if(cs == null) return;
		NodeList nl = element.getElementsByTagName(childEntity.getXMLSubPath());
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if(n.getNodeType() != Node.ELEMENT_NODE) continue;
			Element e = (Element)n;
			String en = cs.getName();
			XModelObject co = model.createModelObject(en, null);
			if(co == null) continue;
			load(e, co);
			if(!o.addChild(co) && co.getFileType() == XModelObject.NONE) {
				if(o.isActive()) try {            		
					XModelObject q = o.getChildByPath(co.getPathPart());
					if(q != null) EnginesLoader.merge(q, co, false);
				} catch (Exception exc) {
					WebModelPlugin.getPluginLog().logError(exc);
				}
				continue;
			} 
		}
	}

	public boolean saveChildren(Element element, XModelObject o) {
		if(o.getFileType() == XModelObject.FILE) {
			XModelObject[] vs = o.getChildren("TLDValidator"); //$NON-NLS-1$
			if(vs.length > 0) {
				TLDValidatorImpl v = (TLDValidatorImpl)vs[0];
				if(!v.isEmpty()) super.save(element, vs[0]);
			}
			XModelObject ls = o.getChildByPath("Listeners"); //$NON-NLS-1$
			if(ls != null) super.saveChildren(element, ls);
			saveTags(element, o);
			XModelObject fs = o.getChildByPath("Functions"); //$NON-NLS-1$
			if(fs != null) super.saveChildren(element, fs);
			return true;
		} else {
			return super.saveChildren(element, o);
		}
	}

	public boolean saveTags(Element element, XModelObject o) {
		XModelObject[] os = o.getChildren(tagEntity);
		boolean b = true;
		for (int i = 0; i < os.length; i++) if(!save(element, os[i])) b = false;
		os = o.getChildren("TLDTagFile"); //$NON-NLS-1$
		for (int i = 0; i < os.length; i++) if(!save(element, os[i])) b = false;
		return b;
	}
	
    protected boolean isSaveable(XModelEntity entity, String n, String v, String dv) {
        if(v == null) return false;
		if(v.length() == 0 || v.equals(dv)) {
			XAttribute attr = entity.getAttribute(n);
			return (attr != null && "always".equals(attr.getProperty("save"))); //$NON-NLS-1$ //$NON-NLS-2$
		}
        return super.isSaveable(entity, n, v, dv);
    }

    public void saveAttributes(Element element, XModelObject o) {
        super.saveAttributes(element, o);
        if(o.getModelEntity().getName().equals("TLDVariable")) { //$NON-NLS-1$
        	eitherOr(element, NAME_FROM_ATTRIBUTE, NAME_GIVEN);
        }
    }

    protected String getChildEntity(XModelEntity entity, Element e) {
    	String ce = super.getChildEntity(entity, e);
    	if(ce != null && ce.startsWith("TLDAttribute2")) { //$NON-NLS-1$
    		Element c = XMLUtilities.getUniqueChild(e, "fragment"); //$NON-NLS-1$
    		if(c != null) return "TLDAttribute2F"; //$NON-NLS-1$
    		if(ce.equals("TLDAttribute2F")) { //$NON-NLS-1$
    			XChild[] cs = entity.getChildren();
    			for (int i = 0; i < cs.length; i++) {
    				String cei = cs[i].getName();
    				if(cei.startsWith("TLDAttribute2") && !cei.endsWith("F")) return cei; //$NON-NLS-1$ //$NON-NLS-2$
    			}
    		}
    	}
    	return ce;
    }
}

