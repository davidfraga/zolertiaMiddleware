package br.ufpe.gprt.zolertia.impl;

import br.ufpe.gprt.zolertia.comm.SerialConnection;

/**
 * Interface for sensor event subscribers
 * 
 * @author simon
 */
public interface ZolertiaListener {

	public void updateTemperature(String degreesCelsius);

	public void serialData(SerialConnection serialConnection, String line);
	
}
