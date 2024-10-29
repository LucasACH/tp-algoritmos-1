package mogul;

import java.util.ArrayList;
import java.util.List;

public class Column<T> {
    private List<Cell<T>> cells;
    private T label;
    // private T type;

    public Column(List<Cell<T>> cells, T label) {
        this.cells = cells;
        this.label = label;
    }

    public Column(T label) {
        this.cells = new ArrayList<>();
        this.label = label;
    }

    public boolean areCellsOfSameType() {

        // TODO: Lanzar excepción si los tipos de datos de las celdas son distintos.

        if (cells.isEmpty()) {
            return true; // No hay elementos, no se puede verificar.
        }

        Class<?> firstCellType = cells.get(0).getValue().getClass(); // Obtener el tipo del primer elemento.

        for (Cell<T> cell : cells) {
            if (!cell.getValue().getClass().equals(firstCellType)) {
                return false; // Si encontramos un tipo diferente, devolvemos false.
            }
        }
        return true; // Todos los elementos son del mismo tipo.
    }

    public Cell<T> getCell(int index) {
        if (index < 0 || index >= cells.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return cells.get(index);
    }

    public void setCell(int index, T value) {
        // Cambiar el valor de la celda a value:

        if (index < 0 || index >= cells.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        // TODO: Verificar que el tipo de value sea el mismo que el tipo de la celda
        // if (typeof(value) != typeof(cells.get(index).value)){
        // throw new IllegalArgumentException("Value type does not match cell type");
        // }

        cells.get(index).value = value;
    }

    public T getLabel() {
        return label;
    }

    public void setLabel(T label) {
        this.label = label;
    }

    public List<Cell<T>> getCells() {
        return cells;
    }

    public void addCell(Cell<T> cell) {
        // TODO: Verificar que el tipo de la celda sea el mismo que el tipo de la
        // columna.
        cells.add(cell);
    }

    public Column<T> copy() {
        Column<T> copy = new Column<>(cells, label);
        return copy;
    }
}
