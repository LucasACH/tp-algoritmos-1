

class Row {
    private String label;
    private int id;

    public Row(String label) {
        this.label = label;
        this.id = -1;
    }
    public Row(int id) {
        this.label = null;
        this.id = id;
    }
}
