package exceptions;

public class LabelNotFound extends Exception {
    public LabelNotFound() {
        super("The label was not found in the DataFrame.");
    }
}