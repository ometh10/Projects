import javafx.collections.ObservableList;

public class InventoryManagementController {
    private final InventoryManagementModel model;

    public InventoryManagementController(InventoryManagementModel model) {
        this.model = model;
    }

    public void addItem(Item i) {
        this.model.addItem(i);
    }

    public void editItem(Item i, int index) {
        this.model.editItem(i, index);
    }

    public void removeItem(int index) {
        this.model.removeItem(index);
    }

    public void removeAll() {
        model.removeAll();
    }

    public ObservableList<Item> retrieveItemData() {
        return model.itemProperty();
    }

    public void sortItemsByQuantityAsc() {
        model.sortItemsByQuantity(true);
    }
    
    public void sortItemsByQuantityDesc() {
        model.sortItemsByQuantity(false);
    }

    public ObservableList<Item> filteredItemProperty() {
        return model.filteredItemProperty();
    }
    
}
