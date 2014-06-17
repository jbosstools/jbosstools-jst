/*******************************************************************************
 * Copyright (c) 2007-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.internal.css.view;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class CSSPreviewPageCreator {
	/**
	 * Generates preview HTML page for given CSS {@code style}
	 * and text {@code content}
	 */
	@SuppressWarnings("nls")
	public static String createPreviewHtml(String style, String content) {
		return
			"<html>"
			+    "<head>"
			+        "<style>"
			+            "div {"
			+                style
			+            "}"
			+         "</style>"
			+     "</head>"
			+     "<body style='margin:0; padding:0px;'>"
			+         "<div>" 
			+             content
			+         "</div>"
			+      "</body>"
			+ "</html>"; 
	}
}
