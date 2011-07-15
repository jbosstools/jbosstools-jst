/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.text.ext.hyperlink;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.common.el.core.ELReference;
import org.jboss.tools.common.el.core.model.ELExpression;
import org.jboss.tools.common.el.core.model.ELInvocationExpression;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.el.core.resolver.ELResolution;
import org.jboss.tools.common.el.core.resolver.ELResolver;
import org.jboss.tools.common.el.core.resolver.ELSegment;
import org.jboss.tools.common.el.core.resolver.JavaMemberELSegment;
import org.jboss.tools.common.el.core.resolver.MessagePropertyELSegment;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.kb.PageContextFactory;

public class ELHyperlinkDetector extends AbstractHyperlinkDetector{

	public IHyperlink[] detectHyperlinks(ITextViewer textViewer,
			IRegion region, boolean canShowMultipleHyperlinks) {
		
		IFile file = getFile();
		if(file == null)
			return null;
		
		ELContext context = PageContextFactory.createPageContext(file);
		if(context == null)
			return null;
		
		ELReference reference = context.getELReference(region.getOffset());
		if(reference != null){
			ELInvocationExpression expression = findInvocationExpressionByOffset(reference, region.getOffset());
			if(expression != null){
				ELResolver[] resolvers = context.getElResolvers();
				for(ELResolver resolver : resolvers){
					ELResolution resolution = resolver.resolve(context, expression, region.getOffset());
					if(resolution != null){
						ELSegment segment = resolution.findSegmentByOffset(region.getOffset()-reference.getStartPosition());
	
						if(segment != null && segment.isResolved()){
								if(segment instanceof JavaMemberELSegment || segment instanceof MessagePropertyELSegment){
									return new IHyperlink[]{new ELHyperlink(textViewer.getDocument(), reference, segment, null)};
								}else{
									StringBuffer sbBuffer = new StringBuffer(); 
									for (ELSegment s : resolution.getSegments()) {
										sbBuffer.append(s.getToken().getText());
										if (s == segment) {
											break;
										}
										sbBuffer.append('.'); // Use default separator for ELs here
									}
									
									String text = sbBuffer.toString();
									XModelObject xObject = findJSF2CCAttributeXModelObject(text, file); 
									if (xObject != null) {
										return new IHyperlink[]{new ELHyperlink(textViewer.getDocument(), reference, segment, xObject)};
									}
								}
									
						}
						
					}
				}
			}
			
		}
		return null;
	}
	
	private ELInvocationExpression findInvocationExpressionByOffset(ELReference reference, int offset){
		ELExpression[] expressions = reference.getEl();
		for(ELExpression expression : expressions){
			if(reference.getStartPosition()+expression.getStartPosition() <= offset && reference.getStartPosition()+expression.getEndPosition() > offset){
				ELInvocationExpression invocation = findInvocationExpressionByOffset(reference, expression, offset);
				if(invocation != null)
					return invocation;
			}
		}
		return null;
	}
	
	private ELInvocationExpression findInvocationExpressionByOffset(ELReference reference, ELExpression expression, int offset){
		List<ELInvocationExpression> invocations = expression.getInvocations();
		for(ELInvocationExpression invocation : invocations){
			if(reference.getStartPosition()+invocation.getStartPosition() <= offset && reference.getStartPosition()+invocation.getEndPosition() > offset)
				return invocation;
		}
		return null;
	}
	
	private IFile getFile(){
		IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(part != null){
			IEditorInput input = part.getEditorInput();
			if(input instanceof FileEditorInput)
				return ((FileEditorInput)input).getFile();
		}
		return null;
	}
	
	static String[] vs = {"cc.attrs", "compositeComponent.attrs"}; //$NON-NLS-1$ //$NON-NLS-2$

	public static XModelObject findJSF2CCAttributeXModelObject(String varName, IFile file) {
		XModelObject xModelObject = EclipseResourceUtil.createObjectForResource(file);
		if(xModelObject == null) return null;
		if(!"FileJSF2Component".equals(xModelObject.getModelEntity().getName())) return null;

		IJavaProject javaProject = EclipseResourceUtil.getJavaProject(file.getProject());
		XModelObject is = xModelObject.getChildByPath("Interface");
		if(is != null && javaProject != null) {	
			for (int i = 0; i < vs.length; i++) {
				if (vs[i].equals(varName)) return is;
			}
			XModelObject[] cs = is.getChildren("JSF2ComponentAttribute");

			for (int i = 0; i < cs.length; i++) {
				String name = cs[i].getAttributeValue("name");
				String[] names = {vs[0] + "." + name, vs[1] + "." + name};
				for (String n: names) {
					if (n.equals(varName)) return cs[i];
				}
			}
		}
		return null;
	}
}
