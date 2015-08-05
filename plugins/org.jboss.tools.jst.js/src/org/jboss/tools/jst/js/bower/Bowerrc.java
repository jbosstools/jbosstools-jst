/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *       Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.jst.js.bower;

/**
 * Bower can be configured using JSON in a .bowerrc file
 *
 * @see <a href="http://bower.io/docs/config/">Bower Configuration</a>
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class Bowerrc {
	private boolean analytics;
	private String cwd;
	private String directory;
	private String shorthandResolver;
	private String proxy;
	private String httpsProxy;
	private String userAgent;
	private long timeout;
	private boolean strictSsl;
	private boolean color;
	private String tmp;
	private boolean interactive;

	public boolean isAnalytics() {
		return analytics;
	}

	public void setAnalytics(boolean analytics) {
		this.analytics = analytics;
	}

	public String getCwd() {
		return cwd;
	}

	public void setCwd(String cwd) {
		this.cwd = cwd;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getShorthandResolver() {
		return shorthandResolver;
	}

	public void setShorthandResolver(String shorthandResolver) {
		this.shorthandResolver = shorthandResolver;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public String getHttpsProxy() {
		return httpsProxy;
	}

	public void setHttpsProxy(String httpsProxy) {
		this.httpsProxy = httpsProxy;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public boolean isStrictSsl() {
		return strictSsl;
	}

	public void setStrictSsl(boolean strictSsl) {
		this.strictSsl = strictSsl;
	}

	public boolean isColor() {
		return color;
	}

	public void setColor(boolean color) {
		this.color = color;
	}

	public String getTmp() {
		return tmp;
	}

	public void setTmp(String tmp) {
		this.tmp = tmp;
	}

	public boolean isInteractive() {
		return interactive;
	}

	public void setInteractive(boolean interactive) {
		this.interactive = interactive;
	}
}
