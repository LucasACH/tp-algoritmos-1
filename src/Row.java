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

}
