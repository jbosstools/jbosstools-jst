/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Exadel, Inc.
 *     Red Hat, Inc. 
 *******************************************************************************/
package org.jboss.tools.jst.web.debug.ui.internal.views.xpl;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.model.IExpression;
import org.eclipse.debug.core.model.ISuspendResume;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.debug.ui.AbstractDebugView;
import org.eclipse.jface.viewers.StructuredSelection;
import org.jboss.tools.jst.web.debug.ui.internal.views.properties.xpl.IWebDataPropertyChangeListener;
import org.jboss.tools.jst.web.debug.ui.internal.views.properties.xpl.WebDataProperties;
import org.jboss.tools.jst.web.debug.ui.xpl.WebDebugUIPlugin;

/**
 * Updates the variables view
 */
public class WebDataViewEventHandler extends 
	Object
//TODO replace with valid code No such class exists				
	///AbstractDebugEventHandler 
	implements IWebDataPropertyChangeListener {	
	WebDataProperties fWdp;
	AbstractDebugView view;
	/**
	 * Constructs a new event handler on the given view
	 * 
	 * @param view variables view
	 */
	public WebDataViewEventHandler(AbstractDebugView view) {
//		TODO replace with valid code				
///		super(view);
		this.view = view;
		fWdp = new WebDataProperties(WebDebugUIPlugin.getDefault().getPreferenceStore());
		fWdp.addWebDataPropertyChangeListener(this);
	}
	
	/**
	 * @see AbstractDebugEventHandler#handleDebugEvents(DebugEvent[])
	 */
	protected void doHandleDebugEvents(DebugEvent[] events) {
		for (int i = 0; i < events.length; i++) {
			DebugEvent event = events[i];
			switch (event.getKind()) {
				case DebugEvent.SUSPEND:
						doHandleSuspendEvent(event);
					break;
				case DebugEvent.CHANGE:
						doHandleChangeEvent(event);
					break;
				case DebugEvent.RESUME:
						doHandleResumeEvent(event);
					break;
			}
		}
	}
	
	/**
	 * @see AbstractDebugEventHandler#updateForDebugEvents(DebugEvent[])
	 */
	protected void updateForDebugEvents(DebugEvent[] events) {
		for (int i = 0; i < events.length; i++) {	
			DebugEvent event = events[i];
			switch (event.getKind()) {
				case DebugEvent.TERMINATE:
					doHandleTerminateEvent(event);
					break;
			}
		}
	}	
	
	/**
	 * Clear the variables immediately upon resume.
	 */
	protected void doHandleResumeEvent(DebugEvent event) {
		if (!event.isStepStart() && !event.isEvaluation()) {
			// Clear existing variables from the view
			getWebDataView().setViewerInput(StructuredSelection.EMPTY);
		}
	}
	
	/**
	 * Clear any cached variable expansion state for the
	 * terminated thread/target. Also, remove the part listener if there are
	 * no more active debug targets.
	 */
	protected void doHandleTerminateEvent(DebugEvent event) {
		getWebDataView().clearExpandedVariables(event.getSource());
	}
	
	/**
	 * Process a SUSPEND event
	 */
	protected void doHandleSuspendEvent(DebugEvent event) {
		if (event.getDetail() != DebugEvent.EVALUATION_IMPLICIT) {
			// Don't refresh everytime an implicit evaluation finishes
			if (event.getSource() instanceof ISuspendResume) {
				if (!((ISuspendResume)event.getSource()).isSuspended()) {
					// no longer suspended
					return;
				}
			}
			try {
				// TODO-3.3: WTP 2.0
//				((WebDataViewContentProvider) getWebDataView().getWebDataViewer().getContentProvider()).postChange();
			} catch (Exception x) {
			}
//			TODO replace with valid code				
///			refresh();
			getWebDataView().populateDetailPane();
		}		
	}
	
	/**
	 * Process a CHANGE event
	 */
	protected void doHandleChangeEvent(DebugEvent event) {
		if (event.getDetail() == DebugEvent.STATE) {
			// only process variable state changes
			if (event.getSource() instanceof IVariable) {
//				TODO replace with valid code				
//				refresh(event.getSource());
			}
		} else {
			if (!(event.getSource() instanceof IExpression)) {
//				TODO replace with valid code				
///				refresh();
			}
		}	
	}	
	
	/**
	 * Returns the view that event handler updates.
	 */
	protected WebDataView getWebDataView() {
		return (WebDataView)view;
///		return (WebDataView)getView();
	}
	
	/**
	 * Also update the details area.
	 * 
	 * @see org.eclipse.debug.internal.ui.views.AbstractDebugEventHandler#viewBecomesVisible()
	 */
	protected void viewBecomesVisible() {
//		TODO replace with valid code				
///		super.viewBecomesVisible();
		getWebDataView().populateDetailPane();
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.debug.ui.internal.views.properties.IWebDataPropertyChangeListener#propertyChanged(java.lang.String)
	 */
	public void propertyChanged(String property) {
		try {
			// TODO-3.3: WTP 2.0
//			((WebDataViewContentProvider) getWebDataView().getWebDataViewer().getContentProvider()).postChange();
//			TODO replace with valid code				
///			refresh();
			getWebDataView().populateDetailPane();
		} catch (Exception x) {
		}
	}

	// TODO Eclipse 3.1 migration. Implements the method 
	
	protected void doHandleDebugEvents(DebugEvent[] events, Object data) {
		// TODO Auto-generated method stub
		
	}
}

