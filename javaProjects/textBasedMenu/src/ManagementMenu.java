import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ManagementMenu 
{
    private List<Item> itemList;
    private Diner diner;
    
    

    public ManagementMenu(Item[] inventory) {
        itemList = new ArrayList<>(); // Create a new ArrayList
        itemList.addAll(Arrays.asList(inventory));} //add items to the list
        public void setDiner(Diner diner) {
            this.diner = diner;   
        }

    void runMainMenu(Diner diner) {
        this.diner = diner;
    System.out.println("--- Main menu ---"); //Main menu
    while (true) {
        System.out.println("Select an option:");
        System.out.println("  1. Manage Inventory");
        System.out.println("  2. Print menu"); 
        System.out.println("  3. Sort the items according to the quantity (Ascending) ");
        System.out.println("  4. Sort the items according to the quantity (Descending) ");
        System.out.println("  5. Manage Diner ");
        System.out.println("  6. Exit");
        int choice = In.nextInt();
        if (choice == 1) {
            manageInventory();
        } else if (choice == 2) {
            printMenu();
        } else if (choice == 3) {
            sortItemsByQuantityASC();
        } else if (choice == 4) {
            sortItemsByQuantityDESC();
        } else if (choice == 5) {
            manageDiner();
        } else if (choice == 6) {
            break;
        } else {
            System.out.println("Pick an option 1, 2, or 3");
        }
    }

    System.out.println("Exiting...");
    }

        //Submenu for inventory management
        void manageInventory() {
        System.out.println("--- Inventory Management ---");
        while (true) {
            System.out.println("Select an option:");
            System.out.println("  1. Add new menu item");
            System.out.println("  2. remove menu item");
            System.out.println("  3. Edit existing menu item");
            System.out.println("  4. Back to main menu");
            int choice = In.nextInt();
            if (choice == 1) {
                addNewItem();
            } else if (choice == 2) {
                removeItem();
            } else if (choice == 3) {
                editItem();
            } else if (choice == 4) {
                break;
            } else {
                System.out.println("Pick an option 1, 2, 3 or 4");
            }
        }
        System.out.println("Back to main menu...");
    }

    //edit item submenu
    private void editItem() {
        System.out.println("--- Edit Item ---");

        // Display the current menu
        displayItems();

        // Get user input for the item to edit
        System.out.println("Enter the item number to edit (or 0 to cancel): ");
        int itemNumber = In.nextInt();

        // Check if a valid item number is provided
        if (itemNumber > 0 && itemNumber <= itemList.size()) {
            int index = itemNumber - 1; // Adjust for zero-based indexing

            // Get details for editing
            System.out.println("What do you want to edit?");
            System.out.println("1. Name");
            System.out.println("2. Size");
            System.out.println("3. Price");
            System.out.print("Enter your choice: ");
            int choice = In.nextInt();

            // Edit based on user choice
            Item itemToEdit = itemList.get(index);
            if (choice == 1) {
                getNewName(itemToEdit);
                
            } else if (choice == 2) {
                getNewSize(itemToEdit);
                
            } else if (choice == 3) {
                getNewPrice(itemToEdit);
            } else {
                System.out.println("Invalid item number.");
            }
        }
    }
    
    //methods to get new prices, name and size. 
    private void getNewPrice(Item itemToEdit) {
        double newPrice = readDouble("Enter new price: ");
        itemToEdit.setPrice(newPrice);
        System.out.println("Item edited successfully!");
    }
    
    //returns
    private void getNewSize(Item itemToEdit) {
        Size newSize = readSize();
        itemToEdit.setSize(newSize);
        System.out.println("Item edited successfully!");
    }

    private void getNewName(Item itemToEdit) {
        String newName = readString("Enter new name: ");
        itemToEdit.setName(newName);
        System.out.println("Item edited successfully!");
    }
        
        //diner submenu
        void manageDiner() {
            System.out.println("--- Diner Management ---");
            while (true) {
                System.out.println("Select an option:");
                System.out.println("  1. Reserve Table");
                System.out.println("  2. Open Diner");
                System.out.println("  3. Close Diner");
                System.out.println("  4. Display diner info");
                System.out.println("  5. Serve Food");
                System.out.println("  6. Exit");
                int choice = In.nextInt();
                if (choice == 1) {
                    diner.menuReserveTable();                   
                } else if (choice == 2) {
                    diner.openRestaurant();
                } else if (choice == 3) {
                    diner.closeRestaurant();
                } else if (choice == 4) {
                    dinerInfo();
                } else if (choice == 5) {
                    diner.serveFood();
                }
                else if (choice == 6) {
                    break;
                } else {
                    System.out.println("Pick an option 1, 2, 3 or 4");
                }
                
                System.out.println("Back to main menu...");
            }
        } 



    private void dinerInfo() {
        Diner DinersDelight = new Diner("Diners Delight", "London", 300, 215); //adding diner info
        System.out.println(DinersDelight);
    }
    

    private void sortItemsByQuantityASC() {
        Collections.sort(itemList, Item.QUANTITY_ASC_COMPARATOR); //calling the ascending comparator in Item class
        System.out.println("Items sorted by quantity.");
        printMenu();
    }

    private void sortItemsByQuantityDESC() {
        Collections.sort(itemList, Item.QUANTITY_DESC_COMPARATOR); //calling the descending comparator in Item class
        System.out.println("Items sorted by quantity.");
        printMenu();
    }

    //add new item sub menu
    private void addNewItem(){

        System.out.println("--- Add Item ---");
        System.out.println("What type of item would you like to add?");
        System.out.println("1. Beverage");
        System.out.println("2. Meal");
        int itemType = In.nextInt();

        Item newItem;
        if (itemType == 1) {
            newItem = addNewBeverage();
        } else if (itemType == 2) {
            newItem = addNewMeal();
        } else {
            System.out.println("Invalid choice.");
            return;
        }
        System.out.println(newItem + " - added successfully");
    }

    private Item addNewBeverage() {
       System.out.println("--- Add Beverage  ---");
        Item beverage = takeBeverageAttributes();
        itemList.add(beverage);

       return beverage;
    }

     //Getting the user inputs for adding new beverages
    private Item takeBeverageAttributes() {
        String name = readString("Please enter the name of the item"); 
        Size size = readSize();
        double price = readDouble("Please enter the price of the item");
        int quantity = readInt("Please enter the quantity of the item");
        addTopping topping = readTopping();
        Temp temp = readTemp();
        return new Beverage(name, size, price, quantity, topping, temp); //return the user inputs to add new beverage
    }
    

    private Item addNewMeal() { 
        System.out.println("--- Add Meal  ---");
        Item meal = takeMealAttributes();
        itemList.add(meal); // Add the new Meals object to the itemList
        
        return meal;  
    }

    //Getting the user inputs for adding new meals
    private Item takeMealAttributes() {
        String name = readString("Please enter the name of the meal");
        Size size = readSize();
        double price = readDouble("Please enter the price of the meal");
        int quantity = readInt("Please enter the quantity of the meal");
        Spice spiceLevel = readSpice();
        Category category = readCategory();
        return new Meal(name, size, price, quantity, spiceLevel, category);  //return the user inputs to add new meal
    }

    private Spice readSpice(){
        Spice spice = null; 
        while(spice == null)
        {
            displaySpice(); //display the spices 
            int spiceNumber = readInt("Please enter a spice leve?");
            spice = findSpice(spiceNumber);            
        }
        return spice;
    }

    private Spice findSpice(int spiceNumber) 
    {
        Spice[] allSpice = Spice.values(); 
        if (spiceNumber <= allSpice.length && spiceNumber > 0)
        {
            return allSpice[spiceNumber - 1]; //checking the input
        }

        return null;        // invalid input
    }

    private void displaySpice() 
    {        
        Spice[] allSpice = Spice.values();
        System.out.println("All Spice levels - Select a number from 1 to " + allSpice.length);
        for (int i = 0; i < allSpice.length; i++) 
        {
            System.out.println((i + 1) + " - "  + allSpice[i]);//list out the spices in correct order
        }
    }


    private Category readCategory(){
        Category category = null;
        while(category == null)
        {
            displayCategory();
            int categoryNumber = readInt("Please enter a category number?");
            category = findCategory(categoryNumber);      //get user input      
        }
        return category;
    }

    private Category findCategory(int categoryNumber) 
    {
        Category[] allCategorys = Category.values();
        if (categoryNumber <= allCategorys.length && categoryNumber > 0)
        {
            return allCategorys[categoryNumber - 1]; //checking the input
        }

        return null;        // invalid input
    }

    private void displayCategory() 
    {        
        Category[] allCategorys = Category.values();
        System.out.println("All Categorys - Select a number from 1 to " + allCategorys.length);
        for (int i = 0; i < allCategorys.length; i++) 
        {
            System.out.println((i + 1) + " - "  + allCategorys[i]); //display the categories in ordered list 
        }
    }

    private addTopping readTopping(){
        addTopping topping = null;
        while(topping == null)
        {
            displayToppings();
            int toppingNumber = readInt("Please enter a topping number?");
            topping = findTopping(toppingNumber);  //read the input          
        }
        return topping;
    }

    private addTopping findTopping(int toppingNumber) 
    {
        addTopping[] allToppings = addTopping.values();
        if (toppingNumber <= allToppings.length && toppingNumber > 0)
        {
            return allToppings[toppingNumber - 1]; //giving the topping according to the number
        }

        return null;        // invalid input
    }

    private void displayToppings() 
    {        
        addTopping[] allToppings = addTopping.values();
        System.out.println("All Toppings - Select a number from 1 to " + allToppings.length);
        for (int i = 0; i < allToppings.length; i++) 
        {
            System.out.println((i + 1) + " - "  + allToppings[i]); //display all the toppings 
        }
    }

    private Temp readTemp(){
        Temp temp = null;
        while(temp == null)
        {
            displayTemp();
            int tempNumber = readInt("Please enter a temperture number?");
            temp = findTemp(tempNumber);   //read the input         
        }
        return temp;
    }

    private Temp findTemp(int tempNumber) 
    {
        Temp[] allTemps = Temp.values();
        if (tempNumber <= allTemps.length && tempNumber > 0)
        {
            return allTemps[tempNumber - 1]; //return the out put according to the user input 
        }

        return null;        // invalid input
    }

    private void displayTemp() 
    {        
        Temp[] allTemps = Temp.values();
        System.out.println("All Temperatures - Select a number from 1 to " + allTemps.length);
        for (int i = 0; i < allTemps.length; i++) 
        {
            System.out.println((i + 1) + " - "  + allTemps[i]);  //display all temperature 
        }
    }

    private void removeItem(){
        System.out.println("--- Remove Item ---");
        displayItems();
        int removeNumber = readInt("Please enter the item number you want to remove: ");

        if (removeNumber > 0 && removeNumber <= itemList.size()) {
            int index = removeNumber - 1; // Adjust for zero-based indexing
            itemList.remove(index);
            System.out.println("Item removed successfully.");
        } else {
            System.out.println("Invalid item number.");
        }
    }

    private void printMenu() {

        int mealCount = 0;
        int bevCount = 0;

        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);

            System.out.println("\n" + item); //polymophism 

            //non polymophism 
            if (item instanceof Meal) {
                ++mealCount;
            } else if (item instanceof Beverage) {
                ++bevCount;
            }   

        }
        System.out.println("\nMeals: " + mealCount + ", Bevs: " + bevCount + "\n"); //display the number of meals and beverages in the list
    }
    
    

    private void displayItems() {
        for (int i = 0; i < itemList.size(); i++) { //itarates each item of th list 
            System.out.println((i + 1) + ". " + itemList.get(i).getName() + " - " + itemList.get(i).getSize() + " - " + itemList.get(i).getPrice());
        }
    }

    private String readString(String question) {
        System.out.println("\t" + question); //read string question
        return In.nextLine();
    }

    private Size readSize(){
        Size size = null;
        while(size == null)
        {
            displaySizes();
            int sizeNumber = readInt("Please enter a size number?");
            size = findSize(sizeNumber);      //get the size input       
        }
        return size;
    }

    private Size findSize(int sizeNumber) 
    {
        Size[] allSizes = Size.values();
        if (sizeNumber <= allSizes.length && sizeNumber > 0)
        {
            return allSizes[sizeNumber - 1]; //return the output according to the user input 
        }

        return null;        // invalid input
    }

    private void displaySizes() 
    {        
        Size[] allSizes = Size.values();
        System.out.println("All Sizes - Select a number from 1 to " + allSizes.length);
        for (int i = 0; i < allSizes.length; i++)  
        {
            System.out.println((i + 1) + " - "  + allSizes[i]);
        }
    }

    private double readDouble(String question) { //read double inputs 
        System.out.println("\t" + question); 
        return In.nextDouble();
    }

    private int readInt(String question) { //read int inputs
        System.out.println("\t" + question);
        return In.nextInt();
    }

}
