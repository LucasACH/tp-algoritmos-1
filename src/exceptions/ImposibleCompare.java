package exceptions;

import structures.Cell;

public class ImposibleCompare extends Exception {
    public ImposibleCompare(Cell<?> a, Cell<?> b) {
        super("Imposible comparar DataFrames.");
    }
}