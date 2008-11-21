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
package org.jboss.tools.jst.jsp.outline.cssdialog.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.css.core.internal.format.FormatProcessorCSS;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSDocument;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSModel;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

/**
 * CSS class model.
 */
public class CSSModel {

    private static String startBraces = "{"; //$NON-NLS-1$
    private static String endBraces = "}"; //$NON-NLS-1$
    private FormatProcessorCSS formatProcessorCSS = null;
    private CSSStyleSheet styleSheet = null;
    private IStructuredModel model = null;
    private IFile styleFile = null;

    /**
     * Constructor.
     *
     * @param styleFile CSS style class that should initialize CSS model
     */
    public CSSModel(IFile styleFile) {
        try {
        	this.styleFile = styleFile;

        	formatProcessorCSS = new FormatProcessorCSS();
            IModelManager modelManager = StructuredModelManager.getModelManager();
            model = modelManager.getModelForEdit(styleFile);
            if (model instanceof ICSSModel) {
                ICSSModel cssModel = (ICSSModel) model;
                ICSSDocument document = cssModel.getDocument();

                if (document instanceof CSSStyleSheet) {
                    styleSheet = (CSSStyleSheet) document;
                }
            }
        } catch (IOException e) {
            JspEditorPlugin.getPluginLog().logError(e.getMessage());
        } catch (CoreException e) {
            JspEditorPlugin.getPluginLog().logError(e.getMessage());
        }
    }

    /**
     * Get selectors
     *
     * @return List<String>
     */
    public List<Selector> getSelectors() {
        List<Selector> selectors = new ArrayList<Selector>();

        if (styleSheet != null) {
            CSSRuleList list = styleSheet.getCssRules();

            if (list != null) {
                for (int i = 0; i < list.getLength(); i++) {
                    if (list.item(i) instanceof CSSStyleRule) {
                    	CSSStyleRule rule = ((CSSStyleRule) list.item(i));
                    	Selector selector = new Selector(rule.toString(), rule.getSelectorText());
//                        selectors.add(((CSSStyleRule) list.item(i)).getSelectorText());
                    	selectors.add(selector);
                    }
                }
            }
        }

        return selectors;
    }

    /**
     * 
     * @param selector
     * @return
     */
    public String getCSSText(String selector) {
        if (styleSheet != null) {
            CSSRuleList list = styleSheet.getCssRules();

            if (list != null) {
                for (int i = 0; i < list.getLength(); i++) {
                    if (list.item(i) instanceof CSSStyleRule &&
                            ((CSSStyleRule) list.item(i)).getSelectorText().equals(selector)) {
                        return ((CSSStyleRule) list.item(i)).getCssText();
                    }
                }
            }
        }

        return null;
    }

    /**
     * Get style by selectorName
     *
     * @param selectorName
     * @return style
     */
    public String getStyle(String selectorName) {
        if (styleSheet != null) {
            CSSRuleList list = styleSheet.getCssRules();

            if (list != null) {
                for (int i = 0; i < list.getLength(); i++) {
                    if (list.item(i) instanceof CSSStyleRule &&
                            ((CSSStyleRule) list.item(i)).getSelectorText().equals(selectorName)) {
                        return ((CSSStyleRule) list.item(i)).getStyle().getCssText();
                    }
                }
            }
        }

        return null;
    }

    /**
     *
     * @param selector
     * @param style
     */
    public void setCSS(String selector, String style) {
        if (styleSheet != null) {
        	CSSRuleList list = styleSheet.getCssRules();

            if (list != null) {
            	// check if selector passed by parameter already exists in CSS
                for (int i = 0; i < list.getLength(); i++) {
                    if (list.item(i) instanceof CSSStyleRule &&
                            ((CSSStyleRule) list.item(i)).getSelectorText().equals(selector)) {
                        CSSStyleRule rule = (CSSStyleRule) list.item(i);
                        CSSStyleDeclaration declaration = rule.getStyle();
                        declaration.setCssText(style);

                        return;
                    }
                }
                // insert NEW selector to style sheet
                styleSheet.insertRule(selector + startBraces + style + endBraces, list.getLength());
                formatProcessorCSS.formatModel(model);
            }
        }
    }

    /**
     * Save model to file
     */
    public void releaseModel() {
    	model.releaseFromEdit();
    }

    /**
     * Save model to file
     */
    public void saveModel() {
//        ICSSModel cssModel = (ICSSModel) model;
//        ICSSDocument document = cssModel.getDocument();

//        if (document instanceof CSSStyleSheet) {
//            styleSheet = (CSSStyleSheet) document;
//        }
//        formatProcessorCSS.formatModel(model);
        try {
            formatProcessorCSS.formatFile(styleFile);
            model.save();
        } catch (IOException e) {
            JspEditorPlugin.getPluginLog().logError(e.getMessage());
        } catch (CoreException e) {
            JspEditorPlugin.getPluginLog().logError(e.getMessage());
        }
    }
}
