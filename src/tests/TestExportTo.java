package tests;

import java.util.Arrays;
import java.util.List;

import libraries.DataImporter;
import structures.DataFrame;

public class TestExportTo {
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // Test con DataFrame vacío
        DataFrame df = new DataFrame();
        df.exportToCSV("data/dummy_export.csv");
        df.exportToJSON("data/dummy_export.json");

        df = DataImporter.readCSV("data/dummy_export.csv");
        assert df.countRows() == 0 : "Error en la exportación a CSV";
        assert df.countColumns() == 0 : "Error en la exportación a CSV";

        df = DataImporter.readJSON("data/dummy_export.json");
        assert df.countRows() == 0 : "Error en la exportación a JSON";
        assert df.countColumns() == 0 : "Error en la exportación a JSON";

        // Test con DataFrame no vacío
        List<List<?>> rows = Arrays.asList(
                Arrays.asList("Alice", 23, "New York", "Engineer", 70000),
                Arrays.asList("Bob", 25, "San Francisco", "Data Scientist", 80000),
                Arrays.asList("Charlie", 27, "Los Angeles", "Product Manager", 90000),
                Arrays.asList("David", 29, "Chicago", "Software Engineer", 100000),
                Arrays.asList("Eve", 31, "Boston", "Data Analyst", 110000));

        List<String> headers = Arrays.asList("name", "age", "city", "occupation", "salary");

        df = new DataFrame(rows, headers);

        df.exportToCSV("data/dummy_export.csv");
        df.exportToJSON("data/dummy_export.json");

        df = DataImporter.readCSV("data/dummy_export.csv");
        assert df.countRows() == 5 : "Error en la exportación a CSV";
        assert df.countColumns() == 5 : "Error en la exportación a CSV";

        df = DataImporter.readJSON("data/dummy_export.json");
        assert df.countRows() == 5 : "Error en la exportación a JSON";
        assert df.countColumns() == 5 : "Error en la exportación a JSON";
    }
}
