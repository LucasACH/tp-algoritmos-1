import java.util.Arrays;
import java.util.List;

import libraries.DataImporter;

public class TestGroupedDataFrame {
    public static void main(String[] args) throws Exception {
        DataFrame df = DataImporter.readCSV("data/dummy.csv");
        List<String> labels = Arrays.asList("Age");

        GroupedDataFrame gdf = df.manipulator.groupBy(labels);

        System.out.println(gdf.sum("Age"));
    }
}
