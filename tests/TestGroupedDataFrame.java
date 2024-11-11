import java.util.Arrays;

import libraries.DataImporter;
import structures.DataFrame;
import structures.GroupedDataFrame;

public class TestGroupedDataFrame {
    public static void main(String[] args) throws Exception {
        DataFrame df = DataImporter.readCSV("data/dummy.csv");

        GroupedDataFrame gdf = df.groupBy(Arrays.asList("Age"));

        System.out.println(gdf.sum("Age"));
    }
}
