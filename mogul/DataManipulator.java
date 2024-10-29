

import java.util.List;
import java.util.function.Predicate;

class DataManipulator {
    private DataFrame dataframe;

    public void sortBy(List<String> columns, boolean descending) {
        // sort the DataFrame by the specified columns
    }

    public <T> void fillna(String column, T value) {
        // fill missing values in the DataFrame
    }

    public void filter(String name, Predicate<String> function) {
        // filter the DataFrame by the specified function
    }

    public DataFrame slice(DataFrame df, int start, int end) {
        DataFrame slicedDF = new DataFrame();

        // Iterar por cada columna y crear una sublista de celdas en el rango
        for (Column<?> column : df.getColumns()) {
            List<Cell<?>> cells = column.getCells().subList(start, end);
            Column<Cell<?>> newColumn = new Column<>(cells, column.getLabel()); // TODO: no se pudo solucionar el error
            slicedDF.insertColumn(newColumn);
        }

        return slicedDF;
    }

    public void sample(double frac) {
        // sample the DataFrame
    }

    public void groupBy(List<String> columns) {
        // group the DataFrame by the specified columns
    }
}
