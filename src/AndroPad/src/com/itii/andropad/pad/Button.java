/**
 * AndroPad Project - ITII CNAM Alsace - Juin 2013
 * Fabrice Latterner - Clement Troesch
 */

package com.itii.andropad.pad;

import java.io.Serializable;
import java.util.Arrays;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Picture;
import android.graphics.Rect;
import android.util.Log;

import com.itii.andropad.R;
import com.itii.andropadcommon.AndroButton;

public class Button implements Serializable {

	private static final long serialVersionUID = -4620388757840729059L;

	public Button(Pad pad, int posX, int posY, int type, int size, int mapping) {
		m_pad = pad;
		m_posX = posX;
		m_posY = posY;
		m_typeIndex = type;
		m_picture = m_pad.getSVG(m_typeIndex).getPicture();
		m_downPicture = m_pad.getSVG((m_typeIndex < 4) ? 8 : m_typeIndex + 5)
				.getPicture();
		m_scale = s_BUTTON_SIZES[size];
		m_mappingIndex = mapping;
		m_pointerIndex = -1;
		this.updatePosition();
	}

	public void draw(Canvas canvas, Context context) {
		if (!isPressed() && (m_pad.isConnected()))
			canvas.drawPicture(m_picture, m_positionRectangle);
		else
			canvas.drawPicture(m_downPicture, m_positionRectangle);

		Paint textPaint = new Paint();
		textPaint.setTextAlign(Align.CENTER);
		int size = context.getResources().getDimensionPixelSize(
				R.dimen.size_button_name);
		textPaint.setTextSize(size);
		canvas.drawText(AndroButton.Mapping.values()[m_mappingIndex].name(),
				m_posX, m_posY + (size / 2), textPaint);
	}

	public boolean pressButton(int pointerIndex, float posX, float posY) {
		if (m_positionRectangle.contains((int) posX, (int) posY)) {
			m_pointerIndex = pointerIndex;
			m_pad.sendButtonState(new AndroButton(
					AndroButton.Mapping.values()[m_mappingIndex],
					AndroButton.State.PRESSED));
			return true;
		}

		return false;
	}

	public void releaseButton(int pointerIndex, float posX, float posY) {
		if (pointerIndex == m_pointerIndex
				&& m_positionRectangle.contains((int) posX, (int) posY)) {
			m_pointerIndex = -1;
			m_pad.sendButtonState(new AndroButton(
					AndroButton.Mapping.values()[m_mappingIndex],
					AndroButton.State.RELEASED));
		}
	}

	public boolean moveFinger(int pointerIndex, float posX, float posY) {
		if (pointerIndex == m_pointerIndex
				&& !m_positionRectangle.contains((int) posX, (int) posY)) {
			m_pointerIndex = -1;
			m_pad.sendButtonState(new AndroButton(
					AndroButton.Mapping.values()[m_mappingIndex],
					AndroButton.State.RELEASED));
		} else if (m_pointerIndex == -1
				&& m_positionRectangle.contains((int) posX, (int) posY)) {
			m_pointerIndex = pointerIndex;
			m_pad.sendButtonState(new AndroButton(
					AndroButton.Mapping.values()[m_mappingIndex],
					AndroButton.State.PRESSED));
			return true;
		}
		return false;

	}

	public boolean dragging(int posX, int posY) {
		if (isPressed()) {
			m_posX = posX;
			m_posY = posY;
			this.updatePosition();
			return true;
		}
		return false;
	}

	public boolean isPressed() {
		return m_pointerIndex >= 0;
	}

	public int getTypeIndex() {
		return m_typeIndex;
	}

	public int getSizeIndex() {
		return Arrays.asList(s_BUTTON_SIZES).indexOf(m_scale);
	}

	public int getMappingIndex() {
		return m_mappingIndex;
	}

	public void updateButton(int typeIndex, int sizeIndex, int mappingIndex) {
		m_scale = Float.valueOf(s_BUTTON_SIZES[sizeIndex]);
		m_typeIndex = typeIndex;
		m_mappingIndex = mappingIndex;
		m_picture = m_pad.getSVG(m_typeIndex).getPicture();
		m_downPicture = m_pad.getSVG((m_typeIndex < 4) ? 8 : m_typeIndex + 5)
				.getPicture();
		this.updatePosition();

	}

	private void updatePosition() {
		int size = (int) (s_BUTTON_SIZE * m_scale * s_DEFAULT_SCALE);
		int newPosX = m_posX - (int) (size / 2);
		int newPosY = m_posY - (int) (size / 2);
		m_positionRectangle = new Rect(newPosX, newPosY, newPosX + size,
				newPosY + size);
	}

	public void resetState() {
		if(m_pointerIndex != -1)
		{
			m_pad.sendButtonState(new AndroButton(
				AndroButton.Mapping.values()[m_mappingIndex],
				AndroButton.State.RELEASED));
		}
		m_pointerIndex = -1;
	}

	@Override
	public String toString() {
		return "<button " + "posX=\"" + m_posX + "\" " + "posY=\"" + m_posY
				+ "\" " + "size=\"" + getSizeIndex() + "\" " + "type=\""
				+ m_typeIndex + "\" " + "mapping=\"" + m_mappingIndex
				+ "\" />\n";
	}

	private Pad m_pad;
	private Picture m_picture;
	private Picture m_downPicture;
	private Rect m_positionRectangle;
	private int m_pointerIndex;
	private float m_scale;
	private int m_posX, m_posY;

	private int m_typeIndex;
	private int m_mappingIndex;

	private static int s_BUTTON_SIZE = 500;
	private static float s_DEFAULT_SCALE = 0.20f;

	public static String[] s_BUTTON_TYPES = { "ROND BLEU", "ROND ROUGE",
			"ROND JAUNE", "ROND VERT", "FLECHE GAUCHE", "FLECHE DROITE",
			"FLECHE BAS", "FLECHE HAUT" };

	public static Float[] s_BUTTON_SIZES = { 0.5f, 1f, 1.5f, 2f, 2.5f, 3f,
			3.5f, 4f, 4.5f, 5f };

}
