package org.jboss.tools.jst.jsp.drop.treeviewer.model;

public class SeamVariableElement extends ModelElement implements IAttributeValueContainer, IAttributeValue {

	public SeamVariableElement(String name, ModelElement parent) {
		super(name, parent);
	}

	public ModelElement[] getChildren() {
		return new ModelElement[0];
	}

	/**
	 * @see IAttributeValue#getValue()
	 */
	public String getValue() {
		return "#{" + getFullName() + "}";
	}

	/**
	 * @see ModelElement#getComparedValue()
	 */
	protected String getComparedValue() {
		return "#{" + getFullName();
	}

}
