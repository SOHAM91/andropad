/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itii.andropadserver.server;

import javax.swing.JTextArea;

/**
 *
 * @author Coralie
 */
public class OutputController {
    
    private static Object m_output;
    
    
    public static void writeLine(String s) {
        if(m_output == null) {
            m_output = System.out;
        }
        if (m_output == System.out) {
            System.out.println(s);
        } else if(m_output instanceof JTextArea) {
            ((JTextArea)m_output).append("\n" + s);
        }
    }
    
    public static void setOutput(Object _out) {
        m_output = _out;
    }
    
}
