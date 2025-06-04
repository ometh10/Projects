import java.util.List;

public class App {
    
    public static void main(String[] args) 
    {
        List<Item> inventoryList = Inventory.INVENTORY;
        ManagementMenu menu = new ManagementMenu(inventoryList.toArray(new Item[inventoryList.size()]));
        Diner diner = new Diner("Diners Delight", "London", 300, 215);
        menu.setDiner(diner);
        menu.runMainMenu(diner); //calling the main menu

    } 

} 


