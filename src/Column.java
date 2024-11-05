import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import exceptions.IndexOutOfBounds;
import exceptions.TypeDoesNotMatch;

public class Column<T> {
    private List<Cell<T>> cells;
    private Object label;

    public Column(Object label, List<Cell<T>> cells) {
        this.cells = cells;
        this.label = label;
    }

    public Column(Object label) {
        this.cells = new ArrayList<>();
        this.label = label;
    }

    public Cell<?> getCell(int index) throws IndexOutOfBounds {
        checkIndexBounds(index);
        return cells.get(index);
    }

    public void setCell(int index, T value) throws IndexOutOfBounds, TypeDoesNotMatch {
        checkIndexBounds(index);
        Cell<T> cell = (Cell<T>) cells.get(index);
        typeMatchCheck(value, cell);
        cell.setValue(value);
    }

    private void typeMatchCheck(T value, Cell<T> cell) throws TypeDoesNotMatch {
        if (!cell.getValue().getClass().isInstance(value)) {
            throw new TypeDoesNotMatch();
        }
    }

    private void checkIndexBounds(int index) throws IndexOutOfBounds {
        if (index < 0 || index >= cells.size()) {
            throw new IndexOutOfBounds();
        }
    }

    public void deleteCell(int index) throws IndexOutOfBounds {
        checkIndexBounds(index);
        cells.remove(index);
    }

    public Object getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Cell<T>> getCells() {
        return cells;
    }

    public void addCell(Cell<T> cell) throws TypeDoesNotMatch {
        if (!isSameType(cell)) {
            throw new TypeDoesNotMatch();
        }
        cells.add(cell);
    }

    private boolean isSameType(Cell<T> cell) {
        return cells.isEmpty() || cell.getValue().getClass().equals(cells.get(0).getValue().getClass());
    }

    public Column<T> copy() {
        List<Cell<T>> copiedCells = new ArrayList<>();
        for (Cell<T> cell : cells) {
            copiedCells.add(cell.copy());
        }
        return new Column<>(label, copiedCells);
    }

    public Class<?> getType() {
        return this.cells.get(0).getClass();
    }

    public int getSize() {
        return this.cells.size();
    }

    public void sort(boolean descending) {
        Comparator<Cell<T>> comparator = new Comparator<Cell<T>>() {
            @Override
            public int compare(Cell<T> cell1, Cell<T> cell2) {
                // Comparamos las celdas usando el compareTo de Cell
                int comparisonResult = cell1.compareTo(cell2);

                // Si descending es true, invertimos el resultado de la comparación
                return descending ? -comparisonResult : comparisonResult;
            }
        };

        // Ordenamos la lista de celdas usando el comparator
        Collections.sort(this.cells, comparator);
    }

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
}
