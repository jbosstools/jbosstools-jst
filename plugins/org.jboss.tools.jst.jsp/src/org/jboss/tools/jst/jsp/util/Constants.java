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
package org.jboss.tools.jst.jsp.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.graphics.RGB;

/**
 * 
 * Class for constants
 * 
 * @author Evgeny Zheleznyakov
 * 
 */
public class Constants {

	public static final String extSizes[] = new String[] { "", "em", "ex", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"px", "in", "cm", "mm", "pt", "pc" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

	public static final Set elemFolder = Collections
			.unmodifiableSet(new HashSet(
					Arrays
							.asList(new String[] {
									"background-image", "list-style-image", "cursor", "cue-after", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
									"cue-before" }))); //$NON-NLS-1$

	public static final Set extElem = Collections
			.unmodifiableSet(new HashSet(
					Arrays
							.asList(new String[] {
									"border-bottom-width", "border-left-width", "borer-right-width", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
									"border-top-width", "border-width", "bottom", "font-size", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
									"height", "left", "letter-spacing", "line-height", "margin", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
									"margin-bottom", "margin-left", "margin-right", "margin-top", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
									"margin-offset", "margin-bottom", "max-height", "max-width", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
									"min-height", "min-width", "outline-width", "padding", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
									"padding-bottom", "padding-left", "padding-right", "padding-top", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
									"right", "size", "text-indent", "top", "vertical-align", "width", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
									"word-spacing" }))); //$NON-NLS-1$

	public static final String NONE = "none"; //$NON-NLS-1$	
	public static final int FIRST_COLUMN = 0;
	public static final int SECOND_COLUMN = 1;

	public static final String EMPTY = ""; //$NON-NLS-1$
	public static final String WHITE_SPACE = " "; //$NON-NLS-1$
	public static final String COLON = ":"; //$NON-NLS-1$
	public static final String SEMICOLON = ";"; //$NON-NLS-1$
	public static final String COMMA = ","; //$NON-NLS-1$
	public static final String DOT = "."; //$NON-NLS-1$
	public static final String DASH = "-"; //$NON-NLS-1$
	public static final String SLASH = "/"; //$NON-NLS-1$
	public static final String EQUAL = "="; //$NON-NLS-1$
	public static final String UNDERSCORE = "_"; //$NON-NLS-1$
	public static final String START_BRACKET = "("; //$NON-NLS-1$
	public static final String END_BRACKET = ")"; //$NON-NLS-1$
	public static final String GT = ">"; //$NON-NLS-1$
	public static final String LT = "<"; //$NON-NLS-1$
	public static final String PROPERTIES_EXTENTION = ".properties"; //$NON-NLS-1$

	public static final String LEFT_BRACE = "{"; //$NON-NLS-1$
	public static final String RIGHT_BRACE = "}"; //$NON-NLS-1$

	public static final RGB RGB_BLACK = new RGB(0, 0, 0);
	public static final RGB RGB_WHITE = new RGB(0xFF, 0xFF, 0xFF);

	public static final int DONT_CONTAIN = -1;

	public static final String OPEN_SPAN_TAG = "<span style=\"width: 100%;"; //$NON-NLS-1$
	public static final String CLOSE_SPAN_TAG = "</span>"; //$NON-NLS-1$
	public static final String OPEN_DIV_TAG = "<div style=\"width: 100%;"; //$NON-NLS-1$
	public static final String CLOSE_DIV_TAG = "</div>"; //$NON-NLS-1$
	public static final String COLOR = "color"; //$NON-NLS-1$
	public static final String STYLE = "style"; //$NON-NLS-1$
	public static final String CLASS = "class"; //$NON-NLS-1$
}