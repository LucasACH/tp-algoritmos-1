package tests;

import libraries.DataImporter;
import structures.DataFrame;

public class TestReadJSON {

        /**
         * @param args
         * @throws Exception
         */
        public static void main(String[] args) throws Exception {
                DataFrame df = DataImporter.readJSON("data/dummy.json");

                // Verificamos que el DataFrame se haya creado correctamente
                assert df.getColumns().size() == 5;
                assert df.getRows().size() == 5;

                // Verificamos que las celdas se hayan creado correctamente
                assert df.getCell(0, 0).equals("Alice");
                assert df.getCell(0, 1).equals(25);
                assert df.getCell(0, 2).equals("New York");
                assert df.getCell(0, 3).equals("Engineer");
                assert df.getCell(0, 4).equals(70000);

                assert df.getCell(1, 0).equals("Bob");
                assert df.getCell(1, 1).equals(30);
                assert df.getCell(1, 2).equals("Los Angeles");
                assert df.getCell(1, 3).equals("Doctor");
                assert df.getCell(1, 4).equals(120000);

                assert df.getCell(2, 0).equals("Charlie");
                assert df.getCell(2, 1).equals(35);
                assert df.getCell(2, 2).equals("Chicago");
                assert df.getCell(2, 3).equals("Artist");
                assert df.getCell(2, 4).equals(50000);

                assert df.getCell(3, 0).equals("David");
                assert df.getCell(3, 1).equals(40);
                assert df.getCell(3, 2).equals("Houston");
                assert df.getCell(3, 3).equals("Lawyer");
                assert df.getCell(3, 4).equals(90000);

                assert df.getCell(4, 0).equals("Eve");
                assert df.getCell(4, 1).equals(28);
                assert df.getCell(4, 2).equals("Phoenix");
                assert df.getCell(4, 3).equals("Nurse");
                assert df.getCell(4, 4).equals(75000);

                // Verificamos que las columnas se hayan creado correctamente
                assert df.getColumn("name")
                                .equals(new Object[] { "Alice", "Bob", "Charlie", "David", "Eve" });

                assert df.getColumn("age").equals(new Object[] { 25, 30, 35, 40, 28 });

                assert df.getColumn("city")
                                .equals(new Object[] { "New York", "Los Angeles", "Chicago", "Houston", "Phoenix" });

                assert df.getColumn("occupation")
                                .equals(new Object[] { "Engineer", "Doctor", "Artist", "Lawyer", "Nurse" });

                assert df.getColumn("salary")
                                .equals(new Object[] { 70000, 120000, 50000, 90000, 75000 });

                // Verificamos que las filas se hayan creado correctamente
                assert df.getRow(0).equals(new Object[] { "Alice", 25, "New York", "Engineer", 70000 });
                assert df.getRow(1).equals(new Object[] { "Bob", 30, "Los Angeles", "Doctor", 120000 });
                assert df.getRow(2).equals(new Object[] { "Charlie", 35, "Chicago", "Artist", 50000 });
                assert df.getRow(3).equals(new Object[] { "David", 40, "Houston", "Lawyer", 90000 });
                assert df.getRow(4).equals(new Object[] { "Eve", 28, "Phoenix", "Nurse", 75000 });
        }
}
