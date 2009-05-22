package org.jboss.tools.jst.jsp.contentassist;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.el.core.resolver.ELContextImpl;
import org.jboss.tools.common.el.core.resolver.ELResolver;
import org.jboss.tools.common.el.core.resolver.ElVarSearcher;

public class XmlContentAssistProcessor extends AbstractXMLContentAssistProcessor {

	@Override
	protected ELContext getContext() {
		IFile file = getResource();
		ElVarSearcher varSearcher = null; // TODO
		ELResolver[] elResolvers = getELResolvers(file);

		ELContextImpl context = new ELContextImpl();
		context.setResource(getResource());
		context.setVarSearcher(varSearcher);
		context.setElResolvers(elResolvers);
		setVars(context);
		
		return context;
	}
	
	protected void setVars(ELContext context) {
		// TODO
	}

}
