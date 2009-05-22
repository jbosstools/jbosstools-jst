package org.jboss.tools.jst.jsp.contentassist;

import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jst.jsp.core.internal.contentmodel.TaglibController;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.TLDCMDocumentManager;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.TaglibTracker;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.el.core.resolver.ELResolver;
import org.jboss.tools.common.el.core.resolver.ElVarSearcher;
import org.jboss.tools.common.el.core.resolver.Var;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.IResourceBundle;
import org.jboss.tools.jst.web.kb.internal.JspContextImpl;
import org.jboss.tools.jst.web.kb.taglib.INameSpace;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;
import org.w3c.dom.Document;

public class JspContentAssistProcessor extends XmlContentAssistProcessor {

	@Override
	protected IPageContext getContext() {
		ELContext superContext = super.getContext();
		
		IFile file = getResource();
		
		JspContextImpl context = new JspContextImpl();
		context.setResource(superContext.getResource());
		context.setVarSearcher(superContext.getVarSearcher());
		context.setElResolvers(superContext.getElResolvers());
		setVars(context);
		context.setResourceBundles(getResourceBundles());
		context.setDocument(getDocument());
		setNameSpaces(context);
		context.setLibraries(getTagLibraries(context));
		
		return context;
	}

	protected void setNameSpaces(JspContextImpl context) {
		IStructuredModel sModel = StructuredModelManager
									.getModelManager()
									.getExistingModelForRead(getDocument());
		try {
			if (sModel == null) 
				return;
			
			Document xmlDocument = (sModel instanceof IDOMModel) ? 
							((IDOMModel) sModel).getDocument() : 
								null;

			if (xmlDocument == null)
				return;

			TLDCMDocumentManager manager = TaglibController.getTLDCMDocumentManager(getDocument());
			List trackers = (manager == null? null : manager.getCMDocumentTrackers(getOffset()));
			for (int i = 0; trackers != null && i < trackers.size(); i++) {
				TaglibTracker tt = (TaglibTracker)trackers.get(i);
				final String prefix = tt.getPrefix();
				final String uri = tt.getURI();
				if (prefix != null && prefix.trim().length() > 0 &&
						uri != null && uri.trim().length() > 0) {
						
					IRegion region = new Region(0, getDocument().getLength());
					INameSpace nameSpace = new INameSpace(){
					
						public String getURI() {
							return uri.trim();
						}
					
						public String getPrefix() {
							return prefix.trim();
						}
					};
					context.addNameSpace(region, nameSpace);
				}
			}

			return;
		}
		finally {
			if (sModel != null) {
				sModel.releaseFromRead();
			}
		}
	}

	protected ITagLibrary[] getTagLibraries(IPageContext context) {
		// TODO
		return null;
	}
	
	protected IResourceBundle[] getResourceBundles() {
		// TODO
		return null;
	}
}
