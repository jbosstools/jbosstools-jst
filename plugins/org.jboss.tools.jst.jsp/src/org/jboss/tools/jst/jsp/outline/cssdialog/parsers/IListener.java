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
package org.jboss.tools.jst.jsp.outline.cssdialog.parsers;

/**
 * 
 * Interface for listenrs
 * 
 * @author Evgeny Zheleznyakov
 * 
 */
public interface IListener {

    /**
     * 
     * Invoke when getting new pair name, value
     * 
     * @param name
     *                Name css attribute
     * @param value
     *                Value css attribute
     */
    public void nextElement(String name, String value);
}