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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.actions.OpenBrowserUtil;
import org.eclipse.jdt.internal.ui.actions.SimpleSelectionProvider;
import org.eclipse.jdt.internal.ui.infoviews.JavadocView;
import org.eclipse.jdt.internal.ui.text.java.hover.JavadocBrowserInformationControlInput;
import org.eclipse.jdt.internal.ui.text.java.hover.JavadocHover;
import org.eclipse.jdt.internal.ui.viewsupport.BasicElementLabels;
import org.eclipse.jdt.internal.ui.viewsupport.JavaElementLinks;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.actions.OpenAttachedJavadocAction;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.internal.text.html.BrowserInformationControl;
import org.eclipse.jface.internal.text.html.BrowserInformationControlInput;
import org.eclipse.jface.internal.text.html.BrowserInput;
import org.eclipse.jface.internal.text.html.HTMLPrinter;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.AbstractReusableInformationControlCreator;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IInformationControlExtension4;
import org.eclipse.jface.text.IInputChangedListener;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension3;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension4;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension5;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension6;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.ui.internal.contentassist.CustomCompletionProposal;
import org.eclipse.wst.sse.ui.internal.contentassist.IRelevanceConstants;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.internal.editor.format.HTMLAnchorProcessor;
import org.osgi.framework.Bundle;

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
		Shell shell= WebUiPlugin.getActiveWorkbenchShell();
		if (shell == null || !BrowserInformationControl.isAvailable(shell))
			return null;

		if (fCreator == null) {
			PresenterControlCreator presenterControlCreator= new PresenterControlCreator(WebUiPlugin.getSite());
			fCreator= new HoverControlCreator(presenterControlCreator);
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
		if (super.getAdditionalProposalInfo() != null) {
			String info= super.getAdditionalProposalInfo();
			if (info != null && info.length() > 0) {
				StringBuffer buffer= new StringBuffer();
				HTMLPrinter.insertPageProlog(buffer, 0, getCSSStyles());
				buffer.append(info.trim());
				HTMLPrinter.addPageEpilog(buffer);
				
				return new ELBrowserInformationControlInput(null, null, processLinks(buffer.toString()), 0);
			}
		}
		return null;
	}
	
	private static final String[] PROTOCOLS = {
		JavaElementLinks.JAVADOC_VIEW_SCHEME,
		JavaElementLinks.JAVADOC_SCHEME,
		JavaElementLinks.OPEN_LINK_SCHEME,
		"http", //$NON-NLS-1$
		"https" //$NON-NLS-1$
	};
	
	protected String processLinks(String html) {
		if (html == null)
			return ""; //$NON-NLS-1$
		String text;
		try {
			HTMLAnchorProcessor reader= new HTMLAnchorProcessor(new StringReader(html), PROTOCOLS);
			text= reader.getString();
			reader.close();
		} catch (IOException e) {
			text= ""; //$NON-NLS-1$
		}
		return text;
	}

	private static String fCSSStyles;
	
	/**
	 * Returns the style information for displaying HTML content.
	 *
	 * @return the CSS styles
	 */
	protected String getCSSStyles() {
		if (fCSSStyles == null) {
			Bundle bundle= Platform.getBundle(WebUiPlugin.PLUGIN_ID);
			URL url= bundle.getEntry("/resources/css/AutoContentAssistantProposalHoverStyleSheet.css"); //$NON-NLS-1$
			if (url != null) {
				BufferedReader reader= null;
				try {
					url= FileLocator.toFileURL(url);
					reader= new BufferedReader(new InputStreamReader(url.openStream()));
					StringBuilder buffer= new StringBuilder(200);
					String line= reader.readLine();
					while (line != null) {
						buffer.append(line);
						buffer.append('\n');
						line= reader.readLine();
					}
					fCSSStyles= buffer.toString();
				} catch (IOException ex) {
					WebUiPlugin.getPluginLog().logError(ex);
				} finally {
					try {
						if (reader != null)
							reader.close();
					} catch (IOException e) {
					}
				}

			}
		}
		String css= fCSSStyles;
		if (css != null) {
			FontData fontData= JFaceResources.getFontRegistry().getFontData(JFaceResources.DIALOG_FONT)[0];
			css= HTMLPrinter.convertTopLevelFont(css, fontData);
		}
		return css;
	}

	
	/**
	 * Hover control creator.
	 */
	public static final class HoverControlCreator extends AbstractReusableInformationControlCreator {
		/**
		 * The information presenter control creator.
		 */
		private final IInformationControlCreator fInformationPresenterControlCreator;

		/**
		 * @param informationPresenterControlCreator control creator for enriched hover
		 */
		public HoverControlCreator(IInformationControlCreator informationPresenterControlCreator) {
			fInformationPresenterControlCreator= informationPresenterControlCreator;
		}

		/*
		 * @see org.eclipse.jdt.internal.ui.text.java.hover.AbstractReusableInformationControlCreator#doCreateInformationControl(org.eclipse.swt.widgets.Shell)
		 */
		@Override
		public IInformationControl doCreateInformationControl(Shell parent) {
			if (BrowserInformationControl.isAvailable(parent)) {
				BrowserInformationControl iControl = new BrowserInformationControl(parent, null, 
								Messages.JspEditorPlugin_additionalInfo_affordance) {
					/*
					 * @see org.eclipse.jface.text.IInformationControlExtension5#getInformationPresenterControlCreator()
					 */
					@Override
					public IInformationControlCreator getInformationPresenterControlCreator() {
						return fInformationPresenterControlCreator;
					}
				};
				addLinkListener(iControl);
				return iControl;
			} else {
				return new DefaultInformationControl(parent, Messages.JspEditorPlugin_additionalInfo_affordance);
			}
		}

		/*
		 * @see org.eclipse.jdt.internal.ui.text.java.hover.AbstractReusableInformationControlCreator#canReuse(org.eclipse.jface.text.IInformationControl)
		 */
		@Override
		public boolean canReuse(IInformationControl control) {
			if (!super.canReuse(control))
				return false;

			if (control instanceof IInformationControlExtension4) {
				((IInformationControlExtension4)control).setStatusText(Messages.JspEditorPlugin_additionalInfo_affordance);
			}

			return true;
		}
	}

	
	/**
	 * Presenter control creator.
	 */
	public static final class PresenterControlCreator extends AbstractReusableInformationControlCreator {
		private final IWorkbenchSite fSite;

		/**
		 * Creates a new PresenterControlCreator.
		 * 
		 * @param site the site or <code>null</code> if none
		 */
		public PresenterControlCreator(IWorkbenchSite site) {
			fSite= site;
		}

		/*
		 * @see org.eclipse.jdt.internal.ui.text.java.hover.AbstractReusableInformationControlCreator#doCreateInformationControl(org.eclipse.swt.widgets.Shell)
		 */
		@Override
		public IInformationControl doCreateInformationControl(Shell parent) {
			if (BrowserInformationControl.isAvailable(parent)) {
				ToolBarManager tbm= new ToolBarManager(SWT.FLAT);
				BrowserInformationControl iControl= new BrowserInformationControl(parent, null, tbm);

				final BackAction backAction= new BackAction(iControl);
				backAction.setEnabled(false);
				tbm.add(backAction);
				final ForwardAction forwardAction= new ForwardAction(iControl);
				tbm.add(forwardAction);
				forwardAction.setEnabled(false);

				final ShowInJavadocViewAction showInJavadocViewAction= new ShowInJavadocViewAction(iControl);
				tbm.add(showInJavadocViewAction);
				final OpenDeclarationAction openDeclarationAction= new OpenDeclarationAction(iControl);
				tbm.add(openDeclarationAction);

				final SimpleSelectionProvider selectionProvider= new SimpleSelectionProvider();
				if (fSite != null) {
					OpenAttachedJavadocAction openAttachedJavadocAction= new OpenAttachedJavadocAction(fSite);
					openAttachedJavadocAction.setSpecialSelectionProvider(selectionProvider);
					openAttachedJavadocAction.setImageDescriptor(JavaPluginImages.DESC_ELCL_OPEN_BROWSER);
					openAttachedJavadocAction.setDisabledImageDescriptor(JavaPluginImages.DESC_DLCL_OPEN_BROWSER);
					selectionProvider.addSelectionChangedListener(openAttachedJavadocAction);
					selectionProvider.setSelection(new StructuredSelection());
					tbm.add(openAttachedJavadocAction);
				}
				
				IInputChangedListener inputChangeListener= new IInputChangedListener() {
					public void inputChanged(Object newInput) {
						backAction.update();
						forwardAction.update();
						if (newInput == null) {
							selectionProvider.setSelection(new StructuredSelection());
						} else if (newInput instanceof BrowserInformationControlInput) {
							BrowserInformationControlInput input= (BrowserInformationControlInput) newInput;
							Object inputElement= input.getInputElement();
							selectionProvider.setSelection(new StructuredSelection(inputElement));
							boolean isJavaElementInput= inputElement instanceof IJavaElement;
							showInJavadocViewAction.setEnabled(isJavaElementInput);
							openDeclarationAction.setEnabled(isJavaElementInput);
						}
					}
				};
				iControl.addInputChangeListener(inputChangeListener);

				tbm.update(true);

				addLinkListener(iControl);
				return iControl;

			} else {
				return new DefaultInformationControl(parent, true);
			}
		}
	}

	private static void addLinkListener(final BrowserInformationControl control) {
		control.addLocationListener(new LocationListener() {
			@Override
			public void changing(LocationEvent event) {
				String loc= event.location;

				if ("about:blank".equals(loc)) { //$NON-NLS-1$
					return;
				}

				event.doit= false;

				if (loc.startsWith("about:")) { //$NON-NLS-1$
					// Relative links should be handled via head > base tag.
					// If no base is available, links just won't work.
					return;
				}

				URI uri= null;
				try {
					uri= new URI(loc);
				} catch (URISyntaxException e) {
					WebUiPlugin.getPluginLog().logError(e);
					return;
				}

				String scheme= uri == null ? null : uri.getScheme();
				if (JavaElementLinks.JAVADOC_VIEW_SCHEME.equals(scheme)) {
					IJavaElement linkTarget= JavaElementLinks.parseURI(uri);
					if (linkTarget == null)
						return;

					handleJavadocViewLink(linkTarget);
				} else if (JavaElementLinks.JAVADOC_SCHEME.equals(scheme)) {
					IJavaElement linkTarget= JavaElementLinks.parseURI(uri);
					if (linkTarget == null)
						return;

					handleInlineJavadocLink(linkTarget);
				} else if (JavaElementLinks.OPEN_LINK_SCHEME.equals(scheme)) {
					IJavaElement linkTarget= JavaElementLinks.parseURI(uri);
					if (linkTarget == null)
						return;

					handleDeclarationLink(linkTarget);
				} else if (scheme != null && (scheme.toLowerCase().equalsIgnoreCase("http") || //$NON-NLS-1$
						scheme.toLowerCase().equalsIgnoreCase("https"))) { //$NON-NLS-1$
					try {
						if (handleExternalLink(new URL(loc), event.display, true))
							return;
	
						event.doit= true;
					} catch (MalformedURLException e) {
						WebUiPlugin.getPluginLog().logError(e);
					}
				}
			}

			/* (non-Javadoc)
			 * @see org.eclipse.jdt.internal.ui.viewsupport.JavaElementLinks.ILinkHandler#handleJavadocViewLink(org.eclipse.jdt.core.IJavaElement)
			 */
			public void handleJavadocViewLink(IJavaElement linkTarget) {
				control.notifyDelayedInputChange(null);
				control.setVisible(false);
				control.dispose();
				try {
					JavadocView view= (JavadocView) JavaPlugin.getActivePage().showView(JavaUI.ID_JAVADOC_VIEW);
					view.setInput(linkTarget);
				} catch (PartInitException e) {
					JavaPlugin.log(e);
				}
			}

			/* (non-Javadoc)
			 * @see org.eclipse.jdt.internal.ui.viewsupport.JavaElementLinks.ILinkHandler#handleInlineJavadocLink(org.eclipse.jdt.core.IJavaElement)
			 */
			public void handleInlineJavadocLink(IJavaElement linkTarget) {
				JavadocBrowserInformationControlInput hoverInfo= JavadocHover.getHoverInfo(new IJavaElement[] { linkTarget }, null, null, (JavadocBrowserInformationControlInput) control.getInput());
				if (control.hasDelayedInputChangeListener())
					control.notifyDelayedInputChange(hoverInfo);
				else
					control.setInput(hoverInfo);
			}

			/* (non-Javadoc)
			 * @see org.eclipse.jdt.internal.ui.viewsupport.JavaElementLinks.ILinkHandler#handleDeclarationLink(org.eclipse.jdt.core.IJavaElement)
			 */
			public void handleDeclarationLink(IJavaElement linkTarget) {
				control.notifyDelayedInputChange(null);
				control.dispose(); //FIXME: should have protocol to hide, rather than dispose
				try {
					JavadocHover.openDeclaration(linkTarget);
				} catch (PartInitException e) {
					JavaPlugin.log(e);
				} catch (JavaModelException e) {
					JavaPlugin.log(e);
				}
			}

			boolean handleExternalLink(URL url, Display display, boolean openInExternalBrowser) {
				control.notifyDelayedInputChange(null);
				control.dispose();

				// Open attached Javadoc links
				if (openInExternalBrowser)
					OpenBrowserUtil.openExternal(url, display);
				else 
					OpenBrowserUtil.open(url, display);
				
				return true;
			}

			public void changed(LocationEvent event) {
			}
		});
	}

	/**
	 * Action to go back to the previous input in the hover control.
	 *
	 * @since 3.4
	 */
	private static final class BackAction extends Action {
		private final BrowserInformationControl fInfoControl;

		public BackAction(BrowserInformationControl infoControl) {
			fInfoControl= infoControl;
			setText(Messages.JavadocHover_back);
			ISharedImages images= PlatformUI.getWorkbench().getSharedImages();
			setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_BACK));
			setDisabledImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_BACK_DISABLED));

			update();
		}

		@Override
		public void run() {
			BrowserInformationControlInput previous= (BrowserInformationControlInput) fInfoControl.getInput().getPrevious();
			if (previous != null) {
				fInfoControl.setInput(previous);
			}
		}

		public void update() {
			BrowserInformationControlInput current= fInfoControl.getInput();

			if (current != null && current.getPrevious() != null) {
				BrowserInput previous= current.getPrevious();
				setToolTipText(org.eclipse.jdt.internal.corext.util.Messages.format(Messages.JavadocHover_back_toElement_toolTip, BasicElementLabels.getJavaElementName(previous.getInputName())));
				setEnabled(true);
			} else {
				setToolTipText(Messages.JavadocHover_back);
				setEnabled(false);
			}
		}
	}

	/**
	 * Action to go forward to the next input in the hover control.
	 *
	 * @since 3.4
	 */
	private static final class ForwardAction extends Action {
		private final BrowserInformationControl fInfoControl;

		public ForwardAction(BrowserInformationControl infoControl) {
			fInfoControl= infoControl;
			setText(Messages.JavadocHover_forward);
			ISharedImages images= PlatformUI.getWorkbench().getSharedImages();
			setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_FORWARD));
			setDisabledImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_FORWARD_DISABLED));

			update();
		}

		@Override
		public void run() {
			BrowserInformationControlInput next= (BrowserInformationControlInput) fInfoControl.getInput().getNext();
			if (next != null) {
				fInfoControl.setInput(next);
			}
		}

		public void update() {
			BrowserInformationControlInput current= fInfoControl.getInput();

			if (current != null && current.getNext() != null) {
				setToolTipText(org.eclipse.jdt.internal.corext.util.Messages.format(Messages.JavadocHover_forward_toElement_toolTip, BasicElementLabels.getJavaElementName(current.getNext().getInputName())));
				setEnabled(true);
			} else {
				setToolTipText(Messages.JavadocHover_forward_toolTip);
				setEnabled(false);
			}
		}
	}

	/**
	 * Action that shows the current hover contents in the Javadoc view.
	 *
	 * @since 3.4
	 */
	private static final class ShowInJavadocViewAction extends Action {
		private final BrowserInformationControl fInfoControl;

		public ShowInJavadocViewAction(BrowserInformationControl infoControl) {
			fInfoControl= infoControl;
			setText(Messages.JavadocHover_showInJavadoc);
			setImageDescriptor(JavaPluginImages.DESC_OBJS_JAVADOCTAG); //TODO: better image
		}

		/*
		 * @see org.eclipse.jface.action.Action#run()
		 */
		@Override
		public void run() {
			JavadocBrowserInformationControlInput infoInput= (JavadocBrowserInformationControlInput) fInfoControl.getInput(); //TODO: check cast
			fInfoControl.notifyDelayedInputChange(null);
			fInfoControl.dispose(); //FIXME: should have protocol to hide, rather than dispose
			try {
				JavadocView view= (JavadocView) JavaPlugin.getActivePage().showView(JavaUI.ID_JAVADOC_VIEW);
				view.setInput(infoInput);
			} catch (PartInitException e) {
				JavaPlugin.log(e);
			}
		}
	}

	/**
	 * Action that opens the current hover input element.
	 *
	 * @since 3.4
	 */
	private static final class OpenDeclarationAction extends Action {
		private final BrowserInformationControl fInfoControl;

		public OpenDeclarationAction(BrowserInformationControl infoControl) {
			fInfoControl= infoControl;
			setText(Messages.JavadocHover_openDeclaration);
			JavaPluginImages.setLocalImageDescriptors(this, "goto_input.gif"); //$NON-NLS-1$ //TODO: better images
		}

		/*
		 * @see org.eclipse.jface.action.Action#run()
		 */
		@Override
		public void run() {
			JavadocBrowserInformationControlInput infoInput= (JavadocBrowserInformationControlInput) fInfoControl.getInput(); //TODO: check cast
			fInfoControl.notifyDelayedInputChange(null);
			fInfoControl.dispose(); //FIXME: should have protocol to hide, rather than dispose

			try {
				JavadocHover.openDeclaration(infoInput.getElement());
			} catch (PartInitException e) {
				JavaPlugin.log(e);
			} catch (JavaModelException e) {
				JavaPlugin.log(e);
			}
		}
	}

}