// a platform serves one train line at a metro station
// actually there are two physical platforms, one for each direction

public class Platform {	
	// in Boston area, these are approximately valid:
	// The circumference of the earth along the equator is 24,901.92 miles, 
	// over 360 degrees, yields
	public final static double MILES_PER_LATITUDE_DEGREE = 69.2;
	// longitude degrees are scaled down by cosine(latitude)--
	public final static double MILES_PER_LONGITUDE_DEGREE = 51.2;
	// Using Government Center coordinates--
	public final static double BOSTON_LATITUDE = 42.359705;
	public final static double BOSTON_LONGITUDE = -71.059215;

	private String stationName;  // the combo (stationName, trainLine) uniquely ids a platform
	private String trainLine;
	private double latitude, longitude;
	private int platformId;  // unique to platform and used in graph as vertex id
	public Platform(String stationName, String trainLine, double lat, double lon, int platformId) {
		this.stationName = stationName;
		this.trainLine = trainLine;
		this.latitude = lat;
		this.longitude = lon;
		this.platformId = platformId;
	}

	public int getPlatformId() {
		return platformId;
	}
	
	public String getStationName() {
		return stationName;
	}
	public String getTrainLine() {
		return trainLine;
	}
	public double getLatitude() {
		//System.out.println("getLat for node " + getNodeId());
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	// Local coordinates in miles from Government Center, central Boston
	public double getNorthSouthMiles() {
		return (latitude-BOSTON_LATITUDE)* MILES_PER_LATITUDE_DEGREE;
	}
	public double getEastWestMiles() {
		return (longitude-BOSTON_LONGITUDE)* MILES_PER_LONGITUDE_DEGREE;
	}
}
