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
package org.jboss.tools.jst.web.ui.palette.html.wizard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.SystemUtils;
import org.eclipse.compare.Splitter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.common.model.ui.editors.dnd.ValidationException;
import org.jboss.tools.common.model.ui.views.palette.IPositionCorrector;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.common.util.SwtUtil;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.dnd.PaletteItemDropCommand;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class AbstractNewHTMLWidgetWizardPage extends AbstractWizardPageWithPreview {
	protected Map<String, IFieldEditor> editors = new HashMap<String, IFieldEditor>();

	protected StyledText text;

	/**
	 * When disableOverridingPreferredBrowser = true, method getPreferredBrowser()
	 * (overridden by wizards that need a specific browser for better rendering)
	 * is ignored. 
	 * 
	 * Because of Java crash when WebKit is loaded in Mars Eclipse in Windows,
	 * we have to disable using preferred browser by wizards, that generate html
	 * currently supported only in WebKit.
	 * 
	 * Since it is a sad thing to dump a good preview for advanced widgets,
	 * we need to keep an eye on browser support in Eclipse in different OS
	 * and before releases try testing wizards with 
	 *     disableOverridingPreferredBrowser = false;
	 * 
	 * Current list of wizards that need WebKit for proper rendering:
	 *     NewRangeSliderWizardPage
	 *     NewTableWizardPage (html)
	 *     NewTableWizardPage (jquery)
	 *     NewTabsWizardPage
	 *     NewHeadingWizardPage
	 *     NewPanelWizardPage
	 *     NewMeterWizardPage
	 *     NewSpinnerWizardPage
	 */
	protected static final boolean disableOverridingPreferredBrowser = true;
	protected Browser browser;

	protected File sourceFile = null;
	protected String sourceURL = null;
	
	public AbstractNewHTMLWidgetWizardPage(String pageName, String title) {
		super(pageName, title);
	}

	public AbstractNewHTMLWidgetWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	@Override
    public AbstractNewHTMLWidgetWizard getWizard() {
        return (AbstractNewHTMLWidgetWizard)super.getWizard();
    }

	protected void createWarningMessage() {
		IPositionCorrector corrector = ((PaletteItemDropCommand)getWizard().command).getPositionCorrector();
		if(corrector != null) {
			ITextSelection s = (ITextSelection)getWizard().getWizardModel().getDropData().getSelectionProvider().getSelection();
			IDocument doc = getWizard().getWizardModel().getDropData().getSourceViewer().getDocument();
			IStructuredModel model = null;
			try{
				model = StructuredModelManager.getModelManager().getExistingModelForRead((IStructuredDocument)doc);
				if(model instanceof IDOMModel) {
					warningMessage = corrector.getWarningMessage(((IDOMModel)model).getDocument(), s);
				}
			} finally {
				if(model != null) {
					model.releaseFromRead();
				}
			}
		}
	}

	String warningMessage;

	@Override
	public void validate() throws ValidationException {
		if(warningMessage != null) {
			throw new ValidationException(warningMessage, true);
		}
	}

	@Override
	protected void createPreview() {
		if(hasVisualPreview()) {
			Splitter previewPanel = new Splitter(panel, SWT.VERTICAL);
			GridData d = new GridData(GridData.FILL_BOTH);
			previewPanel.setLayoutData(d);
			previewPanel.setLayout(new GridLayout());

			createTextPreview(previewPanel);
		
			Composite browserPanel = createBrowserPanel(previewPanel);
			int preferredBrowser = disableOverridingPreferredBrowser ? SWT.NONE : getPreferredBrowser();
			browser = WebUiPlugin.createBrowser(browserPanel, preferredBrowser);

			if(browser != null) {
				browser.setLayoutData(new GridData(GridData.FILL_BOTH));
				browser.pack();
			}
			createDisclaimer(browserPanel);
			previewPanel.setWeights(new int[]{4,6});
			this.previewPanel = previewPanel;
		} else {
			createTextPreview(panel);
			previewPanel = text;
		}
		
	}

	@Override
	protected void startPreview() {
		Display.getCurrent().asyncExec(new Runnable() {
			public void run() {
				if(text == null || text.isDisposed()) {
					return;
				}
				updatePreviewPanel(true, true);
				updatePreviewContent();
				runValidation();
				text.addControlListener(new ControlAdapter() {
					@Override
					public void controlResized(ControlEvent e) {
						if(textLimit < 0 || textLimit == getTextLimit()) return;
						resetText();
					}
				});
			}
		});
	}

	@Override
	protected void onCreateControl() {
		Display.getDefault().addFilter(SWT.FocusOut, focusReturn);
		Display.getDefault().addFilter(SWT.MouseDown, focusReturn);
		Display.getDefault().addFilter(SWT.KeyDown, focusReturn);
		Display.getDefault().addFilter(SWT.Modify, focusReturn);
		createWarningMessage();
	}

	void createTextPreview(Composite parent) {
		text = new StyledText(parent, SWT.MULTI | SWT.READ_ONLY | SWT.BORDER | SWT.V_SCROLL);
		text.setFont(JFaceResources.getTextFont());
		text.setLayoutData(new GridData(GridData.FILL_BOTH));
		/**
		 * We set some initial content to the text widget to provide a reasonable default width
		 * for that widget and for browser. We avoid setting width hint or other ways to 
		 * provide the default width, because text widget and browser should be resizable 
		 * and their content will be formatted to the available width. Also, initial width
		 * is to depend on system font size so that initial content serves best to that purpose. 
		 */
		text.setText("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n<html><body>aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa</body></html>");
	}

	protected static final boolean isLinux = SystemUtils.IS_OS_LINUX;

	protected int getPreferredBrowser() {
		return WebUiPlugin.getPreferredBrowser();
	}

	protected boolean hasVisualPreview() {
		return true;
	}
	
	public String getBrowserType() {
		return browser == null ? null : browser.getBrowserType();
	}

	protected static final String SECTION_NAME = "InsertTag";
	public static final String ADD_JS_CSS_SETTING_NAME = "addJSCSS";

	private Composite createBrowserPanel(Composite previewPanel) {
		Composite browserPanel = new Composite(previewPanel, SWT.BORDER);
		GridData g = new GridData();
		g.horizontalAlignment = SWT.FILL;
		g.verticalAlignment = SWT.FILL;
		g.grabExcessHorizontalSpace = true;
		g.grabExcessVerticalSpace = true;
		browserPanel.setLayoutData(g);
		GridLayout l = new GridLayout();
		l.verticalSpacing = 0;
		l.marginWidth = 0;
		l.marginHeight = 0;
		browserPanel.setLayout(l);
		return browserPanel;
	}

	private void createDisclaimer(Composite browserPanel) {
		Label disclaimer = new Label(browserPanel, SWT.NONE);
		disclaimer.setText(WizardMessages.previewDisclaimer);
		GridData disclaimerData = new GridData();
		disclaimerData.horizontalAlignment = SWT.CENTER;
		disclaimer.setLayoutData(disclaimerData);
		FontData fd = disclaimer.getFont().getFontData()[0];
		fd.setStyle(SWT.ITALIC);
		fd.setHeight(8);
		Font font = new Font(null, fd);
		disclaimer.setFont(font);
		SwtUtil.bindDisposal(font, disclaimer);
	}

	public void addEditor(IFieldEditor editor) {
		editors.put(editor.getName(), editor);
	}

	public void addEditor(IFieldEditor editor, Composite parent) {
		if(parent != null) editor.doFillIntoGrid(parent);
		editor.addPropertyChangeListener(this);
		addEditor(editor);
		Combo c = (parent != null) ? findCombo(editor, parent) : null;
		if(c != null) {
			new ComboContentProposalProvider(c);
		}
	}

	private Combo findCombo(IFieldEditor editor, Composite parent) {
		for (Object o: editor.getEditorControls(parent)) {
			if(o instanceof Combo) {
				return (Combo)o; 
			}
		}
		return null;
	}

	public void addEditor(IFieldEditor editor, Composite parent, boolean expandCombo) {
		addEditor(editor, parent);
		if(expandCombo && parent != null) {
			expandCombo(editor);
		}
	}

	/**
	 * Utility method expanding combo
	 * @param name
	 * @return
	 */
	public void expandCombo(IFieldEditor editor) {
		if(left == null) return;
		Control c = (Control) (editor.getEditorControls()[1]);
		GridData d = (GridData)c.getLayoutData();
		d.horizontalAlignment = SWT.FILL;
		d.grabExcessHorizontalSpace = true;
		c.setLayoutData(d);
	}

	public IFieldEditor getEditor(String name) {
		return editors.get(name);
	}

	public String getEditorValue(String name) {
		return !editors.containsKey(name) ? null : getEditor(name).getValueAsString();
	}

	public void setEditorValue(String name, String value) {
		if(editors.containsKey(name)) {
			getEditor(name).setValue(value);
		}
	}

	File getFile() {
		if(sourceFile == null) {
			try {
				sourceFile = File.createTempFile("jquery_preview", ".html");
				sourceURL = sourceFile.toURI().toURL().toString();
			} catch (IOException e) {
				WebUiPlugin.getDefault().logError(e);
			}
		}
		return sourceFile;
	}

	@Override
	protected void updatePreviewContent() {
		resetText();
		if(browser == null) {
			return;
		}
		File f = getFile();
		FileUtil.writeFile(f, getWizard().getTextForBrowser());

		focusReturn.init();

		if(browser.getUrl() == null || !browser.getUrl().endsWith(f.getName())
				||"mozilla".equals(getBrowserType())) {
			browser.setUrl(sourceURL);
		} else {
			browser.refresh();
		}
	}

	boolean isFocusInBrowser() {
		Control c = Display.getDefault().getFocusControl();
		while(c != null && c != browser) {
			c = c.getParent();
		}
		return c == browser;
	}

	FocusReturn focusReturn = new FocusReturn();

	class FocusReturn implements Listener {
		Control c;
		long t;
		Point s;
		StringBuilder missed = new StringBuilder();
	
		void clear() {
			c = null;
			s = null;
			t = 0;
			missed.setLength(0);
		}

		private Point getSelection() {
			return (c instanceof Combo) ? ((Combo)c).getSelection()
				: (c instanceof Text) ? ((Text)c).getSelection()
				: null;
		}
		
		private String getText() {
			return (c instanceof Combo) ? ((Combo)c).getText()
				: (c instanceof Text) ? ((Text)c).getText()
				: null;
		}

		private void setText(String t) {
			if(c instanceof Combo) {
				((Combo)c).setText(t);
			} else if(c instanceof Text) {
				((Text)c).setText(t);
			}
		}
		
		private void setSelection() {
			if(c instanceof Combo) {
				((Combo)c).setSelection(s);
			} else if(c instanceof Text) {
				((Text)c).setSelection(s);
			}
		}
		
		void init() {
			clear();
			c = Display.getDefault().getFocusControl();
			if(c != null) {
				t = System.currentTimeMillis();
				s = getSelection();
			}
		}
	
		boolean isReady() {
			if(c == null) {
				return false;
			}
			if(System.currentTimeMillis() - t > 2000) {
				clear();
				return false;
			}
			return true;
		}

		void apply() {
			if(!isReady()) {
				return;
			}
			Display.getCurrent().asyncExec(new Runnable() {
				public void run() {
					if(c != null && c != Display.getCurrent().getFocusControl() && isFocusInBrowser()) {
						if(s != null && missed.length() > 0) {
							String t = getText();
							t = t.substring(0, s.x) + missed.toString() + t.substring(s.y);
							s.x += missed.length();
							s.y = s.x;
							setText(t);
						}
						c.forceFocus();
						if(s != null) {
							setSelection();
						}
					}
					clear();
				}
			});
		}

		@Override
		public void handleEvent(Event event) {
			if(event.type == SWT.MouseDown) {
				clear();
			} else if(event.type == SWT.Modify) {
				if(isReady() && c == Display.getDefault().getFocusControl() && c == event.widget) {
					s = getSelection();
				}
			} else if(event.type == SWT.KeyDown) {
				if(isReady() && c == Display.getDefault().getFocusControl()) {
					t = System.currentTimeMillis();
				}
				if(isReady() && event.widget == browser) {
					char ch = event.character;
					if((int)ch >= 32 && (int)ch <= 128) {
						missed.append(ch);
					}
				}
			} else if(event.type == SWT.FocusOut) {
				apply();
			}
		}
	}

	int getTextLimit() {
		int c = text.getSize().x - 2;
		if(text.getVerticalBar() != null) {
			int w = text.getVerticalBar().getSize().x;
			if(w > 0) {
				c -= w + 5;
			}
		}
		return c < 20 ? 20 : c;
	}

	int textLimit = -1;

	protected String formatText(String text) {
		return formatText(text, this.text, textLimit = getTextLimit());
	}

	/**
	 * Formats xml text for styled text widget with given maximum symbols in line.
	 * @param text
	 * @param styledText
	 * @param max
	 * @return
	 */
	public static String formatText(String text, StyledText styledText, int max) {
		GC gc = new GC(styledText);
		gc.setFont(styledText.getFont());
		StringBuilder sb = new StringBuilder();
		boolean inQuota = false;
		boolean inTag = false;
		int offset = 0;
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if(ch == '<' && !inQuota) {
				int n = lookUp(text, i, inTag, false);
				int w = gc.stringExtent(sb.substring(offset, sb.length()) + text.substring(i, n)).x;
				if(w > max) {
					sb.append("\n");
					offset = sb.length();
				}
			}
			sb.append(ch);
			if(inTag && ch == '"') {
				inQuota = !inQuota;
			} else if(ch == '<' && !inQuota) {
				inTag = true;
			} else if(ch == '>' && !inQuota) {
				inTag = false;
			} else if(ch == '\n') {
				offset = sb.length();
			}
			if(sb.length() > offset /*&& !inQuota*/ && (ch == ' ' || ch == '>')) {
				int l = lookUp(text, i + 1, inTag, inQuota);
				int w = gc.stringExtent(sb.substring(offset, sb.length()) + text.substring(i, l)).x;
				if(l > i + 1 && w > max) {
					sb.append("\n");
					offset = sb.length();
					if (inTag) {
						String indent = "        ";
						if(inQuota) indent += "    ";
						sb.append(indent);
					}
					
				}
			}
		}
		gc.dispose();
		return sb.toString();
	}

	protected static int lookUp(String text, int pos, boolean inTag, boolean inQuota) {
		int res = pos;
		for (; res < text.length(); res++) {
			char ch = text.charAt(res);
			if(ch == '\n') return res;
			if(ch == '"' && inTag) {
				inQuota = !inQuota;
			}
			if(!inQuota) {
				if(ch == ' ' || (res > pos && ch == '<')) return res;
				if(ch == '>') return res + 1;
			} else {
				if(ch == ' ' && res > pos + 1) return res;
			}
		}
		return res;
	}

	@Override
	public void dispose() {
		if(sourceFile != null) {
			sourceFile.delete();
			sourceFile = null;
		}
		Display.getDefault().removeFilter(SWT.FocusOut, focusReturn);
		Display.getDefault().removeFilter(SWT.MouseDown, focusReturn);
		Display.getDefault().removeFilter(SWT.KeyDown, focusReturn);
		Display.getDefault().removeFilter(SWT.Modify, focusReturn);
//		valueColor.dispose();
//		tagColor.dispose();
//		attrColor.dispose();
		super.dispose();
	}

	private void resetText() {
		String text = formatText(getWizard().getTextForTextView());
		this.text.setStyleRanges(new StyleRange[0]);
		this.text.setText(text);
		this.text.setStyleRanges(getRanges(text));
		this.text.update();
		this.text.layout();
	}

	static Color valueColor = new Color(null, 42, 0, 255);
	static Color tagColor = new Color(null, 63, 127, 127);
	static Color attrColor = new Color(null, 127, 0, 127);
	static Color commentColor = new Color(null, 63, 127, 127);
	static Color keywordColor = new Color(null, 127, 0, 85);

	static String SCRIPT_OPEN = "<script";
	static String SCRIPT_CLOSE = "</script>";

	/**
	 * Creates elementary coloring of xml.
	 * @param text
	 * @return
	 */
	public static StyleRange[] getRanges(String text) {
		ArrayList<StyleRange> regionList = new ArrayList<StyleRange>();
		char quota = '\0';
		boolean inTag = false;
		boolean scriptDetected = false;
		int offset = 0;
		StringBuilder name = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if(inTag && (quota != '\0' && quota == ch)) {
				quota = '\0'; 
				addRange(offset, 1, valueColor, false, regionList);
				if(i - offset > 1) {
					addRange(offset + 1, i - offset - 1, valueColor, true, regionList);
				}
				addRange(i, 1, valueColor, false, regionList);
			} else if(inTag && quota == '\0' && (ch == '"' || ch == '\'')) {
				quota = ch;
				offset = i;
			} else if(ch == '>' && quota == '\0') {
				if(inTag && name.toString().equalsIgnoreCase(SCRIPT_OPEN)) {
					scriptDetected = true;
				}
				if(scriptDetected && text.charAt(i - 1) == '/') {
					scriptDetected = false;
				}
				inTag = false;
				if(name.length() > 0) {
					addRange(offset, name.length(), tagColor, false, regionList);
					name.setLength(0);
				}
				addRange(i, 1, tagColor, false, regionList);
				if(scriptDetected) {
					int j = text.toLowerCase().indexOf(SCRIPT_CLOSE, i + 1);
					if(j < 0) j = text.length();
					addRangesInJavaScript(text, i + 1, j, regionList);
					if(j < text.length()) {
						addRange(j, SCRIPT_CLOSE.length(), tagColor, false, regionList);
						i = j + SCRIPT_CLOSE.length() - 1;
					} else {
						i = text.length();
					}
					scriptDetected = false;
				}
			} else if(ch == '<' && quota == '\0') {
				inTag = true;
				name.setLength(0);
				name.append(ch);
				offset = i;
			} else if(quota == '\0' && inTag && (Character.isLetterOrDigit(ch) || ch == '-' || ch == '/')) {
				if(name.length() == 0) {
					offset = i;
				}
				name.append(ch);
			} else if(name.length() > 0) {
				Color c = name.charAt(0) == '<' ? tagColor : attrColor;
				addRange(offset, name.length(), c, false, regionList);
				if(inTag && name.toString().equalsIgnoreCase(SCRIPT_OPEN)) {
					scriptDetected = true;
				}
				name.setLength(0);
			}

		}
		return (StyleRange[])regionList.toArray(new StyleRange[0]);
	}

	static Set<String> keywords = new HashSet<String>();
	static {
		String _keywords = "abstract boolean break byte case catch char class const continue debugger default delete do double else enum export extends false final finally float for function goto if implements import in instanceof int interface long native new null package private protected public return short static super switch synchronized this throw throws transient true try typeof var void volatile while with ";
		for (String keyword: _keywords.split(" ")) {
			keywords.add(keyword);
		}
	}

	static void addRangesInJavaScript(String text, int startOffset, int endOffset, ArrayList<StyleRange> regionList) {
		boolean inLineComments = false;
		boolean inBlockComments = false;
		char quota = '\0';
		boolean word = false;
		StringBuilder name = new StringBuilder();
		int offset = -1;
		for (int i = startOffset; i < endOffset; i++) {
			char ch = text.charAt(i);
			if(inBlockComments) {
				if(ch == '*' && i + 1 < endOffset && text.charAt(i + 1) == '/') {
					addRange(offset, i + 2 - offset, commentColor, false, regionList);
					inBlockComments = false;
					i++;
				}
			} else if(inLineComments) {
				if(ch == '\n' || ch == '\r') {
					addRange(offset, i - offset, commentColor, false, regionList);
					inLineComments = false;
				}
			} else if(quota != '\0') {
				if(ch == quota) {
					quota = '\0';
					addRange(offset, i + 1 - offset, valueColor, false, regionList);
				}
			} else if(ch == '\'' || ch =='"') {
				quota = ch;
				offset = i;
			} else {
				if(Character.isLetterOrDigit(ch) || ch == '_') {
					if(word) {
						name.append(ch);
					} else if(Character.isJavaIdentifierStart(ch)) {
						word = true;
						name.append(ch);
						offset = i;
					}
				} else {
					if(word && name.length() > 0) {
						if(keywords.contains(name.toString())) {
							addRange(offset, name.length(), keywordColor, false, true, regionList);
						}
						name.setLength(0);
					}
					word = false;
				}
				if(ch == '/' && i + 1 < endOffset) {
					char ch1 = text.charAt(i + 1);
					if(ch1 == '/') {
						inLineComments = true;
						offset = i;
					} else if(ch1 == '*') {
						inBlockComments = true;
						offset = i;
						i++;
					}
				}
			}
		}
	}

	static void addRange(int offset, int length, Color c, boolean italic, ArrayList<StyleRange> regionList) {
		addRange(offset, length, c, italic, false, regionList);
	}

	static void addRange(int offset, int length, Color c, boolean italic, boolean bold, ArrayList<StyleRange> regionList) {
		StyleRange region = new StyleRange(offset,length, c, null);
		if(italic) region.fontStyle = SWT.ITALIC;
		else if(bold) region.fontStyle = SWT.BOLD;
		regionList.add(region);
	}
}
