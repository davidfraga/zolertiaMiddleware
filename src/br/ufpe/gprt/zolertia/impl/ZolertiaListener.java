package br.ufpe.gprt.zolertia.impl;

import br.ufpe.gprt.zolertia.deviceCommandProxy.SerialConnection;

/**
 * Interface for sensor event subscribers
 * 
 * @author simon
 */
public interface ZolertiaListener {
	public void serialData(SerialConnection serialConnection, String line);
	
}
