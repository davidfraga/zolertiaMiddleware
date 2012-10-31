package br.ufpe.gprt.resources;
import java.util.ArrayList;

import br.ufpe.gprt.zolertia.device.SensorData;

public class StorageManager {
	
	private static ArrayList<SensorData> s_sensorValuesList;
	
	public static void addSensorValue (SensorData newSensorValue ) {
		s_sensorValuesList.add(newSensorValue);
	}
	
	public SensorData getSensorValuesByIndex (int index) {
		return s_sensorValuesList.get(index);
	}
			
	public StorageManager (){
		s_sensorValuesList = new ArrayList<SensorData>();
	}
	

}
