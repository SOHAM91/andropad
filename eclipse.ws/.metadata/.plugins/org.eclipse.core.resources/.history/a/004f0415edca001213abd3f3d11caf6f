package com.itii.andropad;

import com.itii.andropad.components.EditingSurfaceView;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class EditingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extra = getIntent().getExtras();
		String padName = extra.getString("selected_pad");

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		m_surfaceView = new EditingSurfaceView(this, padName);
		
		
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(m_surfaceView);
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.editing, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_save_pad:
			m_surfaceView.savePad();
			return true;
		case R.id.action_delete_pad:
			delete();
			return true;
		case R.id.action_exit:
			exit();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if(requestCode == s_SHOW_EDIT_BUTTON)
			{
				Bundle extras = data.getExtras();
				String action = extras.getString("action");
				if ("edit".equals(action)) {
					int type = extras.getInt("type");
					int size = extras.getInt("size");
					int mapping = extras.getInt("mapping");
					m_surfaceView.updateButton(type, size, mapping);
				} else if ("delete".equals(action)) {
					m_surfaceView.deleteButton();
				}
			}
			else if(requestCode == s_SHOW_PAD_SETTINGS)
			{
				Bundle extras = data.getExtras();
				String action = extras.getString("action");
				if ("save".equals(action)) {
					m_surfaceView.savePad();
				}
				else if ("delete".equals(action)) {
					this.delete();
				}
				else if ("exit".equals(action)) {
					this.exit();
				}
			}
			
		}

	}
	
	@Override
	public void onBackPressed() {
		exit();
	}

	protected void exit() {
		if (!m_surfaceView.isSaved()) {
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Quitter")
					.setMessage("Voulez vous sauvegarder?")
					.setPositiveButton("Oui",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									m_surfaceView.savePad();
									finish();
								}

							})
					.setNegativeButton("Non",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							}).setCancelable(true).show();
		} else {
			finish();
		}

	}

	protected void delete() {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Supprimer")
				.setMessage("Voulez vous supprimer ce pad ?")
				.setPositiveButton("Oui",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								m_surfaceView.deletePad();
								finish();
							}

						}).setNegativeButton("Non", null).setCancelable(true)
				.show();
	}

	private EditingSurfaceView m_surfaceView;
	public static int s_SHOW_EDIT_BUTTON = 1;
	public static int s_SHOW_PAD_SETTINGS = 2;

}
