package cz.uhk.pro.places.model;

public class Place {
	private int id;
	private double longitude;
	private double latitude;
	private String title;
	public Place() {
	}	
	public Place(double longitude, double latitude, String title) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
		this.title = title;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return "Place [id=" + id + ", longitude=" + longitude + ", latitude=" + latitude + ", title=" + title + "]";
	}
	
	
}
