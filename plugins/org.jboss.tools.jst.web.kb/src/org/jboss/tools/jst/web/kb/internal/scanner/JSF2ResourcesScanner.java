package org.jboss.tools.jst.web.kb.internal.scanner;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.XModelObjectConstants;
import org.jboss.tools.common.model.project.ext.IValueInfo;
import org.jboss.tools.common.model.project.ext.impl.ValueInfo;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.jboss.tools.jst.web.kb.internal.taglib.AbstractAttribute;
import org.jboss.tools.jst.web.kb.internal.taglib.composite.CompositeAttribute;
import org.jboss.tools.jst.web.kb.internal.taglib.composite.CompositeComponent;
import org.jboss.tools.jst.web.kb.internal.taglib.composite.CompositeTagLibrary;
import org.jboss.tools.jst.web.model.helpers.InnerModelHelper;
import org.jboss.tools.jst.web.model.project.ext.store.XMLValueInfo;

public class JSF2ResourcesScanner implements IFileScanner {
	public static String ATTR_SHORT_DESCRIPTION = "shortDescription"; //$NON-NLS-1$
	public static String URI_PREFIX = "http://java.sun.com/jsf/composite"; //$NON-NLS-1$

	public static String ENT_COMPOSITE_COMPONENT = "FileJSF2Component"; //$NON-NLS-1$

	public JSF2ResourcesScanner() {}

	public boolean isLikelyComponentSource(IFile f) {
		if(!f.isSynchronized(IFile.DEPTH_ZERO) || !f.exists()) return false;
		XModel model = InnerModelHelper.createXModel(f.getProject());
		if(model == null) return false;
		XModelObject o = EclipseResourceUtil.getObjectByResource(model, f);
		if(o == null) return false; 
		if(LibraryScanner.isCompositeComponentFile(o)) {
			IContainer c = f.getParent();
			while(c != null && c instanceof IFolder) {
				if("resources".equals(c.getName())) {
					return true;
				}
				c = c.getParent();
			}
		}
		return false;
	}

	public boolean isRelevant(IFile resource) {
		//only to be invoked on IFolder named 'resources'
		return false;
	}

	public LoadedDeclarations parse(IFile f, IKbProject sp) throws ScannerException {
		return null;
	}

	public LoadedDeclarations parse(IFolder f, IKbProject sp) throws ScannerException {
		XModel model = InnerModelHelper.createXModel(f.getProject());
		if(model == null) return null;
		XModelObject o = EclipseResourceUtil.getObjectByResource(model, f);
		return parse(o, f.getFullPath(), sp);
	}

	//XModelObject must represent folder named 'resources' under web root.
	public LoadedDeclarations parse(XModelObject o, IPath source, IKbProject sp) {
		if(o == null) return null;

		LoadedDeclarations ds = new LoadedDeclarations();
		processFolder(o, ds, source, URI_PREFIX);
		return ds;
	}

	void processFolder(XModelObject o, LoadedDeclarations ds, IPath source, String uriPrefix) {
		CompositeTagLibrary library = null;
		XModelObject[] cs = o.getChildren();
		for (XModelObject c: cs) {
			if(c.getFileType() == XModelObject.FOLDER) {
				String n = c.getAttributeValue(XModelObjectConstants.ATTR_NAME);
				processFolder(c, ds, source, uriPrefix + "/" + n); //$NON-NLS-1$
			}
			String entity = c.getModelEntity().getName();
			if(ENT_COMPOSITE_COMPONENT.equals(entity)) {
				if(library == null) {
					library = new CompositeTagLibrary();
					library.setId(o);
					library.setURI(createValueInfo(uriPrefix));
					ds.getLibraries().add(library);
				}
				
				CompositeComponent component = new CompositeComponent();
				component.setId(c);
				IResource r = (IResource)c.getAdapter(IResource.class);
				if(r instanceof IFile) {
					component.setSourcePath(r.getFullPath());
				}
				component.setName(createValueInfo(c.getAttributeValue(XModelObjectConstants.ATTR_NAME)));
				library.addComponent(component);
				
				XModelObject is = c.getChildByPath("Interface"); //$NON-NLS-1$
				if(is == null) continue;
				XModelObject[] as = is.getChildren();
				for (XModelObject a: as) {
					CompositeAttribute attr = new CompositeAttribute();
					attr.setId(a);
					attr.setName(new XMLValueInfo(a, XModelObjectConstants.ATTR_NAME));
					attr.setDescription(new XMLValueInfo(a, ATTR_SHORT_DESCRIPTION));
					attr.setRequired(new XMLValueInfo(a, AbstractAttribute.REQUIRED));
					
					component.addAttribute(attr);
				}
			}
		}
	}

	private IValueInfo createValueInfo(String value) {
		ValueInfo v = new ValueInfo();
		v.setValue(value);
		return v;
	}
}
