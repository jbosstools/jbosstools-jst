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
package org.jboss.tools.jst.web.kb.test;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.jst.web.kb.PageProcessor;
import org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttribute;
import org.jboss.tools.jst.web.kb.internal.taglib.html.angular.AngularAttributeProvider;
import org.jboss.tools.jst.web.kb.internal.taglib.html.angular.NameAttributeProvider;
import org.jboss.tools.jst.web.kb.internal.taglib.html.angular.NgAppAttributeProvider;
import org.jboss.tools.jst.web.kb.internal.taglib.html.angular.NgInputAttributeProvider;
import org.jboss.tools.jst.web.kb.internal.taglib.html.angular.NgInputCheckboxAttributeProvider;
import org.jboss.tools.jst.web.kb.internal.taglib.html.angular.NgInputDefaultAttributeProvider;
import org.jboss.tools.jst.web.kb.internal.taglib.html.angular.NgInputRadioAttributeProvider;
import org.jboss.tools.jst.web.kb.internal.taglib.html.angular.NgPluralizeAttributeProvider;
import org.jboss.tools.jst.web.kb.internal.taglib.html.angular.NgSwitchAttributeProvider;

/**
 * @author Alexey Kazakov
 */
public class AngularJSTest extends HTML5Test {

	private static final String TAGLIB_URI = "angularJS";

	@Override
	protected String getTaglibUri() {
		return TAGLIB_URI;
	}

	@Override
	protected String getFilePath() {
		return "WebContent/pages/angular/angularJS.html";
	}

	protected void assertClassDirective(String directiveName) {
		assertClassDirective(null, directiveName, true, true);
	}

	protected void assertClassDirective(String directiveName, boolean strict) {
		assertClassDirective(null, directiveName, true, strict);
	}

	protected void assertClassDirective(String directiveName, boolean expresion, boolean strict) {
		assertClassDirective(null, directiveName, expresion, strict);
	}

	protected void assertClassDirective(String tagName, String directiveName, boolean expresion, boolean strict) {
		if(tagName==null) {
			tagName = "a";
		}
		String[] attributeNames = AngularAttributeProvider.getNgAttributes(directiveName);
		String text = "ng-" + directiveName + (!expresion?"":": {};");
		for (String attributeName : attributeNames) {
			KbQuery query = createKbQuery(tagName, "class", "ng-" + directiveName.substring(0, directiveName.length()-1));
			TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
			assertProposals(strict, proposals, text);

			query = createKbQuery(tagName, "class", "ng-xyz: {expr}; ng-" + directiveName.substring(0, directiveName.length()-1));
			proposals = PageProcessor.getInstance().getProposals(query, context);
			assertProposals(strict, proposals, text);

			query = createKbQuery(tagName, "class", "ng-xyz ng-" + directiveName.substring(0, directiveName.length()-1));
			proposals = PageProcessor.getInstance().getProposals(query, context);
			assertProposals(strict, proposals, text);

			query = createKbQuery(tagName, "class", text + " ng-" + directiveName.substring(0, directiveName.length()-1));
			proposals = PageProcessor.getInstance().getProposals(query, context);
			assertNoProposal(strict, proposals, text);

			query = createKbQuery(tagName, attributeName, "", "class", text + " ng-" + directiveName.substring(0, directiveName.length()-1));
			proposals = PageProcessor.getInstance().getProposals(query, context);
			assertNoProposal(strict, proposals, text);

			query = createKbQuery(tagName, "xyz", "ng-" + directiveName.substring(0, directiveName.length()-1));
			proposals = PageProcessor.getInstance().getProposals(query, context);
			assertNoProposal(strict, proposals, text);
		}
	}

	protected void assertDirectiveProposals(String tagName, String directiveName) {
		assertDirectiveProposals(false, directiveName, true, tagName);
	}

	protected void assertDirectiveProposals(String directiveName) {
		assertDirectiveProposals(true, true, directiveName);
	}

	protected void assertDirectiveProposals(boolean anyTag, String directiveName) {
		assertDirectiveProposals(anyTag, true, directiveName);
	}

	protected void assertDirectiveProposals(boolean anyTag, boolean strict, String directiveName) {
		assertDirectiveProposals(anyTag, directiveName, strict);
	}

	protected void assertDirectiveProposals(boolean anyTag, String directiveName, boolean strict, String... tagNames) {
		if(tagNames.length==0) {
			tagNames = new String[]{"a"};
		}
		String[] attributes = AngularAttributeProvider.getNgAttributes(directiveName);
		assertDirectivesProposals(anyTag, attributes, strict, tagNames);
	}

	protected void assertDirectiveProposals(boolean anyTag, String directiveName, KbQuery.Tag[] tags) {
		assertDirectiveProposals(anyTag, directiveName, true, tags);
	}

	protected void assertDirectiveProposals(boolean anyTag, String directiveName, boolean strict, KbQuery.Tag[] tags) {
		String[] attributes = AngularAttributeProvider.getNgAttributes(directiveName);
		assertDirectivesProposals(anyTag, attributes, strict, tags);
	}

	protected void assertDirectivesProposals(HtmlAttribute[] attributes, boolean strict) {
		assertDirectivesProposals(true, getAttributeNames(attributes), strict);
	}

	protected void assertDirectivesProposals(boolean anyTag, HtmlAttribute[] attributes, boolean strict, String... tagNames) {
		assertDirectivesProposals(anyTag, getAttributeNames(attributes), strict, tagNames);
	}

	protected void assertDirectivesProposals(boolean anyTag, HtmlAttribute[] attributes, boolean strict, KbQuery.Tag[] tags) {
		assertDirectivesProposals(anyTag, getAttributeNames(attributes), strict, tags);
	}

	private String[] getAttributeNames(HtmlAttribute[] attributes) {
		Set<String> attributeNames = new HashSet<String>();
		for (HtmlAttribute attribute : attributes) {
			attributeNames.add(attribute.getName());
		}
		return attributeNames.toArray(new String[attributeNames.size()]);
	}

	public void assertDirectivesProposals(boolean anyTag, String[] attributeNames, boolean strict, String... tagNames) {
		for (String tagName : tagNames) {
			KbQuery.Tag[] tags = new KbQuery.Tag[]{createTag(tagName)};
			assertDirectivesProposals(anyTag, attributeNames, strict, tags);
		}
	}

	protected void assertDirectivesProposals(boolean anyTag, String[] attributeNames, boolean strict, KbQuery.Tag[] tags) {
		for (String attribute : attributeNames) {
			String value = attribute.substring(0, attribute.length() - 1);
			KbQuery query = createKbQuery(tags, value);
			TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
			assertProposals(strict, proposals, attribute);
		}
		if(!anyTag) {
			KbQuery.Tag[] pTags = new KbQuery.Tag[]{createTag("p")};
			for (String attribute : attributeNames) {
				String value = attribute.substring(0, attribute.length() - 1);
				KbQuery query = createKbQuery(pTags, value);
				TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
				assertNoProposal(strict, proposals, attribute);
			}
		}
	}

	protected void assertInputTextareaSelect(String directiveName) {
		assertDirectiveProposals("input", directiveName);
		assertDirectiveProposals("textarea", directiveName);
		assertDirectiveProposals("select", directiveName);
	}

	public void testHtmlWithJsLink() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("a")}, "ng-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertTrue(proposals.length>0);
	}

	public void testHtmlWithoutLibs() {
		IFile file = testProject.getFile(new Path("WebContent/pages/jquery/jQueryMobile.html"));
		ELContext context = PageContextFactory.createPageContext(file);
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("a")}, "ng-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertEquals(0, proposals.length);
	}

	public void testHtmlWithNgAttr() {
		IFile file = testProject.getFile(new Path("WebContent/pages/angular/angularJSNoScript.html"));
		ELContext context = PageContextFactory.createPageContext(file);

		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("a")}, "ng-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertTrue(proposals.length>0);
	}

	public void testNgClick() {
		assertDirectiveProposals("click");
	}

	public void testNgSubmit() {
		assertDirectiveProposals("form", "submit");

		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("form", "ng-click", "")}, "ng-submi");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertEquals(0, proposals.length);

		query = createKbQuery(new KbQuery.Tag[]{createTag("form", "data-ng-click", "")}, "data-ng-submi");
		proposals = PageProcessor.getInstance().getProposals(query, context);
		assertEquals(0, proposals.length);
	}

	public void testNgInput() {
		assertDirectivesProposals(false, NgInputAttributeProvider.ATTRIBUTES, true, "input");
	}

	public void testNgInputCheckbox() {
		KbQuery.Tag[] tags = new KbQuery.Tag[]{createTag("input", "type", "checkbox")};
		assertDirectivesProposals(false, NgInputCheckboxAttributeProvider.ATTRIBUTES, true, tags);

		assertDirectiveProposals(false, "false-value", tags);
	}

	public void testNgEmailCheckbox() {
		KbQuery.Tag[] tags = new KbQuery.Tag[]{createTag("input", "type", "email")};
		assertDirectivesProposals(false, NgInputDefaultAttributeProvider.ATTRIBUTES, true, tags);

		assertDirectiveProposals(false, "pattern", tags);
	}

	public void testNgNumberCheckbox() {
		KbQuery.Tag[] tags = new KbQuery.Tag[]{createTag("input", "type", "number")};
		assertDirectivesProposals(false, NgInputDefaultAttributeProvider.ATTRIBUTES, true, tags);

		assertDirectiveProposals(false, "minlength", tags);
	}

	public void testNgUrlCheckbox() {
		KbQuery.Tag[] tags = new KbQuery.Tag[]{createTag("input", "type", "url")};
		assertDirectivesProposals(false, NgInputDefaultAttributeProvider.ATTRIBUTES, true, tags);

		assertDirectiveProposals(false, "maxlength", tags);
	}

	public void testNgRadioCheckbox() {
		KbQuery.Tag[] tags = new KbQuery.Tag[]{createTag("input", "type", "radio")};
		assertDirectivesProposals(false, NgInputRadioAttributeProvider.ATTRIBUTES, true, tags);

		assertDirectiveProposals(false, "model", tags);
	}

	public void testNgTextCheckbox() {
		KbQuery.Tag[] tags = new KbQuery.Tag[]{createTag("input", "type", "text")};
		assertDirectivesProposals(false, NgInputDefaultAttributeProvider.ATTRIBUTES, true, tags);

		assertDirectiveProposals(false, "trim", tags);
	}

	public void testNgApp() {
		assertDirectivesProposals(NgAppAttributeProvider.ATTRIBUTES, true);

		KbQuery.Tag[] tags = new KbQuery.Tag[]{createTag("html", "ng-app", ""), createTag("input")};
		KbQuery query = createKbQuery(tags, "ng-ap");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertEquals(0, proposals.length);
	}

	public void testNgBind() {
		assertDirectiveProposals(true, false, "bind");
	}

	public void testNgBindClass() {
		assertClassDirective("bind");
	}

	public void testNgBindHtml() {
		assertDirectiveProposals("bind-html");
	}

	public void testNgBindTemplate() {
		assertDirectiveProposals("bind-template");
	}

	public void testNgBlur() {
		assertDirectiveProposals(false, "blur", true, "window", "input", "select", "textarea", "a");
	}

	public void testNgFocus() {
		assertDirectiveProposals(false, "focus", true, "window", "input", "select", "textarea", "a");
	}

	public void testNgChecked() {
		assertDirectiveProposals("input", "checked");
	}

	public void testNgClass() {
		assertDirectiveProposals(true, false, "class");
	}

	public void testNgClassClass() {
		assertClassDirective("class", false);
	}

	public void testNgClassEven() {
		assertDirectiveProposals("class-even");
	}

	public void testNgClassEvenClass() {
		assertClassDirective("class-even");
	}

	public void testNgClassOdd() {
		assertDirectiveProposals("class-odd");
	}

	public void testNgClassOddClass() {
		assertClassDirective("class-odd");
	}

	public void testNgCloak() {
		assertDirectiveProposals("cloak");
	}

	public void testNgCloakClass() {
		assertClassDirective("cloak", false, true);
	}

	public void testNgController() {
		assertDirectiveProposals("controller");
	}

	public void testNgCsp() {
		assertDirectiveProposals("html", "csp");
	}

	public void testNgDblclick() {
		assertDirectiveProposals("dblclick");
	}

	public void testNgForm() {
		assertDirectiveProposals("form");
	}

	public void testNgFormClass() {
		assertClassDirective("form", false, true);
	}

	public void testName() {
		KbQuery.Tag[] tags = new KbQuery.Tag[]{createTag("a", "ng-form", "")};
		assertDirectivesProposals(true, NameAttributeProvider.ATTRIBUTES, true, tags);
	}

	public void testNgDisabled() {
		assertDirectiveProposals(false, "disabled", true, "button", "input");
	}

	public void testNgHide() {
		assertDirectiveProposals("hide");
	}

	public void testNgHref() {
		assertDirectiveProposals("a", "href");
	}

	public void testNgIf() {
		assertDirectiveProposals(true, false, "if");
	}

	public void testNgInclude() {
		assertDirectiveProposals("include");
	}

	public void testNgIncludeClass() {
		assertClassDirective("include");
	}

	public void testNgInit() {
		assertDirectiveProposals("init");
	}

	public void testNgInitClass() {
		assertClassDirective("init");
	}

	public void testNgKeydown() {
		assertDirectiveProposals("keydown");
	}

	public void testNgKeypress() {
		assertDirectiveProposals("keypress");
	}

	public void testNgKeyup() {
		assertDirectiveProposals("keyup");
	}

	public void testNgList() {
		assertDirectiveProposals("input", "list");
	}

	public void testNgModel() {
		assertInputTextareaSelect("model");
	}

	public void testNgMousedown() {
		assertDirectiveProposals("mousedown");
	}

	public void testNgMouseenter() {
		assertDirectiveProposals("mouseenter");
	}

	public void testNgMouseleave() {
		assertDirectiveProposals("mouseleave");
	}

	public void testNgMousemove() {
		assertDirectiveProposals("mousemove");
	}

	public void testNgMouseover() {
		assertDirectiveProposals("mouseover");
	}

	public void testNgMouseup() {
		assertDirectiveProposals("mouseup");
	}

	public void testNgNonBindable() {
		assertDirectiveProposals("non-bindable");
	}

	public void testNgDetails() {
		assertDirectiveProposals("details", "open");
	}

	public void testNgPluralize() {
		assertDirectiveProposals("pluralize");

		KbQuery.Tag[] tags = new KbQuery.Tag[]{createTag("a", "ng-pluralize", "")};
		assertDirectivesProposals(true, NgPluralizeAttributeProvider.CONDITIONAL_ATTRIBUTES, true, tags);
	}

	public void testNgReadonly() {
		assertInputTextareaSelect("readonly");
	}

	public void testNgRepeat() {
		assertDirectiveProposals(true, false, "repeat");
	}

	public void testNgRepeatStart() {
		assertDirectiveProposals("repeat-start");
	}

	public void testNgRepeatEnd() {
		assertDirectiveProposals("repeat-end");
	}

	public void testNgSelected() {
		assertDirectiveProposals("selected");
	}

	public void testNgShow() {
		assertDirectiveProposals("show");
	}

	public void testNgSrc() {
		assertDirectiveProposals(false, "src", false, "img");
	}

	public void testNgSrcset() {
		assertDirectiveProposals("img", "srcset");
	}

	public void testNgStyle() {
		assertDirectiveProposals("style");
	}

	public void testNgStyleClass() {
		assertClassDirective("style");
	}

	public void testNgSwitch() {
		assertDirectiveProposals("switch");

		KbQuery.Tag[] tags = new KbQuery.Tag[]{createTag("div", "ng-switch", "")};
		assertDirectivesProposals(true, NgSwitchAttributeProvider.SWITCH_BODY_ATTRIBUTES, true, tags);
	}

	public void testNgTransclude() {
		assertDirectiveProposals("transclude");
	}

	public void testNgTranscludeClass() {
		assertClassDirective("transclude");
	}

	public void testScript() {
		KbQuery query = createKbQuery("script", "type", "text/ng-templat");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, "text/ng-template");
	}

	public void testNgSelect() {
		assertDirectiveProposals("select", "model");
		assertDirectiveProposals("select", "options");
		assertDirectiveProposals("select", "required");
	}

	public void testNgTextarea() {
		assertDirectiveProposals("textarea", "model");
		assertDirectiveProposals("textarea", "minlength");
		assertDirectiveProposals("textarea", "maxlength");
		assertDirectiveProposals("textarea", "required");
		assertDirectiveProposals("textarea", "pattern");
		assertDirectiveProposals("textarea", "change");
	}
}