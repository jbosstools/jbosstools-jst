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
package org.jboss.tools.jst.jsp.outline.cssdialog.parsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Util;

/**
 * 
 * Parser for input style string
 * 
 * @author Evgeny Zheleznyakov
 * 
 */
public class Parser {

    private static String SEPARATE_SYMBOLS = ":;";

    private ArrayList<IListener> list;

    private HashMap<String, ArrayList<String>> elementsMap;

    public Parser(HashMap<String, ArrayList<String>> elementsMap) {
	this.elementsMap = elementsMap;
	list = new ArrayList<IListener>();
    }

    public void parse(String str) {

	if (str == null)
	    return;
	if (str.trim().equals(Constants.EMPTY))
	    return;

	StringTokenizer st = new StringTokenizer(str, SEPARATE_SYMBOLS);

	while (st.hasMoreTokens()) {
	    String name = null;
	    String value = null;

	    name = st.nextToken().trim().toLowerCase();
	    if (!Util.searchInElement(name, elementsMap))
		continue;
	    if (st.hasMoreTokens())
		value = st.nextToken().trim();
	    else
		continue;

	    if (name.equals(Constants.EMPTY)
		    || value.equals(Constants.EMPTY))
		continue;

	    for (IListener listener : list) {
		listener.nextElement(name, value);
	    }
	}
    }

    /**
     * 
     * Add listener for parser
     * 
     * @param listener
     *                Listener
     */
    public void addListener(IListener listener) {
	list.add(listener);
    }

    /**
     * 
     * Remove listener from parser
     * 
     * @param listener
     *                Listener
     */
    public void removeListener(IListener listener) {
	list.remove(listener);
    }
}