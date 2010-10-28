package org.jboss.tools.jst.web.kb.test;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.jst.web.kb.KbProjectFactory;
import org.jboss.tools.jst.web.kb.internal.KbProject;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;
import org.jboss.tools.test.util.JobUtils;

public class KbModelStorageTest extends TestCase {
	IProject project = null;

	public KbModelStorageTest() {
		super("Kb Model Storage Test");
	}

	public void setUp() throws Exception {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject("TestKbModel2");
		assertNotNull("Can't load TestKbModel2", project); //$NON-NLS-1$
	}

	public void testStorage() throws Exception {
		KbProject kb = (KbProject)KbProjectFactory.getKbProject(project, true);
		assertNotNull(kb);
		int mod = kb.getModificationsSinceLastStore();
		System.out.println("-->" + mod);
		assertTrue("Modification index after load must be greater than 0", mod > 0);
		
		kb.store();
		mod = kb.getModificationsSinceLastStore();
		System.out.println("-->" + mod);
		assertEquals("Modification index after store must be cleared", 0, mod);
		
		IFile fromFile = project.getFile(new Path("WebContent/WEB-INF/lib/jsf-impl.1"));
		IFile toFile = project.getFile(new Path("WebContent/WEB-INF/lib/jsf-impl.jar"));
		
		toFile.create(fromFile.getContents(), true, new NullProgressMonitor());
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		JobUtils.waitForIdle();
		project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		JobUtils.waitForIdle();
		project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, new NullProgressMonitor());
		JobUtils.waitForIdle();
		
		ITagLibrary[] ls = kb.getTagLibraries("http://java.sun.com/jsf/core");
		for (ITagLibrary lb: ls) {
			System.out.println(lb.getSourcePath());
		}
		mod = kb.getModificationsSinceLastStore();
		System.out.println("-->" + mod);
		assertTrue("Modification index after adding new library must be greater than 0", mod > 0);
	}

}
