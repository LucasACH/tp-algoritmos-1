package mogul;

class Cell<T> {
    private T value; // Cambié `public` a `private` para encapsulación

    public Cell(T value) {
        this.value = value;
    }

    public boolean isEmpty() {
        return value == null;
    }
    
    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : "null";
    }
}
