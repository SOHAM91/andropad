/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itii.andropadserver.server;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import com.itii.andropadcommon.AndroButton;
import com.itii.andropadcommon.Observer;

/**
 *
 * @author Coralie
 */
public class ObserverTest implements Observer {

    private int m_gamepadIndex;
    private Robot m_robot = null;
    private Translator m_translator = null;
    
    public ObserverTest(int n) {
        m_gamepadIndex = n;
        m_translator = new Translator();
        try {
			m_robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
   
    @Override
    public void update(AndroButton.State state, Object obj) {
        if(obj instanceof AndroButton) {
            AndroButton button = (AndroButton)obj;
            OutputController.writeLine("Input for gamepad :" + m_gamepadIndex);
            OutputController.writeLine("Button : " + button.getButton().toString());
            OutputController.writeLine("Button : " + button.getState().toString());
            int keycode = m_translator.getKeyEvent(button.getButton());
            if(keycode != -1) {
	            if(state == AndroButton.State.PRESSED) {
	            	m_robot.keyPress(keycode);
	            } else if(state == AndroButton.State.RELEASED) {
	            	m_robot.keyRelease(keycode);
	            }
            }
        }
    }
    
}