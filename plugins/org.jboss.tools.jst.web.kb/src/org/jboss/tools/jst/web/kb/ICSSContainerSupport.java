package org.jboss.tools.jst.web.kb;

import java.util.List;

import org.w3c.dom.css.CSSStyleSheet;

/**
 * The interface defines the methods to collect
 * CSS Stylesheets used within the page.
 * 
 * @author Victor Rubezhny
 *
 */
public interface ICSSContainerSupport {

	/**
	 * Adds the CSS StyleSheet object found within the page
	 * 
	 * @param includedContext
	 */
	void addCSSStyleSheet(CSSStyleSheet cssStyleSheet);
	
	/**
	 * Returns the list of all the collected CSS StyleSheet objects
	 * 
	 * @return
	 */
	List<CSSStyleSheet> getCSSStyleSheets();
}
