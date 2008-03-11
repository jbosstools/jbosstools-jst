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

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.eclipse.wst.xml.ui.internal.contentassist.XMLRelevanceConstants;
import org.eclipse.wst.xml.ui.internal.util.SharedXMLEditorPluginImageHelper;
import org.jboss.tools.common.kb.KbException;
import org.jboss.tools.common.kb.KbProposal;
import org.jboss.tools.common.kb.KbQuery;
import org.jboss.tools.common.kb.wtp.WtpKbConnector;
import org.jboss.tools.common.model.plugin.ModelPlugin;
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
		if(isFacelets && tagName.indexOf(':')<1 && !RedHatHtmlContentAssistProcessor.JSFCAttributeName.equals(attributeName)) {
			Element element = (Element)node;

			NamedNodeMap attributes = element.getAttributes();
			Node jsfC = attributes.getNamedItem(RedHatHtmlContentAssistProcessor.JSFCAttributeName);
			if(jsfC!=null && (jsfC instanceof Attr)) {
				Attr jsfCAttribute = (Attr)jsfC;
				String jsfTagName = jsfCAttribute.getValue();
				if(jsfTagName!=null && jsfTagName.indexOf(':')>0) {
					htmlQuery = new StringBuffer(KbQuery.TAG_SEPARATOR).append(RedHatHtmlContentAssistProcessor.faceletHtmlPrefixStart + tagName).append(KbQuery.ATTRIBUTE_SEPARATOR).append(attributeName).append(KbQuery.ENUMERATION_SEPARATOR).append(matchString).toString();
					tagName = jsfTagName;
					faceletJsfTag = true;
				}
			}
		}

		if(!faceletJsfTag && isFacelets && tagName.indexOf(':')<0) {
			tagName = RedHatHtmlContentAssistProcessor.faceletHtmlPrefixStart + tagName;
		}

	    String query = new StringBuffer(KbQuery.TAG_SEPARATOR).append(tagName).append(KbQuery.ATTRIBUTE_SEPARATOR).append(attributeName).append(KbQuery.ENUMERATION_SEPARATOR).append(matchString).toString();
	    try {
			Collection proposals = wtpKbConnector.getProposals(query);
			if(proposals.size()==0 && htmlQuery!=null) {
				proposals = wtpKbConnector.getProposals(htmlQuery);
			}
            for (Iterator iter = proposals.iterator(); iter.hasNext();) {
            	KbProposal kbProposal = cleanFaceletProposal((KbProposal)iter.next());
            	kbProposal.postProcess(strippedValue, offset);
                int relevance = kbProposal.getRelevance();
                if(relevance==KbProposal.R_NONE) {
                    relevance = XMLRelevanceConstants.R_XML_ATTRIBUTE_VALUE;
                }
                
                if(kbProposal.getStart() >= 0) {
        			String replacementString = kbProposal.getReplacementString();
                    int replacementBeginPosition = contentAssistRequest.getReplacementBeginPosition() + kbProposal.getStart();
                    int replacementLength = kbProposal.getEnd() - kbProposal.getStart();
                	int cursorPositionDelta = 0;
                	if(currentValue.startsWith("\"") || currentValue.startsWith("'")) {
                		replacementBeginPosition = replacementBeginPosition +1;
            		} else {
            			cursorPositionDelta++;
            			replacementString = "\"" + replacementString;
            		}
                	if(currentValue.endsWith("\"") || currentValue.endsWith("'")) {
                		if(currentValue.endsWith(".}\"") && replacementString.endsWith("}")) {
                			replacementLength--;
                			replacementString = replacementString.substring(0, replacementString.length()-1);
                		}
            		} else {
            			replacementString += ("\"");
            		}
                	int cursorPosition = kbProposal.getPosition() + cursorPositionDelta;
                	RedHatCustomCompletionProposal proposal = new RedHatCustomCompletionProposal(kbProposal.autoActivationContentAssistantAfterApplication(), replacementString,
                			replacementBeginPosition, replacementLength, cursorPosition, SharedXMLEditorPluginImageHelper.getImage(SharedXMLEditorPluginImageHelper.IMG_OBJ_ATTRIBUTE),
            				kbProposal.getLabel(), null, kbProposal.getContextInfo(), relevance);
            		contentAssistRequest.addProposal(proposal);

            		continue;
                }
                
                
            	StringBuffer replacementStringBuffer = new StringBuffer(kbProposal.getReplacementString());
                int replacementBeginPosition = contentAssistRequest.getReplacementBeginPosition();
            	int replacementLength = contentAssistRequest.getReplacementLength();
            	int cursorPositionDelta = 0;
            	if(currentValue.startsWith("\"") || currentValue.startsWith("'")) {
            		replacementBeginPosition = replacementBeginPosition +1;
            		replacementLength--;
        		} else {
        			cursorPositionDelta++;
        			replacementStringBuffer.insert(0, "\"");
        		}
            	if(currentValue.endsWith("\"") || currentValue.endsWith("'")) {
            		replacementLength--;
            		if(currentValue.endsWith(".}\"") && replacementStringBuffer.toString().endsWith("}")) {
            			replacementLength--;
            			replacementStringBuffer = new StringBuffer(replacementStringBuffer.toString().substring(0, replacementStringBuffer.length()-1));
            		}
        		} else {
        			replacementStringBuffer.append("\"");
        		}
            	String replacementString = replacementStringBuffer.toString();
            	int cursorPosition = kbProposal.getPosition() + cursorPositionDelta;
            	RedHatCustomCompletionProposal proposal = new RedHatCustomCompletionProposal(kbProposal.autoActivationContentAssistantAfterApplication(), replacementString,
            			replacementBeginPosition, replacementLength, cursorPosition, SharedXMLEditorPluginImageHelper.getImage(SharedXMLEditorPluginImageHelper.IMG_OBJ_ATTRIBUTE),
        				kbProposal.getLabel(), null, kbProposal.getContextInfo(), relevance);
        		contentAssistRequest.addProposal(proposal);
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
		if(tagName.startsWith(RedHatHtmlContentAssistProcessor.faceletHtmlPrefixStart)) {
			return tagName.substring(RedHatHtmlContentAssistProcessor.faceletHtmlPrefixStart.length());
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
}