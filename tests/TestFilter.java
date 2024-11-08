import java.io.IOException;
import java.time.LocalDate;

import exceptions.InvalidShape;
import exceptions.LabelAlreadyInUse;
import exceptions.LabelNotFound;
import exceptions.TypeDoesNotMatch;

public class TestFilter {
    public static void main(String[] args) throws IOException, InvalidShape, TypeDoesNotMatch, LabelAlreadyInUse {
        // Crear un DataFrame
        DataFrame df = DataImporter.readCSV("data/dummy.csv");

        // Filtrar las filas del DataFrame
        DataFrame dfFiltered = df.filter("Age", (value) -> (int) value > 30);

        // Imprimir el DataFrame filtrado
        System.out.println("DataFrame filtrado:");
        System.out.println(dfFiltered);

        // Llenar los valores faltantes en una columna
        // df.fillna("Salary", 1);
    }
}
