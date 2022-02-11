package gov.iti.jets.presentation.util;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StageCoordinator {

    private static final StageCoordinator stageCoordinator = new StageCoordinator();
    private final Map<String,Scene> sceneMap = new HashMap<>();
    private Stage primaryStage;


    private StageCoordinator(){

    }
    public  static StageCoordinator getInstance(){
        return stageCoordinator;
    }
    public void initStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public void switchToAnnouncementScene(){
        prepareScene("announcement","/views/announcement/AnnouncementView.fxml");
    }
    public void switchToStatisticsScene(){
        prepareScene("statistics","/views/statistics/StatisticsView.fxml");
    }

    private void prepareScene(String sceneName, String fxmlLocation) {
        if (sceneMap.get(sceneName) == null) {
            try {
                Pane root = FXMLLoader.load(getClass().getResource(fxmlLocation));
                Scene scene = new Scene(root);
                sceneMap.put(sceneName, scene);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        primaryStage.setScene(sceneMap.get(sceneName));
    }

    
}   