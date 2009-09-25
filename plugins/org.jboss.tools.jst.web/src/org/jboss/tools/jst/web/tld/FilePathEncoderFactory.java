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
package org.jboss.tools.jst.web.tld;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.project.ModelNatureExtension;
import org.jboss.tools.common.model.util.ModelFeatureFactory;

public class FilePathEncoderFactory {
	static IFilePathEncoder pathEncoderInstance;

	public static IFilePathEncoder getPathEncoder (String pathEncoder) {
		if(pathEncoderInstance != null) {
			return pathEncoderInstance;
		}
		if(pathEncoder == null) {
			return null;
		}
		if(pathEncoder.length() == 0) {
			pathEncoder = null;
			return null;
		}
		try {
			pathEncoderInstance = (IFilePathEncoder)ModelFeatureFactory.getInstance().createFeatureInstance(pathEncoder);
		} catch (ClassCastException e) {
			ModelPlugin.getPluginLog().logError(e);
		} finally {
			pathEncoder = null;
		}
		return pathEncoderInstance;
	}


	public static IFilePathEncoder getEncoder(IProject project) {
		if(project == null || !project.isOpen()) return null;
		ModelNatureExtension[] es = ModelNatureExtension.getInstances();
		for (int i = 0; i < es.length; i++) {
			try {
				if(project.hasNature(es[i].getName())) {
					IFilePathEncoder encoder = getPathEncoder(es[i].getPathEncoder());
					if(encoder != null) return encoder;
				}
			} catch (CoreException e) {
				//ignore - all checks are done above
			}
		}
		return null;
	}

}
