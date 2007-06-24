/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc.
 *     Red Hat, Inc. 
 *******************************************************************************/
package org.jboss.tools.jst.web.debug.ui.internal.views.xpl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.jboss.tools.jst.web.debug.ui.internal.views.properties.xpl.WebDataProperties;
import org.jboss.tools.jst.web.debug.ui.xpl.WebDebugUIPlugin;

/**
 * @author Jeremy
 */
public class FilteredVariablesEnumeration implements Enumeration {
	private String fStopWords;
	private String fEnablementPropertyName;
	private String fFilterPropertyName;
	private List fPatterns;
	private List fStopPatterns;
	private WebDataProperties fWdp;
	private IValue fValue;
	private int fCurrentIndex;
	private List fVariables;
		
	FilteredVariablesEnumeration (IValue value, String filterEnablementPropertyName, String filterValuePropertyName, String stopWords) {
		this.fValue = value;
		this.fEnablementPropertyName = filterEnablementPropertyName;
		this.fFilterPropertyName = filterValuePropertyName;
		this.fStopWords = stopWords;
		this.fPatterns = null;
		this.fWdp = new WebDataProperties(WebDebugUIPlugin.getDefault().getPreferenceStore());
		if (fWdp.isEnabledFilter(fEnablementPropertyName)) {
			this.fPatterns = createPatternList(fWdp.getFilterMask(fFilterPropertyName));
		}
		this.fStopPatterns = createPatternList(this.fStopWords);
		createValue();
	}
	
	private List createPatternList(String filterValue) {
		List patterns = new ArrayList();
		try {
			StringBuffer sb = new StringBuffer(filterValue);
			StringBuffer cp = new StringBuffer();
			for (int i = 0; i < sb.length(); i++) {
				boolean doAddPattern = false;
				if (	(cp.length() == 0 && Character.isJavaIdentifierStart(sb.charAt(i))) ||
						(cp.length() > 0 && Character.isJavaIdentifierPart(sb.charAt(i))) || ('*' == sb.charAt(i)) 
					) {
					cp.append(sb.charAt(i));
				} else {
					if(cp.length() > 0) {
						patterns.add(new Pattern(cp.toString()));
						cp = new StringBuffer();
					}
				}
			}
			if(cp.length() > 0) { // Add last pattern
				patterns.add(new Pattern(cp.toString()));
			}

			return (patterns.size() == 0 ? null : patterns);
		} catch (Exception x) {
			return null;
		}
	}
	
	private void createValue() {
		fVariables = null; // For sure
		try {
			if (fValue == null || !fValue.hasVariables()) return;
			IVariable[] vars = fValue.getVariables();
			
			for (int i = 0; vars != null && i < vars.length; i++) {
				if (matches(vars[i])) {
					if (fVariables == null) fVariables = new ArrayList();
					fVariables.add(vars[i]);
				}
			}
		} catch (Exception x) {
			fVariables = null; // For sure
		}
	}

	private boolean matches(IVariable var) {
		String varName = null; 
		try {
			varName = var.getName();
		} catch (Exception x) { return false; }
		if (varName == null || varName.length() == 0) return false;
		
		for (int i = 0; fStopPatterns != null && i < fStopPatterns.size(); i++) {
			try {
				Pattern p = (Pattern)fStopPatterns.get(i);
				if (p.matches(varName)) { 
					return false;
				}
			} catch (Exception x) {
			}
		}
		
		for (int i = 0; fPatterns != null && i < fPatterns.size(); i++) {
			try {
				Pattern p = (Pattern)fPatterns.get(i);
				if (p.matches(varName)) { 
					return true;
				}
			} catch (Exception x) {
			}
		}
		return false;
	}
	
	
	/* (non-Javadoc)
	 * @see java.util.Enumeration#hasMoreElements()
	 */
	public boolean hasMoreElements() {
		return (fVariables != null && fCurrentIndex < fVariables.size());
	}

	/* (non-Javadoc)
	 * @see java.util.Enumeration#nextElement()
	 */
	public Object nextElement() {
		Object obj = null;
		if (hasMoreElements()) {
			obj = fVariables.get(fCurrentIndex);
			fCurrentIndex++;
		}
		return obj;
	}
}

class Pattern {
	private String fPattern;
	private Element[] fSequence;
	
	Pattern(String pattern) {
		this.fPattern = pattern;
		fSequence = createSequence();
	}

	private Element[] createSequence() {
		List sequence = new ArrayList();
		try {
			StringBuffer sb = new StringBuffer(fPattern);
			StringBuffer cp = new StringBuffer();
			for (int i = 0; i < sb.length(); i++) {
				boolean doAddPattern = false;
				if ( Character.isJavaIdentifierStart(sb.charAt(i)) ||
						Character.isJavaIdentifierPart(sb.charAt(i))) {
					cp.append(sb.charAt(i));
				} else {
					if(cp.length() > 0) {
						sequence.add(new SequenceElement(cp.toString()));
						cp = new StringBuffer();
					}
					if ('*' == sb.charAt(i)) {
						sequence.add(new AnySequenceElement());
					} 
				}
			}
			if(cp.length() > 0) { // Add last pattern
				sequence.add(new EndSequenceElement(cp.toString()));
			}

			return (sequence.size() == 0 ? null : (Element[])sequence.toArray(new Element[sequence.size()]));
		} catch (Exception x) {
			return null;
		}
	}
	
	boolean matches (String str) {
		if (str == null || str.length() == 0) return false;
		if (fSequence == null || fSequence.length == 0) return false;
		int searchFrom = 0;
		Element el = null;
		for (int i = 0; i < fSequence.length; i++) {
			boolean strict = (el == null || el instanceof SequenceElement);
			el = fSequence[i];
			if (!fSequence[i].matches(str, searchFrom, strict)) return false;
			searchFrom += fSequence[i].getSearchDelta();
		}
		if (el instanceof SequenceElement && searchFrom != str.length()) return false;
		
		return true;	
	}
	
}

class AnySequenceElement implements Element {
	public boolean matches(String text, int searchFrom, boolean strict) {
		if (text == null || text.length() == 0)	return false;
		return true;
	}
	
	public int getSearchDelta() {
		return 0;
	}
}

class SequenceElement implements Element {
	protected String fSequence;
	protected int fCurrentSearchDelta = 0;
	
	public SequenceElement(String sequence) {
		this.fSequence = sequence;
	}

	public boolean matches(String text, int searchFrom, boolean strict) {
		fCurrentSearchDelta = 0;
		if (text == null || fSequence == null) return false;
		if (text.length() < searchFrom + fSequence.length()) return false;
		int pos = text.toLowerCase().indexOf(fSequence.toLowerCase());
		if (strict) {
			if (pos == searchFrom) {
				fCurrentSearchDelta = fSequence.length();
				return true;
			}
		} else {
			if (pos > -1) {
				fCurrentSearchDelta = pos - searchFrom + fSequence.length();
				return true;
			}
		}
		return false;
	}

	public int getSearchDelta() {
		return fCurrentSearchDelta;
	}
	
}

class EndSequenceElement extends SequenceElement {
	public EndSequenceElement(String sequence) {
		super(sequence);
	}

	public boolean matches(String text, int searchFrom, boolean strict) {
		fCurrentSearchDelta = 0;
		if (text == null || fSequence == null) return false;

		if (text.length() < searchFrom + fSequence.length()) return false;

		if (text.toLowerCase().endsWith(fSequence.toLowerCase())) {
			fCurrentSearchDelta = text.length() - searchFrom;
			return true;
		}
		return false;
	}
}

interface Element {
	boolean matches(String text, int searchFrom, boolean strict);
	int getSearchDelta();
}