package mogul;
import java.util.Map;
import java.util.List;


class GroupedDataFrame {
    private Map<String, List<Row>> groups;


    public void sum(String column) {
        // sum the values in each group
    }

    public void mean(String column) {
        // calculate the mean of the values in each group
    }

    public void min(String column) {
        // find the minimum value in each group
    }

    public void max(String column) {
        // find the maximum value in each group
    }

    public void count() {
        // count the number of rows in each group
    }

    public void std(String column) {
        // calculate the standard deviation of the values in each group
    }

    public void var(String column) {
        // calculate the variance of the values in each group
    }
}
