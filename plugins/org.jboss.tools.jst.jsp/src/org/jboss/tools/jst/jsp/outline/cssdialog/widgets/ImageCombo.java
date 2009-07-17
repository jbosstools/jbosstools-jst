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
package org.jboss.tools.jst.jsp.outline.cssdialog.widgets;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TypedListener;
import org.eclipse.swt.widgets.Widget;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;


/**
 * The ImageCombo Combo-Widget class with images represents a selectable user
 * interface object that combines a text field and a list and issues
 * notification when an item is selected from the list.
 *
 * @author dsakovich@exadel.com
 */
public final class ImageCombo extends Composite implements CSSWidget {
    private static final boolean gtk = "gtk".equals(SWT.getPlatform()); //$NON-NLS-1$
    private static final int X = 0;
    private static final int Y = 0;
    private static final int WIDTH = 20;
    private static final int HEIGHT = 15;
    private static final int DEPTH = 1;
    private static final int DEFAULT_SELECTION = -1;
    private boolean dontDrop = false;
    private int visibleItemCount = 10;
    private Text text;
    private Table table;
    private Shell popup;
    private Button arrowButton;
    private boolean hasFocus;
    private Listener listener;
    private Listener filter;

    /**
     * Constructs a new instance of this class given its parent and a style
     * value describing its behavior and appearance.
     * <p>
     * The style value is either one of the style constants defined in class
     * <code>SWT</code> which is applicable to instances of this class, or must
     * be built by <em>bitwise OR</em>'ing together (that is, using the
     * <code>int</code> "|" operator) two or more of those <code>SWT</code>
     * style constants. The class description lists the style constants that are
     * applicable to the class. Style bits are also inherited from superclasses.
     * </p>
     *
     * @param parent
     *            a widget which will be the parent of the new instance (cannot
     *            be null)
     * @param style
     *            the style of widget to construct
     *
     * @exception IllegalArgumentException
     *                <ul>
     *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
     *                </ul>
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the parent</li>
     *                </ul>
     *
     * @see SWT#BORDER
     * @see SWT#READ_ONLY
     * @see SWT#FLAT
     * @see Widget#getStyle()
     */
    public ImageCombo(Composite parent, int style) {
        super(parent, style = checkStyle(style));

        int textStyle = SWT.SINGLE;

        if (gtk) {
            textStyle |= SWT.BORDER;
        }

        if ((style & SWT.READ_ONLY) != 0) {
            textStyle |= SWT.READ_ONLY;
        }

        if ((style & SWT.FLAT) != 0) {
            textStyle |= SWT.FLAT;
        }

        text = new Text(this, textStyle);

        int arrowStyle = SWT.ARROW | SWT.DOWN;

        if ((style & SWT.FLAT) != 0) {
            arrowStyle |= SWT.FLAT;
        }

        arrowButton = new Button(this, arrowStyle);
        listener = new Listener() {
        	public void handleEvent(Event event) {
        		if (popup == event.widget) {
        			popupEvent(event);

        			return;
        		}
        		if (text == event.widget) {
        			textEvent(event);

        			return;
        		}
        		if (table == event.widget) {
        			listEvent(event);

        			return;
        		}
        		if (arrowButton == event.widget) {
        			arrowEvent(event);

        			return;
        		}
        		if (ImageCombo.this == event.widget) {
        			comboEvent(event);

        			return;
        		}
        		if (getShell() == event.widget) {
        			handleFocus(SWT.FocusOut);
        		}
        	}
        };
        filter = new Listener() {
        	public void handleEvent(Event event) {
        		Shell shell = ((Control) event.widget).getShell();

        		if (shell == ImageCombo.this.getShell()) {
        			handleFocus(SWT.FocusOut);
        		}
        	}
        };

        int[] comboEvents = { SWT.Dispose, SWT.Move, SWT.Resize };
        for (int i = 0; i < comboEvents.length; i++)
            this.addListener(comboEvents[i], listener);

        int[] textEvents = {
        		SWT.KeyDown, SWT.KeyUp, SWT.Modify, SWT.MouseDown, SWT.MouseUp, SWT.Traverse, SWT.FocusIn
        };

        for (int i = 0; i < textEvents.length; i++) {
            text.addListener(textEvents[i], listener);
        }
        int[] arrowEvents = { SWT.Selection, SWT.FocusIn };

        for (int i = 0; i < arrowEvents.length; i++) {
            arrowButton.addListener(arrowEvents[i], listener);
        }
        createPopup(DEFAULT_SELECTION);
    }

    /**
     * Check style.
     * @param style
     * @return
     */
    static int checkStyle(int style) {
        int mask = gtk ? (SWT.READ_ONLY | SWT.FLAT | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT)
                       : (SWT.BORDER | SWT.READ_ONLY | SWT.FLAT | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);

        return style & mask;
    }

    /**
     * Adds the argument to the end of the receiver's list.
     *
     * @param string
     *            the new item
     *
     * @param image
     *            image for the item
     *
     * @exception IllegalArgumentException
     *                <ul>
     *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
     *                </ul>
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     *
     * @see #add(String,int)
     */
    public void add(String string, Image image) {
        checkWidget();

        if (string == null) {
            SWT.error(SWT.ERROR_NULL_ARGUMENT);
        }

        TableItem newItem = new TableItem(this.table, SWT.NONE);
        newItem.setText(string);

        if (image != null) {
            newItem.setImage(image);
        }

        newItem.addDisposeListener(new DisposeListener() {
                public void widgetDisposed(DisposeEvent e) {
                    TableItem item = (TableItem) e.getSource();
                    item.getImage().dispose();
                }
            });
    }

    /**
     * Adds the argument to the receiver's list at the given zero-relative
     * index.
     * <p>
     * Note: To add an item at the end of the list, use the result of calling
     * <code>getItemCount()</code> as the index or use <code>add(String)</code>.
     * </p>
     *
     * @param string
     *            the new item
     *
     * @param image
     *            image for the item
     *
     * @param index
     *            the index for the item
     *
     * @exception IllegalArgumentException
     *                <ul>
     *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
     *                <li>ERROR_INVALID_RANGE - if the index is not between 0
     *                and the number of elements in the list (inclusive)</li>
     *                </ul>
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     *
     * @see #add(String)
     */
    public void add(String string, Image image, int index) {
        checkWidget();

        if (string == null) {
            SWT.error(SWT.ERROR_NULL_ARGUMENT);
        }

        TableItem newItem = new TableItem(this.table, SWT.NONE, index);

        if (image != null) {
            newItem.setImage(image);
            newItem.addDisposeListener(new DisposeListener() {
                    public void widgetDisposed(DisposeEvent e) {
                        TableItem item = (TableItem) e.getSource();
                        item.getImage().dispose();
                    }
                });
        }
    }

    /**
     * Adds the argument to the end of the receiver's list.
     *
     * @param string
     *            the new item
     * @param rgb
     *            RGB color image for the item
     *
     * @exception IllegalArgumentException
     *                <ul>
     *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
     *                </ul>
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     *
     * @see #add(String,int)
     */
    public void add(String string, RGB rgb) {
        Color white = new Color(getDisplay(), Constants.RGB_WHITE);
        Color black = new Color(getDisplay(), Constants.RGB_BLACK);
        Color color = new Color(getDisplay(), rgb);

        Image image = new Image(getDisplay(), WIDTH, HEIGHT);
        GC gc = new GC(image);
        gc.setBackground(color);
        gc.fillRectangle(X, Y, WIDTH, HEIGHT);
        gc.setBackground(black);
        gc.drawRectangle(X, Y, WIDTH, HEIGHT);
        gc.dispose();

        ImageData imageData = image.getImageData();
        image.dispose();
        color.dispose();

        PaletteData palette = new PaletteData(new RGB[] { Constants.RGB_BLACK, Constants.RGB_WHITE, });
        ImageData maskData = new ImageData(WIDTH, HEIGHT, DEPTH, palette);
        Image mask = new Image(getDisplay(), maskData);
        gc = new GC(mask);
        gc.setBackground(black);
        gc.fillRectangle(X, Y, WIDTH, HEIGHT);
        gc.setBackground(white);
        gc.fillRectangle(X, Y, WIDTH, HEIGHT);
        gc.dispose();
        maskData = mask.getImageData();
        mask.dispose();

        Image icon = new Image(getDisplay(), imageData, maskData);
        color.dispose();
        black.dispose();
        white.dispose();
        add(string, icon);
    }

    /**
     * Adds the listener to the collection of listeners who will be notified
     * when the receiver's text is modified, by sending it one of the messages
     * defined in the <code>ModifyListener</code> interface.
     *
     * @param listener
     *            the listener which should be notified
     *
     * @exception IllegalArgumentException
     *                <ul>
     *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
     *                </ul>
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     *
     * @see ModifyListener
     * @see #removeModifyListener
     */
    public void addModifyListener(ModifyListener listener) {
        checkWidget();

        if (listener == null) {
            SWT.error(SWT.ERROR_NULL_ARGUMENT);
        }

        TypedListener typedListener = new TypedListener(listener);
        addListener(SWT.Modify, typedListener);
    }

    /**
     * Adds the listener to the collection of listeners who will be notified
     * when the receiver's selection changes, by sending it one of the messages
     * defined in the <code>SelectionListener</code> interface.
     * <p>
     * <code>widgetSelected</code> is called when the combo's list selection
     * changes. <code>widgetDefaultSelected</code> is typically called when
     * ENTER is pressed the combo's text area.
     * </p>
     *
     * @param listener
     *            the listener which should be notified
     *
     * @exception IllegalArgumentException
     *                <ul>
     *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
     *                </ul>
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     *
     * @see SelectionListener
     * @see #removeSelectionListener
     * @see SelectionEvent
     */
    public void addSelectionListener(SelectionListener listener) {
        checkWidget();

        if (listener == null) {
            SWT.error(SWT.ERROR_NULL_ARGUMENT);
        }

        TypedListener typedListener = new TypedListener(listener);
        addListener(SWT.Selection, typedListener);
        addListener(SWT.DefaultSelection, typedListener);
    }

    /**
     *
     * @param event
     */
    void arrowEvent(Event event) {
        switch (event.type) {
        case SWT.FocusIn: {
            if (gtk) {
                setFocus();

                return;
            }

            handleFocus(SWT.FocusIn);

            break;
        }

        case SWT.Selection: {
            if (gtk) {
                if (!dontDrop) {
                    dropDown(!isDropped());
                }

                dontDrop = false;
            } else {
                dropDown(!isDropped());
            }

            break;
        }
        }
    }

    /**
     *
     * @param event
     */
    void comboEvent(Event event) {
        switch (event.type) {
        case SWT.Dispose:

            if ((popup != null) && !popup.isDisposed()) {
                table.removeListener(SWT.Dispose, listener);
                popup.dispose();
            }

            Shell shell = getShell();
            shell.removeListener(SWT.Deactivate, listener);

            Display display = getDisplay();
            display.removeFilter(SWT.FocusIn, filter);
            popup = null;
            text = null;
            table = null;
            arrowButton = null;

            break;

        case SWT.Move:
            dropDown(false);

            break;

        case SWT.Resize:
            internalLayout(false);

            break;
        }
    }

    /**
     * Method for computing size for widget
     *
     * @param wHint
     *            width
     * @param hHint
     *            height
     * @return Point
     */
    public Point computeSize(int wHint, int hHint, boolean changed) {
        checkWidget();

        int width = 0;
        int height = 0;
        String[] items = getStringsFromTable();
        int textWidth = 0;
        GC gc = new GC(text);
        int spacer = gc.stringExtent(Constants.WHITE_SPACE).x;

        for (int i = 0; i < items.length; i++) {
            textWidth = Math.max(gc.stringExtent(items[i]).x, textWidth);
        }

        gc.dispose();

        Point textSize = text.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
        Point arrowSize = arrowButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
        Point listSize = table.computeSize(wHint, SWT.DEFAULT, changed);
        int borderWidth = getBorderWidth();

        height = Math.max(hHint, Math.max(textSize.y, arrowSize.y) + (2 * borderWidth));
        width = Math.max(wHint,
                Math.max(textWidth + (2 * spacer) + arrowSize.x + (2 * borderWidth), listSize.x));

        return new Point(width, height);
    }

    /**
     * Method for creating popup table
     *
     * @param selectionIndex
     */
    void createPopup(int selectionIndex) {
        // create shell and list
        popup = new Shell(getShell(), SWT.NO_TRIM | SWT.ON_TOP);

        int style = getStyle();
        int listStyle = SWT.SINGLE | SWT.V_SCROLL;

        if ((style & SWT.FLAT) != 0) {
            listStyle |= SWT.FLAT;
        }

        if ((style & SWT.RIGHT_TO_LEFT) != 0) {
            listStyle |= SWT.RIGHT_TO_LEFT;
        }

        if ((style & SWT.LEFT_TO_RIGHT) != 0) {
            listStyle |= SWT.LEFT_TO_RIGHT;
        }

        // create a table instead of a list.
        table = new Table(popup, listStyle);
        table.setToolTipText(JstUIMessages.IMAGE_COMBO_TABLE_TOOL_TIP);

        int[] popupEvents = { SWT.Close, SWT.Paint, SWT.Deactivate };

        for (int i = 0; i < popupEvents.length; i++)
            popup.addListener(popupEvents[i], listener);

        int[] listEvents = {
                SWT.Traverse, SWT.MouseDoubleClick, SWT.KeyDown, SWT.KeyUp, SWT.FocusIn, SWT.Dispose
            };

        for (int i = 0; i < listEvents.length; i++)
            table.addListener(listEvents[i], listener);

        if (selectionIndex != -1) {
            table.setSelection(selectionIndex);
        }
    }

    /**
     *
     * @param drop
     */
    void dropDown(boolean drop) {
        if (drop == isDropped()) {
            return;
        }

        if (!drop) {
            popup.setVisible(false);

            if (!isDisposed() && arrowButton.isFocusControl()) {
                text.setFocus();
            }

            return;
        }

        if (getShell() != popup.getParent()) {
            int selectionIndex = table.getSelectionIndex();
            table.removeListener(SWT.Dispose, listener);
            popup.dispose();
            popup = null;
            table = null;
            createPopup(selectionIndex);
        }

        Point size = getSize();
        int itemCount = table.getItemCount();
        itemCount = (itemCount == 0) ? visibleItemCount : Math.min(visibleItemCount, itemCount);

        int itemHeight = table.getItemHeight() * itemCount;
        Point listSize = table.computeSize(SWT.DEFAULT, itemHeight, false);
        table.setBounds(1, 1, Math.max(size.x - 2, listSize.x), listSize.y);

        int index = table.getSelectionIndex();

        if (index != -1) {
            table.setTopIndex(index);
        }

        Display display = getDisplay();
        Rectangle listRect = table.getBounds();
        Rectangle parentRect = display.map(getParent(), null, getBounds());
        Point comboSize = getSize();
        Rectangle displayRect = getMonitor().getClientArea();
        int width = Math.max(comboSize.x, listRect.width + 2);
        int height = listRect.height + 2;
        int x = parentRect.x;
        int y = parentRect.y + comboSize.y;

        if ((y + height) > (displayRect.y + displayRect.height)) {
            y = parentRect.y - height;
        }

        popup.setBounds(x, y, width, height);
        popup.setVisible(true);
        table.setFocus();
    }

    /**
     * Returns the number of items contained in the receiver's list.
     *
     * @return the number of items
     *
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public int getItemCount() {
        checkWidget();

        return table.getItemCount();
    }

    String[] getStringsFromTable() {
        String[] items = new String[this.table.getItems().length];

        for (int i = 0, n = items.length; i < n; i++) {
            items[i] = this.table.getItem(i).getText();
        }

        return items;
    }

    /**
     * Returns the zero-relative index of the item which is currently selected
     * in the receiver's list, or -1 if no item is selected.
     *
     * @return the index of the selected item
     *
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public int getSelectionIndex() {
        checkWidget();

        return table.getSelectionIndex();
    }

    /**
     * Method for getting widget style
     */
    public int getStyle() {
        int style = super.getStyle();
        style &= ~SWT.READ_ONLY;

        if (!text.getEditable()) {
            style |= SWT.READ_ONLY;
        }

        return style;
    }

    /**
     * Returns a string containing a copy of the contents of the receiver's text
     * field.
     *
     * @return the receiver's text
     *
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public String getText() {
        checkWidget();

        return text.getText();
    }

    /**
     *
     * @param type
     */
    void handleFocus(int type) {
        if (isDisposed()) {
            return;
        }

        switch (type) {
        case SWT.FocusIn: {
            if (hasFocus) {
                return;
            }

            hasFocus = true;

            Shell shell = getShell();
            shell.removeListener(SWT.Deactivate, listener);
            shell.addListener(SWT.Deactivate, listener);

            Display display = getDisplay();
            display.removeFilter(SWT.FocusIn, filter);
            display.addFilter(SWT.FocusIn, filter);

            Event e = new Event();
            notifyListeners(SWT.FocusIn, e);

            break;
        }

        case SWT.FocusOut: {
            if (!hasFocus) {
                return;
            }

            Control focusControl = getDisplay().getFocusControl();

            if ((focusControl == arrowButton) || (focusControl == table) || (focusControl == text)) {
                return;
            }

            hasFocus = false;

            Shell shell = getShell();
            shell.removeListener(SWT.Deactivate, listener);

            Display display = getDisplay();
            display.removeFilter(SWT.FocusIn, filter);

            Event e = new Event();
            notifyListeners(SWT.FocusOut, e);

            break;
        }
        }
    }

    /**
     * Searches the receiver's list starting at the first item (index 0) until
     * an item is found that is equal to the argument, and returns the index of
     * that item. If no item is found, returns -1.
     *
     * @param string
     *            the search item
     * @return the index of the item
     *
     * @exception IllegalArgumentException
     *                <ul>
     *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
     *                </ul>
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public int indexOf(String string) {
        checkWidget();

        if (string == null) {
            SWT.error(SWT.ERROR_NULL_ARGUMENT);
        }

        return Arrays.asList(getStringsFromTable()).indexOf(string);
    }

    /**
     *
     * @return
     */
    boolean isDropped() {
        return popup.getVisible();
    }

    /**
     *
     * @param changed
     */
    void internalLayout(boolean changed) {
        if (isDropped()) {
            dropDown(false);
        }

        Rectangle rect = getClientArea();
        int width = rect.width;
        int height = rect.height;
        Point arrowSize = arrowButton.computeSize(SWT.DEFAULT, height, changed);
        text.setBounds(0, 0, width - arrowSize.x, height);
        arrowButton.setBounds(width - arrowSize.x, 0, arrowSize.x, arrowSize.y);
    }

    /**
     *
     * @param event
     */
    void listEvent(Event event) {
        switch (event.type) {
        case SWT.Dispose:

            if (getShell() != popup.getParent()) {
                int selectionIndex = table.getSelectionIndex();
                popup = null;
                table = null;
                createPopup(selectionIndex);
            }

            break;

        case SWT.FocusIn: {
            handleFocus(SWT.FocusIn);

            break;
        }

        case SWT.MouseDoubleClick: {
            int index = table.getSelectionIndex();

            if (index == -1) {
                return;
            }

            text.setText(table.getItem(index).getText());
            table.setSelection(index);

            Event e = new Event();
            e.time = event.time;
            e.stateMask = event.stateMask;
            e.doit = event.doit;
            notifyListeners(SWT.Selection, e);
            event.doit = e.doit;
            dropDown(false);

            break;
        }

        case SWT.Traverse: {
            switch (event.detail) {
            case SWT.TRAVERSE_RETURN:
            case SWT.TRAVERSE_ESCAPE:
            case SWT.TRAVERSE_ARROW_PREVIOUS:
            case SWT.TRAVERSE_ARROW_NEXT:
                event.doit = false;

                break;
            }

            Event e = new Event();
            e.time = event.time;
            e.detail = event.detail;
            e.doit = event.doit;
            e.character = event.character;
            e.keyCode = event.keyCode;
            notifyListeners(SWT.Traverse, e);
            event.doit = e.doit;
            event.detail = e.detail;

            break;
        }

        case SWT.KeyUp: {
            Event e = new Event();
            e.time = event.time;
            e.character = event.character;
            e.keyCode = event.keyCode;
            e.stateMask = event.stateMask;
            notifyListeners(SWT.KeyUp, e);

            break;
        }

        case SWT.KeyDown: {
            if (event.character == SWT.ESC) {
                // Escape key cancels popup list
                dropDown(false);
            }

            if (((event.stateMask & SWT.ALT) != 0) &&
                    ((event.keyCode == SWT.ARROW_UP) || (event.keyCode == SWT.ARROW_DOWN))) {
                dropDown(false);
            }

            if (event.character == SWT.CR) {
                // Enter causes default selection
                dropDown(false);

                Event e = new Event();
                e.time = event.time;
                e.stateMask = event.stateMask;
                notifyListeners(SWT.DefaultSelection, e);
            }

            if (isDisposed()) {
                break;
            }

            Event e = new Event();
            e.time = event.time;
            e.character = event.character;
            e.keyCode = event.keyCode;
            e.stateMask = event.stateMask;
            notifyListeners(SWT.KeyDown, e);

            int index = table.getSelectionIndex();

            if (index == -1) {
                return;
            }

            text.setText(table.getItem(index).getText());
            table.setSelection(index);

            break;
        }
        }
    }

    /**
     *
     * @param event
     */
    void popupEvent(Event event) {
        switch (event.type) {
        case SWT.Paint:

            // draw black rectangle around list
            Rectangle listRect = table.getBounds();
            Color black = getDisplay().getSystemColor(SWT.COLOR_BLACK);
            event.gc.setForeground(black);
            event.gc.drawRectangle(X, Y, listRect.width + 1, listRect.height + 1);

            break;

        case SWT.Close:
            event.doit = false;
            dropDown(false);

            break;

        case SWT.Deactivate:

            // when the popup shell is deactivated by clicking the button,
            // we receive two Deactivate events on Win32, whereas on GTK
            // we first receive one Deactivation event from the shell and
            // then a Selection event from the button.
            // as a work-around, set a flag (dontDrop) if running GTK
            if (gtk) {
                Point loc = arrowButton.toControl(getDisplay().getCursorLocation());
                Point size = arrowButton.getSize();

                if ((loc.x >= 0) && (loc.y >= 0) && (loc.x < size.x) && (loc.y < size.y)) {
                    dontDrop = true;
                }
            }

            dropDown(false);

            break;
        }
    }

    /**
     * Method for redrawing widget
     */
    public void redraw() {
        super.redraw();
        text.redraw();
        arrowButton.redraw();

        if (popup.isVisible()) {
            table.redraw();
        }
    }

    /**
     * Selects the item at the given zero-relative index in the receiver's list.
     * If the item at the index was already selected, it remains selected.
     * Indices that are out of range are ignored.
     *
     * @param index
     *            the index of the item to select
     *
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public void select(int index) {
        checkWidget();

        if (index == -1) {
            table.deselectAll();
            text.setText(Constants.EMPTY);

            return;
        }

        if ((0 <= index) && (index < table.getItemCount())) {
            if (index != getSelectionIndex()) {
                text.setText(table.getItem(index).getText());
                text.selectAll();
                table.select(index);
                table.showSelection();
            }
        }
    }

    /**
     * Method for setting focus in text widget
     */
    public boolean setFocus() {
        checkWidget();

        return text.setFocus();
    }

    /**
     * Sets the contents of the receiver's text field to the given string.
     * <p>
     * Note: The text field in a <code>Combo</code> is typically only capable of
     * displaying a single line of text. Thus, setting the text to a string
     * containing line breaks or other special characters will probably cause it
     * to display incorrectly.
     * </p>
     *
     * @param string
     *            the new text
     *
     * @exception IllegalArgumentException
     *                <ul>
     *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
     *                </ul>
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public void setText(String string) {
        checkWidget();

        if (string == null) {
            SWT.error(SWT.ERROR_NULL_ARGUMENT);
        }

        int index = -1;

        for (int i = 0, n = table.getItemCount(); i < n; i++) {
            if (table.getItem(i).getText().equals(string)) {
                index = i;

                break;
            }
        }

        if (index == -1) {
            table.deselectAll();
            text.setText(string);

            return;
        }

        text.setText(string);
        text.selectAll();
        table.setSelection(index);
        table.showSelection();
    }

    /**
     *
     * @param event
     */
    void textEvent(Event event) {
        switch (event.type) {
        case SWT.FocusIn: {
            handleFocus(SWT.FocusIn);

            break;
        }

        case SWT.KeyDown: {
            if (event.character == SWT.CR) {
                dropDown(false);

                Event e = new Event();
                e.time = event.time;
                e.stateMask = event.stateMask;
                notifyListeners(SWT.DefaultSelection, e);
            }

            if (isDisposed()) {
                break;
            }

            if ((event.keyCode == SWT.ARROW_UP) || (event.keyCode == SWT.ARROW_DOWN)) {
                event.doit = false;

                if ((event.stateMask & SWT.ALT) != 0) {
                    boolean dropped = isDropped();
                    text.selectAll();

                    if (!dropped) {
                        setFocus();
                    }

                    dropDown(!dropped);

                    break;
                }

                int oldIndex = getSelectionIndex();

                if (event.keyCode == SWT.ARROW_UP) {
                    select(Math.max(oldIndex - 1, 0));
                } else {
                    select(Math.min(oldIndex + 1, getItemCount() - 1));
                }

                if (oldIndex != getSelectionIndex()) {
                    Event e = new Event();
                    e.time = event.time;
                    e.stateMask = event.stateMask;
                    notifyListeners(SWT.Selection, e);
                }

                if (isDisposed()) {
                    break;
                }
            }

            Event e = new Event();
            e.time = event.time;
            e.character = event.character;
            e.keyCode = event.keyCode;
            e.stateMask = event.stateMask;
            notifyListeners(SWT.KeyDown, e);

            break;
        }

        case SWT.KeyUp: {
            Event e = new Event();
            e.time = event.time;
            e.character = event.character;
            e.keyCode = event.keyCode;
            e.stateMask = event.stateMask;
            notifyListeners(SWT.KeyUp, e);

            break;
        }

        case SWT.Modify: {
            table.deselectAll();

            Event e = new Event();
            e.time = event.time;
            notifyListeners(SWT.Modify, e);

            break;
        }

        case SWT.MouseDown: {
            if (event.button != 1) {
                return;
            }

            if (text.getEditable()) {
                return;
            }

            boolean dropped = isDropped();
            text.selectAll();

            if (!dropped) {
                setFocus();
            }

            dropDown(!dropped);

            break;
        }

        case SWT.MouseUp: {
            if (event.button != 1) {
                return;
            }

            if (text.getEditable()) {
                return;
            }

            text.selectAll();

            break;
        }

        case SWT.Traverse: {
            switch (event.detail) {
            case SWT.TRAVERSE_RETURN:
            case SWT.TRAVERSE_ARROW_PREVIOUS:
            case SWT.TRAVERSE_ARROW_NEXT:
                event.doit = false;

                break;
            }

            Event e = new Event();
            e.time = event.time;
            e.detail = event.detail;
            e.doit = event.doit;
            e.character = event.character;
            e.keyCode = event.keyCode;
            notifyListeners(SWT.Traverse, e);
            event.doit = e.doit;
            event.detail = e.detail;

            break;
        }
        }
    }
}
