/******************************************************************************* 
 * Copyright (c) 2009-2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.proposal;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.ICSSContainerSupport;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageContextFactory.CSSStyleSheetDescriptor;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.w3c.dom.css.CSSMediaRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

/**
 * The CSS Class proposal type. Is used to collect and return the proposals on
 * the CSS classes
 * 
 * @author Victor Rubezhny
 *
 */
public class CSSClassProposalType extends CustomProposalType {
	private static final ImageDescriptor IMAGE = WebKbPlugin.getImageDescriptor(WebKbPlugin.class, "EnumerationProposal.gif"); //$NON-NLS-1$

	static String ID = "cssclass"; //$NON-NLS-1$
	static String QUOTE_1 = "'"; //$NON-NLS-1$
	static String QUOTE_2 = "\""; //$NON-NLS-1$
	Set<String> idList = new TreeSet<String>();

	@Override
	protected void init(IPageContext context) {
		idList.clear();
		if (context instanceof ICSSContainerSupport) {
			ICSSContainerSupport cssSource = (ICSSContainerSupport)context;
			
			List<CSSStyleSheetDescriptor> descrs = cssSource.getCSSStyleSheetDescriptors();
			if (descrs != null) {
				for (CSSStyleSheetDescriptor descr : descrs) {
					CSSStyleSheet sheet = descr.getStylesheet();
					if (sheet != null) {
						CSSRuleList rules = sheet.getCssRules();
						for (int i = 0; rules != null && i < rules.getLength(); i++) {
							CSSRule rule = rules.item(i);
							idList.addAll(getClassNamesFromCSSRule(rule));
						}
					}
				}
			}
		}
	}

	/**
	 * Returns the style class name found in the specified CSS Rule 
	 * 
	 * @param cssRule
	 * @param styleName
	 * @return
	 */
	public static Set<String> getClassNamesFromCSSRule(CSSRule cssRule) {
		Set<String> styleNames = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

		if (cssRule instanceof CSSMediaRule) {
			CSSMediaRule cssMediaRule = (CSSMediaRule)cssRule;
			CSSRuleList rules = cssMediaRule.getCssRules();
			for (int i = 0; rules != null && i < rules.getLength(); i++) {
				CSSRule rule = rules.item(i);
				styleNames.addAll(getClassNamesFromCSSRule(rule));
			}
			return styleNames;
		}
		if (!(cssRule instanceof CSSStyleRule))
			return styleNames;
		
		// get selector text
		String selectorText = ((CSSStyleRule) cssRule).getSelectorText();

		if (selectorText != null) {
			String styles[] = selectorText.trim().split(","); //$NON-NLS-1$
			for (String styleText : styles) {
				String[] styleWords = styleText.trim().split(" ");  //$NON-NLS-1$
				if (styleWords != null) {
					for (String styleWord : styleWords) {
						String[] anotherStyleWords = styleWord.split(":");
						for (String name : anotherStyleWords) {
							// this can be not a name but a function that takes another style name as an argument.
							// We're not interrested in such functions/style names here
							if (name.contains("("))
								continue; // Skip this function
							
							String nameWithoutArgs = name;
							
							// Add (if exists) class name defined before args
							if (name.indexOf('[') >= 0) { //$NON-NLS-1$
								nameWithoutArgs = name.substring(0, name.indexOf("[")); //$NON-NLS-1$
							}
							
							if (nameWithoutArgs != null && nameWithoutArgs.indexOf(".") >= 0) { //$NON-NLS-1$
								nameWithoutArgs = nameWithoutArgs.substring(nameWithoutArgs.indexOf(".") + 1); //$NON-NLS-1$
								
								int ind = nameWithoutArgs.indexOf('+');
								if (ind != -1) nameWithoutArgs = nameWithoutArgs.substring(0, ind);
								
								ind = nameWithoutArgs.indexOf('>');
								if (ind != -1) nameWithoutArgs = nameWithoutArgs.substring(0, ind);
								
								styleNames.add(nameWithoutArgs);
							}

							if (name.lastIndexOf(']') >= 0) {
								nameWithoutArgs = name.substring(name.indexOf(']') + 1);
								if (nameWithoutArgs != null && nameWithoutArgs.indexOf(".") >= 0) { //$NON-NLS-1$
									nameWithoutArgs = nameWithoutArgs.substring(nameWithoutArgs.indexOf(".") + 1); //$NON-NLS-1$
									int ind = nameWithoutArgs.indexOf('+');
									if (ind != -1) nameWithoutArgs = nameWithoutArgs.substring(0, ind);
									
									ind = nameWithoutArgs.indexOf('>');
									if (ind != -1) nameWithoutArgs = nameWithoutArgs.substring(0, ind);

									styleNames.add(nameWithoutArgs);
								}
							}
						}
					}
				}
			}
		}
		return styleNames;
	}

	@Override
	public TextProposal[] getProposals(KbQuery query) {
		// Do not use getValue() because it trims the string and removes opening quote char, but all the characters 
		// (including whitespaces and quotes) are valuable here
		String v = query.getStringQuery(); //query.getValue(); 
		int predicateLength = 0;
		while(predicateLength < v.length() && (v.charAt(predicateLength) == '"' || v.charAt(predicateLength) == '\''))
			predicateLength++;
		
		int b = v.lastIndexOf(' ');
		b = (b == -1 ? v.lastIndexOf('\t') : b);
		b = (b == -1 ? predicateLength : b + 1);
		int e = v.length(); 

		String prefix = v.substring(b);

		List<TextProposal> proposals = new ArrayList<TextProposal>();
		for (String text: idList) {
			if(text.startsWith(prefix)) {
				TextProposal proposal = new TextProposal();
				proposal.setLabel(text);
				proposal.setReplacementString(text);
				proposal.setPosition(b + text.length() - predicateLength);
				proposal.setStart(b - predicateLength);
				proposal.setEnd(e - predicateLength);
				proposal.setImageDescriptor(IMAGE);
				
				proposals.add(proposal);
			}
		}

		return proposals.toArray(new TextProposal[0]);
	}
}