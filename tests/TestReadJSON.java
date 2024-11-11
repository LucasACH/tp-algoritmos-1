import libraries.DataImporter;
import structures.DataFrame;

public class TestReadJSON {
    public static void main(String[] args) throws Exception {
        DataFrame df = DataImporter.readJSON("data/dummy.json");

        df.show();
    }
}
