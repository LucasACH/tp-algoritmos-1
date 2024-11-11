package tests;

import libraries.DataImporter;
import structures.DataFrame;

public class TestReadJSON {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        DataFrame df = DataImporter.readJSON("data/dummy.json");

        df.show();
    }
}
