package org.jboss.tools.jst.jsp.outline.cssdialog.cssselector;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.css.core.internal.modelhandler.CSSModelLoader;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSModel;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSStyleRule;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSStyleSheet;
import org.eclipse.wst.css.ui.StructuredTextViewerConfigurationCSS;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.StructuredTextViewerConfiguration;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSRuleContainer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.selection.CSSClassSelectionChangedEvent;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.selection.ICSSClassSelectionChangedListener;
import org.w3c.dom.css.CSSRuleList;

@SuppressWarnings("restriction")
public class CSSSelectorPreview extends Composite implements
		ICSSClassSelectionChangedListener {

	private StructuredTextViewer viewer;
	private ICSSModel model;
	private ICSSStyleSheet styleSheet;

	public CSSSelectorPreview(Composite parent) {
		super(parent, SWT.BORDER);
		setLayout(new FillLayout());
		initPreview();
	}

	private void initPreview() {
		StructuredTextViewerConfiguration baseConfiguration = new StructuredTextViewerConfigurationCSS();

		viewer = new StructuredTextViewer(this, null, null, false, SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL);
		((StructuredTextViewer) viewer).getTextWidget().setFont(
				JFaceResources.getFont("org.eclipse.wst.sse.ui.textfont")); //$NON-NLS-1$

		viewer.configure(baseConfiguration);
		CSSModelLoader cssModelLoader = new CSSModelLoader();
		IStructuredModel model = cssModelLoader.createModel();
		this.model = (ICSSModel) model;
		viewer.setDocument(model.getStructuredDocument());
		viewer.setEditable(false);
	}

	public void classSelectionChanged(final CSSClassSelectionChangedEvent event) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				styleSheet = (ICSSStyleSheet) model.getDocument();
				CSSRuleContainer[] containers = event
						.getSelectedRuleContainers();
				if (containers.length != 0) {
					clearPreview();
					for (int i = 0; i < containers.length; i++) {
						appendRuleFromContainer(containers[i]);
					}
				}
			}
		});

	}

	private void appendRuleFromContainer(CSSRuleContainer container) {
		StringBuilder builder = new StringBuilder(""); //$NON-NLS-1$
		ICSSStyleRule rule = (ICSSStyleRule) container.getRule();
		String text = rule.getCssText();
		String styleDef = text.substring(text.indexOf('{'));
		builder.append("." + container.getSelectorName() + styleDef); //$NON-NLS-1$
		styleSheet.appendRule(styleSheet.createCSSRule(builder.toString()));
	}

	private void clearPreview() {
		CSSRuleList ruleList = styleSheet.getCssRules();
		for (int i = 0; i < ruleList.getLength(); i++) {
			styleSheet.removeRule(ruleList.item(i));
		}
	}

}
