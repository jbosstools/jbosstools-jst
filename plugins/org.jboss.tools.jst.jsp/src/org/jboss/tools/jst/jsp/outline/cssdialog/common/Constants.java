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

    public static final String extSizes[] = new String[] { "", "em", "ex",
	    "px", "in", "cm", "mm", "pt", "pc" };

    public static final Set elemFolder = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[] {
	    "background-image", "list-style-image", "cursor", "cue-after",
	    "cue-before" })));

    public static final Set extElem = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[] {
	    "border-bottom-width", "border-left-width", "borer-right-width",
	    "border-top-width", "border-width", "bottom", "font-size",
	    "height", "left", "letter-spacing", "line-height", "margin",
	    "margin-bottom", "margin-left", "margin-right", "margin-top",
	    "margin-offset", "margin-bottom", "max-height", "max-width",
	    "min-height", "min-width", "outline-width", "padding",
	    "padding-bottom", "padding-left", "padding-right", "padding-top",
	    "right", "size", "text-indent", "top", "vertical-align", "width",
	    "word-spacing" })));

    public static final String NONE = "none";

    public static final String IMAGE_COLOR_FILE_LOCATION = "images/cssdialog/color.gif";
    public static final String IMAGE_FOLDER_FILE_LOCATION = "images/cssdialog/folder.gif";
    public static final String IMAGE_FONT_FILE_LOCATION = "images/cssdialog/font.gif";
    public static final String IMAGE_COLORLARGE_FILE_LOCATION = "images/cssdialog/color_large.gif";
    public static final String IMAGE_FOLDERLARGE_FILE_LOCATION = "images/cssdialog/folder_large.gif";
    public static final String IMAGE_FONTLARGE_FILE_LOCATION = "images/cssdialog/font_large.gif";
    public static final String IMAGE_LEFT_FILE_LOCATION = "images/cssdialog/left.gif";
    public static final String IMAGE_RIGHT_FILE_LOCATION = "images/cssdialog/right.gif";
    public static final String IMAGE_SAMPLE_FILE_LOCATION = "images/cssdialog/sample.gif";
    
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
    
    public static String OPEN_SPAN_TAG = "<span style=\"width: 100%;";
    public static String CLOSE_SPAN_TAG = "</span>";
    public static String OPEN_DIV_TAG = "<div style=\"width: 100%;";
    public static String CLOSE_DIV_TAG = "</div>";
}