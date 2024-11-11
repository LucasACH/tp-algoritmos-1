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
        // Crear un DataFrame
        DataFrame df = DataImporter.readCSV("data/dummy.csv");

        Map<Object, Predicate<Object>> conditions = new HashMap<>();
        conditions.put("Age", value -> (Integer) value > 30);
        conditions.put("Salary", value -> (Integer) value > 70000);

        DataFrame filteredDf = df.filter(conditions);

        // Imprimir el DataFrame filtrado v2
        System.out.println("DataFrame filtrado v2:");
        System.out.println(filteredDf);

        // Llenar los valores faltantes en una columna
        System.out.println("DataFrame original con fillna:");
        df.fillna("City", "N/A");
        df.show();
    }
}