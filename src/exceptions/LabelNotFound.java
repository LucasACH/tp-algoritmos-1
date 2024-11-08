package exceptions;

public class LabelNotFound extends Exception {
    public LabelNotFound(String label_not_found_in_columns) {
        super("The label was not found in the DataFrame.");
    }
}