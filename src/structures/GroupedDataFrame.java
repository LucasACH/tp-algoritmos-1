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
 * GroupedDataFrame provides grouped operations (sum, mean, min, max, etc.)
 * on a DataFrame based on specified group keys.
 */
public class GroupedDataFrame {
    private final DataFrame df;
    private final Map<String, List<Row>> groupedData;

    /**
     * Constructor for GroupedDataFrame that initializes with an existing DataFrame.
     *
     * @param df DataFrame to be grouped
     */
    public GroupedDataFrame(DataFrame df) {
        this.df = df;
        this.groupedData = new HashMap<>();
    }

    /**
     * Constructor for GroupedDataFrame with grouped data specified.
     *
     * @param df          Original DataFrame
     * @param groupedData Grouped data by keys
     */
    public GroupedDataFrame(DataFrame df, Map<String, List<Row>> groupedData) {
        this.df = df;
        this.groupedData = groupedData;
    }

    /**
     * Prints the grouped data to console.
     */
    public void show() {
        System.out.println(this);
    }

    /**
     * Sums the values for a specified label in each group.
     *
     * @param label Column label to sum
     * @return Map of group keys to their summed values
     * @throws LabelNotFound    If the label does not exist in the DataFrame
     * @throws IndexOutOfBounds If column index is invalid
     */
    public Map<Object, Double> sum(Object label) throws LabelNotFound, IndexOutOfBounds {
        return aggregate(label, "sum");
    }

    /**
     * Calculates the mean for a specified label in each group.
     */
    public Map<Object, Double> mean(Object label) throws LabelNotFound, IndexOutOfBounds {
        return aggregate(label, "mean");
    }

    /**
     * Finds the minimum value for a specified label in each group.
     */
    public Map<Object, Double> min(Object label) throws LabelNotFound, IndexOutOfBounds {
        return aggregate(label, "min");
    }

    /**
     * Finds the maximum value for a specified label in each group.
     */
    public Map<Object, Double> max(Object label) throws LabelNotFound, IndexOutOfBounds {
        return aggregate(label, "max");
    }

    /**
     * Counts the non-null values for a specified label in each group.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map<Object, Integer> count(Object label) throws LabelNotFound, IndexOutOfBounds {
        return (Map) aggregate(label, "count");
    }

    /**
     * Calculates the standard deviation for a specified label in each group.
     */
    public Map<Object, Double> std(Object label) throws LabelNotFound, IndexOutOfBounds {
        return aggregate(label, "std");
    }

    /**
     * Calculates the variance for a specified label in each group.
     */
    public Map<Object, Double> var(Object label) throws LabelNotFound, IndexOutOfBounds {
        return aggregate(label, "var");
    }

    /**
     * Aggregates data by performing the specified operation.
     */
    private Map<Object, Double> aggregate(Object label, String operation)
            throws LabelNotFound, IndexOutOfBounds {
        Map<Object, Double> results = new HashMap<>();
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

                    count++;
                }
            }
            if ("count".equals(operation))
                result = count;
            if ("mean".equals(operation))
                result = result / count;
            if ("std".equals(operation) || "var".equals(operation)) {
                double mean = result / count;
                double variance = 0;
                for (Cell<?> cell : values) {
                    double value = ((Number) cell.getValue()).doubleValue();
                    variance += Math.pow(value - mean, 2);
                }
                result = "std".equals(operation) ? Math.sqrt(variance) : variance;
            }
            results.put(groupKey, result);
        }
        return results;
    }

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