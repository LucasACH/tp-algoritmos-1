package structures;

import exceptions.TypeDoesNotMatch;

public class Cell<T> implements Comparable<Cell<T>> {
    private T value;

    public Cell(T value) {
        this.value = value;
    }

    public boolean isEmpty() {
        if (value instanceof String) {
            return value.equals("");
        }
        return value == null;
    }

    public T getValue() {
        return value;
    }

    public Class<?> getType() {
        return value.getClass();
    }

    public void setValue(T value) throws TypeDoesNotMatch {
        if (value.getClass() != this.value.getClass()) {
            throw new TypeDoesNotMatch();
        }
        this.value = value;
    }

    public Cell<T> copy() {
        return new Cell<>(value);
    }

    @Override
    public String toString() {
        return isEmpty() ? "" : value.toString();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
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
        } else {
            throw new UnsupportedOperationException("Unsupported operation");
        }
    }
}
