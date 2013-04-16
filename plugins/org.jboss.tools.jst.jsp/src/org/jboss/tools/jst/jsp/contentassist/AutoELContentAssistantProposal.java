/******************************************************************************* 
 * Copyright (c) 2010-2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.jsp.contentassist;

import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.text.javadoc.JavadocContentAccess2;
import org.eclipse.jdt.internal.ui.viewsupport.JavaElementLinks;
import org.eclipse.jdt.ui.JavaElementLabels;
import org.eclipse.jface.internal.text.html.HTMLPrinter;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.common.el.core.ca.MessagesELTextProposal;
import org.jboss.tools.common.el.ui.ca.ELProposalProcessor;
import org.jboss.tools.common.model.XModelObject;

/**
 * Class to provide EL proposals to Content Assistant. The main purpose is to
 * provide correct additional proposal information based on IJavaElement objects
 * collected for the proposal.
 * 
 * @author Victor Rubezhny
 */
@SuppressWarnings("restriction")
public class AutoELContentAssistantProposal extends
		AutoContentAssistantProposal {
	private IJavaElement[] fJavaElements;
	private MessagesELTextProposal fProperySource;
	private String fAdditionalProposalInfo;

	/**
	 * Constructs the proposal object
	 * 
	 * @param replacementString
	 * @param replacementOffset
	 * @param replacementLength
	 * @param cursorPosition
	 * @param image
	 * @param displayString
	 * @param contextInformation
	 * @param elements
	 * @param relevance
	 */
	public AutoELContentAssistantProposal(String replacementString,
			int replacementOffset, int replacementLength, int cursorPosition,
			Image image, String displayString, String alternateMatch,
			IContextInformation contextInformation, IJavaElement[] elements,
			int relevance) {
		super(replacementString, replacementOffset, replacementLength,
				cursorPosition, image, displayString, alternateMatch,
				contextInformation, null, relevance);
		this.fJavaElements = elements;
		this.fProperySource = null;
	}

	/**
	 * Constructs the proposal object
	 * 
	 * @param replacementString
	 * @param replacementOffset
	 * @param replacementLength
	 * @param cursorPosition
	 * @param image
	 * @param displayString
	 * @param contextInformation
	 * @param properySource
	 * @param relevance
	 */
	public AutoELContentAssistantProposal(String replacementString,
			int replacementOffset, int replacementLength, int cursorPosition,
			Image image, String displayString, String alternateMatch,
			IContextInformation contextInformation,
			MessagesELTextProposal propertySource, int relevance) {
		super(replacementString, replacementOffset, replacementLength,
				cursorPosition, image, displayString, alternateMatch,
				contextInformation, null, relevance);
		this.fJavaElements = null;
		this.fProperySource = propertySource;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.sse.ui.internal.contentassist.CustomCompletionProposal
	 * #getAdditionalProposalInfo()
	 */
	public String getAdditionalProposalInfo() {
		if (fAdditionalProposalInfo == null) {
			if (this.fJavaElements != null && this.fJavaElements.length > 0) {
				Arrays.sort(fJavaElements, ELProposalProcessor.CASE_INSENSITIVE_ORDER);
				this.fAdditionalProposalInfo = extractProposalContextInfo(fJavaElements);
			} else if (fProperySource != null) {
				this.fAdditionalProposalInfo = extractProposalContextInfo(fProperySource);
			}
		}
		return fAdditionalProposalInfo;
	}


	/*
	 * Extracts the additional proposal information based on Javadoc for the
	 * stored IJavaElement objects
	 */
	private String extractProposalContextInfo(IJavaElement[] elements) {
		int nResults = elements.length;
		StringBuffer buffer = new StringBuffer();
		boolean hasContents = false;
		IJavaElement element = null;

		if (nResults > 1) {
			/*
			 * for (int i= 0; i < elements.length; i++) { if (elements[i] ==
			 * null) continue; if (elements[i] instanceof IMember ||
			 * elements[i].getElementType() == IJavaElement.LOCAL_VARIABLE ||
			 * elements[i].getElementType() == IJavaElement.TYPE_PARAMETER) {
			 * buffer
			 * .append('\uE467').append(' ').append(getInfoText(elements[i]));
			 * hasContents= true; } buffer.append("<br/>"); //$NON-NLS-1$ }
			 */
			for (int i = 0; i < elements.length; i++) {
				if (elements[i] == null)
					continue;
				if (elements[i] instanceof IMember
						|| elements[i].getElementType() == IJavaElement.LOCAL_VARIABLE
						|| elements[i].getElementType() == IJavaElement.TYPE_PARAMETER) {
					buffer.append('\uE467').append(' ');
					addFullInfo(buffer, elements[i]);
					buffer.append("<br/>"); //$NON-NLS-1$
					hasContents = true;
				}
			}
		} else {
			element = elements[0];
			if (element instanceof IMember
					|| element.getElementType() == IJavaElement.LOCAL_VARIABLE
					|| element.getElementType() == IJavaElement.TYPE_PARAMETER) {
				addFullInfo(buffer, element);
				hasContents = true;
			}
		}

		if (!hasContents || buffer.length() == 0)
			return null;

		HTMLPrinter.insertPageProlog(buffer, 0, (String) null);
		HTMLPrinter.addPageEpilog(buffer);
		return buffer.toString();
	}

	/*
	 * Extracts the additional proposal information based on Javadoc for the
	 * stored IJavaElement objects
	 */
	private String extractProposalContextInfo(
			MessagesELTextProposal propertySource) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(ELProposalProcessor.getELMessagesHoverInternal(
				propertySource.getBaseName(), propertySource.getPropertyName(),
				(List<XModelObject>) propertySource.getAllObjects()));

		if (buffer.length() == 0)
			return null;

		HTMLPrinter.insertPageProlog(buffer, 0, (String) null);
		HTMLPrinter.addPageEpilog(buffer);
		return buffer.toString();
	}

	private static final long LABEL_FLAGS = JavaElementLabels.ALL_FULLY_QUALIFIED
			| JavaElementLabels.M_PRE_RETURNTYPE
			| JavaElementLabels.M_PARAMETER_TYPES
			| JavaElementLabels.M_PARAMETER_NAMES
			| JavaElementLabels.M_EXCEPTIONS
			| JavaElementLabels.F_PRE_TYPE_SIGNATURE
			| JavaElementLabels.M_PRE_TYPE_PARAMETERS
			| JavaElementLabels.T_TYPE_PARAMETERS
			| JavaElementLabels.USE_RESOLVED;
	private static final long LOCAL_VARIABLE_FLAGS = LABEL_FLAGS
			& ~JavaElementLabels.F_FULLY_QUALIFIED
			| JavaElementLabels.F_POST_QUALIFIED;
	private static final long TYPE_PARAMETER_FLAGS = LABEL_FLAGS
			| JavaElementLabels.TP_POST_QUALIFIED;

	/*
	 * Returns the label for the IJavaElement objects
	 */
	private String getInfoText(IJavaElement element) {
		long flags;
		switch (element.getElementType()) {
		case IJavaElement.LOCAL_VARIABLE:
			flags = LOCAL_VARIABLE_FLAGS;
			break;
		case IJavaElement.TYPE_PARAMETER:
			flags = TYPE_PARAMETER_FLAGS;
			break;
		default:
			flags = LABEL_FLAGS;
			break;
		}
		StringBuffer label = new StringBuffer(JavaElementLinks.getElementLabel(
				element, flags));

		// The following lines were commented out because of JBIDE-8923 faced in
		// Eclipse 3.7
		//
		// StringBuffer buf= new StringBuffer();
		//		buf.append("<span style='word-wrap:break-word;'>"); //$NON-NLS-1$
		// buf.append(label);
		//		buf.append("</span>"); //$NON-NLS-1$

		// return buf.toString();
		return label.toString();
	}

	/*
	 * Adds full information to the additional proposal information
	 * 
	 * @param buffer
	 * 
	 * @param element
	 * 
	 * @return
	 */
	private void addFullInfo(StringBuffer buffer, IJavaElement element) {
		if (element instanceof IMember) {
			IMember member = (IMember) element;
			HTMLPrinter.addSmallHeader(buffer, getInfoText(member));
			Reader reader = null;
			try {
				String content = JavadocContentAccess2.getHTMLContent(member,
						true);
				reader = content == null ? null : new StringReader(content);
			} catch (JavaModelException ex) {
				JavaPlugin.log(ex);
			}

			if (reader == null) {
				reader = new StringReader(Messages.NO_JAVADOC);
			}

			if (reader != null) {
				buffer.append("<br/>"); //$NON-NLS-1$
				buffer.append(HTMLPrinter.read(reader));
				// HTMLPrinter.addParagraph(buffer, reader);
			}

		} else if (element.getElementType() == IJavaElement.LOCAL_VARIABLE
				|| element.getElementType() == IJavaElement.TYPE_PARAMETER) {
			HTMLPrinter.addSmallHeader(buffer, getInfoText(element));
		}
	}

	/**
	 * Return cursor position of proposal replacement string.
	 * 
	 * Method is added because of JBIDE-7168
	 */
	public int getCursorPosition() {
		int cursorPosition = -1;

		int openingQuoteInReplacement = getReplacementString().lastIndexOf('(');
		int closingQuoteInReplacement = getReplacementString().lastIndexOf(')');
		int openingQuoteInDisplay = getDisplayString().lastIndexOf('(');
		int closingQuoteInDisplay = getDisplayString().lastIndexOf(')');

		if (openingQuoteInReplacement != -1
				&& closingQuoteInReplacement != -1
				&& openingQuoteInDisplay != -1
				&& closingQuoteInDisplay != -1
				&& (closingQuoteInReplacement - openingQuoteInReplacement) != (closingQuoteInDisplay - openingQuoteInDisplay)) {
			cursorPosition = openingQuoteInReplacement + 1;
		}

		return cursorPosition > -1 ? cursorPosition : super.getCursorPosition();
	}
}
