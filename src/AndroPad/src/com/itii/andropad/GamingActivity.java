package com.itii.andropad;

import com.itii.andropad.components.GamingSurfaceView;
import com.itii.andropad.pad.Pad;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.itii.andropad.bluetooth.ConnectThread;

public class GamingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extra = getIntent().getExtras();
		String selectedPad = extra.getString("selected_pad");
		m_device = extra.getParcelable("selected_device");

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		m_connectThread = new ConnectThread(m_device,getApplicationContext());
		
		m_surfaceView = new GamingSurfaceView(this, selectedPad);
		setContentView(m_surfaceView);

		this.connectToDevice();
	}

	private void connectToDevice() {
		
        m_connectThread.run();
        if(m_connectThread.getSocket().isConnected()) {
        	m_surfaceView.setPadState(Pad.State.CONNECTED);
        } else {
        	m_surfaceView.setPadState(Pad.State.ERROR);
        	Toast.makeText(this.getApplicationContext(),"Impossible de se connecter au serveur.",Toast.LENGTH_LONG).show();
        }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gaming, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		exit();
	}

	protected void exit() {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Quitter")
				.setMessage("Voulez vous vous déconnecter ?")
				.setPositiveButton("Oui",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								m_connectThread.disconnect();
								finish();
							}

						})
				.setNegativeButton("Non",null).setCancelable(true).show();

	}
	
	public ConnectThread getConnectThread()
	{
		return m_connectThread;
	}

	private GamingSurfaceView m_surfaceView;
	
 	private ConnectThread m_connectThread;
 	
 	private BluetoothDevice m_device;
 	

}
