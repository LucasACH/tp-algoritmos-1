import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import exceptions.IndexOutOfBounds;
import exceptions.TypeDoesNotMatch;

class DataFrame implements Visualizer<DataFrame> {
    private List<Column<?>> columns;
    private DataExporter exporter;
    private DataManipulator manipulator;
    private GroupedDataFrame analyzer;

    public DataFrame() {
        this.columns = new ArrayList<>();
        this.exporter = new DataExporter(this);
        this.manipulator = new DataManipulator(this);
        this.analyzer = new GroupedDataFrame(this);
    }
    public DataFrame(List<Column<?>> columns) {
        this.columns = columns;
        this.exporter = new DataExporter(this);
        this.manipulator = new DataManipulator(this);
        this.analyzer = new GroupedDataFrame(this);
    }

    // TODO: Revisar el casting explícito de tipo Object (preguntar tal vez)
    public DataFrame insertRow(String label, List<Cell<?>> cells) {
        if (cells.size() != columns.size()) {
            throw new IllegalArgumentException("The number of cells does not match the number of columns.");
        }
    
        for (int i = 0; i < columns.size(); i++) {
            Column<?> column = columns.get(i);
            if (!column.areCellsOfSameType()) {
                throw new IllegalArgumentException("Cell types in the row do not match column types.");
            }
            Cell<?> cell = cells.get(i);
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
            copy.insertColumn(column.copy());
        }
        return copy;
    }

    public DataFrame slice(int start, int end) {
        return new DataFrame(columns.subList(start, end));
    }

    public void deleteRow(int index) {
        if (index < 0 || index >= countRows()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        for (Column<?> column : columns) {
            column.deleteCell(index);
        }
    }

    public void show() {
        System.out.println(this);
    }

    public DataFrame head(int n) throws IndexOutOfBounds {
        List<Column<?>> newColumns = new ArrayList<>();
        for (Column<?> column : this.getColumns()) {
            try {
                Column<?> newColumn = new Column<>(column.getCells().subList(0, n), column.getLabel());
                newColumns.add(newColumn);
            } catch (IndexOutOfBoundsException e) {
                throw new IndexOutOfBounds();
            }
        }
        return new DataFrame(newColumns);
    }

    public DataFrame tail(int n) throws IndexOutOfBounds {
        List<Column<?>> newColumns = new ArrayList<>();
        for (Column<?> column : this.getColumns()) {
            try {
                Column<?> newColumn = new Column<>(column.getCells().subList(column.getSize() - n, column.getSize()), column.getLabel());
                newColumns.add(newColumn);
            } catch (IndexOutOfBoundsException e) {
                throw new IndexOutOfBounds();
            }
        }
        return new DataFrame(newColumns);
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<String> columnLabels = getColumnsLables();
        List<Integer> columnWidths = new ArrayList<>();

        // Calculate the width of each column
        for (String columnLabel : columnLabels) {
            int maxWidth = columnLabel.length();
            for (int i = 0; i < columns.get(0).getSize(); i++) {
                int cellWidth = columns.get(columnLabels.indexOf(columnLabel)).getCell(i).getValue().toString()
                        .length();
                if (cellWidth > maxWidth) {
                    maxWidth = cellWidth;
                }
            }
            columnWidths.add(maxWidth);
        }

        // Append column headers
        sb.append("| ");
        for (int i = 0; i < columnLabels.size(); i++) {
            String columnLabel = columnLabels.get(i);
            sb.append(String.format("%-" + columnWidths.get(i) + "s", columnLabel));
            sb.append(" | ");
        }
        sb.append("\n");

        // Append rows
        for (int i = 0; i < columns.get(0).getSize(); i++) {
            sb.append("| ");
            for (int j = 0; j < columns.size(); j++) {
                Column<?> column = columns.get(j);
                String cellValue = column.getCell(i).getValue().toString();
                sb.append(String.format("%-" + columnWidths.get(j) + "s", cellValue));
                sb.append(" | ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public List<Column<?>> getColumns(){
        return columns;
    }

    // Getter for columns
    public List<String> getColumnsLables() {
        List<String> labels = new ArrayList<>();
        for (Column<?> column : columns) {
            labels.add(column.getLabel());
        }
        return labels;
    }
}
