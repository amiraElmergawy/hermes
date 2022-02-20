package gov.iti.jets.client.presentation.controllers;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import common.business.dtos.UserAuthDto;
import gov.iti.jets.client.business.services.util.ServiceFactory;
import gov.iti.jets.client.presentation.models.UserModel;
import gov.iti.jets.client.presentation.util.ModelsFactory;
import gov.iti.jets.client.presentation.util.StageCoordinator;
import gov.iti.jets.client.presistance.network.RMIConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class RegisterationController implements Initializable {

    private final StageCoordinator stageCoordinator = StageCoordinator.INSTANCE;
    private final ModelsFactory modelsFactory = ModelsFactory.INSTANCE;

    @FXML
    private DatePicker birthDateFeild;

    @FXML
    private PasswordField confirmPasswordTextField;

    @FXML
    private TextField countryTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private ImageView eyeImage;

    @FXML
    private RadioButton femaleRadioButton;

    @FXML
    private RadioButton maleRadioButton;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private TextField userNameTextField;

    @FXML
    private ImageView wrongImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        UserModel userModel = modelsFactory.getUserModel();
        ToggleGroup toggleGendGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(toggleGendGroup);
        femaleRadioButton.setToggleGroup(toggleGendGroup);
    }

    @FXML
    void eyeImageMouseClicked(MouseEvent event) {

    }

    @FXML
    void loginAction(MouseEvent event) {
        stageCoordinator.switchToLoginScene();
    }

    @FXML
    void registerationAction(ActionEvent event) {
        Platform.runLater(() -> {

            ModelsFactory.INSTANCE.getUserModel().setPhoneNumber("01285097233");
            ModelsFactory.INSTANCE.getUserModel().setPassword("456");
          
            try {
                RMIConnection.INSTANCE.getServer().login(ServiceFactory.INSTANCE.getClientImpl(),
                        new UserAuthDto("01285097233", "456"));

            } catch (RemoteException e) {

                e.printStackTrace();
            }

            stageCoordinator.switchtoHomePageScene();
        });
        stageCoordinator.switchtoHomePageScene();
    }

    @FXML
    void registerationKeyPressed(KeyEvent event) {
        stageCoordinator.switchtoHomePageScene();
    }

}
