import java.util.Comparator;


public class Item 
{
    private String name;
    private Size size;
    private double price;
    private int quantity;

    //Sort by quantity
    public final static Comparator<Item> QUANTITY_ASC_COMPARATOR = Comparator.comparing(Item :: getQuantity); //comparator to sort the list ascending order
    public final static Comparator<Item> QUANTITY_DESC_COMPARATOR = Comparator.comparing(Item :: getQuantity).reversed(); //comparator to sort the list descending order 


    public Item(String name, Size size, double price, int quantity) 
    {
        this.name = name;
        this.size = size;
        this.price = price;
        this.quantity = quantity;
    }
    
    //getter methods & setter methods
    public String getName() 
    {
        return name;
    }

    public Size getSize() 
    {
        return size;
    }

    public double getPrice() 
    {
        return price;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public void setSize(Size Newsize) {
        this.size = Newsize;
    }

    public void setPrice(double price) 
    {
        this.price = price;
    }

    public void setQuantity(int quantity) 
    {
        this.quantity = quantity; 
    }

    //toString method
    @Override
    public String toString() {
        return "Item [name=" + name + ", size=" + size + ", price=" + price + ", quantity=" + quantity + "]";
    }


}
