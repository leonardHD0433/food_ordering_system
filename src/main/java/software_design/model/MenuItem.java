package software_design.model;

public class MenuItem {
    private int id;
    private String category;
    private String name;
    private String description;
    private String options;
    private double price;
    private boolean availability;
    private byte[] image;

    // Constructor, getters and setters
    public MenuItem(int id, String category, String name, String description, 
                   String options, double price, boolean availability, byte[] image) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.description = description;
        this.options = options;
        this.price = price;
        this.availability = availability;
        this.image = image;
    }

    // Getters
    public int getId() 
    { 
        return id; 
    
    }
    public String getName() 
    { 
        return name; 
    }

    public double getPrice() 
    { 
        return price; 
    }
    public byte[] getImage() 
    { 
        return image; 
    }

    public String getCategory() 
    { 
        return category; 
    }

    public String getDescription() 
    { 
        return description; 
    }

    public String getOptions() 
    { 
        return options; 
    }

    public boolean isAvailable() 
    { 
        return availability; 
    }
}
