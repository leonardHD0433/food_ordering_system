package software_design.model;

public class TableManager {
    private static TableManager instance;
    private final Table[] tables;
    private static final int MAX_TABLES = 15;
    private Table currentTable;

    private TableManager() {
        tables = new Table[MAX_TABLES];
        // Initialize tables
        for (int i = 0; i < MAX_TABLES; i++) {
            tables[i] = new Table(i + 1);
        }
    }

    public static TableManager getInstance() {
        if (instance == null) {
            instance = new TableManager();
        }
        return instance;
    }

    public void setTable(int tableNumber, Table table) {
        tables[tableNumber - 1] = table;
    }

    public Table getTable(int tableNumber) {
        currentTable = tables[tableNumber - 1];
        return currentTable;
    }

    public void setCurrentTable(int tableNumber) {
        this.currentTable = getTable(tableNumber);
    }

    public Table getCurrentTable() {
        return currentTable;
    }

    public Table[] getAllTables() {
        return tables;
    }

    public void clearTable(int tableNumber) {
        tables[tableNumber - 1].clearCart();
        tables[tableNumber - 1].setOrder(null);
    }
}