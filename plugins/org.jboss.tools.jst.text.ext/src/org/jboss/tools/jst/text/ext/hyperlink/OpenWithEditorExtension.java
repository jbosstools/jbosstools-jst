/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.text.ext.hyperlink;

import org.eclipse.osgi.service.runnable.ParameterizedRunnable;

public class OpenWithEditorExtension implements Comparable<OpenWithEditorExtension>{
	private String id;
	private String editorName;
	private ParameterizedRunnable editorLauncher;
	
	public OpenWithEditorExtension(String id, String editorName, ParameterizedRunnable editorLauncher){
		this.id = id;
		this.editorName = editorName;
		this.editorLauncher = editorLauncher;
	}

	public String getId(){
		return id;
	}
	
	public String getEditorName(){
		return editorName;
	}
	
	public ParameterizedRunnable getEditorLauncher(){
		return editorLauncher;
	}

	@Override
	public int compareTo(OpenWithEditorExtension o) {
		return getEditorName().compareTo(o.getEditorName());
	}
}
