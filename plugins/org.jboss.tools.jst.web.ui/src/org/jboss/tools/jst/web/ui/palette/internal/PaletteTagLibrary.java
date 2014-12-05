/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.palette.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.common.model.ui.internal.editors.PaletteItemResult;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.KbQuery.Type;
import org.jboss.tools.jst.web.kb.internal.taglib.CustomTagLibrary;
import org.jboss.tools.jst.web.kb.taglib.IHTMLLibraryVersion;
import org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPTextEditor;

/**
 * @author Alexey Kazakov
 */
public abstract class PaletteTagLibrary extends CustomTagLibrary {

	protected final static int HTML_GROUP_RELEVANCE = TextProposal.R_XML_ATTRIBUTE_VALUE_TEMPLATE + 1;
	protected final static int DEFAULT_GROUP_RELEVANCE = HTML_GROUP_RELEVANCE + 1;

	private static int relevance = DEFAULT_GROUP_RELEVANCE;
	private ImageDescriptor image = null;
	private IHTMLLibraryVersion paletteLibraryVersion;

	public PaletteTagLibrary(String name, String uri, String version, String defaultPrefix, Boolean ignoreCase) {
		setURI(uri);
		setVersion(version);
		setRecognizer(getTagLibRecognizer());
		this.name = name;
		this.defaultPrefix = defaultPrefix;
		this.ignoreCase = ignoreCase;
	}

	@Override
	public TextProposal[] getProposals(KbQuery query, IPageContext context) {
		List<TextProposal> proposals = new ArrayList<TextProposal>();
		if(query.getType() == Type.TAG_NAME || query.getType() == KbQuery.Type.TEXT) {
			JSPTextEditor editor = WebUiPlugin.getActiveEditor();
			if(editor!=null) {
				Collection<RunnablePaletteItem> items = getItems();
				for (RunnablePaletteItem item : items) {
					PaletteItemResult result = item.getResult(editor);
					if(result != null){
						String startText = result.getStartText();
						if(startText != null && startWith(startText, query)){
							proposals.add(getProposal(item, result, editor));
						}
					}
				}
			}
		}
		return proposals.toArray(new TextProposal[proposals.size()]);
	}
	
	protected void setPaletteLibraryVersion(IHTMLLibraryVersion paletteLibraryVersion){
		this.paletteLibraryVersion = paletteLibraryVersion;
	}

	protected IHTMLLibraryVersion getPaletteLibraryVersion(){
		return paletteLibraryVersion;
	}

	private boolean startWith(String text, KbQuery query) {
		String startText = text.trim();
		return startText.startsWith(query.getValue()) || (startText.length()>1 && trim(startText).startsWith(query.getValue()));
	}

	private String trim(String text) {
		if(text.length()>1) {
			return text.substring(1);
		}
		return text;
	}

	protected TextProposal getProposal(RunnablePaletteItem item, PaletteItemResult result, JSPTextEditor editor) {
		TextProposal proposal = new TextProposal();

		String startText = result.getStartText();
		String endText = result.getEndText();

		String fullText = startText + "\n" + endText;
		proposal.setContextInfo(fullText.replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br/>").replace(" ", "&nbsp;").replace("\t", "&nbsp;&nbsp;&nbsp;"));
		proposal.setLabel(getLabel(item));
		proposal.setReplacementString(fullText);
		int position = proposal.getReplacementString().length();
		proposal.setPosition(position);
		proposal.setImageDescriptor(getImage());
		proposal.setRelevance(getRelevance());
		proposal.setAlternateMatch(trim(startText));
		if(!item.getAlternatives().isEmpty()) {
			proposal.getAlternativeMatches().addAll(item.getAlternatives());
		}
		proposal.setFilterable(false);
		proposal.setExecutable(item);
		return proposal;
	}

	public static String getLabel(RunnablePaletteItem item) {
		return item.getName() + " - " + item.getCategory() + " " + item.getVersion();
	}

	protected int getRelevance() {
		return DEFAULT_GROUP_RELEVANCE;
	}

	protected static synchronized int generateUniqueRelevance() {
		relevance++;
		return relevance;
	}

	public synchronized ImageDescriptor getImage() {
		if(image==null) {
			image = PaletteManager.getInstance().getImageDescriptor(getCategory());
		}
		return image;
	}

	public Collection<RunnablePaletteItem> getItems() {
		return PaletteManager.getInstance().getItems(getCategory(), getPaletteLibraryVersion());
	}

	protected abstract String getCategory();

	public abstract ITagLibRecognizer getTagLibRecognizer();
}