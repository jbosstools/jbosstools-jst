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
package org.jboss.tools.jst.jsp.ui.action;

import java.util.*;
import org.eclipse.jface.text.*;

public class JSPFormatter {
	static final int ROOT = 0;
	static final int TEXT = 1;
	static final int TAG = 2;
	static final int TAG_CLOSING = 3;
	static final int JSP = 4;
	static final int COMMENT = 5;

	String text;
	StringBuffer sb = new StringBuffer();
	Token root;
	
	int selectionStart;
	int selectionEnd;
	
	int start = -1;
	int end = -1;
	
	public void format(IDocument document, TextSelection textSelection) throws BadLocationException {
		selectionStart = textSelection.getOffset();
		selectionEnd = selectionStart + textSelection.getLength();
		text = document.get();
		root = new Token(ROOT, "", 0, text.length(), null); //$NON-NLS-1$
		root.indent = ""; //$NON-NLS-1$
		root.indentLevel = -1;
		tokenize();
		doFormat(root.firstChild);
		document.replace(start, end - start, sb.toString());
	}
	
	private void tokenize() {
		int cursor = 0;
		Token current = root;
		Token last = null;
		while(cursor < text.length()) {
			int p = text.indexOf('<', cursor);
			if(p < 0) {
				current.addChild(last = createTag(TEXT, "", cursor, text.length() - cursor, last)); //$NON-NLS-1$
				cursor = text.length();
			} else {
				if(p > cursor) {
					current.addChild(last = createTag(TEXT, "", cursor, p - cursor, last)); //$NON-NLS-1$
					cursor = p;
				}
				if(isStringStart(cursor, "<%")) { //$NON-NLS-1$
					int q = text.indexOf("%>", cursor); //$NON-NLS-1$
					int nc = (q < 0) ? text.length() : q + 2;
					current.addChild(last = createTag(JSP, "", cursor, nc - cursor, last)); //$NON-NLS-1$
					cursor = nc;
				} else if(isStringStart(cursor, "<!--")) { //$NON-NLS-1$
					int q = text.indexOf("-->", cursor); //$NON-NLS-1$
					int nc = (q < 0) ? text.length() : q + 3;
					current.addChild(last = createTag(COMMENT, "", cursor, nc - cursor, last)); //$NON-NLS-1$
					cursor = nc;
				} else if(isStringStart(cursor, "</")) { //$NON-NLS-1$
					String tag = readTag(cursor + 2);
					int q = text.indexOf(">", cursor); //$NON-NLS-1$
					int nc = (q < 0) ? text.length() : q + 1;
					last = createTag(TAG_CLOSING, tag, cursor, nc - cursor, last);
					current = findParent(current, last);
					current.addChild(last);
					cursor = nc;
				} else {
					String tag = readTag(cursor + 1);
					int q = findTagClosingSymbol(cursor);
					int nc = (q < 0) ? text.length() : q + 1;
					last = createTag(TAG, tag, cursor, nc - cursor, last);
					if(isOptionallyClosed(tag)) {
						current = findParentForOptionallyClosedTag(current, tag);
					}
					current.addChild(last);
					cursor = nc;
					if(q > 0 && text.charAt(q - 1) != '/' && areChildrenAllowed(tag)) {
						current = last;
					}
				}
			}
		}
	}
	
	private Token findParent(Token current, Token t) {
		Token c = current;
		while(c.kind != ROOT) {		
			if(c.name.equals(t.name)) return c.parent;
			c = c.parent;
		}
		return current;
	}
	
	private boolean isOptionallyClosed(String name) {
		return ".body.p.dt.dd.li.ol.option.thead.tfoot.tbody.colgroup.tr.td.th.head.html.".indexOf(name.toLowerCase()) >= 0; //$NON-NLS-1$
	}
	
	private Token findParentForOptionallyClosedTag(Token current, String name) {
		String n1 = name.toLowerCase();
		String n2 = current.name.toLowerCase();
		if("p".equals(n1)) { //$NON-NLS-1$
			if(n2.equals("p")) return current.parent; //$NON-NLS-1$
		} else if("tr".equals(n1)) { //$NON-NLS-1$
			if(n2.equals("tr")) return current.parent; //$NON-NLS-1$
			if(n2.equals("th") || n2.equals("td") || n2.equals("p")) return findParentForOptionallyClosedTag(current.parent, name); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} else if("td".equals(n1) || "th".equals(n1)) {  //$NON-NLS-1$ //$NON-NLS-2$
			if(n2.equals("th") || n2.equals("td")) return current.parent; //$NON-NLS-1$ //$NON-NLS-2$
			if(n2.equals("p")) return findParentForOptionallyClosedTag(current.parent, name); //$NON-NLS-1$
		}
		return current;
	}
	
	private boolean areChildrenAllowed(String name) {
		return ".br.area.link.img.param.hr.input.col.isindex.base.meta.".indexOf("." + name.toLowerCase() + ".") < 0; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	private Token createTag(int kind, String name, int off, int length, Token previous) {
		Token t = new Token(kind, name, off, length, previous);
		t.indentLength = (kind == TEXT) ? -1 : computeIndentLength(off);
		if(previous != null && previous.kind == JSPFormatter.TEXT && t.indentLength > 0) {
			previous.length -= t.indentLength;
			t.off -= t.indentLength;
			t.length += t.indentLength;
		}
		return t;
	}
	
	private boolean isStringStart(int c, String s) {
		if(text.length() <= c + s.length()) return false;
		for (int i = 0; i < s.length(); i++) {
			if(text.charAt(c + i) != s.charAt(i)) return false;
		}
		return true;
	}
	
	private String readTag(int c) {
		int k = c;
		while(k < text.length() && isNameChar(text.charAt(k))) ++k;
		return text.substring(c, k);
	}
	
	private boolean isNameChar(char ch) {
		return Character.isJavaIdentifierPart(ch) || ch == '-' || ch == ':';
	}
	
	private int computeIndentLength(int off) {
		off--;
		int l = 0;
		while(off >= 0) {
			char ch = text.charAt(off);
			if(ch == '\n' || ch == '\r') return l;
			if(!Character.isWhitespace(ch)) return -1;
			++l;
			--off;
		}
		return (off < 0) ? l : -1;
	}
	
	private void doFormat(Token t) {
		if(t == null) return;
		if(t.off < selectionStart || t.off + t.length > selectionEnd) {
			if(t.indentLength >= 0) {
				t.indent = text.substring(t.off, t.off + t.indentLength);
			}
		} else {
			if(start < 0) start = t.off;
			end = t.off + t.length;
			boolean hasNewLine = t.indentLength >= 0;
			boolean needNewLine = needNewLine(t);
			if(needNewLine) sb.append("\n"); //$NON-NLS-1$
			String line = (t.indentLength > 0) ? text.substring(t.off + t.indentLength, t.off + t.length)
			              : text.substring(t.off, t.off + t.length);
			String indent = null;
			Token s = t.prevSibling;
			int qq = 0;
			while(s != null && s.indent == null) s = s.prevSibling;
			if(s == null) {
				s = t;
				while(s.indent == null) {
					s = s.parent;
					++qq;
				}
				if(s == root) --qq;
			}
			indent = s.indent;
			for (int k = 0; k < qq; k++) indent += "  "; //$NON-NLS-1$
			t.indent = indent;
			append(line, indent, hasNewLine || needNewLine);
		}
		doFormat(t.firstChild);
		doFormat(t.nextSibling);
		
	}
	
	private boolean needNewLine(Token t) {
		if(t == null) return false;
		if(t.previous == null && (t.parent == null || t.parent == root)) return false;
		if(t.kind == TEXT) return false;
		if(t.indentLength >= 0) return false;
		if(t.kind == TAG || t.kind == TAG_CLOSING) {
			if(isInline(t.name)) return false; 
		}
		Token p = t.previous;
		if(p == null) return true;
		if(p.kind != TEXT) {
			if(p.kind == TAG || p.kind == TAG_CLOSING) {
				if(isInline(p.name)) return false; 
			}
			return true;
		}
		for (int q = 0; q < p.length; q++) {
			char ch = text.charAt(q + p.off);
			if(!Character.isWhitespace(ch)) return false;
		}
		return true;
	}
	
	private boolean isInline(String name) {
		return ".br.a.b.i.u.s.strong.img".indexOf("." + name.toLowerCase() + ".") >= 0; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	private boolean isSpace(String line) {
		for (int q = 0; q < line.length(); q++) {
			char ch = line.charAt(q);
			if(!Character.isWhitespace(ch) && ch != '\n' && ch != '\r') {
				return false;
			} 
		}
		return true;
	}
	
	private void append(String line, String indent, boolean indentFirst) {
		StringTokenizer st = new StringTokenizer(line, "\r\n", true); //$NON-NLS-1$
		boolean first = true;
		boolean doIndent = indentFirst;
		while(st.hasMoreTokens()) {
			String t = st.nextToken();
			if(isSpace(t)) {
				sb.append(t);
			} else {
				if(doIndent) {
					sb.append(indent); 
					if(!first) sb.append(' ');
					t = removeLeadingSpaces(t);
				}
				sb.append(t);
			}
			first = false;
			doIndent = true;
		}
	}
	
	private String removeLeadingSpaces(String line) {
		if(line.length() == 0 || !Character.isWhitespace(line.charAt(0))) return line;
		for (int i = 0; i < line.length(); i++)  {
			char ch = line.charAt(i);
			if(ch == '\r' || ch == '\n') return line;
			if(!Character.isWhitespace(ch)) return line.substring(i);
		}
		return ""; //$NON-NLS-1$
	}
	
	public int findTagClosingSymbol(int i) {
		int l = text.length();
		char quota = '\0';
		while(i < l) {
			char ch = text.charAt(i);
			if(quota != '\0') {
				if(ch == quota) quota = '\0';
			} else if(ch == '\'' || ch == '"') {
				quota = ch;
			} else if(ch == '>') {
				return i;
			}
			++i;
		}
		return -1;
	}
	
}

class Token {
	Token previous;
	Token parent;
	Token firstChild;
	Token prevSibling;
	Token nextSibling;
	
	int kind;
	int indentLevel;
	String name;
	int indentLength = -1;
	int off;
	int length;
	
	String indent = null;

	public Token(int kind, String name, int off, int length, Token previous) {
		this.previous = previous;
		this.kind = kind;
		this.name = name;
		this.off = off;
		this.length = length; 
	}
	
	public String toString() {
		return "k=" + kind + " iL=" + indentLevel + " ind=" + indentLength + " n=" + name + " off=" + off + " l=" + length; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	}
	
	public void addChild(Token t) {
		t.parent = this;
		t.indentLevel = indentLevel + 1;
		if(firstChild == null) {
			firstChild = t;
		} else {
			firstChild.addNextSibling(t);
		}
	}
	
	public void addNextSibling(Token t) {
		if(nextSibling == null) {
			nextSibling = t;
			t.prevSibling = this;
		} else {
			nextSibling.addNextSibling(t);
		}
	}
	
}
