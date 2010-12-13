/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.css.browser;


import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.jst.css.CSSPlugin;

/**
 * @author mareshkau
 *
 */
public class CSSBrowser extends Composite implements CSSBrowserInterface {
	
	private CSSBrowserInterface browser;

	/**
	 * If in system not installed xulrunner, than 
	 * browser will be null
	 * @param browser, can be a null
	 */
	private CSSBrowser(Composite parent, int style) {
		super(parent, style);
	}
	/**
	 * Factory method which creates browser instance
	 * @return
	 */
	public static CSSBrowser createCSSBrowser(Composite parent, int style){
		CSSBrowser cssBrowser = new CSSBrowser(parent,style);
		cssBrowser.setLayout(new GridLayout());
		Browser browser=null;
		try {
			browser = new Browser(cssBrowser, SWT.BORDER | SWT.MOZILLA);
			GridData gridData = new GridData();
			gridData.horizontalAlignment = SWT.FILL;
			gridData.verticalAlignment = SWT.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.grabExcessVerticalSpace = true;
			browser.setLayoutData(gridData);
			browser.pack();
		} catch(SWTError er){
			CSSPlugin.getDefault().logWarning("Xulrunner implementation hasn't been founded, so browser based preview " +
					"will be not available");
		}
		if(browser!=null) {
			cssBrowser.setBrowser(new CSSBrowserMozillaImplementation(browser));
		} else {
			Label label = new Label(cssBrowser,SWT.CENTER);
			label.setText("Browser based preview not availabe,"+System.getProperty("line.separator")+" see log for more details");
			label.setBounds(cssBrowser.getClientArea());
			cssBrowser.setBrowser(new CSSBrowserEmptyImplementation());
		}
		return cssBrowser;
	}
	
	public void setLayoutData(GridData layoutData) {
		super.setLayoutData(layoutData);
	}
	public boolean setFocus() {
		return getBrowser().setFocus();
	}
	public void setText(String generateBrowserPage) {
		getBrowser().setText(generateBrowserPage);
	}
	public void setEnabled(boolean isEnabled) {
		getBrowser().setEnabled(isEnabled);
	}
	public void addMouseListener(MouseAdapter mouseAdapter) {
		getBrowser().addMouseListener(mouseAdapter);
	}
	public boolean isBrowserEvent(TypedEvent e){
		return getBrowser().isBrowserEvent(e);
	}
	private CSSBrowserInterface getBrowser() {
		return this.browser;
	}
	private void setBrowser(CSSBrowserInterface browser) {
		this.browser = browser;
	}
}
