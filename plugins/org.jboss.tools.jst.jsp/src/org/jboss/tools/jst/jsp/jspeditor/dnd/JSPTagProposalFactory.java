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
package org.jboss.tools.jst.jsp.jspeditor.dnd;

import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.common.model.ui.editors.dnd.DropCommandFactory;
import org.jboss.tools.common.model.ui.editors.dnd.ITagProposalFactory;
import org.jboss.tools.common.model.ui.editors.dnd.ITagProposalLoader;

public class JSPTagProposalFactory implements ITagProposalFactory {
	private static final JSPTagProposalFactory INSTANCE = new JSPTagProposalFactory(); 
	public static Map<String,String> loaderMap = new HashMap<String,String>(); 

	static {
		loaderMap.put(DropCommandFactory.kFileMime, FileTagProposalLoader.class.getName());
		loaderMap.put(DropCommandFactory.kURLMime, FileTagProposalLoader.class.getName());
		new FileTagProposalLoader();
	}
	
	public static JSPTagProposalFactory getInstance() {
		return INSTANCE;
	}
	
    private JSPTagProposalFactory() {
	}
    
    public ITagProposalLoader getProposalLoader(String mimeType) {
    	ITagProposalLoader fInstance =  DEFAULT_PROPOSAL_LOADER;
		try {
			String fClassName = (String)loaderMap.get(mimeType);
			if(fClassName == null) {
				//No need to report, just there is no specific proposal loader for this myme type.
				return fInstance;
			}
			Class newClass = this.getClass().getClassLoader().loadClass(fClassName);
			fInstance = (ITagProposalLoader)newClass.newInstance();
		} catch (InstantiationException e) {
			ModelUIPlugin.getPluginLog().logError(e);
		} catch (IllegalAccessException e) {
			ModelUIPlugin.getPluginLog().logError(e);
		} catch (ClassNotFoundException e) {
			ModelUIPlugin.getPluginLog().logError(e);
		}
		return fInstance;
    }
}

