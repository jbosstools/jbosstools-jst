package org.jboss.tools.jst.jsp.contentassist;

import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.internal.FaceletPageContextImpl;

public class FaceletPageContectAssistProcessor extends JspContentAssistProcessor {

	@Override
	protected IPageContext getContext() {
		IPageContext superContext = super.getContext();
		
		
		FaceletPageContextImpl context = new FaceletPageContextImpl();
		context.setResource(superContext.getResource());
		context.setVarSearcher(superContext.getVarSearcher());
		context.setElResolvers(superContext.getElResolvers());
		setVars(context);

		context.setResourceBundles(superContext.getResourceBundles());
		context.setDocument(getDocument());
		setNameSpaces(superContext, context);
		context.setLibraries(getTagLibraries(context));
		
//		IFaceletPageContext getParentContext();
//		Map<String, String> getParams();

		return context;
	}

	protected void setNameSpaces(IPageContext superContext, FaceletPageContextImpl context) {
		// TODO
	}
}
