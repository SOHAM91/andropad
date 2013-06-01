/**
 * AndroPad Project - ITII CNAM Alsace - Juin 2013
 * Fabrice Latterner - Clement Troesch
 */

package com.itii.andropadserver;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import com.itii.andropadcommon.AndroButton;

public class Translator {
	private HashMap<AndroButton.Mapping, Integer> m_map = new HashMap<AndroButton.Mapping, Integer>();

	public Translator() {
		m_map = new HashMap<AndroButton.Mapping, Integer>();
		m_map.put(AndroButton.Mapping.A, KeyEvent.VK_A);
		m_map.put(AndroButton.Mapping.B, KeyEvent.VK_B);
		m_map.put(AndroButton.Mapping.C, KeyEvent.VK_C);
		m_map.put(AndroButton.Mapping.D, KeyEvent.VK_D);
		m_map.put(AndroButton.Mapping.E, KeyEvent.VK_E);
		m_map.put(AndroButton.Mapping.F, KeyEvent.VK_F);
		m_map.put(AndroButton.Mapping.G, KeyEvent.VK_G);
		m_map.put(AndroButton.Mapping.H, KeyEvent.VK_H);
		m_map.put(AndroButton.Mapping.I, KeyEvent.VK_I);
		m_map.put(AndroButton.Mapping.J, KeyEvent.VK_J);
		m_map.put(AndroButton.Mapping.K, KeyEvent.VK_K);
		m_map.put(AndroButton.Mapping.L, KeyEvent.VK_L);
		m_map.put(AndroButton.Mapping.M, KeyEvent.VK_M);
		m_map.put(AndroButton.Mapping.N, KeyEvent.VK_N);
		m_map.put(AndroButton.Mapping.O, KeyEvent.VK_O);
		m_map.put(AndroButton.Mapping.P, KeyEvent.VK_P);
		m_map.put(AndroButton.Mapping.Q, KeyEvent.VK_Q);
		m_map.put(AndroButton.Mapping.R, KeyEvent.VK_R);
		m_map.put(AndroButton.Mapping.S, KeyEvent.VK_S);
		m_map.put(AndroButton.Mapping.T, KeyEvent.VK_T);
		m_map.put(AndroButton.Mapping.U, KeyEvent.VK_U);
		m_map.put(AndroButton.Mapping.V, KeyEvent.VK_V);
		m_map.put(AndroButton.Mapping.W, KeyEvent.VK_W);
		m_map.put(AndroButton.Mapping.X, KeyEvent.VK_X);
		m_map.put(AndroButton.Mapping.Y, KeyEvent.VK_Y);
		m_map.put(AndroButton.Mapping.Z, KeyEvent.VK_Z);
		m_map.put(AndroButton.Mapping.BACKSPACE, KeyEvent.VK_BACK_SPACE);
		m_map.put(AndroButton.Mapping.CTRL_DROIT, KeyEvent.VK_CONTROL);
		m_map.put(AndroButton.Mapping.CTRL_GAUCHE, KeyEvent.VK_CONTROL);
		m_map.put(AndroButton.Mapping.ENTER, KeyEvent.VK_ENTER);
		m_map.put(AndroButton.Mapping.ESCAPE, KeyEvent.VK_ESCAPE);
		m_map.put(AndroButton.Mapping.SPACE, KeyEvent.VK_SPACE);
		m_map.put(AndroButton.Mapping.DOWN, KeyEvent.VK_DOWN);
		m_map.put(AndroButton.Mapping.UP, KeyEvent.VK_UP);
		m_map.put(AndroButton.Mapping.LEFT, KeyEvent.VK_LEFT);
		m_map.put(AndroButton.Mapping.RIGHT, KeyEvent.VK_RIGHT);
	}

	public int getKeyEvent(AndroButton.Mapping m) {
		if (m_map.containsKey(m)) {
			return m_map.get(m);
		} else {
			return -1;
		}
	}
}
