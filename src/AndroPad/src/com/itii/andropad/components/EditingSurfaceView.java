/**
 * AndroPad Project - ITII CNAM Alsace - Juin 2013
 * Fabrice Latterner - Clement Troesch
 */

package com.itii.andropad.components;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;

import com.itii.andropad.EditButtonActivity;
import com.itii.andropad.EditingActivity;
import com.itii.andropad.PadSettingsActivity;
import com.itii.andropad.R;
import com.itii.andropad.pad.Button;
import com.itii.andropad.pad.Pad;

public class EditingSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback, OnTouchListener, OnLongClickListener {

	public EditingSurfaceView(EditingActivity activity, String padPath) {
		super(activity);
		m_pad = new Pad(activity, padPath, Pad.Mode.EDITING);
		m_dragged = false;
		m_activity = activity;
		m_posX = m_posY = 0;
		m_menuBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.menu);
		this.getHolder().addCallback(this);
		this.setOnTouchListener(this);
		this.setOnLongClickListener(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		this.requestRender();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		boolean eventHandled = false;
		float oldPosX = m_posX;
		float oldPosY = m_posY;
		m_posX = event.getX();
		m_posY = event.getY();
		if (event.getAction() == MotionEvent.ACTION_UP) {
			m_pad.releaseButton(0, m_posX, m_posY);
			m_dragged = false;
		} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
			this.pressMenu();
			m_pad.pressButton(0, m_posX, m_posY);

		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			int deltaX = Math.abs((int) m_posX - (int) oldPosX);
			int deltaY = Math.abs((int) m_posY - (int) oldPosY);
			if (deltaX > 2 || deltaY > 2) // Sensitivity
			{
				m_isSaved = false;
				m_pad.dragging(m_posX, m_posY);
				m_dragged = true;
				eventHandled = true;
				Log.d("Action move", "Action move");
			}
		}
		this.requestRender();
		return eventHandled;
	}

	@Override
	public boolean onLongClick(View view) {
		Log.d("Long Click", "Long Click: " + m_dragged);
		m_isSaved = false;
		if (m_pad.hasSelectedButton() && !m_dragged) {
			Intent intent = new Intent(view.getContext(),
					EditButtonActivity.class);
			Button button = m_pad.getSelectedButton();

			intent.putExtra("mode", "edit");
			intent.putExtra("type", button.getTypeIndex());
			intent.putExtra("size", button.getSizeIndex());
			intent.putExtra("mapping", button.getMappingIndex());

			m_activity.startActivityForResult(intent,
					EditingActivity.s_SHOW_EDIT_BUTTON);
			return true;
		} else if (!m_dragged) {
			m_pad.createButton(m_posX, m_posY);

			Intent intent = new Intent(view.getContext(),
					EditButtonActivity.class);
			Button button = m_pad.getSelectedButton();

			intent.putExtra("mode", "create");
			intent.putExtra("type", button.getTypeIndex());
			intent.putExtra("size", button.getSizeIndex());
			intent.putExtra("mapping", button.getMappingIndex());

			m_activity.startActivityForResult(intent,
					EditingActivity.s_SHOW_EDIT_BUTTON);
			return true;
		}

		return false;
	}

	public void savePad() {
		m_pad.save();
		m_isSaved = true;
	}

	private void requestRender() {
		Canvas canvas = this.getHolder().lockCanvas();
		if (canvas == null) {
			Log.e("GamingSurfaceView", "Cannot draw onto the canvas.");
		} else {
			this.drawPad(canvas);
			this.drawMenu(canvas);
			this.getHolder().unlockCanvasAndPost(canvas);
		}
	}

	private void drawPad(final Canvas canvas) {
		canvas.drawRGB(220, 220, 220);
		m_pad.draw(canvas);
	}

	private void drawMenu(final Canvas canvas) {
		Paint paint = new Paint();
		paint.setAlpha(100);
		canvas.drawBitmap(m_menuBitmap, getWidth() - m_menuBitmap.getWidth(),
				getHeight() - m_menuBitmap.getHeight(), paint);
	}

	private boolean pressMenu() {
		boolean res = new Rect(this.getWidth() - m_menuBitmap.getWidth(),
				this.getHeight() - m_menuBitmap.getHeight(), this.getWidth(),
				this.getHeight()).contains((int) m_posX, (int) m_posY);
		if (res) {
			Intent intent = new Intent(m_activity, PadSettingsActivity.class);
			m_activity.startActivityForResult(intent,
					EditingActivity.s_SHOW_PAD_SETTINGS);
		}
		return res;
	}

	public void updateButton(int typeIndex, int sizeIndex, int mappingIndex) {
		m_pad.getSelectedButton().updateButton(typeIndex, sizeIndex,
				mappingIndex);
		this.requestRender();
	}

	public void deleteButton() {
		m_pad.deleteSelectedButton();
		this.requestRender();
	}

	public void deletePad() {
		m_pad.delete();
	}

	public boolean isSaved() {
		return m_isSaved;
	}

	private Pad m_pad;
	private boolean m_dragged;
	private EditingActivity m_activity;
	private float m_posX, m_posY;
	private boolean m_isSaved;
	private Bitmap m_menuBitmap;

}
