import java.util.ArrayList;
import java.util.List;

public class Column<T> {
    private List<Cell<T>> cells;
    private String label;
    private int id;

    public Column(List<Cell<T>> cells, String label) {
        this.cells = cells;
        this.label = label;
        this.id = -1;
    }

    public Column(List<Cell<T>> cells, int id) {
        this.cells = cells;
        this.id = id;
        this.label = null;
    }

    public Column(String label) {
        this.cells = new ArrayList<>();
        this.label = label;
    }

    public Column(int id) {
        this.cells = new ArrayList<>();
        this.id = id;
    }
    public boolean areCellsOfSameType() {

        // TODO: Lanzar excepción si los tipos de datos de las celdas son distintos.

        if (cells.isEmpty()) {
            return true; // No hay elementos, no se puede verificar.
        }

        Class<?> firstCellType = cells.get(0).getValue().getClass(); // Obtener el tipo del primer elemento.

        for (Cell<?> cell : cells) {
            if (!cell.getValue().getClass().equals(firstCellType)) {
                return false; // Si encontramos un tipo diferente, devolvemos false.
            }
        }
        return true; // Todos los elementos son del mismo tipo.
    }

    public Cell<?> getCell(int index) {
        if (index < 0 || index >= cells.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return cells.get(index);
    }

    // @SuppressWarnings("unchecked")
    public void setCell(int index, T value) {
        // Check if the index is within bounds
        if (index < 0 || index >= cells.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
    
        // Get the cell at the specified index
        Cell<T> cell = (Cell<T>) cells.get(index);
    
        // Check if the value type matches the cell type
        if (!cell.getValue().getClass().isInstance(value)) {
            throw new IllegalArgumentException("Value type does not match cell type");
        }
    
        // Cast the cell to the appropriate type and set the value
        cell.setValue(value);
    }

    public String getLabel() {
        return label;
    }
    public int getId() {
        return id;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    public List<Cell<T>> getCells() {
        return cells;
    }

    public void addCell(Cell<T> cell) {
        cells.add(cell);
    }

    public Column<T> copy() {
        Column<T> copy = new Column<T>(cells, label);
        return copy;
    }
}
