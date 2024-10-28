package mogul;
import java.util.ArrayList;
import java.util.List;

class DataFrame<T> implements Visualizer<T> {
    private List<Column<T>> columns;
    private DataExporter<T> exporter;
    private DataManipulator<T> manipulator;
    private GroupedDataFrame analyzer;

    public DataFrame() {
        this.columns = new ArrayList<>();
    }

    public DataFrame<T> insertRow(T label, List<Cell<T>> cells) {
        if (cells.size() != columns.size()) {
            throw new IllegalArgumentException("The number of cells does not match the number of columns.");
        }

        for (int i = 0; i < columns.size(); i++) {
            columns.get(i).addCell(cells.get(i));
        }
        return this;
    }

    public int countRows() {
        return columns.isEmpty() ? 0 : columns.get(0).getCells().size();
    }

    public DataFrame<T> insertColumn(Column<T> column) {
        if (!columns.isEmpty() && column.getCells().size() != countRows()) {
            throw new IllegalArgumentException("Column size must match the number of rows in the DataFrame.");
        }
        columns.add(column);
        return this;
    }

    public int countColumns() {
        return columns.size();
    }

    public Column<T> getColumn(T label) {
        for (Column<T> column : columns) {
            if (column.getLabel().equals(label)) {
                return column;
            }
        }
        throw new IllegalArgumentException("Column with label " + label + " not found.");
    }

    public Cell<T> getCell(int row, int column) {
        if (column >= countColumns() || row >= countRows()) {
            throw new IndexOutOfBoundsException("Invalid row or column index.");
        }
        return columns.get(column).getCell(row);
    }

    public void setCell(int row, int column, T value) {
        if (column >= countColumns() || row >= countRows()) {
            throw new IndexOutOfBoundsException("Invalid row or column index.");
        }
        columns.get(column).getCell(row).setValue(value);
    }

    public DataFrame<T> copy() {
        DataFrame<T> copy = new DataFrame<>();
        for (Column<T> column : this.columns) {
            copy.insertColumn(column.copy());
        }
        return copy;
    }

    @Override
    public void show() {
        for (Column<T> column : columns) {
            System.out.print(column.getLabel() + "\t");
        }
        System.out.println();
        int numRows = countRows();
        for (int i = 0; i < numRows; i++) {
            for (Column<T> column : columns) {
                System.out.print(column.getCell(i).getValue() + "\t");
            }
            System.out.println();
        }
    }

    @Override
    public T head(int n) {
        return manipulator.slice(this, 0, Math.min(n, countRows()));
    }

    @Override
    public T tail(int n) {
        return manipulator.slice(this, countRows() - Math.min(n, countRows()), countRows());
    }

}
