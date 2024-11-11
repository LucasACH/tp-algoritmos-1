package interfaces;

import exceptions.IndexOutOfBounds;
import exceptions.InvalidShape;
import exceptions.TypeDoesNotMatch;

public interface Visualizer<T> {
    public void show();

    public T head(int n) throws IndexOutOfBounds, InvalidShape, TypeDoesNotMatch;

    public T tail(int n) throws IndexOutOfBounds, InvalidShape, TypeDoesNotMatch;
}
