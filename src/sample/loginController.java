package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.effect.BlurType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.sql.*;

public class loginController {
    @FXML
    private AnchorPane pane_signup;
    @FXML
    private JFXButton signup_btn;
    //signup_btn.setDisable(true);

    @FXML
    private JFXButton signin_btn;

    @FXML
    private JFXTextField uname_up;

    @FXML
    private JFXTextField email_up;

    @FXML
    private JFXPasswordField pwd_up;


    @FXML
    private JFXTextField scrtkey;

    @FXML
    private JFXPasswordField pwdc_up;

    @FXML
    private AnchorPane pane_signin;

    @FXML
    private JFXPasswordField passin;

    @FXML
    private JFXTextField email_in;

    @FXML
    private JFXButton frgtBtn;

    @FXML
    private AnchorPane updatePasswordPane;

    @FXML
    private JFXTextField frgt_username;

    @FXML
    private JFXTextField frgt_secretkey;

    @FXML
    private JFXButton createNewPwdBtn;

    @FXML
    private JFXTextField newPwd;

    @FXML
    private JFXButton finishBtn;

    @FXML
    private JFXButton closeBtn;

    @FXML
    private JFXButton go_btn;
    public static Connection connection=null;

    public String useremail="";
    public String username="";
    public static FXMLLoader loader;//this is static because , usuig this 2 Controller class methods
    public static Controller controller;

    @FXML
    private void LoginGo(ActionEvent event) throws Exception{

        String em=email_in.getText();
        String pw=passin.getText();

        if(em.equals("")||pw.equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle(":(");
            alert.setHeaderText(null);
            if(em.equals(""))
                alert.setContentText("Please enter the e-mail.");
            else
                alert.setContentText("Please enter the password.");
            alert.showAndWait();
        }
        else {

            try {
                String query = "SELECT user_name FROM users WHERE email_id=? AND password=?";
                setConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, em);
                statement.setString(2, pw);

                ResultSet rs = statement.executeQuery();
                rs.next();
                if (!rs.getString(1).equals("")) {

                    System.out.println("Login successfull");

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);  //This works like JOptionpane.
                    alert.initStyle(StageStyle.UTILITY);
                    alert.setTitle("Yayy !");
                    alert.setHeaderText(null);
                    alert.setContentText("Login Successful" + "\n" + ":)");
                    alert.showAndWait();

                    loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("home.fxml"));
                    controller = loader.getController();
                    useremail = email_in.getText();
                    username = frgt_username.getText();
                    controller.UnlockFunctionalities(true);
                    controller.initData(useremail, username);

                    Stage stage = (Stage) passin.getScene().getWindow();
                    stage.close();

                } else {
                    System.out.println("erong cedentials");
                    JOptionPane.showMessageDialog(null, "Couldn't login. check the credentials");
                }

            } catch (Exception e) {
                //JOptionPane.showMessageDialog(null,e);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle(":(");
                alert.setHeaderText(null);
                alert.setContentText("Wrong Credentials !" + "\n" + "Please try again.");
                alert.showAndWait();
            }
        }

    }


    public void SignUp() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        boolean ebool=true;
        String username=uname_up.getText();
        String email=email_up.getText();
        String password=pwd_up.getText();
        String secretkey=scrtkey.getText();
        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
        alert1.initStyle(StageStyle.UTILITY);
        alert1.setTitle("Uh-oh!");
        alert1.setHeaderText(null);

        ebool=emailCheck(email);




       /* Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Yayy !");
        alert.setHeaderText(null);
        alert.setContentText("You're signed up."+"\n"+"\n"+"\n"+"use the secret kry provided in case you forgot the password");
        alert.showAndWait(); */

        if(!username.equals("")&&!email.equals("")&&!password.equals("")&&!secretkey.equals("")&&ebool) {


            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("home.fxml"));
            Controller controller = loader.getController();
            controller.initDataSignUp(username, email, password, secretkey);
            controller.UnlockFunctionalities(true);
            Stage stage = (Stage) passin.getScene().getWindow();
            stage.close();
        }else{
            if(ebool==false){
                alert1.setContentText("Please enter a valid e-mail id.");
                alert1.showAndWait();
            }
            if(username.equals("")){
                alert1.setContentText("Please enter the username");
                alert1.showAndWait();
            }
            if(email.equals("")){
                alert1.setContentText("Please enter the email");
                alert1.showAndWait();
            }
            if(password.equals("")){
                alert1.setContentText("Please enter the password");
                alert1.showAndWait();
            }
            if(secretkey.equals("")){
                alert1.setContentText("Please enter the secret-key");
                alert1.showAndWait();
            }
        }

    }
    public boolean emailCheck(String e){
       if(!e.contains("@")){
           return false;
       }else{
           if(!e.substring((e.length()-4),e.length()).equals(".com")){//||!e.substring((e.length()-3),e.length()).equals(".in")){
               return false;
           }else{
               String s[]=e.split("@");
               if(s[0]==""){
                   return false;
               }

           }
       }

       return true;
    }




    public static void setConnection() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String url = "jdbc:mysql://localhost/musicplayer";
        String uname = "root";
        //String pwd = "12Ccbu12!";
        String pwd = "phani@123";
        //TODO:
        try {
            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection(url, uname, pwd);
            System.out.println("Connection succesful");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }



    @FXML
    void shiftPaneSignUp(ActionEvent event) {

        pane_signin.setVisible(true);
        //signup_btn.setVisible(false);
        pane_signup.setVisible(false);
        signin_btn.setDisable(true);
        signup_btn.setDisable(false);
    }

    @FXML
    void shiftPaneSingIn(ActionEvent event) {

        pane_signup.setVisible(true);
        pane_signin.setVisible(false);
        signup_btn.setDisable(true);
        signin_btn.setDisable(false);
    }


    public void forgotPswd(){
        //email_in.setText("");
        if(email_in.getText().equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("");
            alert.setHeaderText(null);
            alert.setContentText("Please enter your e-mail");
            alert.showAndWait();
        }else {
            passin.setText("");
            pane_signin.setVisible(false);
            pane_signup.setVisible(false);
            signin_btn.setVisible(false);
            signup_btn.setVisible(false);
            updatePasswordPane.setVisible(true);
        }
    }


    public void createNewPswd() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {

        String userName=frgt_username.getText();
        String secretKey=frgt_secretkey.getText();

        String query="SELECT user_name FROM users WHERE user_name=? AND secret_key=?";

        setConnection();
        PreparedStatement statement=connection.prepareStatement(query);
        statement.setString(1,userName);
        statement.setString(2,secretKey);
        ResultSet rs1=statement.executeQuery();
        if(rs1.next()){
            frgt_secretkey.setVisible(false);
            //frgt_username.setVisible(false);
            createNewPwdBtn.setVisible(false);

            newPwd.setVisible(true);
            finishBtn.setVisible(true);

        }else{
            //JOptionPane.showMessageDialog(null,"Wrong username/secret key");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("");
            alert.setHeaderText(null);
            alert.setContentText("Please enter correct username/secret-key");
            alert.showAndWait();
        }

    }

    public void finishCreatingNewPwd() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {


        String userName=frgt_username.getText();
        String NewPwd=newPwd.getText();

        String query="UPDATE users u SET u.password=? WHERE u.user_name=?";
        //setConnection();
        PreparedStatement statement=connection.prepareStatement(query);
        statement.setString(1,NewPwd);
        statement.setString(2,userName);
        int affectedRows=statement.executeUpdate();
        System.out.println("password change Affected rows are "+affectedRows);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Done");
        alert.setHeaderText(null);
        alert.setContentText("Password changed successfully"+"\n"+"\n"+"Now sign-in using new password");
        alert.showAndWait();

        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("home.fxml"));
        Controller controller=loader.getController();
        //useremail=email_in.getText();
        username=frgt_username.getText();
        controller.initData(useremail,username);
        Stage stage=(Stage)passin.getScene().getWindow();
        stage.close();



    }

    public void quitMethod(){
        Stage stage=(Stage)passin.getScene().getWindow();
        stage.close();

    }



}
