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
package org.jboss.tools.jst.jsp.outline.cssdialog.common;

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

    public static final Set elemFolder = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[] {
	    "background-image", "list-style-image", "cursor", "cue-after", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	    "cue-before" }))); //$NON-NLS-1$

    public static final Set extElem = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[] {
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

    public static final String IMAGE_COLOR_FILE_LOCATION = "images/cssdialog/color.gif"; //$NON-NLS-1$
    public static final String IMAGE_FOLDER_FILE_LOCATION = "images/cssdialog/folder.gif"; //$NON-NLS-1$
    public static final String IMAGE_FONT_FILE_LOCATION = "images/cssdialog/font.gif"; //$NON-NLS-1$
    public static final String IMAGE_COLORLARGE_FILE_LOCATION = "images/cssdialog/color_large.gif"; //$NON-NLS-1$
    public static final String IMAGE_FOLDERLARGE_FILE_LOCATION = "images/cssdialog/folder_large.gif"; //$NON-NLS-1$
    public static final String IMAGE_FONTLARGE_FILE_LOCATION = "images/cssdialog/font_large.gif"; //$NON-NLS-1$
    public static final String IMAGE_LEFT_FILE_LOCATION = "images/cssdialog/left.gif"; //$NON-NLS-1$
    public static final String IMAGE_RIGHT_FILE_LOCATION = "images/cssdialog/right.gif"; //$NON-NLS-1$
    public static final String IMAGE_SAMPLE_FILE_LOCATION = "images/cssdialog/sample.gif"; //$NON-NLS-1$
    
    public static final int FIRST_COLUMN = 0;
    public static final int SECOND_COLUMN = 1;

    public static final String EMPTY = ""; //$NON-NLS-1$
	public static final String WHITE_SPACE = " "; //$NON-NLS-1$
    public static String COLON = ":"; //$NON-NLS-1$
    public static String SEMICOLON = ";"; //$NON-NLS-1$
    public static String COMMA = ","; //$NON-NLS-1$
    public static String DASH = "-"; //$NON-NLS-1$
    public static String SLASH = "/"; //$NON-NLS-1$
	public static final String START_BRACKET = "("; //$NON-NLS-1$
	public static final String END_BRACKET = ")"; //$NON-NLS-1$


    public static final RGB RGB_BLACK = new RGB(0,0,0);
    public static final RGB RGB_WHITE = new RGB(0xFF, 0xFF, 0xFF);

    public static final int DONT_CONTAIN = -1;
    
    public static String OPEN_SPAN_TAG = "<span style=\"width: 100%;"; //$NON-NLS-1$
    public static String CLOSE_SPAN_TAG = "</span>"; //$NON-NLS-1$
    public static String OPEN_DIV_TAG = "<div style=\"width: 100%;"; //$NON-NLS-1$
    public static String CLOSE_DIV_TAG = "</div>"; //$NON-NLS-1$
}