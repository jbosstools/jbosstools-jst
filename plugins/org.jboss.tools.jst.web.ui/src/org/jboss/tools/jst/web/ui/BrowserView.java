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
package org.jboss.tools.jst.web.ui;

import java.util.ResourceBundle;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.internal.browser.BrowserViewer;
import org.eclipse.ui.internal.browser.WebBrowserView;

public class BrowserView extends WebBrowserView {

    public static final String ID = "org.jboss.tools.jst.web.ui.action.BrowserView";

	private static final String RESOURCE_BUNDLE_NAME = "org.jboss.tools.jst.web.ui.help";
	private static final String HELP_URL_KEY = "Help.url";

	private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);
    private static final String HELP_URL = RESOURCE_BUNDLE.getString(HELP_URL_KEY);

	/**
	 * The constructor.
	 */
	public BrowserView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
//		super.createPartControl(parent);

//		int style = WebBrowserUtil.decodeStyle(getViewSite().getSecondaryId());
        int style = IWorkbenchBrowserSupport.STATUS;
        viewer = new BrowserViewer(parent, style);
        viewer.setContainer(this);

        setURL(HELP_URL);

//        try {
//			IWorkbenchBrowserSupport browserSupport = WebUiPlugin.getDefault().getWorkbench().getBrowserSupport();
//			IWebBrowser browser = browserSupport.createBrowser(IWorkbenchBrowserSupport.STATUS | IWorkbenchBrowserSupport.NAVIGATION_BAR | IWorkbenchBrowserSupport.AS_VIEW, null, null, null);
//			browser.openURL(new URL(HELP_URL));
//		} catch (Exception e) {
//		WebModelPlugin.log(e);
//		}

///	    new WebBrowser(parent, false, true).getBrowser().setUrl(HELP_URL);
	}
	
	public void setSite(IWorkbenchPartSite site) {
		super.setSite(site);
		getViewSite().setSelectionProvider(NULL_PROVIDER);
	}
	
	static NullSelectionProvider NULL_PROVIDER = new NullSelectionProvider();
	
	static class NullSelectionProvider implements ISelectionProvider {
		public void addSelectionChangedListener(ISelectionChangedListener listener) {
		}
		public ISelection getSelection() {
			return null;
		}
		public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		}
		public void setSelection(ISelection selection) {
		}		
	}
}