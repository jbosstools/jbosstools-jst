/*
 *  Copyright 1999-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jboss.tools.jst.web.debug.internal.xpl;

import java.util.ArrayList;
import java.util.List;
import org.jboss.tools.jst.web.debug.internal.IClassNameBuilder;

public class TomcatClassNameBuilder implements IClassNameBuilder {
	/**
	 * The default package name for compiled jsp pages.
	 */
	private static final String JSP_PACKAGE_NAME = "org.apache.jsp";

	private final String javaKeywords[] = {
		"abstract", "boolean", "break", "byte", "case",
		"catch", "char", "class", "const", "continue",
		"default", "do", "double", "else", "extends",
		"final", "finally", "float", "for", "goto",
		"if", "implements", "import", "instanceof", "int",
		"interface", "long", "native", "new", "package",
		"private", "protected", "public", "return", "short",
		"static", "strictfp", "super", "switch", "synchronized",
		"this", "throws", "transient", "try", "void",
		"volatile", "while" };

	public TomcatClassNameBuilder()	{
	}

	public String getJavaClassName(String jspUri) {

		String javaClassName = null; 
		int iSep = jspUri.lastIndexOf('/');
		String packageName = (iSep > 0) ? makeJavaPackage(jspUri.substring(1,iSep)) : "";
		if (packageName.length() == 0) {
			packageName = JSP_PACKAGE_NAME;
		} else {
			packageName = JSP_PACKAGE_NAME + "." + packageName;
		}
		String className = makeJavaIdentifier(jspUri.substring(iSep + 1));

		javaClassName = packageName + "." + className;

		return javaClassName;
	}

	/**
	* Converts the given identifier to a legal Java identifier
	*
	* @param identifier Identifier to convert
	*
	* @return Legal Java identifier corresponding to the given identifier
	*/
	private String makeJavaIdentifier(String identifier) {
		StringBuffer modifiedIdentifier = new StringBuffer(identifier.length());
		if (!Character.isJavaIdentifierStart(identifier.charAt(0))) { 
			modifiedIdentifier.append('_');
		}

		for (int i = 0; i < identifier.length(); i++) {
			char ch = identifier.charAt(i);
			if (Character.isJavaIdentifierPart(ch) && ch != '_') { 
				modifiedIdentifier.append(ch);
			} else if (ch == '.') { 
				modifiedIdentifier.append('_');
			} else { 
				modifiedIdentifier.append(mangleChar(ch));
			}
		}
		if (isJavaKeyword(modifiedIdentifier.toString())) { 
			modifiedIdentifier.append('_');
		}

		return modifiedIdentifier.toString();
	}

	/**
	 * Converts the given path to a Java package or fully-qualified class name
	 *
	 * @param path Path to convert
	 *
	 * @return Java package corresponding to the given path
	 */
	private String makeJavaPackage(String path) {
		String classNameComponents[] = split(path, "/");
		StringBuffer legalClassNames = new StringBuffer();
		for (int i = 0; i < classNameComponents.length; i++) {
			legalClassNames.append(makeJavaIdentifier(classNameComponents[i]));
			if (i < classNameComponents.length - 1) {
				legalClassNames.append('.');
			}
		}
		return legalClassNames.toString();
	}

	/**
	 * Test whether the argument is a Java keyword
	 */
	private boolean isJavaKeyword(String key) {
		int i = 0;
		int j = javaKeywords.length;
		while (i < j) {
			int k = (i+j)/2;
			int result = javaKeywords[k].compareTo(key);
			if (result == 0) {
				return true;
			}
			if (result < 0) {
				i = k+1;
			} else {
				j = k;
			}
		}
		return false;
	}

	/**
	 * Mangle the specified character to create a legal Java class name.
	 */
	private String mangleChar(char ch) {
		char[] result = new char[5];
		result[0] = '_';
		result[1] = Character.forDigit((ch >> 12) & 0xf, 16);
		result[2] = Character.forDigit((ch >> 8) & 0xf, 16);
		result[3] = Character.forDigit((ch >> 4) & 0xf, 16);
		result[4] = Character.forDigit(ch & 0xf, 16);
		return new String(result);
	}

	/**
	 * Splits a string into it's components.
	 * @param path String to split
	 * @param pat Pattern to split at
	 * @return the components of the path
	 */
	private String [] split(String path, String pat) {
		List<String> comps = new ArrayList<String>();
		int pos = path.indexOf(pat);
		int start = 0;
		while( pos >= 0 ) {
			if(pos > start) {
				String comp = path.substring(start,pos);
				comps.add(comp);
			}
			start = pos + pat.length();
			pos = path.indexOf(pat,start);
		}
		if( start < path.length()) { 
			comps.add(path.substring(start));
		}

		String [] result = new String[comps.size()];
		for(int i=0; i < comps.size(); i++) { 
			result[i] = comps.get(i);
		}

		return result;
	}

}

