/**
 * AndroPad Project - ITII CNAM Alsace - Juin 2013
 * Fabrice Latterner - Clement Troesch
 */

package com.itii.andropadcommon;

public interface Observer {
	public void update(AndroButton.State state, Object obj);
}
