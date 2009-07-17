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
    public String DOC_QUALIFIEDNAME = "tiles-definitions"; //$NON-NLS-1$
    public String DOC_PUBLICID   = "-//Apache Software Foundation//DTD Tiles Configuration 1.1//EN"; //$NON-NLS-1$
    public String DOC_EXTDTD     = "http://jakarta.apache.org/struts/dtds/tiles-config_1_1.dtd"; //$NON-NLS-1$
    
    public String ENT_FILE = "FileTiles"; //$NON-NLS-1$
    public String ENT_DEFINITION = "TilesDefinition"; //$NON-NLS-1$
    public String ENT_PROCESS = "TilesProcess"; //$NON-NLS-1$
    public String ENT_PROCESS_ITEM = "TilesProcessItem"; //$NON-NLS-1$
    public String ENT_PROCESS_ITEM_OUTPUT = "TilesProcessItemOutput"; //$NON-NLS-1$
    
    public String ELM_PROCESS = "process"; //$NON-NLS-1$
    
    public String ATT_NAME = "name"; //$NON-NLS-1$
    public String ATT_PATH = "path"; //$NON-NLS-1$
    public String ATT_TARGET = "target"; //$NON-NLS-1$
    public String ATT_EXTENDS = "extends"; //$NON-NLS-1$

}
