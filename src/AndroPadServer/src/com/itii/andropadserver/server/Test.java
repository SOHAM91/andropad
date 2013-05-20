/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itii.andropadserver.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;

/**
 *
 * @author Coralie
 */
public class Test {
    
    
    public static void main(String args[]) {
        try {
            GamepadManager gm = GamepadManager.getInstance(2);
            ObserverTest ot = new ObserverTest(0);
            gm.observeGamepad(0, ot);
            gm.start();
            
        } catch (BluetoothStateException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
