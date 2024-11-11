package software_design.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<MenuItem> items;
    
    public Cart() {
        this.items = new ArrayList<>();
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    public List<MenuItem> getItems() {
        return items;
    }
}
