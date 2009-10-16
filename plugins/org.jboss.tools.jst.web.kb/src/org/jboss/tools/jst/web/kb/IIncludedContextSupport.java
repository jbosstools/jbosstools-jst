package org.jboss.tools.jst.web.kb;

import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.jst.web.kb.taglib.INameSpace;

public interface IIncludedContextSupport {
	void addIncludedContext(IPageContext includedContext);
	List<IPageContext> getIncludedContexts();
	
	/**
	 * Returns Resource of the page
	 * @return
	 */
	IFile getResource();
	
	/**
	 * Returns map of name spaces which are set in particular offset.
	 * Key is URI of name space.
	 * @return
	 */
	Map<String, List<INameSpace>> getNameSpaces(int offset);
}
