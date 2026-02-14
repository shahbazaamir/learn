package com.example.ml;

import com.example.city.City;
import com.example.weather.WeatherResponse;
import java.util.*;
import java.util.stream.Collectors;

public class CityRecommendationEngine {
    
    public static List<City> recommendCities(List<WeatherResponse> weatherData, String preference) {
        Map<City, Double> cityScores = new HashMap<>();
        City[] cities = City.values();
        
        for (int i = 0; i < weatherData.size() && i < cities.length; i++) {
            WeatherResponse weather = weatherData.get(i);
            if (weather != null) {
                double score = calculateScore(weather, preference);
                cityScores.put(cities[i], score);
            }
        }
        
        return cityScores.entrySet().stream()
                .sorted(Map.Entry.<City, Double>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
    
    private static double calculateScore(WeatherResponse weather, String preference) {
        double temp = weather.currentWeather.temperature;
        double wind = weather.currentWeather.windspeed;
        
        return switch (preference.toLowerCase()) {
            case "warm" -> Math.max(0, temp - 15) * 2;
            case "cool" -> Math.max(0, 25 - temp) * 2;
            case "calm" -> Math.max(0, 20 - wind) * 3;
            default -> (temp + 20 - wind) / 2;
        };
    }
}