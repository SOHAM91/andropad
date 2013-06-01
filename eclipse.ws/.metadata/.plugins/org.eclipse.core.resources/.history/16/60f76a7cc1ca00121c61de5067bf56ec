/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itii.andropadcommon;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Coralie
 */

public class AndroButton implements Serializable, Subject {
	
	public enum Mapping { 
		SPACE, UP, DOWN, LEFT,
		RIGHT, CTRL_GAUCHE, CTRL_DROIT, ENTER, BACKSPACE,
		ESCAPE, A, B, C, D, E, F, G, H, I, J, K,
		L, M, N, O, P, Q, R, S, T, U, V, W, X,
		Y, Z
	}
	
	public enum State {
	    RELEASED,
	    PRESSED,
	    INACTIVE
	}
    
   
    
    public AndroButton(Mapping mapping, State state) {
        m_mapping = mapping;
        setState(state);
    }
    
    public Mapping getButton() {
        return m_mapping;
    }
    
    public void setState(State state) {
        if(m_state != state || System.currentTimeMillis() - m_startTime > 500) {
            m_state = state;
            m_startTime = System.currentTimeMillis();
            for(Observer obs : m_observerList) {
                obs.update(state, this);
            }
        }
    }
    
    public State getState() {
        return m_state;
    }
    
    public long getStateTime() {
        return System.currentTimeMillis() - m_startTime;
    }

    @Override
    public void register(Observer obs) {
        if(!m_observerList.contains(obs)) {
            m_observerList.add(obs);
        }
    }

    @Override
    public void unregister(Observer obs) {
        if(m_observerList.contains(obs)) {
            m_observerList.remove(obs);
        }
    }
    
    private static final long serialVersionUID = 7719533840224105858L;
    private Mapping m_mapping;
    private State m_state;
    private long m_startTime;
    
    ArrayList<Observer> m_observerList = new ArrayList<Observer>();

}
