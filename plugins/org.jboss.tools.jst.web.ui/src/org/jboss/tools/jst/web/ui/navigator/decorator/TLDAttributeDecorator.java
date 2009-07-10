package org.jboss.tools.jst.web.ui.navigator.decorator;

import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.ui.navigator.decorator.ICustomVariable;

public class TLDAttributeDecorator implements ICustomVariable {

	@SuppressWarnings("nls")
	public String getLabelPart(XModelObject object, String parameters) {
		if(object == null) return "";
		String required = object.getAttributeValue("required");
		if(required == null || parameters == null) return "";
		if("true".equals(required) || "yes".equals(required)) {
			return "" + parameters;
		}
		return "";
	}

}
