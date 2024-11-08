package exceptions;

public class LabelNotFound extends Exception {
    public LabelNotFound(String label_not_found_in_columns) {
        super(label_not_found_in_columns);
    }
    public LabelNotFound() {
        super("The label was not found in the DataFrame.");
    }
}