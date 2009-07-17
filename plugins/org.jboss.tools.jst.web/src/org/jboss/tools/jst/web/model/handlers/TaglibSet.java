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
package org.jboss.tools.jst.web.model.handlers;

import java.util.*;

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.project.WebProject;
import org.jboss.tools.jst.web.tld.URIConstants;

public class TaglibSet {
	XModel model;
	Map<String,String> uriToDescription = new HashMap<String,String>();
	Map<String,String> descriptionToUri = new HashMap<String,String>();
	Map<String,String> uriToPrefix = new HashMap<String,String>();
	
	public String[] getDescriptions() {
		Set<String> set = new TreeSet<String>();
		set.addAll(uriToDescription.values());
		return set.toArray(new String[0]);
	}
	
	public String getPrefix(String description) {
		String uri = getURIByDescription(description);
		return uri == null ? null : (String)uriToPrefix.get(uri);
	}

	public void initTaglibDescriptions(XModel model) {
		this.model = model;
		uriToDescription.clear();
		descriptionToUri.clear();
		uriToPrefix.clear();
		Map<String,String> uriToDescriptionPalette = new HashMap<String,String>();
		XModelObject palette = PreferenceModelUtilities.getPreferenceModel().getByPath("%Palette%"); //$NON-NLS-1$
		if(palette == null) return;
		XModelObject[] tabs = palette.getChildren();
		for (int i = 0; i < tabs.length; i++) {
			XModelObject[] gs = tabs[i].getChildren();
			for (int j = 0; j < gs.length; j++) {
				String uri = gs[j].getAttributeValue(URIConstants.LIBRARY_URI);
				if(uri == null || uri.length() == 0) continue;
				String description = tabs[i].getAttributeValue("name") + " " + gs[j].getAttributeValue("name"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				uriToDescriptionPalette.put(uri, description);
			}
		}

		if(isJSF()) {
			registerTaglib("http://java.sun.com/jsf/html", "h"); //$NON-NLS-1$ //$NON-NLS-2$
			registerTaglib("http://java.sun.com/jsf/core", "f"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		WebProject p = WebProject.getInstance(model);
		Map taglibObjects = p.getTaglibMapping().getTaglibObjects();
		Iterator it = taglibObjects.keySet().iterator();
		while(it.hasNext()) {
			Object o = it.next();
			if(o == null) break;
			String s = o.toString();
			s = (s == null) ? "" : s.trim(); //$NON-NLS-1$
			if(s.length() == 0) continue;
			XModelObject t = (XModelObject)taglibObjects.get(s);
			if(t == null) continue;
			String sn = t.getAttributeValue("shortname"); //$NON-NLS-1$
			if(sn == null || sn.length() == 0) continue;
			String declaredURI = p.getTaglibMapping().getDeclaredURI(s);
			String description = t.getAttributeValue("description"); //$NON-NLS-1$
			if(description == null || description.length() == 0) {
				description = (String)uriToDescription.get(s);
			}
			if(description == null || description.length() == 0 || description.length() >= 50) {
				description = (String)uriToDescriptionPalette.get(s);
			}
			if(description == null || description.length() == 0 || description.length() >= 50) {
				description = (declaredURI != null) ? declaredURI : s;
			}
			uriToDescription.put(s, description);
			descriptionToUri.put(description, s);
			if(declaredURI != null) {
				uriToDescription.put(declaredURI, description);
				descriptionToUri.put(description, declaredURI);
			}
			if(declaredURI == null) {
				registerTaglib(s, sn);
			} else { 
				registerTaglib(declaredURI, sn);
			}
		}
	}
	
	private void registerTaglib(String uri, String prefix) {
		uriToPrefix.put(uri, prefix);
	}
	
	String getTaglibDescription(String uri) {
		String description = (String)uriToDescription.get(uri);
		return (description != null) ? description : uri;
	}
	
	String getURIByDescription(String description) {
		String uri = (String)descriptionToUri.get(description);
		return uri != null ? uri : description;
	}

	public final Set<String> getTaglibsFromTemplate(String body) {
		Set<String> existing = new HashSet<String>();
		doGetTaglibsFromTemplate(body, existing);
		return existing;
	}
	
	protected void doGetTaglibsFromTemplate(String body, Set<String> existing) {
		StringTokenizer st = new StringTokenizer(body, "\n"); //$NON-NLS-1$
		while(st.hasMoreTokens()) {
			String t = st.nextToken().trim();
			if(t.startsWith("<%@ taglib") && t.endsWith("%>")) { //$NON-NLS-1$ //$NON-NLS-2$
				int b = t.indexOf("uri=\""); //$NON-NLS-1$
				if(b < 0) continue;
				b += 5;
				int e = t.indexOf("\"", b); //$NON-NLS-1$
				if(e < 0) continue;
				String uri = t.substring(b, e);
				b = t.indexOf("prefix=\""); //$NON-NLS-1$
				b += 8;
				if(b < 0) continue;
				e = t.indexOf("\"", b); //$NON-NLS-1$
				if(e < 0) continue;
				String prefix = t.substring(b, e);
				appendURIFound(prefix, uri, existing);
			}
		}
	}
	
	protected final void appendURIFound(String prefix, String uri, Set<String> existing) {
		String description = getTaglibDescription(uri);
		uriToDescription.put(uri, description);
		descriptionToUri.put(description, uri);
		uriToPrefix.put(uri, prefix);
		existing.add(uri);
	}

	boolean isJSF() {
		return EclipseResourceUtil.hasNature(model, WebProject.JSF_NATURE_ID);		
	}
	
	public final String modifyBody(String body, String[] selected) {
		StringBuffer sb = new StringBuffer();
		boolean b = doModifyBody(body, selected, sb);
		return (b) ? sb.toString() : body;
	}
	
	protected boolean doModifyBody(String body, String[] selected, StringBuffer sb) {
		for (int i = 0; i < selected.length; i++) {
			String uri = getURIByDescription(selected[i]);
			String prefix = getPrefix(selected[i]);
			sb.append("<%@ taglib uri=\"" + uri + "\"" + " prefix=\"" + prefix + "\"" + " %>\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}
		body = remove(body, "<%@ taglib", "%>"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append(body);
		return true;
	}
	
	private String remove(String body, String start, String end) {
		StringBuffer sb = new StringBuffer();
		int i = 0;
		while(true) {
			int ib = body.indexOf(start, i);
			int ie = (ib < 0) ? -1 : body.indexOf(end, ib);
			if(ie < 0) break;
			sb.append(body.substring(i, ib));
			i = ie + end.length();
			while(i < body.length() && (body.charAt(i) == '\r' || body.charAt(i) == '\n')) {
				i++;
			}
		}
		sb.append(body.substring(i));
		return sb.toString();
	}
	
}
