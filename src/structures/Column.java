package structures;

import java.util.ArrayList;
import java.util.List;

import exceptions.IndexOutOfBounds;
import exceptions.TypeDoesNotMatch;
import interfaces.CopyableStructure;

/**
 * La clase Column representa una columna que contiene una lista de celdas
 * (Cell) con un mismo tipo de datos.
 * Además, permite realizar operaciones como agregar, eliminar, y ordenar las
 * celdas.
 *
 * @param <T> Tipo de dato que almacenan las celdas de la columna.
 */
public class Column<T> implements CopyableStructure<Column<T>> {
    private List<Cell<T>> cells;
    private Object label;

    /**
     * Constructor que inicializa la columna con una etiqueta y una lista de celdas.
     *
     * @param label Etiqueta de la columna.
     * @param cells Lista de celdas que forman la columna.
     */
    public Column(Object label, List<Cell<T>> cells) {
        this.cells = cells;
        this.label = label;
    }

    /**
     * Constructor que inicializa la columna con una etiqueta sin celdas.
     *
     * @param label Etiqueta de la columna.
     */
    public Column(Object label) {
        this.cells = new ArrayList<>();
        this.label = label;
    }

    @SuppressWarnings("unchecked")
    public Column(int numberOfRows, Object label, String cellFiller){
        this.cells = new ArrayList<>();
        this.label = label;
        for (int i = 0; i < numberOfRows; i++) {
            this.cells.add((Cell<T>) new Cell<>(cellFiller));
        }
    }

    /**
     * Obtiene la celda en el índice especificado.
     *
     * @param index Índice de la celda.
     * @return La celda en el índice especificado.
     * @throws IndexOutOfBounds si el índice está fuera de los límites.
     */
    public Cell<?> getCell(int index) throws IndexOutOfBounds {
        checkIndexBounds(index);
        return cells.get(index);
    }

    /**
     * Establece un nuevo valor en la celda especificada.
     *
     * @param index Índice de la celda a actualizar.
     * @param value Nuevo valor para la celda.
     * @throws IndexOutOfBounds si el índice está fuera de los límites.
     * @throws TypeDoesNotMatch si el tipo del nuevo valor no coincide con el tipo
     *                          de la celda.
     */
    public void setCell(int index, T value) throws IndexOutOfBounds, TypeDoesNotMatch {
        checkIndexBounds(index);
        Cell<T> cell = (Cell<T>) cells.get(index);
        typeMatchCheck(value, cell);
        cell.setValue(value);
    }

    /**
     * Verifica si el tipo de valor proporcionado coincide con el de la celda.
     *
     * @param value Valor a verificar.
     * @param cell  Celda cuyo tipo se va a comparar.
     * @throws TypeDoesNotMatch si el tipo no coincide.
     */
    private void typeMatchCheck(T value, Cell<T> cell) throws TypeDoesNotMatch {
        if (!cell.getValue().getClass().isInstance(value)) {
            throw new TypeDoesNotMatch();
        }
    }

    /**
     * Verifica si el índice proporcionado está dentro de los límites de la columna.
     *
     * @param index Índice a verificar.
     * @throws IndexOutOfBounds si el índice está fuera de los límites.
     */
    private void checkIndexBounds(int index) throws IndexOutOfBounds {
        if (index < 0 || index >= cells.size()) {
            throw new IndexOutOfBounds();
        }
    }

    /**
     * Obtiene la etiqueta de la columna.
     *
     * @return La etiqueta de la columna.
     */
    public Object getLabel() {
        return label;
    }

    /**
     * Establece la etiqueta de la columna.
     *
     * @param label Nueva etiqueta para la columna.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Obtiene la lista de celdas de la columna.
     *
     * @return La lista de celdas.
     */
    public List<Cell<T>> getCells() {
        return cells;
    }

    /**
     * Agrega una nueva celda a la columna.
     *
     * @param value La celda a agregar.
     * @throws TypeDoesNotMatch si el tipo de la celda no coincide con el tipo de la
     *                          columna.
     */
    public void addCell(Cell<T> value) throws TypeDoesNotMatch {
        if (!isSameType(value)) {
            throw new TypeDoesNotMatch();
        }
        cells.add(value);
    }

    /**
     * Verifica si el tipo de la nueva celda coincide con el tipo de las celdas
     * existentes.
     *
     * @param cell Celda a verificar.
     * @return true si el tipo coincide, false en caso contrario.
     */
    private boolean isSameType(Cell<T> cell) {
        return cells.isEmpty() || cell.getValue().getClass().equals(cells.get(0).getValue().getClass());
    }

    /**
     * Crea y devuelve una copia de la columna actual.
     *
     * @return Una nueva instancia de Column con los mismos valores.
     */
    @Override
    public Column<T> copy() {
        List<Cell<T>> copiedCells = new ArrayList<>();
        for (Cell<T> cell : cells) {
            copiedCells.add(cell.copy());
        }
        return new Column<>(label, copiedCells);
    }

    /**
     * Obtiene el tipo de datos de las celdas en la columna.
     *
     * @return La clase del tipo de datos de las celdas.
     */
    public Class<?> getType() {
        return this.cells.get(0).getClass();
    }

    /**
     * Obtiene el número de celdas en la columna.
     *
     * @return Tamaño de la columna.
     */
    public int size() {
        return this.cells.size();
    }

    /**
     * Devuelve una representación en cadena de la columna, donde los valores de las
     * celdas están separados por comas.
     *
     * @return Representación en cadena de la columna.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cells.size(); i++) {
            sb.append(cells.get(i).getValue());
            if (i < cells.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    /**
     * Obtiene una lista de los valores almacenados en las celdas de la columna.
     *
     * @return Lista de valores en la columna.
     */
    public List<T> getValue() {
        List<T> values = new ArrayList<>();
        for (Cell<T> cell : cells) {
            values.add(cell.getValue());
        }
        return values;
    }
}