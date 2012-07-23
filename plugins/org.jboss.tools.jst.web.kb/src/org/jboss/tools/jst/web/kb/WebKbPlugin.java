package org.jboss.tools.jst.web.kb;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ISaveContext;
import org.eclipse.core.resources.ISaveParticipant;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.common.EclipseUtil;
import org.jboss.tools.common.log.BaseUIPlugin;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.kb.internal.KbBuilder;
import org.jboss.tools.jst.web.kb.internal.KbProject;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class WebKbPlugin extends BaseUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.jst.web.kb"; //$NON-NLS-1$

	public static final String CA_KB_IMAGE_PATH = "icons/ca/el.gif"; //$NON-NLS-1$

	// The shared instance
	private static WebKbPlugin plugin;

	/**
	 * The constructor
	 */
	public WebKbPlugin() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener);

		ResourcesPlugin.getWorkspace().addSaveParticipant(PLUGIN_ID, new ISaveParticipant() {
			
			public void saving(ISaveContext context)
					throws CoreException {
				switch (context.getKind()) {
					case ISaveContext.SNAPSHOT:
					case ISaveContext.FULL_SAVE:
						IProject[] ps = ResourcesPlugin.getWorkspace().getRoot().getProjects();
						for (IProject p: ps) {
							KbProject sp = (KbProject)KbProjectFactory.getKbProject(p, false, true);
							if(sp != null && sp.getModificationsSinceLastStore() > 0) {
//								sp.printModifications();
								try {
									sp.store();
								} catch (IOException e) {
									WebKbPlugin.getDefault().logError(e);
								}
							}
						}
						break;
					case ISaveContext.PROJECT_SAVE:
						KbProject sp = (KbProject)KbProjectFactory.getKbProject(context.getProject(), false, true);
						try {
							if(sp != null && sp.getModificationsSinceLastStore() > 0) {
								sp.store();
							}
						} catch (IOException e) {
							WebKbPlugin.getDefault().logError(e);
						}
						break;
				}
				
				cleanObsoleteFiles();
			}
			
			public void rollback(ISaveContext context) {

			}
			
			public void prepareToSave(ISaveContext context) throws CoreException {
			}
			
			public void doneSaving(ISaveContext context) {
			}
		});
	}

	private void cleanObsoleteFiles() {
		IProject[] ps = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		Set<String> projectNames = new HashSet<String>();
		for (IProject p: ps) {
			projectNames.add(p.getName());
		}
		WebKbPlugin plugin = WebKbPlugin.getDefault();
		if(plugin!=null) {
			IPath path = plugin.getStateLocation();
			File file = new File(path.toFile(), "projects"); //$NON-NLS-1$
			if(!file.isDirectory()) return;
			File[] fs = file.listFiles();
			if(fs != null) for (File f: fs) {
				String name = f.getName();
				if(name.endsWith(".xml")) { //$NON-NLS-1$
					name = name.substring(0, name.length() - 4);
					if(!projectNames.contains(name)) {
						f.delete();
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceChangeListener);
		super.stop(context);
	}

	IResourceChangeListener resourceChangeListener = new RCL();

	class RCL implements IResourceChangeListener {

		public void resourceChanged(IResourceChangeEvent event) {
			if(event.getType() == IResourceChangeEvent.PRE_DELETE || event.getType() == IResourceChangeEvent.PRE_CLOSE) {
				IResource r = event.getResource();
				if(r instanceof IProject) {
					KbProject n = (KbProject)KbProjectFactory.getKbProject((IProject)r, false, true);
					if(n != null) {
						n.dispose();
					}
				}
			} else if(event.getType() == IResourceChangeEvent.POST_CHANGE) {
				IResourceDelta[] cs = event.getDelta().getAffectedChildren(IResourceDelta.CHANGED);
				for (IResourceDelta c: cs) {
					if((c.getFlags() & IResourceDelta.OPEN) != 0 && c.getResource() instanceof IProject) {
						IProject p = (IProject)c.getResource();
						KbProjectFactory.getKbProject(p, true, true);
					}
				}
			}
		}
	}
	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static WebKbPlugin getDefault() {
		return plugin;
	}

	public static void enableKB(IProject project, IProgressMonitor monitor) {
		try {
			WebModelPlugin.addNatureToProjectWithValidationSupport(project, KbBuilder.BUILDER_ID, IKbProject.NATURE_ID);
		} catch (CoreException e) {
			getDefault().logError(e);
		}
	}

	public static void disableKB(IProject project) {
		try {
			EclipseUtil.removeNatureFromProject(project, IKbProject.NATURE_ID);
		} catch (CoreException e) {
			getDefault().logError(e);
		}
	}

	public static ImageDescriptor getImageDescriptor(Class<?> baseClass, String imageName) {
		return ImageDescriptor.createFromFile(baseClass, imageName);
	}

	public static Image getImage(Class<?> baseClass, String imageName) {
		ImageRegistry registry = getDefault().getImageRegistry();
		Image result = registry.get(imageName);
		if(result == null || result.isDisposed()) {
			result = ImageDescriptor.createFromFile(baseClass, imageName).createImage();
			if(result != null) {
				registry.put(imageName, result);
			}
		}
		return result;
	}

}