package com.itii.andropad;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PadSettingsActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pad_settings);
		
		Button button = null;

		button = (Button) findViewById(R.id.btn_save_pad);
		button.setOnClickListener(this);
		
		button = (Button) findViewById(R.id.btn_delete_pad);
		button.setOnClickListener(this);
		
		button = (Button) findViewById(R.id.btn_exit_pad);
		button.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pad_settings, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		
		if(view == findViewById(R.id.btn_save_pad))
		{
			Intent intent = new Intent();
			intent.putExtra("action", "save");
			setResult(RESULT_OK, intent);
			finish();
		}
		else if(view == findViewById(R.id.btn_delete_pad))
		{
			Intent intent = new Intent();
			intent.putExtra("action", "delete");
			setResult(RESULT_OK, intent);
			finish();
		}
		else if(view == findViewById(R.id.btn_exit_pad))
		{
			Intent intent = new Intent();
			intent.putExtra("action", "exit");
			setResult(RESULT_OK, intent);
			finish();
		}
		
	}

}
