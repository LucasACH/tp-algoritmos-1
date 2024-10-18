package mogul;
import java.util.List;

class DataFrame<T> implements Visualizer<T>{
    private List<Column<T>> columns;
    private DataExporter<T> exporter;
    private DataManipulator<T> manipulator;
    private GroupedDataFrame analyzer;


    public void insertRow(T label, List<Cell<T>> cells) {
        // insert a row into the DataFrame
    }

    public void countRows() {
        // count the number of rows in the DataFrame
    }
    
    public void insertColumn(Column<T> column) {
        // insert a row into the DataFrame
    }

    public void countColumns() {
        // count the number of columns in the DataFrame
    }

    public void getColumn(T label) {
        // get a column from the DataFrame
    }

    public void getCell(int row, int column) {
        // get a cell from the DataFrame
    }

    public void setCell(int row, int column, T value) {
        // set a cell in the DataFrame
    }
    
    public void copy() {
        // copy the DataFrame
    }

    @Override
    public void show() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public T head(int n) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public T tail(int n) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
