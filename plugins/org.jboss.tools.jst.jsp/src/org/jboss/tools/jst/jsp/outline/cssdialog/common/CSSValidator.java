/*******************************************************************************
 * Copyright (c) 2007-2009 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.outline.cssdialog.common;

import org.eclipse.wst.css.core.internal.provisional.document.ICSSModel;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.w3c.dom.css.CSSStyleSheet;

/**
 * CSS Validator
 * 
 * @author yradtsevich
 * 
 */
public abstract class CSSValidator {
	// FIXME: this hard-coded string have to be replaced by reference
	private static final String CSS_CONTENT_TYPE_IDENTIFIER = "org.eclipse.wst.css.core.csssource"; //$NON-NLS-1$
	private final CSSStyleSheet validatingCSS;
	private final IStructuredDocument validatingDocument;

	public CSSValidator() {
		IModelManager modelManager = StructuredModelManager.getModelManager();
		ICSSModel validatingModel = (ICSSModel) modelManager
				.createUnManagedStructuredModelFor(CSS_CONTENT_TYPE_IDENTIFIER);
		validatingDocument = validatingModel.getStructuredDocument();
		validatingCSS = (CSSStyleSheet) validatingModel.getDocument();
	}

	protected void cleanValidatingDocument() {
		validatingDocument.set(""); //$NON-NLS-1$
	}

	protected CSSStyleSheet getValidatingCSS() {
		return validatingCSS;
	}

	protected IStructuredDocument getValidatingDocument() {
		return validatingDocument;
	}

}
