package exceptions;

public class InvalidShape extends Exception {
    public InvalidShape() {
        super("The number of cells does not match the number of columns.");
    }
}