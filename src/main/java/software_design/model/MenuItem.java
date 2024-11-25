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

    // Setters
    public void setName(String name) 
    { 
        this.name = name; 
    }

    public void setPrice(double price) 
    { 
        this.price = price; 
    }

    public void setImage(byte[] image) 
    { 
        this.image = image; 
    }

    public void setCategory(String category) 
    { 
        this.category = category; 
    }

    public void setDescription(String description) 
    { 
        this.description = description; 
    }

    public void setOptions(String options) 
    { 
        this.options = options; 
    }

    public void setAvailable(boolean availability) 
    { 
        this.availability = availability; 
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
