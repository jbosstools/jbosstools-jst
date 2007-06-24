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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.ui.IEditorInput;

import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.util.ELParser;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.jboss.tools.common.kb.KbDinamicResource;
import org.jboss.tools.common.kb.KbProposal;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;

/**
 * @author Jeremy
 */
public class WTPKbdBundlePropertyResource extends WTPKbdBeanPropertyResource {
	WTPTextJspKbConnector connector;
	public static String SUPPORTED_ID = WebPromptingProvider.JSF_BUNDLE_PROPERTIES;

	public WTPKbdBundlePropertyResource(IEditorInput editorInput, WTPTextJspKbConnector connector) {
		super(editorInput, connector);
		this.connector = connector;
	}
	
	public Collection<KbProposal> queryProposal(String query) {
		Collection<KbProposal> proposals = new ArrayList<KbProposal>();
		try {
			if (!isReadyToUse()) return proposals;

			ELParser p = new ELParser();
			ELParser.Token token = p.parse(query);
			boolean hasProperty = false;
//			boolean isArgument = false;
			ELParser.Token arg = null;
			
			ArrayList<ELParser.Token> beans = new ArrayList<ELParser.Token>();
			
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
					arg = null;
				} else if(c.kind == ELParser.ARGUMENT) {
					hasProperty = true;
//					isArgument = true;
					arg = c;
				} else if(c.kind == ELParser.DOT || c.kind == ELParser.OPEN_ARG) {
					hasProperty = true;
				} else if(c.kind == ELParser.NAME) {
					if(beans.size() > 0 && (c.next == null || (c.next.kind != ELParser.DOT && c.next.kind != ELParser.OPEN_ARG))) {
						hasProperty = true;
						arg = c;
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
			String argName = arg == null ? "" : query.substring(arg.start, arg.end);
			
			Set<String> sorted = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

			Map _bundles = connector.getDeclaredBundles();
			Map<String,String> bundles2 = new TreeMap<String,String>();
			Iterator _it = _bundles.keySet().iterator();
			while(_it.hasNext()) {
				String _var = _it.next().toString();
				WTPTextJspKbConnector.LoadBundleInfo info = (WTPTextJspKbConnector.LoadBundleInfo)_bundles.get(_var);
				bundles2.put(_var, info.getBaseName());				
			}
			List l = fProvider.getList(fXModel, WebPromptingProvider.JSF_REGISTERED_BUNDLES, null, null);
			if(l != null && l.size() > 0 && (l.get(0) instanceof Map)) {
				bundles2.putAll((Map)l.get(0));
			}
			
			if (beanNameFromQuery == null || beanNameFromQuery.length() == 0 || !hasProperty) {
				Iterator<String> itr = bundles2.keySet().iterator();
				while (itr.hasNext())
					sorted.add(itr.next());
			} else {
				String basename = (String)bundles2.get(beanNameFromQuery);
				if (basename == null) return proposals;
				
				List<String> bundleList = new ArrayList<String>(2);
				bundleList.add(basename);
				if (argName != null && argName.length() > 0){
					if (restQuery.indexOf('-') == -1) {
						bundleList.add(basename + "." + argName);	
					} else {
						bundleList.add(basename + "['" + argName + "']");	
					}
				}

				for (int i = 0; bundleList != null && i < bundleList.size(); i++) {
					String bundleName = (String)bundleList.get(i);
					
					List properties = fProvider.getList(fXModel, SUPPORTED_ID, bundleName, null);
					for (int ii = 0; properties != null && ii < properties.size(); ii++) {
						String pi = (String)properties.get(ii);
						if (pi.indexOf('-') == -1 && pi.indexOf('.') == -1 ) {
							sorted.add(beanNameFromQuery + "." + pi);	
							sorted.add(beanNameFromQuery + "['" + pi + "']");	
						} else {
							sorted.add(beanNameFromQuery + "['" + pi + "']");	
						}
					}
				}
			}			
			
			if (sorted.isEmpty()) return proposals;
						
			Iterator it = sorted.iterator();
			while(it.hasNext()) {
				String text = (String)it.next();
				process(proposals, "", "", -1, query.length() - "".length(), query.length(), text, restQuery);
			}
		} catch (Exception x) {
			ModelPlugin.log("Error in executing query " + query, x);
		}
		return proposals;
	}

	private static String[][] MESSAGE_PROPERTY_WRAPPERS = {{"#{", "}"},{"${", "}"}};
	
	protected String[][] getWrappers() {
		return MESSAGE_PROPERTY_WRAPPERS;
	}
	
	public String getType() {
		return KbDinamicResource.BUNDLE_PROPERTY_TYPE;
	}

	public String toString () {
		return "WTPKbdBundlePropertyResource";
	}

	public String getSupportedID () {
		return SUPPORTED_ID;
	}

	String getBundle(XModelObject property) {
		String bundle = XModelObjectLoaderUtil.getResourcePath(property.getParent());
		if(bundle == null) bundle = "";
		if(bundle.endsWith(".properties")) bundle = bundle.substring(0, bundle.length() - 11);
		bundle = bundle.substring(1).replace('/', '.');
		return bundle;
	}

	public void setConstraint(String name, String value) {
	}

	public void clearConstraints() {
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if(!(o instanceof WTPKbdBundlePropertyResource)) return false;
		WTPKbdBundlePropertyResource other = (WTPKbdBundlePropertyResource)o;
		return other.getType().equals(getType()) && other.getXModel() == getXModel() && connector.equals(other.connector);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int hashCode = getType().hashCode();
		if(getXModel()!=null) {
			hashCode += getXModel().hashCode();
		}
		if(connector!=null) {
			hashCode += connector.hashCode();
		}

		return hashCode;
	}
}