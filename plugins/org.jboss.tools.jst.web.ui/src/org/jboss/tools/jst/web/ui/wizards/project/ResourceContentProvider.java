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
package org.jboss.tools.jst.web.ui.wizards.project;

import java.io.*;
import java.util.List;

import org.eclipse.jface.viewers.*;

public class ResourceContentProvider implements ITreeContentProvider {
	public static int FOLDER = 1;
	public static int FILE = 2;
	int type;
	FileFilter filter = new FileFilterImpl();
	File root = null;
	
	public ResourceContentProvider(int type) {
		this.type = type;
	}
	
	public Object[] getChildren(Object parentElement) {
		File f = (File)parentElement;
		return f.listFiles(filter);
	}

	public Object getParent(Object element) {
		if(root == null) return null;
		if(root == element || root.equals(element)) return null;
		File f = (File)element;
		if(f.getAbsolutePath().length() < root.getAbsolutePath().length()) return null;
		return f.getParentFile();
	}

	public boolean hasChildren(Object element) {
		File f = (File)element;
		if(!f.isDirectory()) return false;
		File[] fs = f.listFiles(filter);
		return fs != null && fs.length > 0;
	}

	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof File) {
			return getChildren(inputElement);
		} else if(inputElement instanceof List) {
			return (Object[])((List)inputElement).toArray();
		}
		return null;
	}

	public void dispose() {
		root = null;
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if(newInput instanceof List) {
			List list = (List)newInput;
			newInput = list.isEmpty() ? null : (File)list.get(0);
		}
		File newRoot = (File)newInput;
		if(root != null && !root.equals(newRoot)) {
			root = newRoot;
			if(viewer.getControl() != null && !viewer.getControl().isDisposed()) {
				try {
					viewer.refresh();
				} catch(Exception e) {
					//ignore
				}
			}
		} else {
			root = newRoot;
		}
	}
	
	class FileFilterImpl implements FileFilter {
		public boolean accept(File pathname) {
			if(pathname.isDirectory()) return (type & FOLDER) != 0;
			if(pathname.isFile()) return (type & FILE) != 0;
			return false;
		}
		
	}

}
