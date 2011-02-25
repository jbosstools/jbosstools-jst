/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.jsp.jspeditor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.eclipse.core.internal.content.TextContentDescriber;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.ITextContentDescriber;

/**
 * Content decriber class that helps Eclipse to detect if the *.inc file has jsp content inside.
 * 
 * @author Victor Rubezhny
 *
 */
@SuppressWarnings("restriction")
public class JSPIncludeContentDescriber extends TextContentDescriber implements ITextContentDescriber {

	public int describe(InputStream input, IContentDescription description) throws IOException {
		InputStreamReader reader = new InputStreamReader(input);
		try {
			return (containsJspElements(reader) ? 
					VALID :
					INVALID);
		} finally {
			reader.close();
		}
	}

	public int describe(Reader contents, IContentDescription description) throws IOException {
		return (containsJspElements(contents) ? 
			VALID :
			INVALID);
	}
	
	private boolean containsJspElements(Reader contents) {
		try {
			int c = contents.read();
			while (c != -1) {
				if (c == '<') { // JSP Code/Tag/Directive Opener check
					if ((c = contents.read()) == -1)
						return false;
					if (c == '%') { // JSP Code/Directive opened
						return true;
					}
					if (c == 'j') { // JSP Tag opener check
						if ((c = contents.read()) == -1)
							return false;
						if (c != 's') 
							continue;
						if ((c = contents.read()) == -1)
							return false;
						if (c != 'p') 
							continue;
						if ((c = contents.read()) == -1)
							return false;
						if (c != ':') 
							continue;
						
						// Find the tag close
						while ((c = contents.read()) != -1) {
							if (c == '>') {
								return true;
							}
						}
						continue;
					}
				}
				c = contents.read();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			// Ignore 
		}
		return false;
	}
}
