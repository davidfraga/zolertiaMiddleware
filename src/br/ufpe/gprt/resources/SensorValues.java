package br.ufpe.gprt.resources;

public class SensorValues {
	
	private static int s_temperature;
	private static int s_batteryInformation;
	private static int s_rssi;
	private static int s_atualChannel;
	private static int s_hopCount;
	private static String[] s_dataString;
	
	public static int getTemperature() {
		return s_temperature;
	}
	
	public static int getBatteryInformation() {
		return s_batteryInformation;
	}
	
	public static int getRssi() {
		return s_rssi;
	}
	
	public static int getAtualChannel() {
		return s_atualChannel;
	}
	
	public static int getHopCount() {
		return s_hopCount;
	}
	
	public SensorValues (String dataString){
		
		s_dataString = dataString.split(" ");
		
		String stringTemperature = s_dataString[24];
		String stringBattery = s_dataString[21];
		String stringAtualChannel = s_dataString[27];
		String stringRssi = s_dataString[26];
		String stringHops = s_dataString[6];
		
		s_temperature = Integer.parseInt(stringTemperature);
		s_batteryInformation = Integer.parseInt(stringBattery);
		s_rssi = Integer.parseInt(stringRssi);
		s_atualChannel = Integer.parseInt(stringAtualChannel);
		s_hopCount = Integer.parseInt(stringHops);
	}
	
	

}
