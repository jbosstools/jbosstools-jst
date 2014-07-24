/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.XModelObjectConstants;
import org.jboss.tools.common.model.event.XModelTreeListener;
import org.jboss.tools.common.model.options.SharableConstants;
import org.jboss.tools.common.model.ui.util.ModelUtilities;
import org.jboss.tools.common.model.ui.views.palette.PaletteContents;
import org.jboss.tools.common.model.ui.views.palette.editor.PaletteEditor;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.PagePaletteContents;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;

public class PaletteModel {
	public static String TYPE_HTML5 = PaletteContents.TYPE_MOBILE;
	public static String TYPE_JSF = PaletteContents.TYPE_JSF;
	
	private static Map<String, PaletteModel> instances = new HashMap<String, PaletteModel>();
	private static Object monitor = new Object();

	/**
	 * Path to mobile palette items in internal model.
	 */
	public static String MOBILE_PATH = "%Palette%/Mobile";
	
	/**
	 * Prefix used in a group name under category to set its version,
	 * for example 'version:1.4'.
	 */
	public static String VERSION_PREFIX = "version:";

	private PaletteEditor editor = new PaletteEditor();
	private PaletteRoot paletteRoot = null;
	private String type = TYPE_HTML5;

	PagePaletteContents contents = null;

	private PaletteModel() {
	}
	
	public String getType(){
		return type;
	}

	public static final PaletteModel getInstance(PagePaletteContents contents) {
		String[] natures = contents.getNatureTypes();
		boolean jsf = natures != null && natures.length > 0 && natures[0].equals(TYPE_JSF);
		String type = jsf ? TYPE_JSF : TYPE_HTML5;
		String code = type;
		IFile file = contents.getFile();
		if(!jsf && file != null) {
			code = file.getFullPath().toString();
		}
		PaletteModel instance = instances.get(code);
		if (instance != null) {
			if(file != null) {
				instance.setPaletteContents(contents);
				instance.load(null);
			}
			return instance;
		} else {
			synchronized (monitor) {
				if (instance == null) {
					PaletteModel inst = new PaletteModel();
					if(file != null) {
						inst.setPaletteContents(contents);
					}
					inst.type = type;
					inst.createModel();
					instance = inst;
					instances.put(code, instance);
				}
			}
			if(partListener == null) {
				WebUiPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getPartService().addPartListener(partListener = new PL());
			}
			return instance;
		}
	}

	public static void disposeInstance(PagePaletteContents contents) {
		IFile file = contents.getFile();
		if(file != null) {
			String code = file.getFullPath().toString();
			instances.remove(code);
		}

	}

	static IPartListener partListener = null;
	private static class PL implements IPartListener {

		@Override
		public void partActivated(IWorkbenchPart part) {
			if(part instanceof IEditorPart) {
				IEditorInput input = ((IEditorPart)part).getEditorInput();
				if(input instanceof IFileEditorInput) {
					IFile file = ((IFileEditorInput)input).getFile();
					PaletteModel instance = instances.get(file.getFullPath().toString());
					if(instance != null) {
						instance.getPreferredExpandedCategory();
					}
				}
			}
		}

		@Override
		public void partBroughtToTop(IWorkbenchPart part) {}

		@Override
		public void partClosed(IWorkbenchPart part) {}

		@Override
		public void partDeactivated(IWorkbenchPart part) {}

		@Override
		public void partOpened(IWorkbenchPart part) {}
		
	}

	public XModel getXModel() {
		return ModelUtilities.getPreferenceModel();
	}

	private XModelObject getXPaletteRoot() {
		return getXModel().getRoot("Palette"); //$NON-NLS-1$
	}

	private XModelObject[] findXObjects(XModelObject root, String elementType){
		ArrayList<XModelObject> v = new ArrayList<XModelObject>();
		for (int i = 0; i < root.getChildren().length; i++) {
			if (root.getChildAt(i).getAttributeValue("element type").equals(elementType)) { //$NON-NLS-1$
				v.add(root.getChildAt(i));
			}
		}
		return (v.size() == 0) ? null : (XModelObject[])v.toArray(new XModelObject[0]);
	}
	
	private XModelObject[] getGroups(XModelObject root) {
		ArrayList<XModelObject> v = new ArrayList<XModelObject>();
		collectGroups(root, v, true);
		return (v.size() == 0) ? new XModelObject[0] : (XModelObject[])v.toArray(new XModelObject[0]);
	}
	
	private void collectGroups(XModelObject o, ArrayList<XModelObject> list, boolean isRoot) {
		XModelObject[] cs = o.getChildren();
		for (int i = 0; i < cs.length; i++) {
			XModelObject c = cs[i];
			boolean isHidden = XModelObjectConstants.YES.equals(c.getAttributeValue(SharableConstants.ATTR_HIDDEN));
			if(isRoot) {
				String name = c.getAttributeValue(XModelObjectConstants.ATTR_NAME);
				boolean m1 = name.toLowerCase().equals(TYPE_HTML5);
				boolean m2 = type.equals(TYPE_HTML5);
				if(m1 != m2) continue;
				if(isHidden && !m1) {
					continue;
				}
			} else if(isHidden) {
				continue;
			}
			if(PaletteModelHelper.isSubGroup(c)) {
				list.add(c);
			} else if (PaletteModelHelper.isGroup(c)) {
				collectGroups(c, list, false);
			}
		}
	}
	
	private void createModel() {
		load(null);
	}
	
	public PaletteRoot getPaletteRoot() {
		return paletteRoot;
	}

	public void load(XModelObject lastAddedXCat) {
		XModelObject xpalette = getXPaletteRoot();
		if (paletteRoot == null) {
			paletteRoot = new PaletteRoot(xpalette);
			paletteRoot.setPaletteModel(this);
		}
		if (xpalette == null) return;
		XModelObject[] xcats = getGroups(xpalette);
		if (xcats == null) return;
		int i = 0; 
		for (int l = 0; l < xcats.length; l++) {
			if (XModelObjectConstants.YES.equals(xcats[l].getAttributeValue(SharableConstants.ATTR_HIDDEN))) continue;
			int j = indexOf(paletteRoot, xcats[l], i);
			if (j == -1) {
				paletteRoot.add(i, createCategory(xcats[l], lastAddedXCat ==xcats[l]));
			} else {
				PaletteCategory cat = (PaletteCategory)getEntry(paletteRoot, j);
				if (i < j) {
					moveUp(paletteRoot, cat, j - i);
				}
				cat.setXModelObject(xcats[l]);
				cat.setVisible(isCategoryVisible(cat));
				loadCategory(xcats[l], cat);
			}
			i++;
		}
		cutOff(paletteRoot, i);
		if(lastAddedXCat == null) {
			String preferred = getPreferredExpandedCategory();
			if(preferred != null) {
				for (Object c: paletteRoot.getChildren()) {
					if(c instanceof PaletteCategory) {
						PaletteCategory cat = (PaletteCategory)c;
						if(preferred.equals(cat.getLabel())) {
							cat.setInitialState(PaletteCategory.INITIAL_STATE_OPEN);
						}
					}
				}
			}
		}
	}

	public void reloadCategory(PaletteCategory cat) {
		loadCategory(cat.getXModelObject(), cat);
	}
	
	private PaletteCategory createCategory(XModelObject xcat, boolean open) {
		PaletteCategory cat = new PaletteCategory(xcat, open);
		cat.setPaletteModel(this);
		cat.setVisible(isCategoryVisible(cat));
		loadCategory(xcat, cat);
		return cat;
	}

	private void loadCategory(XModelObject xcat, PaletteCategory cat) {
			List list = new ArrayList(cat.getChildren());
			for (Object o : list) cat.remove((PaletteEntry)o);

		XModelObject[] xitems = xcat.getChildren();

		if(xitems.length > 0 && xitems[0].getAttributeValue(XModelObjectConstants.ATTR_NAME).startsWith(VERSION_PREFIX)) {
			xcat = findSelectedVersion(xcat, cat);
			xitems = xcat.getChildren();
		}

		int i = 0;
		for (int l = 0; l < xitems.length; l++) {
			if (xitems[l].getAttributeValue("element type").equals("macro")) { //$NON-NLS-1$ //$NON-NLS-2$
				PaletteItem item = (PaletteItem)getEntry(cat, i++);
				if (item == null) {
					cat.add(new PaletteItem(xitems[l]));
				} else {
					item.setXModelObject(xitems[l]);
				}
			} else if(xitems[l].getAttributeValue("element type").equals("sub-group")) {
				XModelObject[] xitems2 = xitems[l].getChildren();
				for (int l2 = 0; l2 < xitems2.length; l2++) {
					PaletteItem item = (PaletteItem)getEntry(cat, i++);
					if (item == null) {
						cat.add(new PaletteItem(xitems2[l2]));
					} else {
						item.setXModelObject(xitems2[l2]);
					}
				}
				cat.add(new PaletteSeparator());
				i++;
			}
		}
		cutOff(cat, i);
	}

	private XModelObject findSelectedVersion(XModelObject xcat, PaletteCategory cat) {
		String name = xcat.getAttributeValue(XModelObjectConstants.ATTR_NAME);
		XModelObject[] os = xcat.getChildren();
		Set<String> versions = new TreeSet<String>();
		for (XModelObject c: os) {
			String n = c.getAttributeValue(XModelObjectConstants.ATTR_NAME);
			if(n.startsWith(VERSION_PREFIX)) {
				n = n.substring(VERSION_PREFIX.length());
				versions.add(n);
			}
		}
		String[] availableVersions = versions.toArray(new String[0]);

		String selectedVersion = getSelectedVersion(name);
		
		if(selectedVersion == null || xcat.getChildByPath(VERSION_PREFIX + selectedVersion) == null) {
			selectedVersion = availableVersions[availableVersions.length - 1];
		}
		
		XModelObject selected = xcat.getChildByPath(VERSION_PREFIX + selectedVersion);
		
		cat.setAvailableVersions(availableVersions);
		cat.setVersion(selectedVersion);
		
		return selected;
	}

	private String getSelectedVersion(String categoryName) {
		return contents == null ? null : contents.getVersion(categoryName);
	}
	
	private int indexOf(PaletteContainer container, XModelObject xobject, int startIndex) {
		List v = container.getChildren();
		if (v != null) {
			int max = v.size();
			for (int i = startIndex; i < max; i++) {
				if (xobject ==((PaletteXModelObject)v.get(i)).getXModelObject()) {
					return i;
				}
			}
		}
		return -1;
	}
	
	private PaletteEntry getEntry(PaletteContainer container, int index) {
		List v = container.getChildren();
		if (index <v.size()) {
			return (PaletteEntry)v.get(index);
		} else {
			return null;
		}
	}
	
	private void moveUp(PaletteContainer container, PaletteEntry entry, int height) {
		for (int i = 0; i < height; i++) {
			container.moveUp(entry);
		}
	}
	
	private void cutOff(PaletteContainer container, int size) {
		List v = container.getChildren();
		if (v != null) {
			int oldSize = v.size();
			if (oldSize > size) {
				for (int i = oldSize - 1; i >= size; i--) {
					container.remove((PaletteEntry)v.get(i));
				}
			}
		}
	}

	public void addModelTreeListener(XModelTreeListener listener) {
		getXModel().addModelTreeListener(listener);
	}

	public void removeModelTreeListener(XModelTreeListener listener) {
		getXModel().removeModelTreeListener(listener);
	}
	
	public void openEditor(Shell shell) {
		editor.setObject(shell);
		editor.execute();
	}

	public void runShowHideDialog() {
		XActionInvoker.invoke("HiddenTabs", paletteRoot.getXModelObject(), new java.util.Properties()); //$NON-NLS-1$
	}

	public void runImportTLDDialog() {
		XActionInvoker.invoke("ImportTLDToPaletteWizard", "CreateActions.ImportTLD", paletteRoot.getXModelObject(), new java.util.Properties()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private boolean isCategoryVisible(PaletteCategory cat) {
		return true; ///paletteContents.contains(cat.getNatureTypes(), cat.getEditorTypes()); 
	}

	public void setPaletteContents(PagePaletteContents contents) {
		this.contents = contents;
	}

	public PagePaletteContents getPaletteContents() {
		return contents;
	}

	static String HTML5_EXPANDED_CATEGORY = WebUiPlugin.PLUGIN_ID + ".HTML5_EXPANDED_CATEGORY";
	static QualifiedName HTML5_EXPANDED_CATEGORY_NAME = new QualifiedName(WebUiPlugin.PLUGIN_ID, "HTML5_EXPANDED_CATEGORY");

	public void onCategoryExpandChange(String name, boolean state) {
		if(contents != null && type == TYPE_HTML5) {
			IFile f = contents.getFile();
			if(state) {
				try {
					f.setPersistentProperty(HTML5_EXPANDED_CATEGORY_NAME, name);
				} catch (CoreException e) {
					WebUiPlugin.getDefault().logError(e);
				}
				WebUiPlugin.getDefault().getPreferenceStore().setValue(HTML5_EXPANDED_CATEGORY, name);
			}
		}
	}

	private String getPreferredExpandedCategory() {
		if(contents != null && type == TYPE_HTML5) {
			IFile f = contents.getFile();
			try {
				String s = f.getPersistentProperty(HTML5_EXPANDED_CATEGORY_NAME);
				if(s == null || s.length() == 0) {
					s = WebUiPlugin.getDefault().getPreferenceStore().getString(HTML5_EXPANDED_CATEGORY);
					if(s == null || s.length() == 0) {
						s = JQueryConstants.JQM_CATEGORY;
					}
					f.setPersistentProperty(HTML5_EXPANDED_CATEGORY_NAME, s);
				} else {
					WebUiPlugin.getDefault().getPreferenceStore().setValue(HTML5_EXPANDED_CATEGORY, s);
				}
				return s; 
			} catch (CoreException e) {
				WebUiPlugin.getDefault().logError(e);
			}
		}
		return null;
	}
}
