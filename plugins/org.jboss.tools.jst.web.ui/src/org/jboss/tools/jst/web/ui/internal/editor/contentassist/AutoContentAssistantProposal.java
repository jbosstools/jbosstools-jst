/*******************************************************************************
 * Copyright (c) 2007-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.internal.editor.contentassist;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.internal.text.html.BrowserInformationControl;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension3;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension4;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension5;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension6;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.ui.internal.contentassist.CustomCompletionProposal;
import org.eclipse.wst.sse.ui.internal.contentassist.IRelevanceConstants;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.jboss.tools.common.el.ui.internal.info.ELInfoHover;
import org.jboss.tools.common.el.ui.internal.info.Messages;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

/**
 * @author Igels
 */
@SuppressWarnings("restriction")
public class AutoContentAssistantProposal extends CustomCompletionProposal implements ICompletionProposalExtension3, ICompletionProposalExtension4, ICompletionProposalExtension5, ICompletionProposalExtension6 {
    private boolean autoContentAssistant = false;
    private IRunnableWithProgress runnable = null;
	/**
	 * The control creator.
	 */
	private IInformationControlCreator fCreator;

	public AutoContentAssistantProposal(String replacementString, int replacementOffset, int replacementLength, int cursorPosition, Image image, String displayString, IContextInformation contextInformation, String additionalProposalInfo) {
		super(replacementString, replacementOffset, replacementLength, cursorPosition, image, displayString,  contextInformation, additionalProposalInfo, IRelevanceConstants.R_NONE);
		this.fOriginalReplacementLength = replacementLength;
	}

	public AutoContentAssistantProposal(String replacementString, int replacementOffset, int replacementLength, int cursorPosition, Image image, String displayString, String alternateMatch, IContextInformation contextInformation, String additionalProposalInfo, int relevance) {
	    super(replacementString, replacementOffset, replacementLength, cursorPosition, image, displayString, alternateMatch, contextInformation, additionalProposalInfo, relevance, true);
		this.fOriginalReplacementLength = replacementLength;
	}

	public AutoContentAssistantProposal(String replacementString, int replacementOffset, int replacementLength, int cursorPosition, Image image, String displayString, IContextInformation contextInformation, String additionalProposalInfo, int relevance) {
	    super(replacementString, replacementOffset, replacementLength, cursorPosition, image, displayString, contextInformation, additionalProposalInfo, relevance);
		this.fOriginalReplacementLength = replacementLength;
	}

	public AutoContentAssistantProposal(boolean autoContentAssistant, String replacementString, int replacementOffset, int replacementLength, int cursorPosition, Image image, String displayString, IContextInformation contextInformation, String additionalProposalInfo, int relevance) {
	    super(replacementString, replacementOffset, replacementLength, cursorPosition, image, displayString, contextInformation, additionalProposalInfo, relevance);
	    this.autoContentAssistant = autoContentAssistant;
		this.fOriginalReplacementLength = replacementLength;
	}

	public AutoContentAssistantProposal(boolean autoContentAssistant, String replacementString, int replacementOffset, int replacementLength, int cursorPosition, Image image, String displayString, IContextInformation contextInformation, String additionalProposalInfo, int relevance, IRunnableWithProgress runnable) {
	    super(replacementString, replacementOffset, replacementLength, cursorPosition, image, displayString, contextInformation, additionalProposalInfo, relevance);
	    this.autoContentAssistant = autoContentAssistant;
		this.fOriginalReplacementLength = replacementLength;
		this.runnable = runnable;
	}

	public void apply(ITextViewer viewer, char trigger, int stateMask, int offset) {
		int diff = 0;
		if (runnable != null) {
			
			
			int length = viewer.getDocument().getLength();
			try {
				runnable.run(null);
			} catch (InvocationTargetException e) {
				WebUiPlugin.getPluginLog().logError(e);
			} catch (InterruptedException e) {
				WebUiPlugin.getPluginLog().logError(e);
			}
			diff = viewer.getDocument().getLength() - length;
			setReplacementOffset(getReplacementOffset() + diff);
		}
		
	    super.apply(viewer, trigger, stateMask, offset + diff);
	    if(autoContentAssistant) {
			Point selection = getSelection(viewer.getDocument());
			viewer.setSelectedRange(selection.x, selection.y);
			if(viewer instanceof ITextOperationTarget) {
				((ITextOperationTarget)viewer).doOperation(ISourceViewer.CONTENTASSIST_PROPOSALS);
			}
	    }
	}

	/**
	 * Return cursor position of proposal replacement string.
	 */
	public int getCursorPosition() {
	    int cursorPosition = -1;
		int firstAttributeEndPosition = getReplacementString().indexOf("="); //$NON-NLS-1$
		int openEndTagPosition = getReplacementString().indexOf("</"); //$NON-NLS-1$
		int closeStartAndEndTagPosition = getReplacementString().indexOf("/>"); //$NON-NLS-1$
		if(firstAttributeEndPosition>-1) {
		    cursorPosition = firstAttributeEndPosition + 2;
		} else if(openEndTagPosition>-1) {
		    cursorPosition = openEndTagPosition;
		} else if(closeStartAndEndTagPosition>-1) {
		    cursorPosition = closeStartAndEndTagPosition;
		}
		return cursorPosition>-1?cursorPosition:super.getCursorPosition();
	}

	
	StyledString fStyledDisplayString;
	
	public String getDisplayString() {
		if (super.getDisplayString() != null)
			return super.getDisplayString();
		else {
			if (super.getReplacementString() != null) { 
				setDisplayString(super.getReplacementString());
				return super.getDisplayString();
			}
		}
		return ""; //$NON-NLS-1$
	}
	
	public void setDisplayString(String string) {
		super.setDisplayString(string);
		
		boolean isJavaWordPart = string.length() > 0  && Character.isJavaIdentifierPart(string.charAt(0));
		boolean hasRetType = isJavaWordPart && string.indexOf(':') > 0;
		boolean hasDeclType = isJavaWordPart && string.lastIndexOf('-') > 0;
		
//		int p1i = string.indexOf(':');
		int p2i = string.lastIndexOf('-');
		
		String p1 = hasRetType && hasDeclType ? string.substring(0, p2i) : string;
		String p2 = string.substring(p1.length());
		
		StyledString styledString = new StyledString();

		// name, attrs, type
		styledString.append(p1);

		// decl type
		if (p2 != null && p2.length() > 0) 
			styledString.append(p2, StyledString.QUALIFIER_STYLER);
		fStyledDisplayString = styledString; 
	}
	
	public StyledString getStyledDisplayString() {
		if (fStyledDisplayString == null) {
			setDisplayString(super.getDisplayString()); // This re-creates Styled Display String
		}
		return fStyledDisplayString;
	}
	
	public void setStyledDisplayString(StyledString text) {
		fStyledDisplayString = text;
		super.setDisplayString(fStyledDisplayString == null ? "" : fStyledDisplayString.getString()); //$NON-NLS-1$
	}

	private int fOriginalReplacementLength;

	/**
	 * Returns true in case of the region at specified offset is a node name region
	 * 
	 * @param document
	 * @param offset
	 * @return
	 */
	private boolean isTagName(IDocument document, int offset) {
		if (!(document instanceof IStructuredDocument)) 
			return false;
		
		int lastOffset = offset;
		IStructuredDocumentRegion sdRegion = ((IStructuredDocument)document).getRegionAtCharacterOffset(offset);
		while (sdRegion == null && lastOffset >= 0) {
			lastOffset--;
			sdRegion = ((IStructuredDocument)document).getRegionAtCharacterOffset(lastOffset);
		}
		
		ITextRegion region = sdRegion == null ? null : sdRegion.getRegionAtCharacterOffset(offset);
		
		return DOMRegionContext.XML_TAG_NAME.equals(region == null ? null : region.getType());
	}

	// Fix for JBIDE-5125 >>>
	@Override
	public boolean validate(IDocument document, int offset, DocumentEvent event) {
		int fReplacementOffset = getReplacementOffset();

		if (offset < fReplacementOffset)
			return false;
		boolean validated = startsWith(document, offset, getReplacementString());
		if (!validated && isTagName(document, fReplacementOffset) && getReplacementString() != null && getReplacementString().indexOf(":") != -1) { //$NON-NLS-1$
			String replacementString = getReplacementString().substring(getReplacementString().indexOf(":") + 1); //$NON-NLS-1$
			validated = startsWith(document, offset, replacementString);
		}
		if (!validated && getReplacementString() != null && getReplacementString().startsWith("#{")) { //$NON-NLS-1$
			String replacementString = getReplacementString().substring(getReplacementString().indexOf("#{") + 2); //$NON-NLS-1$
			validated = startsWith(document, offset, replacementString);
		}
		// it would be better to use "originalCursorPosition" instead of
		// getReplacementOffset(), but we don't have that info.
		int newLength = offset - getReplacementOffset();
		int delta = newLength - fOriginalReplacementLength;
		int newReplacementLength = delta + fOriginalReplacementLength;
		setReplacementLength(newReplacementLength);

		// if it's an attribute value, replacement offset is
		// going to be one off from the actual cursor offset...
		try {
			char firstChar = document.getChar(getReplacementOffset());
			if (firstChar == '"' || firstChar == '\'') {
				setReplacementLength(getReplacementLength() + 1);
			}
		}
		catch (BadLocationException e) {
			// just don't increment
		}
		return validated;
	}
	// Fix for JBIDE-5125 <<<

	public boolean isAutoInsertable() {
		return false;
	}
	
	public IInformationControlCreator getInformationControlCreator() {
		final boolean[] browserInformationControlAvailable = new boolean[] {false};
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				Shell shell= WebUiPlugin.getActiveWorkbenchShell();
				browserInformationControlAvailable[0] = (shell != null && BrowserInformationControl.isAvailable(shell));
			}
		});
		
		if (!browserInformationControlAvailable[0])
			return null;

		if (fCreator == null) {
			ELInfoHover.PresenterControlCreator presenterControlCreator= new ELInfoHover.PresenterControlCreator(WebUiPlugin.getSite());
			fCreator= new ELInfoHover.HoverControlCreator(presenterControlCreator, Messages.additionalInfo_affordance);
		}
		return fCreator;
	}

	public CharSequence getPrefixCompletionText(IDocument document,
			int completionOffset) {
		return getReplacementString();
	}

	public int getPrefixCompletionStart(IDocument document, int completionOffset) {
		return getReplacementOffset();
	}

	@Override
	public String getAdditionalProposalInfo() {
		Object info= getAdditionalProposalInfo(new NullProgressMonitor());
		return info == null ? null : info.toString();
	}

	/*
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposalExtension5#getAdditionalProposalInfo(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public Object getAdditionalProposalInfo(IProgressMonitor monitor) {
		return ELInfoHover.getHoverInfo(super.getAdditionalProposalInfo(), monitor);
	}
}