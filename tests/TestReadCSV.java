import java.util.List;

public class TestReadCSV {
    public static void main(String[] args) throws Exception {
        DataFrame df = DataImporter.readCSV("data/dummy.csv");

        assert df.countRows() == 5;
        assert df.countColumns() == 5;

        assert df.getColumn("Name") == df.getColumns().get(0);
        assert df.getColumn("Age") == df.getColumns().get(1);
        assert df.getColumn("City") == df.getColumns().get(2);
        assert df.getColumn("Occupation") == df.getColumns().get(3);
        assert df.getColumn("Salary") == df.getColumns().get(4);

        assert df.copy() != df;

        df.insertRow(List.of(
                new Cell<>("John"),
                new Cell<>(25), new Cell<>("New York"),
                new Cell<>("Developer"),
                new Cell<>(50000)));

        df.show();
    }
}
