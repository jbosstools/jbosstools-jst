/*******************************************************************************
 * Copyright (c) 2011-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.text.ext.hyperlink;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.internal.util.Sorter;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.text.ext.ExtensionsPlugin;
import org.jboss.tools.common.text.ext.hyperlink.AbstractHyperlink;
import org.jboss.tools.common.text.ext.hyperlink.xpl.Messages;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper;
import org.jboss.tools.common.text.ext.util.StructuredSelectionHelper;
import org.jboss.tools.common.text.ext.util.Utils;
import org.jboss.tools.jst.text.ext.JSTExtensionsPlugin;
import org.jboss.tools.jst.web.kb.ICSSContainerSupport;
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.jst.web.kb.PageContextFactory.CSSStyleSheetDescriptor;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.css.CSSMediaRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

/**
 * @author Jeremy
 */
@SuppressWarnings("restriction")
public class CSSClassHyperlink extends AbstractHyperlink {
	CSSRuleDescriptorSorter SORTER = new CSSRuleDescriptorSorter();
	private static final char[] SPACE_CHARS = {' ', '\t', '\r', '\n', '\f'};

	public static final String[] STYLE_TAGS = new String[] { "style", "link" }; //$NON-NLS-1$//$NON-NLS-2$
	public static final String LINK_TAG = "link"; //$NON-NLS-1$
	public static final String HREF_ATTRIBUTE = "href"; //$NON-NLS-1$
	public static final String CONTEXT_PATH_EXPRESSION = "^\\s*(\\#|\\$)\\{facesContext.externalContext.requestContextPath\\}"; //$NON-NLS-1$
	
	@Override
	protected void doHyperlink(IRegion region) {
		ICSSContainerSupport cssContainerSupport = null;
		ELContext context = PageContextFactory.createPageContext(getDocument());
		if (!(context instanceof ICSSContainerSupport)) {
			openFileFailed();
			return;
		}
		cssContainerSupport = (ICSSContainerSupport)context;
		List<CSSStyleSheetDescriptor> descrs = cssContainerSupport.getCSSStyleSheetDescriptors();

		List<CSSRuleDescriptor> bestMatchDescriptors = new ArrayList<CSSRuleDescriptor>();
		
		for (int i = (descrs == null) ? -1 : descrs.size() - 1; descrs != null && i >= 0; i--) {
			CSSStyleSheetDescriptor descr = descrs.get(i);
			CSSStyleSheet sheet = descr.getStylesheet();
			if (sheet != null) {
				CSSRuleList rules = sheet.getCssRules();
				for (int r = 0; rules != null && r < rules.getLength(); r++) {
					Set<CSSRuleDescriptor> matches = getMatchedRuleDescriptors(rules.item(r), getStyleName(region));
					for (CSSRuleDescriptor match : matches) {
						match.stylesheetDescriptor = descr;
						bestMatchDescriptors.add(match);
					}
				}
			}
		}
		
		CSSRuleDescriptor[] sorted = orderCSSRuleDescriptors(
				bestMatchDescriptors.toArray(new CSSRuleDescriptor[bestMatchDescriptors.size()]));
		
		for (CSSRuleDescriptor d : sorted) {
			CSSStyleSheetDescriptor descr = d.stylesheetDescriptor;
			IFile file = findFileForCSSStyleSheet(descr.getFilePath());
			if (file != null) {
				int startOffset = 0;
				CSSStyleSheet sheet = descr.getStylesheet();
				if (sheet != null && descr.getContainerNode() != null) {
					Node node = descr.getContainerNode().getFirstChild();
					if (node instanceof IndexedRegion) {
						startOffset = ((IndexedRegion)node).getStartOffset();
					}
					showRegion(
							file, 
							new Region(startOffset + ((IndexedRegion)d.rule).getStartOffset(), ((IndexedRegion)d.rule).getLength()));
					return;
				}
			}
		}

		openFileFailed();
	}

	protected CSSRuleDescriptor[] orderCSSRuleDescriptors(CSSRuleDescriptor[] descriptors) {
		Object[] sorted = SORTER.sort(descriptors);
		CSSRuleDescriptor[] sortedDescriptors = new CSSRuleDescriptor[sorted.length];
		System.arraycopy(sorted, 0, sortedDescriptors, 0, sorted.length);
		return sortedDescriptors;
	}
	
	class CSSRuleDescriptorSorter extends Sorter {
		
		public boolean compare(Object descr1, Object descr2) {
			CSSAxis[] a1 = ((CSSRuleDescriptor)descr1).axis;
			CSSAxis[] a2 = ((CSSRuleDescriptor)descr2).axis;

			return (calcImportance(a1) > calcImportance(a2));
		}
		
		long calcImportance(CSSAxis[] axis) {
			short b = 0;	// count the number of ID attributes in the selector
			short c = 0;	// count the number of other attributes and pseudo-classes in the selector
			short d = 0;	// count the number of element names and pseudo-elements in the selector

			for (CSSAxis a : axis) {
				b += a.idAttributeValues.size();
				c += a.attributes.size() + a.classNames.size() + a.pseudoClasses.size();
				d += (CSSAxis.CSS_ANY.equals(a.elementName) ? 0 : 1) + a.pseudoElements.size();
			}
			
			return (b<<32) + (c<<16) + d;
		}
	}
	
	/*
	 * Finds a file representing the specified stylesheet
	 * 
	 * Three kinds of filePath values are tested:
	 * - workspace related full path (Comes for a stylesheet defined in STYLE tag. Actually it is a path to the page where the STYLE tag is used)
	 * - full file path to the CSS-file within the project
	 * - relative file path
	 * 
	 * @param filePath
	 * @return
	 */
	private IFile findFileForCSSStyleSheet(String filePath) {
		// First try to find a file by WS-related path (because it's the most longest path)
		IFile file = ResourcesPlugin.getWorkspace().getRoot()
		.getFileForLocation(
				ResourcesPlugin.getWorkspace().getRoot().
					getLocation().append(filePath));
		if (file != null)
			return file;
		
		// Thought if project is imported as a link from some out-of-workspace location and the first segment of path is a project 
		IPath path = new Path(filePath);
		
		if (path.isAbsolute()) {
			IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects(IContainer.INCLUDE_HIDDEN);
			for (int i = 0; i < projects.length; i++) {
				IProject project = projects[i];
				IPath projectLocation = project.getLocation();
				if (projectLocation != null && projectLocation.lastSegment().equals(path.segment(0))) {
					IPath projectRelatedPath = projectLocation.append(path.removeFirstSegments(1));
					file = ResourcesPlugin.getWorkspace().getRoot()
							.getFileForLocation(projectRelatedPath);
					if (file != null)
						return file;
				}
			}
		}

		return PageContextFactory.getFileFromProject(filePath, getFile());
	}
	
	/**
	 * 
	 * @param cssRule
	 * @param styleName
	 * @return
	 */
	protected Set<CSSRuleDescriptor> getMatchedRuleDescriptors(CSSRule cssRule, String styleName) {
		Set<CSSRuleDescriptor> matches = new HashSet<CSSRuleDescriptor>();
		if (cssRule instanceof CSSMediaRule) {
			CSSMediaRule cssMediaRule = (CSSMediaRule)cssRule;
			CSSRuleList rules = cssMediaRule.getCssRules();
			for (int i = 0; rules != null && i < rules.getLength(); i++) {
				CSSRule rule = rules.item(i);
				Set<CSSRuleDescriptor> descrs = getMatchedRuleDescriptors(rule, styleName);
				if (descrs != null) {
					matches.addAll(descrs);
				}
			}
			return matches;
		}

		if (!(cssRule instanceof CSSStyleRule))
			return matches;
		
		// get selector text
		String selectorText = ((CSSStyleRule) cssRule).getSelectorText();

		if (selectorText == null) 
			return null;
		
		String[] selectors = getSelectors(selectorText);
		if (selectors.length == 0)
			return null;
		
		for (String selector : selectors) {
			String[] simpleSelectors = getSimpleSelectors(selector);
			if (simpleSelectors.length == 0)
				continue;
			
			List<CSSAxis> axisList = new ArrayList<CSSAxis>();
			for (String simpleSelector : simpleSelectors) {
				if (simpleSelector.length() > 0) {
					axisList.add(new CSSAxis(simpleSelector));
				}
			}
			if (axisList.size() > 0) {
				CSSAxis[] axis = axisList.toArray(new CSSAxis[0]);
				if (matchesRule(getHyperlinkRegion(), axis)) {
					matches.add(new CSSRuleDescriptor(selector, cssRule, axis));
				}
			}				
		}
		return matches;
	}
	
	private String[] getSelectors(String selectorText) {
		List<String> selectors = new ArrayList<String>();
		String rest = selectorText.trim().toLowerCase();
		int index = -1;
		while ((index = getIndexOfCharNotQuotted(rest, ',')) != -1) {
			String selector = rest.substring(0, index).trim();
			rest = rest.substring(index + 1).trim();
			if (selector.length() > 0) {
				selectors.add(selector);
			}
		}
		if (rest.length() > 0) {
			selectors.add(rest);
		}
		return selectors.toArray(new String[0]);
	}
	
	private String[] getSimpleSelectors(String selector) {
		List<String> simpleSelectors = new ArrayList<String>();
		String rest = dropWSCharsInBrackets(selector);
		int index = -1;
		while ((index = getIndexOfCharNotQuotted(rest, SPACE_CHARS)) != -1) {
			String simpleSelector = rest.substring(0, index).trim();
			rest = rest.substring(index + 1).trim();
			if (simpleSelector.length() > 0) {
				simpleSelectors.add(simpleSelector);
			}
		}
		if (rest.length() > 0) {
			simpleSelectors.add(rest);
		}
		return simpleSelectors.toArray(new String[0]);
	}

	private String dropWSCharsInBrackets(String selector) {
		char pair = 0;
		char pairBracket = 0;
		
		boolean inQuotes = false;
		boolean inBrackets = false;
		StringBuilder sb = new StringBuilder();
		if (selector != null) {
			for (char character : selector.toCharArray()) {
	            if (inQuotes) {
	            	// Append any char
	            	sb.append(character);
	            	if (character == pair) {
	            		inQuotes = false;
	            	} 
	            } else {
	            	if (character == '"' || character == '\'') {
	            		pair = character;
	            		inQuotes = true;
	            		sb.append(character);
	            	} else {
	            		if (inBrackets) {
	            			// Append any char excluding WS chars
	            			boolean wsChar = false; 
	                		for (char ch : SPACE_CHARS) {
	                    		if (character == ch) {
	                        		wsChar = true;
	                        		break;
	                    		}
	                		}
	                		if (!wsChar) {
	                			sb.append(character);
	                		}
	            			if (character == pairBracket) {
	            				pairBracket = 0;
	            				inBrackets = false;
	            			}
	            		} else {
	            			// Append any char
	            			sb.append(character);
	            			if (character == '[') {
	            				pairBracket = ']';
	            				inBrackets = true;
	            			} else if (character == '(') {
	            				pairBracket = ')';
	            				inBrackets = true;
	            			}
	            		}
	            	}
	            }
			}
		}
		return sb.toString();
	}
	
	private boolean matchesRule(IRegion styleNameRegion, CSSAxis[] cssAxis) {
		if (cssAxis == null || cssAxis.length == 0)
			return false;
		
		String classNameFromTheRegion = getStyleName(styleNameRegion);
		
		StructuredModelWrapper smw = new StructuredModelWrapper();
		try {
			smw.init(getDocument());
			Document xmlDocument = smw.getDocument();
			if (xmlDocument == null) return false;
			Node node = Utils.findNodeForOffset(xmlDocument, styleNameRegion.getOffset());
			
			if (node instanceof Attr) {
				Attr attr = (Attr)node;
				node = attr.getOwnerElement();
			}

			for (int i = cssAxis.length - 1; i >= 0; i--) {
				CSSAxis currentAxis = cssAxis[i];
				
				do {
					boolean classFound = currentAxis.classNames.isEmpty() && currentAxis.pseudoClasses.isEmpty();
					boolean nodeFound = currentAxis.elementName == CSSAxis.CSS_ANY && currentAxis.pseudoElements.isEmpty();
					boolean attrFound = currentAxis.attributes.isEmpty() && currentAxis.idAttributeValues.isEmpty();

					if (!classFound) {
						// For the top node's class name we have to use word from the styleNameRegion 
						String nodeClasses = (i == cssAxis.length - 1) ? classNameFromTheRegion : getNodeAttributeValue(node, "class"); //$NON-NLS-1$
						
						// Use getSimpleSelectors() method here since it takes in account all the possible WhiteSpace characters
						String[] classes = getSimpleSelectors(nodeClasses == null ? "" : nodeClasses.toLowerCase()); //$NON-NLS-1$ 
						if (classes != null) {
							for (String cssClass : classes) {
								if (currentAxis.classNames.contains(cssClass) || currentAxis.pseudoClasses.contains(cssClass)) {
									classFound = true;
									break;
								}
							}
						}
					}
					
					if (!nodeFound) {
						nodeFound = currentAxis.elementName.equalsIgnoreCase(node.getNodeName()) || currentAxis.pseudoElements.contains(node.getNodeName().toLowerCase());
					}
					
					if (!attrFound) {
						boolean allValuesAreOK = true;
						for (String attrName : currentAxis.attributes.keySet()) {
							String attrValue = Utils.trimQuotes(currentAxis.attributes.get(attrName));
							if (!attrValue.equalsIgnoreCase(Utils.trimQuotes(getNodeAttributeValue(node, attrName)))) {
								allValuesAreOK = false;
								break;
							}
						}
						if (currentAxis.idAttributeValues.size() > 0) {
							String nodeIDAttributeValue = Utils.trimQuotes(getNodeAttributeValue(node, "id"));  //$NON-NLS-1$
							allValuesAreOK &= currentAxis.idAttributeValues.contains(nodeIDAttributeValue == null ? null : nodeIDAttributeValue.toLowerCase());
						}

						attrFound = allValuesAreOK;
					}

					// proceed with parent node
					node = node.getParentNode();

					if (classFound && nodeFound && attrFound) {
						// Proceed with next Axis
						break;
					}
				} while (node instanceof Element);

				
				// All three parts of the axis were found - proceed with next axis
				// But do re-check that we still can proceed
				if (!(node instanceof Element)) {
					return false;
				}
			}
			
			return true;
		} finally {
			smw.dispose();
		}		
	}
	
	private String getNodeAttributeValue(Node node, String attrName) {
		NamedNodeMap attrs = node.getAttributes();

		for (int i = 0; attrs != null && i < attrs.getLength(); i++) {
			Attr attr = (Attr)attrs.item(i);
			if (attr.getName().toLowerCase().equalsIgnoreCase(attrName)) {
				return ((Attr)attr).getValue();
			}
		}
		return null;
	}

	class CSSRuleDescriptor {
		String selector;
		CSSRule rule;
		CSSAxis[] axis;
		CSSStyleSheetDescriptor stylesheetDescriptor;
		
		CSSRuleDescriptor (String selector, CSSRule rule, CSSAxis[] axis) {
			this.selector = selector;
			this.rule = rule;
			this.axis = axis;
		}

		@Override
		public String toString() {
			return selector;
		}
	}
	
	class CSSAxis {
		static final String CSS_ANY = "*"; //$NON-NLS-1$
		static final int AXIS_TYPE_ANY = 0;
		static final int AXIS_TYPE_IMMEDIATELY_FOLLOWS = 1;
		static final int AXIS_TYPE_CHILD_OF_PARENT = 2;
		final String[] VALID_PSEUDO_CLASSES = {
				"first-element", "link", "visited",  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"hover", "active", "focus", "lang" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			};
		final String[] VALID_PSEUDO_ELEMENTS = {
				"first-line", "first-letter",  //$NON-NLS-1$ //$NON-NLS-2$
				"before", "after"   //$NON-NLS-1$//$NON-NLS-2$
			};
		
		int type;
		Set<String> classNames;
		String elementName;
		Set<String> pseudoClasses;
		Set<String> pseudoElements;
		Set<String> idAttributeValues;
		Map<String, String> attributes;

		CSSAxis(String simpleSelector) {
			type = AXIS_TYPE_ANY;
			classNames = new HashSet<String>();
			elementName = CSS_ANY;
			pseudoClasses = new HashSet<String>();
			pseudoElements = new HashSet<String>();
			idAttributeValues = new HashSet<String>();
			attributes = new HashMap<String, String>();

			/*
			 combinator
				: ’+’ S* 
				| ’>’ S*
				;
			 selector
				: simple_selector [ combinator selector | S+ [ combinator? selector ]? ]?
				;
				simple_selector
				: element_name [ HASH | class | attrib | pseudo ]*
				| [ HASH | class | attrib | pseudo ]+
				;
			 */
			
			String rest = simpleSelector.trim().toLowerCase();

			if (rest.length() == 1) {
				if (rest.charAt(0) == '>') {
					this.type = AXIS_TYPE_CHILD_OF_PARENT;
					return;
				} else if (rest.charAt(0) == '+') {
					this.type = AXIS_TYPE_IMMEDIATELY_FOLLOWS;
					return;
				}
			}
			
			// Extract Element Name (which, if exists, must be the first Identifier or '*'-char)
			if (rest.length() > 0) {
				if (rest.charAt(0) == '*') {
					// Cut it
					rest = rest.substring(1).trim();
				} else {
					StringBuilder sb = new StringBuilder();
					for (char ch : rest.toCharArray()) {
						if (!Character.isJavaIdentifierPart(ch) 
								&& ch != '-' && ch != '_')
							break;
						sb.append(ch);
					}
					if (sb.length() > 0) {
						elementName = sb.toString();
						rest = rest.substring(sb.length()).trim();
					}
				}
			}
			
			// All the other selector parts could be picked up one by one
			
			// attributes (must be retrieved right after type or element name because of their complexity)
			/*
			 	 INCLUDES 
			 	 	: '~=' 
			 	 	;
					DASHMATCH
					: '|='
					'
				 attrib
					: ’[’ S* IDENT S* [ [ ’=’ | INCLUDES | DASHMATCH ] S*
					[ IDENT | STRING ] S* ]? ’]’
					;
			 */
			int index = -1;
			while ((index = getIndexOfCharNotQuotted(rest, '[')) != -1) {
				int start = index;
				int end = getIndexOfCharNotQuotted(rest, ']');
				
				String attrName = null;
				String attrValue = null;
				
				String attribute = (end == -1 ? 
						rest.substring(start + 1) :
						rest.substring(start + 1, end)).trim();
				rest = (end == -1 ? 
						"" :  //$NON-NLS-1$
						rest.substring(end + 1)).trim();
				
				// Eat open/close brackets
				if (attribute.startsWith("["))  //$NON-NLS-1$
					attribute = attribute.substring(1).trim();
				if (attribute.endsWith("]")) //$NON-NLS-1$
					attribute = attribute.substring(0, attribute.length() - 1).trim();
				
				// Get attr name
				StringBuilder sb = new StringBuilder();
				for (char ch : attribute.toCharArray()) {
					if (!Character.isJavaIdentifierPart(ch) 
							&& ch != '-' && ch != '_')
						break;
					sb.append(ch);
				}
				if (sb.length() > 0) {
					attrName = sb.toString();
					attribute = attribute.substring(sb.length()).trim();
				} else {
					// There is some syntax error in this case - skip
					continue; 
				}
				
				// There may be one of '=', '~=' '|=' signs between attr name and value
				if (attribute.startsWith("=")  //$NON-NLS-1$
						|| attribute.startsWith("~=")  //$NON-NLS-1$
						|| attribute.startsWith("|=")) { //$NON-NLS-1$
					
					// Eat the EQUAL/INCLUDES/DASHMATCH sign
					attribute = (attribute.charAt(0) == '=' 
							? attribute.substring(1).trim() 
									: attribute.substring(2).trim());

					// the rest of attribute is the value string (may be quotted)
					attrValue = attribute.trim();
					if (attrValue.length() == 0) {
						attrValue = CSS_ANY;
					}
				}
				
				attributes.put(attrName, attrValue);
			}
			
			// After the attributes are picked up we can read all the other things with no any trick
			
			// HASH ('#'-char followed by an identifier)
			/*
			 	HASH #{name}
				name {nmchar}+
				nonascii [^\0-\237]
				unicode \\[0-9a-f]{1,6}(\r\n|[ \n\r\t\f])?
				escape {unicode}|\\[^\n\r\f0-9a-f]
				nmchar [_a-z0-9-]|{nonascii}|{escape}
			 */
			while ((index = rest.indexOf('#')) != -1) {
				// Get ID attribute value
				StringBuilder sb = new StringBuilder();
				for (char ch : rest.substring(index + 1).toCharArray()) {
					if (ch == '#' || ch == '.' || ch == ':')
						break;
					sb.append(ch);
				}
				rest = rest.substring(0, index) + rest.substring(index + sb.length() + 1);
				if (sb.length() > 0) {
					String idAttributeValue = sb.toString().trim();
					if (idAttributeValue.length() > 0) {
						idAttributeValues.add(idAttributeValue);
					}
				}
			}
			
			// class ('.'-char followed by an identifier)
			while ((index = rest.indexOf('.')) != -1) {
				// Get class name
				StringBuilder sb = new StringBuilder();
				for (char ch : rest.substring(index + 1).toCharArray()) {
					if (ch == '#' || ch == '.' || ch == ':')
						break;
					sb.append(ch);
				}
				rest = rest.substring(0, index) + rest.substring(index + sb.length() + 1);
				if (sb.length() > 0) {
					String className = sb.toString().trim();
					if (className.length() > 0) {
						classNames.add(className);
					}
				}
			}
			
			// pseudo (':'-char followed by an identifier or function)
			/*
			 	pseudo
					: ’:’ [ IDENT | FUNCTION S* [IDENT S*]? ’)’ ]
					;
			 */
			while ((index = rest.indexOf(':')) != -1) {
				// Get class name
				StringBuilder sb = new StringBuilder();
				for (char ch : rest.substring(index + 1).toCharArray()) {
					if (ch == '#' || ch == '.' || ch == ':')
						break;
					sb.append(ch);
				}
				rest = rest.substring(0, index) + rest.substring(index + sb.length() + 1);
				if (sb.length() > 0) {
					String pseudo = sb.toString().trim();
					if (isValidPseudo(pseudo, VALID_PSEUDO_CLASSES)) {
						pseudoClasses.add(pseudo);
					} else if (isValidPseudo(pseudo, VALID_PSEUDO_ELEMENTS)) {
						pseudoElements.add(pseudo);
					}
				}
			}
			
			// Done
		}

		boolean isValidPseudo(String pseudo, String[] template) {
			if (template == null) return false;
			pseudo = pseudo == null ? "" : pseudo.toLowerCase(); //$NON-NLS-1$
			if (pseudo.indexOf('(') != -1) 
				pseudo = pseudo.substring(0, pseudo.indexOf('(')).trim();
			for (String valid : template) {
				if (pseudo.startsWith(valid.toLowerCase()))
					return true;
			}
			return false;
		}
	}
	
	private int getIndexOfCharNotQuotted(String text, char ch) {
		return getIndexOfCharNotQuotted(text, new char[] {ch});
	}

	private int getIndexOfCharNotQuotted(String text, char[] chars) {
		return getIndexOfCharNotQuotted(text, chars, 0);
	}
	
	/* Returns index of the first char found from 'chars' array */
	private int getIndexOfCharNotQuotted(String text, char[] chars, int start) {
		int offset = start;
		int pair = -1;
		boolean inQuotes = false;
		
		while (offset < text.length()) {
            int character = text.charAt(offset);
            if (inQuotes) {
            	if (character == pair) {
            		inQuotes = false;
            	} 
            } else {
            	if (character == '"' || character == '\'') {
            		pair = character;
            		inQuotes = true;
            	} else {
            		for (char ch : chars) {
                		if (character == ch)
                    		return offset;
            		}
            	}
            }
			offset++;
		}
		return -1;
	}

	
	/**
	 * 
	 * @param styleRegion
	 */
	protected void showRegion(IFile file, IRegion region) {

		IWorkbenchPage workbenchPage = ExtensionsPlugin.getDefault()
				.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart part = null;
		if (file != null) {
			try {
				part = IDE.openEditor(workbenchPage, file, true);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
		if (part == null) {
			openFileFailed();
			return;
		}
		StructuredSelectionHelper.setSelectionAndReveal(part,
				region);
	}
	
	/**
	 * 
	 * @param region
	 * @return
	 */
	protected String getStyleName(IRegion region) {
		if(region != null) {
			try {
				return getDocument().get(region.getOffset(), region.getLength()).trim();
			} catch (BadLocationException e) {
				JSTExtensionsPlugin.getPluginLog().logError(e);
			}
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see IHyperlink#getHyperlinkText()
	 */
	public String getHyperlinkText() {
		String styleName = getStyleName(getHyperlinkRegion());
		if (styleName == null)
			return MessageFormat.format(Messages.OpenA, Messages.CSSStyle);

		return MessageFormat.format(Messages.OpenCSSStyle, styleName);
	}

	@Override
	protected String findAndReplaceElVariable(String fileName) {
		if (fileName != null)
			fileName = fileName.replaceFirst(CONTEXT_PATH_EXPRESSION,""); //$NON-NLS-1$
		
		return super.findAndReplaceElVariable(fileName);
	}
}