/**
 * AndroPad Project - ITII CNAM Alsace - Juin 2013
 * Fabrice Latterner - Clement Troesch
 */

package com.itii.andropad.components;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

import com.itii.andropad.pad.Pad;
import com.itii.andropad.pad.Pad.State;

public class GamingSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback, OnTouchListener {

	public GamingSurfaceView(Context context, String padPath) {
		super(context);
		m_pad = new Pad(context, padPath, Pad.Mode.PLAYING);
		this.getHolder().addCallback(this);
		this.setOnTouchListener(this);
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

		final int action = event.getAction();
		final int mask = action & MotionEvent.ACTION_MASK;
		final int pointerIndex = event.getActionIndex();

		if (mask == MotionEvent.ACTION_UP
				|| mask == MotionEvent.ACTION_POINTER_UP) {
			m_pad.releaseButton(pointerIndex, event.getX(pointerIndex),
					event.getY(pointerIndex));
			
			if (event.getPointerCount() == 1) {
				m_pad.resetButtonStates();
			}
		} else if (mask == MotionEvent.ACTION_DOWN
				|| mask == MotionEvent.ACTION_POINTER_DOWN) {
			m_pad.pressButton(pointerIndex, event.getX(pointerIndex),
					event.getY(pointerIndex));
		} 
		if (mask == MotionEvent.ACTION_MOVE) {
			m_pad.moveFinger(pointerIndex, event.getX(pointerIndex),
					event.getY(pointerIndex));
		}

		

		this.requestRender();
		return true;
	}

	public void requestRender() {
		Canvas canvas = this.getHolder().lockCanvas();
		if (canvas == null) {
			Log.e("GamingSurfaceView", "Cannot draw onto the canvas.");
		} else {
			this.drawPad(canvas);
			this.getHolder().unlockCanvasAndPost(canvas);
		}
	}

	private void drawPad(final Canvas canvas) {
		canvas.drawRGB(220, 220, 220);
		m_pad.draw(canvas);

	}

	public void setPadState(State state) {
		m_pad.setState(state);
	}

	public Pad getPad() {
		return m_pad;
	}

	private Pad m_pad;

}
