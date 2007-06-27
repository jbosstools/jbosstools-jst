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
package org.jboss.tools.jst.jsp.support.kb;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.ui.IEditorInput;

import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.util.ELParser;
import org.jboss.tools.common.kb.KbDinamicResource;
import org.jboss.tools.common.kb.KbIcon;
import org.jboss.tools.common.kb.KbProposal;
import org.jboss.tools.common.kb.KbProposal.PostProcessing;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.web.project.list.IWebPromptingProvider;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;

/**
 * @author Jeremy
 */
public class WTPKbdBeanPropertyResource extends WTPKbAbstractModelResource {

	public static String SUPPORTED_ID = WebPromptingProvider.JSF_BEAN_PROPERTIES;
	public Properties type = new Properties();

	public WTPKbdBeanPropertyResource(IEditorInput editorInput, WTPTextJspKbConnector connector) {
		super(editorInput);
	}

	public boolean isReadyToUse() {
		return (fProvider != null && fXModel != null);
	}

	public Collection<KbProposal> queryProposal(String query) {
		Collection<KbProposal> proposals = new ArrayList<KbProposal>();
		proposals.clear();
		try {
			if (!isReadyToUse()) return proposals;

			ELParser p = new ELParser();
			ELParser.Token token = p.parse(query);

			ArrayList<ELParser.Token> beans = new ArrayList<ELParser.Token>();
			boolean hasProperty = false;

			ELParser.Token c = token;
			boolean insideSL = false;
			while(c != null) {
				if(c.kind == ELParser.SPACES) {
					if(!insideSL) {
						beans.clear();
						hasProperty = false;
					} else {
						//do nothing
					}
				} else if(c.kind == ELParser.NONE || c.kind == ELParser.OPEN || c.kind == ELParser.CLOSE) {
					if(c.kind == ELParser.OPEN) insideSL = true;
					else if(c.kind == ELParser.CLOSE) insideSL = false;
					beans.clear();
					hasProperty = false;
				} else if(c.kind == ELParser.ARGUMENT) {
					hasProperty = true;
				} else if(c.kind == ELParser.DOT || c.kind == ELParser.OPEN_ARG) {
					hasProperty = true;
				} else if(c.kind == ELParser.NAME) {
					if(beans.size() > 0 && (c.next == null || (c.next.kind != ELParser.DOT && c.next.kind != ELParser.OPEN_ARG))) {
						hasProperty = true;
					} else {
						beans.add(c);
						hasProperty = false;
					}
				}
				c = c.next;
			}

			ELParser.Token b = (beans.size() == 0) ? null : (ELParser.Token)beans.get(0);
			ELParser.Token e = (beans.size() == 0) ? null : (ELParser.Token)beans.get(beans.size() - 1);

			String beanNameFromQuery = b == null ? null : query.substring(b.start, e.end);
			StringBuffer sb = new StringBuffer();
			ELParser.Token bi = b;
			while(bi != null) {
				if(bi.kind != ELParser.SPACES) sb.append(query.substring(bi.start, bi.end));
				bi = bi.next;
			}
			String restQuery = b == null ? "" : sb.toString();

			Set<String> sorted = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
			fillSortedProposalStrings(sorted, beanNameFromQuery, hasProperty);

			if (sorted.isEmpty()) return proposals;

			Iterator<String> it = sorted.iterator();
			while(it.hasNext()) {
				String text = it.next();
				process(proposals, "", "", -1, query.length() - "".length(), query.length(), text, restQuery);
			}
		} catch (Exception x) {
			JspEditorPlugin.getPluginLog().logError(x);
		}
		return proposals;
	}

	protected void fillSortedProposalStrings(Set<String> sorted, String beanName, boolean hasProperty) {
		if (beanName == null || beanName.length() == 0 || !hasProperty) {
			List beanList = fProvider.getList(fXModel, WebPromptingProvider.JSF_MANAGED_BEANS, "", null);
			Iterator it = beanList.iterator();
			while(it.hasNext()) {
				sorted.add(it.next().toString());
			}
		} else {
			List properties = fProvider.getList(fXModel, getSupportedID(), beanName, type);
			for (int ii = 0; properties != null && ii < properties.size(); ii++) {
				sorted.add(beanName + "." + (String)properties.get(ii));
			}
		}
	}

	private static String[][] BEAN_PROPERTY_WRAPPERS = {{"#{", "}"},{"${", "}"}};

	protected String[][] getWrappers() {
		return BEAN_PROPERTY_WRAPPERS;
	}

	protected boolean process(Collection<KbProposal> proposals, String prefix, String suffix, int start, int cursor, int end, String text, String query) {
		if ((prefix + text).toLowerCase().startsWith((prefix + query).toLowerCase())) {
			KbProposal proposal = new KbProposal();
			proposal.setLabel(prefix + text + suffix);
			proposal.setReplacementString(text);
			proposal.setIcon(KbIcon.ENUM_ITEM);
			proposals.add(proposal);
			proposal.setPosition(cursor);
			proposal.setPostProcessing(postProcessing);
			proposal.setRelevance(getKbProposalRelevance());
			return true;
		} else {
			return false;
		}
	}

	public String getType() {
		return KbDinamicResource.BEAN_PROPERTY_TYPE;
	}

	public InputStream getInputStream() {
		return null;
	}

	public String toString() {
		return "WTPKbdBeanPropertyResource";
	}

	public XModel getXModel() {
		return fXModel;
	}

	public String getSupportedID() {
		return SUPPORTED_ID;
	}

	public void setConstraint(String name, String value) {
		if (name == null) return;
		if ("type".equalsIgnoreCase(name)) {
			if (value == null || value.trim().length() == 0) {
				if(type != null) type.remove(IWebPromptingProvider.PROPERTY_TYPE);
			} else {
				type.put(IWebPromptingProvider.PROPERTY_TYPE, value);
			}
		}
	}

	public void clearConstraints() {
		if (type != null) type.clear();
	}

	PostProcessingImpl postProcessing = new PostProcessingImpl();

	class PostProcessingImpl implements PostProcessing {

		public void process(KbProposal proposal, String value, int offset) {
			ELParser p = new ELParser();
			ELParser.Token token = p.parse(value);
			ELParser.Token c = ELParser.getTokenAt(token, offset);
			ELParser.Token callStart = ELParser.getCallStart(c);
			if(callStart != null) proposal.setStart(callStart.start); else proposal.setStart(offset);
			ELParser.Token callEnd = ELParser.getCallEnd(c);
			if(callEnd != null && callEnd.end >= offset) proposal.setEnd(callEnd.end); else proposal.setEnd(offset);
			String[][] ws = getWrappers();
			int pos = proposal.getReplacementString().length();
			if(ws.length > 0 && ws[0][0].length() > 0) {
				ELParser.Token open = ELParser.getPrecedingOpen(c, offset);
				if(open == null && !proposal.getReplacementString().startsWith(ws[0][0])) {
					pos += ws[0][0].length();
					proposal.setReplacementString(ws[0][0] + proposal.getReplacementString());
					proposal.setLabel(ws[0][0] + proposal.getLabel() + ws[0][1]);
				} else if(open != null && !proposal.getReplacementString().startsWith(ws[0][0]) && open.end - open.start == 1) {
					if(ws[0][0].endsWith("{")) {
						pos += 1;
						proposal.setReplacementString("{" + proposal.getReplacementString());
					}					
				} else if(open != null && open.end - open.start == 2 && offset < open.end && offset > open.start) {
					proposal.setStart(proposal.getStart() + 1);
					proposal.setEnd(proposal.getStart());
					proposal.setPosition(proposal.getStart());
				}
				if(!ELParser.isFollowedByClose(c, offset) && !proposal.getReplacementString().endsWith(ws[0][1])) {
					proposal.setReplacementString(proposal.getReplacementString() + ws[0][1]);
				}
			}
			proposal.setPosition(pos);
		}
	}
}