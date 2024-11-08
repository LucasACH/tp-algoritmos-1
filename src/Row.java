import exceptions.IndexOutOfBounds;
import java.util.ArrayList;
import java.util.List;

class Row {
    private Object label;
    private List<Cell<?>> cells;

    public Row(Object label, List<Cell<?>> cells) {
        this.label = label;
        this.cells = cells;
    }

    public Object getLabel() {
        return label;
    }

    public List<Cell<?>> getCells() {
        return cells;
    }

    public Cell<?> getCell(int index) {
        return cells.get(index);
    }

    public void setCell(int index, Cell<?> cell) {
        cells.set(index, cell);
    }

    public void setLabel(Object label) {
        this.label = label;
    }

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

    public Column<Object> getColumn(int columnIndex) throws IndexOutOfBounds {
    if (columnIndex < 0 || columnIndex >= cells.size()) {
        throw new IndexOutOfBoundsException("El índice de columna está fuera de los límites.");
    }

    List<Cell<Object>> columnCells = new ArrayList<>();
    Cell<?> cell = cells.get(columnIndex);
    
    columnCells.add((Cell<Object>) cell); // Cast a Cell<Object> para agregar a columnCells

    return new Column<>(label, columnCells);
}


}
