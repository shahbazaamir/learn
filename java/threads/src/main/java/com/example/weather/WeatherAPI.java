package com.example.weather;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WeatherAPI {
    private static final Logger logger = LogManager.getLogger(WeatherAPI.class);

    public static void main(String[] args) throws Exception {

        String url = "https://api.open-meteo.com/v1/forecast"
                + "?latitude=28.6139"
                + "&longitude=77.2090"
                + "&current_weather=true";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        System.out.println("Status: " + response.statusCode());
        System.out.println("Body : " + response.body());
    }

    public static WeatherResponse getWeather(double latitude, double longitude)  throws IOException,InterruptedException {
        String url = "https://api.open-meteo.com/v1/forecast"
                + "?latitude=" + latitude
                + "&longitude=" + longitude
                + "&current_weather=true";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        long startTime = System.currentTimeMillis();
        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );
        long latency = System.currentTimeMillis() - startTime;
        
        logger.info("API call latency: {} ms", latency);

        ObjectMapper mapper = new ObjectMapper();
        WeatherResponse weatherResponse = mapper.readValue(response.body(), WeatherResponse.class);
        
        logger.info("Weather response: {}", weatherResponse);
        
        return weatherResponse;
    }
}
