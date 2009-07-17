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

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.jboss.tools.common.meta.XAttribute;
import org.jboss.tools.common.meta.XModelEntity;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.FileAuxiliary;
import org.jboss.tools.common.model.filesystems.impl.AbstractXMLFileImpl;
import org.jboss.tools.common.model.loaders.impl.SimpleWebFileLoader;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.util.EntityXMLRegistration;
import org.jboss.tools.common.model.util.XMLUtil;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.jboss.tools.jst.web.model.AbstractWebDiagramLoader;
import org.jboss.tools.jst.web.model.WebProcessLoader;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FileTilesLoader extends AbstractWebDiagramLoader implements WebProcessLoader, TilesConstants {

    protected FileAuxiliary createFileAuxiliary() {
    	return new FileAuxiliary("l4t", false); //$NON-NLS-1$
    }

    protected XModelObjectLoaderUtil createUtil() {
        return new FTLoaderUtil();
    }

	public void load(XModelObject object) {
		String body = XModelObjectLoaderUtil.getTempBody(object);
        int resolution = EntityXMLRegistration.getInstance().resolve(object.getModelEntity());
		String[] errors = 
//			XMLUtil.getXMLErrors(new StringReader(body));
			XMLUtil.getXMLErrors(new StringReader(body), resolution == EntityXMLRegistration.DTD, resolution == EntityXMLRegistration.SCHEMA);
		boolean hasErrors = (errors != null && errors.length > 0);
		if(hasErrors) {
			object.setAttributeValue("isIncorrect", "yes"); //$NON-NLS-1$ //$NON-NLS-2$
			object.setAttributeValue("incorrectBody", body); //$NON-NLS-1$
			object.set("actualBodyTimeStamp", "-1"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			object.setAttributeValue("isIncorrect", "no"); //$NON-NLS-1$ //$NON-NLS-2$
			object.set("correctBody", body); //$NON-NLS-1$
			object.set("actualBodyTimeStamp", "0"); //$NON-NLS-1$ //$NON-NLS-2$
			object.setAttributeValue("incorrectBody", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}
		Document doc = XMLUtil.getDocument(new StringReader(body));
		if(doc == null) {
			XModelObjectLoaderUtil.addRequiredChildren(object);
			return;
		}
		Element element = doc.getDocumentElement();
		util.load(element, object);
		
		setEncoding(object, body);
		NodeList nl = doc.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if(n instanceof DocumentType) {
				DocumentType dt = (DocumentType)n;
				object.setAttributeValue("systemId", dt.getSystemId()); //$NON-NLS-1$
			}
		}
		String loadingError = util.getError();
		reloadProcess(object);

		object.set("actualBodyTimeStamp", "" + object.getTimeStamp()); //$NON-NLS-1$ //$NON-NLS-2$
		((AbstractXMLFileImpl)object).setLoaderError(loadingError);
		if(!hasErrors && loadingError != null) {
			object.setAttributeValue("isIncorrect", "yes"); //$NON-NLS-1$ //$NON-NLS-2$
			object.setAttributeValue("incorrectBody", body); //$NON-NLS-1$
			object.set("actualBodyTimeStamp", "" + object.getTimeStamp()); //$NON-NLS-1$ //$NON-NLS-2$
		}
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
    
	static boolean DO_NOT_SAVE = true;

	public boolean saveLayout(XModelObject object) {
		if(DO_NOT_SAVE) return true;
		XModelObjectLoaderUtil util = new XModelObjectLoaderUtil();
		try {
			XModelObject process = object.getChildByPath(ELM_PROCESS);
			if(process == null) return true;
			process.setModified(true);
			Element element = XMLUtil.createDocumentElement("PROCESS"); //$NON-NLS-1$
			util.saveAttributes(element, process);
			util.saveChildren(element, process);
			StringWriter sw = new StringWriter();
			XModelObjectLoaderUtil.serialize(element, sw);
			XModelObjectLoaderUtil.setTempBody(process, sw.toString());
			aux.write(object.getParent(), object, process);
			return true;
		} catch (IOException exc) {
			ModelPlugin.getPluginLog().logError(exc);
			return false;
		}
	}    

	public String serializeMainObject(XModelObject object) {
//		String entity = object.getModelEntity().getName();
		String systemId = object.getAttributeValue("systemId"); //$NON-NLS-1$
		if(systemId == null || systemId.length() == 0) systemId = DOC_EXTDTD;
		String publicId = DOC_PUBLICID;
		Element element = XMLUtil.createDocumentElement(object.getModelEntity().getXMLSubPath(), DOC_QUALIFIEDNAME, publicId, systemId, null);
		String result = null;
		try {
			util.setup(null, false);
			String att = object.getAttributeValue("comment"); //$NON-NLS-1$
			if (att.length() > 0) util.saveAttribute(element, "#comment", att); //$NON-NLS-1$
			util.saveChildren(element, object);
			result = SimpleWebFileLoader.serialize(element, object);
		} catch (IOException e) {
			ModelPlugin.getPluginLog().logError(e);
		} catch (XModelException e) {
			ModelPlugin.getPluginLog().logError(e);
		}
		return result;
	}

}

class FTLoaderUtil extends XModelObjectLoaderUtil {
    static String tilesRequired = "!TilesDefinition.name!TilesPut.name!TilesList.name!TilesBean.classtype!TilesSetProperty.property!TilesSetProperty.value!TilesItem.value!TilesItem.link!"; //$NON-NLS-1$
    static String SAVE_CONTENT_ATTR = "save value as 'content' attr"; //$NON-NLS-1$

    protected boolean isSaveable(XModelEntity entity, String n, String v, String dv) {
        if(v == null) return false;
        if(v.length() == 0)
          return (tilesRequired.indexOf("!" + entity.getName() + "." + n + "!") >= 0); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        return super.isSaveable(entity, n, v, dv);
    }

    public String getAttribute(Element element, String xmlname, XAttribute attr) {
        int i = xmlname.indexOf('|');
        if(i < 0 || xmlname.startsWith("content|")) return super.getAttribute(element, xmlname, attr); //$NON-NLS-1$
        String v = super.getAttribute(element, xmlname.substring(0, i), attr);
        return (v != null && v.length() > 0) ? v :
               super.getAttribute(element, xmlname.substring(i + 1), attr);
    }

    public void saveAttribute(Element element, String xmlname, String value) {
        int i = xmlname.indexOf('|');
        if(i >= 0) xmlname = (value.indexOf('\n') >= 0) ? xmlname.substring(i + 1)
                             : xmlname.substring(0, i);
        super.saveAttribute(element, xmlname, value);
    }

}
