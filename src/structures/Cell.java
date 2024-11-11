package structures;

import exceptions.TypeDoesNotMatch;
import interfaces.CopyableStructure;

/**
 * La clase Cell representa una celda genérica que puede almacenar un valor de
 * cualquier tipo.
 * Implementa la interfaz Comparable para permitir la comparación entre celdas.
 * Además, implementa la interfaz CopyableStructure para permitir la creación de
 * copias de celdas.
 *
 * @param <T> Tipo de dato que almacena la celda.
 */
public class Cell<T> implements Comparable<Cell<T>>, CopyableStructure<Cell<T>> {
    private T value;

    /**
     * Constructor que inicializa la celda con un valor específico.
     *
     * @param value el valor inicial de la celda.
     */
    public Cell(T value) {
        this.value = value;
    }

    /**
     * Verifica si la celda está vacía.
     * Una celda se considera vacía si contiene un valor nulo o una cadena vacía.
     *
     * @return true si la celda está vacía, false en caso contrario.
     */
    public boolean isEmpty() {
        if (value instanceof String) {
            return value.equals("");
        }
        return value == null;
    }

    /**
     * Obtiene el valor almacenado en la celda.
     *
     * @return el valor de la celda.
     */
    public T getValue() {
        return value;
    }

    /**
     * Obtiene el tipo de dato de la celda.
     *
     * @return la clase del valor almacenado en la celda.
     */
    public Class<?> getType() {
        return value.getClass();
    }

    /**
     * Establece un nuevo valor en la celda. Lanza una excepción si el tipo
     * del nuevo valor no coincide con el tipo del valor actual.
     *
     * @param value el nuevo valor a almacenar.
     * @throws TypeDoesNotMatch si el tipo del nuevo valor no coincide con el tipo
     *                          actual.
     */
    public void setValue(T value) throws TypeDoesNotMatch {
        if (value.getClass() != this.value.getClass()) {
            throw new TypeDoesNotMatch();
        }
        this.value = value;
    }

    /**
     * Crea y devuelve una copia de la celda actual.
     *
     * @return una nueva instancia de Cell con el mismo valor.
     */
    @Override
    public Cell<T> copy() {
        return new Cell<>(value);
    }

    /**
     * Devuelve una representación en cadena de la celda.
     * Si la celda está vacía, devuelve una cadena vacía.
     *
     * @return una cadena que representa el valor de la celda o una cadena vacía si
     *         está vacía.
     */
    @Override
    public String toString() {
        return isEmpty() ? "" : value.toString();
    }

    /**
     * Compara la celda actual con otra celda.
     * Las celdas vacías se consideran menores que las celdas con valores.
     *
     * @param o la celda con la que se desea comparar.
     * @return 0 si ambas celdas están vacías o contienen valores equivalentes, -1
     *         si la celda
     *         actual es menor, o 1 si es mayor.
     * @throws UnsupportedOperationException si el valor no implementa la interfaz
     *                                       Comparable.
     */
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
            throw new UnsupportedOperationException("Imposible to compare values of this type");
        }
    }
}