package software_design.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        this.items = new ArrayList<>();
        this.quantities = new ArrayList<>();
        this.options = new ArrayList<>();
        this.remarks = new ArrayList<>();
        this.itemStatus = new ArrayList<>();
        
        for (int i = 0; i < cart.getItems().size(); i++) {
            MenuItem item = cart.getItems().get(i);
            int quantity = cart.getQuantities().get(i);
            String option = cart.getOptions().get(i);
            String remark = cart.getRemarks().get(i);
        
            for (int q = 0; q < quantity; q++) {
                this.items.add(item);
                this.quantities.add(1); // Each item is treated individually
                this.options.add(option);
                this.remarks.add(remark);
                this.itemStatus.add("Pending");
            }
        }

        this.total = cart.getTotal();
        this.orderTime = LocalDateTime.now().format(ORDER_TIME_FORMAT);
        
    }

    // Add items from cart to existing order
    public void addItems(Cart cart) {
        for (int i = 0; i < cart.getItems().size(); i++) {
            MenuItem item = cart.getItems().get(i);
            int quantity = cart.getQuantities().get(i);
            String option = cart.getOptions().get(i);
            String remark = cart.getRemarks().get(i);
        
            for (int q = 0; q < quantity; q++) {
                this.items.add(item);
                this.quantities.add(1);
                this.options.add(option);
                this.remarks.add(remark);
                this.itemStatus.add("Pending");
            }
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

    public void calculateTotal() {
        total = 0.0;
        for (int i = 0; i < items.size(); i++) {
            total += items.get(i).getPrice() * quantities.get(i);
        }
    }

    public List<ConsolidatedOrder> getConsolidatedView() {
        Map<String, ConsolidatedOrder> consolidatedMap = new HashMap<>();

        for (int i = 0; i < items.size(); i++) {
            MenuItem item = items.get(i);
            String option = options.get(i);
            String remark = remarks.get(i);
            String status = itemStatus.get(i);
            String key = item.getId() + "-" + option + "-" + remark + "-" + status;

            if (consolidatedMap.containsKey(key)) {
                ConsolidatedOrder consolidatedItem = consolidatedMap.get(key);
                consolidatedItem.setQuantity(consolidatedItem.getQuantity() + 1);
            } else {
                ConsolidatedOrder consolidatedItem = new ConsolidatedOrder(item, 1, option, remark, status);
                consolidatedMap.put(key, consolidatedItem);
            }
        }

        return new ArrayList<>(consolidatedMap.values());
    }
}
