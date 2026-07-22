package com.example.lock;

import com.example.weather.WeatherResponse;

import java.util.List;

public class CityCompare {

    public String compare(List<WeatherResponse> r) {
        /*Map<String, String> comparison = r.stream().collect(
                Collectors.toMap(
                         w -> w.latitude,
                        w -> {
                            WeatherResponse.CurrentWeather weather = w.currentWeather;
                            if (weather.temperature > 25) {
                                return "Warm";
                            } else if (weather.temperature < 10) {
                                return "Cold";
                            } else {
                                return "Moderate";
                            }
                        }
                )
        );
        return comparison;*/
        String warmest = null;
        for ( WeatherResponse w : r){

            double ctemp = -2700;
            if (w.currentWeather.temperature > ctemp){
                ctemp =  w.currentWeather.temperature;
                warmest = String.valueOf(w.latitude);
            }

        }
        return warmest;
    }
}
