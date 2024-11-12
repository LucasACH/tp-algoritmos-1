package tests;

import libraries.DataImporter;
import structures.DataFrame;

public class TestConcat {
    public static void main(String[] args) throws Exception {
        DataFrame df1 = DataImporter.readCSV("data/dummy.csv");
        DataFrame df2 = DataImporter.readJSON("data/dummy.json");

        DataFrame df = df1.concat(df2);
        System.out.println(df);
    }
}
