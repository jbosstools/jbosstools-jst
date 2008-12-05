/*******************************************************************************
  * Copyright (c) 2007-2008 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.jst.jsp.outline.cssdialog.events;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.ImageCombo;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.BaseTabControl;

/**
 * Defines an object which listens for ModifyEvent.
 */
public class AttributeModifyListener implements ModifyListener {

	public static final int MODIFY_SIMPLE_ATTRIBUTE_FIELD = 0;
	public static final int MODIFY_SIMPLE_ATTRIBUTE_FIELD_WITH_COMBO_EXTENSION = 1;
	public static final int MODIFY_COMBO_ATTRIBUTE_FIELD_WITH_COMBO_EXTENSION = 2;
	public static final int MODIFY_COMBO_EXTENSION_ATTRIBUTE_FIELD = 3;

	private int modifyMode;
	private BaseTabControl baseTabControl = null;
	private String attributeName = null;

	private Text dependentTextAttribute = null;
	private Combo dependentComboAttribute = null;

	/**
     * Constructor.
     *
	 * @param baseTabControl BaseTabControl composite object
     * @param attributeName CSS name of the first parameter
     * @param modifyMode this parameter indicates the way how modify action should be process
	 */
	public AttributeModifyListener(BaseTabControl baseTabControl, String attributeName, int modifyMode) {
		this.baseTabControl = baseTabControl;
		this.attributeName = attributeName;
		this.modifyMode = modifyMode;
	}

	/**
	 * Constructor.
	 *
	 * @param baseTabControl BaseTabControl composite object
	 * @param dependentComboAttribute
	 * @param attributeName CSS name of the first parameter
     * @param modifyMode this parameter indicates the way how modify action should be process
	 */
	public AttributeModifyListener(BaseTabControl baseTabControl, Combo dependentComboAttribute, String attributeName,
			int modifyMode) {
		this.baseTabControl = baseTabControl;
		this.dependentComboAttribute = dependentComboAttribute;
		this.attributeName = attributeName;
		this.modifyMode = modifyMode;
	}

	/**
	 * Constructor.
	 *
	 * @param baseTabControl BaseTabControl composite object
	 * @param dependentTextAttribute
	 * @param attributeName CSS name of the first parameter
     * @param modifyMode this parameter indicates the way how modify action should be process
	 */
	public AttributeModifyListener(BaseTabControl baseTabControl, Text dependentTextAttribute, String attributeName,
			int modifyMode) {
		this.baseTabControl = baseTabControl;
		this.dependentTextAttribute = dependentTextAttribute;
		this.attributeName = attributeName;
		this.modifyMode = modifyMode;
	}

	/**
	 * Method is used to correctly process modify event occurred on specify CSS attribute control.
     *
     * @param e ModifyEvent event object
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
	public void modifyText(ModifyEvent e) {
		String attributeValue = null;
		if (e.getSource() instanceof Text) {
			attributeValue = ((Text)e.getSource()).getText();
		} else if (e.getSource() instanceof Combo) {
			attributeValue = ((Combo)e.getSource()).getText();
		} else if (e.getSource() instanceof ImageCombo) {
			attributeValue = ((ImageCombo)e.getSource()).getText();
		}
		switch (this.modifyMode) {
			case MODIFY_SIMPLE_ATTRIBUTE_FIELD:
				modifySimpleAttribute(attributeValue);
				break;
			case MODIFY_SIMPLE_ATTRIBUTE_FIELD_WITH_COMBO_EXTENSION:
				modifySimpleAttributeWithComboExtension(attributeValue);
				break;
			case MODIFY_COMBO_ATTRIBUTE_FIELD_WITH_COMBO_EXTENSION:
				modifyComboAttributeWithComboExtension(attributeValue, (Combo)e.getSource());
				break;
			case MODIFY_COMBO_EXTENSION_ATTRIBUTE_FIELD:
				if (dependentComboAttribute != null) {
					attributeValue = dependentComboAttribute.getText();
				} else if (dependentTextAttribute != null) {
					attributeValue = dependentTextAttribute.getText();
				}
				modifyExtAttribute(attributeValue, (Combo)e.getSource());
				break;
		}
    	if (!baseTabControl.isUpdateDataFromStyleAttributes()) {
    		baseTabControl.notifyListeners();
    	}
	}

    /**
     * Method is used to correctly process modify event occurred on specify text CSS attribute control.
     */
    protected void modifySimpleAttribute(String attributeValue) {
        if (attributeValue != null && !attributeValue.trim().equals(Constants.EMPTY)) {
        	baseTabControl.getStyleAttributes().addAttribute(attributeName, adjustAttributeValue(attributeValue.trim()));
        } else {
        	baseTabControl.getStyleAttributes().removeAttribute(attributeName);
        }
    }

    /**
     * Method is used to correctly process modify event occurred on specify text CSS attribute control
     * that have combo extension control.
     */
    protected void modifySimpleAttributeWithComboExtension(String attributeValue) {
    	if (attributeValue != null && !attributeValue.trim().equals(Constants.EMPTY)) {
    		String extValue = dependentComboAttribute.getText();
    		if (extValue != null) {
    			attributeValue += extValue;
    		}
    		baseTabControl.getStyleAttributes().addAttribute(attributeName, attributeValue);
    	} else {
    		baseTabControl.getStyleAttributes().removeAttribute(attributeName);
    		dependentComboAttribute.select(0);
    	}
    }

    /**
     * Method is used to correctly process modify event occurred on specify combo CSS attribute control
     * that have combo extension control.
     */
    protected void modifyComboAttributeWithComboExtension(String attributeValue, Combo attribute) {
    	boolean found = false;
        for (String str : attribute.getItems()) {
            if (attributeValue.equals(str)) {
            	dependentComboAttribute.select(0);
            	dependentComboAttribute.setEnabled(false);
            	baseTabControl.getStyleAttributes().addAttribute(attributeName, attributeValue);
            	found = true;
            	break;
            }
        }
        if (!found) {
        	dependentComboAttribute.setEnabled(true);
        	modifySimpleAttributeWithComboExtension(attributeValue);
        }
    }

    /**
     * Method is used to correctly process modify event occurred on specify CSS extension attribute control.
     */
    protected void modifyExtAttribute(String attributeValue, Combo extAttribute) {
        if (attributeValue != null && !attributeValue.trim().equals(Constants.EMPTY)) {
            String extValue = extAttribute.getText();
            if (extValue != null) {
            	baseTabControl.getStyleAttributes().addAttribute(attributeName, attributeValue + extValue);
            }
        } else {
        	if (extAttribute.getSelectionIndex() > 0) {
        		extAttribute.select(0);
        	}
        	return;
        }
    }

    /**
     * This is wrapper method that can be override by inherited class.
     *
     * @param attribute the attribute that should be wrapped
     * @return the wrapped value passed by parameter
     */
    protected String adjustAttributeValue(String attribute) {
    	return attribute;
    }
}
