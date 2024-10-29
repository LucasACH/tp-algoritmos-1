package mogul;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DataExporter<T> {
    private DataFrame<T> dataframe;

    public DataExporter(DataFrame<T> dataframe) {
        this.dataframe = dataframe;
    }

    public void toCSV(String path) throws IOException {
        try (FileWriter writer = new FileWriter(path)) {
            // Escribir los nombres de las columnas en la primera fila
            List<Column<T>> columns = dataframe.getColumns();
            for (int i = 0; i < columns.size(); i++) {
                writer.append(columns.get(i).getLabel().toString());
                if (i < columns.size() - 1) {
                    writer.append(",");
                }
            }
            writer.append("\n");

            // Escribir cada fila de datos
            int numRows = dataframe.countRows();
            for (int row = 0; row < numRows; row++) {
                for (int col = 0; col < columns.size(); col++) {
                    T value = columns.get(col).getCell(row).getValue();
                    writer.append(value != null ? value.toString() : "");
                    if (col < columns.size() - 1) {
                        writer.append(",");
                    }
                }
                writer.append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
            throw e;
        }
    }

    public void toJSON(String path) throws IOException {
        try (FileWriter writer = new FileWriter(path)) {
            writer.append("[\n");

            List<Column<T>> columns = dataframe.getColumns();
            int numRows = dataframe.countRows();
            for (int row = 0; row < numRows; row++) {
                writer.append("  {\n");
                for (int col = 0; col < columns.size(); col++) {
                    String label = columns.get(col).getLabel().toString();
                    T value = columns.get(col).getCell(row).getValue();

                    writer.append("    \"" + label + "\": ");
                    writer.append(value != null ? "\"" + value.toString() + "\"" : "null");

                    if (col < columns.size() - 1) {
                        writer.append(",");
                    }
                    writer.append("\n");
                }
                writer.append("  }");
                if (row < numRows - 1) {
                    writer.append(",");
                }
                writer.append("\n");
            }

            writer.append("]");
        } catch (IOException e) {
            System.err.println("Error writing to JSON: " + e.getMessage());
            throw e;
        }
    }
}

/*
 * TODO: Este código considera que:
 * 
 * + dataframe.getColumns() devuelve una lista de columnas como lo ajustamos
 * previamente.
 * + En los valores nulos o vacíos de las celdas, el método toString() devuelve
 * una representación correcta sin errores.
 * 
 */