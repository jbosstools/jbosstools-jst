/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.taglib;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.Platform;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.internal.proposal.CustomProposalType;
import org.jboss.tools.jst.web.kb.internal.proposal.CustomProposalTypeFactory;
import org.jboss.tools.jst.web.kb.taglib.IAttributeProvider;
import org.jboss.tools.jst.web.kb.taglib.ICustomTagLibrary;
import org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer;
import org.osgi.framework.Bundle;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Alexey Kazakov
 */
public class CustomTagLibrary extends AbstractTagLib implements ICustomTagLibrary {

	protected static final String DEFAULT_PREFIX = "defaultPrefix"; //$NON-NLS-1$
	public static final String TAG_LIB = "tag-lib"; //$NON-NLS-1$
	protected static final String COMPONENT = "component"; //$NON-NLS-1$
	protected static final String CLOSE_TAG = "closeTag"; //$NON-NLS-1$
	protected static final String TRUE = "true"; //$NON-NLS-1$
	protected static final String NAME = "name"; //$NON-NLS-1$
	protected static final String PROVIDER = "attributeProvider"; //$NON-NLS-1$
	protected static final String ATTRIBUTE = "attribute"; //$NON-NLS-1$
	protected static final String REQUIRED = "required"; //$NON-NLS-1$
	protected static final String PROPOSAL = "proposal"; //$NON-NLS-1$
	protected static final String TYPE = "type"; //$NON-NLS-1$
	protected static final String ENUMERATION = "enumeration"; //$NON-NLS-1$
	protected static final String PARAM = "param"; //$NON-NLS-1$
	protected static final String VALUE = "value"; //$NON-NLS-1$
	public static final String COMPONET_EXTENSION = "component-extension"; //$NON-NLS-1$
	protected static final String DEFAULT_VALUE = "defaultValue"; //$NON-NLS-1$
	protected static final String EXTENDED = "extended"; //$NON-NLS-1$
	protected static final String DESCRIPTION = "description"; //$NON-NLS-1$
	protected static final String IGNORE_CASE = "ignoreCase"; //$NON-NLS-1$

	protected String name;
	protected String defaultPrefix;
	protected ITagLibRecognizer recognizer;
	protected String contributerName;

	public CustomTagLibrary(String contributer, InputStream inputStream, String uri, String version, String name) {
		setURI(uri);
		setVersion(version);
		this.name = name;
		this.contributerName = contributer;
		Document document = null;
		try {
			DocumentBuilder builder = createDocumentBuilder(false);
			document = builder.parse(inputStream);
		} catch (SAXException e) {
			WebKbPlugin.getDefault().logError(e);
		} catch (IOException e) {
			WebKbPlugin.getDefault().logError(e);
		} catch (ParserConfigurationException e) {
			WebKbPlugin.getDefault().logError(e);
		}
		Element tagLib = document.getDocumentElement();
		defaultPrefix = tagLib.getAttribute(DEFAULT_PREFIX);
		ignoreCase = "true".equals(tagLib.getAttribute(IGNORE_CASE)); //$NON-NLS-1$
		NodeList children = tagLib.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if(child instanceof Element) {
				if(child.getNodeName().equals(COMPONENT)) {
					CustomTagLibComponent component = parseComponent((Element)child);
					component.setIgnoreCase(ignoreCase);
					addComponent(component);
				} else if (child.getNodeName().equals(COMPONET_EXTENSION)) {
					if(componentExtensions==null) {
						componentExtensions = new HashSet<CustomComponentExtension>();
					}
					CustomComponentExtension extension = new CustomComponentExtension();
					addComponent(extension);
					extension.setParentTagLib(this);
					extension.addAttributes(getAttributes((Element)child, ignoreCase));
					parseAttributeProviders((Element)child, extension);
					componentExtensions.add(extension);
				}
			}
		}
	}

	public CustomTagLibrary() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.ICustomTagLibrary#getRecognizer()
	 */
	@Override
	public ITagLibRecognizer getRecognizer() {
		return recognizer;
	}

	@Override
	public void setRecognizer(ITagLibRecognizer recognizer) {
		this.recognizer = recognizer;
	}

	@Override
	protected List<String> getPrefixes(KbQuery query, IPageContext context) {
		if("".equals(defaultPrefix)) {
			return null;
		}
		return super.getPrefixes(query, context);
	}

	private void parseAttributeProviders(Element element, CustomTagLibComponent component) {
		String providerClassNames = element.getAttribute(PROVIDER);
		if(providerClassNames.isEmpty()) {
			providerClassNames = null;
		}
		List<IAttributeProvider> providers = new ArrayList<IAttributeProvider>();
		if(providerClassNames!=null) {
			StringTokenizer st = new StringTokenizer(providerClassNames, " ,;", false);
			while(st.hasMoreElements()) {
				String className = st.nextToken();
				try {
					Bundle contributer = Platform.getBundle(contributerName);
					Class<?> providerClass = contributer.loadClass(className);
					Constructor<?> providerConstructor = providerClass.getConstructor(new Class[0]);
					Object providerInstance = providerConstructor.newInstance(new Object[0]);
					if(providerInstance instanceof IAttributeProvider) {
						providers.add((IAttributeProvider)providerInstance);
					} else {
						WebKbPlugin.getDefault().logError(new Exception(className + " should be instance of " + IAttributeProvider.class));
					}
				} catch (ClassNotFoundException e) {
					WebKbPlugin.getDefault().logError(e);
				} catch (NoSuchMethodException e) {
					WebKbPlugin.getDefault().logError(e);
				} catch (SecurityException e) {
					WebKbPlugin.getDefault().logError(e);
				} catch (InstantiationException e) {
					WebKbPlugin.getDefault().logError(e);
				} catch (IllegalAccessException e) {
					WebKbPlugin.getDefault().logError(e);
				} catch (IllegalArgumentException e) {
					WebKbPlugin.getDefault().logError(e);
				} catch (InvocationTargetException e) {
					WebKbPlugin.getDefault().logError(e);
				}
			}
		}
		if(!providers.isEmpty()) {
			component.setProviders(providers.toArray(new IAttributeProvider[providers.size()]));
		}
	}

	protected CustomTagLibComponent parseComponent(Element component) {
		String name = component.getAttribute(NAME);
		boolean closeTag = TRUE.equalsIgnoreCase(component.getAttribute(CLOSE_TAG));
		String description = getDescription(component);
		String extendedStr = component.getAttribute(EXTENDED);
		Boolean extended = extendedStr==null || extendedStr.length()==0 || TRUE.equals(extendedStr);
		CustomTagLibComponent newComponent = createComponent();
		newComponent.setName(name);
		newComponent.setCanHaveBody(!closeTag);
		newComponent.setDescription(description);
		newComponent.setExtended(extended);
		newComponent.setParentTagLib(this);
		parseAttributeProviders(component, newComponent);

		// Extract attributes
		CustomTagLibAttribute[] attributes = getAttributes(component, ignoreCase);
		for (int i = 0; i < attributes.length; i++) {
			newComponent.addAttribute(attributes[i]);
			attributes[i].setParentComponent(newComponent);
		}

		return newComponent;
	}

	protected CustomTagLibComponent createComponent() {
		return new CustomTagLibComponent();
	}

	public static CustomTagLibAttribute[] getAttributes(Element component) {
		return getAttributes(component, false);
	}

	public static CustomTagLibAttribute[] getAttributes(Element component, boolean ignoreCase) {
		Set<CustomTagLibAttribute> newAttributes = new HashSet<CustomTagLibAttribute>();
		NodeList children = component.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if(child instanceof Element) {
				if(child.getNodeName().equals(ATTRIBUTE)) {
					Element attribute = (Element)child;
					String attributeName = attribute.getAttribute(NAME);
					boolean required = TRUE.equalsIgnoreCase(attribute.getAttribute(REQUIRED));
					CustomTagLibAttribute newAttribute = new CustomTagLibAttribute();
					newAttribute.setIgnoreCase(ignoreCase);
					newAttribute.setName(attributeName);
					newAttribute.setRequired(required);
					String atrDescription = getDescription(attribute);
					newAttribute.setDescription(atrDescription);
					String extendedAtrStr = attribute.getAttribute(EXTENDED);
					Boolean extendedAtr = extendedAtrStr==null || extendedAtrStr.length()==0 || TRUE.equals(extendedAtrStr);
					newAttribute.setExtended(extendedAtr);
					String defaultValue = attribute.getAttribute(DEFAULT_VALUE);
					newAttribute.setDefaultValue(defaultValue);
					newAttributes.add(newAttribute);

					// Extract proposals
					List<CustomProposalType> newProposals = new ArrayList<CustomProposalType>();
					NodeList proposals = attribute.getElementsByTagName(PROPOSAL);
					for (int j = 0; j < proposals.getLength(); j++) {
						Element proposal = (Element)proposals.item(j);
						String type = proposal.getAttribute(TYPE);
						CustomProposalType newProposal = CustomProposalTypeFactory.getInstance().createProposalType(type);
						if(newProposal!=null) {
							newProposal.setType(type);
							newProposals.add(newProposal);

							List<CustomProposalType.Param> newParams = new ArrayList<CustomProposalType.Param>();
							// Extract params
							NodeList params = proposal.getElementsByTagName(PARAM);
							for (int c = 0; c < params.getLength(); c++) {
								Element param = (Element)params.item(c);
								String paramName = param.getAttribute(NAME);
								String paramValue = param.getAttribute(VALUE);
								String description = getDescription(param);
								CustomProposalType.Param newParam = new CustomProposalType.Param();
								newParam.setDescription(description);
								if(paramName!=null && paramName.length()>0) {
									newParam.setName(paramName);
								}
								newParam.setValue(paramValue);
								newParams.add(newParam);
							}
							newProposal.setParams(newParams.toArray(new CustomProposalType.Param[0]));
						}
					}
					newAttribute.setProposals(newProposals.toArray(new CustomProposalType[0]));
				}
			}
		}
		return newAttributes.toArray(new CustomTagLibAttribute[0]);
	}

	public static String getDescription(Element element) {
		NodeList list = element.getChildNodes();
		for(int i=0; i<list.getLength(); i++) {
			Node node = list.item(i);
			if(node instanceof Element) {
				if(DESCRIPTION.equals(node.getNodeName())) {
					String text = getElementBody((Element)node);
					text = text.replace("[\\", "</").replace('[', '<').replace(']', '>');
					return text;
				}
			}
		}
		return null;
	}

	public static String getElementBody(Element element) {
		StringBuffer sb = new StringBuffer();
		NodeList nl = element.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			short nodeType = n.getNodeType();
			if (nodeType == Node.TEXT_NODE || nodeType == Node.CDATA_SECTION_NODE) {
				sb.append(((CharacterData)n).getData());
			}
		}
		return sb.toString();
	}

	public static DocumentBuilder createDocumentBuilder(boolean validate) throws ParserConfigurationException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setValidating(validate);
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

		if(!validate) {
			documentBuilder.setEntityResolver(new EntityResolver() {
				public InputSource resolveEntity(java.lang.String publicId, java.lang.String systemId) throws SAXException, java.io.IOException {
					if((systemId != null) && systemId.toLowerCase().endsWith(".dtd")) { // this deactivates DTD //$NON-NLS-1$
						return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes())); //$NON-NLS-1$
					} else {
						return null;
					}
				}
			});
		}
		return documentBuilder;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the defaultPrefix
	 */
	public String getDefaultPrefix() {
		return defaultPrefix;
	}

	/**
	 * @deprecated
	 * @return the extendedAttributes
	 */
	@Deprecated
	public CustomComponentExtension getComponentExtension() {
		return componentExtensions!=null?componentExtensions.iterator().next():null;
	}
}