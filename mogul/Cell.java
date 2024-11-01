class Cell<T> {
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
        return isEmpty() ? "null" : value.toString();
    }
}
