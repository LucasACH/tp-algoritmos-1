class Cell<T> implements Comparable<Cell<T>> {
    private T value;

    public Cell(T value) {
        this.value = value;
    }

    public boolean isEmpty() {
        return value == null;
    }

    public T getValue() {
        return value;
    }

    public Class<?> getType() {
        return value.getClass();
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Cell<T> copy() {
        return new Cell<>(value);
    }

    @Override
    public String toString() {
        return isEmpty() ? "" : value.toString();
    }

    @Override
    public int compareTo(Cell<T> o) {
        if (isEmpty() && o.isEmpty()) {
            return 0;
        } else if (isEmpty()) {
            return -1;
        } else if (o.isEmpty()) {
            return 1;
        } else if (value instanceof Comparable) {
            return ((Comparable) value).compareTo(o.value);
        } else{
            throw new UnsupportedOperationException("Unsupported operation");
        }
    }
}
