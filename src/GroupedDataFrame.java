import exceptions.LabelNotFound;
import java.util.List;

class GroupedDataFrame {
    private final DataFrame df;

    public GroupedDataFrame(DataFrame df) {
        this.df = df;
    }

    public double sum(String label) throws LabelNotFound {
        List<String> columnLabels = df.getColumnLabels();
        for (String columnName : columnLabels) {
            Column<?> column = df.getColumn(columnName);
            if (column.getLabel().equals(label) && 
                (column.getType() == Integer.class || column.getType() == Double.class || column.getType() == Float.class)) {
                    double sum = 0;
                    for (Cell<?> cell : column.getCells()) {
                        if (cell.getValue() != null) {
                            sum += (double) cell.getValue();
                        }
                    }
                    return sum;
            }
        }
        throw new IllegalArgumentException("The column must contain only numeric values (Integer, Double or Float).");
    }

    public double mean(String label) throws LabelNotFound {
        List<String> columnLabels = df.getColumnLabels();
        for (String columnName : columnLabels) {
            Column<?> column = df.getColumn(columnName);
            if (column.getLabel().equals(label) && 
                (column.getType() == Integer.class || column.getType() == Double.class || column.getType() == Float.class)) {
                    double sum = 0;
                    int count = 0;
                    for (Cell<?> cell : column.getCells()) {
                        if (cell.getValue() != null) {
                            sum += (double) cell.getValue();
                            count++;
                        }
                    }
                    return sum / count;
            }
        }
        throw new IllegalArgumentException("The column must contain only numeric values (Integer, Double or Float).");
    }

    public double min(String label) throws LabelNotFound {
        List<String> columnLabels = df.getColumnLabels();
        for (String columnName : columnLabels) {
            Column<?> column = df.getColumn(columnName);
            if (column.getLabel().equals(label) && 
                (column.getType() == Integer.class || column.getType() == Double.class || column.getType() == Float.class)) {
                    double min = Double.MAX_VALUE;
                    for (Cell<?> cell : column.getCells()) {
                        if (cell.getValue() != null) {
                            min = Math.min(min, (double) cell.getValue());
                        }
                    }
                    return min;
            }
        }
        throw new IllegalArgumentException("The column must contain only numeric values (Integer, Double or Float).");
    }

    public double max(String label) throws LabelNotFound {
        List<String> columnLabels = df.getColumnLabels();
        for (String columnName : columnLabels) {
            Column<?> column = df.getColumn(columnName);
            if (column.getLabel().equals(label) && 
                (column.getType() == Integer.class || column.getType() == Double.class || column.getType() == Float.class)) {
                    double max = Double.MIN_VALUE;
                    for (Cell<?> cell : column.getCells()) {
                        if (cell.getValue() != null) {
                            max = Math.max(max, (double) cell.getValue());
                        }
                    }
                    return max;
            }
        }
        throw new IllegalArgumentException("The column must contain only numeric values (Integer, Double or Float).");
    }

    public int count(String label) throws LabelNotFound {
        List<String> columnLabels = df.getColumnLabels();
        for (String columnName : columnLabels) {
            Column<?> column = df.getColumn(columnName);
            if (column.getLabel().equals(label)) {
                int count = 0;
                for (Cell<?> cell : column.getCells()) {
                    if (cell.getValue() != null) {
                        count++;
                    }
                }
                return count;
            }
        }
        throw new LabelNotFound("Label not found in columns.");
    }

    public double std(String label) throws LabelNotFound {
        List<String> columnLabels = df.getColumnLabels();
        for (String columnName : columnLabels) {
            Column<?> column = df.getColumn(columnName);
            if (column.getLabel().equals(label) && 
                (column.getType() == Integer.class || column.getType() == Double.class || column.getType() == Float.class)) {
                    double sum = 0;
                    double sumSquared = 0;
                    int count = 0;
                    for (Cell<?> cell : column.getCells()) {
                        if (cell.getValue() != null) {
                            sum += (double) cell.getValue();
                            sumSquared += Math.pow((double) cell.getValue(), 2);
                            count++;
                        }
                    }
                    return Math.sqrt((sumSquared - Math.pow(sum, 2) / count) / (count - 1));
            }
        }
        throw new IllegalArgumentException("The column must contain only numeric values (Integer, Double or Float).");
    }

    public double var(String label) throws LabelNotFound {
        List<String> columnLabels = df.getColumnLabels();
        for (String columnName : columnLabels) {
            Column<?> column = df.getColumn(columnName);
            if (column.getLabel().equals(label) && 
                (column.getType() == Integer.class || column.getType() == Double.class || column.getType() == Float.class)) {
                    double sum = 0;
                    double sumSquared = 0;
                    int count = 0;
                    for (Cell<?> cell : column.getCells()) {
                        if (cell.getValue() != null) {
                            sum += (double) cell.getValue();
                            sumSquared += Math.pow((double) cell.getValue(), 2);
                            count++;
                        }
                    }
                    return (sumSquared - Math.pow(sum, 2) / count) / (count - 1);
            }
        }
        throw new IllegalArgumentException("The column must contain only numeric values (Integer, Double or Float).");
    }
}
