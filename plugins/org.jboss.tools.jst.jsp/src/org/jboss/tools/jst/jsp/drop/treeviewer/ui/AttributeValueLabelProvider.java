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
package org.jboss.tools.jst.jsp.drop.treeviewer.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.BundleAliasElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.BundleNameElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.BundlePropertyElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.BundlesNameResourceElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.BundlesPropertiesResourceElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.EnumerationElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.EnumerationResourceElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.IDResourceElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.ImageFileElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.ImageFileResourceElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.ImageFolderElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.JsfVariableElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.JsfVariablesResourceElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.ManagedBeanForMdElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.ManagedBeanForPropElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.ManagedBeanMethodElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.ManagedBeanMethodResourceElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.ManagedBeanPropertyElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.ManagedBeansPropertiesResourceElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.ModelElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.SeamMethodElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.SeamPropertyElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.SeamVariableElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.SeamVariablesResourceElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.ViewActionElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.ViewActionsResorceElement;


/**
 * @author Igels
 */
public class AttributeValueLabelProvider extends LabelProvider {	

	private static Map<Class<? extends ModelElement>, String> imagesPathes = new HashMap<Class<? extends ModelElement>, String>();
	static {
		imagesPathes.put(BundlesNameResourceElement.class, "images/jdo/db_fields_folder.gif"); //$NON-NLS-1$
		imagesPathes.put(BundleNameElement.class, "images/navigationtree/properties.gif"); //$NON-NLS-1$

		imagesPathes.put(BundlesPropertiesResourceElement.class, "images/file/closed_folder.gif"); //$NON-NLS-1$
		imagesPathes.put(BundleAliasElement.class, "images/navigationtree/properties.gif"); //$NON-NLS-1$
		imagesPathes.put(BundlePropertyElement.class, "images/navigationtree/property.gif"); //$NON-NLS-1$

		imagesPathes.put(EnumerationResourceElement.class, "images/file/closed_folder.gif"); //$NON-NLS-1$
		imagesPathes.put(EnumerationElement.class, "images/struts/pro/validator_field.gif"); //$NON-NLS-1$

		imagesPathes.put(JsfVariablesResourceElement.class, "images/file/closed_folder.gif"); //$NON-NLS-1$
		imagesPathes.put(JsfVariableElement.class, "images/struts/pro/validator_constant.gif"); //$NON-NLS-1$

		imagesPathes.put(ManagedBeanMethodResourceElement.class, "images/struts/form_beans.gif"); //$NON-NLS-1$
		imagesPathes.put(ManagedBeanForMdElement.class, "images/struts/form_bean.gif"); //$NON-NLS-1$
		imagesPathes.put(ManagedBeanMethodElement.class, "images/java/method.gif"); //$NON-NLS-1$

		imagesPathes.put(ManagedBeansPropertiesResourceElement.class, "images/struts/form_beans.gif"); //$NON-NLS-1$
		imagesPathes.put(ManagedBeanForPropElement.class, "images/struts/form_bean.gif"); //$NON-NLS-1$
		imagesPathes.put(ManagedBeanPropertyElement.class, "images/java/property.gif"); //$NON-NLS-1$

		imagesPathes.put(ViewActionsResorceElement.class, "images/struts/action_mappings.gif"); //$NON-NLS-1$
		imagesPathes.put(ViewActionElement.class, "images/map/map_rule_source.gif"); //$NON-NLS-1$

		imagesPathes.put(ImageFileResourceElement.class, "images/struts/web_root.gif"); //$NON-NLS-1$
		imagesPathes.put(ImageFolderElement.class, "images/file/closed_folder.gif"); //$NON-NLS-1$
		imagesPathes.put(ImageFileElement.class, "images/file/unknow_file.gif"); //$NON-NLS-1$

		imagesPathes.put(SeamVariablesResourceElement.class, "images/seam/variables_folder.gif"); //$NON-NLS-1$
		imagesPathes.put(SeamVariableElement.class, "images/seam/variable.gif"); //$NON-NLS-1$
		imagesPathes.put(SeamPropertyElement.class, "images/navigationtree/property.gif"); //$NON-NLS-1$
		imagesPathes.put(SeamMethodElement.class, "images/java/method.gif"); //$NON-NLS-1$

		imagesPathes.put(IDResourceElement.class, "images/file/closed_folder.gif"); //$NON-NLS-1$
}

	private static Map<Class<? extends ModelElement>, String> texts = new HashMap<Class<? extends ModelElement>, String>();
	static {
		texts.put(BundlesNameResourceElement.class, TreeViewerMessages.BundlesNameResourceElement_name);
		texts.put(BundlesPropertiesResourceElement.class, TreeViewerMessages.BundlesPropertiesResourceElement_name);
		texts.put(EnumerationResourceElement.class, TreeViewerMessages.EnumerationResourceElement_name);
		texts.put(JsfVariablesResourceElement.class, TreeViewerMessages.JsfVariablesResourceElement_name);
		texts.put(ManagedBeanMethodResourceElement.class, TreeViewerMessages.ManagedBeanMethodResourceElement_name);
		texts.put(ManagedBeansPropertiesResourceElement.class, TreeViewerMessages.ManagedBeansPropertiesResourceElement_name);
		texts.put(ViewActionsResorceElement.class, TreeViewerMessages.ViewActionsResorceElement_name);
	}

	private Map<Object, Image> imageCache = new HashMap<Object, Image>();

	/*
	 * @see ILabelProvider#getImage(Object)
	 */
	public Image getImage(Object element) {
		Image image = (Image)imageCache.get(element);
		if (image == null) {
			Object path = imagesPathes.get(element.getClass());
			if(path==null) {
				path = "images/java/error.gif"; //$NON-NLS-1$
			}
			image = EclipseResourceUtil.getImage((String)path);
			imageCache.put(element, image);
		}

		return image;
	}

	/*
	 * @see ILabelProvider#getText(Object)
	 */
	public String getText(Object element) {
		Object text = texts.get(element.getClass());
		if(text==null) {
			if (element instanceof ModelElement) {
				text = ((ModelElement)element).getName();
			} else {
				throw unknownElement(element);
			}
		}
		return (String)text;
	}

	public void dispose() {
		for (Image i: imageCache.values()) {
			i.dispose();
		}
		imageCache.clear();
	}

	protected RuntimeException unknownElement(Object element) {
		return new RuntimeException("Unknown type of element in tree of type " + element.getClass().getName()); //$NON-NLS-1$
	}
}