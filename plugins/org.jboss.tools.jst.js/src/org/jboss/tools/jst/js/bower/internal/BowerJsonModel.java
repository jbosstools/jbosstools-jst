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
package org.jboss.tools.jst.js.bower.internal;

import java.util.List;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class BowerJsonModel {
	private String name;
	private String version;
	private List<String> authors;
	private String license;
	private List<String> ignore;

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
	
	@Override
	public String toString() {
		return "BowerJson [name=" + name + " , version=" + version + " , authors=" + authors + " , license=" + license //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				+ " , ignore=" + ignore + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}

}
