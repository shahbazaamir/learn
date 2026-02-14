package com.example.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherData {
    @JsonProperty("current_weather")
    private CurrentWeather currentWeather;
    
    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }
    
    public void setCurrentWeather(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }
    
    public static class CurrentWeather {
        private double temperature;
        private double windspeed;
        private int winddirection;
        private int weathercode;
        
        public double getTemperature() { return temperature; }
        public void setTemperature(double temperature) { this.temperature = temperature; }
        
        public double getWindspeed() { return windspeed; }
        public void setWindspeed(double windspeed) { this.windspeed = windspeed; }
        
        public int getWinddirection() { return winddirection; }
        public void setWinddirection(int winddirection) { this.winddirection = winddirection; }
        
        public int getWeathercode() { return weathercode; }
        public void setWeathercode(int weathercode) { this.weathercode = weathercode; }
    }
}