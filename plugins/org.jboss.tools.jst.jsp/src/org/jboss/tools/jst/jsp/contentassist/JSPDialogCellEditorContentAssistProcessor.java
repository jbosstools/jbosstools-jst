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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.jdt.internal.ui.refactoring.contentassist.JavaPackageCompletionProcessor;
import org.eclipse.jface.contentassist.IContentAssistSubjectControl;
import org.eclipse.jface.contentassist.ISubjectControlContentAssistProcessor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.ui.internal.contentassist.XMLRelevanceConstants;
import org.eclipse.wst.xml.ui.internal.util.SharedXMLEditorPluginImageHelper;
import org.jboss.tools.common.kb.KbException;
import org.jboss.tools.common.kb.KbProposal;
import org.jboss.tools.common.kb.KbQuery;
import org.jboss.tools.common.kb.wtp.WtpKbConnector;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.outline.ValueHelper;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.jst.web.tld.VpeTaglibManager;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class JSPDialogCellEditorContentAssistProcessor extends JavaPackageCompletionProcessor implements ISubjectControlContentAssistProcessor {
	Properties context;
	//ValueHelper valueHelper;
	boolean isFacelets = false; 
	
	public JSPDialogCellEditorContentAssistProcessor() {}
	
	public void setContext(Properties context) {
		this.context = context;
		//valueHelper = (ValueHelper)context.get("valueHelper");
		updateFacelets();		
	}

	public ICompletionProposal[] computeCompletionProposals(IContentAssistSubjectControl contentAssistSubjectControl, int documentOffset) {
		List proposals = new ArrayList();
		
		IDocument document = contentAssistSubjectControl.getDocument();
		String text = document.get();
		addAttributeValueProposals(proposals, text, documentOffset);
		ICompletionProposal[] ps = (ICompletionProposal[])proposals.toArray(new ICompletionProposal[0]);
		return FaceletsHtmlContentAssistProcessor.getUniqProposals(ps);
	}

	//not implemented

	public IContextInformation[] computeContextInformation(IContentAssistSubjectControl contentAssistSubjectControl, int documentOffset) {
		// TODO Auto-generated method stub
		return null;
	}

	//JSPActiveContentAssistProcessor
	
	public void addAttributeValueProposals(List proposalsList, String text, int offset) {
		ValueHelper valueHelper = new ValueHelper();
		WtpKbConnector wtpKbConnector = valueHelper.getPageConnector();
		if(wtpKbConnector == null) return;
		Node node = (Node)context.get("node");
		String tagName = "" + context.getProperty("nodeName");
		String attributeName = "" + context.getProperty("attributeName");
		String currentValue = text;
		String strippedValue = null;
		String matchString = null;
		// fixups
		matchString = currentValue.substring(0, offset);
		strippedValue = currentValue;

		boolean faceletJsfTag = false;

		String htmlQuery = null;
		if(isFacelets && tagName.indexOf(':')<1 && !FaceletsHtmlContentAssistProcessor.JSFCAttributeName.equals(attributeName)) {
			Element element = (Element)node;
			String jsfTagName = valueHelper.getFaceletJsfTag(element);
			if(jsfTagName != null) {
				faceletJsfTag = true;
				tagName = jsfTagName;
				htmlQuery = new StringBuffer(KbQuery.TAG_SEPARATOR).append(FaceletsHtmlContentAssistProcessor.faceletHtmlPrefixStart + tagName).append(KbQuery.ATTRIBUTE_SEPARATOR).append(attributeName).append(KbQuery.ENUMERATION_SEPARATOR).append(matchString).toString();
			}
		}

		if(!faceletJsfTag && isFacelets && tagName.indexOf(':')<0) {
			tagName = FaceletsHtmlContentAssistProcessor.faceletHtmlPrefixStart + tagName;
		}

	    String query = new StringBuffer(KbQuery.TAG_SEPARATOR).append(tagName).append(KbQuery.ATTRIBUTE_SEPARATOR).append(attributeName).append(KbQuery.ENUMERATION_SEPARATOR).append(matchString).toString();
		if(!isFacelets && tagName.indexOf(':') < 0) {
			query = WtpKbConnector.ADD_HTML_PREFIX + query;
		}
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
                    int replacementBeginPosition = 0/*contentAssistRequest.getReplacementBeginPosition()*/ + kbProposal.getStart();
                    int replacementLength = kbProposal.getEnd() - kbProposal.getStart();
                	int cursorPositionDelta = 0;
                	int cursorPosition = kbProposal.getPosition() + cursorPositionDelta;
                	
                	Image image = kbProposal.hasImage() ? 
                				kbProposal.getImage() :  
                				SharedXMLEditorPluginImageHelper.getImage(SharedXMLEditorPluginImageHelper.IMG_OBJ_ATTRIBUTE);

                	AutoContentAssistantProposal proposal = new AutoContentAssistantProposal(kbProposal.autoActivationContentAssistantAfterApplication(), replacementString,
                			replacementBeginPosition, replacementLength, cursorPosition, 
                			image,
            				kbProposal.getLabel(), null, kbProposal.getContextInfo(), relevance);

                	proposalsList.add(proposal);
                } else {
                	StringBuffer replacementStringBuffer = new StringBuffer(kbProposal.getReplacementString());
                    int replacementBeginPosition = 0;
                	int replacementLength = text.length();
                	int cursorPositionDelta = 0;
                	String replacementString = replacementStringBuffer.toString();
                	int cursorPosition = kbProposal.getPosition() + cursorPositionDelta;
                	
                	Image image = kbProposal.hasImage() ? 
            				kbProposal.getImage() :  
            				SharedXMLEditorPluginImageHelper.getImage(SharedXMLEditorPluginImageHelper.IMG_OBJ_ATTRIBUTE);

                	AutoContentAssistantProposal proposal = new AutoContentAssistantProposal(kbProposal.autoActivationContentAssistantAfterApplication(), replacementString,
                			replacementBeginPosition, replacementLength, cursorPosition, 
                			image,
            				kbProposal.getLabel(), null, kbProposal.getContextInfo(), relevance);

                	proposalsList.add(proposal);
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

	void updateFacelets() {
		ValueHelper valueHelper = new ValueHelper();
		valueHelper.updateFacelets();
		isFacelets = valueHelper.isFacetets();
	}

}
