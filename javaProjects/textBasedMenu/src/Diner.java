public class Diner implements Restaurant
{
    private String name;
    private String location;
    private int capacity;
    public int RestoID;

    public Diner(String name, String location, int capacity, int restoID) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.RestoID = restoID;
    }

    //Abstract methods from interface overidden
    @Override
    public void openRestaurant() 
    {
        System.out.println(getName() + " is now open!\n");
    }

    @Override
    public void closeRestaurant() 
    {
        System.out.println(getName() + " is now closed!\n");
    }

    @Override
    public void serveFood()
    {
        System.out.println(getName() + " is serving food!\n");
    }
    
    //normal reservation
    public void reserveTable(int numberOfGuests) {
        if (numberOfGuests <= getCapacity()) {
            System.out.println("\nA table for " + numberOfGuests + " guests has been reserved at " + getName() + ".");
        } else {
            System.out.println("\nUnable to reserve a table for " + numberOfGuests + " guests due to capacity limit.");
        }
    }

    //Overloading - reservation with special request
    public void reserveTable(int numberOfGuests, String specialRequest) {
        if (numberOfGuests <= getCapacity()) {
            System.out.println("A table for " + numberOfGuests + " guests has been reserved at " + getName() +
                    ". Special request: " + specialRequest);
        } else {
            System.out.println("Unable to reserve a table for " + numberOfGuests +
                    " guests due to capacity limit.");
        }
    }

    //conditional loop to determine which reserveTable function to call
    public void menuReserveTable(){
             
        int numGuests = getNumGuests();
            boolean hasSpecialRequest = getHasSpecialRequest();
            if (hasSpecialRequest) {
                System.out.println("Enter your special request:");
                String specialRequest = In.nextLine();
                reserveTable(numGuests, specialRequest);
            } else {
                reserveTable(numGuests);
            }
    }

    //If reservation has special request
    private boolean getHasSpecialRequest() {
        System.out.println("Do you have any special requests? (y/n)");
        String input = In.nextLine();
        boolean hasSpecialRequest = input.equalsIgnoreCase("y");
        return hasSpecialRequest;
    }

    //Takes int value for reservations
    private int getNumGuests() {
        System.out.println("Enter the number of guests:");
            int numGuests = In.nextInt();
        return numGuests;
    }

    //getter methods 
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRestoID() {
        return RestoID;
    }

    @Override
    public String toString() {
        return "DinersDelight \nRestoID: " + RestoID + "\nLocation: " + getLocation()
                + "\nCapacity: " + getCapacity();
    }
}
