/******************************************************************************* 
 * Copyright (c) 2015 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.internal.html.impl;

import java.util.Hashtable;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.jboss.tools.jst.web.ui.WebUiPlugin;


public class PaletteCountManager {

	private static PaletteCountManager instance = new PaletteCountManager();

	public static PaletteCountManager getInstance() {
	    return instance;
	}
	
	private Hashtable<IProject, ProjectCounts> projectMap = new Hashtable<IProject, ProjectCounts>();
	
	private ProjectCounts getProjectCounts(IProject project){
		ProjectCounts projectCounts = projectMap.get(project);
		if(projectCounts == null){
			projectCounts = new ProjectCounts(project);
			projectMap.put(project, projectCounts);
		}
		return projectCounts;
	}
	
	public long getCountIndex(IProject project, String id) {
		ProjectCounts projectCounts = getProjectCounts(project);
		return projectCounts.getCountIndex(id);
	}
	
	public void setCountIndex(IProject project, String id, long count) {
		ProjectCounts projectCounts = getProjectCounts(project);
		projectCounts.setCountIndex(id, count);
	}
	
	public void setProjectCountIndex(IProject project, long count) {
		ProjectCounts projectCounts = getProjectCounts(project);
		projectCounts.setProjectCountIndex(count);
	}

	public long getNumberOfCalls(IProject project, String id) {
		ProjectCounts projectCounts = getProjectCounts(project);
		return projectCounts.getNumberOfCalls(id);
	}

	public void setNumberOfCalls(IProject project, String id, long numberOfCalls) {
		ProjectCounts projectCounts = getProjectCounts(project);
		projectCounts.setNumberOfCalls(id, numberOfCalls);
	}
	
	public void called(IProject project, String id) {
		ProjectCounts projectCounts = getProjectCounts(project);
		projectCounts.called(id);
	}
	
	public void load(IProject project, String id) {
		ProjectCounts projectCounts = getProjectCounts(project);
		projectCounts.load(id);
	}
	
	public void save(IProject project, String id) {
		ProjectCounts projectCounts = getProjectCounts(project);
		projectCounts.save(id);
	}
	
	public void saveAll(IProject project) {
		ProjectCounts projectCounts = getProjectCounts(project);
		projectCounts.saveAll();
	}
	
	static class ProjectCounts{
		private IProject project;
		private Hashtable<String, IdCounts> idMap = new Hashtable<String, IdCounts>();
		private long projectCountIndex;
		
		public ProjectCounts(IProject project){
			this.project = project;
		}
		
		public long getProjectCountIndex(){
			return projectCountIndex;
		}
		
		public void setProjectCountIndex(long count) {
			projectCountIndex = count;
		}

		private IdCounts getIdCounts(String id){
			IdCounts idCounts = idMap.get(id);
			if(idCounts == null){
				idCounts = new IdCounts(ProjectCounts.this, id);
				idMap.put(id, idCounts);
			}
			return idCounts;
		}
		
		public IProject getProject(){
			return project;
		}
		
		public long getCountIndex(String id) {
			IdCounts idCounts = getIdCounts(id);
			return idCounts.getCountIndex();
		}
		
		public void setCountIndex(String id, long count) {
			IdCounts idCounts = getIdCounts(id);
			idCounts.setCountIndex(count);
		}

		public long getNumberOfCalls(String id) {
			IdCounts idCounts = getIdCounts(id);
			return idCounts.getNumberOfCalls();
		}

		public void setNumberOfCalls(String id, long numberOfCalls) {
			IdCounts idCounts = getIdCounts(id);
			idCounts.setNumberOfCalls(numberOfCalls);
		}
		
		public void called(String id) {
			IdCounts idCounts = getIdCounts(id);
			idCounts.called();
		}
		
		public void load(String id) {
			IdCounts idCounts = getIdCounts(id);
			idCounts.load();
		}
		
		public void save(String id) {
			IdCounts idCounts = getIdCounts(id);
			idCounts.save();
		}
		
		public void saveAll(){
			for(IdCounts ic : idMap.values()){
				ic.save();
			}
		}
	}
	
	static class IdCounts{
		private String id;
		private ProjectCounts projectCounts;
		private long countIndex;
		private long numberOfCalls;
		
		
		public IdCounts(ProjectCounts projectCounts, String id){
			this.projectCounts = projectCounts;
			this.id = id;
			load();
		}
		
		public long getCountIndex() {
			return countIndex;
		}
		
		public void setCountIndex(long countIndex) {
			this.countIndex = countIndex;
			if(countIndex > projectCounts.getProjectCountIndex()){
				projectCounts.setProjectCountIndex(countIndex);
			}
		}

		public long getNumberOfCalls() {
			return numberOfCalls;
		}

		public void setNumberOfCalls(long numberOfCalls) {
			this.numberOfCalls = numberOfCalls;
		}
		
		public void called() {
			numberOfCalls++;
			long projectCountIndex = projectCounts.getProjectCountIndex();
			
			projectCountIndex++;
			countIndex = projectCountIndex;
			projectCounts.setProjectCountIndex(projectCountIndex);
		}
		
		public void load(){
			try {
				String countString = projectCounts.getProject().getPersistentProperty(new QualifiedName(WebUiPlugin.PLUGIN_ID, id));
				if(countString != null){
					StringTokenizer st = new StringTokenizer(countString," ");
					
					countIndex = new Long(st.nextToken());
					if(countIndex > projectCounts.getProjectCountIndex()){
						projectCounts.setProjectCountIndex(countIndex);
					}
					numberOfCalls = new Long(st.nextToken());
				}
			} catch (CoreException e) {
				WebUiPlugin.getDefault().logError(e);
			}
		}
		
		public void save(){
			try {
				projectCounts.getProject().setPersistentProperty(new QualifiedName(WebUiPlugin.PLUGIN_ID, id), ""+countIndex+" "+numberOfCalls);
			} catch (CoreException e) {
				WebUiPlugin.getDefault().logError(e);
			}
		}

	}
}
