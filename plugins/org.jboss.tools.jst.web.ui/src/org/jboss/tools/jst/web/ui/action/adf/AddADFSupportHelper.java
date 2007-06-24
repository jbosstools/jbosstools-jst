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
package org.jboss.tools.jst.web.ui.action.adf;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.jboss.tools.common.meta.action.impl.handlers.DefaultCreateHandler;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.filesystems.FileSystemsHelper;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.common.model.project.ClassPathUpdate;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.project.WebProject;
import org.jboss.tools.jst.web.project.helpers.*;

public class AddADFSupportHelper {
	static String ORACLE_ADF_LIB_FOLDER_NAME = "OracleADF"; 
	String adfLibPath;
	XModelObject object;
	
	public AddADFSupportHelper() {
		try {
			adfLibPath = LibrarySets.getInstance().getLibrarySetsPath() + "/" + ORACLE_ADF_LIB_FOLDER_NAME;
		} catch (Exception e) {
			WebModelPlugin.log(e);
		}
	}
	
	public void setObject(XModelObject object) {
		this.object = object;
	}
	
	public boolean isEnabled() {
		if(object == null) return false;
		XModel model = object.getModel();
		XModelObject fss = FileSystemsHelper.getFileSystems(model);
		if(fss == null) return false;
		File adfLibFile = new File(adfLibPath);
		if(!adfLibFile.isDirectory()) return false;
		File[] fs = adfLibFile.listFiles();
		if(fs == null || fs.length == 0) return false;
		for (int i = 0; i < fs.length; i++) {
			String n = fs[i].getName();
			if(!n.endsWith(".jar")) continue;
			if(fss.getChildByPath("lib-" + n) == null) return true;
		}
		return false;
	}
	
	public void execute() throws Exception {
		ModelPlugin.getDefault().getWorkbench().getProgressService().run(false, true, new RunImpl());
	}

	class RunImpl implements IRunnableWithProgress {

		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			monitor.beginTask("Add ADF Support", 100);

			XModel model = object.getModel();
			XModelObject fss = FileSystemsHelper.getFileSystems(model);
			File adfLibFile = new File(adfLibPath);
			if(!adfLibFile.isDirectory()) {
				monitor.setCanceled(true);
				return;
			}
			File[] fs = adfLibFile.listFiles();
			if(fs == null || fs.length == 0) {
				monitor.setCanceled(true);
				throw new InterruptedException("Library " + ORACLE_ADF_LIB_FOLDER_NAME + " is empty.");
			}
			String libLocation = NewWebProjectHelper.getLibLocation(model);
			if(libLocation == null || libLocation.length() == 0) {
				monitor.setCanceled(true);
				throw new InterruptedException("Project does not have lib folder.");
			}
			String libName = null;
			XModelObject webinf = FileSystemsHelper.getWebInf(model);
			File webInfDir = ((IResource)webinf.getAdapter(IResource.class)).getLocation().toFile();
	        libName = (new File(libLocation).getParentFile().equals(webInfDir))
	            ? "%redhat.workspace%/lib/" : libLocation.replace('\\', '/')+"/";
			for (int i = 0; i < fs.length; i++) {
				String jarName = fs[i].getName();
				if(!jarName.endsWith(".jar")) continue;
				FileUtil.copyFile(fs[i], new File(libLocation + "/" + jarName), true, false);
				String fsName = "lib-" + jarName;
				if(fss.getChildByPath(fsName) == null) {
	                Properties fsProp = new Properties();
	                fsProp.setProperty("name", fsName);
	                fsProp.setProperty("location", libName + jarName);
	                fsProp.setProperty("info", "hidden=yes");
	                XModelObject fsJar = XModelObjectLoaderUtil.createValidObject(model, "FileSystemJar", fsProp);
	                if(fss.getChildByPath(fsJar.getPathPart()) == null) {
	                	DefaultCreateHandler.addCreatedObject(fss, fsJar, false, -1);
	                }
				}
			}
			monitor.worked(10);
			model.save();
			monitor.worked(10);
			String webRoot = WebProject.getInstance(model).getWebRootLocation();
			for (int i = 0; i < fs.length; i++) {
				String n = fs[i].getName();
				if(!n.endsWith(".zip")) continue;
				try {
					FileUtil.unzip(new File(webRoot), fs[i].getAbsolutePath());
				} catch (Exception e) {
					WebModelPlugin.log(e);
				}
			}
			monitor.worked(30);
			IProject p = EclipseResourceUtil.getProject(model.getRoot());
			if(p != null) try {
				p.refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (Exception e) {}
			monitor.worked(20);
			model.update();
			monitor.worked(10);
			
			try {
				ClassPathUpdate cpu = new ClassPathUpdate();
				cpu.revalidateLibs(model);
			} catch (Exception e) {
				WebModelPlugin.log(e);
			}
			
			monitor.worked(20);
			monitor.done();
		}
		
	}

}
