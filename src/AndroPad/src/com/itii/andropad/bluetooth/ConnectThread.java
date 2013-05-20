package com.itii.andropad.bluetooth;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Build;
import android.util.Log;


public class ConnectThread extends Thread {
    private final BluetoothSocket m_Socket;
    
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public ConnectThread(BluetoothDevice device, Context ctx) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
 
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
        	tmp = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString("B2B8428C-0001-4560-ADB5-C983CEDAD79E"));
           // tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("B2B8428C-0001-4560-ADB5-C983CEDAD79E"));
        } catch (IOException e) { 
        	e.printStackTrace();
        }
        m_Socket = tmp;
    }
 
	public void run() {
        // Cancel discovery because it will slow down the connection
    	BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
 
        try {
            // Connect using the socket (blocking until connection or Exception)
            m_Socket.connect();
        } catch (IOException connectException) {
            Log.e("Connect Exception", connectException.getMessage());
            try {
                m_Socket.close();
            } catch (IOException closeException) {
            	Log.e("Close Exception", connectException.getMessage());
            }
        }
    }
 
    public void disconnect() {
        try {
            m_Socket.close();
        } catch (IOException e) { 
        	e.printStackTrace();
        }
    }
    
    public void sendObject(Object obj) throws IOException {
    	 OutputStream mmOutStream = m_Socket.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream( mmOutStream );
         out.writeObject(obj);
    }
    
    public BluetoothSocket getSocket() {
    	return m_Socket;
    }
    
}