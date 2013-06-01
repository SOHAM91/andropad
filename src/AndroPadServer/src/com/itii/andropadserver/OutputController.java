/**
 * AndroPad Project - ITII CNAM Alsace - Juin 2013
 * Fabrice Latterner - Clement Troesch
 */

package com.itii.andropadserver;

import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class OutputController {

	public enum MessageLevel {
		SERVER_STATE, SERVER_INFO, INPUT_INFO;
	}

	public static void writeLine(String s, MessageLevel level) {
		if (m_output == null) {
			m_output = System.out;
		}
		if (m_output == System.out) {
			System.out.println(s);
		} else if (m_output instanceof JEditorPane) {
			try {
				JEditorPane ep = (JEditorPane) m_output;
				HTMLEditorKit kit = (HTMLEditorKit) ep.getEditorKit();
				HTMLDocument doc = (HTMLDocument) ep.getDocument();
				String line = "<font color=\""
						+ s_LEVEL_COLORS[level.ordinal()] + "\">" + s
						+ "</font>\n";
				kit.insertHTML(doc, doc.getLength(), line, 0, 0, null);
				ep.setCaretPosition(doc.getLength());
			} catch (BadLocationException | IOException ex) {
				System.out.println(s);
			}
		}
	}

	public static void setOutput(Object _out) {
		m_output = _out;
	}

	private static Object m_output;
	private static String[] s_LEVEL_COLORS = { "#A60000", "#062170", "#476BD6" };

}
