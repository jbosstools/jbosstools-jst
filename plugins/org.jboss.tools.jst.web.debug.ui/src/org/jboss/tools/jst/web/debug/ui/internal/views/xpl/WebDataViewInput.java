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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;

public class WebDataViewInput {
	
	static Map inputs = new HashMap();

	public static WebDataViewInput create(IStackFrame frame) {
		IThread thread = null;
		try {
			if(frame != null && !frame.isTerminated()) thread = frame.getThread();
		} catch (Exception e) {
		}
		return create(thread);
	}
	
	public static WebDataViewInput create(IThread thread) {
		if(thread == null) return null;
		WebDataViewInput input = (WebDataViewInput)inputs.get(thread);
		if(input == null) {
			input = new WebDataViewInput();
			input.setThread(thread);
			inputs.put(thread, input);
		}
		return input;
	}
	
	public static void remove(IThread thread) {
		if(thread != null) inputs.remove(thread);
	}
	
	private IStackFrame frame;
	private IThread thread;
	
	void setThread(IThread thread) {
		this.thread = thread;
	}
	
	public void setStackFrame(IStackFrame frame) {
		this.frame = frame;
	}
	
	public IStackFrame getFrame() {
		return frame;
	}
	
	public IThread getThread() {
		return thread;
	}

}
