import java.util.Comparator;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InventoryManagementModel {
    private final ObservableList<Item> items;
    private final ObservableList<Item> filteredItems;

    InventoryManagementModel() {
        this.items = FXCollections.observableArrayList();
        this.filteredItems = FXCollections.observableArrayList();

        items.add(new Meal("Pizza", Size.LARGE, 1900, 10, Spice.HOT, Category.DINNER, "Meal"));
        items.add(new Beverage("Cola", Size.SMALL, 200, 30, addTopping.ICE_CUBES, Temp.CHILLED, "Beverage"));
        items.add(new Meal("Submarine", Size.MEDIUM, 1000, 15, Spice.MEDIUM, Category.LUNCH, "Meal"));
        items.add(new Beverage("Faluda", Size.LARGE, 600, 20, addTopping.ICE_CREAM, Temp.CHILLED, "Beverage"));

        // Initially copy all items to filteredItems
        filteredItems.addAll(items);
    }
    
    public ObservableList<Item> itemProperty() {
        return this.items;
    }

    public ObservableList<Item> filteredItemProperty() {
        return this.filteredItems;
    }

    public void addItem(Item i) {
        this.items.add(i);
        updateFilter(null); // Refresh filter
    }

    public void editItem(Item i, int index) {
        this.items.set(index, i);
        updateFilter(null); // Refresh filter
    }

    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
          items.remove(index);
          updateFilter(null); // Refresh filter
        } else {
          System.out.println("Error in removal");
        }
    }

    public void removeAll() {
        items.clear();
        filteredItems.clear();
    }

    public void updateFilter(String searchQuery) {
        filteredItems.clear();
        if (searchQuery == null || searchQuery.isEmpty()) {
            filteredItems.addAll(items);
        } else {
            for (Item item : items) {
                String lowerCaseFilter = searchQuery.toLowerCase();
                if (item.getName().toLowerCase().contains(lowerCaseFilter) ||
                    item.getItemType().toLowerCase().contains(lowerCaseFilter) ||
                    item.getSize().toString().toLowerCase().contains(lowerCaseFilter)) {
                    filteredItems.add(item);
                }
            }
        }
    }


    public void sortItemsByQuantity(boolean ascending) {
        if (ascending) {
            FXCollections.sort(filteredItems, Item.QUANTITY_ASC_COMPARATOR);
        } else {
            FXCollections.sort(filteredItems, Item.QUANTITY_DESC_COMPARATOR);
        }
    }

    
}





class Item 
{
    private final SimpleStringProperty name;
    private final SimpleObjectProperty<Size> size;
    private final SimpleDoubleProperty price;
    private final SimpleIntegerProperty quantity;
    private final SimpleStringProperty itemType;

    //Sort by quantity
    public final static Comparator<Item> QUANTITY_ASC_COMPARATOR = Comparator.comparing(Item :: getQuantity); //comparator to sort the list ascending order
    public final static Comparator<Item> QUANTITY_DESC_COMPARATOR = Comparator.comparing(Item :: getQuantity).reversed(); //comparator to sort the list descending order 


    public Item(String name, Size size, double price, int quantity, String itemType) 
    {
        this.name = new SimpleStringProperty(name);
        this.size = new SimpleObjectProperty<>(size);
        this.price = new SimpleDoubleProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.itemType = new SimpleStringProperty(itemType);
    }
    
    //getter methods & setter methods
    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getName() 
    {
        return name.get();
    }

    public void setName(String name) 
    {
        this.name.set(name);
    }

    public SimpleObjectProperty<Size> sizeProperty() {
        return size;
    }

    public Size getSize() 
    {
        return size.get();
    }

    public void setSize(Size newSize) {
        this.size.set(newSize);
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }    

    public double getPrice() 
    {
        return price.get();
    }

    public void setPrice(double price) 
    {
        this.price.set(price);;
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public int getQuantity(){
        return quantity.get();
    }

    public void setQuantity(int quantity) 
    {
        this.quantity.set(quantity);; 
    }

    public SimpleStringProperty itemTypeProperty() {
        return itemType;
    }

    public String getItemType() {
        return itemType.get();
    }

    //toString method
    @Override
    public String toString() {
        return "Item [name=" + name + ", size=" + size + ", price=" + price + ", quantity=" + quantity + "]";
    }
}

//child class of Item
class Meal extends Item 
{ 

    private SimpleObjectProperty<Spice> spiceLevel;
    private SimpleObjectProperty<Category> category;

    public Meal(String name, Size size, double price, int quantity, Spice spiceLevel, Category category, String itemType) {
        super(name, size, price, quantity, "Meal");
        this.spiceLevel = new SimpleObjectProperty<>(spiceLevel);
        this.category = new SimpleObjectProperty<>(category);
        
    }

    //getter methods 
    public SimpleObjectProperty<Spice> spiceLevelProperty() {
        return spiceLevel;
    }
    public Spice getSpiceLevel() {
        return spiceLevel.get();
    }

    public SimpleObjectProperty<Category> categoryProperty() {
        return category;
    }
    public Category getCategory() {
        return category.get();
    }

    @Override
    public String toString() {
        return "----Meals----\n" +
        "Name: " + getName() + "\n" +
        "Size: " + getSize() + "\n" +
        "Price: " + getPrice() + "\n" +
        "Quantity: " + getQuantity() + "\n" +
        "Spice Level: " + spiceLevel + "\n" +
        "Category: " + category;
    }
    
}

class Beverage extends Item //child class of Item  
{
    private SimpleObjectProperty<addTopping> bevTopping;
    private SimpleObjectProperty<Temp> temp;

    public Beverage(String name, Size size, double price, int quantity, addTopping bevTopping, Temp temp, String itemType) {
        super(name, size, price, quantity, "Beverage");
        this.bevTopping = new SimpleObjectProperty<>(bevTopping);
        this.temp = new SimpleObjectProperty<>(temp);
    }

    public boolean hasTopping() 
    {
        return bevTopping != null; // Check if bevTopping is not null
    }

    //getter methods 
    public SimpleObjectProperty<addTopping> bevToppingProperty() {
        return bevTopping;
    }

    public addTopping getBevTopping() {
        return bevTopping.get();
    }

    public SimpleObjectProperty<Temp> tempProperty() {
        return temp;
    }

    public Temp getTemp() {
        return temp.get();
    }

    @Override
    public String toString() {
        return "----Beverages---- \nName: " + getName() + "\nSize: " + getSize() + "\nPrice: " + getPrice()
        + "\nQuantity: " + getQuantity() + "\nbevTopping: "+  bevTopping + "\nTemperature: " + temp + "\nhasTopping:" + hasTopping();
    }

}

enum Size 
    { //constants for sizes 
        SMALL("Small"),
        MEDIUM("Medium"),
        LARGE("Large");

        private final String DISPLAY_SIZE_TEXT;

        private Size(String displayText) {
            DISPLAY_SIZE_TEXT = displayText;
        }

        @Override
        public String toString() {
            return DISPLAY_SIZE_TEXT;
        }
    }

enum Spice 
    { //constants for Spices
        MILD("Mild"),
        MEDIUM("Medium"),
        HOT("Hot");

        private final String DISPLAY_SPICE_TEXT;

        private Spice(String displayText) {
            DISPLAY_SPICE_TEXT = displayText;
        }

        @Override
        public String toString() {
            return DISPLAY_SPICE_TEXT;
        }
    }
    
enum Category
    { //constants for category 
        BREAKFAST("Breakfast"),
        LUNCH("Lunch"),
        TEA_TIME("Tea Time"),
        DINNER("Dinner");

        private final String DISPLAY_CATEGORY_TEXT;

        private Category(String displayText) {
            DISPLAY_CATEGORY_TEXT = displayText;
        }

        @Override
        public String toString() {
            return DISPLAY_CATEGORY_TEXT;
        }
    }

enum addTopping
    { //constants for toppings
        ICE_CREAM("Ice Cream"),
        BOBA_PEARLS("Boba Pearls"),
        FRUIT("Fruit"),
        ICE_CUBES("Ice Cubes"),
        NONE("None");

        private final String DISPLAY_ADD_TOPPING_TEXT;

        private addTopping(String displayText) {
            DISPLAY_ADD_TOPPING_TEXT = displayText;
        }

        @Override
        public String toString() {
            return DISPLAY_ADD_TOPPING_TEXT;
        }
    }
    
enum Temp
    { //constants for temp
        CHILLED("Chilled"),
        NORMAL("Normal"),
        HOT("Hot");

        private final String DISPLAY_TEMP_TEXT;

        private Temp(String displayText) {
            DISPLAY_TEMP_TEXT = displayText;
        }

        @Override
        public String toString() {
            return DISPLAY_TEMP_TEXT;
        }
    }
