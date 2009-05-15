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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.jboss.tools.jst.web.kb.internal.scanner.ClassPathMonitor;
import org.jboss.tools.jst.web.kb.internal.scanner.LoadedDeclarations;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;

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
	
		//TODO
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
