import java.util.Arrays;

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

        System.out.println(gdf.var("Age"));
    }
}
