/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.outline.cssdialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Class for choosing CCS font-family attribute
 * 
 * @author dsakovich@exadel.com
 */
public class FontFamilyDialog extends Dialog implements SelectionListener {

	private static final int VIEWER_HEIGHT = 300;
	private static final int BUTTOND_WIDTH = 50;
	private static final int VIEWER_WIDTH = 175;

	/** Font family string */
	private String fontFamily;

	/** Existing font family */
	private String existFontFamily;
	private TableViewer fontFamilyTable;
	private TableViewer allFontFamilyTable;
	private Button rightButton;
	private Button leftButton;
	private Composite buttonsContainer;
	private static final String ALL_FONTS_TABLE_SOURCE = "all_fonts_table_source"; //$NON-NLS-1$
	private static final String FONTS_TABLE_SOURCE = "fonts_table_source"; //$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param parentShell
	 *            parent shell
	 * @param existingFontFamily
	 *            existing font family
	 */
	public FontFamilyDialog(Shell parentShell, String existingFontFamily) {
		super(parentShell);
		this.existFontFamily = existingFontFamily;
	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite composite = (Composite) super.createDialogArea(parent);
		initControls(composite);
		createLayout(composite);
		initListeners();
		initDND();
		initDefaultContent(composite);
		return composite;
	}

	private void initDefaultContent(Composite composite) {
		Set<String> s = new HashSet<String>();
		FontData[] fds = composite.getDisplay().getFontList(null, false);

		for (int i = 0; i < fds.length; ++i) {
			s.add(fds[i].getName());
		}
		fds = composite.getDisplay().getFontList(null, true);

		for (int i = 0; i < fds.length; ++i) {
			s.add(fds[i].getName());
		}
		String[] existFonts = fontFamilyParser();
		Arrays.sort(existFonts);

		String[] answer = new String[s.size()];
		s.toArray(answer);
		Arrays.sort(answer);

		for (int i = 0; i < answer.length; i++) {
			allFontFamilyTable.add(answer[i]);
		}

		if ((existFontFamily != null)
				&& !existFontFamily.equals(Constants.EMPTY)) {
			for (int i = 0; i < existFonts.length; i++) {
				fontFamilyTable.add(existFonts[i]);
				allFontFamilyTable.remove(existFonts[i]);
			}
		}
	}

	private void initDND() {
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };

		allFontFamilyTable.addDragSupport(DND.DROP_MOVE, types,
				new DragSourceListener() {

					public void dragFinished(DragSourceEvent event) {
						if (event.doit) {
							if (event.detail == DND.DROP_MOVE) {
								handleAddFont();
							}
						}
					}

					public void dragSetData(DragSourceEvent event) {
						event.data = ALL_FONTS_TABLE_SOURCE;
					}

					public void dragStart(DragSourceEvent event) {
						event.doit = allFontFamilyTable.getTable()
								.getSelectionCount() > 0;
					}
				});

		allFontFamilyTable.addDropSupport(DND.DROP_MOVE, types,
				new DropTargetAdapter() {

					@Override
					public void drop(DropTargetEvent event) {
						if (event.data == null
								|| event.data.equals(ALL_FONTS_TABLE_SOURCE)) {
							event.detail = DND.DROP_NONE;
							return;
						}
					}

				});

		fontFamilyTable.addDragSupport(DND.DROP_MOVE, types,
				new DragSourceListener() {

					public void dragFinished(DragSourceEvent event) {
						if (event.doit) {
							if (event.detail == DND.DROP_MOVE) {
								handleRemoveFont();
							}
						}
					}

					public void dragSetData(DragSourceEvent event) {
						event.data = FONTS_TABLE_SOURCE;
					}

					public void dragStart(DragSourceEvent event) {
						event.doit = fontFamilyTable.getTable()
								.getSelectionCount() > 0;
					}
				});

		fontFamilyTable.addDropSupport(DND.DROP_MOVE, types,
				new DropTargetAdapter() {
					@Override
					public void drop(DropTargetEvent event) {
						if (event.data == null
								|| event.data.equals(FONTS_TABLE_SOURCE)) {
							event.detail = DND.DROP_NONE;
							return;
						}
					}
				});

	}

	private void initListeners() {
		/** Control listeners */
		allFontFamilyTable.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				handleAddFont();
			}
		});

		allFontFamilyTable
				.addSelectionChangedListener(new ISelectionChangedListener() {

					public void selectionChanged(SelectionChangedEvent event) {
						fontFamilyTable.getTable().deselectAll();
						leftButton.setEnabled(false);
						rightButton.setEnabled(true);
					}
				});

		fontFamilyTable
				.addSelectionChangedListener(new ISelectionChangedListener() {

					public void selectionChanged(SelectionChangedEvent event) {
						allFontFamilyTable.getTable().deselectAll();
						rightButton.setEnabled(false);
						leftButton.setEnabled(true);
					}
				});

		fontFamilyTable.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				handleRemoveFont();
			}
		});

		fontFamilyTable.getTable().addKeyListener(new KeyListener() {
			
			public void keyReleased(KeyEvent e) {
				if (SWT.ARROW_LEFT == e.keyCode && SWT.ALT == e.stateMask) {
					handleRemoveFont();
				}
			}
			
			public void keyPressed(KeyEvent e) {
			}
		});
		
		allFontFamilyTable.getTable().addKeyListener(new KeyListener() {
			
			public void keyReleased(KeyEvent e) {
				if (SWT.ARROW_RIGHT== e.keyCode && SWT.ALT == e.stateMask) {
					handleAddFont();
				}
			}
			
			public void keyPressed(KeyEvent e) {
			}
		});
		
		rightButton.addSelectionListener(this);
		leftButton.addSelectionListener(this);
	}

	private void createLayout(Composite composite) {
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		composite.setLayout(gridLayout);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		composite.setLayoutData(gridData);

		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = VIEWER_HEIGHT;
		gridData.widthHint = VIEWER_WIDTH;
		allFontFamilyTable.getTable().setLayoutData(gridData);

		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = VIEWER_HEIGHT;
		gridData.widthHint = VIEWER_WIDTH;
		fontFamilyTable.getTable().setLayoutData(gridData);

		final GridLayout btmContGridLayout = new GridLayout();
		btmContGridLayout.numColumns = 1;
		buttonsContainer.setLayout(btmContGridLayout);

		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.widthHint = BUTTOND_WIDTH;
		rightButton.setLayoutData(gridData);

		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.widthHint = BUTTOND_WIDTH;
		leftButton.setLayoutData(gridData);

	}

	private void initControls(Composite composite) {
		allFontFamilyTable = new TableViewer(composite, SWT.MULTI
				| SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		buttonsContainer = new Composite(composite, SWT.NONE);
		rightButton = new Button(buttonsContainer, SWT.PUSH);
		leftButton = new Button(buttonsContainer, SWT.PUSH);
		fontFamilyTable = new TableViewer(composite, SWT.MULTI | SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL);
		rightButton.setToolTipText(JstUIMessages.ADD_FONT_FAMILY_TIP);
		ImageDescriptor rightDesc = JspEditorPlugin
				.getImageDescriptor(Constants.IMAGE_RIGHT_FILE_LOCATION);
		Image rightImage = rightDesc.createImage();
		rightButton.setImage(rightImage);
		rightButton.setEnabled(false);
		rightButton.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				Button button = (Button) e.getSource();
				button.getImage().dispose();
			}
		});

		leftButton.setToolTipText(JstUIMessages.REMOVE_FONT_FAMILY_TIP);

		ImageDescriptor leftDesc = JspEditorPlugin
				.getImageDescriptor(Constants.IMAGE_LEFT_FILE_LOCATION);
		Image leftImage = leftDesc.createImage();
		leftButton.setImage(leftImage);
		leftButton.setEnabled(false);
		leftButton.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				Button button = (Button) e.getSource();
				button.getImage().dispose();
			}
		});
	}

	private void handleAddFont() {
		TableItem[] selectedItems = allFontFamilyTable.getTable()
				.getSelection();
		if (selectedItems != null) {
			Object[] data = new Object[selectedItems.length];
			for (int i = 0; i < selectedItems.length; i++) {
				data[i] = selectedItems[i].getData();
			}
			fontFamilyTable.add(data);
			allFontFamilyTable.remove(data);
			rightButton.setEnabled(false);
			leftButton.setEnabled(false);
		}

	}

	private void handleRemoveFont() {
		TableItem[] selectedItems = fontFamilyTable.getTable().getSelection();
		if (selectedItems != null) {
			Object[] data = new Object[selectedItems.length];
			for (int i = 0; i < selectedItems.length; i++) {
				data[i] = selectedItems[i].getData();
			}
			addFonts(allFontFamilyTable, data);
			fontFamilyTable.remove(data);
			rightButton.setEnabled(false);
			leftButton.setEnabled(false);
		}
	}

	/**
	 * Set title for dialog
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(JstUIMessages.FONT_FAMILY_DIALOG_TITLE);
	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		TableItem[] items = fontFamilyTable.getTable().getItems();
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < items.length; i++) {
			buf.append(((i == 0) ? Constants.EMPTY : Constants.COMMA)
					+ items[i].getData());
		}

		fontFamily = buf.toString();
		super.okPressed();
	}

	/**
	 * Method for add to font to sorted list
	 * 
	 * @param table
	 * @param fonts
	 */
	private void addFonts(TableViewer table, Object[] fonts) {
		Set<Object> s = new HashSet<Object>();
		TableItem[] items = table.getTable().getItems();

		for (int i = 0; i < items.length; i++) {
			s.add(items[i].getData());
		}

		for (int i = 0; i < fonts.length; i++) {
			s.add(fonts[i]);
		}
		table.getTable().removeAll();

		String[] answer = new String[s.size()];
		s.toArray(answer);
		Arrays.sort(answer);

		for (int i = 0; i < answer.length; i++) {
			table.add(answer[i]);
		}
	}

	/**
	 * Getter for fontFamily attribute
	 * 
	 * @return fontFamily
	 */
	public String getFontFamily() {
		return fontFamily;
	}

	/**
	 * Setter for fontFamily attribute
	 * 
	 * @param fontFamily
	 */
	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	/**
	 * Method for parse font family string
	 * 
	 * @param font
	 *            family string
	 * @return list font family
	 */
	private String[] fontFamilyParser() {
		existFontFamily = existFontFamily.trim();

		return existFontFamily.split(Constants.COMMA);
	}

	/**
	 * Selection listener
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent e) {
		Object ob = e.getSource();

		if (ob.equals(leftButton)) {
			handleRemoveFont();
		} else if (ob.equals(rightButton)) {
			handleAddFont();
		} else if (ob.equals(allFontFamilyTable)) {
			fontFamilyTable.getTable().deselectAll();
			leftButton.setEnabled(false);
			rightButton.setEnabled(true);
		} else if (ob.equals(fontFamilyTable)) {
			allFontFamilyTable.getTable().deselectAll();
			rightButton.setEnabled(false);
			leftButton.setEnabled(true);
		}
	}

	/**
	 * Selection listener
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		Object ob = e.getSource();

		if (ob.equals(leftButton)) {
			handleRemoveFont();
		} else if (ob.equals(rightButton)) {
			handleAddFont();
		}
	}
}
