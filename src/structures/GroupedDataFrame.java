package structures;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exceptions.IndexOutOfBounds;
import exceptions.InvalidShape;
import exceptions.LabelNotFound;
import exceptions.TypeDoesNotMatch;

public class GroupedDataFrame {
    private final DataFrame df;
    private final Map<String, List<Row>> groupedData;

    public GroupedDataFrame(DataFrame df) {
        this.df = df;
        this.groupedData = new HashMap<String, List<Row>>();
    }

    public GroupedDataFrame(DataFrame df, Map<String, List<Row>> groupedData) {
        this.df = df;
        this.groupedData = groupedData;
    }

    public void show() {
        System.out.println(this);
    }

    public Map<Object, Double> sum(Object label) throws LabelNotFound, IndexOutOfBounds {
        Map<Object, Double> results = new HashMap<>();
        int columnIndex = df.getColumnLabels().indexOf(label);

        for (Map.Entry<String, List<Row>> entry : groupedData.entrySet()) {
            String groupKey = entry.getKey();
            List<Row> rows = entry.getValue();
            double sum = 0;

            for (Row row : rows) {
                Cell<?> cell = row.getCell(columnIndex);
                if (cell != null && cell.getValue() != null) {
                    sum += ((Number) cell.getValue()).doubleValue();
                }
            }
            results.put(groupKey, sum);
        }
        return results;
    }

    public Map<Object, Double> mean(Object label) throws LabelNotFound, IndexOutOfBounds {
        Map<Object, Double> results = new HashMap<>();
        int columnIndex = df.getColumnLabels().indexOf(label);

        for (Map.Entry<String, List<Row>> entry : groupedData.entrySet()) {
            String groupKey = entry.getKey();
            List<Row> rows = entry.getValue();
            double sum = 0;
            int count = 0;

            for (Row row : rows) {
                Cell<?> cell = row.getCell(columnIndex);
                if (cell != null && cell.getValue() != null) {
                    sum += ((Number) cell.getValue()).doubleValue();
                    count++;
                }
            }
            results.put(groupKey, sum / count);
        }
        return results;
    }

    public Map<Object, Double> min(Object label) throws LabelNotFound, IndexOutOfBounds {
        Map<Object, Double> results = new HashMap<>();
        int columnIndex = df.getColumnLabels().indexOf(label);

        for (Map.Entry<String, List<Row>> entry : groupedData.entrySet()) {
            String groupKey = entry.getKey();
            List<Row> rows = entry.getValue();
            double min = Double.MAX_VALUE;

            for (Row row : rows) {
                Cell<?> cell = row.getCell(columnIndex);
                if (cell != null && cell.getValue() != null) {
                    min = Math.min(min, ((Number) cell.getValue()).doubleValue());
                }
            }
            results.put(groupKey, min);
        }
        return results;
    }

    public Map<Object, Double> max(Object label) throws LabelNotFound, IndexOutOfBounds {
        Map<Object, Double> results = new HashMap<>();
        int columnIndex = df.getColumnLabels().indexOf(label);

        for (Map.Entry<String, List<Row>> entry : groupedData.entrySet()) {
            String groupKey = entry.getKey();
            List<Row> rows = entry.getValue();
            double max = Double.MIN_VALUE;

            for (Row row : rows) {
                Cell<?> cell = row.getCell(columnIndex);
                if (cell != null && cell.getValue() != null) {
                    max = Math.max(max, ((Number) cell.getValue()).doubleValue());
                }
            }
            results.put(groupKey, max);
        }
        return results;
    }

    public Map<Object, Integer> count(Object label) throws LabelNotFound, IndexOutOfBounds {
        Map<Object, Integer> results = new HashMap<>();
        int columnIndex = df.getColumnLabels().indexOf(label);

        for (Map.Entry<String, List<Row>> entry : groupedData.entrySet()) {
            String groupKey = entry.getKey();
            List<Row> rows = entry.getValue();
            int count = 0;

            for (Row row : rows) {
                Cell<?> cell = row.getCell(columnIndex);
                if (cell != null && cell.getValue() != null) {
                    count++;
                }
            }
            results.put(groupKey, count);
        }
        return results;
    }

    public Map<Object, Double> std(Object label) throws LabelNotFound, IndexOutOfBounds {
        Map<Object, Double> results = new HashMap<>();
        int columnIndex = df.getColumnLabels().indexOf(label);

        for (Map.Entry<String, List<Row>> entry : groupedData.entrySet()) {
            String groupKey = entry.getKey();
            List<Row> rows = entry.getValue();
            double sum = 0;
            double sumSquared = 0;
            int count = 0;

            for (Row row : rows) {
                Cell<?> cell = row.getCell(columnIndex);
                if (cell != null && cell.getValue() != null) {
                    double value = ((Number) cell.getValue()).doubleValue();
                    sum += value;
                    sumSquared += value * value;
                    count++;
                }
            }

            double mean = sum / count;
            double variance = (sumSquared / count) - (mean * mean);
            results.put(groupKey, Math.sqrt(variance));
        }
        return results;
    }

    public Map<Object, Double> var(Object label) throws LabelNotFound, IndexOutOfBounds {
        Map<Object, Double> results = new HashMap<>();
        int columnIndex = df.getColumnLabels().indexOf(label);

        for (Map.Entry<String, List<Row>> entry : groupedData.entrySet()) {
            String groupKey = entry.getKey();
            List<Row> rows = entry.getValue();
            double sum = 0;
            double sumSquared = 0;
            int count = 0;

            for (Row row : rows) {
                Cell<?> cell = row.getCell(columnIndex);
                if (cell != null && cell.getValue() != null) {
                    double value = ((Number) cell.getValue()).doubleValue();
                    sum += value;
                    sumSquared += value * value;
                    count++;
                }
            }

            double mean = sum / count;
            double variance = (sumSquared / count) - (mean * mean);
            results.put(groupKey, variance);
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
