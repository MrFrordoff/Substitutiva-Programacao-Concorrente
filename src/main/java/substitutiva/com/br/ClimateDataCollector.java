package substitutiva.com.br;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ClimateDataCollector {
    private static final String API_URL_TEMPLATE = "https://historical-forecast-api.open-meteo.com/v1/forecast?latitude=-10.9167&longitude=37.05&start_date=2024-01-01&end_date=2024-01-31&hourly=temperature_2m";
    private static final Gson gson = new Gson();

    public static void main(String[] args) throws Exception {
        List<City> cities = new ArrayList<>();
        cities.add(new City("Aracaju", "-10.9167", "-37.05"));
        cities.add(new City("Belém", "-1.4558", "-48.5039"));
        cities.add(new City("Belo Horizonte", "-19.9167", "-43.9333"));
        cities.add(new City("Boa Vista", "2.81972", "-60.67333"));
        cities.add(new City("Brasília", "-15.7801", "-47.9292"));
        cities.add(new City("Campo Grande", "-20.44278", "-54.64639"));
        cities.add(new City("Cuiabá", "-15.5989", "-56.0949"));
        cities.add(new City("Curitiba", "-25.4297", "-49.2711"));
        cities.add(new City("Florianópolis", "-27.5935", "-48.55854"));
        cities.add(new City("Fortaleza", "-3.7275", "-38.5275"));
        cities.add(new City("Goiânia", "-16.6667", "-49.25"));
        cities.add(new City("João Pessoa", "-7.12", "-34.88"));
        cities.add(new City("Macapá", "0.033", "-51.05"));
        cities.add(new City("Maceió", "-9.66583", "-35.73528"));
        cities.add(new City("Manaus", "-3.1189", "-60.0217"));
        cities.add(new City("Natal", "-5.7833", "-35.2"));
        cities.add(new City("Palmas", "-10.16745", "-48.32766"));
        cities.add(new City("Porto Alegre", "-30.0331", "-51.23"));
        cities.add(new City("Porto Velho", "-8.76194", "-63.90389"));
        cities.add(new City("Recife", "-8.05", "-34.9"));
        cities.add(new City("Rio Branco", "-9.97472", "-67.81"));
        cities.add(new City("Rio de Janeiro", "-22.9111", "-43.2056"));
        cities.add(new City("Salvador", "-12.9747", "-38.4767"));
        cities.add(new City("São Luís", "-2.5283", "-44.3044"));
        cities.add(new City("São Paulo", "-23.55", "-46.6333"));
        cities.add(new City("Teresina", "-5.08917", "-42.80194"));
        cities.add(new City("Vitória", "-20.2889", "-40.3083"));

        runAllExperiments(cities);
    }

    private static void runAllExperiments(List<City> cities) throws InterruptedException {
        // Versão sem threads
        System.out.println("Versão sem threads:");
        runExperimentWithoutThreads(cities);

        // Versão com 3 threads
        System.out.println("Versão com 3 threads:");
        runExperimentWithThreads(cities, 3);

        // Versão com 9 threads
        System.out.println("Versão com 9 threads:");
        runExperimentWithThreads(cities, 9);

        // Versão com 27 threads
        System.out.println("Versão com 27 threads:");
        runExperimentWithThreads(cities, 27);
    }

    // Versão sem threads
    private static void runExperimentWithoutThreads(List<City> cities) {
        long startTime = System.currentTimeMillis();

        for (City city : cities) {
            try {
                String response = HttpClient.makeHttpRequest(String.format(API_URL_TEMPLATE, city.getLatitude(), city.getLongitude()));
                JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
                JsonArray hourlyTemps = jsonResponse.getAsJsonObject("hourly").getAsJsonArray("temperature_2m");

                // Processamento dos dados
                Map<Integer, TemperatureData> dailyTemperatures = processTemperatures(hourlyTemps);
                dailyTemperatures.forEach((day, tempData) -> {
                    System.out.printf("Cidade: %s, Dia: %d, Média: %.2f, Mínima: %.2f, Máxima: %.2f%n",
                            city.getName(), day, tempData.getDailyAverage(), tempData.getDailyMin(), tempData.getDailyMax());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.printf("Tempo de execução sem threads: %d ms%n", (endTime - startTime));
    }

    // Versão com threads
    private static void runExperimentWithThreads(List<City> cities, int numThreads) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        long startTime = System.currentTimeMillis();

        for (City city : cities) {
            executor.submit(() -> {
                try {
                    String response = HttpClient.makeHttpRequest(String.format(API_URL_TEMPLATE, city.getLatitude(), city.getLongitude()));
                    JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
                    JsonArray hourlyTemps = jsonResponse.getAsJsonObject("hourly").getAsJsonArray("temperature_2m");

                    // Processamento dos dados
                    Map<Integer, TemperatureData> dailyTemperatures = processTemperatures(hourlyTemps);
                    dailyTemperatures.forEach((day, tempData) -> {
                        System.out.printf("Cidade: %s, Dia: %d, Média: %.2f, Mínima: %.2f, Máxima: %.2f%n",
                                city.getName(), day, tempData.getDailyAverage(), tempData.getDailyMin(), tempData.getDailyMax());
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        long endTime = System.currentTimeMillis();
        System.out.printf("Tempo de execução com %d threads: %d ms%n", numThreads, (endTime - startTime));
    }

    private static Map<Integer, TemperatureData> processTemperatures(JsonArray hourlyTemps) {
        Map<Integer, List<Double>> dailyTemps = new HashMap<>();

        for (int i = 0; i < hourlyTemps.size(); i++) {
            double temp = hourlyTemps.get(i).getAsDouble();
            int day = (i / 24) + 1; // Cada dia tem 24 horas

            dailyTemps.computeIfAbsent(day, k -> new ArrayList<>()).add(temp);
        }

        Map<Integer, TemperatureData> result = new HashMap<>();
        dailyTemps.forEach((day, temps) -> result.put(day, new TemperatureData(temps)));
        return result;
    }
}
