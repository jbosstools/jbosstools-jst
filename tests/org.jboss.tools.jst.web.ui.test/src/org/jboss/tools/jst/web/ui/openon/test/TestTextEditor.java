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
package org.jboss.tools.jst.web.ui.openon.test;

import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

public class TestTextEditor extends StructuredTextEditor {

	public TestTextEditor() {
		super();
	}

	public ISourceViewer getTextSourceViewer(){
		return getSourceViewer();
	}
}
