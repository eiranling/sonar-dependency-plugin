package seng202.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import seng202.Model.CurrentStorage;
import seng202.Model.DataFetcher;
import seng202.Model.User;

/**
 * Controller for the change password screen.
 */
public class ChangePasswordScreenController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button cancelButton;

    @FXML
    private Button changePasswordButton;

    @FXML
    private PasswordField newPasswordText;

    @FXML
    private PasswordField newPasswordTextAgain;

    @FXML
    private TextField oldPasswordText;

    @FXML
    private Label newPasswordLbl;

    @FXML
    private Label repeatPasswordLbl;

    @FXML
    private Label oldPasswordLbl;
    
    
    /** 
     * Method for when the cancel button is pressed, closes the window.
     * @param event - Auto-generated event when button is pressed
     */
    @FXML
    void cancelButtonPressed(ActionEvent event) throws IOException {  	
    	Stage stage = (Stage) cancelButton.getScene().getWindow();
    	stage.hide();
    }
    

    /**
     * Method for when the change password button is pressed.
     * Verifies the old and new password then runs the database query to update the users 
     * password.
     * @param event - Auto-generated event when button is pressed
     */
    @FXML
    void changePasswordButtonPressed(ActionEvent event) throws IOException, 
    		IllegalAccessException, ClassNotFoundException, InstantiationException {
        User user = CurrentStorage.getUser();
        DataFetcher data = new DataFetcher();
        data.connectDb();
        if (!user.getPassword().equals((oldPasswordText.getText()))) {
            oldPasswordLbl.setTextFill(Paint.valueOf("RED"));
        } 
        else if (!newPasswordText.getText().equals(newPasswordTextAgain.getText())) {
            oldPasswordLbl.setTextFill(Paint.valueOf("BLACK"));
            newPasswordLbl.setTextFill(Paint.valueOf("RED"));
            repeatPasswordLbl.setTextFill(Paint.valueOf("RED"));
        } 
        else {
            data.updateUserPassword(user.getUsername(), newPasswordText.getText().toString());
            Stage stage = (Stage) changePasswordButton.getScene().getWindow();
            stage.hide();
        }
        data.closeConnection();
    }

    
    /**
     * Initializes the controller.
     */
    @FXML
    void initialize() {
        assert cancelButton != null : "fx:id=\"cancelButton\" was not injected: check your FXML "
        		+ "file 'ChangePasswordScreen.fxml'.";
        assert changePasswordButton != null : "fx:id=\"changePasswordButton\" was not injected: "
        		+ "check your FXML file 'ChangePasswordScreen.fxml'.";
        assert newPasswordText != null : "fx:id=\"newPasswordText\" was not injected: check your "
        		+ "FXML file 'ChangePasswordScreen.fxml'.";
        assert newPasswordTextAgain != null : "fx:id=\"newPasswordTextAgain\" was not injected: "
        		+ "check your FXML file 'ChangePasswordScreen.fxml'.";
        assert oldPasswordText != null : "fx:id=\"oldPasswordText\" was not injected: check your "
        		+ "FXML file 'ChangePasswordScreen.fxml'.";
    }
}
