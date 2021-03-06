package gov.iti.jets.client.presentation.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import common.business.dtos.UserDto;
import net.synedra.validatorfx.Validator;
import net.synedra.validatorfx.Check.Context;
import gov.iti.jets.client.business.services.impl.MapperImpl;
import gov.iti.jets.client.business.services.util.ServiceFactory;
import gov.iti.jets.client.presentation.models.UserModel;
import gov.iti.jets.client.presentation.util.ModelsFactory;
import gov.iti.jets.client.presentation.util.StageCoordinator;
import gov.iti.jets.client.presentation.util.validation.Messages;
import gov.iti.jets.client.presentation.util.validation.Validators;
import gov.iti.jets.client.presistance.network.RMIConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class RegisterationController implements Initializable {

    private final StageCoordinator stageCoordinator = StageCoordinator.INSTANCE;
    private final ModelsFactory modelsFactory = ModelsFactory.INSTANCE;
    private static final String PASSWORD = "password";
    private static final String PASSWORD_CONFIRMATION = "password_confirmation";
    private static final String PHONE_NUMBER = "phone_number";

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

    @FXML
    private Button registerationButton;

    private Validator validator = new Validator();
    private BooleanProperty checkIsNull = new SimpleBooleanProperty(false);
    private ToggleGroup toggleGendGroup = new ToggleGroup();
    UserModel userModel = modelsFactory.getUserModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillCountryComboBox();

        maleRadioButton.setToggleGroup(toggleGendGroup);
        femaleRadioButton.setToggleGroup(toggleGendGroup);
        maleRadioButton.setSelected(true);

        birthDateFeild.valueProperty().setValue(LocalDate.now().minusYears(20));
        birthDateFeild.setEditable(false);

        getFormsValues();

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

        checkIsNull.bind(userNameTextField.textProperty().isNull());
        checkIsNull.bind(emailTextField.textProperty().isNull());

        validator.createCheck()
                .dependsOn(PASSWORD, passwordTextField.textProperty())
                .withMethod(this::validatePassword)
                .decorates(passwordTextField)
                .immediate();

        validator.createCheck()
                .dependsOn(PASSWORD, passwordTextField.textProperty())
                .dependsOn(PASSWORD_CONFIRMATION, confirmPasswordTextField.textProperty())
                .withMethod(this::validateConfirmationPassword)
                .decorates(confirmPasswordTextField)
                .immediate();

        validator.createCheck()
                .dependsOn(PHONE_NUMBER, phoneNumberTextField.textProperty())
                .withMethod(this::validatePhoneNumber)
                .decorates(phoneNumberTextField)
                .immediate();

        validator.containsErrorsProperty().addListener((listener) -> {
            if (validator.containsErrors()) {
                registerationButton.setDisable(true);
            } else {
                registerationButton.setDisable(false);
            }
        });
    }

    @FXML
    void eyeImageMouseClicked(MouseEvent event) {
        //
    }

    @FXML
    void loginAction(MouseEvent event) {
        stageCoordinator.switchToLoginScene();
    }

    @FXML
    void registerationAction(ActionEvent event) {
        // map uesrmodel to userDto
        // call register function
        String selectedGender = ((RadioButton) toggleGendGroup.getSelectedToggle()).getText();
        Boolean gender = selectedGender.equals("Male") ? true : false;
        userModel.setGender(gender);
        UserDto userDto = MapperImpl.INSTANCE.mapToUserDto(userModel);
        try {
            RMIConnection.INSTANCE.getServer().register(ServiceFactory.INSTANCE.getClientImpl(), userDto);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (userModel.getBio() != null) {
            stageCoordinator.switchtoHomePageScene();
        }
    }

    @FXML
    void registerationKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            stageCoordinator.switchtoHomePageScene();
        }
    }

    private void validateDateOfBirth(Context context) {
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

    private void validateEmail(Context context) {
        String email = context.get("email");
        if (email == null)
            return;
        if (email.isBlank()) {
            context.error(Messages.INSTANCE.EMAIL_EMPTY);
        } else if (!Validators.INSTANCE.isEmailValidFormat(email)) {
            context.error(Messages.INSTANCE.INVALID_EMAIL_FORMAT);
        }
    }

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

    private void validatePassword(Context context) {
        String passwordToCheck = context.get(PASSWORD);
        if (passwordToCheck == null || passwordToCheck.isBlank())
            context.error(Messages.INSTANCE.PASSWORD_EMPTY);
        else if (passwordToCheck.matches("[a-zA-Z]+"))
            context.error(Messages.INSTANCE.INVALID_PASSWORD_FORMAT);
        else if (passwordToCheck.matches("[0-9]+"))
            context.error(Messages.INSTANCE.INVALID_PASSWORD_FORMAT);
        else if (passwordToCheck.length() < 7)
            context.error(Messages.INSTANCE.PASSWORDS_MUST_MORETHAN_7);
    }

    private void validateConfirmationPassword(Context context) {
        String passwordToCheck = context.get(PASSWORD_CONFIRMATION);
        String originPassword = context.get(PASSWORD);
        if (passwordToCheck == null || originPassword == null)
            return;
        if (!passwordToCheck.equals(originPassword))
            context.error(Messages.INSTANCE.PASSWORDS_MUST_MATCH);
    }

    private void validatePhoneNumber(Context context) {
        String phoneToCheck = context.get(PHONE_NUMBER);
        if (phoneToCheck.isEmpty() || phoneToCheck.isBlank())
            context.error(Messages.INSTANCE.PHONE_MUSTNOT_EMPTY);
        else if (phoneToCheck.contains(" "))
            context.error(Messages.INSTANCE.PHONE_MUSTNOT_CONTAIN_SPACES);
        else if (phoneToCheck.length() < 11 || phoneToCheck.length() > 11)
            context.error(Messages.INSTANCE.PHONE_MUST_CONTAIN_11_NUMBER);
        else if (!phoneToCheck.matches("[0-9]+"))
            context.error(Messages.INSTANCE.PHONE_MUST_CONTAIN_NUMBERS_ONLY);
        else if (!phoneToCheck.startsWith("01"))
            context.error("Phone not correct");
    }

    private void getFormsValues() {
        userModel.countryProperty().bind(countryComboBox.getSelectionModel().selectedItemProperty());
        userModel.dateOfBirthProperty().bind(birthDateFeild.valueProperty());
        userModel.emailProperty().bindBidirectional(emailTextField.textProperty());
        userModel.passwordProperty().bindBidirectional(passwordTextField.textProperty());
        userModel.phoneNumberProperty().bindBidirectional(phoneNumberTextField.textProperty());
        userModel.userNameProperty().bindBidirectional(userNameTextField.textProperty());
    }

}
