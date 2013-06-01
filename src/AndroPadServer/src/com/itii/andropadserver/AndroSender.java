/**
 * AndroPad Project - ITII CNAM Alsace - Juin 2013
 * Fabrice Latterner - Clement Troesch
 */

package com.itii.andropadserver;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.bluetooth.BluetoothStateException;
import javax.microedition.io.StreamConnection;

public class AndroSender extends Thread {

	private StreamConnection m_sc = null;
	private OutputStream m_outStream = null;
	private boolean m_run = false;
	private int m_gamepadIndex = 0;
	private ObjectOutputStream m_oos = null;

	public AndroSender(StreamConnection _sc, int _index) throws IOException {
		m_sc = _sc;
		m_outStream = m_sc.openOutputStream();
		m_run = true;
		m_gamepadIndex = _index;
	}

	@Override
	public void run() {
		// Listen the client and update the gamepad
		while (m_run) {
			try {
				Thread.sleep(10);
			} catch (Exception ex) {
				// Close socket
				Logger.getLogger(AndroSender.class.getName()).log(Level.SEVERE,
						null, ex);
				m_run = false;
			}
		}
		close();
	}

	public void close() {
		try {
			m_outStream.close();
			m_sc.close();
		} catch (BluetoothStateException ex) {
			Logger.getLogger(AndroListen.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (IOException ex) {
			Logger.getLogger(AndroListen.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	public void sendObject(Object _obj) {
		try {
			m_oos = new ObjectOutputStream(m_outStream);
			m_oos.writeObject(_obj);
			System.out.println("send object");
		} catch (Exception ex) {
			Logger.getLogger(AndroSender.class.getName()).log(Level.SEVERE,
					null, ex);
			m_run = false;
		}
	}

	public void setRun(boolean _b) {
		m_run = _b;
	}
}