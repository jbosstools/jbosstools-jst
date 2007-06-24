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
import java.util.Iterator;
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
import org.jboss.tools.jst.jsp.drop.treeviewer.model.ViewActionElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.ViewActionsResorceElement;


/**
 * @author Igels
 */
public class AttributeValueLabelProvider extends LabelProvider {	

	private static Map imagesPathes = new HashMap();
	static {
		imagesPathes.put(BundlesNameResourceElement.class, "images/jdo/db_fields_folder.gif");
		imagesPathes.put(BundleNameElement.class, "images/navigationtree/properties.gif");

		imagesPathes.put(BundlesPropertiesResourceElement.class, "images/jdo/db_fields_folder.gif");
		imagesPathes.put(BundleAliasElement.class, "images/navigationtree/properties.gif");
		imagesPathes.put(BundlePropertyElement.class, "images/navigationtree/property.gif");

		imagesPathes.put(EnumerationResourceElement.class, "images/file/closed_folder.gif");
		imagesPathes.put(EnumerationElement.class, "images/struts/pro/validator_field.gif");

		imagesPathes.put(JsfVariablesResourceElement.class, "images/file/closed_folder.gif");
		imagesPathes.put(JsfVariableElement.class, "images/struts/pro/validator_constant.gif");

		imagesPathes.put(ManagedBeanMethodResourceElement.class, "images/struts/form_beans.gif");
		imagesPathes.put(ManagedBeanForMdElement.class, "images/struts/form_bean.gif");
		imagesPathes.put(ManagedBeanMethodElement.class, "images/java/method.gif");

		imagesPathes.put(ManagedBeansPropertiesResourceElement.class, "images/struts/form_beans.gif");
		imagesPathes.put(ManagedBeanForPropElement.class, "images/struts/form_bean.gif");
		imagesPathes.put(ManagedBeanPropertyElement.class, "images/java/property.gif");

		imagesPathes.put(ViewActionsResorceElement.class, "images/struts/action_mappings.gif");
		imagesPathes.put(ViewActionElement.class, "images/map/map_rule_source.gif");

		imagesPathes.put(ImageFileResourceElement.class, "images/struts/web_root.gif");
		imagesPathes.put(ImageFolderElement.class, "images/file/closed_folder.gif");
		imagesPathes.put(ImageFileElement.class, "images/file/unknow_file.gif");
	}

	private static Map texts = new HashMap();
	static {
		texts.put(BundlesNameResourceElement.class, TreeViewerMessages.getString("BundlesNameResourceElement.name"));
		texts.put(BundlesPropertiesResourceElement.class, TreeViewerMessages.getString("BundlesPropertiesResourceElement.name"));
		texts.put(EnumerationResourceElement.class, TreeViewerMessages.getString("EnumerationResourceElement.name"));
		texts.put(JsfVariablesResourceElement.class, TreeViewerMessages.getString("JsfVariablesResourceElement.name"));
		texts.put(ManagedBeanMethodResourceElement.class, TreeViewerMessages.getString("ManagedBeanMethodResourceElement.name"));
		texts.put(ManagedBeansPropertiesResourceElement.class, TreeViewerMessages.getString("ManagedBeansPropertiesResourceElement.name"));
		texts.put(ViewActionsResorceElement.class, TreeViewerMessages.getString("ViewActionsResorceElement.name"));
	}

	private Map imageCache = new HashMap();

	/*
	 * @see ILabelProvider#getImage(Object)
	 */
	public Image getImage(Object element) {
		Image image = (Image)imageCache.get(element);
		if (image == null) {
			Object path = imagesPathes.get(element.getClass());
			if(path==null) {
				path = "images/process/error.gif";
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
		for (Iterator i = imageCache.values().iterator(); i.hasNext();) {
			((Image) i.next()).dispose();
		}
		imageCache.clear();
	}

	protected RuntimeException unknownElement(Object element) {
		return new RuntimeException("Unknown type of element in tree of type " + element.getClass().getName());
	}
}