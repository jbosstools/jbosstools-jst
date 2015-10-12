/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *       Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.jst.js.npm;

import java.util.Map;

/**
 * Specifics of npm's package.json 
 *
 * @see <a href="https://docs.npmjs.com/files/package.json">https://docs.npmjs.com/files/package.json</a>
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class PackageJson {
	private String name;
	private String version;
	private String description;
	private String main;
	private Map<String, String> scripts;
	private String author;
	private String license;
	private Map<String, String> dependencies;
	
	public static class Builder {
		private String name;
		private String version;
		private String description;
		private String main;
		private Map<String, String> scripts;
		private String author;
		private String license;
		private Map<String, String> dependencies;
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder version(String version) {
			this.version = version;
			return this;
		}
		
		public Builder description(String description) {
			this.description = description;
			return this;
		}
		
		public Builder main(String main) {
			this.main = main;
			return this;
		}
		
		public Builder scripts(Map<String, String> scripts) {
			this.scripts = scripts;
			return this;
		}
		
		public Builder author(String author) {
			this.author = author;
			return this;
		}
		
		public Builder license(String license) {
			this.license = license;
			return this;
		}
		
		public Builder dependencies(Map<String, String> dependencies) {
			this.dependencies = dependencies;
			return this;
		}
				
		public PackageJson build() {
			return new PackageJson(this);
		}
		
	}
	
	private PackageJson(Builder builder) {
		this.name = builder.name;
		this.version = builder.version;
		this.description = builder.description;
		this.main = builder.main;
		this.scripts = builder.scripts;
		this.author = builder.author;
		this.license = builder.license;
		this.dependencies = builder.dependencies;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Map<String, String> getScripts() {
		return scripts;
	}

	public void setScripts(Map<String, String> scripts) {
		this.scripts = scripts;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}
	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMain() {
		return main;
	}

	public void setMain(String main) {
		this.main = main;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	public Map<String, String> getDependencies() {
		return dependencies;
	}

	public void setDependencies(Map<String, String> dependencies) {
		this.dependencies = dependencies;
	}

	@Override
	public String toString() {
		return "PackageJson [name=" + name + " , version=" + version + " , description=" + description + " , main=" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				+ main + " , scripts=" + scripts + " , author=" + author + " , license=" + license + " , dependencies=" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				+ dependencies + "]";//$NON-NLS-1$

	}


}
