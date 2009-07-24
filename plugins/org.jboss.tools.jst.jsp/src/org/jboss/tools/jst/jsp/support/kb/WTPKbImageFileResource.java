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
package org.jboss.tools.jst.jsp.support.kb;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.FileSystemsHelper;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.outline.ValueHelper;
import org.jboss.tools.jst.web.tld.FilePathEncoderFactory;
import org.jboss.tools.jst.web.tld.IFilePathEncoder;

/**
 * @author Igels
 */
public class WTPKbImageFileResource extends WTPKbAbstractModelResource {
	static Set<String> GRAPHIC_FILE_EXTENSIONS = new HashSet<String>();
	static Set<String> PAGE_FILE_EXTENSIONS = new HashSet<String>();
	String pathType = IFilePathEncoder.ABSOLUTE_PATH;
	String pathAddition = null;
	 
	static {
		String[] images = {"gif", "jpeg", "jpg", "png", "wbmp", "bmp"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		for (int i = 0; i < images.length; i++) GRAPHIC_FILE_EXTENSIONS.add(images[i]);
		String[] pages = {"jsp", "htm", "html", "xhtml", "xml"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		for (int i = 0; i < pages.length; i++) PAGE_FILE_EXTENSIONS.add(pages[i]);
	}

	private IContainer webInfResource = null;
	private IContainer webRootResource = null;
	private IFile currentResource = null;
	
	Set<String> extensions = null;

	public WTPKbImageFileResource(IEditorInput editorInput) {
		super(editorInput);
		if(fXModel != null) {
			XModelObject webInf = FileSystemsHelper.getWebInf(fXModel);
			XModelObject webRoot = FileSystemsHelper.getWebRoot(fXModel);
			if(webInf != null && webRoot != null) {
				webInfResource = (IContainer)EclipseResourceUtil.getResource(webInf);
				webRootResource = (IContainer)EclipseResourceUtil.getResource(webRoot);
			}
		}
		if(editorInput instanceof IFileEditorInput) {
			currentResource = ((IFileEditorInput)editorInput).getFile();
		}
	}

	public void setConstraint(String name, String value) {
		if("extensions".equals(name)) { //$NON-NLS-1$
			loadExtensions(value);
		} else if(IFilePathEncoder.PATH_ADDITION.equals(name)) {
			pathAddition = value;
		} else if(IFilePathEncoder.PATH_TYPE.equals(name)) {
			pathType = value;
		}
	}

	public void clearConstraints() {
		extensions = null;
		pathType = IFilePathEncoder.ABSOLUTE_PATH;
		pathAddition = null;
	}
	
	void loadExtensions(String value) {
		if(value != null && !value.equals("*")) { //$NON-NLS-1$
			if("%image%".equals(value)) { //$NON-NLS-1$
				this.extensions = GRAPHIC_FILE_EXTENSIONS;
			} else if("%page%".equals(value)) { //$NON-NLS-1$
				this.extensions = PAGE_FILE_EXTENSIONS;
			} else {
				StringTokenizer st = new StringTokenizer(value, ",;"); //$NON-NLS-1$
				if(st.countTokens() > 0) {
					extensions = new HashSet<String>();
					while(st.hasMoreTokens()) {
						String t = st.nextToken().trim();
						if(t.length() == 0) continue;
						if("%image%".equals(t)) extensions.addAll(GRAPHIC_FILE_EXTENSIONS); //$NON-NLS-1$
						else if("%page%".equals(t)) extensions.addAll(PAGE_FILE_EXTENSIONS); //$NON-NLS-1$
						else extensions.add(t);
					}
				}
			}
		}
	}

	public ImagePathDescriptor[] getImagesFilesPathes(String query) {
		query = query.trim();
		if(query.indexOf('\\')>-1) {
			return new ImagePathDescriptor[0];
		}
		if(query.length()==0) {
			query = "/"; //$NON-NLS-1$
		}
		int lastSeparator = query.lastIndexOf('/');
		String name = null;
		String pathWithoutLastSegment = null;
		if(lastSeparator>-1) {
			pathWithoutLastSegment = query.substring(0, lastSeparator);
			if(lastSeparator+1<query.length()) {
				name = query.substring(lastSeparator+1, query.length());
			} else {
				name = ""; //$NON-NLS-1$
			}
		} else {
			pathWithoutLastSegment = ""; //$NON-NLS-1$
			name = query;
		}
		if(name.equals(".") || name.equals("..")) { //$NON-NLS-1$ //$NON-NLS-2$
			if(pathWithoutLastSegment.length()>0) {
				pathWithoutLastSegment = pathWithoutLastSegment + "/" + name; //$NON-NLS-1$
			} else {
				if(query.startsWith("/")) { //$NON-NLS-1$
					pathWithoutLastSegment = "/" + name; //$NON-NLS-1$
				} else {
					pathWithoutLastSegment = name;
				}
			}
			name = ""; //$NON-NLS-1$
		}
		if(name==null) {
			name = ""; //$NON-NLS-1$
		}
		IResource resource;
		String startPath = pathWithoutLastSegment;
		if(pathWithoutLastSegment.startsWith("/")) { //$NON-NLS-1$
			if(pathWithoutLastSegment.length()>1) {
				startPath = pathWithoutLastSegment.substring(1);
			} else {
				startPath = ""; //$NON-NLS-1$
			}
		}
		if(query.startsWith("/")) { //$NON-NLS-1$
			resource = webRootResource.findMember(startPath);
		} else {
			resource = currentResource.getParent().findMember(startPath);
		}

		List<IResource> resources = new ArrayList<IResource>();
		try {
			if(resource != null) resource.accept(new ImagesFinder(resources, name, extensions));
		} catch (CoreException e) {
			JspEditorPlugin.getPluginLog().logError(e);
		}
		ImagePathDescriptor[] filesPathes = new ImagePathDescriptor[resources.size()];
		for(int i=0; i<filesPathes.length; i++) {
			String prefix = pathWithoutLastSegment.toString();
			if(!prefix.endsWith("/")) { //$NON-NLS-1$
				prefix = prefix + '/';
			}
			IResource r = (IResource)resources.get(i);
			filesPathes[i] = new ImagePathDescriptor(prefix + r.getName(), r);
		}
		return filesPathes;
	}

	public boolean isReadyToUse() {
		return (webInfResource!=null) && (webRootResource!=null) && (currentResource!=null);
	}

	public IContainer getWebRootResource() {
		return webRootResource;
	}

	public static class ImagePathDescriptor {

		private String queryPath;
		private IResource resource;

		public ImagePathDescriptor(String queryPath, IResource resource) {
			this.queryPath = queryPath;
			this.resource = resource;
		}

		public String getQueryPath() {
			return queryPath;
		}

		public IResource getResource() {
			return resource;
		}
	}
	
	public String getPathAddition() {
		return pathAddition;
	}
	
	IFilePathEncoder encoder = null;
	
	public String encodePath(String path, String query, ValueHelper valueHelper) {
		if(valueHelper == null) return path;
		encoder = valueHelper.getProject() == null ? null : FilePathEncoderFactory.getEncoder(valueHelper.getProject());
		if(encoder == null) return path;
		if(fXModelObject == null) return path;
		Properties context = new Properties();
		context.setProperty(IFilePathEncoder.PATH_TYPE, pathType);
		if(pathAddition != null) context.setProperty(IFilePathEncoder.PATH_ADDITION, pathAddition);
		path = encoder.encode(path, fXModelObject, query, valueHelper.getTaglibManager(), context);
		return path;
	}

}

class ImagesFinder implements IResourceVisitor {
	private List<IResource> resources;
	private int count = 0;
	private String name;
	Set extensions = null;

	public ImagesFinder(List<IResource> resources, String name, Set extensions) {
		this.resources = resources;
		this.name = name;
		this.extensions = extensions;
	}
	
	boolean acceptExtension(String ext) {
		if(ext != null) {
//			ext = FilePathHelper.toPathPath(ext); What about UNIX?
	        ext = ext.toLowerCase();
		}
		return (extensions == null || extensions.contains(ext));
	}

	public boolean visit(IResource resource) throws CoreException {
		if(resource instanceof IFile) {
			IFile file = (IFile)resource;
			if(resource.getName().startsWith(name) &&
					acceptExtension(file.getFileExtension())) {
				resources.add(resource);
			}
		} else if(resource instanceof IFolder) {
			if(count==0) {
				count++;
				return true;
			} else if(resource.getName().startsWith(name) && (!resource.getName().equals("WEB-INF")) && (!resource.getName().equals("META-INF"))) { //$NON-NLS-1$ //$NON-NLS-2$
				resources.add(resource);
			}
		}
		return false;
	}
}