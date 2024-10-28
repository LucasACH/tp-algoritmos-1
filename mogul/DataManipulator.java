package mogul;
import java.util.List;
import java.util.function.Predicate;

class DataManipulator <T>{
    private DataFrame<T> dataframe;


    public void sortBy(List<String> columns, boolean descending) {
        // sort the DataFrame by the specified columns
    }

    public void fillna(String column, T value) {
        // fill missing values in the DataFrame
    }

    public void filter(String name, Predicate<String> function) {
        // filter the DataFrame by the specified function
    }


    
    public DataFrame<T> slice(DataFrame<T> df, int start, int end) {
        DataFrame<T> slicedDF = new DataFrame<>();

        // Iterar por cada columna y crear una sublista de celdas en el rango
        for (Column<T> column : df.getColumns()) {
            List<Cell<T>> cells = column.getCells().subList(start, end);
            Column<T> newColumn = new Column<T>(cells, column.getLabel());
            slicedDF.insertColumn(newColumn);
        }

        return slicedDF;
    }
    
        // Aquí también se podrían agregar los métodos `filter`, `fillna`, `sample`, y `groupBy`
    }
    

    public void sample(double frac){
        // sample the DataFrame
    }

    public void groupBy(List<String> columns) {
        // group the DataFrame by the specified columns
    }   
}
