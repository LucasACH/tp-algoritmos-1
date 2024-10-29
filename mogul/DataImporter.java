package mogul;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class DataImporter {

    public DataFrame<Object> fromCSV(String path) throws IOException {
        DataFrame<Object> dataframe = new DataFrame<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();
            if (line == null) {
                throw new IOException("Empty CSV file");
            }

            // Leer los encabezados de columna
            String[] headers = line.split(",");
            for (String header : headers) {
                dataframe.insertColumn(new Column<>(header.trim()));
            }

            // Leer el resto de las filas y llenar las columnas
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                if (values.length != headers.length) {
                    System.err.println("Warning: Inconsistent row length. Skipping row: " + line);
                    continue;
                }

                for (int i = 0; i < values.length; i++) {
                    String value = values[i].trim();
                    Object cellValue = value.isEmpty() ? null : value;
                    dataframe.getColumns().get(i).addCell(new Cell<>(cellValue));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
            throw e;
        }

        return dataframe;
    }

    public DataFrame<Object> fromJSON(String path) throws IOException {
        DataFrame<Object> dataframe = new DataFrame<>();

        String content = new String(Files.readAllBytes(Paths.get(path)));
        JSONArray jsonArray;

        try {
            jsonArray = new JSONArray(content);
        } catch (Exception e) {
            throw new IOException("Invalid JSON format", e);
        }

        if (jsonArray.isEmpty()) {
            throw new IOException("Empty JSON file");
        }

        JSONObject firstObject = jsonArray.getJSONObject(0);
        for (String key : firstObject.keySet()) {
            dataframe.insertColumn(new Column<>(key));
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int colIndex = 0;
            for (String key : firstObject.keySet()) { // Usamos las claves de firstObject para asegurar consistencia
                Object cellValue = jsonObject.opt(key);
                dataframe.getColumns().get(colIndex).addCell(new Cell<>(cellValue));
                colIndex++;
            }
        }

        return dataframe;
    }
}
