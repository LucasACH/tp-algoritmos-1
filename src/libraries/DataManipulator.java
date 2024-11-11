package libraries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

import exceptions.IndexOutOfBounds;
import exceptions.InvalidShape;
import exceptions.LabelAlreadyInUse;
import exceptions.LabelNotFound;
import exceptions.TypeDoesNotMatch;
import structures.Cell;
import structures.Column;
import structures.DataFrame;
import structures.GroupedDataFrame;
import structures.Row;

public class DataManipulator {
    private DataFrame df;

    public DataManipulator(DataFrame df) {
        this.df = df;
    }

    @SuppressWarnings("unchecked")
    public DataFrame sortBy(List<? extends Object> labels, boolean descending)
            throws LabelNotFound, InvalidShape, TypeDoesNotMatch, IndexOutOfBounds {

        if (!this.df.getColumnLabels().containsAll(labels)) {
            throw new LabelNotFound();
        }

        List<List<?>> rows = new ArrayList<>();

        this.df.getRows().sort((row1, row2) -> {
            for (Object label : labels) {
                int index = this.df.getColumnLabels().indexOf(label);
                Cell<?> cell1 = row1.getCell(index);
                Cell<?> cell2 = row2.getCell(index);

                if (cell1.isEmpty() && cell2.isEmpty()) {
                    continue;
                } else if (cell1.isEmpty()) {
                    return descending ? 1 : -1;
                } else if (cell2.isEmpty()) {
                    return descending ? -1 : 1;
                }

                int comparison = ((Comparable<Object>) cell1.getValue()).compareTo(cell2.getValue());
                if (comparison != 0) {
                    return descending ? -comparison : comparison;
                }
            }
            return 0;
        });

        for (Row row : this.df.getRows()) {
            List<Object> cells = new ArrayList<>();
            for (Cell<?> cell : row.getCells()) {
                cells.add(cell.getValue());
            }
            rows.add(cells);
        }

        return new DataFrame(rows, this.df.getColumnLabels());
    }

    @SuppressWarnings("unchecked")
    public <T> void fillna(Object label, T value) throws LabelNotFound, TypeDoesNotMatch, IndexOutOfBounds {
        Column<T> column = (Column<T>) this.df.getColumn(label);
        for (Cell<T> cell : column.getCells()) {
            if (cell.isEmpty()) {
                cell.setValue(value);
            }
        }
    }

    public DataFrame filter(Object label, Predicate<Object> condition)
            throws LabelNotFound, InvalidShape, TypeDoesNotMatch, IndexOutOfBounds {

        int index = this.df.getColumnLabels().indexOf(label);

        if (index == -1) {
            throw new LabelNotFound();
        }

        List<List<?>> rows = new ArrayList<>();

        for (Row row : this.df.getRows()) {
            List<Object> cells = new ArrayList<>();
            if (condition.test(row.getCell(index).getValue())) {
                for (Cell<?> cell : row.getCells()) {
                    cells.add(cell.getValue());
                }
                rows.add(cells);
            }
        }

        return new DataFrame(rows, this.df.getColumnLabels());
    }

    public DataFrame filter(Map<Object, Predicate<Object>> conditions) throws LabelNotFound, InvalidShape, TypeDoesNotMatch, IndexOutOfBounds {
        List<Integer> indices = new ArrayList<>();
        for (Object label : conditions.keySet()) {
            int index = this.df.getColumnLabels().indexOf(label);
            if (index == -1) {
                throw new LabelNotFound("Label not found: " + label);
            }
            indices.add(index);
        }

        List<List<?>> rows = new ArrayList<>();
        for (Row row : this.df.getRows()) {
            boolean match = true;
            
            for (int i = 0; i < indices.size(); i++) {
                int index = indices.get(i);
                Object label = conditions.keySet().toArray()[i];
                Predicate<Object> condition = conditions.get(label);

                if (!condition.test(row.getCell(index).getValue())) {
                    match = false;
                    break;
                }
            }

            if (match) {
                List<Object> cells = new ArrayList<>();
                for (Cell<?> cell : row.getCells()) {
                    cells.add(cell.getValue());
                }
                rows.add(cells);
            }
        }

        return new DataFrame(rows, this.df.getColumnLabels());
    }


    public DataFrame sample(double frac) throws InvalidShape, TypeDoesNotMatch, LabelAlreadyInUse, IndexOutOfBounds {
        if (frac < 0 || frac > 1) {
            throw new IllegalArgumentException("Fraction must be between 0 and 1.");
        }

        List<Row> rows = this.df.getRows();
        int sampleSize = (int) (rows.size() * frac);
        Set<Integer> indices = new HashSet<>();
        Random random = new Random();

        while (indices.size() < sampleSize) {
            indices.add(random.nextInt(rows.size()));
        }

        List<List<?>> sampleRows = new ArrayList<>();
        for (int index : indices) {
            List<Object> cells = new ArrayList<>();
            for (Cell<?> cell : rows.get(index).getCells()) {
                cells.add(cell.getValue());
            }
            sampleRows.add(cells);
        }

        return new DataFrame(sampleRows, this.df.getColumnLabels());
    }

    public DataFrame slice(int start, int end)
            throws InvalidShape, TypeDoesNotMatch, LabelAlreadyInUse, IndexOutOfBounds {
        return new DataFrame(this.df.getColumns().subList(start, end));
    }

    public GroupedDataFrame groupBy(List<? extends Object> labels) throws LabelNotFound, IndexOutOfBounds {
        Map<String, List<Row>> rows = new HashMap<String, List<Row>>();

        for (Row row : this.df.getRows()) {
            StringBuilder groupName = new StringBuilder();
            for (Object label : labels) {
                int index = this.df.getColumnLabels().indexOf(label);
                groupName.append(row.getCell(index).getValue()).append("_");
            }

            String group = groupName.substring(0, groupName.length() - 1);

            if (!rows.containsKey(group)) {
                rows.put(group, new ArrayList<Row>());
            }
            rows.get(group).add(row);
        }

        return new GroupedDataFrame(this.df, rows);
    }
}
