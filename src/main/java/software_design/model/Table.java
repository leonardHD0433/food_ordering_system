package software_design.model;

public class Table 
{
    private int tableId;
    private Cart cart;

    public Table(int tableId) 
    {
        setTableId(tableId);
        this.cart = new Cart();
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
    
    // Add cart management methods
    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
