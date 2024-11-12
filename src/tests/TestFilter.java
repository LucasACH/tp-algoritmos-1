package tests;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import libraries.DataImporter;
import structures.DataFrame;

public class TestFilter {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        DataFrame df = DataImporter.readCSV("data/dummy.csv");

        Map<Object, Predicate<Object>> conditions = new HashMap<>();
        conditions.put("Age", value -> (Integer) value > 30);
        conditions.put("Salary", value -> (Integer) value > 70000);

        DataFrame filtered = df.filter(conditions);

        assert filtered.countRows() == 3 : "Error en el filtro";
        assert filtered.countColumns() == 5 : "Error en el filtro";

        assert filtered.getCell(0, "Name").equals("Charlie") : "Error en el filtro";
        assert filtered.getCell(0, "Age").equals(35) : "Error en el filtro";
        assert filtered.getCell(0, "City").equals("Chicago") : "Error en el filtro";
        assert filtered.getCell(0, "Occupation").equals("Artist") : "Error en el filtro";
        assert filtered.getCell(0, "Salary").equals(50000) : "Error en el filtro";

        assert filtered.getCell(1, "Name").equals("David") : "Error en el filtro";
        assert filtered.getCell(1, "Age").equals(40) : "Error en el filtro";
        assert filtered.getCell(1, "City").equals("Houston") : "Error en el filtro";
        assert filtered.getCell(1, "Occupation").equals("Lawyer") : "Error en el filtro";
        assert filtered.getCell(1, "Salary").equals(90000) : "Error en el filtro";

        assert filtered.getCell(2, "Name").equals("Bobby") : "Error en el filtro";
        assert filtered.getCell(2, "Age").equals(60) : "Error en el filtro";
        assert filtered.getCell(2, "City").equals("New York") : "Error en el filtro";
        assert filtered.getCell(2, "Occupation").equals("Scientist") : "Error en el filtro";
        assert filtered.getCell(2, "Salary").equals(100000) : "Error en el filtro";
    }
}