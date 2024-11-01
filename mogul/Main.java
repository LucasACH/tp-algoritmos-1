import java.io.IOException;

import exceptions.IndexOutOfBounds;

public class Main {
    public static void main(String[] args) throws Exception {
        // DataFrame df = new DataFrame();
        // Column<Integer> col1 = new Column<>("col1");
        // Column<Float> col2 = new Column<>("col2");
        // Column<String> col3 = new Column<>("col3");
        // col1.addCell(new Cell<>(1));
        // col1.addCell(new Cell<>(2));
        // col1.addCell(new Cell<>(3));
        // col2.addCell(new Cell<>(1.1f));
        // col2.addCell(new Cell<>(2.2f));
        // col2.addCell(new Cell<>(3.3f));
        // col3.addCell(new Cell<>("a"));
        // col3.addCell(new Cell<>("b"));
        // col3.addCell(new Cell<>("c"));
        // df.insertColumn(col1);
        // df.insertColumn(col2);
        // df.insertColumn(col3);
        // System.out.println(df.countRows()); // 3
        // System.out.println(df.countColumns()); // 3
        // df.show();
        // System.out.println("\n" + df);

        DataFrame df2 = null;
        try {
            df2 = DataImporter.readCSV("/home/tareas/Downloads/prueba.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataFrame df3 = null;
        try {
            df3 = DataImporter.readJSON("/home/tareas/Downloads/prueba.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        df2.show();
        df3.show();

        DataFrame df_head = df2.head(2);
        df_head.show();
    }
}
