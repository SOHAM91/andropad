/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itii.andropadcommon;

import java.util.ArrayList;

/**
 *
 * @author Coralie
 */
public interface Subject {
    
    ArrayList<Observer> m_observerList = new ArrayList<Observer>();
   
    public void register(Observer obs);
    public void unregister(Observer obs);
    
}
