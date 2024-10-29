import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataImporter {
    public DataFrame readCSV(String path) throws IOException {
        DataFrame dataframe = new DataFrame();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();
            if (line != null) {
                String[] headers = line.split(",");
                for (String header : headers) {
                    dataframe.insertColumn(new Column<>(header));
                }
            }

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                List<Cell<?>> cells = new ArrayList<>();
                for (String value : values) {
                    cells.add(new Cell<>(value)); // Convertir a T si es necesario
                }
                dataframe.insertRow(null, cells); // null como placeholder del label
            }
        }
        return dataframe;
    }

    public DataFrame readJSON(String path) throws IOException {
        DataFrame dataframe = new DataFrame();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                json.append(line.trim());
            }

            String jsonString = json.toString();
            jsonString = jsonString.substring(1, jsonString.length() - 1); // Eliminar [ ]
            String[] rows = jsonString.split("\\},\\{");

            // Procesar encabezados de la primera fila
            String[] firstRow = rows[0].replace("{", "").replace("}", "").split(",");
            for (String column : firstRow) {
                String label = column.split(":")[0].replace("\"", "").trim();
                dataframe.insertColumn(new Column<>(label));
            }

            // Procesar valores de cada fila
            for (String row : rows) {
                row = row.replace("{", "").replace("}", "");
                String[] values = row.split(",");
                List<Cell<?>> cells = new ArrayList<>();
                for (String value : values) {
                    String cellValue = value.split(":")[1].replace("\"", "").trim();
                    cells.add(new Cell<>(cellValue)); // Convertir a T si es necesario
                }
                dataframe.insertRow(null, cells);
            }
        }
        return dataframe;
    }
}
