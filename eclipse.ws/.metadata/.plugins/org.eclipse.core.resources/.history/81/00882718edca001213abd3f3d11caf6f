package com.itii.andropad;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.Rect;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener, SurfaceHolder.Callback {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	    
	    SurfaceView surface = (SurfaceView) findViewById(R.id.surface_logo);
	    surface.getHolder().addCallback(this);

		Button button = null;
		
		//Play
		button = (Button) findViewById(R.id.btn_select_pad);
		button.setOnClickListener(this);
		
		//Edit
		button = (Button) findViewById(R.id.btn_edit_pad);
		button.setOnClickListener(this);
		
		//About
		button = (Button) findViewById(R.id.btn_about);
		button.setOnClickListener(this);
				
		//Exit
		button = (Button) findViewById(R.id.btn_exit);
		button.setOnClickListener(this);

	}
	
	@Override
	public void onClick(View view) {
		Intent intent = null;
		
		if(view == findViewById(R.id.btn_select_pad))
		{
			intent = new Intent(view.getContext(),SelectPadActivity.class);
			intent.putExtra("context", "gaming");
		}
		else if(view == findViewById(R.id.btn_edit_pad))
		{
			intent = new Intent(view.getContext(),SelectPadActivity.class);
			intent.putExtra("context", "editing");
		}
		else if(view == findViewById(R.id.btn_about))
		{
			intent = new Intent(view.getContext(),AboutActivity.class);
		}
		else if(view == findViewById(R.id.btn_exit))
		{
			Log.d("exit", "exit");
			System.exit(0);
		}
		
		startActivityForResult(intent, 0);
		
	}
	
	public void renderLogo()
	{
		
		SVG svg = SVGParser.getSVGFromResource(getResources(), R.raw.logo);
	    Picture picture = svg.getPicture();
	    
	    SurfaceView surface = (SurfaceView) findViewById(R.id.surface_logo);
	    Canvas canvas = surface.getHolder().lockCanvas();   
		if (canvas == null) {
			Log.e("MainActivity", "Cannot draw onto the canvas.");
		} else {
			canvas.drawRGB(255, 255, 255);
			int width = canvas.getWidth();
			int height = canvas.getHeight();
			float ratio = (float)picture.getHeight()/picture.getWidth();
			if(picture.getHeight() > height)
			{
				canvas.drawPicture(picture,new Rect(0,0, width,(int)(width*ratio)));
			}
			else
			{
				ratio = (float)picture.getWidth()/picture.getHeight();
				canvas.translate((width/2)-(ratio*height)/2, 0);
				canvas.drawPicture(picture,new Rect(0,0, (int)(height*ratio),height));
			}
			
			surface.getHolder().unlockCanvasAndPost(canvas);
		}

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		this.renderLogo();
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		this.renderLogo();
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

}
