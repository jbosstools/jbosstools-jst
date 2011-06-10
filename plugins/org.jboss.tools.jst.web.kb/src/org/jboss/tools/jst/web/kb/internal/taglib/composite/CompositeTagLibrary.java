package org.jboss.tools.jst.web.kb.internal.taglib.composite;

import org.jboss.tools.jst.web.kb.internal.KbXMLStoreConstants;
import org.jboss.tools.jst.web.kb.internal.taglib.AbstractTagLib;
import org.jboss.tools.jst.web.kb.taglib.ICompositeTagLibrary;

public class CompositeTagLibrary extends AbstractTagLib implements ICompositeTagLibrary {

	public CompositeTagLibrary() {}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.AbstractTagLib#clone()
	 */
	@Override
	public CompositeTagLibrary clone() throws CloneNotSupportedException {
		return (CompositeTagLibrary)super.clone();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.KbObject#getXMLClass()
	 */
	@Override
	public String getXMLClass() {
		return KbXMLStoreConstants.CLS_COMPOSITE_LIBRARY;
	}
}