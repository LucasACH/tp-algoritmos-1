import exceptions.LabelNotFound;
import java.util.List;

class GroupedDataFrame {
    private final DataFrame df;

    public GroupedDataFrame(DataFrame df) {
        this.df = df;
    }

    public void sum(String label) throws LabelNotFound {
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
                    System.out.println("Sum of " + columnName + " is " + sum);
            } else {
                throw new IllegalArgumentException("THe column must contain only numeric values (Integer, Double or Float).");
            }
        }
    }

    public void mean(String label) throws LabelNotFound {
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
                    System.out.println("Mean of " + columnName + " is " + sum / count);
            } else {
                throw new IllegalArgumentException("The column must contain only numeric values (Integer, Double or Float).");
            }
        }
    }

    public void min(String label) throws LabelNotFound {
        List<String> columnLabels = df.getColumnLabels();
        for (String columnName : columnLabels) {
            Column<?> column = df.getColumn(columnName);
            if (column.getLabel().equals(column) && 
                (column.getType() == Integer.class || column.getType() == Double.class || column.getType() == Float.class)) {
                    double min = Double.MAX_VALUE;
                    for (Cell<?> cell : column.getCells()) {
                        if (cell.getValue() != null) {
                            min = Math.min(min, (double) cell.getValue());
                        }
                    }
                    System.out.println("Min of " + columnName + " is " + min);
            } else {
                throw new IllegalArgumentException("The column must contain only numeric values (Integer, Double or Float).");
            }

        }
    }

    public void max(String label) throws LabelNotFound {
        List <String> columnLabels = df.getColumnLabels();
        for (String columnName : columnLabels) {

            Column<?> column = df.getColumn(columnName);
            if (column.getLabel().equals(column) && 
                (column.getType() == Integer.class || column.getType() == Double.class || column.getType() == Float.class)) {
                    double max = Double.MIN_VALUE;
                    for (Cell<?> cell : column.getCells()) {
                        if (cell.getValue() != null) {
                            max = Math.max(max, (double) cell.getValue());
                        }
                    }
                    System.out.println("Max of " + columnName + " is " + max);
            } else {
                throw new IllegalArgumentException("The column must contain only numeric values (Integer, Double or Float).");
            }
        }
    }

    public void count(String label) throws LabelNotFound {
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
                System.out.println("Count of " + columnName + " is " + count);
            }
        }
    }

    public void std(String label) throws LabelNotFound {
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
                    double std = Math.sqrt((sumSquared - Math.pow(sum, 2) / count) / (count - 1));
                    System.out.println("Std of " + columnName + " is " + std);
            } else {
                throw new IllegalArgumentException("The column must contain only numeric values (Integer, Double or Float).");
            }
        }
    }

    public void var(String label) throws LabelNotFound {
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
                    double var = (sumSquared - Math.pow(sum, 2) / count) / (count - 1);
                    System.out.println("Var of " + columnName + " is " + var);
            } else {
                throw new IllegalArgumentException("The column must contain only numeric values (Integer, Double or Float).");
        }
    }
    }
}