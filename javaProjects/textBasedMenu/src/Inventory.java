import java.util.ArrayList;
import java.util.List;

class Inventory { //store items
    static List<Item> INVENTORY = new ArrayList<>(List.of(
        new Meal("Sandwiches", Size.MEDIUM, 10, 10, Spice.MILD, Category.BREAKFAST), 
        new Beverage("Smoothie", Size.LARGE, 7.0, 20, addTopping.ICE_CREAM, Temp.CHILLED),
        new Meal("Burger", Size.SMALL, 14, 25, Spice.HOT, Category.LUNCH),
        new Beverage("Milk Tea", Size.MEDIUM, 6.0, 15, addTopping.BOBA_PEARLS, Temp.CHILLED)
        
    ));
    
    
}
