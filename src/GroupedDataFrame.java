
import java.util.List;
import java.util.Map;

class GroupedDataFrame {
    private Map<String, List<Row>> groups;
    private DataFrame df;

    public GroupedDataFrame(DataFrame df) {
        this.df = df;

    }

    public void sum(Object column) {
        // sum the values in each group
    }

    public void mean(Object column) {
        // calculate the mean of the values in each group
    }

    public void min(Object column) {
        // find the minimum value in each group
    }

    public void max(Object column) {
        // find the maximum value in each group
    }

    public void count() {
        // count the number of rows in each group
    }

    public void std(Object column) {
        // calculate the standard deviation of the values in each group
    }

    public void var(Object column) {
        // calculate the variance of the values in each group
    }
}
