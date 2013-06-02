/**
 * AndroPad Project - ITII CNAM Alsace - Juin 2013
 * Fabrice Latterner - Clement Troesch
 */

package com.itii.andropad.bluetooth;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.itii.andropad.components.GamingSurfaceView;

public class ConnectThread extends Thread {

	private final BluetoothSocket m_socket;
	private ListenerThread m_listenerThread;
	private Context m_context;
	private GamingSurfaceView m_gamingSurface;

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public ConnectThread(BluetoothDevice _device, Context _context) {
		BluetoothSocket tmp = null;
		m_context = _context;

		// Get a BluetoothSocket to connect with the given BluetoothDevice
		try {
			// UUID is the app's UUID string, also used by the server code
			tmp = _device.createInsecureRfcommSocketToServiceRecord(UUID
					.fromString("B2B8428C-0001-4560-ADB5-C983CEDAD79E"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		m_socket = tmp;
	}

	public void run() {
		// Cancel discovery because it will slow down the connection
		BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

		try {
			// Connect using the socket (blocking until connection or Exception)
			m_socket.connect();
			// Create a listener thread using the socket
			m_listenerThread = new ListenerThread(m_socket, m_context);
			m_listenerThread.start();
		} catch (IOException connectException) {
			Log.e("Connect Exception", connectException.getMessage());
			try {
				m_socket.close();
			} catch (IOException closeException) {
				Log.e("Close Exception", connectException.getMessage());
			}
		}
	}

	public void disconnect() {
		try {
			if(m_listenerThread != null)
			{
				m_listenerThread.setRun(false);
			}
			m_socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendObject(Object obj) throws IOException {
		OutputStream mmOutStream = m_socket.getOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(mmOutStream);
		out.writeObject(obj);
	}

	public BluetoothSocket getSocket() {
		return m_socket;
	}

	public void setGamingSurface(GamingSurfaceView _view) {
		m_gamingSurface = _view;
		if (m_listenerThread != null && m_listenerThread.isAlive()) {
			m_listenerThread.setGamingSurface(m_gamingSurface);
		}
	}

}