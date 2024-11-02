import exceptions.IndexOutOfBounds;

public interface Visualizer<T> {
    public void show();

    public T head(int n) throws IndexOutOfBounds;

    public T tail(int n) throws IndexOutOfBounds;
}
