package org.jboss.tools.jst.jsp.outline;

import java.util.Map;

import org.jboss.tools.jst.web.kb.KbQuery;

public interface ICategoryProvider {
	public void init(KbQuery query);
	public String getCategory(String attributeName);
	public boolean isExpert(String category);
	public void fillAttributeWeights(Map<String, Integer> weights);
}
