package com.example.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    @JsonProperty("current_weather")
    public CurrentWeather currentWeather;
    
    public double latitude;
    public double longitude;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CurrentWeather {
        public double temperature;
        public double windspeed;
        public int winddirection;
        public int weathercode;
        public String time;
        
        @JsonProperty("is_day")
        public int isDay;
        
        @Override
        public String toString() {
            return "CurrentWeather{temperature=" + temperature + ", windspeed=" + windspeed + ", time='" + time + "'}";
        }
    }
    
    @Override
    public String toString() {
        return "WeatherResponse{latitude=" + latitude + ", longitude=" + longitude + ", currentWeather=" + currentWeather + "}";
    }
}

