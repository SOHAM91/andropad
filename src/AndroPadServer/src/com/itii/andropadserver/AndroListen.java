/**
 * AndroPad Project - ITII CNAM Alsace - Juin 2013
 * Fabrice Latterner - Clement Troesch
 */

package com.itii.andropadserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.bluetooth.BluetoothStateException;
import javax.microedition.io.StreamConnection;

import com.itii.andropadcommon.AndroButton;

public class AndroListen extends Thread {

	private StreamConnection m_sc = null;
	private InputStream m_inStream = null;
	private boolean m_run = false;
	private int m_gamepadIndex = 0;
	private ObjectInputStream m_ios = null;

	public AndroListen(StreamConnection _sc, int _index) throws IOException {
		m_sc = _sc;
		m_inStream = m_sc.openInputStream();
		m_run = true;
		m_gamepadIndex = _index;
		GamepadManager.getInstance().getPad(m_gamepadIndex).setConnected(true);
	}

	@Override
	public void run() {
		// Listen the client and update the gamepad
		while (m_run) {
			try {
				m_ios = new ObjectInputStream(m_inStream);
				Object obj = m_ios.readObject();
				if (obj instanceof AndroButton) {
					// Update the gamepad
					AndroButton button = (AndroButton) obj;
					GamepadManager.getInstance().getPad(m_gamepadIndex)
							.getButton(button.getButton())
							.setState(button.getState());
				}
			} catch (Exception ex) {
				// Close socket
				m_run = false;
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException ex) {
				Logger.getLogger(AndroListen.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		}
		close();
	}

	public void close() {
		try {
			// GamepadManager.getInstance().getPad(m_gamepadIndex).setConnected(false);
			m_inStream.close();
			m_sc.close();
			OutputController.writeLine("Déconnexion du pad " + m_gamepadIndex,
					OutputController.MessageLevel.SERVER_INFO);
		} catch (BluetoothStateException ex) {
			Logger.getLogger(AndroListen.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (IOException ex) {
			Logger.getLogger(AndroListen.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
}
