package libraries;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import structures.DataFrame;
import structures.Row;

/**
 * Clase que permite exportar un DataFrame a un archivo CSV o JSON.
 */
public class DataExporter {
    private DataFrame df;

    /**
     * Constructor que inicializa el exportador con un DataFrame.
     *
     * @param df DataFrame a exportar.
     */
    public DataExporter(DataFrame df) {
        this.df = df;
    }

    /**
     * Exporta el DataFrame a un archivo CSV en la ruta especificada.
     *
     * @param path Ruta del archivo CSV.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    public void toCSV(String path) throws IOException {
        try (FileWriter writer = new FileWriter(path)) {
            writeCSVHeaders(this.df.getColumnLabels(), writer);
            writeCSVRows(this.df.getRows(), writer);
        }
    }

    /**
     * @param headers
     * @param writer
     * @throws IOException
     */
    private static void writeCSVHeaders(List<Object> headers, FileWriter writer) throws IOException {
        writer.write(String.join(",", headers.stream().map(Object::toString).collect(Collectors.toList())));
        writer.write("\n");
    }

    private static void writeCSVRows(List<Row> rows, FileWriter writer) throws IOException {
        for (Row row : rows) {
            for (int i = 0; i < row.size(); i++) {
                writer.write(String.valueOf(row.getCell(i).getValue()));
                if (i < row.size() - 1)
                    writer.write(",");
            }
            writer.write("\n");
        }
    }

    /**
     * Exporta el DataFrame a un archivo JSON en la ruta especificada.
     *
     * @param path Ruta del archivo JSON.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    public void toJSON(String path) throws IOException {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write("[\n");
            List<Object> headers = this.df.getColumnLabels();
            List<Row> rows = this.df.getRows();

            for (int i = 0; i < rows.size(); i++) {
                writeJSONRow(rows.get(i), headers, writer);
                if (i < rows.size() - 1)
                    writer.write(",");
                writer.write("\n");
            }
            writer.write("]");
        }
    }

    private static void writeJSONRow(Row row, List<Object> headers, FileWriter writer) throws IOException {
        writer.write("  {");
        for (int i = 0; i < headers.size(); i++) {
            writer.write("\"" + headers.get(i) + "\": " + formatJSONValue(row.getCell(i).getValue()));
            if (i < headers.size() - 1)
                writer.write(", ");
        }
        writer.write("}");
    }

    private static String formatJSONValue(Object value) {
        if (value instanceof String) {
            return "\"" + value + "\"";
        } else if (value instanceof Boolean || value instanceof Number) {
            return value.toString();
        } else {
            return "null";
        }
    }
}
