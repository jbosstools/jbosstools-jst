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
package org.jboss.tools.jst.js.bower;

import java.util.List;
import java.util.Map;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class BowerJson {
	private String name;
	private String version;
	private List<String> authors;
	private String license;
	private List<String> ignore;
	private Map<String, String> dependencies;
	
	public static class Builder {
		private String name;
		private String version;
		private List<String> authors;
		private String license;
		private List<String> ignore;
		private Map<String, String> dependencies;
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder version(String version) {
			this.version = version;
			return this;
		}
		
		public Builder authrors(List<String> authors) {
			this.authors = authors;
			return this;
		}
		
		public Builder license(String license) {
			this.license = license;
			return this;
		}
				
		public Builder ignore(List<String> ignore) {
			this.ignore = ignore;
			return this;
		}
		
		public Builder dependencies(Map<String, String> dependencies) {
			this.dependencies = dependencies;
			return this;
		}
		
		public BowerJson build() {
			return new BowerJson(this);
		}
		
	}
	
	private BowerJson(Builder builder) {
		this.name = builder.name;
		this.version = builder.version;
		this.authors = builder.authors;
		this.license = builder.license;
		this.ignore = builder.ignore;
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

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public List<String> getIgnore() {
		return ignore;
	}

	public void setIgnore(List<String> ignore) {
		this.ignore = ignore;
	}
	
	public Map<String, String> getDependencies() {
		return dependencies;
	}

	public void setDependencies(Map<String, String> dependencies) {
		this.dependencies = dependencies;
	}
	
	@Override
	public String toString() {
		return "BowerJson [name=" + name + " , version=" + version + " , authors=" + authors + " , license=" + license //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				+ " , ignore=" + ignore + " , dependencies=" + dependencies + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

}
