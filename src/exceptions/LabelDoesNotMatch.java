package exceptions;

public class LabelDoesNotMatch extends Exception {
    public LabelDoesNotMatch() {
        super("The labels do not match the columns in the DataFrame");
    }
}