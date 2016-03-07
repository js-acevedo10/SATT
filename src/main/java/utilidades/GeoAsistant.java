package utilidades;

public class GeoAsistant {

	public static int R = 6371000; 

	public static double getDistanceBetween(double lat1, double lng1, double lat2, double lng2){
		double φ1 = Math.toRadians(lat1);
		double φ2 = Math.toRadians(lat2);
		
		double Δφ = Math.toRadians(lat2-lat1);
		double Δλ = Math.toRadians(lng2-lng1);

		double a = Math.sin(Δφ/2) * Math.sin(Δφ/2) + 
				Math.cos(φ1) * Math.cos(φ2) *
				Math.sin(Δλ/2) * Math.sin(Δλ/2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

		return R * c;
	}
}
