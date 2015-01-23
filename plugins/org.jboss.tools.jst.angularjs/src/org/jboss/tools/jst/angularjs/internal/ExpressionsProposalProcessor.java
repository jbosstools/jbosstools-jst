/******************************************************************************* 
 * Copyright (c) 2015 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.angularjs.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.angularjs.AngularJsPlugin;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.IProposalProcessor;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.internal.BrowserDataProviderManager;

/**
 * @author Alexey Kazakoc
 */
public class ExpressionsProposalProcessor implements IProposalProcessor {

	private ImageDescriptor image;

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.IProposalProcessor#getProposals(org.jboss.tools.jst.web.kb.KbQuery, org.jboss.tools.jst.web.kb.IPageContext)
	 */
	@Override
	public TextProposal[] getProposals(KbQuery query, IPageContext context) {
		List<TextProposal> proposals = new ArrayList<TextProposal>();
		String qValue = query.getValue();
		int startEl = qValue.lastIndexOf("{{");
		if(startEl<0) {
			qValue = query.getRegionValuePrefix();
			if(qValue!=null) {
				startEl = qValue.lastIndexOf("{{");
			}
		}
		if(startEl>-1) {
			String value = qValue.substring(startEl + 2);
			if(!value.contains("\n") && !value.contains("}") && (new AngularJSRecognizer()).isUsed(context)) {
				// Create a JS
				String js = generateJs(value, context);
				Collection<Object> results = BrowserDataProviderManager.getInstance().evaluate(js, context);
				// Convert the data to a list of proposals
				if(!results.isEmpty()) {
					if(image == null) {
						image = ImageDescriptor.createFromFile(AngularJsPlugin.class, "angular.png");
					}
					for (Object object : results) {
						if(object==BrowserDataProviderManager.DATA_LOADING) {
							ReplacementString rs = getReplacementString(query, "", qValue, "");
							TextProposal proposal = new TextProposal();
							proposal.setLabel(AngularMessages.dataLoading);
							proposal.setReplacementString(rs.string);
							proposal.setPosition(rs.position);
							proposal.setImageDescriptor(image);
							proposal.setAutoActivationContentAssistantAfterApplication(true);
							proposal.setRelevance(Integer.MAX_VALUE);
							proposals.add(proposal);
						} else {
							String result = object.toString();
							StringTokenizer st = new StringTokenizer(result, ";", false);
							int dotIndex = value.lastIndexOf('.');
							String remove = value;
							if(dotIndex>-1) {
								remove = value.substring(dotIndex + 1);
							}
							while(st.hasMoreElements()) {
								String label = st.nextToken().trim();
								if(!label.matches("\\d+")) { // Ignore numbers
									ReplacementString rs = getReplacementString(query, label, qValue, remove);
									TextProposal proposal = new TextProposal();
									proposal.setRelevance(TextProposal.R_TAG_TEMPLATE + 10);
									proposal.setLabel(label);
									proposal.setReplacementString(rs.string);
									proposal.setPosition(rs.position);
									proposal.setImageDescriptor(image);
									proposal.setAutoActivationContentAssistantAfterApplication(false);
									proposals.add(proposal);
								}
							}
						}
					}
				}
			}
		}
		return proposals.toArray(new TextProposal[proposals.size()]);
	}

	private static ReplacementString getReplacementString(KbQuery query, String label, String qValue, String remove) {
		String suffix = query.getRegionValueSufix();
		if(suffix==null) {
			suffix = "";
		}

		StringBuilder replacementString = new StringBuilder();
		if(query.getType() == KbQuery.Type.ATTRIBUTE_VALUE) {
			replacementString.append(qValue);
		}
		replacementString.append(label.substring(remove.length()));
		int position = replacementString.length();
		if(query.getType() == KbQuery.Type.ATTRIBUTE_VALUE) {
			replacementString.append(suffix);
		}
		int close = suffix.indexOf("}}");
		boolean closedExp = false;
		if(close>-1) {
			String c = suffix.substring(0, close);
			if(!c.contains("\n")) {
				closedExp = true;
			}
		}
		if(!closedExp) {
			replacementString.append("}}");
		}
		ReplacementString rs = new ReplacementString();
		rs.string = replacementString.toString();
		rs.position = position;
		return rs;
	}

	private static class ReplacementString {
		String string;
		int position;
	}

	private String generateJs(String value, IPageContext context) {
		String js =
				  "var ORG_JBOSS_TOOLS_JST = {};"

				+ "ORG_JBOSS_TOOLS_JST.getProposals = function(element, value) {"
				+ 	"var result = '';"
				+   "if(typeof angular == \"object\") {"
				+ 	    "var scope = angular.element(element).scope();"
				+ 	    "var values = value.split(\".\");"
				+ 	    "var parentObject = scope;"
				+ 	    "for (var i = 0; i < values.length-1; i++) {"
				+ 	        "parentObject = ORG_JBOSS_TOOLS_JST.getMemberObject(parentObject, values[i]);"
				+ 	    "}"
				+ 	    "if(parentObject) {"
				+ 	        "result = ORG_JBOSS_TOOLS_JST.getProposalsForLastSegment(parentObject, values[values.length-1]);"
				+ 	    "}"
				+   "}"
				+ 	"return result;"
				+ "};"

				+ "ORG_JBOSS_TOOLS_JST.getProposalsForLastSegment = function(member, nameMask) {"
				+ 	"var result = '';"
				+ 	"for (var p in member) {"
				+ 		"if ((p.indexOf('$') !== 0) && (p.lastIndexOf(nameMask, 0) === 0)) {" //Not Angular internal variable which starts with nameMask
				+ 			"result += p;"
				+ 			"if (typeof member[p] === \"function\") {"
				+ 				"var funStr = member[p].toString();"
				+ 				"var funArgsStr = funStr.slice(funStr.indexOf('(') + 1, funStr.indexOf(')'));"
				+ 				"result += '(' + funArgsStr + ')';"
				+ 			"}"
				+ 			"result += ';';"
				+ 		"}"
				+ 	"}"
				+	"return result;"
				+ "};"

				+ "ORG_JBOSS_TOOLS_JST.getMemberObject = function(parentObject, memberName) {"
				+ 	"var brIndex = memberName.indexOf('(');"
				+	"var name = memberName;"
				+	"if(brIndex>0) {"
				+		"name = memberName.slice(0, brIndex);"
				+	"}"
				+	"for(var propertyName in parentObject) {"
				+		"if(propertyName == name) {"
				+			"return parentObject[propertyName];"
				+		"}"
				+	"}"
				+ "};"

				+ "var element = document.querySelector(\"[#{" + BrowserDataProviderManager.ELEMENT_ID_PARAM + "}='#{" + BrowserDataProviderManager.ELEMENT_ID_VALUE_PARAM + "}']\");"
				+ "if(element) { "
				+ 	"return ORG_JBOSS_TOOLS_JST.getProposals(element, '" + value + "');"
				+ "}"
				+ "return \"\"";
		return js;
	}
}