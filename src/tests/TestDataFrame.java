package tests;

import java.util.Arrays;
import java.util.List;

import exceptions.InvalidShape;
import exceptions.TypeDoesNotMatch;
import structures.DataFrame;

public class TestDataFrame {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        List<List<?>> rows = Arrays.asList(
                Arrays.asList("Alice", 23, "New York", "Engineer", 70000),
                Arrays.asList("Bob", 25, "San Francisco", "Data Scientist", 80000),
                Arrays.asList("Charlie", 27, "Los Angeles", "Product Manager", 90000),
                Arrays.asList("David", 29, "Chicago", "Software Engineer", 100000),
                Arrays.asList("Eve", 31, "Boston", "Data Analyst", 110000));

        List<String> headers = Arrays.asList("Name", "Age", "City", "Occupation", "Salary");

        DataFrame df = new DataFrame(rows, headers);

        assert !df.copy().equals(df.copy());
        System.out.println("Test df.copy(): crea un nuevo DataFrame");

        assert df.copy().equals(df);
        System.out.println("Test df.copy().equals(df): son iguales");

        df.insertRow(Arrays.asList("Frank", 33, "Seattle", "Data Engineer", 120000));
        assert df.getCell(5, 0).equals("Frank");
        assert df.getCell(5, 1).equals(33);
        assert df.getCell(5, 2).equals("Seattle");
        assert df.getCell(5, 3).equals("Data Engineer");
        assert df.getCell(5, 4).equals(120000);
        System.out.println("Test df.insertRow(): inserta una nueva fila");

        try {
            df.copy().insertRow(Arrays.asList("Grace", 35, "Austin", "Data Scientist"));
        } catch (Exception e) {
            assert InvalidShape.class.isInstance(e);
            System.out.println("Test insertRow tamaño incorrecto: lanza InvalidShape");
        }

        try {
            df.copy().insertRow(Arrays.asList("Grace", 35, "Austin", "Data Scientist", "130000"));
        } catch (Exception e) {
            assert TypeDoesNotMatch.class.isInstance(e);
            System.out.println("Test insertRow tipo incorrecto: lanza TypeDoesNotMatch");
        }

        df.insertColumn("Years of Experience", Arrays.asList(1, 3, 5, 7, 9, 11));
        assert df.getCell(0, 5).equals(1);
        assert df.getCell(1, 5).equals(3);
        assert df.getCell(2, 5).equals(5);
        assert df.getCell(3, 5).equals(7);
        assert df.getCell(4, 5).equals(9);
        assert df.getCell(5, 5).equals(11);
        System.out.println("Test df.insertColumn(): inserta una nueva columna");

        try {
            df.copy().insertColumn("Years of Experience", Arrays.asList(1, 3, 5, 7, 9));
        } catch (Exception e) {
            assert InvalidShape.class.isInstance(e);
            System.out.println("Test insertColumn tamaño incorrecto: lanza InvalidShape");
        }

        try {
            df.copy().insertColumn("Years of Experience", Arrays.asList(1, 3, 5, 7, 9, "11"));
        } catch (Exception e) {
            assert TypeDoesNotMatch.class.isInstance(e);
            System.out.println("Test insertColumn tipo incorrecto: lanza TypeDoesNotMatch");
        }

        assert df.getCell(0, 0).equals("Alice");
        assert df.getCell(1, 1).equals(25);
        assert df.getCell(2, 2).equals("Los Angeles");
        assert df.getCell(3, 3).equals("Software Engineer");
        assert df.getCell(4, 4).equals(110000);
        System.out.println("Test df.getCell(): obtiene el valor de una celda");

        df.setCell(0, 0, "Alice Smith");
        assert df.getCell(0, 0).equals("Alice Smith");
        System.out.println("Test df.setCell(): cambia el valor de una celda");

        try {
            df.copy().setCell(0, 0, 1);
        } catch (Exception e) {
            assert TypeDoesNotMatch.class.isInstance(e);
            System.out.println("Test setCell tipo incorrecto: lanza TypeDoesNotMatch");
        }

        try {
            df.copy().setCell(5, 0, "Grace");
        } catch (Exception e) {
            assert IndexOutOfBoundsException.class.isInstance(e);
            System.out.println("Test setCell índice incorrecto: lanza IndexOutOfBoundsException");
        }

        assert df.countRows() == 6;
        System.out.println("Test df.countRows(): cuenta el número de filas");

        assert df.countColumns() == 6;
        System.out.println("Test df.countColumns(): cuenta el número de columnas");

        assert df.getColumn(0).equals(Arrays.asList("Alice Smith", "Bob", "Charlie", "David", "Eve", "Frank"));
        assert df.getColumn(1).equals(Arrays.asList(23, 25, 27, 29, 31, 33));
        assert df.getColumn(2).getLabel().equals("City");
        assert df.getColumn(3).getType().equals("String");
        System.out.println("Test df.getColumn(): obtiene una columna");

        try {
            df.copy().getColumn(6);
        } catch (Exception e) {
            assert IndexOutOfBoundsException.class.isInstance(e);
            System.out.println("Test getColumn índice incorrecto: lanza IndexOutOfBoundsException");
        }
    }
}
