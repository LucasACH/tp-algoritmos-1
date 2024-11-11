import java.util.Arrays;

import exceptions.TypeDoesNotMatch;
import libraries.DataImporter;
import structures.DataFrame;

public class TestReadCSV {
    public static void main(String[] args) throws Exception {
        DataFrame df = DataImporter.readCSV("data/dummy.csv");

        try {
            df.fillna("City", 1);
        } catch (Exception e) {
            assert TypeDoesNotMatch.class.isInstance(e);
        }

        df.fillna("City", "N/A");
        assert df.getCell(6, 3).equals("N/A");

        // df.filter("Age", (value) -> (int) value > 30);

        // df.sortBy(Arrays.asList(
        // "Age", "Salary"), true).show();

        System.out.println(df.manipulator.groupBy(Arrays.asList("City")).var("Salary"));

        // df.show();

    }
}
