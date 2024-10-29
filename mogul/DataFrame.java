import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class DataFrame implements Visualizer<DataFrame> {
    private List<Column<?>> columns;
    private DataExporter exporter;
    private DataManipulator manipulator;
    private GroupedDataFrame analyzer;

    public DataFrame() {
        this.columns = new ArrayList<>();
        this.exporter = new DataExporter(this);
        this.manipulator = new DataManipulator();
        this.analyzer = new GroupedDataFrame();
    }

    // TODO: Revisar el casting explícito de tipo Object (preguntar tal vez)
    public DataFrame insertRow(String label, List<Cell<?>> cells) {
        if (cells.size() != columns.size()) {
            throw new IllegalArgumentException("The number of cells does not match the number of columns.");
        }

        for (int i = 0; i < columns.size(); i++) {
            Column<?> column = columns.get(i);
            Cell<?> cell = cells.get(i);
        
            if (!column.areCellsOfSameType()) {
                throw new IllegalArgumentException("Cell types in the row do not match column types.");
            }
            ((Column<Object>) column).addCell((Cell<Object>) cell); // Casting explícito
        }
        
        return this;
    }

    public DataFrame insertRow(int id, List<Cell<?>> cells) {
        if (cells.size() != columns.size()) {
            throw new IllegalArgumentException("The number of cells does not match the number of columns.");
        }

        for (int i = 0; i < columns.size(); i++) {
            Column<?> column = columns.get(i);
            Cell<?> cell = cells.get(i);
        
            if (!column.areCellsOfSameType()) {
                throw new IllegalArgumentException("Cell types in the row do not match column types.");
            }
            ((Column<Object>) column).addCell((Cell<Object>) cell); // Casting explícito 
        }
        
        return this;
    }

    public int countRows() {
        return columns.isEmpty() ? 0 : columns.get(0).getCells().size();
    }

    public DataFrame insertColumn(Column<?> column) {
        if (!columns.isEmpty() && column.getCells().size() != countRows()) {
            throw new IllegalArgumentException("Column size must match the number of rows in the DataFrame.");
        }
        columns.add(column);
        return this;
    }

    public int countColumns() {
        return columns.size();
    }

    public Column<?> getColumn(String label) {
        for (Column<?> column : columns) {
            if (column.getLabel().equals(label)) {
                return column;
            }
        }
        throw new IllegalArgumentException("Column with label " + label + " not found.");
    }

    public Cell<?> getCell(int row, int column) {
        if (column >= countColumns() || row >= countRows()) {
            throw new IndexOutOfBoundsException("Invalid row or column index.");
        }
        return columns.get(column).getCell(row);
    }

    public <T> void setCell(int rowIndex, int columnIndex, T value) throws ClassCastException {
        if (columnIndex >= countColumns() || rowIndex >= countRows()) {
            throw new IndexOutOfBoundsException("Invalid row or column index.");
        }

        Column<T> column = (Column<T>) columns.get(columnIndex);  

        if (!column.getClass().isInstance(value)) {
            throw new IllegalArgumentException("Value type does not match cell type");
        }

        column.setCell(rowIndex, value);
    }

    public DataFrame copy() {
        DataFrame copy = new DataFrame();
        for (Column<?> column : this.columns) {
            copy.insertColumn(column.copy()); // TODO: Implementar el método copy en Column (deep copy)
        }
        return copy;
    }

    @Override
    public void show() {
        for (Column<?> column : columns) {
            System.out.print(column.getLabel() + "\t");
        }
        System.out.println();
        int numRows = countRows();
        for (int i = 0; i < numRows; i++) {
            for (Column<?> column : columns) {
                System.out.print(column.getCell(i).getValue() + "\t");
            }
            System.out.println();
        }
    }

    @Override
    public DataFrame head(int n) {
        return manipulator.slice(this, 0, Math.min(n, countRows()));
    }

    @Override
    public DataFrame tail(int n) {
        return manipulator.slice(this, countRows() - Math.min(n, countRows()), countRows());
    }

    // Export methods
    public void exportToCSV(String path) {
        try {
            exporter.toCSV(path);
        } catch (IOException error) {
            error.printStackTrace();
            // Handle the exception as needed
        }
    }

    public void exportToJSON(String path) {
        try {
            exporter.toJSON(path);
        } catch (IOException error) {
            error.printStackTrace();
            // Handle the exception as needed
        }
    }

    // Getter for columns
    public List<Column<?>> getColumns() {
        return columns;
    } // TODO: Revisar esta implementación porque esta devolviendo el df entero
}
