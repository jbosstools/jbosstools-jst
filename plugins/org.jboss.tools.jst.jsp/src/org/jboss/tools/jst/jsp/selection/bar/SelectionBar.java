/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.selection.bar;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.compare.Splitter;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.CommandEvent;
import org.eclipse.core.commands.ICommandListener;
import org.eclipse.core.commands.State;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.preferences.IVpePreferencesPage;
import org.jboss.tools.jst.jsp.selection.SelectionHelper;
import org.jboss.tools.jst.jsp.selection.SourceSelection;
import org.jboss.tools.jst.jsp.selection.SourceSelectionBuilder;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class create and manage the Selection Bar under the VPE.
 * Entry point from the class MozilaEditor 
 * This bar can be hidden or  shown.
 *
 * @author erick
 * @author yradtsevich
 * @author mareshkau
 */
public class SelectionBar extends Composite {
	private static final int SEL_ITEM_RIGHT_MARGIN = 5;
	/*
	 * https://issues.jboss.org/browse/JBIDE-12948
	 * Selection bar visibility flag
	 */
	private boolean isBarVisible = false;
	/*
	 * The main composite that holds all other controls
	 */
	private Splitter splitter;
	/*
	 * The SWT ToolBar that contains items with tag names
	 */
	private ToolBar toolbar = null;
	/*
	 * Composite that contains visible toolbar
	 */
	private Composite realBar = null;
	/*
	 * Composite to represent invisible toolbar
	 */
	private Composite emptyBar = null;

	private boolean resizeListenerAdded = false;
    private FormData toolbarData;
    private Menu dropDownMenu = null;
    private int itemCount = 0;
    private StructuredTextEditor textEditor;
    private Node currentSelectedNode = null;
    private Node currentLastNode = null;
    /*
     * Selection Bar State
     */
    private State toggleSelBarState;
    private Command toggleSelBarCommand;
    private ICommandListener toggleSelBarCommandListener;
    private ISelectionChangedListener selectionChangedListener;
    private ImageButton arrowButton;
    
	public SelectionBar(StructuredTextEditor textEditor, 
			Composite parent, int style) {
		super(parent, style);
		this.textEditor = textEditor;
		
		if (this.textEditor.getTextViewer() != null) {
			selectionChangedListener = new ISelectionChangedListener() {
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					runUpdateJob(true);
				}
			};
			this.textEditor.getTextViewer().addSelectionChangedListener(selectionChangedListener);
		}
		
		ICommandService commandService = (ICommandService) PlatformUI
				.getWorkbench().getService(ICommandService.class);
		this.toggleSelBarCommand = commandService.getCommand(SelectionBarHandler.COMMAND_ID);
		toggleSelBarState = toggleSelBarCommand.getState("org.eclipse.ui.commands.toggleState"); //$NON-NLS-1$
		toggleSelBarCommandListener = new ICommandListener() {
			@Override
			public void commandChanged(CommandEvent commandEvent) {
				refreshVisibility();
			}
		};
		toggleSelBarCommand.addCommandListener(toggleSelBarCommandListener);
		
		this.setLayout(new FillLayout());
		/*
		 * Create the Selection Bar Composite in its constructor
		 */
		createToolBarComposite();
		/*
		 * https://issues.jboss.org/browse/JBIDE-12948
		 * Get the visibility value, store the initial state.
		 */
		isBarVisible = JspEditorPlugin.getDefault().getPreferenceStore().
				getBoolean(IVpePreferencesPage.SHOW_SELECTION_TAG_BAR);
		/*
		 * Init the size for the first time.
		 */
		showOneOfRealOrEmptyBar(isBarVisible);
	}
	
	public Composite createToolBarComposite() {
		/*
		 * The parent of all children should be 'this' composite
		 */
		splitter = new Splitter(this, SWT.NONE);
		/*
		 * The invisible Selection Bar that is used to handle
		 * show/hide Selection Bar actions.
		 */
		emptyBar = new Composite(splitter, SWT.NONE) {
			@Override
			public Point computeSize(int wHint, int hHint, boolean changed) {
				Point point = super.computeSize(wHint, hHint, changed);
				point.y = 1;
				return point;
			}
		};
		emptyBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		/*
		 * Visible Composite that contains all other Selection Bar controls 
		 */
		realBar = new Composite(splitter, SWT.NONE);
		realBar.setLayout(new FormLayout());

		final Image closeImage = PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_TOOL_DELETE);

		final Listener closeListener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				/*
				 * Hide the selection bar
				 */
				setVisible(false);
				toggleSelBarState.setValue(false);
				JspEditorPlugin.getDefault().getPreferenceStore().
					setValue(IVpePreferencesPage.SHOW_SELECTION_TAG_BAR, false);
			}
		};

		ImageButton closeButton = new ImageButton(realBar, closeImage,
				JstUIMessages.HIDE_SELECTION_BAR);
		closeButton.addSelectionListener(closeListener);
		FormData closeBarData = new FormData();
		closeBarData.right = new FormAttachment(100);
		closeBarData.top = new FormAttachment(0);
		Composite closeItemComposite = closeButton.getComposite();
		closeItemComposite.setLayoutData(closeBarData);

		/*
		 *  Create tool bar that will contain nodes from the source editor.
		 *  
		 *  https://issues.jboss.org/browse/JBIDE-12435 --
		 *   Do not use SWT.NO_BACKGROUND style, it leads to a black color.
		 */
		toolbar = new ToolBar(realBar, SWT.HORIZONTAL | SWT.FLAT);
		toolbarData = new FormData();
		toolbarData.left = new FormAttachment(0);
		toolbarData.right = new FormAttachment(closeItemComposite, 0, SWT.LEFT);
		toolbarData.top = new FormAttachment(0);
		toolbar.setLayoutData(toolbarData);
		createArrowButton();

		return splitter;
	}

	/**
	 * Sets {@code visible} state to this {@code SelectionBar} and fires
	 * all registered {@code VisibilityListener}s.
	 */
	@Override
	public void setVisible(boolean visible) {
		if (visible != isBarVisible) {
			isBarVisible = visible;
			showOneOfRealOrEmptyBar(visible);
		}
	}

	private void showOneOfRealOrEmptyBar(boolean realBarIsVisible) {
		if (realBarIsVisible) {
			splitter.setVisible(realBar, true);
			splitter.setVisible(emptyBar, false);
		} else {
			splitter.setVisible(realBar, false);
			splitter.setVisible(emptyBar, true);
		}
		/* JBIDE-7387:
		 * By default toolbar size is set to fit regular button ToolItems.
		 * But drop-down items are a little bigger and do not
		 * fit in the default-size toolbars (at least under Windows XP).
		 * This temporary ToolItem is needed to set enough size to the toolbar.*/
		ToolItem tempItem = new ToolItem(toolbar, SWT.DROP_DOWN);
		tempItem.setText(" "); //$NON-NLS-1$
		tempItem.setEnabled(false);
		this.getParent().layout(true, true);
		tempItem.dispose();
	}
	
	public void refreshVisibility() {
		boolean visible = (Boolean) toggleSelBarState.getValue();
		IEditorPart editorPart = textEditor.getEditorPart();
		if(editorPart instanceof JSPMultiPageEditor){
			JSPMultiPageEditor jspEditor = (JSPMultiPageEditor) editorPart;
			if(jspEditor.getSelectedPageIndex() == jspEditor.getPreviewIndex()){
				visible = false;
			}
		}
		setVisible(visible);
	}


	/**
	 * Updates buttons in the selection bar and the drop-down menu
	 * according to the source selection.
	 */
    public void updateNodes(boolean forceUpdate) {
		SourceSelectionBuilder sourceSelectionBuilder = 
				new SourceSelectionBuilder(textEditor);
		SourceSelection selection = sourceSelectionBuilder.getSelection();
		if (selection == null) {
			return;
		}

		// Node node = selection.getFocusNode();
		Node node = selection.getStartNode();
		/*
		 * https://issues.jboss.org/browse/JBIDE-7692
		 * Display selected attribute names and 
		 * text nodes on the VPE selection bar
		 */
		Attr attr = selection.getFocusAttribute();
		if (attr != null) {
			node = attr;
		}
//		if (node != null && node.getNodeType() == Node.TEXT_NODE) {
//			node = node.getParentNode();
//		}

		if (currentSelectedNode == node && !forceUpdate) {
			return;
		}

	    final boolean ancestorSelected = isAncestor(node, currentLastNode);
	    if (ancestorSelected) {
	    	if (forceUpdate) {
	    		// reinitialize selBar with currentLastNode
	    		setSelBarItems(currentLastNode);
	    	} else {
	    		// deselect currentSelectedNode
	    		setNodeSelected(currentSelectedNode, false);
	    	}
	    } else {
	    	setSelBarItems(node);
	    	currentLastNode = node;
	    }
	    
		setNodeSelected(node, true);
		currentSelectedNode = node;
	}

    /**
	 * Sets the selection state of the given node in the selection bar.
	 */
	private void setNodeSelected(Node node, boolean selected) {
		for (ToolItem item : toolbar.getItems()) {
			if (item.getData() == node) {
				item.setSelection(selected);
				return;
			}
		}

		if (dropDownMenu == null) {
			return;
		}

		for (MenuItem item : dropDownMenu.getItems()) {
			if (item.getData() == node) {
				item.setSelection(selected);
				return;
			}
		}
	}
	
	/**
     * Cleans {@link #toolbar} and adds to it buttons which
     * appropriate the {@code node} and all its ancestors.
	 */
	private void setSelBarItems(Node node) {
		removeNodeListenerFromAllNodes();
		cleanToolBar(toolbar);

		disposeDropDownMenu();
		// for now dropDownMenu = null

		int elementCounter = 0;
		boolean createDDLofAttributes = false;
		while (node != null
				&& (node.getNodeType() == Node.ELEMENT_NODE
						|| node.getNodeType() == Node.COMMENT_NODE
						|| node.getNodeType() == Node.ATTRIBUTE_NODE
						|| (node.getNodeType() == Node.TEXT_NODE 
									&& node.getNodeValue().trim().length() > 0))) {
			
			addNodeListenerTo(node);
			/*
			 * If there is no DDM -- item will be added to line
			 */
			if (dropDownMenu == null) {
				ToolItem item;
				NodeList children = node.getChildNodes();
				List<Node> list = new ArrayList<Node>();
				for (int i = 0; i < children.getLength(); i++) {
					Node child = children.item(i);
					if ((child.getNodeType() == Node.ELEMENT_NODE) 
							|| (child.getNodeType() == Node.TEXT_NODE
									&& child.getNodeValue().trim().length() > 0)) {
						list.add(child);
					}
				}
				/*
				 * for the last tag -- show check button
				 */
				if ((elementCounter == 0 && list.size() == 0) || createDDLofAttributes) {
					/*
					 * For the last element in Selection Bar add its attributes
					 * to the DropDownMenu for this toolitem.
					 */
					NamedNodeMap attrsMap = node.getAttributes();
					if (attrsMap != null && attrsMap.getLength() > 0) {
						list.clear();
						for (int i = 0; i < attrsMap.getLength(); i++) {
							list.add(attrsMap.item(i));
						}
						item = new ToolItem(toolbar, SWT.DROP_DOWN, 1);
						final DropdownSelectionListener dropdownListener = 
								new DropdownSelectionListener(item, list);
						item.addSelectionListener(dropdownListener);
						/*
						 * Dispose the menu manually when the item is disposed.
						 * Thus unnecessary memory will be released. 
						 */
						item.addDisposeListener(new DisposeListener() {
							@Override
							public void widgetDisposed(DisposeEvent e) {
								dropdownListener.disposeMenu();
							}
						});
					} else {
						/*
						 * If there is no attributes for the node -- leave it as flat button
						 */
						item = new ToolItem(toolbar, SWT.FLAT | SWT.CHECK, 1);
						item.addSelectionListener(new SelectionListener() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								handleSelectionEvent(e);
							}
							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
								handleSelectionEvent(e);
							}
						});
					}
				} else {
					/*
					 * Create DropDownMenu button
					 */
					item = new ToolItem(toolbar, SWT.DROP_DOWN, 1);
					final DropdownSelectionListener dropdownListener = 
							new DropdownSelectionListener(item, list);
					item.addSelectionListener(dropdownListener);
					/*
					 * Dispose the menu manually when the item is disposed.
					 * Thus unnecessary memory will be released. 
					 */
					item.addDisposeListener(new DisposeListener() {
						@Override
						public void widgetDisposed(DisposeEvent e) {
							dropdownListener.disposeMenu();
						}
					});
				}
				/*
				 * Set the text for the element in Selection Bar
				 */
				item.setData(node);
				item.setText(node.getNodeName());
				/*
				 * When the item does not fit to the bar --
				 * put it to the DDM
				 */
				if (!isItemShown(toolbar.getItem(elementCounter + 1))) {
					item.dispose();
					dropDownMenu = new Menu(toolbar);
				}
			} 
			
			/*
			 * After the DDM has been created
			 * all other items will be added to the DDM
			 * as they do not fit to the selection bar any more. 
			 */
			if (dropDownMenu != null)  {
				MenuItem menuItem = new MenuItem(dropDownMenu, SWT.CHECK, 0);
				menuItem.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						handleSelectionEvent(e);
					}
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						handleSelectionEvent(e);
					}
				});
				menuItem.setText(node.getNodeName());
				menuItem.setData(node);
			}
			/*
			 * Count the elements
			 */
			elementCounter++;
			/*
			 * Get the parent to put it to the bar
			 */
			createDDLofAttributes = false;
			if (node instanceof Attr) {
				node = ((Attr) node).getOwnerElement();
				/*
				 * Create the list of attributes for the parent
				 * during the next iteration.
				 */
				createDDLofAttributes = true;
			} else {
				node = node.getParentNode();
			}
		}
		itemCount = elementCounter;
		arrowButton.setEnabled(dropDownMenu != null);

		if (node != null && node.getNodeType() == Node.DOCUMENT_NODE) {
			addNodeListenerTo(node);
		}

		if (!resizeListenerAdded ) {
			realBar.addListener(SWT.Resize, new Listener() {
				@Override
				public void handleEvent(Event event) {
					runUpdateJob(true);
				}
			});
			resizeListenerAdded = true;
		}
	}

	/**
	 * Checks if the {@code potentialAncestor} is an ancestor of
	 * {@code potentialAncestor node}.
	 */
	private boolean isAncestor(Node potentialAncestor, Node node) {
		if (potentialAncestor == null || node == null) {
			return false;
		}

		Node curAncestor = node;
		while ((curAncestor = curAncestor.getParentNode()) != null) {
			if (potentialAncestor == curAncestor) {
				return true;
			}
		}

		return false;
	}

	/**
     * Deletes all items (except the first item-arrow button)
     * from the given {@code #toolBar}.
     */
	private void cleanToolBar(ToolBar toolBar) {
		ToolItem[] oldItems = toolBar.getItems();
		for (int i = 1; i < oldItems.length; i++) {
			oldItems[i].dispose();
		}
	}

	/**
	 * Initializes {@link #arrowButton}.
	 */
    private void createArrowButton() {
		final Image hoverImage = WorkbenchImages.getImage(
				IWorkbenchGraphicConstants.IMG_LCL_RENDERED_VIEW_MENU);

		arrowButton = new ImageButton(toolbar, hoverImage,
				JstUIMessages.SelectionBar_MoreNodes);
		arrowButton.setEnabled(false);
		arrowButton.addSelectionListener(
				new Listener() {
					@Override
					public void handleEvent(Event event) {
						Rectangle bounds = arrowButton.getButtonBounds();
						Point point = toolbar.toDisplay(bounds.x, bounds.y
								+ bounds.height);
						dropDownMenu.setLocation(point);
						dropDownMenu.setVisible(true);
					}
				});

		ToolItem arrowItem = new ToolItem(toolbar, SWT.SEPARATOR, 0);
		Composite arrowButtonComposite = arrowButton.getComposite();
		arrowItem.setControl(arrowButtonComposite);
		arrowButtonComposite.pack();
		arrowItem.setWidth(arrowButtonComposite.getSize().x);

		FormData arrowToolBarData = new FormData();
		arrowToolBarData.left = new FormAttachment(0);
		arrowToolBarData.top = new FormAttachment(0);
		arrowButtonComposite.setLayoutData(arrowToolBarData);
	}

    /**
     * Checks if the given {@code toolItem} is fully shown on the screen.
     */
	private boolean isItemShown(ToolItem toolItem) {
		ToolBar toolBar = toolItem.getParent();
		Rectangle toolItemBounds = toolItem.getBounds();

		toolItemBounds.width += SEL_ITEM_RIGHT_MARGIN;
		Rectangle intersection = toolBar.getBounds().intersection(
				toolItemBounds);
		return intersection.equals(toolItemBounds);
	}

	/**
     * List of nodes that are notifying {@link #nodeListener} when they are
     * changed.
     */
    private List<INodeNotifier> nodeNotifiers = new ArrayList<INodeNotifier>();
    /**
     * Listener for all {@link #nodeNotifiers}
     */
    private NodeListener nodeListener = new NodeListener(this);

    /**
     * Adds {@link #nodeListener} to the given {@code node}.
     *
     * @param node the node to that the listener must be added.
     */
	private void addNodeListenerTo(Node node) {
		if (node instanceof INodeNotifier) {
			INodeNotifier notifier = (INodeNotifier) node;
			
			/* yradtsevich:
			 * checking for notifier.getExistingAdapter(this) == null
	 		 * is not used because of JBIDE-8049 (JBIDE-8051)
	 		 * (PDT implementation of getExistingAdapter(Object type) works
	 		 * incorrectly with non Class parameters) */
			if (!nodeNotifiers.contains(notifier)) {
				notifier.addAdapter(nodeListener);
				nodeNotifiers.add(notifier);
			}
		}
	}
	/**
	 * Removes {@link #nodeListener} from all nodes associated with this {@code SelectionBar}.
	 */
	private void removeNodeListenerFromAllNodes() {
		for (INodeNotifier notifier : nodeNotifiers) {
			notifier.removeAdapter(nodeListener);
		}
		nodeNotifiers.clear();
	}

    @Override
	public void dispose() {
    	removeNodeListenerFromAllNodes();
    	toggleSelBarCommand.removeCommandListener(toggleSelBarCommandListener);
    	if (textEditor.getTextViewer() != null) {
			textEditor.getTextViewer().removeSelectionChangedListener(selectionChangedListener);
		}

		if (splitter != null) {
			splitter.dispose();
			splitter = null;
		}
    	disposeDropDownMenu();
    	super.dispose();
	}

    /**
     * Disposes {@link #dropDownMenu}.
     */
	private void disposeDropDownMenu() {
		if (dropDownMenu != null) {
			dropDownMenu.dispose();
			dropDownMenu = null;
		}
	}

    public void handleSelectionEvent(SelectionEvent e) {
    	Widget widget = e.widget;
    	
    	/* ensure that the ToolItem or MenuItem is selected
    	 * (for repeated clicks on the same widget)*/
    	if (widget instanceof ToolItem) {
    		((ToolItem)widget).setSelection(true);
    	} else if (widget instanceof MenuItem) {
    		((MenuItem)widget).setSelection(true);
    	}

    	SelectionHelper.setSourceSelection(textEditor,
				(Node) widget.getData());
	}

	public void widgetDefaultSelected(SelectionEvent e) {
	}

    @Override
	public String toString() {
		StringBuffer st = new StringBuffer("CountItem: "); //$NON-NLS-1$
		st.append(itemCount);
		st.append(" Parent Composite: " + realBar.getBounds().width); //$NON-NLS-1$
		st.append(" Bar : " + toolbar.getBounds().width); //$NON-NLS-1$
		return st.toString();
	}
    
    /**
     ********************************************************************************************* 
     * This class provides the "drop down" functionality for the selection bar.
     *********************************************************************************************/ 
    class DropdownSelectionListener extends SelectionAdapter {
		private ToolItem dropdown;
		private Menu menu;
		private List<Node> children;
		private boolean shown = false;
		/**
		 * Constructs a DropdownSelectionListener
		 * 
		 * @param dropdown
		 *            the tool item this listener belongs to
		 */
		public DropdownSelectionListener(ToolItem dropdown, List<Node> children) {
			this.dropdown = dropdown;
			this.children = children;
		}
		
		public void disposeMenu() {
			if (menu != null) {
				menu.dispose();
				menu = null;
			}
		}

		/**
		 * Adds an item to the dropdown list
		 * 
		 * @param item
		 *            the item to add
		 */
		public void add(Node node) {
			MenuItem menuItem = new MenuItem(menu, SWT.NONE);
			menuItem.setText(node.getNodeName());
			menuItem.setData(node);
			menuItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					handleSelectionEvent(event);
				}
			});
		}

		/**
		 * Called when either the button itself or the dropdown arrow is clicked
		 * 
		 * @param event
		 *            the event that trigged this call
		 */
		@Override
		public void widgetSelected(SelectionEvent event) {
			/*
			 * If they clicked the arrow, we show the list or close it
			 */
			if (event.detail == SWT.ARROW) {
				if (shown) {
					if (menu != null) {
						menu.setVisible(false);
					}
					shown = false;
				} else {
					/*
					 * Create menu when it is accessed for the first time.
					 */
					if (menu == null) {
						menu = new Menu(dropdown.getParent());
						if (children != null && children.size() > 0) {
							for (Node node : children) {
								add(node);
							}
						}
					}
					menu.addMenuListener(new MenuListener() {
						@Override
						public void menuShown(MenuEvent e) {
							/*
							 * Do nothing
							 */
						}
						@Override
						public void menuHidden(MenuEvent e) {
							/*
							 * Change the 'shown' state
							 */
							shown = false;
						}
					});
					/*
					 * Determine where to put the dropdown list and show it
					 */
					ToolItem item = (ToolItem) event.widget;
					Rectangle rect = item.getBounds();
					Point pt = item.getParent().toDisplay(
							new Point(rect.x, rect.y));
					menu.setLocation(pt.x, pt.y + rect.height);
					menu.setVisible(true);
					shown = true;
				}
			} else {
				/*
				 * User pushed the button; take appropriate action
				 */
				handleSelectionEvent(event);
			}
		}
	}


    SelectionBarUpdateJob updateJob = null;

    public void runUpdateJob(boolean forceUpdate) {
    	if(updateJob == null) {
    		updateJob = new SelectionBarUpdateJob(this);
    		if(forceUpdate) {
    			updateJob.forceUpdate = true;
    		}
    		updateJob.schedule(100);
    	} else if(forceUpdate) {
    		synchronized(this) {
    			if(updateJob != null) {
    				updateJob.forceUpdate = true;
    			}
    		}
    	}
    }
}

/**
 * Instances of this class represent a flat button with image.
 * 
 * @author yradtsevich
 */
class ImageButton {
	private ToolItem item;
	private Composite composite;
	private Image emptyImage;
	private Image image;

	public ImageButton(Composite parent, Image image, String toolTip) {
		composite = new Composite(parent, SWT.NONE);
		composite.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				release();
			}
		});
		GridLayout layoutTl = new GridLayout(1, false);
		layoutTl.marginBottom = 0;
		layoutTl.marginHeight = 0;
		layoutTl.marginWidth = 0;
		layoutTl.verticalSpacing = 0;
		layoutTl.horizontalSpacing = 0;
		composite.setLayout(layoutTl);

		this.image = image;
		ToolBar toolBar = new ToolBar(composite, SWT.HORIZONTAL | SWT.FLAT);
		item = new ToolItem(toolBar, SWT.FLAT);
		item.setImage(image);
		emptyImage = new Image(Display.getCurrent(), 1, 1);
		Color emptyImageColor = toolBar.getBackground();
		emptyImage.setBackground(emptyImageColor);
		GC gc = new GC(emptyImage);
		gc.setForeground(emptyImageColor);
        gc.drawPoint(0, 0);
        gc.dispose();
		item.setDisabledImage(emptyImage);
		item.setToolTipText(toolTip);
	}

	public void addSelectionListener (Listener listener) {
		item.addListener(SWT.Selection, listener);
	}

	/**
	 * Releases resources.
	 */
	protected void release() {
		if (emptyImage != null) {
			emptyImage.dispose();
			emptyImage = null;
		}
	}

	public void dispose () {
		if (composite != null) {
			composite.dispose();
			composite = null;
			item = null;
			image = null;
		}
	}
	public void setEnabled (boolean enabled) {
		item.setEnabled(enabled);

		// fix for JBIDE-5588
		if (enabled) {
			item.setImage(image);
		} else {
			item.setImage(emptyImage);
		}
	}

	public Rectangle getButtonBounds() {
		return item.getBounds();
	}


	public Composite getComposite() {
		return composite;
	}
}

/**
 * Listener for nodes that are implementing {@link INodeAdapter}.
 * Calls {@link SelectionBar#updateNodes()} every time when these nodes are changed.
 * <P>
 * This class is a part of fix of JBIDE-3919:
 * Incorrect interaction of block comments with selection bar.
 * </P>
 * @author yradtsevich
 */
class NodeListener implements INodeAdapter {
	private SelectionBar selectionBar;

	public NodeListener(SelectionBar selectionBar) {
		this.selectionBar = selectionBar;
	}

	/* (non-javadoc)
     * The infrastructure calls this method to determine if the adapter is
     * appropriate for 'type'. Typically, adapters return true based on
     * identity comparison to 'type', but this is not required, that is, the
     * decision can be based on complex logic.
     */
    @Override
	public boolean isAdapterForType(Object type) {
    	return selectionBar == type;
    }

    /* (non-javadoc)
     * Sent to adapter when notifier changes. Each notifier is responsible for
     * defining specific eventTypes, feature changed, etc.
     *
     * ISSUE: may be more evolvable if the argument was one big 'notifier
     * event' instance.
     */
    @Override
	public void notifyChanged(INodeNotifier notifier, int eventType, 
			Object changedFeature, Object oldValue, Object newValue, int pos) {
    	/*
    	 * https://issues.jboss.org/browse/JBIDE-10881
    	 * Fixing selection bar listeners calls.
    	 */
    	selectionBar.runUpdateJob(false);
    }
    
}

class SelectionBarUpdateJob extends UIJob {
	private SelectionBar selectionBar;
	boolean forceUpdate;

	public SelectionBarUpdateJob(SelectionBar selectionBar) {
		super("Selection Bar update job"); //$NON-NLS-1$
		this.selectionBar = selectionBar;
	}
	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		try {
			selectionBar.updateNodes(forceUpdate);
		} finally {
			synchronized(selectionBar) {
				selectionBar.updateJob = null;
			}
		}
		return Status.OK_STATUS;
	}
}