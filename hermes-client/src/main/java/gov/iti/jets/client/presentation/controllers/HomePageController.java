package gov.iti.jets.client.presentation.controllers;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import common.business.services.Client;
import gov.iti.jets.client.business.services.ClientImpl;
import gov.iti.jets.client.presentation.models.UserModel;
import gov.iti.jets.client.presentation.util.ModelsFactory;
import gov.iti.jets.client.presentation.util.StageCoordinator;
import gov.iti.jets.client.presistance.network.RMIConnection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class HomePageController implements Initializable {

  private final StageCoordinator stageCoordinator = StageCoordinator.INSTANCE;

  Font font = Font.loadFont("file:resources/fonts/TenaliRamakrishna-Regular.ttf", 45);
  private Client clientImpl;
  @FXML
  private BorderPane mainBorderPane;
  @FXML
  private TextField messageTextField;
  @FXML
  private TextField searchTextField;
  @FXML
  private Button sendButton;
  @FXML
  private ImageView profileImageView;
  @FXML
  private ImageView logoutImageView;
  @FXML
  private VBox messagesVerticalBox;
  @FXML
  private ImageView contactImageView;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

      try {
        clientImpl =new ClientImpl();
        //RMIConnection.INSTANCE.getConnectedClients().login(clientImpl);
      } catch (RemoteException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }


    // Sending message to vbox in chat box to a specific contact
    sendButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        String messageToSend = messageTextField.getText();
        if (!messageToSend.isEmpty()) {

          ImageView imageView = new ImageView(contactImageView.getImage());
          imageView.setFitWidth(18);
          imageView.setFitHeight(18);

          HBox messageHorizontalBox = new HBox();
          messageHorizontalBox.setAlignment(Pos.CENTER_RIGHT);
          messageHorizontalBox.setPadding(new Insets(5, 5, 5, 10));

          Text textMessage = new Text(messageToSend);
          TextFlow messageTextFlow = new TextFlow(textMessage);
          messageTextFlow.setStyle("-fx-color: rgb(255,255,255); " + "-fx-background-color:  #685490; "
              + " -fx-background-radius: 20px; ");
          messageTextFlow.setPadding(new Insets(5, 10, 5, 10));
          textMessage.setFill(Color.color(0.934, 0.945, 0.996));

          messageHorizontalBox.getChildren().add(messageTextFlow);
          messageHorizontalBox.getChildren().add(imageView);
          messagesVerticalBox.getChildren().add(messageHorizontalBox);

          // SEND MESSAGE TO SPECIFIC USER

          messageTextField.clear();

        }
      }
    });

  }

  @FXML
  void onSearchTextFieldClick(ActionEvent event) {

  }

  @FXML
  void onProfileClicked(MouseEvent mouseEvent) {
    UserModel userModel1 = ModelsFactory.INSTANCE.getUserModel();
    userModel1.setEmail("hashemalhariry33@gmail.com");
    userModel1.setPhoneNumber("01149056691");
    userModel1.setUserName("HASHEM");

    //RMIConnection.INSTANCE.getConnectedClients().sendMessage(message);
    //stageCoordinator.switchToProfileScene();
    
  }

  @FXML
  void onContactClicked(MouseEvent mouseEvent) {
    UserModel userModel1 = ModelsFactory.INSTANCE.getUserModel();
    userModel1.setEmail("mina@gmail.com");
    userModel1.setPhoneNumber("01285097233");
    userModel1.setUserName("MINA");
    // stageCoordinator.switchToContactScene();
  }

  @FXML
  void onLogoutClicked(MouseEvent mouseEvent) {
    stageCoordinator.switchToLoginScene();
  }

}
