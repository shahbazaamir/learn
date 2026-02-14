package com.example.ml;

import com.example.weather.WeatherResponse;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class WeatherPredictor {
    
    public static String predictWeatherCondition(WeatherResponse weather) {
        double temp = weather.currentWeather.temperature;
        double windSpeed = weather.currentWeather.windspeed;
        int weatherCode = weather.currentWeather.weathercode;
        
        // Simple rule-based ML model
        if (weatherCode >= 80 && weatherCode <= 99) return "Rainy";
        if (temp > 25 && windSpeed < 10) return "Pleasant";
        if (temp < 0) return "Freezing";
        if (windSpeed > 20) return "Windy";
        
        return "Moderate";
    }
    
    public static Map<String, Double> calculateComfortIndex(List<WeatherResponse> weatherData) {
        Map<String, Double> cityComfort = new HashMap<>();
        
        for (WeatherResponse weather : weatherData) {
            if (weather != null && weather.currentWeather != null) {
                double comfort = calculateComfort(weather.currentWeather.temperature, 
                                               weather.currentWeather.windspeed);
                cityComfort.put(weather.latitude + "," + weather.longitude, comfort);
            }
        }
        
        return cityComfort;
    }
    
    private static double calculateComfort(double temp, double wind) {
        // ML-based comfort scoring (0-100)
        double tempScore = Math.max(0, 100 - Math.abs(temp - 22) * 3);
        double windScore = Math.max(0, 100 - wind * 2);
        return (tempScore + windScore) / 2;
    }
}