enum addTopping
{ //constants for toppings
    ICE_CREAM,
    BOBA_PEARLS,
    FRUIT,
    ICE_CUBES,
    NONE;
}

enum Temp
{ //constants for temp
    CHILLED,
    NORMAL,
    HOT;
}

class Beverage extends Item //child class of Item  
{
    private addTopping bevTopping;
    private Temp temp;

    public Beverage(String name, Size size, double price, int quantity, addTopping bevTopping, Temp temp) {
        super(name, size, price, quantity);
        this.bevTopping = bevTopping;
        this.temp = temp;
       
    }

    public boolean hasTopping() 
    {
        return bevTopping != null; // Check if bevTopping is not null
    }

    //getter methods 
    public addTopping getBevTopping() {
        return bevTopping;
    }

    public Temp getTemp() {
        return temp;
    }

    @Override
    public String toString() {
        return "----Beverages---- \nName: " + getName() + "\nSize: " + getSize() + "\nPrice: " + getPrice()
        + "\nQuantity: " + getQuantity() + "\nbevTopping: "+  bevTopping + "\nTemperature: " + temp + "\nhasTopping:" + hasTopping();
    }

}