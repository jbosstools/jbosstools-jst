package org.jboss.tools.jst.web.kb;

import org.jboss.tools.common.log.BaseUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class WebKbPlugin extends BaseUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.jst.web.kb"; //$NON-NLS-1$

	// The shared instance
	private static WebKbPlugin plugin;

	/**
	 * The constructor
	 */
	public WebKbPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static WebKbPlugin getDefault() {
		return plugin;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.log.BaseUIPlugin#getId()
	 */
	@Override
	public String getId() {
		return PLUGIN_ID;
	}
}