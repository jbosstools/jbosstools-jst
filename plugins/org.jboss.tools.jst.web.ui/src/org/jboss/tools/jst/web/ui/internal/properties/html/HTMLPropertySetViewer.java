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
package org.jboss.tools.jst.web.ui.internal.properties.html;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.common.ui.widget.editor.SwtFieldEditorFactory;
import org.jboss.tools.jst.web.html.JQueryHTMLConstants;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.AbstractAdvancedPropertySetViewer;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.IFieldEditorProvider;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.WizardDescriptions;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class HTMLPropertySetViewer extends AbstractAdvancedPropertySetViewer implements JQueryHTMLConstants {
	HTMLPropertySetContext context = new HTMLPropertySetContext(this);
	HTMLLayouts layouts = new HTMLLayouts(this);

	public HTMLPropertySetViewer() {}

	@Override
	public String getCategoryDisplayName() {
		return "HTML";
	}

	@Override
	protected boolean isStructureChanged(List<IPropertyDescriptor> descriptors) {
		return context.update();
	}

	@Override
	protected List<IPropertyDescriptor> getFilteredDescriptors(List<IPropertyDescriptor> descriptors) {
		List<IPropertyDescriptor> result = new ArrayList<IPropertyDescriptor>();
		selectDescriptor(ATTR_ID, result);
		if(context.isTag(TAG_SCRIPT)) {
			IPropertyDescriptor d = getModel().getDescriptor(ATTR_SRC);
			if(d != null && !isPropertySet(d)) {
				selectDescriptor(ATTR_SRC, result);
				selectDescriptor(ATTR_TYPE, result);
			} else {
				selectDescriptors(PreferredHTMLAttributes.getAttributesByTag(context.getElementName()), result);
			}
		} else if(PreferredHTMLAttributes.getAttributesByTag(context.getElementName()) != null) {
			String[] attrs = PreferredHTMLAttributes.getAttributesByTag(context.getElementName());
			selectDescriptors(attrs, result);
		} else if(context.isInput() && INPUT_TYPE_HIDDEN.equals(context.getTypeName())) {
			selectDescriptors(PreferredHTMLAttributes.HIDDEN_INPUT_ATTRS, result);
		} else if(context.isInput()) {
			selectDescriptors(PreferredHTMLAttributes.COMMON_INPUT_ATTRS, result);
			if(context.isNumberType()) {
				selectDescriptors(PreferredHTMLAttributes.NUMBER_INPUT_ATTRS, result);
			} else if(context.isRangeType()) {
				selectDescriptors(PreferredHTMLAttributes.RANGE_INPUT_ATTRS, result);
			} else if(context.isTextType()) {
				if(context.isPatternTextType()) {
					selectDescriptor(ATTR_PATTERN, result);
				}
				selectDescriptors(PreferredHTMLAttributes.TEXT_INPUT_ATTRS, result);
				if(context.isFileType()) {
					selectDescriptor(ATTR_MULTIPLE, result);
				}
			} else if(context.isButtonType()) {
				//
			} else if(context.isCheckbox() || context.isRadio()) {
				selectDescriptor(CHECKED, result);
			}
			
		} else if(context.isTag(TAG_TEXTAREA)) {
			selectDescriptors(PreferredHTMLAttributes.COMMON_INPUT_ATTRS, result);
			selectDescriptors(PreferredHTMLAttributes.TEXT_INPUT_ATTRS, result);
			selectDescriptors(PreferredHTMLAttributes.TEXT_AREA_ATTRS, result);
		}
		return result;
	}

	@Override
	protected void layoutEditors(Composite fields, List<Entry> entries) {
		if(context.isTag(TAG_IMG)) {
			layouts.layoutImage(fields, entries);
		} else if(context.isTag(TAG_TEXTAREA)) {
			layouts.layoutTextArea(fields, entries);
		} else if(context.isTextType() || context.isNumberType() || context.isRangeType()) {
			layouts.layoutInputText(fields, entries);
		} else if(context.isCheckbox() || context.isRadio()) {
			layouts.layoutInputCheckbox(fields, entries);
		} else if(context.isTag(TAG_SELECT)) {
			layouts.layoutSelect(fields, entries);
		}
		for (Entry e: entries) {
			if(!e.isLayout()) {
				layoutEditor(e, fields, true);
			}
		}
	}

	static Set<String> BOOLEAN_ATTRS = new HashSet<String>();
	static {
		String[] attrs = {ATTR_ASYNC, ATTR_AUTOFOCUS, ATTR_AUTOPLAY, 
				ATTR_CHALLENGE, CHECKED, ATTR_CONTROLS, ATTR_DEFAULT, 
				ATTR_DEFER, ATTR_DISABLED, ATTR_ISMAP, ATTR_LOOP,
				ATTR_MULTIPLE, ATTR_MUTED, ATTR_NOVALIDATE, ATTR_OPEN, 
				ATTR_READONLY, ATTR_REVERSED, ATTR_SCOPED, SELECTED, ATTR_SEAMLESS};
		for (String a: attrs) {
			BOOLEAN_ATTRS.add(a);
		}
	}

	/**
	 * Returns true if the html attribute can be used without value or with
	 * any value; this is not the case of true/false attribute.
	 * @param id
	 * @return
	 */
	private boolean isBooleanAttribute(Object id) {
		return BOOLEAN_ATTRS.contains(id);
	}

	@Override
	protected String toVisualValue(String modelValue, IPropertyDescriptor d) {
		if(isBooleanAttribute(d.getId())) {
			return (isPropertySet(d)) ? TRUE : FALSE;
		} else if(ATTR_AUTOCOMPLETE.equals(d.getId())) {
			return "off".equals(modelValue) ? FALSE : TRUE;
		}
		return modelValue;
	}

	@Override
	protected Object toModelValue(Object visualValue, IPropertyDescriptor d) {
		if(isBooleanAttribute(d.getId())) {
			return (visualValue != null && TRUE.equals(visualValue.toString())) ? d.getId() : null;
		} else if(ATTR_AUTOCOMPLETE.equals(d.getId())) {
			return (visualValue != null && FALSE.equals(visualValue.toString())) ? "off" : "on";
		}
		return visualValue;
	}

	@Override
	protected void initEditorProviders() {
		editorProviders.put(ATTR_ACTION, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createFormActionEditor();
			}
		});		
		
		editorProviders.put(ATTR_ALT, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createAltEditor();
			}
		});
		
		editorProviders.put(ATTR_ASYNC, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createAsyncEditor();
			}
		});
		
		editorProviders.put(ATTR_AUTOFOCUS, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createAutofocusEditor();
			}
		});		
		
		editorProviders.put(ATTR_AUTOPLAY, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				String description = (context.isTag(TAG_AUDIO)) ? WizardDescriptions.audioAutoplay : WizardDescriptions.videoAutoplay;
				return JQueryFieldEditorFactory.createAutoplayEditor(description);
			}
		});		
		
		editorProviders.put(ATTR_AUTOCOMPLETE, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createAutocompleteEditor();
			}
		});		
		
		editorProviders.put(ATTR_BORDER, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createBorderEditor();
			}
		});

		editorProviders.put(ATTR_CHALLENGE, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createChallengeEditor();
			}
		});		
		
		editorProviders.put(ATTR_CHARSET, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createCharsetEditor();
			}
		});		
		
		editorProviders.put(CHECKED, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				String description = WizardDescriptions.checkboxIsSelected;
				return JQueryFieldEditorFactory.createCheckedEditor(description);
			}
		});		
		
		editorProviders.put(ATTR_CITE, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				String description = context.isTag(TAG_DEL) ? WizardDescriptions.delCite
						: context.isTag(TAG_INS) ? WizardDescriptions.insCite
						: WizardDescriptions.blockquoteCite;
				return HTMLFieldEditorFactory.createCiteEditor(description);
			}
		});		
		
		editorProviders.put(ATTR_COLS, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createColsEditor();
			}
		});		
		
		editorProviders.put(ATTR_COLSPAN, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createColspanEditor();
			}
		});		
		
		editorProviders.put(ATTR_CONTENT, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createContentEditor();
			}
		});		
		
		editorProviders.put(ATTR_CONTROLS, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				String description = (context.isTag(TAG_AUDIO)) ? WizardDescriptions.audioControls : WizardDescriptions.videoControls;
				return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_CONTROLS, WizardMessages.controlsLabel, false,
						description);
			}
		});		
		
		editorProviders.put(ATTR_COORDS, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createCoordsEditor();
			}
		});		
		
		editorProviders.put(ATTR_CROSSORIGIN, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createCrossoriginEditor();
			}
		});		
		
		editorProviders.put(ATTR_DATETIME, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				String description = context.isTag(TAG_DEL) ? WizardDescriptions.delDatetime
						: WizardDescriptions.insDatetime;
				return HTMLFieldEditorFactory.createDatetimeEditor(description);
			}
		});		
		
		editorProviders.put(ATTR_DEFAULT, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createDefaultEditor();
			}
		});		
		
		editorProviders.put(ATTR_DEFER, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createDeferEditor();
			}
		});
		
		editorProviders.put(ATTR_DIR, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createDirEditor();
			}
		});		
		
		editorProviders.put(ATTR_DISABLED, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createDisabledEditor();
			}
		});		
		
		editorProviders.put(ATTR_DOWNLOAD, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createDownloadEditor();
			}
		});		
		
		editorProviders.put(ATTR_FOR, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createForEditor();
			}
		});
		
		editorProviders.put(ATTR_FORM, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createFormEditor();
			}
		});
		
		editorProviders.put(ATTR_HEADERS, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createHeadersEditor();
			}
		});		
		
		editorProviders.put(ATTR_HEIGHT, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createHeightEditor(WizardDescriptions.imageHeight);
			}
		});

		editorProviders.put(ATTR_HIGH, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createHighEditor();
			}
		});		
		
		editorProviders.put(ATTR_HREF, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createURLEditor();
			}
		});		
		
		editorProviders.put(ATTR_HTTP_EQUIV, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createHttpEquiveEditor();
			}
		});

		editorProviders.put(ATTR_ICON, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createIconEditor(getContextFile());
			}
		});

		editorProviders.put(ATTR_ID, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createIDEditor();
			}
		});

		editorProviders.put(ATTR_ISMAP, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createIsmapEditor();
			}
		});

		editorProviders.put(ATTR_KEYTYPE, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createKeytypeEditor();
			}
		});

		editorProviders.put(ATTR_KIND, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createKindEditor();
			}
		});		
		
		editorProviders.put(ATTR_LABEL, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createLabelEditor(ATTR_LABEL);
			}
		});

		editorProviders.put(ATTR_LOOP, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				String description = (context.isTag(TAG_AUDIO)) ? WizardDescriptions.audioLoop : WizardDescriptions.videoLoop;
				return JQueryFieldEditorFactory.createLoopEditor(description);
			}
		});

		editorProviders.put(ATTR_LOW, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createLowEditor();
			}
		});

		editorProviders.put(ATTR_MANIFEST, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createManifestEditor();
			}
		});

		editorProviders.put(ATTR_MAX, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				String description = context.isTag(TAG_METER) ? WizardDescriptions.meterMax
						: context.isTag(TAG_PROGRESS) ? WizardDescriptions.progressMax
						: context.isNumberType() ? WizardDescriptions.textInputMax : WizardDescriptions.rangeSliderMax;
				return HTMLFieldEditorFactory.createMaxEditor(description);
			}
		});

		editorProviders.put(ATTR_MAXLENGTH, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createMaxlengthEditor();
			}
		});

		editorProviders.put(ATTR_METHOD, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createFormMethodEditor();
			}
		});

		editorProviders.put(ATTR_MIN, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				String description = context.isTag(TAG_METER) ? WizardDescriptions.meterMin
						: context.isNumberType() ? WizardDescriptions.textInputMin : WizardDescriptions.rangeSliderMin;
				return HTMLFieldEditorFactory.createMinEditor(description);
			}
		});

		editorProviders.put(ATTR_MULTIPLE, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createMultipleEditor();
			}
		});

		editorProviders.put(ATTR_MUTED, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				String description = (context.isTag(TAG_AUDIO)) ? WizardDescriptions.audioMuted : WizardDescriptions.videoMuted;
				return JQueryFieldEditorFactory.createMutedEditor(description);
			}
		});

		editorProviders.put(ATTR_NAME, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				if(context.isTag(TAG_META)) {
					return HTMLFieldEditorFactory.createMetaNameEditor();
				}
				return JQueryFieldEditorFactory.createNameEditor();
			}
		});

		editorProviders.put(ATTR_NOVALIDATE, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createNovalidateEditor();
			}
		});

		editorProviders.put(ATTR_OPEN, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				String description = (context.isTag(TAG_DETAILS)) ? WizardDescriptions.detailsOpen
						: WizardDescriptions.dialogOpen;
				return HTMLFieldEditorFactory.createOpenEditor(description);
			}
		});

		editorProviders.put(ATTR_OPTIMUM, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createOptimumEditor();
			}
		});

		editorProviders.put(ATTR_PATTERN, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createPatternEditor();
			}
		});

		editorProviders.put(ATTR_PLACEHOLDER, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createPlaceholderEditor();
			}
		});

		editorProviders.put(ATTR_POSTER, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createPosterEditor(getContextFile());
			}
		});

		editorProviders.put(ATTR_PRELOAD, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return (context.isTag(TAG_AUDIO)) ? JQueryFieldEditorFactory.createPreloadAudioEditor()
									: JQueryFieldEditorFactory.createPreloadVideoEditor();
			}
		});

		editorProviders.put(ATTR_RADIOGROUP, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createRadiogroupEditor();
			}
		});

		editorProviders.put(ATTR_READONLY, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createReadonlyEditor();
			}
		});

		editorProviders.put(ATTR_REL, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				if(context.isTag(TAG_AREA)) {
					return HTMLFieldEditorFactory.createAreaRelEditor();
				}
				return HTMLFieldEditorFactory.createLinkRelEditor();
			}
		});

		editorProviders.put(ATTR_REQUIRED, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createRequiredEditor();
			}
		});

		editorProviders.put(ATTR_REVERSED, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createReversedEditor();
			}
		});

		editorProviders.put(ATTR_ROWS, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createRowsEditor();
			}
		});

		editorProviders.put(ATTR_ROWSPAN, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createRowspanEditor();
			}
		});		
		
		editorProviders.put(ATTR_SANDBOX, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createSandboxEditor();
			}
		});

		editorProviders.put(ATTR_SCOPE, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createScopeEditor();
			}
		});

		editorProviders.put(ATTR_SCOPED, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createScopedEditor();
			}
		});

		editorProviders.put(ATTR_SEAMLESS, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createSeamlessEditor();
			}
		});

		editorProviders.put(SELECTED, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createSelectedEditor();
			}
		});

		editorProviders.put(ATTR_SHAPE, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createShapeEditor();
			}
		});

		editorProviders.put(ATTR_SRC, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				if(context.isTag(TAG_SCRIPT)) {
					//TODO
				} else if(context.isTag(TAG_AUDIO)) {
					return HTMLFieldEditorFactory.createAudioSrcEditor(getContextFile());
				} else if(context.isTag(TAG_SOURCE)) {
					return HTMLFieldEditorFactory.createSourceSrcEditor(getContextFile(), WizardDescriptions.sourceSrc);
				} else if(context.isTag(TAG_SOURCE) || context.isTag(TAG_EMBED)) {
					return HTMLFieldEditorFactory.createSourceSrcEditor(getContextFile(), WizardDescriptions.embedSrc);
				} else if(context.isTag(TAG_IMG)) {
					return HTMLFieldEditorFactory.createSrcEditor(getContextFile());
				} else if(TAG_IFRAME.equals(context.getElementName())) {
					return HTMLFieldEditorFactory.createTextSrcEditor(WizardDescriptions.iframeSrc);
				}
				return HTMLFieldEditorFactory.createTextSrcEditor("");
			}
		});

		editorProviders.put(ATTR_SIZE, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createSizeEditor();
			}
		});

		editorProviders.put(ATTR_START, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createStartEditor();
			}
		});

		editorProviders.put(ATTR_STEP, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				String description = context.isNumberType() ? WizardDescriptions.textInputStep : WizardDescriptions.rangeSliderStep;
				return JQueryFieldEditorFactory.createStepEditor(description);
			}
		});

		editorProviders.put(ATTR_TYPE, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				if(context.isTag(TAG_LINK)) {
					return HTMLFieldEditorFactory.createLinkTypeEditor();
				} else if(context.isTag(TAG_SCRIPT)) {
					return HTMLFieldEditorFactory.createScriptTypeEditor();
				} else if(context.isTag(TAG_MENU)) {
					return HTMLFieldEditorFactory.createMenuTypeEditor();
				} else if(context.isTag(TAG_BUTTON)) {
					IFieldEditor f = JQueryFieldEditorFactory.createFormButtonTypeEditor();
					f.setValue(""); //no default value is defined
					return f;
				} else if(context.isTag(TAG_SOURCE)) {
					return HTMLFieldEditorFactory.createSourceTypeEditor();
				} else if(context.isInput()) {
					return HTMLFieldEditorFactory.createInputTypeEditor();
				} else if(context.isTag(TAG_OL)) {
					return HTMLFieldEditorFactory.createOlTypeEditor();
				} else if(context.isTag(TAG_COMMAND)) {
					return HTMLFieldEditorFactory.createCommandTypeEditor();
				} else if(context.isTag(TAG_STYLE)) {
					return HTMLFieldEditorFactory.createStyleTypeEditor();
				}

				String description = "";
				if(context.isTag(TAG_EMBED)) {
					description = WizardDescriptions.embedType;
				}
				return HTMLFieldEditorFactory.createTextTypeEditor(description);
			}
		});

		editorProviders.put(ATTR_VALUE, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return JQueryFieldEditorFactory.createValueEditor();
			}
		});

		editorProviders.put(ATTR_USEMAP, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createUsemapEditor();
			}
		});

		editorProviders.put(ATTR_WIDTH, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createWidthEditor(WizardDescriptions.imageWidth);
			}
		});

		editorProviders.put(ATTR_WRAP, new IFieldEditorProvider() {
			public IFieldEditor createEditor() {
				return HTMLFieldEditorFactory.createWrapEditor();
			}
		});

	}
}
