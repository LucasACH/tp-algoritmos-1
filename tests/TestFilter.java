import libraries.DataImporter;

public class TestFilter {
    public static void main(String[] args) throws Exception {
        // Crear un DataFrame
        DataFrame df = DataImporter.readCSV("data/dummy.csv");

        // Filtrar las filas del DataFrame
        DataFrame dfFiltered = df.filter("Age", (value) -> (int) value > 30);

        // Imprimir el DataFrame filtrado
        System.out.println("DataFrame filtrado:");
        System.out.println(dfFiltered);

        // Llenar los valores faltantes en una columna
        df.fillna("City", "");
        df.show();
    }
}
