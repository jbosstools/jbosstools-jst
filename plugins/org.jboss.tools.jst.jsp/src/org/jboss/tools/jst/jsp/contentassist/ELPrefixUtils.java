/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.jsp.contentassist;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.ui.contentassist.CompletionProposalInvocationContext;
import org.eclipse.wst.sse.ui.internal.contentassist.ContentAssistUtils;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.jboss.tools.common.el.core.model.ELInstance;
import org.jboss.tools.common.el.core.model.ELInvocationExpression;
import org.jboss.tools.common.el.core.model.ELModel;
import org.jboss.tools.common.el.core.model.ELUtil;
import org.jboss.tools.common.el.core.parser.ELParser;
import org.jboss.tools.common.el.core.parser.ELParserUtil;
import org.jboss.tools.common.text.xml.contentassist.ProposalSorter.IProposalFilter;

/**
 * Utility functions for calculate EL Text Regions
 * 
 * @author Victor V. Rubezhny
 */
public class ELPrefixUtils {
	public static final IProposalFilter EL_PROPOSAL_FILTER = new IProposalFilter() {
		
		@Override
		public boolean isValidProposal(CompletionProposalInvocationContext context,
				ICompletionProposal proposal) {
			
			ELTextRegion prefix = getELPrefix(context);
			if (prefix == null || !prefix.isELStarted() || prefix.isInsideELStartToken()) 
				return true; // Any proposal allowed
			
			return (proposal instanceof AutoELContentAssistantProposal);
		}
	};
	
	/**
	 * Returns EL Prefix Text Region Information Object
	 * 
	 * @return
	 */
	public static ELTextRegion getELPrefix(CompletionProposalInvocationContext context) {
		IStructuredDocumentRegion sdRegion = ContentAssistUtils.getStructuredDocumentRegion(context.getViewer(), context.getInvocationOffset());
		if (sdRegion == null)
			return null;
		
		ITextRegion region = sdRegion.getRegionAtCharacterOffset(context.getInvocationOffset());
		if (region == null)
			return null;

		String text = sdRegion.getFullText(region);
		int startOffset = sdRegion.getStartOffset() + region.getStart();
		
		boolean isAttributeValue = false;
		boolean hasOpenQuote = false;
		boolean hasCloseQuote = false;
		char quoteChar = (char)0;
		if (DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE.equals(region.getType())) {
			isAttributeValue = true;
			if (text.startsWith("\"") || text.startsWith("'")) {//$NON-NLS-1$ //$NON-NLS-2$
				quoteChar = text.charAt(0);
				hasOpenQuote = true;
			}
			if (hasOpenQuote && text.endsWith(String.valueOf(quoteChar))) {
				hasCloseQuote = true;
			}
		}
		
		int inValueOffset = context.getInvocationOffset() - startOffset;
		if (text != null && text.length() < inValueOffset) { // probably, the attribute value ends before the document position
			return null;
		}
		if (inValueOffset<0) {
			return null;
		}
		
		ELParser p = ELParserUtil.getJbossFactory().createParser();
		ELModel model = p.parse(text);
		
		ELInstance is = ELUtil.findInstance(model, inValueOffset);// ELInstance
		ELInvocationExpression ie = ELUtil.findExpression(model, inValueOffset);// ELExpression
		
		boolean isELStarted = (model != null && is != null && (model.toString().startsWith("#{") ||  //$NON-NLS-1$
				model.toString().startsWith("${"))); //$NON-NLS-1$
		boolean isELClosed = (model != null && is != null && model.toString().endsWith("}")); //$NON-NLS-1$
		
		ELTextRegion tr = new ELTextRegion(startOffset,  ie == null ? inValueOffset : ie.getStartPosition(), 
				ie == null ? 0 : ie.getLength(), ie == null ? "" : ie.getText(),  //$NON-NLS-1$ 
				isELStarted, isELClosed,
				isAttributeValue, hasOpenQuote, hasCloseQuote, quoteChar);
		
		return tr;
	}
	
	public static class ELTextRegion {
		private int startOffset;
		private int offset;
		private int length;
		private String text;
		private boolean isELStarted;
		private boolean isInsideELStartToken;
		private boolean isELClosed;
		private boolean isAttributeValue;
		private boolean hasOpenQuote;
		private boolean hasCloseQuote;
		private char quoteChar;
		
		public ELTextRegion(int startOffset, int offset, int length, String text, boolean isELStarted, boolean isELClosed) {
			this(startOffset, offset, length, text, isELStarted, isELClosed, false, false, false, (char)0);
		}

		public ELTextRegion(int startOffset, int offset, int length, String text, boolean isELStarted, boolean isELClosed,
				boolean isAttributeValue, boolean hasOpenQuote, boolean hasCloseQuote, char quoteChar) {
			this.startOffset = startOffset;
			this.offset = offset;
			this.length = length;
			this.text = text;
			this.isELStarted = isELStarted;
			this.isELClosed = isELClosed;
			this.isAttributeValue = isAttributeValue;
			this.hasOpenQuote = hasOpenQuote;
			this.hasCloseQuote = hasCloseQuote;
			this.quoteChar = quoteChar;
		}
		
		public int getStartOffset() {
			return startOffset;
		}
		
		public int getOffset() {
			return offset;
		}
		
		public int getLength() {
			return length;
		}
		
		public String getText() {
			StringBuffer sb = new StringBuffer(length);
			sb = sb.append(text.substring(0, length));
			sb.setLength(length);
			return sb.toString();
		}
		
		public boolean isELStarted() {
			return isELStarted;
		}
	
		public boolean isInsideELStartToken() {
			return isInsideELStartToken;
		}

		public void setInsideELStartToken(boolean b) {
			isInsideELStartToken = b;
		}

		public boolean isELClosed() {
			return isELClosed;
		}

		public boolean isAttributeValue() {
			return isAttributeValue;
		}

		public char getQuoteChar() {
			return quoteChar;
		}

		public boolean hasOpenQuote() {
			return hasOpenQuote;
		}

		public boolean hasCloseQuote() {
			return hasCloseQuote;
		}
	}
}
