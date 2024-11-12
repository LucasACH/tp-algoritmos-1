package structures;

import java.util.List;

import interfaces.CopyableStructure;

/**
 * La clase Row representa una fila que contiene una lista de celdas (Cell) y
 * una etiqueta.
 */
public class Row implements CopyableStructure<Row> {
    private Object label;
    private List<Cell<?>> cells;

    /**
     * Constructor que inicializa la fila con una etiqueta y una lista de celdas.
     *
     * @param label Etiqueta de la fila.
     * @param cells Lista de celdas que forman la fila.
     */
    public Row(Object label, List<Cell<?>> cells) {
        this.label = label;
        this.cells = cells;
    }

    /**
     * Obtiene la etiqueta de la fila.
     *
     * @return La etiqueta de la fila.
     */
    public Object getLabel() {
        return label;
    }

    /**
     * Establece una nueva etiqueta para la fila.
     *
     * @param label Nueva etiqueta para la fila.
     */
    public void setLabel(Object label) {
        this.label = label;
    }

    /**
     * Obtiene la lista de celdas de la fila.
     *
     * @return La lista de celdas.
     */
    public List<Cell<?>> getCells() {
        return cells;
    }

    /**
     * Obtiene la celda en el índice especificado.
     *
     * @param index Índice de la celda.
     * @return La celda en el índice especificado.
     */
    public Cell<?> getCell(int index) {
        return cells.get(index);
    }

    /**
     * Establece una celda en el índice especificado.
     *
     * @param index Índice de la celda a establecer.
     * @param cell  Nueva celda para la posición especificada.
     */
    public void setCell(int index, Cell<?> cell) {
        cells.set(index, cell);
    }

    /**
     * Obtiene el tamaño de la fila (cantidad de celdas).
     *
     * @return El número de celdas en la fila.
     */
    public int size() {
        return cells.size();
    }

    /**
     * Crea una copia de la fila actual.
     *
     * @return Una copia de la fila.
     */
    @Override
    public Row copy() {
        return new Row(label, cells);
    }

    /**
     * Devuelve una representación en cadena de la fila, incluyendo la etiqueta y
     * los valores de las celdas.
     *
     * @return Representación en cadena de la fila.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(label);
        for (Cell<?> cell : cells) {
            sb.append(", ").append(cell.getValue());
        }
        sb.append("]");
        return sb.toString();
    }
}