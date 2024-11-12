package interfaces;

import exceptions.IndexOutOfBounds;
import exceptions.InvalidShape;
import exceptions.LabelAlreadyInUse;
import exceptions.LabelNotFound;
import exceptions.TypeDoesNotMatch;

public interface Visualizer<T> {
    public void show() throws IndexOutOfBounds, InvalidShape, TypeDoesNotMatch, LabelAlreadyInUse, LabelNotFound;

    public T head(int n) throws IndexOutOfBounds, InvalidShape, TypeDoesNotMatch;

    public T tail(int n) throws IndexOutOfBounds, InvalidShape, TypeDoesNotMatch;
}
