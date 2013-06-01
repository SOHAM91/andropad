/**
 * AndroPad Project - ITII CNAM Alsace - Juin 2013
 * Fabrice Latterner - Clement Troesch
 */

package com.itii.andropadserver;

import java.util.HashMap;
import java.util.Map.Entry;

import com.itii.andropadcommon.AndroButton;

public class AndroGamepad {

	private HashMap<AndroButton.Mapping, AndroButton> m_list = new HashMap<AndroButton.Mapping, AndroButton>();
	private boolean m_connected = false;

	public AndroGamepad() {
		m_connected = false;
		for (AndroButton.Mapping b : AndroButton.Mapping.values()) {
			this.add(b, new AndroButton(b, AndroButton.State.INACTIVE));
		}
	}

	public HashMap<AndroButton.Mapping, AndroButton> getButtonsList() {
		return m_list;
	}

	public void setConnected(boolean b) {
		m_connected = b;
	}

	public boolean isConnected() {
		return m_connected;
	}

	public void add(AndroButton.Mapping _button, AndroButton _ab) {
		m_list.put(_button, _ab);
	}

	public void remove(AndroButton.Mapping _button) {
		if (m_list.containsKey(_button)) {
			m_list.remove(_button);
		}
	}

	public void updateState(AndroState as) {
		for (Entry<AndroButton.Mapping, AndroButton.State> entry : as
				.getState().entrySet()) {
			if (m_list.containsKey(entry.getKey())) {
				m_list.get(entry.getKey()).setState(entry.getValue());
			}
		}
	}

	public AndroButton getButton(AndroButton.Mapping button) {
		if (m_list.containsKey(button)) {
			return m_list.get(button);
		} else {
			return null;
		}
	}

	public AndroState getState() {
		AndroState as = new AndroState();
		for (Entry<AndroButton.Mapping, AndroButton> entry : m_list.entrySet()) {
			as.add(entry.getKey(), entry.getValue().getState());
		}
		return as;
	}

}
