package gov.iti.jets.client.presentation.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;
import gov.iti.jets.client.presentation.models.UserModel;
import gov.iti.jets.client.presentation.util.ModelsFactory;
import gov.iti.jets.client.presentation.util.StageCoordinator;
import gov.iti.jets.client.presentation.util.validation.Messages;
import gov.iti.jets.client.presentation.util.validation.Validators;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import net.synedra.validatorfx.Check.Context;

public class RegisterationController implements Initializable {

    private final StageCoordinator stageCoordinator = StageCoordinator.INSTANCE;
    private final ModelsFactory modelsFactory = ModelsFactory.INSTANCE;
    private static final String PASSWORD = "password";
    private static final String PASSWORD_CONFIRMATION = "password_confirmation";

    @FXML
    private BorderPane mainPane;

    @FXML
    private DatePicker birthDateFeild;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private PasswordField confirmPasswordTextField;
    @FXML
    private ComboBox<String> countryComboBox;

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

    // Validator validator = new Validator();

    private void fillCountryComboBox() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("countries.txt");
        try {
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(streamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                countryComboBox.getItems().add(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        countryComboBox.getSelectionModel().select("Egypt");
        countryComboBox.setPromptText("Country");
    }

    private void validatePassword(net.synedra.validatorfx.Check.Context context) {

        String passwordToCheck = context.get(PASSWORD);
        if (passwordToCheck == null || passwordToCheck.isBlank()) {
            context.error(Messages.PASSWORD_EMPTY);
        }
        // else if(!Validators.INSTANCE.isContainCharacters(confirmationPasswordToCheck)
        // )
        context.error(Messages.INVALID_PASSWORD_FORMAT);

    }

    private void validateConfirmationPassword(net.synedra.validatorfx.Check.Context context) {
        String confirmationPasswordToCheck = context.get(PASSWORD_CONFIRMATION);
        if (confirmationPasswordToCheck == null || confirmationPasswordToCheck.isBlank())
            context.error(Messages.PASSWORD_EMPTY);

        else if (!Validators.INSTANCE.isContainCharacters(confirmationPasswordToCheck))
            context.error(Messages.INVALID_PASSWORD_FORMAT);

        else if (!Validators.INSTANCE.isContainCharacters(confirmationPasswordToCheck))
            context.error(Messages.INVALID_PASSWORD_FORMAT);

    }

    @FXML
    private Button registerationButton;

    private Validator validator = new Validator();
    private BooleanProperty checkIsNull = new SimpleBooleanProperty(false);
    private ToggleGroup toggleGendGroup = new ToggleGroup();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserModel userModel = modelsFactory.getUserModel();
        ToggleGroup toggleGendGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(toggleGendGroup);
        femaleRadioButton.setToggleGroup(toggleGendGroup);
        fillCountryComboBox();

        validator.createCheck()
                .dependsOn(PASSWORD, passwordTextField.textProperty())
                .withMethod(this::validatePassword)
                .decorates(passwordTextField)
                .immediate();

        validator.createCheck()
                .dependsOn(PASSWORD_CONFIRMATION, confirmPasswordTextField.textProperty())
                .withMethod(this::validateConfirmationPassword)
                .decorates(confirmPasswordTextField)
                .immediate();

        maleRadioButton.setToggleGroup(toggleGendGroup);
        femaleRadioButton.setToggleGroup(toggleGendGroup);
        maleRadioButton.setSelected(true);
        System.out.println(toggleGendGroup.getSelectedToggle().getUserData());

        birthDateFeild.valueProperty().setValue(LocalDate.now().minusYears(20));
        birthDateFeild.setEditable(false);

        validator.createCheck()
                .dependsOn("username", userNameTextField.textProperty())
                .withMethod(this::validateUserName)
                .decorates(userNameTextField)
                .immediate();

        validator.createCheck()
                .dependsOn("email", emailTextField.textProperty())
                .withMethod(this::validateEmail)
                .decorates(emailTextField)
                .immediate();

        validator.createCheck()
                .dependsOn("dateOfBirth", birthDateFeild.valueProperty())
                .withMethod(this::validateDateOfBirth)
                .decorates(birthDateFeild)
                .immediate();

        // TooltipWrapper<Button> registerWrapper = new TooltipWrapper<>(
        // registerationButton,
        // checkIsNull.or(validator.containsErrorsProperty()),
        // Bindings.concat("Can't login:\n", validator.createStringBinding()));

        checkIsNull.bind(userNameTextField.textProperty().isNull());
        checkIsNull.bind(emailTextField.textProperty().isNull());

        // mainPane.getChildren().add(registerWrapper);
        checkIsNull.or(validator.containsErrorsProperty()).addListener((listener) -> {
            System.out.println(listener);
            System.out.println(checkIsNull.get());
        });

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
        System.out.println(((RadioButton) toggleGendGroup.getSelectedToggle()).getText());
        // stageCoordinator.switchtoHomePageScene();
    }

    @FXML
    void registerationKeyPressed(KeyEvent event) {
        stageCoordinator.switchtoHomePageScene();
    }

    private void validateDateOfBirth(Context context) {
        System.out.println("" + context.get("dateOfBirth"));
        if (Validators.INSTANCE.isDateOfBirthNotValid(birthDateFeild.valueProperty().get())) {
            context.error(Messages.INSTANCE.INVALID_DATE);
        }
    }

    private void validateUserName(Context context) {
        String userNameToCheck = context.get("username");
        if (userNameToCheck == null)
            return;
        if (userNameToCheck.isBlank()) {
            context.error(Messages.INSTANCE.USER_NAME_EMPTY);
        } else if (!Validators.INSTANCE.isUserNameValid(userNameToCheck))
            context.error(Messages.INSTANCE.INVALID_USER_NAME);
    }

    private void validateEmail(Context context) { /////// don't enter this method
        String email = context.get("email");
        System.out.println(email);
        if (email == null)
            return;
        if (email.isBlank()) {
            context.error(Messages.INSTANCE.EMAIL_EMPTY);
        } else if (!Validators.INSTANCE.isEmailValidFormat(email)) {
            context.error(Messages.INSTANCE.INVALID_EMAIL_FORMAT);
        }
    }

}
