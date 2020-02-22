package mydes;

import java.io.IOException;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;

import java.awt.Desktop;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


import mydes.DESCore;

public class DEScrypto extends Application
{

    // private TextField tfData = new TextField();
    private PasswordField tfKey = new PasswordField();
    private TextArea tfData = new TextArea();
    private TextArea tfEncryptData = new TextArea();
    private TextArea tfDecryptData = new TextArea();
    private Button btEncrypt = new Button("encrypt");
    private Button btDecrypt = new Button("decrypt");
    private Button btEncryptFile = new Button("EncryptFile");
    private Button btDecryptFile = new Button("DecryptFile");
    private Stage stage = new Stage();

    public void start(Stage primaryStage)
    {

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        HBox firstRow = new HBox(10);
        firstRow.setPadding(new Insets(0,0,0,50));
        firstRow.getChildren().addAll(new Label("Data:"),tfData);

        HBox secondRow = new HBox(20);
        secondRow.setPadding(new Insets(0,0,0,50));
        secondRow.getChildren().add(new Label("Key:"));
        secondRow.getChildren().add(tfKey);
        secondRow.getChildren().add(btEncrypt);
        secondRow.getChildren().add(btDecrypt);
        secondRow.getChildren().add(btEncryptFile);
        secondRow.getChildren().add(btDecryptFile);

        HBox thirdRow = new HBox(10);
        thirdRow.getChildren().addAll(new Label("EncryptData:"),tfEncryptData);

        HBox fourthRow = new HBox(10);
        fourthRow.getChildren().addAll(new Label("DecryptData:"),tfDecryptData);

        gridPane.add(firstRow,0,0);
        gridPane.add(secondRow,0,1);
        gridPane.add(thirdRow,0,2);
        gridPane.add(fourthRow,0,3);
        gridPane.setAlignment(Pos.CENTER);
        tfEncryptData.setEditable(false);
        tfDecryptData.setEditable(false);

        btEncrypt.setOnAction(e -> encryptData_Button());
        btDecrypt.setOnAction(e -> decryptData_Button());
        btEncryptFile.setOnAction(e -> encryptFile());
        btDecryptFile.setOnAction(e -> decryptFile());

        Scene scene = new Scene(gridPane,800,900);
        primaryStage.setTitle("MyDES");
        // primaryStage.getIcons().add(new Image("encryption.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void encryptData_Button()
    {
        String data = tfData.getText();
        String key = tfKey.getText();


        try
        {

            if (key.length() < 8)
            {
                DESCore util = new DESCore();
                tfEncryptData.setText(util.encryptString(data));

            }
            else
            {
                DESCore util = new DESCore(key);
                tfEncryptData.setText(util.encryptString(data));
            }
        }catch (Exception e1)
        {
            tfEncryptData.setText("error");
        }


    }

    public void decryptData_Button()
    {

        String data = tfEncryptData.getText();
        String key = tfKey.getText();

        try
        {
            if (key.length() < 8)
            {
                DESCore util = new DESCore();
                tfDecryptData.setText(util.decryptString(data));

            }
            else
            {
                DESCore util = new DESCore(key);
                tfDecryptData.setText(util.decryptString(data));
            }
        }catch (Exception e1)
        {
            tfDecryptData.setText("error");
        }
    }

    private void encryptFile()
    {
        FileChooser fileChooser = new FileChooser();
        //文档类型过滤器
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("all files (*.*)", "*.*"),
                new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg"),
                new FileChooser.ExtensionFilter("mp3 files (*.mp3)", "*.mp3"),
                new FileChooser.ExtensionFilter("mp4 files (*.mp4)", "*.mp4")
            );

        //获取选择的文件
        File file = fileChooser.showOpenDialog(stage);
        String soursePath = file.getAbsolutePath();
        String[] string = soursePath.split("\\.");
        tfDecryptData.setText(soursePath);
        //对文件内容进行加密
        String key = tfKey.getText();
        //加密文件路径
        String resultPath = string[0] + "encrypt." + string[1];


        try
        {
            if (key.length() < 8)
            {
                DESCore util = new DESCore();
                util.encryptFile(soursePath,resultPath);
                tfEncryptData.setText("File encryption success !");
            }
            else
            {
                DESCore util = new DESCore(key);
                util.encryptFile(soursePath,resultPath);
                tfEncryptData.setText("File encryption success !");

            }
        }catch (Exception e1)
        {
            tfEncryptData.setText("error");
        }

    }//encryptFile

    private void decryptFile()
    {
        FileChooser fileChooser = new FileChooser();
        //文档类型过滤器
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("all files (*.*)", "*.*"),
                new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg"),
                new FileChooser.ExtensionFilter("mp3 files (*.mp3)", "*.mp3"),
                new FileChooser.ExtensionFilter("mp4 files (*.mp4)", "*.mp4")
            );
        //获取选择的文件
        File file = fileChooser.showOpenDialog(stage);
        String soursePath = file.getAbsolutePath();
        String[] string = soursePath.split("\\.");
        tfEncryptData.setText(soursePath);
        //对文件内容进行加密
        String key = tfKey.getText();
        //加密文件路径
        String resultPath = string[0] + "decrypt." + string[1];


        try
        {
            if (key.length() < 8)
            {
                DESCore util = new DESCore();
                util.decryptFile(soursePath,resultPath);
                tfDecryptData.setText("File encryption success !");

            }
            else
            {
                DESCore util = new DESCore(key);
                util.decryptFile(soursePath,resultPath);
                tfDecryptData.setText("File encryption success !");

            }
        }catch (Exception e1)
        {
            tfDecryptData.setText("error");
        }
    }

}
