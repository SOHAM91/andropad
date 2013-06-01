/**
 * AndroPad Project - ITII CNAM Alsace - Juin 2013
 * Fabrice Latterner - Clement Troesch
 */

package com.itii.andropad.bluetooth;

import java.io.InputStream;
import java.io.ObjectInputStream;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Vibrator;

import com.itii.andropad.components.GamingSurfaceView;
import com.itii.andropad.pad.Pad;
import com.itii.andropadcommon.Player;
import com.itii.andropadcommon.Vibration;

public class ListenerThread extends Thread {

	private BluetoothSocket m_socket;
	private boolean m_run;
	private Context m_context;
	private GamingSurfaceView m_gamingSurface;

	public ListenerThread(BluetoothSocket _socket, Context _context) {
		m_socket = _socket;
		m_context = _context;
	}

	@Override
	public void run() {
		m_run = true;
		while (m_run) {
			try {
				InputStream inStream = m_socket.getInputStream();
				ObjectInputStream in = new ObjectInputStream(inStream);
				Object obj = in.readObject();
				if (obj != null) {
					if (obj instanceof Vibration) {
						Vibration vib = (Vibration) obj;
						Vibrator vibrator = (Vibrator) m_context
								.getSystemService(Context.VIBRATOR_SERVICE);
						vibrator.vibrate(vib.getDelay());
					} else if (obj instanceof Player) {

						Player player = (Player) obj;
						if (m_gamingSurface != null) {
							Pad pad = m_gamingSurface.getPad();
							pad.setPlayerName(player.getName());
							m_gamingSurface.requestRender();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setRun(boolean b) {
		m_run = b;
	}

	public void setGamingSurface(GamingSurfaceView _view) {
		m_gamingSurface = _view;
	}

}
