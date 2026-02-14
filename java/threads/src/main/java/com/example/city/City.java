package com.example.city;

public enum City {
    NEW_YORK(40.7128, -74.0060),
    LONDON(51.5074, -0.1278),
    TOKYO(35.6762, 139.6503),
    PARIS(48.8566, 2.3522),
    SYDNEY(-33.8688, 151.2093);
    
    private final double latitude;
    private final double longitude;
    
    City(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public double getLatitude() {
        return latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
}
