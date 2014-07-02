/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.common.xml.XMLEntityResolver;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.jst.angularjs.AngularJsPlugin;
import org.w3c.dom.Element;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class IonicIconFactory {
	
	static IonicIconFactory instance = new IonicIconFactory();
	
	public static IonicIconFactory getInstance() {
		return instance;
	}

	private List<String> icons = null;

	private IonicIconFactory() {}

	public List<String> getIcons() {
		if(icons == null) {
			try {
				load();
			} catch (IOException e) {
				onLoadFail(e);
			} catch (URISyntaxException e) {
				onLoadFail(e);
			}
		}
		return icons;
	}

	private void onLoadFail(Exception e) {
		 AngularJsPlugin.getDefault().logError(e);
		 icons = new ArrayList<String>();
	}

	private void load() throws IOException, URISyntaxException {
		File root = AngularJsPlugin.getJSStateRoot();
		URI uri = null;
		if(root.isDirectory()) {
			uri = new File(root, "js/fonts/ionicons.svg").toURI();
		} else if(root.isFile()) {
			uri = new URI("jar:" + root.toURI().toString() + "!/" + "js/fonts/ionicons.svg");
		}
		InputStream is = uri.toURL().openStream();
		Element e = XMLUtilities.getElement(is, XMLEntityResolver.getInstance());
		Element defs = XMLUtilities.getUniqueChild(e, "defs");
		Element font = XMLUtilities.getUniqueChild(defs, "font");
		Element[] glyphs = XMLUtilities.getChildren(font, "glyph");
		List<String> icons = new ArrayList<String>();
		for (Element g: glyphs) {
			icons.add(g.getAttribute("glyph-name"));
		}
		this.icons = icons;
	}
}