package substitutiva.com.br;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ClimateDataCollector {

    public static void main(String[] args) throws InterruptedException {
        List<City> cities = City.getCitiesFromFile("cities.txt");

        // Executa todos os experimentos
        runAllExperiments(cities);
    }

    private static void runAllExperiments(List<City> cities) throws InterruptedException {
        int repetitions = 10;

        // Versão sem threads
        System.out.println("Versão sem threads:");
        for (int i = 0; i < repetitions; i++) {
            runExperimentWithoutThreads(cities);
        }

        // Versão com 3 threads
        System.out.println("Versão com 3 threads:");
        for (int i = 0; i < repetitions; i++) {
            runExperimentWithThreads(cities, 3);
        }

        // Versão com 9 threads
        System.out.println("Versão com 9 threads:");
        for (int i = 0; i < repetitions; i++) {
            runExperimentWithThreads(cities, 9);
        }

        // Versão com 27 threads
        System.out.println("Versão com 27 threads:");
        for (int i = 0; i < repetitions; i++) {
            runExperimentWithThreads(cities, 27);
        }
    }

    private static void runExperimentWithoutThreads(List<City> cities) {
        System.out.println("Rodando experimento sem threads.");

        long startTime = System.currentTimeMillis();

        for (City city : cities) {
            TemperatureData data = fetchAndProcessDataForCity(city);
            System.out.println(data);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("Tempo de execução sem threads: " + duration + " ms");
    }

    private static void runExperimentWithThreads(List<City> cities, int numThreads) throws InterruptedException {
        System.out.println("Rodando experimento com " + numThreads + " threads.");
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<TemperatureData>> futures = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        for (City city : cities) {
            Future<TemperatureData> future = executor.submit(() -> {
                return fetchAndProcessDataForCity(city);
            });
            futures.add(future);
        }

        for (Future<TemperatureData> future : futures) {
            try {
                TemperatureData data = future.get();
                System.out.println(data);
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("Tempo de execução com " + numThreads + " threads: " + duration + " ms");
    }

    private static TemperatureData fetchAndProcessDataForCity(City city) {
        String response = HttpClient.getWeatherData(city.getLatitude(), city.getLongitude());
        return TemperatureData.processWeatherData(response);
    }
}
