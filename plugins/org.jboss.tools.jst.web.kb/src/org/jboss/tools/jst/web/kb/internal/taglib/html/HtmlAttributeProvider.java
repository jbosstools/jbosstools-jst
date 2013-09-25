/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.taglib.html;

import org.jboss.tools.jst.web.kb.internal.taglib.AbstractAttributeProvider;

/**
 * @author Alexey Kazakov
 */
public abstract class HtmlAttributeProvider extends AbstractAttributeProvider {

	public static final String BUTTON = "button";
	public static final String COLLAPSIBLE = "collapsible";
	public static final String COLLAPSIBLE_SET = "collapsible-set";
	public static final String CONTROLGROUP = "controlgroup";
	public static final String DIALOG = "dialog";
	public static final String CONTENT = "content";
	public static final String FIELDCONTENT = "fieldcontain";
	public static final String SUBMIT = "submit";
	public static final String RESET = "reset";
	public static final String TYPE = "type";
	public static final String INPUT = "input";
	public static final String CHECKBOX = "checkbox";
	public static final String DIV = "div";
	public static final String HEADER = "header";
	public static final String FOOTER = "footer";
	public static final String NAVBAR = "navbar";
	public static final String SLIDER = "slider";
	public static final String LISTVIEW = "listview";
	public static final String PAGE = "page";
	public static final String TABLE = "table";
	public static final String POPUP = "popup";
	public static final String RADIO = "radio";
	public static final String RANGE = "range";
	public static final String HIDEN = "hiden";
	public static final String TEXTAREA = "textarea";
	public static final String PANEL = "panel";
	public static final String EMAIL = "email";
	public static final String NUMBER = "number";
	public static final String TEXT = "text";
	public static final String URL = "url";

	protected static final AttributeData TYPE_BUTTON = new AttributeData(TYPE, BUTTON);
	protected static final AttributeData TYPE_SUBMIT = new AttributeData(TYPE, SUBMIT);
	protected static final AttributeData TYPE_RESET = new AttributeData(TYPE, RESET);
	protected static final AttributeData TYPE_CHECKBOX = new AttributeData(TYPE, CHECKBOX);
	protected static final AttributeData TYPE_RADIO = new AttributeData(TYPE, RADIO);
	protected static final AttributeData TYPE_RANGE = new AttributeData(TYPE, RANGE);
	protected static final AttributeData TYPE_HIDEN = new AttributeData(TYPE, HIDEN);
	protected static final AttributeData TYPE_EMAIL = new AttributeData(TYPE, EMAIL);
	protected static final AttributeData TYPE_NUMBER = new AttributeData(TYPE, NUMBER);
	protected static final AttributeData TYPE_TEXT = new AttributeData(TYPE, TEXT);
	protected static final AttributeData TYPE_URL = new AttributeData(TYPE, URL);
}