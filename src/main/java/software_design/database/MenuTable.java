package software_design.database;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "menu_table")
public class MenuTable
{
    @Id
    @GeneratedValue(generator = "custom-id")
    @GenericGenerator(name = "custom-id", strategy = "software_design.database.generator.ItemIdGenerator")
    @Column(name = "ItemId", nullable = false)
    private int itemId;

    @Column(name = "ItemCategory", nullable = false, length = 20)
    private String itemCategory;

    @Column(name = "ItemName", nullable = false, length = 50)
    private String itemName;

    @Column(name = "ItemDescription", nullable = false, length = 150)
    private String itemDescription;

    @Column(name = "ItemPrice", nullable = false)
    private double itemPrice;

    @Column(name = "ItemAvailability", nullable = false, columnDefinition = "VARCHAR(5)")
    private String itemAvailability;

    //Setter methods
    public void setItemDetails(String itemCategory, String itemName, String itemDescription, double itemPrice, String itemAvailability)
    {
        this.itemCategory = itemCategory;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.itemAvailability = itemAvailability;
    }

    //Getter methods
    public int getItemId()
    {
        return itemId;
    }

    public String getItemCategory()
    {
        return itemCategory;
    }

    public String getItemName()
    {
        return itemName;
    }

    public String getItemDescription()
    {
        return itemDescription;
    }

    public double getItemPrice()
    {
        return itemPrice;
    }

    public boolean getItemAvailability()
    {
        return itemAvailability.equals("true");
    }
}
