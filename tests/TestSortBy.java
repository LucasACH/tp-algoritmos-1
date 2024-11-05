import java.util.ArrayList;
import java.util.List;

public class TestSortBy {
    public static void main(String[] args) throws Exception {
        DataFrame df = DataImporter.readCSV("data/dummy.csv");
        // df.show();

        List<String> columnNames = new ArrayList<>();
        columnNames.add("Name");
        columnNames.add("Age");

        // // Asigna el DataFrame ordenado a df
        DataFrame df2 = df.manipulator.sortBy(columnNames, false);
        System.out.println("Original DataFrame:");
        df.show();
        System.out.println("Sorted DataFrame:");
        df2.show();

        DataFrame df3 = df2.manipulator.sample(0.5);
        System.out.println("Sampled DataFrame:");
        df3.show();


        // Column columna = df.getColumn("Name");
        // System.out.println(columna);
        // columna.sort(true);
        // System.out.println(columna);
        // columna.sort(false);
        // System.out.println(columna);
    }
}
