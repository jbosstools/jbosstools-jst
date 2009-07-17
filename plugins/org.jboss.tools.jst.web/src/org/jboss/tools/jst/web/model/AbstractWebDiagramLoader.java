/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.model;

import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.XModelObjectConstants;
import org.jboss.tools.common.model.filesystems.FileAuxiliary;
import org.jboss.tools.common.model.filesystems.impl.AbstractExtendedXMLFileImpl;
import org.jboss.tools.common.model.filesystems.impl.FileAnyImpl;
import org.jboss.tools.common.model.filesystems.impl.FolderLoader;
import org.jboss.tools.common.model.loaders.AuxiliaryLoader;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.w3c.dom.Element;

/**
 * @author Viacheslav Kabanovich
 */
public abstract class AbstractWebDiagramLoader implements WebProcessLoader, AuxiliaryLoader {
	protected FileAuxiliary aux = createFileAuxiliary();
	protected XModelObjectLoaderUtil util = createUtil();

	public AbstractWebDiagramLoader() {
		
	}

	abstract protected FileAuxiliary createFileAuxiliary();
	
	abstract protected XModelObjectLoaderUtil createUtil();

	abstract public String serializeMainObject(XModelObject object);

	protected void setEncoding(XModelObject object, String body) {
		String encoding = XModelObjectLoaderUtil.getEncoding(body);
		if(encoding == null) encoding = ""; //$NON-NLS-1$
		object.setAttributeValue(XModelObjectConstants.ATTR_NAME_ENCODING, encoding);
	}
    
    public boolean update(XModelObject object) throws XModelException {
        XModelObject p = object.getParent();
        if (p == null) return true;
        FolderLoader fl = (FolderLoader)p;
		String body = fl.getBodySource(FileAnyImpl.toFileName(object)).get();
//		String encoding = XModelObjectLoaderUtil.getEncoding(body);
//		body = FileUtil.encode(body, encoding);
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
		String text = file.getAsText();
		XModelObjectLoaderUtil.setTempBody(object, text);
		if("yes".equals(object.get("isIncorrect"))) { //$NON-NLS-1$ //$NON-NLS-2$
			return true;
		}
		return saveLayout(object);
	}

	abstract protected boolean saveLayout(XModelObject object);

	public void bind(XModelObject main) {
		aux.getAuxiliaryFile(main.getParent(), main, false);
	}

	public String mainObjectToString(XModelObject object) {
		return "" + serializeMainObject(object); //$NON-NLS-1$
	}

	public String serializeObject(XModelObject object) {
		return serializeMainObject(object);
	}

	public void loadFragment(XModelObject object, Element element) {
		util.load(element, object);		
	}

}
