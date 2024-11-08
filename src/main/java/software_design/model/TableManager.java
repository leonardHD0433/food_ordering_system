package software_design.model;

public class TableManager {
    private static TableManager instance;
    private final Table[] tables;
    private static final int MAX_TABLES = 20;

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

    public Table getTable(int tableNumber) {
        if (tableNumber < 1 || tableNumber > MAX_TABLES) {
            throw new IllegalArgumentException("Invalid table number");
        }
        return tables[tableNumber - 1];
    }
}