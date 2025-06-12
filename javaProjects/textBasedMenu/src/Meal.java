enum Spice 
{ //constants for Spices
    MILD,
    MEDIUM,
    HOT;

}

enum Category
{ //constants for category 
    BREAKFAST,
    LUNCH,
    TEA_TIME,
    DINNER;

}

//child class of Item
class Meal extends Item 
{ 

    private Spice spiceLevel;
    private Category category;

    public Meal(String name, Size size, double price, int quantity, Spice spiceLevel, Category category) {
        super(name, size, price, quantity);
        this.spiceLevel = spiceLevel;
        this.category = category;
    }

    //getter methods 
    public Spice getSpiceLevel() {
        return spiceLevel;
    }

    public Category getCategory() {
        return category;
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