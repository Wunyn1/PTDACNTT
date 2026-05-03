package org.example.controller;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.example.service.impl.AuthServiceImpl;
import org.example.utils.ChangeFXML;
import org.example.utils.EmailUtils;
import org.example.utils.HashedPasswordUtils;
import org.example.utils.UsernameUtils;

public class RegisterController {
    @FXML
    private TextField fullnameRGS;
    @FXML
    private TextField usernameRGS;
    @FXML
    private TextField emailRGS;
    @FXML
    private PasswordField passwordRGS;
    @FXML
    private PasswordField checkPasswordRGS;

    @FXML private Label errorFullname;
    @FXML private Label errorUsername;
    @FXML private Label errorEmail;
    @FXML private Label errorPassword;
    @FXML private Label errorCheckPassword;

    @FXML
    public void registerRGS(ActionEvent event) {
        resetErrorEmpty();
        String fullname = fullnameRGS.getText();
        String username = usernameRGS.getText();
        String email = emailRGS.getText();
        String password = passwordRGS.getText();
        String checkPassword = checkPasswordRGS.getText();

        boolean checkEmpty = false;
        if(fullname.isEmpty()){
            errorFullname.setText("Họ tên không được để trống!");
            checkEmpty = true;
        }
        if(username.isEmpty()){
            errorUsername.setText("Tên đăng nhập không được để trống!");
            checkEmpty = true;
        }
        else if(!UsernameUtils.isValidUsername(username)) {
            errorUsername.setText("Tên đăng nhập không hợp lệ!");
            checkEmpty = true;
        }
        if(email.isEmpty()){
            errorEmail.setText("Email không được để trống!");
            checkEmpty = true;
        }
        else if(!EmailUtils.isValidEmail(email)) {
            errorEmail.setText("Email không đúng định dạng!");
            checkEmpty = true;
        }
        if(password.isEmpty()){
            errorPassword.setText("Mật khẩu không được để trống!");
            checkEmpty = true;
        }
        else if(!HashedPasswordUtils.isValidPassword(password)) {
            errorPassword.setText("Mật khẩu không hợp lệ!");
            checkEmpty = true;
        }
        if(checkPassword.isEmpty()){
            errorCheckPassword.setText("Mật khẩu không được để trống!");
            checkEmpty = true;
        }
        else {
            if(!(checkPassword.equals(password))){
                errorCheckPassword.setText("Mật khẩu không trùng khớp!");
                checkEmpty = true;
            }
        }
        if(checkEmpty){
            return;
        }
        else {
            AuthServiceImpl authService = new AuthServiceImpl();
            if(authService.register(fullname,username,email,password,checkPassword)){
                ChangeFXML.changeFXML(event, "/view/login.fxml");
            }
            else {
                errorUsername.setText("Tên đăng nhập đã tồn tại");
            }
        }
    }
    @FXML
    public void loginRGS(ActionEvent event){
        ChangeFXML.changeFXML(event, "/view/login.fxml");
    }

    private void resetErrorEmpty() {
        errorFullname.setText("");
        errorUsername.setText("");
        errorEmail.setText("");
        errorPassword.setText("");
        errorCheckPassword.setText("");
    }
}
