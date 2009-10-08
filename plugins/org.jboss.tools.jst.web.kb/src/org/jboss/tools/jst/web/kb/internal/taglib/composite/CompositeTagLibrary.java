package org.jboss.tools.jst.web.kb.internal.taglib.composite;

import org.jboss.tools.jst.web.kb.internal.KbXMLStoreConstants;
import org.jboss.tools.jst.web.kb.internal.taglib.AbstractTagLib;

public class CompositeTagLibrary extends AbstractTagLib {

	public CompositeTagLibrary() {}

	public CompositeTagLibrary clone() throws CloneNotSupportedException {
		return (CompositeTagLibrary)super.clone();
	}

	public String getXMLClass() {
		return KbXMLStoreConstants.CLS_COMPOSITE_LIBRARY;
	}

}
