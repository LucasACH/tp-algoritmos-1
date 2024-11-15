package structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exceptions.IndexOutOfBounds;
import exceptions.InvalidShape;
import exceptions.LabelNotFound;
import exceptions.TypeDoesNotMatch;

/**
 * La clase GroupedDataFrame representa un DataFrame agrupado por una o más
 * columnas.
 * 
 * Permite realizar operaciones de agregación como sum, mean, min, max, count,
 */
public class GroupedDataFrame {
    private final DataFrame df;
    private final Map<String, List<Row>> groupedData;

    /**
     * Constructor para GroupedDataFrame con DataFrame original.
     *
     * @param df DataFrame original
     */
    public GroupedDataFrame(DataFrame df) {
        this.df = df;
        this.groupedData = new HashMap<>();
    }

    /**
     * Constructor para GroupedDataFrame con DataFrame original y datos agrupados.
     *
     * @param df          DataFrame original
     * @param groupedData Datos agrupados
     */
    public GroupedDataFrame(DataFrame df, Map<String, List<Row>> groupedData) {
        this.df = df;
        this.groupedData = groupedData;
    }

    /**
     * Imprime el DataFrame agrupado.
     */
    public void show() {
        System.out.println(this);
    }

    /**
     * Suma los valores de una columna en cada grupo.
     * 
     * @param label Etiqueta de la columna
     * @return Mapa con la suma de los valores en cada grupo
     * @throws LabelNotFound    si la etiqueta no se encuentra en el DataFrame
     * @throws IndexOutOfBounds si el índice está fuera de los límites
     */
    public Map<String, Double> sum(Object label) throws LabelNotFound, IndexOutOfBounds {
        return aggregate(label, "sum");
    }

    /**
     * Calcula el promedio de los valores de una columna en cada grupo.
     * 
     * @param label Etiqueta de la columna
     * @return Mapa con el promedio de los valores en cada grupo
     * @throws LabelNotFound    si la etiqueta no se encuentra en el DataFrame
     * @throws IndexOutOfBounds si el índice está fuera de los límites
     */
    public Map<String, Double> mean(Object label) throws LabelNotFound, IndexOutOfBounds {
        return aggregate(label, "mean");
    }

    /**
     * Encuentra el valor mínimo para una etiqueta especificada en cada grupo.
     * 
     * @param label Etiqueta de la columna
     * @return Mapa con el valor mínimo en cada grupo
     * @throws LabelNotFound    si la etiqueta no se encuentra en el DataFrame
     * @throws IndexOutOfBounds si el índice está fuera de los límites
     */
    public Map<String, Double> min(Object label) throws LabelNotFound, IndexOutOfBounds {
        return aggregate(label, "min");
    }

    /**
     * Encuentra el valor máximo para una etiqueta especificada en cada grupo.
     * 
     * @param label Etiqueta de la columna
     * @return Mapa con el valor máximo en cada grupo
     * @throws LabelNotFound    si la etiqueta no se encuentra en el DataFrame
     * @throws IndexOutOfBounds si el índice está fuera de los límites
     */
    public Map<String, Double> max(Object label) throws LabelNotFound, IndexOutOfBounds {
        return aggregate(label, "max");
    }

    /**
     * Cuenta el número de valores no nulos en una columna en cada grupo.
     * 
     * @param label Etiqueta de la columna
     * @return Mapa con el número de valores no nulos en cada grupo
     * @throws LabelNotFound    si la etiqueta no se encuentra en el DataFrame
     * @throws IndexOutOfBounds si el índice está fuera de los límites
     */
    public Map<String, Integer> count(Object label) throws LabelNotFound, IndexOutOfBounds {
        Map<String, Integer> results = new HashMap<>();
        for (Map.Entry<String, Double> entry : aggregate(label, "count").entrySet()) {
            results.put(entry.getKey(), entry.getValue().intValue());
        }
        return results;
    }

    /**
     * Calcula la desviación estándar para una etiqueta especificada en cada grupo.
     * 
     * @param label Etiqueta de la columna
     * @return Mapa con la desviación estándar en cada grupo
     * @throws LabelNotFound    si la etiqueta no se encuentra en el DataFrame
     * @throws IndexOutOfBounds si el índice está fuera de los límites
     */
    public Map<String, Double> std(Object label) throws LabelNotFound, IndexOutOfBounds {
        return aggregate(label, "std");
    }

    /**
     * Calculates the variance for a specified label in each group.
     */
    public Map<String, Double> var(Object label) throws LabelNotFound, IndexOutOfBounds {
        return aggregate(label, "var");
    }

    private Map<String, Double> aggregate(Object label, String operation)
            throws LabelNotFound, IndexOutOfBounds {
        Map<String, Double> results = new HashMap<>();
        int columnIndex = df.getColumnLabels().indexOf(label);
        if (columnIndex < 0)
            throw new LabelNotFound("Label " + label + " not found.");

        for (Map.Entry<String, List<Row>> entry : groupedData.entrySet()) {
            String groupKey = entry.getKey();
            List<Row> rows = entry.getValue();

            double result = operation.equals("min") ? Double.MAX_VALUE : operation.equals("max") ? Double.MIN_VALUE : 0;

            int count = 0;
            List<Cell<?>> values = new ArrayList<>();

            for (Row row : rows) {
                Cell<?> cell = row.getCell(columnIndex);
                if (cell != null && cell.getValue() != null) {
                    if (!"count".equals(operation)) {
                        double value = ((Number) cell.getValue()).doubleValue();
                        if ("sum".equals(operation) || "mean".equals(operation)) {
                            result += value;
                        } else if ("min".equals(operation))
                            result = Math.min(result, value);
                        else if ("max".equals(operation))
                            result = Math.max(result, value);
                        else if ("std".equals(operation) || "var".equals(operation)) {
                            result += value;
                            values.add(cell);
                        }
                    }

                    count++;
                }
            }
            if ("count".equals(operation))
                result = count;
            if ("mean".equals(operation))
                result = result / count;
            if ("std".equals(operation) || "var".equals(operation)) {
                double mean = result / count;
                double sum = 0;
                for (Cell<?> cell : values) {
                    double value = ((Number) cell.getValue()).doubleValue();
                    sum += Math.pow(value - mean, 2);
                }
                double variance = sum / count;
                result = "std".equals(operation) ? Math.sqrt(variance) : variance;
            }
            results.put(groupKey, result);
        }
        return results;
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<Row>> entry : groupedData.entrySet()) {
            sb.append(entry.getKey()).append("\n");
            try {
                sb.append(new DataFrame(entry.getValue(), this.df.getColumnLabels()).toString()).append("\n");
            } catch (IndexOutOfBounds | InvalidShape | TypeDoesNotMatch e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}