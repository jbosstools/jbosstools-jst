/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.internal.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class DefaultPropertySetViewer extends AbstractPropertySetViewer {
	private static String[] columnLabels = {"Name", "Value"};

	interface ICellEditorActivationListener {
	    /**
	     * Notifies that the cell editor has been activated
	     *
	     * @param cellEditor the cell editor which has been activated
	     */
	    public void cellEditorActivated(CellEditor cellEditor);

	    /**
	     * Notifies that the cell editor has been deactivated
	     *
	     * @param cellEditor the cell editor which has been deactivated
	     */
	    public void cellEditorDeactivated(CellEditor cellEditor);
	}

	Table table = null;
	private int columnToEdit = 1;
	TableEditor tableEditor = null;
	private HashMap<Object, TableItem> entryToItemMap = new HashMap<Object, TableItem>();
	private ICellEditorListener editorListener;
	private ListenerList activationListeners = new ListenerList();

	CellEditor cellEditor;

	public DefaultPropertySetViewer() {
	}

	@Override
	public Composite createControl(Composite parent) {
		table = new Table(parent, SWT.FULL_SELECTION | SWT.SINGLE
                | SWT.HIDE_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		addColumns();
		
		hookControl();
		
		tableEditor = new TableEditor(table);

        createEditorListener();
		
		return table;
	}

	@Override
	public Control getControl() {
		return table;
	}

	@Override
	public void dispose() {
		super.dispose();
		table.dispose();
		tableEditor.dispose();
	}

	private void addColumns() {
		TableColumn[] columns = table.getColumns();
		for (int i = 0; i < columnLabels.length; i++) {
			String string = columnLabels[i];
			if (string != null) {
				TableColumn column;
				if (i < columns.length) {
					column = columns[i];
				} else {
					column = new TableColumn(table, 0);
				}
				column.setText(string);
			}
		}

		table.addControlListener(new ControlAdapter() {
            public void controlResized(ControlEvent e) {
                Rectangle area = table.getClientArea();
                TableColumn[] columns = table.getColumns();
                if (area.width > 0) {
                    columns[0].setWidth(area.width * 40 / 100);
                    columns[1].setWidth(area.width - columns[0].getWidth() - 4);
                    table.removeControlListener(this);
                }
            }
        });

    }

	@Override
	public void refresh(List<IPropertyDescriptor> descriptors) {
		descriptors = getFilteredDescriptors(descriptors);
		int c = table.getItemCount();
		for (int i = 0; i < c && i < descriptors.size(); i++) {
			updateEntry(descriptors.get(i), table.getItem(i));
		}
		if(table.getItemCount() > descriptors.size()) {
			for (int i = c - 1; i >= descriptors.size(); i--) {
				removeItem(table.getItem(i));
			}
		} else if(table.getItemCount() < descriptors.size()) {
			for (int i = c; i < descriptors.size(); i++) {
				createItem(descriptors.get(i), i);
			}
		}
	}

	public List<IPropertyDescriptor> getFilteredDescriptors(List<IPropertyDescriptor> descriptors) {
		if(category == null || category.equals(FormPropertySheetViewer.ALL_CATEGORY)) {
			return descriptors;
		}
		List<IPropertyDescriptor> result = new ArrayList<IPropertyDescriptor>();
		for (IPropertyDescriptor d: descriptors) {
			if(category.equals(d.getCategory())) {
				result.add(d);
			}
		}
		return result;
	}

    private void updateEntry(IPropertyDescriptor descriptor, TableItem item) {
        // ensure that backpointer is correct
        item.setData(descriptor);
        
        // update the map accordingly
        entryToItemMap.put(descriptor.getId(), item);

        // update the name and value columns
        item.setText(0, descriptor.getDisplayName());
        item.setText(1, model.getValueAsString(descriptor));
//        Image image = entry.getImage();
//        if (item.getImage(1) != image) {
//			item.setImage(1, image);
//		}
    }

    private void createItem(IPropertyDescriptor descriptor, int index) {
        // create the item
        TableItem item = new TableItem(table, SWT.NONE, index);
        item.setData(descriptor);
        entryToItemMap.put(descriptor.getId(), item);
        
        // Always ensure that if the tree item goes away that it's
        // removed from the cache
        item.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				Object possibleEntry = e.widget.getData();
				if (possibleEntry != null)
					entryToItemMap.remove(possibleEntry);
			}
        });        

       updateEntry(descriptor, item);
    }

    private void removeItem(TableItem item) {
        IPropertyDescriptor data = (IPropertyDescriptor)item.getData();
        item.setData(null);
        entryToItemMap.remove(data.getId());
        item.dispose();
    }

    @Override
    protected void createEntryListener() {
    	entryListener = new IPropertySheetModelListener() {
			
			@Override
			public void valueChanged(IPropertyDescriptor descriptor) {
				TableItem item = findItem(descriptor);
				if(item != null) {
					updateEntry(descriptor, item);
				}				
			}
			
			@Override
			public void descriptorsChanged() {
				if(model != null) {
					refresh(model.getPropertyDescriptors());
				}
			}
		};
    }

    private void createEditorListener() {
        editorListener = new ICellEditorListener() {
            public void cancelEditor() {
                deactivateCellEditor();
            }

            public void editorValueChanged(boolean oldValidState,
                    boolean newValidState) {
                //Do nothing
            }

            public void applyEditorValue() {
                //Do nothing
            }
        };
    }

    private TableItem findItem(IPropertyDescriptor descriptor) {
    	TableItem result = entryToItemMap.get(descriptor.getId());
    	if(result != null) {
    		return result;
    	}
        // Iterate through treeItems to find item
        TableItem[] items = table.getItems();
        for (int i = 0; i < items.length; i++) {
        	TableItem item = items[i];
        	if(item.getData() == descriptor) {
        		return item;
        	}
        }
        return null;
    }

    public CellEditor getActiveCellEditor() {
        return cellEditor;
    }

    private void hookControl() {
        // Handle selections in the Tree
        // Part1: Double click only (allow traversal via keyboard without
        // activation
        table.addSelectionListener(new SelectionAdapter() {
            /* (non-Javadoc)
             * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            public void widgetSelected(SelectionEvent e) {
            	// The viewer only owns the status line when there is
            	// no 'active' cell editor
            	if (getActiveCellEditor() == null || !getActiveCellEditor().isActivated()) {
            		if (e.item instanceof TableItem) {
            			TableItem item = (TableItem)e.item;
            	        IPropertyDescriptor descriptor = (IPropertyDescriptor) item.getData();
            	        editedDescriptor.setDescriptor(descriptor);
            		}
//					updateStatusLine(e.item);
				}
			}

			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetDefaultSelected(SelectionEvent e) {
				if (e.item instanceof TableItem)
					handleSelect((TableItem) e.item);
            }
        });
        // Part2: handle single click activation of cell editor
        table.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent event) {
                // only activate if there is a cell editor
                Point pt = new Point(event.x, event.y);
                TableItem item = table.getItem(pt);
                if (item != null) {
                    handleSelect(item);
                }
            }
        });

        // Refresh the tree when F5 pressed
        table.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.character == SWT.ESC) {
					deactivateCellEditor();
				} else if (e.keyCode == SWT.F5) {
					// The following will simulate a reselect
//                    setInput(getInput());
				}
            }
        });
    }

    private void handleSelect(TableItem selection) {
        // deactivate the current cell editor
        if (getActiveCellEditor() != null) {
            applyEditorValue();
            deactivateCellEditor();
        }

        if (selection == null) {
//            setMessage(null);
//            setErrorMessage(null);
        } else {
            Object object = selection.getData();
            if (object instanceof IPropertyDescriptor) {
                // get the entry for this item
//            	IPropertyDescriptor activeEntry = (IPropertyDescriptor) object;

                // display the description for the item
//                setMessage(activeEntry.getDescription());

                // activate a cell editor on the selection
                activateCellEditor(selection);
            }
        }
        entrySelectionChanged();
    }

    private void activateCellEditor(TableItem item) {
        // ensure the cell editor is visible
        table.showSelection();

        // Get the entry for this item
        IPropertyDescriptor descriptor = (IPropertyDescriptor) item.getData();

        // Get the cell editor for the entry.
        // Note that the editor parent must be the Tree control
        editedDescriptor.setDescriptor(descriptor);
        cellEditor = getCellEditor(table);

        if (cellEditor == null) {
			// unable to create the editor
            return;
		}

        // activate the cell editor
        cellEditor.activate();

        // if the cell editor has no control we can stop now
        Control control = cellEditor.getControl();
        if (control == null) {
            cellEditor.deactivate();
            stopEditing();
            return;
        }

        // add our editor listener
        cellEditor.addListener(editorListener);

        // set the layout of the tree editor to match the cell editor
        CellEditor.LayoutData layout = cellEditor.getLayoutData();
        tableEditor.horizontalAlignment = layout.horizontalAlignment;
        tableEditor.grabHorizontal = layout.grabHorizontal;
        tableEditor.minimumWidth = layout.minimumWidth;
        tableEditor.setEditor(control, item, columnToEdit);

        // set the error text from the cel editor
//        setErrorMessage(cellEditor.getErrorMessage());

        // give focus to the cell editor
        cellEditor.setFocus();

        // notify of activation
        fireCellEditorActivated(cellEditor);
    }

    @Override
    public void stopEditing() {
    	deactivateCellEditor();
    }

    public void deactivateCellEditor() {
    	if(tableEditor.getEditor() == null) {
    		return;
    	}
    	tableEditor.setEditor(null, null, columnToEdit);
        if (cellEditor != null) {
            cellEditor.deactivate();
            fireCellEditorDeactivated(cellEditor);
            cellEditor.removeListener(editorListener);
            cellEditor = null;
        }
        // clear any error message from the editor
//        setErrorMessage(null);
    }

    private void fireCellEditorActivated(CellEditor activatedCellEditor) {
        Object[] listeners = activationListeners.getListeners();
        for (int i = 0; i < listeners.length; ++i) {
            ((ICellEditorActivationListener) listeners[i])
                    .cellEditorActivated(activatedCellEditor);
        }
    }

    /**
     * Notifies all registered cell editor activation listeners of a cell editor
     * deactivation.
     * 
     * @param activatedCellEditor
     *            the deactivated cell editor
     */
    private void fireCellEditorDeactivated(CellEditor activatedCellEditor) {
        Object[] listeners = activationListeners.getListeners();
        for (int i = 0; i < listeners.length; ++i) {
            ((ICellEditorActivationListener) listeners[i])
                    .cellEditorDeactivated(activatedCellEditor);
        }
    }
    public void applyEditorValue() {
        TableItem treeItem = tableEditor.getItem();
        // treeItem can be null when view is opened
        if (treeItem == null || treeItem.isDisposed()) {
			return;
		}
        if(editedDescriptor.getPropertyDescriptor() == treeItem.getData()) {
    		if (cellEditor == null) {
    			return;
    		}

    		// Check if editor has a valid value
    		if (!cellEditor.isValueValid()) {
//    			setErrorText(cellEditor.getErrorMessage());
    			return;
    		}

//    		setErrorText(null);

    		// See if the value changed and if so update
    		editedDescriptor.applyValue(cellEditor.getValue());
        }
    }

    private void entrySelectionChanged() {
    }

    
	public CellEditor getCellEditor() {
		return cellEditor;
	}

	public CellEditor getCellEditor(Composite parent) {
		if (cellEditor == null) {
			cellEditor = editedDescriptor.getPropertyDescriptor().createPropertyEditor(parent);
			if (cellEditor != null) {
				cellEditor.addListener(cellEditorListener);
			}
		}
		if (cellEditor != null) {
			cellEditor.setValue(editedDescriptor.getValue());
//			setErrorText(cellEditor.getErrorMessage());
		}
		return cellEditor;
	}

	private ICellEditorListener cellEditorListener = new ICellEditorListener() {
		public void editorValueChanged(boolean oldValidState,
				boolean newValidState) {
			if (!newValidState) {
				// currently not valid so show an error message
//				setErrorText(cellEditor.getErrorMessage());
			} else {
				// currently valid
//				setErrorText(null);
			}
		}

		public void cancelEditor() {
//			setErrorText(null);
		}

		public void applyEditorValue() {
			DefaultPropertySetViewer.this.applyEditorValue();
		}
	};


}