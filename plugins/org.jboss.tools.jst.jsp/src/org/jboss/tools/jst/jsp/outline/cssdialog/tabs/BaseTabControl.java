/*******************************************************************************
  * Copyright (c) 2007-2008 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.jst.jsp.outline.cssdialog.tabs;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.ChangeStyleEvent;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.ManualChangeStyleListener;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.StyleAttributes;

/**
 * This is base tab control component that should be re-implemented by successor.
 *
 * @author Igor Zhukov
 */
public abstract class BaseTabControl extends Composite {

	protected StyleAttributes styleAttributes = null;
    protected boolean updateDataFromStyleAttributes = false;

	private ArrayList<ManualChangeStyleListener> listeners = new ArrayList<ManualChangeStyleListener>();

	/**
	 * Constructs a new instance of this class given its parent
	 * and a style value describing its behavior and appearance.
	 *
	 * @param parent a widget which will be the parent of the new instance (cannot be null)
	 * @param style the style of widget to construct
	 */
	public BaseTabControl(Composite parent, int style) {
		super(parent, style);
	}

    /**
     * Sets updateDataFromStyleAttributes parameter.
     *
	 * @return the updateDataFromStyleAttributes value
	 */
	public boolean isUpdateDataFromStyleAttributes() {
		return updateDataFromStyleAttributes;
	}

    /**
	 * @return the styleAttributes
	 */
	public StyleAttributes getStyleAttributes() {
		return styleAttributes;
	}

    /**
     * Add ManualChangeStyleListener object.
     *
     * @param listener ManualChangeStyleListener object to be added
     */
    public void addManualChangeStyleListener(ManualChangeStyleListener listener) {
        listeners.add(listener);
    }

    /**
     * Gets an array of ChangeStyleListener object.
     *
     * @return an array of ChangeStyleListener object
     */
    public ManualChangeStyleListener[] getManualChangeStyleListeners() {
        return listeners.toArray(new ManualChangeStyleListener[listeners.size()]);
    }

    /**
     * Remove ManualChangeStyleListener object passed by parameter.
     *
     * @param listener ManualChangeStyleListener object to be removed
     */
    public void removeManualChangeStyleListener(ManualChangeStyleListener listener) {
        listeners.remove(listener);
    }

    /**
     * Method is used to notify all subscribed listeners about any changes within style attribute map.
     */
    public void notifyListeners() {
        ChangeStyleEvent event = new ChangeStyleEvent(this);
        for (ManualChangeStyleListener listener : listeners) {
            listener.styleChanged(event);
        }
    }
}