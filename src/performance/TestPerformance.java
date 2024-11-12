package performance;

import libraries.DataImporter;

public class TestPerformance {
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        PerformanceTimer timer = new PerformanceTimer();
        timer.start();
        DataImporter.readCSV("data/benchmark/dummy_10000.csv");
        timer.stop();

        System.out.println("Tiempo de carga de archivo CSV: " + timer.getElapsedTimeMillis() + " ms");
    }
}
