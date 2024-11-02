import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import exceptions.InvalidShape;
import exceptions.LabelAlreadyInUse;
import exceptions.TypeDoesNotMatch;

public class DataImporter {

    public static DataFrame readCSV(String path) throws IOException, InvalidShape, TypeDoesNotMatch, LabelAlreadyInUse {
        DataFrame df = new DataFrame();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();

            // Procesar encabezados
            if (line != null) {
                String[] headers = line.split(",");
                for (String header : headers) {
                    df.insertColumn(new Column<>(header.trim()));
                }
            }

            // Leer cada fila y verificar longitud
            int columnCount = df.getColumns().size();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                // Rellenar valores faltantes si es necesario
                List<Cell<?>> cells = new ArrayList<>();
                for (int i = 0; i < columnCount; i++) {
                    String value = i < values.length ? values[i].trim() : ""; // Usa "" como valor por defecto

                    // Detectar y convertir el tipo de dato
                    if (value.matches("-?\\d+")) {
                        cells.add(new Cell<>(Integer.parseInt(value))); // Integer
                    } else if (value.matches("-?\\d*\\.\\d+")) {
                        cells.add(new Cell<>(Double.parseDouble(value))); // Double
                    } else if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                        cells.add(new Cell<>(Boolean.parseBoolean(value))); // Boolean
                    } else {
                        cells.add(new Cell<>(value)); // String
                    }
                }
                df.insertRow(cells);
            }
        }

        return df;
    }

    public static DataFrame readJSON(String path)
            throws IOException, InvalidShape, TypeDoesNotMatch, LabelAlreadyInUse {
        DataFrame df = new DataFrame();

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
                df.insertColumn(new Column<>(label));
            }

            // Procesar valores de cada fila
            for (String row : rows) {
                row = row.replace("{", "").replace("}", "");
                String[] values = row.split(",");
                List<Cell<?>> cells = new ArrayList<>();
                for (String value : values) {
                    String cellValue = value.split(":")[1].replace("\"", "").trim();
                    if (cellValue.matches("-?\\d+")) {
                        cells.add(new Cell<>(Integer.parseInt(cellValue)));
                    } else if (cellValue.matches("-?\\d*\\.\\d+")) {
                        cells.add(new Cell<>(Double.parseDouble(cellValue)));
                    } else if (cellValue.equalsIgnoreCase("true") || cellValue.equalsIgnoreCase("false")) {
                        cells.add(new Cell<>(Boolean.parseBoolean(cellValue)));
                    } else {
                        cells.add(new Cell<>(cellValue));
                    }
                }
                df.insertRow(cells);
            }
        }
        return df;
    }
}
