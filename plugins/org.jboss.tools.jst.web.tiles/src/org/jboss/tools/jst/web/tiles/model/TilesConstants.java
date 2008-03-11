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
package org.jboss.tools.jst.web.tiles.model;

public interface TilesConstants {
    public String DOC_QUALIFIEDNAME = "tiles-definitions";
    public String DOC_PUBLICID   = "-//Apache Software Foundation//DTD Tiles Configuration 1.1//EN";
    public String DOC_EXTDTD     = "http://jakarta.apache.org/struts/dtds/tiles-config_1_1.dtd";
    
    public String ENT_FILE = "FileTiles";
    public String ENT_DEFINITION = "TilesDefinition";
    public String ENT_PROCESS = "TilesProcess";
    public String ENT_PROCESS_ITEM = "TilesProcessItem";
    public String ENT_PROCESS_ITEM_OUTPUT = "TilesProcessItemOutput";
    
    public String ELM_PROCESS = "process";
    
    public String ATT_NAME = "name";
    public String ATT_PATH = "path";
    public String ATT_TARGET = "target";
    public String ATT_EXTENDS = "extends";

}
