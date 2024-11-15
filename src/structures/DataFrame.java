package structures;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import exceptions.IndexOutOfBounds;
import exceptions.InvalidShape;
import exceptions.LabelAlreadyInUse;
import exceptions.LabelDoesNotMatch;
import exceptions.LabelNotFound;
import exceptions.TypeDoesNotMatch;
import interfaces.CopyableStructure;
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
public class DataFrame implements Visualizer<DataFrame>, CopyableStructure<DataFrame> {
    private List<Column<?>> columns;
    private List<Row> rows;
    private final DataManipulator manipulator;
    private final DataExporter exporter;

    /**
     * Crea un DataFrame vacío.
     */
    public DataFrame() {
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();
        this.exporter = new DataExporter(this);
        this.manipulator = new DataManipulator(this);
    }

    /**
     * Crea un DataFrame con las columnas y encabezados proporcionados.
     *
     * @param rows    lista de filas que representan los datos iniciales.
     * @param headers lista de encabezados de columna.
     * @throws InvalidShape     si las dimensiones de las filas no coinciden con la
     * @throws TypeDoesNotMatch si el tipo de datos en una celda no coincide con el
     *                          tipo esperado.
     * @throws IndexOutOfBounds si los índices exceden el rango permitido.
     */
    public DataFrame(List<?> rows, List<?> headers) throws InvalidShape, TypeDoesNotMatch, IndexOutOfBounds {
        this();
        initializeDataFrame(rows, headers);
    }

    /**
     * Crea un DataFrame con las filas proporcionadas.
     *
     * @param rows lista de filas que representan los datos iniciales.
     * @throws IndexOutOfBounds si los índices exceden el rango permitido.
     * @throws InvalidShape     si las dimensiones de las filas no coinciden con la
     * @throws TypeDoesNotMatch si el tipo de datos en una celda no coincide con el
     *                          tipo esperado.
     */
    public DataFrame(List<?> rows) throws IndexOutOfBounds, InvalidShape, TypeDoesNotMatch {
        this();
        List<String> headers = generateHeaders(rows);
        initializeDataFrame(rows, headers);
    }

    /**
     * Inicializa un DataFrame con las filas y encabezados proporcionados.
     *
     * @param rows    lista de filas que representan los datos iniciales.
     * @param headers lista de encabezados de columna.
     * @throws InvalidShape     si las dimensiones de las filas no coinciden con la
     * @throws TypeDoesNotMatch si el tipo de datos en una celda no coincide con el
     * @throws IndexOutOfBounds
     */
    private void initializeDataFrame(List<?> rows, List<?> headers)
            throws InvalidShape, TypeDoesNotMatch, IndexOutOfBounds {
        if (rows.isEmpty()) {
            for (int i = 0; i < headers.size(); i++) {
                columns.add(new Column<>(headers.get(i)));
            }
            populateRowsWithCells(columns);
            return;
        }

        validateRowShapes(rows, headers.size());
        initializeColumns(rows, headers);
        populateRowsWithCells(columns);
    }

    /**
     * Valida que todas las filas tengan la misma forma que los encabezados.
     *
     * @param rows         lista de filas.
     * @param expectedSize tamaño esperado de las filas.
     * @throws InvalidShape si la forma de las filas no coincide con el tamaño
     */
    private void validateRowShapes(List<?> rows, int expectedSize) throws InvalidShape {
        for (Object row : rows) {
            int size = (row instanceof Row) ? ((Row) row).size() : ((List<?>) row).size();
            if (size != expectedSize)
                throw new InvalidShape();
        }
    }

    /**
     * Inicializa las columnas del DataFrame con los datos proporcionados.
     *
     * @param rows    lista de filas que representan los datos.
     * @param headers lista de encabezados de columna.
     * @throws TypeDoesNotMatch si los tipos de datos en las celdas no coinciden.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void initializeColumns(List<?> rows, List<?> headers) throws TypeDoesNotMatch {
        for (int i = 0; i < headers.size(); i++) {
            Column<?> column = new Column<>(headers.get(i).toString());
            for (Object row : rows) {
                Object cellValue = (row instanceof Row)
                        ? ((Row) row).getCell(i).getValue()
                        : ((List<?>) row).get(i);
                column.addCell(new Cell(cellValue));
            }
            columns.add(column);
        }
    }

    /**
     * Rellena las filas con celdas basadas en las columnas proporcionadas.
     *
     * @param columns lista de columnas.
     * @throws IndexOutOfBounds si los índices exceden el rango permitido.
     */
    private void populateRowsWithCells(List<Column<?>> columns) throws IndexOutOfBounds {
        for (int i = 0; i < countRows(); i++) {
            List<Cell<?>> cells = new ArrayList<>();
            for (Column<?> column : columns) {
                cells.add(column.getCell(i));
            }
            rows.add(new Row(i, cells));
        }
    }

    /**
     * Genera encabezados de columna predeterminados "Column i" * n.
     *
     * @param rows lista de filas.
     * @return lista de encabezados de columna.
     */
    private List<String> generateHeaders(List<?> rows) {
        int headerCount = (rows.get(0) instanceof Row)
                ? ((Row) rows.get(0)).size()
                : ((List<?>) rows.get(0)).size();

        List<String> headers = new ArrayList<>();
        for (int i = 0; i < headerCount; i++) {
            headers.add("Column " + i);
        }
        return headers;
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
        if (columns.isEmpty()) {
            for (int i = 0; i < row.size(); i++) {
                columns.add(new Column<>("Column " + i));
            }
        }

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
     * Obtiene una fila del DataFrame por su etiqueta.
     *
     * @param label etiqueta de la fila.
     * @return la fila con la etiqueta especificada.
     * @throws LabelNotFound si la etiqueta no se encuentra.
     */
    public Row getRow(Object label) throws LabelNotFound {
        return rows.stream()
                .filter(row -> row.getLabel().equals(label))
                .findFirst()
                .orElseThrow(LabelNotFound::new);
    }

    /**
     * Obtiene una celda del DataFrame por su índice de fila y columna.
     *
     * @param rowIndex    índice de la fila.
     * @param columnIndex índice de la columna.
     * @return la celda en la fila y columna especificadas.
     * @throws IndexOutOfBounds si los índices están fuera de los límites.
     * @throws LabelNotFound
     */
    public Cell<?> getCell(int rowIndex, int columnIndex) throws IndexOutOfBounds {
        return columns.get(columnIndex).getCell(rowIndex);
    }

    /**
     * Obtiene una celda del DataFrame por su etiqueta de fila y columna.
     *
     * @param rowLabel    etiqueta de la fila.
     * @param columnLabel etiqueta de la columna.
     * @return la celda en la fila y columna especificadas.
     * @throws IndexOutOfBounds si los índices están fuera de los límites.
     * @throws LabelNotFound
     */
    public Cell<?> getCell(Object rowLabel, Object columnLabel) throws IndexOutOfBounds, LabelNotFound {
        int columnIndex = columns.indexOf(this.getColumn(columnLabel));
        int rowIndex = rows.indexOf(this.getRow(rowLabel));

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
    @Override
    public DataFrame copy() throws InvalidShape, TypeDoesNotMatch, LabelAlreadyInUse, IndexOutOfBounds {
        List<Row> rows = new ArrayList<>();
        for (Row row : this.rows) {
            rows.add(row.copy());
        }
        return new DataFrame(rows, this.getColumnLabels());
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

    /**
     * Muestra el DataFrame en la consola. Si el DataFrame tiene más de 10 filas o
     * más de 6 columnas, se mostrarán solo las primeras y últimas 5 filas y 3
     * columnas en cada extremo.
     * 
     * @throws IndexOutOfBounds  si hay índices fuera del rango permitido.
     * 
     * @throws InvalidShape      si las dimensiones del DataFrame no son válidas.
     * 
     * @throws TypeDoesNotMatch  si los tipos de datos no coinciden.
     * 
     * @throws LabelAlreadyInUse si la etiqueta de la fila ya está en uso.
     * @throws LabelNotFound
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void show() throws IndexOutOfBounds, InvalidShape, TypeDoesNotMatch, LabelAlreadyInUse, LabelNotFound {
        if (this.countRows() <= 10 && this.countColumns() <= 6) {
            System.out.println(this);
        } else if (this.countColumns() > 6) {
            ArrayList<Object> headers = new ArrayList<>();

            for (int i = 0; i < 3; i++) {
                headers.add(this.getColumnLabels().get(i));
            }

            headers.add(" ... ");

            for (int i = this.getColumns().size() - 3; i < this.getColumns().size(); i++) {
                headers.add(this.getColumnLabels().get(i));
            }

            DataFrame df = new DataFrame(Arrays.asList(), headers);

            for (int i = 0; i < this.countRows(); i++) {
                List<Cell<?>> cells = new ArrayList<>();
                for (int j = 0; j < 3; j++) {
                    cells.add(this.getCell(i, j));
                }
                cells.add(new Cell(" ... "));
                for (int j = this.countColumns() - 3; j < this.countColumns(); j++) {
                    cells.add(this.getCell(i, j));
                }
                df.insertRow(cells);
            }

            System.out.println(df.head(5));
        } else if (this.countRows() > 10) {
            System.out.println(this.head(5));
        }
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
        List<Row> newRows = new ArrayList<>();
        for (int i = start; i < end; i++) {
            newRows.add(rows.get(i));
        }
        return new DataFrame(newRows, getColumnLabels());
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
     * @throws LabelAlreadyInUse
     * @throws InvalidShape
     * @throws LabelNotFound     si la etiqueta de la columna no se encuentra.
     * @throws TypeDoesNotMatch  si los tipos de datos no coinciden.
     * @throws IndexOutOfBounds  si hay índices fuera del rango permitido.
     */
    public DataFrame fillna(Object label, Object value)
            throws InvalidShape, TypeDoesNotMatch, LabelAlreadyInUse, IndexOutOfBounds, LabelNotFound {
        return manipulator.fillna(label, value);
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

    /**
     * Concatena dos DataFrames.
     * 
     * @param other DataFrame a concatenar.
     * @return un nuevo DataFrame con las filas concatenadas.
     * @throws InvalidShape      si las dimensiones de los DataFrames no coinciden.
     * @throws TypeDoesNotMatch  si los tipos de datos no coinciden.
     * @throws IndexOutOfBounds  si hay índices fuera del rango permitido.
     * @throws LabelDoesNotMatch
     */
    public DataFrame concat(DataFrame other)
            throws InvalidShape, TypeDoesNotMatch, IndexOutOfBounds, LabelDoesNotMatch {
        return manipulator.concat(other);
    }
}
