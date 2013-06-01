/**
 * AndroPad Project - ITII CNAM Alsace - Juin 2013
 * Fabrice Latterner - Clement Troesch
 */

package com.itii.andropadserver;

import java.util.HashMap;

import com.itii.andropadcommon.AndroButton;

public class AndroState {
	private HashMap<AndroButton.Mapping, AndroButton.State> m_stateList = new HashMap<AndroButton.Mapping, AndroButton.State>();

	public HashMap<AndroButton.Mapping, AndroButton.State> getState() {
		return m_stateList;
	}

	public void add(AndroButton.Mapping mapping, AndroButton.State state) {
		m_stateList.put(mapping, state);
	}

	public void remove(AndroButton.Mapping mapping) {
		if (m_stateList.containsKey(mapping)) {
			m_stateList.remove(mapping);
		}
	}
}
