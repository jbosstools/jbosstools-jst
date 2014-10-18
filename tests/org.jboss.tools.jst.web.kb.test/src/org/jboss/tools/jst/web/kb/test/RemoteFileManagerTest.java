/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.internal.RemoteFileManager;

/**
 * @author Alexey Kazakov
 */
public class RemoteFileManagerTest extends TestCase {

	private static final String BASE_GOOD_URL = "http://test.domain.com/good.css";
	private static final String UNAVAILABLE_URL = "http://test.domain.com/unavailable.css";
	private static final String IO_EXCEPTION_URL = "http://test.domain.com/error.css";

	private RemoteFileManager manager;
	private boolean slow_connection;
	private Set<String> goodUrls = new HashSet<String>();

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		manager = new TestRemoteFileManager();
		slow_connection = false;
		IEclipsePreferences preferences = DefaultScope.INSTANCE.getNode(WebKbPlugin.PLUGIN_ID);
		preferences.putInt(RemoteFileManager.DOWNLOADING_TIMEOUT_PREFERENCE, 200);
	}

	public void testAvailableStatus() {
		File file = null;
		try {
			IEclipsePreferences preferences = DefaultScope.INSTANCE.getNode(WebKbPlugin.PLUGIN_ID);
			preferences.putInt(RemoteFileManager.DOWNLOADING_TIMEOUT_PREFERENCE, 10000);

			RemoteFileManager.Result result = manager.getFile(generateGoodUrl(BASE_GOOD_URL));
			assertTrue(result.isReady());
			assertNotNull(result.getLocalPath());
			file = new File(result.getLocalPath());
			assertTrue(file.isFile());
		} finally {
			if(file!=null) {
				file.delete();
			}
		}
	}

	public void testDownloadingStatus() {
		File file = null;
		try {
			slow_connection = true;
			IEclipsePreferences preferences = DefaultScope.INSTANCE.getNode(WebKbPlugin.PLUGIN_ID);
			preferences.putInt(RemoteFileManager.DOWNLOADING_TIMEOUT_PREFERENCE, 1);

			RemoteFileManager.Result result = manager.getFile(generateGoodUrl(BASE_GOOD_URL));
			assertFalse(result.isReady());
			if(result.getLocalPath()!=null) {
				file = new File(result.getLocalPath());
			}
			assertNull(result.getLocalPath());
			assertEquals(RemoteFileManager.DownloadingStatus.DOWNLOADING, result.getStatus());
		} finally {
			if(file!=null) {
				file.delete();
			}
		}
	}

	public void testUnavailableStatus() {
		File file = null;
		try {
			RemoteFileManager.Result result = manager.getFile(UNAVAILABLE_URL);
			assertFalse(result.isReady());
			if(result.getLocalPath()!=null) {
				file = new File(result.getLocalPath());
			}
			assertNull(result.getLocalPath());
			assertEquals(RemoteFileManager.DownloadingStatus.UNAVAILABLE, result.getStatus());
		} finally {
			if(file!=null) {
				file.delete();
			}
		}
	}

	public void testWaitForDownloading() {
		File file = null;
		try {
			slow_connection = true;
			IEclipsePreferences preferences = DefaultScope.INSTANCE.getNode(WebKbPlugin.PLUGIN_ID);
			preferences.putInt(RemoteFileManager.DOWNLOADING_TIMEOUT_PREFERENCE, 10);

			String goodUrl = generateGoodUrl(BASE_GOOD_URL);
			RemoteFileManager.Result result = manager.getFile(goodUrl);
			assertFalse(result.isReady());
			if(result.getLocalPath()!=null) {
				file = new File(result.getLocalPath());
			}
			assertNull(result.getLocalPath());
			assertEquals(RemoteFileManager.DownloadingStatus.DOWNLOADING, result.getStatus());

			Thread.sleep(200);
			result = manager.getFile(goodUrl);
			assertTrue(result.isReady());
			file = new File(result.getLocalPath());
			assertNotNull(file);
			file = new File(result.getLocalPath());
			assertTrue(file.isFile());
		} catch (InterruptedException e) {
		} finally {
			if(file!=null) {
				file.delete();
			}
		}
	}

	private static int COUNT;

	private String generateGoodUrl(String baseUrl) {
		String url = baseUrl + (COUNT++) + ".css";
		goodUrls.add(url);
		return url;
	}

	private class TestRemoteFileManager extends RemoteFileManager {

		@Override
		protected IDownloader getDownloader(final File file, final String url) {
			return new IDownloader() {
				@Override
				public InputStreamReader getInputStream(boolean ifModified) throws IOException {
					InputStreamReader in = null;
					if(slow_connection) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
						}
					}
					if(goodUrls.contains(url)) {
						in = new InputStreamReader(new InputStream() {
							@Override
							public int read() throws IOException {
								return -1;
							}
						});
					} else if(IO_EXCEPTION_URL.equals(url)) {
						throw new IOException("Test IOException");
					}
					return in;
				}
			};
		}
	}
}