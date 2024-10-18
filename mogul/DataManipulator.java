package mogul;
import java.util.List;
import java.util.function.Predicate;

class DataManipulator <T>{
    private DataFrame<T> dataframe;


    public void sortBy(List<String> columns, boolean descending) {
        // sort the DataFrame by the specified columns
    }

    public void fillna(String column, T value) {
        // fill missing values in the DataFrame
    }

    public void filter(String name, Predicate<String> function) {
        // filter the DataFrame by the specified function
    }

    public void slice(List<Integer> rows, List<Integer> columns) {
        // slice the DataFrame
    }

    public void sample(double frac){
        // sample the DataFrame
    }

    public void groupBy(List<String> columns) {
        // group the DataFrame by the specified columns
    }   
}
