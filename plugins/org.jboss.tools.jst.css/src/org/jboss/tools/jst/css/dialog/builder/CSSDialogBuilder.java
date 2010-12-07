/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.css.dialog.builder;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.jst.css.dialog.CSSClassDialog;
import org.jboss.tools.jst.css.dialog.CSSStyleDialog;
import org.jboss.tools.jst.css.dialog.selector.CSSStyleClassSelector;
import org.jboss.tools.jst.jsp.outline.css.CSSDialogBuilderInterface;
import org.jboss.tools.jst.jsp.outline.css.CSSStyleClassSelectorInterface;
import org.jboss.tools.jst.jsp.outline.css.CSSStyleDialogDecorator;
import org.jboss.tools.jst.jsp.outline.css.CSSStyleDialogInterface;
import org.jboss.tools.jst.jsp.util.Constants;

/**
 * Builder for building CSSStyleDialog
 * 
 * @author mareshkau
 *
 */
public class CSSDialogBuilder implements CSSDialogBuilderInterface {

	@Override
	public CSSStyleDialogInterface buildCSSStyleDialog(Shell shell, String style) {
		return new CSSStyleDialog(shell,style);
	}
	@Override
	public CSSStyleClassSelectorInterface buildCSSClassDialog(Shell parentShell) {
		return new CSSStyleClassSelector(parentShell);
	}

}
