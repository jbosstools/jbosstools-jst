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
package org.jboss.tools.jst.web.project;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.IDocument;
import org.jboss.tools.common.kb.wtp.TLDVersionHelper;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.FileSystemsHelper;
import org.jboss.tools.common.model.filesystems.impl.FileAnyImpl;
import org.jboss.tools.common.model.filesystems.impl.FileSystemImpl;
import org.jboss.tools.jst.web.tld.IWebProject;
import org.jboss.tools.jst.web.tld.TaglibMapping;

public class WebProject implements IWebProject {

	public static WebProject getInstance(XModel model) {
		WebProject instance = (WebProject)model.getManager("WebProject"); //$NON-NLS-1$
		if(instance == null) {
			instance = new WebProject();
			instance.model = model;
            instance.taglibs = new TaglibMapping(model);
			model.addManager("WebProject", instance); //$NON-NLS-1$
		}
		instance.update();
		return instance;
	}

	private XModel model;
	private TaglibMapping taglibs;
    private String webRoot;
	public static final String STRUTS_NATURE_ID = "org.jboss.tools.struts.strutsnature"; //$NON-NLS-1$
	public static final String JSF_NATURE_ID = "org.jboss.tools.jsf.jsfnature"; //$NON-NLS-1$
	
	public XModel getModel() {
		return model;
	}

	public String getWebRootLocation() {
        if (this.webRoot != null) return this.webRoot;
		XModelObject fs = FileSystemsHelper.getWebRoot(model);
		if (fs == null)	{
			XModelObject m = model.getByPath("Web"); //$NON-NLS-1$
			fs = (m == null) ? null : FileSystemsHelper.getFileSystem(model, m.getAttributeValue(WebModuleConstants.ATTR_ROOT_FS));
		} 
		return (!(fs instanceof FileSystemImpl)) ? null : ((FileSystemImpl)fs).getAbsoluteLocation();
	}

    public String getWebInfLocation() {
        XModelObject fs = FileSystemsHelper.getWebInf(model);
        return (fs == null || !(fs instanceof FileSystemImpl)) ?
            getWebRootLocation() + "/WEB-INF" : //$NON-NLS-1$
            ((FileSystemImpl)fs).getAbsoluteLocation();
    }
    
    /** 
     * 
     * @param uri resource path - "/pages/index.jsp", "/WEB-INF/c.tld" etc.
     * @param base base uri for resolving relative uris
     * @return absolute path or url
     */
    public String getAbsoluteLocation(String uri, String base) {
        if (uri.startsWith("/")) { //$NON-NLS-1$
            if (uri.startsWith("/WEB-INF/")) { //$NON-NLS-1$
                return getWebInfLocation().replace('\\', '/') + uri.substring(8);
            }
            return getWebRootLocation().replace('\\', '/') + uri;
        }
        if (uri.indexOf(":/") > 1) { //$NON-NLS-1$
            return uri;
        }
        int ind = -1;
        if (base != null) {
            base = base.replace('\\', '/');
            ind = base.lastIndexOf('/');
        }
        String location = ind >= 0 ? base.substring(0, ind + 1) + uri : uri;
        if (!new java.io.File(location).isAbsolute()) {
            location = getWebRootLocation() + "/" + location; //$NON-NLS-1$
        }
        return location.replace('\\', '/');
    }
	
	public TaglibMapping getTaglibMapping() {
		
		return taglibs;
	}

	public void update() {
	}
    
    public void setWebRootLocation(String webRoot) {
        this.webRoot = webRoot;
    }
    
    public String getPathInWebRoot(XModelObject file) {
    	String webRoot = getWebRootLocation().replace('\\', '/');
    	if(!webRoot.endsWith("/")) webRoot += "/"; //$NON-NLS-1$ //$NON-NLS-2$
    	if(file == null || !file.isActive()) return null;
    	String path = null;
    	if(!(file instanceof FileAnyImpl)) {
    		IResource r = (IResource)file.getAdapter(IResource.class);
    		if(r == null || r.getLocation() == null) return null;
    		path = r.getLocation().toString().replace('\\', '/') + "/"; //$NON-NLS-1$
    	} else {
    		path = ((FileAnyImpl)file).getAbsolutePath();
    	}
        if(path == null) return null;
        if(!path.toLowerCase().startsWith(webRoot.toLowerCase())) return null;
        return path.substring(webRoot.length() - 1);
    }

	public static String getTldVersion(String uri, String prefix, IDocument document, XModel xm) {
		String version = TLDVersionHelper.getTldVersion(uri, prefix, document);
		if(version == null && xm!=null) {
			XModelObject xmo = WebProject.getInstance(xm).getTaglibMapping().getTaglibObject(uri);
			if(xmo!=null) {
				version = xmo.getAttributeValue("tlibversion"); //$NON-NLS-1$
			}
		}
		return version;
	}
}