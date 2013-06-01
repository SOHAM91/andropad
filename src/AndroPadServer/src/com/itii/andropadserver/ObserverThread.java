/**
 * AndroPad Project - ITII CNAM Alsace - Juin 2013
 * Fabrice Latterner - Clement Troesch
 */

package com.itii.andropadserver;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.bluetooth.BluetoothStateException;

import com.itii.andropadcommon.AndroButton;
import com.itii.andropadcommon.Observer;

public class ObserverThread extends Thread implements Observer {

	private int m_gamepadIndex = -1;
	private int m_time = 1000;
	private Translator m_translator = new Translator();
	private Robot m_robot = new Robot();
	private long m_lastTime;
	private boolean m_run;

	public ObserverThread(int n, int t) throws AWTException {
		m_gamepadIndex = n;
		m_time = t;
		m_robot = new Robot();
		m_lastTime = System.currentTimeMillis();
		m_run = true;
	}

	@Override
	public void run() {
		while (m_run) {
			if (m_gamepadIndex >= 0
					&& System.currentTimeMillis() - m_lastTime > m_time) {
				m_lastTime = System.currentTimeMillis();
				AndroState state;
				try {
					AndroGamepad pad = GamepadManager.getInstance().getPad(
							m_gamepadIndex);
					state = pad.getState();
					for (Entry ent : state.getState().entrySet()) {
						AndroButton.Mapping map = (AndroButton.Mapping) ent
								.getKey();
						// If the button state last longer than m_time, repeat
						// the press action
						if (ent.getValue() == AndroButton.State.PRESSED) {
							if (GamepadManager.getInstance()
									.getPad(m_gamepadIndex).getButton(map)
									.getStateTime() > m_time) {

								int keycode = m_translator
										.getKeyEvent((AndroButton.Mapping) ent
												.getKey());
								if (keycode != -1) {
									m_robot.keyPress(keycode);
								}
							}
						}
					}
				} catch (BluetoothStateException ex) {
					Logger.getLogger(ObserverThread.class.getName()).log(
							Level.SEVERE, null, ex);
				} catch (IOException ex) {
					Logger.getLogger(ObserverThread.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
		}
	}

	@Override
	public void update(AndroButton.State state, Object obj) {
		if (obj instanceof AndroButton) {
			AndroButton button = (AndroButton) obj;
			String info = "Input du gamepad " + m_gamepadIndex + " Bouton "
					+ button.getButton().toString() + " "
					+ button.getState().toString();
			OutputController.writeLine(info,
					OutputController.MessageLevel.INPUT_INFO);
			int keycode = m_translator.getKeyEvent(button.getButton());
			if (keycode != -1) {
				if (state == AndroButton.State.PRESSED) {
					m_robot.keyPress(keycode);
				} else if (state == AndroButton.State.RELEASED) {
					m_robot.keyRelease(keycode);
				}
			}
		}
	}

	public void setRun(boolean b) {
		m_run = b;
	}

}
