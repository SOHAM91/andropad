/**
 * AndroPad Project - ITII CNAM Alsace - Juin 2013
 * Fabrice Latterner - Clement Troesch
 */

package com.itii.andropadcommon;

import java.io.Serializable;

public class Player implements Serializable {

	private static final long serialVersionUID = 1161565057286233801L;

	private String m_name;

	public Player(String _name) {
		m_name = _name;
	}

	public String getName() {
		return m_name;
	}
}