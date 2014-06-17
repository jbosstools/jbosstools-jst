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
package org.jboss.tools.jst.web.kb.internal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.TreeSet;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.codehaus.plexus.util.IOUtil;
import org.eclipse.core.resources.ISaveContext;
import org.eclipse.core.resources.ISavedState;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.common.util.HttpUtil;
import org.jboss.tools.jst.web.kb.WebKbPlugin;

/**
 * Downloads remote files to local workspace catalog.
 * 
 * @author Alexey Kazakov
 */
public class RemoteFileManager {

	private static final String PREFERENCE_KEY = WebKbPlugin.PLUGIN_ID + ".preferences.rfm."; //$NON-NLS-1$
	public static final String UNAVAILABLE_MAX_PREFERENCE = PREFERENCE_KEY + "unavailableUrlsMax"; //$NON-NLS-1$
	public static final String UNAVAILABLE_TIMEOUT_PREFERENCE = PREFERENCE_KEY + "unavailableUrlsTimeout"; //$NON-NLS-1$
	public static final String FILE_CACHE_MAX_PREFERENCE = PREFERENCE_KEY + "unavailableUrlsMax"; //$NON-NLS-1$
	public static final String FILE_CACHE_TIMEOUT_PREFERENCE = PREFERENCE_KEY + "unavailableUrlsTimeout"; //$NON-NLS-1$
	public static final String FILE_UPDATE_TIMEOUT_PREFERENCE = PREFERENCE_KEY + "fileUpdateTimeout"; //$NON-NLS-1$
	public static final String DOWNLOADING_TIMEOUT_PREFERENCE = PREFERENCE_KEY + "downloadingTimeout"; //$NON-NLS-1$
	public static final String DOWNLOADING_IF_MODIFIED_TIMEOUT_PREFERENCE = PREFERENCE_KEY + "downloadingIfModifiedTimeout"; //$NON-NLS-1$
	private static final String FILE_SAVE = "rfm-save";

	private static final RemoteFileManager INSTANCE = new RemoteFileManager();

	private LimitedStringSet unavailableUrls;
	private Map<String, Monitor> downloading;
	private ISavedState lastSavedState;
	private Boolean initialized = false;
	private LimitedStringSet usedFiles;
	private long cssFileUpdateTimeout;
	private long downloadingTimeout;
	private int downloadingIfModifiedTimeout;

	public static RemoteFileManager getInstance() {
		return INSTANCE;
	}

	protected RemoteFileManager() {
	}

	private void init() {
		synchronized (initialized) {
			if(!initialized) {
				IEclipsePreferences preferences = DefaultScope.INSTANCE.getNode(WebKbPlugin.PLUGIN_ID);
				int max = preferences.getInt(UNAVAILABLE_MAX_PREFERENCE, 500);
				long timeout = preferences.getLong(UNAVAILABLE_TIMEOUT_PREFERENCE, 180000);
				unavailableUrls = new LimitedStringSet(max, timeout); // By default keeps last 500 unavailable URLs during this Eclipse session for the last 3 minutes
				downloading = new HashMap<String, Monitor>();

				int cacheMax = preferences.getInt(FILE_CACHE_MAX_PREFERENCE, 100);
				long cacheTimeout = preferences.getLong(FILE_CACHE_TIMEOUT_PREFERENCE, 86400000L*30);
				usedFiles = new LimitedStringSet(cacheMax, cacheTimeout); // By default keeps last 100 CSS files used in the last 30 days in the cache

				cssFileUpdateTimeout = preferences.getLong(FILE_UPDATE_TIMEOUT_PREFERENCE, 900000); // 15 minutes since the local file has been downloaded is enough to check if the corresponding remote file has been changed to update the file if necessary
				downloadingTimeout = preferences.getLong(DOWNLOADING_TIMEOUT_PREFERENCE, 200); // If the file has been already scheduled for downloading then wait the file but not more then for 200 milliseconds by default before returning DOWNLOADING status
				downloadingIfModifiedTimeout = preferences.getInt(DOWNLOADING_IF_MODIFIED_TIMEOUT_PREFERENCE, 200); // The timeout for the connection used for updating modified remote files

				readState(lastSavedState);
				initialized = true;
			}
		}
	}

	public void doneSaving(ISaveContext context) {
		synchronized (initialized) {
			if(initialized) {
		        // delete the old saved state since it is not necessary anymore
				File file = getSaveFile(context.getPreviousSaveNumber());
		        file.delete();
			}
		}
    }

	public void rollback(ISaveContext context) {
		synchronized (initialized) {
			if(initialized) {
		        // since the save operation has failed, delete the saved state we have just written
				File file = getSaveFile(context.getSaveNumber());
				file.delete();
			}
		}
	}

	public void savingState(ISaveContext context) {
		synchronized (initialized) {
			if(initialized) {
				File file = getSaveFile(context.getSaveNumber());
				saveState(file);
				context.map(new Path(FILE_SAVE), new Path(file.getName()));
				context.needSaveNumber();
			}
		}
	}

	private void saveState(File file) {
		File folder = getStorageFolder();
		if(folder!=null) {
			if(folder.isDirectory()) {
				// Delete outdated downloaded files
				File[] files = folder.listFiles();
				if(files!=null) {
					for (File f : files) {
						if(!usedFiles.contains(f.getName())) {
							f.delete();
						}
					}
				}
			}
			usedFiles.save(file);
		}
	}

	private void readState(File file) {
		usedFiles.load(file);
	}

	private File getSaveFile(int saveNumber) {
        String saveFileName = FILE_SAVE + "-" + Integer.toString(saveNumber);
        File file = WebKbPlugin.getDefault().getStateLocation().append(saveFileName).toFile();
        return file;
	}

	private void readState(ISavedState state) {
        if (state != null) {
            IPath location = state.lookup(new Path(FILE_SAVE));
            if (location != null) {
            	File file = WebKbPlugin.getDefault().getStateLocation().append(location).toFile();
            	readState(file);
            }
        }
	}

	public void setLastSavedState(ISavedState state) {
		this.lastSavedState = state;
	}

	/**
	 * Downloads the file from the URL then stores it in the local workspace.
	 * If the file from the same URL has been already downloaded then returns the absolute path of its local copy.
	 * Otherwise starts a new downloading process is a separate thread.
	 * @param url
	 * @return
	 */
	public Result getFile(String url) {
		init();
		Result result = new Result(true);
		if(!unavailableUrls.contains(url)) {
			URL fullUrl = null;
			try {
				fullUrl = new URL(url);
			} catch (MalformedURLException e) {
				// ignore such URLs
				return new Result(true);
			}
			String protocol = fullUrl.getProtocol();
			if("http".equalsIgnoreCase(protocol) || "https".equalsIgnoreCase(protocol)) {
				// work only with http:// or https:// 
				String file = fullUrl.getFile();
				if(!file.isEmpty()) {
					File localFile = getLocalFile(fullUrl);
					if(localFile!=null) {
						Monitor monitor = null;
						Monitor newMonitor = null;
						synchronized (downloading) {
							monitor = downloading.get(url);
							if(monitor==null) {
								newMonitor = new Monitor();
								result = getPath(localFile, newMonitor);
								if(result!=null) {
									return result;
								}
								downloading.put(url, newMonitor);
							}
						}
						if(monitor==null) {
							DownloadingJob job = new DownloadingJob("Downloading CSS file from " + url, localFile, url, newMonitor);
							job.setPriority(Job.LONG);
							job.schedule();
							result = waitForDownloading(newMonitor, url); // Wait a second or less for the downloading job that we have just scheduled
						} else {
							result = waitForDownloading(monitor, url); // Wait a second or less for the downloading job that has been scheduled by another thread
						}
					}
				}
			}
		}
		return result;
	}

	private Result getPath(File file, Monitor monitor) {
		Result result = null;
		if(file.exists()) {
			long lastModified = file.lastModified();
			// Check if we should re-download changed file				
			if(System.currentTimeMillis() - lastModified <= cssFileUpdateTimeout) {
				usedFiles.add(file.getName());
				try {
					String path = file.getCanonicalPath();
					result = new Result(path);
				} catch (IOException e) {
					WebKbPlugin.getDefault().logError(e);
				}
			} else {
				// The corresponding remote file should be checked
				monitor.update = true;
			}
		}
		return result;
	}

	private Result waitForDownloading(Monitor monitor, String url) {
		Result result = null;
		try {
			synchronized(monitor) {
				if(monitor.locked) {
					monitor.wait(downloadingTimeout); 
					if(monitor.locked) { // If the monitor is still locked then return DOWNLOADING status 
						result = new Result();
					}
				}
			}
			if(result == null) {
				result = getFile(url);
			}
		} catch (InterruptedException e) {
			result = new Result();
		}
		return result;
	}

	private File getLocalFile(URL url) {
		File folder = getStorageFolder();
		if(folder!=null) {
			String fileName = null;
			try {
				fileName = URLEncoder.encode(url.toString(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// Ignore incorrect URLs
				return null;
			}
			File file = new File(folder, fileName);
			return file;
		}
		return null;
	}

	private File getStorageFolder() {
		WebKbPlugin plugin = WebKbPlugin.getDefault();
		if( plugin != null) {
			//The plug-in instance can be null at shutdown, when the plug-in is stopped. 
			IPath path = plugin.getStateLocation();
			File file = new File(path.toFile(), "downloadedCssFiles"); //$NON-NLS-1$
			return file;
		} else {
			return null;
		}
	}

	protected IDownloader getDownloader(File file, String url) {
		return new DownloaderImpl(file, url, downloadingIfModifiedTimeout);
	}

	private class DownloadingJob extends Job {

		private File file;
		private String url;
		private Monitor monitor;
		private IDownloader downloader;

		public DownloadingJob(String name, File file, String url, Monitor monitor) {
			super(name);
			this.file = file;
			this.url = url;
			this.setSystem(true);
			this.monitor = monitor;
			this.downloader = getDownloader(file, url);
		}

		@Override
		protected IStatus run(IProgressMonitor progressMonitor) {
			InputStream in = null;
			boolean log = false;
			try {
				in = downloader.getInputStream(monitor.update);
				if(in==null) {
					unavailableUrls.add(url);
				} else {
					log = true;
					file.getParentFile().mkdirs();
					FileOutputStream out = new FileOutputStream(file);
					IOUtil.copy(in, out);
				}
			} catch (IOException e) {
				if(!monitor.update) {
					unavailableUrls.add(url);
				}
				if(log) {
					WebKbPlugin.getDefault().logError(e);
				}
			} finally {
				IOUtil.close(in);
				synchronized (downloading) {
					downloading.remove(url);
				}
				synchronized (monitor) {
					monitor.locked = false;
					monitor.notifyAll();  // Notify all the waiting threads that the file has been downloaded or marked as unavailable.
				}
			}
			return Status.OK_STATUS;
		}

	}

	public static interface IDownloader {
		InputStream getInputStream(boolean ifModified) throws IOException;
	}

	private static class DownloaderImpl implements IDownloader {
		private File file;
		private String url;
		private int timeout;

		public DownloaderImpl(File file, String url, int timeout) {
			this.file = file;
			this.url = url;
			this.timeout = timeout;
		}

		public InputStream getInputStream(boolean ifModified) throws IOException {
			InputStream in = null;
			if(ifModified) {
				// Check if the file has been updated
				String dd = (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < 10) ? "d": "dd";
				SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM  " + dd + " HH:mm:ss yyyy", Locale.ENGLISH);
				formatter.setTimeZone(TimeZone.getDefault());
				Date date = new Date(file.lastModified());

				GetMethod method = new GetMethod(url);
				method.setRequestHeader("If-Modified-Since", formatter.format(date));
				HttpClient httpClient = HttpUtil.createHttpClient(url, timeout);
				httpClient.executeMethod(method);
				if(method.getStatusCode()==HttpStatus.SC_OK) {
					in = method.getResponseBodyAsStream();
					file.delete();
				} else {
					file.setLastModified(System.currentTimeMillis());
				}
			} else {
				// Download the file
				in = HttpUtil.getInputStreamFromUrlByGetMethod(url, true);
			}
			return in;
		}
	}

	private static class Monitor {
		boolean locked = true;
		boolean update; // If true then the file has been already downloaded but we need to update it if the corresponding remote file has been changed  
	}

	public static class Result {
		private String localPath;
		private DownloadingStatus status;

		public Result() {
			this(false);
		}

		public Result(boolean unavailable) {
			status = unavailable?DownloadingStatus.UNAVAILABLE:DownloadingStatus.DOWNLOADING;
		}

		public Result(String localPath) {
			this.localPath = localPath;
			status = DownloadingStatus.DOWNLOADED;
		}

		public String getLocalPath() {
			return localPath;
		}

		public void setLocalPath(String localPath) {
			this.localPath = localPath;
		}

		public DownloadingStatus getStatus() {
			return status;
		}

		public void setStatus(DownloadingStatus status) {
			this.status = status;
		}

		public boolean isReady() {
			return status == DownloadingStatus.DOWNLOADED;
		}
	}

	public static enum DownloadingStatus {
		DOWNLOADED,
		DOWNLOADING,
		UNAVAILABLE;
	}

	private static class LimitedStringSet {

		private int size;
		private long timeout;
		private TreeSet<TimestampedString> set = new TreeSet<TimestampedString>();
		private Map<String, TimestampedString> map = new HashMap<String, TimestampedString>();

	    public LimitedStringSet(int size, long timeoutInMillis) {
	        this.size = size;
	        this.timeout = timeoutInMillis;
	    }

	    synchronized public void add(String string) {
	    	TimestampedString oldTs = map.get(string);
	    	TimestampedString newTs = new TimestampedString(string);
	        map.put(string, newTs);
	        if(oldTs==null) {
		        set.add(newTs);
		        while (set.size() > size) {
		        	TimestampedString removed = set.pollFirst();
		        	if(removed!=null) {
		        		map.remove(removed.string);
		        	}
		        }
	    	} else if(!oldTs.equals(newTs)) {
		        set.add(newTs);
	    		set.remove(oldTs);
	    	}
	    }

	    synchronized public boolean contains(String string) {
	    	TimestampedString oldTs = map.get(string);
	    	if(oldTs!=null) {
		    	long time = System.currentTimeMillis() - timeout;
		    	if(oldTs.timestamp>time) {
		    		return true;
		    	}
	    		// Remove outdated string
	    		map.remove(oldTs.string);
	    		set.remove(oldTs);
	    	}
	    	return false;
	    }

	    synchronized public void save(File file) {
	    	StringBuilder sb = new StringBuilder();
	    	for (TimestampedString ts : set) {
				sb.append(ts.string).append(" ").append(ts.timestamp).append(" ");
			}
	    	if(sb.length()>0) {
	    		FileUtil.writeFile(file, sb.toString());
	    	}
	    }

	    synchronized private void reset() {
	    	set = new TreeSet<TimestampedString>();
	    	map = new HashMap<String, TimestampedString>();
	    }

	    synchronized public void load(File file) {
	    	reset();
			String content = FileUtil.readFile(file);
			StringTokenizer st = new StringTokenizer(content, " ");
			while(st.hasMoreTokens()) {
				String string = st.nextToken();
				if(!st.hasMoreTokens()) {
					WebKbPlugin.getDefault().logError("Corrupted JBT KB remote file manager state file: " + file.getAbsolutePath());
					reset();
					return;
				}
				String timestampStr = st.nextToken();
				long timestamp = 0;
				try {
					timestamp = Long.parseLong(timestampStr);
				} catch (NumberFormatException e) {
					WebKbPlugin.getDefault().logError(e);
					reset();
					return;
				}
				TimestampedString ts = new TimestampedString(string, timestamp);
				set.add(ts);
				map.put(string, ts);
			}
	    }
	}

	private static class TimestampedString implements Comparable<TimestampedString> {
		String string;
		long timestamp;

		public TimestampedString(String string) {
			this(string, System.currentTimeMillis());
		}

		public TimestampedString(String string, long timestamp) {
			this.string = string;
			this.timestamp = timestamp;
		}

		@Override
		public int hashCode() {
			return string.hashCode() + (int)timestamp;
		}

		@Override
		public boolean equals(Object obj) {
			if(obj instanceof TimestampedString) {
				TimestampedString ts = (TimestampedString)obj;
				return string.equals(ts.string) && timestamp == ts.timestamp;
			}
			return super.equals(obj);
		}

		@Override
		public int compareTo(TimestampedString o) {
			return (timestamp + string).compareTo(o.timestamp + o.string);
		}

		@Override
		public String toString() {
			return "{" + string + ":" + timestamp + "}";
		}
	}
}