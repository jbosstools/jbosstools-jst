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
package org.jboss.tools.jst.web.ui.wizards.project;

import java.io.File;
import java.util.*;
import org.jboss.tools.common.model.ui.navigator.NavigatorLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.loaders.EntityRecognizerContext;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;

public class ResourceLabelProvider extends LabelProvider {
	static XModel model;
	static XModelObject folder;
	static NavigatorLabelProvider labelProvider;
	static Map images; 
	
	static void init() {
		if(model != null) return;
		model = PreferenceModelUtilities.getPreferenceModel();
		folder = model.createModelObject("FileFolder", null); //$NON-NLS-1$
		labelProvider = new NavigatorLabelProvider();
		images = new HashMap();
		
	}
	
	public ResourceLabelProvider() {
		init();
	}
	

    public String getText(Object element) {
    	if(element instanceof File) {
    		return ((File)element).getName();
    	} else {
    		return super.getText(element);
    	}
    }

    public Image getImage(Object element) {
    	if(element instanceof File) {
    		File f = (File)element;
    		if(f.isDirectory()) {
    			return labelProvider.getImage(folder);
    		} else {
    	        String entity = getEntity(f.getName());
    	        return getImageInternal(entity);
    		}
    	} else {
            return null;
    	}
    }
    
    Image getImageInternal(String entity) {
    	Image image = (Image)images.get(entity);
    	if(image == null) {
    		XModelObject o = model.createModelObject(entity, null);
    		if(o == null) o = model.createModelObject("FileTXT", null); //$NON-NLS-1$
    		if(o == null) o = folder;
    		image = labelProvider.getImage(o);
    		if(image != null) images.put(entity, image);
    	}
    	return image;
    }
    
    String getEntity(String filename) {
		int i = filename.lastIndexOf('.');
		String ext = i < 0 ? "" : filename.substring(i + 1); //$NON-NLS-1$
		if(ext.equals("xml")) { //$NON-NLS-1$
			if(filename.equals("web.xml")) return "FileWebApp"; //$NON-NLS-1$ //$NON-NLS-2$
			if(filename.startsWith("faces-")) return "FacesConfig11"; //$NON-NLS-1$ //$NON-NLS-2$
			if(filename.startsWith("struts-")) return "StrutsConfig11"; //$NON-NLS-1$ //$NON-NLS-2$
			return "FileXML"; //$NON-NLS-1$
		}
        String entity = model.getEntityRecognizer().getEntityName(new EntityRecognizerContext(filename, ext, null));
        if(entity == null) entity = "FileTXT"; //$NON-NLS-1$
        return entity;
    }

}
