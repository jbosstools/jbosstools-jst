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
	ValueHelper valueHelper;
	boolean isFacelets = false; 
	
	public JSPDialogCellEditorContentAssistProcessor() {}
	
	public void setContext(Properties context) {
		this.context = context;
		valueHelper = (ValueHelper)context.get("valueHelper");
		updateFacelets();		
	}

	public ICompletionProposal[] computeCompletionProposals(IContentAssistSubjectControl contentAssistSubjectControl, int documentOffset) {
		List proposals = new ArrayList();
		
		IDocument document = contentAssistSubjectControl.getDocument();
		String text = document.get();
		addAttributeValueProposals(proposals, text, documentOffset);
		return (ICompletionProposal[])proposals.toArray(new ICompletionProposal[0]);
	}

	//not implemented

	public IContextInformation[] computeContextInformation(IContentAssistSubjectControl contentAssistSubjectControl, int documentOffset) {
		// TODO Auto-generated method stub
		return null;
	}

	//JSPActiveContentAssistProcessor
	
	public void addAttributeValueProposals(List proposalsList, String text, int offset) {
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
		if(isFacelets && tagName.indexOf(':')<1 && !RedHatHtmlContentAssistProcessor.JSFCAttributeName.equals(attributeName)) {
			Element element = (Element)node;

			NamedNodeMap attributes = element.getAttributes();
			Node jsfC = attributes.getNamedItem(RedHatHtmlContentAssistProcessor.JSFCAttributeName);
			if(jsfC != null && (jsfC instanceof Attr)) {
				Attr jsfCAttribute = (Attr)jsfC;
				String jsfTagName = jsfCAttribute.getValue();
				if(jsfTagName != null && jsfTagName.indexOf(':') > 0) {
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
                    int replacementBeginPosition = 0/*contentAssistRequest.getReplacementBeginPosition()*/ + kbProposal.getStart();
                    int replacementLength = kbProposal.getEnd() - kbProposal.getStart();
                	int cursorPositionDelta = 0;
                	int cursorPosition = kbProposal.getPosition() + cursorPositionDelta;
                	RedHatCustomCompletionProposal proposal = new RedHatCustomCompletionProposal(kbProposal.autoActivationContentAssistantAfterApplication(), replacementString,
                			replacementBeginPosition, replacementLength, cursorPosition, SharedXMLEditorPluginImageHelper.getImage(SharedXMLEditorPluginImageHelper.IMG_OBJ_ATTRIBUTE),
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
		if(tagName.startsWith(RedHatHtmlContentAssistProcessor.faceletHtmlPrefixStart)) {
			return tagName.substring(RedHatHtmlContentAssistProcessor.faceletHtmlPrefixStart.length());
		}
		return tagName;
	}

	void updateFacelets() {
		VpeTaglibManager tldManager = valueHelper.getTaglibManager();
		if(tldManager == null) return;
		List list = tldManager.getTagLibs();
		if(list == null) return;
		isFacelets = false;
		for(int i = 0; i < list.size(); i++) {
			TaglibData data = (TaglibData)list.get(i);
			isFacelets = isFacelets || data.getUri().equals(RedHatHtmlContentAssistProcessor.faceletUri);
		}
	}

}
