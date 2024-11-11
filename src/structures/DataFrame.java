package structures;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import exceptions.IndexOutOfBounds;
import exceptions.InvalidShape;
import exceptions.LabelAlreadyInUse;
import exceptions.LabelNotFound;
import exceptions.TypeDoesNotMatch;
import interfaces.Visualizer;
import libraries.DataExporter;
import libraries.DataManipulator;

public class DataFrame implements Visualizer<DataFrame> {
    private List<Column<?>> columns;
    private List<Row> rows;
    public final DataManipulator manipulator;
    public final DataExporter exporter;
    public final GroupedDataFrame groupedDataFrame;

    public DataFrame() {
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();
        this.exporter = new DataExporter(this);
        this.manipulator = new DataManipulator(this);
        this.groupedDataFrame = new GroupedDataFrame(this);
    }

    public DataFrame(List<?> rows, List<?> headers) throws InvalidShape, TypeDoesNotMatch, IndexOutOfBounds {
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();
        this.exporter = new DataExporter(this);
        this.manipulator = new DataManipulator(this);
        this.groupedDataFrame = new GroupedDataFrame(this);

        if (!rows.isEmpty() && rows.get(0) instanceof Row || rows.get(0) instanceof List<?>) {
            initializeColumnsFromData(rows, headers);
        }

        initializeRowsWithCells(columns);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void initializeColumnsFromData(List<?> rows, List<?> headers) throws InvalidShape, TypeDoesNotMatch {
        if (rows.get(0) instanceof Row) {
            for (Row row : (List<Row>) rows) {
                if (row.size() != headers.size()) {
                    throw new InvalidShape();
                }
            }
        }

        if (rows.get(0) instanceof List<?>) {
            for (List<?> row : (List<List<?>>) rows) {
                if (row.size() != headers.size()) {
                    throw new InvalidShape();
                }
            }
        }

        for (int i = 0; i < headers.size(); i++) {
            Column<?> column = new Column<>(headers.get(i).toString());
            if (rows.get(0) instanceof Row) {
                for (Row row : (List<Row>) rows) {
                    column.addCell(new Cell(row.getCell(i).getValue()));
                }
            }
            if (rows.get(0) instanceof List<?>) {
                for (List<?> row : (List<List<?>>) rows) {
                    column.addCell(new Cell(row.get(i)));
                }
            }
            columns.add(column);
        }
    }

    public DataFrame(List<?> rows) throws IndexOutOfBounds, InvalidShape, TypeDoesNotMatch {
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();
        this.exporter = new DataExporter(this);
        this.manipulator = new DataManipulator(this);
        this.groupedDataFrame = new GroupedDataFrame(this);

        if (!rows.isEmpty() && rows.get(0) instanceof Row) {
            List<String> headers = new ArrayList<>();

            for (int i = 0; i < ((Row) rows.get(0)).size(); i++) {
                headers.add("Column " + i);
            }

            initializeColumnsFromData(rows, headers);
        }

        if (rows.get(0) instanceof List<?>) {
            List<String> headers = new ArrayList<>();

            for (int i = 0; i < ((List<?>) rows.get(0)).size(); i++) {
                headers.add("Column " + i);
            }

            initializeColumnsFromData(rows, headers);
        }

        initializeRowsWithCells(columns);
    }

    private void initializeRowsWithCells(List<Column<?>> columns) throws IndexOutOfBounds {
        for (int i = 0; i < countRows(); i++) {
            List<Cell<?>> cells = new ArrayList<>();
            for (Column<?> column : columns) {
                cells.add(column.getCell(i));
            }
            rows.add(new Row(i, cells));
        }
    }

    private enum ExportFormat {
        CSV, JSON
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public DataFrame insertRow(List<?> row) throws InvalidShape, TypeDoesNotMatch, LabelAlreadyInUse {
        if (row.get(0) instanceof Cell) {
            insertCells(countRows(), (List<Cell<?>>) row);
        } else {
            List<Cell<?>> cells = new ArrayList<>();
            for (int i = 0; i < row.size(); i++) {
                cells.add(new Cell(row.get(i)));
            }
            insertCells(countRows(), cells);
        }
        return this;
    }

    public DataFrame insertRow(Object label, List<Cell<?>> cells)
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

    public DataFrame insertColumn(Object label) throws InvalidShape, TypeDoesNotMatch {
        Column<?> column = new Column<>(label);
        columns.add(column);
        return this;
    }

    public DataFrame insertColumn(Object label, List<?> data) throws InvalidShape, TypeDoesNotMatch {
        validateColumnShape(data);
        Column<?> column = createColumnWithData(label, data);
        columns.add(column);
        return this;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Column<?> createColumnWithData(Object label, List<?> data) throws TypeDoesNotMatch {
        Column<?> column = new Column<>(label);
        for (int i = 0; i < data.size(); i++) {
            column.addCell(new Cell(data.get(i)));
        }
        return column;
    }

    public DataFrame insertColumn(List<?> data) throws InvalidShape, TypeDoesNotMatch {
        validateColumnShape(data);
        Column<?> column = createColumnWithData("Column " + countColumns(), data);
        columns.add(column);
        return this;
    }

    public DataFrame insertColumn(Column<?> column) throws InvalidShape {
        columns.add(column);
        return this;
    }

    public int countRows() {
        return columns.isEmpty() ? 0 : columns.get(0).getCells().size();
    }

    public int countColumns() {
        return columns.size();
    }

    public Column<?> getColumn(Object label) throws LabelNotFound {
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

    public DataFrame copy() throws InvalidShape, TypeDoesNotMatch, LabelAlreadyInUse, IndexOutOfBounds {
        List<Column<?>> copiedColumns = new ArrayList<>();
        for (int i = 0; i < countColumns(); i++) {
            copiedColumns.add(columns.get(i).copy());
        }
        return new DataFrame(copiedColumns);
    }

    public DataFrame head(int n) throws IndexOutOfBounds, InvalidShape, TypeDoesNotMatch {
        return createSubDataFrame(0, n);
    }

    public DataFrame tail(int n) throws IndexOutOfBounds, InvalidShape, TypeDoesNotMatch {
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

    public List<Object> getColumnLabels() {
        List<Object> labels = new ArrayList<>();
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

    private void validateColumnShape(List<?> column) throws InvalidShape {
        if (column instanceof Column) {
            if (!columns.isEmpty() && ((Column<?>) column).getCells().size() != countRows()) {
                throw new InvalidShape();
            }
        } else {
            if (column.size() != countRows()) {
                throw new InvalidShape();
            }
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

    private DataFrame createSubDataFrame(int start, int end) throws IndexOutOfBounds, InvalidShape, TypeDoesNotMatch {
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
        List<Object> columnLabels = getColumnLabels();
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
            for (int i = 0; i < width + 2; i++) {
                sb.append("-");
            }
            sb.append("|");
        }
        sb.append("\n");
    }

    private List<Integer> calculateColumnWidths(List<Object> columnLabels) {
        List<Integer> columnWidths = new ArrayList<>();

        for (Object columnLabel : columnLabels) {
            int maxWidth = columnLabel.toString().length();
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

    public DataFrame filter(Object label, Predicate<Object> condition)
            throws LabelNotFound, InvalidShape, TypeDoesNotMatch, IndexOutOfBounds {
        return manipulator.filter(label, condition);
    }

    public void fillna(Object label, Object value) throws LabelNotFound, TypeDoesNotMatch, IndexOutOfBounds {
        manipulator.fillna(label, value);
    }

    public DataFrame sample(double frac) throws InvalidShape, TypeDoesNotMatch, LabelAlreadyInUse, IndexOutOfBounds {
        return manipulator.sample(frac);
    }

    public DataFrame sortBy(List<Object> labels, boolean descending)
            throws LabelNotFound, InvalidShape, TypeDoesNotMatch, IndexOutOfBounds {
        return manipulator.sortBy(labels, descending);
    }

    public DataFrame slice(int start, int end)
            throws IndexOutOfBounds, InvalidShape, TypeDoesNotMatch, LabelAlreadyInUse {
        return manipulator.slice(start, end);
    }
}