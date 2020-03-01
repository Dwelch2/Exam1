package sample;

import com.mysql.jdbc.Connection;

import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class Controller implements Initializable {

   @FXML
   ListView listView;

    @FXML
    Button runButton;

    @FXML
    Button loadButton;

    @FXML
    TextField MIN;

    @FXML
    TextField MAX;

    @FXML
    private Label LabelD;






    final String hostname = "derek-db.ckexfznrvq9e.us-east-1.rds.amazonaws.com";
    final String dbName = "derek_db";
    final String port = "3306";
    final String username = "derek";
    final String password = "D.welch96";
    final String AWS_URL = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + username + "&password=" + password;




    private void runN(int min, int max,String url){
        double randomDouble = Math.random();
        randomDouble = randomDouble * max +  min;
        int randomInt = (int) randomDouble;
        LabelD.setText(String.valueOf(randomInt));

        try{
            Connection con = ( Connection ) DriverManager.getConnection(url);
            Statement stm = con.createStatement();
            try{
                stm.execute("CREATE TABLE NUMBERS (" +
                        "randomInt VARCHAR(36) )");
                System.out.println("TABLE DONE");
            }
            catch (Exception ex){
                System.out.println("TABLE EXISTS");
            }
            String code = String.valueOf(randomInt);
            String sql = "INSERT INTO NUMBERS VALUES" +
                    "('" + code + "')";
            System.out.println("L");
            stm.executeUpdate(sql);
            System.out.println("Table Populated");



            stm.close();
            con.close();
        }
        catch(Exception ex) {
            String msg = ex.getMessage();
            System.out.println(msg);
            System.out.println("Not Loaded");
        }

        MIN.clear();
        MAX.clear();

    }
    private void loadN(String URL) throws SQLException {
        Connection con = ( Connection ) DriverManager.getConnection(URL);
        Statement stm = con.createStatement();
        String sqlStatement = "SELECT randomInt FROM NUMBERS";
        ResultSet result = stm.executeQuery(sqlStatement);
        ObservableList<DBnum> DBlistView = FXCollections.observableArrayList();
        while (result.next()){

            DBnum Rdm = new DBnum();
            Rdm.code = result.getString("randomInt");
            DBlistView.add(Rdm);

        }
        listView.setItems(DBlistView);

        System.out.println("LOAD");
        stm.close();
        con.close();

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    loadN (AWS_URL);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

        runButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {runN(Integer.parseInt(MIN.getText()),Integer.parseInt(MAX.getText()),AWS_URL);

            }
        });

    }








}
