/******************************************************************************* 
 * Copyright (c) 2010 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.require;

/**
 * KbRequireDefinition is used to store the definitions read from the KbRequire Schema
 * 
 * @author Victor Rubezhny
 *
 */
public class KbRequireDefinition {
	private String fNatureId;
	private String fDescription; 

	public KbRequireDefinition(String forId, String description) {
		this.fNatureId = forId;
		this.fDescription = description;
	}
	
	public String getNatureId () {
		return fNatureId;
	}
	
	public String getDescription() {
		return fDescription;
	}
}
