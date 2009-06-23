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
package org.jboss.tools.jst.web.kb.internal;

import org.jboss.tools.jst.web.kb.IResourceBundle;

public class ResourceBundle implements IResourceBundle {
	String basename;
	String var;

	public ResourceBundle(String basename, String var) {
		this.basename = basename;
		this.var = var;
	}

	public String getBasename() {
		return basename;
	}

	public String getVar() {
		return var;
	}

}
