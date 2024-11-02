package exceptions;

public class TypeDoesNotMatch extends Exception {
    public TypeDoesNotMatch() {
        super("The type of the cell does not match the type of the column.");
    }
}
