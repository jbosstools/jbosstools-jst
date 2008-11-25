package org.jboss.tools.jst.jsp.outline.cssdialog;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * This is CSS dialog action delegate class that process actions that is contributed in Eclipse
 * regarding CSS Dialog.
 *
 * @author Igor Zhukov
 */
public class OpenCSSDialogActionDelegate implements IWorkbenchWindowActionDelegate, IObjectActionDelegate,
	IEditorActionDelegate, IViewActionDelegate {

	private Shell shell = null;
	private IStructuredSelection selection = null;

	/**
	 * Initializes this action delegate with the view it will work in.
	 *
	 * @param view the view that provides the context for this delegate
	 * @see IViewActionDelegate#init(IViewPart)
	 */
	public void init(IViewPart view) {
		if (view != null) {
			if (view.getSite() != null) {
				shell = view.getSite().getShell();
			} else if (view.getViewSite() != null) {
				shell = view.getViewSite().getShell();
			}
		}
	}

	/**
	 * Initializes this action delegate with the view it will work in.
	 *
	 * @param view the view that provides the context for this delegate
	 * @see IWorkbenchWindowActionDelegate#init(IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {
		if (window != null) {
			shell = window.getShell();
		}
	}

	/**
	 * Called when the user has selected this action to be executed.
	 */
	public void run(IAction action) {
		if (shell != null) {
			String styleClass = null;
			CSSClassDialog dialog = new CSSClassDialog(shell, selection, false);
			if (dialog.open() == Window.OK) {
				styleClass = dialog.getSelectorName();
			}
		}
	}

	/**
	 * Notifies this action delegate that the selection in the workbench has changed.
	 *
	 * @param action the action proxy that handles presentation portion of the action
	 * @param selection the current selection, or <code>null</code> if there is no selection.
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			this.selection = (IStructuredSelection)selection;
		}
	}

	/**
	 * Sets the active part for the delegate. This method will be called
	 * every time the action appears in a context menu. The targetPart
	 * may change with each invocation.
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		if (targetPart != null && targetPart.getSite() != null) {
			shell = targetPart.getSite().getShell();
		}
	}

	/**
	 * Sets the active editor for the delegate. Implementors should
	 * disconnect from the old editor, connect to the new editor, and
	 * update the action to reflect the new editor.
	 *
	 * @param action the action proxy that handles presentation portion of the action
	 * @param editor the new editor target
	 * @see IEditorActionDelegate #setActiveEditor(IAction, IEditorPart)
	 */
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		if (targetEditor != null && targetEditor.getSite() != null) {
			shell = targetEditor.getSite().getShell();
		}
	}

	public void dispose() {
	}
}
