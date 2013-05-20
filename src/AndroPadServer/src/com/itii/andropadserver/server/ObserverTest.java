/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itii.andropadserver.server;

import com.itii.andropadcommon.AndroButton;
import com.itii.andropadcommon.Observer;

/**
 *
 * @author Coralie
 */
public class ObserverTest implements Observer {

    private int m_gamepadIndex;
    
    public ObserverTest(int n) {
        m_gamepadIndex = n;
    }
   
    @Override
    public void update(AndroButton.State state, Object obj) {
        if(obj instanceof AndroButton) {
            AndroButton button = (AndroButton)obj;
            System.out.println("Input for gamepad :" + m_gamepadIndex);
            System.out.println("Button : " + button.getButton().toString());
            System.out.println("Button : " + button.getState().toString());
        }
    }
    
}
