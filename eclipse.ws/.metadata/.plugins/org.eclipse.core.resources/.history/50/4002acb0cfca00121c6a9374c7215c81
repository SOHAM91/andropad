/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itii.andropadserver.server;

import java.util.HashMap;

import com.itii.andropadcommon.AndroButton;

/**
 *
 * @author Coralie
 */
public class AndroState {
    private HashMap<AndroButton.Mapping,AndroButton.State> m_stateList = new HashMap<AndroButton.Mapping,AndroButton.State>();
    
    public HashMap<AndroButton.Mapping,AndroButton.State> getState() {
        return m_stateList;
    }
    
    public void add(AndroButton.Mapping mapping, AndroButton.State state) {
        m_stateList.put(mapping, state);
    }
    
    public void remove(AndroButton.Mapping mapping) {
        if(m_stateList.containsKey(mapping)) {
            m_stateList.remove(mapping);
        }
    }
}
