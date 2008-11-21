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
package org.jboss.tools.jst.jsp.outline.cssdialog.common;

/**
 * Selector class.
 */
public class Selector {

	private String value = null;
	private String id = null;

	/**
	 * Constructor.
	 *
	 * @param id
	 * @param value
	 */
	public Selector(String id, String value) {
		this.id = id;
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return
	 * @see java.lang.String#toString()
	 */
	public String toString() {
		return id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Selector)) {
			return false;
		}
		Selector selector = (Selector) obj;
		if (id == null || selector.getId() == null) {
			return false;
		}
		if (id.equals(selector.getId())) {
			return true;
		}
		return super.equals(obj);
	}
}