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
package org.jboss.tools.jst.web.tiles.model;

import java.io.*;

import org.w3c.dom.*;

import org.jboss.tools.common.meta.*;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.XModelObjectConstants;
import org.jboss.tools.common.model.filesystems.FileAuxiliary;
import org.jboss.tools.common.model.filesystems.impl.*;
import org.jboss.tools.common.model.loaders.impl.SimpleWebFileLoader;
import org.jboss.tools.common.model.util.*;
import org.jboss.tools.jst.web.model.WebProcessLoader;

public class FileTilesLoader implements WebProcessLoader, TilesConstants {
	private FileAuxiliary aux = new FileAuxiliary("l4t", false);
	XModelObjectLoaderUtil util = createUtil();

	public void load(XModelObject object) {
		String body = XModelObjectLoaderUtil.getTempBody(object);
        int resolution = EntityXMLRegistration.getInstance().resolve(object.getModelEntity());
		String[] errors = 
//			XMLUtil.getXMLErrors(new StringReader(body));
			XMLUtil.getXMLErrors(new StringReader(body), resolution == EntityXMLRegistration.DTD, resolution == EntityXMLRegistration.SCHEMA);
		boolean hasErrors = (errors != null && errors.length > 0);
		if(hasErrors) {
			object.setAttributeValue("isIncorrect", "yes");
			object.setAttributeValue("incorrectBody", body);
			object.set("actualBodyTimeStamp", "-1");
//			XModelObjectLoaderUtil.addRequiredChildren(object);
//			return;
		} else {
			object.setAttributeValue("isIncorrect", "no");
			object.set("correctBody", body);
			object.set("actualBodyTimeStamp", "0");
			object.setAttributeValue("incorrectBody", "");
		}
		Document doc = XMLUtil.getDocument(new StringReader(body));
		if(doc == null) {
			XModelObjectLoaderUtil.addRequiredChildren(object);
			return;
		}
		Element element = doc.getDocumentElement();
		util.load(element, object);
		String loadingError = util.getError();
		
		setEncoding(object, body);
		NodeList nl = doc.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if(n instanceof DocumentType) {
				DocumentType dt = (DocumentType)n;
				object.setAttributeValue("systemId", dt.getSystemId());
			}
		}
		reloadProcess(object);
		object.set("actualBodyTimeStamp", "" + object.getTimeStamp());

		((AbstractXMLFileImpl)object).setLoaderError(loadingError);
		if(!hasErrors && loadingError != null) {
			object.setAttributeValue("isIncorrect", "yes");
			object.setAttributeValue("incorrectBody", body);
			object.set("actualBodyTimeStamp", "" + object.getTimeStamp());
		}
	}
    
	protected void setEncoding(XModelObject object, String body) {
		String encoding = XModelObjectLoaderUtil.getEncoding(body);
		if(encoding == null) encoding = "";
		object.setAttributeValue(XModelObjectConstants.ATTR_NAME_ENCODING, encoding);
	}
    
	public void reloadProcess(XModelObject object) {
		TilesProcessImpl process = (TilesProcessImpl)object.getChildByPath(ELM_PROCESS);
		if(process == null) return;
		process.setReference(object);
		if(!object.isActive()) return;
		String bodyAux = (object.getParent() == null ? null : aux.read(object.getParent(), object));
		if (bodyAux != null) {
			Document doc2 = XMLUtil.getDocument(new StringReader(bodyAux));
			if (doc2 == null) {
			} else {
				util.load(doc2.getDocumentElement(), process);
			}
		}
		process.setReference(null);
		process.firePrepared();
	}
    
	public boolean update(XModelObject object) {
		XModelObject p = object.getParent();
		if (p == null) return true;
		FolderLoader fl = (FolderLoader)p;
		String body = fl.getBodySource(FileAnyImpl.toFileName(object)).get();
		AbstractExtendedXMLFileImpl f = (AbstractExtendedXMLFileImpl)object;
		f.setUpdateLock();
		try {
			f.edit(body, true);
		} finally {
			f.releaseUpdateLock();
		}
		object.setModified(false);
		XModelObjectLoaderUtil.updateModifiedOnSave(object);
		return true;
	}

	public boolean save(XModelObject object) {
		if (!object.isModified()) return true;
		FileAnyImpl file = (FileAnyImpl)object;
		XModelObjectLoaderUtil.setTempBody(object, file.getAsText());
		if("yes".equals(object.get("isIncorrect"))) {
			return true;
		}
		return saveLayout(object);
	}
    
	static boolean DO_NOT_SAVE = true;

	public boolean saveLayout(XModelObject object) {
		if(DO_NOT_SAVE) return true;
		XModelObjectLoaderUtil util = new XModelObjectLoaderUtil();
		try {
			XModelObject process = object.getChildByPath(ELM_PROCESS);
			if(process == null) return true;
			process.setModified(true);
			Element element = XMLUtil.createDocumentElement("PROCESS");
			util.saveAttributes(element, process);
			util.saveChildren(element, process);
			StringWriter sw = new StringWriter();
			XModelObjectLoaderUtil.serialize(element, sw);
			XModelObjectLoaderUtil.setTempBody(process, sw.toString());
			aux.write(object.getParent(), object, process);
			return true;
		} catch (Exception exc) {
			return false;
		}
	}    

	public String serializeMainObject(XModelObject object) {
//		String entity = object.getModelEntity().getName();
		String systemId = object.getAttributeValue("systemId");
		if(systemId == null || systemId.length() == 0) systemId = DOC_EXTDTD;
		String publicId = DOC_PUBLICID;
		Element element = XMLUtil.createDocumentElement(object.getModelEntity().getXMLSubPath(), DOC_QUALIFIEDNAME, publicId, systemId, null);

		try {
			util.setup(null, false);
			String att = object.getAttributeValue("comment");
			if (att.length() > 0) util.saveAttribute(element, "#comment", att);
			util.saveChildren(element, object);
            return SimpleWebFileLoader.serialize(element, object);
		} catch (Exception e) {
			return null;
		}
	}

	public String mainObjectToString(XModelObject object) {
		return "" + serializeMainObject(object);
	}

	public String serializeObject(XModelObject object) {
		return serializeMainObject(object);
	}

    protected XModelObjectLoaderUtil createUtil() {
        return new FTLoaderUtil();
    }

	public void loadFragment(XModelObject object, Element element) {
		util.load(element, object);		
	}

}

class FTLoaderUtil extends XModelObjectLoaderUtil {
    static String tilesRequired = "!TilesDefinition.name!TilesPut.name!TilesList.name!TilesBean.classtype!TilesSetProperty.property!TilesSetProperty.value!TilesItem.value!TilesItem.link!";
    static String SAVE_CONTENT_ATTR = "save value as 'content' attr";

    protected boolean isSaveable(XModelEntity entity, String n, String v, String dv) {
        if(v == null) return false;
        if(v.length() == 0)
          return (tilesRequired.indexOf("!" + entity.getName() + "." + n + "!") >= 0);
        return super.isSaveable(entity, n, v, dv);
    }

    public String getAttribute(Element element, String xmlname) {
        int i = xmlname.indexOf('|');
        if(i < 0 || xmlname.startsWith("content|")) return super.getAttribute(element, xmlname);
        String v = super.getAttribute(element, xmlname.substring(0, i));
        return (v != null && v.length() > 0) ? v :
               super.getAttribute(element, xmlname.substring(i + 1));
    }

    public void saveAttribute(Element element, String xmlname, String value) {
        int i = xmlname.indexOf('|');
        if(i >= 0) xmlname = (value.indexOf('\n') >= 0) ? xmlname.substring(i + 1)
                             : xmlname.substring(0, i);
        super.saveAttribute(element, xmlname, value);
    }

}
