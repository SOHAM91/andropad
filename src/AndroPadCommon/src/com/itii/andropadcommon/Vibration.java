/**
 * AndroPad Project - ITII CNAM Alsace - Juin 2013
 * Fabrice Latterner - Clement Troesch
 */

package com.itii.andropadcommon;

import java.io.Serializable;

public class Vibration implements Serializable {

	private static final long serialVersionUID = 1161565088286233801L;

	private int m_delay;

	public Vibration(int _delay) {
		m_delay = _delay;
	}

	public int getDelay() {
		return m_delay;
	}
}
