package structures;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import exceptions.IndexOutOfBounds;
import exceptions.InvalidShape;
import exceptions.LabelAlreadyInUse;
import exceptions.LabelNotFound;
import exceptions.TypeDoesNotMatch;
import interfaces.Visualizer;
import libraries.DataExporter;
import libraries.DataManipulator;

/**
 * Representa un marco de datos (DataFrame) que organiza datos en filas y
 * columnas.
 * Permite manipulación y exportación de datos, así como visualización en
 * formato tabular.
 * Este marco de datos utiliza filas y columnas dinámicas para almacenar
 * diferentes tipos de datos.
 */
public class DataFrame implements Visualizer<DataFrame> {
    private List<Column<?>> columns;
    private List<Row> rows;
    private final DataManipulator manipulator;
    private final DataExporter exporter;

    /**
     * Crea una instancia vacía de DataFrame.
     */
    public DataFrame() {
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();
        this.exporter = new DataExporter(this);
        this.manipulator = new DataManipulator(this);
    }

    /**
     * Crea una instancia de DataFrame con las filas y encabezados proporcionados.
     *
     * @param rows    lista de filas que representan los datos iniciales.
     * @param headers lista de encabezados de columna.
     * @throws InvalidShape     si las dimensiones de filas y encabezados no
     *                          coinciden.
     * @throws TypeDoesNotMatch si el tipo de datos en una celda no coincide con el
     *                          esperado.
     * @throws IndexOutOfBounds si hay índices fuera del rango permitido.
     */
    public DataFrame(List<?> rows, List<?> headers) throws InvalidShape, TypeDoesNotMatch, IndexOutOfBounds {
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();
        this.exporter = new DataExporter(this);
        this.manipulator = new DataManipulator(this);

        if (!rows.isEmpty() && rows.get(0) instanceof Row || rows.get(0) instanceof List<?>) {
            initializeColumnsFromData(rows, headers);
        }

        initializeRowsWithCells(columns);
    }

    /**
     * @param rows
     * @param headers
     * @throws InvalidShape
     * @throws TypeDoesNotMatch
     */
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

    /**
     * Inserta una nueva fila en el DataFrame.
     *
     * @param row datos de la fila a insertar.
     * @return la instancia del DataFrame.
     * @throws InvalidShape      si la forma de la fila no coincide con el marco de
     *                           datos.
     * @throws TypeDoesNotMatch  si los tipos de las celdas no coinciden.
     * @throws LabelAlreadyInUse si la etiqueta de la fila ya está en uso.
     */
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

    /**
     * Inserta una nueva columna en el DataFrame.
     *
     * @param label etiqueta de la columna.
     * @return la instancia del DataFrame.
     * @throws InvalidShape     si la forma de la columna no es válida.
     * @throws TypeDoesNotMatch si el tipo de datos de la columna no coincide.
     */
    public DataFrame insertColumn(Object label) throws InvalidShape, TypeDoesNotMatch {
        Column<?> column = new Column<>(label);
        columns.add(column);
        return this;
    }

    /**
     * Inserta una nueva columna en el DataFrame con los datos proporcionados.
     *
     * @param label etiqueta de la columna.
     * @param data  datos de la columna.
     * @return la instancia del DataFrame.
     * @throws InvalidShape     si la forma de la columna no es válida.
     * @throws TypeDoesNotMatch si el tipo de datos de la columna no coincide.
     */
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

    /**
     * Inserta una nueva columna en el DataFrame.
     *
     * @param data datos de la columna.
     * @return la instancia del DataFrame.
     * @throws InvalidShape     si la forma de la columna no es válida.
     * @throws TypeDoesNotMatch si el tipo de datos de la columna no coincide.
     */
    public DataFrame insertColumn(List<?> data) throws InvalidShape, TypeDoesNotMatch {
        validateColumnShape(data);
        Column<?> column = createColumnWithData("Column " + countColumns(), data);
        columns.add(column);
        return this;
    }

    /**
     * Obtiene el número de filas en el DataFrame.
     *
     * @return el número de filas.
     */
    public DataFrame insertColumn(Column<?> column) throws InvalidShape {
        columns.add(column);
        return this;
    }

    /**
     * Obtiene el número de filas en el DataFrame.
     *
     * @return el número de filas.
     */
    public int countRows() {
        return columns.isEmpty() ? 0 : columns.get(0).getCells().size();
    }

    /**
     * Obtiene el número de columnas en el DataFrame.
     *
     * @return el número de columnas.
     */
    public int countColumns() {
        return columns.size();
    }

    /**
     * Obtiene una columna del DataFrame por su índice.
     *
     * @param index índice de la columna.
     * @return la columna en el índice especificado.
     * @throws IndexOutOfBounds si el índice está fuera de los límites.
     */
    public Column<?> getColumn(Object label) throws LabelNotFound {
        return columns.stream()
                .filter(column -> column.getLabel().equals(label))
                .findFirst()
                .orElseThrow(LabelNotFound::new);
    }

    /**
     * Obtiene una fila del DataFrame por su índice.
     *
     * @param index índice de la fila.
     * @return la fila en el índice especificado.
     * @throws IndexOutOfBounds si el índice está fuera de los límites.
     */
    public Row getRow(int index) throws IndexOutOfBounds {
        if (!isValidRowIndex(index)) {
            throw new IndexOutOfBounds();
        }
        return rows.get(index);
    }

    /**
     * Obtiene una celda del DataFrame por su índice de fila y columna.
     *
     * @param rowIndex    índice de la fila.
     * @param columnIndex índice de la columna.
     * @return la celda en la fila y columna especificadas.
     * @throws IndexOutOfBounds si los índices están fuera de los límites.
     */
    public Cell<?> getCell(int rowIndex, int columnIndex) throws IndexOutOfBounds {
        validateIndices(rowIndex, columnIndex);
        return columns.get(columnIndex).getCell(rowIndex);
    }

    /**
     * Establece un nuevo valor en la celda especificada.
     *
     * @param rowIndex    índice de la fila.
     * @param columnIndex índice de la columna.
     * @param value       nuevo valor a establecer.
     * @throws IndexOutOfBounds si los índices están fuera de los límites.
     * @throws TypeDoesNotMatch si el tipo del nuevo valor no coincide con el tipo
     *                          de
     *                          la celda.
     */
    @SuppressWarnings("unchecked")
    public <T> void setCell(int rowIndex, int columnIndex, T value) throws IndexOutOfBounds, TypeDoesNotMatch {
        validateIndices(rowIndex, columnIndex);
        ((Column<T>) columns.get(columnIndex)).setCell(rowIndex, value);
    }

    /**
     * Crea y devuelve una copia del DataFrame actual.
     * 
     * @return
     * @throws InvalidShape
     * @throws TypeDoesNotMatch
     * @throws LabelAlreadyInUse
     * @throws IndexOutOfBounds
     */
    public DataFrame copy() throws InvalidShape, TypeDoesNotMatch, LabelAlreadyInUse, IndexOutOfBounds {
        List<Column<?>> copiedColumns = new ArrayList<>();
        for (int i = 0; i < countColumns(); i++) {
            copiedColumns.add(columns.get(i).copy());
        }
        return new DataFrame(copiedColumns);
    }

    /**
     * Obtiene las primeras n filas del DataFrame.
     *
     * @param n número de filas a obtener.
     * @return un nuevo DataFrame con las primeras n filas.
     * @throws IndexOutOfBounds si el número de filas a obtener es mayor que el
     *                          número de filas en el DataFrame.
     * @throws InvalidShape     si las dimensiones del nuevo DataFrame no son
     *                          válidas.
     * @throws TypeDoesNotMatch si los tipos de datos no coinciden.
     */
    public DataFrame head(int n) throws IndexOutOfBounds, InvalidShape, TypeDoesNotMatch {
        return createSubDataFrame(0, n);
    }

    /**
     * Obtiene las últimas n filas del DataFrame.
     *
     * @param n número de filas a obtener.
     * @return un nuevo DataFrame con las últimas n filas.
     * @throws IndexOutOfBounds si el número de filas a obtener es mayor que el
     *                          número de filas en el DataFrame.
     * @throws InvalidShape     si las dimensiones del nuevo DataFrame no son
     *                          válidas.
     * @throws TypeDoesNotMatch si los tipos de datos no coinciden.
     */
    public DataFrame tail(int n) throws IndexOutOfBounds, InvalidShape, TypeDoesNotMatch {
        return createSubDataFrame(countRows() - n, countRows());
    }

    /**
     * Exporta los datos del DataFrame a un archivo CSV.
     *
     * @param path ruta del archivo CSV.
     * @throws IndexOutOfBounds si hay índices fuera del rango permitido.
     */
    public void exportToCSV(String path) throws IndexOutOfBounds {
        exportData(path, ExportFormat.CSV);
    }

    /**
     * Exporta los datos del DataFrame a un archivo JSON.
     *
     * @param path ruta del archivo JSON.
     * @throws IndexOutOfBounds si hay índices fuera del rango permitido.
     */
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

    /**
     * Obtiene las columnas del DataFrame.
     *
     * @return lista de columnas.
     */
    public List<Column<?>> getColumns() {
        return columns;
    }

    /**
     * Obtiene las filas del DataFrame.
     *
     * @return lista de filas.
     */
    public List<Row> getRows() {
        return rows;
    }

    /**
     * Obtiene las etiquetas de las columnas del DataFrame.
     *
     * @return lista de etiquetas de columnas.
     */
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

    /**
     * Calcula el ancho de las columnas basado en las etiquetas de las columnas y
     * los valores de las celdas.
     * 
     * @param columnLabels lista de etiquetas de las columnas.
     * @return lista de anchos de las columnas.
     */
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

    /**
     * Añade los datos de las filas al StringBuilder.
     * 
     * @param sb StringBuilder para añadir los datos de las filas.
     */
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

    /**
     * Filtra las filas del DataFrame basado en una o más condiciones.
     * 
     * @param conditions
     * @return
     * @throws LabelNotFound
     * @throws InvalidShape
     * @throws TypeDoesNotMatch
     * @throws IndexOutOfBounds
     */
    public DataFrame filter(Map<Object, Predicate<Object>> conditions)
            throws LabelNotFound, InvalidShape, TypeDoesNotMatch, IndexOutOfBounds {
        return manipulator.filter(conditions);
    }

    /**
     * Llena los valores nulos en una columna con un valor específico.
     * 
     * @param label etiqueta de la columna.
     * @param value valor para reemplazar los valores nulos.
     * @throws LabelNotFound    si la etiqueta de la columna no se encuentra.
     * @throws TypeDoesNotMatch si los tipos de datos no coinciden.
     * @throws IndexOutOfBounds si hay índices fuera del rango permitido.
     */
    public void fillna(Object label, Object value) throws LabelNotFound, TypeDoesNotMatch, IndexOutOfBounds {
        manipulator.fillna(label, value);
    }

    /**
     * Devuelve una muestra aleatoria de filas del DataFrame.
     * 
     * @param frac fracción de filas a devolver.
     * @return un nuevo DataFrame con una muestra aleatoria de filas.
     * @throws InvalidShape      si las dimensiones del nuevo DataFrame no son
     *                           válidas.
     * @throws TypeDoesNotMatch  si los tipos de datos no coinciden.
     * @throws LabelAlreadyInUse si la etiqueta de la fila ya está en uso.
     * @throws IndexOutOfBounds  si hay índices fuera del rango permitido.
     */
    public DataFrame sample(double frac) throws InvalidShape, TypeDoesNotMatch, LabelAlreadyInUse, IndexOutOfBounds {
        return manipulator.sample(frac);
    }

    /**
     * Ordena las filas del DataFrame basado en una o más columnas.
     * 
     * @param labels     lista de etiquetas de las columnas.
     * @param descending true si se ordena de forma descendente, de lo contrario
     *                   false.
     * @return un nuevo DataFrame con las filas ordenadas.
     * @throws LabelNotFound    si la etiqueta de la columna no se encuentra.
     * @throws InvalidShape     si las dimensiones del nuevo DataFrame no son
     *                          válidas.
     * @throws TypeDoesNotMatch si los tipos de datos no coinciden.
     * @throws IndexOutOfBounds si hay índices fuera del rango permitido.
     */
    public DataFrame sortBy(List<Object> labels, boolean descending)
            throws LabelNotFound, InvalidShape, TypeDoesNotMatch, IndexOutOfBounds {
        return manipulator.sortBy(labels, descending);
    }

    /**
     * Devuelve un subconjunto de columnas del DataFrame.
     * 
     * @param labels lista de etiquetas de las columnas.
     * @return un nuevo DataFrame con las columnas seleccionadas.
     * @throws LabelNotFound    si la etiqueta de la columna no se encuentra.
     * @throws InvalidShape     si las dimensiones del nuevo DataFrame no son
     *                          válidas.
     * @throws TypeDoesNotMatch si los tipos de datos no coinciden.
     * @throws IndexOutOfBounds si hay índices fuera del rango permitido.
     */
    public DataFrame slice(int start, int end)
            throws IndexOutOfBounds, InvalidShape, TypeDoesNotMatch, LabelAlreadyInUse {
        return manipulator.slice(start, end);
    }

    /**
     * Agrupa las filas del DataFrame basado en una o más columnas.
     * 
     * @param label lista de etiquetas de las columnas.
     * @return un nuevo DataFrame agrupado.
     * @throws LabelNotFound    si la etiqueta de la columna no se encuentra.
     * @throws IndexOutOfBounds si hay índices fuera del rango permitido.
     */
    public GroupedDataFrame groupBy(List<Object> label) throws LabelNotFound, IndexOutOfBounds {
        return manipulator.groupBy(label);
    }
}