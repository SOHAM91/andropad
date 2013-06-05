/**
 * AndroPad Project - ITII CNAM Alsace - Juin 2013
 * Fabrice Latterner - Clement Troesch
 */

package com.itii.andropadserver;

import java.io.IOException;
import java.util.ArrayList;

import javax.bluetooth.BluetoothStateException;

import com.itii.andropadcommon.AndroButton;
import com.itii.andropadcommon.Observer;


public class GamepadManager {

	private static GamepadManager m_gm = null;
	private ArrayList<AndroGamepad> m_list = new ArrayList<AndroGamepad>();
	private boolean m_run = false;
	private AndroServer m_androServer;

	public void dispose() {
		m_list.clear();
		m_androServer = null;
	}

	public int getFirstAvailable() {
		int i = 0;
		for (AndroGamepad ag : m_list) {
			if (!ag.isConnected()) {
				return i;
			}
			i++;
		}
		return -1;
	}

	public int getNbActive() {
		int i = 0;
		for (AndroGamepad ag : m_list) {
			if (ag.isConnected()) {
				i++;
			}
		}
		return i;
	}

	public int getNbGamepad() {
		return m_list.size();
	}

	public boolean isFull() {
		for (AndroGamepad ag : m_list) {
			if (!ag.isConnected()) {
				return false;
			}
		}
		return true;
	}

	public static synchronized GamepadManager getInstance()
			throws BluetoothStateException, IOException {
		if (m_gm == null) {
			m_gm = new GamepadManager();
		}
		return m_gm;
	}

	public AndroGamepad getPad(int index) {
		if (m_list.size() > index) {
			return m_list.get(index);
		} else {
			return null;
		}
	}

	public void observeGamepad(int gamepadIndex, Observer obs) {
		if (m_list.size() > gamepadIndex) {
			for (AndroButton ab : m_list.get(gamepadIndex).getButtonsList()
					.values()) {
				ab.register(obs);
			}
		}
	}

	private GamepadManager() {
		// Create gamepads
		for (int i = 0; i < s_NUMBER_OF_PADS; i++) {
			m_list.add(new AndroGamepad());
		}
	}

	public void start() throws BluetoothStateException, IOException {
		if (!m_run) {
			// Launch the connection manager
			m_androServer = new AndroServer();
			m_androServer.start();
                        m_run = true;
		}
	}

	public void stop() {
		m_androServer.setRun(false);
		OutputController.writeLine("Arrêt du serveur",
				OutputController.MessageLevel.SERVER_STATE);
                m_run = false;
	}

	public void sendObject(int _index, Object _obj) {
		if (m_androServer != null && m_androServer.isAlive()) {
			m_androServer.sendObject(_index, _obj);
		}
	}

	public static int s_NUMBER_OF_PADS = 3;

}
