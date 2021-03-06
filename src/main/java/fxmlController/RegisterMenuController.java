package fxmlController;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.App;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import static view.LoginMenu.PersonInfo.*;
import static view.LoginMenu.usernamePattern;
import static clientController.ServerConnection.*;


public class RegisterMenuController implements Initializable {

    @FXML private ImageView profile;
    private File profileFile;
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private TextField firstName;
    @FXML private TextField lastName;
    @FXML private TextField email;
    @FXML private TextField phone;
    @FXML private TextField company;
    @FXML private TextField darSurateVjud;
    @FXML private TextField minBalance;
    @FXML private TextField wage;

    @FXML private Button registerBtn;

    @FXML private ChoiceBox < String > type;

    @FXML private FontAwesomeIcon back;

    private void checkIfEmpty () throws Exception {
        if (username.getText ().isEmpty () ||
                password.getText ().isEmpty () ||
                firstName.getText ().isEmpty () ||
                lastName.getText ().isEmpty () ||
                email.getText ().isEmpty () ||
                phone.getText ().isEmpty () ||
                (type.getValue ().equals ( "Salesperson" ) && (company.getText ().isEmpty () || darSurateVjud.getText ().isEmpty ())))
            throw new Exception ( "Fields should be filled!" );
    }

    private void checkIfExist () throws Exception {
        String response = getPersonByUsername(username.getText());
        if (!response.equals("invalid username."))
            throw new Exception ( "This Dude Already Exists." );
    }

    private void checkValidity () throws Exception {
        if (username.getText ().equalsIgnoreCase ( "nigger" ))
            throw new Exception ( "Username Can't Be Nigger." );
        if ( !usernamePattern.matcher ( username.getText () ).matches ( ) )
            throw new Exception ( "Username Should Contain More Than 3 Characters And Not Contain Spaces." );
        String response = getIsFirstManagerRegistered();
        if ( type.getValue ().equals ( "Manager" ) && response.contains("true") )
            throw new Exception ( "You can't add a manager. Contact one of the existing managers." );
        if ( type.getValue ().equals ( "Manager" ) && !minBalance.getText().matches("^\\d*(\\.\\d+)?$"))
            throw new Exception("Min balance should be balance!");
        if (type.getValue ().equals ( "Manager" ) && !wage.getText().matches("^[1-9][0-9]?$|^100$"))
            throw new Exception("Enter number for wage!");
    }

    @FXML private void registerBtnAction () {
        try {
            checkIfEmpty ();
            checkIfExist();
            checkValidity ();
            HashMap < String, String > personInfo = new HashMap<>();
            personInfo.put ( USERNAME.label , username.getText () );
            personInfo.put ( PASSWORD.label , password.getText () );
            personInfo.put ( "type" , type.getValue () );
            personInfo.put ( FIRST_NAME.label , firstName.getText () );
            personInfo.put ( LAST_NAME.label , lastName.getText () );
            personInfo.put ( EMAIL.label , email.getText () );
            personInfo.put ( PHONE.label , phone.getText () );
            if (profileFile == null)
                personInfo.put ( PROFILE.label , null );
            else
                personInfo.put ( PROFILE.label , profileFile.toURI ().toString () );
            if (type.getValue ().equals ( "Salesperson" )) {
                personInfo.put ( COMPANY.label , company.getText ( ) );
                personInfo.put ( SAYERE_MOSHAKHASAT.label , darSurateVjud.getText ( ) );
            }
            if (type.getValue().equals("Manager")) {
                personInfo.put(MIN_BALANCE.label, minBalance.getText());
                personInfo.put(WAGE.label, wage.getText());
            }
            String response = sendRegisterRequest(personInfo);

            ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancel = new ButtonType("Ok", ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert alert = new Alert( Alert.AlertType.CONFIRMATION,
                    response,
                    ok,
                    cancel);
            alert.showAndWait ();
            back ();
        } catch (Exception e) {
            App.error ( e.getMessage () );
        }
    }

    @FXML void back () {
        App.setRoot ( "userLoginMenu" );
    }

    @FXML void closeApp () {
        System.exit ( 0 );
    }

    @Override
    public void initialize ( URL location , ResourceBundle resources ) {
        ObservableList<String> typeItems = FXCollections.observableArrayList ( "Customer","Salesperson","Manager" );
        type.setItems ( typeItems );
        type.setValue ( "Customer" );
        type.setOnAction ( event -> {
            if (type.getValue ().equals ( "Salesperson" )) {
                company.setVisible ( true );
                darSurateVjud.setVisible ( true );
                registerBtn.setLayoutY ( 906.0 );
            } else if (type.getValue().equals("Manager")) {
                minBalance.setVisible(true);
                wage.setVisible(true);
                registerBtn.setLayoutY ( 906.0 );
            }
            else {
                company.setVisible ( false );
                darSurateVjud.setVisible ( false );
                minBalance.setVisible(false);
                wage.setVisible(false);
                registerBtn.setLayoutY ( 808.0 );
            }

        } );


        Stage stage = new Stage();
        stage.setTitle("FileChooser");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("View Pictures");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG", "*.jpg"));
        profile.setOnMouseClicked ( event -> {
            if (event.getClickCount () == 2) {
                profileFile = fileChooser.showOpenDialog ( stage );

                if (profileFile != null) {
                    profile.setImage ( new Image (profileFile.toURI().toString()) );
                }
            }
        } );

    }

    @FXML private void backSizeBig ( MouseEvent mouseEvent ) {
        back.setStyle ( "-fx-font-family: FontAwesome; -fx-font-size: 20;-fx-effect: innershadow(gaussian, #17b5ff,75,0,5,0);" );
    }

    @FXML private void backSizeSmall ( MouseEvent mouseEvent ) {
        back.setStyle ( "-fx-font-family: FontAwesome; -fx-font-size: 1em" );
    }


}
