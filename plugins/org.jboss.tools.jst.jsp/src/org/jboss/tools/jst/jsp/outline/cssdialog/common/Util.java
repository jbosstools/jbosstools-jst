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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSNode;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSStyleRule;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSStyleSheet;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.jst.jsp.JspEditorPlugin;

/**
 * Utility class
 *
 * @author Evgeny Zheleznyakov
 */
public class Util {

    public static final String CSS_FILE_EXTENTION = "css"; //$NON-NLS-1$

	private static String RGB = "rgb"; //$NON-NLS-1$
	private static String THIN = "thin"; //$NON-NLS-1$

	private static int START_INDEX_RED = 1;
	private static int END_INDEX_RED = 3;
	private static int START_INDEX_GRENN = 3;
	private static int END_INDEX_GREEN = 5;
	private static int START_INDEX_BLUE = 5;
	private static int END_INDEX_BLUE = 7;

	private static int MAX_VALUE_RGB = 255;
	private static int MIN_VALUE_RGB = 0;

	private static int COUNT_COLORS = 3;

	private static int RADIX = 16;

	private static int COLOR_LENGTH = 7;

	private static int START_COLOR_INDEX = 0;

	private static char OPEN_BRACKET = '{';
	private static char CLOSE_BRACKET = '}';

	private static char SHARP = '#';
	private static String SHARP_STRING = "#"; //$NON-NLS-1$

	private static String ZERO_STR = "0"; //$NON-NLS-1$
	private static int NORMAL_MIN_VALUE = 10;
	private final static String EDITOR_ID = "org.eclipse.wst.css.core.csssource.source"; //$NON-NLS-1$

	/**
	 * Method for checking contain or not css attribute folder
	 *
	 * @param name Name css attribute
	 * @return true - contain, or else - don't contain
	 */
	public static boolean containFolder(String name) {
		return Constants.elemFolder.contains(name);
	}

	/**
	 * Method for search string into css attributes
	 *
	 * @param name Name
	 * @param elementMap Map of css attributes
	 * @return true - find, or else - don't find
	 */
	public static boolean searchInElement(String name,
			Map<String, ArrayList<String>> elementMap) {

		Set<String> set = elementMap.keySet();

		for (String str : set) {
			ArrayList<String> list = elementMap.get(str);
			if(list.contains(name.toLowerCase())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Method for get RGB from string
	 *
	 * @param color Color string
	 * @return RGB color, or null, if color invalid
	 */
	public static RGB getColor(String color) {
		if (color.equals(Constants.EMPTY) || color.equals(Constants.NONE)) {
			return null;
		}

		if (color.charAt(START_COLOR_INDEX) == SHARP && color.length() == COLOR_LENGTH) {

			String strR = color.substring(START_INDEX_RED, END_INDEX_RED);
			String strG = color.substring(START_INDEX_GRENN, END_INDEX_GREEN);
			String strB = color.substring(START_INDEX_BLUE, END_INDEX_BLUE);

			try {
				Integer.parseInt(strR, RADIX);
				Integer.parseInt(strG, RADIX);
				Integer.parseInt(strB, RADIX);
			} catch (NumberFormatException e) {
				return null;
			}
			return convertColorHEX(color);
		} else if (color.toLowerCase().indexOf(RGB) != Constants.DONT_CONTAIN) {

			int start = color.indexOf(OPEN_BRACKET);
			int end = color.indexOf(CLOSE_BRACKET);
			String str = color.substring(start + 1, end);

			StringTokenizer st = new StringTokenizer(str, Constants.COMMA);

			int j = 0;
			while (st.hasMoreTokens()) {
				try {
					int i = Integer.parseInt(st.nextToken().trim());
					if (i < MIN_VALUE_RGB || i > MAX_VALUE_RGB) {
						return null;
					}
				} catch (NumberFormatException e) {
					return null;
				}
				j++;
			}
			if (j == COUNT_COLORS) {
				return convertColorRGB(color);
			}
		} else {
			Map<String, String> colorMap = CSSConstants.COLORS;

			for (String key : colorMap.keySet()) {
				if (colorMap.get(key).equalsIgnoreCase(color)) {
					return convertColorHEX(key);
				}
			}
		}

		return null;
	}

	/**
	 * Method for convert string(123px) into two string (123 and px)
	 *
	 * @param str String for convert
	 * @return Array two strings, or null, if str incorrect
	 */
	public static String[] convertExtString(String str) {
		if (THIN.equalsIgnoreCase(str)) {
			return new String[] { THIN, Constants.EMPTY };
		}
		if (str == null) {
			return null;
		}
		if (str.trim().equals(Constants.EMPTY)) {
			return new String[] { Constants.EMPTY, Constants.EMPTY };
		}

		String newStr = str.toLowerCase().trim();
		int index = -1;
		for (int i = 1; i < Constants.extSizes.length; i++) {
			index = newStr.indexOf(Constants.extSizes[i]);
			if (index != -1) {
				break;
			}
		}

		if (index == -1) {
			return new String[] { newStr, Constants.EMPTY };
		}

		String number = newStr.substring(0, index);
		String ext = newStr.substring(index, index + 2);

		return new String[] { number, ext };
	}

	/**
	 * Method for search css attribute into extElements
	 *
	 * @param name Name of css attribute
	 * @return true - find, or else - don't find
	 */
	public static boolean searchInExtElement(String name) {
		return Constants.extElem.contains(name);
	}

	/**
	 * Method for getting RGB color from string color
	 *
	 * @param color String color
	 * @return RGB color
	 */
	public static RGB convertColorRGB(String color) {

		String newStr = color.trim().toLowerCase();

		int rgb[] = new int[COUNT_COLORS];

		int start = newStr.indexOf(OPEN_BRACKET);
		int end = newStr.indexOf(CLOSE_BRACKET);
		String str = newStr.substring(start + 1, end);

		StringTokenizer st = new StringTokenizer(str, Constants.COMMA);
		int i = 0;
		while (st.hasMoreTokens()) {
			rgb[i++] = Integer.parseInt(st.nextToken().trim());
		}
		return new RGB(rgb[0], rgb[1], rgb[2]);
	}

	/**
	 * Method for getting RGB color from hex string
	 *
	 * @param color String color
	 * @return RGB color
	 */
	public static RGB convertColorHEX(String color) {

		String newStr = color.trim().toLowerCase();

		String strR = newStr.substring(START_INDEX_RED, END_INDEX_RED);
		String strG = newStr.substring(START_INDEX_GRENN, END_INDEX_GREEN);
		String strB = newStr.substring(START_INDEX_BLUE, END_INDEX_BLUE);

		int red = Integer.parseInt(strR, RADIX);
		int green = Integer.parseInt(strG, RADIX);
		int blue = Integer.parseInt(strB, RADIX);

		return new RGB(red, green, blue);
	}

	/**
	 * Method for convert RGB to String
	 *
	 * @param rgb RGB color
	 * @return String color
	 */
	public static String createColorString(RGB rgb) {
		String colorStr = SHARP_STRING
				+ (rgb.red < NORMAL_MIN_VALUE ? ZERO_STR
						: Constants.EMPTY)
				+ Integer.toHexString(rgb.red)
				+ (rgb.green < NORMAL_MIN_VALUE ? ZERO_STR
						: Constants.EMPTY)
				+ Integer.toHexString(rgb.green)
				+ Constants.EMPTY
				+ (rgb.blue < NORMAL_MIN_VALUE ? ZERO_STR
						: Constants.EMPTY)
				+ Integer.toHexString(rgb.blue);
		colorStr = colorStr.toUpperCase();
		if (CSSConstants.COLORS.get(colorStr) != null) {
			return CSSConstants.COLORS.get(colorStr);
		}
		return colorStr;
	}

	/**
	 * Get current project for opened editor
	 *
	 * @return IProject object
	 */
	public static IResource getCurrentProject() {
		IResource result = null;
		IWorkbenchPage page = JspEditorPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (page != null && page.getActiveEditor() != null) {
			IEditorPart editor = page.getActiveEditor();
			IEditorInput input = editor.getEditorInput();
			if (input instanceof IFileEditorInput) {
				IFile file = ((IFileEditorInput) input).getFile();
				result = file.getProject();
			}
		} else {
			result = ResourcesPlugin.getWorkspace().getRoot();
		}
		return result;
	}

	/**
	 * Get current opened CSS file. If file is not CSS file - return null instead.
	 *
	 * @return IFile CSS file
	 */
	public static IFile getActiveCssFile() {
		IWorkbenchPage page = JspEditorPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (page != null && page.getActiveEditor() != null) {
			IEditorInput input = page.getActiveEditor().getEditorInput();
			if (input instanceof IFileEditorInput) {
				IFile file = ((IFileEditorInput) input).getFile();
				if (file.getName().toLowerCase().endsWith(CSS_FILE_EXTENTION)) {
					return file;
				}
			}
		}
		return null;
	}

	/**
	 * Method return the name of CSS selector in opened CSS file.
	 *
	 * @return selector name
	 */
	public static String getActivePageCSSSelectorIfAny() {
		IWorkbenchPage page = JspEditorPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		ISelection selection = page.getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ss = (IStructuredSelection)selection;
			for (Iterator iterator = ss.iterator(); iterator.hasNext();) {
				Object node = iterator.next();
				if (node instanceof ICSSNode) {
					ICSSStyleRule styleRule = getSelector((ICSSNode)node, null, 0);
					if (styleRule != null) {
						return styleRule.getSelectorText();
					}
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param node
	 * @param selector
	 * @param index
	 * @return
	 */
	public static ICSSStyleRule getSelector(ICSSNode node, String selector, int index) {
		if (node != null) {
			// NOTE: if needed this method should be extended regarding other properties of ICSSNode class
			short nodeType = node.getNodeType();
			switch (nodeType) {
			case ICSSNode.STYLESHEET_NODE:
				ICSSStyleSheet styleSheet = (ICSSStyleSheet) node;
				return getSelector(styleSheet.getFirstChild(), selector, index);
			case ICSSNode.STYLERULE_NODE:
				ICSSStyleRule styleRule = (ICSSStyleRule) node;
				boolean selectorFound = true;
				if (selector != null) {
					if (styleRule.getSelectorText().equals(selector)) {
						if (index > 0) {
							index--;
							selectorFound = false;
						}
					} else {
						selectorFound = false;
					}
				}
				if (selectorFound) {
					return styleRule;
				} else {
					return getSelector(styleRule.getNextSibling(), selector, index);
				}
			default:
				return getSelector(node.getParentNode(), selector, index);
			}
		}
		return null;
	}

	/**
	 * Method is used to convert style class value to its CSS file view presentation.
	 * Example: class="value1, value2, value3" => ".value1 .value2 .value3"
	 *
	 * @param styleClass the value of class attribute
	 * @return style class presentation in CSS file
	 */
	public static String formatStyleClassToCSSView (String styleClass) {
    	if (styleClass != null && !styleClass.equals(Constants.EMPTY)) {
    		String[] styleClasses = styleClass.split("\\s++"); //$NON-NLS-1$
    		StringBuffer sb = new StringBuffer();
    		for (int i = 0; i < styleClasses.length; i++) {
    			String value = styleClasses[i];
    			// check if first symbol doesn't specify some special CSS symbols
				if (!value.startsWith("#")) { //$NON-NLS-1$
					sb.append("."); //$NON-NLS-1$
				}
				sb.append(value);
				if (i != (styleClasses.length - 1)) {
					sb.append(Constants.COMMA);
					sb.append(Constants.WHITE_SPACE);
				}
			}
	    	return sb.toString().trim();
    	}
    	return null;
	}

	/**
	 * This method is handle input parameter in reverse way as <code>formatStyleClassToCSSView</code> method.
	 * Example: ".value1 .value2 .value3" => "value1, value2, value3"
	 *
	 * @return String value
	 */
    public static String formatCSSSelectorToStyleClassView(String cssSelector) {
    	String className = cssSelector;
    	if (cssSelector != null) {
            className = cssSelector.replaceAll("\\.", Constants.EMPTY); //$NON-NLS-1$
            className = className.replaceAll("\\,", Constants.EMPTY); //$NON-NLS-1$
            className = className.replaceAll("\\s++", Constants.WHITE_SPACE); //$NON-NLS-1$
    	}

        return className;
    }
    
    public static Point getSelectionInFile(IFile file) {

		IEditorReference[] editorReference = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().findEditors(
						new FileEditorInput(file), EDITOR_ID,
						IWorkbenchPage.MATCH_INPUT | IWorkbenchPage.MATCH_ID);
		Point point = new Point(0, 0);
		if ((editorReference != null) && (editorReference.length > 0)) {

			IEditorPart editorPart = editorReference[0].getEditor(false);
			if (editorPart != null)
				point = ((StructuredTextEditor) editorPart).getTextViewer()
						.getSelectedRange();
		}
		return point;
	}
}