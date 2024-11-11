package libraries;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import exceptions.IndexOutOfBounds;
import exceptions.InvalidShape;
import exceptions.LabelAlreadyInUse;
import exceptions.TypeDoesNotMatch;
import structures.DataFrame;

public class DataImporter {

    public static DataFrame readCSV(String path)
            throws IOException, InvalidShape, TypeDoesNotMatch, LabelAlreadyInUse, IndexOutOfBounds {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            List<String> headers = parseHeaders(reader.readLine());
            List<List<?>> rows = parseCSVRows(reader);
            return new DataFrame(rows, headers);
        }
    }

    private static List<String> parseHeaders(String headerLine) {
        List<String> headers = new ArrayList<>();
        for (String header : headerLine.split(",")) {
            headers.add(header.trim());
        }
        return headers;
    }

    private static List<List<?>> parseCSVRows(BufferedReader reader) throws IOException {
        List<List<?>> rows = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(",");
            List<Object> row = new ArrayList<>();
            for (String value : values) {
                row.add(parseCellValue(value.trim()));
            }
            rows.add(row);
        }
        return rows;
    }

    private static Object parseCellValue(String value) {
        if (value.matches("-?\\d+")) {
            return Integer.parseInt(value);
        } else if (value.matches("-?\\d*\\.\\d+")) {
            return Double.parseDouble(value);
        } else if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(value);
        }
        return value;
    }

    public static DataFrame readJSON(String path)
            throws IOException, InvalidShape, TypeDoesNotMatch, LabelAlreadyInUse, IndexOutOfBounds {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String jsonContent = readEntireFile(reader);
            List<String> headers = new ArrayList<>();
            List<List<?>> rows = parseJSONRows(jsonContent, headers);
            return new DataFrame(rows, headers);
        }
    }

    private static String readEntireFile(BufferedReader reader) throws IOException {
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line.trim());
        }
        return content.toString();
    }

    private static List<List<?>> parseJSONRows(String jsonContent, List<String> headers) {
        List<List<?>> rows = new ArrayList<>();
        String[] jsonRows = jsonContent.substring(1, jsonContent.length() - 1).split("\\},\\{");

        for (String jsonRow : jsonRows) {
            List<Object> row = parseJSONRow(jsonRow, headers);
            rows.add(row);
        }
        return rows;
    }

    private static List<Object> parseJSONRow(String jsonRow, List<String> headers) {
        List<Object> row = new ArrayList<>();
        String[] jsonCells = jsonRow.replace("{", "").replace("}", "").split(",");

        for (String cell : jsonCells) {
            String[] parts = cell.split(":");
            String header = parts[0].replace("\"", "").trim();
            String value = parts[1].replace("\"", "").trim();

            if (!headers.contains(header)) {
                headers.add(header);
            }
            row.add(parseCellValue(value));
        }
        return row;
    }
}