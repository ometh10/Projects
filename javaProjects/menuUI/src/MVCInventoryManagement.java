import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MVCInventoryManagement extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Inventory Management");
        InventoryManagementModel model = new InventoryManagementModel();
        InventoryManagementController controller = new InventoryManagementController(model);
        InventoryManagementView view = new InventoryManagementView(controller, model, primaryStage);

        Scene scene = new Scene(view.asParent(), 1135, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
