package tests;

import java.util.Map;
import java.util.function.Predicate;

import exceptions.TypeDoesNotMatch;
import libraries.DataImporter;
import structures.DataFrame;

public class TestReadCSV {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        DataFrame df = DataImporter.readCSV("data/dummy.csv");

        try {
            df.fillna("City", 1);
        } catch (Exception e) {
            assert TypeDoesNotMatch.class.isInstance(e);
        }

        df.fillna("City", "N/A");
        assert df.getCell(6, 3).equals("N/A");

        Map<Object, Predicate<Object>> conditions = Map.of(
                "Age", (value) -> (int) value > 30,
                "Name", (value) -> ((String) value).startsWith("J"));
        assert df.filter(conditions).getRows().size() == 2;
    }
}
