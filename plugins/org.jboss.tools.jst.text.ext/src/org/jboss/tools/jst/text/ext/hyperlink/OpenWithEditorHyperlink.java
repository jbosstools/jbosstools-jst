/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.text.ext.hyperlink;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.osgi.util.NLS;
import org.jboss.tools.common.text.ext.hyperlink.AbstractHyperlink;
import org.jboss.tools.jst.text.ext.JSTExtensionsPlugin;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;

public class OpenWithEditorHyperlink extends AbstractHyperlink{
	private String href;
	private String shortName;
	private OpenWithEditorExtension extension;

	public OpenWithEditorHyperlink(IDocument document, IRegion region, String shortName, String href, OpenWithEditorExtension extension){
		this.href = href;
		this.shortName = shortName;
		this.extension = extension;
		setRegion(region);
		setDocument(document);
	}
	
	@Override
	protected void doHyperlink(IRegion region) {
		try {
			extension.getEditorLauncher().run(href);
		} catch (Exception e) {
			JSTExtensionsPlugin.getDefault().logError(e);
		}
	}

	@Override
	public String getHyperlinkText() {
		return NLS.bind(WebUIMessages.OpenWithBrowser, shortName, extension.getEditorName());
	}

}
