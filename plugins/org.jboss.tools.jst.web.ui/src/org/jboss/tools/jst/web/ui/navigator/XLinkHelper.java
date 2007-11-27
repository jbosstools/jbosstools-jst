package org.jboss.tools.jst.web.ui.navigator;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.navigator.ILinkHelper;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.ui.editor.IModelObjectEditorInput;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.model.util.FindObjectHelper;

public class XLinkHelper implements ILinkHelper {
	
	public XLinkHelper() {}

	public void activateEditor(IWorkbenchPage page,
			IStructuredSelection selection) {
		if(selection == null || selection.isEmpty() || !(selection instanceof IStructuredSelection)) {
			return;
		}
		IStructuredSelection s = (IStructuredSelection)selection;
		Object object = s.getFirstElement();
		if(object instanceof XModelObject) {
			XModelObject o = (XModelObject)object;
			FindObjectHelper.findModelObject(o, FindObjectHelper.IN_EDITOR_ONLY);
		}		
	}

	public IStructuredSelection findSelection(IEditorInput input) {
		if(input instanceof IModelObjectEditorInput) {
			IModelObjectEditorInput mi = (IModelObjectEditorInput)input;
			XModelObject o = mi.getXModelObject();
			return new StructuredSelection(o);
		} else if(input instanceof IFileEditorInput) {
			IFile file = ResourceUtil.getFile(input);
			if(file == null) return null;
			XModelObject o = EclipseResourceUtil.getObjectByResource(file);
			if(o != null) return new StructuredSelection(o);
		}
		return null;
	}

}
