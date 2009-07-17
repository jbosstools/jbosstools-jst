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
import org.jboss.tools.common.el.core.model.ELInstance;
import org.jboss.tools.common.el.core.model.ELInvocationExpression;
import org.jboss.tools.common.el.core.model.ELModel;
import org.jboss.tools.common.el.core.parser.ELParser;
import org.jboss.tools.common.el.core.parser.ELParserFactory;
import org.jboss.tools.common.el.core.parser.ELParserUtil;
import org.jboss.tools.common.kb.KbDinamicResource;
import org.jboss.tools.common.kb.KbProposal;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
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

			ELParser p = ELParserUtil.getDefaultFactory().createParser();
			ELModel model = p.parse(query);

			List<ELInstance> is = model.getInstances();

			ELInvocationExpression expr = null;
			ELInvocationExpression current = null;
			boolean hasProperty = false;

			for (ELInstance i: is) {
				if(!(i.getExpression() instanceof ELInvocationExpression)) continue;
				expr = (ELInvocationExpression)i.getExpression();
				ELInvocationExpression inv = expr;
				current = inv;
				if(inv.getLeft() != null) {
					hasProperty = true;
					current = inv.getLeft(); //bean
				}
			}

			String beanNameFromQuery = current == null ? null : current.getText();

			String restQuery = expr == null ? "" : expr.getText(); //$NON-NLS-1$
			String argName = expr == null ? "" : expr.getMemberName(); //$NON-NLS-1$
			if(argName == null) argName = ""; //$NON-NLS-1$
			if(argName.startsWith("\"") || argName.startsWith("'")) { //$NON-NLS-1$ //$NON-NLS-2$
				argName = argName.substring(1);
			}
			if(argName.endsWith("\"") || argName.endsWith("'")) { //$NON-NLS-1$ //$NON-NLS-2$
				argName = argName.substring(0, argName.length() - 1);
			}
			
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
						bundleList.add(basename + "." + argName);	 //$NON-NLS-1$
					} else {
						bundleList.add(basename + "['" + argName + "']");	 //$NON-NLS-1$ //$NON-NLS-2$
					}
				}

				for (int i = 0; bundleList != null && i < bundleList.size(); i++) {
					String bundleName = (String)bundleList.get(i);
					
					List properties = fProvider.getList(fXModel, SUPPORTED_ID, bundleName, null);
					for (int ii = 0; properties != null && ii < properties.size(); ii++) {
						String pi = (String)properties.get(ii);
						if (pi.indexOf('-') == -1 && pi.indexOf('.') == -1 ) {
							sorted.add(beanNameFromQuery + "." + pi);	 //$NON-NLS-1$
							sorted.add(beanNameFromQuery + "['" + pi + "']");	 //$NON-NLS-1$ //$NON-NLS-2$
						} else {
							sorted.add(beanNameFromQuery + "['" + pi + "']");	 //$NON-NLS-1$ //$NON-NLS-2$
						}
					}
				}
			}			
			
			if (sorted.isEmpty()) return proposals;
						
			Iterator it = sorted.iterator();
			while(it.hasNext()) {
				String text = (String)it.next();
				process(proposals, "", "", -1, query.length() - "".length(), query.length(), text, restQuery, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						JspEditorPlugin.getDefault().getImage(JspEditorPlugin.CA_JSF_MESSAGES_IMAGE_PATH));
			}
		} catch (Exception x) {
			JspEditorPlugin.getPluginLog().logError("Error in executing query " + query, x); //$NON-NLS-1$
		}
		return proposals;
	}

	private static String[][] MESSAGE_PROPERTY_WRAPPERS = {{"#{", "}"},{"${", "}"}}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	
	protected String[][] getWrappers() {
		return MESSAGE_PROPERTY_WRAPPERS;
	}
	
	public String getType() {
		return KbDinamicResource.BUNDLE_PROPERTY_TYPE;
	}

	public String toString () {
		return "WTPKbdBundlePropertyResource"; //$NON-NLS-1$
	}

	public String getSupportedID () {
		return SUPPORTED_ID;
	}

	String getBundle(XModelObject property) {
		String bundle = XModelObjectLoaderUtil.getResourcePath(property.getParent());
		if(bundle == null) bundle = ""; //$NON-NLS-1$
		if(bundle.endsWith(".properties")) bundle = bundle.substring(0, bundle.length() - 11); //$NON-NLS-1$
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