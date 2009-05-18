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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.common.model.project.ext.event.Change;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.jboss.tools.jst.web.kb.KbProjectFactory;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.internal.scanner.ClassPathMonitor;
import org.jboss.tools.jst.web.kb.internal.scanner.LoadedDeclarations;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;
import org.w3c.dom.Element;

/**
 * 
 * @author V.Kabanovich
 *
 */
public class KbProject implements IKbProject {
	IProject project;

	ClassPathMonitor classPath = new ClassPathMonitor(this);

	Set<IPath> sourcePaths = new HashSet<IPath>();
	
	Map<IPath, LoadedDeclarations> sourcePaths2 = new HashMap<IPath, LoadedDeclarations>();
	
	private boolean isStorageResolved = false;
	
	Set<KbProject> dependsOn = new HashSet<KbProject>();
	
	Set<KbProject> usedBy = new HashSet<KbProject>();
	
	LibraryStorage libraries = new LibraryStorage();

	public ITagLibrary[] getTagLibraries() {
		return libraries.getAllFactoriesArray();
	}

	public void configure() throws CoreException {
		addToBuildSpec(KbBuilder.BUILDER_ID);
	}

	public void deconfigure() throws CoreException {
		removeFromBuildSpec(KbBuilder.BUILDER_ID);
	}

	public IProject getProject() {
		return project;
	}

	public IPath getSourcePath() {
		return project == null ? null : project.getFullPath();
	}

	public void setProject(IProject project) {
		this.project = project;
		classPath.init();
	}

	/**
	 * 
	 * @param p
	 */
	public void addSeamProject(KbProject p) {
		if(dependsOn.contains(p)) return;
		dependsOn.add(p);
		p.addDependentSeamProject(this);
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
	public Set<KbProject> getSeamProjects() {
		return dependsOn;
	}
	
	/**
	 * 
	 * @param p
	 */
	public void addDependentSeamProject(KbProject p) {
		usedBy.add(p);
	}

	/**
	 * 
	 * @param p
	 */
	public void removeSeamProject(KbProject p) {
		if(!dependsOn.contains(p)) return;
		p.usedBy.remove(this);
		dependsOn.remove(p);
		IPath[] ps = sourcePaths2.keySet().toArray(new IPath[0]);
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
					if(XMLUtilities.getUniqueChild(root, "paths") != null) {
						loadSourcePaths2(root);
					}
				}
			}

			if(b) {
				getClassPath().process();
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
		IPath[] ps = sourcePaths2.keySet().toArray(new IPath[0]);
		for (int i = 0; i < ps.length; i++) {
			pathRemoved(ps[i]);
		}
		fireChanges();
	}
	
	public long fullBuildTime;
	public List<Long> statistics;


	/**
	 * Method testing how long it takes to load Seam model
	 * serialized previously.
	 * This approach makes sure, that all other services 
	 * (JDT, XModel, etc) are already loaded at first start of 
	 * Seam model, so that now it is more or less pure time 
	 * to be computed.
	 * 
	 * @return
	 */
	public long reload() {
		statistics = new ArrayList<Long>();
		classPath = new ClassPathMonitor(this);
		sourcePaths.clear();
		sourcePaths2.clear();
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
		File file = getStorageFile();
		file.getParentFile().mkdirs();
		
		Element root = XMLUtilities.createDocumentElement("seam-project"); //$NON-NLS-1$
		storeProjectDependencies(root);

//		storeSourcePaths(root);
		storeSourcePaths2(root);
		
		XMLUtilities.serialize(root, file.getAbsolutePath());
	}

	/*
	 * 
	 */
	private File getStorageFile() {
		IPath path = WebKbPlugin.getDefault().getStateLocation();
		File file = new File(path.toFile(), "projects/" + project.getName()); //$NON-NLS-1$
		return file;
	}
	
	public void clearStorage() {
		File f = getStorageFile();
		if(f != null && f.isFile()) f.delete();
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
				KbProject sp = (KbProject)KbProjectFactory.getKbProject(project, false);
				if(sp != null) {
					dependsOn.add(sp);
					sp.addDependentSeamProject(this);
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
		context.put("seamProject", this);
		Element sourcePathsElement = XMLUtilities.getUniqueChild(root, "paths"); //$NON-NLS-1$
		if(sourcePathsElement == null) return;
		Element[] paths = XMLUtilities.getChildren(sourcePathsElement, "path"); //$NON-NLS-1$
		if(paths != null) for (int i = 0; i < paths.length; i++) {
			String p = paths[i].getAttribute("value"); //$NON-NLS-1$
			if(p == null || p.trim().length() == 0) continue;
			IPath path = new Path(p.trim());
			if(sourcePaths2.containsKey(path)) continue;

			if(!getClassPath().hasPath(path)) {
				IFile f = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
				if(f == null || !f.exists() || !f.isSynchronized(IResource.DEPTH_ZERO)) continue;
			}
			
			//TODO

			long t1 = System.currentTimeMillis();
			LoadedDeclarations ds = new LoadedDeclarations();

			Element libraries = XMLUtilities.getUniqueChild(paths[i], "libraries");
			if(libraries != null) {

				//TODO
				
			}

			getClassPath().pathLoaded(path);

			registerComponents(ds, path);
			long t2 = System.currentTimeMillis();
			if(statistics != null) {
				statistics.add(new Long(t2 - t1));
				if(t2 - t1 > 30) {
					System.out.println("--->" + statistics.size() + " " + (t2 - t1));
					System.out.println("stop");
				}
			}
		}
	}

	private void storeSourcePaths2(Element root) {
		Properties context = new Properties();
		Element sourcePathsElement = XMLUtilities.createElement(root, "paths"); //$NON-NLS-1$
		for (IPath path : sourcePaths2.keySet()) {
			IFile f = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			if(f != null && f.exists() && f.getProject() != project) {
				continue;
			}
			//TODO
//			context.put(SeamXMLConstants.ATTR_PATH, path);
			LoadedDeclarations ds = sourcePaths2.get(path);
			Element pathElement = XMLUtilities.createElement(sourcePathsElement, "path"); //$NON-NLS-1$
			pathElement.setAttribute("value", path.toString()); //$NON-NLS-1$

			List<ITagLibrary> fs = ds.getLibraries();
			if(fs != null && !fs.isEmpty()) {
				Element cse = XMLUtilities.createElement(pathElement, "factories"); //$NON-NLS-1$
				for (ITagLibrary d: fs) {
					//TODO
//					SeamObject o = (SeamObject)d;
//					o.toXML(cse, context);
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

	static String EXTERNAL_TOOL_BUILDER = "org.eclipse.ui.externaltools.ExternalToolBuilder";
	static final String LAUNCH_CONFIG_HANDLE = "LaunchConfigHandle";
	
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
		//TODO
	}

	public boolean isPathLoaded(IPath source) {
		return sourcePaths2.containsKey(source);
	}


	/**
	 * Package local method called by builder.
	 * @param source
	 */
	public void pathRemoved(IPath source) {
		if(!sourcePaths.contains(source) && !sourcePaths2.containsKey(source)) return;
		sourcePaths.remove(source);
		sourcePaths2.remove(source);

		List<Change> changes = null;
		//TODO
		
		Set<ITagLibrary> ls = libraries.removePath(source);
		if(ls != null) for (ITagLibrary l: ls) {
			changes = Change.addChange(changes, new Change(this, null, l, null));
		}
		fireChanges(changes);
		
//		firePathRemovedToDependentProjects(source);
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
		//TODO
	}
	class LibraryStorage {
		private Set<ITagLibrary> allFactories = new HashSet<ITagLibrary>();
		private ITagLibrary[] allFactoriesArray = null;
		Map<IPath, Set<ITagLibrary>> factoriesBySource = new HashMap<IPath, Set<ITagLibrary>>();

		public void clear() {
			synchronized(allFactories) {
				allFactories.clear();
				allFactoriesArray = null;
			}
			factoriesBySource.clear();
		}

		public ITagLibrary[] getAllFactoriesArray() {
			ITagLibrary[] result = allFactoriesArray;
			if(result == null) {
				synchronized(allFactories) {
					allFactoriesArray = allFactories.toArray(new ITagLibrary[0]);
					result = allFactoriesArray;
				}
			}
			return result;
		}

		public Set<ITagLibrary> getFactoriesBySource(IPath path) {
			return factoriesBySource.get(path);
		}
		
		public void addFactory(ITagLibrary f) {
			synchronized(allFactories) {
				allFactories.add(f);
				allFactoriesArray = null;
			}
			IPath path = f.getSourcePath();
			if(path != null) {
				Set<ITagLibrary> fs = factoriesBySource.get(path);
				if(fs == null) {
					fs = new HashSet<ITagLibrary>();
					factoriesBySource.put(path, fs);
				}
				fs.add(f);
			}
		}
		
		public void removeFactory(ITagLibrary f) {
			synchronized(allFactories) {
				allFactories.remove(f);
				allFactoriesArray = null;
			}
			IPath path = f.getSourcePath();
			if(path != null) {
				Set<ITagLibrary> fs = factoriesBySource.get(path);
				if(fs != null) {
					fs.remove(f);
				}
				if(fs.isEmpty()) {
					factoriesBySource.remove(fs);
				}
			}
		}

		public Set<ITagLibrary> removePath(IPath path) {
			Set<ITagLibrary> fs = factoriesBySource.get(path);
			if(fs == null) return null;
			for (ITagLibrary f: fs) {
				synchronized(allFactories) {
					allFactories.remove(f);
					allFactoriesArray = null;
				}
			}
			factoriesBySource.remove(path);
			return fs;
		}
		
	}
	
}
