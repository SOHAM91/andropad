/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itii.andropadserver.server;

import java.io.IOException;
import java.util.ArrayList;
import javax.bluetooth.BluetoothStateException;

import com.itii.andropadcommon.AndroButton;
import com.itii.andropadcommon.Observer;
import com.itii.andropadcommon.Subject;

/**
 *
 * @author Coralie
 */

public class GamepadManager {
    
    private static GamepadManager m_gm = null;
    private ArrayList<AndroGamepad> m_list = new ArrayList<AndroGamepad>();
    private boolean m_run = false;
    private int m_nbGamepad = 0;
    private AndroServer m_androServer;
    
    public static synchronized GamepadManager getInstance(int n) throws BluetoothStateException, IOException {
         if(m_gm == null) {
             m_gm = new GamepadManager(n);
         }
         return m_gm;
    }
    
         
    public int getFirstAvailable() {
        int i = 0;
        for(AndroGamepad ag : m_list) {
            if(!ag.isConnected()) {
                 return i;
            }
            i++;
        }
        return -1;
   }
    
    public int getNbActive() {
       int i = 0;
       for(AndroGamepad ag : m_list) {
            if(ag.isConnected()) {
                 i++;
            }
        }
        return i;
    }
    
    public int getNbGamepad() {
        return m_list.size();
    }
   
   public boolean isFull() {
       for(AndroGamepad ag : m_list) {
           if (!ag.isConnected()) {
               return false;
           }
       }
       return true;
   }
     

    public static synchronized GamepadManager getInstance() throws BluetoothStateException, IOException {
         if(m_gm == null) {
             m_gm = new GamepadManager(2);
         }
         return m_gm;
    }    
    
    public AndroGamepad getPad(int index) {
        if(m_list.size() > index) {
            return m_list.get(index);
        } else {
            return null;
        }
    }
    
    public void observeGamepad(int gamepadIndex, Observer obs) {
        if(m_list.size() > gamepadIndex) {
            for(AndroButton ab : m_list.get(gamepadIndex).getButtonsList().values()) {
                ab.register(obs);
             }
        }
    }
    
    private GamepadManager(int n) {
        //Create gamepads
        for(int i = 0; i < n; i++) {
            m_list.add(new AndroGamepad());
        }
        m_nbGamepad = n;
    }
    
    public void start() throws BluetoothStateException, IOException {
        if(!m_run) {
            //Launch the connection manager 
            m_androServer = new AndroServer();
            m_androServer.start();
        }
    }
    
    public void stop() {
         m_androServer.setRun(false);
        OutputController.writeLine("Arrêt du serveur");
    }
    

}
