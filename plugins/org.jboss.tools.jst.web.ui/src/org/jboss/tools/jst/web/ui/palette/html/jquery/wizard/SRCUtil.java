/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.html.jquery.wizard;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.common.web.WebUtils;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class SRCUtil {

	/**
	 * Returns URI of a resource presented by src in one of these ways:
	 * (1) path relative to folder containing IFile current.
	 * (2) path relative to one of Web roots of the project of IFile current.
	 * (3) URI starting with "file:" or "http:"
	 * 
	 * @param current
	 * @param src
	 * @return
	 */
	public static String getAbsoluteSrc(IFile current, String src) {
		if(src.startsWith("file:") || src.startsWith("http:") || src.length() == 0) {
			return src;
		}
		if(src.startsWith("/")) {
			for (IContainer root: WebUtils.getWebRootFolders(current.getProject(), true)) {
				IFile f = root.getFile(new Path(src.substring(1)));
				if(f.exists()) {
					return f.getLocationURI().toString();
				}
			}
		}
		IContainer container = current.getParent();
		while(src.startsWith("../") && container != null) {
			container = container.getParent();
			src = src.substring(3);
		}
		if(container != null) {
			return container.getLocationURI().toString() + "/" + src;
		}
		return src;
	}

	/**
	 * Returns relative path between selected file and current folder,
	 * if it does not leave Web roots of the current project, nor the project itself.
	 * If relative path cannot be calculated, URI to the selected file on disk is returned.
	 * 
	 * @param selected
	 * @param context
	 * @return
	 */
	public static String getRelativePath(IResource selected, IContainer current) {
		//1. Selected file is within current folder. Return path relative to it.
		if(current.getFullPath().isPrefixOf(selected.getFullPath())) {
			return selected.getFullPath().toString().substring(current.getFullPath().toString().length() + 1);
		}
		//2. Look for a Web root that contains selected file.
		for (IPath root: WebUtils.getWebContentPaths(current.getProject())) {
			if(root.isPrefixOf(selected.getFullPath())) {
				if(root.isPrefixOf(current.getFullPath())) {
					//2a. If that root also contains current folder,
					//then relative path between selected file and 
					//current folder does not leave Web root.
					//Break to calculate such path in (3). 
					break;
				}
				//2b. Calculate path relative to Web root, starting with '/'. 
				return selected.getFullPath().toString().substring(root.toString().length());
			}
		}
		//3. Calculate path relative to current folder if it does not leave current project.
		StringBuilder sb = new StringBuilder();
		current = current.getParent();
		while(current != null && (current.getType() == IResource.FOLDER || current.getType() == IResource.PROJECT)) {
			sb.append("../");
			if(current.getFullPath().isPrefixOf(selected.getFullPath())) {
				return sb.toString() + selected.getFullPath().toString().substring(current.getFullPath().toString().length() + 1);
			}
			current = current.getParent();
		}		
		//4. No relative paths are found.
		//Return URI to file on disk.
		return selected.getLocation().toFile().toURI().toString();
	}

	public static boolean isImageFile(String fileName) {
		int i = fileName.lastIndexOf('.');
		return (i >= 0) && IMAGE_EXTENSIONS.contains(fileName.substring(i).toLowerCase());
	}

	static Set<String> IMAGE_EXTENSIONS = new HashSet<String>();
	static {
		String[] exts = {
				".gif", ".png",
				".bmp", ".dib",
				//JPEG
				".jpg", ".jpeg", ".jpe", ".jif", ".jfif", ".jfi", ".jp2", ".j2k", ".jpf", 
				".jpx", ".jpm", ".mj2",	".tiff", ".tif", "wav",
				//Netpbm
				".ppm", ".pgm", ".pbm", ".pnm",
				//WebP
				".webp",
				//raw image format
				".3fr",	".ari", ".arw",	".bay",	".crw", ".cr2", ".cap",	".dcs", ".dcr", 
				".dng", ".drf",	".eip", ".erf",	".fff",	".iiq",	".k25", ".kdc",	".mdc", 
				".mef", ".mos", ".mrw",	".nef", ".nrw", ".obm", ".orf",	".pef", ".ptx", 
				".pxn",	".r3d", ".raf", ".raw", ".rwl", ".rw2", ".rwz",	".sr2", ".srf", 
				".srw",	".x3f"
				};
		for (String ext: exts) IMAGE_EXTENSIONS.add(ext);
	}

	public static boolean isVideoFile(String fileName) {
		//Should we try to filter.
		return true;
	}

	public static boolean isAudioFile(String fileName) {
		//Should we try to filter.
		return true;
	}

}
