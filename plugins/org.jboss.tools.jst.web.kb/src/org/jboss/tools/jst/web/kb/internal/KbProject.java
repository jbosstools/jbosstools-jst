/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.common.model.project.ext.event.Change;
import org.jboss.tools.common.model.project.ext.store.XMLStoreConstants;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.jboss.tools.jst.web.kb.KbMessages;
import org.jboss.tools.jst.web.kb.KbProjectFactory;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.internal.scanner.ClassPathMonitor;
import org.jboss.tools.jst.web.kb.internal.scanner.LoadedDeclarations;
import org.jboss.tools.jst.web.kb.internal.taglib.AbstractTagLib;
import org.jboss.tools.jst.web.kb.internal.taglib.FaceletTagLibrary;
import org.jboss.tools.jst.web.kb.internal.taglib.FacesConfigTagLibrary;
import org.jboss.tools.jst.web.kb.internal.taglib.TLDLibrary;
import org.jboss.tools.jst.web.kb.internal.taglib.composite.CompositeTagLibrary;
import org.jboss.tools.jst.web.kb.internal.validation.ProjectValidationContext;
import org.jboss.tools.jst.web.kb.taglib.ICustomTagLibrary;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;
import org.jboss.tools.jst.web.kb.validation.IValidationContext;
import org.w3c.dom.Element;

/**
 * 
 * @author V.Kabanovich
 *
 */
public class KbProject extends KbObject implements IKbProject {
	IProject project;
	
	boolean isMock = false;

	ClassPathMonitor classPath = new ClassPathMonitor(this);

//	Set<IPath> sourcePaths = new HashSet<IPath>();
	
	Map<IPath, LoadedDeclarations> sourcePaths2 = new HashMap<IPath, LoadedDeclarations>();
	
	private boolean isStorageResolved = false;
	
	Set<KbProject> dependsOn = new HashSet<KbProject>();
	
	Set<KbProject> usedBy = new HashSet<KbProject>();
	
	LibraryStorage libraries = new LibraryStorage();

	ProjectValidationContext validationContext;

	public KbProject() {}

	public void setMock() {
		isMock = true;
	}
	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.IKbProject#getTagLibraries()
	 */
	public ITagLibrary[] getTagLibraries() {
		return libraries.getAllLibrariesArray();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.IKbProject#getTagLibraries(java.lang.String)
	 */
	public ITagLibrary[] getTagLibraries(String uri) {
		ITagLibrary[] result = libraries.getLibrariesArray(uri);
		if(result == null || result.length == 0) {
			result = StaticLibraries.instance.getLibraries(uri);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.resources.IProjectNature#configure()
	 */
	public void configure() throws CoreException {
		addToBuildSpec(KbBuilder.BUILDER_ID);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.resources.IProjectNature#deconfigure()
	 */
	public void deconfigure() throws CoreException {
		removeFromBuildSpec(KbBuilder.BUILDER_ID);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.resources.IProjectNature#getProject()
	 */
	public IProject getProject() {
		return project;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.KbObject#getKbProject()
	 */
	@Override
	public IKbProject getKbProject() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.resources.IProjectNature#setProject(org.eclipse.core.resources.IProject)
	 */
	public void setProject(IProject project) {
		this.project = project;
		setSourcePath(project.getFullPath());
		resource = project;
		classPath.init();
	}

	/**
	 * 
	 * @param p
	 */
	public void addKbProject(KbProject p) {
		if(dependsOn.contains(p)) return;
		dependsOn.add(p);
		p.addDependentKbProject(this);
		if(!p.isStorageResolved) {
			p.resolve();
		} else {
			Map<IPath,LoadedDeclarations> map = null;
			try {
				map = p.getAllDeclarations();
			} catch (CloneNotSupportedException e) {
				WebModelPlugin.getPluginLog().logError(e);
			}
			for (IPath source : map.keySet()) {
				LoadedDeclarations ds = map.get(source);
				registerComponents(ds, source);
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<KbProject> getKbProjects() {
		return dependsOn;
	}
	
	/**
	 * 
	 * @param p
	 */
	public void addDependentKbProject(KbProject p) {
		usedBy.add(p);
	}

	/**
	 * 
	 * @param p
	 */
	public void removeKbProject(KbProject p) {
		if(!dependsOn.contains(p)) return;
		p.usedBy.remove(this);
		dependsOn.remove(p);
		IPath[] ps = null;
		synchronized(sourcePaths2) {
			ps = sourcePaths2.keySet().toArray(new IPath[0]);
		}
		for (int i = 0; i < ps.length; i++) {
			IPath pth = ps[i];
			if(p.getSourcePath().isPrefixOf(pth) || (p.isPathLoaded(pth) && !EclipseResourceUtil.isJar(pth.toString()))) {
				pathRemoved(pth);
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public ClassPathMonitor getClassPath() {
		return classPath;
	}
	
	/**
	 * 
	 * @param load
	 */
	public void resolveStorage(boolean load) {
		if(isStorageResolved) return;
		if(load) {
			load();
		} else {
			isStorageResolved = true;
		}
	}

	/**
	 * 
	 */
	public void resolve() {
		resolveStorage(true);
	}

	/**
	 * Loads results of last build, which are considered 
	 * actual until next build.
	 */	
	public void load() {
		if(isStorageResolved) return;
		isStorageResolved = true;
		
		postponeFiring();
		
		try {
		
			boolean b = getClassPath().update();
			if(b) {
				getClassPath().validateProjectDependencies();
			}
			File file = getStorageFile();
			Element root = null;
			if(file != null && file.isFile()) {
				root = XMLUtilities.getElement(file, null);
				if(root != null) {
					loadProjectDependencies(root);
					if(XMLUtilities.getUniqueChild(root, "paths") != null) { //$NON-NLS-1$
						loadSourcePaths2(root);
					}
				}
			}

			if(b) {
				getClassPath().process();
			}
			
			if(root != null) {
				getValidationContext().load(root);
			}

		} finally {
			fireChanges();
		}

	}

	public void clean() {
		File file = getStorageFile();
		if(file != null && file.isFile()) {
			file.delete();
		}
		classPath.clean();
		postponeFiring();
		IPath[] ps = null;
		synchronized(sourcePaths2) {
			ps = sourcePaths2.keySet().toArray(new IPath[0]);
		}
		for (int i = 0; i < ps.length; i++) {
			pathRemoved(ps[i]);
		}
		fireChanges();
	}
	
	public long fullBuildTime;
	public List<Long> statistics;


	/**
	 * Method testing how long it takes to load Kb model
	 * serialized previously.
	 * This approach makes sure, that all other services 
	 * (JDT, XModel, etc) are already loaded at first start of 
	 * Kb model, so that now it is more or less pure time 
	 * to be computed.
	 * 
	 * @return
	 */
	public long reload() {
		statistics = new ArrayList<Long>();
		classPath = new ClassPathMonitor(this);
//		sourcePaths.clear();
		synchronized(sourcePaths2) {
			sourcePaths2.clear();
		}
		isStorageResolved = false;
		dependsOn.clear();
		usedBy.clear();
		libraries.clear();
		
		long begin = System.currentTimeMillis();

		classPath.init();
		resolve();

		long end = System.currentTimeMillis();
		return end - begin;
	}

	/**
	 * Stores results of last build, so that on exit/enter Eclipse
	 * load them without rebuilding project
	 * @throws IOException 
	 */
	public void store() throws IOException {
		try {
			if(!project.hasNature(IKbProject.NATURE_ID)) return;
		} catch (CoreException e) {
			//ignore
		}
		File file = getStorageFile();
		file.getParentFile().mkdirs();
		
		Element root = XMLUtilities.createDocumentElement("kb-project"); //$NON-NLS-1$
		storeProjectDependencies(root);

		storeSourcePaths2(root);

		if(validationContext != null) validationContext.store(root);
		
		XMLUtilities.serialize(root, file.getAbsolutePath());
	}

	/*
	 * 
	 */
	private File getStorageFile() {
		IPath path = WebKbPlugin.getDefault().getStateLocation();
		File file = new File(path.toFile(), "projects/" + project.getName() + ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
		return file;
	}
	
	public void clearStorage() {
		File f = getStorageFile();
		if(f != null && f.isFile()) f.delete();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.IKbProject#getValidationContext()
	 */
	public IValidationContext getValidationContext() {
		if(validationContext == null) {
			validationContext = new ProjectValidationContext();
		}
		return validationContext;
	}

	/*
	 * 
	 */
	private void loadProjectDependencies(Element root) {
		Element dependsOnElement = XMLUtilities.getUniqueChild(root, "depends-on-projects"); //$NON-NLS-1$
		if(dependsOnElement != null) {
			Element[] paths = XMLUtilities.getChildren(dependsOnElement, "project"); //$NON-NLS-1$
			for (int i = 0; i < paths.length; i++) {
				String p = paths[i].getAttribute("name"); //$NON-NLS-1$
				if(p == null || p.trim().length() == 0) continue;
				IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(p);
				if(project == null || !project.isAccessible()) continue;
				checkKBBuilderInstalled(project);
				KbProject sp = (KbProject)KbProjectFactory.getKbProject(project, false);
				if(sp != null) {
					if(sp.isMock) {
						addKbProject(sp);
					} else {
						dependsOn.add(sp);
						sp.addDependentKbProject(this);
					}
				}
			}
		}

		Element usedElement = XMLUtilities.getUniqueChild(root, "used-by-projects"); //$NON-NLS-1$
		if(usedElement != null) {
			Element[] paths = XMLUtilities.getChildren(usedElement, "project"); //$NON-NLS-1$
			for (int i = 0; i < paths.length; i++) {
				String p = paths[i].getAttribute("name"); //$NON-NLS-1$
				if(p == null || p.trim().length() == 0) continue;
				IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(p);
				if(project == null || !project.isAccessible()) continue;
				KbProject sp = (KbProject)KbProjectFactory.getKbProject(project, false);
				if(sp != null) usedBy.add(sp);
			}
		}
	
	}

	private void loadSourcePaths2(Element root) {
		Properties context = new Properties();
		context.put("kbProject", this); //$NON-NLS-1$
		Element sourcePathsElement = XMLUtilities.getUniqueChild(root, "paths"); //$NON-NLS-1$
		if(sourcePathsElement == null) return;
		Element[] paths = XMLUtilities.getChildren(sourcePathsElement, "path"); //$NON-NLS-1$
		if(paths != null) for (int i = 0; i < paths.length; i++) {
			String p = paths[i].getAttribute("value"); //$NON-NLS-1$
			if(p == null || p.trim().length() == 0) continue;
			IPath path = new Path(p.trim());
			if(sourcePaths2.containsKey(path)) continue;

			if(!getClassPath().hasPath(path)) {
				boolean ok = false;
				IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(path);
				if(folder != null && folder.exists() && folder.isSynchronized(IResource.DEPTH_INFINITE)) {
					ok = true;
				} else {
					IFile f = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
					if(f != null && f.exists() && f.isSynchronized(IResource.DEPTH_ZERO)) {
						ok = true;
					}
				}
				if(!ok) continue;
			}
			
			context.put(XMLStoreConstants.ATTR_PATH, path);

			long t1 = System.currentTimeMillis();
			LoadedDeclarations ds = new LoadedDeclarations();

			Element libraries = XMLUtilities.getUniqueChild(paths[i], "libraries"); //$NON-NLS-1$
			if(libraries != null) {
				Element[] cs = XMLUtilities.getChildren(libraries, KbXMLStoreConstants.TAG_LIBRARY);
				for (Element library: cs) {
					String cls = library.getAttribute(XMLStoreConstants.ATTR_CLASS);
					AbstractTagLib tagLib = null;
					if(KbXMLStoreConstants.CLS_TLD_LIBRARY.equals(cls)) {
						tagLib = new TLDLibrary();
					} else if(KbXMLStoreConstants.CLS_FACELET_LIBRARY.equals(cls)) {
						tagLib = new FaceletTagLibrary();
					} else if(KbXMLStoreConstants.CLS_FACESCONFIG_LIBRARY.equals(cls)) {
						tagLib = new FacesConfigTagLibrary();
					} else if(KbXMLStoreConstants.CLS_COMPOSITE_LIBRARY.equals(cls)) {
						tagLib = new CompositeTagLibrary();
					} else {
						//consider other cases;
					}
					if(tagLib != null) {
						tagLib.loadXML(library, context);
						ds.getLibraries().add(tagLib);
					}
				}
			}

			getClassPath().pathLoaded(path);

			registerComponents(ds, path);
			long t2 = System.currentTimeMillis();
			if(statistics != null) {
				statistics.add(new Long(t2 - t1));
				if(t2 - t1 > 30) {
					System.out.println("--->" + statistics.size() + " " + (t2 - t1)); //$NON-NLS-1$ //$NON-NLS-2$
					System.out.println("stop"); //$NON-NLS-1$
				}
			}
		}
	}

	private void storeSourcePaths2(Element root) {
		Properties context = new Properties();
		Element sourcePathsElement = XMLUtilities.createElement(root, "paths"); //$NON-NLS-1$
		synchronized(sourcePaths2) {
		for (IPath path : sourcePaths2.keySet()) {
			IFile f = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			if(f != null && f.exists() && f.getProject() != project) {
				continue;
			}

			context.put(XMLStoreConstants.ATTR_PATH, path);
			LoadedDeclarations ds = sourcePaths2.get(path);
			Element pathElement = XMLUtilities.createElement(sourcePathsElement, "path"); //$NON-NLS-1$
			pathElement.setAttribute("value", path.toString()); //$NON-NLS-1$

			List<ITagLibrary> fs = ds.getLibraries();
			if(fs != null && !fs.isEmpty()) {
				Element cse = XMLUtilities.createElement(pathElement, "libraries"); //$NON-NLS-1$
				for (ITagLibrary d: fs) {
					if(d instanceof ICustomTagLibrary) continue;
					AbstractTagLib t = (AbstractTagLib)d;
					t.toXML(cse, context);
				}
			}
		}
		}
	}

	/*
	 * 
	 */
	private void storeProjectDependencies(Element root) {
		Element dependsOnElement = XMLUtilities.createElement(root, "depends-on-projects"); //$NON-NLS-1$
		for (IKbProject p : dependsOn) {
			if(!p.getProject().isAccessible()) continue;
			Element pathElement = XMLUtilities.createElement(dependsOnElement, "project"); //$NON-NLS-1$
			pathElement.setAttribute("name", p.getProject().getName()); //$NON-NLS-1$
		}
		Element usedElement = XMLUtilities.createElement(root, "used-by-projects"); //$NON-NLS-1$
		for (IKbProject p : usedBy) {
			if(!p.getProject().isAccessible()) continue;
			Element pathElement = XMLUtilities.createElement(usedElement, "project"); //$NON-NLS-1$
			pathElement.setAttribute("name", p.getProject().getName()); //$NON-NLS-1$
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws CloneNotSupportedException
	 */
	Map<IPath, LoadedDeclarations> getAllDeclarations() throws CloneNotSupportedException {
		Map<IPath, LoadedDeclarations> map = new HashMap<IPath, LoadedDeclarations>();
		for (ITagLibrary f : getTagLibraries()) {
			IPath p = f.getSourcePath();
			if(p == null || EclipseResourceUtil.isJar(p.toString())) continue;
			LoadedDeclarations ds = map.get(p);
			if(ds == null) {
				ds = new LoadedDeclarations();
				map.put(p, ds);
			}
			ds.getLibraries().add(f.clone());
		}
		return map;
	}
	
	/**
	 * 
	 * @param builderID
	 * @throws CoreException
	 */
	protected void addToBuildSpec(String builderID) throws CoreException {
		IProjectDescription description = getProject().getDescription();
		ICommand command = null;
		ICommand commands[] = description.getBuildSpec();
		for (int i = 0; i < commands.length && command == null; ++i) {
			if (commands[i].getBuilderName().equals(builderID)) 
				command = commands[i];
		}
		if (command == null) {
			command = description.newCommand();
			command.setBuilderName(builderID);
			ICommand[] oldCommands = description.getBuildSpec();
			ICommand[] newCommands = new ICommand[oldCommands.length + 1];
			System.arraycopy(oldCommands, 0, newCommands, 0, oldCommands.length);
			newCommands[oldCommands.length] = command;
			description.setBuildSpec(newCommands);
			getProject().setDescription(description, null);
		}
	}

	static String EXTERNAL_TOOL_BUILDER = "org.eclipse.ui.externaltools.ExternalToolBuilder"; //$NON-NLS-1$
	static final String LAUNCH_CONFIG_HANDLE = "LaunchConfigHandle"; //$NON-NLS-1$
	
	/**
	 * 
	 * @param builderID
	 * @throws CoreException
	 */
	protected void removeFromBuildSpec(String builderID) throws CoreException {
		IProjectDescription description = getProject().getDescription();
		ICommand[] commands = description.getBuildSpec();
		for (int i = 0; i < commands.length; ++i) {
			String builderName = commands[i].getBuilderName();
			if (!builderName.equals(builderID)) {
				if(!builderName.equals(EXTERNAL_TOOL_BUILDER)) continue;
				Object handle = commands[i].getArguments().get(LAUNCH_CONFIG_HANDLE);
				if(handle == null || handle.toString().indexOf(builderID) < 0) continue;
			}
			ICommand[] newCommands = new ICommand[commands.length - 1];
			System.arraycopy(commands, 0, newCommands, 0, i);
			System.arraycopy(commands, i + 1, newCommands, i, commands.length - i - 1);
			description.setBuildSpec(newCommands);
			getProject().setDescription(description, null);
			return;
		}
	}

	/**
	 * Package local method called by builder.
	 * @param component
	 * @param source
	 */	
	public void registerComponents(LoadedDeclarations ds, IPath source) {
		ITagLibrary[] libraries = ds.getLibraries().toArray(new ITagLibrary[0]);

		if(libraries.length == 0) {
			pathRemoved(source);
			if(EclipseResourceUtil.isJar(source.toString())) {
//				if(!sourcePaths.contains(source)) sourcePaths.add(source);
				synchronized(sourcePaths2) {
					sourcePaths2.put(source, ds);
				}
			}
			return;
		}
		synchronized(sourcePaths2) {
			sourcePaths2.put(source, ds);
		}

		Map<Object,ITagLibrary> currentLibraries = findLibraryDeclarations(source);
		List<Change> addedLibraries = null;
		
		for (ITagLibrary library: libraries) {
			AbstractTagLib loaded = (AbstractTagLib)library;
			AbstractTagLib current = (AbstractTagLib)currentLibraries.remove(loaded.getId());

			loaded.setSourcePath(source);

			if(current != null && current.getClass() != loaded.getClass()) {
				this.libraries.removeLibrary((ITagLibrary)current);
				current = null;
			}
			if(current != null) {
				boolean uriChanged = !stringsEqual(current.getURI(), loaded.getURI());
				if(uriChanged) {
					this.libraries.removeLibrary(current);
				}
				List<Change> changes = current.merge(loaded);
				if(uriChanged) {
					this.libraries.addLibrary(current);
				}
				fireChanges(changes);
				continue;
			}
			if(((KbObject)library).getParent() == null) {
				adopt((KbObject)library);
			}
			this.libraries.addLibrary(library);
			addedLibraries = Change.addChange(addedLibraries, new Change(this, null, null, loaded));
		}
		fireChanges(addedLibraries); 
		
		libraryDeclarationsRemoved(currentLibraries);

		try {
			registerComponentsInDependentProjects(ds, source);
		} catch (CloneNotSupportedException e) {
			WebModelPlugin.getPluginLog().logError(e);
		}
	}

	/**
	 * 
	 * @param ds
	 * @param source
	 * @throws CloneNotSupportedException
	 */
	public void registerComponentsInDependentProjects(LoadedDeclarations ds, IPath source) throws CloneNotSupportedException {
		if(usedBy.isEmpty()) return;
		if(EclipseResourceUtil.isJar(source.toString())) return;
		
		for (KbProject p : usedBy) {
			p.resolve();
			LoadedDeclarations ds1 = new LoadedDeclarations();
			for (ITagLibrary f : ds.getLibraries()) {
				ds1.getLibraries().add(f.clone());
			}
			p.registerComponents(ds1, source);
		}
	}
	
	public boolean isPathLoaded(IPath source) {
		return sourcePaths2.containsKey(source);
	}


	/**
	 * Package local method called by builder.
	 * @param source
	 */
	public void pathRemoved(IPath source) {
		if(!sourcePaths2.containsKey(source)) return;
		synchronized (sourcePaths2) {
			sourcePaths2.remove(source);
		}

		List<Change> changes = null;
		
		Set<ITagLibrary> ls = libraries.removePath(source);
		if(ls != null) for (ITagLibrary l: ls) {
			changes = Change.addChange(changes, new Change(this, null, l, null));
		}
		fireChanges(changes);
		
		firePathRemovedToDependentProjects(source);
	}

	public void firePathRemovedToDependentProjects(IPath source) {
		if(usedBy.isEmpty()) return;
		if(EclipseResourceUtil.isJar(source.toString())) return;
		
		for (KbProject p : usedBy) {
			p.resolve();
			p.pathRemoved(source);
		}
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	public Map<Object,ITagLibrary> findLibraryDeclarations(IPath source) {
		Map<Object,ITagLibrary> map = new HashMap<Object, ITagLibrary>();
		Set<ITagLibrary> fs = libraries.getLibrariesBySource(source);
		if(fs != null) for (ITagLibrary c: fs) {
			KbObject ci = (KbObject)c;
			map.put(ci.getId(), c);
		}		
		return map;
	}
	
	/**
	 * 
	 * @param removed
	 */
	void libraryDeclarationsRemoved(Map<Object,ITagLibrary> removed) {
		if(removed == null || removed.isEmpty()) return;
		Iterator<ITagLibrary> iterator = removed.values().iterator();
		List<Change> changes = null;
		while(iterator.hasNext()) {
			ITagLibrary c = iterator.next();
			libraries.removeLibrary(c);
			changes = Change.addChange(changes, new Change(this, null, c, null));
		}
		fireChanges(changes);
	}

	List<Change> postponedChanges = null;

	public void postponeFiring() {
		if(postponedChanges == null) {
			postponedChanges = new ArrayList<Change>();
		}
	}

	public void fireChanges() {
		if(postponedChanges == null) return;
		List<Change> changes = postponedChanges;
		postponedChanges = null;
		fireChanges(changes);
	}

	/**
	 * 
	 * @param changes
	 */
	void fireChanges(List<Change> changes) {
		if(changes == null || changes.isEmpty()) return;
		if(postponedChanges != null) {
			postponedChanges.addAll(changes);
			return;
		}
		//TODO Implement if it will be needed events and listeners. and fire events to them.
	}

	/**
	 * Check if KB builder is installed and add a warning with quick fix to the project if it is not.
	 * @param resource
	 */
	public static boolean checkKBBuilderInstalled(IResource resource) {
		IProject project = resource == null || !resource.isAccessible() ? null : resource.getProject();
		if (project == null) 
			return false; // Cannot check anything

		boolean kbNatureFound = false;
		boolean kbBuilderFound = false;
		try {
			kbNatureFound = (project.getNature(IKbProject.NATURE_ID) != null);
		
			IProjectDescription description = project.getDescription();
			ICommand command = null;
			ICommand commands[] = description.getBuildSpec();
			for (int i = 0; i < commands.length && command == null; ++i) {
				if (commands[i].getBuilderName().equals(KbBuilder.BUILDER_ID)) {
					kbBuilderFound = true;
					break;
				}
			}
		}  catch (CoreException ex) {
			WebKbPlugin.getDefault().logError(ex);
		}
		
		if (kbNatureFound && kbBuilderFound) {
			// Find existing KBNATURE problem marker and kill it if exists
			IMarker[] markers = getOwnedMarkers(project);
			if (markers != null && markers.length > 0) {
				for (IMarker m : markers) {
					try {
						project.deleteMarkers(KB_PROBLEM_MARKER_TYPE, true, IResource.DEPTH_ONE);
						project.setPersistentProperty(KbProjectFactory.NATURE_MOCK, null);
					} catch (CoreException ex) {
						WebKbPlugin.getDefault().logError(ex);
					}
				}
			}
			return true;
		}
		
		// Find existing KBNATURE problem marker and install it if doesn't exist
		IMarker[] markers = getOwnedMarkers(project);
		
		if (markers == null || markers.length == 0) {
			try {
				IMarker m = createOrUpdateKbProblemMarker(null, project, !kbNatureFound, !kbBuilderFound);
			} catch (CoreException ex) {
				WebKbPlugin.getDefault().logError(ex);
			}
		} else {
			for (IMarker m : markers) {
				try {
					m = createOrUpdateKbProblemMarker(m, project, !kbNatureFound, !kbBuilderFound);
				} catch (CoreException ex) {
					WebKbPlugin.getDefault().logError(ex);
				}
			}
		}
		return false;
	}

	public static final String KB_PROBLEM_MARKER_TYPE = "org.jboss.tools.jst.web.kb.kbproblemmarker"; //$NON-NLS-1$

	private static IMarker[] getOwnedMarkers(IResource r) {
		ArrayList<IMarker> l = null;
		try {
			if(r!=null && r.isAccessible()) {
				IMarker[] ms = r.findMarkers(null, false, 1);
				if(ms != null) {
					for (int i = 0; i < ms.length; i++) {
						if(ms[i] == null) continue;
	
						String _type = ms[i].getType();
						if(_type == null) continue;
						if(!_type.equals(KB_PROBLEM_MARKER_TYPE)) continue;
						if(!ms[i].isSubtypeOf(IMarker.PROBLEM)) continue;
		
						if(l == null) 
							l = new ArrayList<IMarker>();
						
						l.add(ms[i]);
					}
				}
			}
		} catch (CoreException e) {
			WebKbPlugin.getDefault().logError(e);
		}
		return (l == null) ? null : l.toArray(new IMarker[0]);
	}

	private static IMarker createOrUpdateKbProblemMarker(IMarker m, IResource r, boolean kbNatureIsAbsent, boolean kbBuilderIsAbsent) throws CoreException {
		ArrayList<String> args = new ArrayList<String>();
		args.add(kbNatureIsAbsent ? KbMessages.KBNATURE_NOT_FOUND : ""); //$NON-NLS-1$
		args.add(kbBuilderIsAbsent ? KbMessages.KBBUILDER_NOT_FOUND : ""); //$NON-NLS-1$

		String message = MessageFormat.format(KbMessages.KBPROBLEM, args.toArray());
		if (m == null) {
			m = r.createMarker(KB_PROBLEM_MARKER_TYPE);
			r.setPersistentProperty(KbProjectFactory.NATURE_MOCK, "true"); //$NON-NLS-1$
			KbProjectFactory.getKbProject(r.getProject(), true);
		}
		m.setAttribute(IMarker.MESSAGE, message);
		m.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
		m.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_NORMAL);
		return m;
	}
}