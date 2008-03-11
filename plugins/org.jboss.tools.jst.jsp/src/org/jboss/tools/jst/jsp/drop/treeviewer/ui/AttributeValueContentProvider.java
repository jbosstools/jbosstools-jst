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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import org.jboss.tools.jst.jsp.drop.treeviewer.model.IAttributeValueContainer;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.ModelElement;

/**
 * @author Igels
 */
public class AttributeValueContentProvider implements ITreeContentProvider {

	private static Object[] EMPTY_ARRAY = new Object[0];
	protected TreeViewer viewer;

	private HashMap hidenElements = new HashMap();
	private HashMap treeParents = new HashMap();

	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof IAttributeValueContainer) {
			IAttributeValueContainer container = (IAttributeValueContainer)parentElement;
			ModelElement[] elements = container.getChildren();

			ArrayList resultList = getUniqueChildren(elements);

			Object hidens = hidenElements.get(parentElement);
			if(hidens!=null) {
				ArrayList hidenList = (ArrayList)hidens;
				for(int i=0; i<hidenList.size(); i++) {
					IAttributeValueContainer hiden = (IAttributeValueContainer)hidenList.get(i);
					ModelElement[] hidenElements = hiden.getChildren();
					ArrayList uniqueHidens = getUniqueChildren(hidenElements);
					ArrayList dirtyList = new ArrayList(resultList.size() + uniqueHidens.size());
					dirtyList.addAll(resultList);
					dirtyList.addAll(uniqueHidens);
					Object[] dirtyArray = dirtyList.toArray(new Object[dirtyList.size()]);
					resultList = getUniqueChildren(dirtyArray);
				}
			}
			return (Object[])resultList.toArray(new Object[resultList.size()]);
		}
		return EMPTY_ARRAY;
	}

	private ArrayList getUniqueChildren(Object[] children) {
		ArrayList resultList = new ArrayList();
		Set ignoreElements = new HashSet();
		for(int i=0; i<children.length; i++) {
			ArrayList hidenList = new ArrayList();
			if(!ignoreElements.contains(children[i])) {
				hidenList.contains(children[i]);
				resultList.add(children[i]);
			}
			for(int j=i+1; j<children.length; j++) {
				if((children[i].equals(children[j])) && (children[j] instanceof IAttributeValueContainer)) {
					hidenList.add(children[j]);
					treeParents.put(children[j], ((ModelElement)children[i]).getParent());
					ignoreElements.add(children[j]);
				}
			}
			if(hidenList.size()>0) {
				hidenElements.put(children[i], hidenList);
			}
		}
		return resultList;
	}

	public Object getParent(Object element) {
		if(element instanceof ModelElement) {
			Object treeParent = treeParents.get(element);
			if(treeParent==null) { 
				treeParent = ((ModelElement)element).getParent();
			}
			return treeParent;
		}
		return null;
	}

	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	/*
	 * @see IContentProvider#dispose()
	 */
	public void dispose() {
	}

	/*
	 * @see IContentProvider#inputChanged(Viewer, Object, Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TreeViewer)viewer;
	}
}