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
package org.jboss.tools.jst.web.html;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public interface HTMLConstants {
	public String DOCTYPE = "<!DOCTYPE html>";

	public String HTML_CATEGORY = "HTML";	
	
	public String TAG_HTML = "html";

	public String TAG_HEAD = "head";
	public String TAG_TITLE = "title";
	public String TAG_SCRIPT = "script";
	public String TAG_LINK = "link";

	public String TAG_A = "a";
	public String TAG_AREA = "area";
	public String TAG_AUDIO = "audio";
	public String TAG_BASE = "base";
	public String TAG_BDO = "bdo";
	public String TAG_BLOCKQUOTE = "blockquote";
	public String TAG_BODY = "body";
	public String TAG_BUTTON = "button";
	public String TAG_CANVAS = "canvas";
	public String TAG_COMMAND = "command";
	public String TAG_DEL = "del";
	public String TAG_DETAILS = "details";
	public String TAG_DIALOG = "dialog";
	public String TAG_DIV = "div";
	public String TAG_EMBED = "embed";
	public String TAG_FIELDSET = "fieldset";
	public String TAG_FORM = "form";
	public String TAG_H4 = "h4";
	public String TAG_H6 = "h6";
	public String TAG_IFRAME = "iframe";
	public String TAG_IMG = "img";
	public String TAG_INPUT = "input";
	public String TAG_INS = "ins";
	public String TAG_KEYGEN = "keygen";
	public String TAG_LABEL = "label";
	public String TAG_LEGEND = "legend";
	public String TAG_LI = "li";
	public String TAG_MAP = "map";
	public String TAG_MENU = "menu";
	public String TAG_META = "meta";
	public String TAG_METER = "meter";
	public String TAG_OBJECT = "object";
	public String TAG_OL = "ol";
	public String TAG_OPTGROUP = "optgroup";
	public String TAG_OPTION = "option";
	public String TAG_OUTPUT = "output";
	public String TAG_P = "p";
	public String TAG_PARAM = "param";
	public String TAG_PROGRESS = "progress";
	public String TAG_Q = "q";
	public String TAG_SELECT = "select";
	public String TAG_SOURCE = "source";
	public String TAG_STYLE = "style";
	public String TAG_TABLE = "table";
	public String TAG_TBODY = "tbody";
	public String TAG_TEXTAREA = "textarea";
	public String TAG_THEAD = "thead";
	public String TAG_TD = "td";
	public String TAG_TH = "th";
	public String TAG_TIME = "time";
	public String TAG_TR = "tr";
	public String TAG_TRACK = "track";
	public String TAG_UL = "ul";
	public String TAG_VIDEO = "video";

	public String ATTR_ACTION = "action";
	public String ATTR_ALT = "alt";
	public String ATTR_ASYNC = "async";
	public String ATTR_AUTOPLAY = "autoplay";
	public String ATTR_BORDER = "border";
	public String ATTR_CHALLENGE = "challenge";
	public String ATTR_CHARSET = "charset";
	public String CHECKED = "checked";
	public String ATTR_CLASS = "class";
	public String ATTR_CITE = "cite";
	public String ATTR_COLS = "cols";
	public String ATTR_COLSPAN = "colspan";
	public String ATTR_CONTENT = "content";
	public String ATTR_CONTROLS = "controls";
	public String ATTR_COORDS = "coords";
	public String ATTR_CROSSORIGIN = "crossorigin";
	public String ATTR_DATETIME = "datetime";
	public String ATTR_DEFAULT = "default";
	public String ATTR_DEFER = "defer";
	public String ATTR_DISABLED = "disabled";
	public String ATTR_DIR = "dir";
	public String ATTR_DOWNLOAD = "download";
	public String ATTR_FOR = "for";
	public String ATTR_FORM = "form";
	public String ATTR_HEADERS = "headers";
	public String ATTR_HEIGHT = "height";
	public String ATTR_HIGH = "high";
	public String ATTR_HREF = "href";
	public String ATTR_HTTP_EQUIV = "http-equiv";
	public String ATTR_ICON = "icon";
	public String ATTR_ID = "id";
	public String ATTR_ISMAP = "ismap";
	public String ATTR_KEYTYPE = "keytype";
	public String ATTR_KIND = "kind";
	public String ATTR_LABEL = "label";
	public String ATTR_LOOP = "loop";
	public String ATTR_LOW = "low";
	public String ATTR_MANIFEST = "manifest";
	public String ATTR_MAX = "max";
	public String ATTR_METHOD = "method";
	public String ATTR_MIN = "min";
	public String ATTR_MULTIPLE = "multiple";
	public String ATTR_MUTED = "muted";
	public String ATTR_NAME = "name";
	public String ATTR_OPEN = "open";
	public String ATTR_OPTIMUM = "optimum";
	public String ATTR_POSTER = "poster";
	public String ATTR_PRELOAD = "preload";
	public String ATTR_RADIOGROUP = "radiogroup";
	public String ATTR_READONLY = "readonly";
	public String ATTR_REL = "rel";
	public String ATTR_REVERSED = "reversed";
	public String ATTR_ROWS = "rows";
	public String ATTR_ROWSPAN = "rowspan";
	public String ATTR_SANDBOX = "sandbox";
	public String ATTR_SCOPE = "scope";
	public String ATTR_SCOPED = "scoped";
	public String ATTR_SEAMLESS = "seamless";
	public String SELECTED = "selected";
	public String ATTR_SHAPE = "shape";
	public String ATTR_SIZE = "size";
	public String ATTR_SRC = "src";
	public String ATTR_SRCDOC = "srcdoc";
	public String ATTR_START = "start";
	public String ATTR_STEP = "step";
	public String ATTR_STYLE = "style";
	public String ATTR_TYPE = "type";
	public String ATTR_USEMAP = "usemap";
	public String ATTR_VALUE = "value";
	public String ATTR_WIDTH = "width";
	public String ATTR_WRAP = "wrap";

	public String METHOD_GET = "get";
	public String METHOD_POST = "post";

	public String CROSSORIGIN_ANONIMOUS = "anonymous";
	public String CROSSORIGIN_USE_CREDENTIALS = "use-credentials";
	
	public String AUTO = "auto";
	public String NONE = "none";
	public String METADATA = "metadata";
	
	public String VIDEO_TYPE_MP4 = "video/mp4";
	public String VIDEO_TYPE_OGG = "video/ogg";
	public String VIDEO_TYPE_WEBM = "video/webbm";

	public String AUDIO_TYPE_MPEG = "audio/mpeg";
	public String AUDIO_TYPE_OGG = "audio/ogg";

	public String BUTTON_TYPE_SUBMIT = "submit";
	public String BUTTON_TYPE_RESET = "reset";
	public String BUTTON_TYPE_BUTTON = "button";
	public String INPUT_TYPE_CHECKBOX = "checkbox";
	public String INPUT_TYPE_HIDDEN = "hidden";
	public String INPUT_TYPE_IMAGE = "image";
	public String INPUT_TYPE_RADIO = "radio";
	public String INPUT_TYPE_RANGE = "range";
	public String INPUT_TYPE_TEXT = "text";
	public String INPUT_TYPE_TEXTAREA = "textarea";
	public String INPUT_TYPE_SEARCH = "search";
	public String INPUT_TYPE_PASSWORD = "password";
	public String INPUT_TYPE_FILE = "file";
	public String INPUT_TYPE_NUMBER = "number";
	public String INPUT_TYPE_URL = "url";
	public String INPUT_TYPE_COLOR = "color";
	public String INPUT_TYPE_EMAIL = "email";
	public String INPUT_TYPE_TEL = "tel";
	public String INPUT_TYPE_DATE = "date";
	public String INPUT_TYPE_MONTH = "month";
	public String INPUT_TYPE_WEEK = "week";
	public String INPUT_TYPE_TIME = "time";
	public String INPUT_TYPE_DATETIME = "datetime";

	public String[] TEXT_TYPES = {
		INPUT_TYPE_TEXT, INPUT_TYPE_TEXTAREA, INPUT_TYPE_SEARCH, 
		INPUT_TYPE_PASSWORD, INPUT_TYPE_NUMBER,
		INPUT_TYPE_FILE, INPUT_TYPE_URL, INPUT_TYPE_EMAIL, 
		INPUT_TYPE_TEL, INPUT_TYPE_DATE, INPUT_TYPE_TIME, 
		INPUT_TYPE_DATETIME, INPUT_TYPE_MONTH, INPUT_TYPE_WEEK,
		INPUT_TYPE_COLOR
	};

	public String SOFT_WRAP = "soft";
	public String HARD_WRAP = "hard";
}
