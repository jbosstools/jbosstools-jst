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
package org.jboss.tools.jst.jsp.drop.treeviewer.model;

import java.util.Properties;

/**
 * @author Igels
 */
public class ModelElement {

	public static ModelElement[] EMPTY_LIST = new ModelElement[0];

	protected ModelElement parent;
	protected String name;

	public ModelElement(ModelElement parent) {
		this(null, parent);
	}

	public ModelElement(String name, ModelElement parent) {
		this.parent = parent;
		this.name = name;
	}

	/**
	 * Returns Name of Element
	 * @return
	 */
	// i18n: names have been marked NON-NLS because they appear to be used in processing
	// if they also appear in the UI (AttributeValueLabelProvider?) it may be necessary to
	// maintain internal and visible names in parallel
	public String getName() {
		return name;
	}

	/**
	 * Sets Name of Element
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns Parent Element
	 * @return
	 */
	public ModelElement getParent() {
		return parent;
	}

	/**
	 * Sets Parent Element
	 * @param parent
	 */
	public void setParent(ModelElement parent) {
		this.parent = parent;
	}

	/**
	 * Returns full name (including parents names)
	 * @return
	 */
	protected String getFullName() {
		return name;
	}

	public static final int EQUAL_VALUES = 0;
	public static final int NOT_EQUAL_VALUES = Integer.MAX_VALUE;

	/**
	 * Returns value of this element for comparison with other value. See compareValue().
	 * @return
	 */
	protected String getComparedValue() {
		return getFullName();
	}

	/**
	 * V1 - value param
	 * V2 - value of this element
	 * V1 == V2 return 0 (For example: Bean1.P1 == Bean1.P1)
	 * V1 > V2 return +N (For example: Bean1.P1 > Bean1)
	 * V1 < V2 return -N (For example: Bean1.P1 < Bean1.P1.P2)
	 * V1 != V2 return Integer.MAX_VALUE (For example: Bean1.P1 != Bean2.P1)
	 * N - difference (numbers of characters) between V1 and V2
	 * @param value
	 * @return
	 */
	public int compareValue(String value) {
		if(value==null) {
			return NOT_EQUAL_VALUES;
		}
		if(value.endsWith("}") && value.length()>1) { //$NON-NLS-1$
			value = value.substring(0, value.length()-1);
		}
		String thisValue = getComparedValue();
		if(value.equals(thisValue)) {
			return EQUAL_VALUES;
		} else if(value.startsWith(thisValue) || thisValue.startsWith(value)) {
			return value.length() - thisValue.length();
		}
		return NOT_EQUAL_VALUES;
	}

	private static Class[] EMPTY_EQUAL_CLASSES_LIST = new Class[0];

	protected Class[] getEqualClasses() {
		return EMPTY_EQUAL_CLASSES_LIST;
	}

	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj.getClass() != this.getClass()) {
			Class[] equalClasses = getEqualClasses();
			boolean equal = false;
			for(int i=0; i<equalClasses.length; i++) {
				if(obj.getClass() == equalClasses[i]) {
					equal = true;
					break;
				}
			}
			if(!equal) {
				return false;
			}
		}
		return this.getName().equals(((ModelElement)obj).getName());
	}
	
	// override
	public String[] getActions() {
		return new String[0];
	}
	
	public void action(String name, Properties properties) {
		
	}

}