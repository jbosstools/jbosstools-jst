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
package org.jboss.tools.jst.css.dialog.parsers;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.jboss.tools.jst.css.CSSPlugin;
import org.xml.sax.SAXException;

/**
 * 
 * @author ezheleznyakov
 * 
 */
public class CSSParser {

	private BaseListener listener;

	private SAXParser saxParser;

	public CSSParser() {

		try {
			saxParser = SAXParserFactory.newInstance().newSAXParser();

		} catch (ParserConfigurationException e) {
			CSSPlugin.getDefault().logError(e);
		} catch (SAXException e) {
			CSSPlugin.getDefault().logError(e);
		}
	}

	public void parse(String file) {
		try {
			InputStream is = CSSPlugin.getDefault().getBundle()
					.getResource(file).openStream();
			saxParser.parse(is, listener);
		} catch (SAXException e) {
			CSSPlugin.getDefault().logError(e);
		} catch (IOException e) {
			CSSPlugin.getDefault().logError(e);
		}
	}

	public void setListener(BaseListener listener) {
		this.listener = listener;
	}
}
