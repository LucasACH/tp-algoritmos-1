import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.Random;
import java.util.Set;

import exceptions.IndexOutOfBounds;
import exceptions.InvalidShape;
import exceptions.LabelAlreadyInUse;
import exceptions.LabelNotFound;
import exceptions.TypeDoesNotMatch;

class DataManipulator {
    private DataFrame dataframe;

    DataManipulator(DataFrame dataframe) {
        this.dataframe = dataframe;
    }

    // public DataFrame sortBy(List<String> columnNames, boolean descending) throws InvalidShape, LabelNotFound, Exception {
    //     DataFrame df = this.dataframe.copy();
    //     List<String> columnLabels = df.getColumnLabels();
    //     for (String columnName : columnNames) {
    //         if (columnName == null || !columnLabels.contains(columnName)) {
    //             throw new LabelNotFound();
    //         }
    //         if (columnLabels.contains(columnName)) {
    //             Column<?> column = df.getColumn(columnName);
    //             column.sort(descending);
    //         }
    //     } 
    //     return df;
    // }

    public DataFrame sortBy(List<String> columnNames, boolean descending) throws InvalidShape, LabelNotFound, LabelAlreadyInUse, TypeDoesNotMatch, IndexOutOfBounds {
        // Crear una copia del DataFrame actual
        DataFrame df = new DataFrame();
        List<String> columnLabels = this.dataframe.getColumnLabels();

        // Insertar las columnas del DataFrame original en el nuevo DataFrame
        for (String columnLabel : columnLabels) {
            df.insertColumn(columnLabel);
        }
    
        // Validar que todos los nombres de columnas especificados existan en el DataFrame
        List<Integer> columnIndices = new ArrayList<>();
        for (String columnName : columnNames) {
            int columnIndex = columnLabels.indexOf(columnName);
            if (columnIndex == -1) {
                throw new LabelNotFound();
            }
            columnIndices.add(columnIndex);
        }
    
        // Definir un Comparator para las filas, usando los índices de las columnas
        Comparator<Row> rowComparator = (row1, row2) -> {
            for (int columnIndex : columnIndices) {
                Cell<?> cell1 = row1.getCell(columnIndex);
                Cell<?> cell2 = row2.getCell(columnIndex);
    
                // Verificar que ambos tipos de celda sean comparables
                if (cell1.getValue().getClass() == cell2.getValue().getClass()) {
                    @SuppressWarnings("unchecked")
                    Cell<Comparable<Object>> comparableCell1 = (Cell<Comparable<Object>>) cell1;
                    @SuppressWarnings("unchecked")
                    Cell<Comparable<Object>> comparableCell2 = (Cell<Comparable<Object>>) cell2;
    
                    // Comparar las celdas
                    int comparisonResult = comparableCell1.compareTo(comparableCell2);
                    if (comparisonResult != 0) {
                        return descending ? -comparisonResult : comparisonResult;
                    }
                } else {
                    throw new ClassCastException("Tipos incompatibles en las celdas: "
                        + cell1.getValue().getClass() + " y " + cell2.getValue().getClass());
                }
            }
            return 0;
        };
    
        // Ordenar las filas del DataFrame usando el Comparator creado
        List<Row> sortedRows = new ArrayList<>(this.dataframe.getRows()); // Crear una lista mutable de filas
        sortedRows.sort(rowComparator); // Ordenar la lista de filas
    
        // Actualizar las filas ordenadas en el DataFrame copiado
        for (Row row : sortedRows) {
            List<Cell<?>> cells = new ArrayList<>();
            for (int i = 0; i < columnLabels.size(); i++) {
                cells.add(row.getCell(i));
            }
            df.insertRow(row.getLabel(), cells);  // Insertar cada fila en orden en el nuevo DataFrame
        }
    
        return df;
    }


    public <T> void fillna(String column, T value) throws LabelNotFound {
        for (String columnLabel : this.dataframe.getColumnLabels()) {
            if (columnLabel.equals(column)) {
                @SuppressWarnings("unchecked")
                Column<T> columnToFill = (Column<T>) this.dataframe.getColumn(column);
                for (Cell<T> cell : columnToFill.getCells()) {
                    if (cell.isEmpty()) {
                        cell.setValue(value);
                    }
                }
            }
        }
    }

    public void filter(String name, Predicate<String> function) {
        // filter the DataFrame by the specified function
    }

    public DataFrame sample(double frac) throws InvalidShape, TypeDoesNotMatch, LabelAlreadyInUse, IndexOutOfBounds {
        if (frac < 0 || frac > 1) {
            throw new IllegalArgumentException("La fracción debe estar entre 0 y 1");
        }

        List<String> columnLabels = this.dataframe.getColumnLabels();
        DataFrame df = new DataFrame(); // Crear un nuevo DataFrame vacío con los mismos labels
        for (String columnLabel : columnLabels) {
            df.insertColumn(columnLabel); // TODO: Revisar que este bloque de codigo esta repetido, se puede crear un metodo que haga esto
        }
        int totalRows = this.dataframe.countRows();
        int sampleSize = (int) (frac * totalRows);
        // List<Row> sampleRows = new ArrayList<>();
        Random random = new Random();

        // Para evitar duplicados, podemos usar un Set
        Set<Integer> sampledIndices = new HashSet<>();

        while (sampledIndices.size() < sampleSize) {
            int randomIndex = random.nextInt(totalRows); // Obtener un índice aleatorio
            if (sampledIndices.add(randomIndex)) { // Si el índice no estaba en el Set, se agrega
                Row sampledRow = this.dataframe.getRows().get(randomIndex); // Obtener la fila correspondiente
                List<Cell<?>> cells = new ArrayList<>();
                for (int i = 0; i < sampledRow.getCells().size(); i++) {
                    cells.add(sampledRow.getCell(i)); // Agregar las celdas de la fila a la lista
                }
                df.insertRow(sampledRow.getLabel(), cells); // Insertar la fila en el nuevo DataFrame
            }
        } // TODO: Revisar que devuelve los labels de las filas del dataframe original
        return df; // Retornar el nuevo DataFrame con la muestra
    }

    public void groupBy(List<String> columns) {
        // group the DataFrame by the specified columns
    }
}
