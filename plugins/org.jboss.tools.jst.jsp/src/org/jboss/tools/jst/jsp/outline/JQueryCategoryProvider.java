package org.jboss.tools.jst.jsp.outline;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jboss.tools.jst.web.html.JQueryHTMLConstants;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.KbQuery.Tag;
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileAttrProvider;

public class JQueryCategoryProvider implements ICategoryProvider, JQueryHTMLConstants {
	public static final String CATEGORY_OFTEN_USED = "Often used";
	public static final String CATEGORY_JQM = "Advanced jQuery";
	public static final String CATEGORY_OTHER = "Advanced HTML";
	
	Set<String> oftenUsed = new HashSet<String>();

	private void add(String... attrs) {
		for (String attr: attrs) oftenUsed.add(attr);
	}
	
	public void init(KbQuery query) {
		Tag[] tags = query.getParentTagsWithAttributes();
		if(tags.length == 0) {
			return;
		}
		Tag current = tags[tags.length - 1];
		String tagName = current.getName();
		add(ATTR_ID, ATTR_PLACEHOLDER, ATTR_HREF);
		if(!TAG_INPUT.equals(tagName)) {
			add(ATTR_SRC);
		}
		add(ATTR_DATA_ROLE, ATTR_DATA_ICON, ATTR_DATA_ICONPOS, ATTR_DATA_THEME);

		
		String dataRole = current.getAttributes().get(ATTR_DATA_ROLE);
		
		if(ROLE_TABLE.equals(dataRole)) {
			add(ATTR_DATA_MODE, ATTR_CLASS);
		} else if(ROLE_DIALOG.equals(dataRole)) {
			add(ATTR_DATA_CLOSE_BTN);
		} else if(ROLE_POPUP.equals(dataRole)) {
			add(ATTR_DATA_THEME, ATTR_DATA_OVERLAY_THEME, ATTR_DATA_DISMISSABLE, 
					ATTR_DATA_SHADOW, ATTR_DATA_CORNERS);
		} else if(ROLE_PANEL.equals(dataRole)) {
			add(ATTR_DATA_DISPLAY, ATTR_DATA_POSITION, ATTR_DATA_POSITION_FIXED,
					ATTR_DATA_DISMISSABLE, ATTR_DATA_SWIPE_CLOSE);
		} else if(ROLE_COLLAPSIBLE.equals(dataRole)) {
			add(ATTR_DATA_MINI, ATTR_DATA_INSET, ATTR_DATA_COLLAPSED, 
					ATTR_DATA_COLLAPSED_ICON, ATTR_DATA_EXPANDED_ICON, ATTR_DATA_ICONPOS,
					ATTR_DATA_THEME, ATTR_DATA_CONTENT_THEME);
		} else if(ROLE_COLLAPSIBLE_SET.equals(dataRole)) {
			add(ATTR_DATA_MINI, ATTR_DATA_INSET, 
					ATTR_DATA_COLLAPSED_ICON, ATTR_DATA_EXPANDED_ICON, ATTR_DATA_ICONPOS,
					ATTR_DATA_THEME, ATTR_DATA_CONTENT_THEME);
		} else if(ROLE_HEADER.equals(dataRole)) {
			add(ATTR_DATA_POSITION, ATTR_DATA_FULL_SCREEN, ATTR_DATA_THEME);
		} else if(ROLE_FOOTER.equals(dataRole)) {
			add(ATTR_DATA_POSITION, ATTR_DATA_FULL_SCREEN, ATTR_DATA_THEME);
		} else if(ROLE_FOOTER.equals(dataRole)) {
			add(ATTR_DATA_ICONPOS, ATTR_DATA_THEME);
		} else if(ROLE_LISTVIEW.equals(dataRole)) {
			add(ATTR_DATA_AUTODIVIDERS, ATTR_DATA_FILTER, ATTR_DATA_INSET,
					ATTR_DATA_THEME, ATTR_DATA_DIVIDER_THEME);
		} else if(ROLE_BUTTON.equals(dataRole)) {
			add(ATTR_DATA_REL, ATTR_CLASS, ATTR_DATA_MINI, ATTR_DATA_INLINE,
					ATTR_DATA_CORNERS, ATTR_DATA_THEME);
		} else if(ROLE_CONTROLGROUP.equals(dataRole)) {
			add(ATTR_DATA_MINI, ATTR_DATA_TYPE);
		}
		
		if(TAG_BODY.equals(tagName)) {
			add("background", "bgcolor");
		} else if(TAG_INPUT.equals(tagName)) {
			oftenUsed.remove(ATTR_DATA_ROLE);
			add(ATTR_VALUE, ATTR_TYPE);
			String type = current.getAttributes().get(ATTR_TYPE);
			if(TYPE_RANGE.equals(type)
				|| TYPE_NUMBER.equals(type)) {
				add(ATTR_DATA_MIN, ATTR_DATA_MAX, ATTR_DATA_STEP);
			} else if(TYPE_RADIO.equals(type)) {
				add(CHECKED);
			}
		} else if(TAG_IMG.equals(tagName)) {
			add(ATTR_ALT, ATTR_SRC, ATTR_HEIGHT, ATTR_WIDTH);
		} else if(TAG_VIDEO.equals(tagName)
				|| TAG_AUDIO.equals(tagName)) {
			add(ATTR_SRC, ATTR_HEIGHT, ATTR_WIDTH, ATTR_CONTROLS, ATTR_AUTOPLAY);
			add(ATTR_POSTER, ATTR_LOOP, ATTR_MUTED, ATTR_PRELOAD);
		} else if(TAG_OPTION.equals(tagName)) {
			add(ATTR_VALUE, SELECTED);
		} else if(TAG_LABEL.equals(tagName)) {
			add(ATTR_FOR, ATTR_CLASS);
		} else if(TAG_FORM.equals(tagName)) {
			add(ATTR_AUTOCOMPLETE, ATTR_NOVALIDATE, ATTR_ACTION, ATTR_METHOD, ATTR_NAME);
		} else if(TAG_SELECT.equals(tagName)) {
			add(ATTR_DATA_INLINE, ATTR_DATA_MINI, ATTR_DATA_CORNERS);
		} else if(TAG_A.equals(tagName)) {
			add(ATTR_DATA_REL);
			if(dataRole == null) {
				add(ATTR_DATA_TRANSITION);
			}
		}
		
	}

	public String getCategory(String attributeName) {
		if(oftenUsed.contains(attributeName)) {
			return CATEGORY_OFTEN_USED;
		}
		if(JQueryMobileAttrProvider.getAllAttributes().contains(attributeName)) {
			return CATEGORY_JQM;
		}
		return CATEGORY_OTHER;
	}
	
	public boolean isExpert(String category) {
		return !CATEGORY_OFTEN_USED.equals(category);
	}

	public void fillAttributeWeights(Map<String, Integer> weights) {
		weights.put(CATEGORY_OFTEN_USED, 10);
		weights.put(CATEGORY_JQM, 9);

		addWeight(weights, ATTR_ID, 10);
		addWeight(weights, ATTR_DATA_ROLE, 9);
		addWeight(weights, ATTR_HREF, 8);
		addWeight(weights, ATTR_TYPE, 8);
		addWeight(weights, ATTR_DATA_ICON, 7);
		addWeight(weights, ATTR_DATA_ICONPOS, 7);
	}

	void addWeight(Map<String, Integer> weights, String attr, int w) {
		if(oftenUsed.contains(attr)) {
			weights.put(attr, w);
		}
	}
	
}
