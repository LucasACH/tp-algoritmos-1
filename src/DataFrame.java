import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import exceptions.IndexOutOfBounds;
import exceptions.InvalidShape;
import exceptions.LabelAlreadyInUse;
import exceptions.LabelNotFound;
import exceptions.TypeDoesNotMatch;

class DataFrame implements Visualizer<DataFrame> {
    private List<Column<?>> columns;
    private List<Row> rows;

    private final DataExporter exporter;

    public DataFrame() {
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();
        this.exporter = new DataExporter(this);
    }

    public DataFrame(List<Column<?>> columns) {
        this.columns = columns;

        this.rows = new ArrayList<>();
        for (int i = 0; i < countRows(); i++) {
            List<Cell<?>> cells = new ArrayList<>();
            for (Column<?> column : columns) {
                try {
                    cells.add(column.getCell(i));
                } catch (IndexOutOfBounds e) {
                    e.printStackTrace();
                }
            }
            rows.add(new Row(i, cells));
        }

        this.exporter = new DataExporter(this);
    }

    public DataFrame insertRow(List<Cell<?>> cells) throws InvalidShape, TypeDoesNotMatch, LabelAlreadyInUse {
        insertCells(countRows(), cells);
        return this;
    }

    public <T> DataFrame insertRow(T label, List<Cell<?>> cells)
            throws InvalidShape, TypeDoesNotMatch, LabelAlreadyInUse {
        insertCells(label, cells);
        return this;
    }

    private void insertCells(Object label, List<Cell<?>> cells)
            throws InvalidShape, TypeDoesNotMatch, LabelAlreadyInUse {
        assignCellsToColumns(cells);

        for (Row row : rows) {
            if (row.getLabel().equals(label)) {
                throw new LabelAlreadyInUse();
            }
        }

        rows.add(new Row(label, cells));
    }

    @SuppressWarnings("unchecked")
    private void assignCellsToColumns(List<Cell<?>> cells) throws InvalidShape, TypeDoesNotMatch {
        validateShape(cells);
        for (int i = 0; i < columns.size(); i++) {
            Column<Object> column = (Column<Object>) columns.get(i);
            column.addCell((Cell<Object>) cells.get(i));
        }

    }

    public DataFrame insertColumn(Column<?> column) throws InvalidShape {
        validateColumnShape(column);
        columns.add(column);
        return this;
    }

    public int countRows() {
        return columns.isEmpty() ? 0 : columns.get(0).getCells().size();
    }

    public int countColumns() {
        return columns.size();
    }

    public Column<?> getColumn(String label) throws LabelNotFound {
        return columns.stream()
                .filter(column -> column.getLabel().equals(label))
                .findFirst()
                .orElseThrow(LabelNotFound::new);
    }

    public Row getRow(int index) throws IndexOutOfBounds {
        if (!isValidRowIndex(index)) {
            throw new IndexOutOfBounds();
        }
        return rows.get(index);
    }

    public Cell<?> getCell(int rowIndex, int columnIndex) throws IndexOutOfBounds {
        validateIndices(rowIndex, columnIndex);
        return columns.get(columnIndex).getCell(rowIndex);
    }

    @SuppressWarnings("unchecked")
    public <T> void setCell(int rowIndex, int columnIndex, T value) throws IndexOutOfBounds, TypeDoesNotMatch {
        validateIndices(rowIndex, columnIndex);
        ((Column<T>) columns.get(columnIndex)).setCell(rowIndex, value);
    }

    public DataFrame copy() throws InvalidShape {
        DataFrame copy = new DataFrame();
        for (Column<?> column : this.columns) {
            copy.insertColumn(column.copy());
        }
        return copy;
    }

    public DataFrame slice(int start, int end) {
        return new DataFrame(columns.subList(start, end));
    }

    public DataFrame head(int n) throws IndexOutOfBounds {
        return createSubDataFrame(0, n);
    }

    public DataFrame tail(int n) throws IndexOutOfBounds {
        return createSubDataFrame(countRows() - n, countRows());
    }

    public void exportToCSV(String path) throws IndexOutOfBounds {
        exportData(path, ExportFormat.CSV);
    }

    public void exportToJSON(String path) throws IndexOutOfBounds {
        exportData(path, ExportFormat.JSON);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        appendColumnHeaders(sb);
        appendRowData(sb);
        return sb.toString();
    }

    @Override
    public void show() {
        System.out.println(this);
    }

    public List<Column<?>> getColumns() {
        return columns;
    }

    public List<Row> getRows() {
        return rows;
    }

    public List<String> getColumnLabels() {
        List<String> labels = new ArrayList<>();
        for (Column<?> column : columns) {
            labels.add(column.getLabel().toString());
        }
        return labels;
    }

    private void validateShape(List<Cell<?>> cells) throws InvalidShape {
        if (cells.size() != columns.size()) {
            throw new InvalidShape();
        }
    }

    private void validateColumnShape(Column<?> column) throws InvalidShape {
        if (!columns.isEmpty() && column.getCells().size() != countRows()) {
            throw new InvalidShape();
        }
    }

    private boolean isValidRowIndex(int index) {
        return index >= 0 && index < countRows();
    }

    private void validateIndices(int rowIndex, int columnIndex) throws IndexOutOfBounds {
        if (!isValidRowIndex(rowIndex) || columnIndex < 0 || columnIndex >= countColumns()) {
            throw new IndexOutOfBounds();
        }
    }

    private DataFrame createSubDataFrame(int start, int end) throws IndexOutOfBounds {
        List<Column<?>> newColumns = new ArrayList<>();
        for (Column<?> column : this.getColumns()) {
            try {
                Column<?> newColumn = new Column<>(column.getLabel(), column.getCells().subList(start, end));
                newColumns.add(newColumn);
            } catch (IndexOutOfBoundsException e) {
                throw new IndexOutOfBounds();
            }
        }
        return new DataFrame(newColumns);
    }

    private void exportData(String path, ExportFormat format) throws IndexOutOfBounds {
        try {
            if (format == ExportFormat.CSV) {
                exporter.toCSV(path);
            } else if (format == ExportFormat.JSON) {
                exporter.toJSON(path);
            }
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    private void appendColumnHeaders(StringBuilder sb) {
        List<String> columnLabels = getColumnLabels();
        List<Integer> columnWidths = calculateColumnWidths(columnLabels);

        columnLabels.add(0, "Label");
        columnWidths.add(0, 5);

        sb.append("| ");
        for (int i = 0; i < columnLabels.size(); i++) {
            sb.append(String.format("%-" + columnWidths.get(i) + "s", columnLabels.get(i)));
            sb.append(" | ");
        }
        sb.append("\n");

        // Horizontal line
        sb.append("|");
        for (int width : columnWidths) {
            sb.append("-".repeat(width + 2)).append("|");
        }
        sb.append("\n");
    }

    private List<Integer> calculateColumnWidths(List<String> columnLabels) {
        List<Integer> columnWidths = new ArrayList<>();

        for (String columnLabel : columnLabels) {
            int maxWidth = columnLabel.length();
            for (int i = 0; i < countRows(); i++) {
                try {
                    int cellWidth = columns.get(columnLabels.indexOf(columnLabel)).getCell(i).getValue().toString()
                            .length();
                    if (cellWidth > maxWidth) {
                        maxWidth = cellWidth;
                    }
                } catch (IndexOutOfBounds e) {
                    e.printStackTrace();
                }
            }
            columnWidths.add(maxWidth);
        }

        return columnWidths;
    }

    private void appendRowData(StringBuilder sb) {
        for (int i = 0; i < countRows(); i++) {
            sb.append("| ");
            sb.append(String.format("%-" + 5 + "s", rows.get(i).getLabel()));
            sb.append(" | ");
            for (int j = 0; j < countColumns(); j++) {
                Column<?> column = columns.get(j);
                try {
                    String cellValue = column.getCell(i).getValue().toString();
                    sb.append(String.format("%-" + calculateColumnWidths(getColumnLabels()).get(j) + "s", cellValue));
                    sb.append(" | ");
                } catch (IndexOutOfBounds e) {
                    e.printStackTrace();
                }
            }
            sb.append("\n");
        }
    }

    private enum ExportFormat {
        CSV, JSON
    }
}