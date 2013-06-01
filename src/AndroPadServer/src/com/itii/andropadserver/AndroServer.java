/**
 * AndroPad Project - ITII CNAM Alsace - Juin 2013
 * Fabrice Latterner - Clement Troesch
 */

package com.itii.andropadserver;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class AndroServer extends Thread {
	private LocalDevice m_local = null;
	private UUID m_uuid;
	private String m_connectionString;
	private boolean m_run;

	private HashMap<Integer, AndroListen> m_listeners = new HashMap<Integer, AndroListen>();
	private HashMap<Integer, AndroSender> m_senders = new HashMap<Integer, AndroSender>();

	public AndroServer() throws BluetoothStateException {
		m_uuid = new UUID("B2B8428C00014560ADB5C983CEDAD79E", false);
		// Connexion URL
		m_connectionString = "btspp://localhost:" + m_uuid
				+ ";name=AndropadServer;authenticate=false;encrypt=false;";

		// Set device discoverable

		m_local = LocalDevice.getLocalDevice();

		try {
			m_local.setDiscoverable(DiscoveryAgent.GIAC);
		} catch (BluetoothStateException ex) {
			Logger.getLogger(AndroServer.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		m_run = true;
	}

	@Override
	public void run() {
		try {
			// open server url
			OutputController
					.writeLine(
							"Serveur d�marr�. En attente des connexions des clients...",
							OutputController.MessageLevel.SERVER_STATE);
			StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier) Connector
					.open(m_connectionString);
			// Wait for client connection
			while (m_run) {
				if (GamepadManager.getInstance().getNbGamepad() > GamepadManager
						.getInstance().getNbActive()) {
					OutputController.writeLine("Nombre de joueurs connect�s : "
							+ GamepadManager.getInstance().getNbActive(),
							OutputController.MessageLevel.SERVER_INFO);

					StreamConnection connection = streamConnNotifier
							.acceptAndOpen();
					// Create thread to communicate with the client, and pass it
					// to the GamepadManager
					int gamepadIndex = GamepadManager.getInstance()
							.getFirstAvailable();
					if (gamepadIndex > -1) {
						AndroListen al = new AndroListen(connection,
								gamepadIndex);
						AndroSender as = new AndroSender(connection,
								gamepadIndex);
						m_listeners.put(gamepadIndex, al);
						m_senders.put(gamepadIndex, as);
						al.start();
						as.start();
					} else {
						connection.close();
					}

				} else {
					try {
						Thread.sleep(10);
					} catch (InterruptedException ex) {
						Logger.getLogger(AndroServer.class.getName()).log(
								Level.SEVERE, null, ex);
					}
				}
			}
			// close server
			streamConnNotifier.close();
		} catch (IOException ex) {
			Logger.getLogger(AndroServer.class.getName()).log(Level.SEVERE,
					null, ex);
		}

	}

	public void setRun(boolean run) {

		if (!run) {
			for (AndroListen al : m_listeners.values()) {
				if (al != null) {
					al.close();
				}
			}
			for (AndroSender as : m_senders.values()) {
				if (as != null) {
					as.close();
				}
			}
		}
		m_run = run;
		try {
			m_local.setDiscoverable(DiscoveryAgent.NOT_DISCOVERABLE);
		} catch (BluetoothStateException ex) {
			Logger.getLogger(AndroServer.class.getName()).log(Level.SEVERE,
					null, ex);
		}

	}

	public void sendObject(int _index, Object _obj) {
		if (m_senders.containsKey(_index)) {
			m_senders.get(_index).sendObject(_obj);
		}
	}

}