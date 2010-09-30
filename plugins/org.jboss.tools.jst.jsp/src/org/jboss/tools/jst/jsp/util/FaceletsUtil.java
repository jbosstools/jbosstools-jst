package org.jboss.tools.jst.jsp.util;

import java.util.List;

import org.jboss.tools.jst.web.tld.TaglibData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FaceletsUtil {
	
	public static final String TAG_COMPOSITION = "composition"; //$NON-NLS-1$
	public static final String TAG_COMPONENT = "component"; //$NON-NLS-1$
	public static final String FACELETS_URI = "http://java.sun.com/jsf/facelets"; //$NON-NLS-1$
	
	public static Element findComponentElement(Element root) {
		if(root==null) {
			return null;
		}
		NodeList children = root.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				Element trimmedElement = findComponentElement((Element) child);
				if (trimmedElement != null)
					return trimmedElement;
			}
		}
		if (TAG_COMPOSITION.equalsIgnoreCase(root.getLocalName())
				|| TAG_COMPONENT.equalsIgnoreCase(root.getLocalName())) {
			return root;
		}
		return null;
	}
	
	public static boolean isFacelet(Node sourceNode, List<TaglibData> taglibs) {
		boolean isFacelet = false;
		String sourcePrefix = sourceNode.getPrefix();
		TaglibData sourceNodeTaglib = XmlUtil.getTaglibForPrefix(sourcePrefix, taglibs);
		if (null != sourceNodeTaglib) {
			String sourceNodeUri = sourceNodeTaglib.getUri();
			if (FACELETS_URI.equalsIgnoreCase(sourceNodeUri)) {
				isFacelet = true;
			}
		}
		return isFacelet;
	}
}
