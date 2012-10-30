package br.ufpe.gprt.resources;
import java.util.ArrayList;

public class StorageManager {
	
	private static ArrayList<SensorValues> s_sensorValuesList;
	
	public static void addSensorValue (SensorValues newSensorValue ) {
		s_sensorValuesList.add(newSensorValue);
	}
	
	public SensorValues getSensorValuesByIndex (int index) {
		return s_sensorValuesList.get(index);
	}
			
	public StorageManager (){
		s_sensorValuesList = new ArrayList<SensorValues>();
	}
	

}
