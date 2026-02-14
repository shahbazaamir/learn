
## with stream
- start time : 01:07:05.788
- end time : 01:07:10.252
-  total time : 00:00:04:464
```log 
/opt/homebrew/opt/java/libexec/openjdk.jdk/Contents/Home/bin/java -javaagent:/Applications/IntelliJ IDEA CE.app/Contents/lib/idea_rt.jar=59637 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath /Users/zainabfirdaus/git/learn/java/threads/target/classes:/Users/zainabfirdaus/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.15.2/jackson-databind-2.15.2.jar:/Users/zainabfirdaus/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.15.2/jackson-annotations-2.15.2.jar:/Users/zainabfirdaus/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.15.2/jackson-core-2.15.2.jar:/Users/zainabfirdaus/.m2/repository/org/apache/logging/log4j/log4j-core/2.20.0/log4j-core-2.20.0.jar:/Users/zainabfirdaus/.m2/repository/org/apache/logging/log4j/log4j-api/2.20.0/log4j-api-2.20.0.jar com.example.lock.LockDemo
01:07:05.788 [Thread-0] INFO  com.example.weather.WeatherAPI - API call latency: 1125 ms
01:07:05.872 [Thread-0] INFO  com.example.weather.WeatherAPI - Weather response: WeatherResponse{latitude=40.710335, longitude=-73.99309, currentWeather=CurrentWeather{temperature=10.2, windspeed=12.1, time='2025-11-21T19:30'}}
01:07:07.016 [Thread-0] INFO  com.example.weather.WeatherAPI - API call latency: 1143 ms
01:07:07.020 [Thread-0] INFO  com.example.weather.WeatherAPI - Weather response: WeatherResponse{latitude=51.5, longitude=-0.120000124, currentWeather=CurrentWeather{temperature=3.5, windspeed=5.3, time='2025-11-21T19:30'}}
01:07:08.026 [Thread-0] INFO  com.example.weather.WeatherAPI - API call latency: 1004 ms
01:07:08.029 [Thread-0] INFO  com.example.weather.WeatherAPI - Weather response: WeatherResponse{latitude=35.7, longitude=139.625, currentWeather=CurrentWeather{temperature=8.2, windspeed=4.1, time='2025-11-21T19:30'}}
01:07:09.163 [Thread-0] INFO  com.example.weather.WeatherAPI - API call latency: 1132 ms
01:07:09.166 [Thread-0] INFO  com.example.weather.WeatherAPI - Weather response: WeatherResponse{latitude=48.86, longitude=2.3599997, currentWeather=CurrentWeather{temperature=1.9, windspeed=5.7, time='2025-11-21T19:30'}}
01:07:10.248 [Thread-0] INFO  com.example.weather.WeatherAPI - API call latency: 1080 ms
01:07:10.252 [Thread-0] INFO  com.example.weather.WeatherAPI - Weather response: WeatherResponse{latitude=-33.75, longitude=151.125, currentWeather=CurrentWeather{temperature=17.0, windspeed=3.2, time='2025-11-21T19:30'}}

 

```

### Parallel stream
- start time : 01:11:47.558
- end time : 01:11:47.644
-  total time : 00:00:00.086
```log
/opt/homebrew/opt/java/libexec/openjdk.jdk/Contents/Home/bin/java -javaagent:/Applications/IntelliJ IDEA CE.app/Contents/lib/idea_rt.jar=59653 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath /Users/zainabfirdaus/git/learn/java/threads/target/classes:/Users/zainabfirdaus/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.15.2/jackson-databind-2.15.2.jar:/Users/zainabfirdaus/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.15.2/jackson-annotations-2.15.2.jar:/Users/zainabfirdaus/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.15.2/jackson-core-2.15.2.jar:/Users/zainabfirdaus/.m2/repository/org/apache/logging/log4j/log4j-core/2.20.0/log4j-core-2.20.0.jar:/Users/zainabfirdaus/.m2/repository/org/apache/logging/log4j/log4j-api/2.20.0/log4j-api-2.20.0.jar com.example.lock.LockDemo
01:11:47.558 [Thread-0] INFO  com.example.weather.WeatherAPI - API call latency: 1102 ms
01:11:47.558 [ForkJoinPool.commonPool-worker-2] INFO  com.example.weather.WeatherAPI - API call latency: 1103 ms
01:11:47.558 [ForkJoinPool.commonPool-worker-3] INFO  com.example.weather.WeatherAPI - API call latency: 1102 ms
01:11:47.558 [ForkJoinPool.commonPool-worker-1] INFO  com.example.weather.WeatherAPI - API call latency: 1103 ms
01:11:47.558 [ForkJoinPool.commonPool-worker-4] INFO  com.example.weather.WeatherAPI - API call latency: 1103 ms
01:11:47.644 [ForkJoinPool.commonPool-worker-3] INFO  com.example.weather.WeatherAPI - Weather response: WeatherResponse{latitude=40.710335, longitude=-73.99309, currentWeather=CurrentWeather{temperature=10.2, windspeed=12.1, time='2025-11-21T19:30'}}
01:11:47.644 [Thread-0] INFO  com.example.weather.WeatherAPI - Weather response: WeatherResponse{latitude=35.7, longitude=139.625, currentWeather=CurrentWeather{temperature=8.2, windspeed=4.1, time='2025-11-21T19:30'}}
01:11:47.644 [ForkJoinPool.commonPool-worker-2] INFO  com.example.weather.WeatherAPI - Weather response: WeatherResponse{latitude=-33.75, longitude=151.125, currentWeather=CurrentWeather{temperature=17.0, windspeed=3.2, time='2025-11-21T19:30'}}
01:11:47.644 [ForkJoinPool.commonPool-worker-1] INFO  com.example.weather.WeatherAPI - Weather response: WeatherResponse{latitude=51.5, longitude=-0.120000124, currentWeather=CurrentWeather{temperature=3.5, windspeed=5.3, time='2025-11-21T19:30'}}
01:11:47.644 [ForkJoinPool.commonPool-worker-4] INFO  com.example.weather.WeatherAPI - Weather response: WeatherResponse{latitude=48.86, longitude=2.3599997, currentWeather=CurrentWeather{temperature=1.9, windspeed=5.7, time='2025-11-21T19:30'}}

Process finished with exit code 0
```