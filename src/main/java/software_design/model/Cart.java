package software_design.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<MenuItem> items;
    private List<Integer> itemQuantity;
    private List<String> remarks;
    private List<String> itemOption;
    private double total;


    //Default constructor
    public Cart() {
        this.items = new ArrayList<>();
        this.itemQuantity = new ArrayList<>();
        this.remarks = new ArrayList<>();
        this.itemOption = new ArrayList<>();
        this.total = 0.0;
    }
    
    //Overloaded constructor
    public Cart(List<MenuItem> items, List<Integer> itemQuantity, List<String> itemOption, List<String> remarks) {
        this.items = items;
        this.itemQuantity = itemQuantity;
        this.itemOption = itemOption;
        this.remarks = remarks;
    }

    public void addItem(MenuItem item, int quantity, String option, String remark) {
        // Check if item with same option already exists
        for (int i = 0; i < items.size(); i++) 
        {
            if (items.get(i).getId() == item.getId() && 
                itemOption.get(i).equals(option) && remarks.get(i).equals(remark)) 
            {
                // Update existing item quantity
                itemQuantity.set(i, itemQuantity.get(i) + quantity);
                updateTotal();
                return;
            }
        }
        
        // If not found, add as new item
        items.add(item);
        itemQuantity.add(quantity);
        itemOption.add(option);
        remarks.add(remark);
        updateTotal();
    }

    public void updateTotal() {
        total = 0.0;
        for (int i = 0; i < items.size(); i++) {
            total += items.get(i).getPrice() * itemQuantity.get(i);
        }
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    public List<MenuItem> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }
    
    public List<Integer> getQuantities() {
        return itemQuantity;
    }
    
    public List<String> getOptions() {
        return itemOption;
    }
    
    public List<String> getRemarks() {
        return remarks;
    }
}
