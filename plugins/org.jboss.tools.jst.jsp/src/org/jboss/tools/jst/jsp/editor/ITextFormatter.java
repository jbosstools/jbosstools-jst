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
package org.jboss.tools.jst.jsp.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;

public interface ITextFormatter {
//	void format(IDocument doc, IRegion region);
//	public void reformatText(Object editor, IDocument document, int start, int end);
	public void formatTextRegion(IDocument document, IRegion region);

}
