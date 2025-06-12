import javafx.scene.control.TableColumn;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InventoryManagementView {
    private VBox view;
    private TableView<Item> itemView;
    private Button addMealBtn;
    private Button addBeverageBtn;
    private Button editItemBtn;
    private Button removeItemBtn;
    private Button removeAllBtn;
    private Button searchBtn;
    private Button sortByBtn;

    private InventoryManagementController controller;
    private InventoryManagementModel model;
    private Stage primaryStage;

    public InventoryManagementView(InventoryManagementController controller, InventoryManagementModel model, Stage primaryStage) {
        this.controller = controller;
        this.model = model;
        this.primaryStage = primaryStage;

        createAndConfigurePane();
        createAndLayoutControls();
        updateControllerFromListeners();
        observeModelAndUpdateControls();
    }

    public Parent asParent() {
        return view;
    }

    private void observeModelAndUpdateControls() {
        // Not needed here
    }

    private void updateControllerFromListeners() {
        // Not needed here
    }

    private void createAndLayoutControls() {
        //table view
        this.itemView = new TableView<>();

        //item type column 
        TableColumn<Item, String> itemTypeCol = new TableColumn<>("Item Type");
        itemTypeCol.setMinWidth(100.0);
        itemTypeCol.setCellValueFactory(cellData -> cellData.getValue().itemTypeProperty());

        //name column 
        TableColumn<Item, String> nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(200.0);
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        //size column 
        TableColumn<Item, Size> sizeCol = new TableColumn<>("Size");
        sizeCol.setMinWidth(130.0);
        sizeCol.setCellValueFactory(cellData -> cellData.getValue().sizeProperty());

        //price column 
        TableColumn<Item, Number> priceCol = new TableColumn<>("Price");
        priceCol.setMinWidth(100.0);
        priceCol.setCellValueFactory(cellData -> cellData.getValue().priceProperty());

        //quantity column 
        TableColumn<Item, Number> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setMinWidth(100.0);
        quantityCol.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());

        //Spice column 
        TableColumn<Item, Spice> spiceLevelCol = new TableColumn<>("Spice");
        spiceLevelCol.setMinWidth(125.0);
        spiceLevelCol.setCellValueFactory(cellData -> {
            Item item = cellData.getValue();
            if (item instanceof Meal) {
                return ((Meal) item).spiceLevelProperty();
            } else {
                return null;
            }
        });

        //Category Column 
        TableColumn<Item, Category> categoryCol = new TableColumn<>("Category");
        categoryCol.setMinWidth(125.0);
        categoryCol.setCellValueFactory(cellData -> {
            Item item = cellData.getValue();
            if (item instanceof Meal) {
                return ((Meal) item).categoryProperty();
            } else {
                return null;
            }
        });

        //Topping Column 
        TableColumn<Item, addTopping> toppingCol = new TableColumn<>("Topping");
        toppingCol.setMinWidth(125.0);
        toppingCol.setCellValueFactory(cellData -> {
            Item item = cellData.getValue();
            if (item instanceof Beverage) {
                return ((Beverage) item).bevToppingProperty();
            } else {
                return null;
            }
        });

        //Temp coulmn
        TableColumn<Item, Temp> TempCol = new TableColumn<>("Temperature");
        TempCol.setMinWidth(125.0);
        TempCol.setCellValueFactory(cellData -> {
            Item item = cellData.getValue();
            if (item instanceof Beverage) {
                return ((Beverage) item).tempProperty();
            } else {
                return null;
            }
        });

    
        //add columns
        this.itemView.getColumns().addAll(itemTypeCol, nameCol, sizeCol, priceCol, quantityCol, spiceLevelCol,
                                            categoryCol,toppingCol,TempCol);
        this.itemView.setItems(model.itemProperty());

        //setup the buttons
        this.addMealBtn = new Button("Add Meal");
        this.addMealBtn.setOnAction(event -> createAddMealForm());

        this.addBeverageBtn = new Button("Add Beverage");
        this.addBeverageBtn.setOnAction(event -> createAddBeverageForm());

        this.editItemBtn = new Button("Edit Item");
        this.editItemBtn.setOnAction(event -> {
            int index = this.itemView.getSelectionModel().getSelectedIndex();
            if (index != -1) {
                createEditItemsForm(index);
            }
        });

        this.removeItemBtn = new Button("Remove item");
        removeItemBtn.setOnAction(event -> {
            int index = this.itemView.getSelectionModel().getSelectedIndex();
            if (index != -1) {
                this.controller.removeItem(index);
            }
        });

        this.removeAllBtn = new Button("Remove All");
        removeAllBtn.setOnAction(event -> {
            controller.removeAll();
        });

        this.searchBtn = new Button("Search");
        searchBtn.setOnAction(event -> createSearchForm());

        this.sortByBtn = new Button("Sort By");
        sortByBtn.setOnAction(event -> createSortForm());

        HBox buttonRow = new HBox(5, addMealBtn, addBeverageBtn, editItemBtn, removeItemBtn, removeAllBtn, searchBtn, sortByBtn);
        view.getChildren().addAll(this.itemView, buttonRow);

    }


    private void createAndConfigurePane() {
        view = new VBox(5);
        view.setAlignment(Pos.CENTER);
    }


    public void createSortForm() {
        Stage sortByStage = new Stage();
        sortByStage.setTitle("Sort Items");
        sortByStage.initOwner(primaryStage);
        sortByStage.initModality(Modality.APPLICATION_MODAL);

        ToggleGroup sortByToggleGroup = new ToggleGroup();
        RadioButton sortByAscBtn = new RadioButton("Ascending of quantity");
        sortByAscBtn.setToggleGroup(sortByToggleGroup);

        RadioButton sortByDescBtn = new RadioButton("Descending of quantity");
        sortByDescBtn.setToggleGroup(sortByToggleGroup);    
            
        HBox sortByRadioBtnRow = new HBox(10, sortByAscBtn, sortByDescBtn);
        sortByRadioBtnRow.setAlignment(Pos.CENTER);

        TableView<Item> sortResults = new TableView<>();
        // Clone columns setup from primary TableView
        for (TableColumn<Item, ?> column : itemView.getColumns()) {
            cloneTableColumn(column, sortResults);
        }

        // Get items data
        ObservableList<Item> itemsData = controller.retrieveItemData();
        sortResults.setItems(itemsData);

        sortByAscBtn.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                controller.sortItemsByQuantityAsc();
                sortResults.setItems(controller.filteredItemProperty()); // since items are now sorted, update the view
                //sortResults.refresh();
            }
        });

        sortByDescBtn.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                controller.sortItemsByQuantityDesc();
                sortResults.setItems(controller.filteredItemProperty()); // since items are now sorted, update the view
                sortResults.refresh();
            }
        });

        VBox root = new VBox(10, sortByRadioBtnRow, sortResults);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 610, 400);
        sortByStage.setScene(scene);
        sortByStage.show();
    }
 

    private void createSearchForm() {
        Stage searchStage = new Stage();
        searchStage.setTitle("Search Items");
        searchStage.initOwner(primaryStage);
        searchStage.initModality(Modality.APPLICATION_MODAL);

        // Separate search fields for name, type, and size
        TextField nameSearchField = new TextField();
        nameSearchField.setPromptText("Search by name...");

        TextField typeSearchField = new TextField();
        typeSearchField.setPromptText("Search by item type...");

        TextField sizeSearchField = new TextField();
        sizeSearchField.setPromptText("Search by size...");

        HBox searchRow = new HBox(10);
        searchRow.getChildren().addAll(
            new Label("Name: "), nameSearchField,
            new Label("Type: "), typeSearchField,
            new Label("Size: "), sizeSearchField
        );
        searchRow.setAlignment(Pos.CENTER);

        // TableView<Item> searchResults = new TableView<>();
        // searchResults.getColumns().addAll(itemView.getColumns());

        TableView<Item> searchResults = getSearchResults();

        // Dynamic filtering of the TableView based on search input

        ObservableList<Item> itemsData = getItemsData();
        searchResults.setItems(itemsData);

        nameSearchField.textProperty().addListener((obs, oldVal, newVal) ->
            updateFilteredItems(searchResults, itemsData, nameSearchField, typeSearchField, sizeSearchField));
        typeSearchField.textProperty().addListener((obs, oldVal, newVal) ->
            updateFilteredItems(searchResults, itemsData, nameSearchField, typeSearchField, sizeSearchField));
        sizeSearchField.textProperty().addListener((obs, oldVal, newVal) ->
            updateFilteredItems(searchResults, itemsData, nameSearchField, typeSearchField, sizeSearchField));

        VBox root = new VBox(10, searchRow, searchResults);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 700, 400);
        searchStage.setScene(scene);
        searchStage.show();
    }

    private TableView<Item> getSearchResults() {
        TableView<Item> searchResults = new TableView<>();
        // Clone columns setup from primary TableView
        for (TableColumn<Item, ?> column : itemView.getColumns()) {
            cloneTableColumn(column, searchResults);
        }
        return searchResults;
    }

    private ObservableList<Item> getItemsData() {
        ObservableList<Item> itemsData = controller.retrieveItemData();
        return itemsData;
    }

    private void updateFilteredItems(TableView<Item> tableView, ObservableList<Item> itemsData,
                                        TextField nameField, TextField typeField, TextField sizeField) {
        tableView.setItems(itemsData.filtered(item -> {
            boolean matchesName = item.getName().toLowerCase().contains(nameField.getText().toLowerCase());
            boolean matchesItemType = item.getItemType().toString().toLowerCase().contains(typeField.getText().toLowerCase());
            boolean matchesSize = item.getSize().toString().toLowerCase().contains(sizeField.getText().toLowerCase());
            return matchesName && matchesItemType && matchesSize;
        }));
    }

    
    // Utility method for type-safe copying of TableColumn properties
    private <S, T> void cloneTableColumn(TableColumn<S, T> original, TableView<S> tableView) {
        TableColumn<S, T> newColumn = new TableColumn<>(original.getText());
        newColumn.setCellValueFactory(original.getCellValueFactory());
        tableView.getColumns().add(newColumn);
    }
    

    private void createAddMealForm() {
        Stage stage = new Stage();
        stage.setTitle("Add Meal");
        stage.initOwner(primaryStage);
        stage.initModality(Modality.APPLICATION_MODAL);

        TextField nameField = new TextField();
        extracted(nameField);
        HBox nameRow = getNameRow(nameField);

        TextField priceField = new TextField();
        priceField.setPromptText("Enter price");
        configTextFieldForDoubles(priceField);
        HBox priceRow = new HBox(5, new Label("Price "), priceField);
        priceRow.setAlignment(Pos.CENTER);

        TextField quantityField = new TextField();
        quantityField.setPromptText("Enter quantity");
        configTextFieldForInts(quantityField);
        HBox quantityRow = new HBox(5, new Label("Quantity: "), quantityField);
        quantityRow.setAlignment(Pos.CENTER);

        // Toggle group to set size
        ToggleGroup sizeToggleGroup = new ToggleGroup();
        RadioButton smallBtn = new RadioButton("Small");
        smallBtn.setToggleGroup(sizeToggleGroup);

        RadioButton mediumBtn = new RadioButton("Medium");
        mediumBtn.setToggleGroup(sizeToggleGroup);

        RadioButton largeBtn = new RadioButton("Large");
        largeBtn.setToggleGroup(sizeToggleGroup);

        HBox radioBtnRow = sizeRadioBtnRow(smallBtn, mediumBtn, largeBtn);

        // Toggle group to set spice
        ToggleGroup spiceToggleGroup = new ToggleGroup();
        RadioButton mildBtn = new RadioButton("Mild");
        mildBtn.setToggleGroup(spiceToggleGroup);

        RadioButton mediumSpiceBtn = new RadioButton("Medium");
        mediumSpiceBtn.setToggleGroup(spiceToggleGroup);

        RadioButton hotBtn = new RadioButton("Hot");
        hotBtn.setToggleGroup(spiceToggleGroup);

        HBox spiceRadioBtnRow = new HBox(5, mildBtn, mediumSpiceBtn, hotBtn);
        spiceRadioBtnRow.setAlignment(Pos.CENTER);
        
        // Toggle group to set category
        ToggleGroup categoryToggleGroup = new ToggleGroup();
        RadioButton breakfastBtn = new RadioButton("Breakfast");
        breakfastBtn.setToggleGroup(categoryToggleGroup);

        RadioButton lunchBtn = new RadioButton("Lunch");
        lunchBtn.setToggleGroup(categoryToggleGroup);

        RadioButton teaTimeBtn = new RadioButton("Tea-Time");
        teaTimeBtn.setToggleGroup(categoryToggleGroup);

        RadioButton dinnerBtn = new RadioButton("Dinner");
        dinnerBtn.setToggleGroup(categoryToggleGroup);

        HBox categoryRadioBtnRow = new HBox(5, breakfastBtn, lunchBtn, teaTimeBtn, dinnerBtn);
        categoryRadioBtnRow.setAlignment(Pos.CENTER);

        Button submitBtn = new Button("Submit");
        submitBtn.setOnAction(event -> {
            String text = nameField.getText().trim();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());

            Size size; 
            Spice spice;
            Category category;

            size = sizeSelection(smallBtn, mediumBtn);
            //Spice
            if (mildBtn.isSelected()) {
                spice = Spice.MILD;
            } else if (mediumSpiceBtn.isSelected()) {
                spice = Spice.MEDIUM;
            } else {
                spice = Spice.HOT;
            }
            //category
            if (breakfastBtn.isSelected()) {
                category = Category.BREAKFAST;
            } else if (lunchBtn.isSelected()) {
                category = Category.LUNCH;
            } else if (teaTimeBtn.isSelected()) {
                category = Category.TEA_TIME;
            } else {
                category = Category.DINNER;
            }

            if (!text.isEmpty()) {
                Meal meal = new Meal(nameField.getText(), size, price, quantity, spice, category, "Meal");
                controller.addItem(meal); // Adding a meal through the conroller
                stage.close();
            }
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(event -> stage.close());

        HBox buttonRow = new HBox(5, submitBtn, cancelBtn);
        buttonRow.setAlignment(Pos.CENTER);

        VBox root = new VBox(5, nameRow, radioBtnRow, priceRow, quantityRow, spiceRadioBtnRow, categoryRadioBtnRow, buttonRow);
        root.setAlignment(Pos.CENTER);

        Scene helloScene = new Scene(root, 300, 300);

        stage.setScene(helloScene);
        stage.show();

    }

    private Size sizeSelection(RadioButton smallBtn, RadioButton mediumBtn) {
        Size size;
        if (smallBtn.isSelected()) {
            size = Size.SMALL;
        } else if (mediumBtn.isSelected()) {
            size = Size.MEDIUM;
        } else {
            size = Size.LARGE;
        }
        return size;
    }

    private HBox sizeRadioBtnRow(RadioButton smallBtn, RadioButton mediumBtn, RadioButton largeBtn) {
        HBox radioBtnRow = new HBox(5, smallBtn, mediumBtn, largeBtn);
        radioBtnRow.setAlignment(Pos.CENTER);
        return radioBtnRow;
    }

    private void extracted(TextField nameField) {
        nameField.setPromptText("Enter name");
    }

    private HBox getNameRow(TextField nameField) {
        HBox nameRow = new HBox(5, new Label("Name: "), nameField);
        nameRow.setAlignment(Pos.CENTER);
        return nameRow;
    }

    private void createAddBeverageForm() {
        Stage stage = new Stage();
        stage.setTitle("Add Beverage");
        stage.initOwner(primaryStage);
        stage.initModality(Modality.APPLICATION_MODAL);

        TextField nameField = new TextField();
        extracted(nameField);
        HBox nameRow = getNameRow(nameField);

        TextField priceField = new TextField();
        priceField.setPromptText("Enter price");
        configTextFieldForDoubles(priceField);
        HBox priceRow = new HBox(5, new Label("Price "), priceField);
        priceRow.setAlignment(Pos.CENTER);

        TextField quantityField = new TextField();
        quantityField.setPromptText("Enter quantity");
        configTextFieldForInts(quantityField);
        HBox quantityRow = new HBox(5, new Label("Quantity: "), quantityField);
        quantityRow.setAlignment(Pos.CENTER);

        // Toggle group to set size
        ToggleGroup sizeToggleGroup = new ToggleGroup();
        RadioButton smallBtn = new RadioButton("Small");
        smallBtn.setToggleGroup(sizeToggleGroup);

        RadioButton mediumBtn = new RadioButton("Medium");
        mediumBtn.setToggleGroup(sizeToggleGroup);

        RadioButton largeBtn = new RadioButton("Large");
        largeBtn.setToggleGroup(sizeToggleGroup);

        HBox radioBtnRow = sizeRadioBtnRow(smallBtn, mediumBtn, largeBtn);

        // Toggle group to set addTopping
        ToggleGroup addToppingToggleGroup = new ToggleGroup();

        RadioButton iceCreamBtn = new RadioButton("Ice Cream");
        iceCreamBtn.setToggleGroup(addToppingToggleGroup);

        RadioButton bobaPearlsBtn = new RadioButton("Boba Pearls");
        bobaPearlsBtn.setToggleGroup(addToppingToggleGroup);

        RadioButton fruitBtn = new RadioButton("Fruit");
        fruitBtn.setToggleGroup(addToppingToggleGroup);

        RadioButton iceCubesBtn = new RadioButton("Ice Cubes");
        iceCubesBtn.setToggleGroup(addToppingToggleGroup);

        RadioButton noneBtn = new RadioButton("None");
        noneBtn.setToggleGroup(addToppingToggleGroup);

        HBox toppingRadioBtnRow = new HBox(5, iceCreamBtn, bobaPearlsBtn, fruitBtn, iceCubesBtn, noneBtn);
        toppingRadioBtnRow.setAlignment(Pos.CENTER);
        
        // Toggle group to set temp
        ToggleGroup tmpToggleGroup = new ToggleGroup();
        RadioButton chilledBtn = new RadioButton("Chilled");
        chilledBtn.setToggleGroup(tmpToggleGroup);

        RadioButton normalBtn = new RadioButton("Normal");
        normalBtn.setToggleGroup(tmpToggleGroup);

        RadioButton hotTempBtn = new RadioButton("Hot");
        hotTempBtn.setToggleGroup(tmpToggleGroup);

        HBox tempRadioBtnRow = new HBox(5, chilledBtn, normalBtn, hotTempBtn);
        tempRadioBtnRow.setAlignment(Pos.CENTER);

        Button submitBtn = new Button("Submit");
        submitBtn.setOnAction(event -> {
            String text = nameField.getText().trim();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());

            Size size; // Defaulting to medium; adjust as necessary
            addTopping bevTopping;
            Temp temp;

            size = sizeSelection(smallBtn, mediumBtn);
            //bevTopping
            if (iceCreamBtn.isSelected()) {
                bevTopping = addTopping.ICE_CREAM;
            } else if (bobaPearlsBtn.isSelected()) {
                bevTopping = addTopping.BOBA_PEARLS;    
            } else if (fruitBtn.isSelected()) {
                bevTopping = addTopping.FRUIT;
            } else if (iceCubesBtn.isSelected()) {
                bevTopping = addTopping.ICE_CUBES;
            } else {
                bevTopping = addTopping.NONE;
            }
            //temp
            if (chilledBtn.isSelected()) {
                temp = Temp.CHILLED;
            } else if (normalBtn.isSelected()) {
                temp = Temp.NORMAL;
            } else {
                temp = Temp.HOT;
            }

            if (!text.isEmpty()) {
                Beverage beverage = new Beverage(nameField.getText(), size, price, quantity, bevTopping, temp, "Beverage");
                controller.addItem(beverage); // Adding a beverage through the controller
                stage.close();
            }
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(event -> stage.close());

        HBox buttonRow = new HBox(5, submitBtn, cancelBtn);
        buttonRow.setAlignment(Pos.CENTER);

        VBox root = new VBox(5, nameRow, radioBtnRow, priceRow, quantityRow, toppingRadioBtnRow, tempRadioBtnRow, buttonRow);
        root.setAlignment(Pos.CENTER);

        Scene helloScene = new Scene(root, 450, 300);

        stage.setScene(helloScene);
        stage.show();
    }

    private void configTextFieldForDoubles(TextField field) {
        field.setTextFormatter(new TextFormatter<Double>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty() || newText.matches("-?((\\d*)|(\\d+\\.\\d*))")) {
                return change;
            }
            return null;
            }));
    }
    

    private void configTextFieldForInts(TextField field) {
        field.setTextFormatter(new TextFormatter<Integer>((Change c) -> {
            // "-?\\d*" is called a regular expression. For those who are curious:
            //
            // - The "-?" indicates that the minus sign is optionally present (we need to
            // allow for negative integers too)
            // - "\\d" is a digit character, which matches any digit from 0 to 9.
            // - The following "*" is a quantifier that means "zero or more occurrences".
            // - Therefore, \\d* matches a sequence of zero or more digits.
            if (c.getControlNewText().matches("-?\\d*")) {
                return c;
            }
            return null;
            }));
    }

    private void createEditItemsForm(int index) {
        Item selectedItem = this.itemView.getItems().get(index);
        if (selectedItem instanceof Meal) {
            createEditMealForm((Meal) selectedItem, index);
        } else if (selectedItem instanceof Beverage) {
            createEditBeverageForm((Beverage) selectedItem, index);
        }
    }

    private void createEditMealForm(Meal meal, int index) {
        Stage stage = new Stage();
        stage.setTitle("Edit Meal");
        stage.initOwner(primaryStage);
        stage.initModality(Modality.APPLICATION_MODAL);

        TextField nameField = new TextField();
        String oldName = model.itemProperty().get(index).getName();
        nameField.setPromptText("Enter new name");
        nameField.setText(oldName);
        HBox nameRow = new HBox(5, new Label("Name: "), nameField);
        nameRow.setAlignment(Pos.CENTER);

        //
        
        TextField priceField = new TextField();
        Double oldPrice = model.itemProperty().get(index).getPrice();
        priceField.setPromptText("Enter price");
        configTextFieldForDoubles(priceField); // Configuring the TextField to accept double
        priceField.setText(oldPrice.toString()); 
        HBox priceRow = new HBox(5, new Label("Price "), priceField);
        priceRow.setAlignment(Pos.CENTER);
    
        TextField quantityField = new TextField();
        Integer oldQuantity = model.itemProperty().get(index).getQuantity();
        quantityField.setPromptText("Enter quantity");
        configTextFieldForInts(quantityField);  // Configuring the TextField to accept integer
        quantityField.setText(Integer.toString(oldQuantity));
        HBox quantityRow = new HBox(5, new Label("Quantity: "), quantityField);
        quantityRow.setAlignment(Pos.CENTER);
    

        // Toggle group to set size
        ToggleGroup sizeToggleGroup = new ToggleGroup();
        RadioButton smallBtn = new RadioButton("Small");
        smallBtn.setToggleGroup(sizeToggleGroup);

        RadioButton mediumBtn = new RadioButton("Medium");
        mediumBtn.setToggleGroup(sizeToggleGroup);

        RadioButton largeBtn = new RadioButton("Large");
        largeBtn.setToggleGroup(sizeToggleGroup);

        HBox radioBtnRow = new HBox(5, smallBtn, mediumBtn, largeBtn);
        radioBtnRow.setAlignment(Pos.CENTER);

        Button updateBtn = new Button("Update");
        updateBtn.setOnAction(event -> {
            String text = nameField.getText().trim();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());

            Size oldSize = model.itemProperty().get(index).getSize();
            Size newSize;

            newSize = sizeSelection(smallBtn, mediumBtn);

            boolean changedSize = oldSize != newSize;
            boolean newTextIsNonEmptyAndDiffers = !text.isEmpty() && !text.equals(oldName);
            if (newTextIsNonEmptyAndDiffers || changedSize) {
                // Retrieve the existing item
                Item selectedItem = model.itemProperty().get(index);
        
                // Update the item's properties with the new values
                selectedItem.setName(text);
                selectedItem.setPrice(price);
                selectedItem.setQuantity(quantity);
                selectedItem.setSize(newSize);
        
                // Call the controller to perform the edit operation
                controller.editItem(selectedItem, index);
            

                stage.close();
            }
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(event -> stage.close());

        HBox buttonRow = new HBox(5, updateBtn, cancelBtn);
        buttonRow.setAlignment(Pos.CENTER);

        VBox root = new VBox(5, nameRow, radioBtnRow, priceRow, quantityRow, buttonRow);
        root.setAlignment(Pos.CENTER);

        Scene helloScene = new Scene(root, 300, 300);

        stage.setScene(helloScene);
        stage.show();
    }
    
    private void createEditBeverageForm(Beverage beverage, int index) {
        // Similar setup to createAddBeverageForm, but load the existing Beverage data
        Stage stage = new Stage();
        stage.setTitle("Edit beverages");
        stage.initOwner(primaryStage);
        stage.initModality(Modality.APPLICATION_MODAL);
    
        TextField nameField = new TextField();
        String oldName = model.itemProperty().get(index).getName();
        nameField.setPromptText("Enter new name");
        nameField.setText(oldName);
        HBox nameRow = new HBox(5, new Label("Name: "), nameField);
        nameRow.setAlignment(Pos.CENTER);

        //
        
        TextField priceField = new TextField();
        Double oldPrice = model.itemProperty().get(index).getPrice();
        priceField.setPromptText("Enter price");
        configTextFieldForDoubles(priceField); // Configuring the TextField to accept double
        priceField.setText(oldPrice.toString()); 
        HBox priceRow = new HBox(5, new Label("Price "), priceField);
        priceRow.setAlignment(Pos.CENTER);
    
        TextField quantityField = new TextField();
        Integer oldQuantity = model.itemProperty().get(index).getQuantity();
        quantityField.setPromptText("Enter quantity");
        configTextFieldForInts(quantityField);  // Configuring the TextField to accept integer
        quantityField.setText(Integer.toString(oldQuantity));
        HBox quantityRow = new HBox(5, new Label("Quantity: "), quantityField);
        quantityRow.setAlignment(Pos.CENTER);
    

        // Toggle group to set size
        ToggleGroup sizeToggleGroup = new ToggleGroup();
        RadioButton smallBtn = new RadioButton("Small");
        smallBtn.setToggleGroup(sizeToggleGroup);

        RadioButton mediumBtn = new RadioButton("Medium");
        mediumBtn.setToggleGroup(sizeToggleGroup);

        RadioButton largeBtn = new RadioButton("Large");
        largeBtn.setToggleGroup(sizeToggleGroup);

        HBox radioBtnRow = new HBox(5, smallBtn, mediumBtn, largeBtn);
        radioBtnRow.setAlignment(Pos.CENTER);

        Button updateBtn = new Button("Update");
        updateBtn.setOnAction(event -> {
            String text = nameField.getText().trim();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());

            Size oldSize = model.itemProperty().get(index).getSize();
            Size newSize;

            newSize = sizeSelection(smallBtn, mediumBtn);

            boolean changedSize = oldSize != newSize;
            boolean newTextIsNonEmptyAndDiffers = !text.isEmpty() && !text.equals(oldName);
            if (newTextIsNonEmptyAndDiffers || changedSize) {
                // Retrieve the existing item
                Item selectedItem = model.itemProperty().get(index);
        
                // Update the item's properties with the new values
                selectedItem.setName(text);
                selectedItem.setPrice(price);
                selectedItem.setQuantity(quantity);
                selectedItem.setSize(newSize);
        
                // Call the controller to perform the edit operation
                controller.editItem(selectedItem, index);
            

                stage.close();
            }
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(event -> stage.close());

        HBox buttonRow = new HBox(5, updateBtn, cancelBtn);
        buttonRow.setAlignment(Pos.CENTER);

        VBox root = new VBox(5, nameRow, radioBtnRow, priceRow, quantityRow, buttonRow);
        root.setAlignment(Pos.CENTER);

        Scene helloScene = new Scene(root, 300, 300);

        stage.setScene(helloScene);
        
        stage.show();
    }
}
