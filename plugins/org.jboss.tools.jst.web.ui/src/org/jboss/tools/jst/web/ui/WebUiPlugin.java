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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;
import org.jboss.tools.common.log.BaseUIPlugin;
import org.jboss.tools.common.log.IPluginLog;
import org.jboss.tools.jst.web.ui.editor.pref.template.TemplateContextTypeIdsXHTML;
import org.jboss.tools.jst.web.ui.palette.html.wizard.HTMLWizardVisualPreviewInitializationException;
import org.osgi.framework.BundleContext;

public class WebUiPlugin extends BaseUIPlugin {
	public static final String PLUGIN_ID = "org.jboss.tools.jst.web.ui"; //$NON-NLS-1$
	static WebUiPlugin INSTANCE;
	/**
	 * The template store for the html editor.
	 */
	private TemplateStore fTemplateStore;
	/**
	 * The template context type registry for the html editor.
	 */
	private ContextTypeRegistry fContextTypeRegistry;
	
	public WebUiPlugin() {
		INSTANCE = this;
	}

	public static WebUiPlugin getDefault() {
		if(INSTANCE == null) {
			Platform.getBundle(PLUGIN_ID);
		}
		return INSTANCE;
	}
	
	public void start(BundleContext context) throws Exception {
	    super.start(context);
	}
	
	public static boolean isDebugEnabled() {
		return INSTANCE != null && INSTANCE.isDebugging();
	}
	
	public static Shell getShell() {
		return (INSTANCE == null) ? null : INSTANCE.getWorkbench().getActiveWorkbenchWindow().getShell();
	}
	
	/**
	 * @return IPluginLog object
	 */
	public static IPluginLog getPluginLog() {
		return getDefault();
	}
	public TemplateStore getTemplateStore() {
		if (this.fTemplateStore == null) {
			this.fTemplateStore = new ContributionTemplateStore(getTemplateContextRegistry(), getPreferenceStore(), "org.jboss.tools.jsf.ui.custom_templates");

			try {
				this.fTemplateStore.load();
			} catch (IOException e) {
				this.logError(e);
			}
		}
		return this.fTemplateStore;
	}
	
	/**
	 * Returns the template context type registry for the html plugin.
	 * 
	 * @return the template context type registry for the html plugin
	 */
	public ContextTypeRegistry getTemplateContextRegistry() {
		if (this.fContextTypeRegistry == null) {
			ContributionContextTypeRegistry registry = new ContributionContextTypeRegistry();
			registry.addContextType(TemplateContextTypeIdsXHTML.ALL);
			registry.addContextType(TemplateContextTypeIdsXHTML.NEW);
			registry.addContextType(TemplateContextTypeIdsXHTML.TAG);
			registry.addContextType(TemplateContextTypeIdsXHTML.ATTRIBUTE);
			registry.addContextType(TemplateContextTypeIdsXHTML.ATTRIBUTE_VALUE);

			this.fContextTypeRegistry = registry;
		}

		return this.fContextTypeRegistry;
	}

	private static boolean browserLoadingErrorLoged;
	private static Map<Integer, String> browserNames = new HashMap<Integer, String>();
	static {
		browserNames.put(SWT.WEBKIT, "Webkit");
		browserNames.put(SWT.MOZILLA, "Mozilla");
	}

	/**
	 * Creates an SWT Browser
	 * @param style
	 * @param parent
	 * @return
	 */
	public static Browser createBrowser(int style, Composite parent) {
		return createBrowser(style, parent);
	}

	/**
	 * Creates an SWT Browser
	 * @param parent
	 * @param preferredBrowser SWT.MOZILLA, SWT.WEBKIT or SWT.NONE
	 * @return
	 */
	public static Browser createBrowser(Composite parent, int preferredBrowser) {
		return createBrowser(SWT.READ_ONLY | SWT.NO_SCROLL, parent, preferredBrowser);
	}

	/**
	 * Creates an SWT Browser. Returns null if can't create a browser.
	 * @param style
	 * @param parent
	 * @param preferredBrowser SWT.MOZILLA, SWT.WEBKIT or SWT.NONE
	 * @return
	 */
	public static Browser createBrowser(int style, Composite parent, int preferredBrowser) {
		/*
			// We can provide webkit in this way:
			   String property = "org.eclipse.swt.browser.DefaultType";
			   String defaultBrowser = System.getProperty(property);
			   boolean hasDefaultBrowser = defaultBrowser != null;
			   System.getProperties().setProperty(property, "webkit");
		*/
		try {
			try {
				return new Browser(parent, style | preferredBrowser);
			} catch (Error e) {
				if(!(e instanceof SWTError) && !browserLoadingErrorLoged) {
					logBrowserLoadingProblem(e, browserNames.get(preferredBrowser), true);
				}
				Control[] children = parent.getChildren();
				for (Control child : children) {
					child.dispose();
				}
				try {
					return new Browser(parent, style | SWT.NONE);
				} catch (Error e1) {
					logBrowserLoadingProblem(e1, null, false);
				}
			}
		} finally {
			/*
				//Use if system property was modified
				if(hasDefaultBrowser) {
					System.getProperties().setProperty(property, defaultBrowser);
				}
			*/
		}
		return null;
	}

	private static void logBrowserLoadingProblem(Error e, String browserName, boolean warning) {
		if(browserName==null) {
			browserName = "default";
		}
		String message = "Cannot create " + browserName + " browser";
		Exception ex = new HTMLWizardVisualPreviewInitializationException(message, e);
		if(warning) {
			WebUiPlugin.getDefault().logWarning(message, ex);
		} else {
			WebUiPlugin.getDefault().logError(message, ex);
		}
		browserLoadingErrorLoged = true;
	}

	private static final boolean isMacOS = "Mac OS X".equals(System.getProperty("os.name"));

	public static int getPreferredBrowser() {
		return isMacOS ? SWT.WEBKIT : SWT.MOZILLA;
	}
}