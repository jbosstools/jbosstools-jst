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
package org.jboss.tools.jst.jsp.contentassist;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.wst.sse.core.utils.StringUtils;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.eclipse.wst.xml.ui.internal.contentassist.XMLRelevanceConstants;
import org.eclipse.wst.xml.ui.internal.util.SharedXMLEditorPluginImageHelper;
import org.jboss.tools.common.kb.KbException;
import org.jboss.tools.common.kb.KbProposal;
import org.jboss.tools.common.kb.KbQuery;
import org.jboss.tools.common.kb.wtp.WtpKbConnector;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.contentassist.xpl.JSPBaseContentAssistProcessor;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author Igels
 */
public class JSPActiveContentAssistProcessor extends JSPBaseContentAssistProcessor {

    private WtpKbConnector wtpKbConnector;
    private boolean isFacelets = false;

	public JSPActiveContentAssistProcessor() {
		super();
	}

	public void addAttributeValueProposals(ContentAssistRequest contentAssistRequest) {
		if(wtpKbConnector==null) {
			return;
		}
		super.addAttributeValueProposals(contentAssistRequest);
	}
	
	public void addFaceletAttributeValueProposals(
			ContentAssistRequest contentAssistRequest,String tagName,IDOMNode node,String attributeName, String matchString,String strippedValue, int offset, String currentValue) {
		boolean faceletJsfTag = false;

		String htmlQuery = null;
		if(isFacelets && tagName.indexOf(':')<1 && !FaceletsHtmlContentAssistProcessor.JSFCAttributeName.equals(attributeName)) {
			Element element = (Element)node;

			NamedNodeMap attributes = element.getAttributes();
			Node jsfC = attributes.getNamedItem(FaceletsHtmlContentAssistProcessor.JSFCAttributeName);
			if(jsfC!=null && (jsfC instanceof Attr)) {
				Attr jsfCAttribute = (Attr)jsfC;
				String jsfTagName = jsfCAttribute.getValue();
				if(jsfTagName!=null && jsfTagName.indexOf(':')>0) {
					htmlQuery = new StringBuffer(KbQuery.TAG_SEPARATOR).append(FaceletsHtmlContentAssistProcessor.faceletHtmlPrefixStart + tagName).append(KbQuery.ATTRIBUTE_SEPARATOR).append(attributeName).append(KbQuery.ENUMERATION_SEPARATOR).append(matchString).toString();
					tagName = jsfTagName;
					faceletJsfTag = true;
				}
			}
		}

		if(!faceletJsfTag && isFacelets && tagName.indexOf(':')<0) {
			tagName = FaceletsHtmlContentAssistProcessor.faceletHtmlPrefixStart + tagName;
		}

    	if(!currentValue.startsWith("\"") && !currentValue.startsWith("'")) {
    		// Do not show any value proposals if the attribute value is not started with a quote/double-quote character 
    		return;
    	}

		String matchStringForQuery = matchString;
		String strippedValueForQuery = strippedValue;
		int elStartPosition = getELStartPosition(matchString);
		int delta = 0;
		String proposalPrefix = "";
		String proposalSufix = "";
		if (elStartPosition == -1) {
			if(isCharSharp(matchString, offset-1)) {
				proposalPrefix = "{";  //$NON-NLS-1$
			} else {
				proposalPrefix = "#{";  //$NON-NLS-1$
			}
		}

		if (elStartPosition == -1) {
			matchStringForQuery = "#{";
			strippedValueForQuery = "";
			delta = matchString.length();
		} else {
			matchStringForQuery = matchString.substring(elStartPosition);
			strippedValueForQuery = matchStringForQuery;
	    	delta = matchString.length() - matchStringForQuery.length();
		}
		
    	// Correct delta if matchString starts with a quote (exclude that quote)
		if (matchString.startsWith("\"") || matchString.startsWith("'")) {
			strippedValueForQuery = matchString.substring(0,1) + strippedValueForQuery;
			delta--;
		}
    	
	    String query = new StringBuffer(KbQuery.TAG_SEPARATOR).append(tagName).append(KbQuery.ATTRIBUTE_SEPARATOR).append(attributeName).append(KbQuery.ENUMERATION_SEPARATOR).append(matchStringForQuery).toString();
	    try {
			Collection proposals = wtpKbConnector.getProposals(query);
			if(proposals.size()==0 && htmlQuery!=null) {
				proposals = wtpKbConnector.getProposals(htmlQuery);
			}
            for (Iterator iter = proposals.iterator(); iter.hasNext();) {
            	KbProposal kbProposal = cleanFaceletProposal((KbProposal)iter.next());
            	kbProposal.postProcess(strippedValueForQuery, offset - delta);
                int relevance = kbProposal.getRelevance();
                if(relevance==KbProposal.R_NONE) {
                    relevance = XMLRelevanceConstants.R_XML_ATTRIBUTE_VALUE;
                }
                
                if(kbProposal.getStart() >= 0) {
        			String replacementString = kbProposal.getReplacementString();
                    int replacementBeginPosition = contentAssistRequest.getReplacementBeginPosition() + kbProposal.getStart() + delta;
                    int replacementLength = kbProposal.getEnd() - kbProposal.getStart();
                	int cursorPositionDelta = 0;
                	
                	// Add an EL-starting quotation characters if needed
                	if (elStartPosition == -1) {
                		replacementString = proposalPrefix + replacementString;
            			cursorPositionDelta += proposalPrefix.length();
                	}
  
                	if((currentValue.length() > StringUtils.strip(currentValue).length()) && 
                			(currentValue.endsWith("\"") || currentValue.endsWith("'")) ) {
                		if(!currentValue.endsWith("}\"")) {
                			replacementString += "}";
                		}
            		} else {
                		if(elStartPosition == -1 && !currentValue.endsWith("}")) {
                			replacementString += "}";
                		}
//            			replacementString += ("\"");
            		}
                	int cursorPosition = kbProposal.getPosition() + cursorPositionDelta;
                	String displayString = elStartPosition == -1 ? 
                			proposalPrefix + kbProposal.getReplacementString() + "}" :
                				kbProposal.getReplacementString();
                	AutoContentAssistantProposal proposal = new AutoContentAssistantProposal(
                			kbProposal.autoActivationContentAssistantAfterApplication(), 
                			replacementString,
                			replacementBeginPosition, replacementLength, cursorPosition, 
                			SharedXMLEditorPluginImageHelper.getImage(SharedXMLEditorPluginImageHelper.IMG_OBJ_ATTRIBUTE),
            				displayString, null, kbProposal.getContextInfo(), relevance);
            		contentAssistRequest.addProposal(proposal);
                }
            }
        } catch (KbException e) {
        	JspEditorPlugin.getPluginLog().logError(e);
        }
	}

	private KbProposal cleanFaceletProposal(KbProposal proposal) {
		if(isFacelets) {
			proposal.setLabel(removeFaceletsPrefix(proposal.getLabel()));
			proposal.setReplacementString(removeFaceletsPrefix(proposal.getReplacementString()));
		}
		return proposal;
	}

	private String removeFaceletsPrefix(String tagName) {
		if(tagName.startsWith(FaceletsHtmlContentAssistProcessor.faceletHtmlPrefixStart)) {
			return tagName.substring(FaceletsHtmlContentAssistProcessor.faceletHtmlPrefixStart.length());
		}
		return tagName;
	}

	public void init() {
	    super.init();
	}

	public void setKbConnector(WtpKbConnector connector) {
	    this.wtpKbConnector = connector;
	}

    public void setFacelets(boolean isFacelets) {
		this.isFacelets = isFacelets;
	}
    
	/*
	 * Checks if the EL start starting characters are present
	 * @return
	 */
	private int getELStartPosition(String matchString) {
		if (matchString == null || matchString.length() == 0)
			return -1;

		int offset = matchString.length();

		while (--offset >= 0) {
			if ('}' == matchString.charAt(offset))
				return -1;

			if ('"' == matchString.charAt(offset) || '\'' == matchString.charAt(offset)) {
                int backslashCount = 0;
                while ((offset - 1 - backslashCount) >= 0 && matchString.charAt(offset - 1 - backslashCount) == '\\') {
                    backslashCount++;
                }
                
                if (backslashCount % 2 == 0)
                    return -1;
            }

			if ('{' == matchString.charAt(offset) &&
					(offset - 1) >= 0 && 
					('#' == matchString.charAt(offset - 1) || 
							'$' == matchString.charAt(offset - 1))) {
				return (offset - 1);
			}
		}
		return -1;
	}

	/*  Checks if the preceding character is a Sharp-character
	 */
	private boolean isCharSharp(String matchString, int offset) {
		if (matchString == null || offset > matchString.length() || offset < 0) {
			return false;
		}

		return '#' == matchString.charAt(offset);
	}

}