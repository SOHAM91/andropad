package com.itii.andropad;

import java.util.ArrayList;
import java.util.Set;

import com.itii.andropad.bluetooth.ConnectThread;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class SelectDeviceActivity extends Activity implements OnItemClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_device);

		Bundle extra = getIntent().getExtras();
		m_selectedPad = extra.getString("selected_pad");
		
		m_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, new ArrayList<String>());
		
		m_devices = new ArrayList<BluetoothDevice>();
		
		m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (m_bluetoothAdapter != null) {
			Log.d("bl_deb", "Adapter Ok");
			
			if (!m_bluetoothAdapter.isEnabled()) {
				Log.d("bl_deb", "Adapter not Enabled");
			    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			    startActivityForResult(enableBtIntent, s_REQUEST_ENABLE_BT);
			} else {
				Log.d("bl_deb", "Adapter Enabled");
				this.displayDevices();
			}
		}
		else
		{
			TextView text = (TextView) findViewById(R.id.txt_select_device);
			text.setText("Votre périphérique n'est pas compatible.");
		}
		
		

	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == s_REQUEST_ENABLE_BT){
			if (resultCode == RESULT_OK){
				this.displayDevices();
			}
			if(resultCode == RESULT_CANCELED){
				finish();
			}
		}
	}
	
	public void displayDevices()
	{
		ListView list = (ListView) this.findViewById(R.id.list_select_device);
		list.setAdapter(m_adapter);
		list.setOnItemClickListener(this);
		
		m_bluetoothAdapter.startDiscovery();
		Set<BluetoothDevice> devices = m_bluetoothAdapter.getBondedDevices();
	    for (BluetoothDevice device : devices) {
	    	Log.d("Device", "Device");
	    	m_adapter.add(device.getName());
	    	m_devices.add(device);
	    }
	    
	    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(m_receiver, filter); // Don't forget to unregister during onDestroy
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		m_bluetoothAdapter.cancelDiscovery();
		Intent intent = new Intent(this,GamingActivity.class);
		intent.putExtra("selected_pad", m_selectedPad);
		intent.putExtra("selected_device", m_devices.get(position));
		startActivityForResult(intent, 0);
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(m_receiver);
	}
	
	private final BroadcastReceiver m_receiver = new BroadcastReceiver() {
		@Override
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        // When discovery finds a device
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            // Get the BluetoothDevice object from the Intent
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            m_adapter.add(device.getName());
	            m_devices.add(device);
	        }
	    }
	};

	private ArrayAdapter<String> m_adapter;
	private ArrayList<BluetoothDevice> m_devices;
	private String m_selectedPad;
	private BluetoothAdapter m_bluetoothAdapter;
	
	private static final int s_REQUEST_ENABLE_BT = 1;

}
