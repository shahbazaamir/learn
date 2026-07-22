package com.example.lock;

import com.example.city.City;
import com.example.weather.WeatherAPI;
import com.example.weather.WeatherResponse;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;


public class MyCallableThread implements Callable<List<WeatherResponse>> {

    @Override
    public List<WeatherResponse> call(){
            List<City> c = Arrays.asList(City.values());
            List<WeatherResponse> r = c.parallelStream().map(
                    city -> {
                        try {
                            return WeatherAPI.getWeather(city.getLatitude(), city.getLongitude());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        return null;
                    }
            ).collect(Collectors.toList());

        return r;
            /*
            // ML Integration: Predictive Analytics
            r.forEach(weather -> {
                if (weather != null) {
                    String prediction = WeatherPredictor.predictWeatherCondition(weather);
                    System.out.println("Predicted condition: " + prediction);
                }
            });
            
            // ML Integration: Recommendation Engine
            List<City> warmCities = CityRecommendationEngine.recommendCities(r, "warm");
            System.out.println("Recommended warm cities: " + warmCities);
            
            // ML Integration: Comfort Index Analytics
            Map<String, Double> comfortScores = WeatherPredictor.calculateComfortIndex(r);
            System.out.println("City comfort scores: " + comfortScores);
*/
    }
}
