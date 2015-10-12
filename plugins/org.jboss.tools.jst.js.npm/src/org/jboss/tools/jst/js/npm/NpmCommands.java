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

/**
 * npm CLI commands
 *
 * @see <a href="https://docs.npmjs.com/">npm documentation</a>
 * @author "Ilya Buziuk (ibuziuk)"
 */
public enum NpmCommands {
	ACCESS("access"), //$NON-NLS-1$
	ADDUSER("adduser"), //$NON-NLS-1$
	BIN("bin"), //$NON-NLS-1$
	BUGS("bugs"), //$NON-NLS-1$
	BUILD("build"), //$NON-NLS-1$
	BUNDLE("bundle"), //$NON-NLS-1$
	CACHE("cache"), //$NON-NLS-1$
	COMPLETION("completion"), //$NON-NLS-1$
	CONFIG("config"), //$NON-NLS-1$
	DEDUPE("dedupe"), //$NON-NLS-1$
	DEPRECATE("deprecate"), //$NON-NLS-1$
	DIST_TAG("dist-tag"), //$NON-NLS-1$
	DOCS("docs"), //$NON-NLS-1$
	EDIT("edit"), //$NON-NLS-1$
	EXPLORE("explore"), //$NON-NLS-1$
	HELP("help"), //$NON-NLS-1$
	HELP_SEARCH("help-search"), //$NON-NLS-1$
	INIT("init"), //$NON-NLS-1$
	INSTALL("install"), //$NON-NLS-1$
	LINK("link"), //$NON-NLS-1$
	LOGOUT("logout"), //$NON-NLS-1$
	LS("ls"), //$NON-NLS-1$
	NPM("npm"), //$NON-NLS-1$
	OUTDATED("outdated"), //$NON-NLS-1$
	OWNER("owner"), //$NON-NLS-1$
	PACK("pack"), //$NON-NLS-1$
	PING("ping"), //$NON-NLS-1$
	PREFIX("prefix"), //$NON-NLS-1$
	PRUNE("prune"), //$NON-NLS-1$
	PUBLISH("publish"), //$NON-NLS-1$
	REBUILD("rebuild"), //$NON-NLS-1$
	REPO("repo"), //$NON-NLS-1$
	RESTART("restart"), //$NON-NLS-1$
	ROOT("root"), //$NON-NLS-1$
	RUN_SCRIPT("run-script"), //$NON-NLS-1$
	SEARCH("search"), //$NON-NLS-1$
	SHRINKWRAP("shrinkwrap"), //$NON-NLS-1$
	STAR("star"), //$NON-NLS-1$
	STARS("stars"), //$NON-NLS-1$
	START("start"), //$NON-NLS-1$
	STOP("stop"), //$NON-NLS-1$
	TAG("tag"), //$NON-NLS-1$
	TEAM("team"), //$NON-NLS-1$
	TEST("test"), //$NON-NLS-1$
	UNINSTALL("uninstall"), //$NON-NLS-1$
	UNPUBLISH("unpublish"), //$NON-NLS-1$
	UPDATE("update"), //$NON-NLS-1$
	VERSION("version"), //$NON-NLS-1$
	VIEW("view"), //$NON-NLS-1$
	WHOAMI("whoami"); //$NON-NLS-1$

	private final String value;

	private NpmCommands(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
