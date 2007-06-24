/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Exadel, Inc.
 *     Red Hat, Inc. 
 *******************************************************************************/
package org.jboss.tools.jst.web.debug.ui.internal.views.xpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IExpression;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.debug.internal.ui.DebugPluginImages;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.internal.ui.IDebugHelpContextIds;
import org.eclipse.debug.internal.ui.IInternalDebugUIConstants;
import org.eclipse.debug.internal.ui.LazyModelPresentation;
import org.eclipse.debug.internal.ui.VariablesViewModelPresentation;
import org.eclipse.debug.internal.ui.actions.variables.ChangeVariableValueAction;
import org.eclipse.debug.internal.ui.actions.variables.ShowTypesAction;
import org.eclipse.debug.internal.ui.preferences.IDebugPreferenceConstants;
import org.eclipse.debug.internal.ui.views.IDebugExceptionHandler;
import org.eclipse.debug.internal.ui.views.variables.VariablesView;
import org.eclipse.debug.internal.ui.views.variables.VariablesViewMessages;
import org.eclipse.debug.ui.AbstractDebugView;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.debug.ui.IValueDetailListener;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.jdt.internal.debug.core.model.JDINullValue;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.console.actions.TextViewerAction;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.part.WorkbenchPart;
import org.eclipse.ui.texteditor.FindReplaceAction;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.IUpdate;
import org.jboss.tools.jst.web.debug.ui.xpl.WebDebugUIPlugin;

/**
 *
 * @author Jeremy
 * 
 */

public class WebDataView extends VariablesView implements
		ISelectionListener, IPropertyChangeListener, IValueDetailListener,
		IDebugExceptionHandler, INullSelectionListener {

	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
	}

	public class ShowDetailPaneAction extends Action {

		private WebDataView fWebDataView;

		public ShowDetailPaneAction(WebDataView view) {
			super(
					WebDebugUIPlugin
							.getResourceString("ShowDetailPaneAction.Show_Variable_Detail_Pane_1"), Action.AS_CHECK_BOX); //$NON-NLS-1$
			setWebDataView(view);
			setToolTipText(WebDebugUIPlugin
					.getResourceString("ShowDetailPaneAction.Show_detail_pane_2")); //$NON-NLS-1$
			setImageDescriptor(DebugPluginImages
					.getImageDescriptor(IInternalDebugUIConstants.IMG_ELCL_DETAIL_PANE));
			setDisabledImageDescriptor(DebugPluginImages
					.getImageDescriptor(IInternalDebugUIConstants.IMG_DLCL_DETAIL_PANE));
			setHoverImageDescriptor(DebugPluginImages
					.getImageDescriptor(IDebugUIConstants.IMG_LCL_DETAIL_PANE));
			setId(DebugUIPlugin.getUniqueIdentifier() + ".ShowDetailPaneAction"); //$NON-NLS-1$
		}

		/**
		 * @see Action#run()
		 */
		public void run() {
			toggleDetailPane(isChecked());
		}

		/**
		 * Ask the VariablesView to toggle its detail pane and reset this
		 * action's tooltip as appropriate.
		 */
		private void toggleDetailPane(boolean on) {
			getWebDataView().toggleDetailPane(on);
		}

		/**
		 * @see Action#setChecked(boolean)
		 */
		public void setChecked(boolean value) {
			super.setChecked(value);
			toggleDetailPane(value);
		}

		protected WebDataView getWebDataView() {
			return fWebDataView;
		}

		protected void setWebDataView(WebDataView webDataView) {
			fWebDataView = webDataView;
		}
	}

	class WebDataViewModelPresentation extends VariablesViewModelPresentation {
		public String getText(Object element) {
			try {
				if (element instanceof WebData) {
					return ((WebData) element).getName();
				} else if (element instanceof StrutsData) {
					return ((StrutsData) element).getName();
				} else if (element instanceof EmptyVariable) {
					return ((EmptyVariable) element)
							.getText(showVariableTypeNames());

				} else if (element instanceof HttpServletRequestVariableProxy
						|| element instanceof HttpServletResponseVariableProxy
						|| element instanceof HttpSessionVariableProxy
						|| element instanceof ServletContextVariableProxy
						|| element instanceof PageContextVariableProxy) {
					return getVariableProxyText((IVariable) element, false);
				} else if (element instanceof ActionMappingVariableProxy) {
					return getVariableProxyText((IVariable) element, false);
				} else if (element instanceof ActionVariableProxy) {
					return getVariableProxyText((IVariable) element, false);
				} else if (element instanceof ActionFormVariableProxy) {
					return getVariableProxyText((IVariable) element, false);
				} else if (element instanceof HashMapVariableProxy
						|| element instanceof HashMapEntryVariableProxy) {
					return getVariableProxyText((IVariable) element, false);
				} else if (element instanceof EnumerationVariableProxy
						|| element instanceof EnumerationEntryVariableProxy) {
					return getVariableProxyText((IVariable) element, false);
				} else if (element instanceof ArrayPartitionVariableProxy) {
					return getArrayPartitionVariableProxyText((IVariable) element);
				} else if (element instanceof ArrayVariableProxy) {
					return getArrayVariableProxyText((IVariable) element, false);
				} else if (element instanceof ArrayElementVariableProxy) {
					return getArrayVariableProxyText((IVariable) element, true);
				} else if (element instanceof EvaluatedVariable) {
					return getVariableProxyText((IVariable) element, true);
				} else if (element instanceof VariableProxy) {
					return getVariableProxyText((IVariable) element, true);
				}
			} catch (Exception e) {
			}
			return super.getText(element);
		}

		private String getVariableProxyText(IVariable variable,
				boolean quoteValue) {
			StringBuffer text = new StringBuffer();
			try {
				IValue value = variable.getValue();
				if (showVariableTypeNames())
					text = text.append(variable.getReferenceTypeName()).append(
							' ');

				text = text.append(variable.getName()).append(" = ");
				boolean notNull = !(value == null || value instanceof JDINullValue);
				IJavaType type = null;
				if (notNull) {
					if (value instanceof ValueProxy) {
						if (((ValueProxy) value).getOrigin() != null
								&& !(((ValueProxy) value).getOrigin() instanceof JDINullValue))
							type = ((IJavaValue) ((ValueProxy) value).fOrigin)
									.getJavaType();
						else {
							value = null;
							notNull = false;
						}
					} else {
						type = ((IJavaValue) value).getJavaType();
					}
				}
				quoteValue = (notNull ? TypeSupport.isSimpleTypeOrWrapper(type)
						: false);
				if (notNull && quoteValue)
					text = text.append('"');
				text = text.append((value == null ? "<Not Available>" : value
						.getValueString()));
				if (notNull && quoteValue)
					text = text.append('"');
			} catch (Exception e) {
			}
			return text.toString();
		}

		private String getArrayVariableProxyText(IVariable variable,
				boolean quoteValue) {
			StringBuffer text = new StringBuffer();
			try {
				IValue value = variable.getValue();
				if (showVariableTypeNames())
					text = text.append(variable.getReferenceTypeName()).append(
							' ');
				text = text.append(variable.getName()).append(" = ");
				boolean notNull = !(value == null || value instanceof JDINullValue);
				IJavaType type = null;
				if (notNull) {
					if (value instanceof ValueProxy) {
						if (((ValueProxy) value).getOrigin() != null)
							type = ((IJavaValue) ((ValueProxy) value).fOrigin)
									.getJavaType();
					} else {
						type = ((IJavaValue) value).getJavaType();
					}
				}
				quoteValue = (notNull ? TypeSupport.isSimpleTypeOrWrapper(type)
						: false);
				if (notNull && quoteValue)
					text = text.append('"');
				text = text.append((value == null ? "<Not Available>" : value
						.getValueString()));
				if (notNull && quoteValue)
					text = text.append('"');
			} catch (Exception e) {
			}
			return text.toString();
		}

		private String getArrayPartitionVariableProxyText(IVariable variable) {
			StringBuffer text = new StringBuffer();
			try {
				IValue value = variable.getValue();
				if (showVariableTypeNames())
					text = text.append(variable.getReferenceTypeName()).append(
							' ');
				text = text.append(variable.getName());
			} catch (Exception e) {
			}
			return text.toString();
		}

		private class ProxyValueDetailListener implements IValueDetailListener {
			IValue fOriginalSource;

			IValue fProxySource;

			IValueDetailListener fValueDetailListener;

			ProxyValueDetailListener(IValue originalSource, IValue proxySource,
					IValueDetailListener listener) {
				this.fOriginalSource = originalSource;
				this.fProxySource = proxySource;
				this.fValueDetailListener = listener;
			}

			/**
			 * @see org.eclipse.debug.ui.IValueDetailListener#detailComputed(org.eclipse.debug.core.model.IValue,
			 *      java.lang.String)
			 */
			public void detailComputed(IValue value, String result) {
				fValueDetailListener.detailComputed(fOriginalSource, result);
			}
		}

		/**
		 * @see IDebugModelPresentation#computeDetail(IValue,
		 *      IValueDetailListener)
		 */
		public void computeDetail(IValue value, IValueDetailListener listener) {
			if (value instanceof WebData || value instanceof StrutsData) {
				listener.detailComputed(value, ""); //$NON-NLS-1$
				return;
			}
			if (value instanceof ActionMappingValueProxy) {
				IValue origin = null;
				try {
					origin = ((ActionMappingValueProxy) value).getOrigin();
				} catch (Exception e) {
				}
				if (origin != null) {
					ProxyValueDetailListener pl = new ProxyValueDetailListener(
							value, origin, listener);
					super.computeDetail(origin, pl);
				} else {
					listener.detailComputed(value, "");
				}

				return;
			}
			if (value instanceof ValueProxy) {
				IValue origin = null;
				try {
					origin = ((ValueProxy) value).getOrigin();
				} catch (Exception e) {
				}
				if (origin != null) {
					ProxyValueDetailListener pl = new ProxyValueDetailListener(
							value, origin, listener);
					super.computeDetail(origin, pl);
				} else {
					listener.detailComputed(value, "");
				}

				return;

			}

			super.computeDetail(value, listener);
		}
	}

	/**
	 * 
	 */
	class WebDataViewLabelProvider implements ILabelProvider, IColorProvider {

		private IDebugModelPresentation presentation;

		public WebDataViewLabelProvider(IDebugModelPresentation presentation) {
			this.presentation = presentation;
		}

		public IDebugModelPresentation getPresentation() {
			return presentation;
		}

		public Image getImage(Object element) {
			return presentation.getImage(element);
		}

		public String getText(Object element) {

			String text = presentation.getText(element);

			return text;
		}

		public void addListener(ILabelProviderListener listener) {
			presentation.addListener(listener);
		}

		public void dispose() {
			presentation.dispose();
		}

		public boolean isLabelProperty(Object element, String property) {
			return presentation.isLabelProperty(element, property);
		}

		public void removeListener(ILabelProviderListener listener) {
			presentation.removeListener(listener);
		}

		public Color getForeground(Object element) {
			if (element instanceof IVariable) {
				IVariable variable = (IVariable) element;
				try {
					if (variable.hasValueChanged()) {
						// return
						// DebugUIPlugin.getPreferenceColor(IDebugPreferenceConstants.CHANGED_VARIABLE_RGB);
						return DebugUIPlugin
								.getPreferenceColor("CHANGED_VARIABLE_RGB");
					}
				} catch (DebugException e) {
					DebugUIPlugin.log(e);
				}
			}
			return null;
		}

		public Color getBackground(Object element) {
			return null;
		}

	}

	/**
	 * 
	 */
	class WebDataViewSelectionProvider implements ISelectionProvider {
		private ListenerList fListeners = new ListenerList();

		private ISelectionProvider fUnderlyingSelectionProvider;

		/**
		 * @see ISelectionProvider#addSelectionChangedListener(ISelectionChangedListener)
		 */
		public void addSelectionChangedListener(
				ISelectionChangedListener listener) {
			fListeners.add(listener);
		}

		/**
		 * @see ISelectionProvider#getSelection()
		 */
		public ISelection getSelection() {
			return getUnderlyingSelectionProvider().getSelection();
		}

		/**
		 * @see ISelectionProvider#removeSelectionChangedListener(ISelectionChangedListener)
		 */
		public void removeSelectionChangedListener(
				ISelectionChangedListener listener) {
			fListeners.remove(listener);
		}

		/**
		 * @see ISelectionProvider#setSelection(ISelection)
		 */
		public void setSelection(ISelection selection) {
			getUnderlyingSelectionProvider().setSelection(selection);
		}

		protected ISelectionProvider getUnderlyingSelectionProvider() {
			return fUnderlyingSelectionProvider;
		}

		protected void setUnderlyingSelectionProvider(
				ISelectionProvider underlyingSelectionProvider) {
			fUnderlyingSelectionProvider = underlyingSelectionProvider;
		}

		protected void fireSelectionChanged(SelectionChangedEvent event) {
			Object[] listeners = fListeners.getListeners();
			for (int i = 0; i < listeners.length; i++) {
				ISelectionChangedListener listener = (ISelectionChangedListener) listeners[i];
				listener.selectionChanged(event);
			}
		}
	}

	/**
	 * 
	 */
	private WebDataViewModelPresentation fModelPresentation;

	/**
	 * 
	 */
	private List fSelectionActions = new ArrayList(3);

	/**
	 * 
	 */
	private WebDataViewSelectionProvider fSelectionProvider = new WebDataViewSelectionProvider();

	/**
	 * 
	 */
	private SashForm fSashForm;

	/**
	 * 
	 */
	private ISourceViewer fDetailViewer;

	private IDocument fDetailDocument;

	/**
	 * 
	 */
	private String fDebugModelIdentifier;

	/**
	 * 
	 */
	private SourceViewerConfiguration fSourceViewerConfiguration;

	/**
	 * 
	 */
	private IStructuredSelection fValueSelection = null;

	/**
	 * 
	 */
	private IValue fLastValueDetail = null;

	/**
	 * Iterator for multi-selection details computation
	 */
	private Iterator fSelectionIterator = null;

	/**
	 * Various listeners used to update the enabled state of actions and also to
	 * populate the detail pane.
	 */
	private ISelectionChangedListener fTreeSelectionChangedListener;

	private ISelectionChangedListener fDetailSelectionChangedListener;

	private IDocumentListener fDetailDocumentListener;

	/**
	 * A map of stack frames to <code>ViewerState</code>s. Used to restore
	 * the expanded state of the variables view on re-selection of the same
	 * stack frame. The cache is cleared on a frame by frame basis when a
	 * thread/target is terminated.
	 */
	private HashMap fExpandedVariables = new HashMap(10);

	private HashSet cachedFrames = new HashSet();

	/**
	 * Remembers which viewer (tree viewer or details viewer) had focus, so we
	 * can reset the focus properly when re-activated.
	 */
	private Viewer fFocusViewer = null;

	/**
	 * These are used to initialize and persist the position of the sash that
	 * separates the tree viewer from the detail pane.
	 */
	private static final int[] DEFAULT_SASH_WEIGHTS = { 6, 2 };

	private int[] fLastSashWeights;

	private boolean fToggledDetailOnce;

	protected static final String DETAIL_SELECT_ALL_ACTION = SELECT_ALL_ACTION
			+ ".Detail"; //$NON-NLS-1$

	protected static final String VARIABLES_SELECT_ALL_ACTION = SELECT_ALL_ACTION
			+ ".Variables"; //$NON-NLS-1$

	protected static final String DETAIL_COPY_ACTION = ITextEditorActionConstants.COPY
			+ ".Detail"; //$NON-NLS-1$

	protected static final String VARIABLES_COPY_ACTION = ITextEditorActionConstants.COPY
			+ ".Variables"; //$NON-NLS-1$

	/**
	 * Remove myself as a selection listener and preference change listener.
	 * 
	 * @see IWorkbenchPart#dispose()
	 */
	public void dispose() {
		getSite().getPage().removeSelectionListener(
				IDebugUIConstants.ID_DEBUG_VIEW, this);
		DebugUIPlugin.getDefault().getPreferenceStore()
				.removePropertyChangeListener(this);
		JFaceResources.getFontRegistry().removeListener(this);
		Viewer viewer = getViewer();
		if (viewer != null) {
			getDetailDocument().removeDocumentListener(
					getDetailDocumentListener());
		}
		super.dispose();
	}

	protected void setViewerInput(IStructuredSelection ssel) {
		try {
			WebDataViewInput currentInput = (WebDataViewInput) getViewer()
					.getInput();
			IStackFrame current = (currentInput == null) ? null : currentInput
					.getFrame();

			WebDataViewInput newInput = null;
			IStackFrame frame = null;
			boolean modified = false;
			if (ssel.size() == 1) {
				Object input = ssel.getFirstElement();
				if (input instanceof IStackFrame) {
					frame = (IStackFrame) input;
					newInput = WebDataViewInput.create(frame);
					if (newInput != null) {
						if (newInput == currentInput
								&& newInput.getFrame() != frame) {
							modified = true;
						}
					}
					newInput.setStackFrame(frame);
				} else if (input instanceof IJavaThread) {
					newInput = WebDataViewInput.create((IJavaThread) input);
					frame = (newInput == null) ? null : newInput.getFrame();
					if (frame == null || newInput.getThread().isTerminated()) {
						newInput = null;
					} else if (newInput == currentInput) {
					}
				} else {
					return;
				}
			}

			if (newInput != null
					&& (newInput.getFrame() == null || !newInput.getFrame()
							.isSuspended())) {
				newInput = null;
			}

			getDetailViewer().setEditable(newInput != null);

			if (currentInput == null && newInput == null) {
				return;
			}

			if (newInput == currentInput) {
				if (modified) {
					getViewer().refresh();
				}
				return;
			}
			saveTree();

			if (frame != null) {
				setDebugModel(frame.getModelIdentifier());
			}
			showViewer();

			getViewer().setInput(newInput);

			loadTree();
		} catch (Exception x) {
			//ignore
		}
	}

	private void saveTree() {
		WebDataViewInput currentInput = (WebDataViewInput) getViewer()
				.getInput();
		IThread thread = (currentInput == null) ? null : currentInput
				.getThread();
		if (thread == null || thread.isTerminated())
			return;
		if (currentInput.getFrame() != null)
			cachedFrames.add(currentInput.getFrame());
		// TODO WTP 2.0
//		ViewerState state = new ViewerState(getWebDataViewer());
//		fExpandedVariables.put(thread, state);
	}

	private void loadTree() {
		WebDataViewInput currentInput = (WebDataViewInput) getViewer()
				.getInput();
		IThread thread = (currentInput == null) ? null : currentInput
				.getThread();
		if (thread == null || thread.isTerminated())
			return;
		// TODO WTP 2.0
//		ViewerState state = (ViewerState) fExpandedVariables.get(thread);
//		if (state != null) {
//			state.restoreState(getWebDataViewer());
//		}
	}

	/**
	 * Returns the variables viewer for this view
	 */
	protected WebDataViewer getWebDataViewer() {
		// TODO-3.3: WTP 2.0
//		return (WebDataViewer) getViewer();
		return null;
	}

	/**
	 * Clears expanded state for stack frames which are a child of the given
	 * thread or debug target.
	 */
	protected void clearExpandedVariables(Object parent) {
		List list = null;
		if (parent instanceof IThread) {
			IThread t = (IThread) parent;
			list = getCachedFrames(t);
			fExpandedVariables.remove(t);
		} else if (parent instanceof IDebugTarget) {
			IDebugTarget d = (IDebugTarget) parent;
			list = getCachedFrames(d);
			IThread[] t = new IThread[0];
			try {
				t = d.getThreads();
			} catch (Exception e) {
			}
			for (int i = 0; i < t.length; i++)
				fExpandedVariables.remove(t[i]);
		}
		if (list != null) {
			Iterator frames = list.iterator();
			while (frames.hasNext()) {
				Object frame = frames.next();
				cachedFrames.remove(frame);
			}
		}
	}

	/**
	 * Returns a list of stack frames in the specified thread that have cached
	 * an expansion state.
	 * 
	 * @return a list of stack frames in the specified thread that have cached
	 *         an expansion state
	 */
	protected List getCachedFrames(IThread thread) {
		List list = null;
		Iterator frames = cachedFrames.iterator();
		while (frames.hasNext()) {
			IStackFrame frame = (IStackFrame) frames.next();
			if (frame.getThread().equals(thread)) {
				if (list == null) {
					list = new ArrayList();
				}
				list.add(frame);
			}
		}
		return list;
	}

	/**
	 * Returns a list of stack frames in the specified thread that have cached
	 * an expansion state.
	 * 
	 * @return a list of stack frames in the specified thread that have cached
	 *         an expansion state
	 */
	protected List getCachedFrames(IDebugTarget target) {
		List list = null;
		Iterator frames = cachedFrames.iterator();
		while (frames.hasNext()) {
			IStackFrame frame = (IStackFrame) frames.next();
			if (frame.getDebugTarget().equals(target)) {
				if (list == null) {
					list = new ArrayList();
				}
				list.add(frame);
			}
		}
		return list;
	}

	protected IDebugModelPresentation getModelPresentation() {
		if (fModelPresentation == null) {
			fModelPresentation = new WebDataViewModelPresentation();
		}
		return fModelPresentation;
	}

	/**
	 * Configures the details viewer for the debug model currently being
	 * displayed
	 */
	protected void configureDetailsViewer() {
		LazyModelPresentation mp = (LazyModelPresentation) fModelPresentation
				.getPresentation(getDebugModel());
		SourceViewerConfiguration svc = null;
		if (mp != null) {
			try {
				svc = mp.newDetailsViewerConfiguration();
			} catch (CoreException e) {
				DebugUIPlugin
						.errorDialog(
								getSite().getShell(),
								VariablesViewMessages.VariablesView_Error_1,
								VariablesViewMessages.VariablesView_Unable_to_configure_variable_details_area__2,
								e); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		if (svc == null) {
			svc = new SourceViewerConfiguration();
			getDetailViewer().setEditable(false);
		}
		getDetailViewer().configure(svc);
		// update actions that depend on the configuration of the details viewer
		updateAction("ContentAssist"); //$NON-NLS-1$
		setDetailViewerConfiguration(svc);
	}

	/**
	 * @see IPropertyChangeListener#propertyChange(PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		String propertyName = event.getProperty();
		if (propertyName
				.equals(IDebugPreferenceConstants.VARIABLES_DETAIL_PANE_ORIENTATION)) {
			setDetailPaneOrientation(DebugUIPlugin
					.getDefault()
					.getPreferenceStore()
					.getString(
							IDebugPreferenceConstants.VARIABLES_DETAIL_PANE_ORIENTATION));
		} else if (propertyName
				.equals("CHANGED_VARIABLE_RGB"/* IDebugPreferenceConstants.CHANGED_VARIABLE_COLOR */)) {
			// TODO replace with valid code
			getVariablesViewer().refresh(); // attempt of valid code
			// / getEventHandler().refresh();
		} else if (propertyName
				.equals(IInternalDebugUIConstants.DETAIL_PANE_FONT)) {
			getDetailViewer()
					.getTextWidget()
					.setFont(
							JFaceResources
									.getFont(IInternalDebugUIConstants.DETAIL_PANE_FONT));
		}
	}

	// TODO-3.3: WTP 2.0
//	AsynchronousTreeViewer variablesViewer;

	/**
	 * @see AbstractDebugView#createViewer(Composite)
	 */
	public Viewer createViewer(Composite parent) {
		try {
			fModelPresentation = new WebDataViewModelPresentation();
			DebugUIPlugin.getDefault().getPreferenceStore()
					.addPropertyChangeListener(this);
			JFaceResources.getFontRegistry().addListener(this);
			// create the sash form that will contain the tree viewer & text
			// viewer
			fSashForm = new SashForm(parent, SWT.NONE);
			IPreferenceStore prefStore = DebugUIPlugin.getDefault()
					.getPreferenceStore();
			String orientString = prefStore
					.getString(IDebugPreferenceConstants.VARIABLES_DETAIL_PANE_ORIENTATION);
			setDetailPaneOrientation(orientString);

			// TODO-3.3: WTP 2.0
			// add tree viewer
//			variablesViewer = new WebDataViewer(getSashForm(), SWT.MULTI
//					| SWT.V_SCROLL | SWT.H_SCROLL);
//			variablesViewer.setContentProvider(createContentProvider());
//			variablesViewer.setLabelProvider(new WebDataViewLabelProvider(
//					getModelPresentation()));
//			variablesViewer.setUseHashlookup(true);
//			variablesViewer.getControl().addFocusListener(new FocusAdapter() {
//				/**
//				 * @see FocusListener#focusGained(FocusEvent)
//				 */
//				public void focusGained(FocusEvent e) {
//					getWebDataViewSelectionProvider()
//							.setUnderlyingSelectionProvider(variablesViewer);
//					setAction(SELECT_ALL_ACTION,
//							getAction(VARIABLES_SELECT_ALL_ACTION));
//					setAction(COPY_ACTION, getAction(VARIABLES_COPY_ACTION));
//					getViewSite().getActionBars().updateActionBars();
//					setFocusViewer(getWebDataViewer());
//				}
//			});
//			variablesViewer
//					.addSelectionChangedListener(getTreeSelectionChangedListener());
//			getWebDataViewSelectionProvider().setUnderlyingSelectionProvider(
//					variablesViewer);
//			getSite().setSelectionProvider(getWebDataViewSelectionProvider());
//
//			// add text viewer
//			SourceViewer detailsViewer = new SourceViewer(getSashForm(), null,
//					SWT.V_SCROLL | SWT.H_SCROLL);
//			setDetailViewer(detailsViewer);
//			detailsViewer.setDocument(getDetailDocument());
//			detailsViewer
//					.getTextWidget()
//					.setFont(
//							JFaceResources
//									.getFont(IInternalDebugUIConstants.DETAIL_PANE_FONT));
//			getDetailDocument()
//					.addDocumentListener(getDetailDocumentListener());
//			detailsViewer.setEditable(false);
//			getSashForm().setMaximizedControl(variablesViewer.getControl());
//
//			detailsViewer.getSelectionProvider().addSelectionChangedListener(
//					getDetailSelectionChangedListener());
//
//			detailsViewer.getControl().addFocusListener(new FocusAdapter() {
//				/**
//				 * @see FocusListener#focusGained(FocusEvent)
//				 */
//				public void focusGained(FocusEvent e) {
//					getWebDataViewSelectionProvider()
//							.setUnderlyingSelectionProvider(
//									getDetailViewer().getSelectionProvider());
//					setAction(SELECT_ALL_ACTION,
//							getAction(DETAIL_SELECT_ALL_ACTION));
//					setAction(COPY_ACTION, getAction(DETAIL_COPY_ACTION));
//					getViewSite().getActionBars().updateActionBars();
//					setFocusViewer((Viewer) getDetailViewer());
//				}
//			});
//			// add a context menu to the detail area
//			createDetailContextMenu(detailsViewer.getTextWidget());
//
//			// listen to selection in debug view
//			getSite().getPage().addSelectionListener(
//					IDebugUIConstants.ID_DEBUG_VIEW, this);
//			// TODO replace with valid code
//			// / setEventHandler(createEventHandler(variablesViewer));
//
//			return variablesViewer;
			return null;
		} catch (Exception x) {
			//ignore
			return null;
		}
	}

	/**
	 * Sets the viewer used to display value details.
	 * 
	 * @param viewer
	 *            source viewer
	 */
	private void setDetailViewer(ISourceViewer viewer) {
		fDetailViewer = viewer;
	}

	/**
	 * Returns the viewer used to display value details
	 * 
	 * @return source viewer
	 */
	protected ISourceViewer getDetailViewer() {
		return fDetailViewer;
	}

	protected WebDataViewSelectionProvider getWebDataViewSelectionProvider() {
		return fSelectionProvider;
	}

	/**
	 * Creates this view's content provider.
	 * 
	 * @return a content provider
	 */
	protected IContentProvider createContentProvider() {
		WebDataViewContentProvider cp = new WebDataViewContentProvider();
		cp.setExceptionHandler(this);
		return cp;
	}

	/**
	 * @see AbstractDebugView#getHelpContextId()
	 */
	protected String getHelpContextId() {
		return IDebugHelpContextIds.VARIABLE_VIEW;
	}

	/**
	 * Set the orientation of the sash form to display its controls according to
	 * the value of <code>value</code>. 'VARIABLES_DETAIL_PANE_UNDERNEATH'
	 * means that the detail pane appears underneath the tree viewer,
	 * 'VARIABLES_DETAIL_PANE_RIGHT' means the detail pane appears to the right
	 * of the tree viewer.
	 */
	public void setDetailPaneOrientation(String value) {
		int orientation = value
				.equals(IDebugPreferenceConstants.VARIABLES_DETAIL_PANE_UNDERNEATH) ? SWT.VERTICAL
				: SWT.HORIZONTAL;
		getSashForm().setOrientation(orientation);
	}

	/**
	 * Show or hide the detail pane, based on the value of <code>on</code>.
	 * If showing, reset the sash form to use the relative weights that were in
	 * effect the last time the detail pane was visible, and populate it with
	 * details for the current selection. If hiding, save the current relative
	 * weights, unless the detail pane hasn't yet been shown.
	 */
	public void toggleDetailPane(boolean on) {
		if (on) {
			getSashForm().setMaximizedControl(null);
			getSashForm().setWeights(getLastSashWeights());
			populateDetailPane();
			fToggledDetailOnce = true;
		} else {
			if (fToggledDetailOnce) {
				setLastSashWeights(getSashForm().getWeights());
			}
			getSashForm().setMaximizedControl(getViewer().getControl());
		}
	}

	/**
	 * Set on or off the word wrap flag for the detail pane.
	 */
	public void toggleDetailPaneWordWrap(boolean on) {
		fDetailViewer.getTextWidget().setWordWrap(on);
	}

	/**
	 * Ask the variables tree for its current selection, and use this to
	 * populate the detail pane.
	 */
	public void populateDetailPane() {
		if (isDetailPaneVisible()) {
			IStructuredSelection selection = (IStructuredSelection) getViewer()
					.getSelection();
			populateDetailPaneFromSelection(selection);
		}
	}

	/**
	 * Return the relative weights that were in effect the last time both panes
	 * were visible in the sash form, or the default weights if both panes have
	 * not yet been made visible.
	 */
	protected int[] getLastSashWeights() {
		if (fLastSashWeights == null) {
			fLastSashWeights = DEFAULT_SASH_WEIGHTS;
		}
		return fLastSashWeights;
	}

	/**
	 * Set the current relative weights of the controls in the sash form, so
	 * that the sash form can be reset to this layout at a later time.
	 */
	protected void setLastSashWeights(int[] weights) {
		fLastSashWeights = weights;
	}

	/**
	 * Initializes the viewer input on creation
	 */
	protected void setInitialContent() {
		ISelection selection = getSite().getPage().getSelection(
				IDebugUIConstants.ID_DEBUG_VIEW);
		if (selection instanceof IStructuredSelection && !selection.isEmpty()) {
			setViewerInput((IStructuredSelection) selection);
		}
	}

	/**
	 * Create the context menu particular to the detail pane. Note that anyone
	 * wishing to contribute an action to this menu must use
	 * <code>IDebugUIConstants.VARIABLE_VIEW_DETAIL_ID</code> as the
	 * <code>targetID</code> in the extension XML.
	 */
	protected void createDetailContextMenu(Control menuControl) {
		MenuManager menuMgr = new MenuManager(); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager mgr) {
				fillDetailContextMenu(mgr);
			}
		});
		Menu menu = menuMgr.createContextMenu(menuControl);
		menuControl.setMenu(menu);

		// register the context menu such that other plugins may contribute to
		// it
		getSite().registerContextMenu(
				IDebugUIConstants.VARIABLE_VIEW_DETAIL_ID, menuMgr,
				getDetailViewer().getSelectionProvider());
		addContextMenuManager(menuMgr);
	}

	/**
	 * @see AbstractDebugView#createActions()
	 */
	protected void createActions() {
		IAction action = new ShowTypesAction(this);
		setAction("ShowTypeNames", action); //$NON-NLS-1$

		// TODO Eclipse 3.1 migration
		VariablesView view = (VariablesView) super
				.findView(IDebugUIConstants.ID_VARIABLE_VIEW);
		if (view != null) {
			action = new ChangeVariableValueAction(view);
			action.setEnabled(false);
			setAction("ChangeVariableValue", action); //$NON-NLS-1$
			setAction(DOUBLE_CLICK_ACTION, action);

			action = new ShowDetailPaneAction(this);
			setAction("ShowDetailPane", action); //$NON-NLS-1$
		}

		// detail specific actions

		TextViewerAction textAction = new TextViewerAction(getDetailViewer(),
				ISourceViewer.CONTENTASSIST_PROPOSALS);
		textAction
				.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
		textAction.configureAction(
				VariablesViewMessages.VariablesView_Co_ntent_Assist_3, "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		textAction.setImageDescriptor(DebugPluginImages
				.getImageDescriptor(IDebugUIConstants.IMG_ELCL_CONTENT_ASSIST));
		textAction.setHoverImageDescriptor(DebugPluginImages
				.getImageDescriptor(IDebugUIConstants.IMG_LCL_CONTENT_ASSIST));
		textAction.setDisabledImageDescriptor(DebugPluginImages
				.getImageDescriptor(IDebugUIConstants.IMG_DLCL_CONTENT_ASSIST));
		setAction("ContentAssist", textAction); //$NON-NLS-1$
		getSite().getKeyBindingService().registerAction(textAction);

		textAction = new TextViewerAction(getDetailViewer(),
				ITextOperationTarget.SELECT_ALL);
		textAction.configureAction(
				VariablesViewMessages.VariablesView_Select__All_5, "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		setAction(DETAIL_SELECT_ALL_ACTION, textAction);

		textAction = new TextViewerAction(getDetailViewer(),
				ITextOperationTarget.COPY);
		textAction.configureAction(VariablesViewMessages.VariablesView__Copy_8,
				"", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		setAction(DETAIL_COPY_ACTION, textAction);

		textAction = new TextViewerAction(getDetailViewer(),
				ITextOperationTarget.CUT);
		textAction.configureAction(VariablesViewMessages.VariablesView_Cu_t_11,
				"", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		setAction(ITextEditorActionConstants.CUT, textAction);

		textAction = new TextViewerAction(getDetailViewer(),
				ITextOperationTarget.PASTE);
		textAction.configureAction(
				VariablesViewMessages.VariablesView__Paste_14, "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		setAction(ITextEditorActionConstants.PASTE, textAction);

		// XXX Still using "old" resource access
		ResourceBundle bundle = ResourceBundle
				.getBundle("org.eclipse.debug.internal.ui.views.DebugUIViewsMessages"); //$NON-NLS-1$
		setAction(ITextEditorActionConstants.FIND, new FindReplaceAction(
				bundle, "find_replace_action.", this)); //$NON-NLS-1$

		fSelectionActions.add(ITextEditorActionConstants.COPY);
		fSelectionActions.add(ITextEditorActionConstants.CUT);
		fSelectionActions.add(ITextEditorActionConstants.PASTE);
		updateAction(ITextEditorActionConstants.FIND);

		// set initial content here, as viewer has to be set
		setInitialContent();
	}

	/**
	 * Configures the toolBar.
	 * 
	 * @param tbm
	 *            The toolbar that will be configured
	 */
	protected void configureToolBar(IToolBarManager tbm) {
		tbm.add(new Separator(this.getClass().getName()));
		tbm.add(new Separator(IDebugUIConstants.RENDER_GROUP));
		tbm.add(getAction("ShowTypeNames")); //$NON-NLS-1$
		tbm.add(new Separator("TOGGLE_VIEW")); //$NON-NLS-1$
		IAction action = getAction("ShowDetailPane");
		if (action != null) {
			tbm.add(action); //$NON-NLS-1$
		}
	}

	/**
	 * Adds items to the tree viewer's context menu including any extension
	 * defined actions.
	 * 
	 * @param menu
	 *            The menu to add the item to.
	 */
	protected void fillContextMenu(IMenuManager menu) {
		menu.add(new Separator(IDebugUIConstants.EMPTY_VARIABLE_GROUP));
		menu.add(new Separator(IDebugUIConstants.VARIABLE_GROUP));
		IAction action = getAction("ChangeVariableValue");
		if (action != null)
			menu.add(action); //$NON-NLS-1$
		menu.add(new Separator(IDebugUIConstants.EMPTY_RENDER_GROUP));
		menu.add(new Separator(IDebugUIConstants.EMPTY_NAVIGATION_GROUP));
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	/**
	 * Adds items to the detail area's context menu including any extension
	 * defined actions.
	 * 
	 * @param menu
	 *            The menu to add the item to.
	 */
	protected void fillDetailContextMenu(IMenuManager menu) {

		menu.add(new Separator(IDebugUIConstants.VARIABLE_GROUP));
		menu.add(getAction("ContentAssist")); //$NON-NLS-1$
		menu.add(new Separator());
		menu.add(getAction(ITextEditorActionConstants.CUT));
		menu.add(getAction(ITextEditorActionConstants.COPY + ".Detail")); //$NON-NLS-1$
		menu.add(getAction(ITextEditorActionConstants.PASTE));
		menu.add(getAction(DETAIL_SELECT_ALL_ACTION));
		menu.add(new Separator("FIND")); //$NON-NLS-1$
		menu.add(getAction(ITextEditorActionConstants.FIND));
		menu.add(new Separator("TOGGLE_VIEW")); //$NON-NLS-1$
		menu.add(getAction("ShowDetailPane")); //$NON-NLS-1$
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	/**
	 * Lazily instantiate and return a selection listener that populates the
	 * detail pane, but only if the detail is currently visible.
	 */
	protected ISelectionChangedListener getTreeSelectionChangedListener() {
		if (fTreeSelectionChangedListener == null) {
			fTreeSelectionChangedListener = new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent event) {
					if (event.getSelectionProvider().equals(getWebDataViewer())) {
						clearStatusLine();
						getWebDataViewSelectionProvider().fireSelectionChanged(
								event);
						// if the detail pane is not visible, don't waste time
						// retrieving details
						if (getSashForm().getMaximizedControl() == getViewer()
								.getControl()) {
							return;
						}
						IStructuredSelection selection = (IStructuredSelection) event
								.getSelection();
						populateDetailPaneFromSelection(selection);
						treeSelectionChanged(event);
					}
				}
			};
		}
		return fTreeSelectionChangedListener;
	}

	/**
	 * Selection in the variable tree changed. Perform any updates.
	 * 
	 * @param event
	 */
	protected void treeSelectionChanged(SelectionChangedEvent event) {
	}

	protected void updateSelectionDependentActions() {
		Iterator iterator = fSelectionActions.iterator();
		while (iterator.hasNext()) {
			updateAction((String) iterator.next());
		}
	}

	protected void updateAction(String actionId) {
		IAction action = getAction(actionId);
		if (action instanceof IUpdate) {
			((IUpdate) action).update();
		}
	}

	protected boolean isDetailPaneVisible() {
		IAction action = getAction("ShowDetailPane"); //$NON-NLS-1$
		return action != null && action.isChecked();
	}

	/**
	 * Sets the identifier of the debug model being displayed in this view, or
	 * <code>null</code> if none.
	 * 
	 * @param id
	 *            debug model identifier of the type of debug elements being
	 *            displayed in this view
	 */
	protected void setDebugModel(String id) {
		if (id != fDebugModelIdentifier) {
			fDebugModelIdentifier = id;
			configureDetailsViewer();
		} else {
			updateAction("ContentAssist"); //$NON-NLS-1$
		}
	}

	/**
	 * Returns the identifier of the debug model being displayed in this view,
	 * or <code>null</code> if none.
	 * 
	 * @return debug model identifier
	 */
	protected String getDebugModel() {
		return fDebugModelIdentifier;
	}

	/**
	 * Sets the current configuration being used in the details area.
	 * 
	 * @param config
	 *            source viewer configuration
	 */
	private void setDetailViewerConfiguration(SourceViewerConfiguration config) {
		fSourceViewerConfiguration = config;
	}

	/**
	 * Returns the current configuration being used in the details area.
	 * 
	 * @return source viewer configuration
	 */
	protected SourceViewerConfiguration getDetailViewerConfiguration() {
		return fSourceViewerConfiguration;
	}

	/**
	 * Show the details associated with the first of the selected variables in
	 * the detail pane.
	 */
	protected void populateDetailPaneFromSelection(
			IStructuredSelection selection) {
		try {
			getDetailDocument().set(""); //$NON-NLS-1$
			if (!selection.isEmpty()) {
				IValue val = null;
				Object obj = selection.getFirstElement();

				if (obj instanceof IVariable) {
					val = ((IVariable) obj).getValue();
				} else if (obj instanceof IExpression) {
					val = ((IExpression) obj).getValue();
					if (val == null) {
						return;
					}
				}

				if (val == null)
					return;

				// workaroud for bug 12938
				if (fValueSelection != null
						&& fValueSelection.equals(selection)) {
					return;
				}

				setDebugModel(val.getModelIdentifier());
				fValueSelection = selection;
				fSelectionIterator = selection.iterator();
				fSelectionIterator.next();
				fLastValueDetail = val;
				getModelPresentation().computeDetail(val, this);
			}
		} catch (DebugException de) {
			DebugUIPlugin.log(de);
			// TODO-3.3 WTP 2.0
//			getDetailDocument()
//					.set(
//							VariablesViewMessages.VariablesView__error_occurred_retrieving_value__18); //$NON-NLS-1$
		}
	}

	/**
	 * @see IValueDetailListener#detailComputed(IValue, String)
	 */
	public void detailComputed(final IValue value, final String result) {
		Runnable runnable = new Runnable() {
			public void run() {
				if (isAvailable()) {
					// bug 24862
					// don't display the result if an other detail has been
					// requested
					if (value == fLastValueDetail) {
						String insert = result;
						int length = getDetailDocument().get().length();
						if (length > 0) {
							insert = "\n" + result; //$NON-NLS-1$
						}
						try {
							getDetailDocument().replace(length, 0, insert);
						} catch (BadLocationException e) {
							DebugUIPlugin.log(e);
						}
						fLastValueDetail = null;
					}

					if (fSelectionIterator != null
							&& fSelectionIterator.hasNext()) {
						Object obj = fSelectionIterator.next();
						IValue val = null;
						try {
							if (obj instanceof IVariable) {
								val = ((IVariable) obj).getValue();
							} else if (obj instanceof IExpression) {
								val = ((IExpression) obj).getValue();
							}
							fLastValueDetail = val;
							getModelPresentation().computeDetail(val,
									WebDataView.this);
						} catch (DebugException e) {
							DebugUIPlugin.log(e);
							// TODO-3.3: WTP 2.0
//							getDetailDocument()
//									.set(
//											VariablesViewMessages.VariablesView__error_occurred_retrieving_value__18); //$NON-NLS-1$	
						}
					} else {
						fValueSelection = null;
						fSelectionIterator = null;
					}
				}
			}
		};
		asyncExec(runnable);
	}

	/**
	 * Lazily instantiate and return a selection listener that updates the
	 * enabled state of the selection oriented actions in this view.
	 */
	protected ISelectionChangedListener getDetailSelectionChangedListener() {
		if (fDetailSelectionChangedListener == null) {
			fDetailSelectionChangedListener = new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent event) {
					if (event.getSelectionProvider().equals(
							getWebDataViewSelectionProvider()
									.getUnderlyingSelectionProvider())) {
						getWebDataViewSelectionProvider().fireSelectionChanged(
								event);
						updateSelectionDependentActions();
					}
				}
			};
		}
		return fDetailSelectionChangedListener;
	}

	/**
	 * Lazily instantiate and return a document listener that updates the
	 * enabled state of the 'Find/Replace' action.
	 */
	protected IDocumentListener getDetailDocumentListener() {
		if (fDetailDocumentListener == null) {
			fDetailDocumentListener = new IDocumentListener() {
				public void documentAboutToBeChanged(DocumentEvent event) {
				}

				public void documentChanged(DocumentEvent event) {
					updateAction(ITextEditorActionConstants.FIND);
				}
			};
		}
		return fDetailDocumentListener;
	}

	/**
	 * Lazily instantiate and return a Document for the detail pane text viewer.
	 */
	protected IDocument getDetailDocument() {
		if (fDetailDocument == null) {
			fDetailDocument = new Document();
		}
		return fDetailDocument;
	}

	/**
	 * @see AbstractDebugView#getDefaultControl()
	 */
	protected Control getDefaultControl() {
		return getSashForm();
	}

	protected SashForm getSashForm() {
		return fSashForm;
	}

	/**
	 * @see WorkbenchPart#getAdapter(Class)
	 */
	public Object getAdapter(Class required) {
		if (IFindReplaceTarget.class.equals(required)) {
			return getDetailViewer().getFindReplaceTarget();
		}
		if (ITextViewer.class.equals(required)) {
			return getDetailViewer();
		}
		if (IDebugModelPresentation.class.equals(required)) {
			IBaseLabelProvider labelProvider = getStructuredViewer()
					.getLabelProvider();
			if (labelProvider instanceof WebDataViewLabelProvider) {
				return ((WebDataViewLabelProvider) labelProvider)
						.getPresentation();
			}
		}
		return super.getAdapter(required);
	}

	/**
	 * @see IDebugExceptionHandler#handleException(DebugException)
	 */
	public void handleException(DebugException e) {
		showMessage(e.getMessage());
	}

	/**
	 * The <code>VariablesView</code> listens for selection changes in the
	 * <code>LaunchView</code>
	 * 
	 * @see ISelectionListener#selectionChanged(IWorkbenchPart, ISelection)
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (!isAvailable()) {
			return;
		}

		if (selection == null) {
			setViewerInput(new StructuredSelection());
		} else if (selection instanceof IStructuredSelection) {
			setViewerInput((IStructuredSelection) selection);
		} else {
			getDetailViewer().setEditable(false);
		}
		updateAction("ContentAssist"); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
		if (getFocusViewer() == null) {
			super.setFocus();
		} else {
			getFocusViewer().getControl().setFocus();
		}
	}

	/**
	 * Sets the viewer that has focus.
	 * 
	 * @param viewer
	 */
	protected void setFocusViewer(Viewer viewer) {
		fFocusViewer = viewer;
	}

	/**
	 * Returns the viewer that has focus, or <code>null</code>.
	 * 
	 * @return Viewer
	 */
	protected Viewer getFocusViewer() {
		return fFocusViewer;
	}

}
