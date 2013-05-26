/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itii.andropadserver.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/**
 *
 * @author Coralie
 */
public class AndroServer extends Thread {
    private LocalDevice m_local = null;
    private UUID m_uuid;
    private String m_connectionString;
    private boolean m_run;
    
    private HashMap<Integer,AndroListen> m_listeners = new HashMap<Integer,AndroListen>();
    	
    public AndroServer() throws BluetoothStateException, IOException {
          m_uuid = new UUID("B2B8428C00014560ADB5C983CEDAD79E", false);
          //Connexion URL
          m_connectionString = "btspp://localhost:" + m_uuid +";name=AndropadServer;authenticate=false;encrypt=false;";
          //Set device discoverable
          m_local = LocalDevice.getLocalDevice();
          m_local.setDiscoverable(DiscoveryAgent.GIAC);
          m_run = true;
    }
    
    @Override
	public void run() {
        try {
            //open server url
            OutputController.writeLine("Server Started. Waiting for clients to connect...");
            StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier)Connector.open( m_connectionString );
            //Wait for client connection
            while(m_run) {
                if(GamepadManager.getInstance().getNbGamepad() > GamepadManager.getInstance().getNbActive()) {
                    OutputController.writeLine("Nb players connected :" + GamepadManager.getInstance().getNbActive());
                    
                     StreamConnection connection=streamConnNotifier.acceptAndOpen();
                     //Create thread to communicate with the client, and pass it to the GamepadManager
                     int gamepadIndex = GamepadManager.getInstance().getFirstAvailable();
                     if(gamepadIndex > -1) {
                         AndroListen al = new AndroListen(connection,gamepadIndex);
                         m_listeners.put(gamepadIndex,al);
                         al.start();
                     } else {
                         connection.close();
                     }
                     
                } else {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(AndroServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }   
            }
            //close server
            streamConnNotifier.close();
        } catch (IOException ex) {
            Logger.getLogger(AndroServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }

    public void setRun(boolean b) {
        if(!b) {
            for(AndroListen al : m_listeners.values()) {
                if(al != null) { 
                    al.close();
                }
            }
        }
        m_run = b;
    }
    
    
}
