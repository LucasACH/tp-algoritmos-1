import exceptions.LabelNotFound;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class GroupedDataFrame {
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

    public Map<String, Double> sum(String label) throws LabelNotFound {
        Map<String, Double> results = new HashMap<>();
        Map<String, List<Row>> groupedData = df.getGroupByData(label); // TODO: Cambiar a que busque los grupos en GroupedDataFrame

        for (Map.Entry<String, List<Row>> entry : groupedData.entrySet()) {
            String groupKey = entry.getKey();
            List<Row> rows = entry.getValue();
            double sum = 0;

            for (Row row : rows) {
                Column<?> column = row.getColumn(label);
                if (column != null && column.getValue() != null) {
                    sum += ((Number) column.getValue()).doubleValue();
                }
            }
            results.put(groupKey, sum);
        }
        return results;
    }

    public Map<String, Double> mean(String label) throws LabelNotFound {
        Map<String, Double> results = new HashMap<>();
        Map<String, List<Row>> groupedData = df.getGroupByData(label);

        for (Map.Entry<String, List<Row>> entry : groupedData.entrySet()) {
            String groupKey = entry.getKey();
            List<Row> rows = entry.getValue();
            double sum = 0;
            int count = 0;

            for (Row row : rows) {
                Column<?> column = row.getColumn(label);
                if (column != null && column.getValue() != null) {
                    sum += ((Number) column.getValue()).doubleValue();
                    count++;
                }
            }
            results.put(groupKey, sum / count);
        }
        return results;
    }

    public Map<String, Double> min(String label) throws LabelNotFound {
        Map<String, Double> results = new HashMap<>();
        Map<String, List<Row>> groupedData = df.getGroupByData(label);

        for (Map.Entry<String, List<Row>> entry : groupedData.entrySet()) {
            String groupKey = entry.getKey();
            List<Row> rows = entry.getValue();
            double min = Double.MAX_VALUE;

            for (Row row : rows) {
                Column<?> column = row.getColumn(label);
                if (column != null && column.getValue() != null) {
                    min = Math.min(min, ((Number) column.getValue()).doubleValue());
                }
            }
            results.put(groupKey, min);
        }
        return results;
    }

    public Map<String, Double> max(String label) throws LabelNotFound {
        Map<String, Double> results = new HashMap<>();
        Map<String, List<Row>> groupedData = df.getGroupByData(label);

        for (Map.Entry<String, List<Row>> entry : groupedData.entrySet()) {
            String groupKey = entry.getKey();
            List<Row> rows = entry.getValue();
            double max = Double.MIN_VALUE;

            for (Row row : rows) {
                Column<?> column = row.getColumn(label);
                if (column != null && column.getValue() != null) {
                    max = Math.max(max, ((Number) column.getValue()).doubleValue());
                }
            }
            results.put(groupKey, max);
        }
        return results;
    }

    public Map<String, Integer> count(String label) throws LabelNotFound {
        Map<String, Integer> results = new HashMap<>();
        Map<String, List<Row>> groupedData = df.getGroupByData(label);

        for (Map.Entry<String, List<Row>> entry : groupedData.entrySet()) {
            String groupKey = entry.getKey();
            List<Row> rows = entry.getValue();
            int count = 0;

            for (Row row : rows) {
                Column<?> column = row.getColumn(label);
                if (column != null && column.getValue() != null) {
                    count++;
                }
            }
            results.put(groupKey, count);
        }
        return results;
    }

    public Map<String, Double> std(String label) throws LabelNotFound {
        Map<String, Double> results = new HashMap<>();
        Map<String, List<Row>> groupedData = df.getGroupByData(label);

        for (Map.Entry<String, List<Row>> entry : groupedData.entrySet()) {
            String groupKey = entry.getKey();
            List<Row> rows = entry.getValue();
            double sum = 0;
            double sumSquared = 0;
            int count = 0;

            for (Row row : rows) {
                Column<?> column = row.getColumn(label);
                if (column != null && column.getValue() != null) {
                    double value = ((Number) column.getValue()).doubleValue();
                    sum += value;
                    sumSquared += value * value;
                    count++;
                }
            }

            double mean = sum / count;
            double stdDev = Math.sqrt((sumSquared / count) - (mean * mean));
            results.put(groupKey, stdDev);
        }
        return results;
    }

    public Map<String, Double> var(String label) throws LabelNotFound {
        Map<String, Double> results = new HashMap<>();
        Map<String, List<Row>> groupedData = df.getGroupByData(label);

        for (Map.Entry<String, List<Row>> entry : groupedData.entrySet()) {
            String groupKey = entry.getKey();
            List<Row> rows = entry.getValue();
            double sum = 0;
            double sumSquared = 0;
            int count = 0;

            for (Row row : rows) {
                Column<?> column = row.getColumn(label);
                if (column != null && column.getValue() != null) {
                    double value = ((Number) column.getValue()).doubleValue();
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
}
