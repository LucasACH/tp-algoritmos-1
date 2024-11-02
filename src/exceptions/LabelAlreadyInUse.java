package exceptions;

public class LabelAlreadyInUse extends Exception {
    public LabelAlreadyInUse() {
        super("The label is already in use.");
    }
}
