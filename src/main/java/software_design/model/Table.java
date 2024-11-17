package software_design.model;

public class Table 
{
    private int tableId;
    private Cart cart;
    private Order order;

    public Table(int tableId) 
    {
        setTableId(tableId);
        this.cart = new Cart();
        this.order = null;
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

    public void clearCart() {
        this.cart = new Cart();
    }

    // Add order management methods
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
