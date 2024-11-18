package software_design.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private static final DateTimeFormatter ORDER_TIME_FORMAT = 
        DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    private static final DateTimeFormatter DISPLAY_TIME_FORMAT = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private String orderId;
    private int tableId;
    private List<MenuItem> items;
    private List<Integer> quantities;
    private List<String> options;
    private List<String> remarks;
    private List<String> itemStatus;
    private double total;
    private String orderTime;

    public Order(String orderID, int tableId, Cart cart) {
        this.orderId = orderID;
        this.tableId = tableId;
        this.items = new ArrayList<>(cart.getItems());
        this.quantities = new ArrayList<>(cart.getQuantities());
        this.options = new ArrayList<>(cart.getOptions());
        this.remarks = new ArrayList<>(cart.getRemarks());
        this.itemStatus = new ArrayList<>();

        for (int i = 0; i < cart.getItems().size(); i++) {
            this.itemStatus.add("Pending");
        }

        this.total = cart.getTotal();
        this.orderTime = LocalDateTime.now().format(ORDER_TIME_FORMAT);
        
    }

    // Add items from cart to existing order
    public void addItems(Cart cart) {
        this.items.addAll(cart.getItems());
        this.quantities.addAll(cart.getQuantities());
        this.options.addAll(cart.getOptions());
        this.remarks.addAll(cart.getRemarks());
        // Add "Pending" status for new items
        for (int i = 0; i < cart.getItems().size(); i++) {
            this.itemStatus.add("Pending");
        }
        this.total += cart.getTotal();
    }

    // Getters and setters
    public List<String> getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(int index, String status) {
        if (index >= 0 && index < itemStatus.size()) {
            itemStatus.set(index, status);
        }
    }

    // Getters and setters
    public String getOrderId() {
        return orderId;
    }

    public int getTableId() {
        return tableId;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public List<Integer> getQuantities() {
        return quantities; 
    }

    public List<String> getOptions() {
        return options;
    }

    public List<String> getRemarks() {
        return remarks;
    }

    public double getTotal() {
        return total;
    }

    public String getOrderTime() {
        // Parse stored time string back to LocalDateTime
        LocalDateTime dateTime = LocalDateTime.parse(orderTime, ORDER_TIME_FORMAT);
        // Return formatted display string
        return dateTime.format(DISPLAY_TIME_FORMAT);
    }
}
