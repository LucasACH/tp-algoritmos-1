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
        conditions.put("age", value -> (Integer) value > 30);
        conditions.put("salary", value -> (Integer) value > 70000);

        DataFrame filtered = df.filter(conditions);

        assert filtered.countRows() == 3 : "Error en el filtro";
        assert filtered.countColumns() == 5 : "Error en el filtro";

        assert filtered.getCell(0, "name").equals("Charlie") : "Error en el filtro";
        assert filtered.getCell(0, "age").equals(35) : "Error en el filtro";
        assert filtered.getCell(0, "city").equals("Chicago") : "Error en el filtro";
        assert filtered.getCell(0, "occupation").equals("Artist") : "Error en el filtro";
        assert filtered.getCell(0, "salary").equals(50000) : "Error en el filtro";

        assert filtered.getCell(1, "name").equals("David") : "Error en el filtro";
        assert filtered.getCell(1, "age").equals(40) : "Error en el filtro";
        assert filtered.getCell(1, "city").equals("Houston") : "Error en el filtro";
        assert filtered.getCell(1, "occupation").equals("Lawyer") : "Error en el filtro";
        assert filtered.getCell(1, "salary").equals(90000) : "Error en el filtro";

        assert filtered.getCell(2, "name").equals("Bobby") : "Error en el filtro";
        assert filtered.getCell(2, "age").equals(60) : "Error en el filtro";
        assert filtered.getCell(2, "city").equals("New York") : "Error en el filtro";
        assert filtered.getCell(2, "occupation").equals("Scientist") : "Error en el filtro";
        assert filtered.getCell(2, "salary").equals(100000) : "Error en el filtro";
    }
}