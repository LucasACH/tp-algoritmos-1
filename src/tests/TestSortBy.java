package tests;

import java.util.ArrayList;
import java.util.List;

import libraries.DataImporter;
import structures.DataFrame;

public class TestSortBy {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        DataFrame df = DataImporter.readCSV("data/dummy.csv");

        List<Object> columnNames = new ArrayList<>();
        columnNames.add("name");
        columnNames.add("age");

        DataFrame sorted = df.sortBy(columnNames, false);
        assert sorted.getCell(0, 0).equals("Bobby");
        assert sorted.getCell(0, 1).equals(60.0);
        assert sorted.getCell(1, 0).equals("Marta");
        assert sorted.getCell(1, 1).equals(40.0);
        assert sorted.getCell(2, 0).equals("David");
        assert sorted.getCell(2, 1).equals(40.0);
        assert sorted.getCell(3, 0).equals("Charlie");
        assert sorted.getCell(3, 1).equals(35.0);
        assert sorted.getCell(4, 0).equals("Bob");
        assert sorted.getCell(4, 1).equals(30.0);
        assert sorted.getCell(5, 0).equals("Eve");
        assert sorted.getCell(5, 1).equals(28.0);
        assert sorted.getCell(6, 0).equals("Alice");
        assert sorted.getCell(6, 1).equals(25.0);
    }
}
