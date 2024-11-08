package software_design.model;

public class Table 
{
    private int tableId;

    public Table(int tableId) 
    {
        setTableId(tableId);
    }

    // Getters and setters
    public void setTableId(int tableId) 
    {
        this.tableId = tableId;
    }
    
    public int getTableId() 
    {
        return tableId;
    }
}
