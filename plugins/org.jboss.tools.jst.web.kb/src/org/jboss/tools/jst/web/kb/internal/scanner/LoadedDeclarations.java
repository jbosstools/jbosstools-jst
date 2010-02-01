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
package org.jboss.tools.jst.web.kb.internal.scanner;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;

/**
 * This object keeps all declarations loaded from one source.
 * 
 * @author Viacheslav Kabanovich
 */
public class LoadedDeclarations {
	List<ITagLibrary> libraries = new ArrayList<ITagLibrary>();
	
	public List<ITagLibrary> getLibraries() {
		return libraries;
	}
	
	public void add(LoadedDeclarations ds) {
		if(ds == null) return;
		libraries.addAll(ds.libraries);
	}

	public boolean isEmpty() {
		return libraries.isEmpty();
	}

}
