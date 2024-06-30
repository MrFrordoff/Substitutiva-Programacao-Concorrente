package substitutiva.com.br;

import java.util.List;

public class TemperatureData {
    private double dailyAverage;
    private double dailyMin;
    private double dailyMax;

    public TemperatureData(List<Double> temperatures) {
        this.dailyAverage = temperatures.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
        this.dailyMin = temperatures.stream().mapToDouble(Double::doubleValue).min().orElse(Double.NaN);
        this.dailyMax = temperatures.stream().mapToDouble(Double::doubleValue).max().orElse(Double.NaN);
    }

    public double getDailyAverage() {
        return dailyAverage;
    }

    public double getDailyMin() {
        return dailyMin;
    }

    public double getDailyMax() {
        return dailyMax;
    }
}
