package tests;

import java.util.Arrays;
import java.util.Map;

import libraries.DataImporter;
import structures.DataFrame;
import structures.GroupedDataFrame;

public class TestGroupedDataFrame {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        DataFrame df = DataImporter.readCSV("data/dummy.csv");

        GroupedDataFrame gdf = df.groupBy(Arrays.asList("City"));

        // Test sum
        Map<String, Double> sum = gdf.sum("Age");
        assert sum.get("New York") == 85.0;
        assert sum.get("Chicago") == 35.0;
        assert sum.get("Phoenix") == 28.0;
        assert sum.get("Los Angeles") == 30.0;
        assert sum.get("Houston") == 40.0;

        // Test mean
        Map<String, Double> mean = gdf.mean("Salary");
        assert mean.get("New York") == 85000.0;
        assert mean.get("Chicago") == 50000.0;
        assert mean.get("Phoenix") == 75000.0;
        assert mean.get("Los Angeles") == 120000.0;
        assert mean.get("Houston") == 90000.0;

        // Test min
        Map<String, Double> min = gdf.min("Age");
        assert min.get("New York") == 25.0;
        assert min.get("Chicago") == 35.0;
        assert min.get("Phoenix") == 28.0;
        assert min.get("Los Angeles") == 30.0;
        assert min.get("Houston") == 40.0;

        // Test max
        Map<String, Double> max = gdf.max("Salary");
        assert max.get("New York") == 100000.0;
        assert max.get("Chicago") == 50000.0;
        assert max.get("Phoenix") == 75000.0;
        assert max.get("Los Angeles") == 120000.0;
        assert max.get("Houston") == 90000.0;

        // Test count
        Map<String, Integer> count = gdf.count("Name");
        assert count.get("New York") == 2;
        assert count.get("Chicago") == 1;
        assert count.get("Phoenix") == 1;
        assert count.get("Los Angeles") == 1;
        assert count.get("Houston") == 1;

        // Test std
        Map<String, Double> std = gdf.std("Salary");
        assert std.get("New York") == 21213.203435596424;
        assert std.get("Chicago") == 0.0;
        assert std.get("Phoenix") == 0.0;
        assert std.get("Los Angeles") == 0.0;
        assert std.get("Houston") == 0.0;

        // Test var
        Map<String, Double> var = gdf.var("Salary");
        assert var.get("New York") == 450000000.0;
        assert var.get("Chicago") == 0.0;
        assert var.get("Phoenix") == 0.0;
        assert var.get("Los Angeles") == 0.0;
        assert var.get("Houston") == 0.0;
    }
}
