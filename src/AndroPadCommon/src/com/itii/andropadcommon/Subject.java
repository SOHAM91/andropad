/**
 * AndroPad Project - ITII CNAM Alsace - Juin 2013
 * Fabrice Latterner - Clement Troesch
 */

package com.itii.andropadcommon;

public interface Subject {

	public void register(Observer obs);

	public void unregister(Observer obs);

}
