package tests;

import structures.Cell;

public class TestCellCompare {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // Test con Integer (Comparable)
        Cell<Integer> cell1 = new Cell<>(10);
        Cell<Integer> cell2 = new Cell<>(20);
        assert cell1.compareTo(cell2) < 0;

        // Test con String (Comparable)
        Cell<String> cell3 = new Cell<>("A");
        Cell<String> cell4 = new Cell<>("B");
        assert cell3.compareTo(cell4) < 0;

        // Test con Double (Comparable)
        Cell<Double> cell5 = new Cell<>(3.14);
        Cell<Double> cell6 = new Cell<>(2.718);
        assert cell5.compareTo(cell6) > 0;

        // Test con Cell vacío (Empty)
        Cell<Integer> cell7 = new Cell<>(null);
        Cell<Integer> cell8 = new Cell<>(20);
        assert cell7.compareTo(cell8) < 0;

        // Test con dos Cells vacíos
        Cell<Integer> cell9 = new Cell<>(null);
        Cell<Integer> cell10 = new Cell<>(null);
        assert cell9.compareTo(cell10) == 0;

        // Test con objetos no comparables (comentado si no se utiliza la restricción
        // Comparable)
        Cell<Object> cell11 = new Cell<>(new Object());
        Cell<Object> cell12 = new Cell<>(new Object());

        try {
            cell11.compareTo(cell12);
        } catch (Exception e) {
            assert UnsupportedOperationException.class.isInstance(e);
        }
    }
}
