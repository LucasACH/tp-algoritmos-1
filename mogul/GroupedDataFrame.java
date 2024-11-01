
import java.util.Map;
import java.util.List;

class GroupedDataFrame {
    private Map<String, List<Row>> groups;
    private DataFrame dataframe;

    public GroupedDataFrame(DataFrame dataframe) {
        this.dataframe = dataframe;

    }

    public float sum(String column) { // TODO: Cambiar logica para que use las celdas de la columna
        for (Column<?> col : dataframe.getColumns()) {
            if (col.getLabel().equals(column)) {
                if (col.areCellsOfSameType()) {
                    if (col.getCell(0).getValue() instanceof Integer) {
                        int sum = 0;
                        for (Row row : dataframe.getRows()) {
                            sum += (int) row.getCell(col.getId()).getValue();
                        }
                        return sum;
                    } else if (col.getCell(0).getValue() instanceof Float) {
                        float sum = 0;
                        for (Row row : dataframe.getRows()) {
                            sum += (float) row.getCell(col.getId()).getValue();
                        }
                        return sum;
                    } else if (col.getCell(0).getValue() instanceof Double) {
                        double sum = 0;
                        for (Row row : dataframe.getRows()) {
                            sum += (double) row.getCell(col.getId()).getValue();
                        }
                        return (float) sum;
                    } else if (col.getCell(0).getValue() instanceof String) {
                        throw new IllegalArgumentException("Cannot sum strings.");
                    }
                }
            }
        }
    }

    public void mean(String column) {
        // calculate the mean of the values in each group
    }

    public void min(String column) {
        // find the minimum value in each group
    }

    public void max(String column) {
        // find the maximum value in each group
    }

    public void count() {
        // count the number of rows in each group
    }

    public void std(String column) {
        // calculate the standard deviation of the values in each group
    }

    public void var(String column) {
        // calculate the variance of the values in each group
    }
}
