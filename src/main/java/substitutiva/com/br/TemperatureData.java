package substitutiva.com.br;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TemperatureData {
    private Map<String, DoubleSummaryStatistics> dailyStats;

    public TemperatureData(Map<String, DoubleSummaryStatistics> dailyStats) {
        this.dailyStats = dailyStats;
    }

    public static TemperatureData processWeatherData(String jsonResponse) {
        List<Double> temperatures = parseJsonResponse(jsonResponse);

        Map<String, DoubleSummaryStatistics> dailyStats = temperatures.stream()
                .collect(Collectors.groupingBy(
                        temp -> getDateFromTimestamp(temp.getTimestamp()),
                        Collectors.summarizingDouble(Double::doubleValue)
                ));

        return new TemperatureData(dailyStats);
    }

    private static List<Double> parseJsonResponse(String jsonResponse) {
        // Implementação para parsear a resposta JSON
    }

    private static String getDateFromTimestamp(long timestamp) {
        // Implementação para extrair a data de um timestamp
    }

    @Override
    public String toString() {
        // Implementação do método toString para exibir os dados
    }
}
